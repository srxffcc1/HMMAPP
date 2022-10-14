package com.health.discount.contract;

import com.health.discount.model.SeckillTab;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

public interface SeckillListContract {
    interface View extends BaseView {
        void onSuccessGetTabList(SeckillTab result);

    }
    interface Presenter extends BasePresenter {
        void getTabList(Map<String, Object> map);
    }
}
