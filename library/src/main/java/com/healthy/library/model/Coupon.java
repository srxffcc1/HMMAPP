package com.healthy.library.model;

import android.view.View;

import java.io.Serializable;
import java.util.List;

public class Coupon implements Serializable {
    public String Number;

    public String getGoodsID() {
        return GoodsID;
    }

    public String getCouponID() {
        return StuffNo + GoodsID;
    }

    public String GoodsID;
    public String GoodsName;
    /**
     * 0 固定券 1 可选劵
     */
    public String SendMode;
    public String Price = "0";
    public String StartDate;
    public String EndDate;
    public String StuffNo;
    /**
     * 0 服务 1 商品
     */
    public String GoodsType;
    /**
     * 0 未领取 1 已领取
     */
    public String isReceive;
    /**
     * 标记门店显示位置，控制后续蒙层锚点位置
     */
    public transient View mGuideView;//刚才闪退了 
    public String MaxSelCnt;//控制最大选择品项数

    public String getGbTypeName() {
        try {
            return GbTypeName.substring(GbTypeName.lastIndexOf("]") + 1, GbTypeName.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GbTypeName;
    }

    public String getShopName() {
        if (shopName != null && shopName.length() > 10) {
            return shopName.replace(shopName.substring(10, shopName.length()), "...");
        } else {
            return shopName;
        }
    }

    public String GbTypeName;
    public boolean ischeck = false;//默认选中还是不选中呢
    public List<PersonInfo> PersonInfo;
    /*** 选中的门店名称 */
    public String shopName = null;
    /*** 选中的门店Id */
    public String CheckShopId = null;
    /*** 当前优惠券选中的 DepartID*/
    public String DepartID = null;
    /*** 当前优惠券选中的 PersonID*/
    public String PersonID = null;

    public void setCheckShopName(String checkShopName) {
        shopName = checkShopName;
    }

    public void setCheckShopId(String checkShopId) {
        CheckShopId = checkShopId;
    }
}
