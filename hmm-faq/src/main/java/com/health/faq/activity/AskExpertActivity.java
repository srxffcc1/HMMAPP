package com.health.faq.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.faq.R;
import com.health.faq.contract.AskExpertContract;
import com.health.faq.contract.PayTranslucentContract;
import com.health.faq.model.ExpertInfoModel;
import com.health.faq.presenter.AskExpertPresenter;
import com.health.faq.presenter.PayTranslucentPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.message.AskEndMessage;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.InputFilterUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.ResUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.TopBar;
import com.healthy.library.widget.WebDialog;
import com.lxj.matisse.Matisse;
import com.lxj.matisse.MimeType;
import com.lxj.matisse.engine.impl.GlideEngine;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/07/17 09:21
 * @des 问专家
 */
@Route(path = FaqRoutes.FAQ_ASK_EXPERT)
public class AskExpertActivity extends BaseActivity implements TextWatcher,
        AskExpertContract.View, IsNeedShare, PayTranslucentContract.View {

    @Autowired
    String id;

    String title;
    String content;
    String price;
    boolean isAnonymous;

    private ImageView mIvAvatar;
    private TextView mTvRealName;
    private TextView mTvRank;
    private LinearLayout mLayoutPictures;
    private TextView mTvAsk;
    private EditText mEtContent;
    private CheckBox mCbAnonymous;
    private static final int RC_PERMISSIONS = 13;
    private String[] mPermissions = new String[]{
            Manifest.permission.CAMERA,
    };
    private static final int RC_PICTURE = 100;
    private static final int RC_PAY_WAY = 101;
    private AskExpertPresenter mPresenter;
    private TopBar topBar;
    private ImageView moreUrl;
    private PayTranslucentPresenter payTranslucentPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ask_expert;
    }

    @Override
    protected void findViews() {
        mIvAvatar = findViewById(R.id.iv_avatar);
        mTvRealName = findViewById(R.id.tv_real_name);
        mTvRank = findViewById(R.id.tv_good_at);
        mLayoutPictures = findViewById(R.id.layout_pictures);
        mTvAsk = findViewById(R.id.tv_ask);
        mEtContent = findViewById(R.id.et_content);
        mCbAnonymous = findViewById(R.id.cb_anonymous);
        topBar = findViewById(R.id.top_bar);
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        addAddedImageView();
        mPresenter = new AskExpertPresenter(this, this);
        payTranslucentPresenter = new PayTranslucentPresenter(this, this);
        mEtContent.setFilters(new InputFilter[]{
                new InputFilterUtils.NoEmojiFilter(),
                new InputFilter.LengthFilter(
                        ResUtils.getIntById(mContext, R.integer.question_content_max_length))
        });
        findViewById(R.id.view_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(FaqRoutes.FAQ_EXPERT_HOMEPAGE)
                        .withString("id", id)
                        .navigation();
            }
        });
        mEtContent.addTextChangedListener(this);
        getData();
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
//                showShare();
                WebDialog.newInstance().setUrl(SpUtils.getValue(mContext, UrlKeys.H5_rewardNoticeUrl)).setIsinhome(true).show(getSupportFragmentManager(), UrlKeys.H5_rewardNoticeUrl);
            }
        });
