package com.health.index.adapter

import android.view.View
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.interfaces.OnLabelClickListener
import com.healthy.library.loader.ImageNetAdapter
import com.healthy.library.model.AdModel
import com.healthy.library.model.MainSearchModel
import com.healthy.library.utils.MARouterUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.StackLabel
import com.youth.banner.Banner
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.activity_index_search.*
import java.util.*

/**
 * 创建日期：2021/12/15 10:34
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchHistoryAdapter :
    BaseAdapter<String>(R.layout.item_index_search_history_adapter_layout) {

    private var mList: MutableList<MainSearchModel>? = null

    fun setList(list: MutableList<MainSearchModel>) {
        this.mList = list
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        var stackLabel = holder.getView<StackLabel>(R.id.stackLabelView)
        val mArrayList = ArrayList<String>()
        if (!ListUtil.isEmpty(mList)) {
            for ((index, e) in mList!!.withIndex()) {
                mArrayList.add(e.keyword)
            }
            stackLabel.labels = mArrayList
        }
        stackLabel.setOnLabelClickListener(object : OnLabelClickListener {
            override fun onClick(index: Int, v: View?, s: String?) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("lable", s)
                }
            }
        })
        holder.setOnClickListener(R.id.iv_delete_icon, View.OnClickListener {
            if (moutClickListener != null) {
                moutClickListener.outClick("delete", null)
            }
        })
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }
}