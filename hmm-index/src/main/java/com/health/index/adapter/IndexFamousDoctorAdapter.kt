package com.health.index.adapter

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.utils.TransformUtil

/**
 * author : long
 * Time   :2021/12/20
 * desc   :
 */
class IndexFamousDoctorAdapter : BaseAdapter<String>(R.layout.index_famous_doctor_item_layout) {

    override fun getItemViewType(position: Int): Int {
        return 17
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.bgColor = Color.WHITE
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        val mTvReply = holder.getView<TextView>(R.id.tv_reply)
        val mTvFavorableComment = holder.getView<TextView>(R.id.tv_favorableComment)

        val mIvAvatar = holder.getView<ImageView>(R.id.iv_avatar)

        Glide.with(mIvAvatar.context)
            .load(R.drawable.tx10)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(mIvAvatar)

        mTvReply.text = HtmlCompat.fromHtml(
            "已回复：<font color=\"#3E7EFF\">46235</font>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        mTvFavorableComment.text = HtmlCompat.fromHtml(
            "好评率：<font color=\"#FF5100\">99.8%</font>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
    }

}