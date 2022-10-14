package com.health.second.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.second.R;
import com.health.second.adapter.SecondMainDistanceAdapter;
import com.health.second.adapter.SecondMainEmptyAdapter;
import com.health.second.adapter.SecondMainFuncAdapter;
import com.health.second.adapter.SecondMainFuncTmpAdapter;
import com.health.second.adapter.SecondMainGoodsAdapter;
import com.health.second.adapter.SecondMainLogosAdapter;
import com.health.second.adapter.SecondMainStoreAdapter;
import com.health.second.contract.SecondGoodsSortContract;
import com.health.second.contract.SecondMainContract;
import com.health.second.contract.SecondTypeContract;
import com.health.second.model.SecondAct;
import com.healthy.library.model.SecondSortGoods;
import com.health.second.model.SecondSortShop;
import com.health.second.model.SecondType;
import com.health.second.presenter.SecondGoodsSortPresenter;
import com.health.second.presenter.SecondLocPresenter;
import com.health.second.presenter.SecondMainPresenter;
import com.health.second.presenter.SecondTypePresenter;
import com.healthy.library.LibApplication;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.LocVipContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.LocVip;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ProvinceCityModel;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = SecondRoutes.MAIN_MODULE)
public class SecondMainActivity extends BaseActivity implements SecondGoodsSortContract.View, SecondMainContract.View, BaseAdapter.OnOutClickListener, OnRefreshLoadMoreListener, IsFitsSystemWindows, SecondTypeContract.View, LocVipContract.View {
    SecondMainDistanceAdapter secondMainDistanceAdapter;
    SecondMainFuncAdapter secondMainFuncAdapter;
    SecondMainFuncTmpAdapter secondMainFuncTmpAdapter;
    SecondMainGoodsAdapter secondMainGoodsAdapter;
    SecondMainLogosAdapter secondMainLogosAdapter;
    SecondMainStoreAdapter secondMainStoreAdapter;
    SecondMainEmptyAdapter secondMainEmptyAdapter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    private com.scwang.smart.refresh.layout.SmartRefreshLayout refreshLayout;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private androidx.recyclerview.widget.RecyclerView recyclerview;
    private android.widget.RelativeLayout seachTopLL;
    private android.widget.LinearLayout seachTopBgLL;
    private android.widget.LinearLayout seachTop;
    private com.healthy.library.widget.ImageTextView disLoc;
    private android.widget.LinearLayout serarchKeyWordLL;
    private android.widget.TextView serarchKeyWord;
    private android.widget.LinearLayout typeLL;
    private com.flyco.tablayout.SlidingTabLayout tabType;

    SecondTypePresenter secondTypePresenter;
    SecondLocPresenter secondLocPresenter;
    SecondMainPresenter secondMainPresenter;
    SecondGoodsSortPresenter secondGoodsSortPresenter;
    int alldy = 0;
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    public String nowTab = "商家";
    public int page = 1;
    public int pageSize = 10;
    private List<String> distancetitles = Arrays.asList("商家", "商品");


    //----------------------------------筛选用到的字段-------------------------------

    public String goodsTitle;

    //----------------------------------筛选用到的字段-------------------------------
    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.finishLoadMoreWithNoMoreData();
        buildRecyclerView();
        secondTypePresenter = new SecondTypePresenter(this, this);
        secondLocPresenter = new SecondLocPresenter(this, this);
        secondMainPresenter = new SecondMainPresenter(this, this);
        secondGoodsSortPresenter = new SecondGoodsSortPresenter(this, this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        secondLocPresenter.getLocVip(new SimpleHashMapBuilder<String, Object>());
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerview.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 0);
        secondMainFuncAdapter = new SecondMainFuncAdapter();
        secondMainFuncAdapter.setModel("");
        delegateAdapter.addAdapter(secondMainFuncAdapter);
        secondMainFuncAdapter.setOutClickListener(this);

        secondMainFuncTmpAdapter = new SecondMainFuncTmpAdapter();
        secondMainFuncTmpAdapter.setModel("");
        delegateAdapter.addAdapter(secondMainFuncTmpAdapter);

