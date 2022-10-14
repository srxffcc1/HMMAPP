package com.health.city.adapter

import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.city.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder

/**
 * 创建日期：2021/11/11 14:59
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.city.adapter
 * 类说明：
 */

class RankingEmptyAdapter :
    BaseAdapter<String>(R.layout.ranking_empty) {

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
    }
}