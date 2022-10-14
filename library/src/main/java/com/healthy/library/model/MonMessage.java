package com.healthy.library.model;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class MonMessage implements Serializable, MultiItemEntity {
    public String type;//评论 赞 母婴服务小助手 通知
    public String name;//评论 赞 母婴服务小助手 通知
    public int num=0;//消息数量
    public boolean istoast=true;//消息是否显示小红点 默认显示
    public String newmessagecontent;//最新的相关消息
    public String messageTime;//最新的相关消息
    public String extra;//附带信息
    public int itemType;
    public int imageRes;
    public boolean needShow=true;


    public String getName() {
        if(TextUtils.isEmpty(name)){
            return type;
        }else {
            return name;
        }
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
