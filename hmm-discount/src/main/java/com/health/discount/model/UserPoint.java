package com.health.discount.model;

public class UserPoint {
    /**
     * 昵称
     */
    private String nickname;


    public void setMemberSex(int memberSex) {
        this.memberSex = memberSex;
    }

    public int getMemberSex() {
        return memberSex;
    }

    /**
     * 昵称
     */
    private int memberSex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 状态
     */
    private int status;

    /**
     * 状态描述
     */
    private String statusName;

    /**
     * 头像地址
     */
    private String faceUrl;

    /**
     * 时间描述
     */
    private String dateContent;

    /**
     * 最近月经开始时间
     */
    private String menLatelyDate;

    /**
     * 宝宝性别
     */
    private int babySex;


    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    /**
     * 宝宝性别
     */
    private String babyName;

    /**
     * 分娩方式
     */
    private int birthType;

    /**
     * 宝宝年龄描述
     */
    private String babyAgeDes;

    /**
     * 经期周期 时长描述
     */
    private String menCycleDurationDes;

    /**
     * 预产期描述
     */
    private String birthExpectedDateDes;

    /*
     * 1：已设置 2：未设置
     * */
    private int isSetPayPassword;

    /**
     * 收入
     */
    private String income;

    /**
     * 余额
     */
    private String balance;

    public String getIncome() {
        return income == null ? "" : income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getBalance() {
        return balance == null ? "" : balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getIsSetPayPassword() {
        return isSetPayPassword;
    }

    public void setIsSetPayPassword(int isSetPayPassword) {
        this.isSetPayPassword = isSetPayPassword;
    }

    public String getBirthExpectedDateDes() {
        return birthExpectedDateDes == null ? "" : birthExpectedDateDes;
    }

    public void setBirthExpectedDateDes(String birthExpectedDateDes) {
        this.birthExpectedDateDes = birthExpectedDateDes;
    }

    public String getMenCycleDurationDes() {
        return menCycleDurationDes == null ? "" : menCycleDurationDes;
    }



    public void setMenCycleDurationDes(String menCycleDurationDes) {
        this.menCycleDurationDes = menCycleDurationDes;
    }

    public String getBabyAgeDes() {
        return babyAgeDes == null ? "" : babyAgeDes;
    }

    public void setBabyAgeDes(String babyAgeDes) {
        this.babyAgeDes = babyAgeDes;
    }

    public String getMenLatelyDate() {
        return menLatelyDate == null ? "" : menLatelyDate;
    }

    public void setMenLatelyDate(String menLatelyDate) {
        this.menLatelyDate = menLatelyDate;
    }

    public int getBabySex() {
        return babySex;
    }

    public void setBabySex(int babySex) {
        this.babySex = babySex;
    }

    public int getBirthType() {
        return birthType;
    }

    public void setBirthType(int birthType) {
        this.birthType = birthType;
    }

    public String getDateContent() {
        return dateContent == null ? "" : dateContent;
    }

    public void setDateContent(String dateContent) {
        this.dateContent = dateContent;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFaceUrl() {
        return faceUrl == null ? "" : faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getStatusName() {
        return statusName == null ? "" : statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }


}
