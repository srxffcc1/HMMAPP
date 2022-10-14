package com.health.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.health.mine.R;
import com.healthy.library.model.InviteActReward;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;

import java.util.List;

public class InviteAutoPollAdapter extends RecyclerView.Adapter<InviteAutoPollAdapter.BaseViewHolder> {
    private final Context mContext;
    private final List<InviteActReward> mData;

    public InviteAutoPollAdapter(Context context, List<InviteActReward> list) {
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mine_item_adapter_invitecenter_mine, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        InviteActReward inviteActReward=mData.get(position % mData.size());
        TextView inviteCardMoney;
        TextView inviteCardName;
        TextView inviteCardTime;
        inviteCardMoney = (TextView) holder.itemView.findViewById(R.id.inviteCardMoney);
        inviteCardName = (TextView) holder.itemView.findViewById(R.id.inviteCardName);
        inviteCardTime = (TextView) holder.itemView.findViewById(R.id.inviteCardTime);
        inviteCardMoney.setText(FormatUtils.moneyKeep2Decimals(inviteActReward.coupon.getCouponDenomination()));
        inviteCardName.setText(inviteActReward.coupon.getCouponNormalName()+"  "+inviteActReward.couponNumber+"张");
        inviteCardTime.setText(inviteActReward.coupon.getTimeValidity());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        CornerImageView userImg;
        public BaseViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.userNickName);
            userImg = itemView.findViewById(R.id.userImg);
        }
    }
}
