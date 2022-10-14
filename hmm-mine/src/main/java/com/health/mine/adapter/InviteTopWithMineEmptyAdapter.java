package com.health.mine.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mine.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class InviteTopWithMineEmptyAdapter extends BaseAdapter<String> {


    public InviteTopWithMineEmptyAdapter() {
        this(R.layout.mine_adapter_invitecenter_mine_top_empty);
    }

    private InviteTopWithMineEmptyAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
         ImageView inviteGo;
         TextView inviteGoText;
        inviteGo = (ImageView) baseHolder.itemView.findViewById(R.id.inviteGo);
        inviteGoText = (TextView) baseHolder.itemView.findViewById(R.id.inviteGoText);
        inviteGoText.setText(getModel());
        inviteGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moutClickListener!=null){
                    moutClickListener.outClick("邀请新人",null);
                }
            }
        });
    }

    private void initView() {


    }
}
