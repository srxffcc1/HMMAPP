package com.health.client.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.health.client.R;
import com.healthy.library.model.MonMessage;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.SpUtils;

public class MessageAdapter extends BaseMultiItemQuickAdapter<MonMessage, BaseViewHolder> {


    @Override
    protected void convert(final BaseViewHolder helper, final MonMessage item) {
        ImageView messageIcon;
        TextView messageTitle;
        TextView messageCount;
        TextView messageNoCount;
        TextView messageContent;
        TextView messageTime;
        Switch messageSwitch;
        messageIcon = (ImageView) helper.getView(R.id.messageIcon);
        messageTitle = (TextView) helper.getView(R.id.messageTitle);
        messageCount = (TextView) helper.getView(R.id.messageCount);
        messageNoCount = (TextView) helper.getView(R.id.messageNoCount);
        messageContent = (TextView) helper.getView(R.id.messageContent);
        messageTime = (TextView) helper.getView(R.id.messageTime);
        messageSwitch = (Switch) helper.getView(R.id.messageSwitch);
        com.healthy.library.businessutil.GlideCopy.with(messageIcon.getContext())
                .asBitmap()
                .load(item.imageRes)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)
                
                .into(messageIcon);
        messageTitle.setText(item.getName());
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helper.getItemViewType()!=3){

                    MonMessage monMessageNow=item;
                    monMessageNow.num=0;
                    SpUtils.store(mContext,monMessageNow.type,new Gson().toJson(monMessageNow));
                    if("评论".equals(item.type)){
                        ARouter.getInstance()
                                .build(AppRoutes.APP_MESSAGEDIS)
                                .withString("type",item.type)
                                .navigation();
                    } else if("赞".equals(item.type)){
                        ARouter.getInstance()
                                .build(AppRoutes.APP_MESSAGELIKE)
                                .withString("type",item.type)
                                .navigation();
                    }else if(item.type.contains("母婴服务小助手")){
                        ARouter.getInstance()
                                .build(AppRoutes.APP_MESSAGEHELP2)
                                .withString("type",item.type)
                                .navigation();
                    }else if(item.type.contains("小助手")){
                        ARouter.getInstance()
                                .build(AppRoutes.APP_MESSAGEHELP)
                                .withString("type",item.type)
                                .navigation();
                    }else {
                        ARouter.getInstance()
                                .build(AppRoutes.APP_MESSAGETIP)
                                .withString("type",item.type)
                                .navigation();
                    }

                }
            }

        });
        switch (helper.getItemViewType()) {
            case 1:
                messageCount.setText(item.num+"");
                messageCount.setVisibility(View.VISIBLE);
                messageNoCount.setVisibility(View.VISIBLE);
                if(item.num==0){
                    messageCount.setVisibility(View.GONE);
                    messageNoCount.setVisibility(View.GONE);
                }
                break;
            case 2:
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
            case 3:
                messageSwitch.setChecked(item.istoast);
                messageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!buttonView.isPressed()){
                            return;
                        }
                        item.istoast=isChecked;
                        SpUtils.store(mContext,item.type,new Gson().toJson(item));
                    }
                });
                break;
            case 4:
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

    }

    public MessageAdapter() {
        super(null);
        addItemType(1, R.layout.home_item_message_special);
        addItemType(2, R.layout.home_item_message_normal);//弱提醒
        addItemType(3, R.layout.home_item_message_switch);
        addItemType(4, R.layout.home_item_message_normal);//正常提醒
    }

    private void initView() {

    }
}
