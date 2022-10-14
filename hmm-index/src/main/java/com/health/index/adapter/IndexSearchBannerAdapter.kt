package com.health.index.adapter

import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.loader.ImageNetAdapter
import com.healthy.library.model.AdModel
import com.healthy.library.utils.MARouterUtils
import com.healthy.library.utils.TransformUtil
import com.youth.banner.Banner
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.listener.OnBannerListener
import java.util.*

/**
 * 创建日期：2021/12/15 10:34
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchBannerAdapter :
    BaseAdapter<String>(R.layout.item_index_search_banner_adapter_layout) {

    private var isStart = false
    private var adv: List<AdModel>? = null

    fun setAdv(adv: List<AdModel>) {
        this.adv = adv
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val banner = holder.getView<Banner<Any?, *>>(R.id.act_banner)
        buildBannerView(banner, adv)
    }

    private fun buildBannerView(banner: Banner<Any?, *>, imgList: List<AdModel?>?) {
        val urlList: MutableList<String> = ArrayList()
        val imageLoader: ImageNetAdapter
        if (!ListUtil.isEmpty(imgList)) {
            for (j in imgList!!.indices) {
                urlList.add(imgList[j]!!.photoUrls)
            }
            imageLoader = ImageNetAdapter(urlList, TransformUtil.dp2px(context, 10f), null)
            banner.adapter = imageLoader
            banner.setOnBannerListener(object : OnBannerListener<Any?> {
                override fun OnBannerClick(data: Any?, position: Int) {
                    val adModel = imgList[position]
                    MARouterUtils.passToTarget(context, adModel)
                }

                override fun onBannerChanged(position: Int) {
                }
            })
            if (!isStart) {
                isStart = true
                banner.stop()
                banner.start()
            }
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }
}