package com.health.city.contract;

import com.healthy.library.model.Topic;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

public interface AddTipContract {
    interface View extends BaseView {

        void onSuccessAdd(Topic topic);
    }
    interface Presenter extends BasePresenter {
        void addTip(Map<String,Object> map);
    }
}
