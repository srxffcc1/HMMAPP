package com.healthy.library.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/04 17:26
 * @des 商品model
 */

public class GoodsModel2 {

    public String id;
    public String applicationNo;
    public String goodsTitle;
    public String categoryId;
    public String description;
    public Double storePrice;
    @SerializedName("assemblePrice")
    public Double platformPrice;
    public Double storePlatformPriceDiscount;


    @SerializedName("assembleInventory")
    public int maxInventory;
    public String expiredDate;
    public String salesMax;
    public int isReservation;
    public int isHomeService;
    public String reservationHours;
    public int sameOffer;
    public String additionNote;
    public String manual;
    public int totalSales;
    public String reviewStatus;
    public String reviewNote;
    @SerializedName("assembleStatus")
    public int goodsStatus;
    public String applicationType;
    public String normalId;
    public String createTime;
    public String updateTime;
    public String reviewUser;
    public String reviewTime;
    public int goodsShopsCount;
    public String tagOfCoupon;
    public GoodsCategory goodsCategory;
    public List<String> headImages;
    public List<GoodsFile> goodsFiles;
    public String goodsDescription;
    public List<GoodsPublish> goodsPublishes;
    public List<GoodsShop> goodsShops;
    public List<Attribute> attributes;


    public class GoodsShop {

        public int id;
        public int goodsId;
        public int shopId;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }
        public int getGoodsId() {
            return goodsId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }
        public int getShopId() {
            return shopId;
        }

    }

    public class GoodsPublish {

        public int id;
        public int goodsId;
        public int publish;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }
        public int getGoodsId() {
            return goodsId;
        }

        public void setPublish(int publish) {
            this.publish = publish;
        }
        public int getPublish() {
            return publish;
        }

    }
    public class Attribute{


            public long id;
            public int goodsId;
            public String attributeName;
            public int attributeType;
            public String attributeValue;
            public String placeHolder;
            public String goodsValue;
            

        
    }

    public class GoodsFile {

        public int id;
        public int fileType;
        public String filePath;
        public String imageTitle;
        public String imageDescription;

    }




        class FatherCategory {

        public int id;
        public String goodsCategoryNo;
        public String categoryName;
        public int categoryLevel;
        public int ranking;
        public int status;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setGoodsCategoryNo(String goodsCategoryNo) {
            this.goodsCategoryNo = goodsCategoryNo;
        }
        public String getGoodsCategoryNo() {
            return goodsCategoryNo;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryLevel(int categoryLevel) {
            this.categoryLevel = categoryLevel;
        }
        public int getCategoryLevel() {
            return categoryLevel;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }
        public int getRanking() {
            return ranking;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        public int getStatus() {
            return status;
        }

    }

     class GoodsCategory {

        public int id;
        public String goodsCategoryNo;
        public String categoryName;
        public int categoryLevel;
        public int fatherId;
        public int status;
        public FatherCategory fatherCategory;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setGoodsCategoryNo(String goodsCategoryNo) {
            this.goodsCategoryNo = goodsCategoryNo;
        }
        public String getGoodsCategoryNo() {
            return goodsCategoryNo;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryLevel(int categoryLevel) {
            this.categoryLevel = categoryLevel;
        }
        public int getCategoryLevel() {
            return categoryLevel;
        }

        public void setFatherId(int fatherId) {
            this.fatherId = fatherId;
        }
        public int getFatherId() {
            return fatherId;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        public int getStatus() {
            return status;
        }

        public void setFatherCategory(FatherCategory fatherCategory) {
            this.fatherCategory = fatherCategory;
        }
        public FatherCategory getFatherCategory() {
            return fatherCategory;
        }

    }


    public static class ServiceItem {
        String serviceId;
        String serviceName;

        public ServiceItem() {
        }

        public String getServiceId() {
            return serviceId == null ? "" : serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getServiceName() {
            return serviceName == null ? "" : serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }
    }
}
