package com.health.client.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.health.client.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.MonMessage;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.SpUtils;

import java.util.ArrayList;

public class MessagePAdapter extends BaseAdapter<ArrayList<MonMessage>> {




    @Override
    public int getItemViewType(int position) {
        return 8;
    }

    public MessagePAdapter() {
        this(R.layout.home_item_message_normal_p);

    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        LinearLayout pLL;
        pLL = (LinearLayout) holder.itemView.findViewById(R.id.pLL);
        buildMessageList(pLL, getDatas().get(0));
    }

    private void buildMessageList(LinearLayout pLL, ArrayList<MonMessage> monMessages) {
        pLL.removeAllViews();
        for (int i = 0; i <monMessages.size() ; i++) {
            MonMessage item=monMessages.get(i);
            View view= null;
            ImageView messageIcon=null;
            TextView messageTitle=null;
            TextView messageCount=null;
            TextView messageNoCount=null;
            TextView messageContent=null;
            TextView messageTime=null;
            Switch messageSwitch;
            switch (item.getItemType()){
                case 1:
                    view=LayoutInflater.from(context).inflate(R.layout.home_item_message_special,pLL,false);
                    messageIcon = (ImageView) view.findViewById(R.id.messageIcon);
                    messageTitle = (TextView) view.findViewById(R.id.messageTitle);
                    messageCount = (TextView) view.findViewById(R.id.messageCount);
                    messageNoCount = (TextView) view.findViewById(R.id.messageNoCount);
                    messageContent = (TextView) view.findViewById(R.id.messageContent);
                    messageTime = (TextView) view.findViewById(R.id.messageTime);
                    messageSwitch = (Switch) view.findViewById(R.id.messageSwitch);
                    messageCount.setText(item.num+"");
                    messageCount.setVisibility(View.VISIBLE);
                    messageNoCount.setVisibility(View.VISIBLE);
                    if(item.num==0){
                        messageCount.setVisibility(View.GONE);
                        messageNoCount.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    view=LayoutInflater.from(context).inflate(R.layout.home_item_message_normal,pLL,false);
                    messageIcon = (ImageView) view.findViewById(R.id.messageIcon);
                    messageTitle = (TextView) view.findViewById(R.id.messageTitle);
                    messageCount = (TextView) view.findViewById(R.id.messageCount);
                    messageNoCount = (TextView) view.findViewById(R.id.messageNoCount);
                    messageContent = (TextView) view.findViewById(R.id.messageContent);
                    messageTime = (TextView) view.findViewById(R.id.messageTime);
                    messageSwitch = (Switch) view.findViewById(R.id.messageSwitch);
                    messageTime.setText(DateUtils.getClassDatePost(item.messageTime));
                    messageCount.setText(item.num+"");
                    messageContent.setVisibility(View.VISIBLE);
                    messageCount.setVisibility(View.VISIBLE);
                    if(item.istoast){
                        messageCount.setVisibility(View.VISIBLE);
                        messageNoCount.setVisibility(View.GONE);
                    }else {
//                    messageCount.setVisibility(View.GONE);
                        messageNoCount.setVisibility(View.VISIBLE);
                    }
                    if(!TextUtils.isEmpty(item.newmessagecontent)){
                        messageContent.setText(item.newmessagecontent);
                    }else {
                        messageContent.setText("");
                        messageContent.setVisibility(View.GONE);
                    }
                    if(item.num==0){
                        messageCount.setVisibility(View.GONE);
                        messageNoCount.setVisibility(View.GONE);
                    }
                    messageCount.setVisibility(View.GONE);//弱提醒

                    break;
                case 4:
                    view=LayoutInflater.from(context).inflate(R.layout.home_item_message_normal,pLL,false);
                    messageIcon = (ImageView) view.findViewById(R.id.messageIcon);
                    messageTitle = (TextView) view.findViewById(R.id.messageTitle);
                    messageCount = (TextView) view.findViewById(R.id.messageCount);
                    messageNoCount = (TextView) view.findViewById(R.id.messageNoCount);
                    messageContent = (TextView) view.findViewById(R.id.messageContent);
                    messageTime = (TextView) view.findViewById(R.id.messageTime);
                    messageSwitch = (Switch) view.findViewById(R.id.messageSwitch);

                    messageTime.setText(DateUtils.getClassDatePost(item.messageTime));
                    messageCount.setText(item.num+"");
                    messageContent.setVisibility(View.VISIBLE);
                    messageCount.setVisibility(View.VISIBLE);
                    if(item.istoast){
                        messageCount.setVisibility(View.VISIBLE);
                        messageNoCount.setVisibility(View.GONE);
                    }else {
                        messageCount.setVisibility(View.GONE);
                        messageNoCount.setVisibility(View.VISIBLE);
                    }
                    if(!TextUtils.isEmpty(item.newmessagecontent)){
                        messageContent.setText(item.newmessagecontent);
                    }else {
                        messageContent.setText("");
                        messageContent.setVisibility(View.GONE);
                    }
                    if(item.num==0){
                        messageCount.setVisibility(View.GONE);
                        messageNoCount.setVisibility(View.GONE);
                    }

                    break;
            }
            com.healthy.library.businessutil.GlideCopy.with(messageIcon.getContext())
                    .asBitmap()
                    .load(item.imageRes)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default)
                    
                    .into(messageIcon);
            messageTitle.setText(item.getName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getItemType()!=3){

                        MonMessage monMessageNow=item;
                        monMessageNow.num=0;
                        SpUtils.store(context,monMessageNow.type,new Gson().toJson(monMessageNow));
                        if("评论".equals(item.type)){
                            ARouter.getInstance()
                                    .build(AppRoutes.APP_MESSAGEDIS)
                                    .withString("type",item.type)
                                    .withString("title",item.getName())
                                    .navigation();
                        } else if("赞".equals(item.type)){
                            ARouter.getInstance()
                                    .build(AppRoutes.APP_MESSAGELIKE)
                                    .withString("type",item.type)
                                    .withString("title",item.getName())
                                    .navigation();
                        }else if(item.type.contains("母婴服务小助手")){
                            ARouter.getInstance()
                                    .build(AppRoutes.APP_MESSAGEHELP2)
                                    .withString("type",item.type)
                                    .withString("title",item.getName())
                                    .navigation();
                        }else if(item.type.contains("小助手")){
                            ARouter.getInstance()
                                    .build(AppRoutes.APP_MESSAGEHELP)
                                    .withString("type",item.type)
                                    .withString("title",item.getName())
                                    .navigation();
                        }else {
                            ARouter.getInstance()
                                    .build(AppRoutes.APP_MESSAGETIP)
                                    .withString("type",item.type)
                                    .withString("title",item.getName())
                                    .navigation();
                        }

                    }
                }

            });
            if(item.needShow){

                pLL.addView(view);
            }
        }
    }

    private MessagePAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper helper = new LinearLayoutHelper();
        return helper;
    }

    private void initView() {
    }
}
