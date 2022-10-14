package com.healthy.library.model;

import android.os.Build;
import android.text.TextUtils;

import com.healthy.library.BuildConfig;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.businessutil.ListUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 购物车门店分组
 */
public class GoodsBasketStore implements Serializable {
    public List<GoodsBasketGroup> goodsBasketGroupList = new ArrayList<>();
    public String shopId;
    public int shopType = 2;
    public String goodsCategoryFirstId;
    public String shopName;
    public int checkedId;
    public String backDetail;
    public String msg;
    public int notcheck;
    public String packageMoney;//套餐价
    public String assemblePrice;
    public Object storeBlockChildHolder;
    private boolean checkAll;
    private String deliverType;
    public GoodsShop goodsPickShop;//提货门店
    public GoodsShop orgGoodsPickShop;//提货门店原始
    //    public GoodsShop goodsReceiveShop;//送货门店
//    public GoodsShop orgGoodsReceiveShop;//配送门店原始
    public OrderDelivery deliver = new OrderDelivery();
    public List<GoodsShop> goodsPickShopList = new ArrayList<>();
    //    public List<GoodsShop> goodsReceiveShopList = new ArrayList<>();
    public String[] shopIdList = new String[]{};//指定的shopid备用
    private Collection underCard;
    public String IsChkPopOK = "N";
    public ActVip actVipReq = null;
    public ActVip actVipResult = null;
    public String mchId;

    public void setDepartID(String departID) {
        DepartID = departID;
    }

    public String getDepartID() {
        return DepartID;
    }

    private String DepartID;//有可能异业的会为空哦
    public String appointmentPhone;
    public String merchantType;//商家属性 1异业商家 2合伙人商家
    public boolean feeChange = false;
    public String totalDecimaNoFreeString;//

    public void setUnder(ActVip actVipResult, ActVip actVipReq, String isChkPopOK, String departID) {
        DepartID = departID;
        this.actVipResult = actVipResult;
        this.actVipReq = actVipReq;
        IsChkPopOK = isChkPopOK;
    }

    public double getFee() {
        if ("10".equals(deliver.deliveryType) || "20".equals(deliver.deliveryType)) {
            return 0;
        } else {
            return fee;
        }
    }

    public double fee = 0;//快递费
    public List<CouponInfoZ> couponInfoZList = new ArrayList<>();
    public boolean supportNeedTime = false ;//debug模式 设置提货时间
    public boolean supportOtherdeliveryShop = BuildConfig.DEBUG ? true : false;//debug模式 可以默认切门店
    public boolean supportUseGoodsMondyFree = BuildConfig.DEBUG ? false : true;  //debug的时候用于测试 实际上先行按照支持用商品价格计算邮费
    OnItemChange onItemChange;
    OnItemChange onFeeItemChange;
    OnItemChange onActItemChange;

    public void shopBack() {
        goodsPickShop = null;
        goodsPickShop = orgGoodsPickShop;
//        goodsReceiveShop=null;
//        goodsReceiveShop= orgGoodsReceiveShop;
    }

    public void undo() {//用来变为原来那一套逻辑来获取原始商品方法
//        for (int i = 0; i <goodsBasketGroupList.size() ; i++) {
//            goodsBasketGroupList.get(i).undo();
//        }
    }

    public void redo() {//用来重新增加Extra 然后使得方法拥有extra获得extra逻辑
//        for (int i = 0; i <goodsBasketGroupList.size() ; i++) {
//            goodsBasketGroupList.get(i).redo();
//        }
    }

