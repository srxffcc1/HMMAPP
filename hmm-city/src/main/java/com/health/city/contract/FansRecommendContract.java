package com.health.city.contract;

import com.healthy.library.model.Fans;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface FansRecommendContract {
    interface View extends BaseView {
        void onSuccessFans();
        void onSuccessFansAll();
        void onSuccessGetFans(List<Fans> fans, PageInfoEarly pageInfoEarly);
    }
    interface Presenter extends BasePresenter {
        void getFansList(Map<String, Object> map);
        void fan(Map<String, Object> map);
        void fanAll(Map<String, Object> map);
    }
}
