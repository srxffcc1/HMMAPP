package com.health.index.model;

/**
 * @author Li
 * @date 2019/04/23 13:33
 * @des 疫苗
 */

public class VaccineModel {


    /**
     * vaccinationTime : 该打针时间
     * vaccinationTimestamp : 该打针时间戳
     * vaccineName : 疫苗名称
     * reason : 打原因
     * vaccineAge : 该打年龄
     * vaccineAgeId : id
     */

    private String vaccinationTime;
    private String vaccinationTimestamp;
    private String vaccineName;
    private String reason;
    private String vaccineAge;
    private int vaccineAgeId;
    private String id;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVaccinationTime() {
        return vaccinationTime;
    }

    public void setVaccinationTime(String vaccinationTime) {
        this.vaccinationTime = vaccinationTime;
    }

    public String getVaccinationTimestamp() {
        return vaccinationTimestamp;
    }

    public void setVaccinationTimestamp(String vaccinationTimestamp) {
        this.vaccinationTimestamp = vaccinationTimestamp;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getVaccineAge() {
        return vaccineAge;
    }

    public void setVaccineAge(String vaccineAge) {
        this.vaccineAge = vaccineAge;
    }

    public int getVaccineAgeId() {
        return vaccineAgeId;
    }

    public void setVaccineAgeId(int vaccineAgeId) {
        this.vaccineAgeId = vaccineAgeId;
    }
}
