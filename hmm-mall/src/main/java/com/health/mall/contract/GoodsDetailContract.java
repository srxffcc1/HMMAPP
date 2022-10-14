package com.health.mall.contract;

import com.healthy.library.model.CommentModelOld;
import com.healthy.library.model.Goods2ShopModel;
import com.healthy.library.model.MallCoupon;
import com.healthy.library.model.StoreMallSimpleModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:26
 * @des
 */

public interface GoodsDetailContract {
    interface View extends BaseView {

        /**
         * 获取商品详情成功
         *
         * @param goodsModel 商品详情
         */
        void onGetGoodsDetailSuccess(Goods2ShopModel goodsModel);

        void onGetCommentSuccess(CommentModelOld commentmodel);

        void onGetStoreSimpleSuccess(StoreMallSimpleModel storeMallSimpleModel);
        void onGetGoodsShopSuccess(String shopid);

        /**
         * 获取商品详情失败
         */
        void onGetGoodsDetailFail();

        void getSucessGoodsAppCouponList(List<MallCoupon> couponInfoByMerchants);
    }

    interface Presenter extends BasePresenter {

        void getStoreDetail(String shopId, String goodsId,String cityNo, String lng, String lat);
        void getGoodsDetail(String goodsId, String shopId, String cityNo,String areaNo,String longitude,String latitude);
        void getComment(String shopId, String searchType, String currentPage,String pageSize);
        void getGoodsAppCouponList(Map<String,Object> map);
        void getGoodsShop(String goodsId, String longitude, String latitude);

    }
}
