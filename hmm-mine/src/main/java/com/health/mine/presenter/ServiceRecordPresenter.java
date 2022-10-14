package com.health.mine.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.health.mine.contract.ServiceRecordContract;
import com.health.mine.model.ServiceRecordModel;
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
 * @date 2019/05/28 11:10
 * @des
 */
public class ServiceRecordPresenter implements ServiceRecordContract.Presenter {
    private AppCompatActivity mActivity;
    private ServiceRecordContract.View mView;
    private CommonViewModel mViewModel;
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            resolveData(s);
        }
    };

    public ServiceRecordPresenter(AppCompatActivity activity, ServiceRecordContract.View view) {
        mActivity = activity;
        mView = view;
        mViewModel = ViewModelProviders.of(activity).get(CommonViewModel.class);
    }

    @Override
    public void getServiceRecord(String serviceId, int shopId, int shopBrand) {
        Map<String, Object> map = new HashMap<>(4);
        map.put(Functions.FUNCTION, Functions.FUNCTION_SERVICE_RECORD);
        map.put("serviceId", serviceId);
        map.put("shopId", String.valueOf(shopId));
        map.put("shopBrand", String.valueOf(shopBrand));
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mViewModel.getModel().setValue(obj);
                        mViewModel.getModel().observe(mActivity, mObserver);
                    }
                });
    }

    private void resolveData(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            List<ServiceRecordModel> list = new ArrayList<>();
            JSONArray recordArray = JsonUtils.getJsonArray(jsonObject, "data");
            for (int i = 0; i < recordArray.length(); i++) {
                JSONObject recordObj = JsonUtils.getJsonObject(recordArray, i);
                ServiceRecordModel model = new ServiceRecordModel();
                model.setTechnicianName(JsonUtils.getString(recordObj, "technicianName"));
                model.setServiceDate(JsonUtils.getString(recordObj, "startTime"));
                model.setLeftCount(JsonUtils.getString(recordObj, "serviceLeftCount"));
                list.add(model);
            }
            mView.onGetServiceRecordListSuccess(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}