package com.healthy.library.model;

import com.healthy.library.utils.ResUtil;

public class ToolsMenu {
    public String name;

    public int getImageRes() {
        return ResUtil.getInstance().getResourceId(imageResString);
    }

    private Integer imageRes;
    public String imageResString;

    public ToolsMenu(String name, String imageResString) {
        this.name = name;
        this.imageResString = imageResString;
    }

}
