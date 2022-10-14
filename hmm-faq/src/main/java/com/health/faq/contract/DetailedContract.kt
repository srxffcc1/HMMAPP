package com.health.faq.contract

import com.health.faq.model.DetatiledBean
import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView

interface DetailedContract {
    interface View : BaseView {
        /**
         * 获取收入明细 消费记录
         *
         * @param questionList 数据
         */
        fun getDetailedSuccess(questionList: MutableList<DetatiledBean>)
    }

    interface Presenter : BasePresenter {

        /**
         *获取 获取收入明细 消费记录
         *
         * @param type 1：收入明细 2：消费记录
         * @param time 时间
         */
        fun getDetailed(time: String, type: Int)
    }
}