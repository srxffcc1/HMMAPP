package com.health.mine.model;

import androidx.annotation.NonNull;

import java.util.Locale;

/**
 * @author Li
 * @date 2019/05/27 18:07
 * @des
 */
public class ServiceDetailModel {
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务id
     */
    private String serviceId;

    /**
     * 门店id（由哪个门店提供）
     */
    private int shopId;

    /**
     * 门店品牌
     */
    private int shopBrand;

    /**
     * 服务剩余次数
     */
    private int leftCount;

    /**
     * 服务总次数
     */
    private int totalCount;

    /**
     * 服务何时开始的文案
     */
    private String daysNotice;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 距离服务开始还剩多少天
     */
    private int daysNumber;

    /**
     * 计次还是计时
     * 1.计次
     * 2.计时
     */
    private String courseStyle;

    /**
     * 服务结束时间
     */
    private String serviceEndDate;

    public String getServiceEndDate() {
        return serviceEndDate == null ? "" : serviceEndDate;
    }

    public void setServiceEndDate(String serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public String getCourseStyle() {
        return courseStyle == null ? "" : courseStyle;
    }

    public void setCourseStyle(String courseStyle) {
        this.courseStyle = courseStyle;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getDaysNumber() {
        return daysNumber;
    }

    public void setDaysNumber(int daysNumber) {
        this.daysNumber = daysNumber;
    }

    public String getShopName() {
        return shopName == null ? "" : shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDaysNotice() {
        return daysNotice == null ? "" : daysNotice;
    }

    public void setDaysNotice(String daysNotice) {
        this.daysNotice = daysNotice;
    }

    public int getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(int leftCount) {
        this.leftCount = leftCount;
    }

    public String getServiceName() {
        return serviceName == null ? "" : serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId == null ? "" : serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getShopBrand() {
        return shopBrand;
    }

    public void setShopBrand(int shopBrand) {
        this.shopBrand = shopBrand;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.CHINA, "%d-%d", shopId, shopBrand);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServiceDetailModel) {
            ServiceDetailModel model = (ServiceDetailModel) obj;
            return this.toString().equals(model.toString());
        }
        return super.equals(obj);
    }
}
