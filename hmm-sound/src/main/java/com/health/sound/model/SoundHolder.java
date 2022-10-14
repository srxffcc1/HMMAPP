package com.health.sound.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.health.sound.R;
import com.healthy.library.model.SoundAlbum;
import com.yhao.floatwindow.FloatWindow;

public class SoundHolder {
    public LinearLayout floatCloseLL;
    public ImageView floatClose;
    public ProgressBar soundProgress;
    public LinearLayout floatSoundControlLL;
    public ImageView floatSoundPre;
    public ImageView floatSoundPlay;
    public ImageView floatSoundNext;
    public TextView floatName;
    public SoundAlbum soundAlbum;

    public SoundHolder setSoundAlbum(SoundAlbum soundAlbum) {
        this.soundAlbum = soundAlbum;
        if(soundAlbum!=null){
            soundAlbum.setStop(false);
        }
        return this;
    }

    private static final SoundHolder instance=new SoundHolder();
    public static SoundHolder getInstance(){
        return instance;
    }

    private SoundHolder() {
        try {
            View soundview=FloatWindow.get().getView();
            floatName=soundview.findViewById(R.id.floatName);
            floatCloseLL = (LinearLayout) soundview.findViewById(R.id.floatCloseLL);
            floatClose = (ImageView) soundview.findViewById(R.id.floatClose);
            soundProgress = (ProgressBar) soundview.findViewById(R.id.soundProgress);
            floatSoundControlLL = (LinearLayout) soundview.findViewById(R.id.floatSoundControlLL);
            floatSoundPre = (ImageView) soundview.findViewById(R.id.floatSoundPre);
            floatSoundPlay = (ImageView) soundview.findViewById(R.id.floatSoundPlay);
            floatSoundNext = (ImageView) soundview.findViewById(R.id.floatSoundNext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
