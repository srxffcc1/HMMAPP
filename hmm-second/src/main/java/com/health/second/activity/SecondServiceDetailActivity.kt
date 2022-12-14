package com.health.second.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.health.second.BuildConfig
import com.health.second.R
import com.health.second.adapter.*
import com.health.second.contract.SecondGoodsContract
import com.health.second.contract.SecondShopContract
import com.health.second.presenter.SecondGoodsPresenter
import com.health.second.presenter.SecondShopPresenter
import com.healthy.library.base.BaseActivity
import com.healthy.library.base.BaseAdapter
import com.healthy.library.business.SeckShareDialog
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.Constants
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.dialog.NavigationSelectDialog
import com.healthy.library.dialog.SecondGoodsSpecDialog
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.*
import com.healthy.library.model.GoodsBasketCell.GoodsMarketing
import com.healthy.library.routes.DiscountRoutes
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.routes.ServiceRoutes
import com.healthy.library.utils.PhoneUtils
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.StringUtils
import com.healthy.library.utils.TransformUtil
import kotlinx.android.synthetic.main.activity_second_service_detail.*
import java.util.*


/**
 * @description ??????????????????
 * @author long
 * @createTime 2021/10/09
 */
@Route(path = SecondRoutes.SECOND_SERVICE_DETAIL)
class SecondServiceDetailActivity : BaseActivity(), IsFitsSystemWindows,
    BaseAdapter.OnOutClickListener, SecondGoodsContract.View, SecondShopContract.View {
    @Autowired
    @JvmField
    var shopId: String = ""//????????????

    @Autowired
    @JvmField
    var marketingType: String = ""//????????????????????????

    @Autowired
    @JvmField
    var isNtReal: String = "0" //?????????????????????

    @Autowired
    @JvmField
    var id: String = "" // ?????????????????? ??????id

    @Autowired
    @JvmField
    var goodsId: String = ""

    @Autowired
    @JvmField
    var merchantId: String = ""

    @Autowired
    @JvmField
    var goodsShopId: String = ""//???????????????????????? id

    @Autowired
    @JvmField
    var barcodeSku: String = ""//???????????????

    @Autowired
    @JvmField
    var assembleId: String = ""

    @Autowired
    @JvmField
    var bargainId: String = ""

    @Autowired
    @JvmField
    var bargainMemberId: String = ""

    @Autowired
    @JvmField
    var teamNum: String = ""

    private var mGoodsPresenter: SecondGoodsPresenter? = null
    private var mShopPresenter: SecondShopPresenter? = null
    private lateinit var virtualLayoutManager: VirtualLayoutManager
    private lateinit var delegateAdapter: DelegateAdapter
    private lateinit var mGoodsEmptyAdapter: SecondGoodsEmptyAdapter
    private lateinit var mBannerAdapter: ServiceDetailBannerAdapter
    private lateinit var mPriceAdapter: ServiceDetailPriceAdapter
    private lateinit var mAssembleTeamAdapter: ServiceAssembleTeamAdapter
    private lateinit var mStoreDetailAdapter: ServiceStoreDetailAdapter
    private lateinit var mStoreRecommendAdapter: ServiceStoreRecommendAdapter
    private lateinit var mGoodsDetailAdapter: ServiceGoodsDetailAdapter

    private val mParamsMap = mutableMapOf<String, Any>()
    private val mParamsShopMap = mutableMapOf<String, Any>()
    private val mNetWorkShopList = mutableListOf<ShopDetailModel>()
    private val mShopList = mutableListOf<ShopDetailModel>()
    private var exSkuList: MutableList<GoodsSpecDetail>? = null
    private var mCheckStoreModel: ShopDetailModel? = null
    private var mGoodsDetail: GoodsDetail? = null
    private var mGoodsDetailKick: Goods2DetailKick? = null
    private var mGoodsDetailPin: Goods2DetailPin? = null
    private var mGoodsSpecDetail: GoodsSpecDetail? = null
    private var mGoodsSpecCells: MutableList<GoodsSpecCell>? = null
    private var mGoodsSpecDialog: SecondGoodsSpecDialog? = null

    private var isGetShopList = false
    private var isZero = false
    private var isMarkZero = false

    private var mGoodsSpecId: String? = ""
    private var teamNumOrg: String = ""
    private var mGoodsQuantity: Int? = 0


    /*** ???????????? */
    private val mShopTitleName = "????????????"
    private val mShopRecommendName = "????????????"
    private val mGoodsDetailName = "????????????"
    private val titles = mutableListOf<String>()
    private val mTabEntities = ArrayList<CustomTabEntity>()

    /*** ??????Y??? */
    private var mBannerHeight = 0
    private var mAssembleTeamHeight = 0
    private var mRecommendHeight = 0
    private var mHeaderHeight = 0
    private var mCenterHeight = 0
    private var mFooterHeight = 0

    /*** 0 ?????? 1 ?????? 2 ?????? 3 ?????? ???4 ????????????????????????????????? ?????????????????????*/
    private var mGoodsType = 0

    /*** Y??????????????? */
    private var mScrollDy = 0

    /*** ???????????? */
    private var isScrolled = false

    private var firstPageSize: Int = 0

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.fontScale != 1f) {
            //????????????
            resources
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun getResources(): Resources? {
        val res = super.getResources()
        if (res.configuration.fontScale != 1f) { //????????????
            val newConfig = Configuration()
            newConfig.setToDefaults() //????????????
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_second_service_detail
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        setStatusLayout(layout_status)
        layout_refresh.finishLoadMoreWithNoMoreData()

        if (shopId.isNullOrEmpty()||"0".equals(shopId)) {
            shopId = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)
        }
        teamNumOrg=teamNum;//?????????????????????teamNum
        goodsShopId = "0"//??????????????????????????????0
        if (merchantId.isNullOrEmpty()) {
            merchantId = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
        }
        if (id.isNullOrEmpty()) {
            id = goodsId
        }
        mGoodsPresenter = SecondGoodsPresenter(mContext, this)
        mShopPresenter = SecondShopPresenter(mContext, this)
        //????????????
        mGoodsSpecDialog = SecondGoodsSpecDialog.newInstance()
        getData()
    }

    override fun findViews() {
        super.findViews()
        buildRecyclerHelper()
        initListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPriceAdapter.cancelTimer()
        mGoodsDetailAdapter.onDestroy()
    }

    /**
     * ?????????tab?????????
     */
    fun initTab() {
        if (topTab.tabCount == titles.size) return
        mTabEntities.clear()
        for (i in titles.indices) {
            //??????tab??????
            mTabEntities.add(TabEntity(titles[i], 0, 0))
        }
        topTab.setTabData(mTabEntities)
    }

    override fun getData() {
        super.getData()
        mParamsMap.clear()
        when {
            bargainId.isNullOrEmpty().not() -> {
                /*** 7051 */
                mParamsMap["bargainId"] = bargainId
                mParamsMap["shopId"] = goodsShopId
                mGoodsPresenter?.getGoodsDetailKick(mParamsMap);

                if (goodsShopId != "0"&&!bargainId.isNullOrEmpty()) {
                    mParamsMap.clear()
                    mParamsMap["marketingId"] = bargainId
                    mParamsMap["shopId"] = goodsShopId
                    mParamsMap[Functions.FUNCTION] = "7053"
                    mGoodsPresenter?.getGoodsDetailSkuEx(mParamsMap)
                }
            }
            assembleId.isNullOrEmpty().not() -> {
                /*** 9014 */
                mParamsMap["assembleId"] = assembleId
                mParamsMap["shopId"] = goodsShopId
                mGoodsPresenter?.getGoodsDetailPin(mParamsMap)
                if (goodsShopId != "0"&&!assembleId.isNullOrEmpty()) {
                    mGoodsPresenter?.getTeamList(mParamsMap)
                    mParamsMap.clear()
                    mParamsMap["marketingId"] = assembleId
                    mParamsMap["shopId"] = goodsShopId
                    mParamsMap[Functions.FUNCTION] = "9017"
                    mGoodsPresenter?.getGoodsDetailSkuEx(mParamsMap)
                }
            }
            else -> {
                /*** 9202 */
                mParamsMap["marketingType"] = marketingType
                mParamsMap["shopId"] = goodsShopId
                mParamsMap["id"] = id
                mGoodsPresenter?.getGoodsDetail(mParamsMap)
            }
        }
    }

    /**
     * ??????????????????
     */
    private fun getRecommend() {
        mParamsMap.clear()
        mParamsMap["shopId"] = goodsShopId
        mParamsMap["type"] = "4"
        mParamsMap["pageNum"] = "1"
        mParamsMap["pageSize"] = "4"
        mParamsMap["firstPageSize"] = firstPageSize.toString()
        mGoodsPresenter?.getRecommendList(mParamsMap)
    }

    private fun initListener() {
        img_back.setOnClickListener {
            outClick("back", "")
        }
        img_share.setOnClickListener {
            outClick("share", "")
        }
        tv_bask_action.setOnClickListener {
            outClick("baskAction", "")
        }
        tv_confirm_action.setOnClickListener {
            outClick("confirmAction", "")
        }
        itv_merchantPhone.setOnClickListener {
            if (mCheckStoreModel == null) {
                showToast("????????????????????????????????????????????????")
            } else {
                outClick("phone", mCheckStoreModel?.appointmentPhone.toString())
            }
        }
        layout_refresh.setOnRefreshListener {
            mRecommendHeight = 0
            mHeaderHeight = 0
            mCenterHeight = 0
            mFooterHeight = 0
            if (topTab.tabCount != 0) {
                topTab.currentTab = 0
            }
            getData()
            it.finishRefresh()
            it.finishLoadMoreWithNoMoreData()
        }

        topTab.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                buildHeaderAlpha(1f)
                var scrollPosition = 0
                var offset = 0 //????????? ??????????????????????????? ????????????????????????
                when (position) {
                    0 -> {
                        scrollPosition =
                            if (ListUtil.isEmpty(mAssembleTeamAdapter.getTeamList())) position + 1 else 2
                        offset = if (mGoodsType == 0) {
                            96
                        } else {
                            if (ListUtil.isEmpty(mAssembleTeamAdapter.getTeamList())) -60 else 70
                        }
                        mScrollDy = mHeaderHeight
                    }
                    1 -> {
                        scrollPosition =
                            if (ListUtil.isEmpty(mAssembleTeamAdapter.getTeamList())) position + 1 else 3
                        offset = -75
                        mScrollDy = mCenterHeight
                    }
                    2 -> {
                        scrollPosition =
                            if (ListUtil.isEmpty(mAssembleTeamAdapter.getTeamList())) 3 else 4
                        offset = -340
                        mScrollDy = mFooterHeight
                    }
                }
                virtualLayoutManager.scrollToPositionWithOffset(scrollPosition, offset)
            }

            override fun onTabReselect(position: Int) {}
        })

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val b = recyclerView.canScrollVertically(-1)
                if (!b || mScrollDy < 0) {
                    mScrollDy = 0
                }
                mScrollDy += dy
                val mTargetHeight =
                    mBannerHeight - topTitle.height - topView.height * 2 - topTab.height
                val alpha =
                    (mScrollDy * 1 * 1.0f / mTargetHeight).coerceAtMost(1f)

                buildHeaderAlpha(alpha)
                val findFirstVisibleItemPosition =
                    virtualLayoutManager.findFirstVisibleItemPosition()
                if (isScrolled) {
                    var pos = 0
                    if (BuildConfig.DEBUG) {
                        Log.e(
                            "Detail",
                            "onScrolled:findFirstVisibleItemPosition = $findFirstVisibleItemPosition; dy = $dy; mScrollDy = $mScrollDy;mHeaderHeight = $mHeaderHeight;mCenterHeight = $mCenterHeight;mFooterHeight = $mFooterHeight"
                        )
                    }
                    if (mScrollDy >= mHeaderHeight) {
                        pos = 0
                    }

                    if (mScrollDy >= mCenterHeight) {
                        pos = if (titles.contains(mShopTitleName)) 1 else 0
                    }

                    if (mScrollDy >= mFooterHeight && mFooterHeight != 0) {
                        pos = when (titles.size) {
                            3 -> 2
                            2 -> 1
                            else -> 0
                        }
                    }
                    if (topTab.tabCount != 0) {
                        topTab.currentTab = pos
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolled = newState != 0
            }
        })
    }

    /**
     * ?????????????????????
     * @param alpha ?????????
     */
    private fun buildHeaderAlpha(alpha: Float) {
        img_share.apply {
            setImageResource(if (alpha == 1.0f) R.drawable.icon_second_bank_share else R.drawable.icon_second_white_share)
        }
        if (alpha.isNaN().not()) {
            topView.alpha = alpha
            topTitle.alpha = alpha
            iv_top_shader.alpha = alpha
            if (topTab.tabCount > 0) {
                topTab.alpha = alpha
            }
        }
    }

    private fun buildRecyclerHelper() {
        virtualLayoutManager = VirtualLayoutManager(mContext)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recyclerView.layoutManager = virtualLayoutManager
        recyclerView.adapter = delegateAdapter

        /*** ????????????????????? */
        mGoodsEmptyAdapter = SecondGoodsEmptyAdapter()
        delegateAdapter.addAdapter(mGoodsEmptyAdapter)

        /*** ?????????????????? banner */
        mBannerAdapter = ServiceDetailBannerAdapter()
        delegateAdapter.addAdapter(mBannerAdapter)

        /*** ?????????????????? ?????? */
        mPriceAdapter = ServiceDetailPriceAdapter()
        mPriceAdapter.moutClickListener = this
        delegateAdapter.addAdapter(mPriceAdapter)

        /*** ???????????? ???????????? */
        mAssembleTeamAdapter = ServiceAssembleTeamAdapter()
        mAssembleTeamAdapter.moutClickListener = this
        delegateAdapter.addAdapter(mAssembleTeamAdapter)

        /*** ???????????? ?????????????????? */
        mStoreDetailAdapter = ServiceStoreDetailAdapter()
        mStoreDetailAdapter.moutClickListener = this
        delegateAdapter.addAdapter(mStoreDetailAdapter)

        /*** ???????????? ???????????? */
        mStoreRecommendAdapter = ServiceStoreRecommendAdapter()
        mStoreRecommendAdapter.moutClickListener = this
        delegateAdapter.addAdapter(mStoreRecommendAdapter)

        /*** ???????????? ???????????? */
        mGoodsDetailAdapter = ServiceGoodsDetailAdapter()
        mGoodsDetailAdapter.moutClickListener = this
        delegateAdapter.addAdapter(mGoodsDetailAdapter)

        /*** ???????????? ?????????????????? */
        setScrollY()
    }

    /**
     * ????????????????????????
     */
    private fun setScrollY() {
        mBannerAdapter.getItemHeight {
            mBannerHeight = it
        }
        mPriceAdapter.getItemHeight {
            mHeaderHeight =
                mBannerHeight + it + mAssembleTeamHeight - topTitle.height - topView.height - topTab.height
            mCenterHeight = mHeaderHeight
            mFooterHeight = mCenterHeight
        }
        mGoodsEmptyAdapter.getItemHeight {
            mHeaderHeight = it - topTitle.height - topView.height - topTab.height
            mCenterHeight = mHeaderHeight
            mFooterHeight = mCenterHeight
        }
        mAssembleTeamAdapter.getItemHeight {
            mAssembleTeamHeight = it
            if (mHeaderHeight <= mHeaderHeight + it) {
                mHeaderHeight += it
            }
            if (mCenterHeight != mHeaderHeight - it) {
                mCenterHeight += mHeaderHeight
            } else {
                mCenterHeight = mHeaderHeight
            }
            mFooterHeight = mCenterHeight
        }
        mStoreDetailAdapter.getItemHeight {
            mCenterHeight += it
            mFooterHeight = if (mRecommendHeight != 0)
                mRecommendHeight + mCenterHeight
            else
                mCenterHeight
        }
        mStoreRecommendAdapter.getItemHeight {
            mRecommendHeight = it
            if (mFooterHeight <= mCenterHeight)
                mFooterHeight = it + mCenterHeight
            else
                mFooterHeight = mCenterHeight
        }
    }

    /**
     * ??????????????????????????????
     */
    private fun showSkuAct(isAddShoppingCat: Boolean) {
        if (mCheckStoreModel == null) {
            showToast("????????????????????????????????????????????????")
            return
        }

        if (!isAddShoppingCat && mGoodsDetailPin != null && mGoodsDetailPin?.assembleDTO?.assembleInventory!! <= 0) {
            showToast("??????????????????????????????????????????~")
            return
        }

        if (!isAddShoppingCat && mGoodsDetailKick != null && mGoodsDetailKick?.bargainInfoDTO?.marketingGoodsChildDTOS!![0].availableInventory <= 0) {
            showToast("??????????????????????????????????????????~")
            return
        }

        mGoodsDetail?.let { goodsDetail ->
            mGoodsSpecDialog?.let { it ->
                it.mode = if (isAddShoppingCat) 1 else 2
                it.setMarketing(mGoodsDetail?.marketingType,mGoodsDetail?.mapMarketingGoodsId)
                it.isSingleSelectAct = !isAddShoppingCat
                if (mGoodsDetailKick != null || mGoodsDetailPin != null) {
                    it.exSkuList = exSkuList
                }
                it.mGoodsSpecDetailNew = null
                it.getSpecSubmit { goodsSpecDetail ->
                    //?????????????????????
                    if (isAddShoppingCat) {
                        when (mGoodsType) {
                            0 -> addGoodsShoppingCat(goodsSpecDetail)
                            else -> buildLeftSkuBuy(goodsDetail, goodsSpecDetail!!)
                        }
                    } else {
                        //?????????????????? ????????????????????????????????????????????????
                        goodsSpecDetail?.let { buildRightConfirm(it) }
                    }
                }
            }
            if (goodsDetail.cheIsNoSku()) {
                //?????????????????????
                if (isAddShoppingCat) {//??????????????????????????????
                    when (mGoodsType) {
                        0 -> addGoodsShoppingCat(mGoodsSpecDetail)
                        else -> buildLeftBuy(goodsDetail)
                    }
                } else {
                    mGoodsSpecDetail?.let { buildRightConfirm(it) }
                }
                return
            }
            //????????? ?????????????????????????????????
            mGoodsSpecDialog?.show(supportFragmentManager, "specDialog")
        }
    }

    /**
     * ????????????
     */
    private fun buildLeftBuy(it: GoodsDetail) {
        //?????????
        var goodsMarketing: GoodsMarketing? =
            GoodsMarketing(it.marketingId)
        goodsMarketing = null
        val goodsBasketCell = GoodsBasketCell(
            it.goodsChildren[0].platformPrice,
            it.goodsChildren[0].retailPrice,
            it.goodsChildren[0].plusPrice,
            it.goodsType,
            it.isPlusOnly,
            mCheckStoreModel?.isSupportOverSold,
            it.goodsChildren[0].barcodeSku
        )
        goodsBasketCell.goodsStock =
            it.goodsChildren[0].getAvailableInventory()
        goodsBasketCell.goodsMarketingDTO = goodsMarketing
        goodsBasketCell.mchId = it.userId
        goodsBasketCell.goodsId = it.id
        goodsBasketCell.goodsSpecId = it.goodsChildren[0].id
        goodsBasketCell.goodsTitle = it.goodsTitle
        goodsBasketCell.goodsImage = it.headImage
        goodsBasketCell.setGoodsQuantity(1)
        val goodsBasketGroup = GoodsBasketGroup(goodsBasketCell)
        val list: MutableList<GoodsBasketCell> = java.util.ArrayList()
        goodsBasketCell.shopIdList = it.goodsShopIdListStringArray
        goodsBasketCell.ischeck = true
        goodsBasketCell.goodsShopId = mCheckStoreModel?.id
        goodsBasketCell.goodsShopName = mCheckStoreModel?.shopName
        goodsBasketCell.goodsShopAddress = mCheckStoreModel?.addressDetails
        list.add(goodsBasketCell)
        ARouter.getInstance()
            .build(ServiceRoutes.SERVICE_ORDER)
            .withString("visitShopId", shopId)
            .withObject("goodsbasketlist", list)
            .navigation()
    }

    /**
     * ?????????????????????
     */
    private fun buildLeftSkuBuy(goodsDetail: GoodsDetail, goodsSpecDetail: GoodsSpecDetail) {
        var goodsMarketing: GoodsMarketing? = GoodsMarketing(goodsDetail.marketingId)
        goodsMarketing = null
        val goodsBasketCell = GoodsBasketCell(
            goodsSpecDetail.platformPrice,
            goodsSpecDetail.retailPrice,
            goodsSpecDetail.plusPrice,
            goodsDetail.goodsType,
            goodsSpecDetail.isPlusOnly,
            mCheckStoreModel?.isSupportOverSold, goodsSpecDetail.barcodeSku
        )
        goodsBasketCell.goodsSpecDesc = goodsSpecDetail.goodsSpecStr
        if (mGoodsType == 1) {
            goodsBasketCell.goodsStock = goodsSpecDetail.availableInventory
        }
        if (mGoodsType == 2) {
            goodsBasketCell.goodsStock = goodsSpecDetail.getRealAvailableInventory()
        }
        goodsBasketCell.goodsMarketingDTO = goodsMarketing
        goodsBasketCell.mchId = goodsDetail.userId
        goodsBasketCell.goodsId = goodsDetail.id
        goodsBasketCell.goodsSpecId = goodsSpecDetail.id
        goodsBasketCell.goodsTitle = goodsDetail.goodsTitle
        goodsBasketCell.goodsImage = goodsDetail.headImage
        goodsBasketCell.setGoodsQuantity(goodsSpecDetail.getCount().toInt())
        //val goodsBasketGroup = GoodsBasketGroup(goodsBasketCell)
        val list: MutableList<GoodsBasketCell> = java.util.ArrayList()
        goodsBasketCell.shopIdList = goodsDetail.goodsShopIdListStringArray
        goodsBasketCell.ischeck = true
        goodsBasketCell.goodsShopId = mCheckStoreModel?.id
        goodsBasketCell.goodsShopName = mCheckStoreModel?.shopName
        goodsBasketCell.goodsShopAddress = mCheckStoreModel?.addressDetails
        list.add(goodsBasketCell)
        ARouter.getInstance()
            .build(ServiceRoutes.SERVICE_ORDER)
            .withString("visitShopId", shopId)
            .withObject("goodsbasketlist", list)
            .navigation()


    }

    /**
     * ?????????????????????
     */
    private fun addGoodsShoppingCat(goodsSpecDetail: GoodsSpecDetail?) {
        //?????????????????????????????? ??????????????????????????????
        mGoodsDetail?.let {
            goodsSpecDetail?.let { spec ->
                if (3 == mGoodsType) { // ???????????????????????????????????????????????????
                    //?????????????????????
                    if (it.getMarketingSalesMin() > it.getRealCanBuy(
                            it.marketingSalesMax,
                            spec.nowOrderBuyCount,
                            it.marketingGoodsChildren[0]
                                .getAvailableInventoryMark(it.goodsChildren[0])
                        )
                    ) {
                        when {
                            it.getMarketingSalesMin() > it.marketingGoodsChildren[0]
                                .getAvailableInventoryMark(it.goodsChildren[0]) -> {
                                showToast("??????????????????")
                            }
                            it.marketingSalesMax - spec.nowOrderBuyCount <= 0 -> {
                                showToast("?????????${spec.nowOrderBuyCount}???????????????????????????${it.marketingSalesMax}???")
                            }
                            else -> {
                                showToast("??????${it.marketingSalesMin}???????????????${spec.nowOrderBuyCount}???????????????????????????${it.marketingSalesMax}???")
                            }
                        }
                        it.isErrorCount = true
                    }
                }
            }
            mParamsMap.clear()

            mParamsMap["goodsSource"] = "1"
            mParamsMap["shopId"] = shopId
            mParamsMap["goodsShopId"] = goodsShopId
            //mParamsMap["packageType"] = "SINGLE_PACKAGE"
            mParamsMap["goodsId"] = it.id
            mParamsMap["goodsSpecId"] =
                if (it.cheIsNoSku()) it.goodsChildren[0].id else goodsSpecDetail?.id.toString()
            mParamsMap["goodsQuantity"] =
                if (it.cheIsNoSku()) "1" else goodsSpecDetail?.count.toString()
            mParamsMap["goodsType"] = it.goodsType
            mGoodsPresenter?.addShoppingCat(mParamsMap)
        }
    }

    /**
     * ??????????????????????????????????????????
     */
    private fun buildRightConfirm(goodsSpecDetail: GoodsSpecDetail) {

        mGoodsDetail?.let {
            goodsSpecDetail?.let { spec ->
                if (3 == mGoodsType) { // ???????????????????????????????????????????????????
                    //?????????????????????
                    if (it.getMarketingSalesMin() > it.getRealCanBuy(it.marketingSalesMax, spec.nowOrderBuyCount, it.marketingGoodsChildren[0].getAvailableInventoryMark(it.goodsChildren[0]))) {
                        when {
                            it.getMarketingSalesMin() > it.marketingGoodsChildren[0]
                                .getAvailableInventoryMark(it.goodsChildren[0]) -> {
                                showToast("??????????????????")
                                return
                            }
                            it.marketingSalesMax - spec.nowOrderBuyCount <= 0 -> {
                                showToast("?????????${spec.nowOrderBuyCount}???????????????????????????${it.marketingSalesMax}???")
                                return
                            }
                            else -> {
                                showToast("??????${it.marketingSalesMin}???????????????${spec.nowOrderBuyCount}???????????????????????????${it.marketingSalesMax}???")
                                return
                            }
                        }
                    }
                }
            }
        }

        when (mGoodsType) {
            1 -> { //????????????
                ARouter.getInstance()
                    .build(DiscountRoutes.DIS_KICKDETAIL)
                    .withString("bargainId", bargainId + "")
                    .withString(//????????????
                        "marketingShopId",
                        shopId/*shopId*/
                    )
                    .withString("deliveryShopId", goodsShopId)
                    .withString("bargainMemberId", bargainMemberId)
                    .withString(
                        "marketingGoodsChildId",
                        if (mGoodsDetailKick?.bargainInfoDTO?.marketingGoodsChildDTOS != null && mGoodsDetailKick?.bargainInfoDTO?.marketingGoodsChildDTOS?.size!! > 0)
                            mGoodsDetailKick?.bargainInfoDTO?.marketingGoodsChildDTOS!![0].id else mGoodsDetail?.goodsChildren!![0].id
                    )
                    .navigation()
                finish()
            }
            else -> {
                //?????????????????? \ ??????
                var singleNumber: String? = null
                var assemblePrice: String? = null
                var goodsMarketingType: String? = null
                if (2 == mGoodsType) {
                    singleNumber = String(
                        Base64.decode(
                            SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                            Base64.DEFAULT
                        )
                    ) + Date().time
                    assemblePrice =
                        if (mGoodsDetail?.cheIsNoSku()!!)
                            mGoodsDetailPin?.assembleDTO?.assemblePrice.toString()
                        else goodsSpecDetail.getMarketingPrice().toString()
                    goodsMarketingType = goodsSpecDetail.marketingType
                }

                val triple = getGoodsBasketCell(goodsSpecDetail)
                val goodsMarketing: GoodsMarketing? = triple.first
                val mCellGoodsStock: Int = triple.second
                val goodsBasketCell = triple.third
                if (!mGoodsDetail?.cheIsNoSku()!!) {
                    goodsBasketCell.goodsSpecDesc = goodsSpecDetail.goodsSpecStr
                }
                goodsBasketCell.goodsStock = mCellGoodsStock
                goodsBasketCell.goodsMarketingDTO = goodsMarketing
                goodsBasketCell.mchId = mGoodsDetail?.userId
                goodsBasketCell.goodsId = mGoodsDetail?.id
                goodsBasketCell.goodsSpecId = mGoodsSpecId
                //if (1 == mGoodsType || 2 == mGoodsType) goodsSpecDetail.getGoodsChildId() else goodsSpecDetail.getGoodsSpec()
                goodsBasketCell.goodsTitle = mGoodsDetail?.goodsTitle
                goodsBasketCell.goodsImage = mGoodsDetail?.headImage
                goodsBasketCell.setGoodsQuantity(
                    if (mGoodsDetail?.cheIsNoSku()!!) mGoodsDetail?.getMarketingSalesMin()!! else goodsSpecDetail.getCount()
                        .toInt()
                )
                val list: MutableList<GoodsBasketCell> = java.util.ArrayList()
                goodsBasketCell.shopIdList = mGoodsDetail?.goodsShopIdListStringArray
                goodsBasketCell.ischeck = true
                goodsBasketCell.goodsShopId = mCheckStoreModel?.id
                goodsBasketCell.goodsShopName = mCheckStoreModel?.shopName
                goodsBasketCell.goodsShopAddress = mCheckStoreModel?.addressDetails
                list.add(goodsBasketCell)
                ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_ORDER)
                    .withString("visitShopId", shopId)
                    .withObject("goodsbasketlist", list)
                    .withString("assembleId", assembleId)
                    .withString(
                        "teamNum",
                        if (TextUtils.isEmpty(teamNum)) singleNumber else teamNum
                    )
                    .withString("assemblePrice", assemblePrice)
                    .withString("goodsMarketingType", goodsMarketingType)
                    .navigation()
            }
        }
    }

    /**
     * ?????? GoodsBasketCell??????
     */
    private fun getGoodsBasketCell(goodsSpecDetail: GoodsSpecDetail): Triple<GoodsMarketing?, Int, GoodsBasketCell> {
        var goodsMarketing: GoodsMarketing? =
            GoodsMarketing(if (mGoodsDetail?.cheIsNoSku()!!) mGoodsDetail?.marketingId else goodsSpecDetail.marketingId)
        var availableInventory: Int = goodsSpecDetail.availableInventoryMark
        var mapMarketingGoodsId: String? = goodsSpecDetail.mapMarketingGoodsId
        var marketingType: String? = goodsSpecDetail.marketingType
        var id: String? = goodsSpecDetail.getgoodsMarketingGoodsSpec()
        var marketingPrice: Double = goodsSpecDetail.marketingPrice
        var pointsPrice: Double = goodsSpecDetail.pointsPrice
        val salesMin: Int = mGoodsDetail?.getMarketingSalesMin()!!
        val salesMax: Int = mGoodsDetail?.getMarketingSalesMax()!!
        /*** goodsMarketing ???????????? */
        if (mGoodsDetail?.cheIsNoSku()!!) {
            when {
                2 == mGoodsType -> {
                    availableInventory =
                        if (mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS != null && mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS!!.size > 0)
                            mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS!![0].availableInventory else 0

                    mapMarketingGoodsId =
                        if (mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS != null && mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS!!.size > 0)
                            mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS!![0].mapMarketingGoodsId else "0"
                    marketingType = "2"
                    id =
                        if (mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS != null && mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS?.size!! > 0)
                            mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS!![0].id else null
                    marketingPrice =
                        if (mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS != null && mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS!!.size > 0)
                            mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS!![0].marketingPrice else 0.0
                    pointsPrice = goodsSpecDetail.pointsPrice
                }
                4 != mGoodsType && mGoodsType >= 3 -> {
                    val goodsChildren = mGoodsDetail?.marketingGoodsChildren!![0]
                    availableInventory = goodsChildren
                        .getAvailableInventoryMark(mGoodsDetail?.goodsChildren!![0])
                    mapMarketingGoodsId = mGoodsDetail?.mapMarketingGoodsId
                    marketingType = mGoodsDetail?.marketingType
                    id = goodsChildren.id
                    marketingPrice = goodsChildren.marketingPrice
                    pointsPrice = goodsChildren.pointsPrice
                }
                4 == mGoodsType -> {
                    val goodsChildren = mGoodsDetail?.marketingGoodsChildren!![0]
                    availableInventory =
                        goodsChildren.getAvailableInventoryMark(mGoodsDetail?.goodsChildren!![0])
                    mapMarketingGoodsId = mGoodsDetail?.mapMarketingGoodsId
                    marketingType = mGoodsDetail?.marketingType
                    id = goodsChildren.id
                    marketingPrice = goodsChildren.marketingPrice
                    pointsPrice = goodsSpecDetail.pointsPrice
                }
            }
        }
        if ("1" == isNtReal && "4" == goodsSpecDetail.marketingType) {
            goodsMarketing = null
        } else if (marketingType.isNullOrEmpty()) {
            goodsMarketing = null
        } else {
            goodsMarketing!!.availableInventory = availableInventory
            goodsMarketing.mapMarketingGoodsId = mapMarketingGoodsId
            goodsMarketing.marketingType = marketingType
            goodsMarketing.id = id
            goodsMarketing.marketingPrice = marketingPrice
            goodsMarketing.pointsPrice = pointsPrice
            goodsMarketing.salesMin = salesMin
            goodsMarketing.salesMax = salesMax
        }

        /** ???????????????????????????????????????????????? */
        var mCellPlatformPrice: Double? = goodsSpecDetail.platformPrice
        var mCellRetailPrice: Double? = goodsSpecDetail.retailPrice
        var mCellPlusPrice: Double? = goodsSpecDetail.plusPrice
        var mCellPlusOnly: String? = goodsSpecDetail.isPlusOnly
        var mCellBarcodeSku: String? = goodsSpecDetail.barcodeSku
        var mCellGoodsStock: Int = goodsSpecDetail.getRealAvailableInventory()

        mGoodsSpecId = if (4 == mGoodsType) {
            if ("1" == isNtReal) {
                goodsSpecDetail.getGoodsSpec()
            } else {
                goodsSpecDetail.getGoodsChildId()
            }
        } else if (3 == mGoodsType) {
            goodsSpecDetail.getGoodsSpec()
        } else {
            goodsSpecDetail.id
        }

        mGoodsQuantity = goodsSpecDetail.getCount().toString().toInt()
        /*** ??????????????????????????? */
        if (mGoodsDetail?.cheIsNoSku()!!) {
            mCellPlusOnly = mGoodsDetail?.isPlusOnly
            when (mGoodsType) {
                2 -> {
                    mCellPlatformPrice =
                        mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS?.get(0)?.platformPrice
                    mCellRetailPrice =
                        mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS?.get(0)?.platformPrice
                    mCellPlusPrice = 0.0
                    mCellBarcodeSku =
                        mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS?.get(0)?.barcodeSku
                    mCellGoodsStock = 1
                    mGoodsQuantity = 1
                    mGoodsSpecId =
                        mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS?.get(0)?.goodsChildId
                }
                3 -> {
                    mCellPlatformPrice =
                        mGoodsDetail?.marketingGoodsChildren?.get(0)?.platformPrice
                    mCellRetailPrice =
                        mGoodsDetail?.marketingGoodsChildren?.get(0)?.retailPrice
                    mCellPlusPrice =
                        mGoodsDetail?.marketingGoodsChildren?.get(0)?.plusPrice
                    mCellBarcodeSku =
                        mGoodsDetail?.marketingGoodsChildren?.get(0)?.barcodeSku
                    mCellGoodsStock =
                        mGoodsDetail?.goodsChildren!![0].getAvailableInventory();
                    mGoodsSpecId = mGoodsDetail?.marketingGoodsChildren?.get(0)?.goodsChildId
                    mGoodsQuantity = salesMin
                }
                else -> { //??????????????????goodsChildren
                    mCellPlatformPrice = mGoodsDetail?.goodsChildren?.get(0)?.platformPrice
                    mCellRetailPrice = mGoodsDetail?.goodsChildren?.get(0)?.retailPrice
                    mCellPlusPrice = mGoodsDetail?.goodsChildren?.get(0)?.plusPrice
                    mCellBarcodeSku = mGoodsDetail?.goodsChildren?.get(0)?.barcodeSku
                    if ("1" == isNtReal && "4" == mGoodsDetail?.marketingType) {
                        //????????????
                        mCellGoodsStock = 1
                        mGoodsSpecId =
                            mGoodsDetail?.marketingGoodsChildren?.get(0)?.goodsChildId
                    } else {
                        //????????????
                        mCellGoodsStock =
                            mGoodsDetail?.goodsChildren!![0].getAvailableInventory()
                        mGoodsSpecId = mGoodsDetail?.goodsChildren?.get(0)?.id
                    }
                    mGoodsQuantity = 1
                }
            }
        }

        val goodsBasketCell = GoodsBasketCell(
            mCellPlatformPrice!!, mCellRetailPrice!!,
            mCellPlusPrice!!, mGoodsDetail?.goodsType, mCellPlusOnly,
            mCheckStoreModel?.isSupportOverSold, mCellBarcodeSku
        )
        return Triple(goodsMarketing, mCellGoodsStock, goodsBasketCell)
    }

    /**
     * ?????????????????????
     */
    override fun outClick(function: String, obj: Any) {
        when (function) {
            "???????????????" -> getData()
            "back" -> finish()
            "share" -> {
                if (mGoodsEmptyAdapter.getModel() != null) {
                    showToast("???????????????????????????????????????????????????~")
                    return
                }
                mGoodsDetail?.let {
                    SeckShareDialog.newInstance()
                        .setGoodsDetail(2, mGoodsType, it, shopId, merchantId)
                        .setGoodsKickPin(mGoodsDetailKick, mGoodsDetailPin)
                        .setShopDetailModel(mCheckStoreModel)
                        .setIndustyType(1)
                        .show(supportFragmentManager, "serviceDetailShareDialog")
                }

            }
            "baskAction" -> showSkuAct(true)
            //?????? -> ????????????
            "confirmAction" -> {
                teamNum = teamNumOrg//??????????????????
                showSkuAct(false)
            }
            // ????????????
            "groupDialog" -> {
                if (obj is AssemableTeam) {
                    teamNum = obj.teamNum//??????????????????
                    showSkuAct(false)
                }
            }
            //??????
            "navigation" -> NavigationSelectDialog.newInstance()
                .setStoreDetailModel(mCheckStoreModel!!)
                .show(supportFragmentManager, "navigationDialog")
            //???????????????
            "phone" -> PhoneUtils.callPhone(mContext, obj.toString())
            //??????????????????
            "lookShopList" ->
                ARouter.getInstance()
                    .build(SecondRoutes.SECOND_SHOP_LIST)
                    .withString("shopId", goodsShopId)
                    .withCharSequenceArray(
                        "goodsShopIds",
                        if (mGoodsDetail != null) mGoodsDetail?.goodsShopIdListStringArray else null
                    )
                    .navigation(mActivity, Constants.STATUS_APPOINTMENT_SHOP_CODE)
            //??????????????????
            "lookAllProject" ->
                ARouter.getInstance()
                    .build(SecondRoutes.SECOND_SHOP_DETAIL)
                    .withString("shopId", goodsShopId)
                    .navigation()
            //????????????????????????
            "recommendProject" -> {
                val mRecommendList = obj as? RecommendList
                mRecommendList?.let {
                    ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                        .withString("shopId", shopId)
                        .withString("merchantId", merchantId)
                        .withString("goodsId", it.id)
                        .navigation()
                }
            }
        }
    }

    /**
     * ????????????????????????
     */
    override fun successGetGoodsDetail(goodsDetail: GoodsDetail?) {
        if (goodsDetail == null && !TextUtils.isEmpty(marketingType) && "null" != marketingType&&"0"!=marketingType) {
            marketingType = "0"
            getData()
            return
        }
        if (goodsDetail == null) {
            buildErrorGoods()
            return
        }
        this.mGoodsDetail = goodsDetail
        setModel()
    }

    /**
     *??????????????????
     */
    override fun successGetGoodsDetailKick(goods2DetailKick: Goods2DetailKick?) {
        if (goods2DetailKick == null) {
            buildErrorGoods()
            return
        }
        this.mGoodsDetailKick = goods2DetailKick
        this.mGoodsDetail = goods2DetailKick.goodsDetails

        setModel()
    }

    /**
     * ??????????????????
     */
    override fun successGetGoodsDetailPin(goods2DetailPin: Goods2DetailPin?) {
        if (goods2DetailPin == null) {
            buildErrorGoods()
            return
        }
        this.mGoodsDetailPin = goods2DetailPin
        this.mGoodsDetail = goods2DetailPin.goodsDetails

        setModel()
    }

    /**
     * ???????????????????????????????????????
     */
    override fun successTeamList(couponInfoByMerchants: MutableList<AssemableTeam>?) {
        mAssembleTeamAdapter.setTeamList(couponInfoByMerchants)
    }

    /**
     * ??????????????????
     */
    override fun successGetRecommendList(
        recommendList: MutableList<RecommendList>?,
        firstPageSize: Int
    ) {
        titles.clear()
        //????????????tab????????????
        if (mCheckStoreModel != null) {
            if (!titles.contains(mShopTitleName)) {
                titles.add(mShopTitleName)
            }
        }
        //????????????????????????
        if (recommendList.isNullOrEmpty().not()) {
            mStoreRecommendAdapter.isLoadSuccess = false
            mStoreRecommendAdapter.mRecommendList =
                if (recommendList?.size!! > 3) recommendList.subList(0, 3)
                else recommendList
            mStoreRecommendAdapter.model = ""
            //??????????????????tab????????????
            if (!titles.contains(mShopRecommendName)) {
                titles.add(mShopRecommendName)
            }
        }
        //????????????tab????????????
        if (mGoodsDetail?.additionNote.isNullOrEmpty().not()) {
            if (!titles.contains(mGoodsDetailName)) {
                titles.add(mGoodsDetailName)
            }
        }
        //?????????tab
        initTab()
    }

    /**
     * ??????????????????
     */
    override fun successGetSkuExList(list: MutableList<GoodsSpecDetail>?) {
        list?.let {
            if (bargainId.isNullOrEmpty().not()) {

                for (i in it.indices) {
                    it[i].marketingType = "1"
                }
            }
            if (assembleId.isNullOrEmpty().not()) {
                for (i in it.indices) {
                    it[i].marketingType = "2"
                }
            }
        }

        mGoodsSpecDialog?.let {
            it.exSkuList = list
        }
        this.exSkuList = list
    }

    /**
     * ?????????????????????
     */
    override fun successAddShoppingCat(result: String?) {
        showToast("??????????????????~")
    }

    override fun onSucessGetShopDetailOnly(detailModel: ShopDetailModel?) {
        if(!"1".equals(detailModel!!.merchantType)){//??????????????????
            ARouter.getInstance()
                .build(ServiceRoutes.SERVICE_DETAIL)
                .withString("shopId", shopId)
                .withString("marketingType", marketingType)
                .withString("assembleId", assembleId)
                    .withString("teamNum", teamNum)
                .withString("bargainId", bargainId)
                .withString("bargainMemberId", bargainMemberId)
                .withString("merchantId", merchantId)
                .withString("goodsId", id)
                .navigation()
            finish()
        }
    }


    /**
     * ??????????????????
     */
    override fun onGetShopListSuccess(shopList: MutableList<ShopDetailModel>?) {//
        if (ListUtil.isEmpty(shopList)) {
            showToast("??????????????????????????????")
            showEmpty()
            return
        }
        isGetShopList = true
        mNetWorkShopList.clear()
        shopList?.let {
            mNetWorkShopList.addAll(it)
            if(mNetWorkShopList.size>0){
                try {
//                goodsShopId = it[0].id
                    setModel()
                } catch (e: Exception) {
                }
            }

        }
    }

    /**
     *  ??????????????????
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == Constants.STATUS_APPOINTMENT_SHOP_CODE) {
            val id = data?.getStringExtra("id")
            if (goodsShopId == id) {
                return
            }
            for (newStoreDetailModel in mShopList) {
                if (id == newStoreDetailModel.id) {
                    mCheckStoreModel = newStoreDetailModel
                    break
                }
            }
            goodsShopId = mCheckStoreModel?.id.toString()
            mStoreDetailAdapter.setModel(mCheckStoreModel)
            mStoreDetailAdapter
            getData()
        }
    }

    /**
     * ?????????????????????id
     */
    private fun checkNowServiceShopIsRealy(shopId: String, shopIdList: Array<String>?): Boolean {
        if (shopIdList == null || shopIdList.isEmpty()) {
            return false
        }
        for (i in shopIdList.indices) {
            if (shopId == shopIdList[i]) {
                return true
            }
        }
        return false
    }

    /**
     *  ????????????????????????
     */
    private fun setModel() {
        if("2".equals(mGoodsDetail!!.goodsType)){//????????? ????????????
            ARouter.getInstance()
                .build(ServiceRoutes.SERVICE_DETAIL)
                .withString("shopId", shopId)
                .withString("marketingType", marketingType)
                .withString("assembleId", assembleId)
                    .withString("teamNum", teamNum)
                .withString("bargainId", bargainId)
                .withString("bargainMemberId", bargainMemberId)
                .withString("merchantId", merchantId)
                .withString("goodsId", id)
                .navigation()
            finish()
            return
        } else if(1!=mGoodsDetail!!.differentSymbol){//??????????????????
            ARouter.getInstance()
                .build(ServiceRoutes.SERVICE_DETAIL)
                .withString("shopId", shopId)
                    .withString("teamNum", teamNum)
                .withString("marketingType", marketingType)
                .withString("assembleId", assembleId)
                .withString("bargainId", bargainId)
                .withString("bargainMemberId", bargainMemberId)
                .withString("merchantId", merchantId)
                .withString("goodsId", id)
                .navigation()
            finish()
            return
        }
        cl_second_bottom_action.visibility = View.VISIBLE
        isZero = false
        isMarkZero = false
        tv_bask_action.isEnabled = true
        tv_confirm_action.isEnabled = true
        mGoodsDetail?.let { it ->
            val goodsShopIdListStringArray = it.goodsShopIdListStringArray
            if (goodsShopIdListStringArray != null && goodsShopIdListStringArray.isNotEmpty()
                && ListUtil.isEmpty(mNetWorkShopList)
            ) {
                //???????????????????????? ?????????????????????????????????????????????????????? ???????????????????????????
                mParamsMap.clear()
                mParamsMap["shopId"] = goodsShopIdListStringArray[0]
                mParamsMap["shopTypeList"] = arrayOf("1")
                mShopPresenter?.getShopList(mParamsMap)
                return
            }
            if (!ListUtil.isEmpty(mNetWorkShopList) && mCheckStoreModel == null) {
                for (model in mNetWorkShopList) {
                    /*if (goodsShopIdListStringArray.isNullOrEmpty()) {
                        // ??????????????????????????????????????? ???????????????shopId???????????????????????????????????????????????????
                        if (goodsShopId == model.id) {
                            mCheckStoreModel = model
                            break
                        }
                    } else {*/
                    val goodsShopIdListStringArray = it.goodsShopIdListStringArray
                    if (checkNowServiceShopIsRealy(model.id, goodsShopIdListStringArray)) {
                        mShopList.add(model)
                    }
                    //}
                }
            }
        }

        //???????????????????????????????????????
        if (mShopList.isEmpty().not()) {
            //??????
            mShopList.sort()
            if ("0".equals(goodsShopId)) {
                if ("1".equals(mGoodsDetail!!.goodsType)) {
                    //??????????????????????????? ??????????????????
                    goodsShopId = mShopList[0].id;//??????shopid????????????
                    mCheckStoreModel = mShopList[0]
                    showToast("????????????????????????????????????????????????")
                    getData()
                    return
                }
            }
            mStoreDetailAdapter.mShopSize = mShopList.size
            if(mCheckStoreModel==null){
                mCheckStoreModel = mShopList[0]
                mCheckStoreModel?.let { goodsShopId = it.id }
            }
            mParamsShopMap.clear()
            mParamsShopMap.put("shopId", mCheckStoreModel!!.id)
            mParamsShopMap.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
            mParamsShopMap.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
            mShopPresenter!!.getShopDetailOnly(mParamsShopMap)
            mGoodsDetail!!.setStoreDetialModel(mCheckStoreModel)
        }
        getRecommend()
        /*** ???????????????????????? */
        if (mGoodsDetail != null && mGoodsDetail?.goodsChildren.isNullOrEmpty()
                .not() && mGoodsDetail?.goodsChildren!![0].getAvailableInventory() <= 0
        ) {
            isZero = true
            showToast("??????????????????????????????????????????~")
            tv_bask_action.isEnabled = !isZero
            tv_confirm_action.isEnabled = !isZero
        }
        /*** ???????????????????????? */
        if (mGoodsDetail != null && mGoodsDetail?.marketingType == "3" && mGoodsDetail?.marketingGoodsChildren.isNullOrEmpty()
                .not() && mGoodsDetail?.marketingGoodsChildren!![0].getAvailableInventoryMark(mGoodsDetail?.goodsChildren!![0]) <= 0
        ) {
            isMarkZero = true
            showToast("??????????????????????????????????????????~")
            if("3".equals(marketingType)){//??????????????????
                tv_bask_action.isEnabled = !isZero
                tv_confirm_action.isEnabled = !isMarkZero
            }else{
                mGoodsDetail?.marketingType=null
                tv_bask_action.isEnabled = !isZero
                tv_confirm_action.isEnabled = !isZero
            }

        }

        /*** ???????????????????????? */
        if (mGoodsDetailPin != null
            && mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS?.isNullOrEmpty()!!.not()
            && mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS?.size==1
            &&mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS?.get(0)?.availableInventory!!<=0) {
            isMarkZero = true
            showToast("??????????????????????????????????????????~")
            tv_bask_action.isEnabled = !isZero
            tv_confirm_action.isEnabled = !isMarkZero
        }

        /*** ???????????????????????? */
        if (mGoodsDetailKick != null
            && mGoodsDetailKick?.bargainInfoDTO?.marketingGoodsChildDTOS?.isNullOrEmpty()!!.not()
            && mGoodsDetailKick?.bargainInfoDTO?.marketingGoodsChildDTOS?.size==1
            && mGoodsDetailKick?.bargainInfoDTO?.marketingGoodsChildDTOS?.get(0)?.availableInventory!! <= 0) {
            isMarkZero = true
            showToast("??????????????????????????????????????????~")
            tv_bask_action.isEnabled = !isZero
            tv_confirm_action.isEnabled = !isMarkZero
        }


        tv_confirm_action.textColorBuilder.setTextDisabledColor(
            if (isMarkZero) Color.parseColor("#FFFFFF") else Color.parseColor("#FFFFFF")
        ).intoTextColor()

        setActionStyle()
        mBannerAdapter.isLoadSuccess = false
        mPriceAdapter.isLoadSuccess = false
        mStoreDetailAdapter.isLoadSuccess = false
        mGoodsDetailAdapter.isLoadSuccess = false

        mGoodsEmptyAdapter.model = null
        mBannerAdapter.model = mGoodsDetail
        mPriceAdapter.setGoodsType(mGoodsType)
        mPriceAdapter.setGoodsKickPin(mGoodsDetailKick, mGoodsDetailPin)
        mPriceAdapter.model = mGoodsDetail
        if (2 == mGoodsType && !ListUtil.isEmpty(mAssembleTeamAdapter.getTeamList())) {
            mAssembleTeamAdapter.setGoodsDetailPin(mGoodsDetailPin?.assembleDTO)
            mAssembleTeamAdapter.model = ""
        } else {
            mAssembleTeamAdapter.model = null
        }
        mStoreDetailAdapter.model = mCheckStoreModel
        mGoodsDetailAdapter.model = mGoodsDetail?.additionNote

        /*** ???????????????????????? */
        mGoodsDetail?.let {
            mGoodsSpecCells = it.specCell
            var saleMax = 0
            var saleMin = 0
            if (TextUtils.isEmpty(it.marketingType) || "0" == it.marketingType) {
                saleMax = 999
                saleMin = 1
            } else {
                saleMax = it.marketingSalesMax
                saleMin = it.marketingSalesMin
            }
            /*** ???????????????????????? */
            mGoodsSpecDetail = GoodsSpecDetail(
                it.storePrice,
                it.getPlatformPrice(),
                it.headImages[0].splash,
                it.goodsType,
                saleMax, saleMin,
                "0" == isNtReal,
                it.isPlusOnly
            )
            if (!it.cheIsNoSku()) { //????????????????????????
                mGoodsSpecDialog?.let { spec ->
                    spec.setGoodsId(it.id, goodsShopId)
                    spec.mGoodsSpecDetail = mGoodsSpecDetail
                    spec.setMarketing(mGoodsDetail?.marketingType,mGoodsDetail?.mapMarketingGoodsId)
                    spec.mShopModel = mCheckStoreModel
                    when (mGoodsType) {
                        1 -> {
                            //2021/5/17 ????????????????????????????????????
                            spec.setMarketing(
                                if ("8" == it.marketingType && !SpUtils.getValue(
                                        mContext,
                                        SpKey.PLUSSTATUS,
                                        false
                                    )
                                ) null else it.marketingType, it.mapMarketingGoodsId
                            )
                        }
                        2 -> spec.regimentSize = mGoodsDetailPin?.assembleDTO?.regimentSize!!
                    }
                    spec.initSpec(mGoodsSpecCells!!)
                }
            }
        }
    }

    /**
     * ??????????????????
     */
    @SuppressLint("SetTextI18n")
    private fun setActionStyle() {
        mGoodsType = when { //????????????????????????????????????????????? marketingType????????????
            mGoodsDetail?.marketingType.isNullOrEmpty()
                .not() -> mGoodsDetail?.marketingType.toString().toInt()
            else -> 0
        }

        val layoutParams =
            tv_confirm_action.layoutParams as ConstraintLayout.LayoutParams
        var shapeDrawableBuilder = tv_confirm_action.shapeDrawableBuilder
            .setTopLeftRadius(0f).setBottomLeftRadius(0f)

        /*tv_confirm_action.topLeftRadius = 0
        tv_confirm_action.bottomLeftRadius = 0*/
        layoutParams.leftMargin = 0
        tv_bask_action.visibility = View.VISIBLE
        tv_confirm_action.text = "????????????"
        tv_bask_action.text = "???????????????"
        //????????????
        when (mGoodsType) {
            3 -> {
                layoutParams.leftMargin = TransformUtil.dp2px(mContext, 10f).toInt()
                shapeDrawableBuilder = shapeDrawableBuilder
                    .setTopLeftRadius(shapeDrawableBuilder.topRightRadius)
                    .setBottomLeftRadius(shapeDrawableBuilder.topRightRadius)
                /* tv_confirm_action.topLeftRadius = tv_confirm_action.topRightRadius
                 tv_confirm_action.bottomLeftRadius = tv_confirm_action.topRightRadius*/
                tv_bask_action.visibility = View.GONE
                tv_confirm_action.text = "????????????"
            }
            2 -> {
                tv_bask_action.text =
                    "????????????\n??${
                        StringUtils.getResultPrice(
                            mGoodsDetail?.getPlatformPrice().toString()
                        )
                    }"
                tv_confirm_action.text =
                    "${mGoodsDetailPin?.assembleDTO?.regimentSize}??????\n??${
                        StringUtils.getResultPrice(
                            mGoodsDetailPin?.assembleDTO?.assemblePrice.toString()
                        )
                    }"
            }
            1 -> {
                tv_bask_action.text =
                    "????????????\n??${
                        StringUtils.getResultPrice(
                            mGoodsDetail?.getPlatformPrice().toString()
                        )
                    }"
                tv_confirm_action.text =
                    "????????????\n??${StringUtils.getResultPrice(mGoodsDetailKick?.bargainInfoDTO?.floorPrice.toString())}"
            }
        }
        tv_confirm_action.layoutParams = layoutParams
        shapeDrawableBuilder.intoBackground()
    }

    /**
     * ??????????????????
     */
    private fun buildErrorGoods() {
        cl_second_bottom_action.visibility = View.GONE
        mBannerAdapter.model = null
        mPriceAdapter.model = null
        mGoodsEmptyAdapter.model = "?????????????????????????????????????????????"
    }
}