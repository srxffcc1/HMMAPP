package com.health.mall.contract;

import com.health.mall.model.TypeModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ProvinceCityModel;
import com.healthy.library.model.StoreMallListModel;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/29 14:30
 * @des
 */

public interface StoreListContract {
    interface View extends BaseView {

        /**
         * 获取门店列表成功
         *
         * @param list 门店列表
         */
        void onGetStoreListSuccess(List<StoreMallListModel> list, PageInfoEarly refreshLoadModel);

        /**
         * 获取门店列表成功
         *
         * @param list 门店列表
         */
        void onGetStoreTypeSuccess(List<TypeModel> list);


        void onGetLocationTypeSuccess(List<ProvinceCityModel> provinceCityModels);

        /**
         * 获取门店列表失败
         */
        void onGetStoreListFail();
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取门店列表
         *
         * @param adCode     区域
         * @param lat        维度
         * @param lng        经度
         * @param pageNumber page
         */
        void getStoreList(String addressArea, String addressCity,String categoryNo,String lat, String lng, String pageNumber,String flag);


        void getStoreListWhithCoupon(String cityNo,String lat, String lng, String merchantCouponId);
        /**
         * 获取门店类型
         *
         * @param adCode     区域
         * @param lat        维度
         * @param lng        经度
         * @param pageNumber page
         */
        void getStoreType(String adCode, String lat, String lng, String pageNumber,String fatherId);
        /**
         * 获取区域下的区县
         *
         */
        void getLocationType(String fatherId);
    }
}
