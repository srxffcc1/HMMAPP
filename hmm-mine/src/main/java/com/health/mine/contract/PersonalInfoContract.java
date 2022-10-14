package com.health.mine.contract;

import com.healthy.library.model.UserInfoModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/05/09 09:44
 * @des 个人信息
 */
public interface PersonalInfoContract {

    interface View extends BaseView {
        /**
         * 获取到用户信息
         *
         * @param userInfoModel 用户信息
         */
        void onGetUserInfoSuccess(UserInfoModel userInfoModel);

        void onUpLoadSuccess(List<String> urls, int type);

        void onupdateSucess();

        /**
         * 修改生日回调
         *
         * @param isSuccess 是否成功
         */
        void onUpdateBirthday(boolean isSuccess);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取用户信息
         */
        void getUserInfo();

        void uploadFile(List<String> base64List, int type);

        void updateUserInfoPic(Map<String, Object> map);

        void updateUserInfoNick(Map<String, Object> map);

        /**
         * 修改生日
         *
         * @param map
         */
        void updateUserBirthday(Map<String, Object> map);
    }
}
