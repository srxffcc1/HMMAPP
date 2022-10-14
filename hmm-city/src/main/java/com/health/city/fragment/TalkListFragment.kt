package com.health.city.fragment

import android.os.Bundle
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.city.R
import com.health.city.adapter.*
import com.health.city.contract.RankingListContract
import com.health.city.model.TalentList
import com.health.city.presenter.RankingListPresenter
import com.healthy.library.base.BaseFragment
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.model.PageInfoEarly
import com.healthy.library.model.Topic
import com.healthy.library.utils.SpUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_talent_list.*

class TalkListFragment : BaseFragment(), OnRefreshLoadMoreListener, RankingListContract.View {
    private var param1: String? = null
    private var param2: String? = null
    private var talkListHeaderAdapter: TalkListHeaderAdapter? = null
    private var talkListFooterAdapter: TalkListFooterAdapter? = null
    private var rankingEmptyAdapter: RankingEmptyAdapter? = null
    private var virtualLayoutManager: VirtualLayoutManager? = null
    private var delegateAdapter: DelegateAdapter? = null
    private var mMap = mutableMapOf<String, Any?>()
    private var currentPage = 1

    private var rankingListPresenter: RankingListPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("param1")
            param2 = it.getString("param2")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_talk_list
    }

    override fun findViews() {
        virtualLayoutManager = VirtualLayoutManager(mContext)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recyclerview.layoutManager = virtualLayoutManager
        recyclerview.adapter = delegateAdapter

        talkListHeaderAdapter = TalkListHeaderAdapter()
        delegateAdapter!!.addAdapter(talkListHeaderAdapter)

        talkListFooterAdapter = TalkListFooterAdapter()
        delegateAdapter!!.addAdapter(talkListFooterAdapter)

        rankingEmptyAdapter = RankingEmptyAdapter()
        delegateAdapter!!.addAdapter(rankingEmptyAdapter)

        refresh_layout.setEnableRefresh(false)
        refresh_layout.setOnLoadMoreListener(this)

        rankingListPresenter = RankingListPresenter(mContext, this)
        getData()
    }

    override fun getData() {
        super.getData()
        mMap["currentPage"] = currentPage
        mMap["pageSize"] = "15"
        mMap["faceUrlNum"] = "3"
        mMap["merchantId"] = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
        rankingListPresenter!!.getTalkList(mMap)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TalkListFragment().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        currentPage = 1
        getData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        currentPage++
        getData()
    }

    override fun onSuccessGetTalentList(
        list: MutableList<TalentList>?,
        isMore: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun onSuccessGetTalkList(
        list: MutableList<Topic>?,
        isMore: Boolean
    ) {
        if (isMore) {
            refresh_layout?.resetNoMoreData()
            refresh_layout?.finishLoadMore()
        }else{
            refresh_layout?.setNoMoreData(true)
            refresh_layout?.finishLoadMoreWithNoMoreData()
        }
        if (currentPage == 1) {
            if (list == null || list!!.size < 3) {
                list!!.addAll(getNullList(list))
            }
            talkListHeaderAdapter!!.clear()
            talkListFooterAdapter!!.clear()
            rankingEmptyAdapter!!.clear()
            if (ListUtil.isEmpty(list)) {
                rankingEmptyAdapter!!.model = "null"
            } else {
                if (list!!.size >= 3) {
                    val data = mutableListOf<Any?>()
                    data.add(getList(list.subList(0, 3)))
                    talkListHeaderAdapter!!.setData(data as ArrayList<ArrayList<Topic>>)
                    if (list!!.size == 3) {
                        talkListFooterAdapter!!.clear()
                        rankingEmptyAdapter!!.model = "null"
                    } else {
                        talkListFooterAdapter!!.setData(getList(list.subList(3, list.size)))
                    }
                } else {
                    val data = mutableListOf<Any?>()
                    data.add(getList(list.subList(0, list.size)))
                    talkListHeaderAdapter!!.setData(data as ArrayList<ArrayList<Topic>>)
                }
            }
        } else {
            talkListFooterAdapter!!.addDatas(list as ArrayList<Topic>)
            talkListFooterAdapter!!.notifyDataSetChanged()
        }
    }

    private fun getNullList(list: List<Topic>): ArrayList<Topic> {
        val mArrayList = ArrayList<Topic>()
        when (list.size) {
            0 -> {
                mArrayList.add(Topic(null, "快去新增属于你的热门话题～", "0", "0", null))
                mArrayList.add(Topic(null, "快去新增属于你的热门话题～", "0", "0", null))
                mArrayList.add(Topic(null, "快去新增属于你的热门话题～", "0", "0", null))
            }
            1 -> {
                mArrayList.add(Topic(null, "快去新增属于你的热门话题～", "0", "0", null))
                mArrayList.add(Topic(null, "快去新增属于你的热门话题～", "0", "0", null))
            }
            2 -> {
                mArrayList.add(Topic(null, "快去新增属于你的热门话题～", "0", "0", null))
            }
        }
        return mArrayList
    }

    public fun scroll() {
        recyclerview.scrollToPosition(0)
    }

    private fun getList(list: List<Topic>): ArrayList<Topic> {
        val mArrayList = ArrayList<Topic>()
        for ((index, e) in list.withIndex()) {
            mArrayList.add(e)
        }
        return mArrayList
    }
}