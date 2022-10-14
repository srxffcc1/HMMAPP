package com.healthy.library.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.healthy.library.interfaces.IRouterLink;
import com.healthy.library.utils.StringUtils;

import java.util.List;

public class AppIndexList implements MultiItemEntity, IRouterLink {

    public String id;
    public String planId;
    public String mainTitle;
    public String subTitle;
    public String imageUrl;
    public String listImageUrl;
    public String categoryId;
    public String categoryName;
    public String androidUrl;
    public String iosUrl;
    public int sort;
    public String createTime;
    public String updateTime;
    public List<AppIndexGoodsList> goodsList;

    @Override
    public int getItemType() {
        return 0;
    }

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
