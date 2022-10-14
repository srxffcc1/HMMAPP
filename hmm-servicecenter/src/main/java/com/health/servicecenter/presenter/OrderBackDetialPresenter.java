package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.OrderBackDetialContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.BackListModel;
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
 * @date 2019/04/29 16:19
 * @des
 */

public class OrderBackDetialPresenter implements OrderBackDetialContract.Presenter {

    private Context mContext;
    private OrderBackDetialContract.View mView;

    public OrderBackDetialPresenter(Context context, OrderBackDetialContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getBackDetial(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("refundId", id);
        map.put(Functions.FUNCTION, "25018");
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


    private BackListModel resolveStoreData(String obj) {
        BackListModel model = null;
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
            Type type = new TypeToken<BackListModel>() {
            }.getType();
            model = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;

    }
}
