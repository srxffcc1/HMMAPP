package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.Kick;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface KickListContract {
    interface View extends BaseView {
       void onSuccessGetTopKickList(List<Kick> kickList, PageInfoEarly pageInfoEarly);
        void onSuccessGetKickList(List<Kick> kickList, PageInfoEarly pageInfoEarly);
    }
    interface Presenter extends BasePresenter {
        void getKickList(Map<String,Object> map);
        void getKickTopList(Map<String,Object> map);
    }
}
