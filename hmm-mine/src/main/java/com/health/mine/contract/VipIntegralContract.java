package com.health.mine.contract;

import com.healthy.library.model.BalanceModel;
import com.health.mine.model.IntegralListModel;
import com.healthy.library.model.IntegralModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;


public interface VipIntegralContract {
    interface View extends BaseView {
        void onGetBalanceListSuccess(List<BalanceModel> list);

        void onGetIntegralSuccess(IntegralModel model);

        void onGetIntegralListSuccess(PageInfoEarly pageInfoEarly, List<IntegralListModel> list);
    }

    interface Presenter extends BasePresenter {

        void getIntegral();
        void getBalanceList();
        void getIntegralList(Map<String, Object> map);
    }
}
