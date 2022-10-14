package com.healthy.library.adapter

import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.healthy.library.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder

/**
 * @description
 * @author long
 * @date 2021/7/23
 */
class ItemLineAdapter : BaseAdapter<String>(R.layout.lib_recycerview_item_line) {

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

    }

}