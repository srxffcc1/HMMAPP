package com.health.mall.contract;

import com.healthy.library.model.GoodsModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/29 13:09
 * @des 商品列表
 */

public interface GoodsListContract {
    interface View extends BaseView {
        /**
         * 获取商品列表失败
         *
         * @param goodsModels 商品列表
         */
        void onGetGoodsListSuccess(List<GoodsModel> goodsModels);

        /**
         * 获取商品列表成功
         */
        void onGetGoodsListFail();
    }

    interface Presenter extends BasePresenter {

        /**
         * 获取商品列表
         *
         * @param adCode     区域
         * @param lat        维度
         * @param lng        经度
         * @param pageNumber page
         */
        void getGoodsList(String adCode, String lat, String lng, String pageNumber);
    }
}
