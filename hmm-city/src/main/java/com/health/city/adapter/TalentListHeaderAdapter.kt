package com.health.city.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.city.R
import com.health.city.model.TalentList
import com.healthy.library.banner.ViewPager2Banner
import com.healthy.library.banner.UIndicator
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.utils.TransformUtil

/**
 * 创建日期：2021/11/9 17:04
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.city.adapter
 * 类说明：
 */

class TalentListHeaderAdapter :
    BaseAdapter<ArrayList<TalentList>>(R.layout.item_talent_header_adapter_layout) {

    var talentListBannerAdapter: TalentListBannerAdapter? = null

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        var banner: RecyclerView = holder.getView(R.id.banner)
        var indicator: UIndicator = holder.getView(R.id.indicator)
        if (talentListBannerAdapter == null) {
            talentListBannerAdapter = TalentListBannerAdapter()
            var snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(banner)
            //监听 滚动 获取具体位置
            banner.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (recyclerView != null && recyclerView.getChildCount() > 0) {
                            indicator.setSelectedItem((snapHelper.findSnapView(recyclerView.layoutManager)!!.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition)
                        }
                    }
                }
            })
        }
        var layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        banner.layoutManager = layoutManager
        banner.isNestedScrollingEnabled = false
        banner.adapter = talentListBannerAdapter
        talentListBannerAdapter!!.setData(datas[0])
        indicator.attachToBanner(datas[0].size)

    }
}