package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.GoodsBasketCell;

import java.util.List;
import java.util.Map;

public interface CouponGoodsFinalContract {
    interface View extends BaseView {
        void sucessGetCouponList(List<CouponInfoZ> result);
        void sucessGetCouponMineList(List<CouponInfoZ> result);
    }
    interface Presenter extends BasePresenter {
        void getCouponList(Map<String, Object> map, GoodsBasketCell goodsBasketCell);
        void getCouponMineList(Map<String, Object> map);
    }
}
