package com.health.faq.contract

import com.health.faq.model.MyQuestion
import com.health.faq.model.QuestionModel
import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import java.lang.Exception

interface MyQuestionAnswersContract {
    interface View : BaseView {
        /**
         * 获取我提问我回答数据
         *
         * @param questionList 数据
         */
        fun getRuestionAnswersSuccess(questionList: MutableList<MyQuestion>)
        fun getRuestionAnswersFail(msg:String)
    }

    interface Presenter : BasePresenter {

        /**
         * 获取我提问 我回答
         *
         * @param type 1：我提问 2：我回答
         */
        fun getRuestionAnswers(type: String)
    }
}