package com.healthy.library.model;


import com.healthy.library.R;

public class ToolsCEList {


                public long id;

                public int foodType;

                public String foodName;

                public String foodAliasName;

                public String image;

                public int pregnantStatus;

                public int puerperaStatus;

                public int sucklingPeriodStatus;

                public int babyStatus;

                public String address;

                public int isCurrentArea;

                public int getCanEatImgRes(String ftype){//1孕期 2坐月子 3脯乳期 4婴幼儿
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
                        if(pregnantStatus==2){
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
                 result+="孕期放心吃,";
            }
            if(pregnantStatus==2){
                result+= "孕期少吃,";
            }
            if(pregnantStatus==3){
                result+= "孕期慎吃,";
            }
            if(pregnantStatus==4){
                result+= "孕期不能吃,";
            }

            if(puerperaStatus==1){
                result+= "月子放心吃,";
            }
            if(pregnantStatus==2){
                result+= "月子少吃,";
            }
            if(puerperaStatus==3){
                result+= "月子慎吃,";
            }
            if(puerperaStatus==4){
                result+= "月子不能吃,";
            }
            if(sucklingPeriodStatus==1){
                result+= "哺乳期放心吃,";
            }
            if(sucklingPeriodStatus==2){
                result+= "哺乳期少吃,";
            }
            if(sucklingPeriodStatus==3){
                result+= "哺乳期慎吃,";
            }
            if(sucklingPeriodStatus==4){
                result+= "哺乳期不能吃,";
            }
            if(babyStatus==1){
                result+= "婴幼儿放心吃";
            }
            if(babyStatus==2){
                result+= "婴幼儿少吃";
            }
            if(babyStatus==3){
                result+= "婴幼儿慎吃";
            }
            if(babyStatus==4){
                result+= "婴幼儿不能吃吃";
            }
            return result;
    }




}
