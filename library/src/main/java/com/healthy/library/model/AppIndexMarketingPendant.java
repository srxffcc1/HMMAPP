/**
  * Copyright 2021 bejson.com 
  */
package com.healthy.library.model;
import com.healthy.library.interfaces.IRouterLink;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.StringUtils;

import java.util.Date;

public class AppIndexMarketingPendant implements IRouterLink {

    public String id;
    public String planId;
    public String mainTitle;
    public String subTitle;
    public String imageUrl;
    public String androidUrl;
    public String iosUrl;
    public int sort;
    public String createTime;
    public String updateTime;

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
}