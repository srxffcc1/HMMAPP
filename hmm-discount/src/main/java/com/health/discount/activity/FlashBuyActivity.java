package com.health.discount.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.health.discount.R;
import com.health.discount.contract.FlashBuyContract;
import com.health.discount.fragment.FlashBuyFragment;
import com.health.discount.presenter.FlashBuyPresenter;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.FlashBuyTabPopoWindow;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.message.UpdateActTab;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.FlashBuyTab;
import com.healthy.library.model.PopListInfo;
import com.healthy.library.presenter.CardBoomPresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.ActivityCenterSlidingTabLayout;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = DiscountRoutes.DIS_FLASHBUY)//特惠闪购
public class FlashBuyActivity extends BaseActivity implements IsFitsSystemWindows, FlashBuyContract.View {

    private ActivityCenterSlidingTabLayout mTabLayout_3;
    private ConstraintLayout flashUPCon;
    private LinearLayout seachLL;
    private ViewPager tabViewPager;
    private LinearLayout Lin;
    private TextView changeTab;
    private TextView serarchKeyWord;
    private ImageView img_back, flashUP;
    private ImageView clearEdit;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private boolean isShow = false;
    private FlashBuyPresenter flashBuyPresenter;
    private FlashBuyTabPopoWindow flashBuyTabPopoWindow;

    int currentIndex = 0;
    private CanReplacePageAdapter fragmentPagerItemAdapter;
    private long mills = System.currentTimeMillis();