//        if(SpUtils.isFirst(mContext,"WebDialog_"+UrlKeys.URL_ZXFWYSZC)){
//            NormalDialog.newInstance().setLayoutRes(R.layout.dialog_faq_misc).show(getSupportFragmentManager(),UrlKeys.URL_ZXFWYSZC);
//        }
//        moreUrl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NormalDialog.newInstance().setLayoutRes(R.layout.dialog_faq_misc).show(getSupportFragmentManager(),UrlKeys.URL_ZXFWYSZC);
//            }
//        });

    }

    @Override
    public void getData() {
        mPresenter.getExpertInfo(id);
    }

    /**
     * 添加选择图片的图片按钮
     */
    private void addAddedImageView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                (int) TransformUtil.dp2px(mContext, 75f));
        params.weight = 1;
        params.setMargins(0, (int) TransformUtil.dp2px(mContext, 6f),
                (int) TransformUtil.dp2px(mContext, 6f)
                , 0);
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.ic_reward_add_pic);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mLayoutPictures.addView(imageView, params);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
                    int count = mLayoutPictures.getChildCount();
                    Matisse.from(AskExpertActivity.this)
                            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG,
                                    MimeType.BMP, MimeType.WEBP))
                            .capture(true)
                            .countable(false)
                            .imageEngine(new GlideEngine())
                            .theme(R.style.ImgPicker)
                            .maxSelectable(4 - count)
                            .forResult(RC_PICTURE);
                } else {
                    PermissionUtils.requestPermissions(mActivity, RC_PERMISSIONS, mPermissions);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            int count = mLayoutPictures.getChildCount();
            Matisse.from(AskExpertActivity.this)
                    .choose(MimeType.of(MimeType.JPEG, MimeType.PNG,
                            MimeType.BMP, MimeType.WEBP))
                    .capture(true)
                    .countable(false)
                    .imageEngine(new GlideEngine())
                    .theme(R.style.ImgPicker)
                    .maxSelectable(4 - count)
                    .forResult(RC_PICTURE);
        } else {
            PermissionUtils.showRationale(mContext);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_PICTURE) {
                String capturePath = null;
                String videoPath = null;
                String cropPath = null;
                if ((videoPath = Matisse.obtainCaptureVideoResult(data)) != null) {//
//                        videoUrl = videoFile.getAbsolutePath();
//                        clip(videoUrl,mPos);
                } else if ((capturePath = Matisse.obtainCaptureImageResult(data)) != null) {
                    List<String> filePaths = new ArrayList<>();
                    filePaths.add(capturePath);
                    addSelectedImgs(filePaths);
                } else if ((cropPath = Matisse.obtainCropResult(data)) != null) {
                } else {
                    List<String> filePaths = Matisse.obtainSelectPathResult(data);
                    if (filePaths != null) {
                        addSelectedImgs(filePaths);
                    }
                }
            } else if (requestCode == RC_PAY_WAY) {
                EventBus.getDefault().post(new AskEndMessage());
                finish();
            }
        }

    }

    /**
     * 添加选择的图片
     *
     * @param filePaths 选择的图片地址
     */
    private void addSelectedImgs(List<String> filePaths) {
        for (String filePath : filePaths) {
            final FrameLayout frameLayout = new FrameLayout(mContext);
            frameLayout.setTag(filePath);
            CornerImageView imageView = new CornerImageView(mContext);
            imageView.setCornerRadius(TransformUtil.dp2px(mContext, 4));
            com.healthy.library.businessutil.GlideCopy.with(mContext).load(filePath).into(imageView);

            FrameLayout.LayoutParams contentParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) TransformUtil.dp2px(mContext, 75f)
            );
            contentParams.setMargins(0, (int) TransformUtil.dp2px(mContext, 6f),
                    (int) TransformUtil.dp2px(mContext, 6)
                    , 0);
            frameLayout.addView(imageView, contentParams);

            ImageView delImg = new ImageView(mContext);
            delImg.setImageResource(R.drawable.faq_ic_del_picture);

            FrameLayout.LayoutParams delImgParams = new FrameLayout.LayoutParams(
                    (int) TransformUtil.dp2px(mContext, 15),
                    (int) TransformUtil.dp2px(mContext, 15)
            );
            delImgParams.gravity = Gravity.TOP | Gravity.END;
            frameLayout.addView(delImg, delImgParams);

            delImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLayoutPictures.removeView(frameLayout);
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    (int) TransformUtil.dp2px(mContext, 81f));
            params.weight = 1;
            mLayoutPictures.addView(frameLayout, filePaths.indexOf(filePath), params);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTvAsk.setEnabled(s.length() >= ResUtils.getIntById(mContext,
                R.integer.question_content_min_length));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onGetExpertInfoSuccess(final ExpertInfoModel model) {
        com.healthy.library.businessutil.GlideCopy.with(mContext).load(model.faceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(mIvAvatar);
        mTvRealName.setText(model.name);
        mTvRank.setText(model.rank);
        mTvAsk.setText(String.format("立即提问 (¥ %s)", FormatUtils.formatRewardMoney(model.cost)));
        mTvAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] filePaths;
                List<String> pathList = new ArrayList<>();
                for (int i = 0; i < mLayoutPictures.getChildCount(); i++) {
                    View view = mLayoutPictures.getChildAt(i);
                    if (view instanceof FrameLayout) {
                        pathList.add(String.valueOf(view.getTag()));
                    }
                }
                if (pathList.size() == 0) {
                    filePaths = new String[0];
                } else {
                    filePaths = pathList.toArray(new String[0]);
                }
                if (Integer.parseInt(model.cost) > 0) {
                    ARouter.getInstance()
                            .build(FaqRoutes.FAQ_PAY_TRANSLUCENT)
                            .withString("id", id)
                            .withInt("payType", Constants.PAY_ASK_EXPERT)
                            .withString("title", mEtContent.getText().toString())
                            .withString("content", mEtContent.getText().toString())
                            .withCharSequenceArray("filePaths", filePaths)
                            .withString("price", model.cost)
                            .withBoolean("isAnonymous", mCbAnonymous.isChecked())
                            .navigation(AskExpertActivity.this, RC_PAY_WAY);
                } else {
                    price= model.cost;
                    title=mEtContent.getText().toString();
                    content=mEtContent.getText().toString();
                    isAnonymous=mCbAnonymous.isChecked();
                    if (filePaths.length == 0) {
                        submitReward(new String[0]);
                    } else {
                        String[] base64 = new String[filePaths.length];
                        for (int i = 0; i < filePaths.length; i++) {
                            base64[i] = BitmapUtils.bitmap2Base64(filePaths[i]);
                        }
                        payTranslucentPresenter.uploadPictures(base64);
                    }
                }
            }
        });
    }

    /**
     * 提交问答
     *
     * @param fileUrl 问答图片
     */
    private void submitReward(String[] fileUrl) {
//        payTranslucentPresenter.submitReward(price, title,
//                content, isAnonymous ? "2" : "1",
//                fileUrl, "2");
        payTranslucentPresenter.submitExpert(id, price, content, isAnonymous ? "2" : "1", fileUrl, "2");
    }

    private void initView() {
        moreUrl = (ImageView) findViewById(R.id.more_url);
    }

    @Override
    public String getSurl() {
        return "http://www.baidu.com";
    }

    @Override
    public String getSdes() {
        return "测试描述";
    }

    @Override
    public String getStitle() {
        return "憨妈妈专家";
    }

    @Override
    public Bitmap getsBitmap() {
        Bitmap bm = ((BitmapDrawable) ((ImageView) mIvAvatar).getDrawable()).getBitmap();
        return bm;
    }

    @Override
    public void onCheckBalanceSuccess(String balance, boolean canPay, boolean needSetPwd) {

    }

    @Override
    public void onCheckBalanceFail() {

    }

    @Override
    public void onUploadPictureSuccess(String[] urls) {
        submitReward(urls);
    }

    @Override
    public void onGetOrderInfoSuccess(String payType, String aliInfo, Map<String, String> wxInfo) {
        switch (payType) {
//            case Constants.REWARD_PAY_IN_WX:
//                payByWx(wxInfo);
//                break;
//            case Constants.REWARD_PAY_IN_ALI:
//                payByAli(aliInfo);
//                break;
            case Constants.REWARD_PAY_IN_HDD:
                setResult(RESULT_OK);
                showToastIgnoreLife("支付成功");
                finish();
                break;
            default:
                break;
        }
    }
}
