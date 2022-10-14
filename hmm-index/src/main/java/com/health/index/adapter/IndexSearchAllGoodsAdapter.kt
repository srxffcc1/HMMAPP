package com.health.index.adapter

import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.adapter.IndexSpeedyConsultationAdapter
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.model.SortGoodsListModel
import com.healthy.library.model.VideoListModel
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.routes.ServiceRoutes
import com.healthy.library.utils.*
import com.healthy.library.widget.AutoPollRecyclerView
import com.healthy.library.widget.CornerImageView
import com.healthy.library.widget.RoundedImageView
import kotlinx.android.synthetic.main.fragment_search_child.*
import java.util.ArrayList

/**
 * 创建日期：2022/1/4 14:17
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchAllGoodsAdapter :
    BaseAdapter<String>(R.layout.index_search_all_goods_adapter_layout) {
    private var key: String? = null
    private var records: MutableList<SortGoodsListModel>? = null

    public fun setKey(key: String) {
        this.key = key
    }

    public fun setAdapterData(data: MutableList<SortGoodsListModel>) {
        this.records = data
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        var more = holder.getView<AppCompatImageView>(R.id.more)
        var goodsGrid = holder.getView<GridLayout>(R.id.goodsGrid)
        more.setOnClickListener {
            if (isClickInit) {
                moutClickListener.outClick("跳转搜索商品", null)
            }
        }
        holder.setText(R.id.keyTitle, key)
        goodsGrid.removeAllViews()
        if (!ListUtil.isEmpty(records)) {
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
                        .inflate(R.layout.item_index_search_all_goods_adapter_layout, null)
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
                        .load(e.headImage)
                        
                        .into(goodsImg)
                    goodsTitle.text = e.goodsTitle
                    goods_moneyFlag.text = "¥"
                    goods_moneyValue.text = FormatUtils.moneyKeep2Decimals(e.platformPrice)
                    goods_moneyOld.text = FormatUtils.moneyKeep2Decimals(e.storePrice)
                    goods_moneyOld.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG//中划线

                    view.setOnClickListener {
                        if(e.differentSymbol==0){
                            var shopId = e.shopId
                            if (shopId == null || shopId == "0") {
                                shopId = SpUtils.getValue(context, SpKey.CHOSE_SHOP)
                            }
                            ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", e.id.toString())
                                .withString("shopId", shopId)
                                .navigation()
                        }else{
                            ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                .withString("id", e.id.toString() )
                                .withString("shopId", e.shopId)
                                .navigation()
                        }
                    }
                    goodsGrid.addView(view)
                }
                if (needFixSize > 0) {
                    for (index in 1..needFixSize) {
                        var view = LayoutInflater.from(context)
                            .inflate(R.layout.item_index_search_goods_adapter_layout, null)
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

