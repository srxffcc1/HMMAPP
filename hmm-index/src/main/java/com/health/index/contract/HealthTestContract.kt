package com.health.index.contract

import com.health.index.model.AiCategoryList
import com.health.index.model.AiQuestionDetail
import com.health.index.model.AiResult
import com.healthy.library.model.NewsInfo
import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.*

/**
 * 创建日期：2022/02/15 14:34
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */
interface HealthTestContract {
    interface View : BaseView {

        fun onGetAiCategoryListSuccess(result: MutableList<AiCategoryList>?)

        fun onGetAiQuestionDetailSuccess(result: MutableList<AiQuestionDetail>?)

        fun onGetAiResultSuccess(result: AiResult?)

    }

    interface Presenter : BasePresenter {

        fun getAiCategoryList(map: MutableMap<String, Any>)//获取自诊问题分类列表

        fun getAiQuestionDetail(map: MutableMap<String, Any>)//自诊问题及对应答案选项

        fun getAiResult(map: MutableMap<String, Any>)//自诊问题及对应答案选项

    }
}