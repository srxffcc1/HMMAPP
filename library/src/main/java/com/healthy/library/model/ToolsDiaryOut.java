package com.healthy.library.model;

import android.text.TextUtils;

import com.healthy.library.R;


public class ToolsDiaryOut extends ToolsSumType{



        public String feedingDateTime;

        public String defecationColor;

        public String defecationVolume;

        public String defecationShape;

        public String hasPee;

        public String defecationTime;

        public String memo;


        public String getTimeleft() {
                return feedingDateTime;
        }

        public int getImageLeftRes() {
                if (recordType == 5) {
                        return R.drawable.tools_menu5;
                } else {
                        return R.drawable.tools_menu6;
                }
        }

        public int getImageRightRes() {
                return -1;
        }

        public String getLeftTitle() {
                String result="";
                if (recordType == 5) {
                        result="小便 "+defecationColor+" "+defecationVolume;

                        if(!TextUtils.isEmpty(memo)){
                                result=result+" "+memo;
                        }
                } else {
                        result="大便 "+defecationShape+" "+defecationColor+" "+defecationVolume;
                        if("1".equals(hasPee)){
                                result=result+" "+"有尿";
                        }else {

                                result=result+" "+"无尿";
                        }
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
