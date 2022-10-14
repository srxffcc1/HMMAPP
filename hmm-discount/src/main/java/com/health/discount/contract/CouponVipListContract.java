package com.health.discount.contract;

import com.health.discount.model.CouponVip;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface CouponVipListContract {
    interface View extends BaseView {
        void onGetBalanceListSuccess(List<BalanceModel> list);
       void onSuccessGetCouponList(List<CouponVip> coupons, PageInfoEarly pageInfoEarly);
    }
    interface Presenter extends BasePresenter {
        void getBalanceList();
        void getCouponList(Map<String, Object> map);
    }
}
