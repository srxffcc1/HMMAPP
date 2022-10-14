package com.health.mine.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserInfoExModel implements Serializable {

        public int id;
        public String memberId;
        public int memberSex;
        public String userPhone;
        public int stageStatus;
        public String stageStatusStr;
        public String latelyMensesDate;
        public int menstrualCycle;
        public int menstrualDays;
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
        private List<String> mCycleList;
        private List<String> mDurationList;

        public UserInfoExModel() {
                mCycleList = new ArrayList<>();
                mDurationList = new ArrayList<>();

                int startCycle = 24;
                int endCycle = 35;
                int startDuration = 1;
                int endDuration = 12;
                for (int i = startCycle; i <= endCycle; i++) {
                        mCycleList.add(String.format("周期%s天", i));
                }
                for (int i = startDuration; i <= endDuration; i++) {
                        mDurationList.add(String.format("时长%s天", i));
                }
        }


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

        public int getMenstrualCycleIndex() {
                return menstrualCycle;
        }
        public String getMenstrualCycle() {
                if(menstrualCycle-1<0||menstrualCycle+1>=mCycleList.size()){
                        return "";
                }
                return mCycleList.get(menstrualCycle-1);
        }

        public String getMenstrualDays() {
                try {
                        return mDurationList.get(menstrualDays-1);
                } catch (Exception e) {
                        e.printStackTrace();
                        return mDurationList.get(0);
                }
        }

        public int getMenstrualDaysIndex() {
                return menstrualDays;
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
