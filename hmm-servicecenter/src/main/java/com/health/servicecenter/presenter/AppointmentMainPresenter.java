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
import com.health.servicecenter.contract.AppointmentMainContract;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.AppointmentListItemModel;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.model.AppointmentTimeSettingModel;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;
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
 * @author: user
 * @date: 2021/3/31
 */
public class AppointmentMainPresenter implements AppointmentMainContract.Presenter {

    private Context mContext;
    private AppointmentMainContract.View mView;

    public AppointmentMainPresenter(Context context, AppointmentMainContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 门店项目列表
     *
     * @param map
     */
    @Override
    public void getMainList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9801");
        map.put("pageSize", "10");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        //服务状态（1已上架，2已下架）
        map.put("status", "1");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetMainListSuccess(resolveStoreListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onRequestFinish();
                    }
                });
    }

    /**
     * 获取门店详情数据
     *
     * @param map
     */
    @Override
    public void getStoreDetail(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "101001");
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        //mView.onGetStoreDetialSuccess(resolveStoreData(obj));
                        try {
                            JSONObject data = new JSONObject(obj)
                                    .getJSONObject("data");
                            String merchantBrand = optString(data, "merchantBrand");
                            if (!TextUtils.isEmpty(merchantBrand)) {
                                SpUtils.store(mContext, SpKey.SHOP_BRAND, merchantBrand);
                            }
                            mView.onGetStoreDetailSuccess(resolveStoreData(obj));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    /**
     * 门店服务详情
     *
     * @param map
     */
    @Override
    public void getMainDetail(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9802");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetMainDetailSuccess(resolveStoreDetailData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onRequestFinish();
                    }
                });
    }

    /**
     * 添加服务预约单
     */
    @Override
    public void addService(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9803-1");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);

                        try {
                            long data = new JSONObject(obj).getJSONObject("data").getLong("id");
                            String payOrderId = new JSONObject(obj).getJSONObject("data").optString("payOrderId");
                            mView.onAddServiceSuccess(data, payOrderId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onRequestFinish();
                    }
                });
    }

    /**
     * 查询门店预约服务配置
     *
     * @param map
     */
    @Override
    public void getSettingTimeList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9810");

        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetTimeSettingSuccess(resolveStoreTimeSettingData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onRequestFinish();
                    }
                });
    }

    /**
     * 获取预约单预支付单信息
     *
     * @param map
     */
    @Override
    public void getPayInfo(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "9807");
        map.put(Functions.FUNCTION, "25001-2");
        //新增 使用的支付配置字段
        map.put("payConfigKey", "app");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetPayInfoSuccess(resolvePayInfo(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onRequestFinish();
                    }
                });
    }

    //避免解析奖 null 解析成 "null"
    public String optString(JSONObject json, String key) {
        if (json.isNull(key))
            return "";
        else
            return json.optString(key, null);
    }

    private List<AppointmentMainItemModel> resolveStoreListData(String obj) {
        List<AppointmentMainItemModel> newMainItemModel = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            String userShopInfoDTOS = list.toString();

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<AppointmentMainItemModel>>() {
            }.getType();
            newMainItemModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newMainItemModel;
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

    private AppointmentMainItemModel resolveStoreDetailData(String obj) {
        AppointmentMainItemModel newMainItemModel = null;
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
            Type type = new TypeToken<AppointmentMainItemModel>() {
            }.getType();
            newMainItemModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newMainItemModel;
    }

    private AppointmentTimeSettingModel resolveStoreTimeSettingData(String obj) {
        AppointmentTimeSettingModel timeSettingModel = null;
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
            Type type = new TypeToken<AppointmentTimeSettingModel>() {
            }.getType();
            timeSettingModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return timeSettingModel;
    }

    /**
     * 解析支付信息
     *
     * @param obj 支付信息字符串
     * @return 支付信息map
     */
    private Map<String, Object> resolvePayInfo(String obj) {
        Map<String, Object> payInfoMap = new HashMap<>(5);
        try {
            JSONObject jsonObject = new JSONObject(obj);
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            String payType = JsonUtils.getString(jsonObject, "payType");
            payInfoMap.put("payType", payType);
            if (Constants.PAY_IN_A_LI.equals(payType)) {
//                payInfoMap.put("ali", JsonUtils.getString(jsonObject, "aliPayContent"));
                payInfoMap.put("payInfo", JsonUtils.getString(jsonObject, "payInfo"));
            } else if (Constants.PAY_IN_WX.equals(payType)) {
                JSONObject wxObj = new JSONObject(JsonUtils.getString(jsonObject, "payContent"));
                payInfoMap.put("partnerId", JsonUtils.getString(wxObj, "mch_id"));
                payInfoMap.put("prepayId", JsonUtils.getString(wxObj, "prepay_id"));
                payInfoMap.put("packageValue", "Sign=WXPay");
                payInfoMap.put("nonceStr", JsonUtils.getString(wxObj, "nonce_str"));
                payInfoMap.put("timeStamp", JsonUtils.getString(wxObj, "timeStamp"));
                payInfoMap.put("sign", JsonUtils.getString(wxObj, "sign"));
                payInfoMap.put("appId", JsonUtils.getString(wxObj, "appid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payInfoMap;
    }

    @Override
    public void getPayStatus(String payId) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "pay_1000");//通联支付状态: 1-未支付 3-交易失败 4-交易成功 5-交易成功-发生退款 6-关闭 99-进行中
        map.put("payId", payId);
        ObservableHelper
                .createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false,false,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.getPayStatusSuccess(new JSONObject(obj).optJSONObject("data").optString("orderStatus"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.getPayStatusSuccess(null);
                    }
                });
    }

}
