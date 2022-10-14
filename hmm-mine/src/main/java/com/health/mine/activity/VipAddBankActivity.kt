package com.health.mine.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.health.mine.R
import com.health.mine.contract.VipTakeOutContract
import com.health.mine.presenter.VipTakeOutPresenter
import com.healthy.library.base.BaseActivity
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.constant.SpKey
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.BankCardModel
import com.healthy.library.routes.MineRoutes
import com.healthy.library.utils.CheckUtils
import com.healthy.library.utils.SpUtils
import kotlinx.android.synthetic.main.activity_vip_add_bank.*
import kotlinx.android.synthetic.main.activity_vip_add_bank.identityNumber
import kotlinx.android.synthetic.main.activity_vip_add_bank.img_back
import kotlinx.android.synthetic.main.activity_vip_add_bank.submitBtn
import kotlinx.android.synthetic.main.activity_vip_add_bank.userName
import kotlinx.android.synthetic.main.activity_vip_identity_check.*

/**
 * @Description: (新增银行卡页面 )
 * @author:LiuWei
 * @date 2022/8/29
 * @version V1.0
 */
@Route(path = MineRoutes.MINE_VIPADDBANK)
class VipAddBankActivity : BaseActivity(), IsFitsSystemWindows, TextWatcher,
    VipTakeOutContract.View {

    private var vipTakeOutPresenter: VipTakeOutPresenter? = null
    private var model: BankCardModel? = null
    private var isSubmit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        vipTakeOutPresenter = VipTakeOutPresenter(this, this)
        showContent()
        initView()
    }

    private fun initView() {
        img_back.setOnClickListener { finish() }
        cardNum.addTextChangedListener(this)
        submitBtn.setOnClickListener {
            if (!CheckUtils.isLegalName(userName.text.toString().trim())) {
                showToast("请输入正确的姓名！")
                return@setOnClickListener
            }
            if (!CheckUtils.checkPhone(phone.text.toString().trim())) {
                showToast("请输入正确的手机号！")
                return@setOnClickListener
            }
            if (phone.text.toString().trim().length > 11) {
                showToast("请输入正确的手机号！")
                return@setOnClickListener
            }
            vipTakeOutPresenter?.bindBankCard(
                SimpleHashMapBuilder<String, Any>()
                    .puts("cardNo", cardNum.text.toString().trim())!!
                    .puts("phone", phone.text.toString().trim())!!
                    .puts("name", userName.text.toString().trim())!!
                    .puts("identityType", 1)!! as MutableMap<String, Any>
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_vip_add_bank
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (cardNum.text.toString().trim().length > 15) {
            vipTakeOutPresenter?.getBankCardBin(cardNum.text.toString().trim())
        }
    }

    override fun getBankCardListSuccess(resultData: MutableList<BankCardModel>?) {
        TODO("Not yet implemented")
    }

    override fun getBankCardBinSuccess(resultData: BankCardModel?) {
        if (resultData != null) {
            this.model = resultData
            isSubmit = true
            submitBtn.isEnabled = true
            submitBtn.alpha = 1f
            errorTxt.visibility = View.INVISIBLE
            identityNumber.text = resultData.bankName
        } else {
            identityNumber.text =""
            errorTxt.visibility = View.VISIBLE
            isSubmit = false
            submitBtn.isEnabled = false
            submitBtn.alpha = 0.5f
        }
    }

    override fun getSignContractSuccess(resultData: String) {
        TODO("Not yet implemented")
    }

    override fun withdrawApplySuccess(orderNo: String, bizOrderNo: String) {
        TODO("Not yet implemented")
    }

    override fun bindBankCardSuccess() {
        finish()
    }

    override fun payByBackSMSSuccess(result: String) {
        TODO("Not yet implemented")
    }
}