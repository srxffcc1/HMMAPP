package com.healthy.library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.R;
import com.healthy.library.model.MonMessage;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Author: Li
 * Date: 2018/10/17 0017
 * Description:
 */
public class CheckUtils {

    public static boolean checkPhone(CharSequence phone) {
        return !TextUtils.isEmpty(phone) && Pattern.matches("[1][0-9]{10}", phone);
    }

    /**
     * 检查手机号和验证码的合法性
     *
     * @param phone 手机号
     * @param code  验证码
     */
    public static boolean checkPhoneAndCodeIllegal(String phone, String code) {
        return !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code) &&
                CheckUtils.checkPhone(phone) && Pattern.matches("\\d{6}", code);
    }
    public static boolean checkCodeIllegal(String code){
        return Pattern.matches("\\d{6}",code);
    }
    public static Integer idCardToAge(String idcard) {
        Integer selectYear = Integer.valueOf(idcard.substring(6, 10));         //出生的年份
        Integer selectMonth = Integer.valueOf(idcard.substring(10, 12));       //出生的月份
        Integer selectDay = Integer.valueOf(idcard.substring(12, 14));         //出生的日期
        Calendar cal = Calendar.getInstance();
        Integer yearMinus = cal.get(Calendar.YEAR) - selectYear;
        Integer monthMinus = cal.get(Calendar.MONTH) + 1 - selectMonth;
        Integer dayMinus = cal.get(Calendar.DATE) - selectDay;
        Integer age = yearMinus;
        if (yearMinus < 0) {
            age = 0;
        } else if (yearMinus == 0) {
            age = 0;
        } else if (yearMinus > 0) {
            if (monthMinus == 0) {
                if (dayMinus < 0) {
                    age = age - 1;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        return age;
    }
    /**
     * 通过身份证号码获取出生日期、性别、年龄
     * @param certificateNo
     * @return 返回的出生日期格式：1990-01-01   性别格式：F-女，M-男
     */
    public static Map<String, String> getBirAgeSex(String certificateNo) {
        String birthday = "";
        String age = "";
        String sexCode = "";
        String sexName = "";

        int year = Calendar.getInstance().get(Calendar.YEAR);
        char[] number = certificateNo.toCharArray();
        boolean flag = true;
        if (number.length == 15) {
            for (int x = 0; x < number.length; x++) {
                if (!flag) {
                    return new HashMap<String, String>();
                }
                flag = Character.isDigit(number[x]);
            }
        } else if (number.length == 18) {
            for (int x = 0; x < number.length - 1; x++) {
                if (!flag) {
                    return new HashMap<String, String>();
                }
                flag = Character.isDigit(number[x]);
            }
        }
        if (flag && certificateNo.length() == 15) {
            birthday = "19" + certificateNo.substring(6, 8) + "-"
                    + certificateNo.substring(8, 10) + "-"
                    + certificateNo.substring(10, 12);
            sexCode = Integer.parseInt(certificateNo.substring(certificateNo.length() - 3, certificateNo.length())) % 2 == 0 ? "0" : "1";
            sexName = Integer.parseInt(certificateNo.substring(certificateNo.length() - 3, certificateNo.length())) % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt("19" + certificateNo.substring(6, 8))) + "";
        } else if (flag && certificateNo.length() == 18) {
            birthday = certificateNo.substring(6, 10) + "-"
                    + certificateNo.substring(10, 12) + "-"
                    + certificateNo.substring(12, 14);
            sexCode = Integer.parseInt(certificateNo.substring(certificateNo.length() - 4, certificateNo.length() - 1)) % 2 == 0 ? "0" : "1";
            sexName = Integer.parseInt(certificateNo.substring(certificateNo.length() - 4, certificateNo.length() - 1)) % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt(certificateNo.substring(6, 10))) + "";
        }
        System.out.println("sexName");
        Map<String, String> map = new HashMap<String, String>();
        map.put("birthday", birthday);
        map.put("sexName", sexName);
        map.put("age", age);
        map.put("sexCode", sexCode);
        return map;
    }



    public static void checkMessageCount(Context context, View desview){
        //System.out.println("检测下推送数量");
        Badge badge;
        if(desview.getTag()!=null&&desview.getTag() instanceof Badge){
            badge= (Badge) desview.getTag();
        }else {
            badge = new QBadgeView(context)
                    .setShowShadow(false)
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .setBadgeTextSize(6,true)
                    .bindTarget(desview);
            desview.setTag(badge);
        }

        int messcount=getMessageCount(context);
        if(messcount==0){
//            //System.out.println("检测下推送数量没数据了");
            badge.hide(false);
        }else {
            badge.setBadgeText(messcount>=99?"99":messcount+"");
        }
    }

    public static int getMessageCount(Context mContext) {
        int result=0;
        List<MonMessage> messagelist=new ArrayList<>();
        List<MonMessage> messagelistresult=new ArrayList<>();
        MonMessage messagepl= resolveMessageData(SpUtils.getValue(mContext,"评论"));
        messagepl.type="评论";
        messagepl.itemType=1;
        messagepl.imageRes= R.drawable.message_type1;
        messagelist.add(messagepl);
        MonMessage messagez= resolveMessageData(SpUtils.getValue(mContext,"赞"));
        messagez.type="赞";
        messagez.itemType=1;
        messagez.imageRes= R.drawable.message_type2;
        messagelist.add(messagez);
        MonMessage messagemyfwzs= resolveMessageData(SpUtils.getValue(mContext,"母婴服务小助手"));
        messagemyfwzs.type="母婴服务小助手";
        messagemyfwzs.itemType=2;
        messagemyfwzs.imageRes= R.drawable.message_type3;
        messagelist.add(messagemyfwzs);
        MonMessage messagetchmzs= resolveMessageData(SpUtils.getValue(mContext,"同城圈小助手"));
        messagetchmzs.type="同城圈小助手";
        messagetchmzs.itemType=2;
        messagetchmzs.imageRes= R.drawable.message_type4;
        messagelist.add(messagetchmzs);
//        MonMessage messagemysczs= resolveMessageData(SpUtils.getValue(mContext,"母婴商城小助手"));
//        messagemysczs.type="母婴商城小助手";
//        messagemysczs.itemType=2;
//        messagemysczs.imageRes= R.drawable.message_type5;
//        messagelist.add(messagemysczs);
        MonMessage messagewdzs= resolveMessageData(SpUtils.getValue(mContext,"问答小助手"));
        messagewdzs.type="问答小助手";
        messagewdzs.itemType=2;
        messagewdzs.imageRes= R.drawable.message_type6;
        messagelist.add(messagewdzs);
        MonMessage messagetz= resolveMessageData(SpUtils.getValue(mContext,"通知"));
        messagetz.type="通知";
        messagetz.itemType=2;
        messagetz.imageRes= R.drawable.message_type7;
        messagelist.add(messagetz);

        for (int i = 0; i <messagelist.size() ; i++) {
            MonMessage tmpmessage=messagelist.get(i);
            if(tmpmessage.istoast){
            }
            result+=tmpmessage.num;
        }
        return result;
    }

    private static MonMessage resolveMessageData(String obj) {
        MonMessage result = new MonMessage();
        try {
            JSONObject data=new JSONObject(obj);
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<MonMessage>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return result;
    }
    /**
     * 验证输入的名字是否为“中文”或者是否包含“·”
     */
    public static boolean isLegalName(String name){
        if (name.contains("·") || name.contains("•")){
            if (name.matches("^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]+$")){
                return true;
            }else {
                return false;
            }
        }else {
            if (name.matches("^[\\u4e00-\\u9fa5]+$")){
                return true;
            }else {
                return false;
            }
        }
    }

    /**
     * 验证输入的身份证号是否合法
     */
    public static boolean isLegalId(String id){
        if (id.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")){
            return true;
        }else {
            return false;
        }
    }
}
