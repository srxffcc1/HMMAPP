package com.health.tencentlive.beautysettingkit;

import android.graphics.Bitmap;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.health.tencentlive.beautysettingkit.model.BeautyInfo;
import com.health.tencentlive.beautysettingkit.model.ItemInfo;
import com.health.tencentlive.beautysettingkit.model.TabInfo;
import com.tencent.liteav.beauty.TXBeautyManager;

public interface Beauty {

    void setBeautyManager(@NonNull TXBeautyManager beautyManager);
    void setBeautySpecialEffects(@NonNull TabInfo tabinfo, @IntRange(from = 0) int tabPosition, @NonNull ItemInfo itemInfo, @IntRange(from = 0) int itemPosition);
    void setBeautyStyleAndLevel(int style, int level);
    void setMotionTmplEnable(boolean enable);
    void fillingMaterialPath(@NonNull BeautyInfo beautyInfo);
    void setCurrentFilterIndex(@NonNull BeautyInfo beautyInfo, @IntRange(from = 0) int index);
    void setCurrentBeautyIndex(@NonNull BeautyInfo beautyInfo, @IntRange(from = 0) int index);
    void setOnFilterChangeListener(OnFilterChangeListener listener);
    void clear();
    int getFilterProgress(@NonNull BeautyInfo beautyInfo, @IntRange(from = 0) int index);
    int getFilterSize(@NonNull BeautyInfo beautyInfo);
    ItemInfo getFilterItemInfo(@NonNull BeautyInfo beautyInfo, @IntRange(from = 0) int index);
    Bitmap getFilterResource(@NonNull BeautyInfo beautyInfo, @IntRange(from = 0) int index);
    BeautyInfo getDefaultBeauty();

    interface OnFilterChangeListener {
        void onChanged(Bitmap filterImage, int index);
    }
}
