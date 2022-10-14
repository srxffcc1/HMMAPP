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
 * @description 异业服务详情
 * @author long
 * @createTime 2021/10/09
 */
@Route(path = SecondRoutes.SECOND_SERVICE_DETAIL)
class SecondServiceDetailActivity : BaseActivity(), IsFitsSystemWindows,
    BaseAdapter.OnOutClickListener, SecondGoodsContract.View, SecondShopContract.View {
    @Autowired
    @JvmField
    var shopId: String = ""//访问门店

    @Autowired
    @JvmField
    var marketingType: String = ""//为了新人专享考虑

    @Autowired
    @JvmField
    var isNtReal: String = "0" //是否有新人资格

    @Autowired
    @JvmField
    var id: String = "" // 适配以往内容 商品id

    @Autowired
    @JvmField
    var goodsId: String = ""

    @Autowired
    @JvmField
    var merchantId: String = ""

    @Autowired
    @JvmField
    var goodsShopId: String = ""//切换后的适用门店 id

    @Autowired
    @JvmField
    var barcodeSku: String = ""//直接传条码

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


    /*** 锚点标题 */
    private val mShopTitleName = "适用门店"
    private val mShopRecommendName = "店铺推荐"
    private val mGoodsDetailName = "商品详情"
    private val titles = mutableListOf<String>()
    private val mTabEntities = ArrayList<CustomTabEntity>()

    /*** 锚点Y轴 */
    private var mBannerHeight = 0
    private var mAssembleTeamHeight = 0
    private var mRecommendHeight = 0
    private var mHeaderHeight = 0
    private var mCenterHeight = 0
    private var mFooterHeight = 0

    /*** 0 普通 1 砍价 2 拼团 3 秒杀 （4 新客但是暂时没有说要做 只是做个预留）*/
    private var mGoodsType = 0

    /*** Y轴滑动距离 */
    private var mScrollDy = 0

    /*** 是否滑动 */
    private var isScrolled = false

    private var firstPageSize: Int = 0

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.fontScale != 1f) {
            //非默认值
            resources
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun getResources(): Resources? {
        val res = super.getResources()
        if (res.configuration.fontScale != 1f) { //非默认值
            val newConfig = Configuration()
            newConfig.setToDefaults() //设置默认
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
        teamNumOrg=teamNum;//保存一份原始的teamNum
        goodsShopId = "0"//异业商品第一次请求定0
        if (merchantId.isNullOrEmpty()) {
            merchantId = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
        }
        if (id.isNullOrEmpty()) {
            id = goodsId
        }
        mGoodsPresenter = SecondGoodsPresenter(mContext, this)
        mShopPresenter = SecondShopPresenter(mContext, this)
        //规格弹窗
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
     * 初始化tab栏标题
     */
    fun initTab() {
        if (topTab.tabCount == titles.size) return
        mTabEntities.clear()
        for (i in titles.indices) {
            //插入tab标签
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
     * 获取推荐数据
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
                showToast("服务商品未配置服务门店请联系商家")
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
                var offset = 0 //偏移量 小数额页面往上面走 大数额页面往下走
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
     * 设置头部透明度
     * @param alpha 透明度
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

        /*** 商品异常空布局 */
        mGoodsEmptyAdapter = SecondGoodsEmptyAdapter()
        delegateAdapter.addAdapter(mGoodsEmptyAdapter)

        /*** 服务详情头部 banner */
        mBannerAdapter = ServiceDetailBannerAdapter()
        delegateAdapter.addAdapter(mBannerAdapter)

        /*** 服务详情头部 金额 */
        mPriceAdapter = ServiceDetailPriceAdapter()
        mPriceAdapter.moutClickListener = this
        delegateAdapter.addAdapter(mPriceAdapter)

        /*** 服务详情 拼团列表 */
        mAssembleTeamAdapter = ServiceAssembleTeamAdapter()
        mAssembleTeamAdapter.moutClickListener = this
        delegateAdapter.addAdapter(mAssembleTeamAdapter)

        /*** 服务详情 门店部分详情 */
        mStoreDetailAdapter = ServiceStoreDetailAdapter()
        mStoreDetailAdapter.moutClickListener = this
        delegateAdapter.addAdapter(mStoreDetailAdapter)

        /*** 服务详情 店铺推荐 */
        mStoreRecommendAdapter = ServiceStoreRecommendAdapter()
        mStoreRecommendAdapter.moutClickListener = this
        delegateAdapter.addAdapter(mStoreRecommendAdapter)

        /*** 服务详情 商品详情 */
        mGoodsDetailAdapter = ServiceGoodsDetailAdapter()
        mGoodsDetailAdapter.moutClickListener = this
        delegateAdapter.addAdapter(mGoodsDetailAdapter)

        /*** 获取高度 用于锚点定位 */
        setScrollY()
    }

    /**
     * 设置滑动所需高度
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
     * 商品详情按钮事件处理
     */
    private fun showSkuAct(isAddShoppingCat: Boolean) {
        if (mCheckStoreModel == null) {
            showToast("服务商品未配置服务门店请联系商家")
            return
        }

        if (!isAddShoppingCat && mGoodsDetailPin != null && mGoodsDetailPin?.assembleDTO?.assembleInventory!! <= 0) {
            showToast("该商品已售罄，看看其他商品吧~")
            return
        }

        if (!isAddShoppingCat && mGoodsDetailKick != null && mGoodsDetailKick?.bargainInfoDTO?.marketingGoodsChildDTOS!![0].availableInventory <= 0) {
            showToast("该商品已售罄，看看其他商品吧~")
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
                    //多规格处理地方
                    if (isAddShoppingCat) {
                        when (mGoodsType) {
                            0 -> addGoodsShoppingCat(goodsSpecDetail)
                            else -> buildLeftSkuBuy(goodsDetail, goodsSpecDetail!!)
                        }
                    } else {
                        //选的活动商品 直接去（砍价、参团、抢购、购买）
                        goodsSpecDetail?.let { buildRightConfirm(it) }
                    }
                }
            }
            if (goodsDetail.cheIsNoSku()) {
                //单规格处理地方
                if (isAddShoppingCat) {//底部左侧按钮事件处理
                    when (mGoodsType) {
                        0 -> addGoodsShoppingCat(mGoodsSpecDetail)
                        else -> buildLeftBuy(goodsDetail)
                    }
                } else {
                    mGoodsSpecDetail?.let { buildRightConfirm(it) }
                }
                return
            }
            //多规格 右侧立即抢购或其他类型
            mGoodsSpecDialog?.show(supportFragmentManager, "specDialog")
        }
    }

    /**
     * 单独购买
     */
    private fun buildLeftBuy(it: GoodsDetail) {
        //单独买
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
     * 多规格单独购买
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
     * 商品加入购物车
     */
    private fun addGoodsShoppingCat(goodsSpecDetail: GoodsSpecDetail?) {
        //考虑到多规格模型替换 使用传递模型方式定义
        mGoodsDetail?.let {
            goodsSpecDetail?.let { spec ->
                if (3 == mGoodsType) { // 当前了解只有秒杀需要走这个库存判断
                    //加入购物车左边
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
                                showToast("活动库存不足")
                            }
                            it.marketingSalesMax - spec.nowOrderBuyCount <= 0 -> {
                                showToast("您已购${spec.nowOrderBuyCount}件，该活动商品限购${it.marketingSalesMax}件")
                            }
                            else -> {
                                showToast("起购${it.marketingSalesMin}件，您已购${spec.nowOrderBuyCount}件，该活动商品限购${it.marketingSalesMax}件")
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
     * 发起砍价、参与拼团、立即抢购
     */
    private fun buildRightConfirm(goodsSpecDetail: GoodsSpecDetail) {

        mGoodsDetail?.let {
            goodsSpecDetail?.let { spec ->
                if (3 == mGoodsType) { // 当前了解只有秒杀需要走这个库存判断
                    //加入购物车左边
                    if (it.getMarketingSalesMin() > it.getRealCanBuy(it.marketingSalesMax, spec.nowOrderBuyCount, it.marketingGoodsChildren[0].getAvailableInventoryMark(it.goodsChildren[0]))) {
                        when {
                            it.getMarketingSalesMin() > it.marketingGoodsChildren[0]
                                .getAvailableInventoryMark(it.goodsChildren[0]) -> {
                                showToast("活动库存不足")
                                return
                            }
                            it.marketingSalesMax - spec.nowOrderBuyCount <= 0 -> {
                                showToast("您已购${spec.nowOrderBuyCount}件，该活动商品限购${it.marketingSalesMax}件")
                                return
                            }
                            else -> {
                                showToast("起购${it.marketingSalesMin}件，您已购${spec.nowOrderBuyCount}件，该活动商品限购${it.marketingSalesMax}件")
                                return
                            }
                        }
                    }
                }
            }
        }

        when (mGoodsType) {
            1 -> { //直接砍价
                ARouter.getInstance()
                    .build(DiscountRoutes.DIS_KICKDETAIL)
                    .withString("bargainId", bargainId + "")
                    .withString(//访问门店
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
                //可以参加拼团 \ 抢购
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
     * 获取 GoodsBasketCell模型
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
        /*** goodsMarketing 所需参数 */
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

        /** 规格动态数据默认都取得多规格数据 */
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
        /*** 单规格改变动态数据 */
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
                else -> { //默认缺省都走goodsChildren
                    mCellPlatformPrice = mGoodsDetail?.goodsChildren?.get(0)?.platformPrice
                    mCellRetailPrice = mGoodsDetail?.goodsChildren?.get(0)?.retailPrice
                    mCellPlusPrice = mGoodsDetail?.goodsChildren?.get(0)?.plusPrice
                    mCellBarcodeSku = mGoodsDetail?.goodsChildren?.get(0)?.barcodeSku
                    if ("1" == isNtReal && "4" == mGoodsDetail?.marketingType) {
                        //新客走向
                        mCellGoodsStock = 1
                        mGoodsSpecId =
                            mGoodsDetail?.marketingGoodsChildren?.get(0)?.goodsChildId
                    } else {
                        //不是新客
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
     * 适配器回调处理
     */
    override fun outClick(function: String, obj: Any) {
        when (function) {
            "倒计时结束" -> getData()
            "back" -> finish()
            "share" -> {
                if (mGoodsEmptyAdapter.getModel() != null) {
                    showToast("原商品已下架，您可以在看看其他商品~")
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
            //下单 -> 立即购买
            "confirmAction" -> {
                teamNum = teamNumOrg//参与别人的团
                showSkuAct(false)
            }
            // 一键参团
            "groupDialog" -> {
                if (obj is AssemableTeam) {
                    teamNum = obj.teamNum//参与别人的团
                    showSkuAct(false)
                }
            }
            //导航
            "navigation" -> NavigationSelectDialog.newInstance()
                .setStoreDetailModel(mCheckStoreModel!!)
                .show(supportFragmentManager, "navigationDialog")
            //拨打手机号
            "phone" -> PhoneUtils.callPhone(mContext, obj.toString())
            //查看门店列表
            "lookShopList" ->
                ARouter.getInstance()
                    .build(SecondRoutes.SECOND_SHOP_LIST)
                    .withString("shopId", goodsShopId)
                    .withCharSequenceArray(
                        "goodsShopIds",
                        if (mGoodsDetail != null) mGoodsDetail?.goodsShopIdListStringArray else null
                    )
                    .navigation(mActivity, Constants.STATUS_APPOINTMENT_SHOP_CODE)
            //查看所有项目
            "lookAllProject" ->
                ARouter.getInstance()
                    .build(SecondRoutes.SECOND_SHOP_DETAIL)
                    .withString("shopId", goodsShopId)
                    .navigation()
            //查看店铺推荐项目
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
     * 普通商品详情数据
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
     *砍价详情数据
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
     * 拼团详情数据
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
     * 获取已参与拼团人员列表数据
     */
    override fun successTeamList(couponInfoByMerchants: MutableList<AssemableTeam>?) {
        mAssembleTeamAdapter.setTeamList(couponInfoByMerchants)
    }

    /**
     * 推荐商品列表
     */
    override fun successGetRecommendList(
        recommendList: MutableList<RecommendList>?,
        firstPageSize: Int
    ) {
        titles.clear()
        //门店信息tab加入判断
        if (mCheckStoreModel != null) {
            if (!titles.contains(mShopTitleName)) {
                titles.add(mShopTitleName)
            }
        }
        //门店推荐数据列表
        if (recommendList.isNullOrEmpty().not()) {
            mStoreRecommendAdapter.isLoadSuccess = false
            mStoreRecommendAdapter.mRecommendList =
                if (recommendList?.size!! > 3) recommendList.subList(0, 3)
                else recommendList
            mStoreRecommendAdapter.model = ""
            //门店推荐商品tab加入判断
            if (!titles.contains(mShopRecommendName)) {
                titles.add(mShopRecommendName)
            }
        }
        //商品详情tab加入判断
        if (mGoodsDetail?.additionNote.isNullOrEmpty().not()) {
            if (!titles.contains(mGoodsDetailName)) {
                titles.add(mGoodsDetailName)
            }
        }
        //初始化tab
        initTab()
    }

    /**
     * 条码数据回调
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
     * 加入购物车成功
     */
    override fun successAddShoppingCat(result: String?) {
        showToast("已加入购物车~")
    }

    override fun onSucessGetShopDetailOnly(detailModel: ShopDetailModel?) {
        if(!"1".equals(detailModel!!.merchantType)){//不是异业门店
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
     * 门店列表数据
     */
    override fun onGetShopListSuccess(shopList: MutableList<ShopDetailModel>?) {//
        if (ListUtil.isEmpty(shopList)) {
            showToast("未查询到商品适用门店")
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
     *  页面返回回调
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
     * 是否有匹配门店id
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
     *  设置适配器数据源
     */
    private fun setModel() {
        if("2".equals(mGoodsDetail!!.goodsType)){//标品啊 直接弹出
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
        } else if(1!=mGoodsDetail!!.differentSymbol){//不是异业门店
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
                //有商品有适用门店 然后门店列表接口可能突发问题没有请求 刷新时再次请求一次
                mParamsMap.clear()
                mParamsMap["shopId"] = goodsShopIdListStringArray[0]
                mParamsMap["shopTypeList"] = arrayOf("1")
                mShopPresenter?.getShopList(mParamsMap)
                return
            }
            if (!ListUtil.isEmpty(mNetWorkShopList) && mCheckStoreModel == null) {
                for (model in mNetWorkShopList) {
                    /*if (goodsShopIdListStringArray.isNullOrEmpty()) {
                        // 为空情况也就是没有适用门店 拿到传入的shopId对比门店列表数据获取到门店详情数据
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

        //取交集后的门店里面是否为空
        if (mShopList.isEmpty().not()) {
            //排序
            mShopList.sort()
            if ("0".equals(goodsShopId)) {
                if ("1".equals(mGoodsDetail!!.goodsType)) {
                    //不为拼团、砍价商品 需要请求两次
                    goodsShopId = mShopList[0].id;//修正shopid为最近的
                    mCheckStoreModel = mShopList[0]
                    showToast("憨妈妈将切换至离您最近的服务门店")
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
        /*** 普通商品库存判断 */
        if (mGoodsDetail != null && mGoodsDetail?.goodsChildren.isNullOrEmpty()
                .not() && mGoodsDetail?.goodsChildren!![0].getAvailableInventory() <= 0
        ) {
            isZero = true
            showToast("该商品已售罄，看看其他商品吧~")
            tv_bask_action.isEnabled = !isZero
            tv_confirm_action.isEnabled = !isZero
        }
        /*** 秒杀商品库存判断 */
        if (mGoodsDetail != null && mGoodsDetail?.marketingType == "3" && mGoodsDetail?.marketingGoodsChildren.isNullOrEmpty()
                .not() && mGoodsDetail?.marketingGoodsChildren!![0].getAvailableInventoryMark(mGoodsDetail?.goodsChildren!![0]) <= 0
        ) {
            isMarkZero = true
            showToast("该商品已售罄，看看其他商品吧~")
            if("3".equals(marketingType)){//进来的是活动
                tv_bask_action.isEnabled = !isZero
                tv_confirm_action.isEnabled = !isMarkZero
            }else{
                mGoodsDetail?.marketingType=null
                tv_bask_action.isEnabled = !isZero
                tv_confirm_action.isEnabled = !isZero
            }

        }

        /*** 拼团商品库存判断 */
        if (mGoodsDetailPin != null
            && mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS?.isNullOrEmpty()!!.not()
            && mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS?.size==1
            &&mGoodsDetailPin?.assembleDTO?.marketingGoodsChildDTOS?.get(0)?.availableInventory!!<=0) {
            isMarkZero = true
            showToast("该商品已售罄，看看其他商品吧~")
            tv_bask_action.isEnabled = !isZero
            tv_confirm_action.isEnabled = !isMarkZero
        }

        /*** 砍价商品库存判断 */
        if (mGoodsDetailKick != null
            && mGoodsDetailKick?.bargainInfoDTO?.marketingGoodsChildDTOS?.isNullOrEmpty()!!.not()
            && mGoodsDetailKick?.bargainInfoDTO?.marketingGoodsChildDTOS?.size==1
            && mGoodsDetailKick?.bargainInfoDTO?.marketingGoodsChildDTOS?.get(0)?.availableInventory!! <= 0) {
            isMarkZero = true
            showToast("该商品已售罄，看看其他商品吧~")
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

        /*** 初始化多规格弹框 */
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
            /*** 初始化多规格模型 */
            mGoodsSpecDetail = GoodsSpecDetail(
                it.storePrice,
                it.getPlatformPrice(),
                it.headImages[0].splash,
                it.goodsType,
                saleMax, saleMin,
                "0" == isNtReal,
                it.isPlusOnly
            )
            if (!it.cheIsNoSku()) { //多规格初始化弹框
                mGoodsSpecDialog?.let { spec ->
                    spec.setGoodsId(it.id, goodsShopId)
                    spec.mGoodsSpecDetail = mGoodsSpecDetail
                    spec.setMarketing(mGoodsDetail?.marketingType,mGoodsDetail?.mapMarketingGoodsId)
                    spec.mShopModel = mCheckStoreModel
                    when (mGoodsType) {
                        1 -> {
                            //2021/5/17 为了切换门店去掉这块判断
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
     * 设置按钮样式
     */
    @SuppressLint("SetTextI18n")
    private fun setActionStyle() {
        mGoodsType = when { //此处修改跳转使用详情接口回调的 marketingType决定类型
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
        tv_confirm_action.text = "立即购买"
        tv_bask_action.text = "加入购物车"
        //按钮设置
        when (mGoodsType) {
            3 -> {
                layoutParams.leftMargin = TransformUtil.dp2px(mContext, 10f).toInt()
                shapeDrawableBuilder = shapeDrawableBuilder
                    .setTopLeftRadius(shapeDrawableBuilder.topRightRadius)
                    .setBottomLeftRadius(shapeDrawableBuilder.topRightRadius)
                /* tv_confirm_action.topLeftRadius = tv_confirm_action.topRightRadius
                 tv_confirm_action.bottomLeftRadius = tv_confirm_action.topRightRadius*/
                tv_bask_action.visibility = View.GONE
                tv_confirm_action.text = "立即抢购"
            }
            2 -> {
                tv_bask_action.text =
                    "单独购买\n¥${
                        StringUtils.getResultPrice(
                            mGoodsDetail?.getPlatformPrice().toString()
                        )
                    }"
                tv_confirm_action.text =
                    "${mGoodsDetailPin?.assembleDTO?.regimentSize}人团\n¥${
                        StringUtils.getResultPrice(
                            mGoodsDetailPin?.assembleDTO?.assemblePrice.toString()
                        )
                    }"
            }
            1 -> {
                tv_bask_action.text =
                    "单独购买\n¥${
                        StringUtils.getResultPrice(
                            mGoodsDetail?.getPlatformPrice().toString()
                        )
                    }"
                tv_confirm_action.text =
                    "发起砍价\n¥${StringUtils.getResultPrice(mGoodsDetailKick?.bargainInfoDTO?.floorPrice.toString())}"
            }
        }
        tv_confirm_action.layoutParams = layoutParams
        shapeDrawableBuilder.intoBackground()
    }

    /**
     * 设置商品情况
     */
    private fun buildErrorGoods() {
        cl_second_bottom_action.visibility = View.GONE
        mBannerAdapter.model = null
        mPriceAdapter.model = null
        mGoodsEmptyAdapter.model = "原商品已下架，为您推荐相关商品"
    }
}