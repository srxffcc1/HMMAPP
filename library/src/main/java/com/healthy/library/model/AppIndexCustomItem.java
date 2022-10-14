package com.healthy.library.model;

import com.healthy.library.interfaces.IRouterLink;
import com.healthy.library.utils.StringUtils;

//首页金刚区、小工具、推荐位、底部导航 配置信息
public class AppIndexCustomItem implements IRouterLink {
        public String id;
        public String planId;
        public int settingType;
        public int sortNum;
        public String initialName;
        public String settingName;
        public String createTime;
        public String updateTime;
        public String darkIconUrl;
        public String lightIconUrl;
        public String androidUrl;
        public String iosUrl;
        public String h5Url;
        public String memberStatus;
        public String sourceJumpId;
        public String subscript;
        public int status;

    public String getAndroidLinkName() {
        return StringUtils.noEndLnString(androidUrl);
    }

    public String getLink(){
        return getAndroidLinkName();
    }

    @Override
    public String getId() {
        return null;
    }

    public AppIndexCustomItem(String initialName, String settingName, String h5Url) {
        this.initialName = initialName;
        this.settingName = settingName;
        this.h5Url = h5Url;
    }

    public AppIndexCustomItem() {
    }
}
