package com.health.faq.presenter;

import android.content.Context;

import com.health.faq.contract.LatestQuestionContract;
import com.health.faq.model.QuestionModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Li
 * Date: 2019/7/25 0025
 * Description:
 */
public class LatestQuestionPresenter implements LatestQuestionContract.Presenter {

    private Context mContext;
    private LatestQuestionContract.View mView;

    public LatestQuestionPresenter(Context context, LatestQuestionContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getQuestionList(String page) {
        Map<String, Object> map = new HashMap<>(3);
        map.put(Functions.FUNCTION, Functions.FUNCTION_LATEST_QUESTION);
        map.put("pageSize", "10");
        map.put("currentPage", page+"");
        boolean firstPage = "1".equals(page);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false, false,
                        false, firstPage, firstPage, true) {

                    @Override
                    protected void onGetResultSuccess(String obj) {
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            JSONObject dataObj = JsonUtils.getJsonObject(jsonObject, "data");
                            JSONObject listObj = JsonUtils.getJsonObject(dataObj, "listInfo");
                            JSONArray jsonArray = JsonUtils.getJsonArray(listObj, "items");
                            List<QuestionModel> list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                QuestionModel model = new QuestionModel();
                                JSONObject questionObj = JsonUtils.getJsonObject(jsonArray, i);
                                model.setQuestionTitle(JsonUtils.getString(questionObj, "introduction"));
                                model.setQuestionerPeriodDes(JsonUtils.getString(questionObj, "currentStatus"));
                                model.setAnswerNum(JsonUtils.getInt(questionObj, "replyCount"));
                                model.setReadCount(JsonUtils.getInt(questionObj,"readCount"));
                                model.setQuestionCost(FormatUtils
                                        .formatRewardMoney(JsonUtils.getString(questionObj, "rewardMoney")));
                                model.setQuestionerAvatar(JsonUtils.getString(questionObj, "faceUrl"));
                                model.setQuestionerNickname(JsonUtils.getString(questionObj, "nickName"));
                                model.setQuestionerPeriod(JsonUtils.getInt(questionObj, "currentStatusType"));
                                model.setQuestionId(JsonUtils.getString(questionObj, "id"));
                                list.add(model);
                            }
                            mView.onGetQuestionListSuccess(list, JsonUtils.getInt(listObj, "currentPage"),
                                    JsonUtils.getInt(listObj, "isMore") == 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
