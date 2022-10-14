package com.healthy.library.model;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/28 15:58
 * @des 商城首页数据（首页是一个RecyclerView，根据viewType来区分不同界面，所以对
 * model进行了聚合，同一个字段在不同的viewType中可能代表不同的意思）
 */

public class MallIndexModel {


    private int type;

    /**
     * id
     */
    private String id;


    private String selfId;

    private String shopId;

    /**
     * 标题（商品名称、门店名称、种类）
     */
    private String title;
    /**
     * 轮播图url
     */
    private List<String> bannerUrls;
    /**
     * 图片地址（门店头图、商品头图）
     */
    private String imgUrl;
    /**
     * 售价
     */
    private String price;
    /**
     * 原价
     */
    private String originPrice;

    /**
     * 评价分
     */
    private double score;

    /**
     * 地址
     */
    private String address;

    /**
     * 距离
     */
    private String distance;
    /**
     * 是否上门
     */
    private boolean isHome;

    /**
     * 维度
     */
    private String lat;
    /**
     * 经度
     */
    private String lng;

    /**
     * 功效介绍
     */
    private String introduction;

    /**
     * 品牌：
     */
    private String brand;

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     * 折扣
     */
    private Double discount;

    /**
     * 商品已售数量
     */
    private int salesNum;


    public int getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getBannerUrls() {
        if (bannerUrls == null) {
            return new ArrayList<>();
        }
        return bannerUrls;
    }

    public void setBannerUrls(List<String> bannerUrls) {
        this.bannerUrls = bannerUrls;
    }

    public String getImgUrl() {
        return imgUrl == null ? "" : imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getOriginPrice() {
        return originPrice == null ? "" : originPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isHome() {
        return isHome;
    }

    public void setHome(boolean home) {
        isHome = home;
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

    public String getIntroduction() {
        return introduction == null ? "" : introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }



    public String getSelfId() {
        return selfId == null ? "" : selfId;
    }

    public void setSelfId(String selfId) {
        this.selfId = selfId;
    }

    public String getShopId() {
        return shopId == null ? "" : shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}