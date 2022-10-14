package com.health.city.activity

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.city.R
import com.health.city.contract.EssencesContract
import com.health.city.presenter.EssencesPresenter
import com.healthy.library.adapter.PostAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseMultiItemAdapter
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.contract.DataStatisticsContract
import com.healthy.library.dialog.PostCouponDialog
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.interfaces.IsNeedShare
import com.healthy.library.model.PostDetail
import com.healthy.library.presenter.DataStatisticsPresenter
import com.healthy.library.presenter.HmmSearchPresenter
import com.healthy.library.routes.CityRoutes
import com.healthy.library.widget.StatusLayout
import com.healthy.library.widget.TopBar
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.hss01248.dialog.interfaces.MyItemDialogListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import java.util.ArrayList

/**
 * author long
 * desc 精华帖
 * time 2021/11/09
 */
@Route(path = CityRoutes.CITY_ESSENCES)
class EssencesActivity : BaseActivity(), IsFitsSystemWindows, IsNeedShare, EssencesContract.View,
    BaseAdapter.OnOutClickListener, PostAdapter.OnPostFansClickListener,
    PostAdapter.OnPostLikeClickListener, PostAdapter.OnShareClickListener , DataStatisticsContract.View{

    private var mParamsMap = mutableMapOf<String, Any>()

    private var mTopBar: TopBar? = null
    private var mStatusLayout: StatusLayout? = null
    private var mRefreshLayout: SmartRefreshLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var mPassToSendPost: ImageView? = null
    private var mPassToTop: ImageView? = null

    private var mPresenter: EssencesPresenter? = null
    private var mPostAdapter: PostAdapter? = null
    private var couponDialog: PostCouponDialog? = null
    private var reviewandwarndialog: Dialog? = null
    private var warndialog: Dialog? = null

    private var surl: String = ""
    private var sdes: String = ""
    private var stitle: String = ""

    private var mCurrentPage: Int = 1
    private var mDataStatisticsPresenter: DataStatisticsPresenter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_essences
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        mPresenter = EssencesPresenter(mContext, this)
        mDataStatisticsPresenter = DataStatisticsPresenter(mContext, this)
        getData()
    }

    override fun getData() {
        super.getData()
        mParamsMap.clear()
        mParamsMap["currentPage"] = mCurrentPage
        mPresenter?.getPostingList(mParamsMap)
    }

    override fun findViews() {
        super.findViews()
        mTopBar = findViewById(R.id.top_bar)
        mStatusLayout = findViewById(R.id.layout_status)
        mRefreshLayout = findViewById(R.id.layout_refresh)
        mRecyclerView = findViewById(R.id.recyclerview)
        mPassToSendPost = findViewById(R.id.passToSendPost)
        mPassToTop = findViewById(R.id.passToTop)
        setStatusLayout(mStatusLayout)
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
                    //System.out.println("互动修改显示");
                    mPassToTop?.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun buildRecyclerHelper() {
        val virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)
        mRecyclerView?.layoutManager = virtualLayoutManager
        mRecyclerView?.adapter = delegateAdapter

        mPostAdapter = PostAdapter()
        mPostAdapter?.moutClickListener = this
        mPostAdapter?.setOnPostFansClickListener(this)
        mPostAdapter?.setOnPostLikeClickListener(this)
        mPostAdapter?.setOnShareClickListener(this)
        delegateAdapter.addAdapter(mPostAdapter)
    }

    override fun outClick(function: String, obj: Any) {
        when (function) {
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
                    mParamsMap.clear()
                    mParamsMap["type"] = "1"
                    mParamsMap["sourceId"] = warnid
                    mParamsMap["reason"] = text.toString()
                    mPresenter?.warn(mParamsMap)
                }

                override fun onBottomBtnClick() {}
            }).setTitle("举报内容问题").setTitleColor(com.healthy.library.R.color.color_444444)
            .setTitleSize(15)
            .setCancelable(true, true).show()
        warndialog?.setOnDismissListener { warndialog = null }
    }

    override fun onQueryPostListSuccess(records: MutableList<PostDetail>?, isMore: Boolean) {
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
                if (it[i].postingType == 5 || it[i].postingType == 6) { //行内请求主要请求优惠券和视频关联商品 其他帖子不请求
                    val postDetail: PostDetail = it[i]
                    if (postDetail.postActivityList != null && postDetail.postActivityList.size > 0) {
                    } else {
                        mParamsMap.clear()
                        mParamsMap["postingId"] = postDetail.id
                        mPresenter?.getActivityList(mParamsMap, postDetail)
                    }
                }
            }
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

    override fun onRequestFinish() {
        super.onRequestFinish()
        mRefreshLayout?.finishRefresh()
    }

    override fun postfansclick(view: View, friendId: String, type: String, frtype: String) {
        mParamsMap.clear()
        mParamsMap["friendId"] = friendId
        mParamsMap["friendType"] = frtype
        mParamsMap["type"] = type
        mPresenter?.fan(mParamsMap)
    }

    override fun postlikeclick(view: View, postingId: String, type: String) {
        mParamsMap.clear()
        mParamsMap["postingId"] = postingId
        mParamsMap["type"] = type
        mPresenter?.like(mParamsMap)
    }

    override fun postshareclick(view: View?, url: String, des: String, title: String,postId: String) {
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