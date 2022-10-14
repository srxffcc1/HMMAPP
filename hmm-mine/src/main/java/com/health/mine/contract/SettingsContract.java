package com.health.mine.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.UserInfoModel;

/**
 * @author Li
 * @date 2019/04/17 14:48
 * @des 设置界面
 */
public interface SettingsContract {
    interface View extends BaseView {
        /**
         * 退出登录成功
         */
        void onLogoutSuccess();

        /**
         * 获取到用户信息
         *
         * @param userInfoModel 用户信息
         */
        void onGetUserInfoSuccess(UserInfoModel userInfoModel);
    }

    interface Presenter extends BasePresenter {
        /**
         * 退出登录
         */
        void logout();

        /**
         * 获取用户信息
         */
        void getUserInfo();
    }
}
