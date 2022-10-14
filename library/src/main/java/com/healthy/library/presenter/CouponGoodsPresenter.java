package com.healthy.library.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.contract.CouponGoodsContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.net.NoStringObserver;
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

public class CouponGoodsPresenter implements CouponGoodsContract.Presenter {
    private Context mContext;
    private CouponGoodsContract.View mView;

    public CouponGoodsPresenter(Context context, CouponGoodsContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getCouponList(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.sucessGetCouponList(resolveCouponData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.sucessGetCouponList(new ArrayList<CouponInfoZ>());
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void receivedCoupon(String activityId, String couponId,String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("couponId", couponId);
        map.put("activityId", activityId);
        map.put("memberId", memberId);
        map.put(Functions.FUNCTION, "80008");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,true,true,true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onSucessReceivedCoupon(new JSONObject(obj).getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.onSucessReceivedCoupon(msg);
                    }
                });
    }

    private List<CouponInfoZ> resolveCouponData(String obj) {
        List<CouponInfoZ> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("list");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<CouponInfoZ>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

    }


}
