package com.health.mall.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ProvinceCityModel;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/11 09:15
 * @des
 */

public interface ProvinceCityContract {
    interface View extends BaseView {

        /**
         * 获取省列表成功
         *
         * @param provinceList 省列表
         */
        void onGetProvinceListSuccess(List<ProvinceCityModel> provinceList);

        /**
         * 获取市列表成功
         *
         * @param cityList 市列表
         */
        void onGetCityListSuccess(List<ProvinceCityModel> cityList);


        void onGetStreetListSuccess(List<ProvinceCityModel> streetList);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取省列表
         */
        void getProvinceList(String fatherid);

        /**
         * 通过省获取市列表
         *
         * @param provinceId 省id
         */
        void getCityList(String provinceId);


        /**
         * 通过省获取市列表
         *
         * @param provinceId 省id
         */
        void getStreetList(String cityId);
    }
}
