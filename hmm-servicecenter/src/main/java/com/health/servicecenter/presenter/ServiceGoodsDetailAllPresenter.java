package com.health.servicecenter.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.ServiceGoodsDetailAllContract;
import com.healthy.library.LibApplication;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.AssemableTeam;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.DiscountTopModel;
import com.healthy.library.model.Goods2DetailKick;
import com.healthy.library.model.Goods2DetailPin;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.GoodsRank;
import com.healthy.library.model.GoodsSetAll;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.GoodsSpecLimit;
import com.healthy.library.model.GoodsTran;
import com.healthy.library.model.IntegralModel;
import com.healthy.library.model.MallGroupSimple;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.VipInfo;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.ObjUtil;
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

public class ServiceGoodsDetailAllPresenter implements ServiceGoodsDetailAllContract.Presenter {

    private Context mContext;
    private ServiceGoodsDetailAllContract.View mView;

    public ServiceGoodsDetailAllPresenter(Context context, ServiceGoodsDetailAllContract.View view) {
        mContext = context;
        mView = view;
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
                    }
                });
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
    public void getGoodsTran(Map<String, Object> map) {
        if(map.get("shopId")==null||TextUtils.isEmpty(map.get("shopId").toString())){
            return;
        }
        map.put(Functions.FUNCTION, "9603");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsTran(resolveTranData(obj));

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getGoodsActRule(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9112-3");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        String result = null;
                        try {
                            result = new JSONObject(obj).optString("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mView.successGetActRule(result);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private GoodsTran resolveTranData(String obj) {
        GoodsTran result = null;
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
            Type type = new TypeToken<GoodsTran>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void addGoodsBasket(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25013");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successAddGoodsBasket(null);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getGoodsBasket(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25012");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsBasket(resolveShopCartlData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
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

    @Override
    public void getGoodsDetail(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9202");
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
    public void getGoodsDetailWithCode(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9202-1");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsDetailWithCode(resolveDetailData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.successGetGoodsDetailWithCode(null);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getGoodsDetailKick(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7051");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsDetailKick(resolveDetailDataKick(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.successGetGoodsDetailKick(null);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getGoodsDetailKickSkuEx(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7053");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetSkuExList(resolveSkuListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetSkuExList(new SimpleArrayListBuilder<GoodsSpecDetail>());


                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void getGoodsDetailPinSkuEx(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9017");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetSkuExList(resolveSkuListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetSkuExList(new SimpleArrayListBuilder<GoodsSpecDetail>());

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private List<GoodsSpecDetail> resolveSkuListData(String obj) {
        List<GoodsSpecDetail> result = new ArrayList<>();
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
            Type type = new TypeToken<List<GoodsSpecDetail>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Goods2DetailKick resolveDetailDataKick(String obj) {
        Goods2DetailKick result = null;
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
            Type type = new TypeToken<Goods2DetailKick>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(result!=null){
            result.goodsDetails.marketingGoodsChildren=new ArrayList<>();
            try {
                GoodsDetail.GoodsChildren goodsChildren=new GoodsDetail.GoodsChildren(result.bargainInfoDTO.marketingGoodsChildDTOS.get(0));
                result.goodsDetails.marketingGoodsChildren.add(goodsChildren);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void getGoodsDetailPin(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9014");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsDetailPin(resolveDetailDataPin(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.successGetGoodsDetailPin(null);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private Goods2DetailPin resolveDetailDataPin(String obj) {
        Goods2DetailPin result = null;
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
            Type type = new TypeToken<Goods2DetailPin>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(result!=null){
            result.goodsDetails.marketingGoodsChildren=new ArrayList<>();
            try {
                GoodsDetail.GoodsChildren goodsChildren=new GoodsDetail.GoodsChildren(result.assembleDTO.marketingGoodsChildDTOS.get(0));
                result.goodsDetails.marketingGoodsChildren.add(goodsChildren);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void getGoodsSet(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9105");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsSet(resolveSetData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.successGetGoodsSet(null);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }


    private List<GoodsSetAll> resolveSetData(String obj) {
        List<GoodsSetAll> result = new ArrayList<>();
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
            Type type = new TypeToken<List<GoodsSetAll>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getGoodsRecommendUnder(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9119");
        if(map.get("shopId")==null){
            map.put("shopId", SpUtils.getValue(LibApplication.getAppContext(),SpKey.CHOSE_SHOP));
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsRecommendUnder(resolveRecommendListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.successGetGoodsRecommendUnder(null);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getTeamList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9015");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getSucessTeamList(resolveTeamListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
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

    @Override
    public void getGoodsLimitResult(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25023");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsSkuFinalResult(resolveLimitData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getIsNewAppMember() {
//        if(BuildConfig.DEBUG){
//            mView.onSucessIsNewAppMember(1);
//            return;
//        }
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "25021");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onSucessIsNewAppMember(new JSONObject(obj).getJSONObject("data").getInt("isNewAppMember"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessIsNewAppMember(0);
                    }
                });
    }

    @Override
    public void getVipInfo(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "20006");
        try {
            ActVip.VipShop vipShop= ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(), SpKey.VIPSHOP),ActVip.VipShop.class);
            map.put("ytbAppId",vipShop.ytbAppId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
        map.put("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        map.put("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onVipInfoSuccess(resolvePointData(obj));
                    }
                });
    }

    @Override
    public void getGoodsCouponList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "80001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.sucessGetCouponList(resolveCouponData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getGoodsActShop(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "marketing_9305");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new StringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.sucessGetActList(resolveActsData(obj));
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.sucessGetActList(null);
//                    }
//                });
        map.put(Functions.FUNCTION, "YT_100003");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.sucessGetActShop(resolveShopData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.sucessGetActShop(null);
                    }
                });
    }

    @Override
    public void getActVip(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "YT_100002");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetActVip(resolveActVipData(obj));
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
    private ActVip.VipShop resolveShopData(String obj) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
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

    private DiscountTopModel resolveActsData(String obj) {
        DiscountTopModel result = null;
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
            Type type = new TypeToken<DiscountTopModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private IntegralModel resolvePointData(String obj) {
        IntegralModel result = null;
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
            Type type = new TypeToken<IntegralModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private VipInfo resolveVipInfo(String obj) {
        VipInfo result = null;
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
            Type type = new TypeToken<VipInfo>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<CouponInfoZ> resolveCouponData(String obj) {
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


    private GoodsSpecLimit resolveLimitData(String obj) {
        GoodsSpecLimit result = null;
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
            Type type = new TypeToken<GoodsSpecLimit>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return result;
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

    private List<AssemableTeam> resolveTeamListData(String obj) {
        List<AssemableTeam> result = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(obj);
            String userShopInfoDTOS = data.getJSONObject("data").getJSONArray("items").toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<AssemableTeam>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
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

    private GoodsDetail resolveDetailData(String obj) {
        GoodsDetail result = null;
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
            Type type = new TypeToken<GoodsDetail>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getGoodsChose(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9119");
        map.put("type", 4 + "");
        if(map.get("shopId")==null){
            return;
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsChose(resolveRecommendListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.successGetGoodsChose(null);

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
        map.put("type", 3 + "");
        if(map.get("shopId")==null){
            return;
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsRecommend(resolveRecommendListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.successGetGoodsRecommend(null);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }
}
