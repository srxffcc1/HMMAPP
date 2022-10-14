package com.health.servicecenter.contract;

import com.healthy.library.model.OrderNum;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface OrderListContract {
    interface View extends BaseView {

        void onGetOrderNumSuccess(OrderNum orderNum);
    }

    interface Presenter extends BasePresenter {


        void getOrderNum();//获取订单信息

    }
}
