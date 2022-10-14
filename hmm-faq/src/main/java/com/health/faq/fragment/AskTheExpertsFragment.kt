package com.health.faq.fragment

import android.content.Intent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.SingleLayoutHelper
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.health.faq.R
import com.health.faq.activity.ExpertListActivity
import com.health.faq.model.ExpertsHomeModel
import com.health.faq.model.HotExpertInfoDTO
import com.health.faq.model.Item
import com.health.faq.model.RewardQuestion
import com.health.faq.valaroutadapter.*
import com.healthy.library.base.BasePagingFragment
import com.healthy.library.constant.Functions
import com.healthy.library.loadmore.LoadMoreAdapter
import com.healthy.library.loadmore.LoadMoreWrapper
import com.healthy.library.message.AskEndMessage
import com.healthy.library.message.RefreshExpertIndexMsg
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.routes.AppRoutes
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.utils.JsonUtils
import kotlinx.android.synthetic.main.adapter_bottom.*
import kotlinx.android.synthetic.main.fragment_ask_the_experts.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.util.*


/**
 * @author xinkai.xu
 * 问答页面
 * */
@Route(path = AppRoutes.MODULE_EXPERT_FAQ)
class AskTheExpertsFragment : BasePagingFragment(), androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {

    var expertsHomeModel: ExpertsHomeModel? = null
    private var mLoadMoreAdapter: LoadMoreAdapter? = null
    private var askExpertsAdapter: AskExpertsAdapter? = null
    private var rewardForHelpAdapter: RewardForHelpAdapter? = null
    private var popularExpertsLayoutAdapter: PopularExpertsLayoutAdapter? = null
    private var singleLayoutAdapter: SingleLayoutAdapter? = null
    private var raward: MutableList<RewardQuestion> = mutableListOf()//悬赏问答
    private var hotExpert: MutableList<HotExpertInfoDTO> = mutableListOf()//热门专家
    private var items: MutableList<Item> = mutableListOf()//问专家
    private var strings: MutableList<String> = mutableListOf() //图片

    private var TYPE1 = 1
    private var TYPE2 = 2
    private var TYPE3 = 3
    private var TYPE4 = 4
    private var TYPE5 = 5
    private var TYPE6 = 6
    private var TYPE7 = 7
    override fun getLayoutId(): Int {
        return R.layout.fragment_ask_the_experts
    }

