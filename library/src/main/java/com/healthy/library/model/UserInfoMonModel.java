package com.healthy.library.model;

/**
 * @author Li
 * @date 2019/04/23 09:25
 * @des 用户资料
 */
public class UserInfoMonModel {

    public String deliveryDate;
    /**
     * 昵称
     */
    public String nickName;


    public void setMemberSex(int memberSex) {
        this.memberSex = memberSex;
    }

    public int getMemberSex() {
        return memberSex;
    }

    /**
     * 昵称
     */
    public int memberSex;

    /**
     * 手机号
     */
    public String phone;

    /**
     * 状态
     */
    public int status;// 1 2 3

    /**
     * 状态描述
     */
    public String statusName;

    /**
     * 头像地址
     */
    public String faceUrl;

    /**
     * 时间描述
     */
    public String dateContent;

    /**
     * 最近月经开始时间
     */
    public String menLatelyDate;
    public String babyName;
    /**
     * 宝宝性别
     */
    public int babySex;

    /**
     * 分娩方式
     */
    public int birthType;

    /**
     * 宝宝年龄描述
     */
    public String babyAgeDes;

    /**
     * 经期周期 时长描述
     */
    public String menCycleDurationDes;

    /**
     * 预产期描述
     */
    public String birthExpectedDateDes;

    /*
     * 1：已设置 2：未设置
     * */
    public int isSetPayPassword;

    /**
     * 收入
     */
    public String income;

    /**
     * 余额
     */
    public String balance;

    public String birthday;

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
        return nickName == null ? "" : nickName;
    }

    public void setNickname(String nickname) {
        this.nickName = nickname;
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
