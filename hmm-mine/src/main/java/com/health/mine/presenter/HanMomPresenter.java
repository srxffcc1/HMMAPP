package com.health.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.mine.contract.HanMomContract;
import com.health.mine.model.HanMomInfoModel;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

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
 * @date 2019/03/27 11:37
 * @des
 */

public class HanMomPresenter implements HanMomContract.Presenter {

    private Context mContext;
    private HanMomContract.View mView;

    public HanMomPresenter(Context context, HanMomContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getInfo(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9700");
        if ("0".equals(map.get("lat"))) {
            return;
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetInfoSuccess(resolveMemberInfoData(obj),
                                resolvestarListData(obj),
                                resolvescrollListData(obj),
                                resolveSettingData(obj),
                                resolvePartnerData(obj),
                                resolveLastPartnerData(obj),
                                resolveRightsListData(obj));
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
    public void getBuyId(String partnerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("partnerId", partnerId);
        map.put(Functions.FUNCTION, "9701");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onGetBuyIdSuccess(new JSONObject(obj).getString("data"));
                        } catch (Exception e) {
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
                    }
                });
    }

    @Override
    public void getPayInfo(final Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9702");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        mView.getPayInfoSuccess(resolvePayInfo(obj));
                        try {
                            mView.getPayInfoSuccess(new JSONObject(obj).optJSONObject("data").optString("content"), map.get("payType").toString());
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

                    }
                });
    }

    @Override
    public void getAliPayUrl(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25001-2");
        map.put("openId", "");
        map.put("payConfigKey", "app");
        ObservableHelper
                .createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.getAliPayUrlSuccess(new JSONObject(obj).optJSONObject("data").optString("payInfo"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                payInfoMap.put("ali", JsonUtils.getString(jsonObject, "content"));
            } else if (Constants.PAY_IN_WX.equals(payType)) {
                JSONObject wxObj = new JSONObject(JsonUtils.getString(jsonObject, "content"));
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

    private List<HanMomInfoModel.ScrollListModel> resolvescrollListData(String obj) {
        List<HanMomInfoModel.ScrollListModel> result = null;
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONObject("checkedPartnerInfo").getJSONArray("scrollList");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<HanMomInfoModel.ScrollListModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private List<HanMomInfoModel.StarListModel> resolvestarListData(String obj) {
        List<HanMomInfoModel.StarListModel> result = null;
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONObject("checkedPartnerInfo").getJSONArray("starList");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<HanMomInfoModel.StarListModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private List<HanMomInfoModel.RightsListModel> resolveRightsListData(String obj) {
        List<HanMomInfoModel.RightsListModel> result = null;
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONObject("checkedPartnerInfo").getJSONArray("rights");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<HanMomInfoModel.RightsListModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private HanMomInfoModel.SettingModel resolveSettingData(String obj) {
        HanMomInfoModel.SettingModel result = null;
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data").getJSONObject("checkedPartnerInfo").getJSONObject("setting");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<HanMomInfoModel.SettingModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private HanMomInfoModel.PartnerModel resolvePartnerData(String obj) {
        HanMomInfoModel.PartnerModel result = null;
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data").getJSONObject("partner");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<HanMomInfoModel.PartnerModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private HanMomInfoModel.LastPartnerModel resolveLastPartnerData(String obj) {
        HanMomInfoModel.LastPartnerModel result = null;
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data").getJSONObject("latestPartner");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<HanMomInfoModel.LastPartnerModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private HanMomInfoModel.MemberInfoModel resolveMemberInfoData(String obj) {
        HanMomInfoModel.MemberInfoModel result = null;
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data").getJSONObject("memberInfo");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<HanMomInfoModel.MemberInfoModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}