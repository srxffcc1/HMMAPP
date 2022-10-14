package com.health.second.adapter

import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import kotlin.jvm.JvmOverloads
import com.healthy.library.model.ShopDetailModel
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.healthy.library.base.BaseHolder
import com.healthy.library.utils.TransformUtil
import com.alibaba.android.arouter.launcher.ARouter
import com.health.second.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.routes.LibraryRoutes
import com.healthy.library.utils.ScreenUtils

class StoreDetailQualificationsAdapter @JvmOverloads constructor(viewId: Int = R.layout.store_detail_qualifications_layout) :
    BaseAdapter<ShopDetailModel?>(viewId) {
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val qualificationsGrid = holder.getView<GridLayout>(R.id.qualificationsGrid)
        val qualificationsContent = holder.getView<TextView>(R.id.qualificationsContent)
        val shopContentTitle = holder.getView<TextView>(R.id.shopContentTitle)
        if (getModel()!!.shopIntroduce != null && !TextUtils.isEmpty(getModel()!!.shopIntroduce)) {
            shopContentTitle.visibility = View.VISIBLE
            qualificationsContent.text = Html.fromHtml(getModel()!!.shopIntroduce)
        } else {
            shopContentTitle.visibility = View.GONE
            qualificationsContent.text = "暂无门店介绍"
        }
        if (getModel()!!.businessLicensePicUrl != null && !TextUtils.isEmpty(getModel()!!.businessLicensePicUrl)) {
            val urlArray = getModel()!!.businessLicensePicUrl.split("，").toTypedArray()
            addFunctions(qualificationsGrid, urlArray)
        }
    }

    private fun addFunctions(gridLayout: GridLayout, urls: Array<String>) {
        gridLayout.removeAllViews()
        val row = 2
        val mMargin = TransformUtil.dp2px(context, 40f).toInt()
        val gridlayoutparm = gridLayout.layoutParams
        gridlayoutparm.width = (ScreenUtils.getScreenWidth(context) - mMargin) / 2 * row
        gridLayout.layoutParams = gridlayoutparm
        gridLayout.columnCount = row
        val w = (ScreenUtils.getScreenWidth(context) - mMargin) / 2
        for (i in urls.indices) {
            val params = GridLayout.LayoutParams()
            params.width = w
            val view = LayoutInflater.from(context)
                .inflate(R.layout.item_store_detail_qualifications_grid_layout, gridLayout, false)
            val qualificationsImg = view.findViewById<ImageView>(R.id.qualificationsImg)
            GlideCopy.with(context)
                .load(urls[i])
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(qualificationsImg)
            view.setOnClickListener {
                ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                    .withCharSequenceArray("urls", urls)
                    .withInt("pos", i)
                    .navigation()
            }
            gridLayout.addView(view, params)
        }
    }
}