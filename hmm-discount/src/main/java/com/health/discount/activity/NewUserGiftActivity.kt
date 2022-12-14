package com.health.discount.activity

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.app.hubert.guide.core.Controller
import com.app.hubert.guide.listener.OnGuideChangedListener
import com.health.discount.R
import com.health.discount.adapter.NewUserGiftGoodsAdapter
import com.health.discount.adapter.NewUserGiftServiceAdapter
import com.health.discount.contract.GiftDialogContract
import com.health.discount.presenter.GiftDialogPresenter
import com.healthy.library.BuildConfig
import com.healthy.library.LibApplication
import com.healthy.library.adapter.EmptyAdapter
import com.healthy.library.adapter.ItemDecorationAdapter
import com.healthy.library.adapter.ItemTitleAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.base.BaseAdapter
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.GuideViewHelp
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.constant.UrlKeys
import com.healthy.library.contract.GiftSelectContract
import com.healthy.library.dialog.GiftSelectShopListDialog
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.interfaces.IsNeedShare
import com.healthy.library.model.*
import com.healthy.library.net.RxLifecycleUtils
import com.healthy.library.presenter.ActionPresenterCopy
import com.healthy.library.presenter.GiftSelectPresenter
import com.healthy.library.routes.AppRoutes
import com.healthy.library.routes.DiscountRoutes
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.umeng.analytics.MobclickAgent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_new_user_gift.*
import java.util.concurrent.TimeUnit

/**
 * ??????????????????
 *
 * @author long
 * @date 2021-05-12
 */
