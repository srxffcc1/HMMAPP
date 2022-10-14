package com.health.faq.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.faq.R;
import com.healthy.library.model.FaqVideo;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.DateUtils;

public class VideoListAdapter extends BaseQuickAdapter<FaqVideo, BaseViewHolder> {
    public VideoListAdapter() {
        this(R.layout.faq_onlinevedio);
    }

    public SubListener subListener;

    public void setSubListener(SubListener subListener) {
        this.subListener = subListener;
    }
    private VideoListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FaqVideo item) {
        final FaqVideo indexVideo = item;

        TextView onlineTop;
        TextView onlineTime;
        TextView onlinePeople;
        TextView onlineTitle;
        LinearLayout onlineButtonLL;
        TextView onlineButton;
        TextView onlineButtonAl;
        TextView onlineContent;


        onlineTop = (TextView) helper.itemView.findViewById(R.id.onlineTop);
        onlineTime = (TextView) helper.itemView.findViewById(R.id.onlineTime);
        onlinePeople = (TextView) helper.itemView.findViewById(R.id.onlinePeople);
        onlineTitle = (TextView) helper.itemView.findViewById(R.id.onlineTitle);
        onlineButtonLL = (LinearLayout) helper.itemView.findViewById(R.id.onlineButtonLL);
        onlineButton = (TextView) helper.itemView.findViewById(R.id.onlineButton);
        onlineButtonAl = (TextView) helper.itemView.findViewById(R.id.onlineButtonAl);
        onlineContent = (TextView) helper.itemView.findViewById(R.id.onlineContent);

        onlinePeople.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.onlinev_mos),null,null,null);
        onlineButton.setVisibility(View.GONE);
        onlineButtonAl.setVisibility(View.GONE);
        if(indexVideo.liveStatus==1){
            onlineTop.setText("直播");
            onlineTime.setText("今天 "+DateUtils.formatLongAll(indexVideo.start_time*1000+"","HH:mm"));
            onlinePeople.setText(indexVideo.appointmenNum+"人已预约");
            if(indexVideo.appointmenStatus==0){
                onlineButton.setVisibility(View.VISIBLE);
            }else {
                onlineButtonAl.setVisibility(View.VISIBLE);
            }
        }
        if(indexVideo.liveStatus==2){
            onlineTop.setText("直播中");
            onlineTime.setText("");
            onlinePeople.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.onlinev_mos_g),null,null,null);
            onlinePeople.setText(indexVideo.liveUv+"人正在观看");
            onlinePeople.setVisibility(View.VISIBLE);
            if(true){
                onlinePeople.setVisibility(View.INVISIBLE);
            }
        }
        if(indexVideo.liveStatus==3){
            onlineTop.setText("直播结束");
            onlineTime.setText("");
            onlinePeople.setText(indexVideo.appointmenNum+"人已预约");
        }
        onlineTitle.setText(indexVideo.course_name);
        onlineContent.setText("主讲人："+indexVideo.nickname+","+indexVideo.intro);
        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subListener!=null){
                    subListener.clickSub(indexVideo);
                }
            }
        });
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW)
                        .withString("title", "憨妈直播")
                        .withBoolean("isinhome",false)
                        .withBoolean("leftnow",true)
                        .withBoolean("videoshop",true)
                        .withString("course_id", indexVideo.course_id+"")
                        .withString("liveStatus", indexVideo.liveStatus+"")
                        .navigation();
            }
        });
    }
    public interface SubListener{
        void clickSub(FaqVideo indexVideo);
    }
}
