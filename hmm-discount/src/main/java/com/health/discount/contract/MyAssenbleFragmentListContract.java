package com.health.discount.contract;

import com.healthy.library.model.KKGroup;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface MyAssenbleFragmentListContract {
    interface View extends BaseView {
        void onSuccessGetGroupList(List<KKGroup> kickList, PageInfoEarly pageInfoEarly);
    }
    interface Presenter extends BasePresenter {
        void getGroupList(Map<String, Object> map);
    }
}
