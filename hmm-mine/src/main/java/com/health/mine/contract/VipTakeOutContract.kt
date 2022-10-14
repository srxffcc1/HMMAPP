package com.health.mine.contract

import android.app.Activity
import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.BankCardModel
import com.healthy.library.model.LotteryModel
import com.healthy.library.model.VipProfitModel

/**
 * @Description: (描述)
 * @author:LiuWei
 * @date 2022/9/14
 * @version V1.0
 */
interface VipTakeOutContract {

    interface View : BaseView {

        fun getBankCardListSuccess(resultData: MutableList<BankCardModel>?)

        fun getBankCardBinSuccess(resultData: BankCardModel?)

        fun getSignContractSuccess(resultData: String)

        fun withdrawApplySuccess(orderNo: String,bizOrderNo: String)

        fun bindBankCardSuccess()

        fun payByBackSMSSuccess(result: String)

    }

    interface Presenter : BasePresenter {

        //获取电子签约
        fun getSignContract()

        //查看绑定的银行卡列表
        fun getBankCardList()

        //绑定银行卡
        fun bindBankCard(map: MutableMap<String, Any>)

        //提现申请
        fun withdrawApply(map: MutableMap<String, Any>)

        //确认提现
        fun payByBackSMS(map: MutableMap<String, Any>)

        //获取银行卡信息
        fun getBankCardBin(cardNo: String)

        //解除绑定银行卡
        fun unbindBankCard(cardNo: String,activity:Activity)
    }

}