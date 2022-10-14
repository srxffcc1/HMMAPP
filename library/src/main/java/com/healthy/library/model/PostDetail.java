package com.healthy.library.model;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子 关注 本地 全国 7000 7006
 */
public class PostDetail implements Serializable, MultiItemEntity {
    public boolean isnull = false;
    /**
     * postingId : 1413
     * goodsId : 55777
     * goodsSource : 1
     * goodsUrl : http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/3e612014-0992-452e-ad2b-ecbf7420bfdf.jpg
     * updateTime : 2021-04-15 15:42:06
     * postingImgId : 814
     * sortNum : 1
     */

    private int postingId;
    private int goodsId;
    private int goodsSource;
    private String goodsUrl;
    private String updateTime;
    private int postingImgId;
    private int sortNum;

    public PostDetail(boolean isnull) {
        this.isnull = isnull;
    }

    public PostDetail() {
    }

    public String id;
    public List<Topic> topicList;

    public String getPostingContent() {
        try {
            return postingContent
                    .replace("&nbsp;"," ")
                    .replace("<p>", "")
                    .replace("</p>", "<br>")
                    .replace("<br>", "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String postingContent;
    public String postingRichContent;
    public int createSource;
    public String createUserName;
    public String createUserPhone;
    public String accountNickname;
    public String createUserFaceUrl;
    public int imageState;
    public int videoState;
    public int limitsStatus;
    public int postingStatus;
    public int topStatus;
    public String createTime;
    public String createTimeStr;
    public String[] postingLimitsList;
    public String memberId;
    public String memberState;
    public int focusStatus;
    public int discussNum;
    public int praiseNum;
    public int praiseState;
    public String postingAddress;
    public List<ImageUrl> imgUrls;
    public List<VideoUrl> videoUrls;
    public int anonymousStatus;
    public List<PraiseMember> praiseMemberList;
    public String merchantId;
    public String shopIds;
    public String pushTime;
    public String videoFramePicture;//视频封面
    public int postingType;//1:普通帖子 2:新品首发 3:种草清单 4:图文搭配 5:视频搭配 6:会员专享券 7:投票活动 8:报名活动
    public List<PostActivityList> postActivityList;
    public ActivityModel activity;
    public EnlistActivityModel enlistActivity;
    public PkVotingActivityModel pkActivity;
    public String postingTitle;
    public List<String> postingTagList;
    public String postingTag;
    public boolean isFirst = false;
    public transient List<Fans> fans;

    /**
     * 徽标id
     */
    public String badgeId;
    /**
     * 徽标名称
     */
    public String badgeName;
    /**
     * 徽标认证类型（0：达人认证，1：官方认证）
     */
    public int badgeType;
    //public ViewGroup mViewGroup;

    @Override
    public int getItemType() {
        if (postingType >= 2 && postingType <= 6) {
            return 2;
        } else if (postingType == 7 || postingType == 8) {//7 投票活动 8 报名活动
            return 3;
        } else if (postingType == 9) {
            return 4;
        }else if(postingType == -1){
            return 0;//关注样式
        }else {//后续其他默认走普通帖
            return 1;
        }
    }

    public int getPostingId() {
        return postingId;
    }

    public void setPostingId(int postingId) {
        this.postingId = postingId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsSource() {
        return goodsSource;
    }

    public void setGoodsSource(int goodsSource) {
        this.goodsSource = goodsSource;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getPostingImgId() {
        return postingImgId;
    }

    public void setPostingImgId(int postingImgId) {
        this.postingImgId = postingImgId;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public class ImageUrl implements Serializable {

        public String id;
        public String postingId;
        public String url;
        public String annexUrl;

        public String getUrl() {
            if (!TextUtils.isEmpty(annexUrl)) {
                return annexUrl;
            }
            return url;
        }

        public int urlType;
        public String createTime;
        public String updateTime;
        public int urlStatus;

    }

    public class PraiseMember {

        public String memberId;
        public String nickName;
        public String faceUrl;
    }

    public class VideoUrl implements Serializable {

        public String id;
        public String postingId;
        public String url;
        public String videoCoverImage;
        public int urlType;
        public String createTime;
        public String updateTime;
        public int urlStatus;
        public List<PostingGoodsList> postingGoodsList;
    }

    public class PostingGoodsList {
        public String id;
        public String postingId;
        public String goodsId;
        public String goodsSource;
        public String goodsUrl;
        public String createTime;
        public String updateTime;
        public String postingImgId;
        public int sortNum;
    }

    private boolean isShow = false;//用来标志当前帖子是否是展开状态

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
