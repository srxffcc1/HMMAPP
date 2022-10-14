package com.health.index.activity

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.health.index.BuildConfig
import com.health.index.R
import com.health.index.adapter.IndexMonthlyGoodsAdapter
import com.health.index.adapter.IndexMonthlyQuestionAdapter
import com.health.index.adapter.IndexMonthlyVideoAdapter
import com.health.index.contract.IndexMonthlyContract
import com.health.index.model.ActivityInfo
import com.health.index.presenter.IndexMonthlyPresenter
import com.healthy.library.adapter.PostAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.base.BaseAdapter
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.business.MonthlyShareDialog
import com.healthy.library.businessutil.ChannelUtil
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.constant.UrlKeys
import com.healthy.library.contract.DataStatisticsContract
import com.healthy.library.dialog.PkVotingTipDialog
import com.healthy.library.dialog.PostCouponDialog
import com.healthy.library.interfaces.IsNeedShare
import com.healthy.library.model.*
import com.healthy.library.presenter.DataStatisticsPresenter
import com.healthy.library.routes.AppRoutes
import com.healthy.library.routes.CityRoutes
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.DateUtils
import com.healthy.library.utils.DrawableUtils
import com.healthy.library.utils.SpUtils
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyItemDialogListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_monthly.*
import org.greenrobot.eventbus.EventBus

