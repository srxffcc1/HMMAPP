package com.health.discount.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.discount.contract.ActHomeZContract;
import com.health.discount.model.PointTab;
import com.health.discount.model.SeckillTab;
import com.healthy.library.LibApplication;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.ActGoodsCityItem;
import com.healthy.library.model.ActGroup;
import com.healthy.library.model.ActKick;
import com.healthy.library.model.ActKill;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.HttpTmpResult;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.model.LocVip;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.model.MainIconModel;
import com.healthy.library.model.MainMenuModel;
import com.healthy.library.model.MainSearchModel;
import com.healthy.library.model.ShopInfo;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.presenter.ChangeVipPresenter;
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

public class ActHomeZPresenter implements ActHomeZContract.Presenter {
    private Context mContext;
    private ActHomeZContract.View mView;

    public ActHomeZPresenter(Context context, ActHomeZContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getSeachRecommend(Map<String, Object> map) {

    }

    @Override
    public void getSeachTipList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "101005");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        List<MainSearchModel> result = resolveSeachTipListData(obj);
                        List<MainSearchModel> mainSearchModelList = SpUtils.resolveHistoryArrayData(SpUtils.getValue(mContext, SpKey.USE_SEACHTIP), MainSearchModel.class);
                        if (mainSearchModelList.size() < 10) {
                            mainSearchModelList.addAll(result);
                        }
                        mView.onSucessGetSeachTipList(mainSearchModelList);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        List<MainSearchModel> mainSearchModelList = SpUtils.resolveHistoryArrayData(SpUtils.getValue(mContext, SpKey.USE_SEACHTIP), MainSearchModel.class);
                        mView.onSucessGetSeachTipList(mainSearchModelList);
                    }
                });
    }

    private List<MainSearchModel> resolveSeachTipListData(String obj) {
        List<MainSearchModel> result = new ArrayList<>();
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
            Type type = new TypeToken<List<MainSearchModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getPostContent(Map<String, Object> map) {

    }

    @Override
    public void getFucList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "101006");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetFucList(resolveFucListData(obj), resolveFucIconData(obj), resolveAdTopListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onGetFucList(null, null, null);
                    }
                });
    }

    private List<MainMenuModel> resolveFucListData(String obj) {
        List<MainMenuModel> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("subnavList");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<MainMenuModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private MainIconModel resolveFucIconData(String obj) {
        MainIconModel result = null;
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
            Type type = new TypeToken<MainIconModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getAdvBlock(Map<String, Object> map) {
        map.put("function", "9605");
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
                        mView.onSucessGetAdvBlock(resolveAdListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetAdvBlock(new ArrayList<AdModel>());
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void getKillList(final Map<String, Object> map) {
//        map.put("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
//        map.put("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
//        map.put("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        map.put("marketingType", "3");
        map.put("pageSize", "4");
        map.put("pageNum", "1");
        map.put(Functions.FUNCTION, "9112");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        List<ActKill> list=resolveKillListData(obj);
                        if(!"1".equals(map.get("historyQuery"))&&list.size()<=0){
                            getKillList(new SimpleHashMapBuilder<String, Object>().putMap(map).puts("marketingId",null).puts("historyQuery","1"));
                            return;
                        }
                        mView.onSucessGetActKill(resolveKillListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        if(!"1".equals(map.get("historyQuery"))){
                            getKillList(new SimpleHashMapBuilder<String, Object>().putMap(map).puts("marketingId",null).puts("historyQuery","1"));
                            return;
                        }
                        mView.onSucessGetActKill(null);
                    }
                });
    }

    private List<ActKill> resolveKillListData(String obj) {
        List<ActKill> result = new ArrayList<>();
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
            Type type = new TypeToken<List<ActKill>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getPinList(Map<String, Object> map) {
        map.put("addressProvince", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE));
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE));
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put("pageSize", "10");
        map.put("currentPage", "1");
        map.put("type", "1");
        map.put("faceUrlNum", "6");//获取头像的数量
        map.put(Functions.FUNCTION, "9010");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetActGroup(resolveGroupListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetActGroup(null);
                    }
                });
    }

    private List<ActGroup> resolveGroupListData(String obj) {
        List<ActGroup> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("items");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ActGroup>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getKickList(Map<String, Object> map) {
        map.put("addressProvince", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE));
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE));
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put("pageSize", "10");
        map.put("currentPage", "1");
        map.put("type", "1");
        map.put("faceUrlNum", "6");//获取头像的数量
        map.put(Functions.FUNCTION, "7050");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetActKick(resolveKickListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetActKick(null);
                    }
                });
    }

    private List<ActKick> resolveKickListData(String obj) {
        List<ActKick> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("items");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ActKick>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getPointList(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onPointRecommendSuccess(resolveRecommendPointListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        //System.out.println("系统出错");
                        mView.onPointRecommendSuccess(null);

                    }
                });
    }

    private List<PointTab.PointGoods> resolveRecommendPointListData(String obj) {
        List<PointTab.PointGoods> result = new ArrayList<>();
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
            Type type = new TypeToken<List<PointTab.PointGoods>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void getVideoList(Map<String, Object> map) {
        if (TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))) {
            return;
        }
        map.put("function", "9133");
        map.put("isDelete", "0");
        map.put("statusList", "1,2,4".split(","));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessMainGetLiveList(resolveVideoListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessMainGetLiveList(null);
                    }
                });
    }

    private List<LiveVideoMain> resolveVideoListData(String obj) {
        List<LiveVideoMain> result = new ArrayList<>();
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
            Type type = new TypeToken<List<LiveVideoMain>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

//    @Override
//    public void getAdvCenter(Map<String, Object> map) {
//
//    }

    @Override
    public void getBlockList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "101007");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetBlockList(resolveBlockListData(obj));
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

    private List<MainBlockModel> resolveBlockListData(String obj) {
        List<MainBlockModel> result = new ArrayList<>();
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
            Type type = new TypeToken<List<MainBlockModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void getBottomActTabs(Map<String, Object> map) {

    }

    @Override
    public void getAdvBottom(Map<String, Object> map) {

    }

    @Override
    public void getActBlockMan(Map<String, Object> map) {

    }

    boolean hasInit = false;

    @Override
    public void getActLocVip(final Map<String, Object> map) {
        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)) && !hasInit) {
            mView.onSucessGetActMan(true);
            hasInit = true;
            new ChangeVipPresenter(mContext).getLocVip(map);
            return;
        }
        HttpTmpResult httpTmpResult= ObjUtil.getObj(SpUtils.getValue(mContext,"fun100001"),HttpTmpResult.class);
        if( httpTmpResult!=null){//门店列表处于有效期
            LocVip locVip = resolveLocVipData(httpTmpResult.result);
            LocUtil.getRealShop(locVip);
            mView.onSucessGetActMan(true);
            return;
        }
        map.put("function", "100001");
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        map.put("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_ORG));
        map.put("city", LocUtil.getCityNo(mContext, SpKey.LOC_ORG));
        map.put("district", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        LocVip locVip = resolveLocVipData(obj);
                        LocUtil.getRealShop(locVip);
                        mView.onSucessGetActMan(true);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        getActBlockMan(map);
                    }
                });
    }

    @Override
    public void getBannerTop(Map<String, Object> map) {
        map.put("function", "9605");
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
                        mView.onSucessGetTopAds(resolveAdListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetTopAds(new ArrayList<AdModel>());
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void getBannerTopTop(Map<String, Object> map) {
        map.put("function", "9605");
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
                        mView.onSucessGetTopTopAds(resolveAdListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetTopAds(new ArrayList<AdModel>());
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void getBannerCenter(Map<String, Object> map) {
        map.put("function", "9605");
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
                        mView.onSucessGetCenterAds(resolveAdListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetCenterAds(new ArrayList<AdModel>());
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void getBannerBottom(Map<String, Object> map) {
        map.put("function", "9605");
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
                        mView.onSucessGetBottomAds(resolveAdListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetBottomAds(new ArrayList<AdModel>());
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void getActShopGoodsList(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetList(resolveTabListData(obj), resolveShopData(obj));
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

    @Override
    public void getActKillHistoryList(Map<String, Object> map) {
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        map.put("marketingType", "3");
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put(Functions.FUNCTION, "marketing_9307");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetActKillHistoryList(resolveHistoryListData(obj));
                    }
                });
    }

    private SeckillTab resolveHistoryListData(String obj) {
        SeckillTab result = null;
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
            Type type = new TypeToken<SeckillTab>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void getZxingCode() {
        if(TextUtils.isEmpty(SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC))){
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "20016");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        map.put("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
        map.put("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        map.put("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false, true, true, false, false, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        TokerWorkerInfoModel tokerWorkerInfoModel = resolveTokerData(obj);
                        if (tokerWorkerInfoModel != null) {
                            if (tokerWorkerInfoModel.tokerWorker != null) {
                                mView.getZxingCode(tokerWorkerInfoModel.tokerWorker.referralCode);
                                SpUtils.store(mContext, SpKey.GETTOKEN, tokerWorkerInfoModel.tokerWorker.referralCode);
                            } else {
                                try {
//                                    getZxingBitmap(tokerWorkerInfoModel.bindingList.get(0).bindingTokerWorker.referralCode);
//                                    mView.getZxingCode(tokerWorkerInfoModel.bindingList.get(0).bindingTokerWorker.referralCode);

                                    mView.getZxingCode("");
                                } catch (Exception e) {
                                    mView.getZxingCode("");
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            mView.getZxingCode("");
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.getZxingCode("");
                    }
                });
    }

    private TokerWorkerInfoModel resolveTokerData(String obj) {
        TokerWorkerInfoModel result = null;
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
            Type type = new TypeToken<TokerWorkerInfoModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private LocVip resolveLocVipData(String obj) {
        LocVip result = new LocVip();
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
            Type type = new TypeToken<LocVip>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<AdModel> resolveAdTopListData(String obj) {
        List<AdModel> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("navCarouselList");
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<ActGoodsCityItem> resolveTabListData(String obj) {
        List<ActGoodsCityItem> result = new ArrayList<>();
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
            Type type = new TypeToken<List<ActGoodsCityItem>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
//            e.printStackTrace();
        }

        return result;
    }

    private ShopInfo resolveShopData(String obj) {
        ShopInfo result = null;
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data").getJSONObject("shopInfo");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<ShopInfo>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
