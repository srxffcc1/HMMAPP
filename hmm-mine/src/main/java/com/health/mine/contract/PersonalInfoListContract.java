package com.health.mine.contract;

import com.health.mine.model.UserInfoExModel;
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
public interface PersonalInfoListContract {

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
         */
        void onGetUserInfoListSuccess(List<UserInfoExModel> userInfoExModels);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取用户信息
         */
        void getUserInfoList();
        /**
         * 获取用户信息
         */
        void getUserInfo();


        void delete(Map<String,Object> map);
    }
}
