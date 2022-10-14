package com.health.index.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.banner.ViewPager2Banner
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.AppIndexCustomRecommandAll
import com.healthy.library.model.VideoListModel
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.TransformUtil

/**
 * author : long
 * Time   :2021/12/14
 * desc   :
 */
class IndexFeaturedCoursesAdapter :
    BaseAdapter<AppIndexCustomRecommandAll>(R.layout.index_featured_courses_layout) {

    private var mBannerAdapter: IndexFeaturedCoursesBannerAdapter? = null

    override fun getItemViewType(position: Int): Int {
        return 10
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        holder.itemView.setOnClickListener {

        }
        val recommandAll = model
        val mBanner = holder.getView<RecyclerView>(R.id.banner)
        if (mBannerAdapter == null) {
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(mBanner)
            mBannerAdapter = IndexFeaturedCoursesBannerAdapter()
            mBanner.apply {
                this.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = mBannerAdapter
                val mList = mutableListOf<VideoListModel>()
                mList.clear()
                mList.addAll(recommandAll.video)
                mBannerAdapter?.setNewData(mList)
            }
        }
        holder.setOnClickListener(R.id.videoMore, object : View.OnClickListener {
            override fun onClick(v: View?) {
                ARouter.getInstance()
                    .build(IndexRoutes.INDEX_VIDEOONLINELIST)
                    .navigation()
            }
        })

    }

}