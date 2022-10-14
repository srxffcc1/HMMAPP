package com.health.discount.contract;

import com.health.discount.model.LiveLinkModel;
import com.health.discount.model.LiveListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

public interface LiveListContract {
    interface View extends BaseView {
        void onSucessGetLiveList(List<LiveListModel> result);

        void subVideoSucess();

        void getLiveLinkSucess(LiveLinkModel linkModel);
    }

    interface Presenter extends BasePresenter {
        void getLiveList(Map<String, Object> map);

        void subVideo(Map<String, Object> map);

        void getLiveLink(Map<String, Object> map);
    }
}
