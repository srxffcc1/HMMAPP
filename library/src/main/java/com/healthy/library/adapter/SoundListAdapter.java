package com.healthy.library.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.healthy.library.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.SoundAlbum;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class SoundListAdapter extends BaseAdapter<SoundAlbum> {

    String audioType;

    public SoundListAdapter(String audioType) {
        this(R.layout.sound_item_edition);
        this.audioType=audioType;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final SoundAlbum soundAlbum = getDatas().get(position);
         CornerImageView soundEdititonIcon;
         TextView soundEdititonTitle;
         TextView soundEdititonCotent;
         ImageTextView soundEditionPlays;
         ImageTextView soundEditionBook;
         ImageView soundEditionPlay;

        soundEdititonIcon = (CornerImageView)holder.itemView. findViewById(R.id.soundEdititonIcon);
        soundEdititonTitle = (TextView) holder.itemView.findViewById(R.id.soundEdititonTitle);
        soundEdititonCotent = (TextView) holder.itemView.findViewById(R.id.soundEdititonCotent);
        soundEditionPlays = (ImageTextView) holder.itemView.findViewById(R.id.soundEditionPlays);
        soundEditionBook = (ImageTextView) holder.itemView.findViewById(R.id.soundEditionBook);
        soundEditionPlay = (ImageView) holder.itemView.findViewById(R.id.soundEditionPlay);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SoundRoutes.SOUND_DETAIL)
                        .withString("id",soundAlbum.id+"")
                        .withString("audioType",audioType)
                        .navigation();
            }
        });
        com.healthy.library.businessutil.GlideCopy.with(soundEdititonIcon.getContext())
                .load(soundAlbum.cover_url)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(soundEdititonIcon);
        soundEdititonTitle.setText(soundAlbum.album_title);
        soundEdititonCotent.setText(soundAlbum.recommend_reason);
        soundEditionPlays.setText(ParseUtils.parseNumber(soundAlbum.play_count+"",10000,"万"));
        soundEditionBook.setText(soundAlbum.include_track_count+"");
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private SoundListAdapter(int layoutResId) {
        super(layoutResId);

    }

    private void initView() {

    }
}
