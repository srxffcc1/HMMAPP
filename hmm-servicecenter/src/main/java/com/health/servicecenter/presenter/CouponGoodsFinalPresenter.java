package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.CouponGoodsFinalContract;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CouponGoodsFinalPresenter implements CouponGoodsFinalContract.Presenter {
    private Context mContext;
    private CouponGoodsFinalContract.View mView;

    public CouponGoodsFinalPresenter(Context context, CouponGoodsFinalContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getCouponList(Map<String, Object> map, final GoodsBasketCell goodsBasketCell) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        List<CouponInfoZ> list=resolveCouponData(obj);
//                        if(goodsBasketCell!=null){
//                            goodsBasketCell.setCouponInfoZList(list);
//                        }
                        mView.sucessGetCouponList(new ArrayList<CouponInfoZ>());//不进行线上优惠券得展示了
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.sucessGetCouponList(null);
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                    }
                });
    }

    @Override
    public void getCouponMineList(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        List<CouponInfoZ> list=resolveMineCouponData(obj);
                        mView.sucessGetCouponMineList(new ArrayList<CouponInfoZ>());//不进行线上优惠券得展示了
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private List<CouponInfoZ> resolveCouponData(String obj) {
        List<CouponInfoZ> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONArray("data");
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
    private List<CouponInfoZ> resolveMineCouponData(String obj) {
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
