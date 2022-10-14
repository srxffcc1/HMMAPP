package com.health.index.adapter

import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.routes.CityRoutes

/**
 * 创建日期：2022/1/5 9:39
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexBabyPostTopAdapter :
    BaseAdapter<String>(R.layout.index_baby_post_top_adapter_layout) {


    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.itemView.setOnClickListener {
            ARouter.getInstance()
                .build(CityRoutes.CITY_POSTSEND)
                .navigation()
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }
}