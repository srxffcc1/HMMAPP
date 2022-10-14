package com.healthy.library.model;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/04 17:26
 * @des 商品model
 */

public class ShopSub {

    public int userId;

    public int homeAppointmentStatus;

    public int shopAppointmentStatus;

    public int pcAppointmentMsgStatus;

    public int phoneAppointmentMsgStatus;

    public String serviceStartTime = "9:00";

    public String serviceEndTime = "18:00";

    public int oneHousMaxNum;

    public int advanceDays = 7;


    public String addressProvince;
    public String addressCity;
    public String addressArea;
    public int shopSetStatus;


}
