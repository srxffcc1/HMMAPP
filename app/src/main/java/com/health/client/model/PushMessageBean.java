package com.health.client.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PushMessageBean implements Serializable {
    public String pushType="1";
    public String id;
    @SerializedName("memberCouponId")
    public String extraId;
}
