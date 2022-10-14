package com.healthy.library.model;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/04 17:26
 * @des 商品model
 */

public class ShopModel {
        public String id;
        public String userId;
        public String merchantCode;
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
        public String businessStatus;
        public String appointmentPhone;
        public String enterExpense;
        public String remark;
        public String longitude;
        public String latitude;
        public String createTime;
        public String updateTime;
        public List<UserShopImgs> userShopImgs;
        public String categoryNoLevel1;
        public String categoryNoLevel2;

    public class UserShopImgs {

        public int id;
        public String url;
        public int type;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        public String getUrl() {
            return url;
        }

        public void setType(int type) {
            this.type = type;
        }
        public int getType() {
            return type;
        }

    }

}
