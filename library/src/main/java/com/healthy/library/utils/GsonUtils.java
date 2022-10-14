package com.healthy.library.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class GsonUtils {
    private static Gson gson = new Gson();

    /**
     * json字符串转对象或容器
     */
    public static <T> T fromJson(String json, TypeToken<T> token) {
        try {
            return gson.fromJson(json, token.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转对象或容器
     */
    public static <T> T fromJson(String json, Class<T> c) {
        try {
            return gson.fromJson(json, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(JsonReader jsonReader, Class<T> c) {
        try {
            return gson.fromJson(jsonReader, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    /**
     * 获取Assets目录下json文件字符串对象
     * @param context
     * @param fileName
     * @return
     */
    public String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        String s="[{\"id\":110000,\"name\":\"\\u5317\\u4eac\",\"parentId\":100000,\"shortName\":\"\\u5317\\u4eac\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u5317\\u4eac\",\"pinyin\":\"Beijing\",\"feicuiNo\":\"94A5KQ\"},{\"id\":120000,\"name\":\"\\u5929\\u6d25\",\"parentId\":100000,\"shortName\":\"\\u5929\\u6d25\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u5929\\u6d25\",\"pinyin\":\"Tianjin\",\"feicuiNo\":\"QELRFB\"},{\"id\":130000,\"name\":\"\\u6cb3\\u5317\\u7701\",\"parentId\":100000,\"shortName\":\"\\u6cb3\\u5317\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u6cb3\\u5317\\u7701\",\"pinyin\":\"Hebei\",\"feicuiNo\":\"ZC3UQM\"},{\"id\":140000,\"name\":\"\\u5c71\\u897f\\u7701\",\"parentId\":100000,\"shortName\":\"\\u5c71\\u897f\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u5c71\\u897f\\u7701\",\"pinyin\":\"Shanxi\",\"feicuiNo\":\"Z22F4Y\"},{\"id\":150000,\"name\":\"\\u5185\\u8499\\u53e4\\u81ea\\u6cbb\\u533a\",\"parentId\":100000,\"shortName\":\"\\u5185\\u8499\\u53e4\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u5185\\u8499\\u53e4\\u81ea\\u6cbb\\u533a\",\"pinyin\":\"Inner Mongolia\",\"feicuiNo\":\"ZG4VYD\"},{\"id\":210000,\"name\":\"\\u8fbd\\u5b81\\u7701\",\"parentId\":100000,\"shortName\":\"\\u8fbd\\u5b81\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u8fbd\\u5b81\\u7701\",\"pinyin\":\"Liaoning\",\"feicuiNo\":\"JNNE9X\"},{\"id\":220000,\"name\":\"\\u5409\\u6797\\u7701\",\"parentId\":100000,\"shortName\":\"\\u5409\\u6797\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u5409\\u6797\\u7701\",\"pinyin\":\"Jilin\",\"feicuiNo\":\"T05H1M\"},{\"id\":230000,\"name\":\"\\u9ed1\\u9f99\\u6c5f\\u7701\",\"parentId\":100000,\"shortName\":\"\\u9ed1\\u9f99\\u6c5f\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u9ed1\\u9f99\\u6c5f\\u7701\",\"pinyin\":\"Heilongjiang\",\"feicuiNo\":\"PKEF2M\"},{\"id\":310000,\"name\":\"\\u4e0a\\u6d77\",\"parentId\":100000,\"shortName\":\"\\u4e0a\\u6d77\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u4e0a\\u6d77\",\"pinyin\":\"Shanghai\",\"feicuiNo\":\"7KQZNF\"},{\"id\":320000,\"name\":\"\\u6c5f\\u82cf\\u7701\",\"parentId\":100000,\"shortName\":\"\\u6c5f\\u82cf\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u6c5f\\u82cf\\u7701\",\"pinyin\":\"Jiangsu\",\"feicuiNo\":\"AQ3A56\"},{\"id\":330000,\"name\":\"\\u6d59\\u6c5f\\u7701\",\"parentId\":100000,\"shortName\":\"\\u6d59\\u6c5f\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u6d59\\u6c5f\\u7701\",\"pinyin\":\"Zhejiang\",\"feicuiNo\":\"APEUX0\"},{\"id\":340000,\"name\":\"\\u5b89\\u5fbd\\u7701\",\"parentId\":100000,\"shortName\":\"\\u5b89\\u5fbd\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u5b89\\u5fbd\\u7701\",\"pinyin\":\"Anhui\",\"feicuiNo\":\"5041NA\"},{\"id\":350000,\"name\":\"\\u798f\\u5efa\\u7701\",\"parentId\":100000,\"shortName\":\"\\u798f\\u5efa\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u798f\\u5efa\\u7701\",\"pinyin\":\"Fujian\",\"feicuiNo\":\"KVHMZF\"},{\"id\":360000,\"name\":\"\\u6c5f\\u897f\\u7701\",\"parentId\":100000,\"shortName\":\"\\u6c5f\\u897f\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u6c5f\\u897f\\u7701\",\"pinyin\":\"Jiangxi\",\"feicuiNo\":\"TEBDK3\"},{\"id\":370000,\"name\":\"\\u5c71\\u4e1c\\u7701\",\"parentId\":100000,\"shortName\":\"\\u5c71\\u4e1c\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u5c71\\u4e1c\\u7701\",\"pinyin\":\"Shandong\",\"feicuiNo\":\"D2V7KY\"},{\"id\":410000,\"name\":\"\\u6cb3\\u5357\\u7701\",\"parentId\":100000,\"shortName\":\"\\u6cb3\\u5357\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u6cb3\\u5357\\u7701\",\"pinyin\":\"Henan\",\"feicuiNo\":\"KKV5RS\"},{\"id\":420000,\"name\":\"\\u6e56\\u5317\\u7701\",\"parentId\":100000,\"shortName\":\"\\u6e56\\u5317\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u6e56\\u5317\\u7701\",\"pinyin\":\"Hubei\",\"feicuiNo\":\"H527LD\"},{\"id\":430000,\"name\":\"\\u6e56\\u5357\\u7701\",\"parentId\":100000,\"shortName\":\"\\u6e56\\u5357\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u6e56\\u5357\\u7701\",\"pinyin\":\"Hunan\",\"feicuiNo\":\"Q29BG1\"},{\"id\":440000,\"name\":\"\\u5e7f\\u4e1c\\u7701\",\"parentId\":100000,\"shortName\":\"\\u5e7f\\u4e1c\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u5e7f\\u4e1c\\u7701\",\"pinyin\":\"Guangdong\",\"feicuiNo\":\"J4TTXF\"},{\"id\":450000,\"name\":\"\\u5e7f\\u897f\\u58ee\\u65cf\\u81ea\\u6cbb\\u533a\",\"parentId\":100000,\"shortName\":\"\\u5e7f\\u897f\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u5e7f\\u897f\\u58ee\\u65cf\\u81ea\\u6cbb\\u533a\",\"pinyin\":\"Guangxi\",\"feicuiNo\":\"ZQ6LYT\"},{\"id\":460000,\"name\":\"\\u6d77\\u5357\\u7701\",\"parentId\":100000,\"shortName\":\"\\u6d77\\u5357\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u6d77\\u5357\\u7701\",\"pinyin\":\"Hainan\",\"feicuiNo\":\"UCJM3X\"},{\"id\":500000,\"name\":\"\\u91cd\\u5e86\",\"parentId\":100000,\"shortName\":\"\\u91cd\\u5e86\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u91cd\\u5e86\",\"pinyin\":\"Chongqing\",\"feicuiNo\":\"8L9TL6\"},{\"id\":510000,\"name\":\"\\u56db\\u5ddd\\u7701\",\"parentId\":100000,\"shortName\":\"\\u56db\\u5ddd\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u56db\\u5ddd\\u7701\",\"pinyin\":\"Sichuan\",\"feicuiNo\":\"M498Z7\"},{\"id\":520000,\"name\":\"\\u8d35\\u5dde\\u7701\",\"parentId\":100000,\"shortName\":\"\\u8d35\\u5dde\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u8d35\\u5dde\\u7701\",\"pinyin\":\"Guizhou\",\"feicuiNo\":\"236K0B\"},{\"id\":530000,\"name\":\"\\u4e91\\u5357\\u7701\",\"parentId\":100000,\"shortName\":\"\\u4e91\\u5357\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u4e91\\u5357\\u7701\",\"pinyin\":\"Yunnan\",\"feicuiNo\":\"J4ZJQW\"},{\"id\":540000,\"name\":\"\\u897f\\u85cf\\u81ea\\u6cbb\\u533a\",\"parentId\":100000,\"shortName\":\"\\u897f\\u85cf\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u897f\\u85cf\\u81ea\\u6cbb\\u533a\",\"pinyin\":\"Tibet\",\"feicuiNo\":\"Z3603S\"},{\"id\":610000,\"name\":\"\\u9655\\u897f\\u7701\",\"parentId\":100000,\"shortName\":\"\\u9655\\u897f\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u9655\\u897f\\u7701\",\"pinyin\":\"Shaanxi\",\"feicuiNo\":\"L9LSY8\"},{\"id\":620000,\"name\":\"\\u7518\\u8083\\u7701\",\"parentId\":100000,\"shortName\":\"\\u7518\\u8083\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u7518\\u8083\\u7701\",\"pinyin\":\"Gansu\",\"feicuiNo\":\"46Q1FF\"},{\"id\":630000,\"name\":\"\\u9752\\u6d77\\u7701\",\"parentId\":100000,\"shortName\":\"\\u9752\\u6d77\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u9752\\u6d77\\u7701\",\"pinyin\":\"Qinghai\",\"feicuiNo\":\"F3KXQ2\"},{\"id\":640000,\"name\":\"\\u5b81\\u590f\\u56de\\u65cf\\u81ea\\u6cbb\\u533a\",\"parentId\":100000,\"shortName\":\"\\u5b81\\u590f\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u5b81\\u590f\\u56de\\u65cf\\u81ea\\u6cbb\\u533a\",\"pinyin\":\"Ningxia\",\"feicuiNo\":\"CRFBRA\"},{\"id\":650000,\"name\":\"\\u65b0\\u7586\\u7ef4\\u543e\\u5c14\\u81ea\\u6cbb\\u533a\",\"parentId\":100000,\"shortName\":\"\\u65b0\\u7586\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u65b0\\u7586\\u7ef4\\u543e\\u5c14\\u81ea\\u6cbb\\u533a\",\"pinyin\":\"Xinjiang\",\"feicuiNo\":\"1QKVA8\"},{\"id\":710000,\"name\":\"\\u53f0\\u6e7e\",\"parentId\":100000,\"shortName\":\"\\u53f0\\u6e7e\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u53f0\\u6e7e\",\"pinyin\":\"Taiwan\",\"feicuiNo\":\"0K923N\"},{\"id\":810000,\"name\":\"\\u9999\\u6e2f\\u7279\\u522b\\u884c\\u653f\\u533a\",\"parentId\":100000,\"shortName\":\"\\u9999\\u6e2f\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u9999\\u6e2f\\u7279\\u522b\\u884c\\u653f\\u533a\",\"pinyin\":\"Hong Kong\",\"feicuiNo\":\"9AD3AC\"},{\"id\":820000,\"name\":\"\\u6fb3\\u95e8\\u7279\\u522b\\u884c\\u653f\\u533a\",\"parentId\":100000,\"shortName\":\"\\u6fb3\\u95e8\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u6fb3\\u95e8\\u7279\\u522b\\u884c\\u653f\\u533a\",\"pinyin\":\"Macau\",\"feicuiNo\":\"THWTGY\"},{\"id\":900000,\"name\":\"\\u9493\\u9c7c\\u5c9b\",\"parentId\":100000,\"shortName\":\"\\u9493\\u9c7c\\u5c9b\",\"levelType\":1,\"mergerName\":\"\\u4e2d\\u56fd,\\u9493\\u9c7c\\u5c9b\",\"pinyin\":\"DiaoyuDao\",\"feicuiNo\":\"CH4VCA\"}]";
       List map= fromJson(s, List.class);
        //System.out.println(map);
    }
}