package com.health.faq.contract;

import com.health.faq.model.ExpertAnswerModel;
import com.health.faq.model.ExpertInfoModel;
import com.health.faq.model.ExpertQuestionModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

/**
 * @author Li
 * @date 2019/07/19 11:07
 * @des 问答详情
 */
public interface ExpertQuestionDetailContract {
    interface View extends BaseView {

        /**
         * 获取问题详情
         *
         * @param hasReplied      是否已经回答
         * @param questionModel   问题
         * @param answerModel     回答
         * @param expertInfoModel 专家信息
         */
        void onGetQuestionDetailSuccess(boolean hasReplied, ExpertQuestionModel questionModel,
                                        ExpertAnswerModel answerModel, ExpertInfoModel expertInfoModel);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取问题详情
         *
         * @param questionId 问题id
         */
        void getQuestionDetail(String questionId);
    }
}
