package com.health.index.model;

/**
 * @author Li
 * @date 2019/04/24 16:41
 * @des 待产包
 */

public class BirthPackageModel {

    public static final int PREPARED = 0;
    public static final int UNPREPARED = 1;

    public static final int PACKAGE_MOM = 0;
    public static final int PACKAGE_BABY = 1;
    /**
     * 准备状态
     */
    private int prepareStatus;

    private int id;

    /**
     * 数量描述 ：1套、1个
     */
    private String countDes;
    /**
     * 标题
     */
    private String title;

    /**
     * 名称
     */
    private String introduction;


    /**
     * 待产包类型  0：妈妈用品  1：宝宝用品
     */
    private int packageType;
    /**
     * 隐藏详情
     */
    private boolean hidden;


    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getPrepareStatus() {
        return prepareStatus;
    }

    public void setPrepareStatus(int prepareStatus) {
        this.prepareStatus = prepareStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountDes() {
        return countDes == null ? "" : countDes;
    }

    public void setCountDes(String countDes) {
        this.countDes = countDes;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction == null ? "" : introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
