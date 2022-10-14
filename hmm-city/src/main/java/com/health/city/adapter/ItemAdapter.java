package com.health.city.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.city.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.health.city.widget.FloatingManager;
import com.healthy.library.model.PostImgDetial;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.LogUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;

public class ItemAdapter extends BaseAdapter<PostImgDetial> {

    public ItemAdapter() {
        this(R.layout.item_adapter_layout);
    }

    public ItemAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final PostImgDetial postImgDetial = getDatas().get(position);
        if (postImgDetial == null) {
            return;
        }
        final ImageView ivLoading = holder.getView(R.id.iv_loading);
        final ConstraintLayout picLiner = holder.getView(R.id.picLiner);
        ConstraintLayout remarkLiner = holder.getView(R.id.remarkLiner);
        remarkLiner.removeAllViews();
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(postImgDetial.annexUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int width = (int) TransformUtil.dp2px(context, resource.getIntrinsicWidth());
                        int height = (int) TransformUtil.dp2px(context, resource.getIntrinsicHeight());
                        int swidth = ScreenUtils.getScreenWidth(context);
                        LogUtils.e("width" + width);
                        LogUtils.e("height" + height);
                        LogUtils.e("swidth" + swidth);
                        if (width > swidth) {
                            width = swidth;
                        }
                        //这块是给图片加标签的
                        if (postImgDetial.postingImgTagList != null && postImgDetial.postingImgTagList.size() > 0) {
                            for (int i = 0; i < postImgDetial.postingImgTagList.size(); i++) {
                                showCenterView(picLiner, postImgDetial.postingImgTagList.get(i).pixelCoordinates, postImgDetial.postingImgTagList.get(i).imgTagName, width, height);
                            }
                        }
                        com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(ivLoading);
                    }
                });
    }

    public void showCenterView(View view, String pixelCoordinates, String title, int width, int height) {
        FloatingManager.Builder builder = FloatingManager.getBuilder();
        builder.setAnchorView(view);
        builder.setTitle(title);
        builder.setContext(context);
        builder.setX((Float.parseFloat(pixelCoordinates.split(",")[0]) / 100 * width));
        builder.setY((Float.parseFloat(pixelCoordinates.split(",")[1]) / 100 * height));
        FloatingManager manager = builder.build();
        manager.addCenterView();
    }
}
