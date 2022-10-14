package com.healthy.library.model;

import java.util.List;

public class OrderGoodsListModel {


    /**
     * id : 501
     * merchantId : 8
     * memberId : 2006220000001645
     * orderId : 403
     * goodsId : 10433
     * goodsTitle : 清园＋开封府＋小宋城1日游
     * goodsImage : http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/bd8afd5d-5513-4845-9778-faee49956f9c.jpg
     * goodsNumber : 1
     * goodsPrice : 0.0
     * payStatus : 1
     * payMoney : 0.0
     * ticketList : [{"id":26,"orderId":null,"orderGoodsId":null,"goodsId":null,"goodsSpec":null,"ticketNo":"15195263","isUse":0,"isRefund":0,"qrcodeBase64":"iVBORw0KGgoAAAANSUhEUgAAAH4AAAB+CAIAAABtQTLfAAABiklEQVR42u3aMZKEMAwEwP3/p9kH3AVbLksjm1YMxmoIXBo+n2g9f2rtrl/WWXvWtYUePXr06NG/jP4pqzrEXfSdvaNHjx49evTos/R1B8cTryk0RI8ePXr06NG/mD57AEWPHj169OjRo0+1ih49evTo0aNHPzkgvGM/6NGjR48ePfosfWedGP5dUujRo0ePHv3V9M/4uqOLf/pCjx49evTo0Y+f6uwaV83/TaozZEWPHj169OjR19HXxW/Zpx8wmEOPHj169OjRL9FnY7NdZNkuCptHjx49evTo0UcHYXWhXd0HlA0s0aNHjx49evR19Nn3nH0Z4T2jR48ePXr06KP0ndV5SK27Bj169OjRo0efpZ+fiJ04hpsWxKJHjx49evTopx0c5/+DtK0L9OjRo0ePHn0j/a42sqOxzvAPPXr06NGjR48+FdF1si6Oz9CjR48ePXr0r6G/dWX06NGjR48efSd955CrLn4LHxznZ7Po0aNHjx791fSd1bnpIyND9OjRo0ePHv3KOl94IuQpADgc0wAAAABJRU5ErkJggg==","ticketShopName":null,"ticketTime":null}]
     * useTicketList : []
     * useNum : 0
     * notUseTicketList : [{"id":26,"orderId":null,"orderGoodsId":null,"goodsId":null,"goodsSpec":null,"ticketNo":"15195263","isUse":0,"isRefund":0,"qrcodeBase64":"iVBORw0KGgoAAAANSUhEUgAAAH4AAAB+CAIAAABtQTLfAAABiklEQVR42u3aMZKEMAwEwP3/p9kH3AVbLksjm1YMxmoIXBo+n2g9f2rtrl/WWXvWtYUePXr06NG/jP4pqzrEXfSdvaNHjx49evTos/R1B8cTryk0RI8ePXr06NG/mD57AEWPHj169OjRo0+1ih49evTo0aNHPzkgvGM/6NGjR48ePfosfWedGP5dUujRo0ePHv3V9M/4uqOLf/pCjx49evTo0Y+f6uwaV83/TaozZEWPHj169OjR19HXxW/Zpx8wmEOPHj169OjRL9FnY7NdZNkuCptHjx49evTo0UcHYXWhXd0HlA0s0aNHjx49evR19Nn3nH0Z4T2jR48ePXr06KP0ndV5SK27Bj169OjRo0efpZ+fiJ04hpsWxKJHjx49evTopx0c5/+DtK0L9OjRo0ePHn0j/a42sqOxzvAPPXr06NGjR48+FdF1si6Oz9CjR48ePXr0r6G/dWX06NGjR48efSd955CrLn4LHxznZ7Po0aNHjx791fSd1bnpIyND9OjRo0ePHv3KOl94IuQpADgc0wAAAABJRU5ErkJggg==","ticketShopName":null,"ticketTime":null}]
     * notUseNum : 1
     */

