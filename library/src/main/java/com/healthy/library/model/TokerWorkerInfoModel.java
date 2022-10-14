package com.healthy.library.model;

import java.util.ArrayList;
import java.util.List;

public class TokerWorkerInfoModel {


    public TokerWorkerBean tokerWorker;
    public Integer isTokerWorker;
    public List<BindingListBean> bindingList=new ArrayList<>();

    public class TokerWorkerBean {


        public String id;
        public String appId;
        public String departId;
        public String departName;
        public String personId;
        public String personName;
        public String personTel;
        public String sex;
        public String portrait;
        public String state;
        public String other;
        public String gw;
        public String principals;
        public String birthday;
        public String inDate;
        public String merchantId;
        public String merchantName;
        public String shopId;
        public String shopName;
        public String memberId;
        public String memberName;
        public String referralCode;
        public String deleted;
        public String createTime;
        public String workWxUrl;
        public String workWxImg;
        public String latestBindMemberTime;
        public String latestUpdateTime;

    }

    public static class BindingListBean {

        public BindingTokerWorkerBean bindingTokerWorker;

        public BindingListBean(BindingTokerWorkerBean bindingTokerWorker) {
            this.bindingTokerWorker = bindingTokerWorker;
        }

        public class BindingTokerWorkerBean {

            public String id;
            public String appId;
            public String departId;
            public String departName;
            public String personId;
            public String personName;
            public String personTel;
            public String sex;
            public String portrait;
            public String state;
            public String other;
            public String gw;
            public String principals;
            public String birthday;
            public String inDate;
            public String merchantId;
            public String merchantName;
            public String shopId;
            public String shopName;
            public String memberId;
            public String memberName;
            public String referralCode;
            public String deleted;
            public String createTime;
            public String workWxUrl;
            public String workWxImg;
            public String latestBindMemberTime;
            public String latestUpdateTime;
            public String professionalPhoto;//新增的头像字段
        }

        public class BindingRelationBean {
            public String id;
            public String memberId;
            public String appId;
            public String departId;
            public String departName;
            public String employeeId;
            public String employeeName;
            public String bindingSource;
            public String bindingTime;
            public String spreadMoney;
            public String memberInfo;
            public String memberPhone;
            public String memberName;
            public String memberNickName;
            public String memberSource;
            public String memberLoginType;
            public String memberCreateTime;
            public String personId;
            public String personName;
            public String referralCode;
            public String createTime;
            public String latestUpdateTime;
        }
    }
}
