package com.healthy.library.model;

import android.text.TextUtils;

import com.healthy.library.LibApplication;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车商品
 */
public class GoodsBasketCell implements Comparable<GoodsBasketCell> {


    public String goodsShopId;//增加shopid 如果为空说明就是当前切换到的shop
    public String goodsShopName;//增加 shopnName
    public String goodsShopChainName;
    public String goodsShopBrandName;
    public String goodsShopAppointmentPhone;
    public String goodsShopAddress;
    public boolean isPLusShare = false;//plus专享 商品
    public GoodsBasketGroup goodsBasketGroup;
    public ActVip.PopInfo popInfo;
    public String UUID;
    public boolean isInit=false;
    public int errorCount=0;

    public boolean isError() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.isError;
        }
        return isError;
    }

    public boolean isUndo() {
        return undoStatus;
    }

    public boolean undoStatus;
    public boolean isError = false;//判断商品是不是错误 多用于9203报错返回

    public String getIsSupportOverSold() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getIsSupportOverSold();
        }
        return isSupportOverSold;
    }

    public void setIsSupportOverSold(String isSupportOverSold) {
        if (extraGoodsBasketCell != null) {
            extraGoodsBasketCell.setIsSupportOverSold(isSupportOverSold);
            return;
        }
        this.isSupportOverSold = isSupportOverSold;
    }

    private String isSupportOverSold = "0";


    public List<CouponInfoZ> getCouponInfoZList() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getCouponInfoZList();
        }
        return couponInfoZList;
    }

    public void setCouponInfoZList(List<CouponInfoZ> couponInfoZList) {
        if (extraGoodsBasketCell != null) {
            extraGoodsBasketCell.setCouponInfoZList(couponInfoZList);
            return;
        }
        this.couponInfoZList = couponInfoZList;
    }

    private List<CouponInfoZ> couponInfoZList;//原始优惠券池

    private List<CouponInfoZ> couponInfoLeftZList = new ArrayList<>();//优惠券可选池


    public List<CouponInfoZ> getCouponInfoKeyList() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getCouponInfoKeyList();
        }
        return couponInfoKeyList;
    }

    private List<CouponInfoZ> couponInfoKeyList = new ArrayList<>();


    private String isPlusOnly;

    public boolean isPlusOnly() {
        return "1".equals(isPlusOnly);
    }

    private List<CouponInfoZ> couponInfoAllSelectList = new ArrayList<>();

    public List<CouponInfoZ> getCouponInfoAllSelectList() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getCouponInfoAllSelectList();
        }
        return couponInfoAllSelectList;
    }

    public void setGroupDiscount(double groupDiscount) {
        if (extraGoodsBasketCell != null) {
            extraGoodsBasketCell.setGroupDiscount(groupDiscount);
            return;
        }
        this.groupDiscount = groupDiscount;
    }


    public double getGroupDiscount() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGroupDiscount();
        }
        return groupDiscount;
    }

    private double groupDiscount = 0;//用来记录营销活动优惠


    public double virtualDiscount = 0;//用来记录买送的活动优惠

    private String groupDiscountRule = "";//用来记录营销活动优惠

    public String getGroupDiscountRule() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGroupDiscountRule();
        }
        return groupDiscountRule;
    }

    public String goodsSource;
    public String goodsType = "2";//商品类型 服务标品

    public String cartDetailId;//购物车明细id

    public String cartId;//购物车id

    public boolean isGift = false;//是不是礼物

    public boolean isUseCard = false;//是不是优惠券

    public boolean isCardCanUse = false;//是不是可用优惠券

    public int CardNo = -1;//优惠券编号

    public boolean isCardSelect = false;//是不是选中

    public GoodsBasketCell extraGoodsBasketCell;//用于门店切换的时候保存新的商品实体

    public GoodsBasketCell extraGoodsBasketCellOrg;//用于门店切换的时候保存新的商品实体Org


    public GoodsBasketCell shopBack() {
        this.extraGoodsBasketCellOrg = null;
        this.extraGoodsBasketCell = null;
        return this;
    }

    public GoodsBasketCell undo() {//用来变为原来那一套逻辑来获取原始商品方法
        this.extraGoodsBasketCell = null;
        undoStatus = false;
        return this;
    }

    public GoodsBasketCell redo() {//用来重新增加Extra 然后使得方法拥有extra获得extra逻辑
        ////System.out.println("恢复到ex逻辑");
        this.extraGoodsBasketCell = extraGoodsBasketCellOrg;
        return this;
    }

    public void setExtraGoodsBasketCell(GoodsBasketCell extraGoodsBasketCell) {
        this.extraGoodsBasketCellOrg = extraGoodsBasketCell;
        this.extraGoodsBasketCell = extraGoodsBasketCell;
    }

    public boolean isNeedFixOrg = false;//当判断是礼物时 在提交时判断需不需要把礼物的数量附加到原商品 利于此礼物是买赠的


    public String[] shopIdList = new String[]{};//指定的shopid备用

    public int sourceType;

    public String packageType;

    public int goodsQuantity;
    public int standardPriceType = 1;


    public int getGoodsQuantityGiftNeedFixOrg() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsQuantityGiftNeedFixOrg();
        }
        return goodsQuantityGiftNeedFixOrg;
    }

    public void setGoodsQuantityGiftNeedFixOrg(int goodsQuantityGiftNeedFixOrg) {
        if (extraGoodsBasketCell != null) {
            extraGoodsBasketCell.setGoodsQuantityGiftNeedFixOrg(goodsQuantityGiftNeedFixOrg);
            return;
        }
        this.goodsQuantityGiftNeedFixOrg = goodsQuantityGiftNeedFixOrg;
    }

    private int goodsQuantityGiftNeedFixOrg = 0;

    public void setDiscountTopModel(DiscountTopModel discountTopModel) {
        if (extraGoodsBasketCell != null) {
            extraGoodsBasketCell.setDiscountTopModel(discountTopModel);
            return;
        }
        this.discountTopModel = discountTopModel;
    }

    public DiscountTopModel getDiscountTopModel() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getDiscountTopModel();
        }
        return discountTopModel;
    }

    private DiscountTopModel discountTopModel;


    public int getGoodsQuantity() {//获取数量 不涉及双库存判定
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsQuantity();
        }
        if (isError) {
            ////System.out.println("出错商品");
            return 0;
        }
        return goodsQuantity > getStock() ? getStock() : goodsQuantity;
    }

    public int getGoodsQuantityInBasket() {//获取之前要先判断库存了
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsQuantityInBasket();
        }
        if (isError) {
            ////System.out.println("出错商品");
            return 0;
        }
        return goodsQuantity;
    }

    public void setGoodsQuantity(int goodsQuantity) {
        System.out.println("修改数量了:" + goodsQuantity);
        if (extraGoodsBasketCell != null) {
            extraGoodsBasketCell.setGoodsQuantity(goodsQuantity);
        }
        this.goodsQuantity = goodsQuantity;
    }

    public String getGoodsSpecId() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsSpecId();
        }
        return goodsSpecId;
    }

    public String getGoodsSpecIdOrg() {
        return goodsSpecId;
    }

    public void setGoodsSpecId(String goodsSpecId) {
        if (extraGoodsBasketCell != null) {
            extraGoodsBasketCell.setGoodsSpecId(goodsSpecId);
            return;
        }
        this.goodsSpecId = goodsSpecId;
    }

    private String goodsSpecId;


    public GoodsBasketCell() {

    }

    public GoodsBasketCell(GoodsBasketCell orgbasketCell) {
        this.curGoodsAmount = orgbasketCell.curGoodsAmount;
        this.curGoodsRetailAmount = orgbasketCell.curGoodsRetailAmount;
        this.curGoodsPlusAmount = orgbasketCell.curGoodsPlusAmount;
        this.goodsType = orgbasketCell.goodsType;
        this.goodsMarketingDTO = orgbasketCell.goodsMarketingDTO;
        this.mchId = orgbasketCell.mchId;
        this.goodsId = orgbasketCell.goodsId;
        this.goodsStock = orgbasketCell.goodsStock;
        this.goodsSpecId = orgbasketCell.goodsSpecId;
        this.goodsTitle = orgbasketCell.goodsTitle;
        this.goodsImage = orgbasketCell.goodsImage;
        this.goodsQuantity = (orgbasketCell.goodsQuantity);
        this.shopIdList = orgbasketCell.shopIdList;
        this.ischeck = orgbasketCell.ischeck;
        this.isGift = orgbasketCell.isGift;
        this.goodsShopId = orgbasketCell.goodsShopId;
        this.goodsShopName = orgbasketCell.goodsShopName;
        this.goodsShopAddress = orgbasketCell.goodsShopAddress;
        this.goodsBarCode = orgbasketCell.goodsBarCode;

        ////System.out.println("没有活动了GoodsBasketCell"+discountTopModel);
        this.discountTopModel = orgbasketCell.discountTopModel;
    }

    public void resetGoodsBasketCell(GoodsBasketCell orgbasketCell) {
        this.curGoodsAmount = orgbasketCell.curGoodsAmount;
        this.curGoodsRetailAmount = orgbasketCell.curGoodsRetailAmount;
        this.curGoodsPlusAmount = orgbasketCell.curGoodsPlusAmount;
        this.goodsType = orgbasketCell.goodsType;
        this.goodsMarketingDTO = orgbasketCell.goodsMarketingDTO;
        this.mchId = orgbasketCell.mchId;
        this.goodsId = orgbasketCell.goodsId;
        this.goodsStock = orgbasketCell.goodsStock;
        this.goodsSpecId = orgbasketCell.goodsSpecId;
        this.goodsTitle = orgbasketCell.goodsTitle;
        this.goodsImage = orgbasketCell.goodsImage;
        this.goodsQuantity = (orgbasketCell.goodsQuantity);
        this.shopIdList = orgbasketCell.shopIdList;
        this.ischeck = orgbasketCell.ischeck;
        this.isGift = orgbasketCell.isGift;
        this.goodsShopId = orgbasketCell.goodsShopId;
        this.goodsShopName = orgbasketCell.goodsShopName;
        this.goodsShopAddress = orgbasketCell.goodsShopAddress;
        this.goodsBarCode = orgbasketCell.goodsBarCode;
        this.extraGoodsBasketCell=null;
        this.extraGoodsBasketCellOrg=null;
        ////System.out.println("没有活动了GoodsBasketCell"+discountTopModel);
        this.discountTopModel = orgbasketCell.discountTopModel;
    }

    public String getGoodsShopId() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsShopId();
        }
        if (TextUtils.isEmpty(goodsShopId)) {
            return "";
        }
        return goodsShopId;
    }

    public String getGoodsShopIdWithOrderDelivery(OrderDelivery orderDelivery) {
        if ("2".equals(goodsType)) {
            return getGoodsShopId();
        }
        return goodsShopId;
    }

    public String getGoodsShopIdOrg() {
        return goodsShopId;
    }

    public String getGroupId() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGroupId();
        }
        if (goodsMarketingDTO != null) {
            if ("7".equals(getGoodsMarketingTypeOrg())) {//买送的话 活动要增加一个字段来2次分离
                return "-1";
            }
            if ("6".equals(getGoodsMarketingTypeOrg())) {
                return "-1";
            }
            return goodsMarketingDTO.marketingId;//可能要修改
        }
        return "-1";
    }

    public int getMarkLimitMax() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getMarkLimitMax();
        }
        if (goodsMarketingDTO != null) {
            if(goodsMarketingDTO.salesMax>0){//限购大于库存 那就直接返回库存
                return goodsMarketingDTO.salesMax>getStock()?getStock():goodsMarketingDTO.salesMax;
            }
        }
        return getStock();
    }

    public int getMarkLimitMinNow() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getMarkLimitMinNow();
        }
        if (goodsMarketingDTO != null && goodsMarketingDTO.salesMin > 0) {
            return goodsMarketingDTO.salesMin;
        }
        return 1;
    }

    public int getMarkLimitMin() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getMarkLimitMin();
        }
        if (goodsMarketingDTO != null && goodsMarketingDTO.salesMin > 0) {
            return goodsMarketingDTO.salesMin;
        }
        return 1;
    }

    public int getMarkLimitMinOrg() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getMarkLimitMinOrg();
        }
        if (goodsMarketingDTO != null) {
            return goodsMarketingDTO.salesMin;
        }
        return 0;
    }

    public int getMarkLimitMaxNow() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getMarkLimitMaxNow();
        }
        if (goodsMarketingDTO != null && goodsMarketingDTO.salesMax > 0) {
            if (finishedBuyingGoodsDto != null) {
                return goodsMarketingDTO.salesMax - finishedBuyingGoodsDto.totalQuantity;
            }
            return goodsMarketingDTO.salesMax;
        }
        return 999999;
    }

    public int getHasBuy() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getHasBuy();
        }
        if (finishedBuyingGoodsDto != null) {

            return finishedBuyingGoodsDto.totalQuantity;
        }
        return 0;
    }

    public int getMarkLimitMaxOrg() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getMarkLimitMaxOrg();
        }
        if (goodsMarketingDTO != null) {
            return goodsMarketingDTO.salesMax;
        }
        return 0;
    }

    public int getMarkLimitMaxNowWithInventory() {
//        if (extraGoodsBasketCell != null) {
//            return extraGoodsBasketCell.getMarkLimitMaxNowWithInventory();
//        }
//        if (goodsMarketingDTO != null && goodsMarketingDTO.getAvailableInventory() > getMarkLimitMaxNow()) {//活动库存大于可购就返回可够数量
//
//            return getMarkLimitMaxNow();
//        } else {
//            return goodsMarketingDTO.getAvailableInventory();
//        }
        return getMarkLimitMax();
    }



    public String getGoodsMarketingGoodsSpec() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsMarketingGoodsSpec();
        }
        if (goodsMarketingDTO != null) {//说明是秒杀
            if ("3".equals(goodsMarketingDTO.marketingType)) {
                if (goodsQuantity <= getMarkLimitMaxNowWithInventory() && goodsQuantity >= getMarkLimitMinNow()) {
                    return goodsMarketingDTO.id;
                }
            } else {
                //加判不符合活动要求问题
                if (goodsMarketingDTO.getAvailableInventory() <= 0 && !"1".equals(isSupportOverSold)) {
                    return null;
                }
                if ("6".equals(goodsMarketingDTO.marketingType)) {
                    return null;

                }
                if ("7".equals(goodsMarketingDTO.marketingType)) {
                    return null;
                }
                if ("-4".equals(goodsMarketingDTO.marketingType)||"-5".equals(goodsMarketingDTO.marketingType)) {
                    return null;
                }
                if ("8".equals(goodsMarketingDTO.marketingType) && !SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false)
                        || ("8".equals(goodsMarketingDTO.marketingType) && checkIsAffectWithCoupon()) || ("8".equals(goodsMarketingDTO.marketingType) && SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false) && !isPlusOnly())) {
                    return null;
                }
                return goodsMarketingDTO.id;
            }
        }
        return null;
    }

    public String getGoodsMarketingGoodsSpecOrg() {
//        if (extraGoodsBasketCell != null) {
//            return extraGoodsBasketCell.getGoodsMarketingGoodsSpecOrg();
//        }
        if (goodsMarketingDTO != null) {//说明是秒杀
            if ("-2".equals(getGoodsMarketingTypeParent())) {
                return null;
            }
            return goodsMarketingDTO.id;
        }
        return null;
    }

    public String getRealCurGoodsAmount() {//可能是线下商品 线上平台价是0 导致了出问题
        System.out.println("营销活动商品:" + goodsTitle);
        if (isGift) {
            if (goodsMarketingDTO != null && goodsMarketingDTO.marketingPrice <= 0) {
                return "0";
            }
            return curGoodsRetailAmount + "";
        }
        return curGoodsAmount + "";
    }

    public String getCurGoodsAmount() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getCurGoodsAmount();
        }
        standardPriceType = 1;
