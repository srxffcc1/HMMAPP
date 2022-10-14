package com.health.discount.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.DiscountRoutes;

/**
 * 砍价列表
 */
@Route(path = DiscountRoutes.DIS_COUPONUSE)
public class CouponUseActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }
}
