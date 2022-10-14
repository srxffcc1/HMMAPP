package com.health.second.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AppIndexCustom;
import com.healthy.library.model.AppIndexGoodsList;
import com.healthy.library.model.MainBlockDetailModel;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShopCartModel;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface SecondBlockMainContract {
    interface View extends BaseView {
        void successAddShopCat(String result);
        void onSuccessGetAPPIndexCustom(List<AppIndexGoodsList> goodsList);
    }

    interface Presenter extends BasePresenter {


        void addShopCat(Map<String, Object> map);//加入购物车
        void getShopCart();//获取购物车数据
        void getAPPIndexCustom(Map<String, Object> map); // app_index_1000 //服务体验
    }
}