    private String appID;
    private String departID;
    List<FlashBuyTab> oldresult;
    private RelativeLayout tabLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        new CardBoomPresenter(mContext).boom("5");
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_flash_buy;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        flashBuyPresenter = new FlashBuyPresenter(this, this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        flashBuyPresenter.getYTBShopDetail(new SimpleHashMapBuilder<String, Object>().puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)));
    }

    @Override
    public void onSuccessGetTabList(final List<FlashBuyTab> result) {
        if (result == null || result.size() == 0) {
            System.out.println("活动为空了1");
            showEmpty();
            return;
        }
        this.oldresult = result;
        setTab(result);
    }
    Handler handlerSubmit = new Handler();
    Runnable runnableAct = new Runnable() {
        @Override
        public void run() {

            List<FlashBuyTab> result = new ArrayList<>();
            for (int i = 0; i < oldresult.size(); i++) {
                if (oldresult.get(i).isShow) {
                    result.add(oldresult.get(i));
                }
            }
            boolean isAllInit=true;
            for (int i = 0; i < oldresult.size(); i++) {
                if (!oldresult.get(i).isInit) {
                    isAllInit=false;
                }
            }
            if(isAllInit){

                setTab(result);
            }
        }
    };
    boolean needStop = false;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTabInfo(UpdateActTab updateActTab) {
        if (oldresult != null) {
            boolean isAllInit=true;
            for (int i = 0; i < oldresult.size(); i++) {
                if (!oldresult.get(i).isInit) {
                    isAllInit=false;
                }
            }
            if(isAllInit){
                return;
            }
            System.out.println("修改tab状态:"+updateActTab.id+":"+updateActTab.name+":"+updateActTab.flag);
            for (int i = 0; i < oldresult.size(); i++) {
                if(updateActTab.id.equals(oldresult.get(i).PopLabelID)){
                    oldresult.get(i).isInit=true;
                    oldresult.get(i).isShow=updateActTab.flag;
                    oldresult.get(i).popListInfos=updateActTab.popListInfos;
                }
            }
            if (handlerSubmit != null) {
                handlerSubmit.removeCallbacks(runnableAct);
            }
            handlerSubmit.postDelayed(runnableAct, 200);

        }
    }

    boolean isAllHide=false;

    private void setTab(final List<FlashBuyTab> result) {
        System.out.println("重新设置");
        mTitles.clear();
        mFragments.clear();
        if(result.size()==0){
            System.out.println("真的空了");
            showEmpty();
            return;
        }

        if (result != null && result.size() > 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isAllHide){
                        return;
                    }
                    tabLL.setVisibility(View.VISIBLE);
                }
            },3000);
        } else {
            isAllHide=true;
            tabLL.setVisibility(View.GONE);
        }
        for (int i = 0; i < result.size(); i++) {
            mTitles.add(result.get(i).PopLabelName);
        }
        for (int i = 0; i < result.size(); i++) {
            mFragments.add(FlashBuyFragment.newInstance(result.get(i).PopLabelID, appID, departID,result.get(i).PopLabelName,result.get(i).popListInfos));
        }


        System.out.println("活动中心需要展示的Tab:结束"+result.size());
        if(fragmentPagerItemAdapter==null){
            tabViewPager.removeAllViews();
        }else {//fragemnt不刷新啊 真的烦躁
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            List<Fragment> tmpFragments=fragmentManager.getFragments();
            for (int i = 0; i < tmpFragments.size(); i++) {
                if(tmpFragments.get(i) instanceof FlashBuyFragment){
                    FlashBuyFragment singlefragment= (FlashBuyFragment) tmpFragments.get(i);
                    if(!ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().addList(mTitles),singlefragment.popLabelName)){
                        transaction.remove(tmpFragments.get(i)).commitNowAllowingStateLoss();
                    }
                }else {

                    transaction.remove(tmpFragments.get(i)).commitNowAllowingStateLoss();
                }
            }
        }
        fragmentPagerItemAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), mFragments, mTitles);
        tabViewPager.setAdapter(fragmentPagerItemAdapter);
        fragmentPagerItemAdapter.notifyDataSetChanged();
        // adapter
        tabViewPager.setOffscreenPageLimit(mFragments.size());
        mTabLayout_3.setupWithViewPager(tabViewPager);
        if (result.size() > 4) {
            mTabLayout_3.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            mTabLayout_3.setTabMode(TabLayout.MODE_FIXED);
        }
        for (int i = 0; i < mTabLayout_3.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout_3.getTabAt(i);
            if (tab != null) {
                try {
                    tab.setCustomView(getTabView(i));
                    if (tab.getCustomView() != null) {
                        View tabView = (View) tab.getCustomView().getParent();
                        tabView.setTag(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        tabViewPager.setCurrentItem(currentIndex);
        mTabLayout_3.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "活动中心顶部tab点击量/滑屏切换量");
                MobclickAgent.onEvent(mContext, "event2APPActivityCenterTabClick", nokmap);
                currentIndex = tab.getPosition();
                TextView tv_tab = null;
                try {
                    tv_tab = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_title);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(tv_tab==null){
                    return;
                }
                tv_tab.getPaint().setFakeBoldText(true);//加粗
                tv_tab.setTextSize(16);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tv_tab = null;
                try {
                    tv_tab = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_title);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(tv_tab==null){
                    return;
                }
                tv_tab.getPaint().setFakeBoldText(false);
                tv_tab.setTextSize(16);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTabLayout_3.redrawIndicator(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout_3.resetTabParams();
    }

    @Override
    public void successAddShopCat(String result) {
        if (result.contains("购物车添加商品")) {
            showToast("已加入购物车");
        }
    }

    @Override
    public void onSuccessGetActList(List<PopListInfo> result) {

    }

    @Override
    public void onSuccessGetYTBShopDetail(ActVip.VipShop actVip) {
        if (actVip != null) {
            appID = actVip.ytbAppId;
            departID = actVip.ytbDepartID;
            flashBuyPresenter.getTabList(new SimpleHashMapBuilder<String, Object>().puts("appID", appID).puts("departID", departID));
        }
    }

    @Override
    public void onSuccessGetGoodsList(PopListInfo popListInfo, List<PopListInfo> result) {

    }


    @Override
    protected void findViews() {
        super.findViews();
        mTabLayout_3 = (ActivityCenterSlidingTabLayout) findViewById(R.id.tl_3);
        img_back = findViewById(R.id.img_back);
        flashUP = findViewById(R.id.flashUP);
        tabViewPager = findViewById(R.id.tabViewPager);
        flashUPCon = findViewById(R.id.flashUPCon);
        clearEdit = findViewById(R.id.clearEdit);
        changeTab = findViewById(R.id.changeTab);
        seachLL = findViewById(R.id.seachLL);
        serarchKeyWord = findViewById(R.id.serarchKeyWord);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        seachLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_SERVICESORTGOODS)
                        .withString("categoryId", 0 + "").withString("goodsTitle", "").navigation();
            }
        });
        clearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serarchKeyWord.setText("");
            }
        });
    }


    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_tab_title);
        tv.getPaint().setFakeBoldText(position == currentIndex ? true : false);//加粗
        tv.setText(mTitles.get(position));
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "活动中心页浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000);
        MobclickAgent.onEvent(mContext, "event_APP_ActivityCenter_Stop", nokmap);
    }

    private void initView() {
        tabLL = (RelativeLayout) findViewById(R.id.tabLL);
    }
}