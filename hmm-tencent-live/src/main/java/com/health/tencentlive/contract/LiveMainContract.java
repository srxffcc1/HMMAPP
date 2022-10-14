package com.health.tencentlive.contract;

import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LiveMainContract {
    interface View extends BaseView {
        void onSucessGetHost(AnchormanInfo anchormanInfo);

    }


    interface Presenter extends BasePresenter {
        void getActLocVip(Map<String, Object> map);
        void checkHost(Map<String, Object> map);

    }
}
