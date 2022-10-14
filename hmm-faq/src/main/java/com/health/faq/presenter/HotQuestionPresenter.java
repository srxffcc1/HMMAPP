package com.health.faq.presenter;

import android.content.Context;

import com.health.faq.contract.HotQuestionContract;
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
 * @author Li
 * @date 2019/07/25 17:44
 * @des
 */

public class HotQuestionPresenter implements HotQuestionContract.Presenter {
    private Context mContext;
    private HotQuestionContract.View mView;

    public HotQuestionPresenter(Context context, HotQuestionContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getQuestionList() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, Functions.FUNCTION_HOT_QUESTION);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            JSONArray jsonArray = JsonUtils.getJsonArray(jsonObject, "data");
                            List<QuestionModel> list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                QuestionModel model = new QuestionModel();
                                JSONObject questionObj = JsonUtils.getJsonObject(jsonArray, i);
                                model.setQuestionTitle(JsonUtils.getString(questionObj, "introduction"));
                                model.setQuestionerPeriodDes(JsonUtils.getString(questionObj, "currentStatus"));
                                model.setAnswerNum(JsonUtils.getInt(questionObj, "replyCount"));
                                model.setReadCount(JsonUtils.getInt(questionObj, "readCount"));
                                model.setQuestionCost(FormatUtils
                                        .formatRewardMoney(JsonUtils.getString(questionObj, "rewardMoney")));
                                model.setQuestionerAvatar(JsonUtils.getString(questionObj, "faceUrl"));
                                model.setQuestionerNickname(JsonUtils.getString(questionObj, "nickName"));
                                model.setQuestionerPeriod(JsonUtils.getInt(questionObj, "currentStatusType"));
                                model.setQuestionId(JsonUtils.getString(questionObj, "id"));
                                list.add(model);
                            }
                            mView.onGetQuestionListSuccess(list);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
