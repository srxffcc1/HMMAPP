package com.health.mine.contract;

import com.health.mine.model.OrderBackListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/10 09:30
 * @des
 */
public interface OrderBackListContract {
    interface View extends BaseView {
        /**
         * 获取订单列表成功
         *
         */
        void onGetOrderListSuccess(List<OrderBackListModel> list, PageInfoEarly pageInfo);

    }

    interface Presenter extends BasePresenter {

        void getOrderList(Map<String,Object> map);
    }

}
