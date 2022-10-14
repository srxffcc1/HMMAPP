package com.health.tencentlive.contract;

import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.LiveFans;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LikeManContract {
    interface View extends BaseView {
        void onSucessGetFansStatus(boolean result);

        void onSucessLikeMan();

        void onSuccessGetMemberId(AnchormanInfo result);

        void onSucessGetFansList(List<LiveFans> fansList, PageInfoEarly pageInfoEarly);

    }


    interface Presenter extends BasePresenter {
        void getMineIsFans(Map<String, Object> map);

        void getMineFans(Map<String, Object> map);

        void likeMan(Map<String, Object> map);

        void getMemberId(Map<String, Object> map);

    }
}
