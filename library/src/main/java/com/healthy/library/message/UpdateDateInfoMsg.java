package com.healthy.library.message;

import java.util.Date;

/**
 * @author Li
 * @date 2019/05/09 16:48
 * @des 更改用户信息
 */

public class UpdateDateInfoMsg {
    public int postion;
    public Date start;
    public Date end;

    public UpdateDateInfoMsg(int postion) {
        this.postion = postion;
    }

    public UpdateDateInfoMsg(int postion, Date start, Date end) {
        this.postion = postion;
        this.start = start;
        this.end = end;
    }
}
