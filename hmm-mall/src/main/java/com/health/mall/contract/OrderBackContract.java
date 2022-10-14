package com.health.mall.contract;

import com.healthy.library.model.OrderBackModel;
import com.healthy.library.model.OrderMallDetailModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/08 15:15
 * @des 下单
 */

public interface OrderBackContract {

    interface View extends BaseView {
        /**
         * 获取商品详情成功
         *
         * @param
         */
        void onGetGoodsBackDetailSuccess(List<OrderBackModel> list);

        void onGetOrderDetailSuccess(OrderMallDetailModel detailModel);

        void onBackOrderSucess(String id);

        void onAddBackSuccess(String result, String refundId);

    }

    interface Presenter extends BasePresenter {
        /**
         * 获取订单可退
         *
         * @param map 参数
         */
        void getOrderBack(Map<String, Object> map);

        /**
         * 获取订单可退
         *
         * @param map 参数
         */
        void submitOrderBack(Map<String, Object> map);

        void getOrderDetail(String orderId);

        void addBack(Map<String, Object> map);//提交售后
    }
}
