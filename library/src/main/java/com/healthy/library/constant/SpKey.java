package com.healthy.library.constant;

/**
 * @author Li
 * @date 2019/03/28 10:07
 * @des
 */

public class SpKey {
    public static final String USE_SEACHTIP = "UseSEACH";//商品搜索历史
    public static final String USE_TOKEN = "Usetoken";//用户 UserInfoExModel 对象
    public static final String USE_DOWN = "UseDown";//用户APP下载
    public static final String USE_UPDATE = "UseUpdate";//用户需要更新
    public static final String TOKEN = "token";//token
    public static final String GETTOKEN = "gettoken";//用户导购码
    public static final String MESSAGE_KEY = "message_key";//推送提示弹窗
    public static final String USER_ID = "userId";//userId
    public static final String USER_NICK = "userNick";//user昵称
    public static final String USER_MARKET_CHECK = "userMarketCheckZ";//购物车选择缓存
    public static final String USER_MARKET_MAN = "userMarketMan";//购物车数据缓存
    public static final String USER_Wel_AD_Bean = "userWelAdBean";//开屏广告
    public static final String USER_ICON = "userIcon";//用户头像
    public static final String USER_Main_AD_Bean = "userMainAdBean";//主页弹窗广告
    public static final String POST_TMP = "postTmp";//帖子草稿
    public static final String SUP_TMP = "supTmp";//自定义的小工具里的图标 ToolsFeedSUPFragment
    public static final String MED_TMP = "medTmp";//自定义的小工具里的图标 ToolsFeedMEDFragment
    public static final String TOOL_TMP = "toolTmp";//用户的历史小工具记录
    public static final String LOC_ORG = "locOrg";//用户定位数据
    public static final String VIPSHOP = "vipShop";//用户线下实体 用于 线下下单需求
    public static final String USER_PATCH = "userPatch";//用户补丁缓存
    public static final String INSTALL_REL_CODE = "installRelCode";//用户安装时候的导购码
    public static final String USE_INDEXSEARCH= "IndexSearch";//搜索历史
    public static final String JIGUANGALI = "jiguangAli";//userId

    /**
     * 品牌名称
     */
    public static final String SHOP_BRAND = "shopBrand";//访问门店缩写
    public static final String CHOSE_MC = "choseMc";//选择的访问门店Merchantid
    public static final String CHOSE_PARTNERID = "chosePartnerId";//选择的访问门店partnerId
    public static final String HSOT_STATUS = "hostStatus";//散客还是会员
    public static final String CHOSE_SHOP = "choseShopId";//选择的访问门店 shopid
    public static final String CHOSE_SHOPNAME = "choseShopName";//选择的访问门店 系列
    public static final String CHOSE_SHOPNAMEDETAIL = "choseShopNameDetail";//选择的访问门店 系列
    public static final String CHOSE_SHOPADDRESS = "choseShopAddress";//选择的访问门店 系列
    public static final String CHOSE_SHOPDISTANCE = "choseShopdistance";//选择的访问门店 系列
    public static final String LOC_TEST = "locTest";//测试环境模拟定位 目前可能已废弃
    public static final String IS_LOCERROR = "is_locError";//是否没开启定位
    public static final String LOC_CHOSE = "locChose";//选择的地理数据 对标 LOC_ORG
    @Deprecated  public static final String POST_TMP2 = "postTmp2";// 废弃 意义不明
    public static final String DISCUSS_TMP = "discussTmp";//评论草稿
    @Deprecated public static final String UPDATE_TMP = "updateTmp";
    public static final String JOB_TMP = "jobTmp";//技师招募 草稿
    public static final String WARM_TMP = "warMTmp";// 为保护个人隐私 条例是否看过
    @Deprecated public static final String STATUS = "status";//以前用来判断状态的
    public static final String STATUS_STR = "statusStr";//memberState 中文 用户阶段中文
    public static final String STATUS_USER = "statusUser";//memberState 用户阶段
    public static final String PHONE = "phone";//用户手机号
    public static final String USER_BIRTHDAY = "birthday";//用户生日
    public static final String USER_CREATE_DATE = "UserCreateDate";//用户注册时间
    public static final String USER_DIALOG_TIME = "UserDialogTime";//完善信息弹窗提示时间 用来保证不多弹出
    @Deprecated public static final String SHOW_GUIDE = "showGuide";//是否看过引导
     public static final String SHOW_JUST_LOGIN = "showJustLogin";//是否看过引导
    public static final String GIFT_ALREADYSHOW = "showGiftAlready";//礼物包显示过
//    @Deprecated public static final String COUPON_TMP = "couponTmpTime";
//    @Deprecated public static final String KICKPASSWORD = "1";
//    @Deprecated public static final String VIDEOPASSWORD = "Wm5NbUlVNTBOU0YwRsRlVZV=";
    public static final String LIVEHOSTID = "liveHostId";//主播id
    public static final String LIVETMPCOURSEID = "liveTmpCourseId";//直播悬浮窗 直播id
    public static final String LIVETMPCOURSEADDRESS = "liveTmpAddress";//直播悬浮窗 直播地址
    public static final String CITYNAMEPARTNERNAME = "cityNamePartnerName";//储存格式为  六安·爱婴金摇篮  电子会员卡展示需要


//    public static final String TONGLIANPHONEHAS = "tonglianphonehas";//通联手机绑定状态
    public static final String TONGLIANBIZUSER= "tonglianbizuser";//通联对应的bizuserid
    public static final String PLUSSTATUS = "plusStatus";//是不是plus用户
    public static final String YSLOOK = "ysLook";//开机协议1 同意状态
    public static final String YSLOOKAGREE = "ysLookAgree";//开机协议2 同意状态
    public static final String TEST_IP = "testIp";//测试环境可以修改Ip
    public static final String AUDITSTATUS = "auditStatus";//true 为审核中 fasle为通过
    public static final String INITSTATUS = "auditInitStatus";//true 为审核中 fasle为通过
    public static final String OPERATIONSTATUS = "operationStatus";//运营模式
    public static final String KILLTIME = "killTime";//提示热更新弹窗的时间
    public static final String KILLFILETIME = "killFileTime";//提示热更新弹窗的时间
    public static final String KILLSETTINGTIME = "killSettingTime";//当前访问的门店关店中! 提示窗提醒时间 public static final String KILLTIME = "killTime";//true 为审核中 fasle为通过
    public static final String CUSTOMBOTTOM = "CUSTOMBOTTOM";//首页配置
    public static final String INDEXPLANID = "indexPlanId";//首页方案Id
    public static final String VERSION_CHECK_FLAG = "versionCheckFlag";//版本检测指导 false代表有问题 true代表没问题

}
