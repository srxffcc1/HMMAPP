package com.healthy.library.net;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.healthy.library.BuildConfig;
import com.healthy.library.constant.SpKey;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.utils.SpUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 10:14
 * @des 生成头部
 */

public class GenerateHeader {

    private static final String UUID = "UUID";
    public static String noEndLnString(String org){
        if(org!=null&&org.endsWith("\n")){
            org=org.substring(0,org.length()-1);
            return noEndLnString(org);
        }else {
            return org;
        }
    }
    public static String noStartLnString(String org){
        if(org!=null&&org.startsWith("\n")){
            org=org.substring(1);
            return noStartLnString(org);
        }else {
            return org;
        }
    }
    public static String generateJustBody(Context context, String function, Map<String, Object> body) {
        String uuid = SpUtils.getValue(context, UUID);
        if (TextUtils.isEmpty(uuid)) {
            uuid = java.util.UUID.randomUUID().toString();
            SpUtils.store(context, UUID, uuid);
        }
//        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//        Header header = new Header();
//        header.setPlatformVersion(Build.VERSION.RELEASE);
//        header.setPlatformCode("Android");
//        header.setCmdId(ChannelUtil.getChannel(context.getApplicationContext()));
//        header.setToken(SpUtils.getValue(context, SpKey.TOKEN));
//        header.setAppVersion(BuildConfig.VERSION_NAME);
//        header.setUserId(SpUtils.getValue(context, SpKey.USER_ID));
//        header.setUserLoginCategory("1");
//        header.setUuid(uuid);
//        header.setTs(timestamp);
//        header.setTraceId(uuid);
//        header.setFunction(function);
//        header.setPhoneName(Build.BRAND);

        GsonBuilder builder = new GsonBuilder();;

        builder.disableHtmlEscaping()
                .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

                    @Override
                    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                        if (src == src.longValue())
                            return new JsonPrimitive(src.longValue()+"");
                        return new JsonPrimitive(src);
                    }
                });
        builder.registerTypeAdapter(String.class, new JsonSerializer<String>() {
            @Override
            public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
                if (src !=null)
                    return new JsonPrimitive(noStartLnString(noEndLnString(src.trim()))+"");//去掉首尾换行 去掉前后空格
                return new JsonPrimitive(src);
            }
        });
        Gson gson = builder.create();
        Map<String, Object> map = new HashMap<>(15);
        if(isNeedEscape(function)){
            map.put("body", body == null ? "{}" : gson.toJson(body));
        }else {
            map.put("body", body == null ? "{}" : body);
        }
        return gson.toJson(map);
    }


    public static String generate(Context context, String function, Map<String, Object> body) {
        String uuid = SpUtils.getValue(context, UUID);
        if (TextUtils.isEmpty(uuid)) {
            uuid = java.util.UUID.randomUUID().toString();
            SpUtils.store(context, UUID, uuid);
        }
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        Header header = new Header();
        header.setPlatformVersion(Build.VERSION.RELEASE);
        header.setPlatformCode("Android");
        try {
            header.setCmdId(ChannelUtil.getChannel(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        header.setToken(SpUtils.getValue(context, SpKey.TOKEN));
        header.setAppVersion(ChannelUtil.getAPPFullVersion());
        header.setUserId(SpUtils.getValue(context, SpKey.USER_ID));
        header.setUserLoginCategory("1");
        header.setUuid(uuid);
        header.setTs(timestamp);
        header.setTraceId(uuid);
        header.setFunction(function);
        header.setPhoneName(Build.BRAND+Build.MODEL+"-"+ FrameActivityManager.instance().topActivityName());
        GsonBuilder builder = new GsonBuilder();

        builder.disableHtmlEscaping()
        .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

            @Override
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == src.longValue())
                    return new JsonPrimitive(src.longValue()+"");
                return new JsonPrimitive(src);
            }
        });
        builder.registerTypeAdapter(String.class, new JsonSerializer<String>() {
            @Override
            public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
                if (src !=null)
                    return new JsonPrimitive(noStartLnString(noEndLnString(src.trim()))+"");//去掉首尾换行 去掉前后空格 
                return new JsonPrimitive(src);
            }
        });
        Gson gson = builder.create();
        Map<String, Object> map = new HashMap<>(15);
        map.put("header", header);
        if(isNeedEscape(function)){
            map.put("body", body == null ? "{}" : gson.toJson(body));
        }else {
            map.put("body", body == null ? "{}" : body);
        }
        return gson.toJson(map);
    }

    private static boolean isNeedEscape(String function) {//哪些接口要转义的
        if(TextUtils.isEmpty(function)){
            return false;
        }
        if(function.length()<=4){
            return true;
        }
//        if(function.contains("pwd")){
//            return true;
//        }
        return false;
    }
}
