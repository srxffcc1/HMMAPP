package com.health.faq.activity

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.faq.R
import com.health.faq.model.AccountCenterBean
import com.healthy.library.base.BaseActivity
import com.healthy.library.constant.Functions
import com.healthy.library.constant.UrlKeys
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.widget.WebDialog
import kotlinx.android.synthetic.main.activity_account_center.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*

/**
 * @author xinkai.xu
 * 账户中心
 *
 * */
@Route(path = FaqRoutes.ACCOUNTCENTER)
class AccountCenterActivity : BaseActivity(), View.OnClickListener {

    var accountCenterBean: AccountCenterBean? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_account_center
    }

    override fun findViews() {
    }

    override fun init(savedInstanceState: Bundle?) {
        tvSrmx.setOnClickListener(this)
        tvXfjl.setOnClickListener(this)
        tv_hdd.setOnClickListener(this)
        //获取数据
        getDatas()
    }


    override fun onClick(v: View?) {
        when (v) {
            tvSrmx -> {
                //收入明细
                accountCenterBean?.let {
                    ARouter.getInstance().build(FaqRoutes.DETEILEDACTIVITY)
                            .withInt("type", 1)
                            .navigation()
                }
            }
            tvXfjl -> {
                //消费记录
                accountCenterBean?.let {
                    ARouter.getInstance().build(FaqRoutes.DETEILEDACTIVITY)
                            .withInt("type", 2)
                            .navigation()
                }

            }
            tv_hdd->{
                WebDialog.newInstance().setUrl(UrlKeys.URL_HDDSYSM).setIsinhome(true).show(getSupportFragmentManager(),UrlKeys.URL_HDDSYSM);
            }

        }
    }

    private fun getDatas() {
        val map = HashMap<String, Any>(3)
        map["function"] = Functions.FUNCTION_ACCOUNT_CENTER
        ObservableHelper.createObservable(mContext, map)
                .subscribe(object : StringObserver(this, mContext, false,
                        true, true, false, false, false) {
                    override fun onGetResultSuccess(obj: String?) {
                        super.onGetResultSuccess(obj)
                            accountCenterBean = resolveAccountData(obj)
                            accountCenterBean?.let {
                                tv_syje.withNumber(it.incomeBalance!!).setDuration(500).start()
                                tv_hdd_ye.withNumber(it.virtualBalance!!).setDuration(500).start()
                            }
                    }
                })

    }

    fun passHDDSZ(view: View) {

    }

    private fun resolveAccountData(obj: String?): AccountCenterBean? {
        var result: AccountCenterBean? = null
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<AccountCenterBean?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

}
