package com.health.faq.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.faq.R;
import com.health.faq.adapter.QuestionDetailAdapter;
import com.health.faq.contract.QuestionDetailContract;
import com.health.faq.dialog.ImproveRewardDialog;
import com.health.faq.model.QuestionDetailModel;
import com.health.faq.presenter.QuestionDetailPresenter;
import com.health.faq.widget.PlayerManager;
import com.health.faq.widget.VoiceLayout;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Events;
import com.healthy.library.dialog.SimpleDialog;
import com.healthy.library.message.AskEndMessage;
import com.healthy.library.message.RefreshCountMsg;
import com.healthy.library.message.RefreshRewardListMsg;
import com.healthy.library.routes.FaqRoutes;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/07/02 15:18
 * @des 问题详情
 */
@Route(path = FaqRoutes.FAQ_QUESTION_DETAIL)
public class QuestionDetailActivity extends BaseActivity implements QuestionDetailContract.View,
        QuestionDetailAdapter.OnFunctionClickListener, View.OnClickListener,
        ImproveRewardDialog.OnImproveClickListener {

    private RecyclerView mRecycler;
    private QuestionDetailAdapter mAdapter;
    private Group mGroupReply;
    private TextView mTvReply;
    private static final int RC_PAY = 1;
    private static final int RC_REPLY = 835;
    @Autowired
    String questionId;
    @Autowired
    int pos;
    @Autowired
    boolean host;

    private String mTitle;
    private String mNickname;
    private String mAvatar;
    private QuestionDetailPresenter mPresenter;
    private PlayerManager mPlayerManager;
    private VoiceLayout mVoiceLayout = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_question_detail;
    }

    @Override
    protected void findViews() {
        mRecycler = findViewById(R.id.recycler);
        mGroupReply = findViewById(R.id.group_answer);
        mTvReply = findViewById(R.id.tv_reply);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mPresenter = new QuestionDetailPresenter(mContext, this);
        mAdapter = new QuestionDetailAdapter(mContext, this);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycler.setAdapter(mAdapter);
        mTvReply.setOnClickListener(this);
        getData();
    }

    @Override
    public void getData() {
        mPresenter.getQuestionDetail(questionId);
    }

    @Override
    public void onGetQuestionDetailSuccess(List<QuestionDetailModel> list, boolean showReply, int readCount) {
        mGroupReply.setVisibility(showReply ? View.VISIBLE : View.GONE);
        mAdapter.setData(list);
        EventBus.getDefault().post(new RefreshCountMsg(readCount, pos,host));
    }

    @Override
    public void onAdoptSuccess() {
        getData();
    }

    @Override
    public void onReplyInfo(String title, String nickname, String faceUrl) {
        mTitle = title;
        mNickname = nickname;
        mAvatar = faceUrl;
    }

    @Override
    public void onImproveRewardClick(View view) {
        MobclickAgent.onEvent(mContext, Events.EVENT_IMPROVE_REWARD);
        ImproveRewardDialog dialog = ImproveRewardDialog.newInstance();
        dialog.setOnImproveClickListener(this);
        dialog.show(getSupportFragmentManager(), "improve");
    }

    @Override
    public void onReplyClick(View view) {
        MobclickAgent.onEvent(mContext, Events.EVENT_REPLY);
        ARouter.getInstance()
                .build(FaqRoutes.FAQ_REPLY)
                .withString("title", mTitle)
                .withString("questionId", questionId)
                .withString("nickname", mNickname)
                .withString("faceUrl", mAvatar)
                .navigation(this, RC_REPLY);
    }

    @Override
    public void onAdoptClick(final String id, final String questionId) {
        MobclickAgent.onEvent(mContext, Events.EVENT_ADOPT);
        new SimpleDialog.Builder(mContext)
                .setContent("确定采纳为最佳答案？采纳后问题将关闭，悬赏金将打给该用户")
                .setContentGravity(Gravity.CENTER)
                .setNegativeBtn("再等等", null)
                .setPositiveBtn("采纳", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.adoptReply(id, questionId);
                    }
                }).create().show();
    }

    @Override
    public void onClick(View v) {
        onReplyClick(v);
    }

    @Override
    public void onImproveClick(String price) {
        ARouter.getInstance()
                .build(FaqRoutes.FAQ_PAY_TRANSLUCENT)
                .withInt("payType", Constants.PAY_IMPROVE_REWARD)
                .withString("price", price)
                .withString("questionId", questionId)
                .withString("title", mTitle)
                .navigation(this, RC_PAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (RC_PAY == requestCode) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure","悬赏求助页面");
                MobclickAgent.onEvent(mContext, "event2RewardUpBtnClick",nokmap);

                getData();
                EventBus.getDefault().post(new RefreshRewardListMsg());
                EventBus.getDefault().post(new AskEndMessage());
            } else if (RC_REPLY == requestCode) {
                EventBus.getDefault().post(new RefreshRewardListMsg());
                getData();
            }
        }
    }

    @Override
    public void onVoiceClick(VoiceLayout voiceLayout, String path) {
        if (mPlayerManager == null) {
            mPlayerManager = PlayerManager.newInstance();
            getLifecycle().addObserver(mPlayerManager);
        }
        mPlayerManager.attachVoiceLayout(voiceLayout);
        if (mVoiceLayout != null) {
            if (mVoiceLayout == voiceLayout) {
                if (mVoiceLayout.getIvStatus().getVisibility() == View.VISIBLE) {
                    if (mVoiceLayout.getIvStatus().isSelected()) {
                        mPlayerManager.pause();
                    } else {
                        mPlayerManager.play(path);
                        mVoiceLayout.setState(VoiceLayout.STATE_LOADING);
                    }
                }
            } else {
                mPlayerManager.pause();
                mPlayerManager.setPath("");
                mPlayerManager.play(path);
                mVoiceLayout.setState(VoiceLayout.STATE_PAUSE);
            }
        } else {
            mPlayerManager.play(path);
        }
        mVoiceLayout = voiceLayout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}