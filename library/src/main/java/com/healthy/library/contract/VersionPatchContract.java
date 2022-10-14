package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.HanMomInfoModel;
import com.healthy.library.model.UpdatePatchVersion;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface VersionPatchContract {
    interface View extends BaseView {
        void onSucessCheckPatchVersion(UpdatePatchVersion result);

    }


    interface Presenter extends BasePresenter {
        void checkPatchVersion(Map<String, Object> map);
    }
}
