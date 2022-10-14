package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.HotGoodsContract;
import com.healthy.library.model.HotGoodsList;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.SpUtils;

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

public class HotGoodsPresenter implements HotGoodsContract.Presenter {

    private Context mContext;
    private HotGoodsContract.View mView;

    public HotGoodsPresenter(Context context, HotGoodsContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getGoodsList(Map<String, Object> map) {
        map.put("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
        map.put("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        map.put("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE));
        map.put(Functions.FUNCTION, "9100");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetGoodsListSuccess(resolveStoreData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void addShopCat(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25013");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.successAddShopCat(new JSONObject(obj).getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getShopCart() {
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put(Functions.FUNCTION, "25012");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetShopCartSuccess(resolveShopCartlData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private List<HotGoodsList> resolveStoreData(String obj) {
        List<HotGoodsList> result = new ArrayList<>();
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
            Type type = new TypeToken<List<HotGoodsList>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

    }

    private ShopCartModel resolveShopCartlData(String obj) {
        ShopCartModel result = null;
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
            Type type = new TypeToken<ShopCartModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
