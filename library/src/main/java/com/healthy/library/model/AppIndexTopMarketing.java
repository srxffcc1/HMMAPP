/**
  * Copyright 2021 bejson.com 
  */
package com.healthy.library.model;
import com.healthy.library.interfaces.IRouterLink;
import com.healthy.library.utils.StringUtils;

import java.util.Date;

/**
 * Auto-generated: 2021-12-29 15:35:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class AppIndexTopMarketing implements IRouterLink {

    public String id;
    public String planId;
    public String imageUrl;
    public String androidUrl;
    public String iosUrl;
    public String createTime;
    public String updateTime;
    public String subscript;
    public String settingName;
    public String initialName;
    public String darkIconUrl;

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