package com.health.mine.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.health.mine.R
import com.health.mine.contract.VipTakeOutContract
import com.health.mine.presenter.VipTakeOutPresenter
import com.healthy.library.base.BaseActivity
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.BankCardModel
import com.healthy.library.routes.MineRoutes
import kotlinx.android.synthetic.main.activity_vip_bankcard_del.*

/**
 * @Description: (银行卡删除页面)
 * @author:LiuWei
 * @date 2022/8/29
 * @version V1.0
 */
@Route(path = MineRoutes.MINE_VIPBANKCARDDEL)
class VipBankcardDelActivity : BaseActivity(), IsFitsSystemWindows, VipTakeOutContract.View {

    @Autowired
    @JvmField
    var cardNum = ""

    private var vipTakeOutPresenter: VipTakeOutPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        vipTakeOutPresenter = VipTakeOutPresenter(this, this)
        showContent()
        img_back.setOnClickListener { finish() }
        getData()
    }

    override fun getData() {
        super.getData()
        vipTakeOutPresenter?.getBankCardBin(cardNum)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_vip_bankcard_del
    }

    override fun getBankCardListSuccess(resultData: MutableList<BankCardModel>?) {
        TODO("Not yet implemented")
    }

    override fun getBankCardBinSuccess(resultData: BankCardModel?) {
        if (resultData != null) {
            cardName.text = resultData.bankName
            cardNumTxt.text = "**** ${cardNum.substring(
                cardNum.length - 4,
                cardNum.length
            )}"
            submitBtn.alpha = 1f
            submitBtn.isEnabled = true
            submitBtn.setOnClickListener {
                vipTakeOutPresenter?.unbindBankCard(cardNum,this)
            }
        } else {
            showEmpty()
        }
    }

    override fun getSignContractSuccess(resultData: String) {
        TODO("Not yet implemented")
    }

    override fun withdrawApplySuccess(orderNo: String, bizOrderNo: String) {
        TODO("Not yet implemented")
    }

    override fun bindBankCardSuccess() {
        TODO("Not yet implemented")
    }

    override fun payByBackSMSSuccess(result: String) {
        TODO("Not yet implemented")
    }

}