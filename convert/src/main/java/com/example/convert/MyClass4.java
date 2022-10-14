package com.example.convert;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass4 {
    private static DecimalFormat sKeep2DecimalsFormat = new DecimalFormat("0.##");
    public static void main(String[] args) {
        String org="4\t id\t主键ID\t是\t[string]\t查看\n" +
                "5\t groupName\t群名称\t是\t[string]\t查看\n" +
                "6\t merchantId\t商户id\t是\t[string]\t查看\n" +
                "7\t personId\t员工id(群主)\t是\t[string]\t查看\n" +
                "8\t personName\t员工名称（群主名称）\t是\t[string]\t查看\n" +
                "9\t groupIntroduce\t群介绍\t是\t[string]\t\n" +
                "10\t groupSort\t排序号 越小越靠前\t是\t[string]\t查看\n" +
                "11\t createTime\t\t是\t[string]\t查看\n" +
                "12\t updateTime\t\t是\t[string]\t查看";
        Pattern pattern=null;
        Matcher matcher=null;
        pattern= Pattern.compile(".+");
        matcher=pattern.matcher(org);
        while (matcher.find()){
            //System.out.println("行"+matcher.group());
            String line=matcher.group();
            String[] linarray=line.split("\t");
            System.out.println("public String "+linarray[1].trim()+";");
        }
//        System.out.println("javascript:(function(){ "
//                + "var objs = document.getElementsByTagName('img');"
//                + " var array=new Array(); " + " for(var j=0;j<objs.length;j++){ "
//                + "array[j]=objs[j].src;" + " }  "
//                + "for(var i=0;i<objs.length;i++){"
//                + "objs[i].i=i;"
//                + "objs[i].onclick=function(){  window.JsBridge.openImage(this.src,array,this.i);" + "}  " + "}    })()");

//        System.out.println("javascript:(function () {" +
//                "    let objs = document.getElementsByTagName('img');" +
//                "    let imgList = new Array();" +
//                "    for (let j = 0; j < objs.length; j++) {" +
//                "        let parent = objs[j].parentNode;" +
//                "        if (parent && parent.nodeName.toLowerCase() === 'a') {" +
//                "" +
//                "        } else {" +
//                "            imgList.push(objs[j]);" +
//                "        }" +
//                "    }" +
//                "" +
//                "" +
//                "    let array = new Array();" +
//                "    for (let i = 0; i < imgList.length; i++) {" +
//                "        array[i] = imgList[i].src;" +
//                "    }" +
//                "    for (let i = 0; i < imgList.length; i++) {" +
//                "        imgList[i].i = i;" +
//                "        imgList[i].onclick = function () {" +
//                "            window.JsBridge.openImage(this.src, array, this.i);" +
//                "        }" +
//                "    }" +
//                "})()");
//        char c;
//
//        for(c = 'a'; c <= 'j'; ++c)
//
//            System.out.println(("srxmc")+(c + ""));
//        System.out.println(getBirAgeSex("320321199602061275"));

    }

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

}
