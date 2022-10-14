package com.health.mall.contract;

import com.healthy.library.model.MallCoupon;
import com.healthy.library.model.ShopSub;
import com.healthy.library.model.StoreDetailModel;
import com.healthy.library.model.StoreMallSimpleModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:59
 * @des
 */

public interface StoreDetailContract {
    interface View extends BaseView {
        /**
         * 获取商店详情成功
         *
         * @param storeModel 商店详情
         */
        void successGetStoreSubDetail(ShopSub shopSub);
        void onGetStoreDetailSuccess(StoreDetailModel storeModel);
        void onSucessReAppCoupon();
        /**
         * 获取商店详情失败
         */
        void onGetStoreDetailFail();
        void onSucessGetStoreCouponList(List<MallCoupon> mallCouponList);

        void onFailGetStoreCouponList();
        void onGetStoreSimpleSuccess(StoreMallSimpleModel storeMallSimpleModel);
    }

    interface Presenter extends BasePresenter {
        void getStoreSubDetail(Map<String, Object> map);

        void getStoreCouponList(Map<String,Object> map);

        void getStoreDetail(String shopId, String categoryNo,String cityNo,String longitude,String latitude);

        void getStoreSimpleDetail(String shopId, String goodsId,String cityNo, String lng, String lat);
        void reAppCoupon(Map<String,Object> map);
    }
}
