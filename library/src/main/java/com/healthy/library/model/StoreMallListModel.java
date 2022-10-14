package com.healthy.library.model;

import com.healthy.library.model.CouponInfoByMerchant;
import com.healthy.library.model.Course;
import com.healthy.library.model.GoodsModel;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/04 17:26
 * @des 门店model
 */

public class StoreMallListModel {
    public boolean isshow=false;
    public int id;
    public double longitude;
    public double latitude;
    public String shopName;
    public String areaDetails;
    public String url;
    public String categoryName;
    public String categoryNameOnlyOne;
    public String businessTime;
    public String appointmentPhone;
    public String addressCity;
    public String addressArea;
    public String distance;
    public double commentMarkAvg;
    public String areaNo;
    public String cityNo;
    public String categoryNo;
    public List<GoodsModel> goodsDTOS;
    public List<Course> courseList;
    public List<CouponInfoByMerchant> appMerchantCoupons;
}
