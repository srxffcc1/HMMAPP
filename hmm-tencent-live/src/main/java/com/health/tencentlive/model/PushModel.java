package com.health.tencentlive.model;

public class PushModel {
    //奇怪的model

    //// 精简字段，保证尽量不使IM无法发送消息
    //// 商品id             可售库存                    排序              活动id
    //// [a => goodsId]  [b => availableInventory]  [c => ranking] [d => liveActivityId] [e => goodsChildId]

    public String a;
    public int b;
    public int c;
    public String d;
    public String e;

}
