package com.health.index.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.index.R
import com.health.index.adapter.IndexBabyPostTopAdapter
import com.health.index.contract.IndexBabyContract
import com.healthy.library.model.NewsInfo
import com.health.index.presenter.IndexBabyPresenter
import com.healthy.library.adapter.EmptyAdapter
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
import com.healthy.library.utils.SpUtils
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.hss01248.dialog.interfaces.MyItemDialogListener
import kotlinx.android.synthetic.main.fragment_index_baby_study.recyclerview
import java.util.ArrayList

class IndexBabyPostFragment : BaseFragment(),
    IndexBabyContract.View,
    BaseAdapter.OnOutClickListener,
    PostAdapter.OnPostFansClickListener,
    PostAdapter.OnPostLikeClickListener,
    PostAdapter.OnShareClickListener,
    DataStatisticsContract.View,
    IsNeedShare {

    private var indexBabyPresenter: IndexBabyPresenter? = null
    private var emptyAdapter: EmptyAdapter? = null
    private var virtualLayoutManager: VirtualLayoutManager? = null
    private var indexBabyPostTopAdapter: IndexBabyPostTopAdapter? = null
    private var mDataStatisticsPresenter: DataStatisticsPresenter? = null
    private var mPostAdapter: PostAdapter? = null//帖子

    private var couponDialog: PostCouponDialog? = null
    private var reviewandwarndialog: Dialog? = null
    private var warndialog: Dialog? = null
    private var mMap: MutableMap<String, Any> = mutableMapOf()
    private var surl: String = ""
    private var sdes: String = ""
    private var stitle: String = ""
    private var pageNum = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_index_baby_post
    }

    override fun findViews() {
        indexBabyPresenter = IndexBabyPresenter(mContext, this)
        mDataStatisticsPresenter = DataStatisticsPresenter(mContext, this)

        virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recyclerview?.apply {
            layoutManager = virtualLayoutManager
            adapter = delegateAdapter
            isNestedScrollingEnabled = false
        }

        emptyAdapter = EmptyAdapter()
        delegateAdapter.addAdapter(emptyAdapter)

        indexBabyPostTopAdapter = IndexBabyPostTopAdapter()
        delegateAdapter.addAdapter(indexBabyPostTopAdapter)

        mPostAdapter = PostAdapter()
        mPostAdapter?.moutClickListener = this
        mPostAdapter?.setOnPostFansClickListener(this)
        mPostAdapter?.setOnPostLikeClickListener(this)
        mPostAdapter?.setOnShareClickListener(this)
        delegateAdapter.addAdapter(mPostAdapter)


    }

    override fun onLazyData() {
        super.onLazyData()
        getData()
    }

    override fun getData() {
        super.getData()
        if(indexBabyPresenter==null){
            indexBabyPresenter = IndexBabyPresenter(mContext, this)
        }
        indexBabyPresenter?.queryPostList(
            SimpleHashMapBuilder<String, Any>()
                .puts("pageSize", 10)!!
                .puts("type2", "1")!!
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))!!
                .puts("limitsStatus", "1")!!
                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))!!
                .puts("type", "1")!!
                .puts("currentPage", 1)!!
                .puts("addressArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))!!
                .puts("addressCity", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))!!
                .puts(
                    "addressProvince",
                    LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE)
                )!! as MutableMap<String, Any>
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            IndexBabyPostFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    public fun refresh(){
        getData()
    }

    override fun onQueryPostListSuccess(records: MutableList<PostDetail>?, isMore: Boolean) {
        emptyAdapter?.clear()
        mPostAdapter?.clear()
        indexBabyPostTopAdapter?.model = "null"
        mPostAdapter?.setData(records as ArrayList<PostDetail>)
        if (ListUtil.isEmpty(mPostAdapter?.getDatas())) {
            emptyAdapter?.model = "null"
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
                        indexBabyPresenter?.getActivityList(mMap, postDetail)
                    }
                }
            }
        }
    }

    override fun onQueryHmmVideoListSuccess(
        records: MutableList<VideoListModel>?,
        isMore: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun onQueryVideoListSuccess(records: MutableList<VideoListModel>?, isMore: Boolean) {
        TODO("Not yet implemented")
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

    override fun onSuccessGetNewsList(result: MutableList<NewsInfo>?, isMore: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onSuccessGetAudioNewsList(result: MutableList<SoundAlbum>?) {
        TODO("Not yet implemented")
    }

    override fun onSuccessGetSoundAlbumsDownList(result: SoundHomeSplit?) {
        TODO("Not yet implemented")
    }

    override fun successGetSoundAlbum(result: SoundTypeSplit?) {
        TODO("Not yet implemented")
    }

    override fun postfansclick(view: View?, friendId: String, type: String, frtype: String) {
        mMap.clear()
        mMap["friendId"] = friendId
        mMap["friendType"] = frtype
        mMap["type"] = type
        indexBabyPresenter?.fan(mMap)
    }

    override fun postlikeclick(view: View?, postingId: String, type: String) {
        mMap.clear()
        mMap["postingId"] = postingId
        mMap["type"] = type
        indexBabyPresenter?.like(mMap)
    }

    override fun postshareclick(
        view: View?,
        url: String,
        des: String,
        title: String,
        postId: String?
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

    override fun outClick(function: String?, obj: Any?) {
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
                    indexBabyPresenter?.warn(mMap)
                }

                override fun onBottomBtnClick() {}
            }).setTitle("举报内容问题").setTitleColor(com.healthy.library.R.color.color_444444)
            .setTitleSize(15)
            .setCancelable(true, true).show()
        warndialog?.setOnDismissListener { warndialog = null }
    }
}