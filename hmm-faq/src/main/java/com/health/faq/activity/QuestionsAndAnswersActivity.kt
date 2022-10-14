package com.health.faq.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.flyco.tablayout.listener.OnTabSelectListener
import com.health.faq.R
import com.health.faq.fragment.QuestionsAndAnswersFragment
import com.health.faq.fragment.QuestionsRight
import com.healthy.library.adapter.OrderPagerAdapter
import com.healthy.library.base.BaseActivity
import com.healthy.library.constant.Events
import com.healthy.library.routes.FaqRoutes
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_question_and_answers.*
import java.util.*

/**
 * @author xinkai.xu
 * 个人中心-问答页面
 *
 * */
@Route(path = FaqRoutes.FAQ_QUESTION_ANSWERS)
class QuestionsAndAnswersActivity : BaseActivity() {
    var mFragmentList = mutableListOf<Fragment>(QuestionsAndAnswersFragment().newInstance(1), QuestionsRight())
    var mTitleList = mutableListOf("保健专家", "名医专家")
    var mAdapter: OrderPagerAdapter = OrderPagerAdapter(supportFragmentManager, mFragmentList as ArrayList<Fragment>?, mTitleList.toTypedArray())
    var mTabIndex = 0
    override fun getLayoutId(): Int {
        return R.layout.activity_question_and_answers
    }

    override fun findViews() {
        txt_title.text = "我的提问"
        img_back.setOnClickListener {
            finish()
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        MobclickAgent.onEvent(mContext, Events.EVENT_MY_QUESTION)
        pager_faq.adapter = mAdapter
        stl_tab.setViewPager(pager_faq)
        stl_tab.currentTab = mTabIndex
        stl_tab.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                mTabIndex = position
            }

            override fun onTabReselect(position: Int) {}
        })

        pager_faq.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                mTabIndex = position
                val name = if (position == 0) Events.EVENT_MY_QUESTION else Events.EVENT_MY_ANSWER
                MobclickAgent.onEvent(mContext, name)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }
}


