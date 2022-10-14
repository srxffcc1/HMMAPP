package com.health.mine.contract;

import com.health.mine.model.ServiceRecordModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/05/28 11:09
 * @des
 */

public interface ServiceRecordContract {
    interface View extends BaseView {

        /**
         * 获取调理记录成功
         *
         * @param list 调理记录
         */
        void onGetServiceRecordListSuccess(List<ServiceRecordModel> list);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取调理记录
         *
         * @param serviceId 服务id
         * @param shopId    门店id
         * @param shopBrand 门店品牌
         */
        void getServiceRecord(String serviceId, int shopId, int shopBrand);
    }
}