    override fun onFirstVisible() {
        super.onFirstVisible()
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this)
        }
        setStatusLayout(layout_status)
        layout_status.setOnNetRetryListener(this)
        srlAskTheExperts!!.setOnRefreshListener(this)

        val layoutManager = VirtualLayoutManager(mContext)
        rvAskTheExperts.layoutManager = layoutManager

        val viewPool = androidx.recyclerview.widget.RecyclerView.RecycledViewPool()
        rvAskTheExperts.setRecycledViewPool(viewPool)
        viewPool.setMaxRecycledViews(0, 20)
        val adapters = ArrayList<DelegateAdapter.Adapter<*>>()
        val linearHelper = LinearLayoutHelper()
        val singHelper = SingleLayoutHelper()
        val singHelper1 = SingleLayoutHelper()
        singHelper.setMargin(15, 0, 15, 0)

        singleLayoutAdapter = SingleLayoutAdapter(mContext, strings!!, singHelper, TYPE1)
        adapters.add(singleLayoutAdapter!!)
        adapters.add(TitleAdapter(activity!!, 1, linearHelper, TYPE2))
        rewardForHelpAdapter = RewardForHelpAdapter(mContext, raward, linearHelper, TYPE3)
        adapters.add(rewardForHelpAdapter!!)
        adapters.add(TitleAdapter(activity!!, 2, linearHelper, TYPE4))
        popularExpertsLayoutAdapter = PopularExpertsLayoutAdapter(mContext, hotExpert, singHelper1, TYPE5)
        adapters.add(popularExpertsLayoutAdapter!!)
        adapters.add(TitleAdapter(activity!!, 3, linearHelper, TYPE6))
        askExpertsAdapter = AskExpertsAdapter(mContext, items, linearHelper, TYPE7)
        adapters.add(askExpertsAdapter!!)
        val mDelegateAdapter = DelegateAdapter(layoutManager)
        mDelegateAdapter.setAdapters(adapters)
        mLoadMoreAdapter = LoadMoreWrapper.with(mDelegateAdapter)
                .setLoadMoreEnabled(false)
                .setListener(this)
                .into(rvAskTheExperts)
        if (!srlAskTheExperts.isRefreshing) {
            srlAskTheExperts.isRefreshing = true
            mLoadMoreAdapter!!.loadMoreEnabled = false
        }
        getAskTheExperts()
        askExpertsAdapter?.onAskListener = object : AskExpertsAdapter.OnAskListener {
            override fun onClickListener(id: String) {
                getExpertStatus(id)
            }
        }
    }

    override fun findViews() {
        tvReward.setOnClickListener {
            ARouter.getInstance().build(FaqRoutes.FAQ_REWARD).navigation()
        }
        ivClose.setOnClickListener {
            clAdapterBottom.visibility = View.GONE
        }
        tvAskExpert.setOnClickListener {
            activity!!.startActivity(Intent(activity, ExpertListActivity::class.java))
        }
    }

    override fun onRefresh() {
        resetPageNum()
        getAskTheExperts()
    }

    override fun onLoadMore() {
        getAskTheExperts()
    }

    override fun getData() {
        showLoading()
        onRefresh()
    }

    /**
     *
     * 获取数据
     * */
    private fun getAskTheExperts() {
        val map = HashMap<String, Any>(3)
        map["function"] = Functions.FUNCTION_GET_EXPERTS_HOME
        map["currentPage"] = currentPageNum.toString()
        map["pageSize"] = pageSize
        ObservableHelper.createObservable(mContext, map)
                .subscribe(object : StringObserver(this, mContext, false,
                        true, true, true, true, false) {


                    override fun onGetResultSuccess(obj: String?) {
                        super.onGetResultSuccess(obj)
                        expertsHomeModel = Gson().fromJson<ExpertsHomeModel>(obj!!)
                        if (currentPageNum == 1) {
                            clAdapterBottom.visibility = View.VISIBLE
                            setPages(expertsHomeModel!!.data.listInfo.totalPage)
                            //清空数据
                            strings.clear()
                            raward.clear()
                            hotExpert.clear()
                            items.clear()


                            //刷新
                            strings.addAll(expertsHomeModel!!.data.rewardQuestionFirstPageDTO.expertPhotoUrls)
                            raward.addAll(expertsHomeModel!!.data.rewardQuestionFirstPageDTO.rewardQuestionList)
                            hotExpert.addAll(expertsHomeModel!!.data.rewardQuestionFirstPageDTO.hotExpertInfoDTOList)
                            items.addAll(expertsHomeModel!!.data.listInfo.items)
                            singleLayoutAdapter!!.notifyDataSetChanged()
                            rewardForHelpAdapter!!.notifyDataSetChanged()
                            popularExpertsLayoutAdapter!!.notifyDataSetChanged()
                            askExpertsAdapter!!.notifyDataSetChanged()
                        } else {
                            items.addAll(expertsHomeModel!!.data.listInfo.items)
                            askExpertsAdapter!!.notifyDataSetChanged()
                        }
                        //设置下一页
                        setNextPageNum()
                        if (resetRefreshing()) {
                            if (!mLoadMoreAdapter!!.loadMoreEnabled) {
                                if (expertsHomeModel!!.data.listInfo.items.size < pageSize) {
                                    return
                                }
                                mLoadMoreAdapter!!.loadMoreEnabled = true

                            }
                        }
                    }
                })

    }

    /**
     * 判断当前刷新状态
     * */
    private fun resetRefreshing(): Boolean {
        if (srlAskTheExperts!!.isRefreshing) {
            srlAskTheExperts!!.isRefreshing = false
            return true
        }
        return false
    }

    /**
     * 获取adapter
     * */
    override fun getLoadMoreAdapter(): LoadMoreAdapter {
        return mLoadMoreAdapter!!
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun messageEventBus(event: AskEndMessage) {
        onRefresh()
    }

    /**
     * 刷新问专家阅读量
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshCountMsg(msg: RefreshExpertIndexMsg) {
        val pos = msg.pos
        askExpertsAdapter?.let {
            it.getData()?.get(pos)?.readCount = msg.count
            it.notifyItemChanged(pos)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    /**
     * 获取专家在线状态
     * @param expertId  专家id
     */
    private fun getExpertStatus(expertId: String) {
        val map = HashMap<String, Any>(2)
        map[Functions.FUNCTION] = Functions.FUNCTION_EXPERT_STATUS
        map["userId"] = expertId
        val observer = object : StringObserver(this, mContext, false,
                false, true, false, false, true) {
            override fun onGetResultSuccess(obj: String?) {
                super.onGetResultSuccess(obj)
                try {
                    val jsonObject = JSONObject(obj)
                    val status = JsonUtils.getInt(jsonObject, "data")
                    if (status == 1) {
                        ARouter.getInstance().build(FaqRoutes.FAQ_ASK_EXPERT)
                                .withString("id", expertId)
                                .navigation()
                    } else {

                        Toast.makeText(mContext, "当前专家不在线", Toast.LENGTH_SHORT).show()
                        ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_HOMEPAGE)
                                .withString("id", expertId)
                                .navigation()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        ObservableHelper
                .createObservable(mContext, map)
                .subscribe(observer)
    }
}
