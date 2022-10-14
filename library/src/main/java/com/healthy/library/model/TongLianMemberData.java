package com.healthy.library.model;

import java.io.Serializable;

public class TongLianMemberData implements Serializable {
    public TongLianMemberInfo memberInfo=new TongLianMemberInfo();
    public String memberType;
    public String bizUserId;

    public static class TongLianMemberInfo {
        public boolean isSignContract;//电子签约
        public String registerTime;
        public String userState;
        public String payFailAmount;
        public boolean isIdentityChecked;//实名
        public boolean isSetPayPwd;
        public String identityCardNo;
        public String source;
        public String userId;
        public boolean isPhoneChecked;//手机验证
    }
}
