package com.health.servicecenter.contract;

import com.healthy.library.model.BackListModel;
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
public interface OrderBackListFragmentContract {
    interface View extends BaseView {

        void onGetBackListSuccess(List<BackListModel> list, OrderListPageInfo pageInfo);

        void onRevokeOrderSuccess(String result);

        void onDeleteOrderSuccess(String result);
    }

    interface Presenter extends BasePresenter {


        void getBackList(Map<String, Object> map);

        void revokeOrder(String id);//撤销售后

        void deleteOrder(String id);//删除售后

    }
}
