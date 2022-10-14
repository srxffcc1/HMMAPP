package com.healthy.library.model;

import java.io.Serializable;

public class CategoryListModel implements Serializable {
    /**
     * id : 1
     * idStr : 1
     * goodsCategoryNo : 001003
     * categoryName : 月子照护
     * categoryLevel : 2
     * fatherId : 74
     * categoryType : null
     * typeId : null
     * typeName : null
     * ranking : 6
     * status : 1
     * note :
     * moreFilePath : http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/64b70610-5705-447c-9c41-3a577c5cab93.png
     * filePath : http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/64b70610-5705-447c-9c41-3a577c5cab93.png
     * createUser : 管理员
     * createTime : 2019-08-12T10:00:00
     * updateUser : 白洁
     * updateTime : 2020-05-20T16:52:47
     * deleteUser : null
     * deleteTime : null
     * isDelete : 0
     * serviceFeePercentage : 3.0
     * differentBusinessPercentage : null
     * commission : 11.0
     * distributionLimit : 1.25
     * toHomeTag : 1
     * userId : null
     * isHaveChildren : null
     */

    private long id;
    private String idStr;
    private String goodsCategoryNo;
    private String categoryName;
    private int categoryLevel;
    private long fatherId;
    private Object categoryType;
    private Object typeId;
    private Object typeName;
    private int ranking;
    private int status;
    private String note;
    private String moreFilePath;
    private String filePath;
    private String createUser;
    private String createTime;
    private String updateUser;
    private String updateTime;
    private Object deleteUser;
    private Object deleteTime;
    private int isDelete;
    private double serviceFeePercentage;
    private Object differentBusinessPercentage;
    private double commission;
    private double distributionLimit;
    private int toHomeTag;
    private Object userId;
    private Object isHaveChildren;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
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

    public long getFatherId() {
        return fatherId;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    public Object getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Object categoryType) {
        this.categoryType = categoryType;
    }

    public Object getTypeId() {
        return typeId;
    }

    public void setTypeId(Object typeId) {
        this.typeId = typeId;
    }

    public Object getTypeName() {
        return typeName;
    }

    public void setTypeName(Object typeName) {
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

    public String getMoreFilePath() {
        return moreFilePath;
    }

    public void setMoreFilePath(String moreFilePath) {
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

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
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

    public double getServiceFeePercentage() {
        return serviceFeePercentage;
    }

    public void setServiceFeePercentage(double serviceFeePercentage) {
        this.serviceFeePercentage = serviceFeePercentage;
    }

    public Object getDifferentBusinessPercentage() {
        return differentBusinessPercentage;
    }

    public void setDifferentBusinessPercentage(Object differentBusinessPercentage) {
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
