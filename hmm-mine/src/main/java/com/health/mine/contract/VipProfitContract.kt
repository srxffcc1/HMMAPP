package com.health.mine.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.LotteryModel
import com.healthy.library.model.VipProfitModel

/**
 * @Description: (描述)
 * @author:LiuWei
 * @date 2022/9/14
 * @version V1.0
 */
interface VipProfitContract {

    interface View : BaseView {


        fun getVipProfitDataSuccess(resultData: VipProfitModel?)

        fun getProfitListSuccess(resultData: MutableList<VipProfitModel>?, isMore: Boolean)

        fun identitySuccess(resultData: String)

    }

    interface Presenter : BasePresenter {
        //获取个人收益详细
        fun getVipProfitData()

        //获取个人收益明细日志
        fun getProfitList(map: MutableMap<String, Any>)

        //实名认证
        fun identity(name: String, identityNo: String)
    }

}