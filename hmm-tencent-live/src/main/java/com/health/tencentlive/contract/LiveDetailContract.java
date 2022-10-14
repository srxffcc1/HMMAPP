package com.health.tencentlive.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.LiveRoomInfo;

import java.util.HashMap;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LiveDetailContract {

    interface View extends BaseView {

        void onSuccessGetLiveInfo(AnchormanInfo result);

        void onSuccessGetLiveRoomInfo(LiveRoomInfo result);

    }


    interface Presenter extends BasePresenter {

        void getLiveInfo(HashMap<String, Object> map);

        void getLiveRoomInfo(HashMap<String, Object> map);

    }
}
