package com.health.discount.contract;

import com.health.discount.model.ChannelModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

public interface CardCenterActivityContract {
    interface View extends BaseView {
        void onSucessGetList(List<ChannelModel> result);

    }
    interface Presenter extends BasePresenter {
        void getList(Map<String,Object> map);
    }
}
