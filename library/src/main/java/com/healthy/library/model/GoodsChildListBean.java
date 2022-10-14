package com.healthy.library.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.Date;

public class GoodsChildListBean {
    public String goodsChildId;
    public String mapMarketingGoodsId;
    public String barcodeSku;
    public String goodsTitle;
    public String adTitle;
    public String goodsSpec;
    public double retailPrice;
    public double storePrice;
    public double platformPrice;
    public double marketingPrice;
    public String marketingType;
    public int maxInventory;
    public int availableInventory;
    public String lockedInventory;
    public String sales;
    public String mapMarketingGoodsChildId;

    private GoodsSpecCell goodsSpecCells;

    public GoodsSpecCell getSpecCell() {
        if (goodsSpecCells != null) {
            return goodsSpecCells;
        }
        if (TextUtils.isEmpty(goodsSpec)) {
            return null;
        } else {
            goodsSpecCells = resolveSpecListData(goodsSpec);
            return goodsSpecCells;
        }
    }

    private GoodsSpecCell resolveSpecListData(String obj) {
        GoodsSpecCell result = null;
        try {
            JSONArray data = new JSONArray(obj.replaceAll("\"", ""));
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<GoodsSpecCell>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
