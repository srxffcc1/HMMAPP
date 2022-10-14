package com.healthy.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.constant.SpKey;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/25 11:21
 * @des sp
 */

public class SpUtilsOld {
    public static Context APPLICATIONCONTEXT;

    public static void store(Context context, String k, String v) {
//        if(BuildConfig.DEBUG){
//            if(SpKey.CHOSE_SHOP.equals(k)){
//                v="5";
//            }
//        }
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        preferences.edit().putString(k, v).apply();
    }
    public static void store(Context context, String k, int v) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        preferences.edit().putInt(k, v).apply();
    }
    public static void store(Context context, String k, long v) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        preferences.edit().putLong(k, v).apply();
    }

    public static void store(Context context, String k, boolean v) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(k, v).apply();
    }

    public static String getValue(Context context, String k) {
//        if(BuildConfig.DEBUG){
//            if(SpKey.CHOSE_SHOP.equals(k)){
//                return "5";
//            }
//        }
//        System.out.println("获得的key:"+k);
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString(k, "");
        } catch (Exception e) {
            System.out.println("获得的错误key:"+k);
            e.printStackTrace();
        }
        return null;
    }
    public static int getValue(Context context, String k,int defaultvalue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(k, defaultvalue);
    }
    public static long getValue(Context context, String k,long defaultvalue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(k, defaultvalue);
    }

    public static boolean getValue(Context context, String k, boolean defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(k, defaultValue);
    }

    public static boolean isFirst(Context context,String k){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(preferences.getBoolean(k,true)){
            preferences.edit().putBoolean(k, false).apply();
            return true;
        }else {
            return false;
        }
    }

    public static void clear(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
        store(context, SpKey.YSLOOK,true);
    }

    public static void storeJson(Context mContext, String k, Object userInfoExModel) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        Gson gson = builder.create();
        String v=gson.toJson(userInfoExModel);
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        preferences.edit().putString(k, v).apply();
    }

    public static<T> List<T> resolveHistoryArrayData(String obj,Class<T> cls) {
        List<T> result = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            JsonArray arry = new JsonParser().parse(data.toString()).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                result.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static<T> T resolveHistoryData(String obj) {
        T result = null;
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
            Type type = new TypeToken<T>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
