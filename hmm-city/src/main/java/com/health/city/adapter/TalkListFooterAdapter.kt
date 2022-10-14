package com.health.city.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.example.lib_ShapeView.layout.ShapeLinearLayout
import com.health.city.R
import com.health.city.model.TalentList
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.builder.ObjectIteraor
import com.healthy.library.model.Topic
import com.healthy.library.routes.CityRoutes
import java.util.zip.Inflater

/**
 * 创建日期：2021/11/11 14:59
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.city.adapter
 * 类说明：
 */

class TalkListFooterAdapter :
    BaseAdapter<Topic>(R.layout.item_talk_footer_adapter_layout) {

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        when (position) {
            0 -> {
                holder.setBackGround(R.id.talkLiner, R.drawable.shape_talk_list_item_top_bg)
                holder.setVisibility(R.id.heightView, View.VISIBLE)
            }
            datas.size - 1 -> {
                holder.setBackGround(R.id.talkLiner, R.drawable.shape_talk_list_item_bottom_bg)
                holder.setVisibility(R.id.heightView, View.GONE)
            }
            else -> {
                holder.setBackGround(R.id.talkLiner, R.drawable.shape_talk_list_item_center_bg)
                holder.setVisibility(R.id.heightView, View.GONE)
            }
        }
        var ran = position + 4
        if (ran < 10) {
            holder.setText(R.id.rank, String.format("0%s", ran))
        } else {
            holder.setText(R.id.rank, ran.toString())
        }
        holder.setText(R.id.talkTitle, datas[position].topicName)
        holder.setText(R.id.talkNum, String.format("%s篇帖子", datas[position].postingNum))
        holder.setText(R.id.talkHot, String.format("%s人参与", datas[position].partInNum))
        holder.itemView.setOnClickListener {
            ARouter.getInstance()
                .build(CityRoutes.CITY_TIP)
                .withString("topicId", datas[position].id)
                .navigation()
        }
    }

    override fun getDuplicateObjectIterator(): ObjectIteraor<Topic>? {
        return ObjectIteraor { o: Topic -> o.id }
    }
}