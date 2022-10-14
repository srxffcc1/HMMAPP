package com.health.mall.contract;

import com.healthy.library.model.MallIndexModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/28 15:27
 * @des 商城首页
 */

public interface MallContract {

    interface View extends BaseView {
        /**
         * 获取数据成功
         *
         * @param indexModels 首页数据
         */
        void onGetDataSuccess(List<MallIndexModel> indexModels);

        /**
         * 获取数据失败
         */
        void onGetDataFail();

    }

    interface Presenter extends BasePresenter {

        /**
         * 获取商城首页数据
         *
         * @param areaNo    区域id
         * @param latitude  维度
         * @param longitude 经度
         */
        void getData(String areaNo, String latitude, String longitude);
    }
}
