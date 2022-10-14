package com.health.servicecenter.contract;

import com.health.servicecenter.model.OrderDetailModel;
import com.health.servicecenter.model.StoreSimpleModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

/**
 * @author Li
 * @date 2019/04/10 16:02
 * @des 订单详情
 */

public interface OrderDetailContract {

    interface View extends BaseView {
        /**
         * 获取订单详情成功
         *
         * @param detailModel 订单详情
         */
        void onGetOrderDetailSuccess(OrderDetailModel detailModel);
        void onGetStoreSimpleSuccess(StoreSimpleModel simpleModel);
        void onCancleOrderSuccess(String result);
        void onDeleteOrderSuccess(String result);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取订单详情
         *
         * @param orderId 订单编号
         */
        void getOrderDetail(String orderId);

        void getStoreDetail(String shopId, String goodsId, String cityNo, Double lng, Double lat);
        void cancleOrder(String orderId);//取消订单
        void deleteOrder(String id);//删除订单
    }


}
