package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.VideoCommentModel;

import java.util.List;
import java.util.Map;


public interface HanMomCommentContract {
    interface View extends BaseView {

        void onGetCommentListSuccess(List<VideoCommentModel> result, PageInfoEarly pageInfoEarly);

        void onAddCommentReplySuccess(String result);

        void onAddVideoCommentSuccess(String result);

        void onAddCommentPraiseSuccess(String result, int type);

    }

    interface Presenter extends BasePresenter {

        void getCommentList(Map<String, Object> map);

        void addCommentReply(Map<String, Object> map);

        void addVideoComment(Map<String, Object> map);

        void addCommentPraise(Map<String, Object> map, int type);

    }
}
