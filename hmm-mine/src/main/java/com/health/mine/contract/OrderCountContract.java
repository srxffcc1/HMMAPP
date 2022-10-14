package com.health.mine.contract;

import com.healthy.library.model.OrderInfo;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

/**
 * @author Li
 * @date 2019/04/29 10:26
 * @des 我的
 */

public interface OrderCountContract {
    interface View extends BaseView {
        /**
         * 获取到用户信息
         *
         */

        void onGetOrderInfoSuccess(OrderInfo orderInfo);
    }

    interface Presenter extends BasePresenter {


        /**
         * 获取用户信息
         */

        /**
         * 获取用户信息
         */
        void getOrderInfo();
    }
}
