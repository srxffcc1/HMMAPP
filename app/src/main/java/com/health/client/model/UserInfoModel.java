package com.health.client.model;

import java.io.Serializable;

/**
 * @author Li
 * @date 2019/03/28 09:52
 * @des 用户信息
 */

public class UserInfoModel implements Serializable {

    /**
     * 用户id
     */
    public String memberId;
    /**
     * token
     */
    public String token;
    public String phone;
    public String nickName;
    public String birthday;

    public String getFaceUrl() {
        return faceUrl;
    }

    public String faceUrl;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNickName() {
        return nickName == null ? "" : nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMemberId() {
        return memberId == null ? "" : memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
