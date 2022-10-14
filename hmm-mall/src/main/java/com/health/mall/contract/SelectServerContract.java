package com.health.mall.contract;

import com.healthy.library.model.OrderSub;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.OrderSubShopListModel;

import java.util.List;
import java.util.Map;

public interface SelectServerContract {
    interface View extends BaseView {
        void successGetStoreSubDetail(OrderSub shopSub);

        void successSub();

        void getStorListSuccess(List<OrderSubShopListModel> subShopList);
    }

    interface Presenter extends BasePresenter {

        void getStoreSubDetail(Map<String, Object> map);

        void subStore(Map<String, Object> map);

        void getStorList();
    }
}
