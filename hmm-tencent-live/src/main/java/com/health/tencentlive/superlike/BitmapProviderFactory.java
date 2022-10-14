package com.health.tencentlive.superlike;

import android.content.Context;

import com.health.tencentlive.R;


public class BitmapProviderFactory {

    public static BitmapProvider.Provider getHDProvider(Context context) {
        return new BitmapProvider.Builder(context)
                .setDrawableArray(new int[]{
                        R.drawable.like_heart0, R.drawable.like_heart1, R.drawable.like_heart2, R.drawable.like_heart3,
                        R.drawable.like_heart4, R.drawable.like_heart5, R.drawable.like_heart7, R.drawable.like_heart8,
                        R.drawable.like_heart9, R.drawable.like_heart10, R.drawable.like_heart11, R.drawable.like_heart12,
                        R.drawable.like_heart13, R.drawable.like_heart14})
                .build();
    }


    public static BitmapProvider.Provider getSmoothProvider(Context context) {
        return new BitmapProvider.Builder(context).build();
    }

}
