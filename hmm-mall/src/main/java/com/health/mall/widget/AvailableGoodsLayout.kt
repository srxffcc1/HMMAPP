package com.health.mall.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.health.mall.R
import com.health.mall.model.GoodsInfoModel
import kotlinx.android.synthetic.main.layout_available_goods.view.*

/**
 * @author Li
 * @date 2019-08-08 09:47
 * @des 商户详情的可购服务列表
 */
class AvailableGoodsLayout : FrameLayout {
    private val defaultShowNum = 3
    private var layoutInflater: LayoutInflater

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.layout_available_goods,
                this, true)
    }

    @SuppressLint("SetTextI18n")
    fun setGoods(goodsModels: ArrayList<GoodsInfoModel>) {
        layout_goods.removeAllViews()
        if (goodsModels.size > defaultShowNum) {
            group_toggle.visibility = View.VISIBLE
            tv_toggle.text = "查看剩余${goodsModels.size - defaultShowNum}个商品"
            tv_toggle.setOnClickListener {
                if (layout_goods.getChildAt(defaultShowNum).visibility == View.VISIBLE) {
                    for (i in 0 until layout_goods.childCount) {
                        if (i > defaultShowNum - 1) {
                            layout_goods.getChildAt(i).visibility = View.GONE
                        }
                    }
                    tv_toggle.text = "查看剩余${goodsModels.size - defaultShowNum}个商品"
                } else {

                    for (i in 0 until layout_goods.childCount) {
                        if (i > defaultShowNum - 1) {
                            layout_goods.getChildAt(i).visibility = View.VISIBLE
                        }
                    }
                    tv_toggle.text = "收起"
                }
            }
        } else {
            group_toggle.visibility = View.GONE
        }

        goodsModels.forEach {
            val view = layoutInflater.inflate(R.layout.item_available_goods, layout_goods, false)
            view.findViewById<TextView>(R.id.tv_goods_name).text = it.goodsName
            view.findViewById<TextView>(R.id.tv_goods_effect).text = it.goodsEffect
            view.findViewById<TextView>(R.id.tv_goods_price).text = "¥${it.price}"
            val tvDiscount = view.findViewById<TextView>(R.id.tv_goods_discount)
            if (it.discount == 0.0) {
                tvDiscount.visibility = View.GONE
            } else {
                tvDiscount.visibility = View.VISIBLE
                tvDiscount.text = "${it.discount}折"
            }
            layout_goods.addView(view)
        }
        for (i in 0 until layout_goods.childCount) {
            if (i > defaultShowNum - 1) {
                layout_goods.getChildAt(i).visibility = View.GONE
            }
        }
    }
}