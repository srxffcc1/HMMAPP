package com.healthy.library.model;

import android.text.TextUtils;

public class OrderSubShopListModel {

    public String shopId;
    public String merchantId;
    public String shopName;
    public String chainShopName;
    public String appointmentPhone;
    public String addressDetails;
    public double longitude;
    public double latitude;
    public double distances;

    public String getAppointmentPhone() {
        if (TextUtils.isEmpty(appointmentPhone) || appointmentPhone == null) {
            return "";
        }
        return appointmentPhone;
    }

    public void setAppointmentPhone(String appointmentPhone) {
        this.appointmentPhone = appointmentPhone;
    }
}
