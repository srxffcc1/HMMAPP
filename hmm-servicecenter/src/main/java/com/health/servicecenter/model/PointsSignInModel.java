package com.health.servicecenter.model;

import java.util.List;

/**
 * @author: long
 * @date: 2021/4/17
 * @des
 */
public class PointsSignInModel {

    /** 连续签到次数*/
    public String seriesSignNum;
    /** 签到获得积分*/
    public String signIntegral;
    public String signWeek;
    /** 总积分*/
    public String totalScore;
    /** 签到剩余次数*/
    public int signNum;

    public List<MemberSignRecords> memberSignRecords;

    public class MemberSignRecords {
        /** 根据id判断是否签到过*/
        public String id;
        /** 签到获得积分*/
        public int signIntegral;
        /** 签到星期：0-星期天，1-星期一，2-星期二，3-星期三，4-星期四，5-星期五，6-星期六*/
        public int signWeek;
    }

}
