package com.healthy.library.message;

public class CommentEvent {
    public int type;//1 弹出评论dialog 2刷新数据 3 请求详情  4 分享 5通知滑动过了

    public CommentEvent(int type) {
        this.type = type;
    }
}
