package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.DiscountTopModel;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;

import java.util.Map;


public interface ServiceBasketActDetailContract {
    interface View extends BaseView {
        void onSucessGetBasketActList(DiscountTopModel actDetail);
        void onSucessGetBasketActListEx(GoodsBasketCell goodsBasketCell);
    }

    interface Presenter extends BasePresenter {
        void getActDetailOnline(GoodsBasketCell goodsBasketCell, Map<String, Object> map);//加入购物车
        void getActDetail(GoodsBasketGroup goodsBasketGroup, Map<String, Object> map);//加入购物车
        void getActDetailOnlyCell(GoodsBasketCell goodsBasketCell, Map<String, Object> map);//加入购物车
    }
}
