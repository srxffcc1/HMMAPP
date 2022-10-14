package com.health.mine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mine.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.InviteAct;
import com.healthy.library.model.InviteActReward;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.TransformUtil;

import java.util.List;

public class InviteCenterAdapter extends BaseAdapter<InviteAct> {

    boolean needScroll=false;


    public InviteCenterAdapter() {
        this(R.layout.mine_adapter_invitecenter);
    }

    private InviteCenterAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup p0, int p1) {
        context=p0.getContext();//必须赋值
        return new InviteCenterScrollViewHolder(LayoutInflater.from(p0.getContext()).inflate(getViewId(), p0, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        InviteAct inviteAct = getModel();
        TextView actTime;
        LinearLayout actLines;
        ImageView actGoButton;
        ScrollView canUseCardStoreSc=(ScrollView) baseHolder.itemView.findViewById(R.id.canUseCardStoreSc);
        actTime = (TextView) baseHolder.itemView.findViewById(R.id.actTime);
        actTime.setText("活动时间：" + inviteAct.activity.getBeginTime().replace("-", ".") + "-" + inviteAct.activity.getEndTime().replace("-", "."));
        actLines = (LinearLayout) baseHolder.itemView.findViewById(R.id.actLineL);
        actGoButton = (ImageView) baseHolder.itemView.findViewById(R.id.actGoButton);
        actGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moutClickListener!=null){
                    moutClickListener.outClick("邀请新人",null);
                }
            }
        });
        needScroll=false;
        List<InviteActReward> inviteActRewards = inviteAct.activity.inviteRewardList;
        ViewGroup.LayoutParams params=canUseCardStoreSc.getLayoutParams();
        int singleHeight=115;
        params.height= (int)TransformUtil.dp2px(context,singleHeight);
        if (inviteActRewards.size()>3){
            needScroll=true;
            params.height= (int)TransformUtil.dp2px(context,singleHeight*3);
        } else if(inviteActRewards.size()>2){

            params.height= (int)TransformUtil.dp2px(context,singleHeight*3);
        }else if(inviteActRewards.size()>1){
            params.height= (int)TransformUtil.dp2px(context,singleHeight*2);
        }else {
            params.height= (int)TransformUtil.dp2px(context,singleHeight);
        }
        canUseCardStoreSc.setLayoutParams(params);
        buildCouponList(actLines, inviteAct);
    }

    private void buildCouponList(LinearLayout actLines, InviteAct inviteAct) {
        actLines.removeAllViews();
        List<InviteActReward> inviteActRewards = inviteAct.activity.inviteRewardList;
        for (int i = 0; i < inviteActRewards.size(); i++) {
            InviteActReward inviteActReward=inviteActRewards.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.mine_item_adapter_invitecenter, actLines, false);
             TextView inviteCardMoney;
             TextView inviteRightRule;
             TextView inviteCardName;
             TextView inviteCardTime;
            inviteCardMoney = (TextView) view.findViewById(R.id.inviteCardMoney);
            inviteRightRule = (TextView) view.findViewById(R.id.inviteRightRule);
            inviteCardName = (TextView) view.findViewById(R.id.inviteCardName);
            inviteCardTime = (TextView) view.findViewById(R.id.inviteCardTime);
            inviteCardMoney.setText(FormatUtils.moneyKeep2Decimals(inviteActReward.coupon.getCouponDenomination()));
            if(inviteAct.activity.target==1){
                inviteRightRule.setText("邀请新用户"+inviteActReward.inviteNumber+"位");
            }else {
                inviteRightRule.setText("完成下单新用户"+inviteActReward.inviteNumber+"位");
            }
            inviteCardName.setText(inviteActReward.coupon.getCouponNormalName()+"  "+inviteActReward.couponNumber+"张");
            inviteCardTime.setText(inviteActReward.coupon.getTimeValidity());
            actLines.addView(view);
        }
    }

    public class InviteCenterScrollViewHolder extends BaseHolder {
        ScrollView mall_nsv;

        public InviteCenterScrollViewHolder(@NonNull View itemView) {
            super(itemView);
            mall_nsv = itemView.findViewById(R.id.canUseCardStoreSc);

        }

        public boolean isTouchNsv(float x, float y) {
            if(!needScroll){
                return false;
            }
            int[] pos = new int[2];
            //获取sv在屏幕上的位置
            mall_nsv.getLocationOnScreen(pos);
            int width = mall_nsv.getMeasuredWidth();
            int height = mall_nsv.getMeasuredHeight();
            return x >= pos[0] && x <= pos[0] + width && y >= pos[1] && y <= pos[1] + height;
        }
    }
}
