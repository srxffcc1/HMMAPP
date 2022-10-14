package com.healthy.library.presenter;

import android.content.Context;
import android.text.TextUtils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.AdContract;
import com.healthy.library.model.AdModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.businessutil.LocUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class AdPresenter implements AdContract.Presenter {

    private Context mContext;
    private AdContract.View mView;

    public AdPresenter(Context context, AdContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getAd(Map<String, Object> map) {
        map.put("function", "9605");
        map.put("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        if(map.get("advertisingArea")==null|| TextUtils.isEmpty(map.get("advertisingArea").toString())){
            return;
        }
        //System.out.println("获取开屏信息");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetAds(resolveAdListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetAds(new ArrayList<AdModel>());
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void getAd(String type,String triggerPage,String advertisingArea) {
        Map<String,Object> map=new HashMap<>();
        map.put("function", "9605");
        map.put("type", type);
        map.put("triggerPage", triggerPage);
        map.put("advertisingArea", advertisingArea);
        map.put("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        if(map.get("advertisingArea")==null||TextUtils.isEmpty(map.get("advertisingArea").toString())){
            return;
        }
        //System.out.println("获取开屏信息");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetAds(resolveAdListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetAds(new ArrayList<AdModel>());
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void posAd(Map<String, Object> map) {
        map.put("function", "9606");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
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

    private List<AdModel> resolveAdListData(String obj) {
        List<AdModel> result = new ArrayList<>();
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
            Type type = new TypeToken<List<AdModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}