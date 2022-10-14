package com.healthy.library.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderGroupCopy implements Serializable  {
        public OrderGroupCopy(OrderDeliver deliver, List<OrderGoodsDetail> details) {
                this.deliver = deliver;
                this.details = details;
        }

        public OrderGroupCopy() {
        }

        public OrderDeliver deliver=new OrderDeliver();

        public List<OrderGoodsDetail> details=new ArrayList<>();



}
