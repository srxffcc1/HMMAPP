package com.healthy.library.contract;

import com.healthy.library.model.PostActivityList;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

public interface PostCouponDialogContract {
    interface View extends BaseView {
        void onSuccessGetList(List<PostActivityList> result);

        void onSuccessGetCoupon(String result);
    }

    interface Presenter extends BasePresenter {
        void getCouponList(Map<String, Object> map);

        void getCoupon(Map<String, Object> map);
    }
}
