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
import com.healthy.library.model.AdModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.FrameActivityManager;

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

public class AdPresenterCopy  {

    private Context mContext;

    public AdPresenterCopy(Context context) {
        mContext = context;
        if(mContext == null){
            mContext = FrameActivityManager.instance().topActivity();
        }
    }

    public void getAd(Map<String, Object> map) {
        map.put("function", "9605");
        map.put("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        if(map.get("advertisingArea")==null|| TextUtils.isEmpty(map.get("advertisingArea").toString())){
            return;
        }
        //System.out.println("获取开屏信息");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(null, mContext,
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

    public void posAd(Map<String, Object> map) {
        map.put("function", "9606");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(null, mContext,
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
    public void posAd(String id) {
        Map<String,Object> map=new HashMap<>();
        map.put("function", "9606");
        map.put("id",id);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(null, mContext,
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