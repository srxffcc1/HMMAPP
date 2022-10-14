package com.health.index.contract;

import com.health.index.model.IndexMonTool;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/04 10:38
 * @des
 */
public interface IndexMonToolContract {
    interface View extends BaseView {
        void getToolListSuccess(List<IndexMonTool> indexMonToolList);



    }

    interface Presenter extends BasePresenter {

        void getToolList(Map<String,Object> map);
    }
}
