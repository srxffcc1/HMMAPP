package com.health.city.contract;

import com.health.city.model.TalentList;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.Topic;

import java.util.List;
import java.util.Map;

public interface RankingListContract {
    interface View extends BaseView {
        void onSuccessGetTalentList(List<TalentList> list, boolean isMore);

        void onSuccessGetTalkList(List<Topic> list, boolean isMore);
    }

    interface Presenter extends BasePresenter {
        void getTalentList(Map<String, Object> map);

        void getTalkList(Map<String, Object> map );
    }
}
