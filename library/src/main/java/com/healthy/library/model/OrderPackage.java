package com.healthy.library.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderPackage implements Serializable  {

        public OrderPackage(OrderDelivery delivery) {
                this.delivery=delivery;
        }

        public OrderDelivery delivery=new OrderDelivery();

        public List<OrderPackageGoodsDetail> details=new ArrayList<>();

        public  BargainParams bargainParams;

        public AssembleParams assembleParams;

//        public List<KeyVExtra> attributes;

}
