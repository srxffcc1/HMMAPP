package com.healthy.library.businessutil;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.LibApplication;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.AMapLocationBd;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.LocVip;
import com.healthy.library.utils.GetJuLiUtils;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class LocUtil {

    public static void storeLocation(final Context context, final AMapLocation aMapLocation) {
        if (!TextUtils.isEmpty(SpUtils.getValue(context, SpKey.LOC_TEST)) && ("check".equals(ChannelUtil.getChannel(null)) || "debug".equals(ChannelUtil.getChannel(null)))) {
            //拦截定位问题
            SpUtils.store(context, SpKey.LOC_ORG, SpUtils.getValue(context, SpKey.LOC_TEST));
            return;
        }
        double distance = GetJuLiUtils.getDistance(aMapLocation.getLongitude() + "", aMapLocation.getLatitude() + "", "102.63098225662232", "37.90386966395889");
        if (distance <= 750) {//武威特殊经纬度处理
            aMapLocation.setAdCode("620624");//设置新的区编码
        }
        SpUtils.store(context, SpKey.LOC_TEST, "");
        if (TextUtils.isEmpty(aMapLocation.getAdCode())) {//追加判断是不是因为节电问题导致无法获得详细的地区编码
            LatLonPoint point = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);

            GeocodeSearch geocoderSearch = new GeocodeSearch(context);
            geocoderSearch.getFromLocationAsyn(query);
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    String adCode = regeocodeResult.getRegeocodeAddress().getAdCode();
                    aMapLocation.setAdCode(adCode);
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                    String result = null;
                    try {
                        result = new Gson().toJson(new AMapLocationBd(aMapLocation));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SpUtils.store(context, SpKey.LOC_ORG, result);
                }
            });

        } else {
            String result = null;
            try {
                result = new Gson().toJson(new AMapLocationBd(aMapLocation));
            } catch (Exception e) {
                e.printStackTrace();
            }
            SpUtils.store(context, SpKey.LOC_ORG, result);
        }


    }

    public static void storeLocationChose(final Context context, final AMapLocation aMapLocation) {

        if (TextUtils.isEmpty(aMapLocation.getAdCode())) {//追加判断是不是因为节电问题导致无法获得详细的地区编码
            LatLonPoint point = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);

            GeocodeSearch geocoderSearch = new GeocodeSearch(context);
            geocoderSearch.getFromLocationAsyn(query);
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    String adCode = regeocodeResult.getRegeocodeAddress().getAdCode();
                    aMapLocation.setAdCode(adCode);
                    String result = null;
                    try {
                        result = new Gson().toJson(new AMapLocationBd(aMapLocation));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SpUtils.store(context, SpKey.LOC_CHOSE, result);
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                }
            });
        } else {
            String result = null;
            try {
                result = new Gson().toJson(new AMapLocationBd(aMapLocation));
            } catch (Exception e) {
                e.printStackTrace();
            }
            SpUtils.store(context, SpKey.LOC_CHOSE, result);
        }


    }

    public static String getAreaNameIfError(Context context, String key) {

        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String jsonstring = SpUtils.getValue(context, key);
            AMapLocationBd aMapLocation = resolveLocData(jsonstring);
            if ("市辖区".equals(aMapLocation.getDistrict())) {
                return getCityName(context, key);
            }
            return aMapLocation.getDistrict();
        }
        return null;
    }

    public static String getAreaName(Context context, String key) {

        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String jsonstring = SpUtils.getValue(context, key);
            AMapLocationBd aMapLocation = resolveLocData(jsonstring);
            return aMapLocation.getDistrict();
        }
        return null;
    }

    public static String getLocationAddress(Context context, String key) {

        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String jsonstring = SpUtils.getValue(context, key);
            AMapLocationBd aMapLocation = resolveLocData(jsonstring);
            return aMapLocation.getAddress();
        }
        return null;
    }

    public static AMapLocation getLocationBean(Context context, String key) {

        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String jsonstring = SpUtils.getValue(context, key);
            AMapLocationBd aMapLocationbd = resolveLocData(jsonstring);
            AMapLocation aMapLocation = aMapLocationbd.build();

            return aMapLocation;
        }
        return null;
    }

    public static String getCityName(Context context, String key) {

        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String jsonstring = SpUtils.getValue(context, key);
            AMapLocationBd aMapLocation = resolveLocData(jsonstring);
            if ("市辖区".equals(aMapLocation.getCity())) {
                return aMapLocation.getProvince().replace("市", "");
            }
            return aMapLocation.getCity().replace("市", "");
        }
        return "";
    }//获得区的名字 其实不是获得城市名字

    public static String getCityNameWithLimlt(Context context, String key,int limit) {

        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String jsonstring = SpUtils.getValue(context, key);
            AMapLocationBd aMapLocation = resolveLocData(jsonstring);
            if ("市辖区".equals(aMapLocation.getCity())) {
                return getLimitString(aMapLocation.getProvince().replace("市", ""),limit);
            }
            return getLimitString(aMapLocation.getCity().replace("市", ""),limit);
        }
        return "";
    }//获得区的名字 其实不是获得城市名字

    public static String getLimitString(String value,int limit){
        if(value!=null&&value.length()>limit){
         return value.substring(0,limit)+"...";
        }else {
            return value;
        }
    }

    public static String getCityNameOld(Context context, String key) {

        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String jsonstring = SpUtils.getValue(context, key);
            AMapLocationBd aMapLocation = resolveLocData(jsonstring);
            return aMapLocation.getDistrict();
        }
        return "";
    }

    public static String getProvinceName(Context context, String key) {

        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String jsonstring = SpUtils.getValue(context, key);
            AMapLocationBd aMapLocation = resolveLocData(jsonstring);
            return aMapLocation.getProvince();
        }
        return "";
    }

    public static String getAreaNo(Context context, String key) {

        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String jsonstring = SpUtils.getValue(context, key);
            AMapLocationBd aMapLocation = resolveLocData(jsonstring);
            return aMapLocation.getAdCode();
        }
        return "";
    }

    public static String getCityNo(Context context, String key) {
        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String areaNo = getAreaNo(context, key);

            try {
                return (Integer.parseInt(areaNo) / 100 * 100) + "";
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getProvinceNo(Context context, String key) {
        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String areaNo = getAreaNo(context, key);
            try {
                return (Integer.parseInt(areaNo) / 10000 * 10000) + "";
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getLatitude(Context context, String key) {
        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String jsonstring = SpUtils.getValue(context, key);
            AMapLocationBd aMapLocation = resolveLocData(jsonstring);
            return aMapLocation.getLatitude() + "";
        }
        return "0";
    }

    public static String getLongitude(Context context, String key) {
        if (!TextUtils.isEmpty(SpUtils.getValue(context, key))) {
            String jsonstring = SpUtils.getValue(context, key);
            AMapLocationBd aMapLocation = resolveLocData(jsonstring);
            return aMapLocation.getLongitude() + "";
        }
        return "0";
    }

    private static AMapLocationBd resolveLocData(String obj) {
        AMapLocationBd result = new AMapLocationBd();
        try {
            JSONObject data = new JSONObject(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<AMapLocationBd>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void setNowShop(LocVip.Local.MerchantsShop nowShop) {
        SpUtils.store(LibApplication.getAppContext(), SpKey.CITYNAMEPARTNERNAME, new String(nowShop.getCityName() + "·" + nowShop.partnerName));
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_MC, nowShop.merchantId);
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOP, nowShop.shopId);
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOPNAME, nowShop.shopName);
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_PARTNERID, nowShop.partnerId);
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOPNAMEDETAIL, nowShop.getShopNameDetailToShowOnShopMainUI());
        System.out.println("选择的" + nowShop.getShopNameDetailToShowOnShopMainUI());
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOPADDRESS, nowShop.getAddressDetails());
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOPDISTANCE, nowShop.distance + "");
        SpUtils.store(LibApplication.getAppContext(), SpKey.SHOP_BRAND, nowShop.getMerchantBrand());
        ActVip.VipShop vipShop = new ActVip.VipShop(nowShop.district,
                nowShop.merchantId,
                nowShop.city,
                "2",
                nowShop.merchantName,
                nowShop.shopName,
                "",
                nowShop.ytbAppId,
                "1",
                "1",
                nowShop.province,
                nowShop.ytbDepartID,
                nowShop.shopId,
                "2",
                nowShop.departId
        );
        try {
            SpUtils.store(LibApplication.getAppContext(), SpKey.VIPSHOP, new Gson().toJson(vipShop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearNowShop() {
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_MC, null);
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOP, null);
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOPNAME, null);
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOPNAMEDETAIL, null);
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOPADDRESS, null);
        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOPDISTANCE, null);
        SpUtils.store(LibApplication.getAppContext(), SpKey.SHOP_BRAND, null);
        SpUtils.store(LibApplication.getAppContext(),"fun100001",null);
        try {
            SpUtils.store(LibApplication.getAppContext(), SpKey.VIPSHOP, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getytbAppId() {
        String mVipShop = SpUtils.getValue(LibApplication.getAppContext(), SpKey.VIPSHOP);
        ActVip.VipShop vipShop = null;
        if (!TextUtils.isEmpty(mVipShop)) {
            vipShop = ObjUtil.getObj(mVipShop, ActVip.VipShop.class);
        }
        if (vipShop != null) {
            return vipShop.ytbAppId;
        } else {
            return null;
        }
    }

    public static String getNormalMerchantName() {
        String mVipShop = SpUtils.getValue(LibApplication.getAppContext(), SpKey.VIPSHOP);
        ActVip.VipShop vipShop = null;
        if (!TextUtils.isEmpty(mVipShop)) {
            vipShop = ObjUtil.getObj(mVipShop, ActVip.VipShop.class);
        }
        if (vipShop != null) {
            return vipShop.mchName;
        } else {
            return null;
        }
    }

    public static String getUserId() {
        String mVipShop = SpUtils.getValue(LibApplication.getAppContext(), SpKey.VIPSHOP);
        ActVip.VipShop vipShop = null;
        if (!TextUtils.isEmpty(mVipShop)) {
            vipShop = ObjUtil.getObj(mVipShop, ActVip.VipShop.class);
        }
        if (vipShop != null) {
            return vipShop.mchId;
        } else {
            return null;
        }
    }



    public static String getYTbDepartId() {
        String mVipShop = SpUtils.getValue(LibApplication.getAppContext(), SpKey.VIPSHOP);
        ActVip.VipShop vipShop = null;
        if (!TextUtils.isEmpty(mVipShop)) {
            vipShop = ObjUtil.getObj(mVipShop, ActVip.VipShop.class);
        }
        if (vipShop != null) {
            return vipShop.ytbDepartID;
        } else {
            return null;
        }
    }

    public static String getHmmDepartId() {
        String mVipShop = SpUtils.getValue(LibApplication.getAppContext(), SpKey.VIPSHOP);
        ActVip.VipShop vipShop = null;
        if (!TextUtils.isEmpty(mVipShop)) {
            vipShop = ObjUtil.getObj(mVipShop, ActVip.VipShop.class);
        }
        if (vipShop != null) {
            return vipShop.departId;
        } else {
            return null;
        }
    }

    public static void getRealShop(LocVip locVip) {
        if (TextUtils.isEmpty(SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP))) {
            List<LocVip.Local.MerchantsShop> shopList = locVip.getAllMerchantShopWithMe();
            //System.out.println("默认选一个:"+shopList.get(0).getAddressDetails());
//                                Toast.makeText(mContext,shopList.get(0).getAddressDetails(),Toast.LENGTH_LONG).show();
            AMapLocation aMapLocation = LocUtil.getLocationBean(LibApplication.getAppContext(), SpKey.LOC_CHOSE);
            if (aMapLocation == null) {
                aMapLocation = new AMapLocation("location");
            }
            aMapLocation.setAdCode(locVip.getAllMerchantWithMe().get(0).getNearShop().district);//默认市级加5 得到模糊区县
            aMapLocation.setProvince(locVip.getAllMerchantWithMe().get(0).getNearShop().provinceName);
            aMapLocation.setCity(locVip.getAllMerchantWithMe().get(0).getNearShop().cityName);
            aMapLocation.setDistrict(locVip.getAllMerchantWithMe().get(0).getNearShop().districtName);
            LocUtil.storeLocationChose(LibApplication.getAppContext(), aMapLocation);
            LocUtil.setNowShop(shopList.get(0));
        } else {
            if (!ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(locVip.getAllMerchantShopWithMe(), new ObjectIteraor<LocVip.Local.MerchantsShop>() {
                @Override
                public String getDesObj(LocVip.Local.MerchantsShop o) {
                    return o.shopId;
                }
            }), SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP))) {//加判下历史选择是不是当前可选
                List<LocVip.Local.MerchantsShop> shopList = locVip.getAllMerchantShopWithMe();

                AMapLocation aMapLocation = LocUtil.getLocationBean(LibApplication.getAppContext(), SpKey.LOC_CHOSE);
                if (aMapLocation == null) {
                    aMapLocation = new AMapLocation("location");
                }
                aMapLocation.setAdCode(locVip.getAllMerchantWithMe().get(0).getNearShop().district);//默认市级加5 得到模糊区县
                aMapLocation.setProvince(locVip.getAllMerchantWithMe().get(0).getNearShop().provinceName);
                aMapLocation.setCity(locVip.getAllMerchantWithMe().get(0).getNearShop().cityName);
                aMapLocation.setDistrict(locVip.getAllMerchantWithMe().get(0).getNearShop().districtName);
                LocUtil.storeLocationChose(LibApplication.getAppContext(), aMapLocation);
                LocUtil.setNowShop(shopList.get(0));
            }
        }
    }

}
