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
import com.healthy.library.adapter.FragmentStateItemAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.routes.MineRoutes
import com.healthy.library.widget.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_vote_list.*

/**
 * @author long
 * @description 我的投票界面
 * @date 2021/6/22
 */
@Route(path = MineRoutes.MINE_VOTELIST)
class MyVoteListActivity : BaseActivity(), IsFitsSystemWindows {

    @Autowired
    lateinit var currentItem: String

    private var mMediator: TabLayoutMediator? = null
    private var mTitles: MutableList<String> = mutableListOf("参与的投票", "报名的投票")
    private var mFragments: MutableList<Fragment> = mutableListOf()

    override fun getLayoutId(): Int {
        return R.layout.activity_vote_list
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(mContext)

        mFragments.add(MyVoteListFragment.newInstance(1))
        mFragments.add(MyVoteListFragment.newInstance(2))

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
        //取消关联
        mMediator?.detach()
        super.onDestroy()
    }

}