package com.health.faq.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author Li
 * @date 2019/07/18 14:10
 * @des 专家主页详情
 */
public class ExpertHomepageModel implements MultiItemEntity {

    private int mItemType;
    private boolean online;
    private HeaderInfo mHeaderInfo;
    private Answer mAnswer;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ANSWER = 2;
    public static final int TYPE_NO_ANSWER = 3;

    public ExpertHomepageModel(int itemType) {
        mItemType = itemType;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public HeaderInfo getHeaderInfo() {
        return mHeaderInfo;
    }

    public void setHeaderInfo(HeaderInfo headerInfo) {
        mHeaderInfo = headerInfo;
    }

    public Answer getAnswer() {
        return mAnswer;
    }

    public void setAnswer(Answer answer) {
        mAnswer = answer;
    }

    public static class HeaderInfo {
        public double consultingFees;
        public String id;
        public String faceUrl;
        public String realName;
        public String avgReplyMinutes;
        public String askTimes;
        public String rank;
        public String goodSkills;
        public String expertIntroduction;
        public int answerCount;
        public String cost;
        public boolean online;
    }

    public static class Answer {
        public String answerId;
        public String questionId;
        public String questionTitle;
        public String date;
        public String faceUrl;
        public int readCount;

    }
}
