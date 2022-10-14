package com.health.discount.contract;

import com.health.discount.model.PointGoodsList;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.IntegralModel;

import java.util.List;
import java.util.Map;

public interface PointIcanContract {
    interface View extends BaseView {
        void onVipInfoSuccess(IntegralModel vipInfo);
        void onSucessGetList(List<PointGoodsList> result);

    }
    interface Presenter extends BasePresenter {
        void getVipInfo();
        void getPointGoodsList(Map<String, Object> map);
    }
}
