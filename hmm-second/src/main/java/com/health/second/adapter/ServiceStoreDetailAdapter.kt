package com.health.second.adapter

import android.text.TextUtils
import android.view.View
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.second.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.ShopDetailModel
import com.healthy.library.utils.ParseUtils

/**
 * @author Long
 * @desc:
 * @createTime :2021/10/9 11:54
 */
class ServiceStoreDetailAdapter :
    BaseAdapter<ShopDetailModel>(R.layout.item_service_store_detail_layout) {

    var mShopSize: Int = 0
    var isLoadSuccess = false

    private lateinit var listener: (mViewHeight: Int) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (isLoadSuccess) return
        isLoadSuccess = true
        model?.let {
            val shopName =
                if (!TextUtils.isEmpty(it.chainShopName)) it.shopName + "(" + it.chainShopName + ")"
                else it.shopName

            val mAddress = it.provinceName + it.cityName +
                    it.addressAreaName + it.addressDetails
            holder.setText(R.id.tv_title, "适用门店")
                .setText(R.id.tv_lookAll, "适用${mShopSize}家门店")
                .setVisible(R.id.tv_lookAll, mShopSize > 0)
                .setText(R.id.tv_shopName, shopName)
                .setText(R.id.tv_shop_address, mAddress)
                .setText(R.id.tv_distance, ParseUtils.parseDistance(it.distance.toString()))
                .setOnClickListenerS(
                    View.OnClickListener { view ->
                        if (isClickInit) {
                            val function = when (view.id) {
                                R.id.itv_StoreDetailNavigation -> "navigation"
                                R.id.tv_lookAll -> "lookShopList"
                                else -> "phone"
                            }
                            moutClickListener.outClick(
                                function,
                                if (function == "phone") it.appointmentPhone else position
                            )
                        }
                    }, R.id.tv_lookAll, R.id.itv_StoreDetailNavigation, R.id.itv_StoreDetailPhone
                )
        }

        if (::listener.isInitialized) {
            holder.itemView.post {
                listener.invoke(holder.itemView.height)
            }
        }
    }
}