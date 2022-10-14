package com.example.convert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass {
    public static void main(String[] args) {
        String org="5\t isSignContract\t是否已签电子协议\t是\t[string]\t查看\n" +
                "6\t registerTime\t创建时间\t是\t[string]\t查看\n" +
                "7\t userState\t用户状态。1：有效 3:审核失败\t5:已锁 7:待审核\t是\t[string]\t查看\n" +
                "8\t payFailAmount\t支付失败次数\t是\t[string]\t查看\n" +
                "9\t isIdentityChecked\t是否进行实名认证\t是\t[string]\t查看\n" +
                "10\t isSetPayPwd\t是否已设置支付密码\t是\t[string]\t查看\n" +
                "11\t identityCardNo\t身份证号码，AES加密。\t是\t[string]\t\n" +
                "12\t source\t访问终端类型 1:Mobile 2:PC\t是\t[string]\t查看\n" +
                "13\t userId\t通商云用户id\t是\t[string]\t查看\n" +
                "14\t isPhoneChecked\t是否绑定手机\t是\t[string]\t查看";
        //parm参数
        Pattern pattern=null;
        Matcher matcher=null;
        pattern=Pattern.compile("public String(.*?);");
        matcher=pattern.matcher(org);
        while (matcher.find()){
            try {
                String tmp1=matcher.group(1).replace("\"","").trim();
                System.out.println("statAll."+tmp1+"=statComp."+tmp1+";");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        pattern=Pattern.compile("TextView(.*?);");
        matcher=pattern.matcher(org);
        while (matcher.find()){
            try {
                String tmp1=matcher.group(1).replace("\"","").trim();
                System.out.println(""+tmp1+".setText(bean."+tmp1+");");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("-------------隔断--------------");
         pattern=Pattern.compile("(.*):(.*)");
         matcher=pattern.matcher(org);
        while (matcher.find()){
            try {
                String tmp1=matcher.group(1).replace("\"","").trim();
                String textview=tmp1.split("\\.")[0];
                System.out.println("result.put(\""+tmp1+"\","+textview+".getText().toString());");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("-------------隔断--------------");
        //

         pattern=Pattern.compile("(.*?):(.*)");
        matcher=pattern.matcher(org);
        while (matcher.find()){
            try {
                String tmp1=matcher.group(1).replace("\"","").trim();
                System.out.println("public String "+tmp1+";");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pattern=Pattern.compile("(.*?):(.*)");
        matcher=pattern.matcher(org);
        while (matcher.find()){
            try {
                String tmp1=matcher.group(1).replace("\"","").trim();
                System.out.println("bean."+tmp1+"=jsonObject.optString(\""+tmp1+"\");");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("-------------隔断--------------");
        pattern=Pattern.compile("(.*?)\n");
        matcher=pattern.matcher(org);
        while (matcher.find()){
            try {
                String tmp1=matcher.group(1).replace("\"","").trim();
                System.out.println("map.put(\""+tmp1+"\", \"\");");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
