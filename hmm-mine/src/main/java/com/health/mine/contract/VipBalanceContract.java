package com.health.mine.contract;

import com.health.mine.model.BalanceListModel;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;


public interface VipBalanceContract {
    interface View extends BaseView {

        void onGetBalanceListSuccess(List<BalanceModel> list);

        void onGetBalanceLogListSuccess(PageInfoEarly pageInfoEarly, List<BalanceListModel> list);
    }

    interface Presenter extends BasePresenter {

        void getBalanceList();

        void getBalanceLogList(Map<String, Object> map);
    }
}
