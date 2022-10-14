package com.health.faq.model;

/**
 * @author Li
 * @date 2019/07/02 16:19
 * @des
 */

public class QuestionDetailModel {
    private int type;
    private QuestionModel questionModel;
    private ReplyModel replyModel;
    private TitleModel titleModel;
    private ReplyAdoptedModel adoptedModel;

    public QuestionDetailModel() {
    }

    public QuestionDetailModel(int type) {
        this.type = type;
    }

    public ReplyAdoptedModel getAdoptedModel() {
        return adoptedModel;
    }

    public void setAdoptedModel(ReplyAdoptedModel adoptedModel) {
        this.adoptedModel = adoptedModel;
    }

    public TitleModel getTitleModel() {
        return titleModel;
    }

    public void setTitleModel(TitleModel titleModel) {
        this.titleModel = titleModel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public QuestionModel getQuestionModel() {
        return questionModel;
    }

    public void setQuestionModel(QuestionModel questionModel) {
        this.questionModel = questionModel;
    }

    public ReplyModel getReplyModel() {
        return replyModel;
    }

    public void setReplyModel(ReplyModel replyModel) {
        this.replyModel = replyModel;
    }
}