//        if (getGoodsQuantity() <= 0) {
//            return "0";
//        }
        if (checkIsAffectWithCoupon()) {//判断价格是不是受到优惠券影响
            return getStandPrice(standardPriceType) + "";
        }
        if (goodsMarketingDTO != null) {//说明是秒杀
            System.out.println("展示活动价格");
            if ("3".equals(getGoodsMarketingTypeOrg())) {
                if (goodsQuantity <= getMarkLimitMaxNowWithInventory() && goodsQuantity >= getMarkLimitMinNow()) {
                    return goodsMarketingDTO.marketingPrice + "";
                }
            } else if ("6".equals(goodsMarketingDTO.marketingType) || "7".equals(goodsMarketingDTO.marketingType)) {
//                if(discountTopModel!=null){
//                    if("7".equals(discountTopModel.marketingType)&&goodsQuantityGiftNeedFixOrg>0){
//                        return getStandPrice(discountTopModel.standardPriceType)+"";
//                    } else if("6".equals(discountTopModel.marketingType)&&groupDiscount>0){
//                        return getStandPrice(discountTopModel.standardPriceType)+"";
//                    }
//                }else {
//
//                }
                return getRealCurGoodsAmount() + "";
            } else if ("1".equals(goodsMarketingDTO.marketingType) || "2".equals(goodsMarketingDTO.marketingType)) {
                return goodsMarketingDTO.marketingPrice + "";
            } else if ("8".equals(goodsMarketingDTO.marketingType)) {
                if (getPlusPrice() > 0) {//如果会员价大于0
                    return getPlusPrice() + "";
                } else {
                    return getRealCurGoodsAmount() + "";
                }
            } else if ("-2".equals(goodsMarketingDTO.marketingType)) {
                return curGoodsAmount + "";
            } else {
                return goodsMarketingDTO.marketingPrice + "";
            }
        }
        if (getPlusPrice() > 0) {//如果会员价大于0
            return getPlusPrice() + "";
        }
        ////System.out.println("没有活动了出问题:"+getGoodsSpecId()+":"+groupDiscount);
        return getRealCurGoodsAmount() + "";
    }


    public double getPlusPriceOrPlatformPrice(){
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getPlusPriceOrPlatformPrice();
        }
        if (getPlusPrice() > 0) {//如果会员价大于0
            return getPlusPrice();
        }
        ////System.out.println("没有活动了出问题:"+getGoodsSpecId()+":"+groupDiscount);
        return curGoodsAmount;
    }


    public String getCurGoodsAmountInBasket() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getCurGoodsAmountInBasket();
        }
        standardPriceType = 1;
        if (getGoodsQuantityInBasket() <= 0) {
            return "0";
        }
        if (checkIsAffectWithCoupon()) {//判断价格是不是受到优惠券影响
            return getStandPrice(standardPriceType) + "";
        }
        if (goodsMarketingDTO != null) {
            //说明是秒杀
            if ("3".equals(getGoodsMarketingTypeOrg())) {
                if (goodsQuantity <= getMarkLimitMaxNowWithInventory() && goodsQuantity >= getMarkLimitMinNow()) {
                    return goodsMarketingDTO.marketingPrice + "";
                }
                return curGoodsAmount + "";//购物车中不在计算秒杀 3.4
            } else if ("6".equals(goodsMarketingDTO.marketingType) || "7".equals(goodsMarketingDTO.marketingType)) {
//                if(discountTopModel!=null){
//                    if("7".equals(discountTopModel.marketingType)&&goodsQuantityGiftNeedFixOrg>0){
//                        return getStandPrice(discountTopModel.standardPriceType)+"";
//                    } else if("6".equals(discountTopModel.marketingType)&&groupDiscount>0){
//                        return getStandPrice(discountTopModel.standardPriceType)+"";
//                    }
//                }else {
//                    return curGoodsAmount+"";
//                }
                return curGoodsAmount + "";
            } else if ("1".equals(goodsMarketingDTO.marketingType) || "2".equals(goodsMarketingDTO.marketingType)) {
                return curGoodsAmount + "";
            } else if ("8".equals(goodsMarketingDTO.marketingType)) {
                if (getPlusPrice() > 0) {//如果会员价大于0
                    return curGoodsAmount + "";
                } else {
                    return curGoodsAmount + "";
                }
            } else if ("-2".equals(goodsMarketingDTO.marketingType)) {
                return curGoodsAmount + "";
            } else {
                return goodsMarketingDTO.marketingPrice + "";
            }
        }
        if (getPlusPrice() > 0) {//如果会员价大于0
            return getPlusPrice() + "";
        }
        ////System.out.println("没有活动了出问题:"+getGoodsSpecId()+":"+groupDiscount);
        return curGoodsAmount + "";
    }


    public int couponstandardPriceType = 1;

    private boolean checkIsAffectWithCoupon() {//判断是不是选中券影响当前价格体系
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.checkIsAffectWithCoupon();
        }
        if (couponInfoAllSelectList == null || couponInfoAllSelectList.size() == 0) {//价格体系不用变
            return false;
        }
        if (couponInfoLeftZList == null || couponInfoLeftZList.size() == 0) {//价格体系不用变
            return false;
        }
        for (int i = 0; i < couponInfoLeftZList.size(); i++) {//不是plus价
            if (ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(couponInfoAllSelectList, new ObjectIteraor<CouponInfoZ>() {
                @Override
                public String getDesObj(CouponInfoZ o) {
                    return o.getCouponId();
                }
            }), couponInfoLeftZList.get(i).getCouponId())) {//判断已选存在可选
                standardPriceType = couponInfoLeftZList.get(i).getCriterionType();
                return true;
            }
        }
        return false;

    }

    public String getGoodsMarketingGoodsId() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsMarketingGoodsId();
        }
        if (goodsMarketingDTO != null) {
            if ("3".equals(getGoodsMarketingTypeOrg())) {
                if (goodsQuantity <= getMarkLimitMaxNowWithInventory() && goodsQuantity >= getMarkLimitMinNow()) {
                    return goodsMarketingDTO.mapMarketingGoodsId + "";
                }
            } else {
                //加判不符合活动要求问题
                if (goodsMarketingDTO.getAvailableInventory() <= 0 && !"1".equals(isSupportOverSold)) {
                    return null;
                }
                if ("6".equals(goodsMarketingDTO.marketingType)) {
                    return null;
                }
                if ("7".equals(goodsMarketingDTO.marketingType)) {
                    return null;
                }
                if ("-4".equals(goodsMarketingDTO.marketingType)||"-5".equals(goodsMarketingDTO.marketingType)) {
                    return null;
                }
                if ("8".equals(goodsMarketingDTO.marketingType) && !SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false)
                        || ("8".equals(goodsMarketingDTO.marketingType) && checkIsAffectWithCoupon()) || ("8".equals(goodsMarketingDTO.marketingType) && SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false) && !isPlusOnly())) {
                    return null;
                }
                return goodsMarketingDTO.mapMarketingGoodsId;
            }
        }
        return null;
    }

    public String getGoodsMarketingGoodsIdOrg() {
//        if (extraGoodsBasketCell != null) {
//            return extraGoodsBasketCell.getGoodsMarketingGoodsIdOrg();
//        }
        if (goodsMarketingDTO != null) {
            if ("-2".equals(getGoodsMarketingTypeParent())) {
                return null;
            }
            return goodsMarketingDTO.mapMarketingGoodsId;
        }
        return null;
    }

    public String getGoodsMarketingType() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsMarketingType();
        }
        if (goodsMarketingDTO != null) {
            if ("3".equals(getGoodsMarketingTypeOrg())) {
                if (goodsQuantity <= getMarkLimitMaxNowWithInventory() && goodsQuantity >= getMarkLimitMinNow()) {
                    return goodsMarketingDTO.marketingType;
                }
            } else {
                //加判不符合活动要求问题
                if (goodsMarketingDTO.getAvailableInventory() <= 0 && !"1".equals(isSupportOverSold)) {
                    return null;
                }
                if ("6".equals(goodsMarketingDTO.marketingType)) {
                    return null;

                }
                if ("7".equals(goodsMarketingDTO.marketingType)) {
                    return null;
                }
                if ("-4".equals(goodsMarketingDTO.marketingType)||"-5".equals(goodsMarketingDTO.marketingType)) {
                    return null;
                }
                if ("8".equals(goodsMarketingDTO.marketingType) && !SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false)
                        || ("8".equals(goodsMarketingDTO.marketingType) && checkIsAffectWithCoupon()) || ("8".equals(goodsMarketingDTO.marketingType) && SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false) && !isPlusOnly())) {
                    return null;
                }
                return goodsMarketingDTO.marketingType;
            }
        }
        return null;
    }

    public String getGoodsMarketingTypeOrg() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsMarketingTypeOrg();
        }
        if (goodsMarketingDTO != null) {
            if(goodsMarketingDTOORG!=null){
                if ("3".equals(goodsMarketingDTOORG.marketingType)) {
                    if (goodsQuantity <= getMarkLimitMaxNowWithInventory() && goodsQuantity >= getMarkLimitMinNow()) {
                        goodsMarketingDTO.marketingType="3";
                        return goodsMarketingDTOORG.marketingType;
                    }
                }
            }
            return goodsMarketingDTO.marketingType;
        }
        return "0";
    }

    public String getGoodsMarketingTypeCanOut() {
        String result=getGoodsMarketingTypeOrg();
        if(result!=null&&result.contains("-")){
            return "";
        }else {
            return result;
        }
    }

    public String getGoodsMarketingTypeParent() {

        if (goodsMarketingDTO != null) {
            return goodsMarketingDTO.marketingType;
        }
        return "0";
    }

    public String getGoodsMarketingTypeOrgExpUnder() {
//        if (extraGoodsBasketCell != null) {
//            return extraGoodsBasketCell.getGoodsMarketingTypeOrg();
//        }
        if (goodsMarketingDTO != null) {
            if ("-2".equals(getGoodsMarketingTypeParent())) {
                return null;
            }
            return goodsMarketingDTO.marketingType;
        }
        return "0";
    }

    public int isValid;

    public double curGoodsAmount;//当前的平台价

    public double curGoodsRetailAmount;//当前的零售价

    public double curGoodsPlusAmount;//当前的Plus价

    public double getPlusPrice() {//获得plus价格 为0就没有嘛
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getPlusPrice();
        }
        if (SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false)) {
            return curGoodsPlusAmount;
        }
        return 0;
    }

    public double goodsPlusAmount;//Plus价

    public double goodsRetailAmount;//零售价

    public double goodsAmount;//平台价

