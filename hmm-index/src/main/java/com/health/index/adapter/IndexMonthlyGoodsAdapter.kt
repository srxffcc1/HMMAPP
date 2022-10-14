package com.health.index.adapter

import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.model.ActGoodsItem
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.routes.ServiceRoutes
import com.healthy.library.utils.FormatUtils
import com.healthy.library.utils.ScreenUtils
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.RoundedImageView

class IndexMonthlyGoodsAdapter :
    BaseAdapter<String>(R.layout.index_item_monthly_goods_adapter) {

    private var records: MutableList<ActGoodsItem>? = null

    public fun setAdapterData(data: MutableList<ActGoodsItem>) {
        this.records = data
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        var goodsGrid = holder.getView<GridLayout>(R.id.goodsGrid)
        holder.setOnClickListener(R.id.more, View.OnClickListener {
            if (isClickInit) {
                moutClickListener.outClick("跳转搜索商品", null)
            }
        })
        holder.setOnClickListener(R.id.moreText, View.OnClickListener {
            if (isClickInit) {
                moutClickListener.outClick("跳转搜索商品", null)
            }
        })
        goodsGrid.removeAllViews()
        if (!ListUtil.isEmpty(records)) {
            if (records!!.size > 2) {
                records = records!!.subList(0, 2)
            }
            goodsGrid.post {
                val column = 2
                var needFixSize = 0
                if (records!!.size == 1 || records!!.size == 3) {
                    needFixSize = 1
                }
                val mMargin = TransformUtil.dp2px(context, 60f).toInt()
                goodsGrid.columnCount = column
                val w = (ScreenUtils.getScreenWidth(context) - mMargin) / 2
                for ((index, e) in records!!.withIndex()) {
                    var view = LayoutInflater.from(context)
                        .inflate(R.layout.item_index_search_all_goods_adapter_layout, goodsGrid,false)
                    var goodsImg = view.findViewById<RoundedImageView>(R.id.goodsImg)
                    var goodsTitle = view.findViewById<TextView>(R.id.goodsTitle)
                    var goods_moneyFlag = view.findViewById<TextView>(R.id.goods_moneyFlag)
                    var goods_moneyValue = view.findViewById<TextView>(R.id.goods_moneyValue)
                    var goods_moneyOld = view.findViewById<TextView>(R.id.goods_moneyOld)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        val param = GridLayout.LayoutParams(
                            GridLayout.spec(
                                GridLayout.UNDEFINED, GridLayout.FILL, 1f
                            ),
                            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                        )
                        view.layoutParams = param
                    }

                    val layoutParams = goodsImg.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.width = w
                    goodsImg.layoutParams = layoutParams
                    com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(e.filePath)

                        .into(goodsImg)
                    goodsTitle.text = e.goodsTitle
                    goods_moneyFlag.text = "¥"
                    goods_moneyValue.text = FormatUtils.moneyKeep2Decimals(e.platformPrice)
                    goods_moneyOld.text = FormatUtils.moneyKeep2Decimals(e.storePrice)
                    goods_moneyOld.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG//中划线

                    view.setOnClickListener {

                            var shopId = e.shopId
                            if (shopId == null || shopId == "0") {
                                shopId = SpUtils.getValue(context, SpKey.CHOSE_SHOP)
                            }
                            ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", e.goodsId.toString())
                                .withString("shopId", shopId)
                                .navigation()
                    }
                    goodsGrid.addView(view)
                }
                if (needFixSize > 0) {
                    for (index in 1..needFixSize) {
                        var view = LayoutInflater.from(context)
                            .inflate(R.layout.item_index_search_all_goods_adapter_layout, null)
                        view.visibility = View.INVISIBLE
                        goodsGrid.addView(view)
                    }
                }
            }
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

}