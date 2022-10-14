package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface CardCenterFragmentContract {
    interface View extends BaseView {
        void onSucessGetList(List<CouponInfoZ> result, PageInfoEarly pageInfoEarly);
        void onSucessReceivedCoupon(String msg);

    }
    interface Presenter extends BasePresenter {
        void getList(Map<String,Object> map);
        void receivedCoupon(String activityId,String couponId);
    }
}
