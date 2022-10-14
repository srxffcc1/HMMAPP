package com.health.mine.activity

import android.os.Bundle
import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.health.mine.R
import com.healthy.library.base.BaseActivity
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.routes.MineRoutes
import kotlinx.android.synthetic.main.activity_vip_take_out_result.*

/**
 * @Description: (提现结果页面)
 * @author:LiuWei
 * @date 2022/8/27
 * @version V1.0
 */
@Route(path = MineRoutes.MINE_VIPTAKEOUTRESULT)
class VipTakeOutResultActivity : BaseActivity(), IsFitsSystemWindows {

    @Autowired
    @JvmField
    var onSuccess = ""

    @Autowired
    @JvmField
    var cardNo = ""

    @Autowired
    @JvmField
    var cardName = ""

    @Autowired
    @JvmField
    var money = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        initView()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_vip_take_out_result
    }

    fun initView() {
        showContent()
        img_back.setOnClickListener { finish() }
        if (!TextUtils.isEmpty(onSuccess) && onSuccess == "成功") {//提现成功
            GlideCopy.with(mContext)
                .load(R.drawable.take_out_success_icon)
                .into(take_out_icon)
            take_out_txt.text = "提现成功"
            take_out_log.text = ""
            submitBtn.text = "完成"
        } else {//提现失败
            GlideCopy.with(mContext)
                .load(R.drawable.take_out_fail_icon)
                .into(take_out_icon)
            take_out_txt.text = "提现失败"
            take_out_log.text = "请重新提交提现申请"
            submitBtn.text = "重新提交"
        }
        cardNameTxt.text = "${cardName}(${cardNo.substring(cardNo.length - 4, cardNo.length)})"
        moneyTxt.text = "￥${money}"
        submitBtn.setOnClickListener {
            finish()
        }
    }
}