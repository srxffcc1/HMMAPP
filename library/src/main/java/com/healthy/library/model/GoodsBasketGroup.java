package com.healthy.library.model;

import android.text.TextUtils;

import com.healthy.library.utils.FormatUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车活动分组
 */
public class GoodsBasketGroup implements Serializable, Comparable<GoodsBasketGroup> {
    public List<GoodsBasketCell> goodsBasketCellList = new ArrayList<>();
    public String groupId;
    public String shopId;
    public String shopName;
    public String goodsCategoryFirstId;

    public ActVip.PopInfo popInfo;

    public double groupDiscount = 0;//用来记录营销活动优惠

    public void undo() {//用来变为原来那一套逻辑来获取原始商品方法
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            goodsBasketCellList.get(i).undo();
        }
    }

    public void redo() {//用来重新增加Extra 然后使得方法拥有extra获得extra逻辑
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            goodsBasketCellList.get(i).redo();
        }
    }

    private DiscountTopModel discountTopModel;//group的营销活动实体


    public void setDiscountTopModel(DiscountTopModel discountTopModel) {
//        if(discountTopModel==null||discountTopModel.activityRuleList==null||discountTopModel.activityRuleList.size()==0){
//            this.discountTopModel = null;
//            return;
//        }
        this.discountTopModel = discountTopModel;
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            goodsBasketCellList.get(i).setDiscountTopModel(discountTopModel);
        }
    }

    public DiscountTopModel getDiscountTopModel() {
        if (discountTopModel == null) {
            for (int i = 0; i < goodsBasketCellList.size(); i++) {
                if (goodsBasketCellList.get(i).getDiscountTopModel() != null) {
                    this.discountTopModel = goodsBasketCellList.get(i).getDiscountTopModel();
                    return goodsBasketCellList.get(i).getDiscountTopModel();
                }
            }
        }
        return discountTopModel;
    }

    public List<GoodsBasketCell> goodsBasketCellListGift = new ArrayList<>();

    public List<GoodsBasketCell> getGoodsBasketCellListGift() {
        List<GoodsBasketCell> result = new ArrayList<>();
        for (int i = 0; i < goodsBasketCellListGift.size(); i++) {
            if (!goodsBasketCellListGift.get(i).isNeedFixOrg) {
                result.add(goodsBasketCellListGift.get(i));
            }
        }
        return result;
    }

    public List<GoodsBasketCell> getGoodsBasketCellListNoGift() {
        List<GoodsBasketCell> result = new ArrayList<>();
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            if (!goodsBasketCellList.get(i).isGift) {
                result.add(goodsBasketCellList.get(i));
            }
        }
        return result;
    }

    public String marketingType;
    public String shopAddress;
    private boolean checkAll;

    public GoodsBasketGroup(List<GoodsBasketCell> goodsBasketCellList) {
        this.goodsBasketCellList = goodsBasketCellList;
    }

    public GoodsBasketGroup(GoodsBasketCell goodsBasketCell) {
        this.goodsBasketCellList.add(goodsBasketCell);
    }

    public GoodsBasketGroup() {
    }

    public int getGoodsQuantity() {
        int result = 0;
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            if (goodsBasketCellList.get(i).ischeck) {//说明选中了 增加判断库存问题
                result += (goodsBasketCellList.get(i).getGoodsQuantity() > goodsBasketCellList.get(i).goodsMarketingDTO.availableInventory) ? goodsBasketCellList.get(i).goodsMarketingDTO.availableInventory : goodsBasketCellList.get(i).getGoodsQuantity();
            }
        }
        return result;
    }

    public int getGoodsQuantityWithAll() {
        int result = 0;
        for (int i = 0; i < goodsBasketCellList.size(); i++) {

            result += goodsBasketCellList.get(i).getGoodsQuantity();

        }
        return result;
    }

    public double getGoodsAumoutWithAct() {
        BigDecimal totalDecimal = new BigDecimal(0);
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            if (goodsBasketCellList.get(i).ischeck) {
                if ("6".equals(goodsBasketCellList.get(i).getGoodsMarketingTypeOrg()) || "7".equals(goodsBasketCellList.get(i).getGoodsMarketingTypeOrg())) {
                    if (!TextUtils.isEmpty(goodsBasketCellList.get(i).getGoodsMarketingTypeOrg()) && !"0".equals(goodsBasketCellList.get(i).getGoodsMarketingTypeOrg())) {
                        if (goodsBasketCellList.get(i).getGoodsQuantity() > goodsBasketCellList.get(i).getMarkLimitMaxNowWithInventory()) {
                            totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketCellList.get(i).getStandPrice(discountTopModel.standardPriceType)).multiply(new BigDecimal(0)));
                        } else {
                            totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketCellList.get(i).getStandPrice(discountTopModel.standardPriceType)).multiply(new BigDecimal(goodsBasketCellList.get(i).getGoodsQuantity())));
                        }
                    } else {
                        totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketCellList.get(i).getStandPrice(discountTopModel.standardPriceType)).multiply(new BigDecimal(goodsBasketCellList.get(i).getGoodsQuantity())));

                    }
                }

            }
        }
        return totalDecimal.doubleValue();
    }


    public double getGoodsAumoutWithAll() {
        BigDecimal totalDecimal = new BigDecimal(0);
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketCellList.get(i).getStandPrice(discountTopModel.standardPriceType)).multiply(new BigDecimal(goodsBasketCellList.get(i).getGoodsQuantity())));

        }
        return totalDecimal.doubleValue();
    }

    @Override
    public int compareTo(GoodsBasketGroup o) {
        try {
            return (int) (Long.parseLong(o.groupId) - Long.parseLong(groupId));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isCheckAll() {
        boolean isAllcheck = true;
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            GoodsBasketCell goodsBasketCellTmp = goodsBasketCellList.get(i);
            if (!goodsBasketCellTmp.ischeck) {//说明选中了
                isAllcheck = false;
            }
        }
        return isAllcheck;
    }

    public String getgCurPrice() {
        BigDecimal totalDecimal = new BigDecimal(0);
        BigDecimal groupDiscountDecimal = new BigDecimal(groupDiscount);
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            GoodsBasketCell goodsBasketCellTmp = goodsBasketCellList.get(i);
            if (goodsBasketCellTmp.ischeck&&!goodsBasketCellTmp.isGift) {
                totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketCellTmp.getCurGoodsAmount()).multiply(new BigDecimal(goodsBasketCellTmp.getGoodsQuantity())));
                //System.out.println("计算选中cell价格:"+new BigDecimal(goodsBasketCellTmp.getCurGoodsAmount()).multiply(new BigDecimal(goodsBasketCellTmp.getGoodsQuantity())).doubleValue());
            }
        }
        //System.out.println("计算每个Group的价格:"+totalDecimal.doubleValue()+"");
        //System.out.println("计算每个Group的优惠价格:"+groupDiscountDecimal.doubleValue()+"");
        return (totalDecimal.doubleValue()) + "";//减去优惠价
    }

    public String getgCurPriceNoService() {
        BigDecimal totalDecimal = new BigDecimal(0);
        BigDecimal groupDiscountDecimal = new BigDecimal(groupDiscount);
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            GoodsBasketCell goodsBasketCellTmp = goodsBasketCellList.get(i);
            if (goodsBasketCellTmp.ischeck && "2".equals(goodsBasketCellTmp.goodsType) && !goodsBasketCellTmp.isUseCard) {//且不是优惠券
                totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketCellTmp.getCurGoodsAmount()).multiply(new BigDecimal(goodsBasketCellTmp.getGoodsQuantity())));

            }
        }
        return (totalDecimal.doubleValue()) + "";//减去优惠价
    }

    public String getgCurPriceInBasket() {
        BigDecimal totalDecimal = new BigDecimal(0);
        BigDecimal groupDiscountDecimal = new BigDecimal(groupDiscount);
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            GoodsBasketCell goodsBasketCellTmp = goodsBasketCellList.get(i);
            if (goodsBasketCellTmp.ischeck) {
                totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketCellTmp.getCurGoodsAmountInBasket()).multiply(new BigDecimal(goodsBasketCellTmp.getGoodsQuantityInBasket())));
                //System.out.println("计算选中cell价格:"+new BigDecimal(goodsBasketCellTmp.getCurGoodsAmount()).multiply(new BigDecimal(goodsBasketCellTmp.getGoodsQuantity())).doubleValue());
            }
        }
        //System.out.println("计算每个Group的价格:"+totalDecimal.doubleValue()+"");
        //System.out.println("计算每个Group的优惠价格:"+groupDiscountDecimal.doubleValue()+"");
        return (totalDecimal.doubleValue()) + "";//减去优惠价
    }

    public String getNoSelectCardMoney() {
        BigDecimal totalDecimal = new BigDecimal(0);
        BigDecimal groupDiscountDecimal = new BigDecimal(groupDiscount);
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            GoodsBasketCell goodsBasketCellTmp = goodsBasketCellList.get(i);
            if (goodsBasketCellTmp.isUseCard && !goodsBasketCellTmp.isCardSelect) {
                totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketCellTmp.goodsMarketingDTO.marketingPrice)
                        .multiply(new BigDecimal(goodsBasketCellTmp.getGoodsQuantityInBasket()))

                );
            }
        }
        return (totalDecimal.doubleValue()) + "";//减去优惠价
    }

    public String getCardMoney() {
        BigDecimal totalDecimal = new BigDecimal(0);
        BigDecimal groupDiscountDecimal = new BigDecimal(groupDiscount);
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            GoodsBasketCell goodsBasketCellTmp = goodsBasketCellList.get(i);
            if (goodsBasketCellTmp.isCardCanUse) {
                totalDecimal = totalDecimal.add(
                        new BigDecimal(goodsBasketCellTmp.goodsMarketingDTO.marketingPrice)
                                .multiply(new BigDecimal(goodsBasketCellTmp.getGoodsQuantityInBasket()))
                );
            }
        }
        return (totalDecimal.doubleValue()) + "";//减去优惠价
    }

    public String getgCurNoServicePrice() {
        BigDecimal totalDecimal = new BigDecimal(0);
        BigDecimal groupDiscountDecimal = new BigDecimal(groupDiscount);
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            GoodsBasketCell goodsBasketCellTmp = goodsBasketCellList.get(i);
            if (goodsBasketCellTmp.ischeck) {
                totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketCellTmp.getCurGoodsAmount()).multiply(new BigDecimal(goodsBasketCellTmp.getGoodsQuantity())));
                //System.out.println("计算选中cell价格:"+new BigDecimal(goodsBasketCellTmp.getCurGoodsAmount()).multiply(new BigDecimal(goodsBasketCellTmp.getGoodsQuantity())).doubleValue());
            }
        }
        return (totalDecimal.doubleValue()) + "";//减去优惠价
    }

    public String getgCurPoint() {
        BigDecimal totalDecimal = new BigDecimal(0);
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            GoodsBasketCell goodsBasketCellTmp = goodsBasketCellList.get(i);
            if (goodsBasketCellTmp.ischeck) {
                totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketCellTmp.getGoodsPoint()).multiply(new BigDecimal(goodsBasketCellTmp.getGoodsQuantity())));
                //System.out.println("计算选中cellPoint价格:"+new BigDecimal(goodsBasketCellTmp.getGoodsPoint()).multiply(new BigDecimal(goodsBasketCellTmp.getGoodsQuantity())).doubleValue());
            }
        }
        //System.out.println("计算每个Group积分的价格:"+totalDecimal.doubleValue()+"");
        return (totalDecimal.doubleValue()) + "";//减去优惠价
    }


    //检查当前组的活动
    public void checkAct() {
        //System.out.println("没有买送了重新检测活动");

        if (getDiscountTopModel() != null) {//存在营销活动
            if ("6".equals(discountTopModel.marketingType)) {//满减
                //先重置优惠
                for (int i = 0; i < goodsBasketCellList.size(); i++) {
                    GoodsBasketCell goodsBasketCell = goodsBasketCellList.get(i);
                    goodsBasketCell.setGroupDiscount(0);
                }
                BigDecimal totalDecimal = new BigDecimal(0);
                if (discountTopModel.equalGoods == 0) {//不限品类 就要计算总价是不是超过了
                    if (discountTopModel.getRuleType() == 1) {//满额度
                        if (discountTopModel.overlying == 1) {//是否叠加
                            totalDecimal = new BigDecimal(getGoodsAumoutWithAct());//价格
                            int count = (int) (totalDecimal.doubleValue() / discountTopModel.activityRuleList.get(0).requirement);
                            if (count > 0) {//说明是倍数
                                totalDecimal = new BigDecimal(discountTopModel.activityRuleList.get(0).discountMoney).multiply(new BigDecimal(count));
                                discountTopModel.groupDiscountRule = discountTopModel.getDisActRulesIndex(0);
                            } else {
                                totalDecimal = new BigDecimal(0);
                                discountTopModel.groupDiscountRule = discountTopModel.getDisActRulesIndex(0);
                            }
                        } else {
                            totalDecimal = new BigDecimal(getGoodsAumoutWithAct());//价格
                            int desIndex = -1;
                            for (int i = 0; i < discountTopModel.activityRuleList.size(); i++) {
                                if (totalDecimal.doubleValue() >= discountTopModel.activityRuleList.get(i).requirement) {//判断满足
                                    if (desIndex == -1) {
                                        desIndex = i;
                                    } else {//判断哪个更大说明更符合
                                        desIndex = discountTopModel.activityRuleList.get(i).requirement > discountTopModel.activityRuleList.get(desIndex).requirement ? i : desIndex;
                                    }
                                }
                            }
                            if (desIndex != -1) {

                                discountTopModel.groupDiscountRule = discountTopModel.getDisActRulesIndex(desIndex);
                                totalDecimal = new BigDecimal(discountTopModel.activityRuleList.get(desIndex).discountMoney);
                            } else {
                                discountTopModel.groupDiscountRule = discountTopModel.getDisActRulesIndex(0);
                                totalDecimal = new BigDecimal(0);
                            }

                        }
                    } else {//满数量

                        if (discountTopModel.overlying == 1) {
                            int count = (int) (getGoodsQuantity() / discountTopModel.activityRuleList.get(0).requirement);
                            if (count > 0) {//说明是倍数
                                totalDecimal = new BigDecimal(discountTopModel.activityRuleList.get(0).discountMoney).multiply(new BigDecimal(count));
                                discountTopModel.groupDiscountRule = discountTopModel.getDisActRulesIndex(0);
                            }
                        } else {
                            int desIndex = -1;
                            for (int i = 0; i < discountTopModel.activityRuleList.size(); i++) {
                                if (getGoodsQuantity() >= discountTopModel.activityRuleList.get(i).requirement) {//判断满足
                                    if (desIndex == -1) {
                                        desIndex = i;
                                    } else {//判断哪个更大说明更符合
                                        desIndex = discountTopModel.activityRuleList.get(i).requirement > discountTopModel.activityRuleList.get(desIndex).requirement ? i : desIndex;
                                    }
                                }
                            }
                            if (desIndex != -1) {
                                totalDecimal = new BigDecimal(discountTopModel.activityRuleList.get(desIndex).discountMoney);
                                discountTopModel.groupDiscountRule = discountTopModel.getDisActRulesIndex(desIndex);
                            } else {
                                totalDecimal = new BigDecimal(0);
                                discountTopModel.groupDiscountRule = discountTopModel.getDisActRulesIndex(0);
                            }
                        }

                    }
                    //需要对每个商品进行优惠平摊
                    if (totalDecimal.doubleValue() > 0) {//有优惠
                        double allgoodsamount = getGoodsAumoutWithAct();
                        for (int i = 0; i < goodsBasketCellList.size(); i++) {
                            GoodsBasketCell goodsBasketCell = goodsBasketCellList.get(i);
                            double result = (goodsBasketCell.getStandPrice(discountTopModel.standardPriceType) * goodsBasketCell.getGoodsQuantity() / allgoodsamount) * totalDecimal.doubleValue();
                            goodsBasketCell.setGroupDiscount(result > 0 ? result : 0);
                        }
                    }
                    groupDiscount = totalDecimal.doubleValue();
                } else {//
                    for (int i = 0; i < goodsBasketCellList.size(); i++) {
                        GoodsBasketCell goodsBasketCell = goodsBasketCellList.get(i);
                        totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketCell.checkActDiscount(discountTopModel)));
                        if (goodsBasketCell.ischeck) {
                            groupDiscount += new BigDecimal(goodsBasketCell.checkActDiscount(discountTopModel)).doubleValue();
                        }
                    }
                }
                groupDiscount = totalDecimal.doubleValue();


                if (totalDecimal.doubleValue() > 0) {
                    discountTopModel.extraDiscount = "已优惠￥" + FormatUtils.moneyKeep2Decimals(totalDecimal.doubleValue());
//                    discountTopModel.groupDiscountRule="";
                } else {
                    discountTopModel.extraDiscount = "";
                    discountTopModel.groupDiscountRule = "";
                }
            }
            if ("7".equals(discountTopModel.marketingType)) {//买送

                goodsBasketCellListGift.clear();
                for (int i = 0; i < goodsBasketCellList.size(); i++) {
                    GoodsBasketCell goodsBasketCell = goodsBasketCellList.get(i);
                    List<GoodsBasketCell> goodsBasketCellGifts = goodsBasketCell.checkActGift(discountTopModel);
                    goodsBasketCellListGift.addAll(goodsBasketCellGifts);
                }
            }
        } else {
            //重置活动规则
            //System.out.println("没有买送了活动规则重置");
            groupDiscount = 0;
            for (int i = 0; i < goodsBasketCellList.size(); i++) {
                GoodsBasketCell goodsBasketCell = goodsBasketCellList.get(i);
                goodsBasketCell.setGroupDiscount(0);
                goodsBasketCell.setGoodsQuantityGiftNeedFixOrg(0);
                goodsBasketCellListGift.clear();
            }
        }
    }

    public List<CouponInfoZ> getNowLeftCouponList() {
        List<CouponInfoZ> couponInfoLeftZList = new ArrayList<>();
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            couponInfoLeftZList.addAll(goodsBasketCellList.get(i).getNowLeftCouponList(discountTopModel));
        }
