package com.health.discount.contract;

import com.healthy.library.model.KKGroup;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface NewAssembleContract {

    interface View extends BaseView {

        void onSuccessGetTopAssembleList(List<KKGroup> kickList);

        void onSuccessGetAssembleList(List<KKGroup> kickList, PageInfoEarly pageInfoEarly);
    }

    interface Presenter extends BasePresenter {

        void getAssembleList(Map<String, Object> map);

        void getAssembleTopList(Map<String, Object> map);
    }
}
