package com.healthy.library.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class VipGift {

    public class VipArray {
        public String GoodsType;

        public String LimiteDepartID;

        public String RegDate;

        public String GoodsTypeName;

        public String StuffNo;

        public String EndDate;

        public String GoodsName;

        public String YeNumber;

        public String StartDate;

        public String RegOther;

        public String Price;

        public String State;

        public String PopName;

        public String SeqID;

        public String GoodsID;


    }

    public class VipData {
        public List<VipArray> array;

        public String title;

    }

    public String content;

    public String data;

    public VipData getData() {
        return resolveData(data);
    }

    public String dataType;

    public String id;

    public String pushType;

    public String title;

    private VipData resolveData(String obj) {
        obj = getRealJsonString(obj);
//        obj="{\"array\":[{\"GoodsType\":\"0\",\"LimiteDepartID\":\"[001]妇幼店\",\"RegDate\":\"2020-01-10 17:38:46\",\"GoodsTypeName\":\"0赠券\",\"StuffNo\":\"217322\",\"EndDate\":\"2020-08-31\",\"GoodsName\":\"19年3999充值每月50元非奶粉券\",\"YeNumber\":\"1\",\"StartDate\":\"2020-08-01\",\"RegOther\":\"\",\"Price\":\"-50.00\",\"State\":\"1\",\"PopName\":\"【用券】19年3999充值每月50元非奶粉券\",\"SeqID\":\"3563\",\"GoodsID\":\"217322\"}],\"title\":\"你的生日礼\"}";
//        //System.out.println(obj);
//        obj="{\"array\":[{\"GoodsType\":\"0\",\"LimiteDepartID\":\"[001]妇幼店\",\"RegDate\":\"2020-01-10 17:38:46\",\"GoodsTypeName\":\"0赠券\",\"StuffNo\":\"217322\",\"EndDate\":\"2020-08-31\",\"GoodsName\":\"19年3999充值每月50元非奶粉券\",\"YeNumber\":\"1\",\"StartDate\":\"2020-08-01\",\"RegOther\":\"\",\"Price\":\"-50.00\",\"State\":\"1\",\"PopName\":\"【用券】19年3999充值每月50元非奶粉券\",\"SeqID\":\"3563\",\"GoodsID\":\"217322\"}],\"title\":\"你的生日礼\"}";
//        obj="{\"array\":[{\"GoodsType\":\"0\",\"LimiteDepartID\":\"[001]妇幼店\",\"RegDate\":\"2020-01-10 17:38:46\",\"GoodsTypeName\":\"0赠券\",\"StuffNo\":\"217322\",\"EndDate\":\"2020-08-31\",\"GoodsName\":\"19年3999充值每月50元非奶粉券\",\"YeNumber\":\"1\",\"StartDate\":\"2020-08-01\",\"RegOther\":\"\",\"Price\":\"-50.00\",\"State\":\"1\",\"PopName\":\"【用券】19年3999充值每月50元非奶粉券\",\"SeqID\":\"3563\",\"GoodsID\":\"217322\"}],\"title\":\"你的生日礼\"}";
        VipData result = null;
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
            Type type = new TypeToken<VipData>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @NotNull
    private String getRealJsonString(String obj) {
        //System.out.println(obj);
        if(obj.startsWith("\"")){
            obj=obj.substring(1,obj.length());
        }
        if(obj.endsWith("\"")){
            obj=obj.substring(0,obj.length()-1);
        }
        obj=obj.replace("\\","");
        return obj;
    }
}