    private int id;
    private int merchantId;
    private String memberId;
    private int orderId;
    private int goodsId;
    private String goodsTitle;
    private String goodsImage;
    private int goodsNumber;
    private double goodsPrice;
    private int payStatus;
    private double payMoney;
    private int useNum;
    private int notUseNum;
    private List<TicketListBean> ticketList;
    private List<?> useTicketList;
    private List<NotUseTicketListBean> notUseTicketList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public int getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(int goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    public int getUseNum() {
        return useNum;
    }

    public void setUseNum(int useNum) {
        this.useNum = useNum;
    }

    public int getNotUseNum() {
        return notUseNum;
    }

    public void setNotUseNum(int notUseNum) {
        this.notUseNum = notUseNum;
    }

    public List<TicketListBean> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<TicketListBean> ticketList) {
        this.ticketList = ticketList;
    }

    public List<?> getUseTicketList() {
        return useTicketList;
    }

    public void setUseTicketList(List<?> useTicketList) {
        this.useTicketList = useTicketList;
    }

    public List<NotUseTicketListBean> getNotUseTicketList() {
        return notUseTicketList;
    }

    public void setNotUseTicketList(List<NotUseTicketListBean> notUseTicketList) {
        this.notUseTicketList = notUseTicketList;
    }

    public static class TicketListBean {
        /**
         * id : 26
         * orderId : null
         * orderGoodsId : null
         * goodsId : null
         * goodsSpec : null
         * ticketNo : 15195263
         * isUse : 0
         * isRefund : 0
         * qrcodeBase64 : iVBORw0KGgoAAAANSUhEUgAAAH4AAAB+CAIAAABtQTLfAAABiklEQVR42u3aMZKEMAwEwP3/p9kH3AVbLksjm1YMxmoIXBo+n2g9f2rtrl/WWXvWtYUePXr06NG/jP4pqzrEXfSdvaNHjx49evTos/R1B8cTryk0RI8ePXr06NG/mD57AEWPHj169OjRo0+1ih49evTo0aNHPzkgvGM/6NGjR48ePfosfWedGP5dUujRo0ePHv3V9M/4uqOLf/pCjx49evTo0Y+f6uwaV83/TaozZEWPHj169OjR19HXxW/Zpx8wmEOPHj169OjRL9FnY7NdZNkuCptHjx49evTo0UcHYXWhXd0HlA0s0aNHjx49evR19Nn3nH0Z4T2jR48ePXr06KP0ndV5SK27Bj169OjRo0efpZ+fiJ04hpsWxKJHjx49evTopx0c5/+DtK0L9OjRo0ePHn0j/a42sqOxzvAPPXr06NGjR48+FdF1si6Oz9CjR48ePXr0r6G/dWX06NGjR48efSd955CrLn4LHxznZ7Po0aNHjx791fSd1bnpIyND9OjRo0ePHv3KOl94IuQpADgc0wAAAABJRU5ErkJggg==
         * ticketShopName : null
         * ticketTime : null
         */

        private int id;
        private Object orderId;
        private Object orderGoodsId;
        private Object goodsId;
        private Object goodsSpec;
        private String ticketNo;
        private int isUse;
        private int isRefund;
        private String qrcodeBase64;
        private Object ticketShopName;
        private Object ticketTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getOrderId() {
            return orderId;
        }

        public void setOrderId(Object orderId) {
            this.orderId = orderId;
        }

        public Object getOrderGoodsId() {
            return orderGoodsId;
        }

        public void setOrderGoodsId(Object orderGoodsId) {
            this.orderGoodsId = orderGoodsId;
        }

        public Object getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Object goodsId) {
            this.goodsId = goodsId;
        }

        public Object getGoodsSpec() {
            return goodsSpec;
        }

        public void setGoodsSpec(Object goodsSpec) {
            this.goodsSpec = goodsSpec;
        }

        public String getTicketNo() {
            return ticketNo;
        }

        public void setTicketNo(String ticketNo) {
            this.ticketNo = ticketNo;
        }

        public int getIsUse() {
            return isUse;
        }

        public void setIsUse(int isUse) {
            this.isUse = isUse;
        }

        public int getIsRefund() {
            return isRefund;
        }

        public void setIsRefund(int isRefund) {
            this.isRefund = isRefund;
        }

