package com.health.index.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.donkingliang.consecutivescroller.ConsecutiveViewPager
import com.example.lib_ShapeView.layout.ShapeLinearLayout
import com.google.android.material.tabs.TabLayout
import com.health.index.R
import com.health.index.fragment.*
import com.healthy.library.adapter.CanReplacePageAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.builder.SimpleHashMapBuilder
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.message.UpdateUserLocationMsg
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.routes.SoundRoutes
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_index_baby.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

@Route(path = IndexRoutes.INDEX_INDEXBABY)
class IndexBabyActivity : BaseActivity(), IsFitsSystemWindows, OnRefreshLoadMoreListener {

    var stTab: TabLayout? = null
    var childTab: TabLayout? = null
    var stPager: ViewPager? = null
    var childPager: ConsecutiveViewPager? = null
    var stTitle = mutableListOf<String>("备孕", "孕期", "育儿")
    var stFragment = mutableListOf<Fragment>()
    var childTitle = mutableListOf<String>("看", "学", "听", "聊")
    var childFragment = mutableListOf<Fragment>()

    var indexBabyLookFragment: IndexBabyLookFragment? = null
    var indexBabyStudyFragment: IndexBabyStudyFragment? = null
    var indexBabyListenFragment: IndexBabyListenFragment? = null
    var indexBabyPostFragment: IndexBabyPostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_index_baby
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
        buildStTab()
        buildChildTab()
    }

    private fun buildStTab() {
        stFragment.clear()
        for (i in stTitle.indices) {
            stFragment.add(IndexBabyFragment.newInstance(i.toString()))
        }
        val canReplacePageAdapter =
            CanReplacePageAdapter(supportFragmentManager, stFragment, stTitle)
        stPager?.offscreenPageLimit = stFragment.size
        stPager?.adapter = canReplacePageAdapter
        stTab?.setupWithViewPager(stPager, false)
        stTab?.removeAllTabs()
        for (i in stTitle.indices) {
            val tab: TabLayout.Tab = stTab!!.newTab()
            //插入tab标签
            val result = LayoutInflater.from(mContext)
                .inflate(R.layout.index_baby_tab, stTab, false)
            val titleFirst = result.findViewById<TextView>(R.id.titleFirst)
            titleFirst.text = stTitle[i]
            if (i == 0) {
                tab.select()
                changeStTabStatus(result, true)
            } else {
                changeStTabStatus(result, false)
            }
            tab.customView = result
            stTab?.addTab(tab)
        }
    }

    private fun buildChildTab() {
        childFragment.clear()
        indexBabyLookFragment = IndexBabyLookFragment.newInstance()
        indexBabyStudyFragment = IndexBabyStudyFragment.newInstance()
        indexBabyListenFragment = IndexBabyListenFragment.newInstance()
        indexBabyPostFragment = IndexBabyPostFragment.newInstance()

        childFragment.add(indexBabyLookFragment!!)
        childFragment.add(indexBabyStudyFragment!!)
        childFragment.add(indexBabyListenFragment!!)
        childFragment.add(indexBabyPostFragment!!)
        val canReplacePageAdapter =
            CanReplacePageAdapter(supportFragmentManager, childFragment, childTitle)
        childPager?.offscreenPageLimit = childFragment.size
        childPager?.adapter = canReplacePageAdapter
        childTab?.setupWithViewPager(childPager, false)
        childTab?.removeAllTabs()
        for (i in childTitle.indices) {
            val tab: TabLayout.Tab = childTab!!.newTab()
            //插入tab标签
            val result = LayoutInflater.from(mContext)
                .inflate(R.layout.index_baby_child_tab, childTab, false)
            val title = result.findViewById<TextView>(R.id.title)
            val content = result.findViewById<TextView>(R.id.content)
            val iconImg = result.findViewById<ImageView>(R.id.iconImg)
            val num = result.findViewById<TextView>(R.id.num)
            title.text = childTitle[i]
            when (i) {
                0 -> {
                    num.visibility = View.GONE
                    title.text = childTitle[i]
                    content.text = "珍藏孕育宝典"
                    iconImg.setImageDrawable(resources.getDrawable(R.drawable.index_baby_child_tab_icon1))
                }
                1 -> {
                    num.visibility = View.GONE
                    content.text = "精选孕育课程"
                    iconImg.setImageDrawable(resources.getDrawable(R.drawable.index_baby_child_tab_icon2))
                }
                2 -> {
                    num.visibility = View.GONE
                    content.text = "听专家怎么讲"
                    iconImg.setImageDrawable(resources.getDrawable(R.drawable.index_baby_child_tab_icon3))
                }
                3 -> {
                    num.visibility = View.VISIBLE
                    content.text = "加入热聊"
                    iconImg.setImageDrawable(resources.getDrawable(R.drawable.index_baby_child_tab_icon4))
                }
            }
            if (i == 0) {
                tab.select()
                changeChildTabStatus(result, true)
            } else {
                changeChildTabStatus(result, false)
            }
            tab.customView = result
            childTab?.addTab(tab)
        }
    }

    override fun findViews() {
        super.findViews()
        stTab = findViewById(R.id.st)
        childTab = findViewById(R.id.childTab)
        stPager = findViewById(R.id.st_pager)
        childPager = findViewById(R.id.child_pager)
        img_back.setOnClickListener { finish() }
        layout_refresh.setOnRefreshLoadMoreListener(this)
        stTab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeStTabStatus(tab?.customView, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                changeStTabStatus(tab?.customView, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        childTab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeChildTabStatus(tab?.customView, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                changeChildTabStatus(tab?.customView, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        menu1.setOnClickListener {
            ARouter.getInstance()
                .build(IndexRoutes.INDEX_TOOLS)
                .withInt("currentIndex", 1)
                .navigation()
        }
        menu2.setOnClickListener {
            ARouter.getInstance()
                .build(IndexRoutes.INDEX_MAINPASSNEWS)
                .withString("knowOrInfoStatus", "2")
                .withString("queryDate", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
                .navigation()
        }
        menu3.setOnClickListener {
            ARouter.getInstance()
                .build(IndexRoutes.INDEX_VIDEOONLINELIST)
                .navigation()
        }
        menu4.setOnClickListener {
            ARouter.getInstance()
                .build(SoundRoutes.SOUND_MAIN)
                .withInt("audioType", 1)
                .navigation()
        }
        menu5.setOnClickListener {
            ARouter.getInstance()
                .build(SoundRoutes.SOUND_MAIN_MON)
                .withInt("audioType", 2)
                .navigation()
        }
    }

    /**
     * tab切换
     *
     * @param view
     * @param selected
     */
    private fun changeStTabStatus(view: View?, selected: Boolean) {
        if (view != null) {
            val titleFirst = view.findViewById<TextView>(R.id.titleFirst)
            val titleSecond = view.findViewById<ImageView>(R.id.titleSecond)
            if (selected) {
                titleFirst.textSize = 20f
                titleSecond.visibility = View.VISIBLE
                titleFirst.setTextColor(Color.parseColor("#333333"))
            } else {
                titleFirst.textSize = 16f
                titleSecond.visibility = View.GONE
                titleFirst.setTextColor(Color.parseColor("#999999"))
            }
        }
    }

    /**
     * tab切换
     *
     * @param view
     * @param selected
     */
    private fun changeChildTabStatus(view: View?, selected: Boolean) {
        if (view != null) {
            val tabbgg=view?.findViewById<ImageView>(R.id.tabbgg)
            val tab=view?.findViewById<ShapeLinearLayout>(R.id.tab)
            if (selected) {
                tabbgg?.visibility=View.VISIBLE
                tab?.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                tabbgg?.setImageResource(R.drawable.index_baby_child_tab_bg)
                view.setBackgroundDrawable(null)
            } else {
                tab?.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
                tabbgg?.visibility=View.INVISIBLE
                view.setBackgroundDrawable(null)
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        indexBabyLookFragment?.refresh()
        indexBabyStudyFragment?.refresh()
        indexBabyListenFragment?.refresh()
        indexBabyPostFragment?.refresh()
        layout_refresh.finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(msg: UpdateUserLocationMsg) {
        Handler().postDelayed(Runnable { indexBabyPostFragment?.refresh() }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}