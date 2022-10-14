package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.OrderList;
import com.healthy.library.model.OrderModel;
import com.healthy.library.model.RecommendList;

import java.util.List;
import java.util.Map;


public interface ServiceOrderFinishContract {
    interface View extends BaseView {
        void sucessGetOrderDetail(OrderList.OrderFather orderModel);
        void sucessGetServerOrderDetail(OrderModel orderModel);
        void successGetGoodsRecommend(List<RecommendList> result);
        void successAddShopCat(String result);
    }

    interface Presenter extends BasePresenter {
        void addShopCat(Map<String, Object> map);//加入购物车
        void getOrderDetail(Map<String, Object> map);
        void getServerOrderDetail(Map<String, Object> map);
        void getGoodsRecommend(Map<String, Object> map);
    }
}
