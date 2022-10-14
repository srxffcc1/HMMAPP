package com.health.faq.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.faq.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Events;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.message.AskEndMessage;
import com.healthy.library.message.RefreshRewardListMsg;
import com.healthy.library.routes.FaqRoutes;
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
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/06/28 15:49
 * @des 悬赏求助界面
 */
@Route(path = FaqRoutes.FAQ_REWARD)
public class RewardActivity extends BaseActivity implements TextWatcher,
        View.OnClickListener {

    private LinearLayout mLayoutPictures;
    private static final int RC_PICTURE = 100;
    private static final int RC_PAY_WAY = 101;

    private static final int RC_PERMISSIONS = 13;
    private String[] mPermissions = new String[]{
            Manifest.permission.CAMERA,
    };
    private TextView mTvPriceHint;
    private EditText mEtPrice;
    private TextView mEtQuestionTitle;
    private EditText mEtQuestionContent;
    private TextView mTvRelease;
    private CheckBox mCbAnonymous;
    private TopBar topBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reward;
    }

    @Override
    protected void findViews() {
        mLayoutPictures = findViewById(R.id.layout_pictures);
        mTvPriceHint = findViewById(R.id.tv_price_hint);
        mEtPrice = findViewById(R.id.et_price);
        mEtQuestionTitle = findViewById(R.id.et_question_title);
        mEtQuestionContent = findViewById(R.id.et_question_content);
        mTvRelease = findViewById(R.id.tv_release);
        mCbAnonymous = findViewById(R.id.cb_anonymous);
        topBar=findViewById(R.id.top_bar);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        addAddedImageView();
        mTvRelease.setOnClickListener(this);
        mEtPrice.addTextChangedListener(this);
        mEtPrice.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        String normal = dest.toString();
                        if (".".contentEquals(source) && TextUtils.isEmpty(dest)) {
                            return "0.";
                        } else if (normal.contains(".") && normal.substring(
                                normal.indexOf(".") + 1).length() == 2) {
                            return "";
                        }
                        return null;
                    }
                }
        });
        mEtQuestionTitle.setFilters(new InputFilter[]{
                new InputFilterUtils.NoEmojiFilter(),
                new InputFilter.LengthFilter(
                        ResUtils.getIntById(mContext, R.integer.question_title_max_length))
        });
        mEtQuestionContent.setFilters(new InputFilter[]{
                new InputFilterUtils.NoEmojiFilter(),
                new InputFilter.LengthFilter(
                        ResUtils.getIntById(mContext, R.integer.question_content_max_length))
        });
        mEtQuestionTitle.addTextChangedListener(this);
        mEtQuestionContent.addTextChangedListener(this);
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                WebDialog.newInstance().setUrl(SpUtils.getValue(mContext,UrlKeys.H5_expertNoticeUrl)).setIsinhome(true).show(getSupportFragmentManager(),UrlKeys.H5_expertNoticeUrl);
            }
        });
        if(!SpUtils.getValue(mContext,"WebDialog_"+UrlKeys.URL_ZXFWYSZC,false)){
            WebDialog.newInstance().setUrl(UrlKeys.URL_ZXFWYSZC).setIsinhome(true).show(getSupportFragmentManager(),UrlKeys.URL_ZXFWYSZC);
        }
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
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mLayoutPictures.addView(imageView, params);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, Events.EVENT_UPLOAD_PIC);
                if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
                    int count = mLayoutPictures.getChildCount();
                    Matisse.from(RewardActivity.this)
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
            Matisse.from(RewardActivity.this)
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
                if((videoPath = Matisse.obtainCaptureVideoResult(data))!=null){//
//                        videoUrl = videoFile.getAbsolutePath();
//                        clip(videoUrl,mPos);
                }else if((capturePath = Matisse.obtainCaptureImageResult(data))!=null){
                    List<String> filePaths =new ArrayList<>();
                    filePaths.add(capturePath);
                    addSelectedImgs(filePaths);
                }else if((cropPath = Matisse.obtainCropResult(data))!=null){
                }else {
                    List<String> filePaths = Matisse.obtainSelectPathResult(data);
                    if (filePaths != null) {
                        addSelectedImgs(filePaths);
                    }
                }
            } else if (requestCode == RC_PAY_WAY) {
                EventBus.getDefault().post(new RefreshRewardListMsg());
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
        mTvPriceHint.setVisibility(mEtPrice.getText().length() == 0 ? View.VISIBLE : View.GONE);
        changeBtnEnabled();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    /**
     * 设置提交按钮是否可以点击
     */
    private void changeBtnEnabled() {
        int titleLength = mEtQuestionTitle.length();
        int contentLength = mEtQuestionContent.length();
        int titleMinLength = ResUtils.getIntById(mContext, R.integer.question_title_min_length);
        int contentMinLength = ResUtils.getIntById(mContext, R.integer.question_content_min_length);

        if (titleLength >= titleMinLength && contentLength >= contentMinLength &&
                !TextUtils.isEmpty(mEtPrice.getText())) {
            mTvRelease.setEnabled(true);
        } else {
            mTvRelease.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {
        v.setEnabled(false);
        MobclickAgent.onEvent(mContext, Events.EVENT_RELEASE_PAY_BTN);
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
        ARouter.getInstance()
                .build(FaqRoutes.FAQ_PAY_TRANSLUCENT)
                .withInt("payType", Constants.PAY_REWARD)
                .withString("title", mEtQuestionTitle.getText().toString())
                .withString("content", mEtQuestionContent.getText().toString())
                .withString("price", mEtPrice.getText().toString())
                .withCharSequenceArray("filePaths", filePaths)
                .withBoolean("isAnonymous", mCbAnonymous.isChecked())
                .navigation(this, RC_PAY_WAY);
        v.setEnabled(true);
    }

    @Override
    public void showNetErr() {
        showContent();
    }

    @Override
    public void showDataErr() {
        showContent();
    }

    @Override
    public void onBackPressed() {
        MobclickAgent.onEvent(mContext, Events.EVENT_REWARD_BACK);
        super.onBackPressed();

    }
}
