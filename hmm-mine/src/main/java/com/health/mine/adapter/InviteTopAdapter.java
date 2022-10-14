package com.health.mine.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gongwen.marqueen.MarqueeView;
import com.gongwen.marqueen.SimpleMarqueeFactory;
import com.health.mine.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.model.InviteAct;
import com.healthy.library.model.InviteReward;
import com.healthy.library.utils.ScreenUtils;

import java.util.Arrays;
import java.util.List;

public class InviteTopAdapter extends BaseAdapter<InviteAct> {

    boolean isStart = false;


    public InviteTopAdapter() {
        this(R.layout.mine_adapter_invitetop);
    }

    public InviteTopAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        InviteAct inviteAct=getModel();
        TextView inviteRule;
        final ImageView invitePostImg;
        inviteRule = (TextView) holder.itemView.findViewById(R.id.inviteRule);
        invitePostImg = (ImageView) holder.itemView.findViewById(R.id.invitePostImg);
        MarqueeView inviteJoinMan=holder.itemView.findViewById(R.id.inviteJoinMan);
        inviteJoinMan.setVisibility(View.GONE);
        if(inviteAct.inviteRewardRank.size()>0){
            inviteJoinMan.setVisibility(View.VISIBLE);
            final List<String> datas = new SimpleArrayListBuilder<String>().putList(inviteAct.inviteRewardRank, new ObjectIteraor<InviteReward>() {
                @Override
                public String getDesObj(InviteReward o) {
                    return o.getNameAndReward();
                }
            });
            SimpleMarqueeFactory<String> marqueeFactory = new SimpleMarqueeFactory(context) {
                @Override
                public void fixTextView(TextView mView) {
                    super.fixTextView(mView);
                    mView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    mView.setGravity(Gravity.CENTER);
                    mView.setTextSize(14f);
                    mView.setTextColor(Color.WHITE);
                }
            };
            marqueeFactory.setData(datas);
            inviteJoinMan.setMarqueeFactory(marqueeFactory);
            if (!isStart) {
                inviteJoinMan.startFlipping();
            }
            isStart = true;
        }

        if(inviteAct.activity.pic!=null){
            com.healthy.library.businessutil.GlideCopy.with(context).load(inviteAct.activity.pic)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int swidth = ScreenUtils.getScreenWidth(context);
                            int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);

                            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) invitePostImg.getLayoutParams();
                            layoutParams.height = height;
                            layoutParams.width = swidth;
                            invitePostImg.setLayoutParams(layoutParams);
                            com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(invitePostImg);
                        }
                    });
        }else {
            com.healthy.library.businessutil.GlideCopy.with(context).load(R.drawable.invitetopimg2)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int swidth = ScreenUtils.getScreenWidth(context);
                            int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);

                            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) invitePostImg.getLayoutParams();
                            layoutParams.height = height;
                            layoutParams.width = swidth;
                            invitePostImg.setLayoutParams(layoutParams);
                            com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(invitePostImg);
                        }
                    });
        }




        final List<String> datas = new SimpleArrayListBuilder<String>().putList(inviteAct.inviteRewardRank, new ObjectIteraor<InviteReward>() {
            @Override
            public String getDesObj(InviteReward o) {
                return o.getNameAndReward();
            }
        });
        inviteRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moutClickListener!=null){
                    moutClickListener.outClick("活动规则",null);
                }
            }
        });


    }

}
