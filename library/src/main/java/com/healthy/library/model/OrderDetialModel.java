package com.healthy.library.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

public class OrderDetialModel implements MultiItemEntity, Serializable {


    /**
     * id : 164
     * orderNum : 1357160562819104
     * memberId : 2006220000001645
     * memberName : YV7913E6
     * memberPhone : 13520409204
     * memberFaceUrl : http://hmm-public.oss-cn-beijing.aliyuncs.com/hmm-user-dev/images/59a63dff-4e10-4066-8be5-9af21b2f52b9.png
     * totalGoodsAmount : 0.07
     * totalDeliverAmount : 0.0
     * totalPreferentialAmount : 0.0
     * totalReductionAmount : 0.0
     * totalPayAmount : 0.07
     * totalRefundAmount : 0.0
     * mchId : 8
     * mchName : 爱婴岛
     * shopId : 45
     * shopName : 爱婴岛母婴
     * shopChainName : 苏州总店
     * shopBrandName : 爱婴岛
     * shopAddress : null
     * deliverType : 10
     * deliverConsigneeName : 刘炜
     * deliverConsigneePhone : 13520409204
     * deliverConsigneeProvince : 320000
     * deliverConsigneeProvinceName : 江苏省
     * deliverConsigneeCity : 320500
     * deliverConsigneeCityName : 苏州市
     * deliverConsigneeDistrict : 320505
     * deliverConsigneeDistrictName : 虎丘区
     * deliverConsigneeAddress : 江苏省苏州市虎丘区苏州高新广场
     * deliverLatitude : 31.295151
     * deliverLongitude : 120.569269
     * deliverDate : 2020-07-07 00:00:00
     * deliverTime : 下午
     * paymentId : null
     * paymentType : null
     * paymentStatus : 0
     * paymentSuccessTime : null
     * refundStatus : 0
     * orderStatus : 0
     * orderSource : 1
     * orderPlatform : Android
     * orderType : 0
     * orderRace : 2
     * isMain : 1
     * mainId : null
     * latestPayTime : 2020-07-05 10:23:07
     * comment : null
     * createBy : 2006220000001645
     * createTime : 2020-07-04 10:23:07
     * updateBy : 2006220000001645
     * updateTime : 2020-07-04 10:23:07
     * bargainId : null
     * bargainMemberId : null
     * subList : []
     * details : [{"id":280,"orderId":164,"orderNum":"1357160562819104","goodsId":60,"goodsSpec":86,"goodsTitle":"小儿推拿大促销-限时抢购666 红色 80 S","goodsSpecDesc":"红色 80 S","goodsImage":"http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/cbb28b34-8e6b-446e-8952-1a77fd3d545d.jpg","barcodeSku":null,"goodsQuantity":1,"goodsPrice":0.05,"goodsPreferentialAmount":0,"goodsReductionAmount":0,"goodsPayAmount":0.05,"totalAmount":0.05,"totalPreferentialAmount":0,"totalReductionAmount":0,"totalPayAmount":0.05,"refundTotalQuantity":0,"refundTotalAmount":0,"isAddition":0,"additionType":null,"createBy":"2006220000001645","createTime":"2020-07-04 10:23:07","updateBy":"2006220000001645","updateTime":"2020-07-04 10:23:07"},{"id":281,"orderId":164,"orderNum":"1357160562819104","goodsId":59,"goodsSpec":165,"goodsTitle":"小儿推拿大促销-限时抢购666 红色 100 L","goodsSpecDesc":"红色 100 L","goodsImage":"http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/c6dea564-30ca-4fe6-a582-6c4bf3d30a9f.jpg","barcodeSku":"464654646669","goodsQuantity":1,"goodsPrice":0.01,"goodsPreferentialAmount":0,"goodsReductionAmount":0,"goodsPayAmount":0.01,"totalAmount":0.01,"totalPreferentialAmount":0,"totalReductionAmount":0,"totalPayAmount":0.01,"refundTotalQuantity":0,"refundTotalAmount":0,"isAddition":0,"additionType":null,"createBy":"2006220000001645","createTime":"2020-07-04 10:23:07","updateBy":"2006220000001645","updateTime":"2020-07-04 10:23:07"},{"id":282,"orderId":164,"orderNum":"1357160562819104","goodsId":64,"goodsSpec":166,"goodsTitle":"小儿推拿大促销-限时抢购666 红色 100 L","goodsSpecDesc":"红色 100 L","goodsImage":"http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/4ec5e68f-bf2a-4426-a8ac-90b1fa206f3e.jpg","barcodeSku":"464654646669","goodsQuantity":1,"goodsPrice":0.01,"goodsPreferentialAmount":0,"goodsReductionAmount":0,"goodsPayAmount":0.01,"totalAmount":0.01,"totalPreferentialAmount":0,"totalReductionAmount":0,"totalPayAmount":0.01,"refundTotalQuantity":0,"refundTotalAmount":0,"isAddition":0,"additionType":null,"createBy":"2006220000001645","createTime":"2020-07-04 10:23:07","updateBy":"2006220000001645","updateTime":"2020-07-04 10:23:07"}]
     * isRegiment : 0
     * residuePayTime : 81516
     */

