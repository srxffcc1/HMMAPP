package com.healthy.library.model;

import java.io.Serializable;

public class OrderGoodsPackage implements Serializable  {
        public String packageId;
        public String packageQuantity;

        public OrderGoodsPackage(String packageId, String packageQuantity) {
                this.packageId = packageId;
                this.packageQuantity = packageQuantity;
        }
}
