package com.healthy.library.model;

import com.healthy.library.utils.ResUtil;

public class IndexMenu {
    public String name;
    public String settingname;
    public int imageRes;
    public String imageResString;
    public String subscript;
    public AppIndexCustomItem appIndexCustomItem;

    public IndexMenu(String name, String imageResString) {
        this.name = name;//最初的名字
        this.settingname=name;
        this.imageResString = imageResString;
    }

    public IndexMenu(String name, int imageRes) {
        this.name = name;
        this.imageRes = imageRes;
        this.settingname=name;
    }

    public IndexMenu(AppIndexCustomItem appIndexCustomItem) {
        this.appIndexCustomItem=appIndexCustomItem;
        this.name = appIndexCustomItem.initialName;
        this.settingname=appIndexCustomItem.settingName;
        this.imageResString = appIndexCustomItem.darkIconUrl;
        this.subscript=appIndexCustomItem.subscript;
    }
    public int getImageRes() {
        return ResUtil.getInstance().getResourceId(imageResString);
    }
}
