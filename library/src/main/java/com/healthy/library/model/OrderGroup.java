package com.healthy.library.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderGroup implements Serializable {

    public OrderDeliver deliver = new OrderDeliver();

    public List<OrderGoodsDetail> details = new ArrayList<>();

    public GoodsShop goodsShop = new GoodsShop();//组选择的商店

    public String[] shopIdList = new String[]{};


    public int type = 2;// 1 服务 2 标品

    public boolean isShow = false;
    public String goodsName;


    public GoodsFee goodsFee = new GoodsFee();//邮费

    public OrderGroupCopy getOrderGroupCopy() {
        if (details.size() == 0) {
            return null;
        }
        return new OrderGroupCopy(deliver, details);
    }

}
