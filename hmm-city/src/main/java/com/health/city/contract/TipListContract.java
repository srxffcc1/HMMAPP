package com.health.city.contract;

import com.healthy.library.model.Topic;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface TipListContract {
    interface View extends BaseView {

        void onSuccessGetTipList(List<Topic>topics, PageInfoEarly pageInfoEarly);
    }
    interface Presenter extends BasePresenter {
        void getTipList(Map<String,Object> map);

    }
}
