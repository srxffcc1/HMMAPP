package com.health.tencentlive.model;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.List;

public class InteractionDetail {
    public String id;
    public String interactiveId;
    public String lotteryTime;
    public List<Goods> goodsList;
    public int status;
    public int numberOfWinners;
    public int joinCount;
    public boolean win;
    public boolean join;
    public List<Condition> conditionList;
    public List<Roster> rosterList;

    public class Condition {
        public int conditionId;
        public String name;
        public String content;
        public boolean finished;
    }

    public class Roster {
        public Member member;
        public boolean self;
        public String id;
        public String mapInteractiveRosterGoodsId;
    }

    public class Member {
        public String name;
        public String nickName;
        public String faceUrl;
    }

    public class Goods {
        public String id;
        public String goodsId;
        public String goodsChildId;
        public String mapMarketingGoodsId;
        public String barcodeSku;
        public String goodsTitle;
        public double retailPrice;
        public double storePrice;
        public double platformPrice;
        public double marketingPrice;
        public int maxInventory;
        public int availableInventory;
        public String goodsType;
        public String[] shopIds;
        public String goodsSpecStr;
        public int lockedInventory;
        public int totalInventory;
        public int sales;
        public int offShelf;
        public String commissionRateShop;
        public String commissionRateUser;
        public String commissionRateShopIsOff;
        public String commissionRateUserIsOff;
        public String goodsImage;

        public String getGoodsTitle() {
            if (TextUtils.isEmpty(goodsTitle)) {
                return "";
            }
            return goodsTitle;
        }

        public String getGoodsImage() {
            if (TextUtils.isEmpty(goodsImage)) {
                return "";
            }
            return goodsImage;
        }
    }
}
