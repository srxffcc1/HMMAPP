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
import com.health.discount.contract.PointHomeContract;
import com.health.discount.model.PointOption;
import com.health.discount.model.PointTab;
import com.health.discount.model.UserPoint;
import com.healthy.library.LibApplication;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.IntegralModel;
import com.healthy.library.model.LotteryModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.JsonUtils;
import com.healthy.library.businessutil.LocUtil;
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

public class PointHomePresenter implements PointHomeContract.Presenter {
    private Context mContext;
    private PointHomeContract.View mView;

    public PointHomePresenter(Context context, PointHomeContract.View view) {
        mContext = context;
        mView = view;
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

    private List<PointTab> resolveTabListData(String obj) {
        List<PointTab> result = new ArrayList<>();
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
            Type type = new TypeToken<List<PointTab>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private List<PointOption> resolveOptionListData(String obj) {
        List<PointOption> result = new ArrayList<>();
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
            Type type = new TypeToken<List<PointOption>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void getPointTab(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onPointTabSuccess(resolveTabListData(obj));
                    }
                });
    }

    @Override
    public void getPointOption(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onPointOptionSuccess(resolveOptionListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onPointOptionSuccess(null);

                    }
                });
    }

    @Override
    public void getRecommend(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onPointRecommendSuccess(resolveRecommendListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        //System.out.println("系统出错");
                        mView.onPointRecommendSuccess(null);

                    }
                });
    }

    private List<PointTab.PointGoods> resolveRecommendListData(String obj) {
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
    public void getUserInfo() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, Functions.FUNCTION_USER_INFO);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            UserPoint model = new UserPoint();
                            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
                            model.setMemberSex(JsonUtils.getInt(jsonObject, "memberSex", 2));
                            model.setStatus(JsonUtils.getInt(jsonObject, "status"));
                            model.setFaceUrl(JsonUtils.getString(jsonObject, "faceUrl"));
                            model.setDateContent(JsonUtils.getString(jsonObject, "dateContent"));
                            model.setNickname(JsonUtils.getString(jsonObject, "nickName"));
                            model.setIncome(FormatUtils.formatRewardMoney(
                                    JsonUtils.getString(jsonObject, "incomeBalance", "0")));
                            model.setBalance(FormatUtils.formatRewardMoney(
                                    JsonUtils.getString(jsonObject, "virtualBalance", "0")));
                            mView.onGetUserInfoSuccess(model);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void getBannerTop(Map<String, Object> map) {
        map.put("function", "9605");
        map.put("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        if(map.get("advertisingArea")==null|| TextUtils.isEmpty(map.get("advertisingArea").toString())){
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
    public void getLotteryInfo(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "lottery_6004");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onLotteryInfoSuccess(resolveLotteryData(obj));
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取积分抽奖活动信息
     *
     * @param obj
     * @return
     */
    private LotteryModel resolveLotteryData(String obj) {
        LotteryModel result = new LotteryModel();
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
            Type type = new TypeToken<LotteryModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
