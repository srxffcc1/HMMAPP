package com.healthy.library.model;

import android.text.TextUtils;


import com.healthy.library.R;
import com.healthy.library.utils.DateUtils;

public class ToolsDiaryMK extends ToolsSumType {



    public String feedingDateTime;

    public String leftBeginDateTime;

    public String leftKeepTime;

    public String rightBeginDateTime;

    public String rightKeepTime;

    public String milkVolume;

    public String operate;

    public String memo;



    public String getTimeleft() {
        return feedingDateTime;
    }

    public int getImageLeftRes() {
        if (recordType == 1) {
            return R.drawable.tools_menu1;
        } else {
            return R.drawable.tools_menu2;
        }
    }

    public int getImageRightRes() {
        return -1;
    }

    public String getLeftTitle() {
        String result = "";
        String sleeptimeString="";
        if (recordType == 1) {
            result = "母乳 ";
            if (!TextUtils.isEmpty(leftKeepTime)&&!"0".equals(leftKeepTime)) {

                String[] timetip=DateUtils.getDistanceTimeOnlySecondNoDouble(Long.parseLong(leftKeepTime)*1000);
                if(!"0".equals(timetip[0])){
                    sleeptimeString=sleeptimeString+timetip[0]+"天";
                }
                if(!"0".equals(timetip[1])){
                    sleeptimeString=sleeptimeString+timetip[1]+"时";
                }
                if(!"0".equals(timetip[2])){
                    sleeptimeString=sleeptimeString+timetip[2]+"分";
                }
                if(!"0".equals(timetip[3])){
                    sleeptimeString=sleeptimeString+timetip[3]+"秒";
                }
                result = result + "左 " + sleeptimeString+" ";

            }
            if (!TextUtils.isEmpty(rightKeepTime)&&!"0".equals(rightKeepTime)) {
                sleeptimeString="";
                String[] timetip=DateUtils.getDistanceTimeOnlySecondNoDouble(Long.parseLong(rightKeepTime)*1000);
                if(!"0".equals(timetip[0])){
                    sleeptimeString=sleeptimeString+timetip[0]+"天";
                }
                if(!"0".equals(timetip[1])){
                    sleeptimeString=sleeptimeString+timetip[1]+"时";
                }
                if(!"0".equals(timetip[2])){
                    sleeptimeString=sleeptimeString+timetip[2]+"分";
                }
                if(!"0".equals(timetip[3])){
                    sleeptimeString=sleeptimeString+timetip[3]+"秒";
                }
                result = result + "右 " + sleeptimeString+" ";


            }
            if(!TextUtils.isEmpty(memo)){
                result=result+" "+memo;
            }
        } else {
            result = "配方奶 " + milkVolume+"ml";
            if(!TextUtils.isEmpty(memo)){
                result=result+" "+memo;
            }
        }

        return result;
    }

    public String getRightTitle() {
        return "";
    }

    public String getLeftTime() {
        return feedingDateTime;
    }

    public String getRightTime() {
        return "";
    }
}
