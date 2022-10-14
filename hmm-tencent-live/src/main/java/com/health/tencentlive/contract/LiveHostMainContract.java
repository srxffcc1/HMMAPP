package com.health.tencentlive.contract;

import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LiveHostMainContract {
    interface View extends BaseView {
        void onSucessGetHost(AnchormanInfo anchormanInfo);
        void onSucessGetHostSetting(LiveVideoMain liveVideoMain);
        void onSucessAddLive(String courseId);
        void getSuccessAgain(LiveRoomInfo result);

    }


    interface Presenter extends BasePresenter {
        void getHost(Map<String, Object> map);
        void getHostSetting(Map<String, Object> map);
        void addLive(Map<String, Object> map);
        void getAnchormanLiveing(HashMap<String, Object> map);

    }
}
