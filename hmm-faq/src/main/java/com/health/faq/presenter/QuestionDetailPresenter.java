package com.health.faq.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import com.health.faq.adapter.QuestionDetailAdapter;
import com.health.faq.contract.QuestionDetailContract;
import com.health.faq.model.QuestionDetailModel;
import com.health.faq.model.QuestionModel;
import com.health.faq.model.ReplyAdoptedModel;
import com.health.faq.model.ReplyModel;
import com.health.faq.model.TitleModel;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.CommonViewModel;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/07/02 15:33
 * @des
 */
public class QuestionDetailPresenter implements QuestionDetailContract.Presenter {
    private Context mContext;
    private QuestionDetailContract.View mView;
    private CommonViewModel mViewModel;
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            resolveData(s);
        }
    };


    public QuestionDetailPresenter(Context context, QuestionDetailContract.View view) {
        mContext = context;
        mView = view;
        if (context instanceof AppCompatActivity) {
            mViewModel = ViewModelProviders.of((AppCompatActivity) context)
                    .get(CommonViewModel.class);
            mViewModel.getModel().observe((AppCompatActivity) context, mObserver);
        }
    }

    @Override
    public void getQuestionDetail(String questionId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_QUESTION_DETAIL);
        map.put("id", questionId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, true,
                        false, true, true, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mViewModel.getModel().setValue(obj);
                    }
                });
    }

    @Override
    public void adoptReply(String id, String questionId) {
        Map<String, Object> map = new HashMap<>(3);
        map.put(Functions.FUNCTION, Functions.FUNCTION_ADOPT_REPLY);
        map.put("id", id);
        map.put("questionId", questionId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true, true,
                        true, false, false, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onAdoptSuccess();
                    }
                });
    }

    private void resolveData(String s) {
        try {
            List<QuestionDetailModel> list = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(s);
            JSONObject dataObj = JsonUtils.getJsonObject(jsonObject, "data");
            JSONArray myAnswerArray = JsonUtils.getJsonArray(dataObj, "rewardQuestionReplyListByOneSelf");
            JSONObject questionObj = JsonUtils.getJsonObject(dataObj, "rewardQuestion");
            JSONObject userInfoObj = JsonUtils.getJsonObject(dataObj, "rewardQuestionMemberInfoDTO");

            mView.onReplyInfo(JsonUtils.getString(questionObj, "introduction"),
                    JsonUtils.getString(userInfoObj, "nickName"),
                    JsonUtils.getString(userInfoObj, "faceUrl"));
            String status = JsonUtils.getString(questionObj, "status");
            int myAnswerNum = myAnswerArray.length();
            boolean hasMyAnswer = myAnswerNum > 0;
            boolean isMineQuestion = JsonUtils.getInt(dataObj, "isOneSelf") == 1;
            if (hasMyAnswer) {
                resolveIsMyReplyAdopted(JsonUtils.getJsonObject(dataObj,
                        "isOneSelfAndIsBestDTO"), list);
                QuestionDetailModel detailModel = new QuestionDetailModel(QuestionDetailAdapter.TYPE_TITLE);
                TitleModel titleModel = new TitleModel();
                titleModel.setTitle("我的回答");
                if (myAnswerNum == 1) {
                    String date = JsonUtils.getString(JsonUtils
                            .getJsonObject(myAnswerArray, 0), "createDate");
                    titleModel.setDes(DateUtils.formatPattern("yyyy-MM-dd HH:mm:ss",
                            "MM-dd HH:mm", date));
                }
                detailModel.setTitleModel(titleModel);
                list.add(detailModel);

                resolveMyReply(myAnswerArray, list);
                list.add(new QuestionDetailModel(QuestionDetailAdapter.TYPE_DIVIDER));
            }

            resolveQuestion(list, questionObj, hasMyAnswer);
            JSONArray replyArray = JsonUtils.getJsonArray(dataObj, "rewardQuestionReplyList");
            boolean hasReply = replyArray.length() > 0;
            boolean hasBestReply = !JsonUtils.checkJsonObjectEmpty(dataObj, "bestRewardQuestionReply");
            if (hasBestReply) {
                resolveBestReply(list, JsonUtils.getJsonObject(dataObj, "bestRewardQuestionReply"),
                        hasReply);
            }

            if (hasReply) {
                resolveReply(list, isMineQuestion, replyArray, hasBestReply);
            }

            //问题由当前用户提出
            if (isMineQuestion) {
                //当前悬赏进行中并且没有回答以及最佳回答
                if (Constants.REWARD_STATUS_IN.equals(status)
                        && !hasReply && !hasBestReply) {
                    QuestionDetailModel improveModel = new QuestionDetailModel();
                    improveModel.setType(QuestionDetailAdapter.TYPE_IMPROVE_REWARD);
                    list.add(improveModel);
                }
            } else {
                if (Constants.REWARD_STATUS_IN.equals(status)
                        && !hasReply && !hasBestReply) {
                    QuestionDetailModel improveModel = new QuestionDetailModel();
                    improveModel.setType(QuestionDetailAdapter.TYPE_FIRST_REPLY);
                    list.add(improveModel);
                }
            }
            if (Constants.REWARD_STATUS_CLOSE.equals(status)) {
                list.add(new QuestionDetailModel(QuestionDetailAdapter.TYPE_NO_REPLY));
            }


            boolean showReply = Constants.REWARD_STATUS_IN.equals(status)
                    && hasReply && !isMineQuestion;

            mView.onGetQuestionDetailSuccess(list, showReply,
                    JsonUtils.getInt(dataObj, "readCount"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void resolveIsMyReplyAdopted(JSONObject jsonObject, List<QuestionDetailModel> list) {
        int type = JsonUtils.getInt(jsonObject, "isBestType");
        if (type == 2 || type == 3) {
            QuestionDetailModel model = new QuestionDetailModel(QuestionDetailAdapter.TYPE_MY_REPLY_ADOPTED);
            ReplyAdoptedModel replyAdoptedModel = new ReplyAdoptedModel();
            replyAdoptedModel.setType(type);
            replyAdoptedModel.setIncome(FormatUtils
                    .formatRewardMoney(JsonUtils.getString(jsonObject, "getRewardMoney")));
            model.setAdoptedModel(replyAdoptedModel);
            list.add(model);
        }
    }

    private void resolveMyReply(JSONArray replyArray, List<QuestionDetailModel> list) {
        for (int i = 0; i < replyArray.length(); i++) {
            JSONObject replyObj = JsonUtils.getJsonObject(replyArray, i);
            QuestionDetailModel replyModel = new QuestionDetailModel();
            replyModel.setType(QuestionDetailAdapter.TYPE_MY_ANSWER);
            ReplyModel replyDetail = new ReplyModel();
            replyDetail.setId(JsonUtils.getString(replyObj, "id"));
            replyDetail.setQuestionId(JsonUtils.getString(replyObj, "questionId"));
            replyDetail.setFaceUrl(JsonUtils.getString(replyObj, "faceUrl"));
            replyDetail.setNickname(JsonUtils.getString(replyObj, "nickName"));
            replyDetail.setReplyDetail(JsonUtils.getString(replyObj, "replyDetail"));
            replyDetail.setSoundUrl(JsonUtils.getString(replyObj, "soundUrl"));
            replyDetail.setAudioReply(TextUtils.isEmpty(replyDetail.getReplyDetail()));
            replyDetail.setReplyDate(DateUtils.formatString2Time("yyyy-MM-dd HH:mm:ss",
                    JsonUtils.getString(replyObj, "createDate")));
            replyDetail.setShowDate(replyArray.length() > 1);
            replyDetail.setDuration(JsonUtils.getString(replyObj, "soundLength"));
            replyModel.setReplyModel(replyDetail);
            list.add(replyModel);
        }
    }

    /**
     * 解析问题
     */
    private void resolveQuestion(List<QuestionDetailModel> list, JSONObject questionObj,
                                 boolean hasMyAnswer) {
        QuestionModel questionModel = new QuestionModel();
        questionModel.setShowSupTitle(hasMyAnswer);
        questionModel.setQuestionTitle(JsonUtils.getString(questionObj, "introduction"));
        questionModel.setQuestionerPeriodDes(JsonUtils.getString(questionObj, "currentStatus"));
        questionModel.setAnswerNum(JsonUtils.getInt(questionObj, "replyCount"));
        questionModel.setQuestionCost(FormatUtils
                .formatRewardMoney(JsonUtils.getString(questionObj, "rewardMoney")));
        questionModel.setQuestionerAvatar(JsonUtils.getString(questionObj, "faceUrl"));
        questionModel.setQuestionerNickname(JsonUtils.getString(questionObj, "nickName"));
        questionModel.setQuestionerPeriod(JsonUtils.getInt(questionObj, "currentStatusType"));
        questionModel.setQuestionId(JsonUtils.getString(questionObj, "id"));
        questionModel.setQuestionContent(JsonUtils.getString(questionObj, "detail"));
        questionModel.setStatus(JsonUtils.getString(questionObj, "status"));
        questionModel.setHasReturn("1".equals(JsonUtils.getString(questionObj, "isReturn")));
        questionModel.setCreateDate(DateUtils.formatString2Time("yyyy-MM-dd HH:mm:ss",
                JsonUtils.getString(questionObj, "createDate")));
        String photo = JsonUtils.getString(questionObj, "photo");
        if (TextUtils.isEmpty(photo)) {
            questionModel.setPhotos(new String[0]);
        } else {
            questionModel.setPhotos(photo.split(","));
        }
        QuestionDetailModel model = new QuestionDetailModel();
        model.setType(QuestionDetailAdapter.TYPE_QUESTION_HEADER);
        model.setQuestionModel(questionModel);
        list.add(model);


    }


    /**
     * 解析最佳回答
     */
    private void resolveBestReply(List<QuestionDetailModel> list, JSONObject replyObj, boolean hasReply) {
        QuestionDetailModel bestTitleModel = new QuestionDetailModel();
        TitleModel bestModel = new TitleModel();
        bestModel.setTitle("最佳答案");
        bestModel.setShowBadge(true);
        bestTitleModel.setType(QuestionDetailAdapter.TYPE_TITLE);
        bestTitleModel.setTitleModel(bestModel);
        list.add(bestTitleModel);


        QuestionDetailModel replyDetailModel = new QuestionDetailModel();
        ReplyModel replyModel = new ReplyModel();
        replyDetailModel.setType(QuestionDetailAdapter.TYPE_REPLY);

        replyModel.setId(JsonUtils.getString(replyObj, "id"));
        replyModel.setQuestionId(JsonUtils.getString(replyObj, "questionId"));
        replyModel.setFaceUrl(JsonUtils.getString(replyObj, "faceUrl"));
        replyModel.setNickname(JsonUtils.getString(replyObj, "nickName"));
        replyModel.setReplyDetail(JsonUtils.getString(replyObj, "replyDetail"));
        replyModel.setSoundUrl(JsonUtils.getString(replyObj, "soundUrl"));
        replyModel.setAudioReply(TextUtils.isEmpty(replyModel.getReplyDetail()));
        replyModel.setReplyDate(DateUtils.formatString2Time("yyyy-MM-dd HH:mm:ss",
                JsonUtils.getString(replyObj, "createDate")));
        replyModel.setDuration(JsonUtils.getString(replyObj, "soundLength"));
        replyDetailModel.setReplyModel(replyModel);
        list.add(replyDetailModel);

        if (hasReply) {
            QuestionDetailModel dividerModel = new QuestionDetailModel();
            dividerModel.setType(QuestionDetailAdapter.TYPE_DIVIDER);
            list.add(dividerModel);
        }
    }

    /**
     * 解析回答
     */
    private void resolveReply(List<QuestionDetailModel> list, boolean isMineQuestion,
                              JSONArray replyArray, boolean hasBestReply) {
        QuestionDetailModel detailModel = new QuestionDetailModel();
        detailModel.setType(QuestionDetailAdapter.TYPE_TITLE);
        TitleModel replyTitleModel = new TitleModel();
        replyTitleModel.setTitle(hasBestReply ? "其他答案" : "所有回答");
//        replyTitleModel.setTitleSub(String.format("（%s）", replyArray.length()));

        detailModel.setTitleModel(replyTitleModel);
        list.add(detailModel);

        for (int i = 0; i < replyArray.length(); i++) {
            JSONObject replyObj = JsonUtils.getJsonObject(replyArray, i);
            QuestionDetailModel replyModel = new QuestionDetailModel();
            replyModel.setType(QuestionDetailAdapter.TYPE_REPLY);
            ReplyModel replyDetail = new ReplyModel();
            replyDetail.setId(JsonUtils.getString(replyObj, "id"));
            replyDetail.setQuestionId(JsonUtils.getString(replyObj, "questionId"));
            replyDetail.setFaceUrl(JsonUtils.getString(replyObj, "faceUrl"));
            replyDetail.setNickname(JsonUtils.getString(replyObj, "nickName"));
            replyDetail.setReplyDetail(JsonUtils.getString(replyObj, "replyDetail"));
            replyDetail.setSoundUrl(JsonUtils.getString(replyObj, "soundUrl"));
            replyDetail.setAudioReply(TextUtils.isEmpty(replyDetail.getReplyDetail()));
            replyDetail.setReplyDate(DateUtils.formatString2Time("yyyy-MM-dd HH:mm:ss",
                    JsonUtils.getString(replyObj, "createDate")));
            replyDetail.setDuration(JsonUtils.getString(replyObj, "soundLength"));
            if (hasBestReply) {
                replyDetail.setMyQuestionReply(false);
            } else {
                replyDetail.setMyQuestionReply(isMineQuestion);
            }
            replyModel.setReplyModel(replyDetail);
            list.add(replyModel);
        }
    }
}
