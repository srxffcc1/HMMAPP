package com.health.servicecenter.model;

public class RefundDetails {
    public String orderDetailId;
    public String refundQuantity;

    public RefundDetails(String orderDetailId, String refundQuantity) {
        this.orderDetailId = orderDetailId;
        this.refundQuantity = refundQuantity;
    }
}