        secondMainLogosAdapter = new SecondMainLogosAdapter();
        secondMainLogosAdapter.setModel("");
        delegateAdapter.addAdapter(secondMainLogosAdapter);
        secondMainLogosAdapter.setOutClickListener(this);

        secondMainFuncTmpAdapter = new SecondMainFuncTmpAdapter();
        secondMainFuncTmpAdapter.setModel("");
        delegateAdapter.addAdapter(secondMainFuncTmpAdapter);

        secondMainDistanceAdapter = new SecondMainDistanceAdapter(this);
        secondMainDistanceAdapter.setModel("");
        secondMainDistanceAdapter.setAreasCode(LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        secondMainDistanceAdapter.setAreaString(LocUtil.getAreaName(mContext, SpKey.LOC_CHOSE));
        delegateAdapter.addAdapter(secondMainDistanceAdapter);
        secondMainDistanceAdapter.setOutClickListener(this);

        secondMainFuncTmpAdapter = new SecondMainFuncTmpAdapter();
        secondMainFuncTmpAdapter.setModel("");
        delegateAdapter.addAdapter(secondMainFuncTmpAdapter);

        secondMainEmptyAdapter = new SecondMainEmptyAdapter();
        delegateAdapter.addAdapter(secondMainEmptyAdapter);


        secondMainGoodsAdapter = new SecondMainGoodsAdapter();
        delegateAdapter.addAdapter(secondMainGoodsAdapter);
        secondMainGoodsAdapter.setOutClickListener(this);


        secondMainStoreAdapter = new SecondMainStoreAdapter();
        delegateAdapter.addAdapter(secondMainStoreAdapter);
        serarchKeyWordLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SecondRoutes.MAIN_SEACH)
                        .navigation();
            }
        });

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
                float alpha = Math.min((alldy * 1) * 1.0f / TransformUtil.dp2px(mContext, 95), 1);
                if (alpha > 0.3f) {
                    disLoc.setDrawable(R.drawable.ic_back_withboder, mContext);
                    disLoc.setTextColor(Color.parseColor("#333333"));
                } else {
                    disLoc.setDrawable(R.drawable.ic_back_withboder_white, mContext);
                    disLoc.setTextColor(Color.parseColor("#FFFFFF"));
                }
                if (virtualLayoutManager.findFirstCompletelyVisibleItemPosition() >= 1) {
                    getTopHeight();
                }
