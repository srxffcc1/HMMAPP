package com.health.mine.contract;

import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.DepositList;
import com.healthy.library.model.VipDeposit;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;


public interface VipDepositContract {
    interface View extends BaseView {


        void onGetBalanceListSuccess(List<BalanceModel> list);

        void onGetListSuccess(List<VipDeposit> list, PageInfoEarly pageInfoEarly);

        void onGetDepositListSuccess(List<DepositList> list, PageInfoEarly pageInfoEarly);

        void onGetDepositGoodsSuccess();
    }

    interface Presenter extends BasePresenter {

        void getBalanceList();

        void getList(Map<String, Object> map);

        void getDepositList(Map<String, Object> map,VipDeposit vipDeposit);
    }
}
