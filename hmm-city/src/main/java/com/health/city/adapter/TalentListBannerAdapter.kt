package com.health.city.adapter

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.city.R
import com.health.city.model.TalentList
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.routes.CityRoutes
import com.healthy.library.utils.FormatUtils

/**
 * 创建日期：2021/11/10 15:08
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.city.adapter
 * 类说明：
 */

class TalentListBannerAdapter :
    BaseAdapter<TalentList>(R.layout.item_ranking_list_banner_layout) {

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        holder.setText(R.id.userNickName, datas[position].accountNickname)
        datas[position].accountFaceUrl?.let {
            holder.setImg(
                R.id.userImg,
                it,
                R.drawable.img_avatar_default,
                R.drawable.img_avatar_default
            )
        }
        if (datas[position].memberState == null || TextUtils.isEmpty(datas[position].memberState)) {
            holder.setVisibility(R.id.userStatus, View.INVISIBLE)
        } else {
            holder.setVisibility(R.id.userStatus, View.VISIBLE)
            holder.setText(R.id.userStatus, datas[position].memberState)
        }
        holder.setText(R.id.talentNum, datas[position].postingCount.toString())
        holder.setText(R.id.likeNum, datas[position].postingPraiseCount.toString())
        holder.setText(
            R.id.commentNum,
            (datas[position].discussCount + datas[position].discussReplyCount).toString()
        )
        holder.setText(R.id.shareNum, datas[position].postingShareCount.toString())
        holder.setText(
            R.id.totalPoint,
            FormatUtils.moneyKeep2Decimals(datas[position].livelyValue.toString())
        )
        val mTotalPoint = holder.getView<TextView>(R.id.totalPoint)
        val layoutParams = mTotalPoint.layoutParams as ConstraintLayout.LayoutParams
        if (mTotalPoint.text.toString().trim().length > 3) {
            layoutParams.rightToRight = -1
        } else {
            layoutParams.rightToRight = R.id.icon
        }
        mTotalPoint.layoutParams = layoutParams

        when (position) {
            0 -> {
                holder.setVisibility(R.id.heightView, View.VISIBLE)
                holder.setText(R.id.rank, "NO.1")
            }
            1 -> {
                holder.setVisibility(R.id.heightView, View.GONE)
                holder.setText(R.id.rank, "NO.2")
            }
            2 -> {
                holder.setVisibility(R.id.heightView, View.GONE)
                holder.setText(R.id.rank, "NO.3")
            }
        }
        holder.itemView.setOnClickListener {
            if (datas[position].memberId != null) {
                ARouter.getInstance()
                    .build(CityRoutes.CITY_FANDETAIL)
                    .withString("searchMemberType", datas[position].createSource.toString() + "")
                    .withString("memberId", datas[position].memberId + "")
                    .navigation()
            }
        }
    }
}