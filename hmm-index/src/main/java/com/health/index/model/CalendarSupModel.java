package com.health.index.model;

import android.text.TextUtils;

/**
 * @author Li
 * @date 2019/05/08 16:48
 * @des 经期助手下方辅助对象
 */

public class CalendarSupModel {
    /**
     * 怀孕几率
     */
    private String pregnancyProb;
    /**
     * 内容
     */
    private String content;

    public String getPregnancyProb() {
        return pregnancyProb == null ? "" : pregnancyProb;
    }

    public void setPregnancyProb(String pregnancyProb) {
        this.pregnancyProb = pregnancyProb;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(pregnancyProb) && TextUtils.isEmpty(content);
    }
}