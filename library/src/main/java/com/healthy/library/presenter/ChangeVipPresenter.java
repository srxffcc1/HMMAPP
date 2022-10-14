package com.healthy.library.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
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
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.ChangeVipContract;
import com.healthy.library.message.UpdateUserShop;
import com.healthy.library.model.HttpTmpResult;
import com.healthy.library.model.LocVip;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class ChangeVipPresenter implements ChangeVipContract.Presenter {

    private Context mContext;

    public ChangeVipPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void changeVip(Map<String, Object> map) {
        if(SpUtils.getValue(mContext, "m_10004", false)){
            return;
        }
        if(SpUtils.getValue(LibApplication.getAppContext(), SpKey.IS_LOCERROR, false)){//当前是演示模式
            return;
        }
        if(TextUtils.isEmpty(LocUtil.getAreaNo(mContext,SpKey.LOC_ORG))){
            return;
        }
        map.put(Functions.FUNCTION,"m_10004");
        map.put("provinceNo",LocUtil.getProvinceNo(mContext,SpKey.LOC_ORG));
        map.put("cityNo",LocUtil.getCityNo(mContext,SpKey.LOC_ORG));
        map.put("areaNo",LocUtil.getAreaNo(mContext,SpKey.LOC_ORG));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(null, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        SpUtils.store(LibApplication.getAppContext(),"m_10004",true);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        getLocVip(new SimpleHashMapBuilder<>());

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();

                    }
                });
    }

    @Override
    public void getLocVip(Map<String, Object> map) {
        map.put("function", "100001");
        map.put("longitude",LocUtil.getLongitude(mContext,SpKey.LOC_ORG));
        map.put("latitude",LocUtil.getLatitude(mContext,SpKey.LOC_ORG));
        map.put("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_ORG));
        map.put("city", LocUtil.getCityNo(mContext, SpKey.LOC_ORG));
        map.put("district", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(null, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        LocVip locVip=resolveLocVipData(obj);
                        List<LocVip.Local.MerchantsShop> shopList = locVip.getAllMerchantShopWithMe();
                        if(!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))){//已经选择的门店 补充判断下 是不是还在可选范围呢 确认下闭店问题
                            System.out.println("检测门店合法性");
                            int hasindex=ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(locVip.getAllMerchantShopWithMe(), new ObjectIteraor<LocVip.Local.MerchantsShop>() {
                                @Override
                                public String getDesObj(LocVip.Local.MerchantsShop o) {
                                    return o.shopId;
                                }
                            }), SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP));
                            if (hasindex==-1) {//加判下历史选择是不是当前可选
                                AMapLocation aMapLocation = LocUtil.getLocationBean(LibApplication.getAppContext(), SpKey.LOC_CHOSE);
                                if (aMapLocation == null) {
                                    aMapLocation = new AMapLocation("location");
                                }
                                aMapLocation.setAdCode(locVip.getAllMerchantWithMe().get(0).getNearShop().district);//默认市级加5 得到模糊区县
                                aMapLocation.setProvince(locVip.getAllMerchantWithMe().get(0).getNearShop().provinceName);
                                aMapLocation.setCity(locVip.getAllMerchantWithMe().get(0).getNearShop().cityName);
                                aMapLocation.setDistrict(locVip.getAllMerchantWithMe().get(0).getNearShop().districtName);
                                UpdateUserShop updateUserShop=new UpdateUserShop(shopList.get(0),aMapLocation);
                                EventBus.getDefault().post(updateUserShop);
                            }else {
                                LocUtil.setNowShop(shopList.get(hasindex));//可选的话重新设置必备参数
                            }
                        }else {
                            LocUtil.setNowShop(shopList.get(0));//没选的话重新设置必备参数
                        }
                        SpUtils.store(mContext,"fun100001",new Gson().toJson(new HttpTmpResult(System.currentTimeMillis(),obj)));//保存缓存

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private LocVip resolveLocVipData(String obj) {
        LocVip result = new LocVip();
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<LocVip>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}