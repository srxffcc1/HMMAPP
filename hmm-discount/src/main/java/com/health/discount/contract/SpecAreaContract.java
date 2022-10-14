package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.MainBlockDetailModel;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface SpecAreaContract {
    interface View extends BaseView {
        void onSucessGetList(List<MainBlockDetailModel> result, PageInfoEarly pageInfoEarly);
        void successAddShopCat(String result);
        void successThemeDetial(MainBlockModel result);
        void onGetShopCartSuccess(ShopCartModel shopCartModel);
    }

    interface Presenter extends BasePresenter {
        void getGoodsList(Map<String, Object> map);
        void getThemeDetial(Map<String, Object> map);
        void addShopCat(Map<String, Object> map);//加入购物车
        void getShopCart();//获取购物车数据
    }
}
