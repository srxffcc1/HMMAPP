package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;


public interface ServiceContract {
    interface View extends BaseView {
        void onGetListSuccess();
    }

    interface Presenter extends BasePresenter {
        void getList();
    }
}
