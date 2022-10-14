package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.SecondSortGoods;

import java.util.List;
import java.util.Map;

public interface ActCityBuySecondBlockContract {
    interface View extends BaseView {
        void onSucessGetCityGoodsList(List<SecondSortGoods> result, PageInfoEarly pageInfo);

    }
    interface Presenter extends BasePresenter {
        void getActGoodsList(Map<String, Object> map);
    }
}
