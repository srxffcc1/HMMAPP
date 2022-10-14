package com.health.index.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.model.VideoListModel
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.ScreenUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.ImageTextView
import com.healthy.library.widget.StaggeredGridLayoutHelperFix

/**
 * 创建日期：2021/12/23 10:10
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchVideoAdapter :
    BaseAdapter<VideoListModel>(R.layout.item_index_search_video_adapter_layout) {

    private var likeNormal: Drawable? = null
    private var likeSelect: Drawable? = null
    var imageScalemap= mutableMapOf<String, String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        likeNormal =
            parent.context.resources.getDrawable(com.healthy.library.R.drawable.cityrightlike)
        likeSelect =
            parent.context.resources.getDrawable(com.healthy.library.R.drawable.cityrightliker)
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseHolder, @SuppressLint("RecyclerView") position: Int) {
        var like = holder.getView<ImageTextView>(R.id.like)
        val mallGoodsImg = holder.getView<ImageView>(R.id.videoImg)
        if (!TextUtils.isEmpty(imageScalemap[datas[position].photo])) {
            val value = imageScalemap[datas[position].photo]
            val height = value!!.split(":".toRegex()).toTypedArray()[0].toInt()
            val swidth = value!!.split(":".toRegex()).toTypedArray()[1].toInt()
            val layoutParams = mallGoodsImg.getLayoutParams()
            layoutParams.height = height
            layoutParams.width = swidth
            mallGoodsImg.setLayoutParams(layoutParams)
            //System.out.println("直接设置大小" + position);
            Glide.with(context).load(datas[position].photo)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(mallGoodsImg)
        } else {
            Glide.with(context).load(datas[position].photo)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(object : SimpleTarget<Drawable?>() {


                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        val swidth = (ScreenUtils.getScreenWidth(context) / 2-TransformUtil.dp2px(context,10f)).toInt()
                        val height =
                            (resource.intrinsicHeight * 1.0 / resource.intrinsicWidth * swidth).toInt()
                        val layoutParams = mallGoodsImg.getLayoutParams()
                        layoutParams.height = height
                        layoutParams.width = swidth
                        imageScalemap[datas[position].photo]="$height:$swidth"
                        mallGoodsImg.setLayoutParams(layoutParams)
                        GlideCopy.with(context).load(resource).into(mallGoodsImg)
                    }
                })
        }
        holder.setText(R.id.videoTitle, datas[position].videoName)
        holder.setText(
            R.id.tv_lookNum,
            (datas[position].realView + datas[position].virtualView).toString() + "人已看"
        )
        if (datas[position].praise) {
            like.setTextColor(Color.parseColor("#F93F60"))
            like.setDrawable(likeSelect)
        } else {
            like.setDrawable(likeNormal)
            like.setTextColor(Color.parseColor("#444444"))
        }
        like.text = datas[position].praiseCount.toString()
        like.setOnClickListener {
            if (moutClickListener != null) {
                val arr = arrayOf(
                    datas[position].id,
                    datas[position].getVideoPraiseType(),
                    position.toString()
                )
                moutClickListener.outClick("like", arr)
            }
        }
        holder.itemView.setOnClickListener {
            ARouter.getInstance()
                .build(IndexRoutes.INDEX_HANMOMVIDEODETAIL)
                .withString("id", datas[position].id)
                .withString("categoryCode", datas[position].categoryCode)
                .navigation()
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val payload = payloads[0] as String
            if (payload == "like") {
                var videoListModel = datas[position]
                var like = holder.getView<ImageTextView>(R.id.like)
                like.text = videoListModel.praiseCount.toString()
                if (videoListModel.praise) {
                    like.setTextColor(Color.parseColor("#F93F60"))
                    like.setDrawable(likeSelect)
                } else {
                    like.setDrawable(likeNormal)
                    like.setTextColor(Color.parseColor("#444444"))
                }
            }
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val helper = StaggeredGridLayoutHelperFix()
        if (itemCount > 2) {
//            helper.setMargin(
//                TransformUtil.newDp2px(LibApplication.getAppContext(), 5f),
//                TransformUtil.newDp2px(LibApplication.getAppContext(), 10f),
//                TransformUtil.newDp2px(LibApplication.getAppContext(), 5f),
//                0
//            )
        }else{
//            helper.marginLeft= TransformUtil.newDp2px(LibApplication.getAppContext(), 5f)
//            helper.marginRight= TransformUtil.newDp2px(LibApplication.getAppContext(), 5f)
        }
        helper.lane = 2
        helper.hGap = 3
        return helper
    }
}