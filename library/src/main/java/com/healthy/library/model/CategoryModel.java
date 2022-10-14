package com.healthy.library.model;

public class CategoryModel {


    /**
     * categoryId : 151
     * categoryName : 奶粉纸尿裤
     * filePath : http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/160f63ac-24dc-4eb3-b60e-45f92a5ad4ec.jpg
     */

    private String categoryId;
    private String categoryName;
    private String filePath;
    private String categoryLevel;
    private String fatherId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(String categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }
}
