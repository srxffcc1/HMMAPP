package com.healthy.library.model;

/**
 * @author Li
 * @date 2019/04/09 15:29
 * @des 微信支付
 */

public class OlderResultFresh {
    private int errCode;

    public OlderResultFresh() {
    }

    public OlderResultFresh(int errCode) {
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
