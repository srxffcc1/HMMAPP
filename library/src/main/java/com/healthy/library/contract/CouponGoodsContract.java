package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.CouponInfoZ;

import java.util.List;
import java.util.Map;

public interface CouponGoodsContract {
    interface View extends BaseView {
        void sucessGetCouponList(List<CouponInfoZ> result);
        void onSucessReceivedCoupon(String msg);
    }
    interface Presenter extends BasePresenter {
        void getCouponList(Map<String, Object> map);
        void receivedCoupon(String activityId, String couponId,String memberId);
    }
}
