package com.health.tencentlive.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.tencentlive.R;
import com.healthy.library.model.LiveFans;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;

public class LiveFansAdapter extends BaseAdapter<LiveFans> {




    @Override
    public int getItemViewType(int position) {
        return 70;
    }

    public LiveFansAdapter() {
        this(R.layout.item_live_fans);
    }

    private LiveFansAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int postion) {
        final LiveFans liveFans=getDatas().get(postion);
         CornerImageView fansIcon;
         TextView fansName;
         TextView fansDes;
         final ImageTextView fansPass;
        fansIcon = (CornerImageView) baseHolder.itemView.findViewById(R.id.fansIcon);
        fansName = (TextView) baseHolder.itemView.findViewById(R.id.fansName);
        fansDes = (TextView) baseHolder.itemView.findViewById(R.id.fansDes);
        fansPass = (ImageTextView) baseHolder.itemView.findViewById(R.id.fansPass);


        try {
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(liveFans.avatar)

                    .placeholder(R.drawable.img_avatar_default)
                    .error(R.drawable.img_avatar_default)
                    .into(fansIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fansName.setText(liveFans.name);
        fansDes.setText(liveFans.describe);
        if(liveFans.status==3){
            fansPass.setBackgroundResource(R.drawable.shape_video_focus_has);
            fansPass.setText("已互关");
            fansPass.setDrawable(-1,context);
            fansPass.setTextColor(Color.parseColor("#ff999999"));
        }else {
            fansPass.setBackgroundResource(R.drawable.shape_video_focus);
            fansPass.setText("关注");
            fansPass.setTextColor(Color.parseColor("#ffffffff"));
            fansPass.setDrawable(R.drawable.add_white,context);
        }
        fansPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moutClickListener!=null){
                    liveFans.fansStatus=liveFans.status==3?"2":"1";
                    liveFans.status=liveFans.status==3?2:1;
                    moutClickListener.outClick("关注",liveFans);
                    if(liveFans.status==3){
                        fansPass.setBackgroundResource(R.drawable.shape_video_focus_has);
                        fansPass.setText("已互关");
                        fansPass.setDrawable(-1,context);
                        fansPass.setTextColor(Color.parseColor("#ff999999"));
                    }else {
                        fansPass.setBackgroundResource(R.drawable.shape_video_focus);
                        fansPass.setText("关注");
                        fansPass.setTextColor(Color.parseColor("#ffffffff"));
                        fansPass.setDrawable(R.drawable.add_white,context);
                    }
                }
            }
        });
    }
}
