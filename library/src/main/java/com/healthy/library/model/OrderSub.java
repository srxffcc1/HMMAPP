package com.healthy.library.model;

import java.util.List;

public class OrderSub {


        public long id;
        public int shopId;
        public String shopName;
        public String chainShopName;
        public String amStartTime;
        public String amEndTime;
        public String pmStartTime;
        public String pmEndTime;
        public int oneHousMaxNum;
        public int advanceDays;
        public int homeAppointmentStatus;
        public int shopAppointmentStatus;
        public int pcAppointmentMsgStatus;
        public int phoneAppointmentMsgStatus;
        public List<ServiceTypeListBean> serviceTypeList;

        public  class ServiceTypeListBean {
            /**
             * id : 1267753657624317956
             * serviceName : 游泳
             * serviceTag : 1-3个月,3-10个月,10个月以上
             */
            public long id;
            public String serviceName;
            public String serviceTag;

        }

}
