package com.health.city.contract;

import com.healthy.library.model.PostDetail;
import com.healthy.library.model.TipPost;
import com.health.city.model.UserInfoCityModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface TipPostContract {
    interface View extends BaseView {
        void onSuccessGetPostList(List<PostDetail> posts, PageInfoEarly pageInfoEarly);
        void onSuccessGetTipPost(TipPost tipPost);
        void onSuccessLike();
        void onSuccessFan();
        void onSuccessGetMine(UserInfoCityModel userInfoCityModel);
    }
    interface Presenter extends BasePresenter {
        void getPostList(Map<String, Object> map);
        void getMine();
        void getTipPost(Map<String, Object> map);
        void like(Map<String, Object> map);
        void fan(Map<String, Object> map);
    }
}
