package com.health.mine.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.EnlistActivityModel

/**
 * @description
 * @author long
 * @date 2021/7/22
 */
interface EnlistContract {

    interface View : BaseView {
        /**
         * 获取报名活动列表回调
         * @param resultData 接口回调数据
         * @param isMore 是否还有更多
         */
        fun getEnListSuccess(resultData: MutableList<EnlistActivityModel>, isMore: Boolean)

        /**
         * 获取报名活动详情回调
         * @param resultModel 活动详情数据
         */
        fun getEnlistDetailsSuccess(resultModel: EnlistActivityModel?)

        /**
         *  报名活动成功回调
         *  @param id 报名记录 id
         */
        fun addEnlistSuccess(id: String,payOrderId:String)

        /**
         * 取消报名成功回调
         */
        fun removeEnlistSuccess()

        /**
         * 获取支付信息 调起支付回调
         */
        fun onGetPayInfoSuccess(url: String)

        fun getPayStatusSuccess(status: String?)
    }

    interface Presenter : BasePresenter {
        /**
         * 获取报名活动列表 t_100102
         * @param map 接口所需参数
         */
        fun getEnList(map: MutableMap<String, Any>)

        /**
         * 获取报名活动详情 t_100104
         * @param id 活动id
         */
        fun getEnlistDetails(map: MutableMap<String, Any>)

        /**
         * 新增报名信息 t_100101
         * @param map 接口所需参数
         */
        fun addEnlist(map: MutableMap<String, Any>)

        /**
         * 取消报名 t_100105
         * @param id 接口所需参数
         */
        fun removeEnlist(map: MutableMap<String, Any>)

        /**
         * 获取支付信息
         * @param map 接口所需参数
         */
        fun getPayInfo(map: MutableMap<String, Any>)

        fun getPayStatus(payId: String)

    }

}