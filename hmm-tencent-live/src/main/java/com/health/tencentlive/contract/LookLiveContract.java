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

public interface LookLiveContract {

    interface View extends BaseView {

        void onAddLookLiveNumSuccess();

        void onClickLiveSuccess();

        void onGetFollowInfoSuccess(boolean result);

        void onClickFollowSuccess(String result);

        void getSuccessLiveRoomInfo(LiveRoomInfo result);

        void getSuccessAgain(LiveRoomInfo result);

        void getSuccessClickNum(int count);

        void getSuccessNoSpeakInfo(String Member_Account, String ShuttedUntil);

    }


    interface Presenter extends BasePresenter {

        void addLookLiveNum(HashMap<String, Object> map);

        void clickLive(HashMap<String, Object> map);

        void getFollowInfo(HashMap<String, Object> map);

        void clickFollow(HashMap<String, Object> map);

        void getLiveRoomInfo(HashMap<String, Object> map);

        void getClickNum(HashMap<String, Object> map);

        void getNoSpeakInfo(HashMap<String, Object> map);

        void getAnchormanLiveing(HashMap<String, Object> map);
    }
}
