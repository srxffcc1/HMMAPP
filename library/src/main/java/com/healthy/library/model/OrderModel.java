package com.healthy.library.model;

import java.util.List;

public class OrderModel {
        public long id;
        public int shopId;
        public String shopName;
        public String chainShopName;
        public int buyChannelCode;
        public double payMoney;
        public double goodsMoney;
        public String memberTelephone;
        public String memberName;
        public int payStatus;
        public String payTime;
        public int cancelStatus;
        public int commentStatus;
        public int status;
        public String createTime;
        public List<OrderGoodsList> orderGoodsList;

    public class TicketList {
        public long id;
        public String ticketNo;
        public int isUse;
        public String qrcodeBase64;
    }

    public class OrderGoodsList {
        public long id;
        public long merchantId;
        public String memberId;
        public long orderId;
        public long goodsId;
        public String goodsTitle;
        public String goodsImage;
        public String goodsNo;
        public int goodsNumber;
        public double goodsPrice;
        public int expiredDate;
        public int payStatus;
        public String payTime;
        public String expiredTime;
        public List<TicketList> ticketList;
        public int useNum;

    }
}
