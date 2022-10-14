package com.health.servicecenter.contract;

import com.healthy.library.model.HotGoodsList;
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
public interface HotGoodsContract {
    interface View extends BaseView {

        void onGetGoodsListSuccess(List<HotGoodsList> list);

        void successAddShopCat(String result);

        void onGetShopCartSuccess(ShopCartModel shopCartModel);
    }

    interface Presenter extends BasePresenter {


        void getGoodsList(Map<String, Object> map);//获取爆款商品列表

        void addShopCat(Map<String, Object> map);//加入购物车

        void getShopCart();//获取购物车数据
    }
}
