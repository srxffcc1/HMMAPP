//package com.health.servicecenter.presenter;
//
//import android.content.Context;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import com.google.gson.reflect.TypeToken;
//import com.health.servicecenter.contract.ServiceGoodsDetailKickContract;
//import com.healthy.library.model.Goods2DetailKick;
//import com.healthy.library.model.GoodsBasketMisc;
//import com.healthy.library.model.GoodsSetAll;
//import com.healthy.library.model.GoodsTran;
//import com.healthy.library.model.RecommendList;
//import com.healthy.library.constant.Functions;
//import com.healthy.library.net.NoInsertStringObserver;
//import com.healthy.library.net.ObservableHelper;
//import com.healthy.library.net.StringObserver;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * @author Li
// * @date 2019/04/29 16:19
// * @des
// */
//
//public class ServiceGoodsDetailKickPresenter implements ServiceGoodsDetailKickContract.Presenter {
//
//    private Context mContext;
//    private ServiceGoodsDetailKickContract.View mView;
//
//    public ServiceGoodsDetailKickPresenter(Context context, ServiceGoodsDetailKickContract.View view) {
//        mContext = context;
//        mView = view;
//    }
//
//
//    @Override
//    public void getGoodsTran(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "9603");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.successGetGoodsTran(resolveTranData(obj));
//
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
//    }
//
//    private GoodsTran resolveTranData(String obj) {
//        GoodsTran result = null;
//        try {
//            JSONObject data = new JSONObject(obj).getJSONObject("data");
//            String userShopInfoDTOS = data.toString();
//            GsonBuilder builder = new GsonBuilder();
//            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                    return new Date(json.getAsJsonPrimitive().getAsLong());
//                }
//            });
//            Gson gson = builder.create();
//            Type type = new TypeToken<GoodsTran>() {
//            }.getType();
//            result = gson.fromJson(userShopInfoDTOS, type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public void addGoodsBasket(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "25013");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.successAddGoodsBasket(null);
//
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
//    }
//
//    @Override
//    public void getGoodsBasket(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "9500");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.successGetGoodsBasket(resolveGoodsBasketMiscData(obj));
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
//    }
//
//    private GoodsBasketMisc resolveGoodsBasketMiscData(String obj) {
//        GoodsBasketMisc result = null;
//        try {
//            JSONObject data=new JSONObject(obj).getJSONObject("data");
//            String userShopInfoDTOS=data.toString();
//            GsonBuilder builder = new GsonBuilder();
//            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                    return new Date(json.getAsJsonPrimitive().getAsLong());
//                }
//            });
//            Gson gson = builder.create();
//            Type type = new TypeToken<GoodsBasketMisc>() {
//            }.getType();
//            result=gson.fromJson(userShopInfoDTOS,type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public void getGoodsDetail(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "7051");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new StringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.successGetGoodsDetail(resolveDetailDataKick(obj));
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.successGetGoodsDetail(null);
//
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
//    }
//
//    @Override
//    public void getGoodsSet(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "9105");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.successGetGoodsSet(resolveSetData(obj));
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.successGetGoodsSet(null);
//
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
//    }
//
//
//
//    private List<GoodsSetAll> resolveSetData(String obj) {
//        List<GoodsSetAll> result = new ArrayList<>();
//        try {
//            JSONArray data = new JSONObject(obj).getJSONArray("data");
//            String userShopInfoDTOS = data.toString();
//            GsonBuilder builder = new GsonBuilder();
//            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                    return new Date(json.getAsJsonPrimitive().getAsLong());
//                }
//            });
//            Gson gson = builder.create();
//            Type type = new TypeToken<List<GoodsSetAll>>() {
//            }.getType();
//            result = gson.fromJson(userShopInfoDTOS, type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public void getGoodsRecommendUnder(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "9100");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.successGetGoodsRecommendUnder(resolveRecommendListData(obj));
//                    }
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.successGetGoodsRecommendUnder(null);
//
//                    }
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
//    }
//    private List<RecommendList> resolveRecommendListData(String obj) {
//        List<RecommendList> result = new ArrayList<>();
//        try {
//            JSONArray data = new JSONObject(obj).getJSONArray("data");
//            String userShopInfoDTOS = data.toString();
//            GsonBuilder builder = new GsonBuilder();
//            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                    return new Date(json.getAsJsonPrimitive().getAsLong());
//                }
//            });
//            Gson gson = builder.create();
//            Type type = new TypeToken<List<RecommendList>>() {
//            }.getType();
//            result = gson.fromJson(userShopInfoDTOS, type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i <result.size() ; i++) {
//            result.get(i).setType(2);
//        }
//        return result;
//    }
//    private Goods2DetailKick resolveDetailDataKick(String obj) {
//        Goods2DetailKick result = null;
//        try {
//            JSONObject data=new JSONObject(obj).getJSONObject("data");
//            String userShopInfoDTOS=data.toString();
//            GsonBuilder builder = new GsonBuilder();
//            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                    return new Date(json.getAsJsonPrimitive().getAsLong());
//                }
//            });
//            Gson gson = builder.create();
//            Type type = new TypeToken<Goods2DetailKick>() {
//            }.getType();
//            result=gson.fromJson(userShopInfoDTOS,type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public void getGoodsChose(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "9100");
//        map.put("position",3+"");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.successGetGoodsChose(resolveRecommendListData(obj));
//                    }
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.successGetGoodsChose(null);
//
//                    }
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
//    }
//
//    @Override
//    public void getGoodsRecommend(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "9100");
//        map.put("position",2+"");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.successGetGoodsRecommend(resolveRecommendListData(obj));
//                    }
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.successGetGoodsRecommend(null);
//
//                    }
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
//    }
//}
