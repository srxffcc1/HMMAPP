package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.MarketingGoodsList;

public interface MarketGoodsSpecContract {
    interface View extends BaseView {
        void onSucessGetList();
    }

    interface Presenter extends BasePresenter {
        void getGoodsSpec(String mapMarketingGoodsId, MarketingGoodsList marketingGoodsListBean);
    }
}
