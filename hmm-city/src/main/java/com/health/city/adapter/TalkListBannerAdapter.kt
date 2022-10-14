package com.health.city.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.city.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.Topic
import com.healthy.library.routes.CityRoutes
import com.healthy.library.widget.ImageSpanCentre

/**
 * 创建日期：2021/11/11 15:00
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.city.adapter
 * 类说明：
 */

class TalkListBannerAdapter :
    BaseAdapter<Topic>(R.layout.item_talk_list_banner_layout) {

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        try {
            val spanString = SpannableString("  " + datas[position].topicName)
            val drawable = context.resources.getDrawable(R.drawable.item_talk_ranking_hot_icon)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            val imageSpan = ImageSpanCentre(drawable, ImageSpanCentre.CENTRE)
            spanString.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            holder.setText(R.id.taikTitle, spanString)
        } catch (e: Exception) {
            holder.setText(R.id.taikTitle, datas[position].topicName)
        }
        if (datas[position].id == null) {
            holder.setText(R.id.peopleNum, "抢沙发～")
        } else {
            holder.setText(R.id.peopleNum, String.format("%s人已参与", datas[position].partInNum))
        }
        if (datas[position].faceUrlList != null && datas[position].faceUrlList.size > 0) {
            holder.setVisibility(R.id.headNowLL, View.VISIBLE)
            holder.setVisibility(R.id.head_icon1, View.VISIBLE)
            holder.setVisibility(R.id.head_icon2, View.VISIBLE)
            holder.setVisibility(R.id.head_icon3, View.VISIBLE)
            when (datas[position].partInNum.toInt()) {
                0 -> {
                    holder.setVisibility(R.id.headNowLL, View.GONE)
                }
                1 -> {
                    holder.setImg(R.id.head_icon1, datas[position].faceUrlList[0])
                    holder.setVisibility(R.id.head_icon2, View.GONE)
                    holder.setVisibility(R.id.head_icon3, View.GONE)
                }
                2 -> {
                    holder.setImg(R.id.head_icon1, datas[position].faceUrlList[0])
                    holder.setImg(R.id.head_icon2, datas[position].faceUrlList[1])
                    holder.setVisibility(R.id.head_icon3, View.GONE)
                }
                else -> {
                    holder.setImg(R.id.head_icon1, datas[position].faceUrlList[0])
                    holder.setImg(R.id.head_icon2, datas[position].faceUrlList[1])
                    holder.setImg(R.id.head_icon3, datas[position].faceUrlList[2])
                }
            }
        } else {
            holder.setVisibility(R.id.headNowLL, View.GONE)
        }
        when (position) {
            0 -> {
                holder.setVisibility(R.id.heightView,View.VISIBLE)
                holder.setTextColor(R.id.rank, Color.parseColor("#FF544F"))
                holder.setText(R.id.rank, "01")
            }
            1 -> {
                holder.setVisibility(R.id.heightView,View.GONE)
                holder.setTextColor(R.id.rank, Color.parseColor("#FA8800"))
                holder.setText(R.id.rank, "02")
            }
            2 -> {
                holder.setVisibility(R.id.heightView,View.GONE)
                holder.setTextColor(R.id.rank, Color.parseColor("#F7C700"))
                holder.setText(R.id.rank, "03")
            }
        }
        holder.itemView.setOnClickListener {
            if (datas[position].id != null) {
                ARouter.getInstance()
                    .build(CityRoutes.CITY_TIP)
                    .withString("topicId", datas[position].id)
                    .navigation()
            }
        }
    }
}