//    public GoodsBasketCell(double curGoodsAmount, double curGoodsRetailAmount,String goodsType) {
//        this.curGoodsAmount = curGoodsAmount;
//        this.curGoodsRetailAmount = curGoodsRetailAmount;
//        this.goodsType=goodsType;
//    }

    public GoodsBasketCell(double curGoodsAmount, double curGoodsRetailAmount, double curGoodsPlusAmount, String goodsType, String isPlusOnly, String isSupportOverSold, String goodsBarCode) {
        this.curGoodsAmount = curGoodsAmount;
        this.curGoodsRetailAmount = curGoodsRetailAmount;
        this.curGoodsPlusAmount = curGoodsPlusAmount;
        this.goodsType = goodsType;
        this.isPlusOnly = isPlusOnly;
        this.isSupportOverSold = isSupportOverSold;
        if(goodsBarCode!=null){
            goodsBarCode=goodsBarCode.trim();
        }
        this.goodsBarCode = goodsBarCode;
    }

    public String goodsTitle;

//    public int getStock() {
//        if (extraGoodsBasketCell != null) {
//            return extraGoodsBasketCell.getStock();
//        }
//        if (isError) {
//            return 0;
//        }
//        if(goodsMarketingDTO!=null){//有活动 则开始判断
//            if(goodsMarketingDTO.availableInventory>0){//活动有库存
//                if ("1".equals(isSupportOverSold)) {//超卖
//                    return goodsMarketingDTO.availableInventory;
//                } else {
//                    return goodsMarketingDTO.availableInventory<goodsStock?goodsMarketingDTO.availableInventory:goodsStock;
//                }
//            }else {
//                return 0;//活动就没库存
//            }
//        }else {
//            if ("1".equals(isSupportOverSold)) {
//                return 999999;
//            } else {
//                return goodsStock;
//            }
//        }
//    }

    public int getStock() {//获得表库存 就是最终用的那个库存 如果时活动就看活动
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getStock();
        }
        if (isError) {
            return 0;
        }
        if(goodsMarketingDTO!=null){//有活动 则开始判断双库存
            if("-2".equals(goodsMarketingDTO.marketingType)
                    ||"-5".equals(goodsMarketingDTO.marketingType)
                    ||"-4".equals(goodsMarketingDTO.marketingType)){// -2线下营销活动 -5直播活动 -4普通抽奖 的库存按照门店库存来
                return getRealStock();
            }
            return goodsMarketingDTO.getAvailableInventory()< getRealStock()?goodsMarketingDTO.getAvailableInventory(): getRealStock();
        }else {
            return getRealStock();
        }
    }


    public int getRealStock() {//获得里库存  门店库存 涉及负库存销售
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getRealStock();
        }
        if (isError) {
            return 0;
        }
        if(goodsStock<0){
            goodsStock=0;
        }
        if ("1".equals(isSupportOverSold)) {
            return 999999;
        } else {
            return goodsStock;
        }
    }

    public int getStockInBasket() {
//        return goodsMarketingDTO!=null?(goodsMarketingDTO.availableInventory<goodsStock?goodsMarketingDTO.availableInventory:goodsStock):goodsStock;
        //会影响限制
        return 999999;
    }

    public int getStockInBasketUnderLine() {
//        return goodsMarketingDTO!=null?(goodsMarketingDTO.availableInventory<goodsStock?goodsMarketingDTO.availableInventory:goodsStock):goodsStock;
        //会影响限制
        return getMarkLimitMax();
    }

    public int goodsStock = 999999;

    public String goodsId;

    public String goodsImage;

    public String getGoodsBarCode() {
        if(goodsBarCode!=null){
            return goodsBarCode.replace(" ","");
        }
        return goodsId;
    }

    public void setGoodsBarCode(String goodsBarCode) {
        this.goodsBarCode = goodsBarCode;
    }

    private String goodsBarCode;//条形码

    public int gChecked;

    public boolean ischeck = false;

    public void setAdditionType(String additionType) {
        this.additionType = additionType;
    }

    private String additionType = "0";

    public String goodsSpecDesc;

    public String goodsCategoryId = "0";//品类id 要取第一个

    public String getGoodsCategoryFirstId() {//这样不就分服务标品了 只按门店了
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsCategoryFirstId();
        }
        if ("1".equals(goodsType)) {
            return TextUtils.isEmpty(goodsCategoryId) ? null : goodsCategoryId.split(",")[0];
        }
        return "-1";
    }

    public String getGoodsCategoryFirstNoId() {//这样不就分服务标品了 只按门店了
//        if(extraGoodsBasketCell!=null){
//            return extraGoodsBasketCell.getGoodsCategoryFirstId();
//        }
//        if("1".equals(goodsType)){
//            return TextUtils.isEmpty(goodsCategoryId)?null:goodsCategoryId.split(",")[0];
//        }
        return "-1";
    }

    public String goodsCategoryName;//品类名称

    public String goodsBrandId;//品牌id

    public String goodsBrandName;//品牌名称


    public String mchId;

    public void setGoodsMarketingDTOORG(GoodsMarketing goodsMarketingDTOORG) {
        if(this.goodsMarketingDTOORG!=null){
            return;
        }
        this.goodsMarketingDTOORG = goodsMarketingDTOORG;
    }

    public GoodsMarketing goodsMarketingDTOORG;//最原始字段
    public GoodsMarketing goodsMarketingDTO;//原始字段
    public MemberBuyedTotal finishedBuyingGoodsDto;

    public int getGoodsMarketingTypeInt() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsMarketingTypeInt();
        }
        if (goodsMarketingDTO != null) {
            if ("3".equals(getGoodsMarketingTypeOrg())) {
                if (goodsQuantity <= getMarkLimitMaxNowWithInventory() && goodsQuantity >= getMarkLimitMinNow()) {

                    return Integer.parseInt(goodsMarketingDTO.marketingType);
                }
            } else {

                return Integer.parseInt(goodsMarketingDTO.marketingType);
            }
        }
        return 0;
    }

    //订单中是否可以修改数量
    public boolean isCanChangeNumInOrder() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.isCanChangeNumInOrder();
        }
        if ("4".equals(getGoodsMarketingTypeOrg())) {
            return false;
        }
        if ("-2".equals(getGoodsMarketingTypeOrg())) {
            return false;
        }
        return true;
    }
    public String getOnlyCouponId(){//增加shop id 先区分到门店
        return goodsShopId+getGoodsMarketingId()+getGoodsBarCode()+goodsTitle+CardNo;
    }
    public String getGoodsMarketingId() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsMarketingId();
        }
        if (goodsMarketingDTO != null) {
            if ("3".equals(getGoodsMarketingTypeOrg())) {
                if (goodsQuantity <= getMarkLimitMaxNowWithInventory() && goodsQuantity >= getMarkLimitMinNow()) {
                    return goodsMarketingDTO.marketingId;
                }
            } else {
                if (goodsMarketingDTO.getAvailableInventory() <= 0 && !"1".equals(isSupportOverSold)) {
                    return null;
                }
                if ("6".equals(goodsMarketingDTO.marketingType)) {
                    return null;

                }
                if ("7".equals(goodsMarketingDTO.marketingType)) {
                    return null;
                }
                if ("-4".equals(goodsMarketingDTO.marketingType)||"-5".equals(goodsMarketingDTO.marketingType)) {
                    return null;
                }
                if ("8".equals(goodsMarketingDTO.marketingType) && !SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false)
                        || ("8".equals(goodsMarketingDTO.marketingType) && checkIsAffectWithCoupon()) || ("8".equals(goodsMarketingDTO.marketingType) && SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false) && !isPlusOnly())) {
                    return null;
                }
                return goodsMarketingDTO.marketingId;
            }
        }
        return null;
    }

    public String getGoodsMarketingIdOrg() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getGoodsMarketingIdOrg();
        }
        if (goodsMarketingDTO != null) {
            return goodsMarketingDTO.marketingId;

        }
        return null;
    }

    public void setIsMaster(int isMaster) {
        this.isMaster = isMaster;
    }

    private int isMaster = 1;

    public String getIsAddition() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getIsAddition();
        }
        if (isMaster == 0) {
            return "1";
        }
        return "0";
    }

    public String getAdditionType() {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getAdditionType();
        }
        return additionType;
    }

    public double checkActDiscount(DiscountTopModel discountTopModel) {//计算获得的优惠 限同品计算方法 marketingPrice 可能要修改
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.checkActDiscount(discountTopModel);
        }
        ////System.out.println("没有活动了checkActDiscount"+discountTopModel);
        this.discountTopModel = discountTopModel;
        return 0;
