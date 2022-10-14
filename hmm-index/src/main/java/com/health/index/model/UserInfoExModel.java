package com.health.index.model;


import java.io.Serializable;

public class UserInfoExModel implements Serializable {

        public int id;
        public String memberId;
        public int memberSex;
        public String userPhone;
        public int stageStatus;
        public String stageStatusStr;
        public String latelyMensesDate;
        public String menstrualCycle;
        public String menstrualDays;
        public String lastMensesDate;
        public String deliveryDate;
        public int deliveryMode;
        public int babyId;
        public int babySex;
        public String babyName;
        public String isPregnant;
        public String createDate;
        public String updateDate;
        public int useStatus;
        public String babyfaceUrl="";
        public String faceUrl="";

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getMemberId() {
                return memberId;
        }

        public void setMemberId(String memberId) {
                this.memberId = memberId;
        }

        public int getMemberSex() {
                return memberSex;
        }

        public void setMemberSex(int memberSex) {
                this.memberSex = memberSex;
        }

        public String getUserPhone() {
                return userPhone;
        }

        public void setUserPhone(String userPhone) {
                this.userPhone = userPhone;
        }

        public int getStageStatus() {
                return stageStatus;
        }

        public void setStageStatus(int stageStatus) {
                this.stageStatus = stageStatus;
        }

        public String getLatelyMensesDate() {
                return latelyMensesDate;
        }

        public void setLatelyMensesDate(String latelyMensesDate) {
                this.latelyMensesDate = latelyMensesDate;
        }

        public String getMenstrualCycle() {
                return menstrualCycle;
        }

        public void setMenstrualCycle(String menstrualCycle) {
                this.menstrualCycle = menstrualCycle;
        }

        public String getMenstrualDays() {
                return menstrualDays;
        }

        public void setMenstrualDays(String menstrualDays) {
                this.menstrualDays = menstrualDays;
        }

        public String getLastMensesDate() {
                return lastMensesDate;
        }

        public void setLastMensesDate(String lastMensesDate) {
                this.lastMensesDate = lastMensesDate;
        }

        public String getDeliveryDate() {
                return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
                this.deliveryDate = deliveryDate;
        }

        public int getDeliveryMode() {
                return deliveryMode;
        }

        public void setDeliveryMode(int deliveryMode) {
                this.deliveryMode = deliveryMode;
        }

        public int getBabyId() {
                return babyId;
        }

        public void setBabyId(int babyId) {
                this.babyId = babyId;
        }

        public int getBabySex() {
                return babySex;
        }

        public void setBabySex(int babySex) {
                this.babySex = babySex;
        }

        public String getBabyName() {
                return babyName;
        }

        public void setBabyName(String babyName) {
                this.babyName = babyName;
        }

        public String getIsPregnant() {
                return isPregnant;
        }

        public void setIsPregnant(String isPregnant) {
                this.isPregnant = isPregnant;
        }

        public String getCreateDate() {
                return createDate;
        }

        public void setCreateDate(String createDate) {
                this.createDate = createDate;
        }

        public String getUpdateDate() {
                return updateDate;
        }

        public void setUpdateDate(String updateDate) {
                this.updateDate = updateDate;
        }

        public int getUseStatus() {
                return useStatus;
        }

        public void setUseStatus(int useStatus) {
                this.useStatus = useStatus;
        }
}
