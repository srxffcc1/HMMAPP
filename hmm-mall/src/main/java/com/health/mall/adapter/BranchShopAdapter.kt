package com.health.mall.adapter

import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.health.mall.R
import com.healthy.library.model.ShopBrandModel
import com.healthy.library.routes.MallRoutes
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.utils.IntentUtils
import com.healthy.library.utils.NavigateUtils
import com.healthy.library.utils.ParseUtils
import com.umeng.analytics.MobclickAgent



/**
 * @author Li
 * @date 2019-08-07 11:21
 * @des 其它分店列表适配器
 */
class BranchShopAdapter(layoutId: Int = R.layout.item_branch_shop) :
        BaseQuickAdapter<ShopBrandModel, BaseViewHolder>(layoutId) {
    lateinit var mshopId: String
    lateinit var mcategoryNo: String
    lateinit var mcityNo: String
    lateinit var mlng: String
    lateinit var mlat: String

    fun setLocation(shopId: String, categoryNo: String,cityNo: String, lng: String,lat: String) {
        this.mshopId = shopId
        this.mcategoryNo=categoryNo
        this.mcityNo = cityNo
        this.mlng = lng
        this.mlat=lat
    }
    override fun convert(helper: BaseViewHolder, item: ShopBrandModel?) {
        helper.setText(R.id.tv_shop_name, item?.shopName)
        helper.setText(R.id.tv_shop_address, item?.areaDetails)
        helper.addOnClickListener(R.id.iv_branch_shop_dial)
        helper.itemView.setOnClickListener(View.OnClickListener {
            ARouter.getInstance()
                    .build(SecondRoutes.SECOND_SHOP_DETAIL)
                    .withString("shopId", item!!.id.toString() )
                    .withString("categoryNo", mcategoryNo)
                    .withString("cityNo", mcityNo)
                    .withString("lng", mlng)
                    .withString("lat", mlat)
                    .navigation()
        })
        helper.setOnClickListener(R.id.iv_branch_shop_dial,View.OnClickListener {
            val nokmap = HashMap<String, String>()
            nokmap["soure"] = "其他分店列表"
            MobclickAgent.onEvent(mContext, "event2PhoneStoreClick", nokmap)
            IntentUtils.dial(mContext, item?.appointmentPhone)
        })
        helper.setOnClickListener(R.id.tv_shop_address,View.OnClickListener {
            NavigateUtils.navigate(mContext, item?.areaDetails,
                    item?.latitude.toString(), item?.longitude.toString())
        })
        helper.setText(R.id.tv_distance, ParseUtils.parseDistance(item?.distance.toString()))
    }
}