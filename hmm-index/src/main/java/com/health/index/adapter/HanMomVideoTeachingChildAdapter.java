package com.health.index.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.index.R;
import com.healthy.library.model.VideoListModel;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.base.BaseMultiItemAdapter;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.widget.RoundedImageView;

import org.jetbrains.annotations.NotNull;

public class HanMomVideoTeachingChildAdapter extends BaseMultiItemAdapter<VideoListModel> {

    public HanMomVideoTeachingChildAdapter() {
        this(R.layout.item_han_mom_video_teaching_child_adapter_top);
        addItemType(1, R.layout.item_han_mom_video_teaching_child_adapter_top);
        addItemType(2, R.layout.item_han_mom_video_teaching_child_adapter);
    }

    public HanMomVideoTeachingChildAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && getDatas().get(position).isTop == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseHolder holder, int position) {

        ImageView videoTips = (ImageView) holder.itemView.findViewById(R.id.videoTips);
        TextView videoTitle = (TextView) holder.itemView.findViewById(R.id.videoTitle);
        TextView videoLable = (TextView) holder.itemView.findViewById(R.id.videoLable);
        TextView videoContent = (TextView) holder.itemView.findViewById(R.id.videoContent);
        TextView seeNum = (TextView) holder.itemView.findViewById(R.id.seeNum);
        TextView videoLableLine = (TextView) holder.itemView.findViewById(R.id.videoLableLine);

        final VideoListModel videoListModel = getDatas().get(position);
        if (getItemViewType(position) == 1) {
            ImageView videoImg = holder.itemView.findViewById(R.id.videoImg);
            GlideCopy.with(context)
                    .load(videoListModel.photo)
                    .placeholder(R.drawable.img_690_260_default)
                    .error(R.drawable.img_690_260_default)
                    .into(videoImg);
        } else {
            RoundedImageView img = holder.itemView.findViewById(R.id.img);
            GlideCopy.with(context)
                    .load(videoListModel.photo)
                    .placeholder(R.drawable.img_690_260_default)
                    .error(R.drawable.img_690_260_default)
                    .into(img);
        }
        if (videoListModel.isTop == 1) {
            videoTips.setVisibility(View.VISIBLE);
        } else {
            videoTips.setVisibility(View.GONE);
        }
        videoTitle.setText(videoListModel.videoName);
        videoContent.setText(videoListModel.videoRemark);
        seeNum.setText((videoListModel.realView + videoListModel.virtualView) + "人已看");
        videoLableLine.setText("");
        if (videoListModel.isFree == 1) {
            videoLable.setText("免费");
        } else if (videoListModel.isFree == 2) {
            videoLable.setText("限时免费");
            videoLableLine.setText("￥" + videoListModel.videoPrice);
            videoLableLine.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        } else if (videoListModel.isFree == 3) {
            videoLable.setText("￥" + videoListModel.videoPrice);
        }
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
}
