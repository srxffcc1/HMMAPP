package com.health.mall.contract;

import com.healthy.library.model.StoreIntroduceModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

/**
 * @author Li
 * @date 2019/04/12 17:30
 * @des
 */

public interface StoreIntroduceContract {
    interface View extends BaseView {

        /**
         * 获取数据成功
         *
         */
        void onGetIntroduceSuccess(StoreIntroduceModel result);

    }

    interface Presenter extends BasePresenter {
        /**
         *
         * @param shopId      门店
         */
        void getIntroduce(String shopId);
    }
}
