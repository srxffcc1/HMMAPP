package com.health.city.contract;

import com.healthy.library.model.Fans;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface FansListContract {
    interface View extends BaseView {
        void onSuccessGetFansList(List<Fans> fansList, PageInfoEarly pageInfoEarly);
        void onSuccessFans();
    }
    interface Presenter extends BasePresenter {
        void getFanList(Map<String, Object> map);
        void fan(Map<String, Object> map);
    }
}
