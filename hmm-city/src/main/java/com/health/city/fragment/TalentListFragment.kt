package com.health.city.fragment

import android.os.Bundle
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.city.R
import com.health.city.adapter.RankingEmptyAdapter
import com.health.city.adapter.TalentListFooterAdapter
import com.health.city.adapter.TalentListHeaderAdapter
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
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_talent_list.*

class TalentListFragment : BaseFragment(), OnRefreshLoadMoreListener, RankingListContract.View {
    private var param1: String? = null
    private var param2: String? = null
    private var talentListHeaderAdapter: TalentListHeaderAdapter? = null
    private var talentListFooterAdapter: TalentListFooterAdapter? = null
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
        return R.layout.fragment_talent_list
    }

    override fun findViews() {
        virtualLayoutManager = VirtualLayoutManager(mContext)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        recyclerview.layoutManager = virtualLayoutManager
        recyclerview.adapter = delegateAdapter

        talentListHeaderAdapter = TalentListHeaderAdapter()
        delegateAdapter!!.addAdapter(talentListHeaderAdapter)

        talentListFooterAdapter = TalentListFooterAdapter()
        delegateAdapter!!.addAdapter(talentListFooterAdapter)

        rankingEmptyAdapter = RankingEmptyAdapter()
        delegateAdapter!!.addAdapter(rankingEmptyAdapter)

        refresh_layout.setEnableRefresh(false)
        refresh_layout.setOnLoadMoreListener(this)

        rankingListPresenter = RankingListPresenter(mContext, this)
        rankingEmptyAdapter!!.clear()
        getData()
    }

    override fun getData() {
        super.getData()
        mMap["currentPage"] = currentPage
        mMap["pageSize"] = "10"
        mMap["merchantId"] = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
        rankingListPresenter!!.getTalentList(mMap)
    }

    override fun onSuccessGetTalentList(
        list: MutableList<TalentList>?,
        isMore: Boolean
    ) {
        if (isMore) {
            refresh_layout?.resetNoMoreData()
            refresh_layout?.finishLoadMore()
        } else {
            refresh_layout?.setNoMoreData(true)
            refresh_layout?.finishLoadMoreWithNoMoreData()
        }
        if (currentPage == 1) {
            if (list == null || list!!.size < 3) {
                list!!.addAll(getNullList(list))
            }
            talentListHeaderAdapter!!.clear()
            talentListFooterAdapter!!.clear()
            rankingEmptyAdapter!!.clear()
            if (list!!.size >= 3) {
                val data = mutableListOf<Any?>()
                data.add(getList(list.subList(0, 3)))
                talentListHeaderAdapter!!.setData(data as ArrayList<ArrayList<TalentList>>)
                if (list!!.size == 3) {
                    talentListFooterAdapter!!.clear()
                    rankingEmptyAdapter!!.model = "null"
                } else {
                    talentListFooterAdapter!!.setData(getList(list.subList(3, list.size)))
                }
            } else {
                val data = mutableListOf<Any?>()
                data.add(getList(list.subList(0, list.size)))
                talentListHeaderAdapter!!.setData(data as ArrayList<ArrayList<TalentList>>)
            }

        } else {
            talentListFooterAdapter!!.addDatas(list as ArrayList<TalentList>)
        }
    }

    private fun getList(list: List<TalentList>): ArrayList<TalentList> {
        val mArrayList = ArrayList<TalentList>()
        for ((index, e) in list.withIndex()) {
            mArrayList.add(e)
        }
        return mArrayList
    }

    private fun getNullList(list: List<TalentList>): MutableList<TalentList> {
        val mArrayList = mutableListOf<TalentList>()
        when {
            ListUtil.isEmpty(list) -> {
                mArrayList.add(TalentList("抢沙发~", null, 0, 0, 0, 0, 0.0, null))
                mArrayList.add(TalentList("抢沙发~", null, 0, 0, 0, 0, 0.0, null))
                mArrayList.add(TalentList("抢沙发~", null, 0, 0, 0, 0, 0.0, null))
            }
            list.size == 1 -> {
                mArrayList.add(TalentList("抢沙发~", null, 0, 0, 0, 0, 0.0, null))
                mArrayList.add(TalentList("抢沙发~", null, 0, 0, 0, 0, 0.0, null))
            }
            list.size == 2 -> {
                mArrayList.add(TalentList("抢沙发~", null, 0, 0, 0, 0, 0.0, null))
            }
        }
        return mArrayList
    }

    override fun onSuccessGetTalkList(
        list: MutableList<Topic>?,
        isMore: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        currentPage = 1
        getData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        currentPage++
        getData()
    }

    public fun scroll() {
        recyclerview.scrollToPosition(0)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TalentListFragment().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }

}