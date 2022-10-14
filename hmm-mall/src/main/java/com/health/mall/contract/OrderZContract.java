package com.health.mall.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.Goods2ShopModel;
import com.healthy.library.model.GoodsSpecLimit;
import com.healthy.library.model.MallCoupon;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/08 15:15
 * @des 下单
 */

public interface OrderZContract {

    interface View extends BaseView {
        void onGetGoodsDetailSuccess(Goods2ShopModel goodsModel);
        void onGetGoodsShopSuccess(String shopid);
        void onGetGoodsDetailFail();
        void onGenerateOrderSuccess(String orderId);
        void onSucessGetAppCouponList(List<MallCoupon> mallCouponNoList, List<MallCoupon> mallCouponYesList);
        void successGetGoodsSkuFinalResult(List<GoodsSpecLimit> result);
    }

    interface Presenter extends BasePresenter {
        void getGoodsDetail(Map<String, Object> map);
        void getAppCouponList(Map<String, Object> map);
        void getGoodsShop(Map<String, Object> map);
        void generateOrder(Map<String, Object> map);
        void getGoodsLimitResult(Map<String, Object> map);
    }
}
