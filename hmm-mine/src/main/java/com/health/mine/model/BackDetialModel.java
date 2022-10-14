package com.health.mine.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class BackDetialModel implements MultiItemEntity {

    /**
     * id : 12
     * memberId : 2006220000001645
     * memberName : 小小炜炜
     * memberPhone : 13520409204
     * memberFaceUrl : null
     * mchId : 8
     * mchName : 爱婴岛
     * shopId : 134
     * shopName : 爱婴岛
     * shopChainName : null
     * shopBrandName : null
     * shopAddress : null
     * goodsId : 26488
     * goodsSpec : 258
     * goodsBarCodeSku : null
     * goodsTitle : 贝亲婴儿棉签 灰色 200 s
     * goodsSpecDesc : 灰色 200 s
     * goodsImage : http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/d3706e06-0dd3-47ca-9e30-63e4734222b1.jpg
     * goodsPrice : 0.01
     * orderId : 298
     * orderNum : 1357916823093280
     * subOrderId : 298
     * subOrderNum : 1357916823093280
     * orderDetailId : 404
     * orderCreateTime : 2020-07-08 14:33:20
     * orderPaymentTime : null
     * refundQuantity : 1
     * refundAmount : 0.01
     * refundTicket : null
     * refundReason : 缺少件
     * refundComment : 12345
     * attachList : [{"id":24,"refundId":12,"attachUrl":"http://hmm-public.oss-cn-beijing.aliyuncs.com/hmm-user-dev/images/bfda0d81-7290-4d25-a369-75f23956fe40.9j","createTime":"2020-07-08 21:02:18","updateTime":"2020-07-08 21:02:18"},{"id":25,"refundId":12,"attachUrl":"http://hmm-public.oss-cn-beijing.aliyuncs.com/hmm-user-dev/images/a98af23b-ef53-4409-abd9-11b613898c2f.9j","createTime":"2020-07-08 21:02:18","updateTime":"2020-07-08 21:02:18"}]
     * paymentType : 0
     * outTradeNo : null
     * transactionNo : null
     * paymentTime : null
     * status : -1
     * disposeAgree : 0
     * disposeMember : null
     * disposeAmount : null
     * disposeComment : null
     * createTime : 2020-07-08 21:02:18
     * updateTime : 2020-07-08 21:50:27
     * statusStr : null
     * handleStatus : null
     * handleStatusStr : null
     */

    private long id;
    private String refundNum;
    private String memberId;
    private String memberName;
    private String memberPhone;
    private Object memberFaceUrl;
    private int mchId;
    private String mchName;
    private long shopId;
    private String shopName;
    private Object shopChainName;
    private Object shopBrandName;
    private Object shopAddress;
    private long goodsId;
    private int goodsSpec;
    private Object goodsBarCodeSku;
    private String goodsTitle;
    private String goodsSpecDesc;
    private String goodsImage;
    private double goodsPrice;
    private long orderId;
    private String orderNum;
    private long subOrderId;
    private String subOrderNum;
    private long orderDetailId;
    private String orderCreateTime;
    private Object orderPaymentTime;
    private int refundQuantity;
    private double refundAmount;//退款金额
    private Object refundTicket;
    private String refundReason;
    private String refundComment;
    private int paymentType;//1 2 3 支付宝 微信
    private Object outTradeNo;
    private Object transactionNo;
    private String paymentTime;//退款成功时间
    private int status;//0 未处理 1 已处理 2退款完成
    private int disposeAgree;
    private int unifyOrderDetailRefundCount;
    private int unifyOrderDetailRefundCancelCount;
    private Object disposeMember;
    private String disposeAmount;
    private Object disposeComment;
    private String createTime;//申请时间
    private String updateTime;
    private String disposeTime;//处理时间
    private Object statusStr;
    private Object handleStatus;
    private Object handleStatusStr;
    private List<AttachListBean> attachList;

    public String getDisposeTime() {
        return disposeTime;
    }

    public void setDisposeTime(String disposeTime) {
        this.disposeTime = disposeTime;
    }

    public String getRefundNum() {
        return refundNum;
    }

    public void setRefundNum(String refundNum) {
        this.refundNum = refundNum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public Object getMemberFaceUrl() {
        return memberFaceUrl;
    }

    public void setMemberFaceUrl(Object memberFaceUrl) {
        this.memberFaceUrl = memberFaceUrl;
    }

    public int getMchId() {
        return mchId;
    }

    public void setMchId(int mchId) {
        this.mchId = mchId;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Object getShopChainName() {
        return shopChainName;
    }

    public void setShopChainName(Object shopChainName) {
        this.shopChainName = shopChainName;
    }

    public Object getShopBrandName() {
        return shopBrandName;
    }

    public void setShopBrandName(Object shopBrandName) {
        this.shopBrandName = shopBrandName;
    }

    public Object getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(Object shopAddress) {
        this.shopAddress = shopAddress;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(int goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public Object getGoodsBarCodeSku() {
        return goodsBarCodeSku;
    }

    public void setGoodsBarCodeSku(Object goodsBarCodeSku) {
        this.goodsBarCodeSku = goodsBarCodeSku;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsSpecDesc() {
        return goodsSpecDesc;
    }

    public void setGoodsSpecDesc(String goodsSpecDesc) {
        this.goodsSpecDesc = goodsSpecDesc;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public long getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(long subOrderId) {
        this.subOrderId = subOrderId;
    }

    public String getSubOrderNum() {
        return subOrderNum;
    }

    public void setSubOrderNum(String subOrderNum) {
        this.subOrderNum = subOrderNum;
    }

    public long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public Object getOrderPaymentTime() {
        return orderPaymentTime;
    }

    public void setOrderPaymentTime(Object orderPaymentTime) {
        this.orderPaymentTime = orderPaymentTime;
    }

    public int getRefundQuantity() {
        return refundQuantity;
    }

    public void setRefundQuantity(int refundQuantity) {
        this.refundQuantity = refundQuantity;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Object getRefundTicket() {
        return refundTicket;
    }

    public void setRefundTicket(Object refundTicket) {
        this.refundTicket = refundTicket;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundComment() {
        return refundComment;
    }

    public void setRefundComment(String refundComment) {
        this.refundComment = refundComment;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public Object getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(Object outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Object getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(Object transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDisposeAgree() {
        return disposeAgree;
    }

    public void setDisposeAgree(int disposeAgree) {
        this.disposeAgree = disposeAgree;
    }

    public Object getDisposeMember() {
        return disposeMember;
    }

    public void setDisposeMember(Object disposeMember) {
        this.disposeMember = disposeMember;
    }

    public String getDisposeAmount() {
        return disposeAmount;
    }

    public void setDisposeAmount(String disposeAmount) {
        this.disposeAmount = disposeAmount;
    }

    public Object getDisposeComment() {
        return disposeComment;
    }

    public void setDisposeComment(Object disposeComment) {
        this.disposeComment = disposeComment;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Object getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(Object statusStr) {
        this.statusStr = statusStr;
    }

    public Object getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Object handleStatus) {
        this.handleStatus = handleStatus;
    }

    public Object getHandleStatusStr() {
        return handleStatusStr;
    }

    public void setHandleStatusStr(Object handleStatusStr) {
        this.handleStatusStr = handleStatusStr;
    }

    public List<AttachListBean> getAttachList() {
        return attachList;
    }

    public void setAttachList(List<AttachListBean> attachList) {
        this.attachList = attachList;
    }

    @Override
    public int getItemType() {
        if (getDisposeAgree() == -1) {
            return 5;
        } else if (getStatus() == -1) {
            return 6;
        } else {
            return getStatus();
        }
    }

    public static class AttachListBean {
        /**
         * id : 24
         * refundId : 12
         * attachUrl : http://hmm-public.oss-cn-beijing.aliyuncs.com/hmm-user-dev/images/bfda0d81-7290-4d25-a369-75f23956fe40.9j
         * createTime : 2020-07-08 21:02:18
         * updateTime : 2020-07-08 21:02:18
         */

        private long id;
        private long refundId;
        private String attachUrl;
        private String createTime;
        private String updateTime;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getRefundId() {
            return refundId;
        }

        public void setRefundId(long refundId) {
            this.refundId = refundId;
        }

        public String getAttachUrl() {
            return attachUrl;
        }

        public void setAttachUrl(String attachUrl) {
            this.attachUrl = attachUrl;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

}
