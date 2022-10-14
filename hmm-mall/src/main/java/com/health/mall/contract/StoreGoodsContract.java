package com.health.mall.contract;

import com.healthy.library.model.GoodsModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/12 13:48
 * @des 门店包含商品
 */

public interface StoreGoodsContract {
    interface View extends BaseView {

        /**
         * 获取门店列表商品成功
         *
         * @param list 列表
         */
        void onGetStoreGoodsListSuccess(List<GoodsModel> list);

    }

    interface Presenter extends BasePresenter {
        /**
         * 获取门店商品列表
         *
         * @param shopId    门店id
         * @param shopBrand 门店品牌
         * @param shopNo    门店编号
         */
        void getStoreGoodsList(String shopId, String shopBrand, String shopNo);
    }
}
