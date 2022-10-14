package com.health.mall.contract;

import com.healthy.library.model.TechnicianResult;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

/**
 * @author Li
 * @date 2019/03/29 14:30
 * @des
 */

public interface ManDetailContract {
    interface View extends BaseView {

        /**
         * 获取成功
         *
         */
        void onGetFirstSuccess(TechnicianResult result);
        void onGetMoreSuccess(TechnicianResult result);

        /**
         * 获取门店列表失败
         */
        void onGetStoreListFail();
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取列表
         *
         */
        void getManDetail(String userId);
    }
}
