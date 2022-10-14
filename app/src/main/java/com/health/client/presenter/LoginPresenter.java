package com.health.client.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.health.client.contract.LoginContract;
import com.health.client.model.UserInfoModel;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.presenter.DeleteJiGuangPresenter;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.JsonUtils;
import com.healthy.library.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class LoginPresenter implements LoginContract.Presenter {

    private Context mContext;
    private LoginContract.View mView;

    public LoginPresenter(Context context, LoginContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getCode(String phone) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("function", Functions.FUNCTION_GET_CODE);
        map.put("mobile", phone);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        true, true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetCodeSuccess();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onGetCodeFail();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void login(String phone, String code, String withOutVerifyCode, String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("function", Functions.FUNCTION_LOGIN);
        map.put("mobile", phone);
        map.put("verifyCode", code);
        map.put("loginType", 1 + "");
        map.put("withOutVerifyCode", withOutVerifyCode);
        map.put("token", token);
        map.put("appId", token != null ? "4OVXp9h4" : null);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        UserInfoModel userInfoModel = resolveUserInfo(obj);
                        new DeleteJiGuangPresenter(mContext).deleteJiGuang();//登录成功清除一次别名
                        mView.onLoginSuccess(userInfoModel);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onLoginFail();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });

    }

    @Override
    public void updatePwd(HashMap<String, Object> map) {
        map.put("function", "pwd_1002");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false, true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.updatePwdResult(new JSONObject(obj).optString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.updatePwdResult(null);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void updatePhone(HashMap<String, Object> map) {

    }

    @Override
    public void checkCode(HashMap<String, Object> map) {
        map.put("function", "pwd_1003");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false, true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.checkCodeResult(new JSONObject(obj).optString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.checkCodeResult(null);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void zxingLogin(HashMap<String, Object> map) {
        map.put("function", "9160");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false, true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onZxingLoginSuccess(new JSONObject(obj).optString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.onZxingLoginSuccess(msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onZxingLoginSuccess(null);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void pwdLogin(HashMap<String, Object> map) {
        map.put("function", "pwd_1000");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onLoginSuccess(resolveUserInfo(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onLoginFail();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getPwdCode(HashMap<String, Object> map) {
        map.put("function", "pwd_1001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false, true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetCodeSuccess();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onGetCodeFail();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private UserInfoModel resolveUserInfo(String userInfo) {
        UserInfoModel userInfoModel = new UserInfoModel();
        try {
            JSONObject jsonObject = new JSONObject(userInfo);
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            userInfoModel.setMemberId(JsonUtils.getString(jsonObject, "memberId"));
            userInfoModel.setToken(JsonUtils.getString(jsonObject, "token"));
            userInfoModel.setPhone(JsonUtils.getString(jsonObject, "mobile"));
            userInfoModel.setNickName(JsonUtils.getString(jsonObject, "nickName"));
            userInfoModel.setBirthday(JsonUtils.getString(jsonObject, "birthday"));

            String timestamp = JsonUtils.getString(jsonObject, "createTimestamp");
            String mUserInfoDate = "";
            if (!TextUtils.isEmpty(timestamp)) {
                mUserInfoDate = DateUtils.formatLongAll(timestamp, "yyyy-MM-dd");
            }
            SpUtils.store(mContext, SpKey.USER_ID, userInfoModel.getMemberId());
            System.out.println("登录检查保存Id" + userInfoModel.getMemberId());
            SpUtils.store(mContext, SpKey.TOKEN, userInfoModel.getToken());
            SpUtils.store(mContext, SpKey.PHONE, userInfoModel.getPhone());
            SpUtils.store(mContext, SpKey.USER_ICON, userInfoModel.getFaceUrl());
            SpUtils.store(mContext, SpKey.USER_NICK, userInfoModel.getNickName());
            SpUtils.store(mContext, SpKey.USER_BIRTHDAY, userInfoModel.getBirthday());
            SpUtils.store(mContext, SpKey.USER_CREATE_DATE, mUserInfoDate);

            if (JsonUtils.checkExists(jsonObject, "userInit")) {
                JSONObject object = JsonUtils.getJsonObject(jsonObject, "userInit");
                String currentStatus = JsonUtils
                        .getString(object, "currentStatus", Constants.STATUS_NONE);
                SpUtils.store(mContext, SpKey.STATUS, currentStatus);
            } else {
                SpUtils.store(mContext, SpKey.STATUS, Constants.STATUS_NONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfoModel;
    }
}