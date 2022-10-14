package com.health.faq.fragment

import android.annotation.SuppressLint
import android.view.View
import com.health.faq.R
import com.healthy.library.base.BaseFragment
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.interfaces.OnTopBarListener
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.utils.SpUtils
import com.healthy.library.interfaces.OnPasswordInputFinish
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_pay.*
import java.util.*
import java.util.concurrent.TimeUnit


@SuppressLint("ValidFragment")
class PayFragment constructor(type: Int, title: String) : BaseFragment(), OnPasswordInputFinish, OnTopBarListener {


    /*
     * 1：设置支付密码
     * 2：修改支付密码
     * */
    var type: Int = 0
    //标题
    var title: String = ""
    /*
    *设置密码状态
    *0：还未输入验证码
    * 1：验证码输入对比成功
    * 2：设置6位书密码输入成功
    * 3：重新验证密码输入成功(暂时不用)
    * */
    var setPasswordState: Int = 0
    //设置密码中，第一次输入的密码
    private var passwordOne: String = ""

    /*
    * 修改密码状态
    * 0:初始状态 当前页面位输入原支付密码
    * 1：原支付密码输入成功，重设密码页面
    * 2：重新验证支付密码页面
    * */
    var umodifyState: Int = 0

    var getCodeType: Int = 1

    var mdDisposable: Disposable? = null

    companion object {
        private const val MAX_COUNT_TIME: Long = 60
    }


    init {
        this.type = type
        this.title = title
    }

    var phone: String = ""


    override fun getLayoutId(): Int {
        return R.layout.fragment_pay
    }

    override fun findViews() {
        initView()
    }

    private fun initView() {
        top_bar.setTitle(title)
        top_bar.setTopBarListener(this)
        when (type) {
            //设置支付密码
            1 -> setPassword()
            //修改密码
            2 -> umodifyPassword()
        }
        //设置监听
        pv_code.setVisibility(true)
        pv_code.setOnFinishInput(this)
        pv_code.setForgetPassword(this)

    }


    /*
    * 当第六个数字输入完成之后的回调
    * */
    override fun inputFinish() {

        //密码输入完整之后的操作
        when (type) {
            //设置密码
            1 -> setPasswordSuccess(pv_code.strPassword)
            2 -> umodifyPasswordSuccess(pv_code.strPassword)

        }

    }

