package com.health.mine.contract;

import com.health.mine.model.OrderDetailModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/10 09:30
 * @des
 */
public interface OrderListContract {
    interface View extends BaseView {
        /**
         * 获取订单列表成功
         *
         */
        void onGetOrderListSuccess(List<OrderDetailModel> list, PageInfoEarly pageInfo);
        /**
         * 请求结束
         */
        @Override
        void onRequestFinish();

        /**
         * 取消订单成功
         */
        void onCancelOrderSuccess();

    }

    interface Presenter extends BasePresenter {
        /**
         * 获取订单列表
         *
         * @param currentPage 页码
         * @param type        订单类型
         */
        void getOrderList(int currentPage, String type);

        /**
         * 取消订单
         *
         * @param orderId 订单编号
         */
        void cancelOrder(String orderId);
    }

}
