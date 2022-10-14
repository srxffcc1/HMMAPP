package com.health.city.contract;

import com.health.city.model.UserInfoCityModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

public interface AddDiscussContract {
    interface View extends BaseView {

        void onSuccessAdd();
        void onSuccessGetMine(UserInfoCityModel userInfoCityModel);
    }
    interface Presenter extends BasePresenter {
        void addDiscuss(Map<String, Object> map);
        void addDiscuss2(Map<String, Object> map);
        void addReview(Map<String, Object> map);
        void addReview2(Map<String, Object> map);
        void getMine();
    }
}
