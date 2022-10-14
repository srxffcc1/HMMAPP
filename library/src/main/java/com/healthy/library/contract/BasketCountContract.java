package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ShopCartModel;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface BasketCountContract {
    interface View extends BaseView {
        void onGetShopCartSuccess(ShopCartModel shopCartModel);

    }


    interface Presenter extends BasePresenter {

        void getShopCart();
    }
}
