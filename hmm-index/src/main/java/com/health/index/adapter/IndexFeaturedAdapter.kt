package com.health.index.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.banner.ViewPager2Banner
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.AppIndexSelected
import com.healthy.library.utils.TransformUtil

/**
 * author : long
 * Time   :2021/12/14
 * desc   : 首页为你精选
 */
class IndexFeaturedAdapter : BaseAdapter<AppIndexSelected>(R.layout.index_featured_layout) {

    private var mBannerAdapter: IndexFeaturedBannerAdapter? = null
    private var mItemHeight = 0

    private lateinit var listener: (mViewHeight: Int,className:String) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int,className:String) -> Unit) {
        this.listener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return 6
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.marginTop = TransformUtil.newDp2px(LibApplication.getAppContext(), 12f)
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(),10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(),10f)
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val appIndexSelected=datas[position]
        val mBanner = holder.getView<RecyclerView>(R.id.banner)
        if (mBannerAdapter == null) {
            mBannerAdapter = IndexFeaturedBannerAdapter()
            mBanner.apply {
                this.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = mBannerAdapter
            }
        }
        mBannerAdapter?.setNewData(appIndexSelected.list)
        holder.setText(R.id.tv_title,appIndexSelected?.title?.mainTitle)
        holder.setText(R.id.tv_subtitle,appIndexSelected?.title?.subTitle)
        if(::listener.isInitialized && mItemHeight == 0){
            holder.itemView.post{
                mItemHeight = holder.itemView.height + TransformUtil.newDp2px(LibApplication.getAppContext(), 12f)
                listener.invoke(mItemHeight,this.javaClass.simpleName)
            }
        }
    }


}