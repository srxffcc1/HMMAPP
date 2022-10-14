package com.example.convert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass2 {
    public static void main(String[] args) {
//        Date date=new Date(1556948220*1000L);
//        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
//        //System.out.println(simpleDateFormat.format(date));

        //System.out.println("5rWL6K%2BV".replace("%2B",""));

        //System.out.println(formatLongAll("1561824000"));


//        List<String> mcartattrvaluelist=new ArrayList<>();
//        mcartattrvaluelist.add("3L,白色");
//        mcartattrvaluelist.add("3L,黑色");
//        mcartattrvaluelist.add("4L,白色");
//        mcartattrvaluelist.add("4L,黑色");
//        Map<String,String> destmap=new HashMap<>();
//        destmap.put("大小","3L");
//        destmap.put("颜色","黑色");
//        //System.out.println(checkWhichChose(mcartattrvaluelist,destmap));
    }
    public static int checkWhichChose(List<String> mcartattrvaluelist, Map<String,String> destmap) {
        for (int i = 0; i <mcartattrvaluelist.size() ; i++) {
            String suk=mcartattrvaluelist.get(i);//得到精准规格
            boolean check=true;
            for (Map.Entry<String, String> entry : destmap.entrySet()) {//迭代规格选择
                check=check&suk.contains(entry.getValue());
                if(!check){
                    break;
                }
            }
            if(check){
                return i;
            }
        }
        return 0;
    }

    public static String formatLongAll(String longtime) {
        try {
            long longs =Long.parseLong(longtime)*1000L;
            Date date=new Date();
            date.setTime(longs);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(date);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return longtime;
        }
    }

}
