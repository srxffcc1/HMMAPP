package com.ksyun.player.now.model;

import android.content.Context;

import com.ksyun.media.player.KSYTextureView;

public class FloatingPlayer {
    private KSYTextureView mKsyTextureView;
    private static FloatingPlayer _instance;

    private FloatingPlayer() {}

    public static FloatingPlayer getInstance() {
        if (_instance == null) {
            synchronized (FloatingPlayer.class) {
                if (_instance == null) {
                    _instance = new FloatingPlayer();
                }
            }
        }

        return _instance;
    }

    public void init(final Context context) {
        if (mKsyTextureView != null) {
            mKsyTextureView.release();
            //System.out.println("释放播放器");
            mKsyTextureView = null;
        }
        mKsyTextureView=new KSYTextureView(context);
    }
    public KSYTextureView getKSYTextureView() {
        return mKsyTextureView;
    }

    public void destroy() {
        if (mKsyTextureView != null){

            //System.out.println("释放播放器2");
            mKsyTextureView.release();
        }

        mKsyTextureView = null;
    }
}
