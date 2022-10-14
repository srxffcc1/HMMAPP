package com.healthy.library.model;

import com.healthy.library.model.Course;
import com.healthy.library.model.GoodsModel;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/04 17:26
 * @des 门店model
 */

public class StoreDetailModel {

        public int otherShopCount;
        public double distance;
        public String remainderCount;
        public String userShopIdsResults;
        public List<UserShopInfoResults> userShopInfoResults;
        public List<UserShopInfoDistanceResults> userShopInfoDistanceResults;
        public List<UserShopIntroduceResults> userShopIntroduceResults;
        public String userShopDTOS;
        public String goodsDTOS;
        public List<TechnicianResults> technicianResults;
        public List<GoodsModel> topSaleGoods;
        public double commentMarkAvg;
        public String[] strOfBusinessTimes;
        public List<Course> courseList;

    public class UserShopInfoDistanceResults {

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

    }

    public class UserShopIntroduceResults {

        public int shopId;
        public String brandIntroduce;
        public String shopIntroduce;
        public String businessTime;
        public String shopName;
        public String headUrl;
        public String envUrl;


    }
    public class UserShopInfoResults {

        public int id;
        public double longitude;
        public double latitude;
        public String shopName;
        public String areaDetails;
        public String url;
        public String categoryName;
        public String businessTime;
        public String appointmentPhone;
        public String addressCity;
        public String addressArea;
        public String areaNo;
        public String cityNo;
        public String categoryNo;

    }
    public class TechnicianResults {

        public int userId;
        public String realName;
        public String faceUrl;
        public String tagTechnician="";
        public String positionTechnician="";
        public String profession;
    }
}
