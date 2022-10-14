package com.health.index.fragment

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.index.R
import com.health.index.adapter.*
import com.health.index.contract.IndexSearchContract
import com.health.index.model.IndexAllSee
import com.health.index.presenter.IndexSearchPresenter
import com.healthy.library.adapter.BaseTitleAdapter
import com.healthy.library.adapter.IndexSearchDiscoverAdapter
import com.healthy.library.adapter.PostAdapter
import com.healthy.library.adapter.SearchDiscoverAdapter
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseFragment
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.contract.DataStatisticsContract
import com.healthy.library.dialog.PostCouponDialog
import com.healthy.library.interfaces.IsNeedShare
import com.healthy.library.message.IndexSearchInfo
import com.healthy.library.model.*
import com.healthy.library.presenter.DataStatisticsPresenter
import com.healthy.library.utils.SpUtils
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.hss01248.dialog.interfaces.MyItemDialogListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_index_search_result.*
import kotlinx.android.synthetic.main.fragment_search_child.*
import org.greenrobot.eventbus.EventBus
import java.util.*

class SearchAllFragment : BaseFragment(),
    OnRefreshListener,
    DataStatisticsContract.View,
    IndexSearchContract.View,
    IsNeedShare,
    BaseAdapter.OnOutClickListener,
    IndexSearchAllPostListAdapter.OnPostFansClickListener,
    IndexSearchAllPostListAdapter.OnPostLikeClickListener,
    IndexSearchAllPostListAdapter.OnShareClickListener {

    private var key: String? = null
    private var type: String? = null
    private var pageNum = 1
    private var mTitleModel: TitleModel? = null
    private var surl: String = ""
    private var sdes: String = ""
    private var stitle: String = ""
    private var couponDialog: PostCouponDialog? = null
    private var reviewandwarndialog: Dialog? = null
    private var warndialog: Dialog? = null

    private var mSearchDiscover = mutableListOf<SearchRecordsModel>()

    private var indexSearchPresenter: IndexSearchPresenter? = null
    private var mDataStatisticsPresenter: DataStatisticsPresenter? = null

    private var indexSearchEmptyAdapter: IndexSearchEmptyAdapter? = null//空
    private var baseTitleAdapter: BaseTitleAdapter? = null//搜索发现标题
    private var searchDiscoverAdapter: IndexSearchDiscoverAdapter? = null//搜索发现
    private var indexSearchAllHmmVideoAdapter: IndexSearchAllHmmVideoAdapter? = null//课堂
    private var indexSearchAllVideoAdapter: IndexSearchAllVideoAdapter? = null//短视频
    private var indexSearchAllArticleListAdapter: IndexSearchAllArticleListAdapter? = null//文章
    private var indexSearchAllGoodsAdapter: IndexSearchAllGoodsAdapter? = null//商品
    private var indexSearchAllShopListAdapter: IndexSearchAllShopListAdapter? = null//门店
    private var indexSearchAllQuestionAdapter: IndexSearchAllQuestionAdapter? = null//问答
    private var indexSearchAllPostListAdapter: IndexSearchAllPostListAdapter? = null//帖子
    private var mMap: MutableMap<String, Any> = mutableMapOf()
    private var isNotNull: MutableMap<String, Any> = mutableMapOf() //记录每个位置是否有数据

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString("key")
            type = it.getString("type")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_all
    }

    override fun findViews() {
        indexSearchPresenter = IndexSearchPresenter(mContext, this)
        mDataStatisticsPresenter = DataStatisticsPresenter(mContext, this)
        layout_refresh.setOnRefreshListener(this)
        layout_refresh.setEnableLoadMore(false)
        buildRecyclerHelper()
        getData()
    }

    override fun getData() {
        super.getData()
        indexSearchPresenter?.queryHmmVideoList(
            SimpleHashMapBuilder<String, Any>()
                .puts("pageNum", pageNum)!!
                .puts("pageSize", "3")!!
                .puts("searchContent", key)!!
                .puts("scopeType", "1")!!
                .puts(
                    "memberId", String(
                        Base64.decode(
                            SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                            Base64.DEFAULT
                        )
                    )
                )!! as MutableMap<String, Any>
        )
        indexSearchPresenter?.queryVideoList(
            SimpleHashMapBuilder<String, Any>()
                .puts("pageNum", pageNum)!!
                .puts("pageSize", "4")!!
                .puts("searchContent", key)!!
                .puts("scopeType", "0")!!
                .puts(
                    "memberId", String(
                        Base64.decode(
                            SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                            Base64.DEFAULT
                        )
                    )
                )!! as MutableMap<String, Any>
        )
        indexSearchPresenter?.queryPostList(
            SimpleHashMapBuilder<String, Any>()
                .puts("currentPage", pageNum)!!
                .puts("searchContent", key)!!
                .puts("pageSize", "3")!!
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))!!
                .puts(
                    "shopId",
                    SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)
                )!! as MutableMap<String, Any>
        )
        indexSearchPresenter?.queryArticleList(
            SimpleHashMapBuilder<String, Any>()
                .puts("currentPage", pageNum)!!
                .puts("title", key)!!
                .puts("pageSize", "3")!!
                .puts("knowOrInfoStatus", "1")!! as MutableMap<String, Any>
        )
        indexSearchPresenter?.queryQuestionList(
            SimpleHashMapBuilder<String, Any>()
                .puts("pageNum", pageNum)!!
                .puts("keyWords", key)!!
                .puts("pageSize", "3")!!
                .puts("fragmentBottom", "1")!!
                .puts("addressCityOrg", LocUtil.getCityNo(mContext, SpKey.LOC_ORG))!!
                    as MutableMap<String, Any>
        )
        indexSearchPresenter?.queryGoodsList(
            SimpleHashMapBuilder<String, Any>()
                .puts("pageNum", pageNum)!!
                .puts("goodsTitle", key)!!
                .puts("pageSize", "4")!!
                .puts("publish", "1")!!
                .puts("appSalesSort", "desc")!!
                .puts("platformPriceSort", "asc")!!
                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))!!
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))!!
                    as MutableMap<String, Any>
        )
        indexSearchPresenter?.queryShopList(
            SimpleHashMapBuilder<String, Any>()
                .puts("shopTypeList", "1,3".split(",").toTypedArray())!!
                .puts("shopName", key)!!
                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))!!
                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))!!
                .puts(
                    "shopId",
                    SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)
                )!!
                .puts("haveYY", true) as MutableMap<String, Any>
        )
    }

    override fun onQuerySearchRecordsSuccess(
        records: MutableList<SearchRecordsModel>?,
        isMore: Boolean
    ) {

    }

    override fun onQueryPostListSuccess(records: MutableList<PostDetail>?, isMore: Boolean) {
        indexSearchAllPostListAdapter?.clear()
        if (!ListUtil.isEmpty(records)) {
            indexSearchAllPostListAdapter?.setKey(key!!)
            indexSearchAllPostListAdapter?.setAdapterData(records!!)
            indexSearchAllPostListAdapter?.model = "null"
            isNotNull["3"] = true
        } else {
            isNotNull["3"] = false
        }
        checkNull()
    }

    override fun onQueryHmmVideoListSuccess(
        records: MutableList<VideoListModel>?,
        isMore: Boolean
    ) {
        indexSearchAllHmmVideoAdapter?.clear()
        if (!ListUtil.isEmpty(records)) {
            indexSearchAllHmmVideoAdapter?.setKey(key!!)
            indexSearchAllHmmVideoAdapter?.setAdapterData(records!!)
            indexSearchAllHmmVideoAdapter?.model = "null"
            isNotNull["1"] = true
        } else {
            isNotNull["1"] = false
        }
        checkNull()
    }

    override fun onQueryVideoListSuccess(records: MutableList<VideoListModel>?, isMore: Boolean) {
        indexSearchAllVideoAdapter?.clear()
        if (!ListUtil.isEmpty(records)) {
            indexSearchAllVideoAdapter?.setKey(key!!)
            indexSearchAllVideoAdapter?.setAdapterData(records!!)
            indexSearchAllVideoAdapter?.model = "null"
            isNotNull["2"] = true
        } else {
            isNotNull["2"] = false
        }
        checkNull()
    }

    override fun onSuccessFan(result: Any?) {
        if ("0" == result) {
            showToast("关注成功")
        }
    }

    override fun onSuccessLike() {

    }

    override fun onSuccessGetActivityList() {
        indexSearchAllPostListAdapter?.let { indexSearchAllPostListAdapter?.refreshAdapter() }
    }

    override fun onGetBannerSuccess(adModels: MutableList<AdModel>?) {
    }

    override fun onQueryGoodsListSuccess(
        records: MutableList<SortGoodsListModel>?,
        isMore: Boolean
    ) {
        indexSearchAllGoodsAdapter?.clear()
        if (!ListUtil.isEmpty(records)) {
            indexSearchAllGoodsAdapter?.setKey(key!!)
            indexSearchAllGoodsAdapter?.setAdapterData(records!!)
            indexSearchAllGoodsAdapter?.model = "null"
            isNotNull["6"] = true
        } else {
            isNotNull["6"] = false
        }
        checkNull()
    }

    private fun checkNull() {
        if (isNotNull.size >= 7) {
            var nullNum = 0
            for ((key, value) in isNotNull) {
                if (value == true) {
                    nullNum++
                }
            }
            if (nullNum == 0) {//说明全是false
                showEmptyAdapter()
            }
            layout_refresh.finishRefresh()
            isNotNull.clear()
        }
    }

    override fun onAddPraiseSuccess(result: String, type: Int) {
        if (type == 1) {
            showToast("点赞成功")
        } else {
            showToast("取消赞成功")
        }
        indexSearchPresenter?.queryVideoList(
            SimpleHashMapBuilder<String, Any>()
                .puts("pageNum", pageNum)!!
                .puts("pageSize", "4")!!
                .puts("searchContent", key)!!
                .puts("scopeType", "0")!!
                .puts(
                    "memberId", String(
                        Base64.decode(
                            SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                            Base64.DEFAULT
                        )
                    )
                )!! as MutableMap<String, Any>
        )
    }

    override fun onSearchShopListSuccess(records: MutableList<ShopDetailModel>?) {
        indexSearchAllShopListAdapter?.clear()
        if (!ListUtil.isEmpty(records)) {
            indexSearchAllShopListAdapter?.setKey(key!!)
            indexSearchAllShopListAdapter?.setAdapterData(records!!)
            indexSearchAllShopListAdapter?.model = "null"
            isNotNull["7"] = true
        } else {
            isNotNull["7"] = false
        }
        checkNull()
    }

    override fun onQueryArticleListSuccess(records: MutableList<IndexAllSee>?, isMore: Boolean) {
        indexSearchAllArticleListAdapter?.clear()
        if (!ListUtil.isEmpty(records)) {
            indexSearchAllArticleListAdapter?.setKey(key!!)
            indexSearchAllArticleListAdapter?.setAdapterData(records!!)
            indexSearchAllArticleListAdapter?.model = "null"
            isNotNull["4"] = true
        } else {
            isNotNull["4"] = false
        }
        checkNull()
    }

    override fun onQueryQuestionListSuccess(
        records: MutableList<FaqExportQuestion>?,
        isMore: Boolean
    ) {
        indexSearchAllQuestionAdapter?.clear()
        if (!ListUtil.isEmpty(records)) {
            indexSearchAllQuestionAdapter?.setKey(key!!)
            indexSearchAllQuestionAdapter?.setAdapterData(records!!)
            indexSearchAllQuestionAdapter?.model = "null"
            isNotNull["5"] = true
        } else {
            isNotNull["5"] = false
        }
        checkNull()
    }

    override fun onGetAllQuestionListSuccess(records: MutableList<IndexAllQuestion>?) {
        TODO("Not yet implemented")
    }

    private fun showEmptyAdapter() {
        indexSearchEmptyAdapter?.model = "null"
        if (mSearchDiscover?.isNotEmpty()) {
            var status = SpUtils.getValue(mContext, SpKey.STATUS_USER, 0)
            when (status) {
                1 -> {//备孕中
                    mTitleModel =
                        TitleModel().setTitle("备孕阶段关注热点").setRightTitle("").setShowRight(false)
                            .setSolidColor(mContext.resources.getColor(R.color.color_f7f7fa))
                }
                2 -> {//怀孕中
                    mTitleModel =
                        TitleModel().setTitle("怀孕阶段关注热点").setRightTitle("").setShowRight(false)
                            .setSolidColor(mContext.resources.getColor(R.color.color_f7f7fa))
                }
                3 -> {//育儿期
                    mTitleModel =
                        TitleModel().setTitle("育儿阶段关注热点").setRightTitle("").setShowRight(false)
                            .setSolidColor(mContext.resources.getColor(R.color.color_f7f7fa))
                }
                else -> {
                    mTitleModel =
                        TitleModel().setTitle("关注热点").setRightTitle("").setShowRight(false)
                            .setSolidColor(mContext.resources.getColor(R.color.color_f7f7fa))
                }
            }
            baseTitleAdapter?.model = mTitleModel
            searchDiscoverAdapter?.setAdapterBackgroundColor(R.color.color_f7f7fa, true)
            searchDiscoverAdapter?.setData(mSearchDiscover as ArrayList<SearchRecordsModel>)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchAllFragment().apply {
                arguments = Bundle().apply {
                    putString("key", param1)
                    putString("type", param2)
                }
            }
    }

    public fun refresh(searchStr: String) {
        isNotNull.clear()
        indexSearchEmptyAdapter?.clear()
        baseTitleAdapter?.clear()
        searchDiscoverAdapter?.clear()
        this.key = searchStr
        pageNum = 1
        getData()
    }

    public fun setSearchRecordsList(searchStr: MutableList<SearchRecordsModel>) {
        this.mSearchDiscover.addAll(searchStr!!)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        getData()
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

    private fun buildRecyclerHelper() {

        val virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recyclerview?.layoutManager = virtualLayoutManager
        recyclerview?.adapter = delegateAdapter

        indexSearchEmptyAdapter = IndexSearchEmptyAdapter()
        delegateAdapter.addAdapter(indexSearchEmptyAdapter)

        baseTitleAdapter = BaseTitleAdapter()
        delegateAdapter.addAdapter(baseTitleAdapter)

        searchDiscoverAdapter = IndexSearchDiscoverAdapter()
        searchDiscoverAdapter?.setOutClickListener(this)
        delegateAdapter.addAdapter(searchDiscoverAdapter)

        indexSearchAllHmmVideoAdapter = IndexSearchAllHmmVideoAdapter()
        indexSearchAllHmmVideoAdapter?.setOutClickListener(this)
        delegateAdapter.addAdapter(indexSearchAllHmmVideoAdapter)

        indexSearchAllVideoAdapter = IndexSearchAllVideoAdapter()
        indexSearchAllVideoAdapter?.setOutClickListener(this)
        delegateAdapter.addAdapter(indexSearchAllVideoAdapter)

        indexSearchAllPostListAdapter = IndexSearchAllPostListAdapter()
        indexSearchAllPostListAdapter?.setOutClickListener(this)
        indexSearchAllPostListAdapter?.setOnPostFansClickListener(this)
        indexSearchAllPostListAdapter?.setOnPostLikeClickListener(this)
        indexSearchAllPostListAdapter?.setOnShareClickListener(this)
        delegateAdapter.addAdapter(indexSearchAllPostListAdapter)

        indexSearchAllQuestionAdapter = IndexSearchAllQuestionAdapter()
        indexSearchAllQuestionAdapter?.setOutClickListener(this)
        delegateAdapter.addAdapter(indexSearchAllQuestionAdapter)

        indexSearchAllArticleListAdapter = IndexSearchAllArticleListAdapter()
        indexSearchAllArticleListAdapter?.setOutClickListener(this)
        delegateAdapter.addAdapter(indexSearchAllArticleListAdapter)

        indexSearchAllGoodsAdapter = IndexSearchAllGoodsAdapter()
        indexSearchAllGoodsAdapter?.setOutClickListener(this)
        delegateAdapter.addAdapter(indexSearchAllGoodsAdapter)

        indexSearchAllShopListAdapter = IndexSearchAllShopListAdapter()
        indexSearchAllShopListAdapter?.setOutClickListener(this)
        delegateAdapter.addAdapter(indexSearchAllShopListAdapter)
    }

    override fun outClick(function: String?, obj: Any?) {
        when (function) {
            "matchClick" -> {
                EventBus.getDefault().post(IndexSearchInfo(obj as String, 1))
            }
            "跳转搜索课堂" -> {
                EventBus.getDefault().post(IndexSearchInfo("跳转搜索课堂", 2))
            }
            "跳转搜索短视频" -> {
                EventBus.getDefault().post(IndexSearchInfo("跳转搜索短视频", 2))
            }
            "跳转搜索帖子" -> {
                EventBus.getDefault().post(IndexSearchInfo("跳转搜索帖子", 2))
            }
            "跳转搜索问答" -> {
                EventBus.getDefault().post(IndexSearchInfo("跳转搜索问答", 2))
            }
            "跳转搜索百科" -> {
                EventBus.getDefault().post(IndexSearchInfo("跳转搜索百科", 2))
            }
            "跳转搜索商品" -> {
                EventBus.getDefault().post(IndexSearchInfo("跳转搜索商品", 2))
            }
            "跳转搜索服务门店" -> {
                EventBus.getDefault().post(IndexSearchInfo("跳转搜索服务门店", 2))
            }
            "like" -> {
                var arr = obj as Array<String>
                if (arr[1].toInt() == 1) {
                    indexSearchPresenter?.addPraise(
                        SimpleHashMapBuilder<String, Any>().puts("videoId", arr[0])!!
                            .puts("function", "8097")!!
                            .puts(
                                "memberId",
                                String(
                                    Base64.decode(
                                        SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                                        Base64.DEFAULT
                                    )
                                )
                            )!! as MutableMap<String, Any>,
                        arr[1].toInt()
                    )
                } else {
                    indexSearchPresenter?.addPraise(
                        SimpleHashMapBuilder<String, Any>().puts("videoId", arr[0])!!
                            .puts("function", "8098")!!
                            .puts(
                                "memberId",
                                String(
                                    Base64.decode(
                                        SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                                        Base64.DEFAULT
                                    )
                                )
                            )!! as MutableMap<String, Any>,
                        arr[1].toInt()
                    )
                }
            }
            "submit" -> showReviewWarnDialog(obj.toString())
            "sharePk" -> {
                mDataStatisticsPresenter?.shareAndLook(
                    SimpleHashMapBuilder<String, Any>().puts("sourceId", obj.toString())!!
                        .puts("sourceType", 2)!!.puts("type", 2) as MutableMap<String, Any>?
                )
            }
            "coupon" -> {
                if (couponDialog == null) {
                    couponDialog = PostCouponDialog.newInstance()
                }
                couponDialog?.let {
                    it.setId(obj.toString())
                    it.show(childFragmentManager, "")
                    it.setOnConfirmClick {
                        pageNum = 1
                        getData()
                    }
                }
            }
        }
    }

    override fun postfansclick(view: View, friendId: String, type: String, frtype: String) {
        mMap.clear()
        mMap["friendId"] = friendId
        mMap["friendType"] = frtype
        mMap["type"] = type
        indexSearchPresenter?.fan(mMap)
    }

    override fun postlikeclick(view: View, postingId: String, type: String) {
        mMap.clear()
        mMap["postingId"] = postingId
        mMap["type"] = type
        indexSearchPresenter?.like(mMap)
    }

    override fun postshareclick(
        view: View,
        url: String,
        des: String,
        title: String,
        postId: String
    ) {
        this.surl = url
        this.sdes = des
        this.stitle = title

        showShare()
        mDataStatisticsPresenter?.shareAndLook(
            SimpleHashMapBuilder<String, Any>().puts("sourceId", postId)!!
                .puts("sourceType", 2)!!.puts("type", 2) as MutableMap<String, Any>?
        )
    }

    /**
     * 举报弹窗
     */
    fun showReviewWarnDialog(warnid: String) {
        val strings: MutableList<String> = ArrayList()
        val stringsColors: MutableList<Int> = ArrayList()
        stringsColors.add(Color.parseColor("#444444"))
        strings.add("举报")
        StyledDialog.init(mContext)
        reviewandwarndialog = StyledDialog.buildBottomItemDialog(
            strings,
            stringsColors,
            "取消",
            object : MyItemDialogListener() {
                override fun onItemClick(text: CharSequence, position: Int) {
                    if ("举报" == text.toString()) {
                        showWarnDialog(warnid)
                    }
                }

                override fun onBottomBtnClick() {}
            }).setCancelable(true, true).show()
        reviewandwarndialog?.setOnDismissListener { reviewandwarndialog = null }
    }

    /**
     * 回复举报
     */
    fun showWarnDialog(warnid: String) {
        val stringsColors: MutableList<Int> = ArrayList()
        val strings: MutableList<String> = ArrayList()
        strings.add("垃圾广告")
        stringsColors.add(Color.parseColor("#29BDA9"))
        strings.add("淫秽色情")
        stringsColors.add(Color.parseColor("#29BDA9"))
        strings.add("诈骗信息")
        stringsColors.add(Color.parseColor("#29BDA9"))
        strings.add("不实违法")
        stringsColors.add(Color.parseColor("#29BDA9"))
        strings.add("违规侵权")
        stringsColors.add(Color.parseColor("#29BDA9"))
        strings.add("其他")
        stringsColors.add(Color.parseColor("#29BDA9"))
        StyledDialog.init(mContext)
        warndialog = StyledDialog.buildBottomItemDialog(
            strings,
            stringsColors,
            "取消",
            object : MyItemDialogListener() {
                override fun onItemClick(text: CharSequence, position: Int) {
                    mMap.clear()
                    mMap["type"] = "1"
                    mMap["sourceId"] = warnid
                    mMap["reason"] = text.toString()
                    indexSearchPresenter?.warn(mMap)
                }

                override fun onBottomBtnClick() {}
            }).setTitle("举报内容问题").setTitleColor(com.healthy.library.R.color.color_444444)
            .setTitleSize(15)
            .setCancelable(true, true).show()
        warndialog?.setOnDismissListener { warndialog = null }
    }
}