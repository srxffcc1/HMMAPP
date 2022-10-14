package com.health.discount.contract;

import com.healthy.library.model.KKGroup;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface MyAssembleContract {

    interface View extends BaseView {

        void onSuccessGetTopAssembleList(List<KKGroup> kickList, PageInfoEarly pageInfoEarly);
        void onSuccessUnjoinAssemble();

    }

    interface Presenter extends BasePresenter {

        void getAssembleTopList(Map<String, Object> map);

        void unjoinAssemble(Map<String, Object> map);
    }
}
