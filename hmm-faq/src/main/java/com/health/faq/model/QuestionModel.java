package com.health.faq.model;

import java.util.Date;

/**
 * @author Li
 * @date 2019/06/28 13:44
 * @des 问题
 */

public class QuestionModel {


    /**
     * 问题id
     */
    private String questionId;
    /**
     * 提问人头像url
     */
    private String questionerAvatar;

    /**
     * 提问人昵称
     */
    private String questionerNickname;

    /**
     * 提问人所处阶段描述
     */
    private String questionerPeriodDes;

    /**
     * 提问人所处阶段
     * 1.备孕 2.怀孕中 3.产后
     */
    private int questionerPeriod;

    /**
     * 提问价格
     */
    private String questionCost;

    /**
     * 回答数量
     */
    private int answerNum;

    /**
     * 问题标题
     */
    private String questionTitle;

    /**
     * 问题内容
     */
    private String questionContent;

    /**
     * 问题状态1-进行中2-已关闭 3-已完成 4- 异常
     */
    private String status;

    /**
     * 提问时间
     */
    private Date createDate;

    /**
     * 问题图片
     */
    private String[] photos;

    /**
     * 赏金是否退回
     */
    private boolean hasReturn;

    /**
     * 显示上方求助详情文字
     */
    private boolean showSupTitle;

    /**
     * 阅读数
     */
    private int readCount;

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public boolean isShowSupTitle() {
        return showSupTitle;
    }

    public void setShowSupTitle(boolean showSupTitle) {
        this.showSupTitle = showSupTitle;
    }

    public boolean isHasReturn() {
        return hasReturn;
    }

    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }

    public String[] getPhotos() {
        return photos;
    }

    public void setPhotos(String[] photos) {
        this.photos = photos;
    }

    public Date getCreateDate() {
        return createDate == null ? new Date() : createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuestionId() {
        return questionId == null ? "" : questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getQuestionerPeriod() {
        return questionerPeriod;
    }

    public void setQuestionerPeriod(int questionerPeriod) {
        this.questionerPeriod = questionerPeriod;
    }

    public String getQuestionerAvatar() {
        return questionerAvatar == null ? "" : questionerAvatar;
    }

    public void setQuestionerAvatar(String questionerAvatar) {
        this.questionerAvatar = questionerAvatar;
    }

    public String getQuestionerNickname() {
        return questionerNickname == null ? "" : questionerNickname;
    }

    public void setQuestionerNickname(String questionerNickname) {
        this.questionerNickname = questionerNickname;
    }

    public String getQuestionerPeriodDes() {
        return questionerPeriodDes == null ? "" : questionerPeriodDes;
    }

    public void setQuestionerPeriodDes(String questionerPeriodDes) {
        this.questionerPeriodDes = questionerPeriodDes;
    }

    public String getQuestionCost() {
        return questionCost == null ? "" : questionCost;
    }

    public void setQuestionCost(String questionCost) {
        this.questionCost = questionCost;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public String getQuestionTitle() {
        return questionTitle == null ? "" : questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionContent() {
        return questionContent == null ? "" : questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }
}
