package com.healthy.library.model;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.healthy.library.LibApplication;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import java.util.List;

public class SecondSortGoods implements MultiItemEntity {
    public String id;
    public String applicationNo;
    public String userId;

    public String getMerchantName() {
        if(!TextUtils.isEmpty(merchantShortName)){
            return merchantShortName;
        }
        return merchantName;
    }
    public String getTagFirst(){
        if(tagName==null||"null".equals(tagName)||"".equals(tagName)){
            return null;
        }
        return tagName.split(",")[0];
    }
    public String bargainId;
    public String assembleId;

    private String merchantName;
    private String merchantShortName;
    public String goodsNo;
    public String barcodeSpu;
    public String goodsName;
    public String goodsTitle;
    public String adTitle;
    public String specName;
    public String specValue;
    public double retailPrice;
    public double storePrice;
    public double platformPrice;
    public String salesMin;
    public String salesMax;
    public int goodsType;
    public String typeId;
    public String categoryId;
    public String categoryName;
    public String categoryNo;
    public String brandId;
    public String brandIds;
    public String brandName;
    public String additionNote;
    public String manual;
    public int goodsStatus;
    public String ranking;
    public long maxInventory;
    public long availableInventory;
    public long lockedInventory;
    public long totalInventory;
    public long visitTimes;
    public String virtualVisitTimes;
    public long wxSales;
    public long appSales;
    public long shareprofitSales;
    public long virtualSales;
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
    public int isCheck;
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
    public int goodsDataStatus;
    public String goodsDataStatusName;
    public int goodsDataType;
    public int channelPlatform;
    public String goodsDataSerialNum;
    public String goodsFileList;
    private String filePath;
    public List<Integer> publishes;
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

    public String getExShopId() {
        if(goodsShopIds==null||goodsShopIds.size()==0){
            return SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP);
        }
        return goodsShopIds.get(0);
    }

    public String shopId;
    public String ids;
    public String excludeGoodsIds;
    public String userIds;
    public String categoryIdQuery;
    public String categoryIds;
    public String goodsTypes;
    public String publish;
    public String shareThemeId;
    public List<String> applicables;
    public String goodsStatusStr;
    public String reviewStatusStr;
    public String headImage;
    public List<HeadImages> headImages;
    public String contentImages;
    public String contentText;
    public List<HeadImages> headVideos;
    public List<HeadImages> contentVideos;
    public String goodsFiles;
    public String shareThemes;
    public List<String> goodsShopIds;
    public String marketingGoodsShopIds;
    public List<GoodsChildren> goodsChildren;
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
    public List<Integer> relatedShopIds;
    public String mapMarketingGoodsList;
    public String marketingTypeList;
    public String barcodeSku;
    public int differentSymbol;
    public String goodsKindID;

    public String getFilePath() {
        if (!TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        try {
            return headImages.get(0).filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemType() {
        return 3;
    }

    public class GoodsChildren {
        public int id;
        public String goodsId;
        public String channelPlatform;
        public String barcodeSku;
        public String goodsTitle;
        public String adTitle;
        public String goodsSpec;
        public String goodsSpecStr;
        public long maxInventory;
        public long availableInventory;
        public long lockedInventory;
        public double retailPrice;
        public double storePrice;
        public double platformPrice;
        public int status;
        public String commissionRateShop;
        public String commissionRateUser;
        public int commissionRateShopIsOff;
        public int commissionRateUserIsOff;
        public long totalInventory;
        public String sales;
        public String version;
        public String shopId;
        public int priceGradeNum;
        public double limitPrice;
        public double plusPrice;
        public int goodsChildId;
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
        public int differentSymbol;
    }
    public class HeadImages {

        public long id;
        public int fileType;
        public String filePath;
        public String thumbsPath;
        public String imageTitle;
        public String imageDescription;
        public String textDescription;
        public long goodsId;
        public String channelPlatform;
        public int ranking;
        public int operate;

    }
}
