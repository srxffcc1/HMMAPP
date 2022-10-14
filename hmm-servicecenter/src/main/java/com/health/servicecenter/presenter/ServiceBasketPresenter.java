package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.ServiceBasketContract;
import com.healthy.library.BuildConfig;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.GoodsBasketAll;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.RecommendList;
import com.healthy.library.constant.Functions;
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
import java.util.List;
import java.util.Map;


/**
 * @author Li
 * @date 2019/04/29 16:19
 * @des
 */

public class ServiceBasketPresenter implements ServiceBasketContract.Presenter {

    private Context mContext;
    private ServiceBasketContract.View mView;

    public ServiceBasketPresenter(Context context, ServiceBasketContract.View view) {
        mContext = context;
        mView = view;
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
    public void getBasketList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25015");
//        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetBasketList(resolveBasketData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getGoodsRecommend(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9119");
        if(map.get("shopId")==null){
            return;
        }
//        map.put("shopId",SpUtils.getValue(mContext,SpKey.CHOSE_SHOP));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        List<RecommendList> result=new ArrayList<>();
                            result.addAll(resolveRecommendListData(obj));
                        mView.successGetGoodsRecommend(result);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void updateGoods(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25016");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        mView.sucessUpdate();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void deleteGoods(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25014");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
//                        mView.sucessUpdate();
                        mView.sucessDelete();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private GoodsBasketAll resolveBasketData(String obj) {
        GoodsBasketAll result = null;
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
            Type type = new TypeToken<GoodsBasketAll>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
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
    public void getShopDetailOnly(Map<String, Object> map, final GoodsBasketStore goodsBasketStore) {
        map.put(Functions.FUNCTION, "101001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            ShopDetailModel shopDetailModel = resolveStoreData(obj);
                            goodsBasketStore.merchantType=shopDetailModel.merchantType;//商家属性 1异业商家 2合伙人商家
                            for (int i = 0; i < goodsBasketStore.getGoodsBasketCellAllList().size(); i++) {
                                goodsBasketStore.getGoodsBasketCellAllList().get(i).setIsSupportOverSold(shopDetailModel.isSupportOverSold);
                            }
                            goodsBasketStore.mchId=shopDetailModel.userId;
                            goodsBasketStore.setDepartID(shopDetailModel.getYtbDepartID());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mView.onSucessGetShopDetailOnly(goodsBasketStore);

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onSucessGetShopDetailOnly();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getGoodsActVips(Map<String, Object> map, final RecommendList recommendList) {
        if(BuildConfig.DEBUG){
            return;
        }
        map.put(Functions.FUNCTION, "YT_100002");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false,false,false,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if (recommendList.actVip == null) {
                            recommendList.actVip = resolveActVipData(obj);
                        }
                        mView.onSucessGetGoodsActVips();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
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

    private List<RecommendList> resolveRecommendListData(String obj) {
        List<RecommendList> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("goodsList");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<RecommendList>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setType(2);
        }
        return result;
    }
}
