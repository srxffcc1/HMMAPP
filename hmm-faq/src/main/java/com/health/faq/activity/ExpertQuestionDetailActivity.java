package com.health.faq.activity;

import android.os.Bundle;
import androidx.constraintlayout.widget.Group;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.faq.R;
import com.health.faq.contract.ExpertQuestionDetailContract;
import com.health.faq.model.ExpertAnswerModel;
import com.health.faq.model.ExpertInfoModel;
import com.health.faq.model.ExpertQuestionModel;
import com.health.faq.presenter.ExpertQuestionDetailPresenter;
import com.health.faq.widget.ExpertVoiceView;
import com.health.faq.widget.PlayerManager;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.message.RefreshCountMsg;
import com.healthy.library.message.RefreshExpertIndexMsg;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.TopBar;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Li
 * @date 2019/07/17 10:23
 * @des 专家问题详情界面
 */

@Route(path = FaqRoutes.FAQ_EXPERT_QUESTION_DETAIL)
public class ExpertQuestionDetailActivity extends BaseActivity implements
        ExpertQuestionDetailContract.View {

    @Autowired
    String questionId;
    @Autowired
    int pos;
    @Autowired
    boolean host;
    @Autowired
    boolean index;

    private ExpertQuestionDetailPresenter mPresenter;
    private TopBar mTopBar;
    private TextView mTvRealName;
    private TextView mTvRank;
    private Group mGroupExpertInfo;
    private TextView mTvAnswer;
    private ExpertVoiceView mExpertVoiceView;
    private Group mGroupNoReply;
    private TextView mTvClientName;
    private ImageView mIvClient;
    private TextView mTvPeriod;
    private TextView mTvQuestion;
    private LinearLayout mLayoutPictures;

    private ImageView mIvExpertAvatar;
    private TextView mTvExpertName;
    private TextView mTvExpertRank;
    private PlayerManager mPlayerManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_expert_question_detail;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mTvRealName = findViewById(R.id.tv_real_name);
        mTvRank = findViewById(R.id.tv_good_at);
        mGroupExpertInfo = findViewById(R.id.group_expert_info);
        mTvAnswer = findViewById(R.id.tv_answer);
        mExpertVoiceView = findViewById(R.id.expert_voice);
        mGroupNoReply = findViewById(R.id.group_no_reply);
        mTvClientName = findViewById(R.id.tv_client_name);
        mIvClient = findViewById(R.id.iv_client_avatar);
        mTvPeriod = findViewById(R.id.tv_period);
        mTvQuestion = findViewById(R.id.tv_question);
        mLayoutPictures = findViewById(R.id.layout_pictures);

        mTvExpertName = findViewById(R.id.tv_expert_name);
        mIvExpertAvatar = findViewById(R.id.iv_expert_avatar);
        mTvExpertRank = findViewById(R.id.tv_expert_good_at);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mPresenter = new ExpertQuestionDetailPresenter(this, this);

        mExpertVoiceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mExpertVoiceView.getCurrentState()) {
                    case ExpertVoiceView.STATE_LOADING:

                        break;
                    case ExpertVoiceView.STATE_PAUSE:
                        mPlayerManager.pause();
                        break;
                    case ExpertVoiceView.STATE_PLAY:
                        mPlayerManager.play((String) mExpertVoiceView.getTag());
                        mExpertVoiceView.setState(ExpertVoiceView.STATE_LOADING);
                        break;
                    default:
                        break;
                }
            }
        });
        getData();

    }

    @Override
    public void getData() {
        mPresenter.getQuestionDetail(questionId);
    }

    @Override
    public void onGetQuestionDetailSuccess(boolean hasReplied, ExpertQuestionModel questionModel,
                                           ExpertAnswerModel answerModel, final ExpertInfoModel expertInfoModel) {

        if (index) {
            EventBus.getDefault().post(new RefreshExpertIndexMsg(pos, questionModel.readCount));
        }
        if (host) {
            EventBus.getDefault().post(new RefreshCountMsg(questionModel.readCount, pos, host));
        }
        mTopBar.setTitle(expertInfoModel.name);
        mTvRealName.setText(expertInfoModel.name);
        mTvRank.setText(expertInfoModel.rank);
        renderQuestion(questionModel);
        if (!hasReplied) {
            mGroupNoReply.setVisibility(View.VISIBLE);
            mGroupExpertInfo.setVisibility(View.GONE);
            mTvAnswer.setVisibility(View.GONE);
            mExpertVoiceView.setVisibility(View.GONE);
        } else {
            mGroupNoReply.setVisibility(View.GONE);
            mGroupExpertInfo.setVisibility(View.VISIBLE);
            renderAnswer(answerModel);
            mTvExpertName.setText(expertInfoModel.name);
            mTvExpertRank.setText(expertInfoModel.rank);
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .load(expertInfoModel.faceUrl)
                    .error(R.drawable.img_1_1_default)
                    .placeholder(R.drawable.img_1_1_default2)
                    
                    .into(mIvExpertAvatar);
        }
        findViewById(R.id.view_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_HOMEPAGE)
                        .withString("id", expertInfoModel.id)
                        .navigation();
            }
        });
    }

    private void renderAnswer(ExpertAnswerModel answerModel) {

        if (answerModel.isSoundReply) {
            initPlayerManager();
            mExpertVoiceView.setTag(answerModel.soundUrl);
            mTvAnswer.setVisibility(View.GONE);
            mExpertVoiceView.setVisibility(View.VISIBLE);
            mExpertVoiceView.setDuration(answerModel.soundLength);
        } else {
            mTvAnswer.setVisibility(View.VISIBLE);
            mExpertVoiceView.setVisibility(View.GONE);
            mTvAnswer.setText(answerModel.replyDetail);
        }

    }


    private void initPlayerManager() {
        if (mPlayerManager == null) {
            mPlayerManager = PlayerManager.newInstance();
            mPlayerManager.attachExpertVoiceView(mExpertVoiceView);
            getLifecycle().addObserver(mPlayerManager);
        }
    }

    private void renderQuestion(final ExpertQuestionModel model) {
        mTvPeriod.setBackgroundResource(
                model.currentStatusType == 1 ? R.drawable.shape_period_1 :
                        model.currentStatusType == 2 ?
                                R.drawable.shape_period_2 : R.drawable.shape_period_3);
        mTvPeriod.setText(model.currentStatus);
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(model.faceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                
                .into(mIvClient);
        mTvClientName.setText(model.nickName);
        mTvQuestion.setText(model.detail);
        if (model.photos.length == 0) {
            mLayoutPictures.setVisibility(View.GONE);
        } else {
            mLayoutPictures.setVisibility(View.VISIBLE);
            mLayoutPictures.removeAllViews();
            float corner = TransformUtil.dp2px(mContext, 4);
            for (int i = 0; i < model.photos.length; i++) {
                final int pos = i;
                String photo = model.photos[i];
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                        (int) TransformUtil.dp2px(mContext, 75));
                params.weight = 1;
                CornerImageView imageView = new CornerImageView(mContext);
                imageView.setCornerRadius(corner);
                mLayoutPictures.addView(imageView, params);
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .load(photo)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)
                        
                        .into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                                .withInt("pos", pos)
                                .withCharSequenceArray("urls", model.photos)
                                .navigation();
                    }
                });
            }
        }

    }
}
