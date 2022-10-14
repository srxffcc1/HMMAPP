package com.health.sound.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.sound.R;
import com.health.sound.model.SoundHistory;
import com.health.sound.model.SoundHolder;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.SoundAlbum;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.widget.CornerImageView;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.yhao.floatwindow.FloatWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class SoundListHistoryAdapter extends BaseAdapter<SoundHistory> {


    public  String audioType;

    public SoundListHistoryAdapter(String audioType) {
        this(R.layout.sound_item_edition_again);
        this.audioType=audioType;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final SoundHistory soundHistory = getDatas().get(position);
         ImageView soundEditionPlayAgainImg;
         final TextView soundEditionPlayAgainText;
        CornerImageView soundEdititonIcon;
        TextView soundEdititonTitle;
        TextView soundEdititonCotent;
        TextView soundEditionCount;
        TextView soundEditionOld;
        LinearLayout soundEditionPlayAgain;
        soundEdititonIcon = (CornerImageView) holder.itemView.findViewById(R.id.soundEdititonIcon);
        soundEdititonTitle = (TextView) holder.itemView.findViewById(R.id.soundEdititonTitle);
        soundEdititonCotent = (TextView) holder.itemView.findViewById(R.id.soundEdititonCotent);
        soundEditionCount = (TextView) holder.itemView.findViewById(R.id.soundEditionCount);
        soundEditionOld = (TextView) holder.itemView.findViewById(R.id.soundEditionOld);
        soundEditionPlayAgain = (LinearLayout) holder.itemView.findViewById(R.id.soundEditionPlayAgain);
        soundEditionPlayAgainImg = (ImageView) holder.itemView.findViewById(R.id.soundEditionPlayAgainImg);
        soundEditionPlayAgainText = (TextView) holder.itemView.findViewById(R.id.soundEditionPlayAgainText);
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(soundHistory.coverUrlMiddle)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                
                .into(soundEdititonIcon);

        soundEdititonTitle.setText(soundHistory.albumTitle);
        soundEdititonCotent.setText(soundHistory.recommendReason);
        soundEditionOld.setText(soundHistory.title);
        soundEditionCount.setText("当前播放:");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Track track = new Track();
//                track.setKind(PlayableModel.KIND_TRACK);
//                track.setTrackTitle(soundHistory.title);
//                track.setDataId(soundHistory.tracksId);
//                List<Track> tracks = new ArrayList<Track>();
//                tracks.add(track);
//                XmPlayerManager.getInstance(context).playList(tracks ,0);
                ARouter.getInstance()
                        .build(SoundRoutes.SOUND_DETAIL)
                        .withString("id", soundHistory.albumsId + "")
                        .withString("audioType",audioType)
                        .navigation();
            }
        });
        final SoundAlbum soundAlbum=new SoundAlbum();
        soundAlbum.album_title=soundHistory.albumTitle;
        soundAlbum.id=soundHistory.albumsId;
        soundAlbum.recommend_reason=soundHistory.recommendReason;
        final Track track = new Track();
        track.setCoverUrlLarge(soundHistory.coverUrlMiddle);
        track.setCoverUrlMiddle(soundHistory.coverUrlMiddle);
        track.setCoverUrlSmall(soundHistory.coverUrlMiddle);
        track.setKind(PlayableModel.KIND_TRACK);
        track.setDataId(soundHistory.tracksId);
        track.setTrackTitle(soundHistory.title);
        PlayableModel currSound = XmPlayerManager.getInstance(context).getCurrSound();
        boolean isneedStop= true;
        try {
            isneedStop = SoundHolder.getInstance().soundAlbum.isStop();
        } catch (Exception e) {
            isneedStop=false;
            e.printStackTrace();
        }
        if (track.equals(currSound) && XmPlayerManager.getInstance(context).getPlayerStatus() != PlayerConstants.STATE_PAUSED&&!isneedStop) {
            try {
                FloatWindow.get().show();
                System.out.println("喜马拉雅悬浮出现5");
            } catch (Exception e) {
                e.printStackTrace();
            }

            soundEditionPlayAgainImg.setImageResource(R.drawable.ic_sound_again_pause);
            soundEditionPlayAgainText.setText("暂停播放");
//            soundEdititonTitle.setTextColor(Color.parseColor("#4DC9D8"));
//            soundEditionPlay.setImageResource(R.drawable.ic_sound_pause);
        } else {
//            soundEdititonTitle.setTextColor(Color.parseColor("#ff333333"));
//            soundEditionPlay.setImageResource(R.drawable.ic_sound_play);
            soundEditionPlayAgainImg.setImageResource(R.drawable.ic_sound_again_p);
            soundEditionPlayAgainText.setText("继续播放");
        }
        soundEditionPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundEditionPlayAgainText.getText().toString().equals("继续播放")){
                    List<Track> tracks = new ArrayList<Track>();
                    tracks.add(track);
                    SoundHolder.getInstance().setSoundAlbum(soundAlbum);
                    XmPlayerManager.getInstance(context).playList(tracks, 0);
                    try {
                        FloatWindow.get().show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    XmPlayerManager.getInstance(context).pause();
                }
                notifyDataSetChanged();
//                ARouter.getInstance()
//                        .build(SoundRoutes.SOUND_DETAIL)
//                        .withString("id",soundHistory.albumsId+"")
//                        .navigation();


            }
        });
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private SoundListHistoryAdapter(int layoutResId) {
        super(layoutResId);

    }

    private void initView() {


    }
}
