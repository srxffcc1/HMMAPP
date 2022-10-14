package com.health.discount.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class LiveListModel implements MultiItemEntity {
    
        public String id;
        public String anchormanId;
        public String userId;
        public String merchantName;
        public String shopId;
        public int courseId;
        public String courseName;
        public String beginTime;
        public String endTime;
        public String picUrl;
        public int flag;
        public int playback;
        public String isDelete;
        public String createTime;
        public String updateTime;
        public int isAppointment;
        public List<String> goodsPicUrlList;

        @Override
        public int getItemType() {
                return 4;
        }
}
