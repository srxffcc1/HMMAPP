package com.health.servicecenter.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.LotteryInfoModel

interface LotteryContract {

    interface View : BaseView {

        fun onLotteryInfoSuccess(lotteryInfoModel: LotteryInfoModel?)

        fun onCheckLotterySuccess(triggerPrivilege: Int)

    }

    interface Presenter : BasePresenter {

        /**
         * 获取当前活动触发条件详情
         */
        fun getLotteryInfo(map: MutableMap<String, Any>)

        /**
         * 检测是否获取签到抽奖的资格
         */
        fun checkLottery(map: MutableMap<String, Any>)

    }
}