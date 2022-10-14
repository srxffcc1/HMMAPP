package com.health.servicecenter.contract;

import com.healthy.library.model.OrderDetialModel;
import com.healthy.library.model.OrderList;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface OrderDetialContract {
    interface View extends BaseView {

        void onGetGoodsListSuccess(List<RecommendList> list, int firstPageSize);

        void onGetDetialSuccess(OrderList model);

        void onCancleOrderSuccess(String result);

        void onGetShopCartSuccess(ShopCartModel model);

        void successAddShopCat(String result);

        void onDeleteOrderSuccess(String result);

        void onConfirmOrderSuccess(String result);
    }

    interface Presenter extends BasePresenter {
        void getDetial(Map<String, Object> map);//订单详情

        void getGoodsList(Map<String, Object> map);//获取推荐商品列表

        void cancleOrder(String orderId);//取消订单

        void getShopCart();//获取购物车汇总

        void addShopCat(Map<String, Object> map);//加入购物车

        void deleteOrder(String id);//删除订单

        void confirmOder(Map<String, Object> map);//确认收货

    }
}
