package com.healthy.library.model;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/10 09:31
 * @des 订单详情
 */

public class StoreMallSimpleModel {


        public int otherShopCount;
        public List<UserShopInfoDistanceResult> userShopInfoDistanceResults;
        public void setOtherShopCount(int otherShopCount) {
            this.otherShopCount = otherShopCount;
        }
        public int getOtherShopCount() {
            return otherShopCount;
        }

        public void setUserShopInfoDistanceResults(List<UserShopInfoDistanceResult> userShopInfoDistanceResults) {
            this.userShopInfoDistanceResults = userShopInfoDistanceResults;
        }
        public List<UserShopInfoDistanceResult> getUserShopInfoDistanceResults() {
            return userShopInfoDistanceResults;
        }



    public class UserShopInfoDistanceResult {

        public int id;
        public double longitude;
        public double latitude;
        public String shopName;
        public String areaDetails;
        public String url;
        public String categoryNo;
        public String categoryName;
        public double distance;
        public String appointmentPhone;
        //public String merchantMobile;
        public double commentMarkAvg;
        public String categoryNameOnlyOne;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
        public double getLongitude() {
            return longitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
        public double getLatitude() {
            return latitude;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }
        public String getShopName() {
            return shopName;
        }

        public void setAreaDetails(String areaDetails) {
            this.areaDetails = areaDetails;
        }
        public String getAreaDetails() {
            return areaDetails;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        public String getUrl() {
            return url;
        }

        public void setCategoryNo(String categoryNo) {
            this.categoryNo = categoryNo;
        }
        public String getCategoryNo() {
            return categoryNo;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
        public String getCategoryName() {
            return categoryName;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
        public double getDistance() {
            return distance;
        }

//        public void setAppointmentPhone(String appointmentPhone) {
//            this.appointmentPhone = appointmentPhone;
//        }
//        public String getAppointmentPhone() {
//            return appointmentPhone;
//        }



        public void setCategoryNameOnlyOne(String categoryNameOnlyOne) {
            this.categoryNameOnlyOne = categoryNameOnlyOne;
        }
        public String getCategoryNameOnlyOne() {
            return categoryNameOnlyOne;
        }

    }

}
