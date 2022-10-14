package com.health.faq.presenter;

import android.content.Context;

import com.health.faq.contract.PayTranslucentContract;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/07/04 14:28
 * @des
 */

public class PayTranslucentPresenter implements PayTranslucentContract.Presenter {
    private Context mContext;
    private PayTranslucentContract.View mView;

    public PayTranslucentPresenter(Context context, PayTranslucentContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void checkBalanceEnough(String rewardMoney) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_CHECK_BALANCE);
        map.put("rewardMoney", rewardMoney);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, true, false, false,
                        true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            JSONObject dataObject = JsonUtils.getJsonObject(jsonObject, "data");
                            mView.onCheckBalanceSuccess(
                                    JsonUtils.getString(dataObject, "virtualMoney"),
                                    JsonUtils.getInt(dataObject, "isEnough") == 1,
                                    JsonUtils.getInt(dataObject, "isSetPayPassword") == 2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onCheckBalanceFail();
                    }
                });

    }

    @Override
    public void uploadPictures(String[] base64) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_UPLOAD_REWARD_PICTURES);
        map.put("images", base64);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false, false,
                        true, false, false, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            JSONArray array = JsonUtils.getJsonArray(jsonObject, "data");
                            String[] imgUrls = new String[array.length()];
                            for (int i = 0; i < array.length(); i++) {
                                imgUrls[i] = JsonUtils.getString(array, i);
                            }
                            mView.onUploadPictureSuccess(imgUrls);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void submitReward(String money, String title, String content, String isHidden, String[] photo,
                             final String payType) {
        Map<String, Object> map = new HashMap<>(7);
        map.put(Functions.FUNCTION, Functions.FUNCTION_SUBMIT_REWARD);
        map.put("paidInAmount", money);
        map.put("introduction", title);
        map.put("detail", content);
        map.put("isHidden", isHidden);
        map.put("photo", photo);
        map.put("payType", payType);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, true,
                        true, false, false, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        resolvePayInfo(payType, obj);
                    }
                });
    }

    @Override
    public void submitImproveReward(String money, String questionId, final String payType) {
        Map<String, Object> map = new HashMap<>(4);
        map.put(Functions.FUNCTION, Functions.FUNCTION_IMPROVE_REWARD);
        map.put("questionId", questionId);
        map.put("paidInAmount", money);
        map.put("payType", payType);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, true,
                        true, false, false, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        resolvePayInfo(payType, obj);
                    }
                });
    }


    @Override
    public void submitExpert(String id, String money, String detail, String isHidden,
                             String[] photo, final String payType) {
        Map<String, Object> map = new HashMap<>(7);
        map.put(Functions.FUNCTION, Functions.FUNCTION_ASK_EXPERT);
        map.put("userId", id);
        map.put("paidInAmount", money);
        map.put("detail", detail);
        map.put("isHidden", isHidden);
        map.put("photo", photo);
        map.put("payType", payType);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, true,
                        true, false, false, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        resolvePayInfo(payType, obj);
                    }
                });
    }

    private void resolvePayInfo(String payType, String obj) {
        try {
            JSONObject jsonObject = new JSONObject(obj);
            JSONObject dataObject = JsonUtils.getJsonObject(jsonObject, "data");
            JSONObject payInfoObj = JsonUtils.getJsonObject(dataObject, "payInfo");
            switch (payType) {
                case Constants.REWARD_PAY_IN_WX:
                    JSONObject wxObj = new JSONObject(JsonUtils.getString(payInfoObj, "payContent"));
                    Map<String, String> payInfoMap = new HashMap<>(7);
                    payInfoMap.put("partnerId", JsonUtils.getString(wxObj, "mch_id"));
                    payInfoMap.put("prepayId", JsonUtils.getString(wxObj, "prepay_id"));
                    payInfoMap.put("packageValue", "Sign=WXPay");
                    payInfoMap.put("nonceStr", JsonUtils.getString(wxObj, "nonce_str"));
                    payInfoMap.put("timeStamp", JsonUtils.getString(wxObj, "timeStamp"));
                    payInfoMap.put("sign", JsonUtils.getString(wxObj, "sign"));
                    payInfoMap.put("appId", JsonUtils.getString(wxObj, "appid"));
                    mView.onGetOrderInfoSuccess(payType, null, payInfoMap);
                    break;
                case Constants.REWARD_PAY_IN_ALI:

                    mView.onGetOrderInfoSuccess(payType,
                            JsonUtils.getString(payInfoObj, "payContent"), null);
                    break;
                case Constants.REWARD_PAY_IN_HDD:
                    mView.onGetOrderInfoSuccess(payType, null, null);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
