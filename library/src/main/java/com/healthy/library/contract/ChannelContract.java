package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.UpdateVersion;
import com.healthy.library.model.UserInfoMonModel;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface ChannelContract {
    interface View extends BaseView {
        void getSucessIsAuditing();
        void onSucessCheckVersion(UpdateVersion result);
        void getMineSuccess(UserInfoMonModel userInfoMonModel);
    }


    interface Presenter extends BasePresenter {

        void getIsAuditing(Map<String, Object> map);
        void checkVersion(Map<String, Object> map);
        void getMine();
    }
}
