package com.health.second.contract;

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
public interface SecondBlockShopContract {
    interface View extends BaseView {
        void onSucessGetList(List<MainBlockDetailModel> result, PageInfoEarly pageInfoEarly);
        void onGetShopCartSuccess(ShopCartModel shopCartModel);
    }

    interface Presenter extends BasePresenter {

        void getGoodsList(Map<String, Object> map);
        void getShopCart();//获取购物车数据
    }
}