@Route(path = DiscountRoutes.MINE_NEW_USER_GIFT)
class NewUserGiftActivity : BaseActivity(), IsFitsSystemWindows, BaseAdapter.OnOutClickListener,
    GiftDialogContract.View, IsNeedShare, GiftSelectContract.View {

    @Autowired
    @JvmField
    var birthday: String = ""

    @Autowired
    @JvmField
    var ytbAppId: String = ""

    @Autowired
    @JvmField
    var referralCode: String = ""

    @Autowired
    @JvmField
    var shopId: String = ""

    @Autowired
    @JvmField
    var departId: String = ""

    private var shopListDialog: GiftSelectShopListDialog? = null
    private val mMap = mutableMapOf<String, Any>()
    private val mSendGoods = mutableListOf<CouponUnder>()

    private var mPresenter: GiftDialogPresenter? = null
    private var mGiftSelectPresenter: GiftSelectPresenter? = null
    private var mServiceTitleAdapter: ItemTitleAdapter? = null
    private var mNewUserGiftServiceAdapter: NewUserGiftServiceAdapter? = null
    private var mItemDecorationAdapter: ItemDecorationAdapter? = null
    private var mGoodsTitleAdapter: ItemTitleAdapter? = null
    private var mNewUserGiftGoodsAdapter: NewUserGiftGoodsAdapter? = null
    private var mEmpty: EmptyAdapter? = null
    private var mShopList = mutableListOf<ShopDetailModel>()
    private var mShopListHasRequest = 0
    private var mServiceList = mutableListOf<Coupon>()
    private var mGoodsList = mutableListOf<Coupon>()
    private var mIsReceive = false

    private var mMaxSelCnt = 0
    private var surl = "" //????????????
    private var sdes = "??????????????????????????????????????????????????????????????????~" // ????????????
    private var stitle = "???????????????" // ????????????

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f) {//????????????
            resources
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun getResources(): Resources {
        val resources = super.getResources()
        if (resources.configuration.fontScale != 1f) {//????????????
            val newConfig = Configuration()
            newConfig.setToDefaults()
            resources.updateConfiguration(newConfig, resources.displayMetrics)
        }
        return resources
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_new_user_gift
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(mActivity)
        setStatusLayout(layout_status)
        layout_status.setmEmptyContent(
            "???????????????????????????${
                String(
                    Character.toChars(
                        Integer.parseInt(
                            "1F60A",
                            16
                        )
                    )
                )
            }"
        )
        // ??????????????????
        layout_refresh.finishLoadMoreWithNoMoreData()
        val virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)

        recycler.apply {
            layoutManager = virtualLayoutManager
            adapter = delegateAdapter
        }
        mPresenter = GiftDialogPresenter(mActivity, this)
        mGiftSelectPresenter = GiftSelectPresenter(mActivity, this)

        /***************** ????????? *************/
        mServiceTitleAdapter = ItemTitleAdapter()
        delegateAdapter.addAdapter(mServiceTitleAdapter)

        mNewUserGiftServiceAdapter = NewUserGiftServiceAdapter()
        mNewUserGiftServiceAdapter?.moutClickListener = this
        delegateAdapter.addAdapter(mNewUserGiftServiceAdapter)

        mItemDecorationAdapter = ItemDecorationAdapter()
        delegateAdapter.addAdapter(mItemDecorationAdapter)
        /***************** ????????? END *************/

        /***************** ????????? *************/
        mGoodsTitleAdapter = ItemTitleAdapter()
        delegateAdapter.addAdapter(mGoodsTitleAdapter)

        mNewUserGiftGoodsAdapter = NewUserGiftGoodsAdapter()
        mNewUserGiftGoodsAdapter?.moutClickListener = this
        delegateAdapter.addAdapter(mNewUserGiftGoodsAdapter)

        mEmpty = EmptyAdapter()
        delegateAdapter.addAdapter(mEmpty)
        /***************** ????????? END *************/

        birthday =
            if (TextUtils.isEmpty(birthday)) SpUtils.getValue(
                mContext,
                SpKey.USER_BIRTHDAY
            ) else birthday
        ytbAppId =
            if (TextUtils.isEmpty(ytbAppId) && !TextUtils.isEmpty(LocUtil.getytbAppId()))
                LocUtil.getytbAppId() else ytbAppId
        referralCode =
            if (TextUtils.isEmpty(referralCode)) SpUtils.getValue(
                mContext,
                SpKey.INSTALL_REL_CODE
            ) else referralCode
        departId =
            if (TextUtils.isEmpty(departId) && !TextUtils.isEmpty(LocUtil.getHmmDepartId()))
                LocUtil.getHmmDepartId() else departId
        shopId =
            if (TextUtils.isEmpty(shopId)) SpUtils.getValue(mContext, SpKey.CHOSE_SHOP) else shopId
        img_share.visibility = View.VISIBLE
        initListener()
        getData()
        ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                SimpleHashMapBuilder<String, Any>()
                        .putObject(MemberAction(
                                BuildConfig.VERSION_NAME,
                                1,
                                3,
                                "NewPeopleGiftShow",
                                activitySimpleName,
                                String(Base64.decode(SpUtils.getValue(application, SpKey.USER_ID).toByteArray(), Base64.DEFAULT)))
                        ) as MutableMap<String, Any>?)
    }

    private fun initListener() {

        img_back.setOnClickListener {
            finish()
        }

        img_go_home.setOnClickListener {
            ARouter.getInstance()
                .build(AppRoutes.APP_MAIN)
                .navigation()
        }

        img_share.setOnClickListener {
            var urlPrefix = "http://192.168.10.206:8000/page/couponsView.html"

            if (SpUtils.getValue(mContext, UrlKeys.H5_COUPONS_VIEW_URL).isNullOrEmpty().not()) {
                urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_COUPONS_VIEW_URL)
            }

            val url = String.format(
                "%s?scheme=BirthdayGift&ytbAppId=%s&birthday=%s&referralCode=%s&departId=%s&shopId=%s&type=8",
                urlPrefix,
                ytbAppId,
                birthday,
                referralCode,
                departId,
                shopId
            )
            surl = url
            val nokmap: MutableMap<String, String> = mutableMapOf()
            nokmap["soure"] = "??????????????????"
            MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap)
            showShare()
        }

        tv_all_in.setOnClickListener {
            if (mMaxSelCnt > 0) {
                if (mNewUserGiftServiceAdapter?.getCheckCount()!! < mMaxSelCnt) {
                    mNewUserGiftServiceAdapter?.moutClickListener?.outClick("tip", "???????????????????????????????????????")
                    return@setOnClickListener
                }
            }
            mSendGoods.clear()
            if (!ListUtil.isEmpty(mServiceList)) {
                mServiceList.forEachIndexed { index, coupon ->
                    if (coupon.ischeck) {
                        if (coupon.PersonID.isNullOrEmpty() && coupon.DepartID.isNullOrEmpty() && coupon.GoodsType == "0") {
                            //showToast("???????????????")
                            GuideViewHelp.showGuideRound(
                                mActivity,
                                "????????????",
                                true,
                                coupon.mGuideView,
                                R.layout.guide_user_gift_layout,
                                object : OnGuideChangedListener {
                                    override fun onShowed(controller: Controller) {}
                                    override fun onRemoved(controller: Controller) {

                                    }
                                })
                            return@setOnClickListener
                        }
                        val mCouponUnder = CouponUnder(coupon)
                        mSendGoods.add(mCouponUnder)
                    }
                }
                if (mMaxSelCnt == 0 && mServiceList.size > 0 && mNewUserGiftServiceAdapter?.getCheckCount() == 0) {
                    isAgree()
                    return@setOnClickListener
                }
            }
            /*** ?????????????????????
             * 2021-09-27 ?????????????????????
             *
             */
            if (!ListUtil.isEmpty(mGoodsList)) {
                mGoodsList.forEachIndexed { index, coupon ->
                    if (coupon.ischeck) {
                        val mCouponUnder = CouponUnder(coupon)
                        mSendGoods.add(mCouponUnder)
                    }
                }
            }
            mMap.clear()
            mMap["ytbAppId"] = ytbAppId
            mMap["departId"] = departId
            mMap["SendGoods"] = mSendGoods
            mMap["partnerId"] = SpUtils.getValue(mContext, SpKey.CHOSE_PARTNERID)

            ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                    SimpleHashMapBuilder<String, Any>()
                            .putObject(MemberAction(
                                    BuildConfig.VERSION_NAME,
                                    1,
                                    3,
                                    "NewPeopleAgreeDraw",
                                    activitySimpleName,
                                    String(Base64.decode(SpUtils.getValue(application, SpKey.USER_ID).toByteArray(), Base64.DEFAULT)))
                            ) as MutableMap<String, Any>?)

            mPresenter?.checkCouponGift(mMap)
        }

        /*** ??????/?????????????????? */
        layout_refresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mMaxSelCnt = 0
                getData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                refreshLayout.finishLoadMore(1500)
            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getData()
    }

    override fun onRequestFinish() {
        super.onRequestFinish()
        layout_refresh.finishRefresh()
        // ??????????????????
        layout_refresh.finishLoadMoreWithNoMoreData()
    }

    override fun getData() {
        mMap.clear()
        if (ListUtil.isEmpty(mShopList) && 0 == mShopListHasRequest) {
            mMap["shopId"] = shopId
            mGiftSelectPresenter?.getShopList(mMap)
            return
        }
        mMap["birthday"] = birthday
        mMap["ytbAppId"] = ytbAppId
        if (referralCode.isNullOrEmpty().not()) {
            mMap["referralCode"] = referralCode
        }
        mMap["departId"] = departId
        mMap["memberId"] = String(
            Base64.decode(
                SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                Base64.DEFAULT
            )
        )
        mPresenter?.getCouponList(mMap)
    }

    /**
     * ???????????????????????? ???????????????????????????
     * @return Boolean
     */
    fun onGetIsReceive(coupons: MutableList<Coupon>): Boolean {
        coupons.forEachIndexed { index, coupon ->
            //true ???????????????(???????????????) false ?????????
            if (coupon.isReceive == "1" || coupon.isReceive == "2") {
                return true
            }
        }
        return false
    }

    /**
     * ????????????
     */
    override fun onGetShopListSuccess(detialModel: MutableList<ShopDetailModel>) {
        mShopListHasRequest = 1
        mShopList.addAll(detialModel)
        getData()//?????????????????????
    }

    /**
     * ???????????????????????????
     */
    override fun onSuccessGetCouponList(coupons: MutableList<Coupon>?) {
        if (coupons.isNullOrEmpty()) {
            showEmpty()
            return
        }
        //var mAllPrice = 0.0
        mServiceList.clear()
        mGoodsList.clear()
        mIsReceive = onGetIsReceive(coupons)
        coupons.forEachIndexed { index, coupon ->
            if (coupon.SendMode == "1") {
                if (mMaxSelCnt == 0 && coupon.MaxSelCnt.isNullOrEmpty().not()) {
                    mMaxSelCnt = coupon.MaxSelCnt.toInt()
                }
                mServiceList.add(coupon)
            }
            if (coupon.SendMode == "0") {
                mGoodsList.add(coupon)
            }
            //mAllPrice += coupon.Price.toDouble()
        }

        /**
         * ???????????? ????????????????????????????????????????????????????????????????????????
         */
        for (coupon in mServiceList) {
            if (!ListUtil.isEmpty(mShopList)) {
                var mShops = mutableListOf<ShopDetailModel>()
                coupon.PersonInfo?.let {
                    it.forEachIndexed { index, personInfo ->
                        mShopList.forEachIndexed { index, newStoreDetialModel ->
                            if (personInfo.DepartID == newStoreDetialModel.ytbDepartID) {
                                newStoreDetialModel.PersonID = personInfo.PersonID
                                mShops.add(newStoreDetialModel)
                            }
                        }
                    }
                }
                if (!ListUtil.isEmpty(mShops) && mShops.size == 1 && !mIsReceive) {
                    coupon.DepartID = mShops[0].ytbDepartID
                    coupon.PersonID = mShops[0].PersonID
                    coupon.setCheckShopName(mShops[0].shopName)
                    coupon.setCheckShopId(mShops[0].id)
                }
            }
        }

        tv_all_price.text = "????????????VIP???????????????"//"????????????${mAllPrice}????????????"
        //??????????????????????????????
        bottom_view.visibility = if (mIsReceive) View.GONE else View.VISIBLE

        mEmpty?.setModel(null)
        if (ListUtil.isEmpty(mServiceList) && ListUtil.isEmpty(mGoodsList)) {
            //mEmpty?.setModel("")
            showEmpty()
            return
        }

        if (!SpUtils.getValue(mContext, "isShowUserGift", false)) {
            val imageView = ImageView(mContext)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.setImageResource(R.drawable.guide_user_gift_open_bg)
            val dialog = AlertDialog.Builder(mContext)
                .setView(imageView)
                .create()
            val window = dialog.window
            window?.let {
                window.setWindowAnimations(com.healthy.library.R.style.BottomDialogAnimation)
                val decorView = window.decorView
                decorView.setPadding(0, TransformUtil.dp2px(mContext, 120f).toInt(), 0, 0)
                decorView.setBackgroundColor(Color.TRANSPARENT)
                val params = window.attributes
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                params.gravity = Gravity.TOP
            }
            try {
                dialog.show()
            } catch (e: Exception) {
            }
            imageView.postDelayed({
                try {
                    dialog.dismiss()
                } catch (e: Exception) {
                }
            }, 2000)
            SpUtils.store(mContext, "isShowUserGift", true)
        }

        if (!ListUtil.isEmpty(mServiceList)) {
            val mServiceModel = ItemTitleModel()
            mServiceModel.title = "????????????"
            if (mMaxSelCnt > 0) {
                mServiceModel.titleRight = "???${mServiceList.size}???$mMaxSelCnt???"
            }
            mServiceModel.itemRemark = "???????????????????????????????????????????????????????????????????????????"
            mServiceTitleAdapter?.setModel(mServiceModel)
            mNewUserGiftServiceAdapter?.setCheckCount(0)
            mNewUserGiftServiceAdapter?.setMaxSelCnt(mMaxSelCnt)
            mNewUserGiftServiceAdapter?.setReceive(mIsReceive)
            mNewUserGiftServiceAdapter?.setData(mServiceList as ArrayList<Coupon>)
            mItemDecorationAdapter?.setModel("")
        }

        if (!ListUtil.isEmpty(mGoodsList)) {
            val mGoodsModel = ItemTitleModel()
            mGoodsModel.title = "????????????"
            mGoodsModel.titleRight = "??????????????????"
            mGoodsTitleAdapter?.setModel(mGoodsModel)
            mNewUserGiftGoodsAdapter?.setReceive(mIsReceive)
            mNewUserGiftGoodsAdapter?.setData(mGoodsList as ArrayList<Coupon>)
        }

    }

    override fun onSucessGiftCheck() {
        //??????1.5s????????????
        Observable.intervalRange(0, 1, 1.5.toLong(), 0, TimeUnit.SECONDS, Schedulers.io())
            .to(RxLifecycleUtils.bindLifecycle(this))
            .subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable?) {}

                override fun onNext(t: Long?) {}

                override fun onError(e: Throwable?) {}

                override fun onComplete() {
                    finish()
                }
            });
    }

    override fun outClick(function: String, obj: Any) {
        when (function) {
            "tip" -> {
                showToast(obj.toString())
            }
            "serviceShop" -> {
                ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                        SimpleHashMapBuilder<String, Any>()
                                .putObject(MemberAction(
                                        BuildConfig.VERSION_NAME,
                                        1,
                                        3,
                                        "NewPeopleChoseShop",
                                        activitySimpleName,
                                        String(Base64.decode(SpUtils.getValue(application, SpKey.USER_ID).toByteArray(), Base64.DEFAULT)))
                                ) as MutableMap<String, Any>?)
                val position = obj.toString().toInt()
                val coupon = mServiceList[position]
                shopListDialog?.let {
                    it.dismiss()
                }
                shopListDialog = GiftSelectShopListDialog.newInstance()
                shopListDialog?.setCoupon(coupon)
                shopListDialog?.setShopId(shopId)//????????????Id
                shopListDialog?.setShopModelResultListener(object :

                    GiftSelectShopListDialog.ShopModelResultListener {
                    override fun getShopModel(shopModel: ShopDetailModel) {
                        if (coupon.shopName == shopModel.shopName) {
                            return
                        }
                        coupon.setCheckShopName(shopModel.shopName)
                        coupon.setCheckShopId(shopModel.id)
                        mServiceList[position] = coupon
                        mNewUserGiftServiceAdapter?.notifyItemChanged(position)
                    }

                    override fun showTip(message: String) {
                        showToast(message)
                    }
                })
                shopListDialog?.show(supportFragmentManager, "shopListDialog")
            }
            "serviceChange" -> {
                val position = obj.toString().toInt()
                mServiceList[position] = mNewUserGiftServiceAdapter?.getCoupon()!!
                mNewUserGiftServiceAdapter?.notifyItemChanged(position)
            }
            "goodsChange" -> {
                val position = obj.toString().toInt()
                mGoodsList[position] = mNewUserGiftGoodsAdapter?.getCoupon()!!
                mNewUserGiftGoodsAdapter?.notifyItemChanged(position)
            }
        }
    }

    override fun getSurl(): String {
        return surl
    }

    override fun getSdes(): String {
        return sdes
    }

    override fun getStitle(): String {
        return stitle
    }

    override fun getsBitmap(): Bitmap? {
        return null
    }

    private fun isAgree() {
        StyledDialog.init(this)
        StyledDialog.buildIosAlert("??????", "??????????????????????????????", object : MyDialogListener() {
            override fun onFirst() {}
            override fun onThird() {
                super.onThird()
            }

            override fun onSecond() {
                mServiceList.forEachIndexed { index, coupon ->
                    coupon.SendMode = "0"
                    coupon.ischeck = true
                }
                mNewUserGiftServiceAdapter?.setCheckCount(mServiceList.size)
                mNewUserGiftServiceAdapter?.setMaxSelCnt(mMaxSelCnt)
                mNewUserGiftServiceAdapter?.setData(mServiceList as ArrayList<Coupon>)
            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0)
            .setBtnText("????????????", "????????????").show()
    }

}