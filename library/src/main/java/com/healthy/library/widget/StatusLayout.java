package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.healthy.library.R;
import com.healthy.library.interfaces.OnCustomRetryListener;
import com.healthy.library.interfaces.OnNetRetryListener;
import com.healthy.library.utils.TransformUtil;




/**
 * @author Li
 * @date 2019/04/03 10:41
 * @des 页面状态管理
 */
public class StatusLayout extends FrameLayout {

    private LayoutParams mCenterParams;

    public Status getStatus() {
        return status;
    }

    private Status status = StatusLayout.Status.STATUS_CONTENT;

    private View mEmptyLayout;
    private View mNetErrLayout;
    private View mCustomLayout;//暂时先这样吧
    private final View mProgressLayout;
    private View mDataErrLayout;
    private OnNetRetryListener mOnNetRetryListener;

    public void setmOnCustomNetRetryListener(OnCustomRetryListener mOnCustomNetRetryListener) {
        this.mOnCustomNetRetryListener = mOnCustomNetRetryListener;
    }

    private OnCustomRetryListener mOnCustomNetRetryListener;
    private static final int STATUS_LOADING = 1;
    private static final int STATUS_CONTENT = 0;
    private int mInitStatus;
    public static final float DEFAULT_BIAS = 0.4F;
    private int mEmptyImgSrc;
    private int mNetErrImgSrc;
    private int mDataErrImgSrc;

    public void setmEmptyContent(String mEmptyContent) {
        this.mEmptyContent = mEmptyContent;
    }

    public void setEmptyBottomBtn(boolean visibility, String content) {
        this.mEmptyBottomVisibility = visibility;
        this.mEmptyBottomContent = content;
    }

    public void setEmptyBottomClickListener(View.OnClickListener onClickListener){
        this.mEmptyBottomClickListener = onClickListener;
    }

    private View.OnClickListener mEmptyBottomClickListener;
    private String mEmptyContent;
    private boolean mEmptyBottomVisibility;
    private String mEmptyBottomContent;
    private float mEmptyBias;
    private float mErrBias;
    private float mLoadingBias;
    private float mDataErrBias;
    private int mTxtColor;
    private int mRetryTxtColor;
    private int mRetryTxtBg;

