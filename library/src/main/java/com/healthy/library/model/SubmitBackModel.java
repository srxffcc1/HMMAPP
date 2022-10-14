package com.healthy.library.model;

import java.io.Serializable;

public class SubmitBackModel implements Serializable {
    public String id;
    public String orderId;
    public String orderNum;
    public String goodsId;
    public int goodsSpec;
    public String goodsTitle;
    public String goodsSpecDesc;
    public String goodsImage;
    public Object barcodeSku;
    public int goodsQuantity;
    public double goodsPrice;
    public double goodsPreferentialAmount;
    public double goodsReductionAmount;
    public double goodsPayAmount;
    public double totalAmount;
    public double totalPreferentialAmount;
    public double totalReductionAmount;
    public double totalPayAmount;
    public double totalPayPoint;
    public int refundTotalQuantity;
    public int allowApplyRefund;
    public double refundTotalAmount;
    public int isAddition;
    public Object additionType;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;

    public SubmitBackModel() {
    }

    public SubmitBackModel(String id, String goodsId, String goodsTitle, String goodsSpecDesc, String goodsImage, int goodsQuantity, double goodsPayAmount) {
        this.id = id;
        this.goodsId = goodsId;
        this.goodsTitle = goodsTitle;
        this.goodsSpecDesc = goodsSpecDesc;
        this.goodsImage = goodsImage;
        this.goodsQuantity = goodsQuantity;
        this.goodsPayAmount = goodsPayAmount;
    }
    public SubmitBackModel(String id, long String, String goodsTitle, String goodsSpecDesc, String goodsImage, int goodsQuantity, double goodsPayAmount,double totalPayPoint) {
        this.id = id;
        this.goodsId = goodsId;
        this.goodsTitle = goodsTitle;
        this.goodsSpecDesc = goodsSpecDesc;
        this.goodsImage = goodsImage;
        this.goodsQuantity = goodsQuantity;
        this.goodsPayAmount = goodsPayAmount;
        this.totalPayPoint = totalPayPoint;
    }
    public SubmitBackModel(String id, String goodsId, String goodsTitle, String goodsSpecDesc, String goodsImage, int goodsQuantity, double goodsPayAmount,int refundTotalQuantity) {
        this.id = id;
        this.goodsId = goodsId;
        this.goodsTitle = goodsTitle;
        this.goodsSpecDesc = goodsSpecDesc;
        this.goodsImage = goodsImage;
        this.goodsQuantity = goodsQuantity;
        this.goodsPayAmount = goodsPayAmount;
        this.refundTotalQuantity = refundTotalQuantity;
    }




}
