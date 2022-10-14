package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.LiveVideoMain;

import java.util.List;
import java.util.Map;

public interface LiveListTContract {
    interface View extends BaseView {
        void onSucessGetLiveList(List<LiveVideoMain> result);

        void subVideoSucess();

        void getLiveLinkSucess(LiveVideoMain linkModel);
    }

    interface Presenter extends BasePresenter {
        void getLiveList(Map<String, Object> map);

        void subVideo(Map<String, Object> map);

        void getLiveLink(Map<String, Object> map);
    }
}
