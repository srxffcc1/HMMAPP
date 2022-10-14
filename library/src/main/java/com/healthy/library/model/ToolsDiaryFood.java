package com.healthy.library.model;

import android.text.TextUtils;

import com.healthy.library.R;


public class ToolsDiaryFood extends ToolsSumType{


        public String feedingDateTime;

        public String itemName;

        public String eatingTime;

        public String memo;

        public String getTimeleft() {
                return feedingDateTime;
        }

        public int getImageLeftRes() {
                if (recordType == 3) {
                        return R.drawable.tools_menu3;
                } else {
                        return R.drawable.tools_menu4;
                }
        }

        public int getImageRightRes() {
                return -1;
        }

        public String getLeftTitle() {
                String result=";";
                if (recordType == 3) {
                        result="辅食 "+itemName;
                        if(!TextUtils.isEmpty(memo)){
                                result=result+" "+memo;
                        }
                } else {
                        result="药物 "+itemName;
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
