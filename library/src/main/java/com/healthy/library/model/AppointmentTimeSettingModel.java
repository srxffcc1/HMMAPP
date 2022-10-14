package com.healthy.library.model;

import com.healthy.library.utils.DateUtils;

import java.util.List;

/**
 * 预约服务时间配置
 *
 * @author: long
 * @date: 2021/4/6
 */
public class AppointmentTimeSettingModel {

    private ShopSettingModel shopSetting;
    private List<ShopTimeSetting> shopTimeSettings;

    public ShopSettingModel getShopSetting() {
        return shopSetting;
    }

    public void setShopSetting(ShopSettingModel shopSetting) {
        this.shopSetting = shopSetting;
    }

    public List<ShopTimeSetting> getShopTimeSettings() {
        return shopTimeSettings;
    }

    public void setShopTimeSettings(List<ShopTimeSetting> shopTimeSettings) {
        this.shopTimeSettings = shopTimeSettings;
    }

    public class ShopSettingModel {
        private long id;
        private long mapProjectShopId;
        private int type;
        private int autoAcceptOrder;
        private int acceptOrderCancel;
        private int advanceRange;
        private int appointmentRange;
        private String createTime;
        private String updateTime;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getMapProjectShopId() {
            return mapProjectShopId;
        }

        public void setMapProjectShopId(long mapProjectShopId) {
            this.mapProjectShopId = mapProjectShopId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getAutoAcceptOrder() {
            return autoAcceptOrder;
        }

        public void setAutoAcceptOrder(int autoAcceptOrder) {
            this.autoAcceptOrder = autoAcceptOrder;
        }

        public int getAcceptOrderCancel() {
            return acceptOrderCancel;
        }

        public void setAcceptOrderCancel(int acceptOrderCancel) {
            this.acceptOrderCancel = acceptOrderCancel;
        }

        public int getAdvanceRange() {
            return advanceRange;
        }

        public void setAdvanceRange(int advanceRange) {
            this.advanceRange = advanceRange;
        }

        public int getAppointmentRange() {
            return appointmentRange;
        }

        public void setAppointmentRange(int appointmentRange) {
            this.appointmentRange = appointmentRange;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    public class ShopTimeSetting {
        private String id;
        private String mapProjectShopId;
        private String type;
        private String startTime;
        private String endTime;
        private String count;
        private String createTime;
        private String updateTime;
        private int surplusCount;
        private boolean disabled;
        private boolean isChecked;
        private String dateStr;

        public String getDateStr() {
            return dateStr = DateUtils.getDateTimeSplit(this.startTime) + "-" + DateUtils.getDateTimeSplit(this.endTime);
        }

        public void setDateStr(String dateStr) {
            this.dateStr = dateStr;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMapProjectShopId() {
            return mapProjectShopId;
        }

        public void setMapProjectShopId(String mapProjectShopId) {
            this.mapProjectShopId = mapProjectShopId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getSurplusCount() {
            return surplusCount;
        }

        public void setSurplusCount(int surplusCount) {
            this.surplusCount = surplusCount;
        }

        public boolean isDisabled() {
            return disabled;
        }


        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }
    }

}
