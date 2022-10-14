package com.health.tencentlive.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.LiveVideoMain;

import java.util.HashMap;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface CreateLiveContract {

    interface View extends BaseView {

        void getSuccessAnchormanInfo(AnchormanInfo result);

        void getSuccessLiveRoomConfig(LiveVideoMain result);

        void onStartPushLiveSuccess(String pushAddress, String groupId,String id);
    }


    interface Presenter extends BasePresenter {

        void getAnchormanInfo(HashMap<String, Object> map);

        void getLiveRoomConfig(HashMap<String, Object> map);

        void startPushLive(HashMap<String, Object> map);//开始直播
    }
}
