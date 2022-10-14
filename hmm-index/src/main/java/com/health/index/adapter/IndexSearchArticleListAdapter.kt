package com.health.index.adapter

import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.health.index.model.IndexAllSee
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.constant.UrlKeys
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.SpUtils

/**
 * 创建日期：2021/12/15 10:34
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchArticleListAdapter :
    BaseAdapter<IndexAllSee>(R.layout.item_index_search_hanmom_article_list_adapter_layout) {

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.setText(R.id.title, datas[position].title)
        if (datas[position].images != null) {
            holder.setImg(R.id.iv_avatar, datas[position].images)
        }
        holder.setText(
            R.id.seeNum,
            (datas[position].readQuantity + datas[position].fictitiousReadQuantity).toString()
        )
        holder.setText(
            R.id.collectionNum,
            (datas[position].collectionQuantity + datas[position].fictitiousCollectionQuantity).toString()
        )
        holder.itemView.setOnClickListener {
            val urlPrefix = SpUtils.getValue(context, UrlKeys.H5_KNOWLEDGE)
            val url = String.format("%s?id=%s&scheme=NewsMessage", urlPrefix, datas[position].id)
            ARouter.getInstance()
                .build(IndexRoutes.INDEX_WEBVIEWARTICLE)
                .withString("title", "资讯")
                .withString("url", url)
                .withBoolean("needShare", true)
                .withBoolean("isinhome", true)
                .withBoolean("needfindcollect", true)
                .navigation()
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }
}