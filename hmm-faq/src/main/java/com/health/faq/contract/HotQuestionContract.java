package com.health.faq.contract;

import com.health.faq.model.QuestionModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * Author: Li
 * Date: 2019/7/25 0025
 * Description:
 */
public interface HotQuestionContract {
    interface View extends BaseView {
        /**
         * 获取问题列表成功
         *
         * @param questionList 问题列表
         */
        void onGetQuestionListSuccess(List<QuestionModel> questionList);
    }

    interface Presenter extends BasePresenter {

        /**
         * 获取问题列表
         */
        void getQuestionList();
    }
}
