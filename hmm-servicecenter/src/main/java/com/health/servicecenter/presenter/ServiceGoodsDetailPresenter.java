package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.ServiceGoodsDetailContract;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.GoodsRank;
import com.healthy.library.model.MallGroupSimple;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.net.NoInsertStringObserver;
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


/**
 * @author Li
 * @date 2019/04/29 16:19
 * @des
 */

public class ServiceGoodsDetailPresenter implements ServiceGoodsDetailContract.Presenter {

    private Context mContext;
    private ServiceGoodsDetailContract.View mView;

    public ServiceGoodsDetailPresenter(Context context, ServiceGoodsDetailContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getGoodsDetail(Map<String, Object> map) {
        if(map==null){
            return;
        }
        map.put(Functions.FUNCTION, "9292");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsDetail(resolveDetailData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.successGetGoodsDetail(null);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getGoodsDetailNormal(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9202");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsDetailNormal(resolveDetailData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.successGetGoodsDetailNormal(null);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private ShopDetailModel resolveStoreData(String obj) {
        ShopDetailModel shopDetailModel = null;
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
            Type type = new TypeToken<ShopDetailModel>() {
            }.getType();
            shopDetailModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopDetailModel;
    }
    @Override
    public void getStoreDetial(String shopId) {
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", shopId);
        map.put(Functions.FUNCTION, "101001");
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetStoreDetialSuccess(resolveStoreData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onGetStoreDetialSuccess(null);
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
    @Override
    public void getStoreList(String shopId) {
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", shopId);
        map.put(Functions.FUNCTION, "101004-1");
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
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
    public void getGoodsRankList() {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "9202-rlist");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetGoodsRankListSuccess(resolveRankListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    private List<GoodsRank> resolveRankListData(String obj) {
        List<GoodsRank> shopDetailModel = new ArrayList<>();
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
            Type type = new TypeToken<List<GoodsRank>>() {
            }.getType();
            shopDetailModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopDetailModel;
    }

    private GoodsDetail resolveDetailData(String obj) {
        GoodsDetail result = null;
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<GoodsDetail>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getGroupAlreadyList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9013");
        ////System.out.println("查看拼团");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetGroupAlreadyList(resolveManListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();

                    }
                });
    }
    private List<MallGroupSimple> resolveManListData(String obj) {
        List<MallGroupSimple> result = new ArrayList<>();
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
            Type type = new TypeToken<List<MallGroupSimple>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            ////System.out.println("错误:" + obj);
            e.printStackTrace();
        }
        return result;
    }
}
