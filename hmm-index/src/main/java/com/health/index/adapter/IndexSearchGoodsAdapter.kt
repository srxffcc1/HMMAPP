package com.health.index.adapter

import android.graphics.Paint
import android.text.TextUtils
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.bumptech.glide.Glide
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.model.SortGoodsListModel
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.routes.ServiceRoutes
import com.healthy.library.utils.FormatUtils
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.RoundedImageView
import com.healthy.library.widget.StaggeredGridLayoutHelperFix

/**
 * 创建日期：2021/12/23 10:10
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchGoodsAdapter :
    BaseAdapter<SortGoodsListModel>(R.layout.item_index_search_goods_adapter_layout) {

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        var goodsImg = holder.getView<RoundedImageView>(R.id.goodsImg)
        var goodsModel = datas[position]
        Glide.with(context)
            .load(goodsModel.headImage)
            .placeholder(R.drawable.img_1_1_default2)
            .error(R.drawable.img_1_1_default)

            .into(goodsImg)
        holder.setText(R.id.goodsTitle, goodsModel.goodsTitle)
        holder.setText(R.id.goods_moneyFlag, "¥")
        holder.setText(
            R.id.goods_moneyValue,
            FormatUtils.moneyKeep2Decimals(goodsModel.platformPrice)
        )
        holder.setText(
            R.id.goods_moneyOld,
            FormatUtils.moneyKeep2Decimals(goodsModel.storePrice)
        )
        holder.getView<TextView>(R.id.goods_moneyOld).paint.flags =
            Paint.STRIKE_THRU_TEXT_FLAG//中划线
        holder.itemView.setOnClickListener {
            if (goodsModel.userId.equals(LocUtil.getUserId()) && !TextUtils.isEmpty(LocUtil.getUserId())) {
                var shopId = goodsModel.shopId
                if (shopId == null || shopId == "0") {
                    shopId = SpUtils.getValue(context, SpKey.CHOSE_SHOP)
                }
                ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_DETAIL)
                    .withString("id", goodsModel.id.toString())
                    .withString("shopId", shopId)
                    .navigation()
            } else {
                ARouter.getInstance()
                    .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                    .withString("id", goodsModel.id.toString())
                    .withString("shopId", goodsModel.shopId)
                    .navigation()
            }
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val helper = StaggeredGridLayoutHelperFix()
        helper.setMargin(
            TransformUtil.newDp2px(LibApplication.getAppContext(), 5f),
            TransformUtil.newDp2px(LibApplication.getAppContext(), 12f),
            TransformUtil.newDp2px(LibApplication.getAppContext(), 5f),
            0
        )
        helper.lane = 2
        helper.hGap = 3
        return helper
    }
}