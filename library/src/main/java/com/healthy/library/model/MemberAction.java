package com.healthy.library.model;

public class MemberAction {
    String platformVersion;//	软件版本
    int platform;//	1.Android 2.IOS 3.小程序
    int type;//	事件类  1.页面访问 2.按钮点击 3.新用户大礼包 4.拼团下单 5.砍价下单 6.领券中心
    String typeCode;//	页面代码或 按钮代码 或特殊事件代码
    String activityCode;//	触发页面的代码
    String memberId;//	memberid

    public MemberAction(String platformVersion, int platform, int type, String typeCode, String activityCode, String memberId) {
        this.platformVersion = platformVersion;
        this.platform = platform;
        this.type = type;
        this.typeCode = typeCode;
        this.activityCode = activityCode;
        this.memberId = memberId;
    }
}
