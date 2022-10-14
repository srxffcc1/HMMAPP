package com.health.faq.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.faq.R;
import com.health.faq.fragment.MeetProfessorFragment;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.businessutil.LocUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

@Route(path = AppRoutes.MODULE_EXPERT_FAQ2)
public class FaqIndexActivity extends BaseActivity implements  IsFitsSystemWindows {
    @Autowired
    String cityNo;
    private android.widget.FrameLayout childFrame;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_faq_index;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        initView();
        if(TextUtils.isEmpty(cityNo)){
            cityNo= LocUtil.getCityNo(mContext, SpKey.LOC_ORG);
        }
        Map<String, Object> maporg=new HashMap<>();
        maporg.put("addressCity",cityNo);
        maporg.put("addressCityOrg",cityNo);//用来判断本地专家标签的
        //System.out.println("index过来的"+cityNo);
        getSupportFragmentManager().beginTransaction().replace(R.id.childFrame ,MeetProfessorFragment.newInstance(maporg)).commitAllowingStateLoss();
    }

    private void initView() {
        childFrame = (FrameLayout) findViewById(R.id.childFrame);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("event2FaqHomeTimeLimit"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("event2FaqHomeTimeLimit"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
