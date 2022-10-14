package com.healthy.library.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class LiveVideoGoods implements MultiItemEntity {
    public String id;
    public String goodsId;
    public String liveActivityId;
    public String goodsTitle;
    public String retailPrice;
    public String platformPrice;
    public String livePrice;
    public int salesMin;
    public int salesMax;
    public int goodsType;
    public int ranking;
    public String sales;
    public String maxInventory;
    public int availableInventory;
    public String lockedInventory;
    public String totalInventory;
    public String createUser;
    public String createTime;
    public String updateUser;
    public String updateTime;
    public int offShelf;
    public String isDelete;
    public int isShopTakeOnly;
    public int isSpeak;
    public int isEncore;
    public String pageNum;
    public String pageSize;
    public String statusStr;
    public List<GoodsChildren> goodsChildren;
    public List<ShareGiftDTO> shareGiftDTOS;

    public int getMarkLimitMinNow() {
        if (salesMin > 0) {
            return salesMin;
        }
        return 1;
    }

    public int getMarkLimitMaxNowWithInventory() {
        if (getGoodsChildrenInventory() > getMarkLimitMaxNow()) {//活动库存大于可购就返回可够数量

            return getMarkLimitMaxNow();
        } else {
            return getGoodsChildrenInventory();
        }
    }

    public String getGoodsChildrenId() {
        if (goodsChildren != null && goodsChildren.size() > 0) {
            return goodsChildren.get(0).goodsChildId;
        } else {
            return null;
        }
    }

    public int getMarkLimitMaxNow() {
        if (salesMax > 0) {
            return salesMax;
        }
        return 9999;
    }

    public boolean getGoodsIsOffShelf() {
        if (goodsChildren != null && goodsChildren.size() > 0) {
            for (int i = 0; i < goodsChildren.size(); i++) {
                if (goodsChildren.get(i).offShelf == 0) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    public double getGoodsChildrenLivePrice() {
        double livePrice = 0;
        if (goodsChildren != null && goodsChildren.size() > 0) {
            for (int i = 0; i < goodsChildren.size(); i++) {
                if (goodsChildren.get(i).offShelf == 0) {
                    livePrice = goodsChildren.get(i).livePrice;
                    return livePrice;
                }
            }
            if (livePrice > 0) {
                return livePrice;
            } else {
                return goodsChildren.get(0).livePrice;
            }
        } else {
            return 0;
        }
    }

    public double getGoodsChildrenRetailPricePrice() {
        double retailPrice = 0;
        if (goodsChildren != null && goodsChildren.size() > 0) {
            for (int i = 0; i < goodsChildren.size(); i++) {
                if (goodsChildren.get(i).offShelf == 0) {
                    retailPrice = goodsChildren.get(i).retailPrice;
                    return retailPrice;
                }
            }
            return retailPrice;
        } else {
            return 0;
        }
    }

    public String getGoodsChildrenSpecStr() {
        StringBuffer goodsSpecStr = new StringBuffer();
        if (goodsChildren != null && goodsChildren.size() > 0) {
            for (int i = 0; i < goodsChildren.size(); i++) {
                if (i == 0) {
                    goodsSpecStr = goodsSpecStr.append(goodsChildren.get(i).getGoodsSpecStr());
                } else {
                    goodsSpecStr = goodsSpecStr.append(" | " + goodsChildren.get(i).getGoodsSpecStr());
                }
            }
            return goodsSpecStr.toString();
        } else {
            return "";
        }
    }

    public int getGoodsChildrenInventory() {
        int availableInventory = 0;
        if (goodsChildren != null && goodsChildren.size() > 0) {
            for (int i = 0; i < goodsChildren.size(); i++) {
                if (goodsChildren.get(i).offShelf == 0) {
                    availableInventory = goodsChildren.get(i).availableInventory;
                    return availableInventory;
                }
            }
            return availableInventory;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemType() {
        return isSpeak;
    }

    public class GoodsChildren {
        public String id;
        public String goodsId;
        public String goodsChildId;
        public String liveGoodsId;
        public String barcodeSku;
        public String goodsTitle;
        public String adTitle;
        public String goodsSpec;
        public String goodsSpecStr;
        public double retailPrice;
        public String storePrice;
        public double platformPrice;
        public double livePrice;
        public String maxInventory;
        public int availableInventory;
        public String lockedInventory;
        public String totalInventory;
        public String sales;
        public int offShelf;

        public String getGoodsSpecStr() {
            if (goodsSpecStr == null) {
                goodsSpecStr = "";
            }
            return goodsSpecStr;
        }
    }

    public String[] shopIds;
    public List<HeadImages> headImages;

    public class HeadImages {
        public String id;
        public String fileType;
        public String filePath;
        public String thumbsPath;
        public String imageTitle;
        public String imageDescription;
        public String textDescription;
        public String goodsId;
        public String channelPlatform;
        public String ranking;
        public String operate;
    }
}
