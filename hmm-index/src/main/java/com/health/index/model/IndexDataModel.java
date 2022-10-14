package com.health.index.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/21 14:09
 * @des 首页数据model
 */

public class IndexDataModel {

    private int type;
    private String title;


    /**
     * 出生时长描述
     */
    private String bornDays;
    /**
     * 孕期第多少天
     */
    private String gestationDay;
    /**
     * 宝宝变化
     */
    private String babyContent;
    /**
     * 妈妈变化
     */
    private String momContent;

    /**
     * 宝宝身高
     */
    private String crownRumpLength;

    /**
     * 宝宝图片
     */
    private String babyUrl;

    /**
     * 妈妈图片
     */
    private String momUrl;

    /**
     * 宝宝体重
     */
    private String weight;

    /**
     * 今日知识
     */
    private List<TodayKnowledge> knowledgeList;

    /**
     * 怀孕内容
     */
    private String pregnancyContent;

    /**
     * 怀孕几率
     */
    private String pregnancyProbability;


    /**
     * 文章id
     */
    private String articleId;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 文章列表图片url
     */
    private String articleImgUrl;
    /**
     * 文章地址
     */
    private String articleUrl;
    /**
     * 文章阅读数量
     */
    private String articleReadNum;

    /**
     * url前缀
     */
    private String urlPrefix;

    /**
     * 是否有今日提醒
     */
    private boolean hasRemind;

    /**
     * 提醒文字
     */
    private String remindTxt;
    /**
     * 提醒类型
     */
    private String remindType;
    /**
     * 提醒id
     */
    private String remindId;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String faceUrl;

    /**
     * 今日消息列表
     */
    private List<RemindInfo> remindInfoList;

    public IndexDataModel() {
    }

    public IndexDataModel(int type) {
        this.type = type;
    }

    public List<RemindInfo> getRemindInfoList() {
        if (remindInfoList == null) {
            return new ArrayList<>();
        }
        return remindInfoList;
    }

    public void setRemindInfoList(List<RemindInfo> remindInfoList) {
        this.remindInfoList = remindInfoList;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFaceUrl() {
        return faceUrl == null ? "" : faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getRemindTxt() {
        return remindTxt == null ? "" : remindTxt;
    }

    public void setRemindTxt(String remindTxt) {
        this.remindTxt = remindTxt;
    }

    public String getRemindType() {
        return remindType == null ? "" : remindType;
    }

    public void setRemindType(String remindType) {
        this.remindType = remindType;
    }

    public String getRemindId() {
        return remindId == null ? "" : remindId;
    }

    public void setRemindId(String remindId) {
        this.remindId = remindId;
    }

    public boolean isHasRemind() {
        return hasRemind;
    }

    public void setHasRemind(boolean hasRemind) {
        this.hasRemind = hasRemind;
    }

    public String getUrlPrefix() {
        return urlPrefix == null ? "" : urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getArticleId() {
        return articleId == null ? "" : articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle == null ? "" : articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleImgUrl() {
        return articleImgUrl == null ? "" : articleImgUrl;
    }

    public void setArticleImgUrl(String articleImgUrl) {
        this.articleImgUrl = articleImgUrl;
    }

    public String getArticleUrl() {
        return articleUrl == null ? "" : articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getArticleReadNum() {
        return articleReadNum == null ? "" : articleReadNum;
    }

    public void setArticleReadNum(String articleReadNum) {
        this.articleReadNum = articleReadNum;
    }

    public String getBornDays() {
        return bornDays == null ? "" : bornDays;
    }

    public void setBornDays(String bornDays) {
        this.bornDays = bornDays;
    }

    public String getBabyUrl() {
        return babyUrl == null ? "" : babyUrl;
    }

    public void setBabyUrl(String babyUrl) {
        this.babyUrl = babyUrl;
    }

    public String getMomUrl() {
        return momUrl == null ? "" : momUrl;
    }

    public void setMomUrl(String momUrl) {
        this.momUrl = momUrl;
    }

    public String getPregnancyContent() {
        return pregnancyContent == null ? "" : pregnancyContent;
    }

    public void setPregnancyContent(String pregnancyContent) {
        this.pregnancyContent = pregnancyContent;
    }

    public String getPregnancyProbability() {
        return pregnancyProbability == null ? "" : pregnancyProbability;
    }

    public void setPregnancyProbability(String pregnancyProbability) {
        this.pregnancyProbability = pregnancyProbability;
    }

    public List<TodayKnowledge> getKnowledgeList() {
        if (knowledgeList == null) {
            return new ArrayList<>();
        }
        return knowledgeList;
    }

    public void setKnowledgeList(List<TodayKnowledge> knowledgeList) {
        this.knowledgeList = knowledgeList;
    }

    public String getCrownRumpLength() {
        return crownRumpLength == null ? "" : crownRumpLength;
    }

    public void setCrownRumpLength(String crownRumpLength) {
        this.crownRumpLength = crownRumpLength;
    }

    public String getWeight() {
        return weight == null ? "" : weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGestationDay() {
        return gestationDay == null ? "" : gestationDay;
    }

    public void setGestationDay(String gestationDay) {
        this.gestationDay = gestationDay;
    }

    public String getBabyContent() {
        return babyContent == null ? "" : babyContent;
    }

    public void setBabyContent(String babyContent) {
        this.babyContent = babyContent;
    }

    public String getMomContent() {
        return momContent == null ? "" : momContent;
    }

    public void setMomContent(String momContent) {
        this.momContent = momContent;
    }


    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public static class RemindInfo {
        String id;
        String content;
        String remindType;

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content == null ? "" : content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRemindType() {
            return remindType == null ? "" : remindType;
        }

        public void setRemindType(String remindType) {
            this.remindType = remindType;
        }
    }

    public static class TodayKnowledge {
        String id;
        String coverUrl;
        String title;
        String urlPrefix;

        public TodayKnowledge() {
        }

        public String getUrlPrefix() {
            return urlPrefix == null ? "" : urlPrefix;
        }

        public void setUrlPrefix(String urlPrefix) {
            this.urlPrefix = urlPrefix;
        }

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoverUrl() {
            return coverUrl == null ? "" : coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getTitle() {
            return title == null ? "" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
