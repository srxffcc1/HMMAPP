package com.healthy.library.constant;

/**
 * @author Li
 * @date 2019/03/28 10:07
 * @des
 */
@Deprecated
public class Constants {


    public static final String PAGE_SIZE = "10";

    /**
     * 服务地址：上门
     */
    public static final String SERVICE_ADDRESS_HOME = "1";

    /**
     * 服务地址：到店
     */
    public static final String SERVICE_ADDRESS_SHOP = "0";

    public static final String STATUS = "status";

    /**
     * 为设置状态
     */
    public static final String STATUS_NONE = "0";

    /**
     * 不设置状态跳过了
     */
    public static final String STATUS_DEFAULT = "-1";

    /**
     * 状态为男士优孕备孕
     */
//    public static final String STATUS_MAN_FOR_PREGNANCY = "11";
//    /**
//     * 状态为男士优孕怀孕中
//     */
//    public static final String STATUS_MAN_PREGNANCY = "12";
//    /**
//     * 状态为男士优孕孕后
//     */
//    public static final String STATUS_MAN_AFTER_PREGNANCY = "13";


    /**
     * 状态为无
     */
    public static final String STATUS_FOR_NULL = "-1";

    /**
     * 状态为备孕
     */
    public static final String STATUS_FOR_PREGNANCY = "1";
    /**
     * 状态为怀孕中
     */
    public static final String STATUS_PREGNANCY = "2";
    /**
     * 状态为孕后
     */
    public static final String STATUS_AFTER_PREGNANCY = "3";

    /**
     * 孕后一周岁
     */
    public static final String STATUS_AFTER_ONE_YEAR = "4";

    /**
     * 状态为备孕
     */
    public static final int STATUS_FOR_PREGNANCYCODE = 1001;

    /**
     * 状态为怀孕中
     */
    public static final int STATUS_PREGNANCYCODE = 1002;
    /**
     * 状态为孕后
     */
    public static final int STATUS_AFTER_PREGNANCYCODE = 1003;

    /**
     * 孕后一周岁
     */
    public static final int STATUS_AFTER_ONE_YEARCODE = 1004;

    /**
     * 服务预约 更换门店
     */
    public static final int STATUS_APPOINTMENT_SHOP_CODE = 1005;

    /**
     * 服务预约 更新状态
     */
    public static final int STATUS_APPOINTMENT_LIST_CODE = 1006;


    /**
     * 宝宝性别男
     */
    public static final String SEX_BOY = "1";
    /**
     * 宝宝性别女
     */
    public static final String SEX_GIRL = "0";

    /**
     * 顺产
     */
    public static final String BORN_TYPE_NORMAL = "1";

    /**
     * 剖腹产
     */
    public static final String BORN_TYPE_ABNORMAL = "2";


    /**
     * 婷美
     */
    public static final String BRAND_TM = "1";
    /**
     * 小儿推拿
     */
    public static final String BRAND_ZYT = "2";
    /**
     * mei博士
     */
    public static final String BRAND_MBS = "3";


    /**
     * 支付宝支付
     */
//    public static final String PAY_IN_A_LI = "1";
    public static final String PAY_IN_A_LI = "5";

    /**
     * 微信支付
     */
//    public static final String PAY_IN_WX = "2";
    public static final String PAY_IN_WX = "4";

    /**
     * 订单类型-全部
     */
    public static final String ORDER_TYPE_ALL = "1";

    /**
     * 订单类型-待支付
     */
    public static final String ORDER_TYPE_TO_BE_PAID = "2";

    /**
     * 订单类型-待评价
     */
    public static final String ORDER_TYPE_COMMENT = "3";
    /**
     * 订单类型-待使用
     */
    public static final String ORDER_TYPE_USE = "4";
    /**
     * 刷新/加载数据
     */
    public static final String DATA_REFRESH = "1";
    public static final String DATA_LOAD = "2";

    /**
     * 商品为套盒
     */
    public static final String GOODS_TYPE_PACKAGE = "2";

    /**
     * 订单状态：已支付
     */
    public static final String ORDER_STATUS_PAID = "1";

    /**
     * 订单状态：未支付
     */
    public static final String ORDER_STATUS_UNPAID = "0";

    /**
     * 订单状态：已取消
     */
    public static final String ORDER_STATUS_CANCELLED = "1";

    /**
     * 订单状态：未取消
     */
    public static final String ORDER_STATUS_UNCANCELLED = "0";

    /**
     * 订单状态：已评论
     */
    public static final String ORDER_STATUS_COMMENTED = "1";

    /**
     * 订单状态：未评论
     */
    public static final String ORDER_STATUS_UNCOMMENTED = "0";
    /**
     * 时间格式pattern
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 服务：计次
     */
    public static final String SERVICE_IN_NUMBER = "1";

    /**
     * 服务：计时
     */
    public static final String SERVICE_IN_TIME = "2";

    /**
     * 悬赏-微信支付
     */
    public static final String REWARD_PAY_IN_WX = "0";

    /**
     * 悬赏-支付宝支付
     */
    public static final String REWARD_PAY_IN_ALI = "1";

    /**
     * 悬赏-憨豆豆支付
     */
    public static final String REWARD_PAY_IN_HDD = "2";

    /**
     * 悬赏问题状态：进行中
     */
    public static final String REWARD_STATUS_IN = "1";

    /**
     * 悬赏问题状态：已关闭
     */
    public static final String REWARD_STATUS_CLOSE = "2";

    /**
     * 悬赏问题状态：已完成
     */
    public static final String REWARD_STATUS_FINISH = "3";

    /**
     * 悬赏问题状态：异常
     */
    public static final String REWARD_STATUS_EXCEPTION = "4";

    /**
     * 支付：悬赏
     */
    public static final int PAY_REWARD = 1;

    /**
     * 支付：提高悬赏
     */
    public static final int PAY_IMPROVE_REWARD = 2;

    /**
     * 支付：问专家
     */
    public static final int PAY_ASK_EXPERT = 3;

    /**
     * 匿名回答
     */
    public static final int REPLY_IN_ANONYMOUS = 2;

    /**
     * 实名回答
     */
    public static final int REPLY_IN_REAL_NAME = 1;


    /**
     * 文字回答
     */
    public static final int REPLY_IN_TEXT = 1;
    /**
     * 语音回答
     */
    public static final int REPLY_IN_VOICE = 2;

}
