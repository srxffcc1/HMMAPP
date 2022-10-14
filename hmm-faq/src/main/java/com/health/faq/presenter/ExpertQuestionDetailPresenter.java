package com.health.faq.presenter;

import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import com.health.faq.contract.ExpertQuestionDetailContract;
import com.health.faq.model.ExpertAnswerModel;
import com.health.faq.model.ExpertInfoModel;
import com.health.faq.model.ExpertQuestionModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/07/19 11:09
 * @des
 */
public class ExpertQuestionDetailPresenter implements ExpertQuestionDetailContract.Presenter {
    private AppCompatActivity mActivity;
    private ExpertQuestionDetailContract.View mView;

    public ExpertQuestionDetailPresenter(AppCompatActivity activity,
                                         ExpertQuestionDetailContract.View view) {
        mActivity = activity;
        mView = view;
    }

    @Override
    public void getQuestionDetail(String questionId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_EXPERT_QUESTION_DETAIL);
        map.put("id", questionId);
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false,
                        true,
                        false, true, true, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        resolveData(obj);
                    }
                });
    }

    private void resolveData(String obj) {
        try {
            JSONObject jsonObject = new JSONObject(obj);
            JSONObject dataObj = JsonUtils.getJsonObject(jsonObject, "data");
            boolean hasReplied = JsonUtils.getInt(dataObj, "isAnswer") == 1;
            ExpertQuestionModel questionModel = new ExpertQuestionModel();
            ExpertAnswerModel answerModel = new ExpertAnswerModel();
            JSONObject questionObj = JsonUtils.getJsonObject(dataObj, "rewardQuestion");
            JSONObject answerObj = JsonUtils.getJsonObject(dataObj, "expertRewardQuestionReply");
            JSONObject expertInfoObj = JsonUtils.getJsonObject(dataObj, "expertInfoDTO");

            questionModel.faceUrl = JsonUtils.getString(questionObj, "faceUrl");
            questionModel.nickName = JsonUtils.getString(questionObj, "nickName");
            questionModel.currentStatus = JsonUtils.getString(questionObj, "currentStatus");
            questionModel.currentStatusType = JsonUtils.getInt(questionObj, "currentStatusType");
            questionModel.readCount = JsonUtils.getInt(dataObj, "readCount");
            questionModel.detail = JsonUtils.getString(questionObj, "detail");
            String photo = JsonUtils.getString(questionObj, "photo");
            questionModel.photos = TextUtils.isEmpty(photo) ? new String[0] : photo.split(",");

            answerModel.faceUrl = JsonUtils.getString(answerObj, "expertFaceUrl");
            answerModel.soundUrl = JsonUtils.getString(answerObj, "expertSoundUrl");
            answerModel.isSoundReply = !TextUtils.isEmpty(answerModel.soundUrl);
            answerModel.soundLength = JsonUtils.getLong(answerObj, "soundLength");
            answerModel.replyDetail = JsonUtils.getString(answerObj, "expertReplyDetail");

            ExpertInfoModel expertInfoModel = new ExpertInfoModel();
            expertInfoModel.name = JsonUtils.getString(expertInfoObj, "realName");
            expertInfoModel.rank = JsonUtils.getString(expertInfoObj, "rank");
            expertInfoModel.faceUrl = JsonUtils.getString(expertInfoObj, "faceUrl");
            expertInfoModel.id = JsonUtils.getString(expertInfoObj, "expertId");
            mView.onGetQuestionDetailSuccess(hasReplied, questionModel, answerModel, expertInfoModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
