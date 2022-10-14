package com.health.index.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.health.index.contract.ChangeContract;
import com.health.index.model.ChangeModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.CommonViewModel;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/26 17:46
 * @des
 */
public class ChangePresenter implements ChangeContract.Presenter {

    private Context mContext;
    private Fragment mFragment;
    private ChangeContract.View mView;
    private CommonViewModel mViewModel;
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {

        }
    };

    public ChangePresenter(Fragment fragment, ChangeContract.View view) {
        mContext = fragment.getContext();
        mFragment = fragment;
        mView = view;
        mViewModel = ViewModelProviders.of(fragment).get(CommonViewModel.class);

    }

    @Override
    public void getChange(final Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mViewModel.getModel().setValue(obj);
                        mViewModel.getModel().observe(mFragment, mObserver);
                        resolveData(obj, map);
                    }
                });
    }

    private void resolveData(String obj, Map<String, Object> map) {
        try {
            JSONObject jsonObject = new JSONObject(obj);
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            int weekPos = JsonUtils.getInt(jsonObject, "week");
            List<ChangeModel> list = new ArrayList<>();

            switch (String.valueOf(map.get(Functions.FUNCTION))) {
                case Functions.FUNCTION_PREGNANCY_BABY_CHANGE:
                    JSONArray array = JsonUtils.getJsonArray(jsonObject, "gestationAssistantList");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject babyChangeObj = JsonUtils.getJsonObject(array, i);
                        ChangeModel model = new ChangeModel();
                        model.setBabyChange(JsonUtils.getString(babyChangeObj, "content"));
                        model.setBabyUrl(JsonUtils.getString(babyChangeObj, "image"));
                        model.setBabyWeek(JsonUtils.getString(babyChangeObj, "gestationDay"));
                        model.setShowDate(JsonUtils.getString(babyChangeObj, "showDate"));
                        list.add(model);
                    }
                    break;
                case Functions.FUNCTION_PREGNANCY_MOM_CHANGE:
                    ChangeModel model = new ChangeModel();
                    JSONObject momChangeObj = JsonUtils.getJsonObject(jsonObject, "changeMom");
                    model.setReminder(JsonUtils.getString(momChangeObj, "reminder"));
                    model.setFoodPoints(JsonUtils.getString(momChangeObj, "dietKeyPoint"));
                    model.setMomChange(JsonUtils.getString(momChangeObj, "content"));
                    list.add(model);
                    break;
                case Functions.FUNCTION_PARENTING_MOM_CHANGE:
                    ChangeModel parentingModel = new ChangeModel();
                    JSONObject momChangeObject = JsonUtils.getJsonObject(jsonObject, "postpartumChangeMom");
                    parentingModel.setReminder(JsonUtils.getString(momChangeObject, "reminder"));
                    parentingModel.setFoodPoints(JsonUtils.getString(momChangeObject, "dietKeyPoint"));
                    parentingModel.setMomChange(JsonUtils.getString(momChangeObject, "content"));
                    list.add(parentingModel);
                    break;

                case Functions.FUNCTION_PARENTING_BABY_CHANGE:
                    JSONArray babyArray = JsonUtils.getJsonArray(jsonObject, "postpartumRecoveryList");
                    for (int i = 0; i < babyArray.length(); i++) {
                        JSONObject babyChange = JsonUtils.getJsonObject(babyArray, i);
                        ChangeModel babyModel = new ChangeModel();
                        babyModel.setBabyChange(JsonUtils.getString(babyChange, "content"));
                        babyModel.setBabyDay(JsonUtils.getString(babyChange, "dateContent"));
                        babyModel.setShowDate(JsonUtils.getString(babyChange, "showDate"));
                        list.add(babyModel);
                    }
                    weekPos = JsonUtils.getInt(jsonObject, "months");
                    break;
                default:
                    break;
            }
            mView.onGetChangesSuccess(list, weekPos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
