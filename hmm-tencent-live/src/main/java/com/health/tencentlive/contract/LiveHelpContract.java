package com.health.tencentlive.contract;

import com.health.tencentlive.model.JackpotList;
import com.health.tencentlive.model.LiveHelpList;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.HashMap;
import java.util.List;

public interface LiveHelpContract {

    interface View extends BaseView {

        void getSuccessHelpRankingList(List<LiveHelpList> result, PageInfoEarly pageInfo);

        void getSuccessLiveHelpList(List<LiveHelpList> result, PageInfoEarly pageInfo);

        void getSuccessLiveHelpInfo(LiveHelpList result);

        void getSuccessCoupon(String result,String couponName);

        void getSuccessWinId(String result);

        void getSuccessHelpStatus(int result);

        void getSuccessJackpotList(List<JackpotList> result, PageInfoEarly pageInfo);

    }

    interface Presenter extends BasePresenter {

        void getHelpRankingList(HashMap<String, Object> map);

        void getLiveHelpList(HashMap<String, Object> map);

        void getLiveHelpInfo(HashMap<String, Object> map);

        void getJackpotList(HashMap<String, Object> map);

        void getCoupon(HashMap<String, Object> map,String couponName);

        void getHelpStatus(HashMap<String, Object> map);

        void getWinId(HashMap<String, Object> map);
    }
}
