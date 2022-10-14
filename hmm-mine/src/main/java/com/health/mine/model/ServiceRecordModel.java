package com.health.mine.model;

/**
 * @author Li
 * @date 2019/05/28 15:22
 * @des 调理记录
 */

public class ServiceRecordModel {

    /**
     * 技师名称
     */
    private String technicianName;

    /**
     * 服务时间
     */
    private String serviceDate;

    /**
     * 剩余次数
     */
    private String leftCount;

    public String getTechnicianName() {
        return technicianName == null ? "" : technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getServiceDate() {
        return serviceDate == null ? "" : serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(String leftCount) {
        this.leftCount = leftCount;
    }
}
