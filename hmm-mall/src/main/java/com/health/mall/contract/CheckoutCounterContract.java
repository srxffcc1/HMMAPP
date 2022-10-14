package com.health.mall.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.OrderModel;

import java.util.Map;

/**
 * @author Li
 * @date 2019/04/09 13:44
 * @des 收银台
 */

public interface CheckoutCounterContract {
    interface View extends BaseView {
        /**
         * 获取时间成功
         *
         */
        void onOrderDataSuccess(OrderModel orderModel);

        /**
         * 获取支付信息
         *
         * @param map 支付信息
         */
        void onGetPayInfoSuccess(Map<String, Object> map);
    }

    interface Presenter extends BasePresenter {
        /**
         * 根据订单编号获取订单生成时间以及服务器时间
         *
         * @param orderId 订单编号
         */
        void getServerOrderData(String orderId);

        /**
         * 获取订单支付信息
         *
         * @param orderId 订单编号
         * @param payType 支付方式 支付宝/微信
         */
        void getPayInfo(String orderId, String payType);
    }
}
