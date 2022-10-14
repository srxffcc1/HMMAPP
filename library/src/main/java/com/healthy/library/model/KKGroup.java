package com.healthy.library.model;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.util.List;

public class KKGroup {


    public String id;
    public String getMerchantName() {
        if(!TextUtils.isEmpty(merchantShortName)){
            return merchantShortName;
        }
        return merchantName;
    }

    private String merchantName;
    private String merchantShortName;
    public String merchantId;
    public int goodsId;
    public String goodsTitle;
    public String goodsImage;
    public double goodsStorePrice;
    public double goodsPlatformPrice;
    public double assemblePrice;
    public double retailPrice;
    public int regimentSize;
    public int regimentTimeLength;
    public int assembleInventory;
    public String startTime;
    public String endTime;
    public long assembleStatus;
    public String createTime;
    public long joinNum;
    public long salesMax;
    public String shopId;
    public int goodsType;
    public double distance;
    public String shopName;
    public String addressDetails;
    public String goodsDescription;
    public Bitmap bitmap;


    public long orderId;
    public String assembleId;
    public String teamNum;
    public double payPrice;
    public String statusStr;
    public int assembleType;
    public String regimentTime;
    public String joinTime;
    public int remainderNum;
    public int orderStatus;
    public Bitmap bitmapShre;

    public List<TeamMemberList> teamMemberList;
    public List<String> faceUrlList;

    public class TeamMemberList {
        public int commanderStatus;
        public String memberId;
        public String memberPhone;
        public String memberFaceUrl;
        public String memberNickName;
    }


}
