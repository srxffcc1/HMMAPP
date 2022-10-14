package com.health.second.adapter

import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.second.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder

/**
 * @author Long
 * @desc:
 * @createTime :2021/10/15 11:21
 */
class SecondGoodsEmptyAdapter : BaseAdapter<String>(R.layout.item_mall_goods_no) {
    private lateinit var listener: (mViewHeight: Int) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int) -> Unit) {
        this.listener = listener
    }
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        getModel()?.let {
            holder.setText(R.id.noMSgTitle, it)
        }
        if(::listener.isInitialized){
            holder.itemView.post {
                listener.invoke(holder.itemView.height)
            }
        }
    }
}