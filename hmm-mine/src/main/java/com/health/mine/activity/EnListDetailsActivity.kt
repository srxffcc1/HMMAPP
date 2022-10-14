package com.health.mine.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import autodispose2.AutoDispose
import autodispose2.AutoDisposeConverter
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alipay.sdk.app.PayTask
import com.health.mine.R
import com.health.mine.adapter.EnlistDetailsBottomAdapter
import com.health.mine.adapter.EnlistDetailsBottomInfoAdapter
import com.health.mine.adapter.EnlistDetailsCenterAdapter
import com.health.mine.adapter.EnlistDetailsHeaderAdapter
import com.health.mine.contract.EnlistContract
import com.health.mine.presenter.EnListPresenter
import com.health.mine.widget.HanMomPayDialog
import com.healthy.library.LibApplication
import com.healthy.library.adapter.DecorationAdapter
import com.healthy.library.adapter.ItemLineAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.business.SeckShareDialog
import com.healthy.library.businessutil.ChannelUtil
import com.healthy.library.constant.Constants
import com.healthy.library.constant.Functions
import com.healthy.library.constant.Ids
import com.healthy.library.constant.SpKey
import com.healthy.library.dialog.QueryCodeDialog
import com.healthy.library.dialog.TongLianPhoneBindDialog
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.EnlistActivityModel
import com.healthy.library.model.PayResultModel
import com.healthy.library.model.TongLianMemberData
import com.healthy.library.net.RxThreadUtils
import com.healthy.library.routes.MineRoutes
import com.healthy.library.utils.*
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.hyb.library.PreventKeyboardBlockUtil
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.mine_activity_enlist_details.*
import kotlinx.android.synthetic.main.vip_adapter_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.contentView
import java.util.*

/**
 * @description 报名详情页面
 * @author long
 * @date 2021/7/23
 */
@Route(path = MineRoutes.MINE_ENLIST_DETAILS)
class EnListDetailsActivity : BaseActivity(), IsFitsSystemWindows, EnlistContract.View {

    @Autowired
    lateinit var id: String

    @Autowired // 是否报名 （默认：是 内容为空报名)
    lateinit var isEnlist: String

    private var countDownTimer: CountDownTimer? = null
    private var mSeconds = 0
    private lateinit var mResultIntent: Intent

    private lateinit var mEnListPresenter: EnListPresenter
    private val mParamMap: MutableMap<String, Any> = mutableMapOf()
    private var payInfoMap: MutableMap<String, Any>? = null
    private var mOnGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    private lateinit var virtualLayoutManager: VirtualLayoutManager
    private lateinit var delegateAdapter: DelegateAdapter
    private lateinit var mEnlistDetailsHeaderAdapter: EnlistDetailsHeaderAdapter
    private lateinit var mEnlistDetailsCenterAdapter: EnlistDetailsCenterAdapter
    private lateinit var mEnlistDetailsBottomAdapter: EnlistDetailsBottomAdapter
    private lateinit var mEnlistDetailsBottomInfoAdapter: EnlistDetailsBottomInfoAdapter
    private lateinit var itemLineAdapter: ItemLineAdapter

    private var mPayDialog: HanMomPayDialog? = null
    private var payStatus: Int = -1
    private var mResultModel: EnlistActivityModel? = null
    private var mPayType: String = ""
    private var payOrderId: String = ""
    private var isToPay = false //标志是否已经跳转到支付页面

    private var tongLianPhoneBindDialog: TongLianPhoneBindDialog? = null


    override fun getLayoutId(): Int {
        return R.layout.mine_activity_enlist_details
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)

