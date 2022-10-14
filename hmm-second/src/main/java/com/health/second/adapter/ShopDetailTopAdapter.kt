package com.health.second.adapter

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import kotlin.jvm.JvmOverloads
import com.healthy.library.model.ShopDetailModel
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.second.R
import com.healthy.library.banner.ViewPager2Banner
import com.healthy.library.banner.ScaleInTransformer
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.widget.ImageTextView
import com.healthy.library.utils.ParseUtils
import com.healthy.library.utils.TransformUtil
import java.util.ArrayList

/**
 * 创建日期：2021/10/13 16:18
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.second.adapter
 * 类说明：
 */
class ShopDetailTopAdapter @JvmOverloads constructor(viewId: Int = R.layout.item_shop_detail_top_adapter_layout) :
    BaseAdapter<ShopDetailModel?>(viewId) {
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val shopName = holder.getView<View>(R.id.shopName) as TextView
        val bannerLayout = holder.getView<View>(R.id.bannerLayout) as ConstraintLayout
        val banner = holder.getView<View>(R.id.banner) as ViewPager2Banner
        val shopTime = holder.getView<View>(R.id.shopTime) as TextView
        val shopAddress = holder.getView<View>(R.id.shopAddress) as TextView
        val itvStoreDetailPhone = holder.getView<View>(R.id.itv_StoreDetailPhone) as ImageTextView
        val itvStoreDetailNavigation =
            holder.getView<View>(R.id.itv_StoreDetailNavigation) as ImageTextView
        val shopDistance = holder.getView<View>(R.id.shopDistance) as TextView
        val storeDetailModel = getModel() ?: return
        banner.setPageMargin(
            0,
            TransformUtil.dp2px(context, 25f).toInt(),
            TransformUtil.dp2px(context, 6f).toInt()
        )
            .setAutoPlay(false)
//            .addPageTransformer(ScaleInTransformer())
            .setOuterPageChangeListener(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
//                        currentPage = 1
                }
            })
        if (storeDetailModel.chainShopName != null && !TextUtils.isEmpty(storeDetailModel.chainShopName)) {
            shopName.text = storeDetailModel.shopName + "(" + storeDetailModel.chainShopName + ")"
        } else {
            shopName.text = if (storeDetailModel.shopName != null) storeDetailModel.shopName else ""
        }
        shopAddress.text = storeDetailModel.addressDetails
        shopDistance.text = ParseUtils.parseDistance(
            (storeDetailModel.distance.toString().toDouble() * 1000).toString()
        )
        if (storeDetailModel.shopBusinessOfArea != null && storeDetailModel.shopBusinessOfArea.size > 0) {
            var time: String? = ""
            if (storeDetailModel.shopBusinessOfArea.size == 1) {
                time = storeDetailModel.shopBusinessOfArea[0]
            }
            if (storeDetailModel.shopBusinessOfArea.size == 2) {
                time =
                    storeDetailModel.shopBusinessOfArea[0].toString() + "," + storeDetailModel.shopBusinessOfArea[1]
            }
            shopTime.text = time
        }
        if (storeDetailModel.envPicUrl != null && !TextUtils.isEmpty(storeDetailModel.envPicUrl)) {
            val urlArray = storeDetailModel.envPicUrl.split(",").toTypedArray()
            val bannerAdapter = BannerAdapter()
            banner.setAdapter(bannerAdapter)
            bannerAdapter.setData(urlArray, context)
            bannerAdapter.notifyDataSetChanged()
        }
        itvStoreDetailPhone.setOnClickListener {
            if (moutClickListener != null) {
                moutClickListener.outClick("phone", "")
            }
        }
        itvStoreDetailNavigation.setOnClickListener {
            if (moutClickListener != null) {
                moutClickListener.outClick("navigation", "")
            }
        }
    }
}