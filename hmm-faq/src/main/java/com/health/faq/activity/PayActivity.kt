package com.health.faq.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.health.faq.R
import com.health.faq.fragment.PayFragment
import com.healthy.library.base.BaseActivity
import com.healthy.library.routes.FaqRoutes

/**
 * @author xinkai.xu
 * 支付页面
 *
 * */
@Route(path = FaqRoutes.FAQ_PAY_PASSWORD)
class PayActivity : BaseActivity() {
    /*
    * 1:已设置 2：未设置
    * */
    @Autowired(name = "type")
    @JvmField
    var type: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_pay
    }

    override fun findViews() {
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        if (type == 1) {
            //已设置
            supportFragmentManager    //
                    .beginTransaction()
                    .add(R.id.fragment_faq, PayFragment(2, "修改支付密码"))   // 此处的R.id.fragment_container是要盛放fragment的父容器
                    .commit()
        } else if (type == 2) {
            //未设置
            supportFragmentManager    //
                    .beginTransaction()
                    .add(R.id.fragment_faq, PayFragment(1, "设置支付密码"))   // 此处的R.id.fragment_container是要盛放fragment的父容器
                    .commit()
        }


    }
}