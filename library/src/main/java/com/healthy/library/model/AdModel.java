package com.healthy.library.model;

import android.text.TextUtils;

import com.healthy.library.interfaces.IRouterLink;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.StringUtils;

public class AdModel implements IRouterLink {

    @Override
    public String getId() {
        return id;
    }

    public String id;

    public int type;

    public String title;

    public int triggerPage;

    public AdModel(String id, int type, int triggerPage) {
        this.id = id;
        this.type = type;
        this.triggerPage = triggerPage;
    }

    public AdModel() {
    }

    public String photoUrls;

    public String colorValue;

    public String getColorValue() {
        if(colorValue!=null&&!colorValue.startsWith("#")){
         return  "#"+colorValue;
        }
        return colorValue;
    }

    String targetId;


    public int linkType;

    public String linkUrl;

    public AdModel(String photoUrls) {
        this.photoUrls = photoUrls;
    }

    public AdModel(String photoUrls,String androidLinkName) {
        this.photoUrls = photoUrls;
        this.androidLinkName=androidLinkName;
    }

    public String validityStart;

    public String validityEnd;

    public String appVersion;

    public int sendType;

    public String sendTime;

    public String sendTimeStr;

    public int frequency;

    public String memberState;

    public int status;

    public String backRemark;

    public String createTime;

    public String updateTime;

    public String operator;

    public int bannerType;

    public int ranking;

    public String advertisingUid;

    public String advertisingArea;

    public int viewCount;

    public String logAdvertising;

    public int isAllVersion;

    public int isWholeCountry;

    public String aliasName;

    public String iosLinkName;

    public String getAndroidLinkName() {
        return StringUtils.noEndLnString(androidLinkName);
    }

    public String androidLinkName;

    public String validatyStr;

    public String logOfAdvertising;

    public String cardGiftType;
    public String codeName;
    public String guideWords;
    public String isReceive;//0 未领取 1 已领取(只用于 23 节日礼包使用此字段）

    public String iconUrl;

    public String getLink(){
        if(!TextUtils.isEmpty(cardGiftType)){//不为空 直接拼接一个出来
            return String.format("/discount/newUserActGift?CardGiftType=%s&CodeName=%s&title=%s&guideWords=%s&isReceive=%s",cardGiftType,codeName,title,guideWords,isReceive);
        }
        if(!TextUtils.isEmpty(targetId)){
            if(type==3){//专区详情
                return DiscountRoutes.DIS_SPECAREA+"?blockId="+targetId;
            }else if(type==2){//商品详情
                return ServiceRoutes.SERVICE_DETAIL+"?id="+targetId;
            }else {
                return null;
            }
        }else {
            if(TextUtils.isEmpty(linkUrl)){

                return getAndroidLinkName();
            }else {
                return getHttpLick();
            }
        }

    }

    private String getHttpLick() {
        if(linkUrl.startsWith("http")){
            return linkUrl;
        }else {
            return "http://"+linkUrl;
        }
    }

}
