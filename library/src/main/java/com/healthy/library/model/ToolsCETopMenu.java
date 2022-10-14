package com.healthy.library.model;

import com.healthy.library.utils.ResUtil;

public class ToolsCETopMenu {
    public String bgRes;
    public String iconRes;
    public String name;
    public String eatStage;

    public ToolsCETopMenu(String bgRes, String iconRes, String name, String eatStage) {
        this.bgRes = bgRes;
        this.iconRes = iconRes;
        this.name = name;
        this.eatStage = eatStage;
    }

    public int getIconRes() {
        return ResUtil.getInstance().getResourceId(iconRes);
    }

    public int getBgRes() {
        return ResUtil.getInstance().getResourceId(bgRes);
    }
}
