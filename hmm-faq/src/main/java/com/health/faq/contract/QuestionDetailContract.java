package com.health.faq.contract;

import com.health.faq.model.QuestionDetailModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/07/02 15:30
 * @des 问题详情
 */

public interface QuestionDetailContract {
    interface View extends BaseView {

        /**
         * 获取问题详情
         *
         * @param list      详情
         * @param showReply 是否显示底部回答按钮
         * @param readCount 阅读量
         */
        void onGetQuestionDetailSuccess(List<QuestionDetailModel> list, boolean showReply, int readCount);

        /**
         * 采纳成最佳答案
         */
        void onAdoptSuccess();

        /**
         * 我来回答时需要的参数
         *
         * @param title    问题标题
         * @param nickname 昵称
         * @param faceUrl  用户头像
         */
        void onReplyInfo(String title, String nickname, String faceUrl);

    }

    interface Presenter extends BasePresenter {
        /**
         * 获取问题详情
         *
         * @param questionId 问题id
         */
        void getQuestionDetail(String questionId);

        /**
         * 采纳最佳答案
         *
         * @param id         回复id
         * @param questionId 问题id
         */
        void adoptReply(String id, String questionId);
    }
}
