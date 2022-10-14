package com.health.index.adapter;

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
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.index.R;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.model.VideoListModel;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.RoundedImageView;
import com.healthy.library.widget.StaggeredGridLayoutHelperFix;

import java.util.HashMap;
import java.util.Map;

public class IndexTabFoundAdapter extends BaseAdapter<VideoListModel> {
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean visible = true;

    @Override
    public int getItemViewType(int position) {
        return 14;
    }

    public IndexTabFoundAdapter() {
        this(R.layout.index_mon_found);
    }

    private IndexTabFoundAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StaggeredGridLayoutHelperFix helper = new StaggeredGridLayoutHelperFix(2);
//        helper.setAutoExpand(false);
        helper.setMarginTop(TransformUtil.newDp2px(LibApplication.getAppContext(),12));
        helper.setLane(2);
        helper.setHGap(3);
        return helper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final RoundedImageView videoImg;
        LinearLayout titleLayout;
        ImageView videoTips;
        TextView videoTitle;
        TextView videoContent;
        TextView seeNum;
        videoImg = (RoundedImageView) holder.itemView.findViewById(R.id.videoImg);
        titleLayout = (LinearLayout) holder.itemView.findViewById(R.id.titleLayout);
        videoTips = (ImageView) holder.itemView.findViewById(R.id.videoTips);
        videoTitle = (TextView) holder.itemView.findViewById(R.id.videoTitle);
        videoContent = (TextView) holder.itemView.findViewById(R.id.videoContent);
        seeNum = (TextView) holder.itemView.findViewById(R.id.seeNum);
        final VideoListModel videoListModel = getDatas().get(position);
        if (videoListModel.photo != null && !TextUtils.isEmpty(videoListModel.photo)) {
            if(videoListModel.photo!=null&&videoListModel.photo.contains("width=")){
                String str = videoListModel.photo.split("\\?").length>1?videoListModel.photo.split("\\?")[1]:videoListModel.photo.split("\\?")[0];
                Map<String,String> map=new HashMap<>();
                String[] resultarray=str.split("&");
                for (int i = 0; i <resultarray.length ; i++) {
                    map.put(resultarray[i].split("=")[0],resultarray[i].split("=").length>1?resultarray[i].split("=")[1]:"");
                }
                int pheight=Integer.parseInt(map.get("height"));
                int pwidth=Integer.parseInt(map.get("width"));
                int swidth = (ScreenUtils.getScreenWidth(context)-(int)TransformUtil.dp2px(LibApplication.getAppContext(),16)) / 2;
                int height = (int) ((pheight * 1.0 / pwidth) * swidth);
                ViewGroup.LayoutParams params = videoImg.getLayoutParams();
                params.height = height;
                videoImg.setLayoutParams(params);
                GlideCopy.with(context).load(videoListModel.photo)
                        .placeholder(R.drawable.img_9_16_default2)
                        .error(R.drawable.img_9_16_default2)

                        .into(videoImg);
            }else {
                GlideCopy.with(context).load(videoListModel.photo)
                        .placeholder(R.drawable.img_9_16_default2)
                        .error(R.drawable.img_9_16_default2)

                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                int swidth = (ScreenUtils.getScreenWidth(context)-(int)TransformUtil.dp2px(LibApplication.getAppContext(),16)) / 2;
                                int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);
                                ViewGroup.LayoutParams params = videoImg.getLayoutParams();
                                params.height = height;
                                videoImg.setLayoutParams(params);
                                com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(videoImg);
                            }
                        });
            }


        } else {
            Drawable btnDrawable = context.getResources().getDrawable(R.drawable.img_9_16_default2);
            int swidth = (ScreenUtils.getScreenWidth(context)-(int)TransformUtil.dp2px(LibApplication.getAppContext(),16)) / 2;
            int height = (int) ((btnDrawable.getIntrinsicHeight() * 1.0 / btnDrawable.getIntrinsicWidth()) * swidth);
            ViewGroup.LayoutParams params = videoImg.getLayoutParams();
            params.height = height;
            videoImg.setLayoutParams(params);
            videoImg.setImageDrawable(btnDrawable);
        }
        if (videoListModel.isTop == 1) {
            videoTips.setVisibility(View.VISIBLE);
        } else {
            videoTips.setVisibility(View.GONE);
        }
        videoTitle.setText(videoListModel.videoName);
        videoContent.setText(videoListModel.videoRemark);
        seeNum.setText((videoListModel.realView + videoListModel.virtualView) + "人已看");
//        Glide.with(context)
//                .load(videoListModel.photo)
//                .error(R.drawable.hanmom_video_list_default)
//                .placeholder(R.drawable.hanmom_video_list_default)
//                .into(videoImg);
        videoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_HANMOMVIDEODETAIL)
                        .withString("id", videoListModel.id)
                        .withString("categoryCode", videoListModel.categoryCode)
                        .navigation();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_HANMOMTEACHINGDETAIL)
                        .withString("id", videoListModel.id)
                        .withInt("type", 2)
                        .navigation();
            }
        });
    }

    private boolean isOddNumber(int i) {
        return ((i & 1) == 1) ? true : false;
    }
}
