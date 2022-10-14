package com.healthy.library.model;

import java.util.List;

/**
 * @author: long
 * @date: 2021/4/1
 */
public class AppointmentListItemModel {
    private long id;//预约单id
    private String appointmentNo;//预约单编号
    private long projectId;
    private String projectName;
    private String projectAttributeId;
    private String projectAttributeName;
    private String projectAttributeType;
    private double projectPrice;
    private String gallery;
    private int supportRefund;
    /**
     * 提前取消预约并退款单位（1：小时 2：天）
     */
    private int advanceCancelUnit;
    /**
     * 提前取消预约并退款数（小时：1～24， 天：1~365）
     */
    private int advanceCancelNumber;
    private int status;//	1待接单		2带到店		3已完成		4已取消
    private String cancelReason;
    private int payType;
    private int payWay;
    private String payOrderNo;
    private String tradeId;
    private String transactionId;
    private double appointmentAmount;
    private int appointmentSource;
    private String appointmentDate;
    private String appointmentRangeStartTime;
    private String appointmentRangeEndTime;
    private String appointmentDuration;
    private String memberId;
    private String memberName;
    private String memberNickname;
    private String memberPhone;
    private String memberAvatar;
    private String memberRemark;
    private String lastAppointmentTime;
    private String categoryName;
    private long technicianId;
    private String technicianName;
    private String technicianNickName;
    private String technicianPhone;
    private long shopId;
    private String shopName;
    private String merchantId;
    private String merchantRemark;
    private String createTime;
    private String updateTime;
    private AppointmentTrade appointmentTrade;
    private AppointmentTrade appointmentRefund;
    private List<String> imgList;
    private long autoCancelSurplusSeconds;

    public String getProjectAttributeType() {
        return projectAttributeType;
    }

    public void setProjectAttributeType(String projectAttributeType) {
        this.projectAttributeType = projectAttributeType;
    }

    public long getAutoCancelSurplusSeconds() {
        return autoCancelSurplusSeconds;
    }

    public void setAutoCancelSurplusSeconds(long autoCancelSurplusSeconds) {
        this.autoCancelSurplusSeconds = autoCancelSurplusSeconds;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getProjectAttributeId() {
        return projectAttributeId;
    }

    public void setProjectAttributeId(String projectAttributeId) {
        this.projectAttributeId = projectAttributeId;
    }

    public String getProjectAttributeName() {
        return projectAttributeName;
    }

    public void setProjectAttributeName(String projectAttributeName) {
        this.projectAttributeName = projectAttributeName;
    }

    public double getProjectPrice() {
        return projectPrice;
    }

    public void setProjectPrice(double projectPrice) {
        this.projectPrice = projectPrice;
    }

    public double getAppointmentAmount() {
        return appointmentAmount;
    }

    public void setAppointmentAmount(double appointmentAmount) {
        this.appointmentAmount = appointmentAmount;
    }

    public AppointmentTrade getAppointmentTrade() {
        return appointmentTrade;
    }

    public void setAppointmentTrade(AppointmentTrade appointmentTrade) {
        this.appointmentTrade = appointmentTrade;
    }

    public AppointmentTrade getAppointmentRefund() {
        return appointmentRefund;
    }

    public void setAppointmentRefund(AppointmentTrade appointmentRefund) {
        this.appointmentRefund = appointmentRefund;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGallery() {
        return gallery;
    }

    public void setGallery(String gallery) {
        this.gallery = gallery;
    }

    public int getSupportRefund() {
        return supportRefund;
    }

    public void setSupportRefund(int supportRefund) {
        this.supportRefund = supportRefund;
    }

    public int getAdvanceCancelUnit() {
        return advanceCancelUnit;
    }

    public void setAdvanceCancelUnit(int advanceCancelUnit) {
        this.advanceCancelUnit = advanceCancelUnit;
    }

    public int getAdvanceCancelNumber() {
        return advanceCancelNumber;
    }

    public void setAdvanceCancelNumber(int advanceCancelNumber) {
        this.advanceCancelNumber = advanceCancelNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getAppointmentSource() {
        return appointmentSource;
    }

    public void setAppointmentSource(int appointmentSource) {
        this.appointmentSource = appointmentSource;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentRangeStartTime() {
        return appointmentRangeStartTime;
    }

    public void setAppointmentRangeStartTime(String appointmentRangeStartTime) {
        this.appointmentRangeStartTime = appointmentRangeStartTime;
    }

    public String getAppointmentRangeEndTime() {
        return appointmentRangeEndTime;
    }

    public void setAppointmentRangeEndTime(String appointmentRangeEndTime) {
        this.appointmentRangeEndTime = appointmentRangeEndTime;
    }

    public String getAppointmentDuration() {
        return appointmentDuration;
    }

    public void setAppointmentDuration(String appointmentDuration) {
        this.appointmentDuration = appointmentDuration;
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

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }

    public String getMemberRemark() {
        return memberRemark;
    }

    public void setMemberRemark(String memberRemark) {
        this.memberRemark = memberRemark;
    }

    public String getLastAppointmentTime() {
        return lastAppointmentTime;
    }

    public void setLastAppointmentTime(String lastAppointmentTime) {
        this.lastAppointmentTime = lastAppointmentTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(long technicianId) {
        this.technicianId = technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getTechnicianNickName() {
        return technicianNickName;
    }

    public void setTechnicianNickName(String technicianNickName) {
        this.technicianNickName = technicianNickName;
    }

    public String getTechnicianPhone() {
        return technicianPhone;
    }

    public void setTechnicianPhone(String technicianPhone) {
        this.technicianPhone = technicianPhone;
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantRemark() {
        return merchantRemark;
    }

    public void setMerchantRemark(String merchantRemark) {
        this.merchantRemark = merchantRemark;
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

    public class AppointmentTrade {
        private String tradeId;
        private String transactionId;
        private int payWay;
        private double payAmount;
        private boolean payed;
        private String refundId;
        private double refundAmount;
        private String createTime;
        private String paySuccessTime;

        public String getRefundId() {
            return refundId;
        }

        public void setRefundId(String refundId) {
            this.refundId = refundId;
        }

        public double getRefundAmount() {
            return refundAmount;
        }

        public void setRefundAmount(double refundAmount) {
            this.refundAmount = refundAmount;
        }

        public String getPaySuccessTime() {
            return paySuccessTime;
        }

        public void setPaySuccessTime(String paySuccessTime) {
            this.paySuccessTime = paySuccessTime;
        }

        public String getTradeId() {
            return tradeId;
        }

        public void setTradeId(String tradeId) {
            this.tradeId = tradeId;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public int getPayWay() {
            return payWay;
        }

        public void setPayWay(int payWay) {
            this.payWay = payWay;
        }

        public double getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(double payAmount) {
            this.payAmount = payAmount;
        }

        public boolean isPayed() {
            return payed;
        }

        public void setPayed(boolean payed) {
            this.payed = payed;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }

}
