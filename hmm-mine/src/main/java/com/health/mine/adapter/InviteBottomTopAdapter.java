package com.health.mine.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mine.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.InviteAct;

public class InviteBottomTopAdapter extends BaseAdapter<InviteAct> {




    public InviteBottomTopAdapter() {
        this(R.layout.mine_adapter_invitebottom);
    }

    private InviteBottomTopAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        TextView inviteJoinCount;
        inviteJoinCount = (TextView) baseHolder.itemView.findViewById(R.id.inviteJoinCount);
        inviteJoinCount.setText(getModel().inviteJoinCount+"");

    }

    private void initView() {
    }
}
