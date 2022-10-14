package com.health.city.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.AppBarLayout
import com.health.city.R
import com.healthy.library.base.BaseFragment
import com.healthy.library.interfaces.AppBarStateChangeListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_ranking_list.*

class RankingListFragment : BaseFragment(), OnRefreshListener {
    private var param1: String? = null
    private var param2: String? = null
    private var talentListFragment: TalentListFragment? = null
    private var talkListFragment: TalkListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("param1")
            param2 = it.getString("param2")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_ranking_list
    }

    @SuppressLint("Range")
    override fun findViews() {
        showContent()
        talkTxt.isSelected = true
        refresh_layout.setEnableLoadMore(false)
        refresh_layout.setOnRefreshListener(this)
        talentTxt.setOnClickListener {
            buildCheck(true)
        }
        talkTxt.setOnClickListener {
            buildCheck(false)
        }
        talentTxt2.setOnClickListener {
            buildCheck(true)
        }
        talkTxt2.setOnClickListener {
            buildCheck(false)
        }
        initView()
    }

    private fun buildCheck(isCheck: Boolean) {
        when (isCheck) {
            true -> {
                talentTxt.isSelected = true
                talkTxt.isSelected = false
                talentTxt2.alpha = 1.toFloat()
                talkTxt2.alpha = 0.5.toFloat()
                changeFragment(1)
            }
            false -> {
                talentTxt.isSelected = false
                talkTxt.isSelected = true
                talentTxt2.alpha = 0.5.toFloat()
                talkTxt2.alpha = 1.toFloat()
                changeFragment(2)
            }
        }
    }

    private fun initView() {
        app_bar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if (state == State.EXPANDED) {
                    tabLayout2.visibility = View.GONE
                    bgImg.visibility = View.VISIBLE
                    tabView.setBackgroundResource(R.color.transparent)
                    //展开状态
                } else if (state == State.COLLAPSED) {
                    tabLayout2.visibility = View.VISIBLE
                    bgImg.visibility = View.INVISIBLE
                    tabView.setBackgroundResource(R.drawable.city_ranking_fragment_top_tab_bg)
                    //折叠状态
                } else {
                    //中间状态
                    tabLayout2.visibility = View.INVISIBLE
                    bgImg.visibility = View.VISIBLE
                }
            }

        })
        var supportFragmentManager: FragmentManager = childFragmentManager
        talentListFragment = TalentListFragment.newInstance("", "")
        talkListFragment = TalkListFragment.newInstance("", "")
        var transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame, talentListFragment!!).hide(talentListFragment!!)
        transaction.add(R.id.frame, talkListFragment!!).show(talkListFragment!!)
        transaction.commit()

    }

    private fun changeFragment(type: Int) {
        when (type) {
            1 -> {
                childFragmentManager.beginTransaction()
                    .show(talentListFragment!!)
                    .hide(talkListFragment!!)
                    .commit()
                talkListFragment!!.scroll()
            }
            2 -> {
                childFragmentManager.beginTransaction()
                    .show(talkListFragment!!)
                    .hide(talentListFragment!!)
                    .commit()
                talentListFragment!!.scroll()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RankingListFragment().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        talentListFragment!!.onRefresh(refreshLayout)
        talkListFragment!!.onRefresh(refreshLayout)
        refresh_layout.finishRefresh()
    }
}