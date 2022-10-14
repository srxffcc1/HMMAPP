package com.health.second.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.second.R;
import com.health.second.adapter.SecondActGoodsAdapter;
import com.health.second.adapter.SecondActGoodsEmptyAdapter;
import com.health.second.adapter.SecondActGoodsExAdapter;
import com.health.second.adapter.SecondActTabAdapter;
import com.health.second.contract.SecondActGoodsListContract;
import com.health.second.presenter.SecondActGoodsListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.model.KKGroup;
import com.healthy.library.model.Kick;
import com.healthy.library.model.Kill;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.TabEntity;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = SecondRoutes.MAIN_ACTMODULE)
public class SecondActMainActivity extends BaseActivity implements
        OnRefreshLoadMoreListener, IsFitsSystemWindows,
        BaseAdapter.OnOutClickListener, SecondActGoodsListContract.View,
        IsNeedShare {

    @Autowired
    String merchantId;
    @Autowired
    String shopId;

    SecondActTabAdapter secondActTabAdapter;
    SecondActGoodsAdapter secondActGoodsAdapter;
    SecondActGoodsEmptyAdapter secondActGoodsEmptyAdapter;
    SecondActGoodsExAdapter secondActGoodsExAdapter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerview;
    private RelativeLayout seachTopLL;
    private LinearLayout seachTopBgLL;
    private LinearLayout seachTop;
    private com.healthy.library.widget.AutoClickImageView imgBack;
    private com.flyco.tablayout.CommonTabLayout topTab;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private com.healthy.library.widget.AutoClickImageView imgShare;
    private String[] titles = {"秒抢", "拼团", "砍价"};
    private int mCheckPosition = 0;
    int alldy = 0;
    SecondActGoodsListPresenter secondActGoodsListPresenter;
    int page = 1;
    int pageSize = 10;
    private LinearLayout actAlreadyLL;
    private android.widget.TextView actAlreadyText;
    private android.widget.TextView actAlreadyButton;
    private android.widget.ImageView actAlreadyImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_actmain;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.finishLoadMoreWithNoMoreData();
        secondActGoodsListPresenter = new SecondActGoodsListPresenter(this, this);

        if(TextUtils.isEmpty(merchantId)){
            merchantId = SpUtils.getValue(mContext,SpKey.CHOSE_MC);
        }

        if(TextUtils.isEmpty(shopId)){
            shopId = SpUtils.getValue(mContext,SpKey.CHOSE_SHOP);
        }

        buildRecyclerView();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        changeTab();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);

        secondActTabAdapter = new SecondActTabAdapter();
        secondActTabAdapter.setModel("");
        secondActTabAdapter.setOutClickListener(this);
        delegateAdapter.addAdapter(secondActTabAdapter);

        secondActGoodsEmptyAdapter = new SecondActGoodsEmptyAdapter();
        delegateAdapter.addAdapter(secondActGoodsEmptyAdapter);

        secondActGoodsAdapter = new SecondActGoodsAdapter();
        secondActGoodsAdapter.setOutClickListener(this);
        delegateAdapter.addAdapter(secondActGoodsAdapter);
        secondActGoodsExAdapter = new SecondActGoodsExAdapter();
        delegateAdapter.addAdapter(secondActGoodsExAdapter);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //newState分 0,1,2三个状态,2是滚动状态,0是停止
                super.onScrollStateChanged(recyclerView, newState);
                //-1代表顶部,返回true表示没到顶,还可以滑
                //1代表底部,返回true表示没到底部,还可以滑
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean b = recyclerView.canScrollVertically(-1);
                if (!b) {
                    alldy = 0;
                }
                alldy += dy;
                float alpha = Math.min((alldy * 1) * 1.0f / TransformUtil.dp2px(mContext, 250), 1);
                if (virtualLayoutManager.findFirstVisibleItemPosition() > 2) {
                    alpha = 1;
                }
                if (alpha > 0.5) {
                    topTab.setVisibility(View.VISIBLE);
                } else {
                    topTab.setVisibility(View.GONE);
                }

                seachTopBgLL.setAlpha(alpha);
            }
        });
        mTabEntities.clear();
        for (int i = 0; i < titles.length; i++) {
            //插入tab标签
            mTabEntities.add(new TabEntity(titles[i], 0, 0));
        }
        topTab.setTabData(mTabEntities);
        topTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mCheckPosition = position;
                secondActTabAdapter.setGroupChange(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1砍价 2拼团 3秒杀
                int marketingType = 0;
                if (mCheckPosition == 0) {
                    marketingType = 3;
                }
                if (mCheckPosition == 1) {
                    marketingType = 2;
                }
                if (mCheckPosition == 2) {
                    marketingType = 1;
                }
                showShare();
            }
        });
    }


    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        changeTab();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        changeTab();
    }

    private void initView() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        seachTopLL = (RelativeLayout) findViewById(R.id.seachTopLL);
        seachTopBgLL = (LinearLayout) findViewById(R.id.seachTopBgLL);
        seachTop = (LinearLayout) findViewById(R.id.seachTop);
        imgBack = (AutoClickImageView) findViewById(R.id.img_back);
        topTab = (CommonTabLayout) findViewById(R.id.topTab);
        imgShare = (AutoClickImageView) findViewById(R.id.img_share);
        actAlreadyLL = (LinearLayout) findViewById(R.id.actAlreadyLL);
        actAlreadyText = (TextView) findViewById(R.id.actAlreadyText);
        actAlreadyButton = (TextView) findViewById(R.id.actAlreadyButton);
        actAlreadyImg = (ImageView) findViewById(R.id.actAlreadyImg);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        switch (function) {
            case "tabChange":
                mCheckPosition = (int) obj;
                topTab.setCurrentTab(mCheckPosition);
                changeTab();
                break;

        }
    }

    private void changeTab() {
        secondActGoodsAdapter.stopAllCountDownTimer();
        topTab.setVisibility(View.GONE);
        seachTopBgLL.setAlpha(0);
        recyclerview.getLayoutManager().scrollToPosition(0);
        if (mCheckPosition == 0) {
            secondActGoodsListPresenter.getKillGoodsList(new SimpleHashMapBuilder<String, Object>()
                    .puts("pageNum", page)
                    .puts("pageSize", pageSize)
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("marketingType", "3")
                    .puts("merchantId", merchantId)
                    .puts("shopId", shopId)
                    .puts("YY", true)
                    .puts("goodsType", 1)
            );
        } else if (mCheckPosition == 1) {
            secondActGoodsListPresenter.getPinGoodsList(new SimpleHashMapBuilder<String, Object>()
                    .puts("addressProvince", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageSize", "10")
                    .puts("currentPage", page)
                    .puts("merchantId", merchantId)
                    .puts("shopId", shopId)
                    .puts("faceUrlNum", "3")//获取头像的数量
                    .puts("YY", true)
                    .puts("goodsType", 1)

            );
        } else {
            secondActGoodsListPresenter.getKickGoodsList(new SimpleHashMapBuilder<String, Object>()
                    .puts("currentPage", page)
                    .puts("pageSize", pageSize + "")
                    .puts("merchantId", merchantId)
                    .puts("shopId", shopId)
                    .puts("addressProvince", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("faceUrlNum", "3")//获取头像的数量
                    .puts("YY", true)
                    .puts("goodsType", 1)
            );
        }
        if (page == 1) {
            if (mCheckPosition == 0) {
                secondActGoodsListPresenter.getAlreadyKillGoodsList(new SimpleHashMapBuilder<String, Object>()
                        .puts("pageNum", page)
                        .puts("pageSize", pageSize)
                        .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                        .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                        .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                        .puts("marketingType", "3")
                        .puts("merchantId", merchantId)
                        .puts("shopId", shopId)
                        .puts("YY", true)
                        .puts("goodsType", 1)
                );
            } else if (mCheckPosition == 1) {
                secondActGoodsListPresenter.getAlreadyPinGoodsList(new SimpleHashMapBuilder<String, Object>()
                        .puts("addressProvince", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                        .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                        .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                        .puts("pageSize", "10")
                        .puts("currentPage", page)
                        .puts("merchantId", merchantId)
                        .puts("shopId", shopId)
                        .puts("faceUrlNum", "3")//获取头像的数量
                        .puts("YY", true)
                        .puts("goodsType", 1)

                );
            } else {
                secondActGoodsListPresenter.getAlreadyKickGoodsList(new SimpleHashMapBuilder<String, Object>()
                        .puts("currentPage", page)
                        .puts("pageSize", pageSize + "")
                        .puts("addressProvince", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                        .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                        .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                        .puts("faceUrlNum", "3")//获取头像的数量
                        .puts("merchantId", merchantId)
                        .puts("shopId", shopId)
                        .puts("YY", true)
                        .puts("goodsType", 1)
                );
            }
        }
    }

    private void showAdapterEmpty() {
        secondActGoodsAdapter.clear();
        secondActGoodsEmptyAdapter.setModel("null");
        secondActGoodsExAdapter.setModel(null);
    }

    @Override
    public void onGetKillGoodsSucess(List<Kill> result, PageInfoEarly pageInfoEarly) {//无分页
        secondActGoodsEmptyAdapter.setModel(null);
        secondActGoodsExAdapter.setModel(null);
        if (page == 1) {
            if (result == null || result.size() == 0) {
                showAdapterEmpty();
            } else {
                secondActGoodsAdapter.setData((ArrayList) result);
                if(result!=null&&result.size()<3){
                    secondActGoodsExAdapter.setModel("null");
                }
            }
        } else {
            if (result == null || result.size() == 0) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                secondActGoodsAdapter.addDatas((ArrayList) result);
                refreshLayout.finishLoadMore();
            }
        }
    }

    @Override
    public void onGetPinGoodsSucess(List<KKGroup> result, PageInfoEarly pageInfoEarly) {
        secondActGoodsEmptyAdapter.setModel(null);
        secondActGoodsExAdapter.setModel(null);
        if (page == 1 || page == 0) {
            if (ListUtil.isEmpty(result)) {
                showAdapterEmpty();
            } else {
                secondActGoodsAdapter.setData((ArrayList) result);
                if(result!=null&&result.size()<3){
                    secondActGoodsExAdapter.setModel("null");
                }
            }
        } else {
            secondActGoodsAdapter.addDatas((ArrayList) result);
        }
        if (pageInfoEarly.isMore != 1) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }

    @Override
    public void onGetKickGoodsSucess(List<Kick> result, PageInfoEarly pageInfoEarly) {
        secondActGoodsEmptyAdapter.setModel(null);
        secondActGoodsExAdapter.setModel(null);
        if (page == 1 || page == 0) {
            if (ListUtil.isEmpty(result)) {
                showAdapterEmpty();
            } else {
                secondActGoodsAdapter.setData((ArrayList) result);
                if(result!=null&&result.size()<3){
                    secondActGoodsExAdapter.setModel("null");
                }
            }
        } else {
            secondActGoodsAdapter.addDatas((ArrayList) result);
        }
        if (pageInfoEarly.isMore != 1) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }

    @Override
    public void onGetActsGoodsTitleSucess(final String result) {
        if (!TextUtils.isEmpty(result)) {
            if (result.contains("砍价")) {
                actAlreadyImg.setImageResource(R.drawable.second_already_leftimg2);
            } else {
                actAlreadyImg.setImageResource(R.drawable.second_already_leftimg);
            }
            actAlreadyLL.setVisibility(View.VISIBLE);
            actAlreadyText.setText(result);
            actAlreadyLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (result.contains("砍价")) {
                        ARouter.getInstance().build(DiscountRoutes.DIS_MYKICKACTIVITY).navigation();
                    } else {
                        ARouter.getInstance().build(DiscountRoutes.DIS_MYASSEMBLEACTIVITY).navigation();
                    }
                }
            });
        } else {
            actAlreadyLL.setVisibility(View.GONE);
        }
    }

    @Override
    public String getSurl() {
        Map<String, String> nokmap = new HashMap<String, String>();
        nokmap.put("soure", "聚惠团右上角的分享按钮点击量");
        MobclickAgent.onEvent(mContext, "event2APPShopDetiaShareClick", nokmap);

        String url = String.format(
                "%s?type=9&shopId=%s&merchantId=%s",
                SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl),
                shopId,
                merchantId
        );
        return url;
    }

    @Override
    public String getSdes() {
        return "同城品质生活，为您精挑惠选。";
    }

    @Override
    public String getStitle() {
        return "本地母婴钜惠来袭，爆款服务优惠多多多，快来买买买！";
    }

    @Override
    public Bitmap getsBitmap() {
        return null;
    }
}
