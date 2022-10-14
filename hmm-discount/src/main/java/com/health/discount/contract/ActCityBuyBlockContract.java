package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ActGoodsCityItem;

import java.util.List;
import java.util.Map;

public interface ActCityBuyBlockContract {
    interface View extends BaseView {
        void onSucessGetCityGoodsList(List<ActGoodsCityItem> result);

    }
    interface Presenter extends BasePresenter {
        void getActGoodsList(Map<String, Object> map);
    }
}
