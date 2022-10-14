package com.health.mine.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.health.mine.R
import com.health.mine.adapter.LotteryListAdapter
import com.health.mine.contract.LotteryListContract
import com.health.mine.presenter.LotteryListPresenter
import com.healthy.library.base.BaseFragment
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.model.GoodsBasketCell
import com.healthy.library.model.LotteryModel
import com.healthy.library.routes.ServiceRoutes
import com.healthy.library.utils.DateUtils
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_vote_list.*

/**
 * description 中奖纪录列表页面
 * author long
 * date 2021/8/2
 */
class LotteryListFragment : BaseFragment(), LotteryListContract.View {

    companion object {
        fun newInstance(): LotteryListFragment {
            val args = Bundle()
            val fragment = LotteryListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val mRequestMap: MutableMap<String, Any> = mutableMapOf()
    private val mResultList: MutableList<LotteryModel> = mutableListOf()

    /** 领奖配置门店 */
    private val mShopIdList: MutableList<String> = mutableListOf()

    /** 领奖奖品数据 */
    private val mGoodsList: MutableList<GoodsBasketCell> = mutableListOf()
    private lateinit var mListPresenter : LotteryListPresenter
    private lateinit var mLotteryAdapter: LotteryListAdapter
    private var mCurrentPage = 1
    private var isMore: Boolean = false
    private var isRefresh: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.mine_fragment_lottery_list
    }

    override fun findViews() {
        if (arguments == null) {
            return
        }
        setStatusLayout(layout_status)
        mListPresenter = LotteryListPresenter(mContext, this)
        rv.layoutManager = LinearLayoutManager(mContext)
        mLotteryAdapter = LotteryListAdapter()
        rv.adapter = mLotteryAdapter

        initListener()
    }

    private fun initListener() {
        mLotteryAdapter.setOnItemClickListener { adapter, view, position ->
            val lotteryModel = mResultList[position]
            if (lotteryModel.orderId.isNullOrEmpty().not()) {
                ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                    .withString("orderId", lotteryModel.orderId)
                    .withString("function", "25007")
                    .navigation()
            }
        }

        mLotteryAdapter.setOnItemChildClickListener { adapter, view, position ->
            val lotteryModel = mResultList[position]
            when (view.id) {
                R.id.tv_action -> {
                    mShopIdList.clear()
                    mGoodsList.clear()
                    // 领取奖品
                    var mShopId = ""
                    // 商家id
                    var mMerchantId = ""
                    // 领奖截至时间
                    var mRewardEndTime: Long = 0
                    // 活动名称
                    var activityName = ""
                    //商家电话
                    var mMobile = ""

                    lotteryModel.lotteryActivity?.let {
                        mMerchantId = it.merchantId
                        activityName = it.title
                        /** 获取当前活动适用门店Id */
                        it.deliveryProfiles?.let { it ->
                            it.forEach { model ->
                                mShopIdList.add(model.shopId)
                            }
                        }
                        /** 获取当前活动奖品领奖截止时间 */
                        it.restrict?.let { it ->
                            if (it.latestExchangeTime.isNullOrEmpty().not()) {
                                mRewardEndTime = DateUtils.str2Long(
                                    it.latestExchangeTime,
                                    DateUtils.DATE_FORMAT_PATTERN_YMD_HMS
                                )
                            }
                        }
                        /** 获取当前活动商家手机号 */
                        it.merchantInfo?.let { it ->
                            mMobile = it.mobile
                        }
                    }

                    /** 判断是否超出领奖时间 */
                    if (System.currentTimeMillis() > mRewardEndTime) {
                        isAgree("当前活动已过领奖截止日期！")
                        return@setOnItemChildClickListener
                    }

                    /** 判断门店是否异常 */
                    if (ListUtil.isEmpty(mShopIdList)) {
                        isAgree("领奖门店存在异常，请联系活动商家：$mMobile!")
                        return@setOnItemChildClickListener
                    }
                    mShopId = mShopIdList[0]
                    /** 奖品名称 */
                    var prizeName = ""
                    lotteryModel.lotteryPrizeProfile?.let {
                        val availableInventory = it.prizeCurrentNumber
                        val goodsBasketCell = GoodsBasketCell()
                        goodsBasketCell.goodsId = it.goodsId
                        goodsBasketCell.mchId = mMerchantId
                        goodsBasketCell.goodsTitle = it.goodsTitle
                        goodsBasketCell.goodsImage = it.goodsHeadImage
                        goodsBasketCell.goodsStock = availableInventory
                        goodsBasketCell.goodsQuantity = 1
                        goodsBasketCell.goodsShopId = ""
                        goodsBasketCell.shopIdList = mShopIdList.toTypedArray()
                        goodsBasketCell.goodsSpecId = it.goodsChildId
                        goodsBasketCell.goodsSpecDesc =
                            if (it.goodsSpecStr.isNullOrEmpty()) "" else "规格：" + it.goodsSpecStr
                        val goodsMarketing = GoodsBasketCell.GoodsMarketing("")
                        goodsMarketing.marketingType = "-4"
                        goodsMarketing.id = ""
                        goodsMarketing.mapMarketingGoodsId = ""
                        goodsMarketing.availableInventory = availableInventory
                        goodsMarketing.goodsId = it.goodsId.toInt()
                        goodsBasketCell.goodsMarketingDTO = goodsMarketing
                        prizeName = it.prizeName
                        mGoodsList.add(goodsBasketCell)
                    }
                    /** 空数据异常不做任何操作 拦截 */
                    if (ListUtil.isEmpty(mGoodsList)) {
                        return@setOnItemChildClickListener
                    }

                    isRefresh = true
                    ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER)
                        .withString("visitShopId", mShopId)
                        .withObject("goodsbasketlist", mGoodsList)
                        .withString("goodsMarketingType", "-4")
                        .withString("prizeName", prizeName)
                        .withString("activityName", activityName)
                        .withString("activitySignupId", lotteryModel.id)//中奖纪录id
                        .navigation()
                }
            }
        }

        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mCurrentPage = 1
                mResultList.clear()
                getData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mCurrentPage++
                getData()
            }
        })
    }

    override fun onLazyData() {
        super.onLazyData()
        getData()
    }

    override fun getData() {
        isMore = false
        mRequestMap["currentPage"] = mCurrentPage.toString()
        mListPresenter.getLotteryList(mRequestMap)
    }

    override fun getLotteryListSuccess(
        resultData: MutableList<LotteryModel>,
        isMore: Boolean
    ) {
        this.isMore = isMore
        //判断是否还有更多
        if (isMore) {
            refresh_layout.resetNoMoreData()
            refresh_layout.finishLoadMore()
        } else {
            refresh_layout.finishLoadMoreWithNoMoreData()
        }

        /*resultData.forEachIndexed { index, lotteryModel ->
            if (index % 3 == 0) {
                lotteryModel.lotteryActivity?.let {
                    it.title = it.title + "打法嘎哈哈发货发话耗费的合同期阿萨德"
                }
            }
        }*/

        mResultList.addAll(resultData)
        mLotteryAdapter.setNewData(mResultList)

        if (ListUtil.isEmpty(mResultList)) {
            showEmpty()
            refresh_layout.finishLoadMoreWithNoMoreData()
            return
        }
        showContent()
    }

    override fun onRequestFinish() {
        super.onRequestFinish()
        refresh_layout.finishRefresh()
        if (isMore) {
            refresh_layout.finishLoadMore()
        }
        if (ListUtil.isEmpty(mResultList)) {
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