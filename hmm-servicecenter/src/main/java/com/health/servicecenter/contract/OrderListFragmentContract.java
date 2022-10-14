package com.health.servicecenter.contract;

import com.healthy.library.model.OrderList;
import com.healthy.library.model.OrderListModel;
import com.healthy.library.model.OrderListPageInfo;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface OrderListFragmentContract {
    interface View extends BaseView {

        void onGetOrderListSuccess(List<OrderList> list, OrderListPageInfo pageInfo);

        void onDeleteOrderSuccess(String result);

        void onConfirmOrderSuccess(String result);
    }

    interface Presenter extends BasePresenter {


        void getOrderList(Map<String, Object> map);//获取订单列表

        void deleteOrder(String id);//删除订单

        void confirmOder(Map<String, Object> map);//确认收货

    }
}
