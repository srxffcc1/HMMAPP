package com.healthy.library.model;

/**
 * @author Li
 * @date 2019/04/09 15:29
 * @des 微信支付
 */

public class PayResultFresh {
    private int errCode;

    public PayResultFresh() {
    }

    public PayResultFresh(int errCode) {
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
