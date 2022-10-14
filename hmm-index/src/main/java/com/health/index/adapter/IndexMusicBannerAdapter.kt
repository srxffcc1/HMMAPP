package com.health.index.adapter

import androidx.appcompat.widget.AppCompatTextView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.health.index.R
import com.healthy.library.base.BaseView
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.model.SoundAlbum
import com.healthy.library.routes.SoundRoutes
import com.healthy.library.utils.ParseUtils
import com.healthy.library.widget.CornerImageView
import com.healthy.library.widget.ImageTextView

/**
 * author : long
 * Time   :2021/12/15
 * desc   :
 */
class IndexMusicBannerAdapter :
    BaseQuickAdapter<SoundAlbum, BaseViewHolder>(R.layout.index_music_banner_layout) {

    override fun convert(helper: BaseViewHolder, item: SoundAlbum) {
        val ivAvatar: CornerImageView
        val tvMusicplay: ImageTextView
        val tvTitle: AppCompatTextView
        ivAvatar = helper.getView(R.id.iv_avatar)
        tvMusicplay = helper.getView(R.id.tv_musicplay)
        tvTitle = helper.getView(R.id.tv_title)
        GlideCopy.with(mContext)
            .load(item.cover_url)
            .placeholder(R.drawable.img_avatar_default)
            .error(R.drawable.img_avatar_default)

            .into(helper.getView(R.id.iv_avatar))
        tvTitle.setText(item.album_title)
        tvMusicplay.setText(
            ParseUtils.parseNumber(
                item.play_count.toString() + "",
                10000,
                "万"
            )
        )
        helper.itemView.setOnClickListener {
            ARouter.getInstance()
                .build(SoundRoutes.SOUND_DETAIL)
                .withString("id", item.id.toString() + "")
                .withString("audioType", "1")
                .navigation()
        }
    }
}