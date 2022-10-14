package com.healthy.library.model;

public class TXUserInfo {
    public String accountId;
    public String userName;
    public String message;

    @Override
    public String toString() {
        return "TXUserInfo{" +
                "accountId='" + accountId + '\'' +
                ", userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
