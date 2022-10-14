package com.healthy.library.message;

import com.amap.api.location.AMapLocation;
import com.healthy.library.model.LocVip;

/**
 * @author Li
 * @date 2019/05/09 16:48
 * @des 更改用户信息
 */

public class UpdateUserShop {
    public LocVip.Local.MerchantsShop nowShop;
    public AMapLocation aMapLocation;

    public UpdateUserShop(LocVip.Local.MerchantsShop nowShop, AMapLocation aMapLocation) {
        this.nowShop = nowShop;
        this.aMapLocation = aMapLocation;
    }
}