    public void undogoodsStock() {//用来变为原来那一套逻辑来获取原始商品方法
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            goodsBasketGroupList.get(i).undogoodsStock();
        }
    }

    public void redogoodsStock() {//用来重新增加Extra 然后使得方法拥有extra获得extra逻辑
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            goodsBasketGroupList.get(i).redogoodsStock();
        }
    }

    public void setOnItemChange(OnItemChange onItemChange) {
        this.onItemChange = onItemChange;
    }

    public void setOnFeeChange(OnItemChange onItemChange) {
        this.onFeeItemChange = onItemChange;
    }

    public void setOnStoreActChange(OnItemChange onItemChange) {
        this.onActItemChange = onItemChange;
    }

    public void changeItem() {
        if (onItemChange != null) {

            onItemChange.bitNice();
        }
    }

    public void changeFee() {
        if (onFeeItemChange != null) {
            feeChange = true;
            onFeeItemChange.bitNice();
        }
    }

    public void changeUnderAct() {
        if (onActItemChange != null) {
            onActItemChange.bitNice();
        }
    }

    public String checkPopInfoCountLimit(GoodsBasketCell goodsBasketCell) {
        String result = "";
        result = goodsBasketGroupList.get(0).checkPopInfoCountLimit(goodsBasketCell);
        return result;
    }

    public int getSelectCount() {
        int result = 0;
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            result += goodsBasketGroupList.get(i).getSelectCount();
        }
        return result;
    }

    public int getSelectCountWithNoGift() {
        int result = 0;
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            result += goodsBasketGroupList.get(i).getSelectCountWithNoGift();
        }
        return result;
    }

    public int getSelectLine() {
        int result = 0;
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            result += goodsBasketGroupList.get(i).getSelectLine();
        }
        return result;
    }

    public List<GoodsBasketCell> getUnderCardCanUseNoSelect() {// 获得没有选择的可用优惠券
        List<GoodsBasketCell> result = new ArrayList<>();
        for (int i = 0; i < getGoodsBasketCellList().size(); i++) {
            if (getGoodsBasketCellList().get(i).isCardCanUse && !getGoodsBasketCellList().get(i).isCardSelect) {
                result.add(getGoodsBasketCellList().get(i));
            }
        }
        return result;
    }

    public List<GoodsBasketCell> getUnderIfCardMustSelect() {//获得 如果是优惠券 则必须选择 否则不加入结果 用于提交订单时
        List<GoodsBasketCell> result = new ArrayList<>();
        List<GoodsBasketCell> orglist = getGoodsBasketCellList();
        for (int i = 0; i < orglist.size(); i++) {
            if (orglist.get(i).isGift) {
                if (orglist.get(i).isCardCanUse) {//是优惠券哦
                    if (orglist.get(i).isCardSelect) {
                        result.add(orglist.get(i));
                    }
                } else {
                    result.add(orglist.get(i));
                }
            } else {
                result.add(orglist.get(i));
            }
        }
        return result;
    }

    public List<GoodsBasketCell> getUnderCardCanUse() {//获得可以使用的优惠券 排除掉 赠送的 用户展示可用优惠券
        List<GoodsBasketCell> result = new ArrayList<>();
        for (int i = 0; i < getGoodsBasketCellList().size(); i++) {
            if (getGoodsBasketCellList().get(i).isCardCanUse) {
                result.add(getGoodsBasketCellList().get(i));
            }
        }
        return result;
    }

    public List<GoodsBasketCell> getUnderCardCanUseSelect() {//获得选择的优惠券
        List<GoodsBasketCell> result = new ArrayList<>();

        System.out.println("当前GroupZ2券" + "----------------------------");
        for (int i = 0; i < getGoodsBasketCellList().size(); i++) {
            if (getGoodsBasketCellList().get(i).isCardSelect) {
                System.out.println("当前GroupZ2券" + getGoodsBasketCellList().get(i).goodsTitle);
                result.add(getGoodsBasketCellList().get(i));
            }
        }
        System.out.println("当前GroupZ2券" + "++++++++++++++++++++++++++++");
        System.out.println("获取选择的券数量:" + result.size());
        return result;
    }

    public List<GoodsBasketCell> getUnderGift() {//获得赠品列表
        List<GoodsBasketCell> result = new ArrayList<>();
        for (int i = 0; i < getGoodsBasketCellList().size(); i++) {
            if (getGoodsBasketCellList().get(i).isGift) {
                result.add(getGoodsBasketCellList().get(i));
            }
        }
        return result;
    }

    public void removeUnderGift() {
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            goodsBasketGroupList.get(i).removeUnderGift();
        }
    }

    String cellGoodsMarketingType = "0";

    public List<GoodsBasketCell> getGoodsBasketCellAllExpCardList(String cellGoodsMarketingType) { //获得除了优惠券以外的商品 多用于列表展示
        this.cellGoodsMarketingType = cellGoodsMarketingType;
        List<GoodsBasketCell> result = new ArrayList<>();
        for (int i = 0; i < getGoodsBasketCellList().size(); i++) {
            if (!getGoodsBasketCellList().get(i).isCardCanUse) {
                if (!"0".equals(cellGoodsMarketingType) && !"-2".equals(cellGoodsMarketingType)) {
                    if (!getGoodsBasketCellList().get(i).isGift) {
                        result.add(getGoodsBasketCellList().get(i));
                    }
                } else {
                    result.add(getGoodsBasketCellList().get(i));
                }
            }
        }
        return result;
    }

    public interface OnItemChange {
        void bitNice();
    }


    public <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public List<GoodsShop> buildFlashList(List<GoodsShop> goodsShopList) {//获取门店过滤列表
        List<GoodsShop> result = new ArrayList<>();
        if (getAllShopList().length == 0) {//没有过滤的时候就要小心了 可能goodsShopList 返回了全部门店 建议 只过滤得到标品和综合
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                goodsShopList = ListUtil.where(goodsShopList, new ListUtil.Where<GoodsShop>() {
                    @Override
                    public boolean where(GoodsShop obj) {
                        return ("2".equals(obj.shopType) || "3".equals(obj.shopType));
                    }
                });

            }
        }
        for (int i = 0; i < goodsShopList.size(); i++) {
            GoodsShop goodsShop = goodsShopList.get(i);
            if (checkNowServiceShopIsRealy(goodsShop.shopId, getAllShopList())) {
                result.add(goodsShopList.get(i));
            }//放开可以选择所有门店
//                result.add(goodsShopList.get(i));
        }
        return result;
    }


    private String[] getAllShopList() {
        String[] result = new String[0];
        for (int i = 0; i < getGoodsBasketCellAllList().size(); i++) {
            GoodsBasketCell goodsBasketCell = getGoodsBasketCellAllList().get(i);
            result = concat(result, goodsBasketCell.shopIdList);
        }
        return result;
    }

    private boolean checkNowServiceShopIsRealy(String shopId, String[] shopIdList) {
        if (shopIdList == null || shopIdList.length == 0) {
            return true;
        }
        for (int i = 0; i < shopIdList.length; i++) {
            if (shopId.equals(shopIdList[i])) {
                return true;
            }
        }
        return false;
    }

    public List<OrderGoodsDetail> getDetailsOrg() {
        List<OrderGoodsDetail> details = new ArrayList<>();
        List<GoodsBasketCell> goodsBasketCellList = getGoodsBasketCellAllList();
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            GoodsBasketCell goodsSetCell = goodsBasketCellList.get(i);
            details.add(new OrderGoodsDetail(
                    goodsSetCell.goodsId + "",
                    goodsSetCell.getGoodsSpecId() + "",
                    goodsSetCell.getGoodsQuantity() + "",
                    goodsSetCell.cartDetailId,
                    goodsSetCell.getGoodsMarketingGoodsId(),
                    goodsSetCell.getGoodsMarketingType(),
                    goodsSetCell.getGoodsMarketingGoodsSpec()
            ));
        }
        return details;
    }

    public List<OrderPackageGoodsDetail> getDetails() {
        //System.out.println("计算商品");
        List<OrderPackageGoodsDetail> details = new ArrayList<>();
        List<GoodsBasketCell> goodsBasketCellList = getGoodsBasketCellAllList();
        for (int i = 0; i < goodsBasketCellList.size(); i++) {

            GoodsBasketCell goodsSetCell = goodsBasketCellList.get(i);
            if (goodsSetCell.isGift) {//开始复合回去

            } else {
                if (goodsSetCell.getGoodsQuantity() > 0) {//有数量时才进行价格计算
                    //System.out.println("计算商品OK商品2"+goodsSetCell.goodsTitle);
                    details.add(new OrderPackageGoodsDetail(
                            goodsSetCell.goodsId,
                            goodsSetCell.getGoodsSpecId(),
                            (goodsSetCell.getGoodsQuantity() + goodsSetCell.getGoodsQuantityGiftNeedFixOrg()) + "",
                            goodsSetCell.cartDetailId,
                            goodsSetCell.getGoodsMarketingGoodsId(),
                            goodsSetCell.getGoodsMarketingType(),
                            goodsSetCell.getGoodsMarketingGoodsSpec(),
                            goodsSetCell.getGoodsMarketingId(),
                            goodsSetCell.getGoodsShopIdWithOrderDelivery(deliver),
                            goodsSetCell.getIsAddition(),
                            goodsSetCell.getAdditionType()
                    ));
                } else {
                    details.add(new OrderPackageGoodsDetail(
                            goodsSetCell.goodsId,
                            goodsSetCell.getGoodsSpecId(),
                            (goodsSetCell.getGoodsQuantityInBasket() + ""),
                            goodsSetCell.cartDetailId,
                            goodsSetCell.getGoodsMarketingGoodsId(),
                            goodsSetCell.getGoodsMarketingType(),
                            goodsSetCell.getGoodsMarketingGoodsSpec(),
                            goodsSetCell.getGoodsMarketingId(),
                            goodsSetCell.getGoodsShopIdWithOrderDelivery(deliver),
                            goodsSetCell.getIsAddition(),
                            goodsSetCell.getAdditionType()
                    ));
                    //System.out.println("计算商品出错商品2"+goodsSetCell.goodsTitle);
                }
            }
        }
        Collections.sort(details);
        return details;
    }

    public List<GoodsBasketCell> getGoodsBasketCellList() {//获得全部商品 包括优惠券
        List<GoodsBasketCell> goodsBasketCellList = new ArrayList<>();
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            goodsBasketCellList.addAll(goodsBasketGroupList.get(i).goodsBasketCellList);
        }
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            if(mchId!=null){
                goodsBasketCellList.get(i).mchId=mchId;
            }
        }
        return goodsBasketCellList;
    }

    public List<GoodsBasketCell> getGoodsBasketCellOnlyGoodsList() {//只获得商品 不带赠品玩
        List<GoodsBasketCell> result = new ArrayList<>();
        for (int i = 0; i < getGoodsBasketCellList().size(); i++) {
            if (!getGoodsBasketCellList().get(i).isGift) {
                result.add(getGoodsBasketCellList().get(i));
            }
        }
        return result;
    }

    public List<GoodsBasketCell> getGoodsBasketCellAllList() {//只获得商品 包括赠品玩
        List<GoodsBasketCell> goodsBasketCellAllList = new ArrayList<>();
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            goodsBasketCellAllList.addAll(goodsBasketGroupList.get(i).goodsBasketCellList);
            goodsBasketCellAllList.addAll(goodsBasketGroupList.get(i).goodsBasketCellListGift);
        }
        return goodsBasketCellAllList;
    }

    public List<GoodsBasketCell> getGoodsBasketCellAllListExpFixGift() {//感觉没什么意义 就是获得
        List<GoodsBasketCell> goodsBasketCellAllList = new ArrayList<>();
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            goodsBasketCellAllList.addAll(goodsBasketGroupList.get(i).goodsBasketCellList);
//            goodsBasketCellAllList.addAll(goodsBasketGroupList.get(i).getGoodsBasketCellListGift());
        }
        return goodsBasketCellAllList;
    }


