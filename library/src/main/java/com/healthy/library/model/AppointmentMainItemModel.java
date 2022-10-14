package com.healthy.library.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author: user
 * @date: 2021/3/31
 */
public class AppointmentMainItemModel implements Serializable {

    private int id;
    private String name;
    private int virtualSoldNumber;
    private int soldNumber;
    private String priceType;
    private double price;
    private int categoryId;
    //付款类型(1:到店付款，2:在线支付)
    private int payType;
    private int voice;
    private String detail;
    private int duration;
    //是否支持取消并退款（1：支持 2：不支持）
    private int supportRefund;
    private int advanceCancelNumber;
    private String advanceCancelUnit;
    private int merchantId;
    private String createTime;
    private String updateTime;
    private List<AttributeList> attributeList;
    private String status;
    private List<TechnicianList> technicianList;
    private List<String> imgList;
    private AttributeList attribute;

    public AttributeList getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeList attribute) {
        this.attribute = attribute;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVirtualSoldNumber(int virtualSoldNumber) {
        this.virtualSoldNumber = virtualSoldNumber;
    }

    public int getVirtualSoldNumber() {
        return virtualSoldNumber;
    }

    public void setSoldNumber(int soldNumber) {
        this.soldNumber = soldNumber;
    }

    public int getSoldNumber() {
        return soldNumber;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getPayType() {
        return payType;
    }

    public void setVoice(int voice) {
        this.voice = voice;
    }

    public int getVoice() {
        return voice;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setSupportRefund(int supportRefund) {
        this.supportRefund = supportRefund;
    }

    public int getSupportRefund() {
        return supportRefund;
    }

    public void setAdvanceCancelNumber(int advanceCancelNumber) {
        this.advanceCancelNumber = advanceCancelNumber;
    }

    public int getAdvanceCancelNumber() {
        return advanceCancelNumber;
    }

    public void setAdvanceCancelUnit(String advanceCancelUnit) {
        this.advanceCancelUnit = advanceCancelUnit;
    }

    public String getAdvanceCancelUnit() {
        return advanceCancelUnit;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setAttributeList(List<AttributeList> attributeList) {
        this.attributeList = attributeList;
    }

    public List<AttributeList> getAttributeList() {
        return attributeList;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setTechnicianList(List<TechnicianList> technicianList) {
        this.technicianList = technicianList;
    }

    public List<TechnicianList> getTechnicianList() {
        return technicianList;
    }

    public class AttributeList implements Serializable {
        private int id;
        private String name;
        private double price;
        private int projectId;
        private String createTime;
        private String updateTime;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getPrice() {
            return price;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

    }

    public class TechnicianList implements Serializable {
        private int id;
        private String avatar;
        private String nickname;
        private String phone;
        private String shopId;
        private String merchantId;
        private String createTime;
        private String updateTime;

        public TechnicianList() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
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
