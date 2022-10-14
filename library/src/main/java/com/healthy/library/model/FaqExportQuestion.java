package com.healthy.library.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 热门问题
 */
public class FaqExportQuestion implements Serializable {

    public int questionId;
    public int expertId;
    public String realName;
    public String faceUrl;
    public String addressCity;
    public String rankName;
    public String introduction;
    public String replyDetail;
    public String expertCategoryNo;

    public String getExpertCategoryName() {
        if(TextUtils.isEmpty(expertCategoryName)){
            return "";
        }
        return expertCategoryName;
    }

    public String expertCategoryName;
    public String keyWords;
    public int readCount;
    public String ranks;
    public String detail;

}
