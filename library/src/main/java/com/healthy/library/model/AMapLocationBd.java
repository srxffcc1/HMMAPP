package com.healthy.library.model;

import com.amap.api.location.AMapLocation;

import java.io.Serializable;

/**
 * 定位包装类
 */
public class AMapLocationBd implements Serializable {




    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getDistrict() {
        return district;
    }


    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public String address="";
    public String city="0";
    public String province="0";
    public String adCode="0";
    public double latitude=0;
    public double longitude=0;
    public String district="";
    public String getAddress() {
        return address;
    }


    public AMapLocationBd(AMapLocation aMapLocation) {
        this.address=aMapLocation.getDistrict()+aMapLocation.getStreet()+aMapLocation.getStreetNum();
        this.city=aMapLocation.getCity();
        this.province=aMapLocation.getProvince();
        this.adCode=aMapLocation.getAdCode();
        this.district=aMapLocation.getDistrict();
        this.latitude=aMapLocation.getLatitude();
        this.longitude=aMapLocation.getLongitude();

    }

    public AMapLocationBd(String address, String district, String city, String province, String adCode, double latitude, double longitude) {
        this.address = address;
        this.district=district;
        this.city = city;
        this.province = province;
        this.adCode = adCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AMapLocationBd() {
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public AMapLocation build() {
        AMapLocation aMapLocation=new AMapLocation("location");
        aMapLocation.setAdCode(this.adCode);
        aMapLocation.setDistrict(this.district);
        aMapLocation.setCity(this.city);
        aMapLocation.setProvince(this.province);
        aMapLocation.setLatitude(this.latitude);
        aMapLocation.setLongitude(this.longitude);
        aMapLocation.setErrorCode(0);
        return aMapLocation;
    }
}
