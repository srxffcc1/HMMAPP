package com.health.tencentlive.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.tencentlive.contract.LiveShoppingBagDialogContract;
import com.health.tencentlive.model.JackpotList;
import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.businessutil.LocUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class LiveShoppingBagDialogPresenter implements LiveShoppingBagDialogContract.Presenter {

    private Context mContext;
    private LiveShoppingBagDialogContract.View mView;

    public LiveShoppingBagDialogPresenter(Context context, LiveShoppingBagDialogContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getLiveRoomGoodsList(final HashMap<String, Object> map) {
        List<String> stringList = (List<String>) map.get("liveActivityIds");
        if (stringList != null && stringList.size() > 0) {
            map.put(Functions.FUNCTION, "9291");
            ObservableHelper.createObservable(mContext, map)
                    .subscribe(new NoStringObserver(mView, mContext, false) {
                        @Override
                        protected void onGetResultSuccess(String obj) {
                            super.onGetResultSuccess(obj);
                            List<LiveVideoGoods> liveVideoGoodsList = resolveData(obj);
                            if (map.get("isEncore") != null && 1 == Integer.parseInt(map.get("isEncore").toString())) {
                                if (liveVideoGoodsList != null) {
                                    for (int i = 0; i < liveVideoGoodsList.size(); i++) {
                                        if (liveVideoGoodsList.get(i).isEncore == 0) {
                                            liveVideoGoodsList.remove(i);
                                            i--;
                                        }
                                    }
                                }

                            }
                            mView.getLiveRoomGoodsListSuccess(liveVideoGoodsList);
                        }

                        @Override
                        protected void onFinish() {
                            super.onFinish();
                            mView.onRequestFinish();
                        }
                    });
        } else {
            mView.getLiveRoomGoodsListSuccess(null);
        }

    }

    @Override
    public void getGoodsShopList(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "101004-1");
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetStoreListSuccess(resolveStoreListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    @Override
    public void getMerchantGoodsShopList(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "101004-1");
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetMerchantStoreListSuccess(resolveStoreListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    @Override
    public void getJackpotList(HashMap<String, Object> map, final int type) {
        map.put(Functions.FUNCTION, "live-help-100");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getSuccessJackpotList(resolveJackpotData(obj), null, type);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.showDataErr();
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.showDataErr();
                    }
                });
    }

    private List<ShopDetailModel> resolveStoreListData(String obj) {
        List<ShopDetailModel> shopDetailModel = new ArrayList<>();
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
            Type type = new TypeToken<List<ShopDetailModel>>() {
            }.getType();
            shopDetailModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopDetailModel;
    }

    private List<LiveVideoGoods> resolveData(String obj) {
        List<LiveVideoGoods> result = new ArrayList<>();
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
            Type type = new TypeToken<List<LiveVideoGoods>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<JackpotList> resolveJackpotData(String obj) {
        List<JackpotList> result = null;
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
            Type type = new TypeToken<List<JackpotList>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}