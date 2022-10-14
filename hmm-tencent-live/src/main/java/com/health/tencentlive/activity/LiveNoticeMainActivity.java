package com.health.tencentlive.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.tencentlive.R;
import com.health.tencentlive.fragment.LiveNoticeFragment;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.widget.TopBar;

@Route(path = TencentLiveRoutes.LiveNotice)
public class LiveNoticeMainActivity extends BaseActivity implements IsFitsSystemWindows {


    private com.healthy.library.widget.TopBar topBar;
    private android.widget.FrameLayout fragmentParent;

    @Autowired
    String courseId;//直播ID
    @Autowired
    String merchantId;//商户id
    @Autowired
    String shopId;//门店id
    @Autowired
    String liveShareType;//可能分享类型
    @Autowired
    String fromMemberId;
    @Autowired
    String referral_code;
    @Autowired
    String shareLiveGoodsId;//可能分享商品了
    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_notice;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
//        if(TextUtils.isEmpty(merchantId)){
//            merchantId= SpUtils.getValue(mContext, SpKey.CHOSE_MC);
//        }
//        if(TextUtils.isEmpty(shopId)){
//            shopId= SpUtils.getValue(mContext, SpKey.CHOSE_SHOP);
//        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentParent , LiveNoticeFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("isActivity","1")
                .puts("courseId",courseId)
                .puts("merchantId",merchantId)
                .puts("shopId",shopId)
                .puts("liveShareType",liveShareType)
                .puts("fromMemberId",fromMemberId)
                .puts("referral_code",referral_code)
                .puts("shareLiveGoodsId",shareLiveGoodsId)
        )).commitAllowingStateLoss();
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {


        topBar = (TopBar) findViewById(R.id.top_bar);
        fragmentParent = (FrameLayout) findViewById(R.id.fragmentParent);
    }
}
