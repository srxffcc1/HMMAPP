package com.health.mine.contract;

import com.health.mine.model.ServiceDetailModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/05/27 17:28
 * @des 剩余服务项目
 */

public interface InServiceContract {

    interface View extends BaseView {

        /**
         * 获取服务列表成功
         *
         * @param topNotice 头部提示文字
         * @param list      滚动列表
         * @param map       门店-服务
         */
        void onGetServicesListSuccess(String topNotice, List<ServiceDetailModel> list,
                                      Map<String, List<ServiceDetailModel>> map);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取服务
         */
        void getServices();
    }
}
