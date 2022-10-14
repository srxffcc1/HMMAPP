package com.health.discount.contract;

import com.health.discount.model.PointTab;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

public interface PointThemeContract {
    interface View extends BaseView {
        void onSucessGetList(PointTab result);

    }
    interface Presenter extends BasePresenter {
        void getGoodsList(Map<String, Object> map);
    }
}
