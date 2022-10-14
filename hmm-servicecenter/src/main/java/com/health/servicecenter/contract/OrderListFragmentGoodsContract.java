package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.OrderList;
import com.healthy.library.model.OrderListPageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface OrderListFragmentGoodsContract {
    interface View extends BaseView {
        void onGetOrderListGoodsSuccess();
    }

    interface Presenter extends BasePresenter {
        void getOrderListGoods(Map<String, Object> map, OrderList orderList);//获取订单中的商品信息
    }
}
