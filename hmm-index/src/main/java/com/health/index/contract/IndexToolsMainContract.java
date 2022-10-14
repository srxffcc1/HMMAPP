package com.health.index.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AppIndexTopMarketing;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/25 15:07
 * @des
 */
public interface IndexToolsMainContract {
    interface View extends BaseView {
        void onGetToolsSuccess(List<AppIndexTopMarketing> result);

    }

    interface Presenter extends BasePresenter {
        void getTools(Map<String, Object> map);
    }
}
