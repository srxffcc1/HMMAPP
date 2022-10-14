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
import com.healthy.library.model.IndexAllQuestion
import com.health.index.model.IndexAllSee
import com.health.index.presenter.IndexSearchPresenter
import com.healthy.library.adapter.*
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
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_search_child.*
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList


class SearchChildFragment : BaseFragment(),
    IndexSearchContract.View,
    BaseAdapter.OnOutClickListener,
    PostAdapter.OnPostFansClickListener,
    PostAdapter.OnPostLikeClickListener,
    PostAdapter.OnShareClickListener,
    DataStatisticsContract.View,
    OnRefreshLoadMoreListener,
    IsNeedShare {

    private var surl: String = ""
    private var sdes: String = ""
    private var stitle: String = ""

    private var videoPosition: Int = -1
    private var key: String? = null
    private var type: String? = null

    private var couponDialog: PostCouponDialog? = null
    private var reviewandwarndialog: Dialog? = null
    private var warndialog: Dialog? = null
    private var mMap: MutableMap<String, Any> = mutableMapOf()
    private var pageNum = 1

    private var indexSearchPresenter: IndexSearchPresenter? = null
    private var mDataStatisticsPresenter: DataStatisticsPresenter? = null
    private var mTitleModel: TitleModel? = null

    private var mSearchDiscover = mutableListOf<SearchRecordsModel>()

    private var mPostAdapter: PostAdapter? = null//帖子
    private var indexSearchHanMomVideoAdapter: IndexSearchHanMomVideoAdapter? = null//憨妈课堂视频
    private var indexSearchVideoAdapter: IndexSearchVideoAdapter? = null//短视频视频
    private var indexSearchGoodsAdapter: IndexSearchGoodsAdapter? = null//商品
    private var indexSearchArticleListAdapter: IndexSearchArticleListAdapter? = null//百科
    private var indexSearchShopListAdapter: IndexSearchShopListAdapter? = null//门店
    private var indexSearchQuestionAdapter: IndexSearchQuestionAdapter? = null//问答
    private var indexSearchEmptyAdapter: IndexSearchEmptyAdapter? = null//空
    private var baseTitleAdapter: BaseTitleAdapter? = null//搜索发现标题
    private var searchDiscoverAdapter: IndexSearchDiscoverAdapter? = null//搜索发现

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString("key")
            type = it.getString("type")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_child
    }

    override fun findViews() {
        indexSearchPresenter = IndexSearchPresenter(mContext, this)
        mDataStatisticsPresenter = DataStatisticsPresenter(mContext, this)
        buildRecyclerHelper()
        getData()

    }


    private fun buildRecyclerHelper() {
        layout_refresh.setOnRefreshLoadMoreListener(this)

        val virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recyclerview?.layoutManager = virtualLayoutManager
        recyclerview?.adapter = delegateAdapter

        mPostAdapter = PostAdapter()
        mPostAdapter?.moutClickListener = this
        mPostAdapter?.setOnPostFansClickListener(this)
        mPostAdapter?.setOnPostLikeClickListener(this)
        mPostAdapter?.setOnShareClickListener(this)
        delegateAdapter.addAdapter(mPostAdapter)

        indexSearchHanMomVideoAdapter = IndexSearchHanMomVideoAdapter()
        indexSearchHanMomVideoAdapter?.setOutClickListener(this)
        delegateAdapter.addAdapter(indexSearchHanMomVideoAdapter)

        indexSearchVideoAdapter = IndexSearchVideoAdapter()
        indexSearchVideoAdapter?.setOutClickListener(this)
        delegateAdapter.addAdapter(indexSearchVideoAdapter)

        indexSearchGoodsAdapter = IndexSearchGoodsAdapter()
        delegateAdapter.addAdapter(indexSearchGoodsAdapter)

        indexSearchArticleListAdapter = IndexSearchArticleListAdapter()
        delegateAdapter.addAdapter(indexSearchArticleListAdapter)

        indexSearchQuestionAdapter = IndexSearchQuestionAdapter()
        delegateAdapter.addAdapter(indexSearchQuestionAdapter)

        indexSearchShopListAdapter = IndexSearchShopListAdapter()
        delegateAdapter.addAdapter(indexSearchShopListAdapter)

        indexSearchEmptyAdapter = IndexSearchEmptyAdapter()
        delegateAdapter.addAdapter(indexSearchEmptyAdapter)

        baseTitleAdapter = BaseTitleAdapter()
        delegateAdapter.addAdapter(baseTitleAdapter)

        searchDiscoverAdapter = IndexSearchDiscoverAdapter()
        searchDiscoverAdapter?.setOutClickListener(this)
        delegateAdapter.addAdapter(searchDiscoverAdapter)
    }

    override fun getData() {
        super.getData()
        if (key.isNullOrEmpty().not()) {
            mMap.clear()
            when (type) {
                "1" -> {//搜憨妈课堂
                    mMap["pageNum"] = pageNum
                    mMap["searchContent"] = key!!
                    mMap["pageSize"] = "10"
                    mMap["scopeType"] = "1"
                    mMap["memberId"] = String(
                        Base64.decode(
                            SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                            Base64.DEFAULT
                        )
                    )
                    indexSearchPresenter?.queryHmmVideoList(mMap)
                }
                "2" -> {//搜短视频
                    mMap["pageNum"] = pageNum
                    mMap["searchContent"] = key!!
                    mMap["pageSize"] = "10"
                    mMap["scopeType"] = "0"
                    mMap["memberId"] = String(
                        Base64.decode(
                            SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                            Base64.DEFAULT
                        )
                    )
                    indexSearchPresenter?.queryVideoList(mMap)
                }
                "3" -> {//搜同城圈帖子
                    mMap["currentPage"] = pageNum
                    mMap["searchContent"] = key!!
                    mMap["pageSize"] = "10"
                    mMap["merchantId"] = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
                    mMap["shopId"] = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)
                    indexSearchPresenter?.queryPostList(mMap)
                }
                "4" -> {//搜互动问答
                    mMap["pageNum"] = pageNum
                    mMap["keyWords"] = key!!
                    mMap["pageSize"] = "10"
                    mMap["addressCityOrg"] = LocUtil.getCityNo(mContext, SpKey.LOC_ORG)
                    mMap["fragmentBottom"] = "1"
                    indexSearchPresenter?.queryQuestionList(mMap)
                }
                "5" -> {//搜百科文章
                    mMap["currentPage"] = pageNum
                    mMap["title"] = key!!
                    mMap["pageSize"] = "10"
                    mMap["knowOrInfoStatus"] = "1"
                    indexSearchPresenter?.queryArticleList(mMap)
                }
                "6" -> {//搜商品
                    mMap["pageNum"] = pageNum
                    mMap["goodsTitle"] = key!!
                    mMap["pageSize"] = "10"
                    mMap["publish"] = "1"
                    mMap["appSalesSort"] = "desc"
                    mMap["platformPriceSort"] = "asc"
                    mMap["shopId"] = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)
                    mMap["merchantId"] = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
                    indexSearchPresenter?.queryGoodsList(mMap)
                }
                "7" -> {//搜服务门店
                    mMap["shopTypeList"] = "1,3".split(",").toTypedArray()
                    mMap["shopName"] = key!!
                    mMap["longitude"] = LocUtil.getLongitude(mContext, SpKey.LOC_ORG)
                    mMap["latitude"] = LocUtil.getLatitude(mContext, SpKey.LOC_ORG)
                    mMap["shopId"] = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)
                    mMap["haveYY"] = true
                    indexSearchPresenter?.queryShopList(mMap)
                }
            }
        }
    }

    public fun refresh(searchStr: String) {
        this.key = searchStr
        pageNum = 1
        getData()
    }

    public fun setSearchRecordsList(searchStr: MutableList<SearchRecordsModel>) {
        this.mSearchDiscover.addAll(searchStr)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchChildFragment().apply {
                arguments = Bundle().apply {
                    putString("key", param1)
                    putString("type", param2)
                }
            }
    }

    override fun onQuerySearchRecordsSuccess(
        records: MutableList<SearchRecordsModel>?,
        isMore: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun onQueryPostListSuccess(records: MutableList<PostDetail>?, isMore: Boolean) {
        clearAdapter()
        if (isMore) {
            layout_refresh?.resetNoMoreData()
            layout_refresh?.finishLoadMore()
        } else {
            layout_refresh?.finishLoadMoreWithNoMoreData()
        }

        if (pageNum == 1) {
            mPostAdapter?.clear()
            mPostAdapter?.setData(records as ArrayList<PostDetail>)
        } else {
            mPostAdapter?.addDatas(records as ArrayList<PostDetail>)
        }

        if (ListUtil.isEmpty(mPostAdapter?.getDatas())) {
            showEmptyAdapter()
            layout_refresh?.finishLoadMoreWithNoMoreData()
            return
        }
        records?.let {
            for (i in it.indices) {
                if (it[i].postingType == 5 || it[i].postingType == 6) { //行内请求主要请求优惠券和视频关联商品 其他帖子不请求
                    val postDetail: PostDetail = it[i]
                    if (postDetail.postActivityList != null && postDetail.postActivityList.size > 0) {
                    } else {
                        mMap.clear()
                        mMap["postingId"] = postDetail.id
                        indexSearchPresenter?.getActivityList(mMap, postDetail)
                    }
                }
            }
        }
    }

    override fun onQueryHmmVideoListSuccess(
        records: MutableList<VideoListModel>?,
        isMore: Boolean
    ) {
        clearAdapter()
        if (isMore) {
            layout_refresh?.resetNoMoreData()
            layout_refresh?.finishLoadMore()
        } else {
            layout_refresh?.finishLoadMoreWithNoMoreData()
        }

        if (pageNum == 1) {
            indexSearchHanMomVideoAdapter?.clear()
            indexSearchHanMomVideoAdapter?.setData(records as ArrayList<VideoListModel>)
        } else {
            indexSearchHanMomVideoAdapter?.addDatas(records as ArrayList<VideoListModel>)
        }

        if (ListUtil.isEmpty(indexSearchHanMomVideoAdapter?.getDatas())) {
            showEmptyAdapter()
            layout_refresh?.finishLoadMoreWithNoMoreData()
            return
        }
    }

    override fun onQueryVideoListSuccess(records: MutableList<VideoListModel>?, isMore: Boolean) {
        clearAdapter()
        if (isMore) {
            layout_refresh?.resetNoMoreData()
            layout_refresh?.finishLoadMore()
        } else {
            layout_refresh?.finishLoadMoreWithNoMoreData()
        }
        if (pageNum == 1) {
            indexSearchVideoAdapter?.clear()
            indexSearchVideoAdapter?.setData(records as ArrayList<VideoListModel>)
        } else {
            indexSearchVideoAdapter?.addDatas(records as ArrayList<VideoListModel>)
        }
        if (ListUtil.isEmpty(indexSearchVideoAdapter?.getDatas())) {
            showEmptyAdapter()
            layout_refresh?.finishLoadMoreWithNoMoreData()
            return
        }
    }

    override fun onSuccessFan(result: Any?) {
        if ("0" == result) {
            showToast("关注成功")
        }
    }

    override fun onSuccessLike() {
    }

    override fun onSuccessGetActivityList() {
        mPostAdapter?.let { it.notifyDataSetChanged() }
    }

    override fun onGetBannerSuccess(adModels: MutableList<AdModel>?) {
    }

    override fun onQueryGoodsListSuccess(
        records: MutableList<SortGoodsListModel>?,
        isMore: Boolean
    ) {
        clearAdapter()
        if (isMore) {
            layout_refresh?.resetNoMoreData()
            layout_refresh?.finishLoadMore()
        } else {
            layout_refresh?.finishLoadMoreWithNoMoreData()
        }

        if (pageNum == 1) {
            indexSearchGoodsAdapter?.clear()
            indexSearchGoodsAdapter?.setData(records as ArrayList<SortGoodsListModel>)
        } else {
            indexSearchGoodsAdapter?.addDatas(records as ArrayList<SortGoodsListModel>)
        }
        if (ListUtil.isEmpty(indexSearchGoodsAdapter?.getDatas())) {
            showEmptyAdapter()
            layout_refresh?.finishLoadMoreWithNoMoreData()
            return
        }
    }

    override fun onAddPraiseSuccess(result: String, type: Int) {
        if (type == 1) {
            showToast("点赞成功")
        } else {
            showToast("取消赞成功")
        }
        if (indexSearchVideoAdapter != null && videoPosition > -1 && result != null) {
            val model: VideoListModel = indexSearchVideoAdapter?.datas!!.get(videoPosition)
            model.praise = type == 1
            if (type == 1) {
                model.praiseCount = model.praiseCount + 1
            } else {
                model.praiseCount = model.praiseCount - 1
            }
            indexSearchVideoAdapter?.notifyItemChanged(videoPosition, "like")
            videoPosition = -1
        }
    }

    override fun onSearchShopListSuccess(records: MutableList<ShopDetailModel>?) {
        clearAdapter()
        layout_refresh?.finishRefresh()
        layout_refresh?.finishLoadMore()
        indexSearchShopListAdapter?.clear()
        indexSearchShopListAdapter?.setData(records as ArrayList<ShopDetailModel>)

        if (ListUtil.isEmpty(indexSearchShopListAdapter?.datas)) {
            showEmptyAdapter()
            layout_refresh?.finishLoadMoreWithNoMoreData()
            return
        }
    }

    override fun onQueryArticleListSuccess(records: MutableList<IndexAllSee>?, isMore: Boolean) {
        clearAdapter()
        if (isMore) {
            layout_refresh?.resetNoMoreData()
            layout_refresh?.finishLoadMore()
        } else {
            layout_refresh?.finishLoadMoreWithNoMoreData()
        }
        if (pageNum == 1) {
            indexSearchArticleListAdapter?.clear()
            indexSearchArticleListAdapter?.setData(records as ArrayList<IndexAllSee>)
        } else {
            indexSearchArticleListAdapter?.addDatas(records as ArrayList<IndexAllSee>)
        }

        if (ListUtil.isEmpty(indexSearchArticleListAdapter?.datas)) {
            showEmptyAdapter()
            layout_refresh?.finishLoadMoreWithNoMoreData()
            return
        }
    }

    override fun onQueryQuestionListSuccess(
        records: MutableList<FaqExportQuestion>?,
        isMore: Boolean
    ) {
        clearAdapter()
        if (isMore) {
            layout_refresh?.resetNoMoreData()
            layout_refresh?.finishLoadMore()
        } else {
            layout_refresh?.finishLoadMoreWithNoMoreData()
        }

        if (pageNum == 1) {
            indexSearchQuestionAdapter?.clear()
            indexSearchQuestionAdapter?.setAdapterData(records as ArrayList<FaqExportQuestion>)
        } else {
            indexSearchQuestionAdapter?.addAdapterData(records as ArrayList<FaqExportQuestion>)
        }
        indexSearchQuestionAdapter?.model = "null"
        if (ListUtil.isEmpty(indexSearchQuestionAdapter?.getDatas())) {
            showEmptyAdapter()
            layout_refresh?.finishLoadMoreWithNoMoreData()
            return
        }
    }

    override fun onGetAllQuestionListSuccess(records: MutableList<IndexAllQuestion>?) {

    }

    override fun outClick(function: String, obj: Any) {
        when (function) {
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
            "like" -> {
                var arr = obj as Array<String>
                videoPosition = arr[2].toInt()
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
            "matchClick" -> {
                EventBus.getDefault().post(IndexSearchInfo(obj as String, 1))
            }
        }
    }

    private fun showEmptyAdapter() {
        indexSearchEmptyAdapter?.model = "null"
        if (mSearchDiscover!!.isNotEmpty()) {
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

    private fun clearAdapter() {
        indexSearchEmptyAdapter?.clear()
        baseTitleAdapter?.clear()
        searchDiscoverAdapter?.clear()
    }

    override fun postfansclick(view: View?, friendId: String, type: String, frtype: String) {
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
        view: View?,
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

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageNum = 1
        getData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageNum++
        getData()
    }

    override fun onRequestFinish() {
        super.onRequestFinish()
        layout_refresh?.finishRefresh()
        layout_refresh?.finishLoadMore()
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