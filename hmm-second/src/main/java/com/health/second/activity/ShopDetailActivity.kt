package com.health.second.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.health.second.R
import com.health.second.adapter.*
import com.health.second.contract.ShopDetailContract
import com.health.second.fragment.ShopDetailGoodsFragment
import com.health.second.fragment.ShopDetailInfoFragment
import com.health.second.fragment.ShopDetailMarketingFragment
import com.health.second.model.PeopleListModel
import com.health.second.presenter.ShopDetailPresenter
import com.healthy.library.adapter.CanReplacePageAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.base.BaseAdapter
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.dialog.NavigationSelectDialog
import com.healthy.library.constant.UrlKeys
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.interfaces.IsNeedShare
import com.healthy.library.model.*
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.utils.*
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_shop_detail.*
import java.util.*
import kotlin.collections.ArrayList

@Route(path = SecondRoutes.SECOND_SHOP_DETAIL)
class ShopDetailActivity : BaseActivity(), IsFitsSystemWindows, IsNeedShare,
    ShopDetailContract.View, BaseAdapter.OnOutClickListener, OnRefreshListener {

    @Autowired
    @JvmField
    var shopId: String = ""

    private var mFragmentList: MutableList<Fragment> = ArrayList()
    private var mTitles: MutableList<String> = ArrayList()
    private var pageAdapter: CanReplacePageAdapter? = null
    private var shopDetailMarketingFragment: ShopDetailMarketingFragment? = null
    private var shopDetailGoodsFragment: ShopDetailGoodsFragment? = null
    private var shopDetailInfoFragment: ShopDetailInfoFragment? = null

    private var shopDetailPresenter: ShopDetailPresenter? = null
    private var shopDetail: ShopDetailModel? = null
    private var surl = ""
    private var sdes = "分享给你一家我喜欢的商户，进来看看吧~" // 分享描述
    private var stitle = "商家联盟门店详情" // 分享标题
    private var sBitmap: Bitmap? = null
    private var isFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        shopDetailPresenter = ShopDetailPresenter(this, this)
        showLoading()
        getData()
        initListener()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_shop_detail
    }

    override fun findViews() {
        super.findViews()
        initListener()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        getData()
        shopDetailMarketingFragment!!.refresh()
        shopDetailGoodsFragment!!.refresh()
        shopDetailInfoFragment!!.refresh()
    }

    override fun getData() {
        super.getData()
        shopDetailPresenter?.getStoreDetail(shopId)
    }


    override fun onGetStoreDetailSuccess(shopDetailModel: ShopDetailModel?) {
        layout_refresh.finishRefresh()
        if (shopDetailModel != null) {
            this.shopDetail = shopDetailModel
            buildTopView()
        } else {
            showEmpty()
        }
        if (isFirst) {
            isFirst = false
            buildTab()
        }
    }

    private fun buildTopView() {
        banner.setPageMargin(
            0,
            TransformUtil.dp2px(mContext, 200f).toInt(),
            TransformUtil.dp2px(mContext, 6f).toInt()
        )
            .setAutoPlay(false)
            .setOuterPageChangeListener(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
//                        currentPage = 1
                }
            })
        if (shopDetail?.chainShopName != null && !TextUtils.isEmpty(shopDetail?.chainShopName)) {
            shopName.text = shopDetail?.shopName + "(" + shopDetail?.chainShopName + ")"
            storeName.text = shopDetail?.shopName + "(" + shopDetail?.chainShopName + ")"
        } else {
            shopName.text = if (shopDetail?.shopName != null) shopDetail?.shopName else ""
            storeName.text = if (shopDetail?.shopName != null) shopDetail?.shopName else ""
        }
        shopAddress.text =
            shopDetail?.provinceName + shopDetail?.cityName + shopDetail?.addressAreaName + shopDetail?.addressDetails
        shopDistance.text = ParseUtils.parseDistance(
            (shopDetail?.distance.toString().toDouble()).toString()
        )
        if (shopDetail?.shopBusinessOfArea != null && shopDetail?.shopBusinessOfArea!!.size > 0) {
            var time: String? = ""
            if (shopDetail?.shopBusinessOfArea!!.size == 1) {
                time = shopDetail?.shopBusinessOfArea!![0]
            }
            if (shopDetail?.shopBusinessOfArea!!.size >= 2) {
                time =
                    shopDetail?.shopBusinessOfArea!![0].toString() + "," + shopDetail?.shopBusinessOfArea!![1]
            }
            shopTime.text = time
        }
        if (shopDetail?.envPicUrl != null && !TextUtils.isEmpty(shopDetail?.envPicUrl)) {
            val urlArray = shopDetail?.envPicUrl!!.split(",").toTypedArray()
            GlideCopy.with(mContext).load(urlArray[0])
                .into(object : SimpleTarget<Drawable?>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        sBitmap = DrawableUtils.drawableToBitmap(resource)
                    }
                })
            GlideCopy.with(mContext)
                .load(urlArray[0])
                .into(headImg);
            val bannerAdapter = BannerAdapter()
            banner.adapter = bannerAdapter
            bannerAdapter.setData(urlArray, mContext)
            bannerAdapter.notifyDataSetChanged()
        }
        itv_StoreDetailPhone.setOnClickListener {
            PhoneUtils.callPhone(mContext, shopDetail!!.appointmentPhone)
        }
        itv_StoreDetailNavigation.setOnClickListener {
            var navigationSelectDialog: NavigationSelectDialog =
                NavigationSelectDialog.newInstance();
            shopDetail?.let { navigationSelectDialog.setStoreDetailModel(it) }
            navigationSelectDialog.show(supportFragmentManager, "navigationDialog")
        }
    }

    private fun buildTab() {
        mTitles.clear()
        mTitles.add("本店活动")
        mTitles.add("商品")
        mTitles.add("门店信息")
        mFragmentList.clear()
        shopDetailMarketingFragment = ShopDetailMarketingFragment
            .newInstance(shopId, shopDetail?.merchantType.toString())
        shopDetailGoodsFragment = ShopDetailGoodsFragment
            .newInstance(shopId, shopDetail?.merchantType.toString())
        shopDetailInfoFragment = ShopDetailInfoFragment
            .newInstance(shopId, null.toString())
        mFragmentList.add(shopDetailMarketingFragment!!)
        mFragmentList.add(shopDetailGoodsFragment!!)
        mFragmentList.add(shopDetailInfoFragment!!)
        pager.offscreenPageLimit = mFragmentList.size - 1
        pageAdapter = CanReplacePageAdapter(supportFragmentManager, mFragmentList, mTitles)
        pager.adapter = pageAdapter
        pager.setCurrentItem(0, false)
        tab.setupWithViewPager(pager)
        for (i in 0 until tab.getTabCount()) {
            //插入tab标签
            val tab: TabLayout.Tab = tab.getTabAt(i)!!
            if (tab != null) {
                val result: View =
                    LayoutInflater.from(mContext)
                        .inflate(R.layout.item_shop_detial_tab_layout, null)
                if (i == 0) {
                    changeTabStatus(result, true)
                } else {
                    changeTabStatus(result, false)
                }
                val titleFirst = result.findViewById<TextView>(R.id.titleFirst)
                titleFirst.text = mTitles[i]
                tab.customView = result
            }
        }
        tab.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab == null || tab.text == null) {
                    return
                }
                val trim = tab.text.toString().trim { it <= ' ' }
                val spStr = SpannableString(trim)
                val styleSpan_B = StyleSpan(Typeface.BOLD)
                spStr.setSpan(styleSpan_B, 0, trim.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                tab.text = spStr
                changeTabStatus(tab.customView, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                if (tab == null || tab.text == null) {
                    return
                }
                val trim = tab.text.toString().trim { it <= ' ' }
                val spStr = SpannableString(trim)
                val styleSpan_B = StyleSpan(Typeface.NORMAL)
                spStr.setSpan(styleSpan_B, 0, trim.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                tab.text = spStr
                changeTabStatus(tab.customView, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }

    private fun changeTabStatus(view: View?, selected: Boolean) {
        if (view != null) {
            val titleFirst = view.findViewById<TextView>(R.id.titleFirst)
            val indictor = view.findViewById<View>(R.id.indictor)
            if (selected) {
                indictor.visibility = View.VISIBLE
                titleFirst.setTextColor(Color.parseColor("#333333"))
                titleFirst.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                indictor.visibility = View.INVISIBLE
                titleFirst.setTextColor(Color.parseColor("#666666"))
                titleFirst.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
    }

    private fun initListener() {
        layout_refresh.setOnRefreshListener(this)
        layout_refresh.setEnableLoadMore(false)
        storeShare.setOnClickListener {
            showShare()
        }
        storeShare2.setOnClickListener {
            showShare()
        }
        app_bar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                when {
                    state === State.EXPANDED -> {
                        //展开状态
                        translateTopBar.setBackgroundColor(Color.parseColor("#00000000"))
                        storeBack2.setImageResource(R.drawable.ic_back_withboder_white)
                        storeShare2.setImageResource(R.drawable.ic_share_withboder_white)
                        storeName2.text = ""
                        tabLiner.setBackgroundColor(Color.parseColor("#F6F7F9"))
                        translateTopBar.visibility = View.VISIBLE
                        whiteTopBar.visibility = View.GONE
                    }
                    state === State.COLLAPSED -> {
                        //折叠状态
                        translateTopBar.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        storeBack2.setImageResource(R.drawable.ic_back_withboder)
                        storeShare2.setImageResource(R.drawable.icon_second_bank_share)
                        if (shopDetail?.chainShopName != null && !TextUtils.isEmpty(shopDetail?.chainShopName)) {
                            storeName2.text =
                                shopDetail?.shopName + "(" + shopDetail?.chainShopName + ")"
                        } else {
                            storeName2.text =
                                if (shopDetail?.shopName != null) shopDetail?.shopName else ""
                        }
                        tabLiner.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        translateTopBar.visibility = View.VISIBLE
                        whiteTopBar.visibility = View.INVISIBLE
                    }
                    else -> {
                        //中间状态
                    }
                }
            }
        })
        storeBack.setOnClickListener { finish() }
        storeBack2.setOnClickListener { finish() }
    }

    override fun onGetPeopleListSuccess(result: MutableList<PeopleListModel>?) {

    }

    override fun onGetGoodsListSuccess(
        list: MutableList<SortGoodsListModel>?,
        pageInfo: OrderListPageInfo?
    ) {
        TODO("Not yet implemented")
    }

    override fun onGetMarketingSuccess(list: MutableList<ShopDetailMarketing>?) {

    }

    override fun onSuccessManDetail(result: TechnicianResult?) {
        TODO("Not yet implemented")
    }

    override fun outClick(function: String, obj: Any) {
    }

    override fun getSurl(): String {
        val nokmap: MutableMap<String, String> = HashMap<String, String>()
        nokmap["soure"] = "门店右上角的分享按钮点击量"
        MobclickAgent.onEvent(mContext, "event2APPShopDetiaShareClick", nokmap)

        val url = String.format(
            "%s?type=7&id=%s&categoryNo=%s&cityNo=%s",
            SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl),
            shopId,
            null,
            LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE)
        )
        surl = url
        return surl
    }

    override fun getSdes(): String {
        return sdes
    }

    override fun getStitle(): String {
        stitle = shopDetail!!.shopName
        return stitle
    }

    override fun getsBitmap(): Bitmap? {
        return sBitmap
    }

    abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {
        enum class State {
            EXPANDED, COLLAPSED, IDLE
        }

        private var mCurrentState = State.IDLE

        override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
            mCurrentState = if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED)
                }
                State.EXPANDED
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED)
                }
                State.COLLAPSED
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE)
                }
                State.IDLE
            }
        }

        abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: State?)
    }
}