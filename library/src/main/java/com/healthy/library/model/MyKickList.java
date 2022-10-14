package com.healthy.library.model;

public class MyKickList {


    /**
     * id : 112
     * merchantId : 32
     * merchantName : 苏州自由飞翔母婴用品有限公司
     * bargainMemberId : 289
     * goodsId : 92904
     * goodsType : 2
     * goodsTitle : 雅士利亲儿硕2段900g
     * goodsImage : http://hmm-public.oss-cn-beijing.aliyuncs.com/data/upload/mall/store/goods/12/12_701596174393255.jpg
     * goodsStorePrice : null
     * goodsPlatformPrice : 10.0
     * floorPrice : 0.1
     * bargainTimeLength : 24
     * bargainInventory : null
     * initialInventory : null
     * joinNum : null
     * joinStatus : null
     * bargainStatus : 0
     * bargainStatusStr : 砍价中
     * moneyStatus : null
     * discountMoney : 4.16
     * bargainNum : null
     * joinTime : 2020-08-17 10:45:02
     * endTime : 2020-08-20 23:59:59
     * shopId : null
     * distance : null
     * addressDetails : null
     * orderInfo : null
     */

    public int id;
    public int merchantId;
    public String merchantName;
    public String bargainMemberId;
    public int goodsId;
    public int goodsType;
    public String goodsTitle;
    public String goodsImage;
    public String goodsStorePrice;
    public double goodsPlatformPrice;
    public double floorPrice;
    public int bargainTimeLength;
    public String bargainInventory;
    public String initialInventory;
    public String joinNum;
    public String joinStatus;
    public int bargainStatus;
    public String bargainStatusStr;
    public String moneyStatus;
    public double discountMoney;
    public String bargainNum;
    public String joinTime;
    public String endTime;
    public String shopId;
    public String distance;
    public String addressDetails;
    public OrderInfo orderInfo;

    public class OrderInfo {
        public String orderId;
        public String paymentStatus;
        public int orderStatus;
        public String payAmount;
        public String latestPayTime;//最晚支付时间
        public String createdTime;//订单创建时间
        public String paySuccessTime;//支付成功时间
    }

}