//    public List<GoodsBasketCell> getGoodsBasketCellListGift() {
//         List<GoodsBasketCell> goodsBasketCellListGift=new ArrayList<>();
//        for (int i = 0; i <goodsBasketGroupList.size() ; i++) {
//            goodsBasketCellListGift.addAll(goodsBasketGroupList.get(i).goodsBasketCellListGift);
//        }
//        return goodsBasketCellListGift;
//    }

    public GoodsBasketStore(GoodsBasketGroup goodsBasketCellGroup) {
        this.goodsBasketGroupList.add(goodsBasketCellGroup);
    }

    public boolean isCheckAll() {
        boolean isAllcheck = true;
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroupTmp = goodsBasketGroupList.get(i);
            if (!goodsBasketGroupTmp.isCheckAll()) {//说明选中了
                isAllcheck = false;
            }
        }
        return isAllcheck;
    }

    public String getgCurPrice() {//获得总金额 不包括优惠 且不会算切门店的东西

        if (!TextUtils.isEmpty(packageMoney) && !"null".equals(packageMoney)) {//套餐价不为空
            return packageMoney;
        }
        if (actVipResult != null && ("0".equals(cellGoodsMarketingType) || "-2".equals(cellGoodsMarketingType)) && !"1".equals(merchantType)) {
            return actVipResult.Total;
        }
//        if(!TextUtils.isEmpty(assemblePrice)&&!"null".equals(assemblePrice)){//套餐价不为空
//            return assemblePrice;
//        }
        BigDecimal totalDecimal = new BigDecimal(0);
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroupTmp = goodsBasketGroupList.get(i);
            totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketGroupTmp.getgCurPrice()));
        }
        return totalDecimal.doubleValue() + "";
    }

    public String getgCurPrice(String bargainMoney) {//获得总金额 不包括优惠 且不会算切门店的东西
        if (bargainMoney == null) {
            bargainMoney = "0";
        }

        if (!TextUtils.isEmpty(packageMoney) && !"null".equals(packageMoney)) {//套餐价不为空
            return packageMoney;
        }
        if (actVipResult != null && !"1".equals(merchantType)) {//计算线下营销活动来的价格 actVipResult为挂载在当前门店实体下的一个线下结算数据
            BigDecimal totalDecimalDiscount = new BigDecimal(0);//由actVipResult 计算出来的 单纯优惠 不包含优惠券的
            BigDecimal totalDecimal = new BigDecimal(0);//用途计算运费的结果价格
            BigDecimal totalDecimalQuan = new BigDecimal(0);//优惠券优惠价格
            List<CouponInfoZ> selectInfo = new ArrayList<>();//选择的优惠券
            try {
                selectInfo = new SimpleArrayListBuilder<CouponInfoZ>().putList(getUnderCardCanUseSelect(), new ObjectIteraor<GoodsBasketCell>() {
                    @Override
                    public CouponInfoZ getDesObj(GoodsBasketCell o) {
                        return new CouponInfoZ(o);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < selectInfo.size(); i++) {
                totalDecimalQuan = totalDecimalQuan.add(new BigDecimal(selectInfo.get(i).getCouponDenomination()));
            }
            totalDecimalDiscount = totalDecimalDiscount.add(new BigDecimal(getgCurDiscount()));
            totalDecimal = totalDecimal.add(new BigDecimal(actVipResult.Total)).subtract(totalDecimalDiscount).subtract(totalDecimalQuan);///计算获得 商品线下结算的价格 减去 单纯的优惠价格 减去 优惠券
            if (supportUseGoodsMondyFree) {
                return actVipResult.Total;
            }
            return totalDecimal.doubleValue() + "";
        }
        if (!supportUseGoodsMondyFree) {//不支持原商品 价格计算时
            BigDecimal totalNoFreeDecimal = new BigDecimal(0);//计算25025来的 价格
            try {
                if(totalDecimaNoFreeString!=null){

                    totalNoFreeDecimal = new BigDecimal(totalDecimaNoFreeString);//noFreeMoney 为25025返回之后挂载到 当前门店实体下的数据
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (totalDecimaNoFreeString != null) {//说明有数据
                return totalNoFreeDecimal.doubleValue() + "";
            }
        }
        BigDecimal totalDecimal = new BigDecimal(0);
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroupTmp = goodsBasketGroupList.get(i);
            totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketGroupTmp.getgCurPriceNoService()));
        }
        return totalDecimal.subtract(new BigDecimal(bargainMoney)).doubleValue() + "";
    }

    public String getgCurPriceInBasket() {//获得总金额 不包括优惠 且不会算切门店的东西

        if (!TextUtils.isEmpty(packageMoney) && !"null".equals(packageMoney)) {//套餐价不为空
            return packageMoney;
        }
        if (actVipResult != null && !"1".equals(merchantType)) {
            return actVipResult.Total;
        }
//        if(!TextUtils.isEmpty(assemblePrice)&&!"null".equals(assemblePrice)){//套餐价不为空
//            return assemblePrice;
//        }
        BigDecimal totalDecimal = new BigDecimal(0);
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroupTmp = goodsBasketGroupList.get(i);
            totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketGroupTmp.getgCurPriceInBasket()));
        }
        return totalDecimal.doubleValue() + "";
    }

    public String getCardNoSelectMoney() {//获得没有选择的优惠券
        BigDecimal totalDecimal = new BigDecimal(0);
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroupTmp = goodsBasketGroupList.get(i);
            totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketGroupTmp.getNoSelectCardMoney()));
        }
        return totalDecimal.doubleValue() + "";
    }

    public String getCardMoney() {//获得可使用的优惠券进行全部扣除计算出单纯的立减
        BigDecimal totalDecimal = new BigDecimal(0);
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroupTmp = goodsBasketGroupList.get(i);
            totalDecimal = totalDecimal.add(
                    new BigDecimal(goodsBasketGroupTmp.getCardMoney()));
        }
        return totalDecimal.doubleValue() + "";
    }


    public String getgCurDiscount() {//获得立减

        BigDecimal totalDecimal = new BigDecimal(0);
        if (actVipResult != null) {
            String favTotal = TextUtils.isEmpty(actVipResult.FavTotal) ? "0" : actVipResult.FavTotal;
            totalDecimal = new BigDecimal(favTotal);
            totalDecimal = totalDecimal.add(new BigDecimal(getCardMoney()));
        } else {
            for (int i = 0; i < goodsBasketGroupList.size(); i++) {
                GoodsBasketGroup goodsBasketGroupTmp = goodsBasketGroupList.get(i);
                totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketGroupTmp.groupDiscount));
            }
        }
        return totalDecimal.doubleValue() + "";
    }

    public String getgCurDiscountInBasket() {//获得满减的优惠金额

        BigDecimal totalDecimal = new BigDecimal(0);
        if (actVipResult != null) {
            String favTotal = TextUtils.isEmpty(actVipResult.FavTotal) ? "0" : actVipResult.FavTotal;
            totalDecimal = new BigDecimal(favTotal);
            totalDecimal = totalDecimal.add(new BigDecimal(getCardNoSelectMoney()));
        } else {
            for (int i = 0; i < goodsBasketGroupList.size(); i++) {
                GoodsBasketGroup goodsBasketGroupTmp = goodsBasketGroupList.get(i);
                totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketGroupTmp.groupDiscount));
            }
        }
        return totalDecimal.doubleValue() + "";
    }

    public String getgCurPoint() {

        BigDecimal totalDecimal = new BigDecimal(0);
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroupTmp = goodsBasketGroupList.get(i);
            totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketGroupTmp.getgCurPoint()));
        }
        return totalDecimal.doubleValue() + "";
    }


    public String getMchId() {
        if(mchId!=null){
            return mchId;
        }
        return getGoodsBasketCellList().get(0).mchId;
    }

    public List<CouponInfoZ> getNowLeftCouponList() {
        List<CouponInfoZ> couponInfoLeftZList = new ArrayList<>();
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            couponInfoLeftZList.addAll(goodsBasketGroupList.get(i).getNowLeftCouponList());
        }
        Map<String, CouponInfoZ> clearMap = new HashMap<>();
        List<CouponInfoZ> result = new ArrayList<>();
        for (int i = 0; i < couponInfoLeftZList.size(); i++) {
            clearMap.put(couponInfoLeftZList.get(i).getUseId(), couponInfoLeftZList.get(i));
        }
        Set<Map.Entry<String, CouponInfoZ>> set = clearMap.entrySet();
        // 遍历键值对对象的集合，得到每一个键值对对象
        for (Map.Entry<String, CouponInfoZ> me : set) {
            result.add(me.getValue());
        }
        return couponInfoLeftZList;
    }

    public void checkAct() {
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            goodsBasketGroupList.get(i).checkAct();
        }
    }

    public boolean checkAllIsService() {//判断是不是所有都是服务
        List<GoodsBasketCell> list = getGoodsBasketCellList();
        for (int i = 0; i < list.size(); i++) {
            GoodsBasketCell goodsBasketCell = list.get(i);
            if (!"1".equals(goodsBasketCell.goodsType)) {
                return false;
            }
        }
        return true;
    }
}
