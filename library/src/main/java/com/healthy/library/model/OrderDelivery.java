package com.healthy.library.model;

public class OrderDelivery {

    public String deliveryType;

    public String deliveryConsigneeName;

    public String deliveryConsigneePhone;

    public String deliveryConsigneeProvince;

    public String deliveryConsigneeProvinceName;

    public String deliveryConsigneeCity;

    public String deliveryConsigneeCityName;

    public String deliveryConsigneeDistrict;

    public String deliveryConsigneeDistrictName;

    public String deliveryConsigneeAddress;

    public String deliveryLatitude;

    public String deliveryLongitude;

    public String deliveryShopId;

    public String deliveryShopDeptId;

    public String deliveryDate;

    public String deliveryTime;

    public String remark;

    public OrderDelivery getFlash(){
        OrderDelivery orderDelivery=new OrderDelivery();
        orderDelivery.deliveryType=deliveryType;
        orderDelivery.deliveryShopId=deliveryShopId;
        orderDelivery.deliveryDate=deliveryDate;
        orderDelivery.deliveryTime=deliveryTime;
        orderDelivery.remark=remark;
        if("10".equals(deliveryType)||"20".equals(deliveryType)){

        }else {
            orderDelivery.deliveryLatitude=deliveryLatitude;
            orderDelivery.deliveryLongitude=deliveryLongitude;
            orderDelivery.deliveryConsigneeName=deliveryConsigneeName;
            orderDelivery.deliveryConsigneePhone=deliveryConsigneePhone;
            orderDelivery.deliveryConsigneeProvince=deliveryConsigneeProvince;
            orderDelivery.deliveryConsigneeProvinceName=deliveryConsigneeProvinceName;
            orderDelivery.deliveryConsigneeCity=deliveryConsigneeCity;
            orderDelivery.deliveryConsigneeCityName=deliveryConsigneeCityName;
            orderDelivery.deliveryConsigneeDistrict=deliveryConsigneeDistrict;
            orderDelivery.deliveryConsigneeDistrictName=deliveryConsigneeDistrictName;
            orderDelivery.deliveryConsigneeAddress=deliveryConsigneeAddress;
        }
        return orderDelivery;
    }
}
