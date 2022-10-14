package com.healthy.library.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.healthy.library.R
import com.healthy.library.model.ShopDetailModel
import com.healthy.library.utils.ParseUtils

/**
 * @description
 * @author long
 * @date 2021/9/16
 */
class GiftSelectShopListAdapter :
    BaseQuickAdapter<ShopDetailModel, BaseViewHolder>(R.layout.dialog_gift_shop_list_child_layout) {

    private var checkShopId: Int = 0

    fun setCheckShopId(shopId:Int){
        this.checkShopId = shopId
    }

    override fun convert(helper: BaseViewHolder?, newStoreDetialModel: ShopDetailModel) {

        val shopName = if (!TextUtils.isEmpty(newStoreDetialModel.chainShopName)) {
            newStoreDetialModel.shopName + "(" + newStoreDetialModel.chainShopName + ")"
        } else {
            newStoreDetialModel.shopName
        }
        val mAddress = newStoreDetialModel.provinceName + newStoreDetialModel.cityName +
                newStoreDetialModel.addressAreaName + newStoreDetialModel.addressDetails
        helper?.setText(R.id.tv_shop_name, shopName)
            ?.setText(R.id.tv_shop_address, mAddress)
            ?.setText(
                R.id.tv_distance,
                ParseUtils.parseDistance(newStoreDetialModel.distance.toString() + "")
            )?.setVisible(R.id.checkOk, checkShopId == newStoreDetialModel.id.toInt())
    }
}