package com.health.discount.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;

public class SpecAreaBannerAdapter extends BaseAdapter<MainBlockModel> {

    public SpecAreaBannerAdapter(int viewId) {
        super(viewId);
    }

    public SpecAreaBannerAdapter() {
        this(R.layout.specarea_banner_adapter_layout);
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        MainBlockModel bean=getModel();
        final CornerImageView bannerImg = holder.getView(R.id.bannerImg);
        TextView topImgTag=holder.itemView.findViewById(R.id.topImgTag);
        ImageView topImgTagIcon=holder.itemView.findViewById(R.id.topImgTagIcon);
        TextView topImgTitle=holder.itemView.findViewById(R.id.topImgTitle);
        TextView topImgSecondTitle=holder.itemView.findViewById(R.id.topImgSecondTitle);
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(bean.themeImage)
                .error(R.drawable.img_1_1_default)
                .placeholder(R.drawable.img_1_1_default)
                
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int swidth = ScreenUtils.getScreenWidth(context) -(int) TransformUtil.dp2px(context,16);
                        int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);
                        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) bannerImg.getLayoutParams();
                        layoutParams.height = height;
                        layoutParams.width = swidth;
                        bannerImg.setLayoutParams(layoutParams);
                        com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(bannerImg);
                    }
                });
        topImgTag.setVisibility(View.VISIBLE);
        topImgTag.setVisibility(View.VISIBLE);
        topImgSecondTitle.setVisibility(View.VISIBLE);
        topImgTagIcon.setVisibility(View.INVISIBLE);
        topImgTag.setText(bean.themeTag);
        if(TextUtils.isEmpty(bean.themeTag)){
            topImgTag.setVisibility(View.INVISIBLE);
        }
        topImgTitle.setText(bean.describe);
        topImgSecondTitle.setText(bean.adContent);
        if(TextUtils.isEmpty(bean.adContent)){
            topImgSecondTitle.setVisibility(View.GONE);
        }
        if(bean.themeTag.contains("http")){
            topImgTag.setVisibility(View.INVISIBLE);
            topImgTagIcon.setVisibility(View.VISIBLE);
            com.healthy.library.businessutil.GlideCopy.with(context).load(bean.themeTag)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    
                    .into(topImgTagIcon);
        }
    }
}
