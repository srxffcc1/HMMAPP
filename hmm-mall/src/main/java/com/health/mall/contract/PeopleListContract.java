package com.health.mall.contract;

import com.health.mall.model.PeopleListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface PeopleListContract {
    interface View extends BaseView {
        void onGetPeopleListSuccess(List<PeopleListModel> result, PageInfoEarly pageInfoEarly);
    }

    interface Presenter extends BasePresenter {


        void getPeopleList(Map<String, Object> map);
    }
}
