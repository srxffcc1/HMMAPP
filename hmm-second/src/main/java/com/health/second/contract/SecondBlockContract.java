package com.health.second.contract;

import com.health.second.model.SecondAct;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
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
public interface SecondBlockContract {
    interface View extends BaseView {
        void onSuccessGetList(List<MainBlockDetailModel> result, PageInfoEarly pageInfoEarly);
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
