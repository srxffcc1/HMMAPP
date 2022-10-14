package com.healthy.library.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class OrderListModel implements MultiItemEntity {


    /**
     * id : 199
     * orderNum : 1357522095046688
     * memberId : 2006220000001645
     * memberName : YV7913E6
     * memberPhone : 13520409204
     * memberFaceUrl : http://hmm-public.oss-cn-beijing.aliyuncs.com/hmm-user-dev/images/59a63dff-4e10-4066-8be5-9af21b2f52b9.png
     * totalGoodsAmount : 25.5
     * totalDeliverAmount : 0.0
     * totalPreferentialAmount : 0.0
     * totalReductionAmount : 0.0
     * totalPayAmount : 25.5
     * totalRefundAmount : 0.0
     * mchId : 8
     * mchName : 爱婴岛
     * shopId : 59
     * shopName : 测试一下01
     * shopChainName : 测试一下01
     * shopBrandName : 测试一下01
     * shopAddress : null
     * deliverType : 0
     * deliverConsigneeName : null
     * deliverConsigneePhone : null
     * deliverConsigneeProvince : null
     * deliverConsigneeProvinceName : null
     * deliverConsigneeCity : null
     * deliverConsigneeCityName : null
     * deliverConsigneeDistrict : null
     * deliverConsigneeDistrictName : null
     * deliverConsigneeAddress : null
     * deliverLatitude : null
     * deliverLongitude : null
     * deliverDate : null
     * deliverTime : null
     * paymentId : null
     * paymentType : null
     * paymentStatus : 0
     * paymentSuccessTime : null
     * refundStatus : 0
     * orderStatus : 0
     * orderSource : 1
     * orderPlatform : IOS
     * orderType : 0
     * orderRace : 3
     * isMain : 1
     * mainId : null
     * latestPayTime : 2020-07-07 10:16:18
     * comment : null
     * createBy : 2006220000001645
     * createTime : 2020-07-06 10:16:18
     * updateBy : 2006220000001645
     * updateTime : 2020-07-06 10:16:18
     * bargainId : null
     * bargainMemberId : null
     * subList : [{"id":200,"orderNum":"1357522095046688-001","memberId":"2006220000001645","memberName":"YV7913E6","memberPhone":"13520409204","memberFaceUrl":"http://hmm-public.oss-cn-beijing.aliyuncs.com/hmm-user-dev/images/59a63dff-4e10-4066-8be5-9af21b2f52b9.png","totalGoodsAmount":25.5,"totalDeliverAmount":0,"totalPreferentialAmount":0,"totalReductionAmount":0,"totalPayAmount":25.5,"totalRefundAmount":0,"mchId":8,"mchName":"爱婴岛","shopId":59,"shopName":"测试一下01","shopChainName":"测试一下01","shopBrandName":"测试一下01","shopAddress":null,"deliverType":10,"deliverConsigneeName":"xxx","deliverConsigneePhone":"135xxxxxxxx","deliverConsigneeProvince":"320000","deliverConsigneeProvinceName":"江苏省","deliverConsigneeCity":"320500","deliverConsigneeCityName":"苏州市","deliverConsigneeDistrict":"320506","deliverConsigneeDistrictName":"吴中区","deliverConsigneeAddress":"吴中区xx小区","deliverLatitude":31.336392,"deliverLongitude":120.617296,"deliverDate":"2020-07-31 00:00:00","deliverTime":"上午","paymentId":null,"paymentType":null,"paymentStatus":0,"paymentSuccessTime":null,"refundStatus":0,"orderStatus":0,"orderSource":1,"orderPlatform":"IOS","orderType":0,"orderRace":2,"isMain":0,"mainId":199,"latestPayTime":"2020-07-07 10:16:18","comment":null,"createBy":"2006220000001645","createTime":"2020-07-06 10:16:18","updateBy":"2006220000001645","updateTime":"2020-07-06 10:16:18","bargainId":null,"bargainMemberId":null,"subList":[],"details":[{"id":317,"orderId":200,"orderNum":"1357522095046688-001","goodsId":68,"goodsSpec":60,"goodsTitle":"小儿推拿大促销-限时抢购666 红色 80 S","goodsSpecDesc":"红色 80 S","goodsImage":"http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/47dea72f-6363-4dd7-8de3-7792f936b525.jpg","barcodeSku":null,"goodsQuantity":2,"goodsPrice":1.5,"goodsPreferentialAmount":0,"goodsReductionAmount":0,"goodsPayAmount":1.5,"totalAmount":3,"totalPreferentialAmount":0,"totalReductionAmount":0,"totalPayAmount":3,"refundTotalQuantity":0,"refundTotalAmount":0,"isAddition":0,"additionType":1,"createBy":"2006220000001645","createTime":"2020-07-06 10:16:18","updateBy":"2006220000001645","updateTime":"2020-07-06 10:16:18"},{"id":318,"orderId":200,"orderNum":"1357522095046688-001","goodsId":60,"goodsSpec":85,"goodsTitle":"小儿推拿大促销-限时抢购666 红色 100 M","goodsSpecDesc":"红色 100 M","goodsImage":"http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/cbb28b34-8e6b-446e-8952-1a77fd3d545d.jpg","barcodeSku":null,"goodsQuantity":3,"goodsPrice":7.5,"goodsPreferentialAmount":0,"goodsReductionAmount":0,"goodsPayAmount":7.5,"totalAmount":22.5,"totalPreferentialAmount":0,"totalReductionAmount":0,"totalPayAmount":22.5,"refundTotalQuantity":0,"refundTotalAmount":0,"isAddition":0,"additionType":1,"createBy":"2006220000001645","createTime":"2020-07-06 10:16:18","updateBy":"2006220000001645","updateTime":"2020-07-06 10:16:18"}],"isRegiment":0,"residuePayTime":82549}]
     * details : []
     * isRegiment : 0
     * residuePayTime : 82549
     */

    private long id;
    private String orderNum;
    private String memberId;
    private String memberName;
    private String memberPhone;
    private String memberFaceUrl;
    private double totalGoodsAmount;
    private double totalDeliverAmount;
    private double totalPreferentialAmount;
    private double totalReductionAmount;
    private double totalPayAmount;
    private double totalPoints;
    private double totalPayPoints;
    private double totalRefundAmount;
    private int mchId;
    private String mchName;
    private int shopId;
    private String shopName;
    private String shopChainName;
    private String shopBrandName;
    private Object shopAddress;
    private int deliverType;
    private Object deliverConsigneeName;
    private Object deliverConsigneePhone;
    private Object deliverConsigneeProvince;
    private Object deliverConsigneeProvinceName;
    private Object deliverConsigneeCity;
    private Object deliverConsigneeCityName;
    private Object deliverConsigneeDistrict;
    private Object deliverConsigneeDistrictName;
    private Object deliverConsigneeAddress;
    private Object deliverLatitude;
    private Object deliverLongitude;
    private Object deliverDate;
    private Object deliverTime;
    private Object paymentId;
    private Object paymentType;
    private int paymentStatus;
    private Object paymentSuccessTime;
    private int refundStatus;
    private int orderStatus;
    private int orderSource;
    private String orderPlatform;
    private int orderType;
    private int orderRace;
    private int isMain;
    private Object mainId;
    private String latestPayTime;
    private Object comment;
    private String createBy;
    private String createTime;
    private String updateBy;
    private String updateTime;
    private Object bargainId;
    private Object bargainMemberId;
    private int isRegiment;
    private int residuePayTime;
    private int commentStatus;
    private List<SubListBean> subList;
    private List<DetailsBean> details;

    public double getTotalPayPoints() {
        return totalPayPoints;
    }

    public void setTotalPayPoints(double totalPayPoints) {
        this.totalPayPoints = totalPayPoints;
    }

    public double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(int commentStatus) {
        this.commentStatus = commentStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberFaceUrl() {
        return memberFaceUrl;
    }

    public void setMemberFaceUrl(String memberFaceUrl) {
        this.memberFaceUrl = memberFaceUrl;
    }

    public double getTotalGoodsAmount() {
        return totalGoodsAmount;
    }

    public void setTotalGoodsAmount(double totalGoodsAmount) {
        this.totalGoodsAmount = totalGoodsAmount;
    }

    public double getTotalDeliverAmount() {
        return totalDeliverAmount;
    }

    public void setTotalDeliverAmount(double totalDeliverAmount) {
        this.totalDeliverAmount = totalDeliverAmount;
    }

    public double getTotalPreferentialAmount() {
        return totalPreferentialAmount;
    }

    public void setTotalPreferentialAmount(double totalPreferentialAmount) {
        this.totalPreferentialAmount = totalPreferentialAmount;
    }

    public double getTotalReductionAmount() {
        return totalReductionAmount;
    }

    public void setTotalReductionAmount(double totalReductionAmount) {
        this.totalReductionAmount = totalReductionAmount;
    }

    public double getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(double totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    public double getTotalRefundAmount() {
        return totalRefundAmount;
    }

    public void setTotalRefundAmount(double totalRefundAmount) {
        this.totalRefundAmount = totalRefundAmount;
    }

    public int getMchId() {
        return mchId;
    }

    public void setMchId(int mchId) {
        this.mchId = mchId;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopChainName() {
        return shopChainName;
    }

    public void setShopChainName(String shopChainName) {
        this.shopChainName = shopChainName;
    }

    public String getShopBrandName() {
        return shopBrandName;
    }

    public void setShopBrandName(String shopBrandName) {
        this.shopBrandName = shopBrandName;
    }

    public Object getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(Object shopAddress) {
        this.shopAddress = shopAddress;
    }

    public int getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(int deliverType) {
        this.deliverType = deliverType;
    }

    public Object getDeliverConsigneeName() {
        return deliverConsigneeName;
    }

    public void setDeliverConsigneeName(Object deliverConsigneeName) {
        this.deliverConsigneeName = deliverConsigneeName;
    }

    public Object getDeliverConsigneePhone() {
        return deliverConsigneePhone;
    }

    public void setDeliverConsigneePhone(Object deliverConsigneePhone) {
        this.deliverConsigneePhone = deliverConsigneePhone;
    }

    public Object getDeliverConsigneeProvince() {
        return deliverConsigneeProvince;
    }

    public void setDeliverConsigneeProvince(Object deliverConsigneeProvince) {
        this.deliverConsigneeProvince = deliverConsigneeProvince;
    }

    public Object getDeliverConsigneeProvinceName() {
        return deliverConsigneeProvinceName;
    }

    public void setDeliverConsigneeProvinceName(Object deliverConsigneeProvinceName) {
        this.deliverConsigneeProvinceName = deliverConsigneeProvinceName;
    }

    public Object getDeliverConsigneeCity() {
        return deliverConsigneeCity;
    }

    public void setDeliverConsigneeCity(Object deliverConsigneeCity) {
        this.deliverConsigneeCity = deliverConsigneeCity;
    }

    public Object getDeliverConsigneeCityName() {
        return deliverConsigneeCityName;
    }

    public void setDeliverConsigneeCityName(Object deliverConsigneeCityName) {
        this.deliverConsigneeCityName = deliverConsigneeCityName;
    }

    public Object getDeliverConsigneeDistrict() {
        return deliverConsigneeDistrict;
    }

    public void setDeliverConsigneeDistrict(Object deliverConsigneeDistrict) {
        this.deliverConsigneeDistrict = deliverConsigneeDistrict;
    }

    public Object getDeliverConsigneeDistrictName() {
        return deliverConsigneeDistrictName;
    }

    public void setDeliverConsigneeDistrictName(Object deliverConsigneeDistrictName) {
        this.deliverConsigneeDistrictName = deliverConsigneeDistrictName;
    }

    public Object getDeliverConsigneeAddress() {
        return deliverConsigneeAddress;
    }

    public void setDeliverConsigneeAddress(Object deliverConsigneeAddress) {
        this.deliverConsigneeAddress = deliverConsigneeAddress;
    }

    public Object getDeliverLatitude() {
        return deliverLatitude;
    }

    public void setDeliverLatitude(Object deliverLatitude) {
        this.deliverLatitude = deliverLatitude;
    }

    public Object getDeliverLongitude() {
        return deliverLongitude;
    }

    public void setDeliverLongitude(Object deliverLongitude) {
        this.deliverLongitude = deliverLongitude;
    }

    public Object getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Object deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Object getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Object deliverTime) {
        this.deliverTime = deliverTime;
    }

    public Object getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Object paymentId) {
        this.paymentId = paymentId;
    }

    public Object getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Object paymentType) {
        this.paymentType = paymentType;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Object getPaymentSuccessTime() {
        return paymentSuccessTime;
    }

    public void setPaymentSuccessTime(Object paymentSuccessTime) {
        this.paymentSuccessTime = paymentSuccessTime;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(int orderSource) {
        this.orderSource = orderSource;
    }

    public String getOrderPlatform() {
        return orderPlatform;
    }

    public void setOrderPlatform(String orderPlatform) {
        this.orderPlatform = orderPlatform;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderRace() {
        return orderRace;
    }

    public void setOrderRace(int orderRace) {
        this.orderRace = orderRace;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }

    public Object getMainId() {
        return mainId;
    }

    public void setMainId(Object mainId) {
        this.mainId = mainId;
    }

    public String getLatestPayTime() {
        return latestPayTime;
    }

    public void setLatestPayTime(String latestPayTime) {
        this.latestPayTime = latestPayTime;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
        this.comment = comment;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Object getBargainId() {
        return bargainId;
    }

    public void setBargainId(Object bargainId) {
        this.bargainId = bargainId;
    }

    public Object getBargainMemberId() {
        return bargainMemberId;
    }

    public void setBargainMemberId(Object bargainMemberId) {
        this.bargainMemberId = bargainMemberId;
    }

    public int getIsRegiment() {
        return isRegiment;
    }

    public void setIsRegiment(int isRegiment) {
        this.isRegiment = isRegiment;
    }

    public int getResiduePayTime() {
        return residuePayTime;
    }

    public void setResiduePayTime(int residuePayTime) {
        this.residuePayTime = residuePayTime;
    }

    public List<SubListBean> getSubList() {
        return subList;
    }

    public void setSubList(List<SubListBean> subList) {
        this.subList = subList;
    }

    public List<DetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsBean> details) {
        this.details = details;
    }

    @Override
    public int getItemType() {
        return getOrderRace();
    }

    public static class SubListBean {
        /**
         * id : 200
         * orderNum : 1357522095046688-001
         * memberId : 2006220000001645
         * memberName : YV7913E6
         * memberPhone : 13520409204
         * memberFaceUrl : http://hmm-public.oss-cn-beijing.aliyuncs.com/hmm-user-dev/images/59a63dff-4e10-4066-8be5-9af21b2f52b9.png
         * totalGoodsAmount : 25.5
         * totalDeliverAmount : 0.0
         * totalPreferentialAmount : 0.0
         * totalReductionAmount : 0.0
         * totalPayAmount : 25.5
         * totalRefundAmount : 0.0
         * mchId : 8
         * mchName : 爱婴岛
         * shopId : 59
         * shopName : 测试一下01
         * shopChainName : 测试一下01
         * shopBrandName : 测试一下01
         * shopAddress : null
         * deliverType : 10
         * deliverConsigneeName : xxx
         * deliverConsigneePhone : 135xxxxxxxx
         * deliverConsigneeProvince : 320000
         * deliverConsigneeProvinceName : 江苏省
         * deliverConsigneeCity : 320500
         * deliverConsigneeCityName : 苏州市
         * deliverConsigneeDistrict : 320506
         * deliverConsigneeDistrictName : 吴中区
         * deliverConsigneeAddress : 吴中区xx小区
         * deliverLatitude : 31.336392
         * deliverLongitude : 120.617296
         * deliverDate : 2020-07-31 00:00:00
         * deliverTime : 上午
         * paymentId : null
         * paymentType : null
         * paymentStatus : 0
         * paymentSuccessTime : null
         * refundStatus : 0
         * orderStatus : 0
         * orderSource : 1
         * orderPlatform : IOS
         * orderType : 0
         * orderRace : 2
         * isMain : 0
         * mainId : 199
         * latestPayTime : 2020-07-07 10:16:18
         * comment : null
         * createBy : 2006220000001645
         * createTime : 2020-07-06 10:16:18
         * updateBy : 2006220000001645
         * updateTime : 2020-07-06 10:16:18
         * bargainId : null
         * bargainMemberId : null
         * subList : []
         * details : [{"id":317,"orderId":200,"orderNum":"1357522095046688-001","goodsId":68,"goodsSpec":60,"goodsTitle":"小儿推拿大促销-限时抢购666 红色 80 S","goodsSpecDesc":"红色 80 S","goodsImage":"http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/47dea72f-6363-4dd7-8de3-7792f936b525.jpg","barcodeSku":null,"goodsQuantity":2,"goodsPrice":1.5,"goodsPreferentialAmount":0,"goodsReductionAmount":0,"goodsPayAmount":1.5,"totalAmount":3,"totalPreferentialAmount":0,"totalReductionAmount":0,"totalPayAmount":3,"refundTotalQuantity":0,"refundTotalAmount":0,"isAddition":0,"additionType":1,"createBy":"2006220000001645","createTime":"2020-07-06 10:16:18","updateBy":"2006220000001645","updateTime":"2020-07-06 10:16:18"},{"id":318,"orderId":200,"orderNum":"1357522095046688-001","goodsId":60,"goodsSpec":85,"goodsTitle":"小儿推拿大促销-限时抢购666 红色 100 M","goodsSpecDesc":"红色 100 M","goodsImage":"http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/cbb28b34-8e6b-446e-8952-1a77fd3d545d.jpg","barcodeSku":null,"goodsQuantity":3,"goodsPrice":7.5,"goodsPreferentialAmount":0,"goodsReductionAmount":0,"goodsPayAmount":7.5,"totalAmount":22.5,"totalPreferentialAmount":0,"totalReductionAmount":0,"totalPayAmount":22.5,"refundTotalQuantity":0,"refundTotalAmount":0,"isAddition":0,"additionType":1,"createBy":"2006220000001645","createTime":"2020-07-06 10:16:18","updateBy":"2006220000001645","updateTime":"2020-07-06 10:16:18"}]
         * isRegiment : 0
         * residuePayTime : 82549
         */

        private long id;
        private String orderNum;
        private String memberId;
        private String memberName;
        private String memberPhone;
        private String memberFaceUrl;
        private double totalGoodsAmount;
        private double totalDeliverAmount;
        private double totalPreferentialAmount;
        private double totalReductionAmount;
        private double totalPayAmount;
        private double totalRefundAmount;
        private long mchId;
        private String mchName;
        private long shopId;
        private String shopName;
        private String shopChainName;
        private String shopBrandName;
        private Object shopAddress;
        private int deliverType;
        private String deliverConsigneeName;
        private String deliverConsigneePhone;
        private String deliverConsigneeProvince;
        private String deliverConsigneeProvinceName;
        private String deliverConsigneeCity;
        private String deliverConsigneeCityName;
        private String deliverConsigneeDistrict;
        private String deliverConsigneeDistrictName;
        private String deliverConsigneeAddress;
        private double deliverLatitude;
        private double deliverLongitude;
        private String deliverDate;
        private String deliverTime;
        private Object paymentId;
        private Object paymentType;
        private int paymentStatus;
        private Object paymentSuccessTime;
        private int refundStatus;
        private int orderStatus;
        private int orderSource;
        private String orderPlatform;
        private int orderType;
        private int orderRace;
        private int isMain;
        private long mainId;
        private int commentStatus;
        private String latestPayTime;
        private Object comment;
        private String createBy;
        private String createTime;
        private String updateBy;
        private String updateTime;
        private Object bargainId;
        private Object bargainMemberId;
        private int isRegiment;
        private int residuePayTime;
        private List<?> subList;
        private List<DetailsBean> details;

        public int getCommentStatus() {
            return commentStatus;
        }

        public void setCommentStatus(int commentStatus) {
            this.commentStatus = commentStatus;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberPhone() {
            return memberPhone;
        }

        public void setMemberPhone(String memberPhone) {
            this.memberPhone = memberPhone;
        }

        public String getMemberFaceUrl() {
            return memberFaceUrl;
        }

        public void setMemberFaceUrl(String memberFaceUrl) {
            this.memberFaceUrl = memberFaceUrl;
        }

        public double getTotalGoodsAmount() {
            return totalGoodsAmount;
        }

        public void setTotalGoodsAmount(double totalGoodsAmount) {
            this.totalGoodsAmount = totalGoodsAmount;
        }

        public double getTotalDeliverAmount() {
            return totalDeliverAmount;
        }

        public void setTotalDeliverAmount(double totalDeliverAmount) {
            this.totalDeliverAmount = totalDeliverAmount;
        }

        public double getTotalPreferentialAmount() {
            return totalPreferentialAmount;
        }

        public void setTotalPreferentialAmount(double totalPreferentialAmount) {
            this.totalPreferentialAmount = totalPreferentialAmount;
        }

        public double getTotalReductionAmount() {
            return totalReductionAmount;
        }

        public void setTotalReductionAmount(double totalReductionAmount) {
            this.totalReductionAmount = totalReductionAmount;
        }

        public double getTotalPayAmount() {
            return totalPayAmount;
        }

        public void setTotalPayAmount(double totalPayAmount) {
            this.totalPayAmount = totalPayAmount;
        }

        public double getTotalRefundAmount() {
            return totalRefundAmount;
        }

        public void setTotalRefundAmount(double totalRefundAmount) {
            this.totalRefundAmount = totalRefundAmount;
        }

        public long getMchId() {
            return mchId;
        }

        public void setMchId(int mchId) {
            this.mchId = mchId;
        }

        public String getMchName() {
            return mchName;
        }

        public void setMchName(String mchName) {
            this.mchName = mchName;
        }

        public long getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopChainName() {
            return shopChainName;
        }

        public void setShopChainName(String shopChainName) {
            this.shopChainName = shopChainName;
        }

        public String getShopBrandName() {
            return shopBrandName;
        }

        public void setShopBrandName(String shopBrandName) {
            this.shopBrandName = shopBrandName;
        }

        public Object getShopAddress() {
            return shopAddress;
        }

        public void setShopAddress(Object shopAddress) {
            this.shopAddress = shopAddress;
        }

        public int getDeliverType() {
            return deliverType;
        }

        public void setDeliverType(int deliverType) {
            this.deliverType = deliverType;
        }

        public String getDeliverConsigneeName() {
            return deliverConsigneeName;
        }

        public void setDeliverConsigneeName(String deliverConsigneeName) {
            this.deliverConsigneeName = deliverConsigneeName;
        }

        public String getDeliverConsigneePhone() {
            return deliverConsigneePhone;
        }

        public void setDeliverConsigneePhone(String deliverConsigneePhone) {
            this.deliverConsigneePhone = deliverConsigneePhone;
        }

        public String getDeliverConsigneeProvince() {
            return deliverConsigneeProvince;
        }

        public void setDeliverConsigneeProvince(String deliverConsigneeProvince) {
            this.deliverConsigneeProvince = deliverConsigneeProvince;
        }

        public String getDeliverConsigneeProvinceName() {
            return deliverConsigneeProvinceName;
        }

        public void setDeliverConsigneeProvinceName(String deliverConsigneeProvinceName) {
            this.deliverConsigneeProvinceName = deliverConsigneeProvinceName;
        }

        public String getDeliverConsigneeCity() {
            return deliverConsigneeCity;
        }

        public void setDeliverConsigneeCity(String deliverConsigneeCity) {
            this.deliverConsigneeCity = deliverConsigneeCity;
        }

        public String getDeliverConsigneeCityName() {
            return deliverConsigneeCityName;
        }

        public void setDeliverConsigneeCityName(String deliverConsigneeCityName) {
            this.deliverConsigneeCityName = deliverConsigneeCityName;
        }

        public String getDeliverConsigneeDistrict() {
            return deliverConsigneeDistrict;
        }

        public void setDeliverConsigneeDistrict(String deliverConsigneeDistrict) {
            this.deliverConsigneeDistrict = deliverConsigneeDistrict;
        }

        public String getDeliverConsigneeDistrictName() {
            return deliverConsigneeDistrictName;
        }

        public void setDeliverConsigneeDistrictName(String deliverConsigneeDistrictName) {
            this.deliverConsigneeDistrictName = deliverConsigneeDistrictName;
        }

        public String getDeliverConsigneeAddress() {
            return deliverConsigneeAddress;
        }

        public void setDeliverConsigneeAddress(String deliverConsigneeAddress) {
            this.deliverConsigneeAddress = deliverConsigneeAddress;
        }

        public double getDeliverLatitude() {
            return deliverLatitude;
        }

        public void setDeliverLatitude(double deliverLatitude) {
            this.deliverLatitude = deliverLatitude;
        }

        public double getDeliverLongitude() {
            return deliverLongitude;
        }

        public void setDeliverLongitude(double deliverLongitude) {
            this.deliverLongitude = deliverLongitude;
        }

        public String getDeliverDate() {
            return deliverDate;
        }

        public void setDeliverDate(String deliverDate) {
            this.deliverDate = deliverDate;
        }

        public String getDeliverTime() {
            return deliverTime;
        }

        public void setDeliverTime(String deliverTime) {
            this.deliverTime = deliverTime;
        }

        public Object getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(Object paymentId) {
            this.paymentId = paymentId;
        }

        public Object getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(Object paymentType) {
            this.paymentType = paymentType;
        }

        public int getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(int paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public Object getPaymentSuccessTime() {
            return paymentSuccessTime;
        }

        public void setPaymentSuccessTime(Object paymentSuccessTime) {
            this.paymentSuccessTime = paymentSuccessTime;
        }

        public int getRefundStatus() {
            return refundStatus;
        }

        public void setRefundStatus(int refundStatus) {
            this.refundStatus = refundStatus;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getOrderSource() {
            return orderSource;
        }

        public void setOrderSource(int orderSource) {
            this.orderSource = orderSource;
        }

        public String getOrderPlatform() {
            return orderPlatform;
        }

        public void setOrderPlatform(String orderPlatform) {
            this.orderPlatform = orderPlatform;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public int getOrderRace() {
            return orderRace;
        }

        public void setOrderRace(int orderRace) {
            this.orderRace = orderRace;
        }

        public int getIsMain() {
            return isMain;
        }

        public void setIsMain(int isMain) {
            this.isMain = isMain;
        }

        public long getMainId() {
            return mainId;
        }

        public void setMainId(long mainId) {
            this.mainId = mainId;
        }

        public String getLatestPayTime() {
            return latestPayTime;
        }

        public void setLatestPayTime(String latestPayTime) {
            this.latestPayTime = latestPayTime;
        }

        public Object getComment() {
            return comment;
        }

        public void setComment(Object comment) {
            this.comment = comment;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public Object getBargainId() {
            return bargainId;
        }

        public void setBargainId(Object bargainId) {
            this.bargainId = bargainId;
        }

        public Object getBargainMemberId() {
            return bargainMemberId;
        }

        public void setBargainMemberId(Object bargainMemberId) {
            this.bargainMemberId = bargainMemberId;
        }

        public int getIsRegiment() {
            return isRegiment;
        }

        public void setIsRegiment(int isRegiment) {
            this.isRegiment = isRegiment;
        }

        public int getResiduePayTime() {
            return residuePayTime;
        }

        public void setResiduePayTime(int residuePayTime) {
            this.residuePayTime = residuePayTime;
        }

        public List<?> getSubList() {
            return subList;
        }

        public void setSubList(List<?> subList) {
            this.subList = subList;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

        public static class DetailsBean {
            /**
             * id : 317
             * orderId : 200
             * orderNum : 1357522095046688-001
             * goodsId : 68
             * goodsSpec : 60
             * goodsTitle : 小儿推拿大促销-限时抢购666 红色 80 S
             * goodsSpecDesc : 红色 80 S
             * goodsImage : http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/47dea72f-6363-4dd7-8de3-7792f936b525.jpg
             * barcodeSku : null
             * goodsQuantity : 2
             * goodsPrice : 1.5
             * goodsPreferentialAmount : 0.0
             * goodsReductionAmount : 0.0
             * goodsPayAmount : 1.5
             * totalAmount : 3.0
             * totalPreferentialAmount : 0.0
             * totalReductionAmount : 0.0
             * totalPayAmount : 3.0
             * refundTotalQuantity : 0
             * refundTotalAmount : 0.0
             * isAddition : 0
             * additionType : 1
             * createBy : 2006220000001645
             * createTime : 2020-07-06 10:16:18
             * updateBy : 2006220000001645
             * updateTime : 2020-07-06 10:16:18
             */

            private long id;
            private long orderId;
            private String orderNum;
            private long goodsId;
            private int goodsSpec;
            private String goodsTitle;
            private String goodsSpecDesc;
            private String goodsImage;
            private Object barcodeSku;
            private int goodsQuantity;
            private double goodsPrice;
            private double goodsPreferentialAmount;
            private double goodsReductionAmount;
            private double goodsPayAmount;
            private double totalAmount;
            private double totalPreferentialAmount;
            private double totalReductionAmount;
            private double totalPayAmount;
            private int refundTotalQuantity;
            private double refundTotalAmount;
            private int isAddition;
            private int additionType;
            private String createBy;
            private String createTime;
            private String updateBy;
            private String updateTime;
            private int allowApplyRefund;

            public int getAllowApplyRefund() {
                return allowApplyRefund;
            }

            public void setAllowApplyRefund(int allowApplyRefund) {
                this.allowApplyRefund = allowApplyRefund;
            }

            public long getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public String getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(String orderNum) {
                this.orderNum = orderNum;
            }

            public long getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public int getGoodsSpec() {
                return goodsSpec;
            }

            public void setGoodsSpec(int goodsSpec) {
                this.goodsSpec = goodsSpec;
            }

            public String getGoodsTitle() {
                return goodsTitle;
            }

            public void setGoodsTitle(String goodsTitle) {
                this.goodsTitle = goodsTitle;
            }

            public String getGoodsSpecDesc() {
                return goodsSpecDesc;
            }

            public void setGoodsSpecDesc(String goodsSpecDesc) {
                this.goodsSpecDesc = goodsSpecDesc;
            }

            public String getGoodsImage() {
                return goodsImage;
            }

            public void setGoodsImage(String goodsImage) {
                this.goodsImage = goodsImage;
            }

            public Object getBarcodeSku() {
                return barcodeSku;
            }

            public void setBarcodeSku(Object barcodeSku) {
                this.barcodeSku = barcodeSku;
            }

            public int getGoodsQuantity() {
                return goodsQuantity;
            }

            public void setGoodsQuantity(int goodsQuantity) {
                this.goodsQuantity = goodsQuantity;
            }

            public double getGoodsPrice() {
                return goodsPrice;
            }

            public void setGoodsPrice(double goodsPrice) {
                this.goodsPrice = goodsPrice;
            }

            public double getGoodsPreferentialAmount() {
                return goodsPreferentialAmount;
            }

            public void setGoodsPreferentialAmount(double goodsPreferentialAmount) {
                this.goodsPreferentialAmount = goodsPreferentialAmount;
            }

            public double getGoodsReductionAmount() {
                return goodsReductionAmount;
            }

            public void setGoodsReductionAmount(double goodsReductionAmount) {
                this.goodsReductionAmount = goodsReductionAmount;
            }

            public double getGoodsPayAmount() {
                return goodsPayAmount;
            }

            public void setGoodsPayAmount(double goodsPayAmount) {
                this.goodsPayAmount = goodsPayAmount;
            }

            public double getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(double totalAmount) {
                this.totalAmount = totalAmount;
            }

            public double getTotalPreferentialAmount() {
                return totalPreferentialAmount;
            }

            public void setTotalPreferentialAmount(double totalPreferentialAmount) {
                this.totalPreferentialAmount = totalPreferentialAmount;
            }

            public double getTotalReductionAmount() {
                return totalReductionAmount;
            }

            public void setTotalReductionAmount(double totalReductionAmount) {
                this.totalReductionAmount = totalReductionAmount;
            }

            public double getTotalPayAmount() {
                return totalPayAmount;
            }

            public void setTotalPayAmount(double totalPayAmount) {
                this.totalPayAmount = totalPayAmount;
            }

            public int getRefundTotalQuantity() {
                return refundTotalQuantity;
            }

            public void setRefundTotalQuantity(int refundTotalQuantity) {
                this.refundTotalQuantity = refundTotalQuantity;
            }

            public double getRefundTotalAmount() {
                return refundTotalAmount;
            }

            public void setRefundTotalAmount(double refundTotalAmount) {
                this.refundTotalAmount = refundTotalAmount;
            }

            public int getIsAddition() {
                return isAddition;
            }

            public void setIsAddition(int isAddition) {
                this.isAddition = isAddition;
            }

            public int getAdditionType() {
                return additionType;
            }

            public void setAdditionType(int additionType) {
                this.additionType = additionType;
            }

            public String getCreateBy() {
                return createBy;
            }

            public void setCreateBy(String createBy) {
                this.createBy = createBy;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateBy() {
                return updateBy;
            }

            public void setUpdateBy(String updateBy) {
                this.updateBy = updateBy;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }
        }
    }

    public static class DetailsBean {
        /**
         * id : 317
         * orderId : 200
         * orderNum : 1357522095046688-001
         * goodsId : 68
         * goodsSpec : 60
         * goodsTitle : 小儿推拿大促销-限时抢购666 红色 80 S
         * goodsSpecDesc : 红色 80 S
         * goodsImage : http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/47dea72f-6363-4dd7-8de3-7792f936b525.jpg
         * barcodeSku : null
         * goodsQuantity : 2
         * goodsPrice : 1.5
         * goodsPreferentialAmount : 0.0
         * goodsReductionAmount : 0.0
         * goodsPayAmount : 1.5
         * totalAmount : 3.0
         * totalPreferentialAmount : 0.0
         * totalReductionAmount : 0.0
         * totalPayAmount : 3.0
         * refundTotalQuantity : 0
         * refundTotalAmount : 0.0
         * isAddition : 0
         * additionType : 1
         * createBy : 2006220000001645
         * createTime : 2020-07-06 10:16:18
         * updateBy : 2006220000001645
         * updateTime : 2020-07-06 10:16:18
         */

        private long id;
        private long orderId;
        private String orderNum;
        private int goodsId;
        private int goodsSpec;
        private String goodsTitle;
        private String goodsSpecDesc;
        private String goodsImage;
        private Object barcodeSku;
        private int goodsQuantity;
        private double goodsPrice;
        private double goodsPoints;
        private double goodsPreferentialAmount;
        private double goodsReductionAmount;
        private double goodsPayAmount;
        private double totalAmount;
        private double totalPreferentialAmount;
        private double totalReductionAmount;
        private double totalPayAmount;
        private int refundTotalQuantity;
        private double refundTotalAmount;
        private int isAddition;
        private int allowApplyRefund;
        private int additionType;
        private int goodsMarketingType;
        private String createBy;
        private String createTime;
        private String updateBy;
        private String updateTime;

        public double getGoodsPoints() {
            return goodsPoints;
        }

        public void setGoodsPoints(double goodsPoints) {
            this.goodsPoints = goodsPoints;
        }

        public int getGoodsMarketingType() {
            return goodsMarketingType;
        }

        public void setGoodsMarketingType(int goodsMarketingType) {
            this.goodsMarketingType = goodsMarketingType;
        }

        public int getAllowApplyRefund() {
            return allowApplyRefund;
        }

        public void setAllowApplyRefund(int allowApplyRefund) {
            this.allowApplyRefund = allowApplyRefund;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getOrderId() {
            return orderId;
        }

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public int getGoodsSpec() {
            return goodsSpec;
        }

        public void setGoodsSpec(int goodsSpec) {
            this.goodsSpec = goodsSpec;
        }

        public String getGoodsTitle() {
            return goodsTitle;
        }

        public void setGoodsTitle(String goodsTitle) {
            this.goodsTitle = goodsTitle;
        }

        public String getGoodsSpecDesc() {
            return goodsSpecDesc;
        }

        public void setGoodsSpecDesc(String goodsSpecDesc) {
            this.goodsSpecDesc = goodsSpecDesc;
        }

        public String getGoodsImage() {
            return goodsImage;
        }

        public void setGoodsImage(String goodsImage) {
            this.goodsImage = goodsImage;
        }

        public Object getBarcodeSku() {
            return barcodeSku;
        }

        public void setBarcodeSku(Object barcodeSku) {
            this.barcodeSku = barcodeSku;
        }

        public int getGoodsQuantity() {
            return goodsQuantity;
        }

        public void setGoodsQuantity(int goodsQuantity) {
            this.goodsQuantity = goodsQuantity;
        }

        public double getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoodsPrice(double goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public double getGoodsPreferentialAmount() {
            return goodsPreferentialAmount;
        }

        public void setGoodsPreferentialAmount(double goodsPreferentialAmount) {
            this.goodsPreferentialAmount = goodsPreferentialAmount;
        }

        public double getGoodsReductionAmount() {
            return goodsReductionAmount;
        }

        public void setGoodsReductionAmount(double goodsReductionAmount) {
            this.goodsReductionAmount = goodsReductionAmount;
        }

        public double getGoodsPayAmount() {
            return goodsPayAmount;
        }

        public void setGoodsPayAmount(double goodsPayAmount) {
            this.goodsPayAmount = goodsPayAmount;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public double getTotalPreferentialAmount() {
            return totalPreferentialAmount;
        }

        public void setTotalPreferentialAmount(double totalPreferentialAmount) {
            this.totalPreferentialAmount = totalPreferentialAmount;
        }

        public double getTotalReductionAmount() {
            return totalReductionAmount;
        }

        public void setTotalReductionAmount(double totalReductionAmount) {
            this.totalReductionAmount = totalReductionAmount;
        }

        public double getTotalPayAmount() {
            return totalPayAmount;
        }

        public void setTotalPayAmount(double totalPayAmount) {
            this.totalPayAmount = totalPayAmount;
        }

        public int getRefundTotalQuantity() {
            return refundTotalQuantity;
        }

        public void setRefundTotalQuantity(int refundTotalQuantity) {
            this.refundTotalQuantity = refundTotalQuantity;
        }

        public double getRefundTotalAmount() {
            return refundTotalAmount;
        }

        public void setRefundTotalAmount(double refundTotalAmount) {
            this.refundTotalAmount = refundTotalAmount;
        }

        public int getIsAddition() {
            return isAddition;
        }

        public void setIsAddition(int isAddition) {
            this.isAddition = isAddition;
        }

        public int getAdditionType() {
            return additionType;
        }

        public void setAdditionType(int additionType) {
            this.additionType = additionType;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

}
