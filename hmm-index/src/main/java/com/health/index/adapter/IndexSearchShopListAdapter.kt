package com.health.index.adapter

import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.ShopDetailModel
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.utils.ParseUtils

/**
 * 创建日期：2021/12/15 10:34
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchShopListAdapter :
    BaseAdapter<ShopDetailModel>(R.layout.item_index_search_hanmom_shop_list_adapter_layout) {

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.setText(R.id.shopName, datas[position].shopName)
        holder.setText(
            R.id.address,
            datas[position].provinceName + datas[position].cityName + datas[position].addressAreaName + datas[position].addressDetails
        )
        holder.setText(R.id.distance, ParseUtils.parseDistance(datas[position].distance.toString()))
        if (datas[position].envPicUrl != null) {
            holder.setImg(R.id.iv_avatar, datas[position].envPicUrl.split(",")[0])
        }
        holder.itemView.setOnClickListener {
            ARouter.getInstance()
                .build(SecondRoutes.SECOND_SHOP_DETAIL)
                .withString("shopId", datas[position].id)
                .navigation()
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }
}