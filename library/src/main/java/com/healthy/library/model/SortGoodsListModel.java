package com.healthy.library.model;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.LibApplication;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SortGoodsListModel implements MultiItemEntity {


    public String bargainId;
    public String assembleId;
    /**
     * id : 146
     * applicationNo : null
     * userId : 8
     * merchantName : 爱婴岛
     * goodsNo :
     * barcodeSpu : xxxx0555
     * goodsName : 奶粉33333
     * goodsTitle : 奶粉33333
     * adTitle : 奶粉33333
     * specName : [{"id":1,"specName":"颜色","specValue":[],"realSpecName":"颜色","editor":false,"chooseValues":[]},{"id":2,"specName":"大小","specValue":[],"realSpecName":"大小","editor":false,"chooseValues":[]},{"id":3,"specName":"尺码","specValue":[],"realSpecName":"尺码","editor":false,"chooseValues":[]}]
     * specValue : [{"id":1,"specName":"颜色","specValue":[],"realSpecName":"颜色"},{"id":2,"specName":"大小","specValue":[],"realSpecName":"大小"},{"id":3,"specName":"尺码","specValue":[],"realSpecName":"尺码"}]
     * retailPrice : 1999.0
     * storePrice : 133.0
     * platformPrice : 1444.0
     * salesMin : null
     * salesMax : null
     * goodsType : 2
     * typeId : null
     * categoryId : 153
     * categoryName :  半水解奶粉
     * categoryNo : 003001001
     * brandId : 6
     * brandName : null
     * additionNote : <p><img src="http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/e5b01ffc-23a5-4fdb-b939-3b658b4d1a19.jpg"></p>
     * manual : null
     * goodsStatus : 2
     * ranking : null
     * maxInventory : 0
     * availableInventory : null
     * lockedInventory : null
     * visitTimes : 0
     * wxSales : 0
     * appSales : 0
     * shareprofitSales : 0
     * virtualSales : null
     * sameOffer : null
     * expiredDate : null
     * isReservation : null
     * reservationHours : null
     * isHomeSend : null
     * isHomeService : null
     * isDelete : 0
     * reviewStatus : 4
     * reviewNote : null
     * reviewUser : null
     * reviewTime : null
     * isCheck : null
     * checkUser : null
     * checkTime : null
     * checkNote : null
     * createUser : 爱婴岛
     * createTime : 2020-07-02T15:09:55
     * updateUser : null
     * updateTime : null
     * deleteUser : null
     * deleteTime : null
     * applicationType : null
     * distributionAmount : 18.0
     * distributionPercentage : null
     * goodsDataStatus : null
     * goodsDataType : 2
     * pageNum : null
     * pageSize : null
     * ids : null
     * userIds : null
     * categoryIdQuery : null
     * categoryIds : null
     * publish : null
     * publishes : [2,1]
     * applicables : null
     * goodsStatusStr : 已上架
     * headImage : http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/b5886163-d8fa-4467-bfdc-732ae1ae1771.jpg
     * headImages : [{"id":12289,"fileType":1,"filePath":"http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/b5886163-d8fa-4467-bfdc-732ae1ae1771.jpg","thumbsPath":null,"imageTitle":null,"imageDescription":null,"textDescription":null,"goodsId":146,"channelPlatform":null,"ranking":1},{"id":12290,"fileType":1,"filePath":"http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/3f6cce34-4415-4c44-a9f4-2a643d808860.jpg","thumbsPath":null,"imageTitle":null,"imageDescription":null,"textDescription":null,"goodsId":146,"channelPlatform":null,"ranking":2}]
     * contentImages : null
     * contentText : null
     * headVideos : null
     * contentVideos : null
     * goodsFiles : null
     * goodsChildren : [{"id":184,"goodsId":146,"channelPlatform":1,"barcodeSku":null,"goodsTitle":"奶粉33333","adTitle":"奶粉33333","goodsSpec":null,"goodsSpecStr":null,"maxInventory":0,"availableInventory":null,"lockedInventory":null,"storePrice":null,"platformPrice":null}]
     * ghr : null
     */

    private int id;
    private Object applicationNo;
    private int userId;
    private String merchantName;
    private String goodsNo;
    private String barcodeSpu;
    private String goodsName;
    private String goodsTitle;
    private String adTitle;
    private String specName;
    private String specValue;
    private double retailPrice;
    private double storePrice;
    private double platformPrice;
    private String plusPrice;
    private Object salesMin;
    private Object salesMax;
    private int goodsType;
    private Object typeId;
    private String categoryId;
    private String categoryName;
    private String categoryNo;
    private int brandId;
    private Object brandName;
    private String additionNote;
    private Object manual;
    private int goodsStatus;
    private Object ranking;
    private int maxInventory;
    private Object availableInventory;
    private Object lockedInventory;
    private int visitTimes;
    private int wxSales;
    private int appSales;
    private int shareprofitSales;
    private Object virtualSales;
    private Object sameOffer;
    private Object expiredDate;
    private Object isReservation;
    private Object reservationHours;
    private Object isHomeSend;
    private Object isHomeService;
    private int isDelete;
    private int reviewStatus;
    public int differentSymbol;
    private Object reviewNote;
    private Object reviewUser;
    private Object reviewTime;
    private Object isCheck;
    private Object checkUser;
    private Object checkTime;
    private Object checkNote;
    private String createUser;
    private String createTime;
    private Object updateUser;
    private Object updateTime;
    private Object deleteUser;
    private Object deleteTime;
    private Object applicationType;
    private double distributionAmount;
    private Object distributionPercentage;
    private Object goodsDataStatus;
    private int goodsDataType;
    private Object pageNum;
    private Object pageSize;
    private Object ids;
    private Object userIds;
    private Object categoryIdQuery;
    private Object categoryIds;
    private Object publish;
    private Object applicables;
    private String goodsStatusStr;
    private String headImage;
    private String shopId;
    private Object contentImages;
    private Object contentText;
    private Object headVideos;
    private Object contentVideos;
    private Object goodsFiles;
    private Object ghr;
    private List<Integer> publishes;
    private List<HeadImagesBean> headImages;
    private List<GoodsChildrenBean> goodsChildren;
    private List<GoodsSpecCell> goodsSpecCells;
    public String tagName;//标签名称（逗号分隔）
    public String marketingType;

    public String getPlusPrice() {
        return plusPrice;
    }

    public void setPlusPrice(String plusPrice) {
        this.plusPrice = plusPrice;
    }

    public String getTagFirst() {
        if (tagName == null || "null".equals(tagName) || "".equals(tagName)) {
            return null;
        }
        return tagName.split(",")[0];
    }

    public String[] goodsShopIds;

    public String getShopId() {
        if (goodsShopIds != null && goodsShopIds.length > 0) {
            return goodsShopIds[0];
        }
        if (!TextUtils.isEmpty(shopId)) {
            return shopId;
        }
        return SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP);
    }

    public List<GoodsSpecCell> getSpecCell() {
        if (goodsSpecCells != null) {
            return goodsSpecCells;
        }
        if (TextUtils.isEmpty(specValue) || specValue == null) {
            return new ArrayList<>();
        } else {
            goodsSpecCells = resolveSpecListData(specValue);
            return goodsSpecCells;
        }
    }

    private List<GoodsSpecCell> resolveSpecListData(String obj) {
        List<GoodsSpecCell> result = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<GoodsSpecCell>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(Object applicationNo) {
        this.applicationNo = applicationNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getBarcodeSpu() {
        return barcodeSpu;
    }

    public void setBarcodeSpu(String barcodeSpu) {
        this.barcodeSpu = barcodeSpu;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecValue() {
        return specValue;
    }

    public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getStorePrice() {
        if (retailPrice != 0) {
            return retailPrice;
        }
        return storePrice;
    }

    public void setStorePrice(double storePrice) {
        this.storePrice = storePrice;
    }

    public double getPlatformPrice() {
        return platformPrice;
    }

    public void setPlatformPrice(double platformPrice) {
        this.platformPrice = platformPrice;
    }

    public Object getSalesMin() {
        return salesMin;
    }

    public void setSalesMin(Object salesMin) {
        this.salesMin = salesMin;
    }

    public Object getSalesMax() {
        return salesMax;
    }

    public void setSalesMax(Object salesMax) {
        this.salesMax = salesMax;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public Object getTypeId() {
        return typeId;
    }

    public void setTypeId(Object typeId) {
        this.typeId = typeId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public Object getBrandName() {
        return brandName;
    }

    public void setBrandName(Object brandName) {
        this.brandName = brandName;
    }

    public String getAdditionNote() {
        return additionNote;
    }

    public void setAdditionNote(String additionNote) {
        this.additionNote = additionNote;
    }

    public Object getManual() {
        return manual;
    }

    public void setManual(Object manual) {
        this.manual = manual;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Object getRanking() {
        return ranking;
    }

    public void setRanking(Object ranking) {
        this.ranking = ranking;
    }

    public int getMaxInventory() {
        return maxInventory;
    }

    public void setMaxInventory(int maxInventory) {
        this.maxInventory = maxInventory;
    }

    public Object getAvailableInventory() {
        return availableInventory;
    }

    public void setAvailableInventory(Object availableInventory) {
        this.availableInventory = availableInventory;
    }

    public Object getLockedInventory() {
        return lockedInventory;
    }

    public void setLockedInventory(Object lockedInventory) {
        this.lockedInventory = lockedInventory;
    }

    public int getVisitTimes() {
        return visitTimes;
    }

    public void setVisitTimes(int visitTimes) {
        this.visitTimes = visitTimes;
    }

    public int getWxSales() {
        return wxSales;
    }

    public void setWxSales(int wxSales) {
        this.wxSales = wxSales;
    }

    public int getAppSales() {
        return appSales;
    }

    public void setAppSales(int appSales) {
        this.appSales = appSales;
    }

    public int getShareprofitSales() {
        return shareprofitSales;
    }

    public void setShareprofitSales(int shareprofitSales) {
        this.shareprofitSales = shareprofitSales;
    }

    public Object getVirtualSales() {
        return virtualSales;
    }

    public void setVirtualSales(Object virtualSales) {
        this.virtualSales = virtualSales;
    }

    public Object getSameOffer() {
        return sameOffer;
    }

    public void setSameOffer(Object sameOffer) {
        this.sameOffer = sameOffer;
    }

    public Object getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Object expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Object getIsReservation() {
        return isReservation;
    }

    public void setIsReservation(Object isReservation) {
        this.isReservation = isReservation;
    }

    public Object getReservationHours() {
        return reservationHours;
    }

    public void setReservationHours(Object reservationHours) {
        this.reservationHours = reservationHours;
    }

    public Object getIsHomeSend() {
        return isHomeSend;
    }

    public void setIsHomeSend(Object isHomeSend) {
        this.isHomeSend = isHomeSend;
    }

    public Object getIsHomeService() {
        return isHomeService;
    }

    public void setIsHomeService(Object isHomeService) {
        this.isHomeService = isHomeService;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public int getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(int reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Object getReviewNote() {
        return reviewNote;
    }

    public void setReviewNote(Object reviewNote) {
        this.reviewNote = reviewNote;
    }

    public Object getReviewUser() {
        return reviewUser;
    }

    public void setReviewUser(Object reviewUser) {
        this.reviewUser = reviewUser;
    }

    public Object getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Object reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Object getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Object isCheck) {
        this.isCheck = isCheck;
    }

    public Object getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(Object checkUser) {
        this.checkUser = checkUser;
    }

    public Object getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Object checkTime) {
        this.checkTime = checkTime;
    }

    public Object getCheckNote() {
        return checkNote;
    }

    public void setCheckNote(Object checkNote) {
        this.checkNote = checkNote;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Object updateUser) {
        this.updateUser = updateUser;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public Object getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(Object deleteUser) {
        this.deleteUser = deleteUser;
    }

    public Object getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Object deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Object getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(Object applicationType) {
        this.applicationType = applicationType;
    }

    public double getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(double distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    public Object getDistributionPercentage() {
        return distributionPercentage;
    }

    public void setDistributionPercentage(Object distributionPercentage) {
        this.distributionPercentage = distributionPercentage;
    }

    public Object getGoodsDataStatus() {
        return goodsDataStatus;
    }

    public void setGoodsDataStatus(Object goodsDataStatus) {
        this.goodsDataStatus = goodsDataStatus;
    }

    public int getGoodsDataType() {
        return goodsDataType;
    }

    public void setGoodsDataType(int goodsDataType) {
        this.goodsDataType = goodsDataType;
    }

    public Object getPageNum() {
        return pageNum;
    }

    public void setPageNum(Object pageNum) {
        this.pageNum = pageNum;
    }

    public Object getPageSize() {
        return pageSize;
    }

    public void setPageSize(Object pageSize) {
        this.pageSize = pageSize;
    }

    public Object getIds() {
        return ids;
    }

    public void setIds(Object ids) {
        this.ids = ids;
    }

    public Object getUserIds() {
        return userIds;
    }

    public void setUserIds(Object userIds) {
        this.userIds = userIds;
    }

    public Object getCategoryIdQuery() {
        return categoryIdQuery;
    }

    public void setCategoryIdQuery(Object categoryIdQuery) {
        this.categoryIdQuery = categoryIdQuery;
    }

    public Object getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Object categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Object getPublish() {
        return publish;
    }

    public void setPublish(Object publish) {
        this.publish = publish;
    }

    public Object getApplicables() {
        return applicables;
    }

    public void setApplicables(Object applicables) {
        this.applicables = applicables;
    }

    public String getGoodsStatusStr() {
        return goodsStatusStr;
    }

    public void setGoodsStatusStr(String goodsStatusStr) {
        this.goodsStatusStr = goodsStatusStr;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Object getContentImages() {
        return contentImages;
    }

    public void setContentImages(Object contentImages) {
        this.contentImages = contentImages;
    }

    public Object getContentText() {
        return contentText;
    }

    public void setContentText(Object contentText) {
        this.contentText = contentText;
    }

    public Object getHeadVideos() {
        return headVideos;
    }

    public void setHeadVideos(Object headVideos) {
        this.headVideos = headVideos;
    }

    public Object getContentVideos() {
        return contentVideos;
    }

    public void setContentVideos(Object contentVideos) {
        this.contentVideos = contentVideos;
    }

    public Object getGoodsFiles() {
        return goodsFiles;
    }

    public void setGoodsFiles(Object goodsFiles) {
        this.goodsFiles = goodsFiles;
    }

    public Object getGhr() {
        return ghr;
    }

    public void setGhr(Object ghr) {
        this.ghr = ghr;
    }

    public List<Integer> getPublishes() {
        return publishes;
    }

    public void setPublishes(List<Integer> publishes) {
        this.publishes = publishes;
    }

    public List<HeadImagesBean> getHeadImages() {
        return headImages;
    }

    public void setHeadImages(List<HeadImagesBean> headImages) {
        this.headImages = headImages;
    }

    public List<GoodsChildrenBean> getGoodsChildren() {
        return goodsChildren;
    }

    public void setGoodsChildren(List<GoodsChildrenBean> goodsChildren) {
        this.goodsChildren = goodsChildren;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    public static class HeadImagesBean {
        /**
         * id : 12289
         * fileType : 1
         * filePath : http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/b5886163-d8fa-4467-bfdc-732ae1ae1771.jpg
         * thumbsPath : null
         * imageTitle : null
         * imageDescription : null
         * textDescription : null
         * goodsId : 146
         * channelPlatform : null
         * ranking : 1
         */

        private int id;
        private int fileType;
        private String filePath;
        private Object thumbsPath;
        private Object imageTitle;
        private Object imageDescription;
        private Object textDescription;
        private int goodsId;
        private Object channelPlatform;
        private int ranking;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getFileType() {
            return fileType;
        }

        public void setFileType(int fileType) {
            this.fileType = fileType;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Object getThumbsPath() {
            return thumbsPath;
        }

        public void setThumbsPath(Object thumbsPath) {
            this.thumbsPath = thumbsPath;
        }

        public Object getImageTitle() {
            return imageTitle;
        }

        public void setImageTitle(Object imageTitle) {
            this.imageTitle = imageTitle;
        }

        public Object getImageDescription() {
            return imageDescription;
        }

        public void setImageDescription(Object imageDescription) {
            this.imageDescription = imageDescription;
        }

        public Object getTextDescription() {
            return textDescription;
        }

        public void setTextDescription(Object textDescription) {
            this.textDescription = textDescription;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public Object getChannelPlatform() {
            return channelPlatform;
        }

        public void setChannelPlatform(Object channelPlatform) {
            this.channelPlatform = channelPlatform;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }
    }

    public static class GoodsChildrenBean {
        /**
         * id : 184
         * goodsId : 146
         * channelPlatform : 1
         * barcodeSku : null
         * goodsTitle : 奶粉33333
         * adTitle : 奶粉33333
         * goodsSpec : null
         * goodsSpecStr : null
         * maxInventory : 0
         * availableInventory : null
         * lockedInventory : null
         * storePrice : null
         * platformPrice : null
         */

        private int id;
        private int goodsId;
        private int channelPlatform;
        private Object barcodeSku;
        private String goodsTitle;
        private String adTitle;
        private Object goodsSpec;
        private Object goodsSpecStr;
        private int maxInventory;
        private Object availableInventory;
        private Object lockedInventory;
        private Object storePrice;
        private Object platformPrice;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public int getChannelPlatform() {
            return channelPlatform;
        }

        public void setChannelPlatform(int channelPlatform) {
            this.channelPlatform = channelPlatform;
        }

        public Object getBarcodeSku() {
            return barcodeSku;
        }

        public void setBarcodeSku(Object barcodeSku) {
            this.barcodeSku = barcodeSku;
        }

        public String getGoodsTitle() {
            return goodsTitle;
        }

        public void setGoodsTitle(String goodsTitle) {
            this.goodsTitle = goodsTitle;
        }

        public String getAdTitle() {
            return adTitle;
        }

        public void setAdTitle(String adTitle) {
            this.adTitle = adTitle;
        }

        public Object getGoodsSpec() {
            return goodsSpec;
        }

        public void setGoodsSpec(Object goodsSpec) {
            this.goodsSpec = goodsSpec;
        }

        public Object getGoodsSpecStr() {
            return goodsSpecStr;
        }

        public void setGoodsSpecStr(Object goodsSpecStr) {
            this.goodsSpecStr = goodsSpecStr;
        }

        public int getMaxInventory() {
            return maxInventory;
        }

        public void setMaxInventory(int maxInventory) {
            this.maxInventory = maxInventory;
        }

        public Object getAvailableInventory() {
            return availableInventory;
        }

        public void setAvailableInventory(Object availableInventory) {
            this.availableInventory = availableInventory;
        }

        public Object getLockedInventory() {
            return lockedInventory;
        }

        public void setLockedInventory(Object lockedInventory) {
            this.lockedInventory = lockedInventory;
        }

        public Object getStorePrice() {
            return storePrice;
        }

        public void setStorePrice(Object storePrice) {
            this.storePrice = storePrice;
        }

        public Object getPlatformPrice() {
            return platformPrice;
        }

        public void setPlatformPrice(Object platformPrice) {
            this.platformPrice = platformPrice;
        }
    }

}