@Route(path = IndexRoutes.INDEX_MONTHLY)
class MonthlyActivity : BaseActivity(),
    PostAdapter.OnPostFansClickListener,
    PostAdapter.OnPostLikeClickListener,
    PostAdapter.OnShareClickListener,
    OnRefreshLoadMoreListener,
    IsNeedShare,
    BaseAdapter.OnOutClickListener,
    IndexMonthlyContract.View,
    DataStatisticsContract.View {

    @Autowired
    @JvmField
    var specialld = ""

    @Autowired
    @JvmField
    var merchantId = ""

    private var indexMonthlyVideoAdapter: IndexMonthlyVideoAdapter? = null
    private var indexMonthlyQuestionAdapter: IndexMonthlyQuestionAdapter? = null
    private var indexMonthlyGoodsAdapter: IndexMonthlyGoodsAdapter? = null
    private var mPostAdapter: PostAdapter? = null//帖子

    private var mMap: MutableMap<String, Any> = mutableMapOf()
    private var mPost: PostDetail? = null

    private var indexMonthlyPresenter: IndexMonthlyPresenter? = null//帖子
    private var mDataStatisticsPresenter: DataStatisticsPresenter? = null

    private var surl: String? = null
    private var sdes: String? = null
    private var stitle: String? = null
    private var sBitmap: Bitmap? = null
    private var couponDialog: PostCouponDialog? = null
    private var reviewandwarndialog: Dialog? = null
    private var warndialog: Dialog? = null
    private var shareBitmap: Bitmap? = null

    private var activityTitle: String? = null
    private var description: String? = null
    private var postimg: String? = null
    private var isNews = true//最新还是热门
    private var topicId: String? = null
    private var topicName: String? = null
    private var questionType: String? = null
    private var postPkId: String? = null
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        if (TextUtils.isEmpty(merchantId)) {
            merchantId = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
        }
        buildRecyclerHelper()
        showLoading()
        layout_refresh.setOnRefreshLoadMoreListener(this)
        indexMonthlyPresenter = IndexMonthlyPresenter(this, this)
        mDataStatisticsPresenter = DataStatisticsPresenter(mContext, this)
        getData()

        news.setOnClickListener {
            isNews = true
            currentPage = 1
            getTopic()
            news.textColorBuilder.setTextColor(Color.parseColor("#FFFFFF")).intoTextColor()
            news.shapeDrawableBuilder.setGradientColors(
                Color.parseColor("#FD6F93"),
                Color.parseColor("#FA3C5A")
            ).intoBackground()
            hots.textColorBuilder.setTextColor(Color.parseColor("#333333")).intoTextColor()
            hots.shapeDrawableBuilder.setGradientColors(
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#FFFFFF")
            ).intoBackground()
            postRecycle.scrollToPosition(0)
        }
        hots.setOnClickListener {
            currentPage = 1
            isNews = false
            getTopic()
            hots.textColorBuilder.setTextColor(Color.parseColor("#FFFFFF")).intoTextColor()
            hots.shapeDrawableBuilder.setGradientColors(
                Color.parseColor("#FD6F93"),
                Color.parseColor("#FA3C5A")
            ).setAngle(45).intoBackground()
            news.textColorBuilder.setTextColor(Color.parseColor("#333333")).intoTextColor()
            news.shapeDrawableBuilder.setGradientColors(
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#FFFFFF")
            ).intoBackground()
            postRecycle.scrollToPosition(0)
        }
        top_bar.submitBack.setOnClickListener {
            if (TextUtils.isEmpty(specialld)||TextUtils.isEmpty(postimg)) {
                showToast("获取分享参数失败")
                return@setOnClickListener
            }
            var shareDialog = MonthlyShareDialog.newInstance()
            shareDialog.setData(specialld,postimg)
            shareDialog.show(supportFragmentManager, "分享")
//            if (TextUtils.isEmpty(description)||TextUtils.isEmpty(activityTitle)) {
//                showToast("获取分享参数失败")
//                return@setOnClickListener
//            }
//            val urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl)
//            val url =
//                String.format(
//                    "%s?type=8&scheme=MonthlySpecial&specialld=%s&referral_code=%s&merchantId=%s",
//                    urlPrefix,
//                    specialld,
//                    SpUtils.getValue(mContext, SpKey.GETTOKEN),
//                    merchantId
//                )
//            surl = url
//            stitle = activityTitle
//            sBitmap = shareBitmap
//            sdes = description
//            showShare()
        }
        dragLayout.bringToFront()
        scrollerLayout.setOnVerticalScrollChangeListener { v, scrollY, oldScrollY, scrollState ->
            if (oldScrollY < 0) { //可能是负数 属于刚进来就触发了这个调用方法也不清楚为啥
                return@setOnVerticalScrollChangeListener
            }
            if (scrollerLayout.theChildIsStick(topic)) {
                dragLayout.visibility = View.VISIBLE
                topic.setBackgroundColor(Color.parseColor("#FFFFFF"))
            } else {
                dragLayout.visibility = View.GONE
                topic.setBackgroundColor(Color.parseColor("#F6F7F9"))
            }
            if (scrollerLayout.theChildIsStick(tabLayout)) {
                tabLayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
            } else {
                tabLayout.setBackgroundColor(Color.parseColor("#F6F7F9"))
            }
        }
        sendPost.setOnClickListener {
            ARouter.getInstance()
                .build(CityRoutes.CITY_POSTSEND)
                .withString("topicId", topicId)
                .withString("topicName", topicName)
                .navigation()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_monthly
    }

    override fun getData() {
        super.getData()
        indexMonthlyPresenter?.getActivityInfo(
            SimpleHashMapBuilder<String, Any>().puts(
                "id",
                specialld
            )!! as MutableMap<String, Any>
        )
        mDataStatisticsPresenter?.shareAndLook(
            SimpleHashMapBuilder<String, Any>().puts("sourceId", specialld)!!
                .puts("sourceType", 3)!!.puts("type", 1) as MutableMap<String, Any>?
        )
        indexMonthlyPresenter?.queryGoodsList(
            SimpleHashMapBuilder<String, Any>()
                .puts("type", "2")!!
                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))!!
                .puts("pageNum", "1")!!
                .puts("pageSize", "2")!!
                .puts("firstPageSize", "0")!!
                    as MutableMap<String, Any>
        )
    }

    private fun buildRecyclerHelper() {
        val virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)
        listRecycle?.layoutManager = virtualLayoutManager
        listRecycle?.adapter = delegateAdapter

        indexMonthlyVideoAdapter = IndexMonthlyVideoAdapter()
        delegateAdapter.addAdapter(indexMonthlyVideoAdapter)
        indexMonthlyVideoAdapter!!.setShareVideoListener(object :
            IndexMonthlyVideoAdapter.ShareVideoListener {
            override fun shareVideo(id: String, title: String, bitmap: Bitmap) {
//                buildDes(id, title, bitmap)
//                showShare()
                var shareDialog = MonthlyShareDialog.newInstance()
                shareDialog.setData(specialld,postimg)
                shareDialog.show(supportFragmentManager, "分享")
            }
        })
        indexMonthlyVideoAdapter!!.moutClickListener = this

        indexMonthlyQuestionAdapter = IndexMonthlyQuestionAdapter()
        delegateAdapter.addAdapter(indexMonthlyQuestionAdapter)
        indexMonthlyQuestionAdapter!!.moutClickListener = this

        indexMonthlyGoodsAdapter = IndexMonthlyGoodsAdapter()
        delegateAdapter.addAdapter(indexMonthlyGoodsAdapter)
        indexMonthlyGoodsAdapter!!.moutClickListener = this

        mPostAdapter = PostAdapter()
        mPostAdapter?.moutClickListener = this
        mPostAdapter?.setOnPostFansClickListener(this)
        mPostAdapter?.setOnPostLikeClickListener(this)
        mPostAdapter?.setOnShareClickListener(this)
        postRecycle.layoutManager = LinearLayoutManager(this)
        postRecycle.adapter = mPostAdapter
    }

    private fun buildDes(id: String, title: String, bitmap: Bitmap) {
        val urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_classVideoContUrl)
        val url =
            String.format("%s?id=%s&scheme=HMMVideoDetail&videoId=%s", urlPrefix, id, id)
        surl = url
        stitle = "憨妈妈专家课堂"
        sBitmap = bitmap
        sdes = title
    }

    override fun postfansclick(view: View, friendId: String, type: String, frtype: String) {
        mMap.clear()
        mMap["friendId"] = friendId
        mMap["friendType"] = frtype
        mMap["type"] = type
        indexMonthlyPresenter?.fan(mMap)
    }

    override fun postlikeclick(view: View, postingId: String, type: String) {
        mMap.clear()
        mMap["postingId"] = postingId
        mMap["type"] = type
        indexMonthlyPresenter?.like(mMap)
    }

    override fun postshareclick(
        view: View?,
        url: String?,
        des: String?,
        title: String?,
        postId: String?
    ) {
        this.surl = url
        this.sdes = des
        this.stitle = title
        this.sBitmap = null

        showShare()
        mDataStatisticsPresenter?.shareAndLook(
            SimpleHashMapBuilder<String, Any>().puts("sourceId", postId)!!
                .puts("sourceType", 2)!!.puts("type", 2) as MutableMap<String, Any>?
        )
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        currentPage = 1
        indexMonthlyVideoAdapter!!.clear()
        indexMonthlyVideoAdapter!!.setPKData(null)
        indexMonthlyQuestionAdapter!!.clear()
        indexMonthlyGoodsAdapter!!.clear()
        activityTitle = null
        description = null
        topicId = null
        topicName = null
        questionType = null
        postPkId = null
        mPost = null
        postimg = null
        getData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        currentPage++;
        getTopic()
    }

    override fun outClick(function: String?, obj: Any?) {
        when (function) {

            "跳转搜索问答" -> {
                ARouter.getInstance()
                    .build(AppRoutes.MODULE_EXPERT_LEFT)
                    .withString("cityNo", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .withString("expertCategoryNo", questionType)
                    .navigation()
            }
            "跳转搜索商品" -> {
                this.finish()
                EventBus.getDefault().post(TabChangeModel(3))
                EventBus.getDefault().post(ServiceTabChangeModel(1))
            }
            "like" -> {
                var arr = obj as Array<String>
                if (arr[1].toInt() == 1) {
                    indexMonthlyPresenter?.addPraise(
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
                    indexMonthlyPresenter?.addPraise(
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
                    it.show(supportFragmentManager, "")
                    it.setOnConfirmClick {
                        currentPage = 1
                        getData()
                    }
                }
            }
            "voteStand" -> {
                //obj = 1 (支持正方） / 2 （支持反方)
                val voteStand = obj.toString()
                val pkActivity = mPost?.pkActivity
                val mActivityEndTime = DateUtils.str2Long(
                    pkActivity?.activityEndTime,
                    DateUtils.DATE_FORMAT_PATTERN_YMD_HMS
                )
                val mActivityStartTime = DateUtils.str2Long(
                    pkActivity?.activityStartTime,
                    DateUtils.DATE_FORMAT_PATTERN_YMD_HMS
                )
                val startTime = (mActivityStartTime - System.currentTimeMillis()) / 1000 //转换为秒

                if (startTime > 0) {
                    showTipDialog("活动未开始！", "亲，这场活动还未开始哟~")
                    return
                }
                if (mActivityEndTime < System.currentTimeMillis() || "1" != pkActivity?.status) {
                    showTipDialog("活动结束！", "亲，这场活动已经投票结束咯~")
                    return
                }
                if (pkActivity.currentUserTodayVoteNum >= pkActivity.onePeopleOneDayNum) {
                    showTipDialog("投票次数不足！", "亲，每天只可以投${pkActivity.onePeopleOneDayNum}票哦~~")
                    return
                }
                if (!pkActivity.canCrossVote &&
                    (pkActivity.currentUserLastVoteStand.isNullOrEmpty().not()
                            && pkActivity.currentUserLastVoteStand != voteStand)
                ) {
                    //判断不支持交叉投情况
                    showTipDialog("投错票了哦~", "亲，一个人只能支持一方哦~~")
                    return
                }
                mMap.clear()
                mMap["pkActivityId"] = mPost?.pkActivity?.id!!
                mMap["voteStand"] = voteStand
                mMap["merchantId"] = merchantId
                indexMonthlyPresenter?.addPkVote(mMap)
            }
        }
    }

    override fun getSurl(): String {
        return surl!!
    }

    override fun getSdes(): String {
        return sdes!!
    }

    override fun getStitle(): String {
        return stitle!!
    }

    override fun getsBitmap(): Bitmap? {
        if (sBitmap != null) {
            return sBitmap!!
        } else {
            return null
        }
    }

    override fun onQueryPostListSuccess(records: MutableList<PostDetail>?, isMore: Boolean) {
        showContent()
        if (isMore) {
            layout_refresh?.resetNoMoreData()
            layout_refresh?.finishLoadMore()
        } else {
            layout_refresh?.finishLoadMoreWithNoMoreData()
        }

        if (currentPage == 1) {
            mPostAdapter?.clear()
            mPostAdapter?.setData(records as ArrayList<PostDetail>)
        } else {
            mPostAdapter?.addDatas(records as ArrayList<PostDetail>)
        }

        if (ListUtil.isEmpty(mPostAdapter?.getDatas())) {
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
                        indexMonthlyPresenter?.getActivityList(mMap, postDetail)
                    }
                }
            }
        }
    }

    override fun onQueryHmmVideoListSuccess(
        records: MutableList<VideoListModel>?,
        isMore: Boolean
    ) {
        indexMonthlyVideoAdapter?.clear()
        if (!ListUtil.isEmpty(records)) {
            indexMonthlyVideoAdapter?.setAdapterData(records!!)
            indexMonthlyVideoAdapter?.model = "null"
        }
    }

    override fun onQueryVideoListSuccess(records: MutableList<VideoListModel>?, isMore: Boolean) {

    }

    override fun onSuccessFan(result: Any?) {

    }

    override fun onSuccessLike() {

    }

    override fun onSuccessGetActivityList() {

    }

    override fun onQueryGoodsListSuccess(
        records: MutableList<ActGoodsItem>?,
        isMore: Boolean
    ) {
        indexMonthlyGoodsAdapter?.clear()
        if (!ListUtil.isEmpty(records)) {
            indexMonthlyGoodsAdapter?.setAdapterData(records!!)
            indexMonthlyGoodsAdapter?.model = "null"
        }
    }

    override fun onAddPraiseSuccess(result: String, type: Int) {

    }

    override fun onQueryQuestionListSuccess(
        records: MutableList<FaqExportQuestion>?,
        isMore: Boolean
    ) {
        indexMonthlyQuestionAdapter?.clear()
        if (!ListUtil.isEmpty(records)) {
            indexMonthlyQuestionAdapter?.setAdapterData(records!!)
            indexMonthlyQuestionAdapter?.model = "null"
        }
    }

    override fun onGetAllQuestionListSuccess(records: MutableList<IndexAllQuestion>?) {

    }

    override fun onGetActivityInfoSuccess(records: ActivityInfo?, status: Int) {
        if (status == -1) {
            showEmpty()
            return
        }
        layout_refresh?.finishRefresh()
        if (records != null) {
            postimg = records.postimg
            if (!TextUtils.isEmpty(records.name)) {
                top_bar.setTitle(records.name)
                activityTitle = records.name
                description = records.description
            }
            if (!TextUtils.isEmpty(records.postPkId)) {
                postPkId = records.postPkId
                getPKData()
            }
            if (!TextUtils.isEmpty(records.videoIds)) {
                indexMonthlyPresenter?.queryHmmVideoList(
                    SimpleHashMapBuilder<String, Any>()
                        .puts("ids", records.videoIds.split(","))!!
                        .puts(
                            "merchantId",
                            merchantId
                        )!! as MutableMap<String, Any>
                )
            }
            if (!TextUtils.isEmpty(records.questionType)) {
                questionType = records.questionType
                indexMonthlyPresenter?.queryQuestionList(
                    SimpleHashMapBuilder<String, Any>()
                        .puts("expertCategoryNo", records.questionType)!!
                        .puts("pageSize", "4")!!
                        .puts("pageNum", "1")!!
                        .puts("fragmentBottom", "1")!! as MutableMap<String, Any>
                )
            }
            if (!TextUtils.isEmpty(records.topicId)) {
                topicId = records.topicId
                topicName = records.name
                indexMonthlyPresenter?.getTipPost(
                    SimpleHashMapBuilder<String, Any>()
                        .puts("topicId", topicId)!!
                        .puts(
                            "merchantId",
                            merchantId
                        )!! as MutableMap<String, Any>
                )

                getTopic()
            }
            Glide.with(mContext).load(records.img)
                .placeholder(R.drawable.index_share_humb)
                .error(R.drawable.index_share_humb)
                .into(object : SimpleTarget<Drawable?>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        shareBitmap = DrawableUtils.drawableToBitmap(resource)
                    }
                })
        } else {
            showEmpty()
        }
    }

    private fun getPKData() {
        indexMonthlyPresenter?.getPKPostDetail(
            SimpleHashMapBuilder<String, Any>()
                .puts("id", postPkId)!!
                .puts("merchantId", merchantId)!!
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

    private fun getTopic() {
        showLoading()
        mMap.clear()
        mMap["type"] = "2"
        mMap["topicId"] = topicId!!
        mMap["pageSize"] = "10"
        mMap["currentPage"] = currentPage
        mMap["addressCity"] = LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE)
        mMap["addressProvince"] = LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE)
        mMap["addressArea"] = LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)
        mMap["shopId"] = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)
        mMap["merchantId"] = merchantId
        if (isNews) {//最新
            mMap["type2"] = "1"
        } else {//热门
            mMap["type2"] = "2"
        }
        indexMonthlyPresenter?.queryPostList(mMap)
    }

    override fun onSuccessGetTipPost(records: TipPost?) {
        if (records != null) {
            topicTitle.text = "#" + records.topicsBaseDTO.topicName
            topicContent.text = records.topicsBaseDTO.topicDescription
        }
    }

    override fun onSuccessGetPKPostDetail(records: PostDetail?) {
        if (records != null) {
            mPost = records
            indexMonthlyVideoAdapter?.setPKData(records!!)
            indexMonthlyVideoAdapter?.model = "null"
        }
    }

    override fun onAddPkVoteSuccess() {
        showTipDialog(
            "+${mPost?.pkActivity?.voteScore}分！",
            "恭喜完成投票～"
        )
        //重新请求详情
        getPKData()
    }

    /**
     * 提示弹框
     */
    private fun showTipDialog(tipTitle: String, tipMessage: String) {
        PkVotingTipDialog.newInstance()
            .setBody(tipTitle, tipMessage)
            .show(supportFragmentManager, "pkVotingTipDialog")
    }

    /**
     * 举报弹窗
     */
    fun showReviewWarnDialog(warnid: String) {
        val strings: MutableList<String> = java.util.ArrayList()
        val stringsColors: MutableList<Int> = java.util.ArrayList()
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
        val stringsColors: MutableList<Int> = java.util.ArrayList()
        val strings: MutableList<String> = java.util.ArrayList()
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
                    indexMonthlyPresenter?.warn(mMap)
                }

                override fun onBottomBtnClick() {}
            }).setTitle("举报内容问题").setTitleColor(com.healthy.library.R.color.color_444444)
            .setTitleSize(15)
            .setCancelable(true, true).show()
        warndialog?.setOnDismissListener { warndialog = null }
    }
}