package com.health.client.contract;

import com.healthy.library.model.MonMessage;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface MessageContract {
    interface View extends BaseView {

        void onSuccessGetMessage(List<MonMessage> results);

    }


    interface Presenter extends BasePresenter {
       void getMessage();
        void getMessageSepcial();
    }
}
