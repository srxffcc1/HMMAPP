package com.health.mine.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.mine.contract.ZxingScanContract;
import com.healthy.library.LibApplication;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.LocMessageDialog;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.message.UpdateUserBindInfoMsg;
import com.healthy.library.model.LocVip;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.model.ZxingReferralCodeModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class ZxingScanPresenter implements ZxingScanContract.Presenter {

    private Context mContext;
    private ZxingScanContract.View mView;
    LocMessageDialog.MessageOkClickListener messageOkClickListener;

    public void setMessageOkClickListener(LocMessageDialog.MessageOkClickListener messageOkClickListener) {
        this.messageOkClickListener = messageOkClickListener;
    }

    public ZxingScanPresenter(Context context, ZxingScanContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getCodeInfo(final String referralCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("referralCode", referralCode);
        map.put(Functions.FUNCTION, "20014");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false, false, false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        ZxingReferralCodeModel model = resolveData(obj);
                        if (model != null) {
                            if (TextUtils.isEmpty(model.referralCode) || "null".equals(model.referralCode)) {//加固下导购码 也不清楚为啥报错 还能为null的
                                model.referralCode = referralCode;
                            }
                        }
                        try {
                            mView.onGetCodeInfoSuccess(model);
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
                        mView.onGetCodeInfoSuccess(null);
                    }
                });
    }

    @Override
    public void getTokerWorkerInfo() {
        if (TextUtils.isEmpty(SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC))) {
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
                        try {
                            TokerWorkerInfoModel model = resolveTokerData(obj);
                            mView.onGetTokerWorkerInfoSuccess(model);
                            if (model != null) {
                                if (model.tokerWorker != null) {
                                    SpUtils.store(mContext, SpKey.GETTOKEN, model.tokerWorker.referralCode);
                                }
                            }
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
    public void ticketCoupon(String couponId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", couponId);
        map.put(Functions.FUNCTION, "80007-v");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true, true, true, true, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.ticketCoupon();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.ticketCoupon();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.ticketCoupon();
                    }
                });
    }

    public void getLocVip(Map<String, Object> map, final String merchantId) {
        map.put("function", "100001");
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        map.put("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_ORG));
        map.put("city", LocUtil.getCityNo(mContext, SpKey.LOC_ORG));
        map.put("district", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        LocVip locVip = resolveLocVipData(obj);
                        boolean needshowDialog = true;
                        if (locVip != null) {
                            if (!TextUtils.isEmpty(merchantId)) {
                                if (locVip.local != null) {
                                    if (merchantId.equals(locVip.local.merchantId)) {//等于本地的则直接关闭
                                        needshowDialog = false;
                                    }
                                }
                            }
//                            if(needshowDialog){
//                                LocMessageDialog.newInstance().setMessageOkClickListener(messageOkClickListener).setMessage("您扫码绑定了其他地区的母婴顾问","是否要切换").setMerchantId(merchantId).show(((BaseActivity)mContext).getSupportFragmentManager(),"绑定之后的地址切换");
//                            }

                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
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

    @Override
    public void binding(String workerId, final String merchantId, String isDownload, String bindType) {
        Map<String, Object> map = new HashMap<>();
        map.put("referral", workerId);
        map.put("isDownload", isDownload);
        map.put("bindType", bindType);
        map.put(Functions.FUNCTION, "9113");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false, true, true, false, false, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onBindingSuccess(new JSONObject(obj).getString("msg"));
                            EventBus.getDefault().post(new UpdateUserBindInfoMsg());
                            getLocVip(new SimpleHashMapBuilder<String, Object>(), merchantId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        try {
                            mView.onBindingSuccess(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private ZxingReferralCodeModel resolveData(String obj) {
        ZxingReferralCodeModel result = null;
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
            Type type = new TypeToken<ZxingReferralCodeModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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
}