    public long id;
    public String orderNum;
    public String memberId;
    public String memberName;
    public String memberPhone;
    public String memberFaceUrl;
    public double totalGoodsAmount;
    public double totalPoints;
    public double totalPayPoints;
    public double totalDeliverAmount;
    public double totalPreferentialAmount;
    public double totalReductionAmount;
    public double totalPayAmount;
    public double totalRefundAmount;
    public int mchId;
    public String mchName;
    public int shopId;
    public String shopName;
    public String shopChainName;
    public String shopBrandName;
    public String shopAddress;
    public int deliverType;
    public String deliverConsigneeName;
    public String deliverConsigneePhone;
    public String deliverConsigneeProvince;
    public String deliverConsigneeProvinceName;
    public String deliverConsigneeCity;
    public String deliverConsigneeCityName;
    public String deliverConsigneeDistrict;
    public String deliverConsigneeDistrictName;
    public String deliverConsigneeAddress;
    public double deliverLatitude;
    public double deliverLongitude;
    public String deliverDate;
    public String deliverTime;
    public Object paymentId;
    public String paymentType;
    public int paymentStatus;
    public String paymentSuccessTime;
    public String cancelTime;//取消订单时间
    public int refundStatus;
    public int orderStatus;
    public int orderSource;
    public String orderPlatform;
    public int orderType;
    public int orderRace;
    public int isMain;
    public long mainId;
    public String latestPayTime;
    public Object comment;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;
    public Object bargainId;
    public Object bargainMemberId;
    public int isRegiment;
    public int residuePayTime;
    public String latestConfirmDeliveryTime;

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

    public String getLatestConfirmDeliveryTime() {
        return latestConfirmDeliveryTime;
    }

