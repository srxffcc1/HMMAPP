package com.healthy.library.model;

import android.text.TextUtils;

public class ShareGiftDTO {
    public long id;
    public long shareGoodsId;//商品id
    public long shareMarketingGoodsId;//商品活动id
    public int shareMarketingType;//商品类型
    public long shareResultId;//商品分享结果id
    public double shareMoney;//商品分享收益
    public String shareName;
    public int shareResultType;//商品分享结果类型
    public long shareResultLimit;//分享限制规则
    public String createTime;//
    public String updateTime;//

    public String getShareName() {
        if (TextUtils.isEmpty(shareName)) {
            return "分享赚优惠券";
        } else {
            return shareName;
        }
    }
}
