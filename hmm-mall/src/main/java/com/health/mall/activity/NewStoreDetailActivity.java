package com.health.mall.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.mall.R;
import com.health.mall.adapter.MallGoodsItemAdapter;
import com.health.mall.adapter.MallGoodsTitleAdapter;
import com.health.mall.adapter.NewStoreDetailPeopleAdapter;
import com.health.mall.adapter.NewStoreDetailQualificationsAdapter;
import com.health.mall.adapter.NewStoreDetailTopAdapter;
import com.health.mall.contract.NewStoreDetialContract;
import com.health.mall.model.PeopleListModel;
import com.health.mall.presenter.NewStoreDetialPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseMultiItemAdapter;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.RecommendList;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.business.CouponGoodsDialog;
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

//@Route(path = MallRoutes.MALL_STORE_DETAIL)
public class NewStoreDetailActivity extends BaseActivity implements IsFitsSystemWindows, IsNeedShare, OnRefreshLoadMoreListener,
        BaseAdapter.OnOutClickListener, NewStoreDetialContract.View {

    @Autowired
    String shopId;

    @Autowired
    String merchantId;

    private SmartRefreshLayout layoutRefresh;
    private ConstraintLayout topBarLayout;
    private ConstraintLayout translateTopBar;
    private ImageView storeBack;
    private ImageView storeShare;
    private ImageView img_back;
    private ImageView storeShare2;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerStore;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private NewStoreDetailTopAdapter newStoreDetailTopAdapter;
    private NewStoreDetailPeopleAdapter newStoreDetailPeopleAdapter;
    private NewStoreDetailQualificationsAdapter newStoreDetailQualificationsAdapter;
    private MallGoodsItemAdapter mallGoodsItemAdapter;
    private MallGoodsTitleAdapter mallGoodsTitleAdapter;
    private NewStoreDetialPresenter newStoreDetialPresenter;
    private Map<String, Object> map = new HashMap<>();
    private int pageNum = 1;
    private CouponGoodsDialog couponGoodsDialog;
    private String surl;
    private String sdes;
    private String stitle;
    private Bitmap sBitmap;
    private ShopDetailModel detialModel;
    private long mills = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_store_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if(TextUtils.isEmpty(shopId)){
            shopId=SpUtils.getValue(mContext,SpKey.CHOSE_SHOP);
        }
        if(TextUtils.isEmpty(merchantId)){
            merchantId=SpUtils.getValue(mContext,SpKey.CHOSE_MC);
        }
        newStoreDetialPresenter = new NewStoreDetialPresenter(this, this);
        showContent();
        buildRecyclerView();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        newStoreDetialPresenter.getStoreDetial(shopId);
        newStoreDetialPresenter.getPeopleList(new SimpleHashMapBuilder<String, Object>().puts("shopId", shopId)
                .puts("currentPage", "1").puts("pageSize", "10"));
        getList();
        newStoreDetialPresenter.getCouponList(new SimpleHashMapBuilder<String, Object>().puts("shopId", shopId)
                .puts("page", "1").puts("pageSize", "10").puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
    }

    private void getList() {
        map.put("pageNum", pageNum + "");
        map.put("pageSize", 10 + "");
        map.put("shopId", shopId);
        newStoreDetialPresenter.getGoodsList(map);
    }

    private void buildRecyclerView() {
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerStore.setLayoutManager(virtualLayoutManager);
        recyclerStore.setAdapter(delegateAdapter);

        newStoreDetailTopAdapter = new NewStoreDetailTopAdapter();
        delegateAdapter.addAdapter(newStoreDetailTopAdapter);
        newStoreDetailTopAdapter.setOutClickListener(this);

        newStoreDetailPeopleAdapter = new NewStoreDetailPeopleAdapter();
        delegateAdapter.addAdapter(newStoreDetailPeopleAdapter);
        newStoreDetailPeopleAdapter.setOutClickListener(this);
        newStoreDetailPeopleAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
            @Override
            public void outClick(@NotNull String function, @NotNull Object obj) {
                if ("people".equals(function)) {
                    ARouter.getInstance()
                            .build(MallRoutes.MALL_PEOPLELIST)
                            .withString("shopId", shopId)
                            .navigation();
                }
            }
        });

        newStoreDetailQualificationsAdapter = new NewStoreDetailQualificationsAdapter();
        delegateAdapter.addAdapter(newStoreDetailQualificationsAdapter);
        newStoreDetailQualificationsAdapter.setOutClickListener(this);

        mallGoodsTitleAdapter = new MallGoodsTitleAdapter();
        delegateAdapter.addAdapter(mallGoodsTitleAdapter);
        mallGoodsTitleAdapter.setOutClickListener(this);

        mallGoodsItemAdapter = new MallGoodsItemAdapter();
        delegateAdapter.addAdapter(mallGoodsItemAdapter);
        mallGoodsItemAdapter.setOutClickListener(this);
        mallGoodsItemAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
            @Override
            public void outClick(@NotNull String function, @NotNull Object obj) {
                if ("addShopCat".equals(function)) {
                    RecommendList goodsListModel = (RecommendList) obj;
                    if (goodsListModel != null) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "门店商品加入购物车按钮点击量");
                        MobclickAgent.onEvent(mContext, "event2APPShopDetialGoodsCartClick", nokmap);
                        newStoreDetialPresenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
                                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                                .puts("goodsShopId", goodsListModel.getShopId())
                                .puts("goodsSource", "1")
                                .puts("goodsType", goodsListModel.getGoodsType() + "")
                                .puts("goodsId", goodsListModel.getId() + "")
                                .puts("goodsSpecId", goodsListModel.skuId + "")
                                .puts("goodsQuantity", 1 + "")
                        );
                    }
                }
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        getList();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "门店详情页浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000);
        MobclickAgent.onEvent(mContext, "event_APP_ShopDetial_Stop", nokmap);
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("coupon".equals(function)) {
            if (couponGoodsDialog == null) {
                couponGoodsDialog = CouponGoodsDialog.newInstance();
            }
            couponGoodsDialog.show(((BaseActivity) mContext).getSupportFragmentManager(), "优惠券商品详情");
            couponGoodsDialog.setData(shopId, new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)), null, "2", merchantId,null);
        }
    }

    @Override
    public void onGetStoreDetialSuccess(ShopDetailModel storeDetialModel) {
        if (storeDetialModel != null) {
            detialModel = storeDetialModel;
            newStoreDetailTopAdapter.setModel(storeDetialModel);
            newStoreDetailQualificationsAdapter.setModel(storeDetialModel);
        } else {
            translateTopBar.setVisibility(View.GONE);
            topBarLayout.setVisibility(View.VISIBLE);
            showEmpty();
        }
    }

    @Override
    public void onGetPeopleListSuccess(List<PeopleListModel> result) {
        if (result != null && result.size() > 0) {
            List data = new ArrayList();
            data.add(result);
            newStoreDetailPeopleAdapter.setData(new ArrayList(data));
        }
    }

    @Override
    public void onGetGoodsListSuccess(List<RecommendList> list) {
        if (list != null && list.size() == 0) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            if (pageNum == 1) {
                if (list == null) {
                    return;
                }
                mallGoodsItemAdapter.setData((ArrayList<RecommendList>) list);
            } else {
                mallGoodsItemAdapter.addDatas((ArrayList<RecommendList>) list);
            }
            mallGoodsTitleAdapter.setModel("门店商品");
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void onGetCouponListSuccess(List<CouponInfoZ> list) {
        if (list != null && list.size() > 0) {
            if (newStoreDetailTopAdapter != null) {
                newStoreDetailTopAdapter.setCouponList(list);
            }
        }

    }

    @Override
    public void successAddShopCat(String result) {
        if (result.contains("购物车添加商品")) {
            showToast("已加入购物车");
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        topBarLayout = (ConstraintLayout) findViewById(R.id.topBarLayout);
        translateTopBar = (ConstraintLayout) findViewById(R.id.translateTopBar);
        storeBack = (ImageView) findViewById(R.id.storeBack);
        storeShare = (ImageView) findViewById(R.id.storeShare);
        img_back = (ImageView) findViewById(R.id.img_back);
        storeShare2 = (ImageView) findViewById(R.id.storeShare2);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerStore = (RecyclerView) findViewById(R.id.recyclerStore);
        storeShare2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        storeShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        storeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerStore.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstCompletelyVisibleItemPosition = virtualLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition > 0) {
                    topBarLayout.setVisibility(View.VISIBLE);
                    translateTopBar.setVisibility(View.GONE);
                } else {
                    topBarLayout.setVisibility(View.GONE);
                    translateTopBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public String getSurl() {
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "门店右上角的分享按钮点击量");
        MobclickAgent.onEvent(mContext, "event2APPShopDetiaShareClick", nokmap);
        String url = String.format("%s?type=7&id=%s&categoryNo=%s&cityNo=%s", SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl), shopId, null, LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        surl = url;
        return surl;
    }

    @Override
    public String getSdes() {
        return "分享给你一家我喜欢的商户，进来看看吧~";
    }

    @Override
    public String getStitle() {
        return detialModel != null ? detialModel.shopName : "";
    }

    @Override
    public Bitmap getsBitmap() {
        if (newStoreDetailTopAdapter != null) {
            return newStoreDetailTopAdapter.getsBitmap();
        } else {
            return sBitmap;
        }
    }
}