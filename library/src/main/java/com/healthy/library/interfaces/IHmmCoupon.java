package com.healthy.library.interfaces;

/**
 * 优惠券实体接口
 */
public interface IHmmCoupon {

    public String getCouponId();//优惠券的固有Id

    public String getUseId();//优惠券领取记录Id 用于使用时作为凭证

    public int getCouponType();//优惠券类型 平台或者商家 1 2 3

    public String getCouponTypeName();//优惠券类型 平台或者商家

    public String getCouponUseName();//优惠券配置的名字 比如 新人专属优惠券

    public String getCouponUseLimitName();//优惠券限制名称 比如 仅限购买奶粉类商品使用

    public boolean isSupportSuperposition();//支持叠加平台券

    public String getCouponNormalName();//优惠券标准名字    满100减20 或者无门槛10

    public String getCouponUseTip();//优惠券使用说明

    public String getCouponRemark();//优惠券备注

    public String getCouponDenomination();//优惠券额度 20

    public String getOverPayment();//满多少可用 200 或者 0

    public String getRequirement();//使用要求的具体名字 满200可用, 无门槛

    public String getTimeValidityStart();//优惠券有效期开始

    public String getTimeValidityEnd();//优惠券有效期结束

    public String getTimeValidity();//优惠券有效期结束

    public boolean isHold();//优惠券是否持有

    public boolean isCanReceive();//是否可以领取 用于领券界面判断有没有余量

    public int getCriterionType();//获得价格体系

    public int getStatus();//判断已使用还是已过期

}
