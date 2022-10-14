package com.health.mine.activity

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.health.mine.R
import com.health.mine.adapter.EnlistAdapter
import com.health.mine.contract.EnlistContract
import com.health.mine.presenter.EnListPresenter
import com.healthy.library.base.BaseActivity
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.model.ActivityModel
import com.healthy.library.model.EnlistActivityModel
import com.healthy.library.routes.MineRoutes
import com.hyb.library.KeyboardUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_vote_list.*
import kotlinx.android.synthetic.main.mine_activity_enlist.*
import kotlinx.android.synthetic.main.mine_activity_enlist.layout_status
import kotlinx.android.synthetic.main.mine_activity_enlist.refresh_layout
import kotlinx.android.synthetic.main.mine_activity_enlist.rv

/**
 * @description 报名活动页面
 * @author long
 * @date 2021/7/21
 */
@Route(path = MineRoutes.MINE_ENLIST)
class EnlistActivity : BaseActivity(), IsFitsSystemWindows, EnlistContract.View {

    private val mData: MutableList<EnlistActivityModel> = mutableListOf()
    private lateinit var enListAdapter: EnlistAdapter
    private lateinit var mEnListPresenter: EnListPresenter
    private val mParamMap: MutableMap<String, Any> = mutableMapOf()
    private var mCurrentPage: Int = 1
    private var mSearchName: String = ""
    private var isMore: Boolean = true

    override fun getLayoutId(): Int {
        return R.layout.mine_activity_enlist
    }

    override fun init(savedInstanceState: Bundle?) {

        val layoutParams = tabs.layoutParams
        layoutParams.height = statusBarHeight
        tabs.layoutParams = layoutParams

        setStatusLayout(layout_status)

        mEnListPresenter = EnListPresenter(mContext, this)
        enListAdapter = EnlistAdapter()
        enListAdapter.setNewData(mData)
        rv.layoutManager = LinearLayoutManager(mContext)
        rv.adapter = enListAdapter

        getData()
        initListener()
    }

    override fun getEnListSuccess(resultData: MutableList<EnlistActivityModel>, isMore: Boolean) {
        this.isMore = isMore
        //判断是否还有更多
        if (isMore) {
            refresh_layout.resetNoMoreData()
            refresh_layout.finishLoadMore()
        } else {
            refresh_layout.finishLoadMoreWithNoMoreData()
        }

        if (mCurrentPage == 1) {
            mData.clear()
        }
        mData.addAll(resultData)
        enListAdapter.notifyDataSetChanged()
        if (ListUtil.isEmpty(mData)) {
            showEmpty()
            refresh_layout.finishLoadMoreWithNoMoreData()
            return
        }
        showContent()
    }

    override fun getEnlistDetailsSuccess(resultModel: EnlistActivityModel?) {
    }

    override fun addEnlistSuccess(id: String, payOrderId: String) {
        TODO("Not yet implemented")
    }


    override fun removeEnlistSuccess() {
    }

    override fun onGetPayInfoSuccess(url: String) {
        TODO("Not yet implemented")
    }

    override fun getPayStatusSuccess(status: String?) {
        TODO("Not yet implemented")
    }


    override fun getData() {
        super.getData()
        mParamMap["pageNum"] = mCurrentPage.toString()
        mParamMap["enlistActivityName"] = mSearchName
        mEnListPresenter.getEnList(mParamMap)
    }

    override fun onRequestFinish() {
        super.onRequestFinish()
        refresh_layout.finishRefresh()

        if (ListUtil.isEmpty(mData)) {
            showEmpty()
        }
    }

    private fun initListener() {

        enListAdapter.setOnItemClickListener { adapter, view, position ->
            ARouter.getInstance()
                .build(MineRoutes.MINE_ENLIST_DETAILS)
                .withString("id", mData[position].id)
                .withString("isEnlist", "1")
                .navigation()
        }

        img_back.setOnClickListener {
            onBackPressed()
        }

        et_enlist_search.setOnEditorActionListener { v, actionId, event ->
            //搜索
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val mKeyWord: String = et_enlist_search.text.toString().trim()
                mSearchName = mKeyWord
                mCurrentPage = 1
                getData()
                et_enlist_search.findFocus()
                KeyboardUtils.hideSoftInput(et_enlist_search)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

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
}