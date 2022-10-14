package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.AppointmentListContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.AppointmentListItemModel;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: long
 * @date: 2021/4/1
 */
public class AppointmentListPresenter implements AppointmentListContract.Presenter {

    private Context mContext;
    private AppointmentListContract.View mView;

    public AppointmentListPresenter(Context context, AppointmentListContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9806");
        map.put("pageSize", "10");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetListSuccess(resolveStoreListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getDetails(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9805");

        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetDetailsSuccess(resolveStoreData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void cancleAppointment(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9804");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onCleanSuccess();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onRequestFinish();
                    }
                });
    }

    private List<AppointmentListItemModel> resolveStoreListData(String obj) {
        List<AppointmentListItemModel> newStoreDetialModel = new ArrayList<>();
        try {
            JSONArray list = new JSONObject(obj).getJSONObject("data").getJSONArray("list");
            String userShopInfoDTOS = list.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<AppointmentListItemModel>>() {
            }.getType();
            newStoreDetialModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newStoreDetialModel;
    }


    private AppointmentListItemModel resolveStoreData(String obj) {
        AppointmentListItemModel newStoreDetialModel = null;
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<AppointmentListItemModel>() {
            }.getType();
            newStoreDetialModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newStoreDetialModel;
    }
}
