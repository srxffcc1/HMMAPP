package com.health.mine.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mine.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.model.InviteAct;
import com.healthy.library.widget.AutoPollRecyclerView;

public class InviteCenterWithMineEmptyAdapter extends BaseAdapter<String> {
    public InviteCenterWithMineEmptyAdapter() {
        this(R.layout.mine_adapter_invitecenter_mine_empty);
    }

    private InviteCenterWithMineEmptyAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {

    }

    private void initView() {

    }
}
