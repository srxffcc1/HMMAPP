package com.health.tencentlive.contract;

import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LiveMainBodyDetailContract {
    interface View extends BaseView {
        void onSucessGetLive(LiveVideoMain liveVideoMain);

        void onStartLiveSuccess(String pushAddress, String groupId);
    }


    interface Presenter extends BasePresenter {

        void getLive(Map<String, Object> map);

        void startLive(Map<String, Object> map);
    }
}
