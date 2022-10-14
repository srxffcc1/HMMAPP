package com.health.index.model;

/**
 * @author Li
 * @date 2019/04/26 11:32
 * @des
 */
public class ChangeModel {

    /**
     * 妈妈变化
     */
    private String momChange;
    /**
     * 注意事项
     */
    private String reminder;
    /**
     * 营养指导
     */
    private String foodPoints;

    /**
     * 宝宝变化
     */
    private String babyChange;
    /**
     * 宝宝变化图片
     */
    private String babyUrl;
    /**
     * 宝宝第几周描述
     */
    private String babyWeek;

    /**
     * 宝宝第几天描述
     */
    private String babyDay;

    /**
     * 对应的时间
     */
    private String showDate;


    public String getShowDate() {
        return showDate == null ? "" : showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getBabyDay() {
        return babyDay == null ? "" : babyDay;
    }

    public void setBabyDay(String babyDay) {
        this.babyDay = babyDay;
    }

    public String getMomChange() {
        return momChange == null ? "" : momChange;
    }

    public void setMomChange(String momChange) {
        this.momChange = momChange;
    }

    public String getReminder() {
        return reminder == null ? "" : reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getFoodPoints() {
        return foodPoints == null ? "" : foodPoints;
    }

    public void setFoodPoints(String foodPoints) {
        this.foodPoints = foodPoints;
    }

    public String getBabyChange() {
        return babyChange == null ? "" : babyChange;
    }

    public void setBabyChange(String babyChange) {
        this.babyChange = babyChange;
    }

    public String getBabyUrl() {
        return babyUrl == null ? "" : babyUrl;
    }

    public void setBabyUrl(String babyUrl) {
        this.babyUrl = babyUrl;
    }

    public String getBabyWeek() {
        return babyWeek == null ? "" : babyWeek;
    }

    public void setBabyWeek(String babyWeek) {
        this.babyWeek = babyWeek;
    }
}
