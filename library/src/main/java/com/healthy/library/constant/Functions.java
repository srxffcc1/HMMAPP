package com.healthy.library.constant;

/**
 * @author Li
 * @date 2019/03/28 11:17
 * @des 接口
 */
@Deprecated
public class Functions {

    public static final String FUNCTION = "function";

    /**
     * 获取认证码
     */
    public static final String FUNCTION_GET_CODE = "1001";
    /**
     * 登录
     */
    public static final String FUNCTION_LOGIN = "1002";

    /**
     * 退出登录
     */
    public static final String FUNCTION_LOGOUT = "1003";


    /**
     * 修改状态
     */

    public static final String FUNCTION_CHANGE_STATUS = "1010";

    /**
     * 修改状态
     */

    public static final String FUNCTION_CHANGE_STATUS_NEW = "1006";

    /**
     * 个人信息
     */
    public static final String FUNCTION_USER_INFO = "1011";

    /**
     * 意见反馈
     */
    public static final String FUNCTION_FEEDBACK = "1012";

    /**
     * 商城首页数据
     */
    public static final String FUNCTION_MALL_INDEX = "2001";
    /**
     * 商品列表
     */
    public static final String FUNCTION_GOODS_LIST = "2002";

    /**
     * 商品详情
     */
    public static final String FUNCTION_GOODS_DETAIL = "2003";

    /**
     * 门店列表
     */
    public static final String FUNCTION_STORE_LIST = "2004";

    /**
     * 门店详情
     */
    public static final String FUNCTION_STORE_DETAIL = "2005";

    /**
     * 获取省
     */
    public static final String FUNCTION_PROVINCE_LIST = "2006";

    /**
     * 获取市
     */
    public static final String FUNCTION_CITY_LIST = "2007";
    /**
     * 添加评论
     */
    public static final String FUNCTION_ADD_COMMENT = "2008";

    /**
     * 门店评论列表
     */
    public static final String FUNCTION_COMMENTS = "2009";

    /**
     * 门店商品
     */
    public static final String FUNCTION_STORE_GOODS = "2010";
    /**
     * 上传评论图片
     */
    public static final String FUNCTION_UPLOAD_PIC = "2011";

    /**
     * 生成订单
     */
    public static final String FUNCTION_GENERATE_ORDER = "3001";

    /**
     * 订单列表
     */
    public static final String FUNCTION_ORDER_LIST = "3002";

    /**
     * 订单详情
     */
    public static final String FUNCTION_ORDER_DETAIL = "3003";

    /**
     * 获取支付消息
     */
    public static final String FUNCTION_GET_PAY_INFO = "3004";

    /**
     * 获取订单生成时间以及服务器时间
     */
    public static final String FUNCTION_SERVER_DATE = "3005";

    /**
     * 取消订单
     */
    public static final String FUNCTION_CANCEL_ORDER = "3006";

    /**
     * 获取 待付款 待评价 数量
     */
    public static final String FUNCTION_ORDER_TYPE_NUM = "3007";

    /**
     * 待服务列表
     */
    public static final String FUNCTION_IN_SERVICE = "3008";

    /**
     * 服务记录
     */
    public static final String FUNCTION_SERVICE_RECORD = "3009";

    /**
     * 服务单详情
     */
    public static final String FUNCTION_SERVICE_DETAIL = "3010";

    /**
     * 经期日历
     */
    public static final String FUNCTION_CALENDAR = "4001";

    /**
     * app首页
     */
    public static final String FUNCTION_INDEX_INDEX = "4002";

    /**
     * 孕期妈妈变化
     */
    public static final String FUNCTION_PREGNANCY_MOM_CHANGE = "4003";
    /**
     * 孕期宝宝变化
     */
    public static final String FUNCTION_PREGNANCY_BABY_CHANGE = "4004";

    /**
     * 产检表
     */
    public static final String FUNCTION_BIRTH_CHECK_LIST = "4007";

    /**
     * 获取B超信息
     */
    public static final String FUNCTION_ANALYZE = "4011";

    /**
     * 待产包
     */
    public static final String FUNCTION_PACKAGE_LIST = "4012";
    /**
     * 疫苗列表
     */
    public static final String FUNCTION_VACCINE_LIST = "4013";

