package com.health.city.contract;

import com.healthy.library.model.Fans;
import com.healthy.library.model.PostDetail;
import com.healthy.library.model.Topic;
import com.health.city.model.UserInfoCityModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface PostListContract {
    interface View extends BaseView {
        void onSuccessGetPostList(List<PostDetail> posts, PageInfoEarly pageInfoEarly);
        void onSuccessGetFans(List<Fans> fans);
        void onSuccessGetHotTopic(List<Topic> topics);
        void onSuccessLike();
        void onSuccessFan(Object result);
        void onGetMine(UserInfoCityModel userInfoCityModel);
        void onGetUserFanSucess(Fans mineFans);
    }
    interface Presenter extends BasePresenter {
        void getUserFans(Map<String,Object> map);
        void getPostList(Map<String,Object> map);
        void getRecommendFans(Map<String, Object> map);
        void getMine();
        void getHotTopicList(Map<String,Object> map);
        void like(Map<String,Object> map);
        void fan(Map<String,Object> map);
        void fan2(Map<String,Object> map);
    }
}
