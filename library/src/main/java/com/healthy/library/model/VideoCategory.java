package com.healthy.library.model;

import java.io.Serializable;
import java.util.List;

public class VideoCategory implements Serializable {

    public String id;
    public String parentId;
    public String categoryName;
    public String categoryCode;
    public String scope;
    public int isUse;
    public int ranking;
    public String createTime;
    public String updateTime;
    public boolean hasChildren;
    public List<VideoCategory> children;
    public boolean isSelect = false;

}
