//package com.health.mall.fragment
//
//import android.Manifest
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.graphics.Color
//import android.view.View
//import androidx.recyclerview.widget.RecyclerView
//import com.alibaba.android.arouter.facade.annotation.Route
//import com.alibaba.android.arouter.launcher.ARouter
//import com.alibaba.android.vlayout.DelegateAdapter
//import com.alibaba.android.vlayout.VirtualLayoutManager
//import com.amap.api.location.AMapLocation
//import com.health.mall.R
//import com.health.mall.adapter.IndexCategoryGridAdapter
//import com.health.mall.adapter.index.*
//import com.health.mall.contract.MallContract2
//import com.health.mall.model.*
//import com.health.mall.presenter.MallPresenter2
//import com.healthy.library.base.BaseFragment
//import com.healthy.library.constant.SpKey
//import com.healthy.library.contract.AdContract
//import com.healthy.library.message.UpdateMallInfo
//import com.healthy.library.message.UpdateUserLocationMsg
//import com.healthy.library.model.AdModel
//import com.healthy.library.model.MallKKGroup
//import com.healthy.library.model.MallKKModel
//import com.healthy.library.presenter.AdPresenter
//import com.healthy.library.routes.AppRoutes
//import com.healthy.library.routes.LibraryRoutes
//import com.healthy.library.routes.ServiceRoutes
//import com.healthy.library.utils.*
//import com.healthy.library.business.MessageDialog
//import com.healthy.library.business.NoStoreDialog
//import com.healthy.library.businessutil.LocUtil
//import com.healthy.library.widget.StatusLayout
//import com.scwang.smart.refresh.layout.api.RefreshLayout
//import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
//import com.umeng.analytics.MobclickAgent
//import kotlinx.android.synthetic.main.fragment_mall2.*
//import org.greenrobot.eventbus.EventBus
//import org.greenrobot.eventbus.Subscribe
//import org.greenrobot.eventbus.ThreadMode
//
///**
// * @author Li
// * @date 2019/03/01 11:24
// * @des 商城
// */
//@Route(path = AppRoutes.MODULE_MALL2)
//class MallFragment2 : BaseFragment(), AdContract.View, MallContract2.View, OnRefreshLoadMoreListener {
//    override fun onGetKKDataSuccess(mallkkmodels: ArrayList<MallKKModel>?) {
////        mallkkmodels?.let {
////            if (!this::indexDiscountHotAdapter.isInitialized) {
////                indexDiscountHotAdapter = IndexDiscountHotAdapter(mContext)
////            }
////            indexDiscountHotAdapter.setData(mutableListOf(it) as ArrayList<ArrayList<MallKKModel>>)
////        }
//    }
//
//    override fun onGetKKGroupDataSuccess(mallkkmodels: ArrayList<MallKKGroup>?) {
////        mallkkmodels?.let {
////            if (!this::indexGroupHotAdapter.isInitialized) {
////                indexGroupHotAdapter = IndexGroupHotAdapter(mContext)
////            }
////            indexGroupHotAdapter.setData(mutableListOf(it) as ArrayList<ArrayList<MallKKGroup>>)
////        }
//    }
//
//
//    private val mPermissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION)
//    private val RC_LOCATION = 102
//    private val RC_PROVINCE_CITY = 469
//    private var mLocationUtil: LocationUtil? = null
//    private var aMapLocation: AMapLocation? = null
//    private var storetypes: ArrayList<TypeModel>? = null
//    private lateinit var presenter2: MallPresenter2
//    private lateinit var adPresenter: AdPresenter
//    private lateinit var delegateAdapter: DelegateAdapter
//    private var currentPage = 1
//    private var reLocTime = 0
//    private var mallTab = -1
//
//    private lateinit var nostoredialog: androidx.fragment.app.DialogFragment
//
//    //    private lateinit var indexCategoryToHomeAdapter: IndexCategoryAdapter
//    //private lateinit var indexCategoryToStoreAdapter: IndexCategoryAdapter
//    private lateinit var indexCategoryGridAdapter: IndexCategoryGridAdapter
//
//    private lateinit var indexBannerAdapter: IndexBannerAdapter
//    private lateinit var indexDiscountShopAdapter: IndexDiscountShopAdapter
//    private lateinit var indexDiscountHotAdapter: IndexDiscountHotAdapter
//    private lateinit var indexGroupHotAdapter: IndexGroupHotAdapter
//    private lateinit var indexDiscountTitleAdapter: IndexTitleAdapter
//
//    //    private lateinit var indexCategoryTitleToHomeAdapter: IndexCategoryHomeTitleAdapter
////    private lateinit var indexCategoryTitleToStoreAdapter: IndexCategoryStoreTitleAdapter
//    private lateinit var indexTitleNearbyAdapter: IndexTitleAdapter2
//    private lateinit var indexNearbyShopAdapter: IndexNearbyShopAdapter
//    lateinit var locCityName: String
//    lateinit var newCityName: String
//    lateinit var mAdName: String
//    lateinit var mAdCode: String
//    lateinit var mlatitude: String
//    lateinit var mlongitude: String
//
//
//    override fun getLayoutId(): Int {
//        return R.layout.fragment_mall2
//    }
//
//    override fun findViews() {
//
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance(): MallFragment2 {
//            val fragment = MallFragment2()
//            return fragment
//        }
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        init()
//    }
//
//    override fun onFirstVisible() {
//        super.onFirstVisible()
//
//    }
//
//    override fun onClick(v: View?) {
//        super.onClick(v)
//        when (v?.id) {
//            R.id.tv_location -> {
//                ARouter.getInstance().build(LibraryRoutes.LIBRARY_LOC)
//                        .navigation(mActivity, RC_PROVINCE_CITY)
//            }
//
//            R.id.tv_choose_city -> {
//
//                if (this::nostoredialog.isInitialized) {
//                    try {
//                        nostoredialog.dismiss()
//                    } catch (e: Exception) {
//                    }
//                }
//                nostoredialog = NoStoreDialog.newInstance().setRightClickListener {
//                    val aMapLocation = LocUtil.getLocationBean(mContext, SpKey.LOC_ORG)
//                    aMapLocation.district = "虎丘区"
//                    aMapLocation.city = "苏州市"
//                    aMapLocation.adCode = "320505"
//                    LocUtil.storeLocationChose(mContext, aMapLocation)
//                    successLoaction()
//                }
//                nostoredialog.show(childFragmentManager, "入驻")
//            }
//            R.id.serarchKeyWordLL -> {
//                ARouter.getInstance().build(ServiceRoutes.SERVICE_SEACH)
//                        .navigation(mActivity, RC_PROVINCE_CITY)
//            }
//            R.id.img_back -> {
//                this.activity?.finish()
//            }
//        }
//
//    }
//
//
//    private fun init() {
//        println("开去")
//        layout_refresh.setOnRefreshLoadMoreListener(this)
//        layout_refresh.finishLoadMoreWithNoMoreData()
//        img_back.setOnClickListener(this)
//        serarchKeyWordLL.setOnClickListener(this)
//        val virtualLayoutManager = VirtualLayoutManager(mContext)
//        delegateAdapter = DelegateAdapter(virtualLayoutManager)
//        recycler_index.layoutManager = virtualLayoutManager
//        recycler_index.adapter = delegateAdapter
//        layout_status.setmOnCustomNetRetryListener { mLocationUtil!!.startLocation() }
//        tv_choose_city.setOnClickListener(this)
//        mLocationUtil = LocationUtil(mContext, object : LocationUtil.OnLocationListener {
//            override fun onLocationStart() {
//                showLoading()
//            }
//
//            override fun onLocationSuccess(aMapLocation: AMapLocation) {
//
//                LocUtil.storeLocation(mContext, aMapLocation)
//                LocUtil.storeLocationChose(mContext, aMapLocation)
//                successLoaction()
//            }
//
//            override fun onLocationFail(errCode: Int) {
//                println("定位失败了：" + errCode)
//                layout_status.updateStatus(StatusLayout.Status.STATUS_CUSTOM)
//                if (NavigateUtils.openGPSSettings(mContext) && reLocTime == 0) {
//                    mLocationUtil!!.startLocation()
//                    reLocTime++;
//                } else {
//                    MessageDialog.newInstance()
//                            .setMessageTopRes(R.drawable.dialog_message_icon_loc, "开启定位", "为给您提供更好的本地化服务，请您到系统设置中打开GPS定位")
//                            .setMessageOkClickListener { IntentUtils.toLocationSetting(mContext) }
//                            .show(childFragmentManager, "打开定位")
//                }
//
//            }
//        })
//        adPresenter = AdPresenter(mContext, this)
//        presenter2 = MallPresenter2(mContext, this)
//        if (PermissionUtils.hasPermissions(mContext, *mPermissions)) {
//            locate()
//        } else {
//            requestPermissions(mPermissions, RC_LOCATION)
//        }
//        recycler_index.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val firstCompletelyVisibleItemPosition = virtualLayoutManager.findFirstCompletelyVisibleItemPosition()
//                if (firstCompletelyVisibleItemPosition >= 1 || firstCompletelyVisibleItemPosition < 0) {
//                    serviceTop.setBackgroundColor(Color.parseColor("#FFFFFF"))
//                    serarchKeyWordLL.setBackgroundResource(R.drawable.shape_search_loc_country_grey)
//                } else {
//                    serviceTop.setBackgroundColor(Color.parseColor("#00000000"))
//                    serarchKeyWordLL.setBackgroundResource(R.drawable.shape_search_loc_country)
//                }
//            }
//        })
//    }
//
//    private fun successLoaction() {
//
//        this@MallFragment2.aMapLocation = LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE)
//        try {
//            //tv_location.text = LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE)
//        } catch (e: Exception) {
//        }
//        locCityName = LocUtil.getCityName(mContext, SpKey.LOC_ORG)
//        newCityName = LocUtil.getCityName(mContext, SpKey.LOC_CHOSE)
//        showContent()
//        getData()
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        EventBus.getDefault().register(this)
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        EventBus.getDefault().unregister(this)
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun updateUserInfo(msg: UpdateUserLocationMsg?) {
//        println("首页进行了变化")
//        mallTab = 0
//        successLoaction()
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun updateMallInfo(msg: UpdateMallInfo?) {
//        if (msg!!.flag == 88) {
//            println("切换了变化")
//            mallTab = 0
//            successLoaction()
//        }
//    }
//
//    fun locate() {
//        println("开开来")
//        if (LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE) != null) {
//            successLoaction()
//        } else {
//            mLocationUtil!!.startLocation()
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == RC_LOCATION) {
//            if (PermissionUtils.hasPermissions(mContext, *permissions)) {
//                locate()
//            } else {
//                if (PermissionUtils.somePermissionPermanentlyDenied(mActivity, *permissions)) {
//                    PermissionUtils.showRationale(mActivity)
//                } else {
//                    requestPermissions(mPermissions, RC_LOCATION)
//                }
//            }
//        }
//    }
//
//    override fun onSucessGetAds(adModels: MutableList<AdModel>?) {
//        indexBannerAdapter.clear()
//        adModels?.let {
//            indexBannerAdapter.setLocation(mAdCode, mAdName, mlatitude, mlongitude)
//            if (it.size > 0) {
//                indexBannerAdapter.setData(mutableListOf(adModels) as ArrayList<ArrayList<AdModel>>)
//            }
//
//        }
//
//    }
//
//    override fun getData() {
//        super.getData()
//        println("载入")
//        aMapLocation?.let {
//            println("载入开始")
//            this.mAdCode = it.adCode
//            this.mlatitude = it.latitude.toString()
//            this.mlongitude = it.longitude.toString()
//            this.mAdName = it.district.toString()
//            if (this::presenter2.isInitialized) {
//                presenter2.getData(it.cityCode,
//                        it.adCode,
//                        it.latitude.toString(),
//                        it.longitude.toString(),
//                        "1",
//                        if (locCityName.equals(newCityName)) {
//                            "1"
//                        } else {
//                            "0"
//                        })
//            }
//        }
//    }
//
//    override fun onRequestFinish() {
//        layout_refresh.finishLoadMore()
//        layout_refresh.finishRefresh()
//    }
//
//    override fun onRefresh(refreshLayout: RefreshLayout) {
//        getData()
//    }
//
//    override fun onLoadMore(refreshLayout: RefreshLayout) {
//        aMapLocation?.let {
//            if (this::presenter2.isInitialized) {
//                presenter2.getData(it.cityCode,
//                        it.adCode,
//                        it.latitude.toString(),
//                        it.longitude.toString(),
//                        (currentPage + 1).toString(), if (locCityName.equals(newCityName)) {
//                    "1"
//                } else {
//                    "0"
//                })
//            }
//        }
//    }
//
//
//    override fun onGetFirstPageDataSuccess(categoriestohome: ArrayList<CategoryModel>?,
//                                           categoriestostore: ArrayList<CategoryModel>?,
//                                           banners: ArrayList<BannerModel>?,
//                                           discountShops: ArrayList<ShopInfoModel>?,
//                                           nearbyShops: ArrayList<ShopInfoModel>?,
//                                           refreshLoadModel: RefreshLoadModel?) {
//        delegateAdapter.clear()
//        currentPage = refreshLoadModel!!.currentPage
//        if (refreshLoadModel!!.shopEnter == 1) {
//            println("装载0")
//            noshopenter.visibility = View.GONE
//            //轮播图
//
//            categoriestohome?.let {
////                if (!this::indexCategoryTitleToHomeAdapter.isInitialized) {
////                    indexCategoryTitleToHomeAdapter = IndexCategoryHomeTitleAdapter(mContext)
////
////                    println("装载2")
////                }
////                if(currentPage==1){
////                    delegateAdapter.addAdapter(indexCategoryTitleToHomeAdapter)
////                }
////                indexCategoryTitleToHomeAdapter.setData(mutableListOf("可到家服务") as ArrayList<String>)
////                if (!this::indexCategoryToHomeAdapter.isInitialized) {
////                    indexCategoryToHomeAdapter = IndexCategoryAdapter(mContext)
////
////                    println("装载3")
////
////                }
////                if(currentPage==1){
////                    delegateAdapter.addAdapter(indexCategoryToHomeAdapter)
////                }
////                indexCategoryToHomeAdapter.setFatherid("74")
////                indexCategoryToHomeAdapter.setLocation(mAdCode,mAdName,mlatitude,mlongitude)
////                indexCategoryToHomeAdapter.setData(it)
//            }
//            //分类到店
//            categoriestostore?.let {
////                if (!this::indexCategoryTitleToStoreAdapter.isInitialized) {
////                    indexCategoryTitleToStoreAdapter = IndexCategoryStoreTitleAdapter(mContext)
////
////                }
////                if(currentPage==1){
////                    delegateAdapter.addAdapter(indexCategoryTitleToStoreAdapter)
////                }
////                indexCategoryTitleToStoreAdapter.setData(mutableListOf("可到店服务") as ArrayList<String>)
////                if (!this::indexCategoryToStoreAdapter.isInitialized) {
////                    indexCategoryToStoreAdapter = IndexCategoryAdapter(mContext)
////                }
////                if (currentPage == 1) {
////                    delegateAdapter.addAdapter(indexCategoryToStoreAdapter)
////                }
////                indexCategoryToStoreAdapter.setLocation(mAdCode, mAdName, mlatitude, mlongitude)
////                indexCategoryToStoreAdapter.setFatherid("74")
////                it.add(CategoryModel("更多服务", "-1", "-1", R.drawable.ic_mall_more_type.toString(), -1, 0))
////                indexCategoryToStoreAdapter.setData(it
//                if (!this::indexCategoryGridAdapter.isInitialized) {
//                    indexCategoryGridAdapter = IndexCategoryGridAdapter()
//                }
//                if (currentPage == 1) {
//                    delegateAdapter.addAdapter(indexCategoryGridAdapter)
//                }
//                indexCategoryGridAdapter.setLocation(mAdCode, mAdName, mlatitude, mlongitude)
//                indexCategoryGridAdapter.setFatherid("74")
//                it.add(CategoryModel("更多服务", "-1", "-1", R.drawable.ic_mall_more_type.toString(), -1, 0))
//                indexCategoryGridAdapter.setData(mutableListOf(it) as ArrayList<ArrayList<CategoryModel>>)
//            }
//            if (!this::indexBannerAdapter.isInitialized) {
//                indexBannerAdapter = IndexBannerAdapter(mContext)
//            }
//            if (currentPage == 1) {
//                delegateAdapter.addAdapter(indexBannerAdapter)
//            }
//            adPresenter.getAd("2", "4", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG))
//
//            if (!this::indexDiscountHotAdapter.isInitialized) {
//                indexDiscountHotAdapter = IndexDiscountHotAdapter(mContext)
//
//            }
//            indexDiscountHotAdapter.setLocation(mAdCode, mAdName, mlatitude, mlongitude)
//            if (currentPage == 1) {
//                delegateAdapter.addAdapter(indexDiscountHotAdapter)
//            }
//            indexDiscountHotAdapter.setData(mutableListOf(null) as ArrayList<ArrayList<MallKKModel>>)
//
//
//
//
//            if (!this::indexGroupHotAdapter.isInitialized) {
//                indexGroupHotAdapter = IndexGroupHotAdapter(mContext)
//
//            }
//            indexGroupHotAdapter.setLocation(mAdCode, mAdName, mlatitude, mlongitude)
//            if (currentPage == 1) {
//                delegateAdapter.addAdapter(indexGroupHotAdapter)
//            }
//            indexGroupHotAdapter.setData(mutableListOf(null) as ArrayList<ArrayList<MallKKGroup>>)
//
//            //优惠商家
//            discountShops?.let {
//
//                if (!this::indexDiscountTitleAdapter.isInitialized) {
//                    indexDiscountTitleAdapter = IndexTitleAdapter(mContext)
//                }
//                indexDiscountTitleAdapter.setLocation(mAdCode, mAdName, mlatitude, mlongitude)
//                indexDiscountTitleAdapter.setFatherid("74")
//                indexDiscountTitleAdapter.setDisCount(refreshLoadModel!!.maxGoodsDiscount.toString())
//                if (currentPage == 1) {
//                    delegateAdapter.addAdapter(indexDiscountTitleAdapter)
//                }
//                indexDiscountTitleAdapter.setData(mutableListOf("优惠商家") as ArrayList<String>)
//
//                if (!this::indexDiscountShopAdapter.isInitialized) {
//                    indexDiscountShopAdapter = IndexDiscountShopAdapter(mContext)
//                }
//                indexDiscountShopAdapter.setLocation(mAdCode, mAdName, mlatitude, mlongitude)
//                if (currentPage == 1) {
//                    delegateAdapter.addAdapter(indexDiscountShopAdapter)
//                }
//                indexDiscountShopAdapter.setData(mutableListOf(it) as ArrayList<ArrayList<ShopInfoModel>>)
//
//            }
//            //附近好店
//            nearbyShops?.let {
//                if (!this::indexTitleNearbyAdapter.isInitialized || currentPage == 1) {
//                    indexTitleNearbyAdapter = IndexTitleAdapter2(mContext)
//                }
//                if (currentPage == 1) {
//                    delegateAdapter.addAdapter(indexTitleNearbyAdapter)
//                }
//                indexTitleNearbyAdapter.setData(mutableListOf("附近好店") as ArrayList<String>)
//
//                if (!this::indexNearbyShopAdapter.isInitialized) {
//                    indexNearbyShopAdapter = IndexNearbyShopAdapter(mContext)
//
//                }
//                indexNearbyShopAdapter.setLocation(mAdCode, mAdName, mlatitude, mlongitude)
//                if (currentPage == 1) {
//                    delegateAdapter.addAdapter(indexNearbyShopAdapter)
//                }
//                indexNearbyShopAdapter.setData(nearbyShops)
//
//            }
//        } else {
//
//            if (this::nostoredialog.isInitialized) {
//                try {
//                    nostoredialog.dismiss()
//                } catch (e: Exception) {
//                }
//            }
//            nostoredialog = NoStoreDialog.newInstance().setRightClickListener {
//                val aMapLocation = LocUtil.getLocationBean(mContext, SpKey.LOC_ORG)
//                aMapLocation.district = "虎丘区"
//                aMapLocation.city = "苏州市"
//                aMapLocation.adCode = "320505"
//                LocUtil.storeLocationChose(mContext, aMapLocation)
//                successLoaction()
//            }
//            if (nostoredialog != null) {
//                println("显示入驻商家2" + ":" + mallTab + ":" + isfragmenthow)
//                nostoredialog.show(childFragmentManager, "入驻")
//            }
//
//            mallTab = -1;
//            noshopenter.visibility = View.VISIBLE
//
//
//        }
//        //分类到家
//
//        refreshLoadModel?.let {
//
//            if (it.isMore != 1) {
//                println("无更多")
//                layout_refresh.finishLoadMoreWithNoMoreData()
//            } else {
//                println("有更多")
//                layout_refresh.setNoMoreData(false)
//                layout_refresh.setEnableLoadMore(true)
//            }
//        }
//        aMapLocation?.let {
//            this.mAdCode = it.adCode
//            this.mlatitude = it.latitude.toString()
//            this.mlongitude = it.longitude.toString()
//            this.mAdName = it.district.toString()
//            if (this::presenter2.isInitialized) {
//                presenter2.getKKData(it.cityCode,
//                        it.adCode,
//                        it.latitude.toString(),
//                        it.longitude.toString(),
//                        "1",
//                        if (locCityName.equals(newCityName)) {
//                            "1"
//                        } else {
//                            "0"
//                        })
//
//                presenter2.getGroupData(it.cityCode,
//                        it.adCode,
//                        it.latitude.toString(),
//                        it.longitude.toString(),
//                        "1",
//                        if (locCityName.equals(newCityName)) {
//                            "1"
//                        } else {
//                            "0"
//                        })
//            }
//        }
//
//    }
//
//    override fun onGetMoreDataSuccess(nearbyShops: ArrayList<ShopInfoModel>?,
//                                      refreshLoadModel: RefreshLoadModel?) {
//        nearbyShops?.let {
//            if (!this::indexNearbyShopAdapter.isInitialized) {
//                indexNearbyShopAdapter = IndexNearbyShopAdapter(mContext)
//                delegateAdapter.addAdapter(indexNearbyShopAdapter)
//            }
//            indexNearbyShopAdapter.addDatas(nearbyShops)
//        }
//
//        refreshLoadModel?.let {
//            currentPage = it.currentPage
//
//            if (it.isMore != 1) {
//                layout_refresh.finishLoadMoreWithNoMoreData()
//            } else {
//                layout_refresh.setNoMoreData(false)
//                layout_refresh.setEnableLoadMore(true)
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_PROVINCE_CITY && resultCode == Activity.RESULT_OK) {
//            successLoaction()
////            if (data != null && data.extras != null) {
////                val extras = data.extras
////                if(extras!!.getString("prov")!=null){
////
////                    tv_location.text = extras!!.getString("prov")+extras!!.getString("city")
////                }else{
////
////                    tv_location.text = extras!!.getString("city")
////                }
////                newCityName=extras!!.getString("city")
////                mlatitude=extras.getDouble("lat").toString();
////                mlongitude=extras.getDouble("lng").toString();
////                mAdCode=extras.getString("code");
////                presenter2.getData(extras!!.getString("city"),
////                        extras.getString("code"),
////                        extras.getDouble("lat").toString(),
////                        extras.getDouble("lng").toString(),
////                        "1",if(locCityName.equals(newCityName)){"1"}else{"0"})
////            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        MobclickAgent.onPageStart("event2MonServiceTimeLimit")//统计页面，"MainScreen"为页面名称，可自定义
//    }
//
//    override fun onPause() {
//        super.onPause()
//        MobclickAgent.onPageEnd("event2MonServiceTimeLimit")
//    }
//}
//
//
//
//
//
//
//
//
