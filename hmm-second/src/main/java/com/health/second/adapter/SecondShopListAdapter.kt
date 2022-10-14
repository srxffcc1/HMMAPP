package com.health.second.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.health.second.R
import com.healthy.library.model.ShopDetailModel
import com.healthy.library.utils.ParseUtils

/**
 * @author Long
 * @desc:
 * @createTime :2021/10/9 16:24
 */
class SecondShopListAdapter :
    BaseQuickAdapter<ShopDetailModel, BaseViewHolder>(R.layout.item_second_shoplist_layout) {

    override fun convert(helper: BaseViewHolder, newStoreDetialModel: ShopDetailModel) {

        val shopName = if (!TextUtils.isEmpty(newStoreDetialModel.chainShopName)) {
            newStoreDetialModel.shopName + "(" + newStoreDetialModel.chainShopName + ")"
        } else {
            newStoreDetialModel.shopName
        }
        val mAddress = newStoreDetialModel.provinceName + newStoreDetialModel.cityName +
                newStoreDetialModel.addressAreaName + newStoreDetialModel.addressDetails

        helper.setText(R.id.tv_shopName, shopName)
            .setText(R.id.tv_shop_address, mAddress)
            .setText(R.id.tv_distance,  ParseUtils.parseDistance(newStoreDetialModel.distance.toString()))
            .addOnClickListener(R.id.tv_distance)

    }
}