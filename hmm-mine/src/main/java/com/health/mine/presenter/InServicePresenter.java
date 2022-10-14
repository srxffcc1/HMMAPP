package com.health.mine.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.health.mine.contract.InServiceContract;
import com.health.mine.model.ServiceDetailModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.CommonViewModel;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/05/27 17:40
 * @des
 */
public class InServicePresenter implements InServiceContract.Presenter {

    private AppCompatActivity mActivity;
    private InServiceContract.View mView;
    private CommonViewModel mViewModel;
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            resolveData(s);
        }
    };

    public InServicePresenter(AppCompatActivity activity, InServiceContract.View view) {
        mActivity = activity;
        mView = view;
        mViewModel = ViewModelProviders.of(activity).get(CommonViewModel.class);
    }

    @Override
    public void getServices() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, Functions.FUNCTION_IN_SERVICE);
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResult(String obj) {
                        super.onGetResult(obj);
                        mViewModel.getModel().setValue(obj);
                        mViewModel.getModel().observe(mActivity, mObserver);
                    }
                });
    }

    private void resolveData(String obj) {
        try {
            JSONObject jsonObject = new JSONObject(obj);
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            String topNotice = JsonUtils.getString(jsonObject, "topNotice");
            List<ServiceDetailModel> serviceReminds = new ArrayList<>();
            JSONArray serviceRemindArray = JsonUtils.getJsonArray(jsonObject, "serviceNotice");
            for (int i = 0; i < serviceRemindArray.length(); i++) {
                JSONObject serviceObj = JsonUtils.getJsonObject(serviceRemindArray, i);
                ServiceDetailModel model = new ServiceDetailModel();
                model.setServiceName(JsonUtils.getString(serviceObj, "serviceNames"));
                model.setServiceId(JsonUtils.getString(serviceObj, "orderServeId"));
                serviceReminds.add(model);
            }

            List<ServiceDetailModel> tmpList = new ArrayList<>();
            JSONArray serviceArray = JsonUtils.getJsonArray(jsonObject, "memberServiceList");
            for (int i = 0; i < serviceArray.length(); i++) {
                JSONObject serviceObj = JsonUtils.getJsonObject(serviceArray, i);
                ServiceDetailModel model = new ServiceDetailModel();
                model.setServiceName(JsonUtils.getString(serviceObj, "serviceName"));
                model.setServiceId(JsonUtils.getString(serviceObj, "serviceId"));
                model.setShopId(JsonUtils.getInt(serviceObj, "shopId"));
                model.setShopBrand(JsonUtils.getInt(serviceObj, "shopBrand"));
                model.setLeftCount(JsonUtils.getInt(serviceObj, "serviceLeftCount"));
                model.setDaysNotice(JsonUtils.getString(serviceObj, "daysNotice"));
                model.setShopName(JsonUtils.getString(serviceObj, "shopName"));
                model.setTotalCount(JsonUtils.getInt(serviceObj, "serviceTotalCount"));
                model.setCourseStyle(JsonUtils.getString(serviceObj, "courseStyle"));
                model.setServiceEndDate(JsonUtils.getString(serviceObj, "serviceEndDate"));
                model.setDaysNumber(JsonUtils.getInt(serviceObj, "daysNumber"));
                tmpList.add(model);
            }

            Map<String, List<ServiceDetailModel>> map = new LinkedHashMap<>(10);
            for (ServiceDetailModel model : tmpList) {
                List<ServiceDetailModel> list = map.get(model.toString());
                if (list == null) {
                    list = new ArrayList<>();
                    list.add(model);
                    map.put(model.toString(), list);
                } else {
                    list.add(model);
                }
            }
            Map<String, List<ServiceDetailModel>> realMap = new LinkedHashMap<>(10);
            for (String s : map.keySet()) {
                List<ServiceDetailModel> list = map.get(s);
                if (list != null && list.size() > 0) {
                    realMap.put(list.get(0).getShopName(), list);
                }
            }
            mView.onGetServicesListSuccess(topNotice, serviceReminds, realMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
