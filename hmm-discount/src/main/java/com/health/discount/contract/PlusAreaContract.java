package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.MainBlockDetailModel;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShopCartModel;

import java.util.List;
import java.util.Map;

public interface PlusAreaContract {
    interface View extends BaseView {
        void onSucessPlusGetList(List<GoodsDetail> result, PageInfoEarly pageInfoEarly);
        void onSucessPlusOnlyGetList(List<GoodsDetail> result, PageInfoEarly pageInfoEarly);
        void successAddShopCat(String result);
        void onGetShopCartSuccess(ShopCartModel shopCartModel);
    }

    interface Presenter extends BasePresenter {
        void getGoodsPlusList(Map<String, Object> map);
        void getGoodsPlusOnlyList(Map<String, Object> map);
        void addShopCat(Map<String, Object> map);//加入购物车
        void getShopCart();//获取购物车数据
    }
}
