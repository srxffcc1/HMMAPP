package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.OrderZxingContract;
import com.healthy.library.model.OrderGoodsListModel;
import com.healthy.library.constant.Functions;
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
 * @date 2019/04/10 16:29
 * @des 订单详情
 */

public class OrderZxingPresenter implements OrderZxingContract.Presenter {

    private Context mContext;
    private OrderZxingContract.View mView;


    public OrderZxingPresenter(Context context, OrderZxingContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getOrderDetail(String orderId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("orderId", orderId);
        map.put(Functions.FUNCTION, "9534");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetOrderDetailSuccess(resolveListData(obj));
                    }
                });
    }


    private OrderGoodsListModel resolveListData(String obj) {
        List<OrderGoodsListModel> result = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(obj).getJSONObject("data");
            JSONArray data = object.getJSONArray("orderGoodsList");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<OrderGoodsListModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }

    }

}