//        Map<String,CouponInfoZ> clearMap=new HashMap<>();
//        List<CouponInfoZ> result=new ArrayList<>();
//        for (int i = 0; i <couponInfoLeftZList.size() ; i++) {
//            clearMap.put(couponInfoLeftZList.get(i).getCouponId(),couponInfoLeftZList.get(i));
//        }

        return couponInfoLeftZList;
    }

    public void undogoodsStock() {
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            goodsBasketCellList.get(i).undogoodsStock();
        }
    }

    public void redogoodsStock() {
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            goodsBasketCellList.get(i).redogoodsStock();
        }
    }

    public String checkPopInfoCountLimit(GoodsBasketCell goodsBasketCell) {
        if ("1".equals(popInfo.getSelMode())) {
            int SelMaxNum = Integer.parseInt(popInfo.SelMaxNum);
            int count = 0;
            count = 0;
            for (int i = 0; i < goodsBasketCellList.size(); i++) {
                if (goodsBasketCellList.get(i).ischeck && !goodsBasketCell.getGoodsBarCode().equals(goodsBasketCellList.get(i).getGoodsBarCode())) {
                    count += goodsBasketCellList.get(i).getGoodsQuantity();
                }
            }
            if (count + goodsBasketCell.getGoodsQuantity() > SelMaxNum) {//选择总数超过了

                return "只能选" + SelMaxNum + "个商品";
            }
        } else if ("2".equals(popInfo.getSelMode())) {
            int count = 0;
            for (int i = 0; i < goodsBasketCellList.size(); i++) {
                if (goodsBasketCellList.get(i).ischeck && !goodsBasketCell.getGoodsBarCode().equals(goodsBasketCellList.get(i).getGoodsBarCode())) {
                    count += 1;
                }
            }
            if (count > 0) {//说明一开始没选 那就能继续单选
                return "只能选" + 1 + "件商品";
            }
        } else {

        }

        return "";
    }

    public int getSelectCount() {
        int count = 0;
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            if (goodsBasketCellList.get(i).ischeck) {
                count += goodsBasketCellList.get(i).getGoodsQuantity();
            }
        }
        return count;
    }

    public int getSelectCountWithNoGift() {
        int count = 0;
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            if (goodsBasketCellList.get(i).ischeck&&!goodsBasketCellList.get(i).isGift) {
                count += goodsBasketCellList.get(i).getGoodsQuantity();
            }
        }
        return count;
    }

    public int getSelectLine() {
        int count = 0;
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            if (goodsBasketCellList.get(i).ischeck) {
                count += 1;
            }
        }
        return count;
    }

    public void removeUnderGift() {
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            if (goodsBasketCellList.get(i).isGift) {
                goodsBasketCellList.remove(i);
                i--;
            }
        }
    }
}
