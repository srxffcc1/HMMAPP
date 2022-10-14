package com.healthy.library.presenter;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.CardPackContract;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class CardPickLoopPresenter implements CardPackContract.Presenter {

    private Context mContext;
    private CardPackContract.View mView;

    public CardPickLoopPresenter(Context context, CardPackContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void checkInsert(Map<String, Object> map) {
        map.put("function", "get_user_info_002");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        boolean result=false;
                        try {
                            if(new JSONObject(obj).getJSONObject("data")!=null){
                                result=true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mView.onSucessGetInsert(result);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetInsert(false);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void getCouponList(Map<String, Object> map) {
        map.put("function", "80012");
        map.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetCouponList(resolveAdListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void getCouponDetail(Map<String, Object> map) {
        map.put("function", "80013");
        map.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetCoupon(resolveAdData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        if(msg!=null&&msg.contains("已使用")){
                            mView.onSucessGetCoupon(new CouponInfoZ().setStatus(1));
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }
    private CouponInfoZ resolveAdData(String obj) {
        CouponInfoZ result = null;
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
            Type type = new TypeToken<CouponInfoZ>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private List<CouponInfoZ> resolveAdListData(String obj) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}