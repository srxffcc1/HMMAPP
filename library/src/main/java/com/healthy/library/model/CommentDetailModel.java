package com.healthy.library.model;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/12 17:39
 * @des 评价
 */

public class CommentDetailModel {

        public int id;
        public int shopId;
        public String shopName;
        public String shopBrand;
        public String orderId;
        public String memberId;
        public String memberState;
        public double commentScore;
        public String content;
        public String memberName;
        public String memberTelephone;
        public String createTime;
        public int anonymousStatus;
        public String memberFaceUrl;
        public int imageState;
        public int videoState;
        public List<String> imgUrls;
        public List<String> videoUrls;
        public int examineStatus;
        public int discussNum;
        public int praiseNum;
        public int praiseState;
        public List<CommentPraiseMember> commentPraiseMemberList;

        public class CommentPraiseMember {

                public String memberId;
                public String nickName;
                public String faceUrl;
        }
}
