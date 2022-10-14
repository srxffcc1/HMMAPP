package com.healthy.library.model;

import com.healthy.library.model.GoodsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/04 17:26
 * @des 门店model
 */

public class OldStoreModel {

    /**
     * id
     */
    private String id;

    /**
     * 门店名称
     */
    private String title;

    /**
     * 门店头图
     */
    private String imgUrl;

    /**
     * 评价分
     */
    private String score;

    /**
     * 地址
     */
    private String address;

    /**
     * 距离
     */
    private String distance;

    /**
     * 维度
     */
    private String lat;
    /**
     * 经度
     */
    private String lng;

    /**
     * 品牌：
     */
    private String brand;

    /**
     * 营业时间
     */

    private String businessHours;

    /**
     * 门店编号
     */
    private String shopNo;

    /**
     * 门店包含服务
     */
    private List<GoodsModel> goodsList;
    /**
     * 门店包含服务数量
     */
    private int serviceTotal;

    /**
     * 门店图片列表
     */
    private List<String> urls;

    public List<String> getUrls() {
        if (urls == null) {
            return new ArrayList<>();
        }
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public int getServiceTotal() {
        return serviceTotal;
    }

    public void setServiceTotal(int serviceTotal) {
        this.serviceTotal = serviceTotal;
    }

    public List<GoodsModel> getGoodsList() {
        if (goodsList == null) {
            return new ArrayList<>();
        }
        return goodsList;
    }

    public void setGoodsList(List<GoodsModel> goodsList) {
        this.goodsList = goodsList;
    }

    public String getShopNo() {
        return shopNo == null ? "" : shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getBusinessHours() {
        return businessHours == null ? "" : businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl == null ? "" : imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getScore() {
        return score == null ? "" : score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance == null ? "" : distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLat() {
        return lat == null ? "" : lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng == null ? "" : lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getBrand() {
        return brand == null ? "" : brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
