package com.health.city.contract;

import com.healthy.library.model.Fans;
import com.healthy.library.model.PostDetail;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface FansContract {
    interface View extends BaseView {
        void onSuccessGetPostList(List<PostDetail> posts, PageInfoEarly pageInfoEarly);
        void onSuccessGetFanDetail(Fans fans);
        void onSuccessLike();
        void onSuccessFans();
        void onSuccessDelete();
    }
    interface Presenter extends BasePresenter {
        void getPostList(Map<String,Object> map);
        void getFansDetail(Map<String,Object> map);
        void like(Map<String,Object> map);
        void fan(Map<String,Object> map);
        void delete(Map<String,Object> map);
    }
}
