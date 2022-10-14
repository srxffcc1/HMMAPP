package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ActGoodsItem;
import com.healthy.library.model.CouponInfoZ;

import java.util.List;
import java.util.Map;

public interface CouPonGoodsContract {
    interface View extends BaseView {
        void onSucessGetList();

    }

    interface Presenter extends BasePresenter {
        void getGoodsList(Map<String, Object> map, CouponInfoZ couponInfoZ);
    }
}
