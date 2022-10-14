package com.health.tencentlive.contract;

import com.health.tencentlive.model.RosterList;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.model.PageInfoEarly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LiveRosterListContract {

    interface View extends BaseView {


        void getSuccessRosterList(List<RosterList> result, PageInfoEarly pageInfo);

        void getSuccessCoupon(String result, String couponName);

        void onSuccessAddGift(String result, String couponName);
    }


    interface Presenter extends BasePresenter {

        void getRosterList(HashMap<String, Object> map);

        void getCoupon(HashMap<String, Object> map, String couponName);

        void addGift(Map<String, Object> map, String couponName);
    }
}
