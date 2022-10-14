package com.healthy.library.model;

import android.text.TextUtils;

import com.healthy.library.R;


public class ToolsDiarySleep extends ToolsSumType {


    public String feedingDateTime;

    public String sleepTime;

    public String wakeUpTime;

    public String sleepMemo;

    public String wakeUpMemo;

    public String recordName;

    public String getTimeleft() {
        if (!TextUtils.isEmpty(recordName)) {
            if ("醒来".equals(recordName)) {
                return wakeUpTime;
            } else {

                return sleepTime;
            }
        } else {

            return feedingDateTime;
        }
    }

    public int getImageLeftRes() {
        if (TextUtils.isEmpty(recordName)) {

            return R.drawable.tools_menu7;
        } else {
            if ("醒来".equals(recordName)) {

                return R.drawable.tools_menu8;
            } else {

                return R.drawable.tools_menu7;
            }
        }
    }

    public int getImageRightRes() {
        return R.drawable.tools_menu8;
    }

    public String getLeftTitle() {
        if (TextUtils.isEmpty(recordName)) {
            if (!TextUtils.isEmpty(wakeUpMemo)) {
                return "睡觉" + " " + wakeUpMemo;
            } else {
                return "睡觉";
            }

        } else {
            if ("醒来".equals(recordName)) {
                if(!TextUtils.isEmpty(wakeUpMemo)){

                    return recordName + " " + wakeUpMemo;
                }else {

                    return recordName;
                }
            } else {
                if(!TextUtils.isEmpty(sleepMemo)){

                    return recordName + " " + sleepMemo;
                }else {
                    return recordName;
                }

            }

        }
    }

    public String getRightTitle() {
        if (TextUtils.isEmpty(recordName)) {

            if (!TextUtils.isEmpty(sleepMemo)) {
                return "醒来" + " " + sleepMemo;
            } else {
                return "醒来";
            }
        } else {
            return recordName;
        }
    }

    public String getLeftTime() {
        return sleepTime;
    }

    public String getRightTime() {
        return wakeUpTime;
    }
}
