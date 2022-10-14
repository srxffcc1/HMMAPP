package com.healthy.library.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.StaggeredGridLayoutHelper;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.healthy.library.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.model.VideoListModel;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.widget.CornerImageView;

import org.jetbrains.annotations.NotNull;

public class HanMomVideoFindChildAdapter extends BaseAdapter<VideoListModel> {

    public HanMomVideoFindChildAdapter() {
        this(R.layout.item_han_mom_video_find_child_adapter);
    }

    public HanMomVideoFindChildAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StaggeredGridLayoutHelper helper = new StaggeredGridLayoutHelper(2);
        helper.setMargin(10, 0, 10, 0);
        helper.setHGap(3);
        return helper;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseHolder holder, int position) {
        final CornerImageView videoImg;
        LinearLayout titleLayout;
        ImageView videoTips;
        TextView videoTitle;
        TextView videoContent;
        TextView seeNum;
        videoImg = (CornerImageView) holder.itemView.findViewById(R.id.videoImg);
        titleLayout = (LinearLayout) holder.itemView.findViewById(R.id.titleLayout);
        videoTips = (ImageView) holder.itemView.findViewById(R.id.videoTips);
        videoTitle = (TextView) holder.itemView.findViewById(R.id.videoTitle);
        videoContent = (TextView) holder.itemView.findViewById(R.id.videoContent);
        seeNum = (TextView) holder.itemView.findViewById(R.id.seeNum);
        final VideoListModel videoListModel = getDatas().get(position);
        if (videoListModel.isTop == 1) {
            videoTips.setVisibility(View.VISIBLE);
        } else {
            videoTips.setVisibility(View.GONE);
        }
        if (videoListModel.photo != null && !TextUtils.isEmpty(videoListModel.photo)) {
            GlideCopy.with(context).load(videoListModel.photo)
                    .placeholder(R.drawable.hanmom_video_list_default)
                    .error(R.drawable.hanmom_video_list_default)

                    .into(new SimpleTarget<Drawable>() {

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int swidth = ScreenUtils.getScreenWidth(context) / 2;
                            int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);
                            ViewGroup.LayoutParams params = videoImg.getLayoutParams();
                            params.height = height;
                            videoImg.setLayoutParams(params);
                            com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(videoImg);
//                            videoImg.setImageDrawable(resource);
                        }
                    });
        } else {
            Drawable btnDrawable = context.getResources().getDrawable(R.drawable.hanmom_video_list_default);
            int swidth = ScreenUtils.getScreenWidth(context) / 2;
            int height = (int) ((btnDrawable.getIntrinsicHeight() * 1.0 / btnDrawable.getIntrinsicWidth()) * swidth);
            ViewGroup.LayoutParams params = videoImg.getLayoutParams();
            params.height = height;
            videoImg.setLayoutParams(params);
            videoImg.setImageDrawable(btnDrawable);
        }
        videoTitle.setText(videoListModel.videoName);
        videoContent.setText(videoListModel.videoRemark);
        seeNum.setText((videoListModel.realView + videoListModel.virtualView) + "人已看");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_HANMOMVIDEODETAIL)
                        .withString("id", videoListModel.id)
                        .withString("categoryCode", videoListModel.categoryCode)
                        .navigation();
            }
        });
    }
}
