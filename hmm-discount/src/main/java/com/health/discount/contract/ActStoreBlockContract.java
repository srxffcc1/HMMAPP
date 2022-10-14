package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ActStoreItem;

import java.util.List;
import java.util.Map;

public interface ActStoreBlockContract {
    interface View extends BaseView {
        void onSucessGetList(List<ActStoreItem> result);

    }
    interface Presenter extends BasePresenter {
        void getActGoodsList(Map<String, Object> map);
    }
}
