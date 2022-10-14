package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.BackListModel;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface OrderBackDetialContract {
    interface View extends BaseView {

        void onGetBackDetialSuccess(BackListModel detialModel);
    }

    interface Presenter extends BasePresenter {


        void getBackDetial(String id);

    }
}
