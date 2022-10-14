package com.health.tencentlive.contract;

import com.healthy.library.model.LiveVideoMain;
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

public interface LiveMainBodyContract {
    interface View extends BaseView {
        void onSucessGetLiveList(List<LiveVideoMain> liveVideoMains,PageInfoEarly pageInfoEarly);

    }


    interface Presenter extends BasePresenter {

        void getLiveList(Map<String,Object> map);
    }
}
