package com.health.mine.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.tabs.TabLayout
import com.health.mine.R
import com.health.mine.fragment.MyVoteListFragment
import com.health.mine.fragment.LotteryListFragment
import com.healthy.library.adapter.FragmentStateItemAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.routes.MineRoutes
import com.healthy.library.widget.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_vote_list.*

/**
 * desc 中奖记录界面
 * author long
 * date 2021/8/2
 */
@Route(path = MineRoutes.MINE_AWARD_CENTER)
class AwardCenterActivity : BaseActivity(), IsFitsSystemWindows {

    @Autowired
    lateinit var currentItem: String

    private var mMediator: TabLayoutMediator? = null
    private var mTitles: MutableList<String> = mutableListOf("抽奖活动", "投票活动")
    private var mFragments: MutableList<Fragment> = mutableListOf()

    override fun getLayoutId(): Int {
        return R.layout.mine_activity_award_center
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(mContext)

        mFragments.add(LotteryListFragment.newInstance()) //获奖记录列表
        mFragments.add(MyVoteListFragment.newInstance(2)) //投票活动列表

        vp2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val mPagerItemAdapter =
            FragmentStateItemAdapter(this, mFragments)
        vp2.adapter = mPagerItemAdapter
        vp2.offscreenPageLimit = mFragments.size
        if (::currentItem.isInitialized) {
            vp2.currentItem = currentItem.toInt()
        }
        mMediator = TabLayoutMediator(
            tab, vp2, object : TabLayoutMediator.OnConfigureTabCallback {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    //这里需要根据position修改tab的样式和文字等
                    tab.text = mTitles[position]
                }

                override fun onPageSelected(position: Int) {

                }
            })
        mMediator?.attach()

        initListener()
    }

    /**
     * 监听回调
     */
    private fun initListener() {
        top_bar.setTopBarListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        //取消关联
        mMediator?.detach()
    }

}