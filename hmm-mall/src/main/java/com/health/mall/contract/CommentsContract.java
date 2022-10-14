package com.health.mall.contract;

import com.healthy.library.model.CommentListModel;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/12 17:30
 * @des
 */

public interface CommentsContract {
    interface View extends BaseView {

        /**
         * 获取数据成功
         *
         */
        void onGetCommentListSuccess(List<CommentListModel> list, PageInfoEarly refreshLoadModel);
        void onLikeSuccess(String msg);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取评论列表
         *
         * @param currentPage 页码
         * @param shopId      门店
         */
        void getCommentList(int currentPage, String shopId,String searchType);
        void like(String shopCommentId, String type);
    }
}
