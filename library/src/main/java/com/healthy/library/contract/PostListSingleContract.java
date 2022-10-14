package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.PostDetail;

import java.util.List;
import java.util.Map;

public interface PostListSingleContract {
    interface View extends BaseView {
        void onSuccessGetPostList(List<PostDetail> posts, PageInfoEarly pageInfoEarly);

        void onSuccessLike();

        void onSuccessGetActivityList();

        void onSuccessFan(Object result);

        void onSuccessFan();
    }

    interface Presenter extends BasePresenter {
        void getPostList(Map<String, Object> map);

        void like(Map<String, Object> map);

        void fan(Map<String, Object> map);

        void getActivityList(Map<String, Object> map, PostDetail postDetail);

        void warn(Map<String,Object> map);
    }
}
