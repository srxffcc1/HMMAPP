package com.healthy.library.model;

import android.text.TextUtils;

import java.util.List;

public class StoreListModel {

    /**
     * shopId : 20
     * shopName : 晓明大酒店
     * filePath : http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/6745ec7b-dffd-407d-8c9a-d5a7f410b181.jpg
     * shopAddress : 邓尉路绿宝广场二期7栋112室(大众书局向南15米)
     * longitude : 120.550351
     * latitude : 31.301575
     * score : 5.0
     * distance : 1792
     * isSelected : 0
     * appointmentPhone : 17775212052
     * businessTime : ["周一至周五 08:00-17:30","周六至周日 08:00-17:30"]
     */

    private int shopId;
    private String shopName;
    private String filePath;
    private String shopAddress;
    private double longitude;
    private double latitude;
    private float score;
    private int distance;
    private int isSelected;
    private String appointmentPhone;
    private String contactPhone;
    private List<String> businessTime;

    public String courseId;
    public int courseFlag;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getAppointmentPhone() {
        if(TextUtils.isEmpty(appointmentPhone)){
            return contactPhone;
        }
        return appointmentPhone;//转为服务电话
    }

    public void setAppointmentPhone(String appointmentPhone) {
        this.appointmentPhone = appointmentPhone;
    }

    public List<String> getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(List<String> businessTime) {
        this.businessTime = businessTime;
    }

    public StoreListModel() {
    }
}
