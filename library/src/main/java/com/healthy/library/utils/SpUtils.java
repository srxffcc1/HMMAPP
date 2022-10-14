package com.healthy.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
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
import com.healthy.library.LibApplication;
import com.healthy.library.constant.SpKey;
import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Li
 * @date 2019/03/25 11:21
 * @des sp
 */

public class SpUtils {
    private static final MMKV mmkv = LibApplication.mmkv;

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public static <T> void store(Context context, String key, T object) {
        if (object instanceof String) {
            mmkv.encode(key, (String) object);
        } else if (object instanceof Integer) {
            mmkv.encode(key, (Integer) object);
        } else if (object instanceof Boolean) {
            mmkv.encode(key, (Boolean) object);
        } else if (object instanceof Float) {
            mmkv.encode(key, (Float) object);
        } else if (object instanceof Long) {
            mmkv.encode(key, (Long) object);
        } else if (object instanceof Double) {
            mmkv.encode(key, (Double) object);
        } else if (object instanceof Set) {
            mmkv.encode(key, (Set<String>) object);
        } else if (object instanceof Parcelable) {
            mmkv.encode(key, (Parcelable) object);
        } else {
            mmkv.encode(key, object == null ? "" : object.toString());
        }
    }

    public static String getValue(Context context, String k) {
        try {
            return mmkv.decodeString(k,"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getValue(Context context, String k, int defaultValue) {
        return mmkv.decodeInt(k, defaultValue);
    }

    public static long getValue(Context context, String k, long defaultValue) {
        return mmkv.decodeLong(k, defaultValue);
    }

    public static boolean getValue(Context context, String k, boolean defaultValue) {
        return mmkv.decodeBool(k, defaultValue);
    }

    public static boolean isFirst(Context context, String k) {
        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(preferences.getBoolean(k,true)){
            preferences.edit().putBoolean(k, false).apply();
            return true;
        }else {
            return false;
        }*/
        if (getValue(context,k, true)) {
            store(context, k, false);
            return true;
        } else {
            return false;
        }
    }

    public static void clear(Context context) {
        mmkv.clearAll();
        store(context, SpKey.YSLOOK, true);
        store(context, "keyFrame", false);
        store(context, "referralCodeBind", false);
//        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }

    public static void storeJson(Context mContext, String k, Object userInfoExModel) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        Gson gson = builder.create();
        String v = gson.toJson(userInfoExModel);
       /* SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        preferences.edit().putString(k, v).apply();*/

        //储存
        store(mContext, k, v);
    }

    public static <T> List<T> resolveHistoryArrayData(String obj, Class<T> cls) {
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
//            e.printStackTrace();
        }

        return result;
    }

    public static <T> T resolveHistoryData(String obj) {
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
