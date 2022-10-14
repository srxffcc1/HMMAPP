package com.healthy.library.loader;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.palette.graphics.Palette;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.healthy.library.model.ColorInfo;
import com.healthy.library.widget.CornerImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/11 11:22
 * @des 轮播图图片加载器
 */

public class BannerBgImageLoader {

    private float mRadius;
    private List<ColorInfo> colorList=new ArrayList<>();
    private Palette palette;
    private Context context;

    public BannerBgImageLoader() {
        this(0,null);
    }

    public BannerBgImageLoader(float radius, List<ColorInfo> colorList) {
        mRadius = radius;
        this.colorList = colorList;
    }

    public void displayImage(Context context, final Object imgObj, ImageView imageView) {
        com.healthy.library.businessutil.GlideCopy.with(context).asBitmap().load(imgObj.toString()).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                setColorList(resource, imgObj.toString());
                return false;
            }
        }).into(imageView);

    }

    public ImageView createImageView(Context context) {
        CornerImageView cornerImageView = new CornerImageView(context);
        cornerImageView.setCornerRadius(mRadius);
        return cornerImageView;
    }

    private void setColorList(Bitmap bitmap, String imgUrl) {
        if (colorList == null) {
            return;
        }
        palette = Palette.from(bitmap).generate();
        for (int i = 0; i < colorList.size(); i++) {
            if (colorList.get(i).getImgUrl().equals(imgUrl)) {// imgUrl作为识别标志
                if (palette.getVibrantSwatch() != null) {
                    colorList.get(i).setVibrantColor(palette.getVibrantSwatch().getRgb());
                }
                if (palette.getDarkVibrantSwatch() != null) {
                    colorList.get(i).setVibrantDarkColor(palette.getDarkVibrantSwatch().getRgb());
                }
                if (palette.getLightVibrantSwatch() != null) {
                    colorList.get(i).setVibrantLightColor(palette.getLightVibrantSwatch().getRgb());
                }
                if (palette.getMutedSwatch() != null) {
                    colorList.get(i).setMutedColor(palette.getMutedSwatch().getRgb());
                }
                if (palette.getDarkMutedSwatch() != null) {
                    colorList.get(i).setMutedDarkColor(palette.getDarkMutedSwatch().getRgb());
                }
                if (palette.getLightVibrantSwatch() != null) {
                    colorList.get(i).setMutedLightColor(palette.getLightVibrantSwatch().getRgb());
                }
            }
        }
    }

    /**
     * Vibrant （有活力）
     * Vibrant dark（有活力 暗色）
     * Vibrant light（有活力 亮色）
     * Muted （柔和）
     * Muted dark（柔和 暗色）
     * Muted light（柔和 亮色）
     */

    public int getVibrantColor(int position) {
        return colorList.get(position).getVibrantColor();
    }

    public int getVibrantDarkColor(int position) {
        return colorList.get(position).getVibrantDarkColor();
    }

    public int getVibrantLightColor(int position) {
        return colorList.get(position).getVibrantLightColor();
    }

    public int getMutedColor(int position) {
        return colorList.get(position).getMutedColor();
    }

    public int getMutedDarkColor(int position) {
        return colorList.get(position).getMutedDarkColor();
    }

    public int getMutedLightColor(int position) {
        return colorList.get(position).getMutedLightColor();
    }
}
