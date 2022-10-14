package com.healthy.library.model;

import android.content.Context;
import android.util.Base64;

import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import java.util.List;

public class AssemableTeam {
    public String teamNum;
    public String memberNickName;
    public String memberFaceUrl;
    public String regimentTime;
    public String realEndTime;
    public int regimentTimeLength;
    public String remainderNum;
    public int regimentSize;
    public List<TeamMember> teamMemberList;

    public boolean isMyTeam(Context mContext){
        boolean result=false;
        if(teamMemberList!=null&&teamMemberList.size()>0){
            for (int i = 0; i <teamMemberList.size() ; i++) {
                if((teamMemberList.get(i).memberId+"").equals(new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))){//id包含
                    result=true;
                }
            }
        }
        return result;
    }


    public  class TeamMember {

        public int commanderStatus;
        public String memberId;
        public String memberPhone;
        public String memberNickName;
        public String memberFaceUrl;

    }
}
