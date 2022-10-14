package com.health.discount.contract;

import com.healthy.library.model.CouponGoodsModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface PublicCouponContract {
    interface View extends BaseView {
        void onSucessGetList(List<CouponGoodsModel> result, PageInfoEarly pageInfoEarly);
        void successAddShopCat(String result);
    }

    interface Presenter extends BasePresenter {
        void getGoodsList(Map<String, Object> map);
        void addShopCat(Map<String, Object> map);
    }
}
