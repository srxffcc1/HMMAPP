package com.healthy.library.loader;

import android.graphics.Bitmap;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.healthy.library.R;
import com.healthy.library.model.ColorInfo;
import com.healthy.library.widget.CornerImageView;
import com.youth.banner.adapter.BannerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义布局，网络图片
 */
public class ImageNetBg2Adapter extends BannerAdapter<String,ImageHolder> {

    private List<ColorInfo> colorList=new ArrayList<>();
    private float mRadius;
    private Palette palette;
    public ImageNetBg2Adapter(List<String> mDatas, float radius, List<ColorInfo> colorList) {
        super(mDatas);
        mRadius = radius;
        this.colorList = colorList;
    }

    @Override
    public ImageHolder onCreateHolder(ViewGroup parent, int viewType) {
//        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_float,parent,false);
//        view.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
        CornerImageView cornerImageView = new CornerImageView(parent.getContext());
        cornerImageView.setCornerRadius(mRadius);
        cornerImageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return new ImageHolder(cornerImageView);
    }

    @Override
    public void onBindView(ImageHolder holder, final String data, int position, int size) {

            com.healthy.library.businessutil.GlideCopy.with(holder.itemView).asBitmap().load(data).skipMemoryCache(true).listener(new RequestListener<Bitmap>() {

                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    setColorList(resource, data);
                    return false;
                }
            })
                    .placeholder(R.drawable.img_690_260_default)
                    .error(R.drawable.img_690_260_default).into(holder.imageView);

//        com.healthy.library.businessutil.GlideCopy.with(holder.itemView)
//                .load(data)
//                .into(holder.imageView);
    }
    private void setColorList(Bitmap bitmap, String imgUrl) {
        if (colorList == null) {
            return;
        }
        //System.out.println("得到最多的颜色分析");
        palette = Palette.from(bitmap).generate();
        for (int i = 0; i < colorList.size(); i++) {
            if (colorList.get(i).getImgUrl().equals(imgUrl)) {// imgUrl作为识别标志
                if (palette.getVibrantSwatch() != null) {
                    //System.out.println("得到最多的颜色分析");
                    colorList.get(i).setVibrantColor(palette.getVibrantSwatch().getRgb());
                }else {

                    //System.out.println("无法得到最多的颜色分析");
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
