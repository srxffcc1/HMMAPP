package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface StoreShareContract {
    interface View extends BaseView {
    }


    interface Presenter extends BasePresenter {
        void getShareStoreDetial(String shopId);
    }
}
