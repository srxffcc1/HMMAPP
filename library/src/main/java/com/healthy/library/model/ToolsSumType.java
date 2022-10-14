package com.healthy.library.model;



public abstract class ToolsSumType {


    public String childId;
    public String recordId;
    public int recordType;
    public abstract String getTimeleft() ;

    public abstract int getImageLeftRes();

    public abstract int getImageRightRes() ;

    public abstract String getLeftTitle() ;

    public abstract String getRightTitle() ;

    public abstract String getLeftTime();

    public abstract String getRightTime() ;

//    public ToolsSumType() {
//    }
//
//    public ToolsSumType(String timeleft, int imageLeftRes, String leftTitle, String leftTime) {
//        this.timeleft = timeleft;
//        this.imageLeftRes = imageLeftRes;
//        this.leftTitle = leftTitle;
//        this.leftTime = leftTime;
//    }
//
//    public ToolsSumType(String timeleft, int imageLeftRes, int imageRightRes, String leftTitle, String rightTitle, String leftTime, String rightTime) {
//        this.timeleft = timeleft;
//        this.imageLeftRes = imageLeftRes;
//        this.imageRightRes = imageRightRes;
//        this.leftTitle = leftTitle;
//        this.rightTitle = rightTitle;
//        this.leftTime = leftTime;
//        this.rightTime = rightTime;
//    }
}
