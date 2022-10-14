package com.health.second.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.second.R
import com.healthy.library.banner.ViewPager2Banner
import com.healthy.library.banner.BannerAdapter
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.GoodsDetail
import com.healthy.library.routes.LibraryRoutes

/**
 * @author Long
 * @desc: 服务详情 -> 头部banner
 * @createTime :2021/10/9 10:09
 */
class ServiceDetailBannerAdapter :
    BaseAdapter<GoodsDetail>(R.layout.item_service_detail_banner_layout) {

    var isLoadSuccess = false
    private lateinit var listener: (mViewHeight: Int) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int) -> Unit) {
        this.listener = listener
    }

    private var bannerAdapter: BannerAdapter? = null
    private var selectedPos: Int = 0
    private var mImgList = mutableListOf<String>()

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if(isLoadSuccess) return
        isLoadSuccess = true
        val mBanner = holder.setText(R.id.currentPage, "1")
            .getView<ViewPager2Banner>(R.id.banner)
        if (bannerAdapter == null) {
            model?.let {
                it.headImages.forEachIndexed { index, headImages ->
                    mImgList.add(headImages.filePath)
                }
            }
            holder.setText(R.id.countPage, "/${mImgList.size}")
            bannerAdapter = BannerAdapter()
            bannerAdapter?.setData(listOf<Any>(*mImgList.toTypedArray()))
            bannerAdapter?.setOnItemClickListener {
                ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                    .withCharSequenceArray("urls", mImgList.toTypedArray())
                    .withInt("pos", selectedPos)
                    .navigation()
            }

            mBanner.apply {
                isAutoPlay = true
                setAutoTurningTime(3000) //.addPageTransformer(new ScaleInTransformer())
                setOuterPageChangeListener(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        selectedPos = position
                        holder.setText(R.id.currentPage, (position + 1).toString())
                    }
                })
                adapter = bannerAdapter
            }

            if (::listener.isInitialized) {
                holder.itemView.post {
                    listener.invoke(holder.itemView.height)
                }
            }

        } else {
            bannerAdapter?.notifyDataSetChanged()
        }
    }
}