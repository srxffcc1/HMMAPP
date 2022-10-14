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

public class InviteCenterWithMineAdapter extends BaseAdapter<InviteAct> {
    boolean isinit = false;
    public String underString;

    public void setUnderString(String underString) {
        this.underString = underString;
    }

    public InviteCenterWithMineAdapter() {
        this(R.layout.mine_adapter_invitecenter_mine);
    }

    private InviteCenterWithMineAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup p0, int p1) {//重写用这个做替换才能把 isTouchNsv 交给他
        context=p0.getContext();//必须赋值
        return new InviteScrollViewHolder(LayoutInflater.from(p0.getContext()).inflate(getViewId(), p0, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        InviteAct inviteAct = getModel();
         LinearLayout titleLL;
         ScrollView canUseCardStoreSc;
         LinearLayout actLineL;
         final ImageView actGoButton;
         TextView inviteUnderString;
        inviteUnderString=(TextView) baseHolder.itemView.findViewById(R.id.inviteUnderString);
        titleLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.titleLL);
        canUseCardStoreSc = (ScrollView) baseHolder.itemView.findViewById(R.id.canUseCardStoreSc);
        actLineL = (LinearLayout) baseHolder.itemView.findViewById(R.id.actLineL);
        actGoButton = (ImageView) baseHolder.itemView.findViewById(R.id.actGoButton);
        ViewGroup.LayoutParams layoutParams=canUseCardStoreSc.getLayoutParams();
        if(inviteAct.activity.inviteTargetRewardList.size()>1){
            layoutParams.height= (int)TransformUtil.dp2px(context,145);
            canUseCardStoreSc.setLayoutParams(layoutParams);
        }
        buildCouponList(actLineL, inviteAct);
        actGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moutClickListener!=null){
                    moutClickListener.outClick("邀请新人",null);
                }
            }
        });
        inviteUnderString.setText(underString);
        //启动滚动
//        if(!isinit){
//            AutoPollRecyclerView autoPollRecyclerView;
//            autoPollRecyclerView = (AutoPollRecyclerView) baseHolder.itemView.findViewById(R.id.actLineL);
//            autoPollRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//            InviteAutoPollAdapter autoPollAdapter = new InviteAutoPollAdapter(context, getModel().activity.inviteTargetRewardList);
//            autoPollRecyclerView.setAdapter(autoPollAdapter);
//            autoPollRecyclerView.start();
//            isinit=true;
//        }
    }

    private void buildCouponList(LinearLayout actLines, InviteAct inviteAct) {
        actLines.removeAllViews();
        List<InviteActReward> inviteActRewards = inviteAct.activity.inviteTargetRewardList;
        for (int i = 0; i < inviteActRewards.size(); i++) {
            InviteActReward inviteActReward=inviteActRewards.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.mine_item_adapter_invitecenter_mine, actLines, false);
            TextView inviteCardMoney;
            TextView inviteCardName;
            TextView inviteCardTime;
            inviteCardMoney = (TextView) view.findViewById(R.id.inviteCardMoney);
            inviteCardName = (TextView) view.findViewById(R.id.inviteCardName);
            inviteCardTime = (TextView) view.findViewById(R.id.inviteCardTime);
            inviteCardMoney.setText(FormatUtils.moneyKeep2Decimals(inviteActReward.coupon.getCouponDenomination()));
            inviteCardName.setText(inviteActReward.coupon.getCouponNormalName()+"  "+inviteActReward.couponNumber+"张");
            inviteCardTime.setText(inviteActReward.coupon.getTimeValidity());
            actLines.addView(view);
        }
    }

    public class InviteScrollViewHolder extends BaseHolder {
        ScrollView mall_nsv;

        public InviteScrollViewHolder(@NonNull View itemView) {
            super(itemView);
            mall_nsv = itemView.findViewById(R.id.canUseCardStoreSc);

        }

        public boolean isTouchNsv(float x, float y) {
            int[] pos = new int[2];
            //获取sv在屏幕上的位置
            mall_nsv.getLocationOnScreen(pos);
            int width = mall_nsv.getMeasuredWidth();
            int height = mall_nsv.getMeasuredHeight();
            return x >= pos[0] && x <= pos[0] + width && y >= pos[1] && y <= pos[1] + height;
        }
    }
}
