package com.health.faq.activity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.faq.R;
import com.health.faq.contract.ReplyContract;
import com.health.faq.presenter.ReplyPresenter;
import com.health.faq.widget.PlayerManager;
import com.health.faq.widget.VoiceLayout;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.dialog.SimpleDialog;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.utils.InputFilterUtils;
import com.healthy.library.utils.InputMethodUtils;
import com.healthy.library.utils.ResUtils;

import java.io.File;

/**
 * @author Li
 * @date 2019/07/08 09:38
 * @des 回复界面
 */

@Route(path = FaqRoutes.FAQ_REPLY)
public class ReplyActivity extends BaseActivity implements View.OnClickListener, ReplyContract.View {

    @Autowired
    String title;
    @Autowired
    String questionId;
    @Autowired
    String nickname;
    @Autowired
    String faceUrl;

    TextView mTvQuestionTitle;
    TextView mTvNickname;
    ImageView mIvAvatar;
    ImageView mIvReplyType;
    TextView mTvRouteRecord;
    VoiceLayout mVoiceLayout;
    EditText mEtReply;
    CheckBox mCbAnonymous;
    private static final int RC_VOICE = 384;
    private ReplyPresenter mPresenter;
    private String mDuration;
    private PlayerManager mPlayerManager;
    private TextView mTvRelease;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reply;
    }


    private VoiceLayout.OnSelfClickListener mOnSelfClickListener = new VoiceLayout.OnSelfClickListener() {
        @Override
        public void onDelClick() {
            new SimpleDialog.Builder(mContext)
                    .setContent("确定要删除语音重新录？")
                    .setContentGravity(Gravity.CENTER)
                    .setNegativeBtn("取消", null)
                    .setPositiveBtn("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mVoiceLayout.setTag(null);
                            changeReplyType(Constants.REPLY_IN_VOICE);
                            mPlayerManager.pause();
                        }
                    }).create().show();
        }
    };

    @Override
    protected void findViews() {
        mTvQuestionTitle = findViewById(R.id.tv_question_title);
        mTvNickname = findViewById(R.id.tv_nickname);
        mIvAvatar = findViewById(R.id.iv_avatar);
        mIvReplyType = findViewById(R.id.iv_reply_type);
        mTvRouteRecord = findViewById(R.id.tv_record);
        mVoiceLayout = findViewById(R.id.voice);
        mVoiceLayout.getIvDel().setVisibility(View.VISIBLE);
        mVoiceLayout.setOnClickListener(this);
        mEtReply = findViewById(R.id.et_reply);
        mCbAnonymous = findViewById(R.id.cb_anonymous);
        mTvRelease = findViewById(R.id.tv_release);
        mIvReplyType.setOnClickListener(this);
        mTvRouteRecord.setOnClickListener(this);
        mTvRelease.setOnClickListener(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mPresenter = new ReplyPresenter(this, this);

        mPlayerManager = PlayerManager.newInstance();
        getLifecycle().addObserver(mPlayerManager);
        mPlayerManager.attachVoiceLayout(mVoiceLayout);
        mVoiceLayout.setOnSelfClickListener(mOnSelfClickListener);

        mTvQuestionTitle.setText(title);
        mTvNickname.setText(nickname);
        mEtReply.setFilters(new InputFilter[]{
                new InputFilterUtils.NoEmojiFilter(),
                new InputFilter.LengthFilter(ResUtils.getIntById(mContext, R.integer.reply_max_length))
        });
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(faceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)
                
                .into(mIvAvatar);
        changeReplyType(Constants.REPLY_IN_TEXT);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_reply_type) {
            v.setSelected(!v.isSelected());
            if (v.isSelected()) {
                changeReplyType(Constants.REPLY_IN_VOICE);
            } else {
                changeReplyType(Constants.REPLY_IN_TEXT);
            }
        } else if (v.getId() == R.id.tv_record) {
            ARouter.getInstance()
                    .build(FaqRoutes.FAQ_SOUND_RECORDING)
                    .navigation(this, RC_VOICE);
        } else if (v.getId() == R.id.tv_release) {
            if (mIvReplyType.isSelected()) {
                if (mVoiceLayout.getTag() == null) {
                    showToast("请先录音");
                } else {
                    mPresenter.uploadFile(new File(String.valueOf(mVoiceLayout.getTag())));
                    mTvRelease.setClickable(false);
                }
            } else {
                String content = mEtReply.getText().toString();
                if (content.length() < ResUtils.getIntById(mContext, R.integer.reply_min_length)) {
                    showToast("请输入至少5个字的回答");
                } else {
                    mPresenter.releaseReply(questionId, mCbAnonymous.isChecked() ?
                                    String.valueOf(Constants.REPLY_IN_ANONYMOUS) :
                                    String.valueOf(Constants.REPLY_IN_REAL_NAME),
                            String.valueOf(Constants.REPLY_IN_TEXT), content, null,
                            null);
                    mTvRelease.setClickable(false);
                }

            }
        } else if (v.getId() == R.id.voice) {
            if (mVoiceLayout.getIvStatus().getVisibility() == View.VISIBLE) {
                if (mVoiceLayout.getIvStatus().isSelected()) {
                    mPlayerManager.pause();
                } else {
                    mPlayerManager.play(String.valueOf(mVoiceLayout.getTag()));
                    mVoiceLayout.setState(VoiceLayout.STATE_LOADING);
                }
            }

        }
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        mTvRelease.setClickable(true);
    }

    private void changeReplyType(int type) {
        if (type == Constants.REPLY_IN_TEXT) {
            mEtReply.setVisibility(View.VISIBLE);
            mVoiceLayout.setVisibility(View.GONE);
            mTvRouteRecord.setVisibility(View.GONE);
            mCbAnonymous.setVisibility(View.VISIBLE);
            mPlayerManager.pause();
        } else {
            mEtReply.setVisibility(View.GONE);
            InputMethodUtils.hideKeyboard(mContext, mEtReply);
            if (mVoiceLayout.getTag() == null) {
                mVoiceLayout.setVisibility(View.GONE);
                mTvRouteRecord.setVisibility(View.VISIBLE);
                mCbAnonymous.setVisibility(View.GONE);
            } else {
                mVoiceLayout.setVisibility(View.VISIBLE);
                mTvRouteRecord.setVisibility(View.GONE);
                mCbAnonymous.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void showDataErr() {

    }

    @Override
    public void onUploadFileSuccess(String fileUrl) {
        mPresenter.releaseReply(questionId, mCbAnonymous.isChecked() ?
                        String.valueOf(Constants.REPLY_IN_ANONYMOUS) :
                        String.valueOf(Constants.REPLY_IN_REAL_NAME),
                String.valueOf(Constants.REPLY_IN_VOICE), null, fileUrl, mDuration);
    }

    @Override
    public void onUploadFileFail() {

    }

    @Override
    public void onReleaseSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (RC_VOICE == requestCode) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras == null) {
                        return;
                    }
                    String filePath = extras.getString("path");
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(filePath);
                    String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    mDuration = duration;
                    mVoiceLayout.setTag(filePath);
                    mVoiceLayout.setDuration(Long.parseLong(duration));
                    changeReplyType(Constants.REPLY_IN_VOICE);
                }
            }
        }
    }
}