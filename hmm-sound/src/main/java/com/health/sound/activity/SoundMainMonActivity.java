package com.health.sound.activity;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.health.sound.R;
import com.health.sound.contract.SoundIndexContract;
import com.health.sound.fragment.SoundHomeMonFragment;
import com.health.sound.fragment.SoundTypeFragment;
import com.health.sound.model.SoundIndex;
import com.health.sound.presenter.SoundIndexPresenter;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ezy.assist.compat.SettingsCompat;
import permison.FloatWindowManager;


@Route(path = SoundRoutes.SOUND_MAIN_MON)
public class SoundMainMonActivity extends BaseActivity implements IsNeedShare,IsFitsSystemWindows, OnRefreshLoadMoreListener, SoundIndexContract.View {

    @Autowired
    String audioType;
    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
//    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SlidingTabLayout st;
    private View dividerStore;
    private ViewPager vp;
    private TopBar topBar;
    @Autowired
    int currentIndex = 0;
    private ImageView imgBackTmp;
    private SoundIndexPresenter soundIndexPresenter;
    Map<String,Object> indexmap=new HashMap<>();
    private ImageView soundTopBg;
    @Autowired
    String choseTypeName;
    private ImageView seachTip;

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sound_activity_mian_mon;
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void init(Bundle savedInstanceState) {

        ARouter.getInstance().inject(this);
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                showShare();
            }
        });
        seachTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SoundRoutes.SOUND_SEACH)
                        .navigation();
            }
        });
        if(TextUtils.isEmpty(audioType)){
            audioType="2";
        }
        indexmap.put("audioType",audioType);
        if("1".equals(audioType)){
            topBar.setTitle("宝宝爱听");
            soundTopBg.setBackgroundColor(Color.parseColor("#4DC9D8"));
        }else {
            topBar.setTitle("妈妈听听");
            soundTopBg.setBackgroundColor(Color.parseColor("#FFA3AF"));
        }
        soundIndexPresenter=new SoundIndexPresenter(mContext,this);
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentIndex = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBar.setOutlineProvider(null);
            collapsingToolbarLayout.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.M){
            FloatWindowManager.getInstance().applyOrShowFloatWindow(this);
        }else {
            if(!SettingsCompat.canDrawOverlays(mContext)){
                Toast.makeText(mContext,"需要打开悬浮窗权限",Toast.LENGTH_SHORT).show();
                SettingsCompat.manageDrawOverlays(mContext);
            }
        }


//        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if(verticalOffset!=verticalOffsetold){
//                    topBar.setVisibility(((appBarLayout.getHeight() + verticalOffset - collapsingToolbarLayout.getMinimumHeight()) <= 20) ? View.GONE : View.VISIBLE);
//                    float alpha = Math.min(((appBarLayout.getHeight() - collapsingToolbarLayout.getMinimumHeight()) - (appBarLayout.getHeight() + verticalOffset - collapsingToolbarLayout.getMinimumHeight())) * 1.0f / (appBarLayout.getHeight() - collapsingToolbarLayout.getMinimumHeight()), 1);
//                    ViewGroup.LayoutParams params=imgBackTmp.getLayoutParams();
//                    params.width= (int)TransformUtil.dp2px(mContext,(float)alpha*35);
//                    params.height=(int)TransformUtil.dp2px(mContext,(float)alpha*35);
//                    imgBackTmp.setLayoutParams(params);
//                    imgBackTmp.setAlpha(alpha);
//                    vp.setBackgroundColor(((appBarLayout.getHeight() + verticalOffset - collapsingToolbarLayout.getMinimumHeight()) ==0) ? Color.parseColor("#FFFFFFFF") : Color.parseColor("#00000000"));
//                    float alpha2 = Math.min(((appBarLayout.getHeight() + verticalOffset - collapsingToolbarLayout.getMinimumHeight())) * 1.0f / (appBarLayout.getHeight() - collapsingToolbarLayout.getMinimumHeight()), 1);
//                    topBar.setAlpha(alpha2);
//                }
//                verticalOffsetold=verticalOffset;
//
//            }
//        });
//        layoutRefresh.setOnRefreshLoadMoreListener(this);

        getData();
    }

    @Override
    public void getData() {
        super.getData();
        soundIndexPresenter.getSoundIndex(indexmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        FloatWindow.get().hide();
//        XmPlayerManager.getInstance(getApplicationContext()).pause();
//        String actionName = "com.ximalaya.ting.android.ACTION_CLOSE";
//        if (getPackageName().equals("com.ximalaya.ting.android")) {
//            actionName = "com.ximalaya.ting.android.ACTION_CLOSE_MAIN";
//        }
//        Intent intenclose = new Intent(actionName);
//        intenclose.setClass(getApplicationContext(), PlayerReceiver.class);
//        getApplicationContext().sendBroadcast(intenclose);
    }

    public void successGetSoundIndex(List<SoundIndex> soundIndices){
        List<String> titles = new ArrayList<>();
        for (int i = 0; i <soundIndices.size() ; i++) {
            titles.add(soundIndices.get(i).audioCategoryName);
            if(soundIndices.get(i).audioCategoryName.equals(choseTypeName)&&currentIndex==0){
                currentIndex=i;
            }
        }
        fragments.clear();
        for (int i = 0; i <titles.size() ; i++) {
            if(i==0){
                fragments.add(SoundHomeMonFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("audioType",audioType).puts("audioFrequencyCategoryId",soundIndices.get(i).audioFrequencyCategoryId)));
            }else {
                fragments.add(SoundTypeFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("audioCategoryName",soundIndices.get(i).audioCategoryName).puts("audioType",audioType).puts("audioFrequencyCategoryId",soundIndices.get(i).audioFrequencyCategoryId)));
            }
        }


        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        try {
            vp.setOffscreenPageLimit(fragments.size());
        } catch (Exception e) {
            vp.setOffscreenPageLimit(1);
            e.printStackTrace();
        }
        // bind to SmartTabLayout
        st.setViewPager(vp);
        vp.setCurrentItem(currentIndex);
    }

    int verticalOffsetold=0;
    private void initView() {
//        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        st = (SlidingTabLayout) findViewById(R.id.st);
        dividerStore = (View) findViewById(R.id.divider_store);
        vp = (ViewPager) findViewById(R.id.vp);
        topBar = (TopBar) findViewById(R.id.top_bar);
        imgBackTmp = (ImageView) findViewById(R.id.img_back_tmp);
        imgBackTmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        soundTopBg = (ImageView) findViewById(R.id.soundTopBg);
        seachTip = (ImageView) findViewById(R.id.seachTip);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        onRequestFinish();

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRequestFinish();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
//        layoutRefresh.finishRefresh();
//        layoutRefresh.finishLoadMore();
    }

    @Override
    public String getSurl() {
        String url;
        if(st.getCurrentTab()==0){
            String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
            url= String.format("%s?type=8&scheme=HMMMumAudio&index=0", urlPrefix);
        }else {
            String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
            url= String.format("%s?type=8&scheme=HMMMumAudio&index="+st.getCurrentTab(), urlPrefix);
        }

        return url;
    }

    @Override
    public String getSdes() {
        return "流行音乐、育儿知识、轻松段子，海量音频资源点这里~";
    }

    @Override
    public String getStitle() {
        return "憨妈妈-妈妈听听";
    }

    @Override
    public Bitmap getsBitmap() {
        return BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.index_share_humb_mmtt));
    }
}
