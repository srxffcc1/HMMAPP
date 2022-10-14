package com.healthy.library.model;

import android.text.TextUtils;
import android.util.Base64;

import com.healthy.library.LibApplication;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ActVip {
    private Collection<? extends ActVip.PopInfo> realPopInfo;

    public void checkHistory(ActVip actVipHistory) {//和历史比较进行修复出比较符合预期的选中类目之后以便重新请求
        for (int i = 0; i < SaleInfo.size(); i++) {
            System.out.println("对列表进行构造增加之前:" + SaleInfo.get(i).GoodsName);
        }
        for (int i = 0; i < actVipHistory.SaleInfo.size(); i++) {//修复赠品 修复原始商品增加活动标签
            SaleInfo saleInfo = actVipHistory.SaleInfo.get(i);
            if ("Y".equals(saleInfo.IsZP)) {//说明缓存里是赠品
                int desindex = ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(PopInfo, new ObjectIteraor<PopInfo>() {
                    @Override
                    public Object getDesObj(PopInfo o) {
                        return o.PopNo;
                    }
                }), saleInfo.PopNo);//说明又是可选活动
                if (desindex != -1) {//还要再判 下是不是当前已经也有同赠品且同活动 防止赠品重复加入
                    int desindex2 = ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(SaleInfo, new ObjectIteraor<SaleInfo>() {
                        @Override
                        public Object getDesObj(SaleInfo o) {
                            return o.PopNo + o.getGoodsID();
                        }
                    }), saleInfo.PopNo + saleInfo.getGoodsID());
                    if (desindex2 == -1) {
                        System.out.println("对列表进行构造增加:" + saleInfo.GoodsName);
                        SaleInfo.add(saleInfo);//增加此赠品到购物车
                    }
                }
            } else {
                int desindex = ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(SaleInfo, new ObjectIteraor<SaleInfo>() {
                    @Override
                    public Object getDesObj(SaleInfo o) {
                        return o.PopNo + o.getGoodsID();
                    }
                }), saleInfo.PopNo + saleInfo.getGoodsID());//判断这个不是赠品的缓存数据 是不是需要进行设置活动参数
                if (desindex != -1) {
                    //还原商品挂上活动
                    SaleInfo saleInfoNow = SaleInfo.get(desindex);
                    System.out.println("对列表进行构造修改:" + saleInfoNow.GoodsName);
                    saleInfoNow.GoodsID = saleInfo.getGoodsID();
                    saleInfoNow.Number = saleInfo.Number;
                    saleInfoNow.Price = saleInfo.Price;
                    saleInfoNow.SalePrice = saleInfo.SalePrice;
                    saleInfoNow.DOther = saleInfo.DOther;
                    saleInfoNow.FactPrice = saleInfo.FactPrice;
                    saleInfoNow.IsZP = saleInfo.IsZP;
                    saleInfoNow.PopNo = saleInfo.PopNo;
                    saleInfoNow.GoodsName = saleInfo.GoodsName;
                    System.out.println("对列表进行构造修改结果:" + saleInfo.GoodsName);
                }
            }
        }
        for (int i = 0; i < actVipHistory.PopInfo.size(); i++) {//修复活动已选项
            PopInfo saleInfo = actVipHistory.PopInfo.get(i);
            if ("Y".equals(saleInfo.SelFlag)) {//历史是选中 新请求的也不是不可选 则设置为已选标签
                int desindex = ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(PopInfo, new ObjectIteraor<PopInfo>() {
                    @Override
                    public Object getDesObj(PopInfo o) {
                        return o.PopNo;
                    }
                }), saleInfo.PopNo);
                if (desindex != -1) {
                    PopInfo saleInfoNow = PopInfo.get(desindex);
                    if (!"Y".equals(saleInfoNow.DisFlag)) {
                        saleInfoNow.SelFlag = "Y";
                    }

                }
            }
        }
        for (int i = 0; i < SaleInfo.size(); i++) {
            System.out.println("对列表进行构造增加之后:" + SaleInfo.get(i).GoodsName);
        }
    }

    public void setPopInfoGoods(PopInfo popInfo) {//将选完之后的结果装载给当前结果 进行保存 和 替换掉赠品
        if (!checkHasThisPop(popInfo)) {
            ReCalcPopNo = popInfo.PopNo;
        }
        removeGiftWithPop(popInfo);
        if (popInfo.PopDetail != null) {
            for (int i = 0; i < popInfo.PopDetail.size(); i++) {
                PopDetail popDetail = popInfo.PopDetail.get(i);
                SaleInfo.add(new SaleInfo(popDetail, popInfo));
            }
        }

    }

    boolean isBasketCountChange = false;

    public void buildWithBasket(List<GoodsBasketCell> validList) {//用购物车的内容首次构造自己
        isBasketCountChange = false;
        System.out.println("移除SaleInfo中的重新构造");
        //清空SaleInfo里的error商品 用validList里的商品来代替
        for (int i = 0; i < SaleInfo.size(); i++) {
            if ("Y".equals(SaleInfo.get(i).IsErr)) {
                SaleInfo.remove(i);
                i--;
            }
        }
        if (SaleInfo.size() == 0) {//说明白板进来
            for (int i = 0; i < validList.size(); i++) {
                if (validList.get(i).ischeck) {
                    SaleInfo.add(new SaleInfo(validList.get(i)));
                }
            }
        } else {//说明不是白板进来可能选择过来已经回来一次了
            for (int i = 0; i < validList.size(); i++) {
                if (validList.get(i).ischeck) {//判断购物车的此商品选中了
                    int desindex = ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(SaleInfo, new ObjectIteraor<SaleInfo>() {
                        @Override
                        public Object getDesObj(SaleInfo o) {
                            return o.getGoodsID() + o.IsZP;//查找 原始购物车里的这个商品 在线下购物车中是否存在
                        }
                    }), validList.get(i).getGoodsBarCode() + (validList.get(i).isGift ? "Y" : "N"));//如果条形码相同看看是不是赠品 都不是线上不是赠品线下不是赠品才能修改数量
                    if (desindex == -1) {// 线下购物车没这件商品 同时又是新选中的
                        SaleInfo.add(new SaleInfo(validList.get(i)));
                    } else {//存在但是是赠品
                        ///线下购物车有 但是是赠品
                        if ("Y".equals(SaleInfo.get(desindex).IsZP) && !validList.get(i).isGift) {//线下是赠品但是线上不是赠品
                            if (validList.get(i).ischeck) {
                                SaleInfo.add(new SaleInfo(validList.get(i)));
                            }
                        } else if (!"Y".equals(SaleInfo.get(desindex).IsZP) && !validList.get(i).isGift) {
                            if (validList.get(i).ischeck) {//选中了
                                if (Double.parseDouble(SaleInfo.get(desindex).SalePrice) == validList.get(i).curGoodsAmount) {
                                    System.out.println("修改数量为" + SaleInfo.get(desindex).GoodsName + validList.get(i).getGoodsQuantityInBasket());
                                    int actNumber = Integer.parseInt(SaleInfo.get(desindex).Number);
                                    int basketNumber = validList.get(i).getGoodsQuantityInBasket();
                                    if (actNumber != basketNumber) {
                                        isBasketCountChange = true;
                                    }
                                    SaleInfo.get(desindex).Number = validList.get(i).getGoodsQuantityInBasket() + "";
                                    SaleInfo.get(desindex).MemberCartDetailId = validList.get(i).cartDetailId;
                                    SaleInfo.get(desindex).GoodsName = validList.get(i).goodsTitle;
                                    SaleInfo.get(desindex).filePath = validList.get(i).goodsImage;
                                }
                            }
                        }
                    }
                } else {
                    int desindex = ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(SaleInfo, new ObjectIteraor<SaleInfo>() {
                        @Override
                        public Object getDesObj(SaleInfo o) {
                            return o.getGoodsID() + o.IsZP;//查找 原始购物车里的这个商品 在线下购物车中是否存在
                        }
                    }), validList.get(i).getGoodsBarCode() + "N");//
                    if (desindex != -1) {//
                        SaleInfo.remove(desindex);
                    }
                }
            }
        }

    }

    public List<ActVip.SaleInfo> getGoodsListWithOutGift() {
        List<ActVip.SaleInfo> result = new ArrayList<>();
        for (int i = 0; i < SaleInfo.size(); i++) {
            if (!"Y".equals(SaleInfo.get(i).IsZP)) {//不是赠品的时候加入result
                result.add(SaleInfo.get(i));
            }
        }
        return result;
    }

    public List<ActVip.SaleInfo> getGoodsListWithGift() {
        List<ActVip.SaleInfo> result = new ArrayList<>();
        for (int i = 0; i < SaleInfo.size(); i++) {
            if ("Y".equals(SaleInfo.get(i).IsZP)) {//不是赠品的时候加入result
                if (!"Y".equals(SaleInfo.get(i).IsCardGoods)) {
                    result.add(SaleInfo.get(i));
                }
            }
        }
        return result;
    }

    public void removeGiftWithPop(ActVip.PopInfo popInfo) {
        for (int i = 0; i < SaleInfo.size(); i++) {
            if ("Y".equals(SaleInfo.get(i).IsZP) && popInfo.PopNo.equals(SaleInfo.get(i).PopNo)) {//只能删除赠送的商品
                SaleInfo.remove(i);
                i--;
            }
        }
    }

    public boolean removePop(ActVip.PopInfo popInfo) {//删除活动
        boolean hasRemove = false;
        for (int i = 0; i < SaleInfo.size(); i++) {
            if ("Y".equals(SaleInfo.get(i).IsZP)) {//需要对此礼物进行删除
                if (popInfo.PopNo.equals(SaleInfo.get(i).PopNo)) {
                    SaleInfo.remove(i);
                    i--;
                    hasRemove = true;
                }
            } else {//不是赠品 则修改活动匹配数据就行了
                if (popInfo.PopNo.equals(SaleInfo.get(i).PopNo)) {
                    SaleInfo.get(i).PopNo = "";
                    SaleInfo.get(i).SalePrice = SaleInfo.get(i).SalePrice;
                    hasRemove = true;
                }
            }
        }
        return hasRemove;
    }

    public void addGift(PopDetail popDetail, final ActVip.PopInfo popInfo) {
        for (int i = 0; i < SaleInfo.size(); i++) {
            if ("Y".equals(SaleInfo.get(i).IsZP) &&
                    popInfo.PopNo.equals(SaleInfo.get(i).PopNo) &&
                    popDetail.getGoodsID().equals(SaleInfo.get(i).getGoodsID()) &&
                    popDetail.FactPrice.equals(SaleInfo.get(i).FactPrice)
            ) {//需要对此礼物进行删除
                SaleInfo.remove(i);
                i--;
            }
        }
        popInfo.setCheck(true);
        SaleInfo.add(new SaleInfo(popDetail, popInfo));
    }

    public void removeGift(PopDetail popDetail, final ActVip.PopInfo popInfo) {
        for (int i = 0; i < SaleInfo.size(); i++) {
            if ("Y".equals(SaleInfo.get(i).IsZP) &&
                    popInfo.PopNo.equals(SaleInfo.get(i).PopNo) &&
                    popDetail.getGoodsID().equals(SaleInfo.get(i).getGoodsID()) &&
                    popDetail.FactPrice.equals(SaleInfo.get(i).FactPrice)
            ) {//需要对此礼物进行删除
                SaleInfo.remove(i);
                i--;
            }
        }
    }

    public void setPopDetailFindInSales() {//设置所有detail里的状态 带进去
        //先清理掉所有活动里的商品选中项
        //然后对每个商品去比较和购物车列表中的加过对每个活动的赠品进行勾选
        System.out.println("重新设置不可选31");
        for (int i = 0; i < PopInfo.size(); i++) {
            PopInfo.get(i).setCheckGoods(false);
            System.out.println("重新设置不可选3");
            PopInfo.get(i).setCheckGoods(SaleInfo);
            PopInfo.get(i).setCheckPopDetail(SelPopInfo);
        }

    }

    public ActVip buildWithRecommdPop() {//将推荐的活动加入到当前内容里
        if (PopInfo.size() > 0) {
            PopInfo popInfoRecommend = PopInfo.get(0);
            ReCalcPopNo = popInfoRecommend.PopNo;
            if (popInfoRecommend.PopDetail.size() > 0) {
                SaleInfo.add(new SaleInfo(popInfoRecommend.PopDetail.get(0), popInfoRecommend));
            }
        }
        return this;
    }

    public ActVip sortAndExp(String IsChkPopOKEx) {//进行SaleInfo 的排序
        ActVip actVip = new ActVip();
        actVip.IsDelPop = IsDelPop;
        actVip.ReCalcPopNo = ReCalcPopNo;
        actVip.MemberID = MemberID;
        actVip.DepartID = DepartID;
        actVip.CardName = CardName;
        actVip.CardTel = CardTel;
        actVip.AppID = AppID;
        actVip.Command = Command;
        actVip.LoginSequence = "7B2978F6";
        for (int i = 0; i < SaleInfo.size(); i++) {
            SaleInfo.get(i).RowNo = (i + 1);
        }
        if (IsChkPopOKEx != null) {
            actVip.IsChkPopOK = IsChkPopOKEx;
        } else {
            actVip.IsChkPopOK = IsChkPopOK;
        }
        if (isBasketCountChange) {
            actVip.IsChkPopOK = "R";
        }
        if ("Y".equals(actVip.IsChkPopOK)) {//移动端去掉Y 只能出现S
            actVip.IsChkPopOK = "S";
        }
        actVip.SaleInfo = new ArrayList<>();
        if (SaleInfo != null && SaleInfo.size() > 0) {
            for (int i = 0; i < SaleInfo.size(); i++) {
                SaleInfo saleInfoTmp = new SaleInfo(SaleInfo.get(i));
                actVip.SaleInfo.add(saleInfoTmp);
            }
        }
        if ("R".equals(actVip.IsChkPopOK)) {//如果判断是R那就手动干掉 SaleInfo里的赠品吧 最近接口不太正常 还是先手动处理下吧
            for (int i = 0; i < actVip.SaleInfo.size(); i++) {
                if ("Y".equals(actVip.SaleInfo.get(i).IsZP)) {
                    actVip.SaleInfo.remove(i);
                    i--;
                }
            }
        }

        Collections.sort(actVip.SaleInfo);
        for (int i = 0; i < actVip.SaleInfo.size(); i++) {
            actVip.SaleInfo.get(i).MemberCartDetailId = null;
        }
//        for (int i = 0; i < actVip.SaleInfo.size(); i++) {
//            SaleInfo saleInfo=actVip.SaleInfo.get(i);
//            if(!TextUtils.isEmpty(saleInfo.FactPrice)){
//                saleInfo.SalePrice=saleInfo.FactPrice;
//            }
//        }
        return actVip;
    }

    public ActVip sortAndExpZ(String IsChkPopOKEx) {//进行SaleInfo 的排序
        ActVip actVip = new ActVip();
        actVip.IsDelPop = IsDelPop;
        actVip.ReCalcPopNo = ReCalcPopNo;
        actVip.MemberID = MemberID;
        actVip.DepartID = DepartID;
        actVip.CardName = CardName;
        actVip.CardTel = CardTel;
        actVip.AppID = AppID;
        actVip.Command = Command;
        actVip.LoginSequence = "7B2978F6";
        for (int i = 0; i < SaleInfo.size(); i++) {
            SaleInfo.get(i).RowNo = (i + 1);
        }
        if (IsChkPopOKEx != null) {
            actVip.IsChkPopOK = IsChkPopOKEx;
        } else {
            actVip.IsChkPopOK = IsChkPopOK;
        }
//        if(getSelPopInfo().size()==0){
//            actVip.IsChkPopOK = "N";
//        }
        actVip.SaleInfo = new ArrayList<>();
        if (SaleInfo != null && SaleInfo.size() > 0) {
            for (int i = 0; i < SaleInfo.size(); i++) {
                SaleInfo saleInfoTmp = new SaleInfo(SaleInfo.get(i));
                actVip.SaleInfo.add(saleInfoTmp);

            }
        }
//        for (int i = 0; i < actVip.SaleInfo.size(); i++) {
//            SaleInfo saleInfo=actVip.SaleInfo.get(i);
//            if(!TextUtils.isEmpty(saleInfo.FactPrice)){
//                saleInfo.SalePrice=saleInfo.FactPrice;
//            }
//        }
        return actVip;
    }

    public ActVip setVipShop(VipShop vipShop) {
        try {
            if (vipShop == null) {
                vipShop = ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(), SpKey.VIPSHOP), VipShop.class);
            }
            MemberID = new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT));
            DepartID = vipShop.ytbDepartID;
            CardName = SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_NICK);
            CardTel = SpUtils.getValue(LibApplication.getAppContext(), SpKey.PHONE);
            AppID = vipShop.ytbAppId;
            LoginSequence = "7B2978F6";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void buildPopInfo() {
        for (int i = 0; i < PopInfo.size(); i++) {
            PopInfo.get(i).DepartID = DepartID;
        }
    }

    public boolean checkHasThisPop(ActVip.PopInfo popInfo) {
        if (ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(SaleInfo, new ObjectIteraor<SaleInfo>() {
            @Override
            public String getDesObj(SaleInfo o) {
                return o.PopNo;
            }
        }), popInfo.PopNo) != -1) {
            return true;
        }
        return false;
    }

    public List<PopInfo> getRealPopInfo() {
        List<PopInfo> result = new ArrayList<>();
        for (int i = 0; i < PopInfo.size(); i++) {
            if (!"Y".equals(PopInfo.get(i).DisFlag)) {//外层disFlag得活动不展示了
                result.add(PopInfo.get(i));
            }
        }
        return result;
    }

    public boolean isHasZp() {//判断是不是有营销活动 
        for (int i = 0; i < SaleInfo.size(); i++) {
            long PopNo = 0;
            try {
                PopNo = Long.parseLong(SaleInfo.get(i).PopNo);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (PopNo != 0) {
                return true;
            }
        }
        return false;
    }

    public static class SaleInfo implements Comparable<SaleInfo> {
        public String MemberCartDetailId = "9999999";
        public int RowNo;
        public String IsErr;

        private String GoodsID;

        private String YYGoodsID;

        private String YYGoodsSpecId;

        public String Number;

        public String Price = "0";

        public String SalePrice = "0";

        public String DOther;

        public String FactPrice = "0";

        public String IsZP;

        public String PopNo;

        public String GoodsName;

        public String StuffNo;

        public String erpGoodsID;

        public String IsCardGoods = "N";

        public String filePath;

        public SaleInfo(ActVip.SaleInfo saleInfo) {
            RowNo = saleInfo.RowNo;
            GoodsID = saleInfo.getGoodsID();
            Number = saleInfo.Number;
            if ("0".equals(Number)) {
                Number = "1";
            }
            FactPrice = saleInfo.FactPrice;
            SalePrice = saleInfo.SalePrice;
            Price = saleInfo.Price;
            IsZP = saleInfo.IsZP;
            PopNo = saleInfo.PopNo;
            IsCardGoods = saleInfo.IsCardGoods;
            MemberCartDetailId = null;
        }

        public String getGoodsID() {
//            if(!TextUtils.isEmpty(erpGoodsID)){
//                if("Y".equals(IsZP)){
//                    return erpGoodsID;
//                }
//            }
//            if("Y".equals(IsErr)){
//                return StuffNo;
//            }
            if (GoodsID != null) {
                GoodsID = GoodsID.trim();
            }
            return GoodsID;
        }

        public SaleInfo(PopDetail popDetail, ActVip.PopInfo popInfo) {
            RowNo = popDetail.RowNo;
            GoodsID = popDetail.getGoodsID();
            Number = popDetail.getFirstRealNumber();
            FactPrice = popDetail.FactPrice;
//            SalePrice = popDetail.FactPrice;
//            Price = popDetail.FactPrice;
            IsCardGoods = popDetail.IsCardGoods;
            IsZP = "Y";
            PopNo = popInfo.PopNo;
            GoodsName = popDetail.GoodsName;
        }

        public SaleInfo(GoodsBasketCell goodsBasketCell) {
            GoodsID = goodsBasketCell.getGoodsBarCode();
            Number = goodsBasketCell.getGoodsQuantityInBasket() + "";
            Price = goodsBasketCell.getPlusPriceOrPlatformPrice()+"";
            SalePrice = goodsBasketCell.getPlusPriceOrPlatformPrice() + "";
            FactPrice = goodsBasketCell.getPlusPriceOrPlatformPrice() + "";
            if (goodsBasketCell.goodsMarketingDTO != null) {
                FactPrice = goodsBasketCell.goodsMarketingDTO.marketingPrice + "";
                if("0.0".equals(FactPrice)){
                    FactPrice="0";
                }
            }
            GoodsName = goodsBasketCell.goodsTitle;
            IsCardGoods = "N";
            IsZP = "N";
            PopNo = goodsBasketCell.getGoodsMarketingId();
            if (goodsBasketCell.cartDetailId != null) {
                MemberCartDetailId = goodsBasketCell.cartDetailId;
            }
            if (goodsBasketCell.isGift) {//有奖品
                IsZP = "Y";
                if (goodsBasketCell.isUseCard) {//是券
                    IsCardGoods = "S";
                    if (goodsBasketCell.isCardCanUse && goodsBasketCell.isCardSelect) {//是CardUs
                        IsCardGoods = "Y";
                    }
                }

            }
        }

        public SaleInfo(GoodsBasketCell goodsBasketCell,boolean isYY) {
            YYGoodsID = goodsBasketCell.goodsId;
            Number = goodsBasketCell.getGoodsQuantityInBasket() + "";
            YYGoodsSpecId = goodsBasketCell.getGoodsSpecId();
            if (goodsBasketCell.cartDetailId != null) {
                MemberCartDetailId = goodsBasketCell.cartDetailId;
            }

        }

        @Override
        public int compareTo(ActVip.SaleInfo o) {
            if (MemberCartDetailId == null) {
                MemberCartDetailId = "9999999";
            }
            if (o.MemberCartDetailId == null) {
                o.MemberCartDetailId = "9999999";
            }
            return Integer.parseInt(MemberCartDetailId) - Integer.parseInt(o.MemberCartDetailId);
        }
    }

    public static class VipShop {
        public String area;
        public String mchId;
        public String city;
        public String shopFlag;
        public String mchName;
        public String shopName;
        public String ytbDepartName;
        public String ytbAppId;
        public String shopTag;
        public String shopStatus;
        public String province;
        public String ytbDepartID;
        public String shopId;
        public String departId;
        public String shopType;

        public boolean checkAllIsRight() {//判断所有字段都有效 则使用 不然则使用最新的
            if (!TextUtils.isEmpty(area)
                    && !TextUtils.isEmpty(mchId)
                    && !TextUtils.isEmpty(city)
                    && !TextUtils.isEmpty(shopFlag)
                    && !TextUtils.isEmpty(mchName)
                    && !TextUtils.isEmpty(shopName)
                    && !TextUtils.isEmpty(ytbDepartName)
                    && !TextUtils.isEmpty(ytbAppId)
                    && !TextUtils.isEmpty(shopTag)
                    && !TextUtils.isEmpty(shopStatus)
                    && !TextUtils.isEmpty(province)
                    && !TextUtils.isEmpty(ytbDepartID)
                    && !TextUtils.isEmpty(shopId)
                    && !TextUtils.isEmpty(shopType)
                    && !TextUtils.isEmpty(departId)) {
                return true;

            }
            return false;
        }

        public VipShop(String area, String mchId, String city, String shopFlag, String mchName, String shopName,
                       String ytbDepartName, String ytbAppId, String shopTag, String shopStatus, String province,
                       String ytbDepartID, String shopId, String shopType, String departId) {
            this.area = area;
            this.mchId = mchId;
            this.city = city;
            this.shopFlag = shopFlag;
            this.mchName = mchName;
            this.shopName = shopName;
            this.ytbDepartName = ytbDepartName;
            this.ytbAppId = ytbAppId;
            this.shopTag = shopTag;
            this.shopStatus = shopStatus;
            this.province = province;
            this.ytbDepartID = ytbDepartID;
            this.shopId = shopId;
            this.shopType = shopType;
            this.departId = departId;
        }

        public VipShop() {
        }
    }

    public class SelPopInfo {
        public String PopNo;

        public String PopDesc;

        public String SelFlag;

        public String DisFlag;

        public String GoodsCnt;

        public String SelMaxNum;

        public String MustReCalc;
    }

    public static class PopInfo implements Comparable<PopInfo> {
        public Object extra;

        public String PopNo;

        public String DepartID;

        public String PopDesc;

        public String SelFlag;

        public int getSelFlagInt() {
            return "Y".equals(SelFlag) ? 1 : 0;
        }

        public String DisFlag;

        public String PopLabelName;

//        public String GoodsCnt;无效字段 判断不适用该字段

        public String SelMaxNum;

        public String MustReCalc;
        public boolean isShowContent = false;


        public List<ActVip.PopDetail> getRealPopDetail() {
            List<ActVip.PopDetail> result = new ArrayList<>();
            for (int i = 0; i < PopDetail.size(); i++) {
                if (!"0".equals(PopDetail.get(i).Number)||"1".equals(PopDetail.get(i).SelMode)) {
                    result.add(PopDetail.get(i));
                }
            }
            return result;
        }

        public List<PopDetail> PopDetail = new ArrayList<>();


        public String getSelMode() {
            if (PopDetail.size() > 0) {
                return PopDetail.get(0).getSelMode();
            }
            return null;
        }

        public void setCheck(boolean flag) {
            if (flag) {
                SelFlag = "Y";
            } else {
                SelFlag = "N";
                for (int i = 0; i < PopDetail.size(); i++) {
                    PopDetail.get(i).ischeck = false;
                    System.out.println("重新设置不可选");
                }
            }
        }

        public void setCheckGoods(boolean flag) {
            if (flag) {

            } else {
                for (int i = 0; i < PopDetail.size(); i++) {
                    PopDetail.get(i).ischeck = false;
                    System.out.println("重新设置不可选2");
                }
            }
        }

        public void setCheckGoods(List<ActVip.SaleInfo> saleInfos) {
            for (int i = 0; i < PopDetail.size(); i++) {
                PopDetail popDetail = PopDetail.get(i);
                for (int j = 0; j < saleInfos.size(); j++) {
                    ActVip.SaleInfo saleInfo = saleInfos.get(j);
                    System.out.println("打印设置选中促销");
                    System.out.println(popDetail.getGoodsID()+":"+saleInfo.getGoodsID());
                    System.out.println(PopNo+":"+saleInfo.PopNo);
                    System.out.println(popDetail.FactPrice+":"+saleInfo.FactPrice);
                    if ("Y".equals(saleInfo.IsZP) &&
                            popDetail.getGoodsID().equals(saleInfo.getGoodsID()) &&
                            PopNo.equals(saleInfo.PopNo) &&
                            popDetail.FactPrice.equals(saleInfo.FactPrice)) { //是赠品 且 id 活动价 活动id一致 则认为选中
                        if (popDetail.IsCardGoods != null) {
                            if (popDetail.IsCardGoods.equals(saleInfo.IsCardGoods)) {
                                popDetail.ischeck = true;
                                popDetail.Number = saleInfo.Number;
                                System.out.println("设置活动商品选中:" + popDetail.GoodsName);
                            }
                        } else {
                            popDetail.ischeck = true;
                            popDetail.Number = saleInfo.Number;
                            System.out.println("设置活动商品选中:" + popDetail.GoodsName);
                        }

                    }
                }
            }
        }

        public GoodsBasketStore getBasketStore() {
            GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup();
            goodsBasketGroup.popInfo = this;
            for (int i = 0; i < PopDetail.size(); i++) {
                if (!"0".equals(PopDetail.get(i).Number)||"1".equals(getSelMode())) {
                    GoodsBasketCell goodsBasketCell = PopDetail.get(i).getBasketCell(this);
                    goodsBasketCell.popInfo = this;
                    goodsBasketGroup.goodsBasketCellList.add(goodsBasketCell);
                }
            }
            GoodsBasketStore GoodsBasketStores = new GoodsBasketStore(goodsBasketGroup);
            return GoodsBasketStores;
        }

        public String checkPopInfoCountLimit(ActVip.PopDetail popDetail) {
            if ("1".equals(getSelMode())) {
                int SelMaxNumInt = Integer.parseInt(SelMaxNum);
                int count = 0;
                for (int i = 0; i < PopDetail.size(); i++) {
                    if (PopDetail.get(i).ischeck) {
                        count += Integer.parseInt(PopDetail.get(i).getFirstRealNumber());
                    }
                }
                if (count + Integer.parseInt(popDetail.getFirstRealNumber()) > SelMaxNumInt) {//选择总数超过了
                    return "只能选" + SelMaxNumInt + "个商品";
                }
            } else if ("2".equals(getSelMode())) {
                int count = 0;
                for (int i = 0; i < PopDetail.size(); i++) {
                    if (PopDetail.get(i).ischeck) {
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

        @Override
        public int compareTo(ActVip.PopInfo o) {
            return o.getSelFlagInt() - getSelFlagInt();
        }

        public int getSelectCount() {
            int result = 0;
            for (int i = 0; i < PopDetail.size(); i++) {
                if (PopDetail.get(i).ischeck) {
                    result++;
                }
            }
            return result;
        }

        public void setCheckPopDetail(List<ActVip.PopInfo> selPopInfo) {
            for (int i = 0; i < selPopInfo.size(); i++) {
                if (PopNo.equals(selPopInfo.get(i).PopNo)) {
                    setCheck(true);
                }
            }
        }
    }

    public static class PopDetail implements Comparable<PopDetail> {
        public int RowNo;
        public String IsCardGoods = "N";

        public String getGoodsID() {
            return StuffNo;
        }

//        public String GoodsID;


        public String StuffNo;
        public String GoodsName;
        public String Number;

        public String FactPrice;

        public String filePath;

        public String getSelMode() {
            return SelMode;
        }

        public String SelMode;

        public boolean isIscheck() {
            return ischeck;
        }

        public int getCheckInt() {
            return ischeck ? 1 : 0;
        }

        private boolean ischeck;//需要再一开始就把活动中所有的

        public void setIscheck(boolean ischeck) {
            this.ischeck = ischeck;
        }

        public PopDetail(GoodsBasketCell goodsBasketCell) {//互转
            StuffNo = goodsBasketCell.getGoodsBarCode();
            GoodsName = goodsBasketCell.goodsTitle;
            Number = goodsBasketCell.getGoodsQuantity() + "";
            FactPrice = goodsBasketCell.goodsMarketingDTO.marketingPrice + "";
            if("0.0".equals(FactPrice)){
                FactPrice="0";
            }
        }

        public String getFirstRealNumber(){
            if(Integer.parseInt(Number)==0){
                return "1";
            }
            return Number;
        }

        public GoodsBasketCell getBasketCell(PopInfo popInfo) {//互转
            GoodsBasketCell goodsBasketCell;
            GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(popInfo.PopNo);
            if("1".equals(SelMode)){
                goodsMarketing.availableInventory = Integer.parseInt(popInfo.SelMaxNum);
            }else {
                goodsMarketing.availableInventory = Integer.parseInt(Number);
            }
//                        goodsMarketing.mapMarketingGoodsId = goodsSpecDetail.mapMarketingGoodsId;
            goodsMarketing.marketingType = "-" + SelMode;
            goodsMarketing.id = popInfo.PopNo;
            goodsMarketing.marketingPrice = Double.parseDouble(FactPrice);
            try {
                goodsMarketing.salesMin = Integer.parseInt("1");
                goodsMarketing.salesMax = Integer.parseInt(popInfo.SelMaxNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
            goodsBasketCell = new GoodsBasketCell(0,
                    0,
                    0,
                    "2",
                    "0", "0", StuffNo);
            goodsBasketCell.goodsSpecDesc = "";
            goodsBasketCell.goodsMarketingDTO = goodsMarketing;
            goodsBasketCell.goodsId = "";
            goodsBasketCell.ischeck = ischeck;
            goodsBasketCell.isGift = true;
            goodsBasketCell.isUseCard = false;
            goodsBasketCell.isCardCanUse = false;
            goodsBasketCell.isCardSelect = false;
            try {
                goodsBasketCell.goodsStock = Integer.parseInt(popInfo.SelMaxNum);
                System.out.println("转换大小");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
//                    goodsBasketCell.setGoodsSpecId(saleInfo.getGoodsID());
            goodsBasketCell.setGoodsBarCode(StuffNo);
            if("1".equals(SelMode)){
                goodsBasketCell.setGoodsQuantity(Integer.parseInt("1"));
            }else {
                goodsBasketCell.setGoodsQuantity(Integer.parseInt(Number));
            }
            goodsBasketCell.goodsTitle = GoodsName;
//                    goodsBasketCell.goodsImage = saleInfo.goodsImage;
//            goodsBasketCell.goodsQuantity = Integer.parseInt(Number);
            if("1".equals(SelMode)){
                 goodsBasketCell.goodsQuantity = Integer.parseInt("1");
            }else {
                goodsBasketCell.goodsQuantity = Integer.parseInt(Number);
            }
            return goodsBasketCell;
        }

        @Override
        public int compareTo(PopDetail o) {
            return (getCheckInt() - o.getCheckInt()) * -1;
        }
    }

    public String Command;

    public String OperType;

    public String MemberID;


    public String DepartID;


    public String CardName;

    public String CardTel;

    public String AppID;

    public String LoginSequence;

    public String Total;

    public String FactTotal;

    public String FavTotal;

    public String ReCalcPopNo;

    public String IsDelPop = null;

    public String IsChkPopOK = "N";

    public ActVip() {

    }


    public List<SaleInfo> SaleInfo = new ArrayList<>();

    public List<ActVip.PopInfo> getSelPopInfo() {
        if (PopInfo == null) {
            PopInfo = new ArrayList<>();
        }
        List<PopInfo> result = new ArrayList<>();
        for (int i = 0; i < PopInfo.size(); i++) {
            if ("Y".equals(PopInfo.get(i).SelFlag)) {
                result.add(PopInfo.get(i));
            }
        }
        return result;
    }

    public List<PopInfo> SelPopInfo = new ArrayList<>();
    public List<PopInfo> PopInfo = new ArrayList<>();
}
