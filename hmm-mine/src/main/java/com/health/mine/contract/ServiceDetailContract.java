package com.health.mine.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

/**
 * @author Li
 * @date 2019/05/28 17:06
 * @des
 */

public interface ServiceDetailContract {
    interface View extends BaseView {


        /**
         * 获取服务单详情成功
         *
         * @param storeName    门店名称
         * @param serviceDate  服务内容
         * @param serviceNames 服务名称
         */
        void onGetServiceDetailSuccess(String storeName, String serviceDate, String serviceNames);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取服务单详情
         *
         * @param serviceId 服务单编号
         */
        void getServiceDetail(String serviceId);
    }
}
