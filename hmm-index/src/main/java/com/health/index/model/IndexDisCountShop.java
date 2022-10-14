package com.health.index.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndexDisCountShop implements Serializable {

        public int id;
        public int userId;
        public long merchantCode;
        public String brandName;
        public String shopName;
        public String chainShopName;
        public String addressProvince;
        public String addressCity;
        public String addressArea;
        public String addressAreaName;
        public String addressDetails;
        public String businessHourStart;
        public String businessHourEnd;
        public int businessStatus;
        public String appointmentPhone;
        public double enterExpense;
        public double longitude;
        public double latitude;
        public String createTime;
        public String updateTime;
        public List<UserShopImg> userShopImgs;
        public String categoryNoLevel1;
        public String categoryNoLevel2;
        public IndexGoodsModel maxDiscountGoods;
        public double distance;
        public double maxGoodsDiscount;
        public List<String> topicList=new ArrayList<>();

    public class UserShopImg {

        public int id;
        public String url;
        public int type;
    }


}
