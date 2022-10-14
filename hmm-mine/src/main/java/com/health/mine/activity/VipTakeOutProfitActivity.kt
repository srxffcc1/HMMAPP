package com.health.mine.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.health.mine.R
import com.health.mine.contract.VipTakeOutContract
import com.health.mine.presenter.VipTakeOutPresenter
import com.health.mine.widget.PointLengthFilter
import com.health.mine.widget.SelectBankCardDialog
import com.health.mine.widget.TakeOutCodeDialog
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseActivity
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.dialog.SignContractDialog
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.BankCardModel
import com.healthy.library.model.TongLianMemberData
import com.healthy.library.routes.MineRoutes
import com.healthy.library.utils.FormatUtils
import com.healthy.library.utils.ObjUtil
import com.healthy.library.utils.SpUtils
import kotlinx.android.synthetic.main.activity_vip_take_out_profit.*
import java.math.BigDecimal

/**
 * @Description: (提现页面)
 * @author:LiuWei
 * @date 2022/8/26
 * @version V1.0
 */
@Route(path = MineRoutes.MINE_VIPTAKEOUTPROFIT)
class VipTakeOutProfitActivity : BaseActivity(), IsFitsSystemWindows, TextWatcher,
    VipTakeOutContract.View {

    @Autowired
    @JvmField
    var totalUsableAmount = ""

    private var takeOutCodeDialog: TakeOutCodeDialog? = null
    private var selectBankCardDialog: SelectBankCardDialog? = null
    private var signContractDialog: SignContractDialog? = null
    private var vipTakeOutPresenter: VipTakeOutPresenter? = null
    private var mBankCardList: MutableList<BankCardModel> = mutableListOf()
    private var bankCardNo: String? = null
    private var bankCardName: String? = null
    private var position: Int? = 0
    private var url: String? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_vip_take_out_profit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        vipTakeOutPresenter = VipTakeOutPresenter(this, this)
        showContent()
        initView()
        getData()
    }

    override fun getData() {
        super.getData()
        vipTakeOutPresenter?.getSignContract()
        vipTakeOutPresenter?.getBankCardList()
    }

    override fun onResume() {
        super.onResume()
        getUserStatus()
    }

    private fun initView() {
        checkSignContract()
        money.setFilters(arrayOf(PointLengthFilter(2)));
        if (TextUtils.isEmpty(totalUsableAmount)) {
            totalUsableAmount = "0"
        }
        allReceived.text = "可提现金额￥${FormatUtils.moneyKeep2Decimals(totalUsableAmount)}"
        allMoney.setOnClickListener {
            money.setText(FormatUtils.moneyKeep2Decimals(totalUsableAmount))
        }
        img_back.setOnClickListener { finish() }
        money.addTextChangedListener(this)
        submitBtn.setOnClickListener {
            if (TextUtils.isEmpty(bankCardNo)) {
                showToast("请选择银行卡！")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(money.text.toString())) {
                showToast("请输入提现金额！")
                return@setOnClickListener
            }
            if (FormatUtils.moneyKeep2Decimals(money.text.trim().toString())
                    .toDouble() > totalUsableAmount.toDouble()
            ) {
                showToast("单次提现金额不能大于账户总可提现金额！")
                return@setOnClickListener
            }
            val tongLianMemberData = ObjUtil.getObj(
                SpUtils.getValue(LibApplication.getAppContext(), SpKey.TONGLIANBIZUSER),
                TongLianMemberData::class.java
            )
            if (tongLianMemberData == null || !tongLianMemberData.memberInfo.isSignContract) {
                showToast("您还未阅读并签约提现电子协议！")
                return@setOnClickListener
            }
            vipTakeOutPresenter?.withdrawApply(
                SimpleHashMapBuilder<String, Any>()
                    .puts(
                        "amount",
                        money.text.toString().toBigDecimal().multiply(BigDecimal(100))
                    )!!
                    .puts("bankCardNo", bankCardNo)!! as MutableMap<String, Any>
            )
        }
        changeBankCard.setOnClickListener {
            if (ListUtil.isEmpty(mBankCardList)) {
                ARouter.getInstance().build(MineRoutes.MINE_VIPADDBANK).navigation()
            } else {
                if (selectBankCardDialog == null) {
                    selectBankCardDialog = SelectBankCardDialog.newInstance(1)
                }
                selectBankCardDialog?.setResultListener { position -> changeSelect(position) }
                selectBankCardDialog?.setData(mBankCardList)
                selectBankCardDialog?.show(supportFragmentManager, "银行卡")
            }
        }
        signContract.setOnClickListener {
            if (TextUtils.isEmpty(url)) {
                showToast("获取电子签约协议失败")
                vipTakeOutPresenter?.getSignContract()
                return@setOnClickListener
            }
            var uri: Uri = Uri.parse(url)
            var intent = Intent()
            intent.setAction("android.intent.action.VIEW");
            intent.setData(uri)
            startActivity(intent)
//            if (signContractDialog == null) {
//                signContractDialog = SignContractDialog.newInstance()
//            }
//            signContractDialog?.setResultListener {
//                getUserStatus()
//                Handler().postDelayed(
//                    Runnable {
//                        checkSignContract()
//                    }, 500
//                )
//            }
//            signContractDialog?.setData(url)
//            signContractDialog?.show(supportFragmentManager, "电子协议")
        }
    }

    private fun changeSelect(position: Int) {
        for (i in mBankCardList.indices) {
            if (i == position) {
                mBankCardList[i].isSelect = true
                cardName.text = mBankCardList[i].bankName
                changeBankCard.text = "更换银行卡"
                bankCardNo = mBankCardList[i].bankCardNo
                bankCardName = mBankCardList[i].bankName
            } else {
                mBankCardList[i].isSelect = false
            }
        }
    }

    private fun checkSignContract() {
        val tongLianMemberData = ObjUtil.getObj(
            SpUtils.getValue(LibApplication.getAppContext(), SpKey.TONGLIANBIZUSER),
            TongLianMemberData::class.java
        )
        selectImg.isChecked =
            !(tongLianMemberData == null || !tongLianMemberData.memberInfo.isSignContract)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (money.text.trim().isNotEmpty()) {
            submitBtn.isEnabled = true
            submitBtn.alpha = 1f
        } else {
            submitBtn.isEnabled = false
            submitBtn.alpha = 0.5f
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun getBankCardListSuccess(resultData: MutableList<BankCardModel>?) {
        if (ListUtil.isEmpty(resultData)) {
            changeBankCard.text = "绑定银行卡"
            cardName.text = "请绑定银行卡"
        } else {
            cardName.text = "请选择银行卡"
            changeBankCard.text = "选择银行卡"
            this.mBankCardList.clear()
            this.mBankCardList.addAll(resultData!!)
            changeSelect(0)
        }
    }

    override fun getBankCardBinSuccess(resultData: BankCardModel?) {
        TODO("Not yet implemented")
    }

    override fun getSignContractSuccess(resultData: String) {
        if (resultData != null) {
            this.url = resultData
        }
    }

    override fun withdrawApplySuccess(orderNo: String, bizOrderNo: String) {
        if (!TextUtils.isEmpty(orderNo) && !TextUtils.isEmpty(bizOrderNo)) {
            //验证码校验
            if (takeOutCodeDialog == null) {
                takeOutCodeDialog = TakeOutCodeDialog.newInstance();
            }
            takeOutCodeDialog?.setInputListener(object : TakeOutCodeDialog.onInputListener {
                override fun onInput(data: String?) {
                    vipTakeOutPresenter?.payByBackSMS(
                        SimpleHashMapBuilder<String, Any>()
                            .puts("tradeNo", orderNo)!!
                            .puts("verificationCode", data)!!
                            .puts("bizOrderNo", bizOrderNo)!! as MutableMap<String, Any>
                    )
                }

                override fun sendSms() {
                    vipTakeOutPresenter?.withdrawApply(
                        SimpleHashMapBuilder<String, Any>()
                            .puts(
                                "amount",
                                money.text.toString().toBigDecimal().multiply(BigDecimal(100))
                            )!!
                            .puts("bankCardNo", bankCardNo)!! as MutableMap<String, Any>
                    )
                }
            })
            takeOutCodeDialog?.show(supportFragmentManager, "验证码")
        }
    }

    override fun bindBankCardSuccess() {
        TODO("Not yet implemented")
    }

    override fun payByBackSMSSuccess(result: String) {
        if (takeOutCodeDialog != null) {
            takeOutCodeDialog?.dismiss()
        }
        if (result == "失败") {
            return
        }
        ARouter.getInstance()
            .build(MineRoutes.MINE_VIPTAKEOUTRESULT)
            .withString("onSuccess", result)
            .withString("cardNo", bankCardNo)
            .withString("cardName", bankCardName)
            .withString("money", money.text.trim().toString())
            .navigation()
        finish()
    }


}