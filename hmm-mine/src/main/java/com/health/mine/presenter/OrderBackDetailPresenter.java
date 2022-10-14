package com.health.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.mine.contract.OrderBackDetailContract;
import com.health.mine.model.BackDetialModel;
import com.health.mine.model.OrderBackDetailModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/10 16:29
 * @des 订单详情
 */

public class OrderBackDetailPresenter implements OrderBackDetailContract.Presenter {

    private Context mContext;
    private OrderBackDetailContract.View mView;


    public OrderBackDetailPresenter(Context context, OrderBackDetailContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getBackDetial(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("refundId", id);
        map.put(Functions.FUNCTION, "9552");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetBackDetialSuccess(resolveStoreData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }
    @Override
    public void getOrderDetail(Map<String, Object> map) {

        map.put(Functions.FUNCTION, "7048");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false,false,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetOrderDetailSuccess(resolveData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });


    }

    private OrderBackDetailModel resolveData(String obj) {
        OrderBackDetailModel result = null;
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<OrderBackDetailModel>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private BackDetialModel resolveStoreData(String obj) {
        BackDetialModel model = null;
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
            Type type = new TypeToken<BackDetialModel>() {
            }.getType();
            model = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;

    }
}