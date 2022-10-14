package com.health.sound.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.sound.R;
import com.health.sound.activity.SoundDeatilActivity;
import com.health.sound.model.SoundHolder;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.SoundAlbum;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.widget.ImageTextView;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.yhao.floatwindow.FloatWindow;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class SoundCellAdapter extends BaseAdapter<Track> {
    public void setSelectPage(long selectPage) {
        this.selectPage = selectPage;
    }

    public long selectPage=1;
    public SoundAlbum soundAlbum;

    public void setSoundAlbum(SoundAlbum soundAlbum) {
        this.soundAlbum = soundAlbum;
    }

    public SoundCellAdapter() {
        this(R.layout.sound_item_edition_cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseHolder holder, final int position) {
        final Track track= getDatas().get(position);
         TextView soundEdititonNum;
         TextView soundEdititonTitle;
         ImageTextView soundEditionPlays;
         ImageTextView soundEditionBook;
         ImageView soundEditionPlay;

        soundEdititonNum = (TextView) holder.itemView.findViewById(R.id.soundEdititonNum);
        soundEdititonTitle = (TextView) holder.itemView.findViewById(R.id.soundEdititonTitle);
        soundEditionPlays = (ImageTextView) holder.itemView.findViewById(R.id.soundEditionPlays);
        soundEditionBook = (ImageTextView) holder.itemView.findViewById(R.id.soundEditionBook);
        soundEditionPlay = (ImageView) holder.itemView.findViewById(R.id.soundEditionPlay);

        final PlayableModel currSound = XmPlayerManager.getInstance(context).getCurrSound();
        System.out.println("喜马拉雅悬浮出现状态:"+XmPlayerManager.getInstance(context).getPlayerStatus() );
        boolean isneedStop= true;
        try {
            isneedStop = SoundHolder.getInstance().soundAlbum.isStop();
        } catch (Exception e) {
            isneedStop=false;
            e.printStackTrace();
        }
        if(track.equals(currSound)&&XmPlayerManager.getInstance(context).getPlayerStatus() != PlayerConstants.STATE_PAUSED&&!isneedStop){
            try {
                FloatWindow.get().show();
                System.out.println("喜马拉雅悬浮出现3");
            } catch (Exception e) {
                e.printStackTrace();
            }
            soundEdititonTitle.setTextColor(Color.parseColor("#4DC9D8"));
            soundEditionPlay.setImageResource(R.drawable.ic_sound_pause);
        }else {
            //System.out.println("刷新播放状态4");
            soundEdititonTitle.setTextColor(Color.parseColor("#ff333333"));
            soundEditionPlay.setImageResource(R.drawable.ic_sound_play);
        }
        soundEdititonNum.setText((20*(selectPage-1)+1+position)+"");
        soundEdititonTitle.setText(track.getTrackTitle());
        soundEditionPlays.setText(ParseUtils.parseNumber(track.getPlayCount()+"",10000,"万"));
        String[] timetip= DateUtils.getDistanceTimeOnlySecond(track.getDuration()*1000);
        String timeresult="";
        if(!"00".equals(timetip[1])){
            timeresult=timeresult+timetip[1]+":";
        }
        timeresult=timeresult+timetip[2]+":";
//        if(!timetip[2].equals("00")){
//            timeresult=timeresult+timetip[2]+":";
//        }
        if(!"".equals(timetip[3])){
            timeresult=timeresult+timetip[3]+":";
        }
        if(timeresult.length()>0){

            timeresult=timeresult.substring(0,timeresult.length()-1);
        }
        soundEditionBook.setText(timeresult);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundDeatilActivity.initXMLY();
                holder.itemView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(track.equals(currSound)&&XmPlayerManager.getInstance(context).getPlayerStatus() != PlayerConstants.STATE_PAUSED&&XmPlayerManager.getInstance(context).getPlayerStatus() != PlayerConstants.STATE_STOPPED){
                            XmPlayerManager.getInstance(context).pause();
                        }else {
                            if(moutClickListener!=null){
                                moutClickListener.outClick("播放点击开始",null);
                            }
                            SoundHolder.getInstance().setSoundAlbum(soundAlbum);
                            XmPlayerManager.getInstance(context).playList(getDatas() ,position);
                            try {
                                FloatWindow.get().show();
                                System.out.println("喜马拉雅悬浮出现4");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        holder.itemView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        },300);
                    }
                },200);
            }
        });
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private SoundCellAdapter(int layoutResId) {
        super(layoutResId);

    }

    private void initView() {

    }
}
