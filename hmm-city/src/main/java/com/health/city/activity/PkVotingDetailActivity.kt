package com.health.city.activity

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_ShapeView.view.ShapeTextView
import com.health.city.R
import com.health.city.adapter.PkVotingDetailCommentAdapter
import com.health.city.contract.PkVoteContract
import com.health.city.contract.PostDetailContract
import com.health.city.model.UserInfoCityModel
import com.health.city.presenter.PkVotePresenter
import com.health.city.presenter.PostDetailPresenter
import com.health.city.widget.DiscussDialog
import com.healthy.library.base.BaseActivity
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.contract.DataStatisticsContract
import com.healthy.library.dialog.PkVotingDialog
import com.healthy.library.dialog.PkVotingTipDialog
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.*
import com.healthy.library.presenter.DataStatisticsPresenter
import com.healthy.library.routes.CityRoutes
import com.healthy.library.utils.DateUtils
import com.healthy.library.utils.ParseUtils
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.StatusLayout
import com.healthy.library.widget.TopBar
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.hss01248.dialog.interfaces.MyItemDialogListener
import com.hyb.library.KeyboardUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import java.util.*

/**
 * author long
 * desc PK???????????????
 * time 2021/11/09
 */
@Route(path = CityRoutes.CITY_PK_VOTING_DETAIL)
class PkVotingDetailActivity : BaseActivity(), IsFitsSystemWindows, PostDetailContract.View,
    OnRefreshLoadMoreListener, DiscussDialog.OnScaleDialogClickListener,
    DiscussDialog.OnDiscussDialogClickListener,
    DiscussDialog.OnDiscussDialogDismissListener,
    PkVotingDetailCommentAdapter.OnReViewClickListener, PkVoteContract.View,
    DataStatisticsContract.View {

    @Autowired
    @JvmField
    var id: String = ""//??????id

    @Autowired
    @JvmField
    var merchantId: String = ""//??????Id

    @Autowired
    @JvmField
    var shopId: String = ""//??????Id

    @Autowired
    @JvmField
    var isShowDiscuss: Boolean = false

    private var mTopBar: TopBar? = null
    private var mRefreshLayout: SmartRefreshLayout? = null
    private var mStatusLayout: StatusLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var mIvEmoji: ImageView? = null
    private var mTvEdit: TextView? = null
    private var mSendAction: TextView? = null

    private var isfirst = true
    private var nowmanname: String = ""
    private var activitytype = "??????"
    private var warntype = "??????"
    private var warnid = ""
    private var followNormal: Drawable? = null
    private var followSelect: Drawable? = null
    private var followNormal2: Drawable? = null
    private var followSelect2: Drawable? = null
    private var reviewandwarndialog: Dialog? = null
    private var warndialog: Dialog? = null
    private var reviewdialog: DiscussDialog? = null

    private var mParams = mutableMapOf<String, Any>()
    private var mPresenter: PostDetailPresenter? = null
    private var mPkVotePresenter: PkVotePresenter? = null
    private var mDataStatisticsPresenter: DataStatisticsPresenter? = null
    private var mCommentAdapter: PkVotingDetailCommentAdapter? = null
    private var mPost: PostDetail? = null
    private var mUserInfoCityModel: UserInfoCityModel? = null
    private var mCurrentPage = 1
    private var mReplyCurrentPage = 1
    private var mCurrentForSize = 0

    private var countDownTimer: CountDownTimer? = null
    private var mDrawable: Drawable? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_pk_voting_detail
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        followNormal = mContext.resources.getDrawable(R.drawable.cityrightlike)
        followSelect = mContext.resources.getDrawable(R.drawable.cityrightliker)
        followNormal2 = mContext.resources.getDrawable(R.drawable.ic_star_tofollow)
        followSelect2 = mContext.resources.getDrawable(R.drawable.ic_star_isfollow)
        mDrawable = mContext.resources.getDrawable(R.drawable.shape_pk_time_style)
        mPresenter = PostDetailPresenter(merchantId, mContext, this)
        mPkVotePresenter = PkVotePresenter(merchantId, mContext, this)
        mDataStatisticsPresenter = DataStatisticsPresenter(mContext, this)
        mPresenter?.getMine()
        getData()
        mDataStatisticsPresenter!!.shareAndLook(
            SimpleHashMapBuilder<String, Any>().puts("sourceId", id)!!
                .puts("sourceType", 2)!!.puts("type", 2) as MutableMap<String, Any>?
        )
    }

    override fun findViews() {
        super.findViews()
        mTopBar = findViewById(R.id.top_bar)
        mRefreshLayout = findViewById(R.id.layout_refresh)
        mStatusLayout = findViewById(R.id.layout_status)
        mRecyclerView = findViewById(R.id.recyclerView)
        mIvEmoji = findViewById(R.id.iv_emoji)
        mTvEdit = findViewById(R.id.tv_edit)
        mSendAction = findViewById(R.id.tv_sendAction)
        setStatusLayout(mStatusLayout)

        initListener()
        buildRecyclerHolder()
    }

    private fun initListener() {
        mTopBar?.setSubmitListener {
            if (mPost == null || mPost?.pkActivity == null) {
                return@setSubmitListener
            }
            mDataStatisticsPresenter!!.shareAndLook(
                SimpleHashMapBuilder<String, Any>().puts("sourceId", id)!!
                    .puts("sourceType", 2)!!.puts("type", 1) as MutableMap<String, Any>?
            )
            PkVotingDialog
                .newInstance()
                .setMerchantShopId(merchantId, shopId)
                .setActivityObj(mPost!!)
                .show(supportFragmentManager, "pkVotingShareDialog")
        }
        mRefreshLayout?.setOnRefreshLoadMoreListener(this)

        mIvEmoji?.setOnClickListener {}

        mTvEdit?.setOnClickListener {
            mParams.clear()
            mParams["postingId"] = id
            if (mUserInfoCityModel != null) {
                mParams["memberState"] =
                    if (mUserInfoCityModel?.dateContent.isNullOrEmpty()) "" else mUserInfoCityModel?.dateContent!!
            }
            nowmanname = "????????????"
            showReviewDialog("??????")
        }

        mSendAction?.setOnClickListener {
            showReviewDialog("??????")
        }
    }

    private fun buildRecyclerHolder() {
        mCommentAdapter = PkVotingDetailCommentAdapter()
        mRecyclerView?.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = mCommentAdapter
        }
        mCommentAdapter?.setOnReViewClickListener(this)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mCurrentPage = 1
        mReplyCurrentPage = 1
        mCurrentForSize = 0
        mPost = null
        countDownTimer?.let { timer ->
            timer.cancel()
        }
        getData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mCurrentPage++
        mCurrentForSize = 0
        getData()
    }

    override fun getData() {
        super.getData()
        mParams.clear()
        if (mPost == null) {
            mParams["id"] = id;
            mPresenter?.getPostDetail(mParams)
        } else {
            mReplyCurrentPage = 1
            mParams["postingId"] = id
            mParams["currentPage"] = mCurrentPage.toString()
            mPkVotePresenter?.getCommentList(mParams)
        }
    }

    /**
     * ??????????????????????????????
     */
    private fun getCommentReplyList(
        discuss: Discuss,
        size: Int
    ) {
        mParams.clear()
        mParams["currentPage"] = mReplyCurrentPage.toString()
        mParams["pageSize"] = "3"
        mParams["postingDiscussId"] = discuss.id
        mPkVotePresenter?.getCommentReply(mParams, discuss, size)
    }

    fun outClick(function: String?, obj: Any?) {
        when (function) {
            "commentLike" -> {
                if (mPost?.postingStatus == 1) {
                    return
                }
                val mClickDiscuss = obj as Discuss
                val map: MutableMap<String, Any> = HashMap()
                map["postingDiscussId"] = mClickDiscuss.id
                map["type"] = if (mClickDiscuss.praiseState == 0) "0" else "1"
                mPresenter?.likeChild(map)
            }
            "voteStand" -> {
                //obj = 1 (??????????????? / 2 ???????????????)
                val voteStand = obj.toString()
                val pkActivity = mPost?.pkActivity
                val mActivityEndTime = DateUtils.str2Long(
                    pkActivity?.activityEndTime,
                    DateUtils.DATE_FORMAT_PATTERN_YMD_HMS
                )
                if (mActivityEndTime < System.currentTimeMillis() || "1" != pkActivity?.status) {
                    showTipDialog("???????????????", "???????????????????????????????????????~")
                    return
                }
                if (pkActivity.currentUserTodayVoteNum >= pkActivity.onePeopleOneDayNum) {
                    showTipDialog("?????????????????????", "????????????????????????${pkActivity.onePeopleOneDayNum}??????~~")
                    return
                }
                if (!pkActivity.canCrossVote &&
                    (pkActivity.currentUserLastVoteStand.isNullOrEmpty().not()
                            && pkActivity.currentUserLastVoteStand != voteStand)
                ) {
                    //??????????????????????????????
                    showTipDialog("???????????????~", "????????????????????????????????????~~")
                    return
                }
                mParams.clear()
                mParams["pkActivityId"] = mPost?.pkActivity?.id!!
                mParams["voteStand"] = voteStand
                mPkVotePresenter?.addPkVote(mParams)
            }
        }
    }

    /**
     * ??????????????????
     */
    private fun addHeaderView() {
        var holder: View? = null
        if (mCommentAdapter?.headerLayout == null) {
            holder = LayoutInflater.from(mContext)
                .inflate(R.layout.item_pk_voting_detail_header_layout, null)
            mCommentAdapter?.let { it.addHeaderView(holder!!) }
        } else {
            holder = mCommentAdapter?.headerLayout?.rootView
        }
        val mPkVotingTitle = holder!!.findViewById<TextView>(R.id.tv_pkVoting_title)
        val mPkVotingContent = holder.findViewById<TextView>(R.id.tipContent)
        val mPkVotingPopularity = holder.findViewById<TextView>(R.id.pkVoting_popularity)
        val mFlProgress = holder.findViewById<FrameLayout>(R.id.fl_progress)
        val mTipSquare = holder.findViewById<TextView>(R.id.tipSquare)
        val mTipConSide = holder.findViewById<TextView>(R.id.tipConSide)
        val mAndroidTabs = holder.findViewById<RelativeLayout>(R.id.tabs)
        val mIvSquare = holder.findViewById<ImageView>(R.id.iv_pkVoting_square)
        val mIvConSide = holder.findViewById<ImageView>(R.id.iv_pkVoting_conSide)
        val mIvPkVotingTextVs = holder.findViewById<ImageView>(R.id.iv_pkVoting_text_vs)
        val mPkVotingTitleLayout =
            holder.findViewById<RelativeLayout>(R.id.pkVoting_title_layout)
        val mSquareName = holder.findViewById<TextView>(R.id.tv_pkVoting_square_name)
        val mConSideName = holder.findViewById<TextView>(R.id.tv_pkVoting_conSide_name)
        val mSquareProgress = holder.findViewById<ProgressBar>(R.id.tipSquareProgress)
        val mConSideProgress = holder.findViewById<ProgressBar>(R.id.tipConSideProgress)
        val mSquareProgressName = holder.findViewById<TextView>(R.id.tipSquareProgressName)
        val mConSideProgressName = holder.findViewById<TextView>(R.id.tipConSideProgressName)
        val mSquareAction = holder.findViewById<ShapeTextView>(R.id.pkVoting_SupportSquare)
        val mConSideAction = holder.findViewById<ShapeTextView>(R.id.pkVoting_SupportConSide)
        val mPkVotingTime = holder.findViewById<ShapeTextView>(R.id.pkVoting_time)
        val mTimeTitle = holder.findViewById<TextView>(R.id.pkVoting_time_title)

        mPost?.apply {
            mPkVotingTitle.text = postingTitle
            mPkVotingContent.text = postingRichContent
            pkActivity?.let {
                mPkVotingPopularity.text = "${
                    ParseUtils.parseNumber(
                        it.initialPopularity,
                        10000, "???"
                    )
                }??????"
                mSquareName.text = it.squareTitle
                mConSideName.text = it.conSideTitle
                //?????? ?????????????????????????????????(2021-12-10)
                mFlProgress.visibility =
                    if (!TextUtils.isEmpty(it.currentUserLastVoteStand) || it.status != "1") View.VISIBLE else View.GONE
                mTipSquare.visibility =
                    if ("1" == it.currentUserLastVoteStand) View.VISIBLE else View.INVISIBLE
                mTipConSide.visibility =
                    if ("2" == it.currentUserLastVoteStand) View.VISIBLE else View.INVISIBLE
                mAndroidTabs.visibility =
                    if (it.squareImgUrl.isNullOrEmpty().not()
                        && it.conSideImgUrl.isNullOrEmpty().not()
                    ) View.VISIBLE else View.GONE
                mIvPkVotingTextVs.visibility =
                    if (mAndroidTabs.visibility == View.GONE) View.VISIBLE else View.GONE
                mPkVotingTime.typeface = Typeface.DEFAULT_BOLD
                mPkVotingTime.setTextColor(Color.parseColor("#333333"))
                mTimeTitle.text = "?????????"
                val mOnClickListener = View.OnClickListener { clickView ->
                    when (clickView.id) {
                        R.id.pkVoting_SupportSquare -> {
                            //????????????
                            outClick("voteStand", 1)

                        }
                        R.id.pkVoting_SupportConSide -> {
                            //????????????
                            outClick("voteStand", 2)
                        }
                    }
                }
                mSquareAction.setOnClickListener(mOnClickListener)
                mConSideAction.setOnClickListener(mOnClickListener)

                val mActivityEndTime = DateUtils.str2Long(
                    it.activityEndTime,
                    DateUtils.DATE_FORMAT_PATTERN_YMD_HMS
                )
                countDownTimer?.let { timer ->
                    timer.cancel()
                }
                val time = (mActivityEndTime - System.currentTimeMillis()) / 1000 //????????????
                countDownTimer = object : CountDownTimer(time * 1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        var millisUntilFinished = millisUntilFinished
                        millisUntilFinished /= 1000
                        val day = millisUntilFinished.toInt() / (24 * 60 * 60)
                        val hours = (millisUntilFinished.toInt() / (60 * 60) - day * 24)
                        val minutes =
                            millisUntilFinished.toInt() / 60 - day * 24 * 60 - hours * 60
                        val seconds =
                            millisUntilFinished.toInt() - day * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60
                        val mTimeText = if (day > 0) {
                            (String.format("%02d", Math.max(0, day)) + ":"
                                    + String.format("%02d", Math.max(0, hours)) + ":"
                                    + String.format("%02d", Math.max(0, minutes)) + ":"
                                    + String.format("%02d", Math.max(0, seconds)))
                        } else {
                            (String.format("%02d", Math.max(0, hours)) + ":"
                                    + String.format("%02d", Math.max(0, minutes)) + ":"
                                    + String.format("%02d", Math.max(0, seconds)))
                        }
                        mPkVotingTime.text = mTimeText
                    }

                    override fun onFinish() {
                        //???????????????????????????????????????????????????
                        mPkVotingTime.apply {
                            text = "???????????????"
                            textSize = 12f
                            setTextColor(Color.parseColor("#FA3C5A"))
                            typeface = Typeface.DEFAULT
                            background = mDrawable
                        }
                        mTimeTitle.text = ""
                    }
                }
                if ("1" == it.status && mActivityEndTime > System.currentTimeMillis()) {
                    if (time > 0) {
                        if (countDownTimer != null) {
                            countDownTimer?.cancel()
                        }
                        countDownTimer?.start()
                    } else {
                        countDownTimer?.cancel()
                    }
                } else {
                    countDownTimer?.cancel()
                    mPkVotingTime.apply {
                        text = "???????????????"
                        textSize = 12f
                        setTextColor(Color.parseColor("#FA3C5A"))
                        typeface = Typeface.DEFAULT
                        background = mDrawable
                    }
                    mTimeTitle.text = ""
                }

                if (it.currentUserTodayVoteNum >= it.onePeopleOneDayNum || "1" != it.status) {
                    mSquareAction.shapeDrawableBuilder
                        .setGradientColors(Color.parseColor("#EBEBEB"), Color.parseColor("#E2E2E2"))
                        .intoBackground()

                    mConSideAction.shapeDrawableBuilder
                        .setGradientColors(Color.parseColor("#EBEBEB"), Color.parseColor("#E2E2E2"))
                        .intoBackground()
                } else {
                    mSquareAction.shapeDrawableBuilder
                        .setGradientColors(Color.parseColor("#FF6060"), Color.parseColor("#FF256C"))
                        .intoBackground()

                    mConSideAction.shapeDrawableBuilder
                        .setGradientColors(Color.parseColor("#2EAFFF"), Color.parseColor("#227FFF"))
                        .intoBackground()
                }
                mSquareProgress.progress = it.squarePercentage
                mConSideProgress.progress = it.conSidePercentage
                mSquareProgressName.text = "${it.squarePercentage}%"
                mConSideProgressName.text = "${it.conSidePercentage}%"

                mSquareName.post {
                    if (mSquareName.lineCount < 2) {
                        mSquareName.gravity = Gravity.CENTER
                        mSquareName.setPadding(
                            mSquareName.paddingLeft,
                            0,
                            mSquareName.paddingRight,
                            0
                        )
                    } else {
                        mSquareName.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                        mSquareName.setPadding(
                            mSquareName.paddingLeft,
                            TransformUtil.dp2px(mContext, 12f).toInt(),
                            mSquareName.paddingRight,
                            TransformUtil.dp2px(mContext, 18f).toInt()
                        )
                    }
                    if (mConSideName.lineCount < 2) {
                        mConSideName.gravity = Gravity.CENTER
                        mConSideName.setPadding(
                            mConSideName.paddingLeft,
                            0,
                            mConSideName.paddingRight,
                            0
                        )
                    } else {
                        mConSideName.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                        mConSideName.setPadding(
                            mConSideName.paddingLeft,
                            TransformUtil.dp2px(mContext, 12f).toInt(),
                            mConSideName.paddingRight,
                            TransformUtil.dp2px(mContext, 18f).toInt()
                        )
                    }
                }
                val mShowImg = it.squareImgUrl.isNullOrEmpty() || it.conSideImgUrl.isNullOrEmpty()
                val layoutParams = mPkVotingTitleLayout.layoutParams as RelativeLayout.LayoutParams
                val mProgressLayoutParams =
                    mFlProgress.layoutParams as RelativeLayout.LayoutParams
                if (!mShowImg) {
                    layoutParams.topMargin = 0
                    GlideCopy.with(mContext)
                        .load(it.squareImgUrl)
                        .error(R.drawable.img_1_1_default)
                        .placeholder(R.drawable.img_1_1_default)
                        .into(mIvSquare)
                    GlideCopy.with(mContext)
                        .load(it.conSideImgUrl)
                        .error(R.drawable.img_1_1_default)
                        .placeholder(R.drawable.img_1_1_default)
                        .into(mIvConSide)
                    mProgressLayoutParams.topMargin = TransformUtil.dp2px(mContext,6f).toInt()
                } else {
                    var topMargin = TransformUtil.dp2px(mContext, 18f).toInt()
                    if (!TextUtils.isEmpty(it.currentUserLastVoteStand)) {
                        //????????????????????????/?????????????????????
                        topMargin = 0
                    }
                    layoutParams.topMargin = topMargin
                    mProgressLayoutParams.topMargin = TransformUtil.dp2px(mContext, 18f).toInt()
                }
                mPkVotingTitleLayout.layoutParams = layoutParams
            }
        }
    }

    override fun onRequestFinish() {
        super.onRequestFinish()
        mRefreshLayout?.finishRefresh()
    }

    /**
     * ???????????? -> ????????????
     */
    override fun onSuccessGetPostDetail(post: PostDetail?) {
        this.mPost = post;
        if (mPost == null) {
            showToast("???????????????")
            finish()
            return
        }
        mPost?.pkActivity?.let {
            mCommentAdapter?.setPkTitle(it.squareTitle, it.conSideTitle)
        }
        addHeaderView()
        getData()
    }

    /**
     * ???????????? -> ????????????
     */
    override fun onSuccessGetDiscuss(
        discusses: MutableList<Discuss>?,
        pageInfoEarly: PageInfoEarly?
    ) {
    }

    /**
     * ???????????? -> ??????????????????
     */
    override fun onSuccessGetMine(userInfoCityModel: UserInfoCityModel?) {
        this.mUserInfoCityModel = userInfoCityModel
        if (isfirst) {
            mParams.clear()
            mParams["postingId"] = id
            userInfoCityModel?.let {
                mParams["memberState"] =
                    if (it.dateContent.isNullOrEmpty()) "" else it.dateContent
            }
            nowmanname = "????????????"
            if (isShowDiscuss) {
                showReviewDialog("??????")
            }
            isfirst = false
        }
    }

    /**
     * ???????????? -> ????????????
     */
    override fun onSuccessDelete() {
        finish()
    }

    /**
     * ???????????? -> ??????/?????? ??????
     */
    override fun onSuccessAdd() {
        mCurrentPage = 1
        if (2 == mPkVotePresenter?.mNetWorkCode) {
            //code == 2 ???????????????????????????
            showTipDialog(
                "+${mPost?.pkActivity?.discussScore}??????",
                "??????????????????????????????${mPost?.pkActivity?.discussScore}???~~"
            )
            //??????????????????
            mPost = null
        }
        mCurrentForSize = 0
        getData()
    }

    /**
     * ???????????? ->
     */
    override fun onSuccessLike() {

    }

    /**
     * ???????????? ->
     */
    override fun onSuccessFan() {
    }

    /**
     * ???????????? -> PK????????????
     */
    override fun onAddPkVoteSuccess() {
        showTipDialog(
            "+${mPost?.pkActivity?.voteScore}??????",
            "?????????????????????"
        )
        //??????????????????
        mPost = null
        mCurrentForSize = 0
        mCurrentPage = 1
        getData()
    }

    /**
     * ???????????? -> ????????????
     */
    override fun onGetCommentListSuccess(commentList: MutableList<Discuss>, isMore: Boolean) {
        if (isMore) {
            mRefreshLayout?.resetNoMoreData()
            mRefreshLayout?.finishLoadMore()
        } else {
            mRefreshLayout?.finishLoadMoreWithNoMoreData()
        }

        //mTitleAdapter?.model = mTitleModel
        if (mCurrentPage == 1) {
            mCommentAdapter?.setNewData(commentList)
        } else {
            mCommentAdapter?.addData(commentList)
        }

        if (ListUtil.isEmpty(mCommentAdapter?.data)) {
            mCommentAdapter?.data?.add(Discuss())
        } else {
            for (discuss in commentList) {
                getCommentReplyList(discuss, commentList.size)
            }
        }
    }

    /**
     * ???????????? ->??????????????????
     */
    override fun onGetCommentReplyListSuccess(forSize: Int) {
        mCurrentForSize++
        if (mCurrentForSize == forSize) {
            mCommentAdapter?.let {
                it.isRemoveViews = true
                it.notifyDataSetChanged()
            }
        }
    }

    /**
     * ????????????
     */
    private fun showTipDialog(tipTitle: String, tipMessage: String) {
        PkVotingTipDialog.newInstance()
            .setBody(tipTitle, tipMessage)
            .show(supportFragmentManager, "pkVotingTipDialog")
    }

    override fun onScaleClick(view: View?, map: MutableMap<String, Any>?) {
        if (mPost?.postingStatus == 1) {
            return
        }
        if (reviewdialog != null) {
            if ("??????" == activitytype) {
                ARouter.getInstance()
                    .build(CityRoutes.CITY_ADDDIS)
                    .withString("activityType", activitytype)
                    .withString("postingDiscussId", mParams["postingDiscussId"].toString())
                    .withString("toMemberId", mParams["toMemberId"].toString())
                    .withString("toMemberType", mParams["toMemberType"].toString())
                    .withString("fatherId", mParams["fatherId"].toString())
                    .navigation(this, 1000)
            } else {
                val postcard = ARouter.getInstance()
                    .build(CityRoutes.CITY_ADDDIS)
                try {
                    postcard.withString("activityType", activitytype)
                    postcard.withString("postingId", mParams["postingId"].toString())
                    postcard.withString("memberState", mParams["memberState"].toString())
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                postcard.navigation(this, 1000)
            }
        }
    }

    override fun onDiscussClick(view: View?, map: MutableMap<String, Any>?) {
        if (mPost?.postingStatus == 1) {
            return
        }
        mParams["postingId"] = id
        mUserInfoCityModel?.let {
            mParams["memberState"] =
                if (it.dateContent.isNullOrEmpty()) "" else it.dateContent
        }
        if ("??????" == activitytype) {
            mParams["status"] = "0"
            mParams["content"] = map!!["content"].toString()
            mPresenter?.addReview(mParams)
        } else {
            mParams["status"] = "0"
            mParams["content"] = map!!["content"].toString()
            mPkVotePresenter?.addDiscuss(mParams)
        }
    }

    override fun onDiscussDiss(result: String?) {
        mTvEdit?.postDelayed(KeyHideRunnable, 100)
        if (TextUtils.isEmpty(result)) {
            mTvEdit?.text = "?????????????????????????????????"
        } else {
            mTvEdit?.text = result
        }
    }

    private var KeyHideRunnable = Runnable { KeyboardUtils.hideSoftInput(mTvEdit) }

    /**
     * ????????????
     */
    fun showReviewWarnDialog(activitytype: String) {
        val strings: MutableList<String> = ArrayList()
        val stringsColors: MutableList<Int> = ArrayList()
        strings.add("??????")
        stringsColors.add(Color.parseColor("#29BDA9"))
        if (mPost?.memberId != null && mPost?.memberId == String(
                Base64.decode(
                    SpUtils.getValue(
                        mContext,
                        SpKey.USER_ID
                    ).toByteArray(), Base64.DEFAULT
                )
            ) && "??????" != activitytype
        ) {
            strings.add("??????")
            stringsColors.add(Color.parseColor("#444444"))
        } else {
            stringsColors.add(Color.parseColor("#444444"))
            strings.add("??????")
        }
        StyledDialog.init(this)
        reviewandwarndialog = StyledDialog.buildBottomItemDialog(
            strings,
            stringsColors,
            "??????",
            object : MyItemDialogListener() {
                override fun onItemClick(text: CharSequence, position: Int) {
                    if ("??????" == text.toString()) {
                        showReviewDialog(activitytype)
                    }
                    if ("??????" == text.toString()) {
                        showWarnDialog()
                    }
                    if ("??????" == text.toString()) {
                        showDeleteDialog()
                    }
                }

                override fun onBottomBtnClick() {}
            }).setCancelable(true, true).show()
        reviewandwarndialog?.setOnDismissListener { reviewandwarndialog = null }
    }

    /**
     * ??????????????????
     */
    private fun showDeleteDialog() {
        StyledDialog.init(this)
        StyledDialog.buildIosAlert("", "?????????????????????????", object : MyDialogListener() {
            override fun onFirst() {}
            override fun onSecond() {
                val map: MutableMap<String, Any> = HashMap()
                map["id"] = id
                map["postingStatus"] = "2"
                mPresenter?.delete(map)
            }
        }).setCancelable(true, true)
            .setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0)
            .setBtnText("??????", "??????").show()
    }

    /**
     * ????????????
     */
    fun showWarnDialog() {
        val stringsColors: MutableList<Int> = ArrayList()
        val strings: MutableList<String> = ArrayList()
        //        strings.add("??????????????????");
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
        StyledDialog.init(this)
        warndialog = StyledDialog.buildBottomItemDialog(
            strings,
            stringsColors,
            "??????",
            object : MyItemDialogListener() {
                override fun onItemClick(text: CharSequence, position: Int) {
                    val map: MutableMap<String, Any> = HashMap()
                    if ("??????" == warntype) {
                        map["type"] = "1"
                        map["sourceId"] = warnid
                    } else if ("??????" == warntype) {
                        map["type"] = "2"
                        map["sourceId"] = warnid
                    } else {
                        map["type"] = "3"
                        map["sourceId"] = warnid
                    }
                    map["reason"] = text.toString()
                    mPresenter?.warn(map)
                }

                override fun onBottomBtnClick() {}
            }).setTitle("??????????????????").setTitleColor(R.color.dialogTitleColor).setTitleSize(15)
            .setCancelable(true, true).show()
        warndialog?.setOnDismissListener(DialogInterface.OnDismissListener {
            warndialog = null
        })
    }

    /**
     * ?????????????????????
     *
     * @param type
     */
    fun showReviewDialog(type: String) {
        reviewdialog = DiscussDialog(this, R.style.customdialogstyle)
        reviewdialog?.setHiht(nowmanname)
        activitytype = type
        reviewdialog?.apply {
            val trim = mTvEdit?.text.toString().trim()
            this.setEditText(if ("?????????????????????????????????" == trim) "" else trim)
            this.setOnScaleDialogClickListener(this@PkVotingDetailActivity)
            this.setOnDiscussDialogClickListener(this@PkVotingDetailActivity)
            this.setOnDiscussDialogDismissListener(this@PkVotingDetailActivity)
            this.show()
        }
    }

    /**
     * ??????????????????
     */
    override fun onClick(
        view: View,
        commentDiscussId: String,
        toMemberId: String,
        fatherId: String,
        fromName: String,
        toMemberType: String
    ) {
        if (mPost == null || mPost?.postingStatus == 1 || TextUtils.isEmpty(toMemberId)) {
            return
        }
        var toMemberId = toMemberId
        if (TextUtils.isEmpty(toMemberId)) {
            toMemberId = ""
        }
        mParams.clear()
        mParams["postingDiscussId"] = commentDiscussId
        mParams["toMemberId"] = toMemberId
        mParams["toMemberType"] = toMemberType
        mParams["fatherId"] = fatherId
        this.nowmanname = fromName.replace("??????", "??????")
        this.warntype = fromName.split(":".toRegex()).toTypedArray()[0]
        this.warnid = if ("??????" == warntype) fatherId else commentDiscussId
        showReviewWarnDialog("??????")
    }

    override fun onCollapsed(discuss: Discuss, position: Int) {
        //????????????
        discuss.apply {
            if (discuss.isMore) {
                mCurrentForSize = 0
                this.currentPage += 1
                mReplyCurrentPage = this.currentPage
                getCommentReplyList(this, 1)
            } else {
                if (discuss.isShow) {
                    openCount = 0
                    //discuss.isShow = true
                }
                mCommentAdapter?.isRemoveViews = true
                mCommentAdapter?.notifyItemChanged(position)
            }
        }
    }
}