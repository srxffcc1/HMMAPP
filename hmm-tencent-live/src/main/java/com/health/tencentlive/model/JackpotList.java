package com.health.tencentlive.model;

import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.GoodsDetail;

import java.util.List;

public class JackpotList {
    public String name;
    public String id;
    public int numberOfInvitees;
    public int needNumberOfInvitees;
    public boolean finished;
    public List<HelpWinCouponList> helpWinCouponList;//完成后的优惠券列表
    public List<HelpWinGoodsList> helpWinGoodsList;//完成后的商品列表
    public List<HelpCouponList> helpCouponList;//未完成优惠券列表
    public List<HelpGoodsList> helpGoodsList;//未完成商品列表

    public class HelpWinCouponList {
        public String id;
        public String winId;
        public String itemRealId;
        public String helpItemId;
        public String helpId;
        public String num;
        public int status;
        public Detail detail;
    }

    public class HelpWinGoodsList {
        public String id;
        public String winId;
        public String itemRealId;
        public String num;
        public int status;
        public InteractionDetail.Goods detail;
    }

    public class HelpCouponList {
        public String id;
        public String itemRealId;
        public int num;
        public String helpId;
        public int status;
        public Detail detail;
    }

    public class HelpGoodsList {
        public String id;
        public String itemRealId;
        public String num;
        public String helpId;
        public int status;
        public InteractionDetail.Goods detail;
    }

    public class Detail {
        public CouponInfoZ basic;
    }
}
