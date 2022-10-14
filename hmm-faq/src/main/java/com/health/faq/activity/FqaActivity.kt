package com.health.faq.activity

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.health.faq.R
import com.health.faq.fragment.HotQuestionFragment
import com.health.faq.fragment.LatestQuestionFragment
import com.healthy.library.base.BaseActivity
import com.healthy.library.routes.AppRoutes
import com.healthy.library.routes.FaqRoutes
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_fqa.*
import kotlinx.android.synthetic.main.adapter_bottom.*
import java.util.*



/**
 * 悬赏求助
 *
 * */
class FqaActivity : BaseActivity(),View.OnClickListener{

    internal var cityNo: String? = null
    private var mFragmentList: MutableList<androidx.fragment.app.Fragment> = mutableListOf()
    private var mTitleList: MutableList<String> = mutableListOf()
    override fun getLayoutId(): Int {
        return R.layout.activity_fqa
    }

    override fun findViews() {
        println("跳问题创建2")
        top_bar.setTitle("悬赏求助")
        mTitleList = ArrayList()
        mTitleList.add("最新求助")
        mTitleList.add("热门求助")
        cityNo=intent.getStringExtra("cityNo")
        mFragmentList = ArrayList()
        mFragmentList.add(LatestQuestionFragment())
        mFragmentList.add(HotQuestionFragment())
        vpFaq.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(i: Int): androidx.fragment.app.Fragment {
                return mFragmentList[i]
            }

            override fun getCount(): Int {
                return mFragmentList.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitleList[position]
            }
        }

        val nokmap = HashMap<String, String>()
        nokmap["soure"] = "专家答疑"
        MobclickAgent.onEvent(mContext, "event2RewardShow", nokmap)

        tlFqa.setupWithViewPager(vpFaq)
        tlFqa.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val nokmap = HashMap<String, String>()
                nokmap["soure"] = "专家答疑"
                MobclickAgent.onEvent(mContext, "event2RewardShow", nokmap)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        ivClose.setOnClickListener(View.OnClickListener {
            need_s.setVisibility(View.GONE)
        })
        tvAskExpert.setOnClickListener(this)
        tvReward.setOnClickListener(this)
    }
    override fun onClick(p0: View?) {
        if(p0?.id==R.id.clAdapterBottom){
            clAdapterBottom.visibility=View.GONE
        }
        if (p0?.getId() == R.id.tvReward) {
            val nokmap = HashMap<String, String>()
            nokmap["soure"] = "悬赏求助列表-悬赏求助"
            MobclickAgent.onEvent(mContext, "event2RewardFrom", nokmap)
            ARouter.getInstance().build(FaqRoutes.FAQ_REWARD).navigation()
        }
        if (p0?.getId() == R.id.tvAskExpert) {

            val nokmap = HashMap<String, String>()
            nokmap["soure"] = "悬赏求助列表-问专家"
            MobclickAgent.onEvent(mContext, "event2QuestExportFrom", nokmap)

            ARouter.getInstance()
                    .build(AppRoutes.MODULE_EXPERT_LEFT)
                    .withString("cityNo", cityNo)
                    .navigation()
        }
    }
    override fun init(savedInstanceState: Bundle?) {
    }


}