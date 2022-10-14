package com.health.tencentlive.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.tencentlive.R;
import com.health.tencentlive.model.LiveHelpList;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.widget.CornerImageView;

import java.util.List;

public class MyHelpListFragmentAdapter extends BaseAdapter<LiveHelpList> {

    public MyHelpListFragmentAdapter() {
        this(R.layout.live_myhelplistfragment_adapter_layout);
    }

    public MyHelpListFragmentAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        CornerImageView userAvatar = (CornerImageView) holder.getView(R.id.userAvatar);
        TextView userName = (TextView) holder.getView(R.id.userName);
        ImageView looking = (ImageView) holder.getView(R.id.looking);
        TextView type = (TextView) holder.getView(R.id.type);
        TextView date = (TextView) holder.getView(R.id.date);
        TextView typeTxt = (TextView) holder.getView(R.id.typeTxt);

        List<LiveHelpList> liveHelpLists = getDatas();
        if (liveHelpLists == null) {
            return;
        }
        if (liveHelpLists.get(position).memberStatus == 0) {
            looking.setVisibility(View.GONE);
            type.setText("已离开");
            type.setTextColor(Color.parseColor("#666666"));
        } else {
            looking.setVisibility(View.VISIBLE);
            type.setText("正在观看");
            type.setTextColor(Color.parseColor("#FA3C5A"));
        }
        date.setText(liveHelpLists.get(position).createTime);
        userName.setText(liveHelpLists.get(position).member != null ? liveHelpLists.get(position).member.nickName : "");
        if (liveHelpLists.get(position).member!=null){
            com.healthy.library.businessutil.GlideCopy.with(getContext())
                    .load(liveHelpLists.get(position).member.faceUrl)
                    
                    .placeholder(R.drawable.image_live_help_avatar_default)
                    .error(R.drawable.image_live_help_avatar_default)
                    .into(userAvatar);
        }

    }
}
