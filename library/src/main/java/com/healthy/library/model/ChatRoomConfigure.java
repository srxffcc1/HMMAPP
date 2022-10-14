package com.healthy.library.model;

public class ChatRoomConfigure {

    public String sdkAppId;
    public String userSig;
    public AccountBean account;

    public class AccountBean {
        public String accountId;
        public String accountName;
        public String accountType;
        public String identifier;
    }
}
