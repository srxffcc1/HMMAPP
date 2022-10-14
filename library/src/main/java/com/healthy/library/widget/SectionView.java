package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthy.library.LibApplication;
import com.healthy.library.R;

/**
 * @author Li
 * @date 2019/03/26 14:14
 * @des 内容控件 类似于 iv tv--------------tv-iv
 * <attr name="section_start_src" format="reference"/>
 * <attr name="section_start_size" format="dimension"/>
 * <attr name="section_start_width" format="dimension"/>
 * <attr name="section_start_height" format="dimension"/>
 * <attr name="section_start_margin_start" format="dimension"/>
 * <p>
 * <p>
 * <attr name="section_end_src" format="reference"/>
 * <attr name="section_end_size" format="reference"/>
 * <attr name="section_end_width" format="reference"/>
 * <attr name="section_end_height" format="reference"/>
 * <attr name="section_end_margin_end" format="reference"/>
 * <p>
 * <attr name="section_title" format="string"/>
 * <attr name="section_title_size" format="dimension"/>
 * <attr name="section_title_color" format="color"/>
 * <attr name="section_title_margin_start" format="dimension"/>
 * <p>
 * <attr name="section_des" format="string"/>
 * <attr name="section_des_size" format="dimension"/>
 * <attr name="section_des_color" format="color"/>
 * <attr name="section_des_margin_end" format="dimension"/>
 * <attr name="section_des_margin_start" format="dimension"/>
 * <p>
 * <attr name="section_show_divider" format="boolean"/>
 * <attr name="section_divider_full_width" format="boolean"/>
 * <attr name="section_divider_color" format="color"/>
 * <attr name="section_divider_height" format="dimension"/>
 */
public class SectionView extends ConstraintLayout {


    private static final float DEFAULT_MARGIN_START = 15;
    private static final float DEFAULT_MARGIN_END = 15;
    private static final float DEFAULT_TITLE_SIZE_IN_DP = 14;
    private static final String DEFAULT_TITLE_COLOR = "#222222";
    private static final float DEFAULT_DES_SIZE_IN_DP = 14;
    private static final String DEFAULT_DES_COLOR = "#222222";
    private static final float DEFAULT_DIVIDER_HEIGHT_IN_DP = 1;
    private static final String DEFAULT_DIVIDER_COLOR = "#E4E8EE";
    private ImageView mIvStart;
    private ImageView mIvEnd;



    private TextView mTvTitle;
    private TextView mTvDes;
    private View mDividerView;
    private int mStartSrc;
    private float mStartSize;
    private float mStartWidth;
    private float mStartHeight;
    private float mStartMarginStart;
    private int mEndSrc;
    private float mEndSize;
    private float mEndWidth;
    private float mEndHeight;
    private float mEndMarginEnd;
    private String mTitle;
    private String mTitleHint;
    private float mTitleSize;
    private int mTitleColor;
    private float mTitleMarginStart;
    private String mDes;
    private String mDesHint;
    private float mDesSize;
    private int mDesColor;
    private float mDesMarginStart;
    private float mDesMarginEnd;
    private boolean mShowDivider;
    private boolean mDividerFullWidth;
    private int mDividerColor;
    private float mDividerHeight;

    public SectionView(Context context) {
        this(context, null);
    }

    public SectionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
        mIvStart = new ImageView(context);
        mIvStart.setId(R.id.ivStart);


        mIvEnd = new ImageView(context);
        mIvEnd.setId(R.id.ivEnd);

        mTvTitle = new TextView(context);
        mTvTitle.setId(R.id.tvTitle);

        mTvDes = new TextView(context);
        mTvDes.setId(R.id.tvDes);

        mDividerView = new View(context);
        mDividerView.setId(R.id.divider);

        addIvStart();
        addIvEnd();

        addTvDes();

