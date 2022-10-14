package com.health.mall.contract;

import com.healthy.library.model.CommentDetailModel;
import com.healthy.library.model.CommentReviewModel;
import com.healthy.library.model.StoreDetailModel;
import com.healthy.library.model.UserInfoMallModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/12 17:30
 * @des
 */

public interface CommentContract {
    interface View extends BaseView {

        void onGetCommentSuccess(CommentDetailModel list);
        void onGetReviewListSuccess(List<CommentReviewModel> list, PageInfoEarly pageInfoEarly);
        void onGetStoreSuccess(StoreDetailModel storeModel);
        void onReviewSuccess(String msg);
        void onLikeSuccess(String msg);
        void onGetMineFail();
        void onMineSuccess(UserInfoMallModel userInfoModel);

    }

    interface Presenter extends BasePresenter {

        void reviewCommentHead(String shopCommentId, String content, String memberState);

        void reviewCommentChild(String commentDiscussId, String content, String toMemberId,String fatherId);

        void getComment(String shopCommentId);

        void getReviewList(int currentPage, String shopId);

        void getStore(String shopId, String categoryNo,String cityNo,String longitude,String latitude);

        void getMine();

        void likeHead(String shopCommentId, String type);

        void likeClild(String commentDiscussId, String type);
    }
}