    /**
     * 修改待产包状态
     */
    public static final String FUNCTION_CHANGE_PACKAGE = "4021";

    /**
     * 知识列表
     */
    public static final String FUNCTION_TIPS_LIST = "4022";

    /**
     * 产后宝宝变化
     */
    public static final String FUNCTION_PARENTING_BABY_CHANGE = "4023";

    /**
     * 产后妈妈变化
     */
    public static final String FUNCTION_PARENTING_MOM_CHANGE = "4026";

    /**
     * 修改收藏状态
     */

    public static final String FUNCTION_UPDATE_COLLECTED_STATUS = "4027";

    /**
     * 查询收藏列表
     */
    public static final String FUNCTION_COLLECTIONS = "4028";

    /**
     * 查询文章是否被收藏
     */
    public static final String FUNCTION_CHECK_COLLECTED_STATUS = "4029";

    /**
     * 上传悬赏求助的图片
     */
    public static final String FUNCTION_UPLOAD_REWARD_PICTURES = "5001";

    /**
     * 获取最新求助
     */
    public static final String FUNCTION_LATEST_QUESTION = "5002";

    /**
     * 获取热门求助
     */
    public static final String FUNCTION_HOT_QUESTION = "5003";

    /**
     * 提交悬赏问答
     */
    public static final String FUNCTION_SUBMIT_REWARD = "5006";

    /**
     * 检查余额是否够悬赏
     */
    public static final String FUNCTION_CHECK_BALANCE = "5019";
    /**
     * 设置密码，获取验证码 type:1 设置密码 2：修改密码
     */
    public static final String FUNCTION_GET_PAY_CODE = "5007";
    /**
     * 验证验证码
     */
    public static final String FUNCTION_GET_YANZHENG_CODE = "5012";
    /**
     * 设置个人支付密码
     */
    public static final String FUNCTION_SET_PASSWORD = "5009";
    /**
     * 验证原密码是否正确
     */
    public static final String FUNCTION_OLD_PASSWORD = "5011";
    /**
     * 修改支付密码
     */
    public static final String FUNCTION_UPDATE_PASSWORD = "5010";

    /**
     * 获取问题详情
     */
    public static final String FUNCTION_QUESTION_DETAIL = "5013";
    /**
     * 支付前的验证支付密码
     */
    public static final String FUNCTION_PAY_MOWTME = "5022";
    /**
     * 支付前的验证支付密码
     */
    public static final String FUNCTION_GET_QUESTIONS_ANSWERS = "5017";

    /**
     * 上传音频文件
     */
    public static final String FUNCTION_UPLOAD_AUDIO = "5021";

    /**
     * 采纳最佳答案
     */
    public static final String FUNCTION_ADOPT_REPLY = "5023";

    /**
     * 追加赏金
     */
    public static final String FUNCTION_IMPROVE_REWARD = "5024";
    /**
     * 账户余额，憨豆豆
     */
    public static final String FUNCTION_ACCOUNT_CENTER = "5015";
    /**
     * 收入明细
     */
    public static final String FUNCTION_DETAILED_INCOME = "5014";
    /**
     * 消费明细 consumption
     */
    public static final String FUNCTION_DETAILED__CONSUMPTION = "5016";

    /**
     * 发布问题
     */
    public static final String FUNCTION_RELEASE_REPLY = "5005";
    /**
     * 问答首页
     */
    public static final String FUNCTION_GET_EXPERTS_HOME = "5025";

    /**
     * 专家列表
     */
    public static final String FUNCTION_EXPERT_LIST = "5026";


    /**
     * 专家列表
     */
    public static final String FUNCTION_EXPERT_LIST2 = "5070";

    /**
     * 专家主页
     */
    public static final String FUNCTION_EXPERT_HOMEPAGE = "5027";

    /**
     * 问专家
     */
    public static final String FUNCTION_ASK_EXPERT = "5028";

    /**
     * 专家问题详情
     */
    public static final String FUNCTION_EXPERT_QUESTION_DETAIL = "5029";

    /**
     * 获取专家是否在线
     */
    public static final String FUNCTION_EXPERT_STATUS = "5033";
}