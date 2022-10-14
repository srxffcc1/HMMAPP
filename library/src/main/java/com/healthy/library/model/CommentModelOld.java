package com.healthy.library.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Li
 * @date 2019/04/12 17:39
 * @des 评价
 */

public class CommentModelOld implements Serializable {

        public int currentPage;
        public int pageSize;
        public int totalNum;
        public int isMore;
        public int totalPage;
        public int startIndex;
        public List<CommentItems> items;
        public double overallRatingAvg;
        
    public class CommentPraiseMember {

        public String memberId;
        public String nickName;
        public String faceUrl;
    }

    
    public static class CommentItems {

        public int id;
        public int shopId;
        public String shopName;
        public String shopBrand;
        public String orderId;
        public String memberId;
        public String memberState;
        public float commentScore;
        public String content;
        public String memberName;
        public String memberTelephone;
        public String createTime;
        public int anonymousStatus;
        public String memberFaceUrl;
        public List<String> imgUrls;
        public List<String> videoUrls;
        public int discussNum;
        public int praiseNum;
        public int praiseState;
        public List<CommentPraiseMember> commentPraiseMemberList;
    }
}
