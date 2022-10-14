package com.health.mine.model;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/10 09:31
 * @des 订单详情
 */

public class OrderBackListModel {


    public long refundId;
    public int refundStatus;
    public long orderId;


    public long merchantId;
    public String merchantName;
    public List<OrderBackGood> orderGoodsRefundsList;


    public class OrderBackGood {
        public long orderGoodsId;
        public int goodsId;
        public String goodsTitle;
        public String goodsImage;
        public int refundNumber;
        public double refundMoney;
    }

}
