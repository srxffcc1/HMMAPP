package com.health.mine.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.EnlistActivityModel

/**
 * @description
 * @author long
 * @date 2021/7/27
 */
interface EnlistSignOffContract {

    interface View : BaseView {

        /**
         * 获取核销信息成功回调
         */
        fun getCodeInfoSuccess(resultModel:EnlistActivityModel)

        /**
         * 确认核销成功回调
         */
        fun confirmSignOffSuccess()

    }

    interface Presenter : BasePresenter {

        /**
         * 获取扫描核销信息 t_100107
         */
        fun getCodeInfo(map: MutableMap<String, Any>)

        /**
         * 确认核销 t_100106
         */
        fun confirmSignOff(map: MutableMap<String, Any>)
    }

}