package com.health.tencentlive.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.tencentlive.contract.CreateLiveContract;
import com.health.tencentlive.contract.LiveGiftContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class LiveGiftPresenter implements LiveGiftContract.Presenter {

    private Context mContext;
    private LiveGiftContract.View mView;

    public LiveGiftPresenter(Context context, LiveGiftContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getGoodsInfo(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9202");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getSuccessGoodsInfo(resolveData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getCouponInfo(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "80022");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getSuccessCouponInfo(resolveCouponData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private GoodsDetail resolveData(String obj) {
        GoodsDetail result = null;
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
            Type type = new TypeToken<GoodsDetail>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private CouponInfoZ resolveCouponData(String obj) {
        CouponInfoZ result = null;
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data").getJSONObject("basic");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<CouponInfoZ>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}