//                if (virtualLayoutManager.findFirstCompletelyVisibleItemPosition() >= 2) {
//                    typeLL.setVisibility(View.GONE);
//                }else {
//                    typeLL.setVisibility(View.GONE);
//                }
//                if (virtualLayoutManager.findFirstVisibleItemPosition() > 2) {
//                    alpha = 1;
//                    typeLL.setVisibility(View.GONE);
//                }
                if (secondMainDistanceAdapter.stickyLayoutHelper != null) {
                    secondMainDistanceAdapter.checkSticky();
//                    System.out.println("当前是不是吸顶了:"+secondMainDistanceAdapter.stickyLayoutHelper.isStickyNow());
                }
                seachTopBgLL.setAlpha(alpha);
            }
        });
        disLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refreshLayout.finishLoadMoreWithNoMoreData();
    }

    public void getTopHeight() {
        final ViewTreeObserver viewTreeObserver = seachTopLL.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                seachTopLL.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int height = seachTopLL.getHeight();
                if (secondMainDistanceAdapter != null) {
                    secondMainDistanceAdapter.setTopheight(height);
                }


            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        seachTopLL = (RelativeLayout) findViewById(R.id.seachTopLL);
        seachTopBgLL = (LinearLayout) findViewById(R.id.seachTopBgLL);
        seachTop = (LinearLayout) findViewById(R.id.seachTop);
        disLoc = (ImageTextView) findViewById(R.id.dis_loc);
        serarchKeyWordLL = (LinearLayout) findViewById(R.id.serarchKeyWordLL);
        serarchKeyWord = (TextView) findViewById(R.id.serarchKeyWord);
        typeLL = (LinearLayout) findViewById(R.id.seachTopTmp);
        tabType = (SlidingTabLayout) findViewById(R.id.tabType);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        changeTab(false,nowTab);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page=1;
        getData();
    }

    @Override
    public void onGetFirstTypeMenuSucess(List<SecondType> list) {

    }

    @Override
    public void onGetRecommendTypeMenuSucess(List<SecondType.SecondTypeMenu> list) {
        if(list!=null&&list.size()>0){
            list.add(new SecondType.SecondTypeMenu("全部"));
        }
        secondMainFuncAdapter.setMenus(list);
        secondMainFuncAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetTypeMenuSucess(final List<SecondType.SecondTypeMenu> list) {
        if (list != null && list.size() > 0) {
            titles.clear();
            titles.addAll(new SimpleArrayListBuilder<String>().putList(list, new ObjectIteraor<SecondType.SecondTypeMenu>() {
                @Override
                public String getDesObj(SecondType.SecondTypeMenu o) {
                    return o.name;
                }
            }));
            tabType.setViewPager(null, titles);
            tabType.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    outClick("请求底部数据", null);
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
        }


    }

    @Override
    public void onGetTypeMenuBindTypeSucess(List<SecondType.SecondTypeMenu> list, SecondType secondType) {

    }

    @Override
    public void onGetLocationListSuccess(List<ProvinceCityModel> provinceCityModels) {
        secondMainDistanceAdapter.setProvinceCityModels(provinceCityModels);
    }

    public void changeTab(boolean needTabTop, String index) {
        if ("商家".equals(nowTab) && 0 == page && secondMainGoodsAdapter.getDatas().size() > 0) {//就不重复请求了 如果是1
            return;
        }
        if ("商品".equals(nowTab) && 0 == page && secondMainGoodsAdapter.getDatas().size() > 0) {//就不重复请求了 如果是1
            return;
        }
        if (page == 0) {
            page = 1;
        }
        nowTab = index;
        if (page == 1 || page == 0) {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
        if ("商品".equals(nowTab)) {//请求新闻
            secondGoodsSortPresenter.getActSortGoods(new SimpleHashMapBuilder<String, Object>()
                    .puts("publish", 1)
                    .puts("pageNum", page)
                    .puts("pageSize", pageSize)
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("appSalesSort", secondMainDistanceAdapter.appSalesSort)
                    .puts("platformPriceSort", secondMainDistanceAdapter.platformPriceSort)
                    .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
            );
        } else {
            secondGoodsSortPresenter.getActSortShops(new SimpleHashMapBuilder<String, Object>()
                    .puts("publish", 1)
                    .puts("level", 3)
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", secondMainDistanceAdapter.areasCode)
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("distanceSort", secondMainDistanceAdapter.distanceSort)
                    .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))


            );
        }
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onSucessGetLocVip(LocVip locVip) {
        if (TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_PARTNERID))) {//考虑下老的APP进来没重新选择门店 无法触发合伙人保存到SP 需要手动查找到正确的
            if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))) {
                List<LocVip.Local.MerchantsShop> list = locVip.getAllMerchantShopWithMe();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).shopId.equals(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))) {
                        SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_PARTNERID, list.get(i).partnerId);
                    }
                }
            }
        }
        secondTypePresenter.getLocationList(LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        secondMainPresenter.getActGoods(new SimpleHashMapBuilder<String, Object>()
                .puts("partnerId", SpUtils.getValue(mContext, SpKey.CHOSE_PARTNERID))
                .puts("pageNum", 1)
                .puts("pageSize", 6)

        );
        changeTab(false, nowTab);
        secondMainPresenter.getHotShopLogos(new SimpleHashMapBuilder<String, Object>()
                .puts("partnerId", SpUtils.getValue(mContext, SpKey.CHOSE_PARTNERID)));
        secondTypePresenter.getRecommendTypeMenu(new SimpleHashMapBuilder<String, Object>()
                .puts("partnerId", SpUtils.getValue(mContext, SpKey.CHOSE_PARTNERID)));
        secondTypePresenter.getTypeMenu(new SimpleHashMapBuilder<String, Object>()
                .puts("partnerId", SpUtils.getValue(mContext, SpKey.CHOSE_PARTNERID)));
    }

    @Override
    public void outClick(String function, Object obj) {
        System.out.println("触发了:" + function);
        if ("综合活动".equals(function)) {
            ARouter.getInstance()
                    .build(SecondRoutes.MAIN_ACTMODULE)
                    .navigation();
        }
        if ("请求底部数据".equals(function)) {
            page = 1;
            nowTab = distancetitles.get(secondMainDistanceAdapter.tabType);
            changeTab(true, nowTab);

        }
        if ("滑动到吸顶".equals(function)) {
            smoothMoveToPosition(recyclerview, 4);
        }
        if ("服务分类".equals(function)) {
            SecondType.SecondTypeMenu secondTypeMenu= (SecondType.SecondTypeMenu) obj;
            if(secondTypeMenu.goodsCategoryIdList==null){
                ARouter.getInstance()
                        .build(SecondRoutes.MAIN_ALLTYPE)
                        .navigation();
            }else {
                ARouter.getInstance()
                        .build(SecondRoutes.MAIN_SEACH)
                        .withString("categoryIds",secondTypeMenu.goodsCategoryIdList)
                        .navigation();
            }

        }
        if ("加入购物车".equals(function)) {
            SecondSortGoods goodsDetail = (SecondSortGoods) obj;
            if (goodsDetail != null) {
                secondGoodsSortPresenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
                .puts("shopId", SpUtils.getValue(LibApplication.getAppContext(),SpKey.CHOSE_SHOP))
                .puts("goodsShopId", goodsDetail.getExShopId())
                .puts("goodsSource", "1")
                .puts("goodsType", goodsDetail.goodsType)
                .puts("goodsId", goodsDetail.id)
                .puts("goodsSpecId", goodsDetail.goodsChildren != null ? goodsDetail.goodsChildren.get(0).id : null)
                .puts("goodsQuantity", "1")
                );
            }
        }
    }


    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    @Override
    public void onGetHotShopLogosSucess(List<String> list) {
        secondMainLogosAdapter.setLogos(list);
        secondMainLogosAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActGoodsSucess(List<SecondAct> list) {
        secondMainLogosAdapter.setSecondActs(list);
        secondMainLogosAdapter.notifyDataSetChanged();
    }

    private void showAdpaterEmpty() {
        secondMainEmptyAdapter.setModel("null");
        clearAllUnderAdpaterEmpty();
    }

    public boolean isTabClick = false;//

    private void clearAllUnderAdpaterEmpty() {
        secondMainGoodsAdapter.clear();
        secondMainStoreAdapter.clear();
        if (isTabClick) {
            virtualLayoutManager.scrollToPositionWithOffset(delegateAdapter.getItemCount() - 1, 0);
            isTabClick = false;
        }
    }

    @Override
    public void onGetActSortGoodsSuccess(List<SecondSortGoods> result, PageInfoEarly pageInfo) {
        secondMainEmptyAdapter.setModel(null);
        if (result == null || result.size() == 0) {
            refreshLayout.finishLoadMoreWithNoMoreData();
            if (page == 1 || page == 0) {
                showAdpaterEmpty();
            }
        } else {
            if (page == 1) {
                clearAllUnderAdpaterEmpty();
                ArrayList<SecondSortGoods> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                secondMainGoodsAdapter.setData(tmp);
            } else {
                ArrayList<SecondSortGoods> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                secondMainGoodsAdapter.addDatas(tmp);

            }
            if (pageInfo.nextPage == 0) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                refreshLayout.setNoMoreData(false);
                refreshLayout.setEnableLoadMore(true);
            }
        }
    }

    @Override
    public void onGetActSortShopsSuccess(List<SecondSortShop> result, PageInfoEarly pageInfo) {
        secondMainEmptyAdapter.setModel(null);
        if (result == null || result.size() == 0) {
            refreshLayout.finishLoadMoreWithNoMoreData();
            if (page == 1 || page == 0) {
                showAdpaterEmpty();
            }
        } else {
                clearAllUnderAdpaterEmpty();
                ArrayList<SecondSortShop> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                secondMainStoreAdapter.setData(tmp);
                refreshLayout.finishLoadMoreWithNoMoreData();

        }
    }

    @Override
    public void onGetActSortGoodDetailSucess(GoodsDetail result) {

    }

    @Override
    public void addShopCatSuccess(String result) {
        showToast("已加入购物车");

    }
}
