package com.healthy.library.model;

/**
 * @author Li
 * @date 2019/04/12 17:39
 * @des 评价
 */

public class OldCommentModel {

    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论平均分
     */
    private String overallRating;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 会员头像
     */
    private String memberFaceUrl;
    /**
     * 评论图片
     */
    private String[] imgs;

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOverallRating() {
        return overallRating == null ? "" : overallRating;
    }

    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }

    public String getMemberName() {
        return memberName == null ? "" : memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMemberFaceUrl() {
        return memberFaceUrl == null ? "" : memberFaceUrl;
    }

    public void setMemberFaceUrl(String memberFaceUrl) {
        this.memberFaceUrl = memberFaceUrl;
    }

    public String[] getImgs() {
        return imgs;
    }

    public void setImgs(String[] imgs) {
        this.imgs = imgs;
    }
}