        addTvTitle();
        addDivider();

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SectionView);
        mStartSrc = array.getResourceId(R.styleable.SectionView_section_start_src, 0);
        mStartSize = array.getDimensionPixelSize(R.styleable.SectionView_section_start_size, 0);
        mStartHeight = array.getDimensionPixelSize(R.styleable.SectionView_section_start_height, 0);
        mStartWidth = array.getDimensionPixelSize(R.styleable.SectionView_section_start_width, 0);
        mStartMarginStart = array.getDimensionPixelSize(R.styleable.SectionView_section_start_margin_start,
                (int) dp2px(context, DEFAULT_MARGIN_START));

        mEndSrc = array.getResourceId(R.styleable.SectionView_section_end_src, 0);
        mEndSize = array.getDimensionPixelSize(R.styleable.SectionView_section_end_size, 0);
        mEndHeight = array.getDimensionPixelSize(R.styleable.SectionView_section_end_height, 0);
        mEndWidth = array.getDimensionPixelSize(R.styleable.SectionView_section_end_width, 0);
        mEndMarginEnd = array.getDimensionPixelSize(R.styleable.SectionView_section_end_margin_end,
                (int) dp2px(context, DEFAULT_MARGIN_END));

        mTitle = array.getString(R.styleable.SectionView_section_title);
        mTitleSize = array.getDimensionPixelSize(R.styleable.SectionView_section_title_size,
                (int) dp2px(context, DEFAULT_TITLE_SIZE_IN_DP));
        mTitleColor = array.getColor(R.styleable.SectionView_section_title_color,
                Color.parseColor(DEFAULT_TITLE_COLOR));
        mTitleHint = array.getString(R.styleable.SectionView_section_title_hint);
        mTitleMarginStart = array
                .getDimensionPixelSize(R.styleable.SectionView_section_title_margin_start, 0);


        mDes = array.getString(R.styleable.SectionView_section_des);
        mDesSize = array.getDimensionPixelSize(R.styleable.SectionView_section_des_size,
                (int) dp2px(context, DEFAULT_DES_SIZE_IN_DP));
        mDesColor = array.getColor(R.styleable.SectionView_section_des_color,
                Color.parseColor(DEFAULT_DES_COLOR));
        mDesMarginStart = array
                .getDimensionPixelSize(R.styleable.SectionView_section_des_margin_start, 0);
        mDesHint = array.getString(R.styleable.SectionView_section_des_hint);
        mDesMarginEnd = array
                .getDimensionPixelSize(R.styleable.SectionView_section_des_margin_end, 0);


        mShowDivider = array.getBoolean(R.styleable.SectionView_section_show_divider, true);
        mDividerFullWidth = array.getBoolean(R.styleable.SectionView_section_divider_full_width, true);
        mDividerHeight = array.getDimensionPixelSize(R.styleable.SectionView_section_divider_height,
                (int) dp2px(context, DEFAULT_DIVIDER_HEIGHT_IN_DP));
        mDividerColor = array.getColor(R.styleable.SectionView_section_divider_color,
                Color.parseColor(DEFAULT_DIVIDER_COLOR));
        array.recycle();
    }

    private void addIvStart() {
        ConstraintLayout.LayoutParams ivStartParams;
        if (mStartSize != 0) {
            ivStartParams = new Constraints.LayoutParams((int) mStartSize, (int) mStartSize);
        } else if (mStartWidth != 0 && mStartHeight != 0) {
            ivStartParams = new Constraints.LayoutParams((int) mStartWidth, (int) mStartHeight);
        } else {
            ivStartParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        ivStartParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        ivStartParams.bottomToBottom = LayoutParams.PARENT_ID;
        ivStartParams.startToStart = LayoutParams.PARENT_ID;
        ivStartParams.leftMargin = (int) mStartMarginStart;
        if (mStartSrc != 0) {
            mIvStart.setImageResource(mStartSrc);
        }
        addView(mIvStart, ivStartParams);
    }

    private void addIvEnd() {
        ConstraintLayout.LayoutParams ivEndParams;
        if (mEndSize != 0) {
            ivEndParams = new Constraints.LayoutParams((int) mEndSize, (int) mEndSize);
        } else if (mEndWidth != 0 && mEndHeight != 0) {
            ivEndParams = new Constraints.LayoutParams((int) mEndWidth, (int) mEndHeight);
        } else {
            ivEndParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        ivEndParams.topToTop = LayoutParams.PARENT_ID;
        ivEndParams.bottomToBottom = LayoutParams.PARENT_ID;
        ivEndParams.endToEnd = LayoutParams.PARENT_ID;
        ivEndParams.rightMargin = (int) mEndMarginEnd;
        if (mEndSrc != 0) {
            mIvEnd.setImageResource(mEndSrc);
        }
        addView(mIvEnd, ivEndParams);

    }

    private void addTvTitle() {
        ConstraintLayout.LayoutParams tvTitleParams = new Constraints.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        tvTitleParams.startToEnd = mIvStart.getId();
        tvTitleParams.endToStart = mTvDes.getId();
        tvTitleParams.topToTop = LayoutParams.PARENT_ID;
        tvTitleParams.bottomToBottom = LayoutParams.PARENT_ID;
        mTvTitle.setText(mTitle);
        mTvTitle.setHint(mTitleHint);
        mTvTitle.setSingleLine(true);
        mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
        mTvTitle.setTextColor(mTitleColor);
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
        tvTitleParams.leftMargin = (int) mTitleMarginStart;
        addView(mTvTitle, tvTitleParams);
    }

    private void addTvDes() {
        ConstraintLayout.LayoutParams tvDesParams = new Constraints.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        tvDesParams.topToTop = LayoutParams.PARENT_ID;
        tvDesParams.bottomToBottom = LayoutParams.PARENT_ID;
        tvDesParams.endToStart = mIvEnd.getId();
        tvDesParams.startToEnd = mTvTitle.getId();
        mTvDes.setText(mDes);
        mTvDes.setHint(mDesHint);
        mTvDes.setTextColor(mDesColor);
        mTvDes.setSingleLine(true);
        mTvDes.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDesSize);
        tvDesParams.leftMargin = (int) mDesMarginStart;
        tvDesParams.rightMargin = (int) mDesMarginEnd;
        mTvDes.setGravity(Gravity.END);
        addView(mTvDes, tvDesParams);
    }

    private void addDivider() {
        ConstraintLayout.LayoutParams dividerParams = new Constraints.LayoutParams(
                0, (int) mDividerHeight
        );
        dividerParams.bottomToBottom = LayoutParams.PARENT_ID;
        mDividerView.setBackgroundColor(mDividerColor);
        if (!mDividerFullWidth) {
            dividerParams.startToStart = mIvStart.getId();
            dividerParams.endToEnd = mIvEnd.getId();
        } else {
            dividerParams.startToStart = LayoutParams.PARENT_ID;
            dividerParams.endToEnd = LayoutParams.PARENT_ID;
        }
        addView(mDividerView, dividerParams);
        if (mShowDivider) {
            mDividerView.setVisibility(VISIBLE);
        } else {
            mDividerView.setVisibility(GONE);
        }
    }


    public void setTitle(CharSequence charSequence) {
        mTvTitle.setText(charSequence);
    }

    public void setDes(CharSequence charSequence) {
        mTvDes.setText(charSequence);
    }

    public void setDesColor(int color) {
        mTvDes.setTextColor(LibApplication.getAppContext().getResources().getColor(color));
    }

    public void setEndSrc(int src) {
        mIvEnd.setImageResource(src);
    }

    public TextView getTvDes() {
        return mTvDes;
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    private float dp2px(Context context, float dp) {
        return Math.max(1, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics()));
    }

}
