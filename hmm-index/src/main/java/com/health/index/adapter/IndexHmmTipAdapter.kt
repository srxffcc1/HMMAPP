package com.health.index.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.AppIndexCustomRecommandAll
import com.healthy.library.model.TipPost
import com.healthy.library.model.TipPostOther
import com.healthy.library.routes.CityRoutes
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.ImageTextView

/**
 * author : long
 * Time   :2021/12/15
 * desc   :
 */
class IndexHmmTipAdapter : BaseAdapter<AppIndexCustomRecommandAll>(R.layout.index_hmm_tip_layout) {

    private var mBannerAdapter: IndexHmmTipBannerAdapter? = null

    override fun getItemViewType(position: Int): Int {
        return 12
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.marginTop = TransformUtil.newDp2px(LibApplication.getAppContext(),12f)
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(),10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(),10f)
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        val mBanner = holder.getView<RecyclerView>(R.id.banner)
        val talkMore = holder.getView<AppCompatImageView>(R.id.talkMore)
        val appIndexCustomRecommandAll=model
        if(mBannerAdapter == null){
            mBannerAdapter = IndexHmmTipBannerAdapter()
            mBanner.apply {
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(mBanner)
                this.layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL,false)
                adapter = mBannerAdapter
                val mList = mutableListOf<TipPostOther>()
                mList.clear()
                mList.addAll(appIndexCustomRecommandAll.getRealHotTopic())
                mBannerAdapter?.setNewData(mList)
            }
        }
        talkMore.setOnClickListener {
            ARouter.getInstance()
                .build(CityRoutes.CITY_TIPBOARD)
                .withString("fragmentType", "本地")
                .navigation()
        }
    }

}