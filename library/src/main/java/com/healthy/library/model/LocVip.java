package com.healthy.library.model;

import android.text.TextUtils;

import com.healthy.library.LibApplication;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocVip {

    public List<LocVip.Local> getAllMerchantWithMe(){
        SpUtils.store(LibApplication.getAppContext(), SpKey.HSOT_STATUS,true);
        List<LocVip.Local> localList=new ArrayList<>();
        options=getRealOptions(options);//防止一个市的层级有问题导致去重只剩一层 去重是是市加商户id去重
        if(options!=null&&options.size()>0){//有会员城市 可能存在包含本地 如果没有会员门店可能是全部闭店了
            if(local.isHasShop()&&!isLocalInOpt()){//判断本地有商家 且 不在opt中
//                localList.add(local);//不加本地了 2021/10/06
            }
            localList.addAll(options);
        }else {//没有会员城市
            if(local.isHasShop()){//判断本地有商家
                localList.add(local);
            }else {//没有本地             可能要标记下当前用户是散客 发帖这块要优化下
                SpUtils.store(LibApplication.getAppContext(), SpKey.HSOT_STATUS,false);
                localList.addAll(demonstration);
            }
        }
        //此处还得去重
        localList= ListUtil.flashDuplicateList(localList, new ObjectIteraor<Local>() {
            @Override
            public Object getDesObj(LocVip.Local o) {
                return o.city+o.merchantId;
            }
        });
        Collections.sort(localList);//排序
        if(localList!=null){//可能没取到数据了
            for (int i = 0; i < localList.size(); i++) {
                localList.get(i).complete();
            }
        }

        if(localList==null||localList.size()==0){//可能没取到数据了

        }
        return localList;
    }

    private List<Local> getRealOptions(List<Local> options) {
        List<Local> result=new ArrayList<>();
        Map<String, Local> optRealMap = new HashMap<>();
        if(options!=null){
            for (int i = 0; i <options.size() ; i++) {
                if(optRealMap.get(options.get(i).city+options.get(i).merchantId)!=null){
                    optRealMap.get(options.get(i).city+options.get(i).merchantId).merchantsMap.addAll(options.get(i).getMerchantsMap());
                }else {
                    optRealMap.put(options.get(i).city+options.get(i).merchantId,options.get(i));
                    result.add(options.get(i));
                }
            }
        }

        return result;
    }


    public List<Local.MerchantsShop> getAllMerchantShopWithMe(){
        List<LocVip.Local> localList=getAllMerchantWithMe();
        List<Local.MerchantsShop> localShopList=new ArrayList<>();
        for (int i = 0; i < localList.size(); i++) {
            if(localList.get(i).getMerchantsMap()!=null){
                localShopList.addAll(localList.get(i).getMerchantsMap());
            }
        }
        Collections.sort(localShopList);
        return localShopList;
    }

    private boolean isLocalInOpt() {
        for (int i = 0; i <options.size() ; i++) {
            if(local.city.equals(options.get(i).city)){
                return true;
            }
        }
        return false;
    }


    public List<Local> options;

    public Local local;

    public List<Local> demonstration;//演示站

    public static class Local implements Comparable<Local>{
        public String getNearPartnerNameWithDistrictName() {
            if (merchantsMap == null || merchantsMap.size() == 0) {
                return "";
            } else {
                return getMerchantsMap().get(0).cityName + "·" + getMerchantsMap().get(0).merchantName;
            }
        }
        public String getDistrict() {
            return getMerchantsMap().size() > 0 ? getMerchantsMap().get(0).district : "";
        }
        public String getCityName() {
            if ("市辖区".equals(cityName)) {
                return provinceName.replace("市", "");
            }
            if ("县".equals(cityName)) {
                return provinceName.replace("市", "");
            }
            try {
                return cityName.replace("市", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
        public String getDistrictName() {
            return getMerchantsMap().size() > 0 ? getMerchantsMap().get(0).districtName : "";
        }
        public boolean isHasShop() {
            if (merchantsMap == null || merchantsMap.size() == 0) {
                return false;
            } else {
                return true;
            }
        }
        public List<MerchantsShop> getMerchantsMap() {
            if (merchantsMap != null && merchantsMap.size() > 0) {
                for (int i = 0; i < merchantsMap.size(); i++) {

                }
            }
            Collections.sort(merchantsMap);
            return merchantsMap;
        }
        public MerchantsShop getNearShop() {
            if (merchantsMap == null || merchantsMap.size() == 0) {
                return new MerchantsShop();
            } else {
                return getMerchantsMap().get(0);
            }
        }

        public String getNearShopNameDetail() {
            if (merchantsMap == null || merchantsMap.size() == 0) {
                return "";
            } else {
                return getFormatShopName(getMerchantsMap().get(0).shopName, getMerchantsMap().get(0).addressDetails);
            }
        }
        public String province;

        public String cityName;

        public String districtName;

        public String city;

        public String merchantId;

        public String partnerName;

        public String provinceName;

        public String partnerId;

        private List<MerchantsShop> merchantsMap;

        public static String getFormatShopName(String left, String right) {
            String leftresult = left.substring(0, left.length() > 5 ? 5 : left.length());
            String rightresult = left.substring(left.length() > 3 ? left.length() - 3 : 0, left.length());
            if (left.length() < 8) {
                return left;
            }
            return leftresult + "..." + rightresult;
        }

        @Override
        public int compareTo(Local o) {
            return (int) (getMerchantsMap().get(0).distance - o.getMerchantsMap().get(0).distance);
        }

        public void complete() {
            for (int i = 0; i <merchantsMap.size() ; i++) {
                merchantsMap.get(i).partnerId=partnerId;
            }
            for (int i = 0; i <merchantsMap.size() ; i++) {
                merchantsMap.get(i).partnerName=partnerName;
            }
        }

        public boolean check(String key) {
            if(TextUtils.isEmpty(key)){
                return true;
            }
            if(!TextUtils.isEmpty(partnerName)&&partnerName.contains(key)){
                return true;
            }
            if(!TextUtils.isEmpty(cityName)&&cityName.contains(key)){
                return true;
            }
            if(!TextUtils.isEmpty(provinceName)&&provinceName.contains(key)){
                return true;
            }
            if(!TextUtils.isEmpty(districtName)&&districtName.contains(key)){
                return true;
            }
            for (int i = 0; i < merchantsMap.size(); i++) {
                if(merchantsMap.get(i).check(key)){
                    return true;
                }
            }
            return false;
        }

        public class MerchantsShop implements Comparable<MerchantsShop> {

            public String province;

            public String cityName;

            public String districtName;

            public int distance;

            public String city;

            public String merchantId;

            public String district;

            public String shopName;

            public String addressDetails;

            public String provinceName;

            public String shopId;

            public String merchantName;

            public String partnerName;

            public String ytbAppId;

            public String ytbDepartID;

            public String partnerId;

            public String departId;

            /**
             * 商家品牌名称
             */
            private String merchantBrand;

            /**
             * 商家品牌Logo
             */
            private String merchantLogoUrl;

            public String getCityName() {
                if ("市辖区".equals(cityName)) {
                    return provinceName.replace("市", "");
                }
                if ("县".equals(cityName)) {
                    return provinceName.replace("市", "");
                }
                return cityName.replace("市", "");
            }

            @Override
            public String toString() {
                return "MerchantsShop{" +
                        "province='" + province + '\'' +
                        ", cityName='" + cityName + '\'' +
                        ", districtName='" + districtName + '\'' +
                        ", distance=" + distance +
                        ", city='" + city + '\'' +
                        ", merchantId='" + merchantId + '\'' +
                        ", district='" + district + '\'' +
                        ", shopName='" + shopName + '\'' +
                        ", addressDetails='" + addressDetails + '\'' +
                        ", provinceName='" + provinceName + '\'' +
                        ", shopId='" + shopId + '\'' +
                        ", merchantName='" + merchantName + '\'' +
                        ", ytbAppId='" + ytbAppId + '\'' +
                        ", ytbDepartID='" + ytbDepartID + '\'' +
                        ", merchantBrand='" + merchantBrand + '\'' +
                        ", merchantLogoUrl='" + merchantLogoUrl + '\'' +
                        '}';
            }


            public String getMerchantBrand() {
                if(merchantBrand!=null){
                    merchantBrand=merchantBrand.replace("null","");
                }
                return merchantBrand;
            }

            public void setMerchantBrand(String merchantBrand) {
                this.merchantBrand = merchantBrand;
            }

            public String getMerchantLogoUrl() {
                return merchantLogoUrl;
            }

            public void setMerchantLogoUrl(String merchantLogoUrl) {
                this.merchantLogoUrl = merchantLogoUrl;
            }

            public String getAddressDetails() {//省市区编码
                return (provinceName + cityName + districtName + addressDetails).replaceAll("null", "");
            }
            public String getShopNameDetailToShowOnShopMainUI() {
                System.out.println("选择的:"+shopName+"|"+addressDetails);
                return getFormatShopName(shopName, addressDetails);

            }
            @Override
            public int compareTo(MerchantsShop o) {
                return (int) (distance - o.distance);
            }


            public boolean check(String key) {
                if(TextUtils.isEmpty(key)){
                    return true;
                }
                if(!TextUtils.isEmpty(partnerName)&&partnerName.contains(key)){
                    return true;
                }
                if(!TextUtils.isEmpty(cityName)&&cityName.contains(key)){
                    return true;
                }
                if(!TextUtils.isEmpty(provinceName)&&provinceName.contains(key)){
                    return true;
                }
                if(!TextUtils.isEmpty(districtName)&&districtName.contains(key)){
                    return true;
                }
                if(!TextUtils.isEmpty(shopName)&&shopName.contains(key)){
                    return true;
                }
                if(!TextUtils.isEmpty(addressDetails)&&addressDetails.contains(key)){
                    return true;
                }
                return false;
            }
        }
    }
}
