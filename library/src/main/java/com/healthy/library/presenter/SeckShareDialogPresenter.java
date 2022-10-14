package com.healthy.library.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.LibApplication;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.SeckShareDialogContract;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;
import com.healthy.library.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SeckShareDialogPresenter implements SeckShareDialogContract.Presenter {
    private Context mContext;
    private SeckShareDialogContract.View mView;
    public ShopDetailModel detailModel;

    public SeckShareDialogPresenter(Context context, SeckShareDialogContract.View view) {
        mContext = context;
        mView = view;
    }

    //避免解析奖 null 解析成 "null"
    public String optString(JSONObject json, String key) {
        if (json.isNull(key))
            return "";
        else
            return json.optString(key, null);
    }

    /**
     * 获取门店详情 (获取品牌Logo)
     *
     * @param shopId
     */
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
                        //mView.onGetStoreDetialSuccess(resolveStoreData(obj));
                        try {
                            JSONObject data = new JSONObject(obj)
                                    .getJSONObject("data");
                            detailModel = resolveDetailData(obj);
                            String mShopLogo = optString(data, "merchantLogoUrl");
                            String mPartnerMerchantLogo = optString(data,"partnerMerchantLogo");
                            String merchantBrand = optString(data, "merchantBrand");
//                            if (!TextUtils.isEmpty(merchantBrand)) {
//                                SpUtils.store(mContext, SpKey.SHOP_BRAND, merchantBrand);
//                            }
                            mView.onGetStoreDetialSuccess(mShopLogo,mPartnerMerchantLogo);
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

    @Override
    public void setProgram(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "mini_program_9801");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        //mView.onGetStoreDetialSuccess(resolveStoreData(obj));
                        try {
                            String mBase64Data = new JSONObject(obj)
                                    .getString("data");
                            mView.onGetBase64DataSuccess(mBase64Data);
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

    @Override
    public void setAppProgram(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "mini_program_9802");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        //mView.onGetStoreDetialSuccess(resolveStoreData(obj));
                        try {
                            String mBase64Data = new JSONObject(obj)
                                    .getString("data");
                            mView.onGet32DataSuccess(mBase64Data);
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

    @Override
    public void getUserInfo() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, Functions.FUNCTION_USER_INFO);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
                            mView.onGetUserInfoSuccess(JsonUtils.getString(jsonObject, "faceUrl"), JsonUtils.getString(jsonObject, "nickName"));
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
                .subscribe(new StringObserver(mView, mContext, false, true, true, false, false, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        TokerWorkerInfoModel tokerWorkerInfoModel = resolveTokerData(obj);
                        if (tokerWorkerInfoModel != null) {
                            if (tokerWorkerInfoModel.tokerWorker != null) {
                                mView.getZxingCode(tokerWorkerInfoModel.tokerWorker.referralCode, tokerWorkerInfoModel.tokerWorker.personId);
                                //getZxingBitmap(tokerWorkerInfoModel.tokerWorker.referralCode);
                                SpUtils.store(mContext, SpKey.GETTOKEN, tokerWorkerInfoModel.tokerWorker.referralCode);
                            } else {
                                try {
//                                    getZxingBitmap(tokerWorkerInfoModel.bindingList.get(0).bindingTokerWorker.referralCode);
//                                    mView.getZxingCode(tokerWorkerInfoModel.bindingList.get(0).bindingTokerWorker.referralCode);
                                    //getZxingBitmap("");
                                    mView.getZxingCode("", "");
                                } catch (Exception e) {
                                    //getZxingBitmap("");
                                    mView.getZxingCode("", "");
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            //getZxingBitmap("");
                            mView.getZxingCode("", "");
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        //getZxingBitmap("");
                        mView.getZxingCode("", "");
                    }
                });
    }

    @Override
    public void addLookLivePeopleNum(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9408");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
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

    private ShopDetailModel resolveDetailData(String obj) {
        ShopDetailModel newStoreDetialModel = null;
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
            newStoreDetialModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newStoreDetialModel;
    }

    /*@Override
    public void getZxingBitmap(String referralCode) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, "20017");
        map.put("referralCode", referralCode);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.getZxingBitmap(new JSONObject(obj).getString("data"));
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
    }*/
}
