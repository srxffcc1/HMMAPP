package com.health.mall.contract;

import com.healthy.library.model.ShopSub;
import com.healthy.library.model.StoreDetailModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/08 15:15
 * @des 下单
 */

public interface OrderSubContract {

    interface View extends BaseView {
        void successGetStoreSubDetail(ShopSub shopSub);
        void successGetStoreMans(List<StoreDetailModel.TechnicianResults> technicianResults);
        void successSub();
    }

    interface Presenter extends BasePresenter {

        void getStoreSubDetail(Map<String, Object> map);
        void getStoreMan(Map<String, Object> map);
        void subStore(Map<String, Object> map);
    }
}