    /*--------------------------------------------------------------设置密码-----------------------------------------------------*/
    /*
    * 此方法是设置支付密码的时候所用的
    * */
    private fun setPassword() {
        phone = SpUtils.getValue(mContext, SpKey.PHONE);
        tv_phone.text = "当前账号已与${getPhone(phone)}绑定"
        tv_type_text.text = "请输入验证码"
        //开放获取验证码
        cv_pay.visibility = View.VISIBLE
        //获取验证码
        sb_get_code.setOnClickListener {
            sb_get_code.isEnabled = false
            val map = HashMap<String, Any>(3)
            map["function"] = Functions.FUNCTION_GET_PAY_CODE
            map["mobile"] = phone
            map["type"] = getCodeType

            ObservableHelper.createObservable(mContext, map)
                    .subscribe(object : StringObserver(this, mContext, false,
                            true, true, false, false, false) {
                        override fun onGetResultSuccess(obj: String?) {
                            super.onGetResultSuccess(obj)
                            /*---倒计时----*/
                            mdDisposable = Flowable.intervalRange(0, MAX_COUNT_TIME, 0, 1, TimeUnit.SECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext { aLong -> sb_get_code.text = "剩余${(MAX_COUNT_TIME - aLong!!)}秒" }
                                    .doOnComplete {
                                        //倒计时完毕置为可点击状态
                                        sb_get_code.isEnabled = true
                                        sb_get_code.text = "获取验证码"
                                    }
                                    .subscribe()
                        }

                        override fun onFailure() {
                            super.onFailure()
                            sb_get_code.isEnabled = true
                        }

                    })

        }
    }

    private fun getPhone(phone: String): String {

        return phone.substring(0, 3) + "****" + phone.substring(7, phone.length)

    }

    override fun onDestroy() {
        super.onDestroy()
        mdDisposable?.dispose()
    }

    /*
   * 设置密码页面操作
   * */
    private fun setPasswordSuccess(password: String) {
        when (setPasswordState) {
            0 -> {
                //这里调用接口对比验证码是否正确
                val map = HashMap<String, Any>(3)
                map["function"] = Functions.FUNCTION_GET_YANZHENG_CODE
                map["mobile"] = phone
                map["verifyCode"] = pv_code.strPassword
                ObservableHelper.createObservable(mContext, map)
                        .subscribe(object : StringObserver(this, mContext, false,
                                true, true, false, false, false) {
                            override fun onGetResultSuccess(obj: String?) {
                                super.onGetResultSuccess(obj)
                                //验证码验证正确
                                setPasswordState = 1
                                //清空输入框
                                pv_code.clearAllPassword()
                                //隐藏顶部获取验证码按钮
                                cv_pay.visibility = View.GONE
                                //设置文字
                                tv_type_text.text = "请设置6位数字密码"
                            }

                        })


            }
            1 -> {
                //当前是验证码验证正确之后，输入完成六位密码
                //当前输入验证码需要保存
                passwordOne = password
                //清空输入框
                pv_code.clearAllPassword()
                //设置文字
                tv_type_text.text = "重输验证新支付密码"
                setPasswordState = 2
            }
            2 -> {
                //当前是重新输入密码页面
                //获取到验证码进行两次比对
                if (passwordOne.equals(password)) {
                    //如果两次密码一致，进行提交
                    val map = HashMap<String, Any>(3)
                    map["function"] = Functions.FUNCTION_SET_PASSWORD
                    map["payPassword"] = passwordOne
                    map["payPasswordAgain"] = pv_code.strPassword
                    /**
                     * payPasswordType 1-设置支付密码 2-忘记密码
                     * */
                    when (getCodeType) {
                        1 -> map["payPasswordType"] = getCodeType.toString()
                        2 -> map["payPasswordType"] = getCodeType.toString()
                    }

                    ObservableHelper.createObservable(mContext, map)
                            .subscribe(object : StringObserver(this, mContext, false,
                                    true, true, false, false, false) {
                                override fun onGetResultSuccess(obj: String?) {
                                    super.onGetResultSuccess(obj)
                                    activity!!.finish()
                                }

                            })
                } else {
                    //如果两次密码不一致，提示密码输入不一致
                    showToast("两次密码输入不一致，请重新输入")
                }
            }
        }

    }


    /*---------------------------------------------------------修改密码------------------------------------------------------------------*/
    /*
     * 此方法是修改支付密码时
     * */
    private fun umodifyPassword() {
        //关闭验证码
        cv_pay.visibility = View.GONE
        //设置文字
        tv_type_text.text = "请输入原支付密码"
        //设置忘记密码文字显示
        pv_code.setForgetPasswordVisibility(true)

    }

    /*
    * 修改密码操作
    *
    * */
    private fun umodifyPasswordSuccess(strPassword: String) {
        when (umodifyState) {
            0 -> {
                //原支付密码输入成功，调用接口进行对比
                val map = HashMap<String, Any>(3)
                map["function"] = Functions.FUNCTION_OLD_PASSWORD
                map["oldPayPassword"] = strPassword
                ObservableHelper.createObservable(mContext, map)
                        .subscribe(object : StringObserver(this, mContext, false,
                                true, true, false, false, false) {
                            override fun onGetResultSuccess(obj: String?) {
                                super.onGetResultSuccess(obj)
                                //对比成功，跳转下一个页面
                                //设置文字
                                tv_type_text.text = "请重新设置6位数字密码"
                                pv_code.setForgetPasswordVisibility(false)
                                //清除文字
                                pv_code.clearAllPassword()
                                umodifyState = 1
                            }

                        })
            }
            1 -> {
                //当前是重新设置密码页面
                //重设密码完成，记录密码
                passwordOne += strPassword
                //设置文字，清除数字
                tv_type_text.text = "重输验证新支付密码"
                pv_code.clearAllPassword()
                umodifyState = 2

            }
            2 -> {
                //当前页面位重新验证支付密码
                //调用接口进行验证
                if (passwordOne == strPassword) {
                    //如果相等,调用接口设置密码
                    val map = HashMap<String, Any>(3)
                    map["function"] = Functions.FUNCTION_UPDATE_PASSWORD
                    map["payPassword"] = passwordOne
                    map["payPasswordAgain"] = strPassword
                    ObservableHelper.createObservable(mContext, map)
                            .subscribe(object : StringObserver(this, mContext, false,
                                    true, true, false, false, false) {
                                override fun onGetResultSuccess(obj: String?) {
                                    super.onGetResultSuccess(obj)
                                    //对比成功，跳转下一个页面，修改密码已完成
                                    showToast("密码修改成功！")
                                    activity!!.finish()

                                    //设置文字
//                                    tv_type_text.text = "请重新设置6位密码"
//                                    //清除文字
//                                    pv_code.clearAllPassword()
//                                    umodifyState = 1
                                }

                            })

                } else {
                    //对比失败提示
                    showToast("两次密码不一致，请重新输入")
                }
            }
        }

    }


    /*----------------------------------------------------------------忘记密码-------------------------------------------------*/
    /*
    * 忘记密码点击回调
    *
    * */
    override fun onForgetPassword() {
        //进入找回密码页面。和设置密码一样，重新进入设置密码
        type = 1
        getCodeType = 2
        pv_code.setForgetPasswordVisibility(false)
        top_bar.setTitle("找回密码")
        pv_code.clearAllPassword()
        setPassword()


    }

    /*
    *
    * 此处是处理回退事件
    * */

    override fun onBackBtnPressed() {
        when (type) {
            1 -> {
                when (setPasswordState) {
                    0 -> {
                        if (getCodeType == 1) {
                            mActivity.finish()
                        } else {
                            type = 2
                            umodifyState = 0
                            top_bar.setTitle("修改支付密码")
                            cv_pay.visibility = View.GONE
                            pv_code.clearAllPassword()
                            //设置文字
                            tv_type_text.text = "请输入原支付密码"
                            //设置忘记密码文字显示
                            pv_code.setForgetPasswordVisibility(true)

                        }
                    }
                    1 -> {
                        //清空输入框
                        pv_code.clearAllPassword()
                        //隐藏顶部获取验证码按钮
                        cv_pay.visibility = View.VISIBLE
                        //设置文字
                        tv_type_text.text = "请输入验证码"
                        setPasswordState = 0
                    }
                    2 -> {
                        //清空输入框
                        pv_code.clearAllPassword()
                        //设置文字
                        tv_type_text.text = "请设置6位数字密码"
                        setPasswordState = 1
                    }
                }
            }
            2 -> {
                when (umodifyState) {
                    0 -> mActivity.finish()
                    1 -> {
                        //清空输入框
                        pv_code.clearAllPassword()
                        //隐藏顶部获取验证码按钮
                        cv_pay.visibility = View.GONE
                        //设置文字
                        tv_type_text.text = "请输入原支付密码"
                        pv_code.setForgetPasswordVisibility(true)
                        umodifyState = 0
                    }
                    2 -> {
                        //清空输入框
                        pv_code.clearAllPassword()
                        //设置文字
                        tv_type_text.text = "请重新设置6位数字密码"
                        pv_code.setForgetPasswordVisibility(false)
                        umodifyState = 1

                    }
                }
            }

        }


    }

}