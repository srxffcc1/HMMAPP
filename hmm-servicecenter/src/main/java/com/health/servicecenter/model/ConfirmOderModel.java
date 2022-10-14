package com.health.servicecenter.model;

public class ConfirmOderModel {
    public String ticket;
    public String subOrderId;

    public ConfirmOderModel(String ticket, String subOrderId) {
        this.ticket = ticket;
        this.subOrderId = subOrderId;
    }
}