    public StatusLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StatusLayout);
        mInitStatus = array.getInteger(R.styleable.StatusLayout_status_init, STATUS_LOADING);
        mEmptyBias = array.getFloat(R.styleable.StatusLayout_empty_bias, DEFAULT_BIAS);
        mErrBias = array.getFloat(R.styleable.StatusLayout_err_bias, DEFAULT_BIAS);
        mLoadingBias = array.getFloat(R.styleable.StatusLayout_loading_bias, DEFAULT_BIAS);
        mDataErrBias = array.getFloat(R.styleable.StatusLayout_data_err_bias, DEFAULT_BIAS);

        mEmptyImgSrc = array.getResourceId(R.styleable.StatusLayout_empty_drawable,
                R.drawable.status_empty_normal);
        mNetErrImgSrc = array.getResourceId(R.styleable.StatusLayout_net_err_drawable,
                R.drawable.status_net_err);
        mDataErrImgSrc = array.getResourceId(R.styleable.StatusLayout_data_err_drawable,
                R.drawable.status_data_err);

        mEmptyContent = array.getString(R.styleable.StatusLayout_empty_txt);
        mTxtColor = array.getColor(R.styleable.StatusLayout_txt_color,
                Color.parseColor("#9596A4"));
        mRetryTxtColor = array.getColor(R.styleable.StatusLayout_retry_txt_color,
                Color.parseColor("#FF6266"));
        mRetryTxtBg = array.getResourceId(R.styleable.StatusLayout_retry_txt_bg,
                R.drawable.selector_retry);
        array.recycle();

        mCenterParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        mCenterParams.gravity = Gravity.CENTER;
        mProgressLayout = progressLayout(context);
        addView(mProgressLayout, mCenterParams);
    }

    public StatusLayout(@NonNull Context context) {
        this(context, null);
    }

    public StatusLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mProgressLayout != null && mProgressLayout.getParent() != null) {
            bringChildToFront(mProgressLayout);
        }


        switch (mInitStatus) {
            case STATUS_LOADING:
                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    if (view == mProgressLayout) {
                        //System.out.println("进来转");
                        view.setVisibility(VISIBLE);
                    } else if (view == mEmptyLayout || view == mNetErrLayout) {
                        view.setVisibility(GONE);
                    } else {
                        view.setVisibility(GONE);
                    }
                }
                break;
            case STATUS_CONTENT:
                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    if (view == mProgressLayout || view == mEmptyLayout || view == mNetErrLayout) {
                        view.setVisibility(GONE);
                    } else {
                        view.setVisibility(VISIBLE);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 更改页面状态
     *
     * @param status 需要切换的状态
     */
    public void updateStatus(Status status) {
        this.status = status;
        if (getContext() == null) {
            return;
        }
        switch (status) {
            case STATUS_EMPTY:
                if (mEmptyLayout == null) {
                    mEmptyLayout = emptyLayout(getContext());
                    addView(mEmptyLayout, mCenterParams);
                }

                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    if (view == mEmptyLayout) {
                        view.setVisibility(VISIBLE);
                    } else {
                        view.setVisibility(GONE);
                    }
                }

                break;
            case STATUS_LOADING:
                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    if (view == mProgressLayout) {
                        view.setVisibility(VISIBLE);
                    } else if (view == mEmptyLayout || view == mNetErrLayout || view == mDataErrLayout || view == mCustomLayout) {
                        view.setVisibility(GONE);
                    } else if (view.getVisibility() == VISIBLE) {
                        view.setVisibility(VISIBLE);
                    } else {
                        view.setVisibility(GONE);
                    }
                }


                break;
            case STATUS_NET_ERR:
                if (mNetErrLayout == null) {
                    mNetErrLayout = netErrLayout(getContext());
                    addView(mNetErrLayout, mCenterParams);
                }

                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    if (view == mNetErrLayout) {
                        view.setVisibility(VISIBLE);
                    } else {
                        view.setVisibility(GONE);
                    }
                }

                break;
            case STATUS_CUSTOM:
                if (mCustomLayout == null) {
                    mCustomLayout = customErrLayout(getContext());
                    addView(mCustomLayout, mCenterParams);
                }

                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    if (view == mCustomLayout) {
                        view.setVisibility(VISIBLE);
                    } else {
                        view.setVisibility(GONE);
                    }
                }

                break;
            case STATUS_DATA_ERR:
                if (mDataErrLayout == null) {
                    mDataErrLayout = dataErrLayout(getContext());
                    addView(mDataErrLayout, mCenterParams);
                }

                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    if (view == mDataErrLayout) {
                        view.setVisibility(VISIBLE);
                    } else {
                        view.setVisibility(GONE);
                    }
                }
                break;
            case STATUS_CONTENT:
                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    if (view == mProgressLayout || view == mEmptyLayout || view == mNetErrLayout ||
                            view == mDataErrLayout || view == mCustomLayout) {
                        view.setVisibility(GONE);
                    } else {
                        view.setVisibility(VISIBLE);
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * 获取数据失败布局
     */
    private View dataErrLayout(Context context) {
        int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int parentId = ConstraintLayout.LayoutParams.PARENT_ID;
        int margin = (int) TransformUtil.dp2px(context, 15);
        ConstraintLayout layout = new ConstraintLayout(context);

//        NestedScrollView nestedScrollView=new NestedScrollView(context);
//        nestedScrollView.addView(layout);
        ImageView ivErr = new ImageView(context);
        ivErr.setId(R.id.dataErrImg);
        ivErr.setImageResource(mDataErrImgSrc);

        TextView tvErr = new TextView(context);
        tvErr.setId(R.id.dataErrTxt);
        tvErr.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tvErr.setTextColor(mTxtColor);
        tvErr.setGravity(Gravity.CENTER);
        tvErr.setText("哎呦(ಥ﹏ಥ)获取数据失败了");

        TextView tvRetry = new TextView(context);
        tvRetry.setText("重新加载");
        tvRetry.setId(R.id.dataErrRetryTxt);
        tvRetry.setTextColor(mRetryTxtColor);
        tvRetry.setPadding((int) dp2px(context, 25), (int) dp2px(context, 10),
                (int) dp2px(context, 25), (int) dp2px(context, 10));
        tvRetry.setBackgroundResource(mRetryTxtBg);
        tvRetry.setClickable(true);
        tvRetry.setFocusable(true);
        tvRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnNetRetryListener != null) {
                    mOnNetRetryListener.onRetryClick();
                }
            }
        });

        ConstraintLayout.LayoutParams ivErrParams =
                new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
        ivErrParams.startToStart = parentId;
        ivErrParams.endToEnd = parentId;
        ivErrParams.topToTop = parentId;
        ivErrParams.bottomToTop = tvErr.getId();
        ivErrParams.verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED;
        ivErrParams.verticalBias = mDataErrBias;
        ivErrParams.bottomMargin = margin;
        layout.addView(ivErr, ivErrParams);

        ConstraintLayout.LayoutParams tvErrParams =
                new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
        tvErrParams.startToStart = parentId;
        tvErrParams.endToEnd = parentId;
        tvErrParams.topToBottom = ivErr.getId();
        tvErrParams.bottomToTop = tvRetry.getId();
        layout.addView(tvErr, tvErrParams);

        ConstraintLayout.LayoutParams tvRetryParams =
                new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
        tvRetryParams.startToStart = parentId;
        tvRetryParams.endToEnd = parentId;
        tvRetryParams.topToBottom = tvErr.getId();
        tvRetryParams.bottomToBottom = parentId;
        tvRetryParams.topMargin = (int) TransformUtil.dp2px(context, 35);
        layout.addView(tvRetry, tvRetryParams);

        return layout;
    }


    /**
     * 自定义失败的布局
     */
    private View customErrLayout(Context context) {
        int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int parentId = ConstraintLayout.LayoutParams.PARENT_ID;
        int margin = (int) TransformUtil.dp2px(context, 15);
        ConstraintLayout layout = new ConstraintLayout(context);

//        NestedScrollView nestedScrollView=new NestedScrollView(context);
//        nestedScrollView.addView(layout);
        ImageView ivErr = new ImageView(context);
        ivErr.setId(R.id.netErrImg);
        ivErr.setImageResource(mNetErrImgSrc);

        TextView tvErr = new TextView(context);
        tvErr.setId(R.id.netErrTxt);
        tvErr.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tvErr.setTextColor(mTxtColor);
        tvErr.setGravity(Gravity.CENTER);
        tvErr.setText("呜呜(╥﹏╥)定位失败了");

        TextView tvRetry = new TextView(context);
        tvRetry.setText("重新定位");
        tvRetry.setId(R.id.netErrRetryTxt);
        tvRetry.setTextColor(mRetryTxtColor);
        tvRetry.setPadding((int) dp2px(context, 25), (int) dp2px(context, 10),
                (int) dp2px(context, 25), (int) dp2px(context, 10));
        tvRetry.setBackgroundResource(mRetryTxtBg);
        tvRetry.setClickable(true);
        tvRetry.setFocusable(true);
        tvRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCustomNetRetryListener != null) {
                    mOnCustomNetRetryListener.onRetryClick();
                }
            }
        });

        ConstraintLayout.LayoutParams ivErrParams =
                new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
        ivErrParams.startToStart = parentId;
        ivErrParams.endToEnd = parentId;
        ivErrParams.topToTop = parentId;
        ivErrParams.bottomToTop = tvErr.getId();
        ivErrParams.verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED;
        ivErrParams.verticalBias = mErrBias;
        ivErrParams.bottomMargin = margin;
        layout.addView(ivErr, ivErrParams);

        ConstraintLayout.LayoutParams tvErrParams =
                new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
        tvErrParams.startToStart = parentId;
        tvErrParams.endToEnd = parentId;
        tvErrParams.topToBottom = ivErr.getId();
        tvErrParams.bottomToTop = tvRetry.getId();
        layout.addView(tvErr, tvErrParams);

        ConstraintLayout.LayoutParams tvRetryParams =
                new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
        tvRetryParams.startToStart = parentId;
        tvRetryParams.endToEnd = parentId;
        tvRetryParams.topToBottom = tvErr.getId();
        tvRetryParams.bottomToBottom = parentId;
        tvRetryParams.topMargin = (int) TransformUtil.dp2px(context, 35);
        layout.addView(tvRetry, tvRetryParams);

        return layout;
    }

    /**
     * 网络失败的布局
     */
    private View netErrLayout(Context context) {
        int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int parentId = ConstraintLayout.LayoutParams.PARENT_ID;
        int margin = (int) TransformUtil.dp2px(context, 15);
        ConstraintLayout layout = new ConstraintLayout(context);
//        NestedScrollView nestedScrollView=new NestedScrollView(context);
//        nestedScrollView.addView(layout);

        ImageView ivErr = new ImageView(context);
        ivErr.setId(R.id.netErrImg);
        ivErr.setImageResource(mNetErrImgSrc);

        TextView tvErr = new TextView(context);
        tvErr.setId(R.id.netErrTxt);
        tvErr.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tvErr.setTextColor(mTxtColor);
        tvErr.setGravity(Gravity.CENTER);
        tvErr.setText("呜呜(╥﹏╥)网络走丢了");

        TextView tvRetry = new TextView(context);
        tvRetry.setText("重新加载");
        tvRetry.setId(R.id.netErrRetryTxt);
        tvRetry.setTextColor(mRetryTxtColor);
        tvRetry.setPadding((int) dp2px(context, 25), (int) dp2px(context, 10),
                (int) dp2px(context, 25), (int) dp2px(context, 10));
        tvRetry.setBackgroundResource(mRetryTxtBg);
        tvRetry.setClickable(true);
        tvRetry.setFocusable(true);
        tvRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnNetRetryListener != null) {
                    mOnNetRetryListener.onRetryClick();
                }
            }
        });

        ConstraintLayout.LayoutParams ivErrParams =
                new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
        ivErrParams.startToStart = parentId;
        ivErrParams.endToEnd = parentId;
        ivErrParams.topToTop = parentId;
        ivErrParams.bottomToTop = tvErr.getId();
        ivErrParams.verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED;
        ivErrParams.verticalBias = mErrBias;
        ivErrParams.bottomMargin = margin;
        layout.addView(ivErr, ivErrParams);

        ConstraintLayout.LayoutParams tvErrParams =
                new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
        tvErrParams.startToStart = parentId;
        tvErrParams.endToEnd = parentId;
        tvErrParams.topToBottom = ivErr.getId();
        tvErrParams.bottomToTop = tvRetry.getId();
        layout.addView(tvErr, tvErrParams);

        ConstraintLayout.LayoutParams tvRetryParams =
                new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
        tvRetryParams.startToStart = parentId;
        tvRetryParams.endToEnd = parentId;
        tvRetryParams.topToBottom = tvErr.getId();
        tvRetryParams.bottomToBottom = parentId;
        tvRetryParams.topMargin = (int) TransformUtil.dp2px(context, 35);
        layout.addView(tvRetry, tvRetryParams);

        return layout;
    }

    /**
     * 列表为空的布局
     */
    private View emptyLayout(Context context) {
        int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int parentId = ConstraintLayout.LayoutParams.PARENT_ID;
        int margin = (int) TransformUtil.dp2px(context, 15);
        ConstraintLayout layout = new ConstraintLayout(context);
        layout.setPadding(0,500,0,500);
        NestedScrollView nestedScrollView=new NestedScrollView(context);
        nestedScrollView.addView(layout);
        ImageView ivEmpty = new ImageView(context);
        ivEmpty.setImageResource(mEmptyImgSrc);
        ivEmpty.setId(R.id.emptyImg);

        TextView tvEmpty = new TextView(context);
        tvEmpty.setId(R.id.emptyTxt);
        tvEmpty.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tvEmpty.setTextColor(mTxtColor);
        String content = TextUtils.isEmpty(mEmptyContent) ? "暂无数据" : mEmptyContent;
        tvEmpty.setText(content);

        ConstraintLayout.LayoutParams ivEmptyParams =
                new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
        ivEmptyParams.startToStart = parentId;
        ivEmptyParams.endToEnd = parentId;
        ivEmptyParams.topToTop = parentId;
        ivEmptyParams.bottomToTop = tvEmpty.getId();
        ivEmptyParams.bottomMargin = margin;
        ivEmptyParams.verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED;
        ivEmptyParams.verticalBias = mEmptyBias;
        layout.addView(ivEmpty, ivEmptyParams);

        ConstraintLayout.LayoutParams tvEmptyParams =
                new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
        tvEmptyParams.startToStart = parentId;
        tvEmptyParams.endToEnd = parentId;
        tvEmptyParams.topToBottom = ivEmpty.getId();
        tvEmptyParams.bottomToBottom = parentId;

        layout.addView(tvEmpty, tvEmptyParams);

        //控制是否显示空布局下方按钮
        if (mEmptyBottomVisibility) {
            TextView tvEmptyBottom = new TextView(context);
            tvEmptyBottom.setId(R.id.emptyBottomTxt);
            tvEmptyBottom.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tvEmptyBottom.setTextColor(tvEmptyBottom.getContext().getResources().getColor(R.color.colorWhite));
            tvEmptyBottom.setGravity(Gravity.CENTER);
            String mbody = TextUtils.isEmpty(mEmptyContent) ? "去预约" : mEmptyBottomContent;
            tvEmptyBottom.setText(mbody);
            tvEmptyBottom.setBackground(tvEmptyBottom.getContext().getResources().getDrawable(R.drawable.shape_select_servive_btn));

            ConstraintLayout.LayoutParams bottomEmptyParams =
                    new ConstraintLayout.LayoutParams((int) TransformUtil.dp2px(context, 200),
                            (int) TransformUtil.dp2px(context, 40));
            bottomEmptyParams.startToStart = parentId;
            bottomEmptyParams.endToEnd = parentId;
            bottomEmptyParams.topToBottom = tvEmpty.getId();
            bottomEmptyParams.topMargin = margin;
            layout.addView(tvEmptyBottom, bottomEmptyParams);
            //设置底部按钮点击事件
            tvEmptyBottom.setOnClickListener(mEmptyBottomClickListener);
        }

        return nestedScrollView;
    }

    private View progressLayout(Context context) {
        int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int parentId = ConstraintLayout.LayoutParams.PARENT_ID;
        ConstraintLayout layout = new ConstraintLayout(context);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setId(R.id.loading);

        ConstraintLayout.LayoutParams loadingParams =
                new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
        loadingParams.topToTop = parentId;
        loadingParams.bottomToBottom = parentId;
        loadingParams.startToStart = parentId;
        loadingParams.endToEnd = parentId;
        loadingParams.verticalBias = mLoadingBias;
        layout.addView(progressBar, loadingParams);
        layout.setClickable(true);
        return layout;
    }

    private float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public void setOnNetRetryListener(OnNetRetryListener onNetRetryListener) {
        mOnNetRetryListener = onNetRetryListener;
    }


    public enum Status {
        /**
         * 数据项为空（适用于列表页）
         */
        STATUS_EMPTY,
        /**
         * 网络错误
         */
        STATUS_NET_ERR,

        /**
         * 数据错误
         */
        STATUS_DATA_ERR,
        /**
         * 加载
         */
        STATUS_LOADING,
        /**
         * 出现不明状况时 例如关了定位
         */
        STATUS_CUSTOM,

        /**
         * 展示内容
         */
        STATUS_CONTENT
    }
}
