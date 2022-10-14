package com.healthy.library.model;

import android.util.Base64;

import com.healthy.library.LibApplication;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import java.util.List;

public class ActVipOrder {

        public ActVipOrder setVipShop(ActVip.VipShop vipShop) {
                MemberID = new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT));
                DepartID = vipShop.ytbDepartID;
                CardName = SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_NICK);
                CardTel = SpUtils.getValue(LibApplication.getAppContext(), SpKey.PHONE);
                AppID = vipShop.ytbAppId;
                LoginSequence = "7B2978F6";
                return this;
        }

        public ActVipOrder setOnlienInfo(String MchID,String VisitShopId) {
                this.MchID=MchID;
                this.VisitShopId=VisitShopId;
                return this;
        }


        public String Command="pcPreCalcPop";

        public String CardTel;

        public String CardName;

        public String MemberID;

        public String AppID;


        public String DepartID;

        public String MchID;

        public String VisitShopId;

        public String LoginSequence;

        public String Platform="Android";

        public String Source="1";

        public String Race="0";

        public String Type="6";

        public String YtbApi="Y";

        public ActVipOrder setSaleList(List<SaleStore> saleList) {
                SaleList = saleList;
                return this;
        }

        public List<SaleStore> SaleList;

        public ActVipOrder setYYSaleList(List<SaleStore> YYSaleList) {
                this.YYSaleList = YYSaleList;
                return this;
        }

        public List<SaleStore> YYSaleList;

        public static class  SaleStore{
            public DeliverInfo DeliverInfo;
            public String YYMchID;
            public List<ActVip.SaleInfo> SaleInfo;

//                public SaleStore(ActVipOrder.DeliverInfo deliverInfo, List<ActVip.SaleInfo> saleInfo) {
//                        DeliverInfo = deliverInfo;
//                        SaleInfo = saleInfo;
//                }

                public SaleStore(GoodsBasketStore o) {
                        DeliverInfo=new DeliverInfo(o.deliver);
                        SaleInfo=new SimpleArrayListBuilder<ActVip.SaleInfo>().putList(o.getUnderIfCardMustSelect(), new ObjectIteraor<GoodsBasketCell>() {
                                @Override
                                public ActVip.SaleInfo getDesObj(GoodsBasketCell o) {
                                        return new ActVip.SaleInfo(o);
                                }
                        });
                }

                public SaleStore(GoodsBasketStore o,boolean isYY) {//异业构造方法
                        YYMchID=o.getMchId();
                        DeliverInfo=new DeliverInfo(o.deliver);
                        SaleInfo=new SimpleArrayListBuilder<ActVip.SaleInfo>().putList(o.getUnderIfCardMustSelect(), new ObjectIteraor<GoodsBasketCell>() {
                                @Override
                                public ActVip.SaleInfo getDesObj(GoodsBasketCell o) {
                                        return new ActVip.SaleInfo(o,isYY);
                                }
                        });
                }
        }

        public static class  DeliverInfo{

                public String ThDepartID;

                public String DeliveryShopId;

                public String DeliveryType;

                public String DeliveryConsigneeName;

                public String DeliveryConsigneePhone;

                public String DeliveryConsigneeProvince;

                public String DeliveryConsigneeProvinceName;

                public String DeliveryConsigneeCity;

                public String DeliveryConsigneeCityName;

                public String DeliveryConsigneeDistrict;

                public String DeliveryConsigneeDistrictName;

                public String DeliveryConsigneeAddress;

                public String DeliveryLatitude;

                public String DeliveryLongitude;

                public String DeliveryDate;

                public String DeliveryTime;

                public String Remark;

                public DeliverInfo(OrderDelivery deliver) {
                        ThDepartID=deliver.deliveryShopDeptId;
                        DeliveryType=deliver.deliveryType;
                        DeliveryShopId=deliver.deliveryShopId;
                        DeliveryDate=deliver.deliveryDate;
                        DeliveryTime=deliver.deliveryTime;
                        Remark=deliver.remark;
                        if("10".equals(deliver.deliveryType)||"20".equals(deliver.deliveryType)){

                        }else {
                                DeliveryLatitude=deliver.deliveryLatitude;
                                DeliveryLongitude=deliver.deliveryLongitude;
                                DeliveryConsigneeName=deliver.deliveryConsigneeName;
                                DeliveryConsigneePhone=deliver.deliveryConsigneePhone;
                                DeliveryConsigneeProvince=deliver.deliveryConsigneeProvince;
                                DeliveryConsigneeProvinceName=deliver.deliveryConsigneeProvinceName;
                                DeliveryConsigneeCity=deliver.deliveryConsigneeCity;
                                DeliveryConsigneeCityName=deliver.deliveryConsigneeCityName;
                                DeliveryConsigneeDistrict=deliver.deliveryConsigneeDistrict;
                                DeliveryConsigneeDistrictName=deliver.deliveryConsigneeDistrictName;
                                DeliveryConsigneeAddress=deliver.deliveryConsigneeAddress;
                        }
                }
        }

}
