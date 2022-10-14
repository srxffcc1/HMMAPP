package com.healthy.library.model;

import java.util.List;

public class LiveRoomDecoration {
    public String id;
    public int roomDecorationType;
    public String anchormanId;
    public int position;
    public String templateName;
    public int isUsing;
    public String merchantId;
    public String shopId;
    public String createUser;
    public String createTime;
    public String updateUser;
    public String updateTime;
    public List<IconDtoListBean> iconDtoList;

    public class IconDtoListBean {
        public String id;
        public String templateId;
        public int roomDecorationType;
        public int iconType;
        public String iconPath;
        public int ranking;
        public int status;
        public int isSysDefined;
        public String createUser;
        public String createTime;
        public String updateUser;
        public String updateTime;
        public String anchormanId;
    }
}
