//package com.health.discount.presenter;
//
//import android.content.Context;
//import android.text.TextUtils;
//
//import com.amap.api.location.AMapLocation;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import com.google.gson.reflect.TypeToken;
//import com.health.discount.contract.ActHomeContract;
//import com.healthy.library.constant.Functions;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.model.ActGroup;
//import com.healthy.library.model.ActKick;
//import com.healthy.library.model.ActKill;
//import com.healthy.library.model.ActTabInfo;
//import com.healthy.library.model.AdModel;
//import com.healthy.library.model.LocVip;
//import com.healthy.library.net.NoStringObserver;
//import com.healthy.library.net.ObservableHelper;
//import com.healthy.library.net.StringObserver;
//import com.healthy.library.builder.SimpleArrayListBuilder;
//import com.healthy.library.businessutil.ListUtil;
//import com.healthy.library.businessutil.LocUtil;
//import com.healthy.library.utils.SpUtils;
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
//public class ActHomePresenter implements ActHomeContract.Presenter {
//    private Context mContext;
//    private ActHomeContract.View mView;
//
//    public ActHomePresenter(Context context, ActHomeContract.View view) {
//        mContext = context;
//        mView = view;
//    }
//
//
//    @Override
//    public void getActKick(Map<String, Object> map) {
//        map.put("addressProvince", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
//        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE));
//        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE));
//        map.put("pageSize", "10");
//        map.put("currentPage", "1");
//        map.put("type", "1");
//        map.put(Functions.FUNCTION, "7050");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new StringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.onSucessGetActKick(resolveKickListData(obj));
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
//                        mView.onSucessGetActKick(null);
//                    }
//                });
//    }
//
//    @Override
//    public void getActPin(Map<String, Object> map) {
//        map.put("addressProvince", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
//        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE));
//        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE));
//        map.put("pageSize", "10");
//        map.put("currentPage", "1");
//        map.put("type", "1");
//        map.put(Functions.FUNCTION, "9010");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new StringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.onSucessGetActGroup(resolveGroupListData(obj));
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
//                        mView.onSucessGetActGroup(null);
//                    }
//                });
//    }
//
//    @Override
//    public void getActKill(Map<String, Object> map) {
////        map.put("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
////        map.put("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
////        map.put("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
//        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
//        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
//        map.put("marketingType", "3");
//        map.put("pageSize", "4");
//        map.put("pageNum", "1");
//        map.put(Functions.FUNCTION, "9112");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new StringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.onSucessGetActKill(resolveKillListData(obj));
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
//                        mView.onSucessGetActKill(null);
//                    }
//                });
//    }
//
//    @Override
//    public void getActLocVip(final Map<String, Object> map) {
//        map.put("function", "100001");
//        map.put("longitude",LocUtil.getLongitude(mContext,SpKey.LOC_ORG));
//        map.put("latitude",LocUtil.getLatitude(mContext,SpKey.LOC_ORG));
//        map.put("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_ORG));
//        map.put("city", LocUtil.getCityNo(mContext, SpKey.LOC_ORG));
//        map.put("district", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG));
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new StringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        LocVip locVip = resolveLocVipData(obj);
//                        if(TextUtils.isEmpty(SpUtils.getValue(mContext,SpKey.CHOSE_SHOP))){//当前没有选
//
//                            AMapLocation aMapLocation = LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE);
//                            if(aMapLocation==null){
//                                aMapLocation=new AMapLocation("location");
//                            }
//                            aMapLocation.setAdCode(locVip.getAllMerchantWithMe().get(0).getNearShop().district);//默认市级加5 得到模糊区县
//                            aMapLocation.setProvince(locVip.getAllMerchantWithMe().get(0).getNearShop().provinceName);
//                            aMapLocation.setCity(locVip.getAllMerchantWithMe().get(0).getNearShop().cityName);
//                            aMapLocation.setDistrict(locVip.getAllMerchantWithMe().get(0).getNearShop().districtName);
//                            LocUtil.storeLocationChose(mContext, aMapLocation);
//                            LocUtil.setNowShop(locVip.getAllMerchantWithMe().get(0).getNearShop());
//                        }else {
//                            if(!ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(locVip.getAllMerchantShopWithMe(), new SimpleArrayListBuilder.ObjectIteraor<LocVip.Local.MerchantsShop>() {
//                                @Override
//                                public String getDesObj(LocVip.Local.MerchantsShop o) {
//                                    return o.shopId;
//                                }
//                            }),SpUtils.getValue(mContext,SpKey.CHOSE_SHOP))){//加判下历史选择是不是当前可选
//                                AMapLocation aMapLocation = LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE);
//                                if(aMapLocation==null){
//                                    aMapLocation=new AMapLocation("location");
//                                }
//                                aMapLocation.setAdCode(locVip.getAllMerchantWithMe().get(0).getNearShop().district);//默认市级加5 得到模糊区县
//                                aMapLocation.setProvince(locVip.getAllMerchantWithMe().get(0).getNearShop().provinceName);
//                                aMapLocation.setCity(locVip.getAllMerchantWithMe().get(0).getNearShop().cityName);
//                                aMapLocation.setDistrict(locVip.getAllMerchantWithMe().get(0).getNearShop().districtName);
//                                LocUtil.storeLocationChose(mContext, aMapLocation);
//                                LocUtil.setNowShop(locVip.getAllMerchantWithMe().get(0).getNearShop());
//                            }
//                        }
//                        mView.onSucessGetActMan(true);
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
//                        getActBlockMan(map);
//                    }
//                });
//    }
//
//    @Override
//    public void getActBlockMan(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "9110");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new StringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.onSucessGetActMan(true);
//                    }
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.onSucessGetActMan(false);
//                    }
//                });
//    }
//
//    private LocVip resolveLocVipData(String obj) {
//        LocVip result = new LocVip();
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
//            Type type = new TypeToken<LocVip>() {
//            }.getType();
//            result = gson.fromJson(userShopInfoDTOS, type);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public void getActTabs(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "9108");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new StringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.onSucessGetActTabs(resolveListData(obj));
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
//                        mView.onSucessGetActTabs(resolveListData(null));
//                    }
//                });
//    }
//
//    @Override
//    public void getBannerTop(Map<String, Object> map) {
//        map.put("function", "9605");
//        map.put("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
//        //System.out.println("获取开屏信息");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoStringObserver(mView, mContext,
//                        false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.onSucessGetTopAds(resolveAdListData(obj));
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.onSucessGetTopAds(new ArrayList<AdModel>());
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                    }
//                });
//    }
//
//    private List<AdModel> resolveAdListData(String obj) {
//        List<AdModel> result = new ArrayList<>();
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
//            Type type = new TypeToken<List<AdModel>>() {
//            }.getType();
//            result = gson.fromJson(userShopInfoDTOS, type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    private List<ActKick> resolveKickListData(String obj) {
//        List<ActKick> result = new ArrayList<>();
//        try {
//            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("items");
//            String userShopInfoDTOS = data.toString();
//            GsonBuilder builder = new GsonBuilder();
//            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                    return new Date(json.getAsJsonPrimitive().getAsLong());
//                }
//            });
//            Gson gson = builder.create();
//            Type type = new TypeToken<List<ActKick>>() {
//            }.getType();
//            result = gson.fromJson(userShopInfoDTOS, type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    private List<ActKill> resolveKillListData(String obj) {
//        List<ActKill> result = new ArrayList<>();
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
//            Type type = new TypeToken<List<ActKill>>() {
//            }.getType();
//            result = gson.fromJson(userShopInfoDTOS, type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    private List<ActGroup> resolveGroupListData(String obj) {
//        List<ActGroup> result = new ArrayList<>();
//        try {
//            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("items");
//            String userShopInfoDTOS = data.toString();
//            GsonBuilder builder = new GsonBuilder();
//            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                    return new Date(json.getAsJsonPrimitive().getAsLong());
//                }
//            });
//            Gson gson = builder.create();
//            Type type = new TypeToken<List<ActGroup>>() {
//            }.getType();
//            result = gson.fromJson(userShopInfoDTOS, type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public void getBannerCenter(Map<String, Object> map) {
//        map.put("function", "9605");
//        map.put("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
//        //System.out.println("获取开屏信息");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoStringObserver(mView, mContext,
//                        false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.onSucessGetCenterAds(resolveAdListData(obj));
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.onSucessGetCenterAds(new ArrayList<AdModel>());
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                    }
//                });
//    }
//
//    @Override
//    public void getBannerBottom(Map<String, Object> map) {
//        map.put("function", "9605");
//        map.put("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
//        //System.out.println("获取开屏信息");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoStringObserver(mView, mContext,
//                        false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.onSucessGetBottomAds(resolveAdListData(obj));
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.onSucessGetBottomAds(new ArrayList<AdModel>());
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                    }
//                });
//    }
//
//    private List<ActTabInfo> resolveListData(String obj) {
//        List<ActTabInfo> result = new ArrayList<>();
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
//            Type type = new TypeToken<List<ActTabInfo>>() {
//            }.getType();
//            result = gson.fromJson(userShopInfoDTOS, type);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//}
