package com.healthy.library.presenter;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.LibApplication;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.MineContract;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.CouponInfo;
import com.healthy.library.model.MineFans;
import com.healthy.library.model.OrderInfo;
import com.healthy.library.model.OrderNum;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.model.VipCard;
import com.healthy.library.model.VipInfo;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.JsonUtils;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 10:29
 * @des
 */
public class MinePresenter implements MineContract.Presenter {

    private Context mContext;
    private MineContract.View mView;

    public MinePresenter(Context context, MineContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getUserFans(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7008");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetUserFanSucess(resolveFanData(obj));
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

    private MineFans resolveFanData(String obj) {
        MineFans result = new MineFans();
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
            Type type = new TypeToken<MineFans>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, Functions.FUNCTION_USER_INFO);
        map.put("merchantId",SpUtils.getValue(mContext,SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            UserInfoModel model = new UserInfoModel();
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
                            model.setBadgeId(JsonUtils.getString(jsonObject,"badgeId"));
                            model.setBadgeType(JsonUtils.getInt(jsonObject,"badgeType"));
                            model.setBabyName(JsonUtils.getString(jsonObject,"badgeName"));
                            mView.onGetUserInfoSuccess(model);
                        } catch (Exception e) {
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
    public void getOrderInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "60003");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        mCommonViewModel.getModel().setValue(obj);
//                        mCommonViewModel.getModel().observe(mActivity, mObserver);
                        mView.onGetOrderInfoSuccess(resolveOrderData(obj));
                    }
                });
    }

    @Override
    public void getCouponInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "7039");
        map.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)) + "");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        mCommonViewModel.getModel().setValue(obj);
//                        mCommonViewModel.getModel().observe(mActivity, mObserver);
                        mView.onGetCouponSucess(resolveCouponData(obj));
                    }
                });
    }

    @Override
    public void getVipInfo(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "20002");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
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
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onVipInfoSuccess(resolveVipInfo(obj));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onVipInfoSuccess(null);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onVipInfoSuccess(null);
                    }
                });
    }

    @Override
    public void getVipCards(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "20003");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
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
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onVipCardSuccess(resolveVipCardsData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onVipCardSuccess(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onVipCardSuccess(null);
                    }
                });
    }

    @Override
    public void getOrderNum() {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "25017");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetOrderNumSuccess(resolveOrderNumData(obj));
                    }
                });
    }

    private CouponInfo resolveCouponData(String obj) {
        CouponInfo result = null;
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
            Type type = new TypeToken<CouponInfo>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private OrderInfo resolveOrderData(String obj) {
        OrderInfo result = null;
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
            Type type = new TypeToken<OrderInfo>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private OrderNum resolveOrderNumData(String obj) {
        OrderNum result = null;
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
            Type type = new TypeToken<OrderNum>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<VipCard> resolveVipCardsData(String obj) {
        List<VipCard> result = new ArrayList<>();
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
            Type type = new TypeToken<List<VipCard>>() {
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
}
