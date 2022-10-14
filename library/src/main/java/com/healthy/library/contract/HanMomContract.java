package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.HanMomInfoModel;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface HanMomContract {
    interface View extends BaseView {
        void onGetInfoSuccess(HanMomInfoModel.MemberInfoModel memberInfoModel);

    }


    interface Presenter extends BasePresenter {
        void getInfo(Map<String, Object> map);
    }
}
