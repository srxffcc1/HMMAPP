package com.health.tencentlive.adapter;

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

public class HelpListFooterAdapter extends BaseAdapter<LiveHelpList> {

    public HelpListFooterAdapter() {
        this(R.layout.live_helplist_footer_adapter_layout);
    }

    public HelpListFooterAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView num = (TextView) holder.getView(R.id.num);
        CornerImageView userAvatar = (CornerImageView) holder.getView(R.id.userAvatar);
        TextView userName = (TextView) holder.getView(R.id.userName);
        TextView peopleNum = (TextView) holder.getView(R.id.peopleNum);
        TextView typeTxt = (TextView) holder.getView(R.id.typeTxt);

        List<LiveHelpList> liveHelpLists = getDatas();
        num.setText(liveHelpLists.get(position).ranking);
        userName.setText(liveHelpLists.get(position).fromMember.nickName);
        peopleNum.setText(liveHelpLists.get(position).countMember);
        com.healthy.library.businessutil.GlideCopy.with(getContext())
                .load(liveHelpLists.get(position).fromMember.faceUrl)
                
                .placeholder(R.drawable.image_live_help_avatar_default)
                .error(R.drawable.image_live_help_avatar_default)
                .into(userAvatar);
    }
}
