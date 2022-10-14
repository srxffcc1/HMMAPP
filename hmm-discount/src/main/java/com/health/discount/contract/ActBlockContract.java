package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ActGoodsItem;

import java.util.List;
import java.util.Map;

public interface ActBlockContract {
    interface View extends BaseView {
        void onSucessGetList(List<ActGoodsItem> result,int firstPageSize);

    }
    interface Presenter extends BasePresenter {
        void getActGoodsList(Map<String, Object> map);
    }
}