        setStatusLayout(layout_status)
        top_bar.setSubmitColor(mContext.resources.getColor(R.color.color_444444))
        //详情页面禁止上拉加载
        refresh_layout.apply {
            finishLoadMoreWithNoMoreData()
        }
        mResultIntent = Intent()
        mResultIntent.putExtra("isLoadData", false)
        mEnListPresenter = EnListPresenter(mActivity, this)
        buildRecyclerHelper()
        getData()
        initListener()
    }


    override fun getData() {
        super.getData()
        mParamMap.clear()
        mParamMap[if (::isEnlist.isInitialized) "id" else "enlistActivityId"] = id
        mParamMap[Functions.FUNCTION] = if (::isEnlist.isInitialized) "t_100104" else "t_100109"
        mEnListPresenter.getEnlistDetails(mParamMap)
    }

    private fun buildRecyclerHelper() {
        virtualLayoutManager = VirtualLayoutManager(mContext)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        rv.layoutManager = virtualLayoutManager
        rv.adapter = delegateAdapter

        mEnlistDetailsHeaderAdapter = EnlistDetailsHeaderAdapter()
        delegateAdapter.addAdapter(mEnlistDetailsHeaderAdapter)

        itemLineAdapter = ItemLineAdapter()
        delegateAdapter.addAdapter(itemLineAdapter)

        mEnlistDetailsCenterAdapter = EnlistDetailsCenterAdapter()
        delegateAdapter.addAdapter(mEnlistDetailsCenterAdapter)

        val decorationAdapter = DecorationAdapter()
        decorationAdapter.setModel("")
        delegateAdapter.addAdapter(decorationAdapter)

        if (::isEnlist.isInitialized.not()) {
            //是报名状态
            mEnlistDetailsBottomAdapter = EnlistDetailsBottomAdapter()
            delegateAdapter.addAdapter(mEnlistDetailsBottomAdapter)
        } else {
            mEnlistDetailsBottomInfoAdapter = EnlistDetailsBottomInfoAdapter()
            delegateAdapter.addAdapter(mEnlistDetailsBottomInfoAdapter)
        }

        delegateAdapter.addAdapter(decorationAdapter)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {

//        if (::isEnlist.isInitialized.not()) {
//            mOnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
//                mActivity?.let {
//                    val r = Rect()
//                    contentView?.getWindowVisibleDisplayFrame(r)
//                    val screenHeight: Int = contentView?.rootView?.height!!
//                    val heightDifference: Int = (screenHeight
//                            - (r.bottom - r.top))
//                    mEnlistDetailsBottomAdapter?.let { it.changeTabs(heightDifference > 150) }
//                    if (heightDifference > 150) {
//                        // 显示键盘，并在这里重新让RecyclerView滚动到底部
//                        rv.scrollBy(0,2);
//                    } else {
//                        // 隐藏键盘, 没办法高度低于100的我就把它当成键盘隐藏了
//                        //keyboardHeight = heightDifference
//                        //由于全局已经监听 关闭软键盘了没有必要再次调用
//                    }
//                }
//            }
//            //报名状态才使用一下内容
//            contentView?.viewTreeObserver?.addOnGlobalLayoutListener(mOnGlobalLayoutListener)
//        }

        top_bar.setSubmitListener {
            /** 分享 */
            val mShareDialog = SeckShareDialog.newInstance()
            mShareDialog.setActivityDialog(5, 0, mResultModel?.enlistActivity)
            mShareDialog.mShopLogo = mResultModel?.enlistActivity?.merchantLogoUrl
            mShareDialog.show(supportFragmentManager, "shareDialog")
        }

        /** 取消报名活动 */
        tv_clear_action.setOnClickListener {
            isAgree("", "是否确定取消此活动！")
        }

        tv_confirm_action.setOnClickListener {
            val tongLianMemberData = ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(), SpKey.TONGLIANBIZUSER), TongLianMemberData::class.java)
            if (tongLianMemberData != null && tongLianMemberData.memberInfo.isPhoneChecked) { //绑定了

                buildData()
            } else {
                if (tongLianPhoneBindDialog == null) {
                    tongLianPhoneBindDialog = TongLianPhoneBindDialog.newInstance()
                }
                tongLianPhoneBindDialog!!.show(supportFragmentManager, "手机绑定")
                tongLianPhoneBindDialog!!.setOnDialogAgreeClickListener {
                    buildData()
                }
            }
        }

        refresh_layout.setOnRefreshListener {
            getData()
        }
    }

    private fun buildData() {
        mPayType = ""
        /** 报名流程走向 */
        if (::isEnlist.isInitialized.not()) {
            if (mEnlistDetailsBottomAdapter.editEnlistName.isNullOrEmpty() || mEnlistDetailsBottomAdapter.editEnlistPhone.isNullOrEmpty()
            /*|| mEnlistDetailsBottomAdapter.enlistChooseStatus.isNullOrEmpty()*/
            ) {
                showToast("请完善信息！")
                return
            }
            if (!CheckUtils.checkPhone(mEnlistDetailsBottomAdapter.editEnlistPhone)) {
                showToast("请输入正确的手机号")
                return
            }

            if (mResultModel?.enlistActivity?.isFree == 0 && mResultModel?.enlistActivity?.price!! > 0) {
                if (mPayDialog == null) {
                    mPayDialog = HanMomPayDialog.newInstance()
                    mPayDialog?.setPayTypeListener {
                        mPayType = it
                        addEnlistFunction()
                    }
                }
                mPayDialog?.setPrice(mResultModel?.enlistActivity?.price.toString())
                    ?.setType(1)
                mPayDialog?.show(supportFragmentManager, "payDialog")
                return
            }
            addEnlistFunction()
            return
        }

        /** 列表详情处理走向 */
        if (payStatus == 0) {
            mResultModel?.let {
                if (mPayDialog == null) {
                    mPayDialog = HanMomPayDialog.newInstance()
                    mPayDialog?.setPayTypeListener {
                        mPayType = it
                        payOrderId = mResultModel!!.payOrderId
                        if (payOrderId.isEmpty()) {
                            showToast("未获取到payOrderId")
                        } else {
                            buildPay()
                        }
                    }
                }
                mPayDialog?.setPrice(mResultModel?.enlistActivity?.price.toString())
                    ?.setType(1)
                mPayDialog?.show(supportFragmentManager, "payDialog")
            }
            return
        }

        if (payStatus == 3) {
            /*val mCode =
                "verificationCode=${mResultModel?.verificationCode}&&enlistActivityId=${mResultModel?.enlistActivity?.id}"*/
            val mCode =
                "{\"enlistActivityId\":\"${mResultModel?.enlistActivity?.id}\",\"verificationCode\":\"${mResultModel?.verificationCode}\"}"
            /** 查看核销二维码 */
            QueryCodeDialog.newInstance(mCode).show(supportFragmentManager, "queryCode")
        }
    }

    /**
     * 取消报名活动确认弹窗
     */
    private fun isAgree(title: String, msg: String) {
        StyledDialog.init(mContext)
        StyledDialog.buildIosAlert(
            title,
            msg,
            object : MyDialogListener() {
                override fun onFirst() {}
                override fun onThird() {
                    super.onThird()
                }

                override fun onSecond() {
                    mParamMap.clear()
                    mParamMap["id"] = id
                    mEnListPresenter.removeEnlist(mParamMap)
                }
            }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0)
            .setBtnText("取消", "确定")
            .show()
    }

    /**
     * 添加报名信息 方法
     */
    private fun addEnlistFunction() {
        mParamMap.clear()
        mParamMap["enlistActivityId"] = id
        mParamMap["enlistName"] = mEnlistDetailsBottomAdapter.editEnlistName
        mParamMap["enlistPhone"] = mEnlistDetailsBottomAdapter.editEnlistPhone
        mParamMap["enlistSex"] =
            mEnlistDetailsBottomAdapter.editEnlistSex.toString()
        mParamMap["enlistStage"] =
            mEnlistDetailsBottomAdapter.enlistChooseStatus
        mEnListPresenter.addEnlist(mParamMap)
    }

    override fun getEnListSuccess(resultData: MutableList<EnlistActivityModel>, isMore: Boolean) {}

    /**
     * 获取报名活动详情
     */
    override fun getEnlistDetailsSuccess(resultModel: EnlistActivityModel?) {
        if (resultModel == null) {
            showDataErr()
            return
        }
        //页面公用 适配同城圈详情数据结构
        if (::isEnlist.isInitialized.not()) {
            resultModel.enlistActivity = resultModel
        }
        if (resultModel.enlistActivity == null) {
            showDataErr()
            return
        }

        mResultModel = resultModel
        payStatus = resultModel.payStatus
        /** 当前状态不为“未支付” 且定时器已经初始化*/
        if (payStatus != 0 && countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
        top_bar.setTitle(
            when (payStatus) {
                0 -> "未支付" + (if (resultModel.autoCancelSeconds > 0) {
                    downTimer(resultModel.autoCancelSeconds)
                } else {
                    ""
                })
                1 -> "已取消"
                2 -> "已退款"
                3 -> "待核销"
                4 -> "已完成"
                else -> "活动报名"
            }
        )

        val mEnlistEndTime = DateUtils.str2Long(
            resultModel.enlistActivity?.enlistEndTime,
            DateUtils.DATE_FORMAT_PATTERN_YMD_HMS
        )

        ll_mine_bottom_action.visibility = when {
            ::isEnlist.isInitialized.not() -> if (mEnlistEndTime < System.currentTimeMillis()) View.GONE else View.VISIBLE
            payStatus == 3 -> View.VISIBLE
            payStatus == 0 -> View.VISIBLE
            //payStatus == 0 -> if (mEnlistEndTime < System.currentTimeMillis()) View.GONE else View.VISIBLE
            else -> View.GONE
        }

        if (payStatus == 0 || ::isEnlist.isInitialized.not()) {
            tv_clear_action.visibility = View.GONE
            val layoutParams = tv_confirm_action.layoutParams
            if (layoutParams is ConstraintLayout.LayoutParams) {
                layoutParams.leftMargin = TransformUtil.dp2px(mContext, 20f).toInt()
                layoutParams.marginStart = TransformUtil.dp2px(mContext, 20f).toInt()
            }
            tv_confirm_action.layoutParams = layoutParams

            tv_confirm_action.text = when {
                payStatus == 0 -> "去支付"
                ::isEnlist.isInitialized.not() -> if (resultModel.enlistActivity?.isFree == 0) "报名并支付" else "报名"
                else -> "报名"
            }
            tv_confirm_action.shapeDrawableBuilder
                .setTopLeftRadius(TransformUtil.dp2px(mContext, 22f))
                .setBottomLeftRadius(TransformUtil.dp2px(mContext, 22f))
                .intoBackground()
            /* tv_confirm_action.topLeftRadius = TransformUtil.dp2px(mContext, 22f).toInt()
             tv_confirm_action.bottomLeftRadius = TransformUtil.dp2px(mContext, 22f).toInt()
             tv_confirm_action.intoBackground()*/
        }

        if (payStatus == 3) {
            tv_clear_action.visibility = View.VISIBLE
            val layoutParams = tv_confirm_action.layoutParams
            if (layoutParams is ConstraintLayout.LayoutParams) {
                layoutParams.leftMargin = 0
                layoutParams.marginStart = 0
            }
            tv_confirm_action.layoutParams = layoutParams
            tv_confirm_action.text = "查看核销码"
            tv_confirm_action.shapeDrawableBuilder
                .setTopLeftRadius(0f)
                .setBottomLeftRadius(0f)
                .intoBackground()
        }

        mEnlistDetailsHeaderAdapter.setModel(resultModel)
        itemLineAdapter.setModel("")
        resultModel.enlistActivity?.let {
            mEnlistDetailsCenterAdapter.setModel(it.description)
        }

        if (::isEnlist.isInitialized.not()) {
            if (mEnlistEndTime > System.currentTimeMillis()) {
                mEnlistDetailsBottomAdapter.setModel(resultModel)
            }
        } else {
            mEnlistDetailsBottomInfoAdapter.setModel(resultModel)
        }

        virtualLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    override fun addEnlistSuccess(id: String, payOrderId: String) {
        if (mResultModel?.enlistActivity?.isFree == 0 && mPayType.isNullOrEmpty()
                .not() && payOrderId.isNullOrEmpty().not()
        ) {
            this.payOrderId = payOrderId
            buildPay()
            return
        }
        showToast("报名成功!")
        ARouter.getInstance()
            .build(MineRoutes.MINE_ENLIST)
            .navigation()
        finish()
    }

    private fun buildPay() {
        mPayDialog!!.dismiss()
        if (mPayType == Constants.PAY_IN_A_LI) {
            mParamMap.clear()
            mParamMap["payOrderId"] = payOrderId
            mParamMap["payType"] = mPayType
            mEnListPresenter.getPayInfo(mParamMap)
        }
        if (mPayType == Constants.PAY_IN_WX) {
            goWeiXinPay(payOrderId)
        }
    }

    private fun goWeiXinPay(payId: String) {
        val appId = Ids.WX_APP_ID
        val api = WXAPIFactory.createWXAPI(this, appId)
        val req = WXLaunchMiniProgram.Req()
        req.userName = Ids.WEIXIN_PAY_ID
        req.path = "/pages/wxpay/wxpay?fromPlace=1&payId=$payId"
        if (ChannelUtil.isIpRealRelease()) {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE
        } else {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW
        }
        api.sendReq(req)
        isToPay = true
    }

    override fun onResume() {
        super.onResume()
        if (isToPay) {
            showLoading()
            Handler().postDelayed({ //延迟调用  防止通联那边回调不及时
                mEnListPresenter.getPayStatus(payOrderId)
            }, 1000)
        }
        Handler().postDelayed({
            PreventKeyboardBlockUtil.getInstance().setContext(mActivity).setBtnView(tmpBottom)
                .register()
        }, 1500)
    }

    override fun removeEnlistSuccess() {
        showToast("取消成功!")
        getData()
    }
    override fun onPause() {
        super.onPause()
        PreventKeyboardBlockUtil.getInstance().setContext(mActivity).unRegister()
    }

    override fun onGetPayInfoSuccess(url: String) {
        if (url != null) {
            if (url.startsWith("http") || url.startsWith("https")) {
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                mContext.startActivity(intent)
                isToPay = true
            } else {
                showToast("payInfo错误$url")
            }
        } else {
            showToast("获取支付信息错误")
        }
//        this.payInfoMap = map
//        val payType = map["payType"].toString()
//        if (Constants.PAY_IN_A_LI == payType) {
//            payByAli(map["ali"].toString())
//        } else if (Constants.PAY_IN_WX == payType) {
//            val iwxapi = WXAPIFactory.createWXAPI(mContext, null)
//            iwxapi.registerApp(map["appId"].toString())
//            val request = PayReq()
//            request.appId = map["appId"].toString()
//            request.partnerId = map["partnerId"].toString()
//            request.prepayId = map["prepayId"].toString()
//            request.packageValue = map["packageValue"].toString()
//            request.nonceStr = map["nonceStr"].toString()
//            request.timeStamp = map["timeStamp"].toString()
//            request.sign = map["sign"].toString()
//            iwxapi.sendReq(request)
//        }
//        mPayDialog?.dismiss()
    }

    override fun getPayStatusSuccess(status: String?) {
        if (status.isNullOrEmpty().not() && status == "4") {
            showToast("支付成功")
            Handler().postDelayed({
                if (::isEnlist.isInitialized.not()) {
                    ARouter.getInstance()
                        .build(MineRoutes.MINE_ENLIST)
                        .navigation()
                    finish()
                } else {
                    getData()
                }
            }, 500)
        } else {
            showToast("支付失败")
            isToPay = false
        }
    }

    /**
     * 微信支付支付结果通知
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getWxPayResult(model: PayResultModel) {
        when (model.errCode) {
            0 -> {
                showToast("支付成功")
                Handler().postDelayed({
                    if (::isEnlist.isInitialized.not()) {
                        ARouter.getInstance()
                            .build(MineRoutes.MINE_ENLIST)
                            .navigation()
                        finish()
                    } else {
                        getData()
                    }
                }, 500)
            }
            -2 -> {
                showToast("支付失败")
            }
            else -> {
                showToast("支付失败")
            }
        }
    }

    /**
     * 拉起支付宝
     */
    fun payByAli(orderInfo: String) {
        val autoDisposable: AutoDisposeConverter<Map<String, String>> = AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider.from(
                this@EnListDetailsActivity,
                Lifecycle.Event.ON_DESTROY
            )
        )

        Observable.create<Map<String, String>> { emitter ->
            val aliPay = PayTask(this)
            val result = aliPay.payV2(orderInfo, true)
            emitter.onNext(result)
            emitter.onComplete()
        }
            .compose(RxThreadUtils.Obs_io_main())
            .to(autoDisposable)
            .subscribe(object : Observer<Map<String, String>> {
                override fun onSubscribe(d: Disposable) {
                    addDisposable(d)
                }

                override fun onNext(map: Map<String, String>) {
                    var isSuccess = false
                    Handler().postDelayed({
                        when (map["resultStatus"]) {
                            "9000" -> {
                                //成功
                                showToast("支付成功")
                                isSuccess = true
                            }
                            "6001" -> {
                                isSuccess = false
                            }
                            else -> {
                                isSuccess = false
                            }
                        }
                        if (isSuccess) {
                            Handler().postDelayed({
                                if (::isEnlist.isInitialized.not()) {
                                    ARouter.getInstance()
                                        .build(MineRoutes.MINE_ENLIST)
                                        .navigation()
                                    finish()
                                } else {
                                    getData()
                                }
                            }, 500)
                        } else {
                            showToast("支付失败")
                        }
                        //后续对应操作
                    }, 200)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    ////System.out.println("进来了4");
                }

                override fun onComplete() {
                    ////System.out.println("进来了5");
                }
            })
    }

    override fun onRequestFinish() {
        super.onRequestFinish()
        refresh_layout.finishRefresh()
        refresh_layout.finishLoadMoreWithNoMoreData()
    }

    /**
     * 倒计时
     *
     * @param time
     */
    private fun downTimer(time: Long) {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        } else {
            //后台返回距离自动取消剩余：秒数
            mSeconds = time.toString().toInt()
        }
        if (mSeconds > 0) {
            countDownTimer = object : CountDownTimer((mSeconds * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var millisUntilFinished = millisUntilFinished
                    millisUntilFinished = millisUntilFinished / 1000
                    mSeconds = millisUntilFinished.toInt()
                    //val hours = (millisUntilFinished / (60 * 60)).toInt()
                    val minutes = millisUntilFinished.toInt() / 60 % 60
                    val seconds = millisUntilFinished.toInt() % 60
                    top_bar.setTitle(
                        "未支付 剩余 " +
                                /*String.format("%02d", Math.max(0, hours)) + ":"
                                + */String.format("%02d", Math.max(0, minutes)) + ":"
                                + String.format("%02d", Math.max(0, seconds))
                    )
                }

                override fun onFinish() {
                    //剩余支付时间结束后进行相应逻辑处理
                    top_bar.setTitle("未支付")
                    mResultIntent.putExtra("isLoadData", true)
                    getData()
                }
            }.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
        mOnGlobalLayoutListener?.let {
            contentView?.viewTreeObserver?.removeOnGlobalLayoutListener(mOnGlobalLayoutListener)
            mOnGlobalLayoutListener = null
        }
        mEnlistDetailsCenterAdapter.onDestroy()
    }
}