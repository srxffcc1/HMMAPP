package com.healthy.library.model;

import java.util.List;

public class CategoryChildModel {

    /**
     * id : 168
     * goodsCategoryNo : 014001
     * categoryName : 咬咬乐
     * goodsCategorys : [{"id":169,"idStr":null,"goodsCategoryNo":"014001001","categoryName":"硅胶","categoryLevel":3,"fatherId":168,"categoryType":2,"typeId":4,"typeName":"玩具图书","ranking":234,"status":1,"note":"","moreFilePath":null,"filePath":"http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/1bce4ca5-97f3-44e4-ab49-efb755f20ae9.jpeg","createUser":"管理员","createTime":"2020-06-29T11:54:45","updateUser":null,"updateTime":"2020-06-29T17:24:26","deleteUser":null,"deleteTime":null,"isDelete":0,"serviceFeePercentage":null,"differentBusinessPercentage":0.8,"commission":0.6,"distributionLimit":1,"toHomeTag":0,"userId":null,"isHaveChildren":null},{"id":170,"idStr":null,"goodsCategoryNo":"014001002","categoryName":"玩具","categoryLevel":3,"fatherId":168,"categoryType":2,"typeId":4,"typeName":"玩具图书","ranking":67,"status":1,"note":"","moreFilePath":null,"filePath":"http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/e267ef44-920c-4980-a829-801a61182e75.jpeg","createUser":"管理员","createTime":"2020-06-29T17:15:23","updateUser":null,"updateTime":"2020-06-29T17:24:26","deleteUser":null,"deleteTime":null,"isDelete":0,"serviceFeePercentage":null,"differentBusinessPercentage":0.8,"commission":0.7,"distributionLimit":1,"toHomeTag":0,"userId":null,"isHaveChildren":null},{"id":171,"idStr":null,"goodsCategoryNo":"014001003","categoryName":"棉签","categoryLevel":3,"fatherId":168,"categoryType":2,"typeId":4,"typeName":"玩具图书","ranking":344,"status":1,"note":"","moreFilePath":null,"filePath":"http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/bb65f9f3-4ad1-414e-be98-657cbdcd0b3f.jpeg","createUser":"管理员","createTime":"2020-06-29T18:14:40","updateUser":null,"updateTime":null,"deleteUser":null,"deleteTime":null,"isDelete":0,"serviceFeePercentage":null,"differentBusinessPercentage":1.2,"commission":0.8,"distributionLimit":1,"toHomeTag":0,"userId":null,"isHaveChildren":null},{"id":172,"idStr":null,"goodsCategoryNo":"014001004","categoryName":"辅食勺","categoryLevel":3,"fatherId":168,"categoryType":2,"typeId":4,"typeName":"玩具图书","ranking":122,"status":1,"note":"","moreFilePath":null,"filePath":"http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/18fc825d-3c6a-4eea-ac6c-35da09f14f0b.jpeg","createUser":"管理员","createTime":"2020-07-03T09:17:11","updateUser":null,"updateTime":null,"deleteUser":null,"deleteTime":null,"isDelete":0,"serviceFeePercentage":null,"differentBusinessPercentage":1.2,"commission":0.8,"distributionLimit":1,"toHomeTag":0,"userId":null,"isHaveChildren":null},{"id":173,"idStr":null,"goodsCategoryNo":"014001005","categoryName":"刘炜之唯美先生","categoryLevel":3,"fatherId":168,"categoryType":2,"typeId":4,"typeName":"玩具图书","ranking":123,"status":1,"note":"","moreFilePath":null,"filePath":"http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/f6245a27-bb63-44c1-afe2-d50edf0dccc2.jpeg","createUser":"管理员","createTime":"2020-07-03T09:39:51","updateUser":null,"updateTime":null,"deleteUser":null,"deleteTime":null,"isDelete":0,"serviceFeePercentage":null,"differentBusinessPercentage":1.2,"commission":0.8,"distributionLimit":1,"toHomeTag":0,"userId":null,"isHaveChildren":null},{"id":174,"idStr":null,"goodsCategoryNo":"014001006","categoryName":"刘炜之威猛先生","categoryLevel":3,"fatherId":168,"categoryType":2,"typeId":4,"typeName":"玩具图书","ranking":111,"status":1,"note":"","moreFilePath":null,"filePath":"http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/5dd18aa9-01f1-4514-ae01-49a91afd1584.jpeg","createUser":"管理员","createTime":"2020-07-03T09:51:21","updateUser":null,"updateTime":null,"deleteUser":null,"deleteTime":null,"isDelete":0,"serviceFeePercentage":null,"differentBusinessPercentage":1.2,"commission":0.8,"distributionLimit":1,"toHomeTag":0,"userId":null,"isHaveChildren":null}]
     */

