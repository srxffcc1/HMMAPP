package com.health.index.model;

/**
 * @author Li
 * @date 2019/04/23 09:25
 * @des 用户资料
 */
public class UserInfoModel {

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户头像url
     */
    private String avatarUrl;

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl == null ? "" : avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
