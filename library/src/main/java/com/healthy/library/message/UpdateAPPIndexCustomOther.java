package com.healthy.library.message;

import com.healthy.library.model.AppIndexCustomItem;
import com.healthy.library.model.AppIndexMarketingPendant;

import java.util.List;

/**
 * @author Li
 * @date 2019/05/09 16:48
 * @des 更改用户信息
 */

public class UpdateAPPIndexCustomOther {

    public List<AppIndexCustomItem> bottomNavigation;
    public UpdateAPPIndexCustomOther(List<AppIndexCustomItem> list) {
        bottomNavigation=list;
    }
}
