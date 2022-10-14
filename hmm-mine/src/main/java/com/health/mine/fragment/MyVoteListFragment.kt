package com.health.mine.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.health.mine.R
import com.health.mine.adapter.VoteListAdapter
import com.health.mine.contract.VoteListContract
import com.health.mine.presenter.VoteListPresenter
import com.healthy.library.BuildConfig
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseFragment
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.business.SeckShareDialog
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.constant.UrlKeys
import com.healthy.library.model.ActivityModel
import com.healthy.library.model.GoodsBasketCell
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.routes.ServiceRoutes
import com.healthy.library.utils.DateUtils
import com.healthy.library.utils.LogUtils
import com.healthy.library.utils.SpUtils
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_vote_list.*

/**
 * @description
 * @author long
 * @date 2021/6/21
 */
class MyVoteListFragment : BaseFragment(), VoteListContract.View,
    BaseAdapter.OnOutClickListener {

    companion object {
        fun newInstance(mType: Int): MyVoteListFragment {
            val args = Bundle()
            args.putInt("type", mType)
            val fragment = MyVoteListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mType: Int? = 0
    private val mRequestMap: MutableMap<String, Any> = mutableMapOf()
    private var mCurrentPage = 1
    private var isRefresh: Boolean = false
    private lateinit var mVoteListPresenter: VoteListPresenter
    private lateinit var mVoteAdapter: VoteListAdapter
    private val mPrizeList: MutableList<GoodsBasketCell> = mutableListOf()

    override fun getLayoutId(): Int {
        return R.layout.fragment_vote_list
    }

    override fun findViews() {
        if (arguments?.isEmpty!!) {
            return
        }
        setStatusLayout(layout_status)

        mVoteListPresenter = VoteListPresenter(mContext, this)
        mType = arguments?.getInt("type")
        rv.layoutManager = LinearLayoutManager(mContext)
        mVoteAdapter = VoteListAdapter(mType!!)
        //item 事件回调
        mVoteAdapter.moutClickListener = this
        rv.adapter = mVoteAdapter

        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mCurrentPage = 1
                getData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mCurrentPage++
                getData()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (isRefresh) {
            mCurrentPage = 1
            getData()
            isRefresh = false
        }
    }

    override fun onLazyData() {
        super.onLazyData()
        getData()
    }

    override fun getData() {
        //true 参与投票列表 false 报名的投票
        mRequestMap[Functions.FUNCTION] = if (1 == mType) "act_1005" else "act_1002"
        mRequestMap["currentPage"] = mCurrentPage.toString()
        mVoteListPresenter.getVoteList(mRequestMap)
    }

    override fun outClick(function: String, obj: Any) {
        val item = mVoteAdapter.getDatas()[obj.toString().toInt()]
        when (function) {
            //按钮相关一系列操作
            "action" -> {
                if (1 == mType) {
                    //当前是“参与的投票”界面 只做我要投票
                    val host =
                        if (!TextUtils.isEmpty(SpUtils.getValue(context, UrlKeys.H5_VOTE_LIST_URL)))
                            SpUtils.getValue(context, UrlKeys.H5_VOTE_LIST_URL)
                        else "http://192.168.10.181:8000/voteList.html"
                    val mUrl = "$host?id=${item.id}&token=${SpUtils.getValue(context, SpKey.TOKEN)}"
                    ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEWSINGLE)
                        .withString("url", mUrl)
                        .withString("title", item.name)
                        .navigation()
                    return
                }

                /** 如果冻结状态提示弹框 */
                if (item.status == -1) {
                    isAgree(
                        if (item.freezeReason.isNullOrEmpty()
                                .not()
                        ) item.freezeReason else "投票行为存在异常，已被发起方冻结处理"
                    )
                    return
                }

                /** 领取奖品 */
                var mShopId: String? = "";
                if (item.awardStatus == 1) {
                    mPrizeList.clear()
                    var mRewardEndTime: Long = 0
                    if (!item.activity?.rewardEndTime.isNullOrEmpty()) {
                        mRewardEndTime = DateUtils.str2Long(
                            item.activity?.rewardEndTime,
                            DateUtils.DATE_FORMAT_PATTERN_YMD_HMS
                        )
                    }
                    /** 判断是否超出领奖时间 */
                    if (System.currentTimeMillis() > mRewardEndTime) {
                        isAgree("当前活动已过领奖截止日期！")
                        return
                    }

                    if (ListUtil.isEmpty(item.activity?.shopIdList)) {
                        isAgree("领奖门店存在异常，请联系活动商家：" + item.merchantInfo?.mobile + "!")
                        return
                    }
                    mShopId = item.activity?.shopIdList?.get(0)
                    var prizeName: String = ""
                    var activityName: String = ""

                    /** 循环获取奖品信息 */
                    item.activity?.prizeList?.let {
                        it.forEach { it ->
                            if (item.awardPrizeId == it.id) {
                                var availableInventory = 0
                                it.goodsDto?.let { it ->
                                    availableInventory = it.availableInventory
                                }
                                val goodsBasketCell = GoodsBasketCell()
                                goodsBasketCell.goodsId = it.goodsId
                                goodsBasketCell.mchId = item.activity?.merchantId.toString()
                                goodsBasketCell.goodsTitle =
                                    it.goodsDto?.let { it -> it.goodsTitle }
                                goodsBasketCell.goodsImage =
                                    it.goodsDto?.let { it -> it.goodsImage }
                                goodsBasketCell.goodsStock = availableInventory
                                goodsBasketCell.goodsQuantity = 1
                                goodsBasketCell.goodsShopId = ""
                                goodsBasketCell.shopIdList =
                                    item.activity?.shopIdList?.toTypedArray()
                                goodsBasketCell.goodsSpecId = it.goodsChildId
                                goodsBasketCell.goodsSpecDesc =
                                    it.goodsDto?.let { it -> if (it.goodsSpecStr.isNullOrEmpty()) "" else "规格：" + it.goodsSpecStr }
                                val goodsMarketing = GoodsBasketCell.GoodsMarketing("")
                                goodsMarketing.marketingType = "9"
                                goodsMarketing.id = it.mapMarketingGoodsChildId
                                goodsMarketing.mapMarketingGoodsId = it.mapMarketingGoodsId
                                goodsMarketing.availableInventory = availableInventory
                                goodsMarketing.goodsId = it.goodsId.toInt()
                                goodsBasketCell.goodsMarketingDTO = goodsMarketing
                                prizeName = it.name
                                activityName = item.activity?.name!!
                                mPrizeList.add(goodsBasketCell)
                                return@forEach
                            }
                        }
                    }

                    /** 空数据异常不做任何操作 拦截 */
                    if (ListUtil.isEmpty(mPrizeList)) {
                        return
                    }
                    isRefresh = true
                    ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER)
                        .withString("visitShopId", mShopId)
                        .withObject("goodsbasketlist", mPrizeList)
                        .withString("goodsMarketingType", "9")
                        .withString("prizeName", prizeName)
                        .withString("activityName", activityName)
                        .withString("activitySignupId", item.id)//报名记录id
                        .navigation()
                    return
                }

                /** 活动结束 要在领奖优先级后 */
                if (item.activity?.status == 3) {
                    showToast("活动已结束！")
                    return
                }

                /** 海报拉票分享 */
                item.activity?.voteNo = item.voteNo
                val newInstance = SeckShareDialog.newInstance()
                    .setActivityDialog(4, 0, item.activity)
                if (item.merchantInfo?.merchantLogoUrl.isNullOrEmpty().not()) {
                    newInstance.mShopLogo = item.merchantInfo?.merchantLogoUrl
                }
                newInstance.visitShopId = mShopId
                val extraMap = SimpleHashMapBuilder<String, String>().puts("type", "8")
                    ?.puts("scheme", "VoteDetail")
                newInstance.setExtraMap(extraMap as SimpleHashMapBuilder<String, String>)
                newInstance.show(childFragmentManager, "voteShare")
            }
            //跳转订单详情
            "orderDetail" -> {
                ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                    .withString("orderId", item.awardOrderId)
                    .withString("function", "25007")
                    .navigation()
            }
            //投票详情/列表页面
            "voteDetail" -> {
                val host =
                    if (!TextUtils.isEmpty(SpUtils.getValue(context, UrlKeys.H5_VOTE_LIST_URL)))
                        SpUtils.getValue(context, UrlKeys.H5_VOTE_LIST_URL)
                    else "http://192.168.10.181:8000/voteList.html"
                val mUrl =
                    "$host?id=${item.activity?.id}&token=${SpUtils.getValue(context, SpKey.TOKEN)}"
                item.activity?.voteNo = item.voteNo
                ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEWSINGLE)
                    .withString("url", mUrl)
                    .withString("title", item.activity?.name)
                    .withString("activity", Gson().toJson(item.activity))
                    .withBoolean("isShowShare",true)
                    .navigation()

            }
        }
    }

    /**
     * 参与的投票/报名的投票活动列表回调
     * @param resultData 接口回调数据
     * @param isMore 是否还有更多 true 拥有 false 没有
     */
    override fun getVoteListSuccess(resultData: MutableList<ActivityModel>, isMore: Boolean) {

        //判断是否还有更多
        if (isMore) {
            refresh_layout.resetNoMoreData()
            refresh_layout.finishLoadMore()
        } else {
            refresh_layout.finishLoadMoreWithNoMoreData()
        }

        if (mCurrentPage == 1) {
            mVoteAdapter.clear()
            mVoteAdapter.setData(resultData as ArrayList<ActivityModel>)
        } else {
            mVoteAdapter.addDatas(resultData as ArrayList<ActivityModel>)
        }

        if (ListUtil.isEmpty(mVoteAdapter.getDatas())) {
            showEmpty()
            refresh_layout.finishLoadMoreWithNoMoreData()
            return
        }
        showContent()
    }

    override fun onRequestFinish() {
        super.onRequestFinish()
        refresh_layout.finishRefresh()
        refresh_layout.finishLoadMore()
        if (ListUtil.isEmpty(mVoteAdapter.getDatas())) {
            showEmpty()
        }
    }

    private fun isAgree(msg: String) {
        StyledDialog.init(mContext)
        StyledDialog.buildIosAlert(
            "",
            msg,
            object : MyDialogListener() {
                override fun onFirst() {}
                override fun onThird() {
                    super.onThird()
                }

                override fun onSecond() {

                }
            }).setMsgColor(R.color.color_da1818).setBtnColor(0, R.color.colorPrimaryDark, 0)
            .setBtnText("确定")
            .show()
    }
}