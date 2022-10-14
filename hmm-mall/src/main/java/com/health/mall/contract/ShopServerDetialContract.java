package com.health.mall.contract;

import com.healthy.library.model.ShopServerDetial;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

public interface ShopServerDetialContract {
    interface View extends BaseView {
        void successShopServerDetial(ShopServerDetial serverDetial);
    }

    interface Presenter extends BasePresenter {
        void getShopServerDetial(Map<String, Object> map);
    }
}
