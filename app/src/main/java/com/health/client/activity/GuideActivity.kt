package com.health.client.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.health.client.R
import com.healthy.library.base.BaseNoTitleActivity
import com.healthy.library.constant.Constants
import com.healthy.library.constant.SpKey
import com.healthy.library.routes.AppRoutes
import com.healthy.library.utils.SpUtils
import kotlinx.android.synthetic.main.activity_guide.*


/**
 * @author Li
 * @date 2019-08-12 13:45
 * @des 引导页
 */
@Route(path = AppRoutes.APP_GUIDE)
class GuideActivity : BaseNoTitleActivity() {

    lateinit var list: ArrayList<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_guide)
        list = ArrayList()
        initGuidePic()
        pager_guide.adapter = object : PagerAdapter() {
            override fun isViewFromObject(p0: View, p1: Any): Boolean {
                return p0 == p1
            }

            override fun getCount(): Int {
                return list.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view = list[position]
                container.addView(view)
                return view
            }

            override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
                container.removeView(view as View)
            }

        }
        pager_guide.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                tv_skip.visibility = if (p0 == pager_guide.adapter?.count?.minus(1))
                    View.GONE else View.VISIBLE
            }
        })
        dot_guide.setupViewPager(pager_guide)


    }


    fun skip(view: View) {
        route()
    }

    private fun initGuidePic() {
        val resArray = intArrayOf(R.drawable.app_guide_1,
                R.drawable.app_guide_2, R.drawable.app_guide_3, R.drawable.app_guide_4)
        resArray.forEach {

            val imageView = ImageView(this)
            imageView.setImageResource(it)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            if (resArray.indexOf(it) == resArray.size.minus(1)) {
                imageView.setOnClickListener {
                    route()
                }
            }
            list.add(imageView)

        }
    }

    private fun route() {
        SpUtils.store(this, SpKey.SHOW_GUIDE, false)
        val id = SpUtils.getValue(this, SpKey.USER_ID)
        val status = SpUtils.getValue(this, SpKey.STATUS)
        val route = if (TextUtils.isEmpty(id)) {
            AppRoutes.APP_LOGINTRANSFER
        } else if (TextUtils.isEmpty(status) || Constants.STATUS_NONE == status) {
            AppRoutes.APP_CHOOSE_SEX
        } else {
            AppRoutes.APP_MAIN
        }
        ARouter.getInstance().build(route)
                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation()

    }
}
