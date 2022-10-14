package com.health.mine.contract;

import com.health.mine.model.UserInfoExModel;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/05/09 11:24
 * @des
 */

public interface PersonalDetailContract {
    interface View extends BaseView {
        /**
         * 获取到用户信息
         *
         * @param userInfoModel 用户信息
         */
        void onGetUserInfoSuccess(UserInfoModel userInfoModel);

        void onDeleteUserInfoSuccess();
        /**
         * 获取到用户信息
         *
         * @param userInfoModel 用户信息
         */
        void onGetUserInfoExSuccess(UserInfoExModel userInfoModel);

        /**
         * 更改信息成功
         */
        void onUpdateUserInfoSuccess();
        void onUpdateUserInfoSuccessEx();

        void onUpdateUserInfoSuccessF();
        void onUpdateUserInfoSuccessExF();
    }

    interface Presenter extends BasePresenter {



        void delete(Map<String,Object> map);
//        /**
//         * 获取用户信息
//         */
//        void getUserInfo();
        /**
         * 获取用户信息
         */
        void getUserInfo(String id);

        void updateUserInfo(Map<String, Object> map);

        void updateUserInfoEx(Map<String, Object> map);


        void updateUserInfoWithF(Map<String, Object> map);

        void updateUserInfoExWithF(Map<String, Object> map);
    }
}
