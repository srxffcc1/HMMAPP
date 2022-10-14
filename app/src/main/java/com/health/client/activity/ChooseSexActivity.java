package com.health.client.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.client.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.widget.ImageTextView;

/**
 * @author Li
 * @date 2019/03/18 10:54
 * @des 选择性别
 */

@Route(path = AppRoutes.APP_CHOOSE_SEX)
public class ChooseSexActivity extends BaseActivity implements IsNoNeedPatchShow {

    private TextView tvChooseStatusTitle;
    private com.healthy.library.widget.ImageTextView iconSexWomen;
    private com.healthy.library.widget.ImageTextView iconSexMan;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_sex;
    }

    @Override
    protected void findViews() {
        initView();

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        iconSexWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build(AppRoutes.APP_CHOOSE_STATUS)
                        .withString("sex", "女")
                        .navigation();
            }
        });
        iconSexMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build(AppRoutes.APP_CHOOSE_STATUS)
                        .withString("sex", "男")
                        .navigation();
            }
        });
    }
    


    public void back(View view) {
        finish();
    }

    private void initView() {
        tvChooseStatusTitle = (TextView) findViewById(R.id.tv_choose_status_title);
        iconSexWomen = (ImageTextView) findViewById(R.id.icon_sex_women);
        iconSexMan = (ImageTextView) findViewById(R.id.icon_sex_man);
    }
}
