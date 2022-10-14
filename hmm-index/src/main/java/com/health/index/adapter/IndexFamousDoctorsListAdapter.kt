package com.health.index.adapter

import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.utils.TransformUtil

/**
 * author : long
 * Time   :2021/12/20
 * desc   : 名医推荐 -》名医榜单 三个卡片页
 */
class IndexFamousDoctorsListAdapter :
    BaseAdapter<String>(R.layout.index_famous_doctors_list_layout) {

    override fun getItemViewType(position: Int): Int {
        return 18
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(),10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(),10f)
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

    }
}