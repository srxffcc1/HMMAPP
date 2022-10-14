package com.health.mine.contract;

import com.health.mine.model.IntegralListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.HotGoodsList;
import com.healthy.library.model.IntegralModel;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;


public interface VipGoodsContract {
    interface View extends BaseView {

        void onSucessPlusGetList(List<GoodsDetail> result, PageInfoEarly pageInfoEarly);
    }

    interface Presenter extends BasePresenter {
        void getVipGoods(Map<String, Object> map);
    }
}
