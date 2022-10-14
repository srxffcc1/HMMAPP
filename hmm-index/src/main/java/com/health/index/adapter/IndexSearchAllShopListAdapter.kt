package com.health.index.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.model.SearchRecordsModel
import com.healthy.library.model.ShopDetailModel
import com.healthy.library.model.VideoListModel
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.utils.ParseUtils
import com.healthy.library.widget.CornerImageView

/**
 * 创建日期：2022/1/4 14:17
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchAllShopListAdapter :
    BaseAdapter<String>(R.layout.index_search_all_shop_list_adapter_layout) {
    private var key: String? = null
    private var records: MutableList<ShopDetailModel>? = null

    public fun setKey(key: String) {
        this.key = key
    }

    public fun setAdapterData(data: MutableList<ShopDetailModel>) {
        this.records = data
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        var shopLiner = holder.getView<LinearLayout>(R.id.shopLiner)
        var more = holder.getView<AppCompatImageView>(R.id.more)
        more.setOnClickListener {
            if (isClickInit) {
                moutClickListener.outClick("跳转搜索服务门店", null)
            }
        }
        shopLiner.removeAllViews()
        holder.setText(R.id.keyTitle, key)
        if (!ListUtil.isEmpty(records)) {
            for ((index, e) in records!!.withIndex()) {
                var view = LayoutInflater.from(context)
                    .inflate(R.layout.item_index_search_all_shop_list, null)
                var iv_avatar = view.findViewById<CornerImageView>(R.id.iv_avatar)
                var shopName = view.findViewById<TextView>(R.id.shopName)
                var address = view.findViewById<TextView>(R.id.address)
                var distance = view.findViewById<TextView>(R.id.distance)
                var line = view.findViewById<View>(R.id.line)
                com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(e.envPicUrl.split(",")[0])
                    
                    .into(iv_avatar)
                shopName.text = e.shopName
                address.text = e.provinceName +e.cityName + e.addressAreaName + e.addressDetails
                distance.text = ParseUtils.parseDistance(e.distance.toString())
                if (index == records!!.size - 1) {
                    line.visibility = View.INVISIBLE
                } else {
                    line.visibility = View.VISIBLE
                }
                view.setOnClickListener {
                    ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SHOP_DETAIL)
                        .withString("shopId", e.id)
                        .navigation()
                }
                shopLiner.addView(view)
            }
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }
}

