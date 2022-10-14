package com.health.servicecenter.contract;

import com.healthy.library.model.OrderGoodsListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

/**
 * @author Li
 * @date 2019/04/10 16:02
 * @des 订单详情
 */

public interface OrderZxingContract {

    interface View extends BaseView {
        /**
         * 获取订单详情成功
         *
         * @param detailModel 订单详情
         */
        void onGetOrderDetailSuccess(OrderGoodsListModel detailModel);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取订单详情
         *
         * @param orderId 订单编号
         */
        void getOrderDetail(String orderId);

    }


}
