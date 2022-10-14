package com.health.servicecenter.contract;

import com.healthy.library.model.GoodsBasketAll;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.model.RecommendList;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;


public interface ServiceBasketContract {
    interface View extends BaseView {
        void onSucessGetBasketList(GoodsBasketAll goodsBasketAll);
        void successGetGoodsRecommend(List<RecommendList> result);
        void sucessUpdate();
        void sucessDelete();
        void successAddShopCat(String result);
        void onSucessGetShopDetailOnly(GoodsBasketStore goodsBasketStore);
        void onSucessGetGoodsActVips();
    }

    interface Presenter extends BasePresenter {
        void addShopCat(Map<String, Object> map);//加入购物车
        void getBasketList(Map<String, Object> map);
        void getGoodsRecommend(Map<String, Object> map);
        void updateGoods(Map<String, Object> map);
        void deleteGoods(Map<String, Object> map);
        void getShopDetailOnly(Map<String, Object> map, final GoodsBasketStore goodsBasketStore);
        void getGoodsActVips(Map<String, Object> map, final RecommendList recommendList);
    }
}
