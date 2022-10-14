package com.health.mall.presenter;

import android.content.Context;

import com.health.mall.contract.ProvinceCityContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.ProvinceCityModel;
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
 * @date 2019/04/11 09:20
 * @des
 */

public class ProvinceCityPresenter implements ProvinceCityContract.Presenter {
    private Context mContext;
    private ProvinceCityContract.View mView;

    public ProvinceCityPresenter(Context context, ProvinceCityContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getProvinceList(String fatherid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, "5075");
        map.put("fatherId", fatherid);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            List<ProvinceCityModel> list = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(obj);
                            JSONArray array = JsonUtils.getJsonArray(jsonObject, "data");
                            for (int i = 0; i < array.length(); i++) {
                                ProvinceCityModel model = new ProvinceCityModel();
                                JSONObject object = array.getJSONObject(i);
                                model.setName(JsonUtils.getString(object, "name"));
                                model.setId(JsonUtils.getString(object, "id"));
                                model.setAreaNo(JsonUtils.getString(object, "areaNo"));
                                list.add(model);
                            }
                            mView.onGetProvinceListSuccess(list);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void getCityList(String provinceId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, "5075");
        map.put("fatherId", provinceId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            List<ProvinceCityModel> list = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(obj);
                            JSONArray array = JsonUtils.getJsonArray(jsonObject, "data");
                            for (int i = 0; i < array.length(); i++) {
                                ProvinceCityModel model = new ProvinceCityModel();
                                JSONObject object = array.getJSONObject(i);
                                model.setName(JsonUtils.getString(object, "name"));
                                model.setId(JsonUtils.getString(object, "id"));
                                model.setAreaNo(JsonUtils.getString(object, "areaNo"));
                                list.add(model);
                            }
                            mView.onGetCityListSuccess(list);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void getStreetList(String cityId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, "5075");
        map.put("fatherId", cityId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            List<ProvinceCityModel> list = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(obj);
                            JSONArray array = JsonUtils.getJsonArray(jsonObject, "data");
                            for (int i = 0; i < array.length(); i++) {
                                ProvinceCityModel model = new ProvinceCityModel();
                                JSONObject object = array.getJSONObject(i);
                                model.setName(JsonUtils.getString(object, "name"));
                                model.setId(JsonUtils.getString(object, "id"));
                                model.setAreaNo(JsonUtils.getString(object, "areaNo"));
                                list.add(model);
                            }
                            mView.onGetStreetListSuccess(list);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