//        BigDecimal totalDecimal = new BigDecimal(0);
//        if(goodsMarketingDTO!=null&&goodsMarketingDTO.getAvailableInventory()<=0){
//            return 0;
//        }
//        if(goodsMarketingDTO.getAvailableInventory()<getGoodsQuantity()){
//            return 0;
//        }
//        if(discountTopModel==null){
//            return 0;
//        }
//        if(discountTopModel.activityRuleList==null||discountTopModel.activityRuleList.size()==0){
//            return 0;
//        }
//        discountTopModel.extraDiscount="";
//        if(!"6".equals(getGoodsMarketingTypeOrg())){
//            return 0;
//        }
//        if(discountTopModel.getRuleType()==1){//满额度
//            if(discountTopModel.overlying==1){
//                totalDecimal=new BigDecimal(getStandPrice(discountTopModel.standardPriceType)).multiply(new BigDecimal(goodsQuantity));
//                int count=(int)(totalDecimal.doubleValue()/discountTopModel.activityRuleList.get(0).requirement);
//                if(count>0) {//说明是倍数
//                    totalDecimal = new BigDecimal(discountTopModel.activityRuleList.get(0).discountMoney).multiply(new BigDecimal(count));
//                    discountTopModel.groupDiscountRule=discountTopModel.getDisActRulesIndex(0);
//                }else {
//                    totalDecimal=new BigDecimal(0);
//                }
//            }else {
//                int desIndex=-1;
//                totalDecimal=new BigDecimal(getStandPrice(discountTopModel.standardPriceType)).multiply(new BigDecimal(goodsQuantity));
//                for (int i = 0; i <discountTopModel.activityRuleList.size() ; i++) {
//                    if(totalDecimal.doubleValue()>=discountTopModel.activityRuleList.get(i).requirement){//判断满足
//                        if(desIndex==-1){
//                            desIndex=i;
//                        }else {//判断哪个更大说明更符合
//                            desIndex=discountTopModel.activityRuleList.get(i).requirement>discountTopModel.activityRuleList.get(desIndex).requirement?i:desIndex;
//                        }
//                    }
//                }
//                if(desIndex!=-1) {
//                    totalDecimal=new BigDecimal(discountTopModel.activityRuleList.get(desIndex).discountMoney);
//                    discountTopModel.groupDiscountRule=discountTopModel.getDisActRulesIndex(desIndex);
//                }else {
//                    totalDecimal=new BigDecimal(0);
//                    discountTopModel.groupDiscountRule=discountTopModel.getDisActRulesIndex(0);
//                }
//            }
//
//        }else {//满数量
//            if(discountTopModel.overlying==1){
//                int count=(int)(goodsQuantity/discountTopModel.activityRuleList.get(0).requirement);
//                if(count>0) {//说明是倍数
//                    totalDecimal = new BigDecimal(discountTopModel.activityRuleList.get(0).discountMoney).multiply(new BigDecimal(count));
//                    discountTopModel.groupDiscountRule=discountTopModel.getDisActRulesIndex(0);
//                }else {
//                    totalDecimal=new BigDecimal(0);
//                }
//            }else {
//                int desIndex=-1;
//                for (int i = 0; i <discountTopModel.activityRuleList.size() ; i++) {
//                    if(goodsQuantity>=discountTopModel.activityRuleList.get(i).requirement){//判断满足
//                        if(desIndex==-1){
//                            desIndex=i;
//                        }else {//判断哪个更大说明更符合
//                            desIndex=discountTopModel.activityRuleList.get(i).requirement>discountTopModel.activityRuleList.get(desIndex).requirement?i:desIndex;
//                        }
//                    }
//                }
//                if(desIndex!=-1) {
//                    totalDecimal=new BigDecimal(discountTopModel.activityRuleList.get(desIndex).discountMoney);
//                    discountTopModel.groupDiscountRule=discountTopModel.getDisActRulesIndex(desIndex);
//                }else {
//                    totalDecimal=new BigDecimal(0);
//                    discountTopModel.groupDiscountRule=discountTopModel.getDisActRulesIndex(0);
//                }
//            }
//        }
//        groupDiscount=totalDecimal.doubleValue();
//        if(groupDiscount>0){
//            discountTopModel.extraDiscount="已优惠￥"+ FormatUtils.moneyKeep2Decimals(groupDiscount);
////            discountTopModel.groupDiscountRule="";
//        }else {
//            discountTopModel.extraDiscount="";
//            discountTopModel.groupDiscountRule="";
//        }
//        return totalDecimal.doubleValue();
    }

    public List<GoodsBasketCell> checkActGift(DiscountTopModel discountTopModel) {//计算获得的赠品

        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.checkActGift(discountTopModel);
        }
        groupDiscountRule = "";
        goodsQuantityGiftNeedFixOrg = 0;//买送数量
        virtualDiscount = 0;

        this.discountTopModel = discountTopModel;
        List<GoodsBasketCell> result = new ArrayList<>();
        if (discountTopModel == null) {
            ////System.out.println("没有活动了checkActGift2"+goodsTitle);
            return result;
        }
        ////System.out.println("没有活动了checkActGift"+goodsTitle);
        if (discountTopModel.activityRuleList == null || discountTopModel.activityRuleList.size() == 0) {
            return result;
        }
        if (goodsMarketingDTO != null && goodsMarketingDTO.getAvailableInventory() <= 0) {
            return result;
        }
        if (goodsMarketingDTO != null && goodsMarketingDTO.getAvailableInventory() < getGoodsQuantity()) {
            return result;
        }
        if ("7".equals(discountTopModel.marketingType) && "7".equals(getGoodsMarketingTypeOrg())) {
            if (discountTopModel.overlying == 1) {//叠加则只读取第一条规则
                int count = (int) (goodsQuantity / discountTopModel.activityRuleList.get(0).requirement);
                if (count > 0) {//说明是倍数
                    goodsQuantityGiftNeedFixOrg = (int) (count * discountTopModel.activityRuleList.get(0).discountNum);
                    if (goodsQuantityGiftNeedFixOrg + goodsQuantity > goodsMarketingDTO.getAvailableInventory()) {//说明还在范围内 可以支持买送
                        goodsQuantityGiftNeedFixOrg = 0;
                    } else {
                        groupDiscountRule = discountTopModel.getDisActRulesIndex(0);
                        GoodsBasketCell goodsBasketCellGift = new GoodsBasketCell(curGoodsAmount, curGoodsRetailAmount, curGoodsPlusAmount, goodsType, isPlusOnly, isSupportOverSold, goodsBarCode);
                        goodsBasketCellGift.isGift = true;
                        goodsBasketCellGift.isNeedFixOrg = true;
                        goodsBasketCellGift.goodsSpecDesc = goodsSpecDesc;
                        goodsBasketCellGift.mchId = mchId;
                        goodsBasketCellGift.goodsId = goodsId;
                        goodsBasketCellGift.goodsStock = goodsStock;
                        goodsBasketCellGift.goodsSpecId = goodsSpecId;
                        goodsBasketCellGift.goodsTitle = goodsTitle;
                        goodsBasketCellGift.goodsImage = goodsImage;
                        goodsBasketCellGift.goodsQuantity = (int) (count * discountTopModel.activityRuleList.get(0).discountNum);
                        goodsBasketCellGift.shopIdList = shopIdList;
                        goodsBasketCellGift.ischeck = true;
                        goodsBasketCellGift.goodsShopId = goodsShopId;
                        result.add(goodsBasketCellGift);
                    }

                }
            } else {//不叠加则 逐条计算
                int desIndex = -1;
                for (int i = 0; i < discountTopModel.activityRuleList.size(); i++) {
                    if (goodsQuantity >= discountTopModel.activityRuleList.get(i).requirement) {//判断满足
                        if (desIndex == -1) {
                            desIndex = i;
                        } else {//判断哪个更大说明更符合
                            desIndex = discountTopModel.activityRuleList.get(i).requirement > discountTopModel.activityRuleList.get(desIndex).requirement ? i : desIndex;
                        }
                    }
                }
                if (desIndex != -1) {
                    goodsQuantityGiftNeedFixOrg = discountTopModel.activityRuleList.get(desIndex).discountNum;
                    if (goodsQuantityGiftNeedFixOrg + goodsQuantity > goodsMarketingDTO.getAvailableInventory()) {//库存小于0则直接买送出问题了
                        goodsQuantityGiftNeedFixOrg = 0;
                        //买送库存不足
                    } else {
                        groupDiscountRule = discountTopModel.getDisActRulesIndex(desIndex);
                        GoodsBasketCell goodsBasketCellGift = new GoodsBasketCell(curGoodsAmount,
                                curGoodsRetailAmount,
                                curGoodsPlusAmount,
                                goodsType,
                                isPlusOnly,
                                isSupportOverSold,
                                goodsBarCode);
                        goodsBasketCellGift.isGift = true;
                        goodsBasketCellGift.isNeedFixOrg = true;
                        goodsBasketCellGift.goodsSpecDesc = goodsSpecDesc;
                        goodsBasketCellGift.mchId = mchId;
                        goodsBasketCellGift.goodsId = goodsId;
                        goodsBasketCellGift.goodsStock = goodsStock;
                        goodsBasketCellGift.goodsSpecId = goodsSpecId;
                        goodsBasketCellGift.curGoodsAmount = curGoodsAmount;
                        goodsBasketCellGift.goodsTitle = goodsTitle;
                        goodsBasketCellGift.goodsImage = goodsImage;
                        goodsBasketCellGift.goodsQuantity = discountTopModel.activityRuleList.get(desIndex).discountNum;
                        goodsBasketCellGift.shopIdList = shopIdList;
                        goodsBasketCellGift.ischeck = true;
                        goodsBasketCellGift.goodsShopId = goodsShopId;
                        result.add(goodsBasketCellGift);
                    }

                }

            }
        }
        if (goodsQuantityGiftNeedFixOrg > 0) {
            if (goodsMarketingDTO != null && goodsMarketingDTO.getAvailableInventory() < goodsQuantityGiftNeedFixOrg) {//库存小于0则直接买送出问题了
                goodsQuantityGiftNeedFixOrg = 0;
                groupDiscountRule = "";
            } else {
//                groupDiscountRule=discountTopModel.getDisActRulesIndex(0);
                try {
                    virtualDiscount = new BigDecimal(getCurGoodsAmount()).multiply(new BigDecimal(goodsQuantityGiftNeedFixOrg + goodsQuantity)).subtract(new BigDecimal(getCurGoodsAmount()).multiply(new BigDecimal(goodsQuantity))).doubleValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    public List<CouponInfoZ> getNowLeftCouponList(DiscountTopModel discountTopModel) {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getNowLeftCouponList(discountTopModel);
        }
        couponInfoLeftZList.clear();
        if (discountTopModel != null && (goodsQuantityGiftNeedFixOrg > 0 || groupDiscount > 0)) {//判断营销活动
            return couponInfoLeftZList;
        }
        if (goodsMarketingDTO != null && ("3".equals(getGoodsMarketingType()))) {
            return couponInfoLeftZList;
        }
        if (getGoodsQuantity() == 0) {//商品0就别用优惠券了
            return couponInfoLeftZList;
        }

        if (couponInfoZList != null && couponInfoZList.size() > 0) {
            for (int i = 0; i < couponInfoZList.size(); i++) {
                CouponInfoZ coupon = couponInfoZList.get(i);
                BigDecimal money = new BigDecimal(0);
                if (discountTopModel != null && discountTopModel.standardPriceType != coupon.criterionType && (goodsQuantityGiftNeedFixOrg > 0 || groupDiscount > 0)) {

                } else {
                    if (coupon.getRequirement().contains("无门槛")) {//无门槛就不要判断价格了 直接返回
                        couponInfoLeftZList.add(coupon);
                    } else {
                        double limitAmount = Double.parseDouble(coupon.getOverPayment());
                        money = new BigDecimal(getStandPrice(coupon.criterionType)).multiply(new BigDecimal(goodsQuantity));
                        if (limitAmount <= money.doubleValue()) {//满足需求 则视为可以直接用的优惠券
                            couponInfoLeftZList.add(coupon);
                        }
                    }
                }
                if (couponInfoKeyList != null && couponInfoKeyList.size() > 0) {//计算这些额外的券
                    for (int j = 0; j < couponInfoKeyList.size(); j++) {
                        if (couponInfoKeyList.get(j).isCouponIsKey()) {
                            couponInfoLeftZList.add(couponInfoKeyList.get(j));
                        }
                    }
                }

            }
        }
        return couponInfoLeftZList;//返回可用券 可能是重复的
    }

    public double getStandPrice(int standardPriceType) {
        if (extraGoodsBasketCell != null) {
            return extraGoodsBasketCell.getStandPrice(standardPriceType);
        }
        if (standardPriceType == 1) {
            return curGoodsAmount;
        } else {
            return curGoodsRetailAmount;
        }
    }

    public double getGoodsPoint() {
        if (goodsMarketingDTO != null) {
            return goodsMarketingDTO.pointsPrice;
        }
        return 0;
    }

    OnItemChange onItemChange;

    public void setOnItemChange(OnItemChange onItemChange) {
        if (extraGoodsBasketCell != null) {
            extraGoodsBasketCell.setOnItemChange(onItemChange);
        }
        this.onItemChange = onItemChange;
    }

    public void changeItem() {
        if (onItemChange != null) {
            onItemChange.bitNice();
        }
    }

    public String getActEx() {
//        if ("7".equals(getGoodsMarketingTypeOrg())) {
//            return goodsId + goodsSpecId;
//        }
        return null;
    }

    String orggoodsStock;

    public void undogoodsStock() {
        if (orggoodsStock == null) {
            orggoodsStock = goodsStock + "";
        }
        goodsStock = goodsMarketingDTO != null ? (goodsMarketingDTO.getAvailableInventory() < goodsStock ? goodsMarketingDTO.getAvailableInventory() : goodsStock) : goodsStock;
        //会影响限制
        goodsStock = 999999;
    }

    public void redogoodsStock() {
        try {
            goodsStock = Integer.parseInt(orggoodsStock);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public int getCompareId() {
        if (isGift) {
            return 0;
        } else {
            return Integer.parseInt(goodsId);
        }
    }


    @Override
    public int compareTo(GoodsBasketCell o) {
        System.out.println("排序返回:" + (getCompareId() - o.getCompareId()) + "当前排序:" + getCompareId());
        return (int) (o.getCompareId() - getCompareId());
    }

    public interface OnItemChange {
        void bitNice();
    }

    public static class GoodsMarketing {
        public GoodsMarketing(String marketingId) {
            this.marketingId = marketingId;
        }

        public String id;

        public int goodsId;

        public String mapMarketingGoodsId;

        public String marketingId;

        public String marketingType;

        public String startTime;

        public String endTime;

        public String goodsTitle;

        public String goodsSpec;

        public String goodsSpecStr;

        public double storePrice;

        public double platformPrice;

        public double marketingPrice;
        public double pointsPrice;

//        public int maxInventory;

        public int getAvailableInventory() {
            return availableInventory > 0 ? availableInventory : 0;
        }

        public int availableInventory;

        public int salesMax = 999999;

        public int salesMin = 1;
    }

    public class MemberBuyedTotal //购物车中返回的已购买实体信息

    {
        public MemberBuyedTotal() {
        }

        public String sourceId;

        public String memberId;

        public int goodsId;

        public int goodsSpecId;

        public int marketingGoodsId;

        public int marketingGoodsSpecId;

        public int marketingType;

        public int marketingId;

        public int totalQuantity;
    }


}
