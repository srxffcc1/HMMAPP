package com.health.index.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.index.R
import com.health.index.adapter.*
import com.health.index.contract.IndexTabMainContract
import com.healthy.library.model.NewsInfo
import com.health.index.presenter.IndexTabMainPresenter
import com.healthy.library.adapter.BaseTitleAdapter
import com.healthy.library.adapter.ItemDecorationAdapter
import com.healthy.library.adapter.PostAdapter
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseFragment
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.contract.DataStatisticsContract
import com.healthy.library.dialog.PostCouponDialog
import com.healthy.library.interfaces.IsNeedShare
import com.healthy.library.model.*
import com.healthy.library.presenter.DataStatisticsPresenter
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.StatusLayout
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.hss01248.dialog.interfaces.MyItemDialogListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient
import com.tencent.smtt.sdk.WebChromeClient
import java.text.SimpleDateFormat
import java.util.*


/**
 * author : long
 * Time   :2021/12/14
 * desc   : ?????? tab????????????????????????
 */
class IndexTabFragment : BaseFragment(),
    IndexTabMainContract.View,
    PostAdapter.OnPostLikeClickListener,
    PostAdapter.OnShareClickListener,
    PostAdapter.OnPostFansClickListener,
    BaseAdapter.OnOutClickListener, IsNeedShare,
    DataStatisticsContract.View,
    IndexTabKnowledgeAdapter.OnLikeClickListener {

    private var mType = "0"

    private var mTarget = ""

    private var mRecyclerView: RecyclerView? = null
    private var mIndexTabPresenter: IndexTabMainPresenter? = null
    private var mDataStatisticsPresenter: DataStatisticsPresenter? = null

    private var virtualLayoutManager: VirtualLayoutManager? = null
    private var recycledViewPool: RecyclerView.RecycledViewPool? = null
    private var layoutRefresh: SmartRefreshLayout? = null
    private var layoutStatus: StatusLayout? = null

    private var indexTabEmptyAdapter: IndexTabEmptyAdapter? = null

    /*** ??????????????????????????????  */
    private var indexFeaturedCoursesAdapter: IndexFeaturedCoursesAdapter? = null
    private var indexMusicAdapter: IndexMusicAdapter? = null
    private var indexHmmTipAdapter: IndexHmmTipAdapter? = null
    private var indexPostAdapter: PostAdapter? = null

    /*** ??????????????????????????????????????????  */
    private var indexTabFoundAdapter: IndexSearchVideoAdapter? = null

    /*** ??????????????????????????????  */
    private var indexOnlineAdapter: IndexOnlineConsultationAdapter? = null

    //    private var indexQuestionRecommendTitleAdapter: BaseTitleAdapter? = null
//    private var indexFamousDoctorAdapter: IndexFamousDoctorAdapter? = null
//    private var indexFamousDoctorsListAdapter: IndexFamousDoctorsListAdapter? = null
    private var indexQuestionListTitleAdapter: BaseTitleAdapter? = null
    private var indexTabQuestionAdapter: IndexTabQuestionAdapter? = null
    private var indexItemDecorationAdapter: ItemDecorationAdapter? = null
    private var mRecommendTitleModel: TitleModel = TitleModel()
    private var mQuestionListTitleModel: TitleModel = TitleModel()

    /*** ??????????????????????????????  */
    private var indexTabKnowledgeAdapter: IndexTabKnowledgeAdapter? = null

    /*** ??????????????????????????????  */
    private var indexTabGoodsAdapter: IndexTabGoodsAdapter? = null

    /**
     * H5???????????????
     */
    private var indexTabH5Adapter: IndexTabH5Adapter? = null
    private var surl: String = ""
    private var sdes: String = ""
    private var stitle: String = ""

    private var couponDialog: PostCouponDialog? = null
    private var reviewandwarndialog: Dialog? = null
    private var warndialog: Dialog? = null
    var mIsNoMoreData = false;
    private var mfirstPageSize = 0
    private var mCategoryCode = "0"
    private var mCurrentPage = 1
    private var mPageSize = "10"
    private var mParams = mutableMapOf<String, Any>()
    private var videoPosition: Int = -1
    protected val COVER_SCREEN_PARAMS = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    private var customView: View? = null
    private var fullscreenContainer: FrameLayout? = null
    private var customViewCallback: IX5WebChromeClient.CustomViewCallback? = null

    companion object {
        fun newInstance(type: String, target: String?): IndexTabFragment {
            val args = Bundle()
            args.putString("type", type)
            args.putString("target", target)
            val fragment = IndexTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun setRecyclerHelper(
        layoutStatus: StatusLayout,
        recycledViewPool: RecyclerView.RecycledViewPool,
        layoutRefresh: SmartRefreshLayout
    ) {
        this.recycledViewPool = recycledViewPool
        this.layoutRefresh = layoutRefresh
        this.layoutStatus = layoutStatus
    }

    fun setCategoryCode(categoryCode: String) {
        this.mCategoryCode = categoryCode
        this.mCurrentPage = 1
        getData()
    }

    override fun getLayoutId(): Int {
        return R.layout.index_tab_fragment
    }

    override fun findViews() {
        mRecyclerView = mContentView.findViewById(R.id.recyclerView)
        mIndexTabPresenter = IndexTabMainPresenter(mContext, this)
        mDataStatisticsPresenter = DataStatisticsPresenter(mContext, this)
        mType = arguments?.getString("type")!!
        try {
            mTarget = arguments?.getString("target")!!
        } catch (e: Exception) {
        }
        buildRecyclerHelper()
    }

    private fun buildRecyclerHelper() {
        virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)
        mRecyclerView?.apply {
            layoutManager = virtualLayoutManager
            adapter = delegateAdapter
            isNestedScrollingEnabled = false
            this.setRecycledViewPool(recycledViewPool)
        }

        indexTabEmptyAdapter = IndexTabEmptyAdapter()
        delegateAdapter.addAdapter(indexTabEmptyAdapter)

        when (mType) {
            "??????" -> {
                indexFeaturedCoursesAdapter = IndexFeaturedCoursesAdapter()
                indexMusicAdapter = IndexMusicAdapter()
                indexHmmTipAdapter = IndexHmmTipAdapter()
                indexPostAdapter = PostAdapter()
                indexPostAdapter?.setOnShareClickListener(this)
                indexPostAdapter?.setOnPostFansClickListener(this)
                indexPostAdapter?.setOnPostLikeClickListener(this)
                indexPostAdapter?.moutClickListener = this
                delegateAdapter.addAdapter(indexFeaturedCoursesAdapter)
                delegateAdapter.addAdapter(indexMusicAdapter)
                delegateAdapter.addAdapter(indexHmmTipAdapter)
                delegateAdapter.addAdapter(indexPostAdapter)
            }
            "??????" -> {
                indexTabFoundAdapter = IndexSearchVideoAdapter()
                indexTabFoundAdapter?.setOutClickListener(this)
                delegateAdapter.addAdapter(indexTabFoundAdapter)
            }
            "??????" -> {
                indexOnlineAdapter = IndexOnlineConsultationAdapter()
//                indexQuestionRecommendTitleAdapter =
//                    BaseTitleAdapter(
//                        TransformUtil.newDp2px(mContext, 10f),
//                        TransformUtil.newDp2px(mContext, 12f)
//                    )
                mRecommendTitleModel.apply {
                    title = "????????????"
                    rightTitle = ""
                    topLeftRadius = TransformUtil.newDp2px(mContext, 6f)
                    topRightRadius = TransformUtil.newDp2px(mContext, 6f)
                    isDrawableRightShow = true
                }
                /***?????????????????????*/
//                indexFamousDoctorAdapter = IndexFamousDoctorAdapter()
//                indexFamousDoctorsListAdapter = IndexFamousDoctorsListAdapter()
                indexQuestionListTitleAdapter =
                    BaseTitleAdapter(
                        TransformUtil.newDp2px(mContext, 10f),
                        TransformUtil.newDp2px(mContext, 12f)
                    )
                indexQuestionListTitleAdapter?.setOutClickListener(this)
                mQuestionListTitleModel.apply {
                    title = "????????????"
                    rightTitle = "????????????"
                    topLeftRadius = TransformUtil.newDp2px(mContext, 6f)
                    topRightRadius = TransformUtil.newDp2px(mContext, 6f)
                    isDrawableRightShow = true
                }
                indexTabQuestionAdapter = IndexTabQuestionAdapter()
                indexItemDecorationAdapter =
                    ItemDecorationAdapter(TransformUtil.newDp2px(mContext, 10f))
                indexItemDecorationAdapter?.apply {
                    bottomLeftRadius = TransformUtil.newDp2px(mContext, 6f)
                    bottomRightRadius = TransformUtil.newDp2px(mContext, 6f)
                    itemHeight = TransformUtil.newDp2px(mContext, 12f)
                }

                delegateAdapter.addAdapter(indexOnlineAdapter)
//                delegateAdapter.addAdapter(indexQuestionRecommendTitleAdapter)
//                delegateAdapter.addAdapter(indexFamousDoctorAdapter)
//                delegateAdapter.addAdapter(indexFamousDoctorsListAdapter)
                delegateAdapter.addAdapter(indexQuestionListTitleAdapter)
                delegateAdapter.addAdapter(indexTabQuestionAdapter)
                delegateAdapter.addAdapter(indexItemDecorationAdapter)
            }
            "??????" -> {
                indexTabKnowledgeAdapter = IndexTabKnowledgeAdapter()
                indexTabKnowledgeAdapter?.setOnLikeClickListener(this)
                delegateAdapter.addAdapter(indexTabKnowledgeAdapter)
            }
            "??????" -> {
                indexTabGoodsAdapter = IndexTabGoodsAdapter()
                delegateAdapter.addAdapter(indexTabGoodsAdapter)
            }
            else -> {//H5??????
                indexTabH5Adapter = IndexTabH5Adapter()
                indexTabH5Adapter?.setmWebChromeClient(mWebChromeClient)
                indexTabH5Adapter?.model = mTarget
                delegateAdapter.addAdapter(indexTabH5Adapter)
                isMore(false)
            }
        }

    }

    private val mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        override fun getVideoLoadingProgressView(): View {
            val frameLayout = FrameLayout(activity)
            frameLayout.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            return frameLayout
        }

        override fun onShowCustomView(view: View, callback: IX5WebChromeClient.CustomViewCallback) {
            showCustomView(view, callback)
        }

        override fun onHideCustomView() {
            hideCustomView()
        }
    }

    internal class FullscreenHolder(ctx: Context) : FrameLayout(ctx) {
        override fun onTouchEvent(evt: MotionEvent): Boolean {
            return true
        }

        init {
            setBackgroundColor(ctx.resources.getColor(android.R.color.black))
        }
    }

    private fun setStatusBarVisibility(visible: Boolean) {
        val flag = if (visible) 0 else WindowManager.LayoutParams.FLAG_FULLSCREEN
        activity?.getWindow()?.setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun showCustomView(view: View, callback: IX5WebChromeClient.CustomViewCallback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden()
            return
        }
        activity?.getWindow()?.getDecorView()
        val decor = activity?.getWindow()?.getDecorView() as FrameLayout
        fullscreenContainer = getActivity()?.let { FullscreenHolder(it) }
        fullscreenContainer?.addView(view, COVER_SCREEN_PARAMS)
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS)
        customView = view
        setStatusBarVisibility(false)
        customViewCallback = callback
    }

    private fun hideCustomView() {
        if (customView == null) {
            return
        }
        setStatusBarVisibility(true)
        val decor = activity?.getWindow()?.getDecorView() as FrameLayout
        decor.removeView(fullscreenContainer)
        fullscreenContainer = null
        customView = null
        customViewCallback?.onCustomViewHidden()
        indexTabH5Adapter?.tipContentWeb?.setVisibility(View.VISIBLE)
    }

    override fun outClick(function: String?, obj: Any?) {

        if (function == "banner") {
            mParams.clear()
            mParams["advertisingId"] = obj.toString()
            mDataStatisticsPresenter?.bannerClickNum(mParams)
        }

        if (function == "coupon") {
            if (couponDialog == null) {
                couponDialog = PostCouponDialog.newInstance()
            }
            couponDialog?.setId(obj.toString())
            couponDialog?.show(childFragmentManager, "")
            couponDialog?.setOnConfirmClick {
                mCurrentPage = 1
                getData()
            }
        }

        if (function == "submit") {
            showReviewWarnDialog(obj.toString())
        }
        if (function == "sharePk") {
            mParams.clear()
            mParams["sourceId"] = obj.toString()
            mParams["sourceType"] = 2
            mParams["type"] = 2
            mDataStatisticsPresenter?.shareAndLook(mParams)
        }
        if (function == "like") {
            var arr = obj as Array<String>
            videoPosition = arr[2].toInt()
            if (arr[1].toInt() == 1) {
                mIndexTabPresenter?.addPraise(
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
                mIndexTabPresenter?.addPraise(
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
        if (function.equals("????????????")) {
            ARouter.getInstance().build(FaqRoutes.FAQ_QUESTION_ANSWERS)
                .navigation()
        }
    }

    fun onRefresh() {
        mCurrentPage = 1
        getData()
    }

    override fun onRequestFinish() {
        super.onRequestFinish()
        showContent()
        layoutRefresh?.finishRefresh()
        layoutRefresh?.finishLoadMore()
    }

    fun onLoadMore() {
        mCurrentPage++
        getData()
    }

    override fun onLazyData() {
        super.onLazyData()
        getData()
    }

    //    mTitles.add("??????")
//    mTitles.add("??????")
//    mTitles.add("??????")
//    mTitles.add("??????")
//    mTitles.add("??????")
    override fun getData() {
        mParams.clear()
        when (mType) {
            "??????" -> {
                if (mCurrentPage == 1 || mCurrentPage == 0) {
                    mIndexTabPresenter?.getAPPIndexCustomRecommand(mParams)
                }
//                indexFeaturedCoursesAdapter?.model = ""
//                indexMusicAdapter?.model = ""
//                indexHmmTipAdapter?.model = ""

                //????????????
                mParams["addressCity"] = LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE)
                mParams["addressProvince"] = LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE)
                mParams["addressArea"] = LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)
                mParams["currentPage"] = mCurrentPage.toString()
                mParams["pageSize"] = mPageSize
                mParams["type"] = "1"
                mParams["type2"] = "1"
                mIndexTabPresenter?.getPostingList(mParams)
            }
            "??????" -> {
                mParams["memberId"] = String(
                    Base64.decode(
                        SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                        Base64.DEFAULT
                    )
                )
                mParams["categoryCode"] = mCategoryCode
                mParams["pageNum"] = mCurrentPage.toString()
                mParams["pageSize"] = mPageSize
                mIndexTabPresenter?.getVideoList(mParams)

            }
            "??????" -> {
                indexOnlineAdapter?.model = ""
//                indexQuestionRecommendTitleAdapter?.model = mRecommendTitleModel
//                val list = mutableListOf<String>()
//                list.clear()
//                list.add("")
//                list.add("")
//                list.add("")
//                list.add("")
//                indexFamousDoctorAdapter?.setData(list as ArrayList<String>)
//                indexFamousDoctorsListAdapter?.model = ""

                mParams["pageNum"] = mCurrentPage.toString()
                mParams["pageSize"] = mPageSize
                mIndexTabPresenter?.getQuestionList(mParams)
            }
            "??????" -> {
                mParams["addressCity"] = LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE)
                mParams["addressProvince"] = LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE)
                mParams["addressArea"] = LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)
                mParams["currentPage"] = mCurrentPage.toString()
                mParams["pageSize"] = mPageSize
                mParams["queryDate"] = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                mIndexTabPresenter?.getRecommendNews(mParams)
            }
            "??????" -> {
                mParams["firstPageSize"] = mfirstPageSize
                mParams["type"] = "8"
                mParams["differentSymbol"] = "1"
                mParams["pageNum"] = mCurrentPage.toString()
                mParams["pageSize"] = mPageSize
                mParams["shopId"] = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)
                mIndexTabPresenter?.getRecommendGoods(mParams)
            }
        }
    }

    fun isMore(isMore: Boolean) {
        if (isMore) {
            mIsNoMoreData = false
            layoutRefresh?.resetNoMoreData()
            layoutRefresh?.finishLoadMore()
        } else {
            mIsNoMoreData = true
            layoutRefresh?.finishLoadMoreWithNoMoreData()
        }
    }

    override fun onSuccessGetPostList(posts: MutableList<PostDetail>?, isMore: Boolean) {
        isMore(isMore)

        if (mCurrentPage == 1) {
            indexPostAdapter?.setData(posts as ArrayList<PostDetail>?)
        } else {
            indexPostAdapter?.addDatas(posts as ArrayList<PostDetail>?)
        }

        if (ListUtil.isEmpty(indexPostAdapter?.datas)) {
            indexTabEmptyAdapter?.model = ""
            return
        }

        indexTabEmptyAdapter?.model = null

        posts?.let {
            for (i in it.indices) {
                if (it[i].postingType == 5 || it[i].postingType == 6) {
                    val postDetail: PostDetail = it[i]
                    if (postDetail.postActivityList == null) {
                        mParams.clear()
                        mParams["postingId"] = postDetail.id
                        mIndexTabPresenter?.getActivityList(mParams, postDetail)
                    }
                }
            }
        }

    }

    override fun onSuccessGetActivityList() {

    }

    override fun onSuccessFan(result: Any?) {
        if ("0" == result) {
            showToast("????????????")
        }
    }

    /**
     * ????????????????????????
     */
    override fun onGetVideoListSuccess(result: MutableList<VideoListModel>?, isMore: Boolean) {
        isMore(isMore)

        if (mCurrentPage == 1) {
            indexTabFoundAdapter?.setData(result as ArrayList<VideoListModel>?)
        } else {
            indexTabFoundAdapter?.addDatas(result as ArrayList<VideoListModel>?)
        }

        if (ListUtil.isEmpty(indexTabFoundAdapter?.datas)) {
            indexTabEmptyAdapter?.model = ""
            return
        }
        indexTabEmptyAdapter?.model = null
    }

    override fun onGetAPPIndexCustomRecommandSuccess(result: AppIndexCustomRecommandAll) {
        indexFeaturedCoursesAdapter?.model = result
        if (null != result.xmly) {
            indexMusicAdapter?.model = result
        }
        indexHmmTipAdapter?.model = result
    }

    override fun onAddPraiseSuccess(result: String, type: Int) {
        if (type == 1) {
            showToast("????????????")
        } else {
            showToast("???????????????")
        }
        if (indexTabFoundAdapter != null && videoPosition > -1 && result != null) {
            val model: VideoListModel = indexTabFoundAdapter?.datas!!.get(videoPosition)
            model.praise = type == 1
            if (type == 1) {
                model.praiseCount = model.praiseCount + 1
            } else {
                model.praiseCount = model.praiseCount - 1
            }
            indexTabFoundAdapter?.notifyItemChanged(videoPosition, "like")
            videoPosition = -1
        }
    }

    /**
     * ?????? -> ??????????????????
     */
    override fun onSuccessGetQuestionList(
        result: MutableList<FaqExportQuestion>?,
        isMore: Boolean
    ) {
        isMore(isMore)

        if (mCurrentPage == 1) {
            indexTabQuestionAdapter?.setData(result as ArrayList<FaqExportQuestion?>)
        } else {
            indexTabQuestionAdapter?.addDatas(result as ArrayList<FaqExportQuestion?>)
        }

        if (ListUtil.isEmpty(indexTabQuestionAdapter?.datas)) {
            indexTabEmptyAdapter?.model = ""
            indexItemDecorationAdapter?.model = null
            return
        }
        indexQuestionListTitleAdapter?.model = mQuestionListTitleModel
        indexItemDecorationAdapter?.model = ""
        indexTabEmptyAdapter?.model = null
    }

    /**
     * ?????? ????????????
     * @param indexAllSees
     * @param isMore
     */
    override fun onSuccessGetNewsList(indexAllSees: MutableList<NewsInfo>?, isMore: Boolean) {

        isMore(isMore)

        if (mCurrentPage == 1) {
            indexTabKnowledgeAdapter?.setData(indexAllSees as ArrayList<NewsInfo>?)
        } else {
            indexTabKnowledgeAdapter?.addDatas(indexAllSees as ArrayList<NewsInfo>?)
        }

        if (ListUtil.isEmpty(indexTabKnowledgeAdapter?.datas)) {
            indexTabEmptyAdapter?.model = ""
            return
        }
        indexTabEmptyAdapter?.model = null
    }

    /**
     * ??????????????????
     *
     * @param result
     * @param firstPageSize
     */
    override fun onSuccessGetGoodsRecommendList(
        result: MutableList<ActGoodsItem>?,
        firstPageSize: Int
    ) {
        mfirstPageSize = firstPageSize;
        if (ListUtil.isEmpty(result)) {
            mIsNoMoreData = true
            layoutRefresh?.finishLoadMoreWithNoMoreData()
        } else {
            mIsNoMoreData = false
            layoutRefresh?.resetNoMoreData()
            layoutRefresh?.finishLoadMore()
        }

        if (mCurrentPage == 1) {
            indexTabGoodsAdapter?.setData(result as ArrayList<ActGoodsItem>?)
        } else {
            indexTabGoodsAdapter?.addDatas(result as ArrayList<ActGoodsItem>?)
        }

        if (ListUtil.isEmpty(indexTabGoodsAdapter?.datas)) {
            indexTabEmptyAdapter?.model = ""
            return
        }
        indexTabEmptyAdapter?.model = null
    }

    override fun showLoading() {
        super.showLoading()
        layoutStatus?.updateStatus(StatusLayout.Status.STATUS_LOADING)
    }

    override fun showEmpty() {
        super.showEmpty()
        layoutStatus?.updateStatus(StatusLayout.Status.STATUS_CONTENT)
    }

    override fun showDataErr() {
        super.showDataErr()
        layoutStatus?.updateStatus(StatusLayout.Status.STATUS_CONTENT)
    }

    override fun showNetErr() {
        super.showNetErr()
        layoutStatus?.updateStatus(StatusLayout.Status.STATUS_CONTENT)
    }

    override fun showContent() {
        super.showContent()
        layoutStatus?.updateStatus(StatusLayout.Status.STATUS_CONTENT)
    }


    override fun postlikeclick(view: View?, postingId: String?, type: String?) {
        mParams.clear()
        mParams["postingId"] = postingId!!
        mParams["type"] = type!!
        mIndexTabPresenter?.like(mParams)
    }

    override fun postshareclick(
        view: View?,
        url: String?,
        des: String?,
        title: String?,
        postId: String?
    ) {
        this.surl = url!!
        this.sdes = des!!
        this.stitle = title!!
        showShare()
        mParams.clear()
        mParams["sourceId"] = postId!!
        mParams["sourceType"] = 2
        mParams["type"] = 2
        mDataStatisticsPresenter?.shareAndLook(mParams)
    }

    fun showReviewWarnDialog(warnid: String?) {
        val strings: MutableList<String> = ArrayList()
        val stringsColors: MutableList<Int> = ArrayList()
        stringsColors.add(Color.parseColor("#444444"))
        strings.add("??????")
        StyledDialog.init(context)
        reviewandwarndialog = StyledDialog.buildBottomItemDialog(
            strings,
            stringsColors,
            "??????",
            object : MyItemDialogListener() {
                override fun onItemClick(text: CharSequence, position: Int) {
                    if ("??????" == text.toString()) {
                        showWarnDialog(warnid)
                    }
                }

                override fun onBottomBtnClick() {}
            }).setCancelable(true, true).show()
        reviewandwarndialog?.setOnDismissListener { reviewandwarndialog = null }
    }

    fun showWarnDialog(warnid: String?) {
        val stringsColors: MutableList<Int> = ArrayList()
        val strings: MutableList<String> = ArrayList()
        strings.add("????????????")
        stringsColors.add(Color.parseColor("#29BDA9"))
        strings.add("????????????")
        stringsColors.add(Color.parseColor("#29BDA9"))
        strings.add("????????????")
        stringsColors.add(Color.parseColor("#29BDA9"))
        strings.add("????????????")
        stringsColors.add(Color.parseColor("#29BDA9"))
        strings.add("????????????")
        stringsColors.add(Color.parseColor("#29BDA9"))
        strings.add("??????")
        stringsColors.add(Color.parseColor("#29BDA9"))
        StyledDialog.init(context)
        warndialog = StyledDialog.buildBottomItemDialog(
            strings,
            stringsColors,
            "??????",
            object : MyItemDialogListener() {
                override fun onItemClick(text: CharSequence, position: Int) {
                    mParams.clear()
                    mParams["type"] = "1"
                    mParams["sourceId"] = warnid!!
                    mParams["reason"] = text.toString()
                    mIndexTabPresenter?.warn(mParams)
                }

                override fun onBottomBtnClick() {}
            }).setTitle("??????????????????").setTitleColor(R.color.color_444444).setTitleSize(15)
            .setCancelable(true, true).show()
        warndialog?.setOnDismissListener { warndialog = null }
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

    override fun postfansclick(view: View?, friendId: String, type: String, frtype: String) {
        mParams.clear()
        mParams["friendId"] = friendId
        mParams["friendType"] = frtype
        mParams["type"] = type
        mIndexTabPresenter?.fan(mParams)
    }

    override fun articleLike(id: String, function: String) {
        mParams.clear()
        mParams["function"] = function
        mParams["knowledgeId"] = id
        mIndexTabPresenter?.articleLike(mParams)
    }

}