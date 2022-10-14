package com.healthy.library.message;

/**
 * @author Li
 * @date 2019/05/09 16:48
 * @des 更改用户信息
 */

public class CouponShowBean {
    public String actId ="";
    public String detailId="";

    public CouponShowBean(String actId,String detailId) {
        this.actId = actId;
        this.detailId=detailId;
    }
}
