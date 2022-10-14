package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AssemableTeam;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:26
 * @des
 */

public interface AppPinContract {
    interface View extends BaseView {
        void onSucessGetAppCouponList(List<AssemableTeam> mallCouponNoList);
    }

    interface Presenter extends BasePresenter {
        void getAppPinList(Map<String, Object> map);
    }
}
