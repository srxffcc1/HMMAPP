package com.healthy.library.model;

import java.io.Serializable;

public class AddressListModel implements Serializable {

    @Override
    public String toString() {
        return "AddressListModel{" +
                "id=" + id +
                ", consigneeName='" + consigneeName + '\'' +
                ", configneePhone='" + configneePhone + '\'' +
                ", province='" + province + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", city='" + city + '\'' +
                ", cityName='" + cityName + '\'' +
                ", district='" + district + '\'' +
                ", districtName='" + districtName + '\'' +
                ", address='" + address + '\'' +
                ", houseNum='" + houseNum + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", orderBy=" + orderBy +
                '}';
    }

    /**
     * id : 16
     * consigneeName : 邵先生1
     * configneePhone : 18036095223
     * province : 320000
     * provinceName : 江苏省
     * city : 320500
     * cityName : 苏州市
     * district : 320505
     * districtName : 虎丘区
     * address : 乐嘉汇商务广场1号楼
     * lat : 31.296883
     * lng : 120.570409
     * orderBy : -1
     */


    private int id;
    private String consigneeName;
    private String configneePhone;
    private String province;
    private String provinceName;
    private String city;
    private String cityName;
    private String district;
    private String districtName;
    private String address;


    private String houseNum;
    private String lat;
    private String lng;
    private int orderBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConfigneePhone() {
        return configneePhone;
    }

    public String getConfigneePhoneHide() {

        String hidenPhone = configneePhone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        return hidenPhone;
    }

    public void setConfigneePhone(String configneePhone) {
        this.configneePhone = configneePhone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public String getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(String houseNum) {
        this.houseNum = houseNum;
    }
}
