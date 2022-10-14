package com.health.client.contract;

import com.health.client.model.UserInfoModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.HashMap;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LoginContract {
    interface View extends BaseView {
        /**
         * 验证码获取成功
         */
        void onGetCodeSuccess();

        /**
         * 获取验证码失败
         */
        void onGetCodeFail();

        /**
         * 登录成功
         *
         * @param userInfo 用户信息
         */
        void onLoginSuccess(UserInfoModel userInfo);

        /**
         * 登录失败
         */
        void onLoginFail();

        void onZxingLoginSuccess(String result);

        void updatePwdResult(String result);

        void checkCodeResult(String result);

    }


    interface Presenter extends BasePresenter {
        /**
         * 获取验证码
         *
         * @param phone 手机号
         */
        void getCode(String phone);

        /**
         * 登录
         *
         * @param phone 手机号
         * @param code  验证码
         */
        void login(String phone, String code, String withOutVerifyCode, String token);

        void updatePwd(HashMap<String, Object> map);

        void updatePhone(HashMap<String, Object> map);

        void checkCode(HashMap<String, Object> map);

        void zxingLogin(HashMap<String, Object> map);

        void pwdLogin(HashMap<String, Object> map);

        void getPwdCode(HashMap<String, Object> map);
    }
}
