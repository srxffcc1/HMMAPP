package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AppPinCheck;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:26
 * @des
 */

public interface AppPinCheckContract {
    interface View extends BaseView {
        void onSucessGetPinExtra(AppPinCheck appPinCheck);
    }

    interface Presenter extends BasePresenter {
        void getPinExtra(Map<String, Object> map);
    }
}
