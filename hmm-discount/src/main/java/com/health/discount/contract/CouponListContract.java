package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface CouponListContract {
    interface View extends BaseView {
       void onSuccessGetCouponList(List<CouponInfoZ> coupons, PageInfoEarly pageInfoEarly);
       void onSucessDelete();
    }
    interface Presenter extends BasePresenter {
        void getCouponList(Map<String, Object> map);
        void deleteCouponS(Map<String, Object> map);
    }
}