        public String getQrcodeBase64() {
            return qrcodeBase64;
        }

        public void setQrcodeBase64(String qrcodeBase64) {
            this.qrcodeBase64 = qrcodeBase64;
        }

        public Object getTicketShopName() {
            return ticketShopName;
        }

        public void setTicketShopName(Object ticketShopName) {
            this.ticketShopName = ticketShopName;
        }

        public Object getTicketTime() {
            return ticketTime;
        }

        public void setTicketTime(Object ticketTime) {
            this.ticketTime = ticketTime;
        }
    }

    public static class NotUseTicketListBean {
        /**
         * id : 26
         * orderId : null
         * orderGoodsId : null
         * goodsId : null
         * goodsSpec : null
         * ticketNo : 15195263
         * isUse : 0
         * isRefund : 0
         * qrcodeBase64 : iVBORw0KGgoAAAANSUhEUgAAAH4AAAB+CAIAAABtQTLfAAABiklEQVR42u3aMZKEMAwEwP3/p9kH3AVbLksjm1YMxmoIXBo+n2g9f2rtrl/WWXvWtYUePXr06NG/jP4pqzrEXfSdvaNHjx49evTos/R1B8cTryk0RI8ePXr06NG/mD57AEWPHj169OjRo0+1ih49evTo0aNHPzkgvGM/6NGjR48ePfosfWedGP5dUujRo0ePHv3V9M/4uqOLf/pCjx49evTo0Y+f6uwaV83/TaozZEWPHj169OjR19HXxW/Zpx8wmEOPHj169OjRL9FnY7NdZNkuCptHjx49evTo0UcHYXWhXd0HlA0s0aNHjx49evR19Nn3nH0Z4T2jR48ePXr06KP0ndV5SK27Bj169OjRo0efpZ+fiJ04hpsWxKJHjx49evTopx0c5/+DtK0L9OjRo0ePHn0j/a42sqOxzvAPPXr06NGjR48+FdF1si6Oz9CjR48ePXr0r6G/dWX06NGjR48efSd955CrLn4LHxznZ7Po0aNHjx791fSd1bnpIyND9OjRo0ePHv3KOl94IuQpADgc0wAAAABJRU5ErkJggg==
         * ticketShopName : null
         * ticketTime : null
         */

        private int id;
        private Object orderId;
        private Object orderGoodsId;
        private Object goodsId;
        private Object goodsSpec;
        private String ticketNo;
        private int isUse;
        private int isRefund;
        private String qrcodeBase64;
        private Object ticketShopName;
        private Object ticketTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getOrderId() {
            return orderId;
        }

        public void setOrderId(Object orderId) {
            this.orderId = orderId;
        }

        public Object getOrderGoodsId() {
            return orderGoodsId;
        }

        public void setOrderGoodsId(Object orderGoodsId) {
            this.orderGoodsId = orderGoodsId;
        }

        public Object getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Object goodsId) {
            this.goodsId = goodsId;
        }

        public Object getGoodsSpec() {
            return goodsSpec;
        }

        public void setGoodsSpec(Object goodsSpec) {
            this.goodsSpec = goodsSpec;
        }

        public String getTicketNo() {
            return ticketNo;
        }

        public void setTicketNo(String ticketNo) {
            this.ticketNo = ticketNo;
        }

        public int getIsUse() {
            return isUse;
        }

        public void setIsUse(int isUse) {
            this.isUse = isUse;
        }

        public int getIsRefund() {
            return isRefund;
        }

        public void setIsRefund(int isRefund) {
            this.isRefund = isRefund;
        }

        public String getQrcodeBase64() {
            return qrcodeBase64;
        }

        public void setQrcodeBase64(String qrcodeBase64) {
            this.qrcodeBase64 = qrcodeBase64;
        }

        public Object getTicketShopName() {
            return ticketShopName;
        }

        public void setTicketShopName(Object ticketShopName) {
            this.ticketShopName = ticketShopName;
        }

        public Object getTicketTime() {
            return ticketTime;
        }

        public void setTicketTime(Object ticketTime) {
            this.ticketTime = ticketTime;
        }
    }


}
