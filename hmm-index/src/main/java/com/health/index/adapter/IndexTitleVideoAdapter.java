package com.health.index.adapter;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.index.R;
import com.health.index.model.IndexVideo;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.DateUtils;

public class IndexTitleVideoAdapter extends BaseAdapter<IndexVideo> {


    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean visible = true;
    public SubListener subListener;

    public void setSubListener(SubListener subListener) {
        this.subListener = subListener;
    }

    @Override
    public int getItemViewType(int position) {
        return 10;
    }

    public IndexTitleVideoAdapter() {
        this(R.layout.index_onlinevedio);
    }

    private IndexTitleVideoAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {

        final IndexVideo indexVideo = getDatas().get(i);

         TextView onlineTop;
         TextView onlineTime;
         TextView onlinePeople;
         TextView onlineTitle;
         LinearLayout onlineButtonLL;
         TextView onlineButton;
         TextView onlineButtonAl;
         TextView onlineContent;


        onlineTop = (TextView) baseHolder.itemView.findViewById(R.id.onlineTop);
        onlineTime = (TextView) baseHolder.itemView.findViewById(R.id.onlineTime);
        onlinePeople = (TextView) baseHolder.itemView.findViewById(R.id.onlinePeople);
        onlineTitle = (TextView) baseHolder.itemView.findViewById(R.id.onlineTitle);
        onlineButtonLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.onlineButtonLL);
        onlineButton = (TextView) baseHolder.itemView.findViewById(R.id.onlineButton);
        onlineButtonAl = (TextView) baseHolder.itemView.findViewById(R.id.onlineButtonAl);
        onlineContent = (TextView) baseHolder.itemView.findViewById(R.id.onlineContent);

        onlineButton.setVisibility(View.GONE);
        onlineButtonAl.setVisibility(View.GONE);
        onlinePeople.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.onlinev_mos),null,null,null);
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
            onlinePeople.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.onlinev_mos_g),null,null,null);
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
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
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

    private void initView() {

    }
    public interface SubListener{
        void clickSub(IndexVideo indexVideo);
    }
}
