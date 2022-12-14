package com.healthy.library.model;


import com.healthy.library.R;

public class ToolsCETypeList {

                public long id;

                public int foodType;

                public String foodName;

                public String foodAliasName;

                public String image;

                public int pregnantStatus;

                public String pregnantContent;

                public int puerperaStatus;

                public String puerperaContent;

                public int sucklingPeriodStatus;

                public String sucklingPeriodContent;

                public int babyStatus;

                public String babyContent;

                public String nutritionalValue;

                public String shoppingGuide;

                public String edibleSkills;

                public String tips;

                public String createDate;

                public String updateDate;

                public String createUser;

                public int isDelete;

                public int status;

                public String recommendIndex;

                public String appStage;

                public String eatStage;


    public int getCanEatImgRes(String ftype){
        if("1".equals(ftype)){
            if(pregnantStatus==1){
                return R.drawable.tools_eat_tip2;
            }
            if(pregnantStatus==2){
                return R.drawable.tools_eat_tip2_1;
            }
            if(pregnantStatus==3){
                return R.drawable.tools_eat_tip3;
            }
            if(pregnantStatus==4){
                return R.drawable.tools_eat_tip1;
            }
        }
        if("2".equals(ftype)){
            if(puerperaStatus==1){
                return R.drawable.tools_eat_tip2;
            }
            if(puerperaStatus==2){
                return R.drawable.tools_eat_tip2_1;
            }
            if(puerperaStatus==3){
                return R.drawable.tools_eat_tip3;
            }
            if(puerperaStatus==4){
                return R.drawable.tools_eat_tip1;
            }
        }
        if("3".equals(ftype)){
            if(sucklingPeriodStatus==1){
                return R.drawable.tools_eat_tip2;
            }
            if(sucklingPeriodStatus==2){
                return R.drawable.tools_eat_tip2_1;
            }
            if(sucklingPeriodStatus==3){
                return R.drawable.tools_eat_tip3;
            }
            if(sucklingPeriodStatus==4){
                return R.drawable.tools_eat_tip1;
            }
        }
        if("4".equals(ftype)){
            if(babyStatus==1){
                return R.drawable.tools_eat_tip2;
            }
            if(babyStatus==2){
                return R.drawable.tools_eat_tip2_1;
            }
            if(babyStatus==3){
                return R.drawable.tools_eat_tip3;
            }
            if(babyStatus==4){
                return R.drawable.tools_eat_tip1;
            }
        }
        return R.drawable.tools_eat_tip1;
    }


    public String getCanEatStringRes(){
        String result="";

        if(pregnantStatus==1){
            result+="???????????????,";
        }
        if(pregnantStatus==2){
            result+= "????????????,";
        }
        if(pregnantStatus==3){
            result+= "????????????,";
        }
        if(pregnantStatus==4){
            result+= "???????????????,";
        }

        if(puerperaStatus==1){
            result+= "???????????????,";
        }
        if(pregnantStatus==2){
            result+= "????????????,";
        }
        if(puerperaStatus==3){
            result+= "????????????,";
        }
        if(puerperaStatus==4){
            result+= "???????????????,";
        }
        if(sucklingPeriodStatus==1){
            result+= "??????????????????,";
        }
        if(sucklingPeriodStatus==2){
            result+= "???????????????,";
        }
        if(sucklingPeriodStatus==3){
            result+= "???????????????,";
        }
        if(sucklingPeriodStatus==4){
            result+= "??????????????????,";
        }
        if(babyStatus==1){
            result+= "??????????????????";
        }
        if(babyStatus==2){
            result+= "???????????????";
        }
        if(babyStatus==3){
            result+= "???????????????";
        }
        if(babyStatus==4){
            result+= "?????????????????????";
        }
        return result;
    }

}
