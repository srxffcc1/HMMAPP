package com.health.sound.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.sound.R;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class SoundListSeachAdapter extends BaseQuickAdapter<com.ximalaya.ting.android.opensdk.model.album.Album, BaseViewHolder> {


     public  String audioType;

    @Override
    protected void convert(BaseViewHolder helper, com.ximalaya.ting.android.opensdk.model.album.Album item) {
        final int position=helper.getPosition();
        final com.ximalaya.ting.android.opensdk.model.album.Album soundAlbum = item;
        CornerImageView soundEdititonIcon;
        TextView soundEdititonTitle;
        TextView soundEdititonCotent;
        ImageTextView soundEditionPlays;
        ImageTextView soundEditionBook;
        ImageView soundEditionPlay;

        soundEdititonIcon = (CornerImageView)helper.itemView. findViewById(R.id.soundEdititonIcon);
        soundEdititonTitle = (TextView) helper.itemView.findViewById(R.id.soundEdititonTitle);
        soundEdititonCotent = (TextView) helper.itemView.findViewById(R.id.soundEdititonCotent);
        soundEditionPlays = (ImageTextView) helper.itemView.findViewById(R.id.soundEditionPlays);
        soundEditionBook = (ImageTextView) helper.itemView.findViewById(R.id.soundEditionBook);
        soundEditionPlay = (ImageView) helper.itemView.findViewById(R.id.soundEditionPlay);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SoundRoutes.SOUND_DETAIL)
                        .withString("id",soundAlbum.getId()+"")
                        .withString("audioType",audioType)
                        .navigation();
            }
        });
        com.healthy.library.businessutil.GlideCopy.with(soundEdititonIcon.getContext())
                .load(soundAlbum.getCoverUrlMiddle())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(soundEdititonIcon);
        soundEdititonTitle.setText(soundAlbum.getAlbumTitle());
        soundEdititonCotent.setText(soundAlbum.getRecommendReason());
        soundEditionPlays.setText(ParseUtils.parseNumber(soundAlbum.getPlayCount()+"",10000,"万"));
        soundEditionBook.setText(soundAlbum.getIncludeTrackCount()+"");
    }

    public SoundListSeachAdapter(String audioType) {
        this(R.layout.sound_item_edition);
        this.audioType=audioType;
    }

    private SoundListSeachAdapter(int layoutResId) {
        super(layoutResId);

    }
}
