package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.UpdateVersion;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:26
 * @des
 */

public interface AppVersionContract {
    interface View extends BaseView {
        void onSucessCheckVersion(UpdateVersion result);
        void onSucessCheckVersionOnly(UpdateVersion result);
    }

    interface Presenter extends BasePresenter {
        void checkVersion(Map<String, Object> map);
        void checkVersionOnly(Map<String, Object> map);
    }
}
