package com.health.city.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.city.R
import com.healthy.library.banner.ViewPager2Banner
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.Topic
import com.healthy.library.utils.TransformUtil

/**
 * 创建日期：2021/11/11 14:59
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.city.adapter
 * 类说明：
 */

class TalkListHeaderAdapter :
    BaseAdapter<ArrayList<Topic>>(R.layout.item_talk_header_adapter_layout) {

    var talkListBannerAdapter: TalkListBannerAdapter? = null

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        var banner: RecyclerView = holder.getView(R.id.banner)
        if (talkListBannerAdapter == null) {
            talkListBannerAdapter = TalkListBannerAdapter()
        }
        var layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        banner.isNestedScrollingEnabled = false
        banner.layoutManager = layoutManager
        banner.adapter = talkListBannerAdapter
        talkListBannerAdapter!!.setData(datas[0])
    }
}