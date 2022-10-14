package com.healthy.library.model;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/12 17:39
 * @des 评价
 */

public class CommentListModel {

        public int id;
        public int shopId;
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
        public List<String> imgUrls;
        public List<String> videoUrls;
        public int discussNum;
        public int praiseNum;
        public int praiseState;
        public boolean isShowContent=false;
        public List<CommentPraiseMember> commentPraiseMemberList;


        public class CommentPraiseMember {

                private String memberId;
                private String nickName;
                private String faceUrl;
                public void setMemberId(String memberId) {
                        this.memberId = memberId;
                }
                public String getMemberId() {
                        return memberId;
                }

                public void setNickName(String nickName) {
                        this.nickName = nickName;
                }
                public String getNickName() {
                        return nickName;
                }

                public void setFaceUrl(String faceUrl) {
                        this.faceUrl = faceUrl;
                }
                public String getFaceUrl() {
                        return faceUrl;
                }

        }

}
