package com.healthy.library.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.example.lib_ShapeView.view.ShapeView
import com.healthy.library.LibApplication
import com.healthy.library.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.utils.TransformUtil

/**
 * @author: long
 * @date: 2021/4/24
 * @des
 */
class ItemDecorationAdapter() : BaseAdapter<String?>(R.layout.item_decoration_layout) {


    var marginHorizontal = 0
    var isRadius = false
    var radius = TransformUtil.newDp2px(LibApplication.getAppContext(), 8f)
    var topLeftRadius = 0
    var topRightRadius = 0
    var bottomLeftRadius = 0
    var bottomRightRadius = 0
    var bgColor = Color.TRANSPARENT
    var itemHeight = 0

    constructor(marginHorizontal: Int) : this() {
        this.marginHorizontal = marginHorizontal
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        if (marginHorizontal != 0) {
            linearLayoutHelper.marginLeft = marginHorizontal
            linearLayoutHelper.marginRight = marginHorizontal
        }
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        val view = holder.getView<ShapeView>(R.id.line)
        val layoutParams = view.layoutParams
        if (itemHeight != 0) {
            val height = itemHeight
            layoutParams.height = height
            view.layoutParams = layoutParams
        }

        var shapeDrawableBuilder = view.shapeDrawableBuilder
        if (isRadius) {
            shapeDrawableBuilder
                .setRadius(radius.toFloat())
                .setSolidColor(bgColor)
                .intoBackground()
        } else {
            if (topLeftRadius != 0) {
                shapeDrawableBuilder =
                    shapeDrawableBuilder.setTopLeftRadius(topLeftRadius.toFloat())
            }
            if (topRightRadius != 0) {
                shapeDrawableBuilder =
                    shapeDrawableBuilder.setTopRightRadius(topRightRadius.toFloat())
            }

            if (bottomLeftRadius != 0) {
                shapeDrawableBuilder =
                    shapeDrawableBuilder.setBottomLeftRadius(bottomLeftRadius.toFloat())
            }
            if (bottomRightRadius != 0) {
                shapeDrawableBuilder =
                    shapeDrawableBuilder.setBottomRightRadius(bottomRightRadius.toFloat())
            }
            shapeDrawableBuilder.setSolidColor(bgColor)
                .intoBackground()
        }
    }
}