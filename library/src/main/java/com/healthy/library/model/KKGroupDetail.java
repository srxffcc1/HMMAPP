package com.healthy.library.model;

import java.util.ArrayList;
import java.util.List;

public class KKGroupDetail {
    public KKGroupDetail() {
    }

    public String assembleId;
    public String teamNum;
    public String goodsId;
    public String goodsTitle;
    public String goodsImage;
    public double goodsStorePrice;
    public double goodsPlatformPrice;
    public double assemblePrice;
    public int regimentSize;
    public int regimentTimeLength;
    public int assembleInventory;
    public String startTime;
    public String endTime;
    public int assembleStatus;
    public int regimentStatus;
    public int salesMax;
    public int goodsType;
    public int memberSuccessNum;
    //        public ApplyShop applyShopDTO;
    public String regimentTime;
    public List<TeamMember> teamMemberList=new ArrayList<>();
    public int joinStatus;
    private String whomaster;
    public String shopId;
    public String shopName;
    public String addressDetails;
    public double distance;
    public String addressAreaName;

    public String getWhomaster() {
        if (teamMemberList != null && teamMemberList.size() > 0) {
            for (int i = 0; i < teamMemberList.size(); i++) {
                if (teamMemberList.get(i).commanderStatus == 1) {
                    whomaster = teamMemberList.get(i).memberNickName;
                    break;
                }
            }
        }
        return whomaster;
    }

    public class TeamMember {

        public int commanderStatus;
        public String memberId;
        public String memberPhone;
        public String memberNickName;
        public String memberFaceUrl;

    }

    public class ApplyShop {

        public int shopId;
        public String shopName;
        public String addressDetails;
        public double distance;
        public String addressAreaName;
    }

}
