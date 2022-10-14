package com.health.discount.contract;

import com.healthy.library.model.MyKickList;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface MyKickContract {
    interface View extends BaseView {
       void onSuccessGetTopKickList(List<MyKickList> kickList, PageInfoEarly pageInfoEarly);
    }
    interface Presenter extends BasePresenter {
        void getKickTopList(Map<String, Object> map);
    }
}
