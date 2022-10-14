package com.healthy.library.message;

import com.healthy.library.model.AppIndexMarketingPendant;

import java.util.List;

/**
 * @author Li
 * @date 2019/05/09 16:48
 * @des 更改用户信息
 */

public class UpdateAPPIndexCustom {

    public List<AppIndexMarketingPendant> appIndexMarketingPendant;//营销挂件底部的
    public UpdateAPPIndexCustom(List<AppIndexMarketingPendant> list) {
        appIndexMarketingPendant=list;
    }
}
