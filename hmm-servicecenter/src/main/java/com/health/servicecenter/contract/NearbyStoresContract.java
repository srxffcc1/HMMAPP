package com.health.servicecenter.contract;

import com.healthy.library.model.StoreListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface NearbyStoresContract {
    interface View extends BaseView {

        void onGetStoreListSuccess(List<StoreListModel> list);
    }

    interface Presenter extends BasePresenter {


        void getStoreList(Map<String, Object> map);//获取门店列表

    }
}
