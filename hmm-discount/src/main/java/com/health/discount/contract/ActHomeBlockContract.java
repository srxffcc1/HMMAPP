package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.MainBlockModel;

import java.util.Map;

public interface ActHomeBlockContract {
    interface View extends BaseView {
        void onSucessGetBlockDetailList(android.view.View view,MainBlockModel item,int position);
    }
    interface Presenter extends BasePresenter {
        void getBlockDetail(android.view.View view,MainBlockModel item, MainBlockModel itemDes,int position, Map<String, Object> map);//获得专区
        void getBlockDetailCity(android.view.View view,MainBlockModel item, MainBlockModel.AllianceMerchant itemDes,int position, Map<String, Object> map);//获得专区
    }
}
