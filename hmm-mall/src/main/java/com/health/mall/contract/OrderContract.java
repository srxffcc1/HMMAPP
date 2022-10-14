package com.health.mall.contract;

import com.healthy.library.model.Goods2ShopModel;
import com.healthy.library.model.MallCoupon;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/08 15:15
 * @des 下单
 */

public interface OrderContract {

    interface View extends BaseView {
        /**
         * 获取商品详情成功
         *
         * @param goodsModel 商品详情
         */
        void onGetGoodsDetailSuccess(Goods2ShopModel goodsModel);


        /**
         * 获取商品门店详情成功
         *
         */
        void onGetGoodsShopSuccess(String shopid);

        /**
         * 获取商品详情失败
         */
        void onGetGoodsDetailFail();

        /**
         * 订单生成成功
         *
         * @param orderId 订单编号
         */
        void onGenerateOrderSuccess(String orderId);


        void onSucessGetAppCouponList(List<MallCoupon> mallCouponNoList, List<MallCoupon> mallCouponYesList);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取商品详情
         *
         */
        void getGoodsDetail(String goodsId, String shopId, String cityNo, String areaNo, String longitude, String latitude);


        void getAppCouponList(Map<String,Object> map);
        /**
         * 获取商品门店
         *
         */
        void getGoodsShop(String goodsId, String longitude, String latitude);

        /**
         * 生成订单
         *
         * @param map 参数
         */
        void generateOrder(Map<String, Object> map);

    }
}
