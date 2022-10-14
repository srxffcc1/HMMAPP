package com.healthy.library.model;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/10 09:31
 * @des 订单详情
 */

public class OrderMallDetailModel {


    public long id;
    public long shopId;
    public String orderNum;
    public String shopName;
    public String chainShopName;
    public String shopBrandName;
    public int merchantId;
    public double payMoney;
    public double goodsMoney;
    public int payStatus;
    public int cancelStatus;
    public int commentStatus;
    public int status;
    public double commentScore;
    public List<OrderGood> orderGoodsList;
    public String cityNo;
    public int buyChannelCode;

    public String memberTelephone;
    public String memberName;
    public String payTime;
    public String createTime;
    public double denomination;
    public double discountMoney;

    public String remark;

    public List<Refunds> orderRefundsList;

    public class OrderGood {

        public long id;
        public int merchantId;
        public String memberId;
        public long orderId;
        public long goodsId;
        public String goodsTitle;
        public String goodsImage;
        public String goodsNo;
        public int goodsNumber;
        public String goodsCategory;
        public double goodsPrice;
        public double payMoney;
        public int expiredDate;
        public int payStatus;
        public String payTime;
        public String expiredTime;
        public List<Ticket> ticketList;
        public String useNum;
        public String description;

        public int getNowTicketSize() {
            int result = 0;
            if (ticketList != null && ticketList.size() > 0) {
                for (int i = 0; i < ticketList.size(); i++) {
                    if (ticketList.get(i).isRefund != 1) {
                        result++;
                    }
                }
            }

            return result;
        }
    }

    public class Refunds {
        public long refundId;
        public int refundStatus;
        public List<GoodsRefunds> orderGoodsRefundsList;
    }

    public class Ticket {

        public int id;
        public int isRefund;
        public String ticketNo;
        public int isUse;
        public String qrcodeBase64;
    }

    public class GoodsRefunds {
        public long orderGoodsId;
        public List<Ticket> ticketNoList;

    }

}
