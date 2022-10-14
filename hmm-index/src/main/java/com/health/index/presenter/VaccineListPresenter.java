package com.health.index.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.health.index.contract.VaccineListContract;
import com.health.index.model.VaccineModel;
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
 * @date 2019/04/23 13:35
 * @des
 */
public class VaccineListPresenter implements VaccineListContract.Presenter {
    private AppCompatActivity mActivity;
    private VaccineListContract.View mView;
    private CommonViewModel mViewModel;
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            mView.onGetVaccineListSuccess(resolveVaccineList(s), getCurrentId(s));
        }
    };

    public VaccineListPresenter(AppCompatActivity activity, VaccineListContract.View view) {
        mActivity = activity;
        mView = view;
        mViewModel = ViewModelProviders.of(activity).get(CommonViewModel.class);
    }

    @Override
    public void getVaccineList() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, Functions.FUNCTION_VACCINE_LIST);
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mViewModel.getModel().setValue(obj);
                        mViewModel.getModel().observe(mActivity, mObserver);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    /**
     * 解析疫苗列表
     *
     * @param s json
     * @return 疫苗列表
     */
    private List<VaccineModel> resolveVaccineList(String s) {
        List<VaccineModel> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            JSONArray array = JsonUtils.getJsonArray(jsonObject, "vaccineTimeResultList");
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = JsonUtils.getJsonObject(array, i);
                VaccineModel vaccineModel = new VaccineModel();
                vaccineModel.setVaccineAgeId(JsonUtils.getInt(item, "vaccineAgeId"));
                vaccineModel.setVaccineAge(JsonUtils.getString(item, "vaccineAge"));
                vaccineModel.setReason(JsonUtils.getString(item, "reason"));
                vaccineModel.setVaccineName(JsonUtils.getString(item, "vaccineName"));
                vaccineModel.setId(JsonUtils.getString(item, "id"));
                vaccineModel.setVaccinationTime(JsonUtils.getString(item, "vaccinationTime"));
                vaccineModel.setVaccinationTimestamp(JsonUtils.getString(item, "vaccinationTimestamp"));
                list.add(vaccineModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    private int getCurrentId(String s) {
        int id = 0;
        try {
            JSONObject jsonObject = new JSONObject(s);
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            id = JsonUtils.getInt(jsonObject, "vaccintAgeId");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
