package com.health.index.adapter

import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.health.index.model.AiCategoryList
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.ScreenUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.CornerImageView

/**
 * 创建日期：2022/1/4 14:17
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexHealthTestMainAdapter :
    BaseAdapter<String>(R.layout.item_health_test_main_adapter_layout) {
    private var key: String? = null
    private var records: MutableList<AiCategoryList>? = null

    public fun setKey(key: String) {
        this.key = key
    }

    public fun setAdapterData(data: MutableList<AiCategoryList>) {
        this.records = data
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        var grid = holder.getView<GridLayout>(R.id.grid)
        holder.setText(R.id.top_title, "婴幼儿健康")
        grid.removeAllViews()
        if (!ListUtil.isEmpty(records)) {
            grid.post {
                val column = 5
                val mMargin = TransformUtil.dp2px(context, 20f).toInt()
                grid.columnCount = column
                val w = (ScreenUtils.getScreenWidth(context) - mMargin) / 5
                for ((index, e) in records!!.withIndex()) {
                    var view = LayoutInflater.from(context)
                        .inflate(R.layout.index_health_main_function, grid, false)
                    var iv_category = view.findViewById<CornerImageView>(R.id.iv_category)
                    var tv_category = view.findViewById<TextView>(R.id.tv_category)
                    val params = view.layoutParams
                    params.width = w
                    com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(e.imageUrl)
                        .error(R.drawable.img_avatar_default)
                        .placeholder(R.drawable.img_avatar_default)
                        .into(iv_category)
                    tv_category.text = e.problem
                    view.setOnClickListener {
                        ARouter.getInstance()
                            .build(IndexRoutes.INDEX_HEALTHTESTOPTION)
                            .withString("id", e.id.toString())
                            .withString("title", e.problem)
                            .navigation()
                    }
                    grid.addView(view, params)
                }
            }
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }
}

