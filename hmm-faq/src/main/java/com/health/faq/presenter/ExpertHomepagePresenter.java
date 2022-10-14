package com.health.faq.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.health.faq.contract.ExpertHomepageContract;
import com.health.faq.model.ExpertHomepageModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.CommonViewModel;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/07/18 14:19
 * @des
 */
public class ExpertHomepagePresenter implements ExpertHomepageContract.Presenter {


    private AppCompatActivity mActivity;
    private ExpertHomepageContract.View mView;
    private CommonViewModel mViewModel;

    public ExpertHomepagePresenter(AppCompatActivity activity, ExpertHomepageContract.View view) {
        mActivity = activity;
        mView = view;
        mViewModel = ViewModelProviders.of(activity).get(CommonViewModel.class);
        mViewModel.getModel().observe(activity, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                resolveData(s);
            }
        });

    }

    @Override
    public void getExpertInfo(String id) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_EXPERT_HOMEPAGE);
        map.put("userId", id);
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false, false,
                        false, true, true, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mViewModel.getModel().setValue(obj);
                    }
                });
    }

    private void resolveData(String obj) {
        try {
            JSONObject jsonObject = new JSONObject(obj);
            JSONObject dataObject = JsonUtils.getJsonObject(jsonObject, "data");
            ExpertHomepageModel.HeaderInfo headerInfo = new ExpertHomepageModel.HeaderInfo();
            headerInfo.consultingFees = JsonUtils.getDouble(dataObject, "consultingFees");
            headerInfo.id = JsonUtils.getString(dataObject, "userId");


            headerInfo.faceUrl = JsonUtils.getString(dataObject, "faceUrl");
            headerInfo.realName = JsonUtils.getString(dataObject, "realName");
            headerInfo.avgReplyMinutes = JsonUtils.getString(dataObject, "avgReplyMinutes", "0");
            headerInfo.askTimes = JsonUtils.getString(dataObject, "askTimes", "0");
            headerInfo.rank = JsonUtils.getString(dataObject, "rank");
            headerInfo.goodSkills = JsonUtils.getString(dataObject, "goodSkills");
            headerInfo.expertIntroduction = JsonUtils.getString(dataObject, "expertIntroduction");
            headerInfo.answerCount = JsonUtils.getInt(dataObject, "answerCount");
            headerInfo.cost = JsonUtils.getString(dataObject, "consultingFees");
            headerInfo.online = "1".equals(JsonUtils.getString(dataObject, "isOnline"));

            List<ExpertHomepageModel> list = new ArrayList<>();
            ExpertHomepageModel headerInfoModel = new ExpertHomepageModel(ExpertHomepageModel.TYPE_HEADER);
            headerInfoModel.setHeaderInfo(headerInfo);
            list.add(headerInfoModel);


            if (headerInfo.answerCount == 0) {
                ExpertHomepageModel expertHomepageModel = new ExpertHomepageModel(ExpertHomepageModel.TYPE_NO_ANSWER);
                expertHomepageModel.setOnline(headerInfo.online);
                list.add(expertHomepageModel);
            } else {
                JSONArray answerArray = JsonUtils
                        .getJsonArray(dataObject, "expertRewardQuestionReplyList");
                list.addAll(resolveAnswers(answerArray));
            }

            mView.onGetExpertInfoSuccess(list, headerInfo.answerCount, headerInfo.online);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ExpertHomepageModel> resolveAnswers(JSONArray array) {
        List<ExpertHomepageModel> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            ExpertHomepageModel model = new ExpertHomepageModel(ExpertHomepageModel.TYPE_ANSWER);
            JSONObject jsonObject = JsonUtils.getJsonObject(array, i);
            ExpertHomepageModel.Answer answer = new ExpertHomepageModel.Answer();

            answer.answerId = JsonUtils.getString(jsonObject, "id");
            answer.questionId = JsonUtils.getString(jsonObject, "questionId");
            answer.questionTitle = JsonUtils.getString(jsonObject, "rewardQuestionDetail");
            answer.date = JsonUtils.getString(jsonObject, "createDateIllustration");
            answer.faceUrl = JsonUtils.getString(jsonObject, "expertFaceUrl");
            answer.readCount = JsonUtils.getInt(jsonObject, "readCount");
            model.setAnswer(answer);
            list.add(model);
        }
        return list;

    }
}
