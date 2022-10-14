package com.health.second.contract;

import com.health.second.model.PeopleListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.OrderList;
import com.healthy.library.model.OrderListPageInfo;
import com.healthy.library.model.ShopDetailMarketing;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.SortGoodsListModel;
import com.healthy.library.model.TechnicianResult;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface OrderTicketContract {
    interface View extends BaseView {

        void onGetOrderInfoSuccess(OrderList model);

        void onTicketSuccess(String result);
    }

    interface Presenter extends BasePresenter {

        void getOrderInfo(Map<String, Object> map);

        void ticket(Map<String, Object> map);

    }
}
