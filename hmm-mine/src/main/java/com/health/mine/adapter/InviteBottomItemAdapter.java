package com.health.mine.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mine.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.model.InviteJoinInfo;
import com.healthy.library.widget.CornerImageView;

public class InviteBottomItemAdapter extends BaseAdapter<InviteJoinInfo> {




    public InviteBottomItemAdapter() {
        this(R.layout.mine_adapter_invitebottom_item);
    }

    private InviteBottomItemAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        InviteJoinInfo inviteJoinInfo=getDatas().get(i);
         LinearLayout inviteManBg;
         TextView inviteNoNormal;
         ImageView inviteNoSpecial;
         CornerImageView invitePic;
         TextView inviteName;
         TextView inviteNumber;
        inviteManBg = (LinearLayout) baseHolder.itemView.findViewById(R.id.inviteManBg);
        inviteNoNormal = (TextView) baseHolder.itemView.findViewById(R.id.inviteNoNormal);
        inviteNoSpecial = (ImageView)baseHolder.itemView. findViewById(R.id.inviteNoSpecial);
        invitePic = (CornerImageView) baseHolder.itemView.findViewById(R.id.invitePic);
        inviteName = (TextView) baseHolder.itemView.findViewById(R.id.inviteName);
        inviteNumber = (TextView)baseHolder.itemView. findViewById(R.id.inviteNumber);
        if(i%2==0){//整数
            inviteManBg.setBackgroundColor(Color.parseColor("#FFF9EB"));
        }else {
            inviteManBg.setBackgroundColor(Color.parseColor("#FFF9EB"));
        }
        inviteNoNormal.setVisibility(View.GONE);
        inviteNoSpecial.setVisibility(View.INVISIBLE);
        if(i==0){
            inviteNoSpecial.setVisibility(View.VISIBLE);
            inviteNoSpecial.setImageResource(R.drawable.inviteone);
        }else if(i==1){
            inviteNoSpecial.setVisibility(View.VISIBLE);
            inviteNoSpecial.setImageResource(R.drawable.invitetwo);
        }else if(i==2){
            inviteNoSpecial.setVisibility(View.VISIBLE);
            inviteNoSpecial.setImageResource(R.drawable.invitethree);
        }else {
            inviteNoNormal.setVisibility(View.VISIBLE);
        }
        inviteNoNormal.setText(""+(i+1));
        GlideCopy.with(context)
                .load(inviteJoinInfo.inviteMemberFaceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(invitePic);
        inviteName.setText(inviteJoinInfo.inviteMemberNickName);
        inviteNumber.setText(inviteJoinInfo.inviteCount+"");
    }

    private void initView() {

    }
}
