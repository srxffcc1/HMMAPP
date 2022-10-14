package com.health.index.contract;

import com.health.index.model.IndexAllChat2;
import com.healthy.library.model.IndexAllQuestion;
import com.health.index.model.IndexAllSee;
import com.health.index.model.IndexBean;
import com.health.index.model.IndexVideo;
import com.health.index.model.IndexVideoOnline;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/04 10:38
 * @des
 */
public interface IndexMonContract {
    interface View extends BaseView {
        void getIndexSuccess(IndexBean indexBean);
        void getAllStatusSuccess(List<UserInfoExModel> userInfoExModels);
        void getIndexFail();
        void onSuccessLike();
        void changeStatusSuccess();
        void getAllVideoOnlineSuccess(List<IndexVideoOnline> indexVideos, PageInfoEarly pageInfoEarly);
        void getAllVideoSuccess(List<IndexVideo> indexVideos, PageInfoEarly pageInfoEarly);
        void getAllChatSuccess(List<IndexAllChat2> indexAllChats, PageInfoEarly pageInfoEarly);
        void getAllQuestionSuccess(List<IndexAllQuestion> indexAllQuestions, PageInfoEarly pageInfoEarly);
        void getAllSeeSuccess(List<IndexAllSee> indexAllSees, PageInfoEarly pageInfoEarly);
        void getMineSuccess(UserInfoMonModel userInfoMonModel);
        void subVideoSucess();
    }

    interface Presenter extends BasePresenter {
        void getH5();
        void getIndexMain(String queryDate,String cityNo,String areaNo,String longitude,String latitude,String isCurrentCity);
        void changeStatus(String id);
        void getMine();
        void getAllVideo();
        void getAllVideoOnline();
        void subVideo(Map<String,Object> map);
        void getAllStatus();
        void getAllChat(String id);
        void getAllQuestion(String currentPage);
        void getAllSee(String currentPage,String queryDate);
        void like(Map<String,Object> map);
        void warn(Map<String,Object> map);
    }
}
