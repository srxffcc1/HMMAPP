package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.OrderListFragmentGoodsContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.OrderList;
import com.healthy.library.model.OrderListPageInfo;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Li
 * @date 2019/04/29 16:19
 * @des
 */

public class OrderListFragmentGoodsPresenter implements OrderListFragmentGoodsContract.Presenter {

    private Context mContext;
    private OrderListFragmentGoodsContract.View mView;

    public OrderListFragmentGoodsPresenter(Context context, OrderListFragmentGoodsContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getOrderListGoods(Map<String, Object> map, final OrderList orderList) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if (orderList.isMain == 0) {
                            orderList.orderChild = resolveOrderChildData(obj);
                        } else if (orderList.isMain == 1) {
                            orderList.orderFather = resolveOrderFatherData(obj);
                        }
                        mView.onGetOrderListGoodsSuccess();
                    }
                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private OrderList.OrderChild resolveOrderChildData(String obj) {
        OrderList.OrderChild result = null;
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
            Type type = new TypeToken<OrderList.OrderChild>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private OrderList.OrderFather resolveOrderFatherData(String obj) {
        OrderList.OrderFather result = null;
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
            Type type = new TypeToken<OrderList.OrderFather>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
