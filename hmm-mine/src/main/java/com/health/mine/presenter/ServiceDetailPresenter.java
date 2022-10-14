package com.health.mine.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.health.mine.contract.ServiceDetailContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.CommonViewModel;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/05/28 17:08
 * @des
 */
public class ServiceDetailPresenter implements ServiceDetailContract.Presenter {

    private AppCompatActivity mActivity;
    private ServiceDetailContract.View mView;
    private CommonViewModel mViewModel;
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            resolveData(s);
        }
    };

    public ServiceDetailPresenter(AppCompatActivity activity, ServiceDetailContract.View view) {
        mActivity = activity;
        mView = view;
        mViewModel = ViewModelProviders.of(activity).get(CommonViewModel.class);
    }

    @Override
    public void getServiceDetail(String serviceId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_SERVICE_DETAIL);
        map.put("orderServeId", serviceId);
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
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            String shopName = JsonUtils.getString(jsonObject, "shopName");
            String serviceDate = JsonUtils.getString(jsonObject, "scheduleStartTime");
            StringBuilder builder = new StringBuilder();
            JSONArray serviceArray = JsonUtils.getJsonArray(jsonObject, "serviceList");
            for (int i = 0; i < serviceArray.length(); i++) {
                JSONObject serviceObj = JsonUtils.getJsonObject(serviceArray, i);
                String serviceName = JsonUtils.getString(serviceObj, "serviceName");
                if (i == 0) {
                    builder.append(serviceName);
                } else {
                    builder.append("\n");
                    builder.append(serviceName);
                }
            }

            mView.onGetServiceDetailSuccess(shopName, serviceDate, builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
