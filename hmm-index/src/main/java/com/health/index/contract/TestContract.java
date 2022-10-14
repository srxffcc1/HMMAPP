package com.health.index.contract;

import com.health.index.model.TipModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/28 15:08
 * @des
 */
public interface TestContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter {

        void getTestData(Map<String, Object> map,String tag);
    }
}
