package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.Coupon;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.UpdateVersion;

import java.util.List;
import java.util.Map;

public interface GiftDialogContract {
    interface View extends BaseView {
        void onSuccessGetCouponList(List<Coupon> coupons);
        void onSucessGiftCheck();
    }

    interface Presenter extends BasePresenter {
        void getCouponList(Map<String, Object> map);

        void checkCouponGift(Map<String, Object> map);

    }
}
