package com.health.mine.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Route
import com.health.mine.R
import com.health.mine.contract.VipProfitContract
import com.health.mine.presenter.VipProfitPresenter
import com.health.mine.widget.SelectIdentityDialog
import com.healthy.library.base.BaseActivity
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.VipProfitModel
import com.healthy.library.routes.MineRoutes
import com.healthy.library.utils.CheckUtils
import kotlinx.android.synthetic.main.activity_vip_identity_check.*

/**
 * @Description: (实名认证页面)
 * @author:LiuWei
 * @date 2022/8/29
 * @version V1.0
 */
@Route(path = MineRoutes.MINE_VIPIDENTITYCHECK)
class VipIdentityCheckActivity : BaseActivity(), IsFitsSystemWindows, TextWatcher ,
    VipProfitContract.View{

    private var selectIdentityDialog: SelectIdentityDialog? = null
    private var vipProfitPresenter: VipProfitPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        vipProfitPresenter = VipProfitPresenter(this, this)
        showContent()
        initView()
    }

    private fun initView() {
        userName.addTextChangedListener(this)
        identityNumber.addTextChangedListener(this)
//        identityLayout.setOnClickListener {
//            if (selectIdentityDialog == null) {
//                selectIdentityDialog = SelectIdentityDialog.newInstance();
//            }
//            selectIdentityDialog!!.setResultListener(object : SelectIdentityDialog.ContentListener {
//                override fun resultContent(result: String) {
//                    identityType.setText(result)
//                }
//            })
//            selectIdentityDialog!!.show(supportFragmentManager, "")
//        }
        img_back.setOnClickListener { finish() }
        submitBtn.setOnClickListener {
            if (!CheckUtils.isLegalName(userName.text.toString().trim())) {
                showToast("请输入正确的姓名！")
                return@setOnClickListener
            }
            if (!CheckUtils.isLegalId(identityNumber.text.toString().trim())) {
                showToast("请输入正确的身份证号！")
                return@setOnClickListener
            }
            vipProfitPresenter?.identity(
                userName.text.toString().trim(),
                identityNumber.text.toString().trim()
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_vip_identity_check
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (CheckUtils.isLegalName(userName.text.toString().trim()) && CheckUtils.isLegalId(
                identityNumber.text.toString().trim()
            )
        ) {
            submitBtn.textColorBuilder.setTextColor(Color.parseColor("#ffffff")).intoTextColor()
            submitBtn.shapeDrawableBuilder.setSolidColor(Color.parseColor("#FF6060"))
                .intoBackground()
        } else {
            submitBtn.textColorBuilder.setTextColor(Color.parseColor("#999999")).intoTextColor()
            submitBtn.shapeDrawableBuilder.setSolidColor(Color.parseColor("#F2F2F2"))
                .intoBackground()
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun getVipProfitDataSuccess(resultData: VipProfitModel?) {
    }

    override fun getProfitListSuccess(resultData: MutableList<VipProfitModel>?, isMore: Boolean) {
    }

    override fun identitySuccess(resultData: String) {
        showToast(resultData)
        getUserStatus()
        finish()
    }

}