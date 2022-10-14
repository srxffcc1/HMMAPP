package com.healthy.library.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.constant.SpKey;
import com.healthy.library.builder.SimpleStringBuilder;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsSetCell {
    public int combinationId;

    public String goodsId;

    public String goodsTitle;

    public String filePath;

    public double platformPrice;

    public String skuId;

    public String skuName;

    public int isMaster;

    public String goodsShopId;//增加shopid 如果为空说明就是当前切换到的shop
    public String goodsShopName;//增加 shopnName

    public String getGoodsShopId(Context context) {
        if("1".equals(type)){//1服务
            return goodsShopId;
        }else {
            return SpUtils.getValue(context, SpKey.CHOSE_SHOP);
        }
    }

    public String getGoodsShopName(Context context) {
        if("1".equals(type)){//1服务
            return goodsShopName;
        }else {
            return SpUtils.getValue(context, SpKey.CHOSE_SHOPNAME);
        }
    }

    public String type;

    public String shopName;

    public String shopAddress;
    public String[] shopIdList=new String[]{};

    public String getShopIdListString() {
        return new SimpleStringBuilder<String>().putList(shopIdList,null).text();
    }

    public Map<String, Object> setmap = new HashMap<>();

    public GoodsSpecDetail goodsSpecDetail;

    public String specValue;
    private List<GoodsSpecCell> goodsSpecCells;

    public List<GoodsSpecCell> getSpecCell() {
        if(goodsSpecCells!=null){
            return goodsSpecCells;
        }
        if(TextUtils.isEmpty(specValue)){
            return new ArrayList<>();
        }else {
            goodsSpecCells= resolveSpecListData(specValue);
            return goodsSpecCells;
        }
    }
    private List<GoodsSpecCell> resolveSpecListData(String obj) {
        List<GoodsSpecCell> result = new ArrayList<>();
        try {
            JSONArray data=new JSONArray(obj);
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<GoodsSpecCell>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    
}
