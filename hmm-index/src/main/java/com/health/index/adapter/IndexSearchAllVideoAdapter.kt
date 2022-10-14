package com.health.index.adapter

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
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
import com.healthy.library.model.VideoListModel
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.routes.ServiceRoutes
import com.healthy.library.utils.FormatUtils
import com.healthy.library.utils.ScreenUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.AutoPollRecyclerView
import com.healthy.library.widget.CornerImageView
import com.healthy.library.widget.ImageTextView
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

class IndexSearchAllVideoAdapter :
    BaseAdapter<String>(R.layout.index_search_all_video_adapter_layout) {
    private var key: String? = null
    private var records: MutableList<VideoListModel>? = null
    private var likeNormal: Drawable? = null
    private var likeSelect: Drawable? = null

    public fun setKey(key: String) {
        this.key = key
    }

    public fun setAdapterData(data: MutableList<VideoListModel>) {
        this.records = data
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        likeNormal =
            context.resources.getDrawable(com.healthy.library.R.drawable.cityrightlike)
        likeSelect =
            context.resources.getDrawable(com.healthy.library.R.drawable.cityrightliker)
        var more = holder.getView<AppCompatImageView>(R.id.more)
        var goodsGrid = holder.getView<GridLayout>(R.id.goodsGrid)
        more.setOnClickListener {
            if (isClickInit) {
                moutClickListener.outClick("跳转搜索短视频", null)
            }
        }
        holder.setText(R.id.keyTitle, key)
        goodsGrid.removeAllViews()
        if (!ListUtil.isEmpty(records)) {
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
                        .inflate(R.layout.item_index_search_all_video_adapter_layout, null)
                    var videoImg = view.findViewById<RoundedImageView>(R.id.videoImg)
                    var videoTitle = view.findViewById<TextView>(R.id.videoTitle)
                    var tv_lookNum = view.findViewById<TextView>(R.id.tv_lookNum)
                    var like = view.findViewById<ImageTextView>(R.id.like)
                    val layoutParams = videoImg.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.width = w
                    videoImg.layoutParams = layoutParams
                    com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(e.photo)

                        .into(videoImg)
                    videoTitle.text = e.videoName
                    tv_lookNum.text = (e.realView + e.virtualView).toString() + "人已看"
                    if (e.praise) {
                        like.setTextColor(Color.parseColor("#F93F60"))
                        like.setDrawable(likeSelect)
                    } else {
                        like.setDrawable(likeNormal)
                        like.setTextColor(Color.parseColor("#444444"))
                    }
                    like.text = e.praiseCount.toString()
                    like.setOnClickListener {
                        if (moutClickListener != null) {
                            val arr = arrayOf(
                                e.id,
                                e.getVideoPraiseType()
                            )
                            moutClickListener.outClick("like", arr)
                        }
                    }
                    view.setOnClickListener {
                        ARouter.getInstance()
                            .build(IndexRoutes.INDEX_HANMOMVIDEODETAIL)
                            .withString("id", e.id)
                            .withString("categoryCode", e.categoryCode)
                            .navigation()
                    }
                    goodsGrid.addView(view)
                }
                if (needFixSize > 0) {
                    for (index in 1..needFixSize) {
                        var view = LayoutInflater.from(context)
                            .inflate(R.layout.item_index_search_all_video_adapter_layout, null)
                        view.visibility = View.INVISIBLE
                        goodsGrid.addView(view)
                    }
                }
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }
}

