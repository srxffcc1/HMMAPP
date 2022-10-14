package com.health.index.adapter

import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder

/**
 * 创建日期：2021/12/23 14:06
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchEmptyAdapter :
    BaseAdapter<String>(R.layout.item_index_search_empty_adapter_layout) {

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }
}