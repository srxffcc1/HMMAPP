package com.health.tencentlive.contract;

import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.HashMap;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface PushLiveContract {

    interface View extends BaseView {

        void onStopPushLiveSuccess(int result);

        void onStopSpeedSuccess();

        void onChangeLiveStatusSuccess();

        void getSuccessLiveRoomInfo(LiveRoomInfo result);

        void getSuccessAgainLive(String pushAddress, String groupId,String courseId);
    }


    interface Presenter extends BasePresenter {

        void stopPushLive(HashMap<String, Object> map);

        void stopSpeed(HashMap<String, Object> map);

        void getLiveRoomInfo(HashMap<String, Object> map);

        void changeLiveStatus(HashMap<String, Object> map);

        void againLive(HashMap<String, Object> map);

    }
}
