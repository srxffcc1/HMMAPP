package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ShopDetailModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface VoteCouponDetailContract {
    interface View extends BaseView {
        void onSucessSubmit();
        void onGetStoreListSuccess(List<ShopDetailModel> storeDetialModelList);

    }


    interface Presenter extends BasePresenter {

        void save(Map<String, Object> map);
        void getStoreList(HashMap<String, Object> map);
    }
}
