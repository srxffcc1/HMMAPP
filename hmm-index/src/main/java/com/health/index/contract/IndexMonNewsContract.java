package com.health.index.contract;

import com.health.index.model.IndexAllSee;
import com.health.index.model.IndexMonTool;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/04 10:38
 * @des
 */
public interface IndexMonNewsContract {
    interface View extends BaseView {
        void getNewListSuccess(List<IndexAllSee> indexAllSees, PageInfoEarly pageInfoEarly);
        void getNewListSuccessEx(List<IndexAllSee> indexAllSees, PageInfoEarly pageInfoEarly);
        void getNewTypeSucess(List<IndexMonTool> indexAllSees);



    }

    interface Presenter extends BasePresenter {

        void getNewListEx(String page,Map<String, Object> map);
        void getNewList(String page,Map<String, Object> map);
        void getNewTypeList(Map<String, Object> map);
    }
}
