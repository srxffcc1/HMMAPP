package com.healthy.library.model;

import com.healthy.library.R;
import com.healthy.library.utils.ResUtil;

import java.io.Serializable;

public class ToolsMedType implements Serializable {
    public String name;
    private int normalIcon=-1;
    private int selectIcon=-1;

    public String normalIconString;
    public String selectIconString;

    public int normalBg= R.drawable.shape_tools_med_normal;
    public int selectBg=R.drawable.shape_tools_med_select;
    public int normalTextColor=0xFF444444;
    public int selectTextColor=0xFFFFFFFF;
    public ToolsMedType(String name, String normalIconString, String selectIconString) {
        this.name = name;
        this.normalIconString = normalIconString;
        this.selectIconString = selectIconString;
    }

    public int getNormalIcon() {
        return ResUtil.getInstance().getResourceId(normalIconString);
    }

    public int getSelectIcon() {
        return ResUtil.getInstance().getResourceId(selectIconString);
    }

    public ToolsMedType(String name) {
        this.name = name;
    }


    public ToolsMedType(String name,  String normalIconString, String selectIconString, int normalBg, int selectBg, int normalTextColor, int selectTextColor) {
        this.name = name;
        this.normalIconString = normalIconString;
        this.selectIconString = selectIconString;
        this.normalBg = normalBg;
        this.selectBg = selectBg;
        this.normalTextColor = normalTextColor;
        this.selectTextColor = selectTextColor;
    }
}
