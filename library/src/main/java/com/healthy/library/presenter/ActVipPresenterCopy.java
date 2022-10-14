package com.healthy.library.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.ActVipContract;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.SpUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class ActVipPresenterCopy implements ActVipContract.Presenter {

    private Context mContext;

    public ActVipPresenterCopy(Context context) {
        mContext = context;
    }



    private ActVip resolveActVipData(String obj) {
        ActVip result = null;
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data").getJSONObject("Data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<ActVip>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private ActVip.VipShop resolveActVipShop(String obj) {
        ActVip.VipShop result = null;
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
            Type type = new TypeToken<ActVip.VipShop>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getVipActs(Map<String, Object> map,final GoodsBasketStore goodsBasketStore) {


    }

    @Override
    public void getVipShopDetail(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "YT_100003");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(null, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        ActVip.VipShop vipShop=resolveActVipShop(obj);
                        try {
                            SpUtils.store(mContext, SpKey.VIPSHOP,new Gson().toJson(vipShop));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }

                });
    }
}