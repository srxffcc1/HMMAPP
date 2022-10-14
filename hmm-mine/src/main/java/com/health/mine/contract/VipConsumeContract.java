package com.health.mine.contract;

import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.VipConsume;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;


public interface VipConsumeContract {
    interface View extends BaseView {

        void onGetBalanceListSuccess(List<BalanceModel> list);
        void onGetListSuccess(List<VipConsume> list, PageInfoEarly pageInfoEarly);
    }

    interface Presenter extends BasePresenter {

        void getBalanceList();
        void getList(Map<String, Object> map);
    }
}
