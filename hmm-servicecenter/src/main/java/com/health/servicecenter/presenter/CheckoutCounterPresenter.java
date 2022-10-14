package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.CheckoutCounterContract;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.OrderList;
import com.healthy.library.model.OrderModel;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Li
 * @date 2019/04/29 16:19
 * @des
 */

public class CheckoutCounterPresenter implements CheckoutCounterContract.Presenter {

    private Context mContext;
    private CheckoutCounterContract.View mView;

    public CheckoutCounterPresenter(Context context, CheckoutCounterContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getOrderData(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25006");
        ObservableHelper
                .createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onOrderDataSuccess(resolveOrderInfo(obj));
                    }
                });
    }

    @Override
    public void getServerOrderData(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9534");
        ObservableHelper
                .createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onServerOrderDataSuccess(resolveServerOrderInfo(obj));
                    }
                });
    }

    private OrderModel resolveServerOrderInfo(String obj) {
        OrderModel result = null;
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
            Type type = new TypeToken<OrderModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private OrderList.OrderFather resolveOrderInfo(String obj) {
        OrderList.OrderFather result = null;
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
            Type type = new TypeToken<OrderList.OrderFather>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getPayInfo(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25001-2");
        map.put("openId", "");
        map.put("payConfigKey", "app");
        ObservableHelper
                .createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, true,true,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            if (new JSONObject(obj).optJSONObject("data")==null){
                                mView.showToast(new JSONObject(obj).optString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mView.onGetPayInfoSuccess(resolvePayInfo(obj));
                    }
                });
    }

    @Override
    public void getPayOrderId(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25001-1");
        ObservableHelper
                .createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.getPayOrderId(new JSONObject(obj).optString("data"),null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.getPayOrderId(null,msg);
                    }
                });
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
                //payInfoMap.put("ali", JsonUtils.getString(jsonObject, "aliPayContent"));
                //新加字段
//                payInfoMap.put("payInterfaceOutTradeNo", JsonUtils.getString(jsonObject, "payInterfaceOutTradeNo"));
//                payInfoMap.put("orderNo", JsonUtils.getString(jsonObject, "orderNo"));
                payInfoMap.put("payInfo", JsonUtils.getString(jsonObject, "payInfo"));
//                payInfoMap.put("bizUserId", JsonUtils.getString(jsonObject, "bizUserId"));
//                payInfoMap.put("bizOrderNo", JsonUtils.getString(jsonObject, "bizOrderNo"));
            } else if (Constants.PAY_IN_WX.equals(payType)) {
                JSONObject wxObj = new JSONObject(JsonUtils.getString(jsonObject, "payContent"));
                payInfoMap.put("partnerId", JsonUtils.getString(wxObj, "mch_id"));
                payInfoMap.put("prepayId", JsonUtils.getString(wxObj, "prepay_id"));
                payInfoMap.put("packageValue", "Sign=WXPay");
                payInfoMap.put("nonceStr", JsonUtils.getString(wxObj, "nonce_str"));
                payInfoMap.put("timeStamp", JsonUtils.getString(wxObj, "timeStamp"));
                payInfoMap.put("sign", JsonUtils.getString(wxObj, "sign"));
                payInfoMap.put("appId", JsonUtils.getString(wxObj, "appid"));


                payInfoMap.put("payId", JsonUtils.getString(jsonObject, "payId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payInfoMap;
    }
}
