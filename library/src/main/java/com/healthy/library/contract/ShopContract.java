package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ShopDetailModel;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface ShopContract {
    interface View extends BaseView {
        void onSucessGetShopDetailOnly(ShopDetailModel shopDetailModel);
    }


    interface Presenter extends BasePresenter {

        void getShopDetailOnly(Map<String, Object> map);
    }
}
