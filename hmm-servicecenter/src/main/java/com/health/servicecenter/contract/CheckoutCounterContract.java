package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.OrderList;
import com.healthy.library.model.OrderModel;

import java.util.Map;

/**
 * @author Li
 * @date 2019/04/09 13:44
 * @des 收银台
 */

public interface CheckoutCounterContract {
    interface View extends BaseView {

        void onServerOrderDataSuccess(OrderModel orderModel);
        void onOrderDataSuccess(OrderList.OrderFather orderModel);
        void onGetPayInfoSuccess(Map<String, Object> map);
        void getPayOrderId(String orderId,String msg);
        void getPayStatusSuccess(String status);
    }

    interface Presenter extends BasePresenter {
        void getOrderData(Map<String, Object> map);
        void getServerOrderData(Map<String, Object> map);
        void getPayInfo(Map<String, Object> map);
        void getPayOrderId(Map<String, Object> map);
        void getPayStatus(String payId);
    }
}
