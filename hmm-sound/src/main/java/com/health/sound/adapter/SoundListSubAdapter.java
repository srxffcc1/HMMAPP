package com.health.sound.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.sound.R;
import com.health.sound.model.SoundHistory;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.widget.CornerImageView;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class SoundListSubAdapter extends BaseAdapter<SoundHistory> {

    String audioType;


    public SoundListSubAdapter(String audioType) {
        this(R.layout.sound_item_edition_again_sub);
        this.audioType=audioType;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final SoundHistory soundHistory= getDatas().get(position);
        CornerImageView soundEdititonIcon;
        TextView soundEdititonTitle;
        TextView soundEdititonCotent;
        TextView soundEditionCount;
        TextView soundEditionOld;
        LinearLayout soundEditionPlayAgain;
        soundEdititonIcon = (CornerImageView) holder.itemView.findViewById(R.id.soundEdititonIcon);
        soundEdititonTitle = (TextView)holder.itemView. findViewById(R.id.soundEdititonTitle);
        soundEdititonCotent = (TextView)holder.itemView. findViewById(R.id.soundEdititonCotent);
        soundEditionCount = (TextView)holder.itemView. findViewById(R.id.soundEditionCount);
        soundEditionOld = (TextView) holder.itemView.findViewById(R.id.soundEditionOld);
        soundEditionPlayAgain = (LinearLayout) holder.itemView.findViewById(R.id.soundEditionPlayAgain);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SoundRoutes.SOUND_DETAIL)
                        .withString("id",soundHistory.albumsId+"")
                        .withString("audioType",audioType)
                        .navigation();
            }
        });
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(soundHistory.coverUrlMiddle)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                
                .into(soundEdititonIcon);

        soundEdititonTitle.setText(soundHistory.albumTitle);
        soundEdititonCotent.setText(soundHistory.recommendReason);
//        soundEditionCount.setVisibility(View.INVISIBLE);
//        soundEditionCount.setWidth(1);
//        soundEditionCount.setTextColor(Color.WHITE);
        if(soundHistory.title!=null){

            soundEditionOld.setText(soundHistory.title);
        }
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private SoundListSubAdapter(int layoutResId) {
        super(layoutResId);

    }

}