    private int id;
    private String goodsCategoryNo;
    private String categoryName;
    private List<GoodsCategorysBean> goodsCategorys;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsCategoryNo() {
        return goodsCategoryNo;
    }

    public void setGoodsCategoryNo(String goodsCategoryNo) {
        this.goodsCategoryNo = goodsCategoryNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<GoodsCategorysBean> getGoodsCategorys() {
        return goodsCategorys;
    }

    public void setGoodsCategorys(List<GoodsCategorysBean> goodsCategorys) {
        this.goodsCategorys = goodsCategorys;
    }

    public static class GoodsCategorysBean {
        /**
         * id : 169
         * idStr : null
         * goodsCategoryNo : 014001001
         * categoryName : 硅胶
         * categoryLevel : 3
         * fatherId : 168
         * categoryType : 2
         * typeId : 4
         * typeName : 玩具图书
         * ranking : 234
         * status : 1
         * note :
         * moreFilePath : null
         * filePath : http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/1bce4ca5-97f3-44e4-ab49-efb755f20ae9.jpeg
         * createUser : 管理员
         * createTime : 2020-06-29T11:54:45
         * updateUser : null
         * updateTime : 2020-06-29T17:24:26
         * deleteUser : null
         * deleteTime : null
         * isDelete : 0
         * serviceFeePercentage : null
         * differentBusinessPercentage : 0.8
         * commission : 0.6
         * distributionLimit : 1.0
         * toHomeTag : 0
         * userId : null
         * isHaveChildren : null
         */

        private String id;
        private Object idStr;
        private String goodsCategoryNo;
        private String categoryName;
        private int categoryLevel;
        private int fatherId;
        private int categoryType;
        private int typeId;
        private String typeName;
        private int ranking;
        private int status;
        private String note;
        private Object moreFilePath;
        private String filePath;
        private String createUser;
        private String createTime;
        private Object updateUser;
        private String updateTime;
        private Object deleteUser;
        private Object deleteTime;
        private int isDelete;
        private Object serviceFeePercentage;
        private double differentBusinessPercentage;
        private double commission;
        private double distributionLimit;
        private int toHomeTag;
        private Object userId;
        private Object isHaveChildren;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getIdStr() {
            return idStr;
        }

        public void setIdStr(Object idStr) {
            this.idStr = idStr;
        }

        public String getGoodsCategoryNo() {
            return goodsCategoryNo;
        }

        public void setGoodsCategoryNo(String goodsCategoryNo) {
            this.goodsCategoryNo = goodsCategoryNo;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public int getCategoryLevel() {
            return categoryLevel;
        }

        public void setCategoryLevel(int categoryLevel) {
            this.categoryLevel = categoryLevel;
        }

        public int getFatherId() {
            return fatherId;
        }

        public void setFatherId(int fatherId) {
            this.fatherId = fatherId;
        }

        public int getCategoryType() {
            return categoryType;
        }

        public void setCategoryType(int categoryType) {
            this.categoryType = categoryType;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public Object getMoreFilePath() {
            return moreFilePath;
        }

        public void setMoreFilePath(Object moreFilePath) {
            this.moreFilePath = moreFilePath;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(Object updateUser) {
            this.updateUser = updateUser;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public Object getDeleteUser() {
            return deleteUser;
        }

        public void setDeleteUser(Object deleteUser) {
            this.deleteUser = deleteUser;
        }

        public Object getDeleteTime() {
            return deleteTime;
        }

        public void setDeleteTime(Object deleteTime) {
            this.deleteTime = deleteTime;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public Object getServiceFeePercentage() {
            return serviceFeePercentage;
        }

        public void setServiceFeePercentage(Object serviceFeePercentage) {
            this.serviceFeePercentage = serviceFeePercentage;
        }

        public double getDifferentBusinessPercentage() {
            return differentBusinessPercentage;
        }

        public void setDifferentBusinessPercentage(double differentBusinessPercentage) {
            this.differentBusinessPercentage = differentBusinessPercentage;
        }

        public double getCommission() {
            return commission;
        }

        public void setCommission(double commission) {
            this.commission = commission;
        }

        public double getDistributionLimit() {
            return distributionLimit;
        }

        public void setDistributionLimit(double distributionLimit) {
            this.distributionLimit = distributionLimit;
        }

        public int getToHomeTag() {
            return toHomeTag;
        }

        public void setToHomeTag(int toHomeTag) {
            this.toHomeTag = toHomeTag;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public Object getIsHaveChildren() {
            return isHaveChildren;
        }

        public void setIsHaveChildren(Object isHaveChildren) {
            this.isHaveChildren = isHaveChildren;
        }
    }


}