    public void setLatestConfirmDeliveryTime(String latestConfirmDeliveryTime) {
        this.latestConfirmDeliveryTime = latestConfirmDeliveryTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getConfirmDeliveryTime() {
        return confirmDeliveryTime;
    }

    public void setConfirmDeliveryTime(String confirmDeliveryTime) {
        this.confirmDeliveryTime = confirmDeliveryTime;
    }

    public String getMchDeliveryTime() {
        return mchDeliveryTime;
    }

    public void setMchDeliveryTime(String mchDeliveryTime) {
        this.mchDeliveryTime = mchDeliveryTime;
    }

    public String confirmDeliveryTime;
    public String mchDeliveryTime;
    public List<SubListBean> subList;
    public List<DetailsBean> details;

    public OrderDetialModel() {
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

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentSuccessTime() {
        return paymentSuccessTime;
    }

    public void setPaymentSuccessTime(String paymentSuccessTime) {
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

    public static class SubListBean implements Serializable {
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
        private int mchId;
        private String mchName;
        private int shopId;
        private String shopName;
        private String shopChainName;
        private String shopBrandName;
        private String shopAddress;
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
        private String paymentType;
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
        private String latestPayTime;
        private Object comment;
        private String createBy;
        private String createTime;
        private String updateBy;
        private String updateTime;
        private Object bargainId;
        private Object bargainMemberId;
        private int isRegiment;
        private int commentStatus;
        private int residuePayTime;
        private List<SubListBean> subList;
        private List<DetailsBean> details;

        public SubListBean() {
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

        public String getShopAddress() {
            return shopAddress;
        }

        public void setShopAddress(String shopAddress) {
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

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
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

        public static class DetailsBean implements Serializable {
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
            public int allowApplyRefund;
            private int additionType;
            private String createBy;
            private String createTime;
            private String updateBy;
            private String updateTime;
            private String latestRefundId;

            public String getLatestRefundId() {
                return latestRefundId;
            }

            public void setLatestRefundId(String latestRefundId) {
                this.latestRefundId = latestRefundId;
            }

            public DetailsBean() {
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

    public static class DetailsBean implements Serializable {
        @Override
        public String toString() {
            return "DetailsBean{" +
                    "id=" + id +
                    ", orderId=" + orderId +
                    ", orderNum='" + orderNum + '\'' +
                    ", goodsId=" + goodsId +
                    ", goodsSpec=" + goodsSpec +
                    ", goodsTitle='" + goodsTitle + '\'' +
                    ", goodsSpecDesc='" + goodsSpecDesc + '\'' +
                    ", goodsImage='" + goodsImage + '\'' +
                    ", barcodeSku=" + barcodeSku +
                    ", goodsQuantity=" + goodsQuantity +
                    ", goodsPrice=" + goodsPrice +
                    ", goodsPreferentialAmount=" + goodsPreferentialAmount +
                    ", goodsReductionAmount=" + goodsReductionAmount +
                    ", goodsPayAmount=" + goodsPayAmount +
                    ", totalAmount=" + totalAmount +
                    ", totalPreferentialAmount=" + totalPreferentialAmount +
                    ", totalReductionAmount=" + totalReductionAmount +
                    ", totalPayAmount=" + totalPayAmount +
                    ", refundTotalQuantity=" + refundTotalQuantity +
                    ", refundTotalAmount=" + refundTotalAmount +
                    ", isAddition=" + isAddition +
                    ", additionType=" + additionType +
                    ", createBy='" + createBy + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", updateBy='" + updateBy + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    '}';
        }

        /**
         * id : 280
         * orderId : 164
         * orderNum : 1357160562819104
         * goodsId : 60
         * goodsSpec : 86
         * goodsTitle : 小儿推拿大促销-限时抢购666 红色 80 S
         * goodsSpecDesc : 红色 80 S
         * goodsImage : http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/cbb28b34-8e6b-446e-8952-1a77fd3d545d.jpg
         * barcodeSku : null
         * goodsQuantity : 1
         * goodsPrice : 0.05
         * goodsPreferentialAmount : 0.0
         * goodsReductionAmount : 0.0
         * goodsPayAmount : 0.05
         * totalAmount : 0.05
         * totalPreferentialAmount : 0.0
         * totalReductionAmount : 0.0
         * totalPayAmount : 0.05
         * refundTotalQuantity : 0
         * refundTotalAmount : 0.0
         * isAddition : 0
         * additionType : null
         * createBy : 2006220000001645
         * createTime : 2020-07-04 10:23:07
         * updateBy : 2006220000001645
         * updateTime : 2020-07-04 10:23:07
         */

        public long id;
        public long orderId;
        public String orderNum;
        public long goodsId;
        public int goodsSpec;
        public String goodsTitle;
        public String goodsSpecDesc;
        public String goodsImage;
        public Object barcodeSku;
        public int goodsQuantity;
        public double totalPayPoints;
        public double goodsPoints;
        public double goodsPrice;
        public double goodsPreferentialAmount;
        public double goodsReductionAmount;
        public double goodsPayAmount;
        public double totalAmount;
        public double totalPreferentialAmount;
        public double totalReductionAmount;
        public double totalPayAmount;
        public int refundTotalQuantity;
        public int allowApplyRefund;
        public double refundTotalAmount;
        public int isAddition;
        public Object additionType;
        public String createBy;
        public String createTime;
        public String updateBy;
        public String updateTime;
        public String latestRefundId;
        public int goodsMarketingType;//秒杀

        public double getTotalPayPoints() {
            return totalPayPoints;
        }

        public void setTotalPayPoints(double totalPayPoints) {
            this.totalPayPoints = totalPayPoints;
        }

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

        public String getLatestRefundId() {
            return latestRefundId;
        }

        public void setLatestRefundId(String latestRefundId) {
            this.latestRefundId = latestRefundId;
        }

        public DetailsBean() {
        }

        public int getAllowApplyRefund() {
            return allowApplyRefund;
        }

        public void setAllowApplyRefund(int allowApplyRefund) {
            this.allowApplyRefund = allowApplyRefund;
        }

        public DetailsBean(int id, int goodsId, String goodsTitle, String goodsSpecDesc, String goodsImage, int goodsQuantity, double goodsPayAmount) {
            this.id = id;
            this.goodsId = goodsId;
            this.goodsTitle = goodsTitle;
            this.goodsSpecDesc = goodsSpecDesc;
            this.goodsImage = goodsImage;
            this.goodsQuantity = goodsQuantity;
            this.goodsPayAmount = goodsPayAmount;
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

        public long getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(long goodsId) {
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

        public Object getAdditionType() {
            return additionType;
        }

        public void setAdditionType(Object additionType) {
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
