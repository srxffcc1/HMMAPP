package com.health.discount.contract;

import com.health.discount.model.PointTab;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

public interface PointBlockContract {
    interface View extends BaseView {
        void onPointBlockSuccess();

    }
    interface Presenter extends BasePresenter {
        void getPointTabContent(Map<String, Object> map, PointTab pointTab);
    }
}
