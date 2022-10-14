package com.healthy.library.model;

public class OrderBackModel {


        public long orderGoodsId;
        public long orderId;
        public long goodsId;
        public String goodsTitle;
        public int goodsNumber;
        public double goodsPrice;
        public double goodsSubtotal;
        public double payMoney;
        public int canRefundGoodsNumber;
        public int toUseCoupon;
        public double discountMoney;
        public int selectCount;
        public double rpayMoney;

        @Override
        public String toString() {
                return "OrderBackModel{" +
                        "orderGoodsId=" + orderGoodsId +
                        ", orderId=" + orderId +
                        ", goodsId=" + goodsId +
                        ", goodsTitle='" + goodsTitle + '\'' +
                        ", goodsNumber=" + goodsNumber +
                        ", goodsPrice=" + goodsPrice +
                        ", goodsSubtotal=" + goodsSubtotal +
                        ", payMoney=" + payMoney +
                        ", canRefundGoodsNumber=" + canRefundGoodsNumber +
                        ", toUseCoupon=" + toUseCoupon +
                        ", discountMoney=" + discountMoney +
                        ", selectCount=" + selectCount +
                        ", rpayMoney=" + rpayMoney +
                        '}';
        }
}
