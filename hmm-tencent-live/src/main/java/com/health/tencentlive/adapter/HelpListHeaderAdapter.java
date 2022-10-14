package com.health.tencentlive.adapter;

import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.tencentlive.R;
import com.health.tencentlive.model.LiveHelpList;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.widget.CornerImageView;

import java.util.ArrayList;
import java.util.List;

public class HelpListHeaderAdapter extends BaseAdapter<List<LiveHelpList>> {
    private int type = 0;//1是没数据 0 正常

    public HelpListHeaderAdapter() {
        this(R.layout.live_helplist_header_adapter_layout);
    }

    public HelpListHeaderAdapter(int viewId) {
        super(viewId);
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView no2Num = (TextView) holder.getView(R.id.no2Num);
        CornerImageView no2Avatar = (CornerImageView) holder.getView(R.id.no2Avatar);
        TextView no2Nickname = (TextView) holder.getView(R.id.no2Nickname);
        TextView no1Num = (TextView) holder.getView(R.id.no1Num);
        CornerImageView no1Avatar = (CornerImageView) holder.getView(R.id.no1Avatar);
        TextView no1Nickname = (TextView) holder.getView(R.id.no1Nickname);
        TextView no3Num = (TextView) holder.getView(R.id.no3Num);
        CornerImageView no3Avatar = (CornerImageView) holder.getView(R.id.no3Avatar);
        TextView no3Nickname = (TextView) holder.getView(R.id.no3Nickname);

        if (type == 1) {
            no1Nickname.setText("抢沙发");
            no2Nickname.setText("抢沙发");
            no3Nickname.setText("抢沙发");
            no1Nickname.setTextColor(Color.parseColor("#999999"));
            no2Nickname.setTextColor(Color.parseColor("#999999"));
            no3Nickname.setTextColor(Color.parseColor("#999999"));
            no1Num.setText("0");
            no2Num.setText("0");
            no3Num.setText("0");
        } else {
            List<LiveHelpList> liveHelpLists = getModel();
            if (liveHelpLists.size() == 1) {
                no1Nickname.setText(liveHelpLists.get(0).fromMember.nickName);
                no2Nickname.setText("抢沙发");
                no3Nickname.setText("抢沙发");
                no1Nickname.setTextColor(Color.parseColor("#333333"));
                no2Nickname.setTextColor(Color.parseColor("#999999"));
                no3Nickname.setTextColor(Color.parseColor("#999999"));
                com.healthy.library.businessutil.GlideCopy.with(getContext())
                        .load(liveHelpLists.get(0).fromMember.faceUrl)

                        .placeholder(R.drawable.image_live_help_avatar_default)
                        .error(R.drawable.image_live_help_avatar_default)
                        .into(no1Avatar);
                no1Num.setText(liveHelpLists.get(0).countMember);
                no2Num.setText("0");
                no3Num.setText("0");
            } else if (liveHelpLists.size() == 2) {
                no1Nickname.setText(liveHelpLists.get(0).fromMember.nickName);
                no2Nickname.setText(liveHelpLists.get(1).fromMember.nickName);
                no3Nickname.setText("抢沙发");
                no1Nickname.setTextColor(Color.parseColor("#333333"));
                no2Nickname.setTextColor(Color.parseColor("#333333"));
                no3Nickname.setTextColor(Color.parseColor("#999999"));
                com.healthy.library.businessutil.GlideCopy.with(getContext())
                        .load(liveHelpLists.get(0).fromMember.faceUrl)

                        .placeholder(R.drawable.image_live_help_avatar_default)
                        .error(R.drawable.image_live_help_avatar_default)
                        .into(no1Avatar);
                com.healthy.library.businessutil.GlideCopy.with(getContext())
                        .load(liveHelpLists.get(1).fromMember.faceUrl)

                        .placeholder(R.drawable.image_live_help_avatar_default)
                        .error(R.drawable.image_live_help_avatar_default)
                        .into(no2Avatar);
                no1Num.setText(liveHelpLists.get(0).countMember);
                no2Num.setText(liveHelpLists.get(1).countMember);
                no3Num.setText("0");
            } else if (liveHelpLists.size() == 3) {
                no1Nickname.setText(liveHelpLists.get(0).fromMember.nickName);
                no2Nickname.setText(liveHelpLists.get(1).fromMember.nickName);
                no3Nickname.setText(liveHelpLists.get(2).fromMember.nickName);
                no1Nickname.setTextColor(Color.parseColor("#333333"));
                no2Nickname.setTextColor(Color.parseColor("#333333"));
                no3Nickname.setTextColor(Color.parseColor("#333333"));
                com.healthy.library.businessutil.GlideCopy.with(getContext())
                        .load(liveHelpLists.get(0).fromMember.faceUrl)

                        .placeholder(R.drawable.image_live_help_avatar_default)
                        .error(R.drawable.image_live_help_avatar_default)
                        .into(no1Avatar);
                com.healthy.library.businessutil.GlideCopy.with(getContext())
                        .load(liveHelpLists.get(1).fromMember.faceUrl)

                        .placeholder(R.drawable.image_live_help_avatar_default)
                        .error(R.drawable.image_live_help_avatar_default)
                        .into(no2Avatar);
                com.healthy.library.businessutil.GlideCopy.with(getContext())
                        .load(liveHelpLists.get(2).fromMember.faceUrl)

                        .placeholder(R.drawable.image_live_help_avatar_default)
                        .error(R.drawable.image_live_help_avatar_default)
                        .into(no3Avatar);
                no1Num.setText(liveHelpLists.get(0).countMember);
                no2Num.setText(liveHelpLists.get(1).countMember);
                no3Num.setText(liveHelpLists.get(2).countMember);
            }
        }
    }

}
