package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ShopDetailModel;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author long
 * @date 2021/03/30 11:02
 * @des
 */
public interface AppointmentShopListContract {

    interface View extends BaseView {

        /**
         * 门店列表成功回调
         * @param detialModel
         */
        void onGetShopListSuccess(ArrayList<ShopDetailModel> detialModel);

        /**
         * 门店详情成功回调
         * @param detailModel
         */
        void onGetStoreDetailSuccess(ShopDetailModel detailModel);
    }

    interface Presenter extends BasePresenter {

        /**
         * 查询门店列表
         * @param map
         */
        void getShopList(Map<String, Object> map);

        /**
         * 查询门店详情
         * @param map
         */
        void getStoreDetail(Map<String, Object> map);

    }
}
