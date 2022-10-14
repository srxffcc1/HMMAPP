package com.health.index.contract;

import com.health.index.model.CommentModel;
import com.health.index.model.IndexVideoOnline;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface WebContract {
    interface View extends BaseView {
        /**
         * 修改收藏状态成功
         *
         * @param collectStatus 修改后的状态
         */
        void onUpdateCollectedStatusSuccess(String collectStatus);

        /**
         * 查询收藏状态成功
         *
         * @param collectStatus 当前收藏状态
         * @param collectId     取消收藏时 传入
         * @param title         标题  作分享使用
         * @param des           描述  作分享使用
         */
        void onCheckCollectedStatusSuccess(String collectStatus, String collectId, String title, String des);

        void onGetRealUrlSuccess(String url);

        void onAddCommentSuccess(String result);

        void onAddPraiseSuccess(String result);

        void onAddReplySuccess(String result);

        void getCommentListSuccess(List<CommentModel> indexVideos, PageInfoEarly pageInfoEarly);
    }

    interface Presenter extends BasePresenter {
        /**
         * 修改收藏状态
         *
         * @param knowledgeId   资讯id
         * @param collectStatus 1-收藏 2-取消收藏
         * @param collectId     在取消收藏的时候，要传入，查询收藏状态时会给出
         */
        void updateCollectedStatus(String knowledgeId, String collectStatus, String collectId);

        /**
         * 查询收藏状态
         *
         * @param knowledgeId 资讯id
         */
        void checkCollectedStatus(String knowledgeId);

        void getRealVideoUrl(Map<String, Object> map);

        void addComment(Map<String, Object> map);

        void getCommentList(Map<String, Object> map);

        void addPraise(Map<String, Object> map,String type);

        void addReply(Map<String, Object> map);
    }
}
