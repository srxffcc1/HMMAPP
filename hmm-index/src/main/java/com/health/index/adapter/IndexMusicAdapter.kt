package com.health.index.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.AppIndexCustomRecommandAll
import com.healthy.library.model.SoundAlbum
import com.healthy.library.routes.SoundRoutes
import com.healthy.library.utils.TransformUtil

/**
 * author : long
 * Time   :2021/12/15
 * desc   :
 */
class IndexMusicAdapter : BaseAdapter<AppIndexCustomRecommandAll>(R.layout.index_music_layout) {

    private var mBannerAdapter: IndexMusicBannerAdapter? = null

    override fun getItemViewType(position: Int): Int {
        return 11
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.marginTop = TransformUtil.newDp2px(LibApplication.getAppContext(), 12f);
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val appIndexCustomRecommandAll = model
        val mBanner = holder.getView<RecyclerView>(R.id.banner)
        if (mBannerAdapter == null) {
            mBannerAdapter = IndexMusicBannerAdapter()
            mBanner.apply {
                this.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL, false
                )
                adapter = mBannerAdapter
                appIndexCustomRecommandAll.xmly?.apply {
                    try {
                        if (this.values != null) {
                            val mList = mutableListOf<SoundAlbum>()
                            mList.clear()
                            mList.addAll(appIndexCustomRecommandAll.xmly.values)
                            mBannerAdapter?.setNewData(mList)
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        }
        holder.setOnClickListener(R.id.musicMore, View.OnClickListener {
            ARouter.getInstance()
                .build(SoundRoutes.SOUND_MAIN_MON)
                .withString("audioType", "2")
                .navigation()
        })
    }

}