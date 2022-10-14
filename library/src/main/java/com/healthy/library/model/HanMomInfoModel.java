package com.healthy.library.model;

public class HanMomInfoModel {
    public static class MemberInfoModel {
        public String partnerId;
    }

    public static class SettingModel {
        public String id;
        public String partnerId;
        public Double feePrice;
        public String feeValidityDays;
    }

    public static class PartnerModel {
        public String id;
        public String fullName;
        public String shortName;
        public String type;
        public String provinceName;
        public String cityName;
        public String districtName;
        public String address;
    }

    public static class LastPartnerModel {
        public String id;
        public String fullName;
        public String shortName;
        public String type;
        public String provinceName;
        public String cityName;
        public String districtName;
        public String address;
    }

    public class StarListModel {
        public String memberName;
        public String memberAlias;
        public String memberAvatarUrl;
        public Double totalAmount;
        public String memberEntryTime;
        public String createTime;
    }

    public class ScrollListModel {
        public String memberAvatarUrl;
        public String memberName;
        public String content;
    }
}
