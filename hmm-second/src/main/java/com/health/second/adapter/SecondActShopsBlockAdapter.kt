package com.health.second.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.health.second.R
import com.healthy.library.base.MBaseQuickAdapter
import com.healthy.library.builder.ObjectIteraor
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.model.MainBlockDetailModel
import com.healthy.library.model.MainBlockModel
import com.healthy.library.model.SecondSortGoods
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.utils.FormatUtils
import com.healthy.library.utils.NavigateUtils
import com.healthy.library.utils.ParseUtils
import com.healthy.library.widget.AutoClickImageView
import com.healthy.library.widget.CornerImageView
import com.healthy.library.widget.DashLine
import com.healthy.library.widget.ImageTextView
import java.lang.ref.SoftReference
import java.util.*

/**
 * @desc
 * @author long
 * @createTime MainBlockModel
 */
class SecondActShopsBlockAdapter :
    MBaseQuickAdapter<MainBlockModel.AllianceMerchant, BaseViewHolder>(R.layout.item_second_mian_store) {

    var viewmap: MutableMap<String, SoftReference<View>?> = HashMap()

    fun setNeeduseNewmap(mainBlockModel: MainBlockModel) {
        viewmap[mainBlockModel.id] = null
    }

    override fun convert(baseHolder: BaseViewHolder, item: MainBlockModel.AllianceMerchant) {

        val cityTopTitle: View = baseHolder.getView(R.id.cityTopTitle)
        val cityShopLin: LinearLayout = baseHolder.getView(R.id.cityShopLin)
        val topCityTitle: ImageTextView = baseHolder.getView(R.id.topCityTitle)
        val topSecondTitle: ImageTextView = baseHolder.getView(R.id.topSecondTitle)
        val topMore: ImageTextView = baseHolder.getView(R.id.topMore)
        val shopCon: ConstraintLayout = baseHolder.getView(R.id.shopCon)
        val shopIcon: ImageView = baseHolder.getView(R.id.shopIcon)
        val videoTip: ImageView = baseHolder.getView(R.id.videoTip)
        val shopName: TextView = baseHolder.getView(R.id.shopName)
        val loc: ImageTextView = baseHolder.getView(R.id.loc)
        val locDistance: TextView = baseHolder.getView(R.id.locDistance)
        val couponMore: ImageView = baseHolder.getView(R.id.couponMore)
        val goodsCouponPLL: HorizontalScrollView = baseHolder.getView(R.id.goodsCouponPLL)
        val goodsCouponLL: LinearLayout = baseHolder.getView(R.id.goodsCouponLL)
        val couponDash: DashLine = baseHolder.getView(R.id.couponDash)
        val headNowLL: RelativeLayout = baseHolder.getView(R.id.headNowLL)
        val headIcon: CornerImageView = baseHolder.getView(R.id.head_icon)
        val headIcon3: CornerImageView = baseHolder.getView(R.id.head_icon3)
        val manCount: TextView = baseHolder.getView(R.id.manCount)
        val bottomGrid: LinearLayout = baseHolder.getView(R.id.bottomGrid)
        cityTopTitle.visibility =
            View.GONE//if (baseHolder.layoutPosition == 0) View.GONE else View.GONE

        onSucessGetList(
            cityShopLin,
            shopIcon,
            shopName,
            loc,
            shopCon,
            locDistance,
            topMore,
            bottomGrid,
            item.detailList,
            item
        )
    }

    fun onSucessGetList(
        cityShopLin: LinearLayout,
        shopIcon: ImageView,
        shopName: TextView,
        loc: ImageTextView,
        shopCon: ConstraintLayout,
        locDistance: TextView,
        topMore: ImageTextView,
        bottomGrid: LinearLayout,
        result: MutableList<MainBlockDetailModel>?,
        shopInfo: MainBlockModel.AllianceMerchant?
    ) {
        if (shopInfo != null) {
            val shopDto = shopInfo.shopDto
            try {
                cityShopLin.visibility = View.VISIBLE
                GlideCopy.with(mContext).load(shopInfo.shopDto.envPicUrl.toString().split(",")[0])
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    
                    .into(shopIcon)
                shopName.text = shopDto.shopName
                loc.text =
                    shopDto.provinceName + shopDto.cityName + shopDto.addressAreaName + shopDto.addressDetails
                loc.setOnClickListener {
                    if (shopDto.latitude > 0 && shopDto.longitude > 0) {
                        NavigateUtils.navigate(
                            mContext, shopDto.addressDetails,
                            shopDto.latitude.toString(), shopDto.longitude.toString()
                        )
                    }
                }
                shopCon.setOnClickListener {
                    ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SHOP_DETAIL)
                        .withString("shopId", shopDto.id.toString())
                        .navigation()
                }
                locDistance.text = ParseUtils.parseDistance(shopDto.distance.toString())
                topMore.setOnClickListener {
                    ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SHOP_DETAIL)
                        .withString("shopId", shopDto.id.toString())
                        .navigation()
                }
                result?.let { buildShopGoods(bottomGrid, it, shopDto) }
            } catch (e: Exception) {
            }
            /*val shopDistanceDes = if (shopDto.distance >= 1000) {
                String.format("%.1f", shopDto.distance / 1000) + "km"
            } else {
                shopDto.distance.toString() + "m"
            }*/

        } else {
            cityShopLin.visibility = View.GONE
        }
    }

    var mMargin = 0
    private fun buildShopGoods(
        bottomGrid: LinearLayout,
        result: MutableList<MainBlockDetailModel>?,
        secondSortShop: MainBlockModel.ShopDto
    ) {
        bottomGrid.removeAllViews()
        val needsize = if (result != null && result.size >= 6) 6 else result!!.size
        for (i in 0 until needsize) {
            val viewparent = LayoutInflater.from(mContext)
                .inflate(R.layout.item_second_main_hgoods, bottomGrid, false)
            val goodsCityItem = result[i].goodsDTO
            val goodsImg = viewparent.findViewById<ImageView>(R.id.goodsImg)
            val goodsName = viewparent.findViewById<TextView>(R.id.goodsName)
            val goodsMoney = viewparent.findViewById<TextView>(R.id.goodsMoney)
            val pinOldPrice = viewparent.findViewById<TextView>(R.id.pinOldPrice)
            val passbasket: AutoClickImageView = viewparent.findViewById(R.id.passbasket)
            GlideCopy.with(mContext).load(goodsCityItem.getFilePath())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                
                .into(goodsImg)
            goodsName.text = goodsCityItem.goodsTitle
            goodsMoney.text = FormatUtils.moneyKeep2Decimals(goodsCityItem.platformPrice) + ""
            pinOldPrice.text = "¥" + FormatUtils.moneyKeep2Decimals(goodsCityItem.retailPrice)
            pinOldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            viewparent.setOnClickListener {
                ARouter.getInstance()
                    .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                    .withString("id", goodsCityItem.id)
                    .withString("marketingType", goodsCityItem.marketingType)
                    .navigation()
            }
            bottomGrid.addView(viewparent)
        }
    }

    override fun getDuplicateObjectIterator(): ObjectIteraor<MainBlockModel.AllianceMerchant>? {
        return ObjectIteraor { o: MainBlockModel.AllianceMerchant -> o.id }
    }
}