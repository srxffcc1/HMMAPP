package com.healthy.library.misc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.healthy.library.LibApplication;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.model.AppIndexCustomItem;

public class StateListDrawableBuilder {
    private static final int[] STATE_NORMAL = {-android.R.attr.state_selected};
    private static final int[] STATE_SELECTED = {android.R.attr.state_selected};
    StateListDrawable stateListDrawable;

    public StateListDrawableBuilder(Context context,AppIndexCustomItem appIndexCustomItem, int selectedRes, int normalRes) {
        this();
        this.appIndexCustomItem = appIndexCustomItem;
//        stateListDrawable.addState(STATE_NORMAL, LibApplication.getAppContext().getResources().getDrawable(normalRes));
//        stateListDrawable.addState(STATE_SELECTED, LibApplication.getAppContext().getResources().getDrawable(selectedRes));
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(appIndexCustomItem.darkIconUrl)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Drawable result=resource.getConstantState().newDrawable();
                        setSelectorDrawable(STATE_NORMAL,result);

                    }
                });
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(appIndexCustomItem.lightIconUrl)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Drawable result=resource.getConstantState().newDrawable();
                        setSelectorDrawable(STATE_SELECTED,result);

                    }
                });
    }

    AppIndexCustomItem appIndexCustomItem;
    public StateListDrawableBuilder() {
        stateListDrawable=new StateListDrawable();
    }
    public StateListDrawableBuilder setSelectorDrawable(int[] stateSet,Drawable drawable){
            stateListDrawable.addState(stateSet,drawable);
            return this;
    }
    public StateListDrawableBuilder setSelectorDrawable(int[] stateSet,int drawableRes){
        stateListDrawable.addState(stateSet, LibApplication.getAppContext().getResources().getDrawable(drawableRes));
        return this;
    }
    public StateListDrawable build(){
        return stateListDrawable;
    }

}
