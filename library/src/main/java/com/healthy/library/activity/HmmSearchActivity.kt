package com.healthy.library.activity

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.healthy.library.R
import com.healthy.library.adapter.*
import com.healthy.library.base.BaseActivity
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseMultiItemAdapter
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.contract.DataStatisticsContract
import com.healthy.library.contract.HmmSearchContract
import com.healthy.library.dialog.PostCouponDialog
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.interfaces.IsNeedShare
import com.healthy.library.model.PostDetail
import com.healthy.library.model.TitleModel
import com.healthy.library.model.VideoListModel
import com.healthy.library.presenter.DataStatisticsPresenter
import com.healthy.library.presenter.HmmSearchPresenter
import com.healthy.library.routes.CityRoutes
import com.healthy.library.routes.LibraryRoutes
import com.healthy.library.widget.StatusLayout
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.hss01248.dialog.interfaces.MyItemDialogListener
import com.hyb.library.KeyboardUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import java.util.*

@Route(path = LibraryRoutes.LIBRARY_HMM_SEARCH)
class HmmSearchActivity : BaseActivity(), IsFitsSystemWindows, IsNeedShare, HmmSearchContract.View,
    BaseAdapter.OnOutClickListener,
    PostAdapter.OnPostFansClickListener, PostAdapter.OnPostLikeClickListener,
    PostAdapter.OnShareClickListener, DataStatisticsContract.View {

    @Autowired
    @JvmField
    var searchType: Int? = 1 // ???????????? 1 ???????????? 2 ????????????

    private var mParamsMap: MutableMap<String, Any> = mutableMapOf()

    private var mEtSearch: EditText? = null
    private var mClean: TextView? = null
    private var mStatusLayout: StatusLayout? = null
    private var mRefreshLayout: SmartRefreshLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var mPassToSendPost: ImageView? = null
    private var mPassToTop: ImageView? = null
    private var mBeedS: ViewGroup? = null

    private var mSearchPresenter: HmmSearchPresenter? = null
    private var mTitleModel: TitleModel? = null
    private var mTitleAdapter: BaseTitleAdapter? = null
    private var mSearchDiscoverAdapter: SearchDiscoverAdapter? = null
    private var mSearchMatchAdapter: SearchMatchAdapter? = null
    private var mPostAdapter: PostAdapter? = null
    private var mVideoAdapter: HanMomVideoFindChildAdapter? = null
    private var couponDialog: PostCouponDialog? = null
    private var reviewandwarndialog: Dialog? = null
    private var warndialog: Dialog? = null

    private var surl: String = ""
    private var sdes: String = ""
    private var stitle: String = ""

    /*** ?????? 0???????????? 1?????????????????? 2???????????? */
    private var mRecyclerItemType: Int = 0
    private var isMatchClick = false
    private var mSearchName: String = ""
    private var mCurrentPage: Int = 1

    //?????????????????? ???????????????????????????
    private var mSearchDiscover = mutableListOf<String>()

    private var mDataStatisticsPresenter: DataStatisticsPresenter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_hmm_search
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        mSearchPresenter = HmmSearchPresenter(mContext, this)
        mDataStatisticsPresenter = DataStatisticsPresenter(mContext, this)
        if (2 == searchType) {
            mPassToSendPost?.visibility = View.GONE
        }
        getData()
    }

    override fun findViews() {
        mEtSearch = findViewById(R.id.et_search)
        mClean = findViewById(R.id.clean)
        mStatusLayout = findViewById(R.id.layout_status)
        mRefreshLayout = findViewById(R.id.layout_refresh)
        mRecyclerView = findViewById(R.id.recyclerview)
        mPassToSendPost = findViewById(R.id.passToSendPost)
        mPassToTop = findViewById(R.id.passToTop)
        mBeedS = findViewById(R.id.need_s)
        setStatusLayout(mStatusLayout)
        mRefreshLayout?.setEnableRefresh(false)
        mRefreshLayout?.setEnableLoadMore(false)
        mRefreshLayout?.setEnableOverScrollDrag(true)
        initListener()
        buildRecyclerHelper()
    }

    private fun initListener() {
        mPassToSendPost?.setOnClickListener {
            ARouter.getInstance()
                .build(CityRoutes.CITY_POSTSEND)
                .navigation()
        }
        mPassToTop?.setOnClickListener {
            mRecyclerView?.smoothScrollToPosition(0)
        }

        mRefreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mCurrentPage = 1
                getData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mCurrentPage++
                getData()
            }
        })

        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val firstCompletelyVisibleItemPosition =
                    layoutManager!!.findFirstCompletelyVisibleItemPosition()

                if (firstCompletelyVisibleItemPosition <= 6) {
                    mPassToTop?.visibility = View.GONE
                } else {
                    //System.out.println("??????????????????");
                    mPassToTop?.visibility = View.VISIBLE
                }
            }
        })

        mEtSearch?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                isMatchClick = false
            }
        }
        mEtSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNullOrEmpty()) {
                    mRecyclerItemType = 0 // ??????????????? 0????????????????????????
                    setModel()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                mSearchName = s.toString().trim()
                if (mSearchName.isNullOrEmpty().not()) {
                    if (isMatchClick) return
                    mRecyclerItemType = 1 //??????????????? 1 ?????????~~
                    setModel()
                }
            }
        })
        mEtSearch?.setOnEditorActionListener { v, actionId, event ->
            //??????
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val mKeyWord: String = mEtSearch?.text.toString().trim()
                if (mKeyWord.isNullOrEmpty()) {
                    showToast("???????????????????????????")
                    return@setOnEditorActionListener true
                }
                mSearchName = mKeyWord
                mRecyclerItemType = 2 // ????????????????????????
                isMatchClick = true
                setModel()
                getData()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        mClean?.setOnClickListener {
            finish()
        }
    }

    private fun buildRecyclerHelper() {
        val virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)
        mRecyclerView?.layoutManager = virtualLayoutManager
        mRecyclerView?.adapter = delegateAdapter

        mTitleModel = TitleModel().setTitle("????????????").setRightTitle("").setShowRight(false)
        mTitleAdapter = BaseTitleAdapter()
        delegateAdapter.addAdapter(mTitleAdapter)

        mSearchDiscoverAdapter = SearchDiscoverAdapter()
        mSearchDiscoverAdapter?.moutClickListener = this
        delegateAdapter.addAdapter(mSearchDiscoverAdapter)

        mSearchMatchAdapter = SearchMatchAdapter()
        mSearchMatchAdapter?.moutClickListener = this
        delegateAdapter.addAdapter(mSearchMatchAdapter)

        mPostAdapter = PostAdapter()
        mPostAdapter?.moutClickListener = this
        mPostAdapter?.setOnPostFansClickListener(this)
        mPostAdapter?.setOnPostLikeClickListener(this)
        mPostAdapter?.setOnShareClickListener(this)
        delegateAdapter.addAdapter(mPostAdapter)

        mVideoAdapter = HanMomVideoFindChildAdapter()
        delegateAdapter.addAdapter(mVideoAdapter)
    }

    override fun getData() {
        super.getData()
        mParamsMap.clear()
        if (ListUtil.isEmpty(mSearchDiscover)) {
            mParamsMap["contentSource"] = searchType.toString()
            mParamsMap["limit"] = "8"
            mParamsMap["searchContent"] = mSearchName
            mSearchPresenter?.querySearchRecords(mParamsMap)
        }
        if (mSearchName.isNullOrEmpty().not()) {
            if (searchType == 1) {
                mParamsMap["currentPage"] = mCurrentPage.toString()
                mParamsMap["searchContent"] = mSearchName
                mSearchPresenter?.queryPostList(mParamsMap)
            }
            if (searchType == 2) {
                mParamsMap["pageNum"] = mCurrentPage.toString()
                mParamsMap["searchContent"] = mSearchName
                mSearchPresenter?.queryHmmVideoList(mParamsMap)
            }
        }
    }

    override fun outClick(function: String, obj: Any) {
        when (function) {
            "matchClick" -> {
                isMatchClick = true
                mSearchName = obj.toString()
                mRecyclerItemType = 2 //??????????????? ????????????
                setModel()
                getData()
            }
            "submit" -> showReviewWarnDialog(obj.toString())
            "sharePk" -> {
                mDataStatisticsPresenter!!.shareAndLook(
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
                    it.show(supportFragmentManager, "")
                    it.setOnConfirmClick {
                        mCurrentPage = 1
                        getData()
                    }
                }
            }
        }
    }

    /**
     * ??????????????????????????????
     */
    fun setModel() {
        mRefreshLayout?.setEnableRefresh(false)
        mRefreshLayout?.setEnableLoadMore(false)
        mRefreshLayout?.setEnableOverScrollDrag(false)
        when (mRecyclerItemType) {
            0 -> { // ????????????
                mBeedS?.setBackgroundColor(Color.WHITE)
                mRefreshLayout?.setEnableOverScrollDrag(true)
                mSearchMatchAdapter?.clear()
                mPostAdapter?.clear()
                mVideoAdapter?.clear()
                if (mSearchDiscoverAdapter?.getDatas()?.size != mSearchDiscover.size) {
                    mSearchDiscoverAdapter?.setDataAll(mSearchDiscover as ArrayList<String>)
                }
                if (!ListUtil.isEmpty(mSearchDiscoverAdapter?.getDatas())) {
                    mTitleAdapter?.setModel(mTitleModel)
                    showContent()
                } else {
                    showEmpty()
                }
            }
            1 -> { // ??????????????????
                mBeedS?.setBackgroundColor(Color.WHITE)
                mRefreshLayout?.setEnableOverScrollDrag(true)
                mPostAdapter?.clear()
                mVideoAdapter?.clear()
                mParamsMap.clear()
                mParamsMap["contentSource"] = searchType.toString()
                mParamsMap["limit"] = "10"
                mParamsMap["searchContent"] = mSearchName
                mSearchPresenter?.querySearchRecords(mParamsMap)
            }
            2 -> { // ????????????
                mCurrentPage = 1
                mBeedS?.setBackgroundColor(Color.parseColor("#F6F7F9"))
                mRefreshLayout?.setEnableRefresh(true)
                mRefreshLayout?.setEnableLoadMore(true)
                mEtSearch?.setText(mSearchName)
                mEtSearch?.clearFocus()
                KeyboardUtils.hideSoftInput(mEtSearch)
                mTitleAdapter?.clear()
                mSearchDiscoverAdapter?.clear()
            }
        }
    }

    /**
     * ????????????
     */
    fun showReviewWarnDialog(warnid: String) {
        val strings: MutableList<String> = ArrayList()
        val stringsColors: MutableList<Int> = ArrayList()
        stringsColors.add(Color.parseColor("#444444"))
        strings.add("??????")
        StyledDialog.init(mContext)
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

    /**
     * ????????????
     */
    fun showWarnDialog(warnid: String) {
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
        StyledDialog.init(mContext)
        warndialog = StyledDialog.buildBottomItemDialog(
            strings,
            stringsColors,
            "??????",
            object : MyItemDialogListener() {
                override fun onItemClick(text: CharSequence, position: Int) {
                    mParamsMap.clear()
                    mParamsMap["type"] = "1"
                    mParamsMap["sourceId"] = warnid
                    mParamsMap["reason"] = text.toString()
                    mSearchPresenter?.warn(mParamsMap)
                }

                override fun onBottomBtnClick() {}
            }).setTitle("??????????????????").setTitleColor(R.color.color_444444).setTitleSize(15)
            .setCancelable(true, true).show()
        warndialog?.setOnDismissListener { warndialog = null }
    }

    /**
     * ??????????????????????????????
     */
    override fun onQuerySearchRecordsSuccess(records: MutableList<String>?) {
        if (ListUtil.isEmpty(records)) {
            showEmpty()
            return
        }
        if (ListUtil.isEmpty(mSearchDiscover)) {
            mSearchDiscover.addAll(records!!)
            mTitleAdapter?.setModel(mTitleModel)
            mSearchDiscoverAdapter?.setDataAll(mSearchDiscover as ArrayList<String>)
            return
        }
        mTitleAdapter?.clear()
        mSearchDiscoverAdapter?.clear()
        mSearchMatchAdapter?.mSearchName = mSearchName
        mSearchMatchAdapter?.clear()
        mSearchMatchAdapter?.setDataAll(records as ArrayList<String>)
    }

    /**
     * ???????????????????????????
     */
    override fun onQueryPostListSuccess(records: MutableList<PostDetail>?, isMore: Boolean) {
        mSearchMatchAdapter?.clear()
        if (isMore) {
            mRefreshLayout?.resetNoMoreData()
            mRefreshLayout?.finishLoadMore()
        } else {
            mRefreshLayout?.finishLoadMoreWithNoMoreData()
        }

        if (mCurrentPage == 1) {
            mPostAdapter?.clear()
            mPostAdapter?.setData(records as ArrayList<PostDetail>)
        } else {
            mPostAdapter?.addDatas(records as ArrayList<PostDetail>)
        }

        if (ListUtil.isEmpty(mPostAdapter?.getDatas())) {
            showEmpty()
            mRefreshLayout?.finishLoadMoreWithNoMoreData()
            return
        }
        records?.let {
            for (i in it.indices) {
                if (it[i].postingType == 5 || it[i].postingType == 6) { //?????????????????????????????????????????????????????? ?????????????????????
                    val postDetail: PostDetail = it[i]
                    if (postDetail.postActivityList != null && postDetail.postActivityList.size > 0) {
                    } else {
                        mParamsMap.clear()
                        mParamsMap["postingId"] = postDetail.id
                        mSearchPresenter?.getActivityList(mParamsMap, postDetail)
                    }
                }
            }
        }

        if (mSearchDiscover.contains(mSearchName).not() && mSearchDiscover.size < 8) {
            mSearchDiscover.add(mSearchName)
        }
    }

    /**
     * ??????????????????????????????
     */
    override fun onQueryVideoListSuccess(
        records: MutableList<VideoListModel>?,
        isMore: Boolean
    ) {
        mSearchMatchAdapter?.clear()
        if (isMore) {
            mRefreshLayout?.resetNoMoreData()
            mRefreshLayout?.finishLoadMore()
        } else {
            mRefreshLayout?.finishLoadMoreWithNoMoreData()
        }

        if (mCurrentPage == 1) {
            mVideoAdapter?.clear()
            mVideoAdapter?.setData(records as ArrayList<VideoListModel>)
        } else {
            mVideoAdapter?.addDatas(records as ArrayList<VideoListModel>)
        }

        if (ListUtil.isEmpty(mVideoAdapter?.getDatas())) {
            showEmpty()
            mRefreshLayout?.finishLoadMoreWithNoMoreData()
            return
        }
        if (mSearchDiscover.contains(mSearchName).not() && mSearchDiscover.size < 8) {
            mSearchDiscover.add(mSearchName)
        }
    }

    /**
     * ?????????????????????
     */
    override fun onSuccessFan(result: Any?) {
        if ("0" == result) {
            showToast("????????????")
        }
    }

    /**
     * ?????????????????????
     */
    override fun onSuccessLike() {

    }

    /**
     * ???????????? ??????????????????????????????
     */
    override fun onSuccessGetActivityList() {
        mPostAdapter?.let { it.notifyDataSetChanged() }
    }

    override fun onRequestFinish() {
        super.onRequestFinish()
        mRefreshLayout?.finishRefresh()
    }

    override fun postfansclick(view: View, friendId: String, type: String, frtype: String) {
        mParamsMap.clear()
        mParamsMap["friendId"] = friendId
        mParamsMap["friendType"] = frtype
        mParamsMap["type"] = type
        mSearchPresenter?.fan(mParamsMap)
    }

    override fun postlikeclick(view: View, postingId: String, type: String) {
        mParamsMap.clear()
        mParamsMap["postingId"] = postingId
        mParamsMap["type"] = type
        mSearchPresenter?.like(mParamsMap)
    }

    override fun postshareclick(view: View, url: String, des: String, title: String,postId:String) {
        this.surl = url
        this.sdes = des
        this.stitle = title

        showShare()
        mDataStatisticsPresenter!!.shareAndLook(
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

}