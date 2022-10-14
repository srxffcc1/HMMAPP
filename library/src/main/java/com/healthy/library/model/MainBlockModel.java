package com.healthy.library.model;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 专题内容
 */
public class MainBlockModel implements Comparable<MainBlockModel>, MultiItemEntity {
    public String id;

    public String userId;

    public String channelPlatform;

    public String themeName;

    public String themeSubname;

    public String themeImage;

    public String themeTag = "";

    public int exhibition;//1、单行通栏列表 2、错位组合栏 3、通栏大图+介绍 4、多栏商品+商品组合展示 5、通栏图片+商品组合列表

    public String describe;

    public String adContent;

    public int parentId;

    public String ranking;

    public String status;

    public String createUser;

    public String createTime;

    public String updateUser;

    public String updateTime;

    public String deleteUser;

    public String deleteTime;

    public String isDelete;

    public String iconUrl;

    public int themeType;//1憨妈赚首页主题专区、2积分商城主页主题专区、3App首页主题专区、4商城首页专区、5普通专区

    public String isThemeShow;

    public List<MainBlockModel> childList = new ArrayList<>();

    public MainBlockModel(String listImageUrl) {
        themeImage=listImageUrl;
    }

    public boolean isComplete() {
        if (childList!=null&&childList.size() > 0) {
            for (int i = 0; i < childList.size(); i++) {
                if (!childList.get(i).isComplete()) {
                    return false;
                }
            }
            return true;
        } else if(allianceMerchantList!=null&&allianceMerchantList.size()>0){
            for (int i = 0; i < allianceMerchantList.size(); i++) {
                if (!allianceMerchantList.get(i).isComplete()) {
                    return false;
                }
            }
            return true;
        }else {
            if (detailList != null) {
                return true;
            } else {
                return false;
            }
        }
    }

    public String goodsCount;

    public String shopCount;


    public List<MainBlockDetailModel> detailList;


    @Override
    public int compareTo(MainBlockModel o) {
        return (int) (exhibition - o.exhibition);
    }

    @Override
    public int getItemType() {
        return exhibition;
    }

    public List<AllianceMerchant> getRealAllianceMerchantList() {//去除没有门店的
        List<AllianceMerchant> result=new ArrayList<>();
        if(allianceMerchantList!=null){
            for (int i = 0; i < allianceMerchantList.size(); i++) {
                if(allianceMerchantList.get(i).shopDto!=null){
                    result.add(allianceMerchantList.get(i));
                }
            }
        }
        return result;
    }

    private List<AllianceMerchant> allianceMerchantList;

    public class AllianceMerchant {

        public String id;
        public String merchantId;
        public String themeId;
        public String ranking;
        public String merchant;
        public ShopDto shopDto;
        public List<MainBlockDetailModel> detailList;

        public boolean isComplete() {
            if (detailList != null) {
                return true;
            } else {
                return false;
            }
        }
    }


    public class ShopDto {
        public int id;
        public int userId;
        public long merchantCode;
        private String merchantName;

        public String getMerchantName() {
            if(!TextUtils.isEmpty(merchantShortName)){
                return merchantShortName;
            }
            return merchantName;
        }

        public String merchantShortName;
        public String brandName;
        public String shopName;
        public String chainShopName;
        public String addressProvince;
        public String provinceName;
        public String addressCity;
        public String cityName;
        public String addressArea;
        public String addressAreaName;
        public String addressDetails;
        public String businessHourStart;
        public String businessHourEnd;
        public int businessStatus;
        public String appointmentPhone;
        public String enterExpense;
        public String remark;
        public double longitude;
        public double latitude;
        public String createTime;
        public String updateTime;
        public String addRemark;
        public String businessLicensePicUrl;
        public String envPicUrl;
        public String categoryNoLevel1;
        public String categoryNoLevel2;
        public String auditStatus;
        public long distance;
        public String brandIntroduce;
        public String shopIntroduce;
        public String shopContent;
        public int shopType;
        public double shopStartFee;
        public String shopBusinessOfArea;
        public String ytbDepartID;
        public String ytbDepartName;
        public String categoryList;
        public int shopFlag;
        public int shopTag;
        public String principal;
        public int isSupportOverSold;
        public int departId;
        public String departName;
        
        
    }
}
