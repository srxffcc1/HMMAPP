package com.health.discount.contract;

import com.healthy.library.model.Coupon;
import com.healthy.library.model.ShareEntity;
import com.healthy.library.model.UpdatePatchVersion;
import com.healthy.library.model.UpdateVersion;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface MainDialogContract {
    interface View extends BaseView {
        void onSuccessGetCouponList(List<Coupon> coupons, PageInfoEarly pageInfoEarly);
        void onSucessGiftCheck(int result);
        void onSucessGiftCheckHasCard(List<Coupon> coupons);
        void onSucessMessageCheck(boolean result);
        void onSucessUpdateGift(String result);
        void onSucessGetAppProgram(ShareEntity shareEntity);
        void onSucessCheckVersion(UpdateVersion result);
    }

    interface Presenter extends BasePresenter {
        void getCouponList(Map<String, Object> map);

        void checkCouponGift(Map<String, Object> map);
        void checkCouponGiftHasCard(Map<String, Object> map);

        void checkVersion(Map<String, Object> map);
        void getAppProgram(Map<String, Object> map);
        void checkMessageDialog();
        void updateGift(Map<String, Object> map);

    }
}
