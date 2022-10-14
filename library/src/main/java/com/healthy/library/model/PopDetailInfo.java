package com.healthy.library.model;

import android.text.TextUtils;

import com.healthy.library.LibApplication;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import java.io.Serializable;
import java.util.List;

public class PopDetailInfo implements Serializable {


    public List<GoodsDTOListBean> goodsDTOList;

    public class GoodsDTOListBean implements Serializable{
        public String id;
        public String applicationNo;
        public int userId;
        public String merchantName;
        public String goodsNo;
        public String barcodeSpu;
        public String goodsName;
        public String goodsTitle;
        public String adTitle;
        public String specName;
        public String specValue;
        public double retailPrice;
        public String storePrice;
        public double platformPrice;
        public String salesMin;
        public String salesMax;
        public int goodsType;
        public String typeId;
        public String categoryId;
        public String categoryName;
        public String categoryNo;
        public int brandId;
        public String brandIds;
        public String brandName;
        public String additionNote;
        public String manual;
        public int goodsStatus;
        public String ranking;
        public int maxInventory;
        public int availableInventory;
        public int lockedInventory;
        public int totalInventory;
        public int visitTimes;
        public String virtualVisitTimes;
        public int wxSales;
        public int appSales;
        public int shareprofitSales;
        public String virtualSales;
        public String sameOffer;
        public String expiredDate;
        public String isReservation;
        public String reservationHours;
        public String isHomeSend;
        public String isHomeService;
        public int isDelete;
        public int reviewStatus;
        public String reviewNote;
        public String reviewUser;
        public String reviewTime;
        public String isCheck;
        public String checkUser;
        public String checkTime;
        public String checkNote;
        public String offShelfRemark;
        public String createUser;
        public String createTime;
        public String updateUser;
        public String updateTime;
        public String deleteUser;
        public String deleteTime;
        public String applicationType;
        public String distributionAmount;
        public String distributionPercentage;
        public String goodsDataStatus;
        public String goodsDataStatusName;
        public int goodsDataType;
        public int channelPlatform;
        public String goodsDataSerialNum;
        public String goodsFileList;
        public String filePath;
        public String resourceUserId;
        public String description;
        public String deptId;
        public double limitPrice;
        public double plusPrice;
        public String ytbGoodsId;
        public String tagId;
        public String tagName;
        public int isSelfGoods;
        public String pageNum;
        public String pageSize;
        public String createTimeBegin;
        public String createTimeEnd;
        public String shopId;
        public String ids;
        public String excludeGoodsIds;
        public String userIds;
        public String categoryIdQuery;
        public String categoryIds;
        public String goodsTypes;
        public String publish;
        public String shareThemeId;
        public String goodsStatusStr;
        public String reviewStatusStr;
        public String headImage;
        public String contentImages;
        public String contentText;
        public String goodsFiles;
        public String shareThemes;
        public String marketingGoodsShopIds;
        public String marketingGoodsChildren;
        public String ghr;
        public String appSalesSort;
        public String platformPriceSort;
        public String goodsShops;
        public String mapMarketingGoodsId;
        public String marketingType;
        public String marketingTypeStr;
        public String marketingBeginTime;
        public String marketingEndTime;
        public String marketingSales;
        public String marketingSalesMin;
        public String marketingSalesMax;
        public String marketingId;
        public String isPlusOnly;
        public String pointsPrice;
        public String pointsGoodsType;
        public String pointsTodayInventory;
        public String pointsEverydayInventory;
        public String pointsGoodsStatus;
        public String pointsGoodsStatusStr;
        public String pointsGoodsNote;
        public String goodsShopPrices;
        public String relatedShopIds;
        public String mapMarketingGoodsList;
        public String marketingTypeList;
        public String goodsKindID;
        public List<Integer> publishes;
        public List<?> applicables;
        public List<HeadImagesBean> headImages;
        public List<?> headVideos;
        public List<?> contentVideos;
        public String[] goodsShopIds;

        public String getShopId() {
            if(goodsShopIds!=null&&goodsShopIds.length>0){
                return goodsShopIds[0];
            }
            if(!TextUtils.isEmpty(shopId)){
                return shopId;
            }
            return SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP);
        }
        public List<GoodsChildrenBean> goodsChildren;

        public class HeadImagesBean implements Serializable{
            public long id;
            public int fileType;
            public String filePath;
            public String thumbsPath;
            public String imageTitle;
            public String imageDescription;
            public String textDescription;
            public int goodsId;
            public String channelPlatform;
            public int ranking;
            public int operate;
        }

        public class GoodsChildrenBean implements Serializable{
            public String id;
            public String goodsId;
            public String channelPlatform;
            public String barcodeSku;
            public String goodsTitle;
            public String adTitle;
            public String goodsSpec;
            public String goodsSpecStr;
            public int maxInventory;
            public int availableInventory;
            public int lockedInventory;
            public double retailPrice;
            public String storePrice;
            public double platformPrice;
            public int status;
            public String commissionRateShop;
            public String commissionRateUser;
            public int commissionRateShopIsOff;
            public int commissionRateUserIsOff;
            public long totalInventory;
            public int sales;
            public String version;
            public String shopId;
            public long priceGradeNum;
            public double limitPrice;
            public double plusPrice;
            public String goodsChildId;
            public String goodsImage;
            public String mchId;
            public String mchName;
            public String goodsType;
            public String differentBusinessPercentage;
            public String commission;
            public String expiredDate;
            public String goodsCommissionMoney;
            public String salesMin;
            public String salesMax;
            public String categoryId;
            public String categoryName;
            public String brandId;
            public String brandName;
            public String commissionRateMoneyPercent;
            public String relatedShopIds;
            public String offLineMaxInventory;
            public String offLineDepartId;
            public String offLineGoodsName;

            public String getGoodsSpecStr() {
                if (goodsSpecStr != null && !TextUtils.isEmpty(goodsSpecStr)) {
                    return goodsSpecStr;
                } else if (goodsTitle != null && !TextUtils.isEmpty(goodsTitle)) {
                    return goodsTitle;
                } else {
                    return "";
                }
            }

            public int getAvailableInventory() {
                return availableInventory;
            }

            public String getCount() {
                return count;
            }

            public String count = "1";

            public GoodsChildrenBean setCount(String count) {
                this.count = count;
                return this;
            }
        }
    }
}
