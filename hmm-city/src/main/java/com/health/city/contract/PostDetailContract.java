package com.health.city.contract;

import com.healthy.library.model.Discuss;
import com.healthy.library.model.PostDetail;
import com.health.city.model.UserInfoCityModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface PostDetailContract {
    interface View extends BaseView {

        void onSuccessDelete();
        void onSuccessAdd();
        void onSuccessLike();
        void onSuccessFan();
        void onSuccessGetPostDetail(PostDetail post);
        void onSuccessGetDiscuss(List<Discuss> discusses, PageInfoEarly pageInfoEarly);
        void onSuccessGetMine(UserInfoCityModel userInfoCityModel);
    }
    interface Presenter extends BasePresenter {
        void getPostDetail(Map<String,Object> map);
        void getDisgussList(Map<String,Object> map);
        void delete(Map<String,Object> map);
        void addDiscuss(Map<String, Object> map);
        void addReview(Map<String, Object> map);
        void likeChild(Map<String,Object> map);
        void like(Map<String,Object> map);
        void fan(Map<String,Object> map);
        void warn(Map<String,Object> map);
        void getMine();
    }
}
