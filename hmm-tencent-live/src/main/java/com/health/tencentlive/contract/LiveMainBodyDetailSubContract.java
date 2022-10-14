package com.health.tencentlive.contract;

import com.healthy.library.model.LiveVideoSub;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LiveMainBodyDetailSubContract {
    interface View extends BaseView {
        void onSucessGetSub(LiveVideoSub liveVideoSub);
        void onSucessSub();

    }


    interface Presenter extends BasePresenter {

        void addSub(Map<String, Object> map);
        void getSubDetail(Map<String, Object> map);
    }
}
