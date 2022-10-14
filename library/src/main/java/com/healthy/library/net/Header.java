package com.healthy.library.net;

/**
 * @author Li
 * @date 2019/03/29 16:20
 * @des
 */
public class Header {

    /**
     * platformVersion : "6.0.1",//平台版本号，如手机系统版本"6.0.1"
     * platformCode : "Android",//平台标识，如手机系统 Android，IPhone（必传,值写死）
     * cmdId : "app_xiaomi",//渠道号 "app_xiaomi",渠道标识（必传）
     * token :  "",//身份验证标识 （登陆后必传，当没有时传入空字串）
     * appVersion : "1.0.0",//"1.0.0"app版本号（必传）
     * userId : "",//Base64类型userId(有些开放接口，校验token与不校验token混合的)
     * userLoginCategory : 1,//用户类型1.APP登录 2.支付宝登录 3.微信登录
     * uuid : "3597e2ee-359a-4858-920a-45e1106f7bdb",//设备号 "3597e2ee-359a-4858-920a-45e1106f7bdb",(必传）
     * ts : "",//时间戳
     * traceId : "",//跟踪ID （建议传入唯一值，如UUID）
     * function : "1001",//接口标识（必传）
     * phoneName : "Xiaomi",//手机名称 "Xiaomi", （必传）
     */

    private String platformVersion;
    private String platformCode;
    private String cmdId;
    private String token;
    private String appVersion;
    private String memberId;
    private String userLoginCategory;
    private String uuid;
    private String ts;
    private String traceId;
    private String function;
    private String phoneName;

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setUserId(String memberId) {
        this.memberId = memberId;
    }

    public String getUserLoginCategory() {
        return userLoginCategory;
    }

    public void setUserLoginCategory(String userLoginCategory) {
        this.userLoginCategory = userLoginCategory;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }
}
