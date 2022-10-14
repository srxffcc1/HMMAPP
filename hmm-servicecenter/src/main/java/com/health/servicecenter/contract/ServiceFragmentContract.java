package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;


public interface ServiceFragmentContract {
    interface View extends BaseView {
        void onGetListSuccess();
    }

    interface Presenter extends BasePresenter {
        void getList();
    }
}
