package com.healthy.library.model;

public class ChatMessage {
    public String userName;
    public String message;
    public String groupID;
    public String senderID;
    public int type;//1代表普通文本消息  2代表主播发的自定义消息  3加入直播间   4 购买消息

    public ChatMessage(String userName, String message, int type, String senderID) {
        this.userName = userName;
        this.message = message;
        this.type = type;
        this.senderID = senderID;
    }
}
