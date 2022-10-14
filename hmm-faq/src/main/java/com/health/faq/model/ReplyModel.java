package com.health.faq.model;

import java.util.Date;

/**
 * @author Li
 * @date 2019/07/02 16:08
 * @des 问题回复
 */
public class ReplyModel {

    private String id;
    private String questionId;
    private String faceUrl;
    private String nickname;
    private String replyDetail;
    private String soundUrl;
    private Date replyDate;
    private boolean isAudioReply;
    private boolean isMyQuestionReply;
    private boolean showDate;
    private String mDuration;

    public String getDuration() {
        return mDuration == null ? "" : mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }

    public boolean isMyQuestionReply() {
        return isMyQuestionReply;
    }

    public void setMyQuestionReply(boolean myQuestionReply) {
        isMyQuestionReply = myQuestionReply;
    }

    public boolean isAudioReply() {
        return isAudioReply;
    }

    public void setAudioReply(boolean audioReply) {
        isAudioReply = audioReply;
    }

    public String getSoundUrl() {
        return soundUrl == null ? "" : soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId == null ? "" : questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getFaceUrl() {
        return faceUrl == null ? "" : faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getReplyDetail() {
        return replyDetail == null ? "" : replyDetail;
    }

    public void setReplyDetail(String replyDetail) {
        this.replyDetail = replyDetail;
    }

}
