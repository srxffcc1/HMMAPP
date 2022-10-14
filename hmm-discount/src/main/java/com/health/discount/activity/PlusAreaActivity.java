package com.health.discount.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.discount.R;
import com.health.discount.adapter.PlusAreaActAdapter;
import com.health.discount.adapter.PlusAreaBannerAdapter;
import com.health.discount.contract.PlusAreaContract;
import com.health.discount.model.PlusAreaPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.presenter.CardBoomPresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = DiscountRoutes.DIS_PLUSAREA)
public class PlusAreaActivity extends BaseActivity implements OnRefreshLoadMoreListener, BaseAdapter.OnOutClickListener, PlusAreaContract.View {

    @Autowired
    String function="9112-0";
    @Autowired
    String blockName;

    private SmartRefreshLayout layoutRefresh;
    private TopBar topBar;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    private ConstraintLayout shopCartRel;
    private CornerImageView goSub;
    private TextView shopCartNum;
    private ImageView mallScrollUp;


    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private PlusAreaActAdapter specAreaActAdapter;
    private PlusAreaBannerAdapter plusAreaBannerAdapter;
    private Map<String, Object> map = new HashMap<>();
    private int pageNum = 1;
    private PlusAreaPresenter presenter;
    private long mills = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CardBoomPresenter(mContext).boom("6");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "专区列表页浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000 );
        MobclickAgent.onEvent(mContext, "event_APP_SpecialList_Stop", nokmap);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plus_area;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if(TextUtils.isEmpty(function)){
            function="9112-0";
            blockName="PLUS专区";
        }
        if ("9112-1".equals(function)) {
            topBar.setTitle(blockName);
        }
        if ("9112-0".equals(function)) {
            topBar.setTitle(blockName);
        }
        presenter = new PlusAreaPresenter(this, this);
        buildRecycle();
        layoutRefresh.setOnRefreshLoadMoreListener(this);
    }

    private void buildRecycle() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);
        layoutRefresh.setOnLoadMoreListener(this);

        plusAreaBannerAdapter = new PlusAreaBannerAdapter();
        delegateAdapter.addAdapter(plusAreaBannerAdapter);
        plusAreaBannerAdapter.setOutClickListener(this);

        specAreaActAdapter = new PlusAreaActAdapter();
        specAreaActAdapter.setFunction(function);
        delegateAdapter.addAdapter(specAreaActAdapter);
        specAreaActAdapter.setOutClickListener(this);
//        specAreaActAdapter.setOnBasketClickListener(new PlusAreaActAdapter.OnBasketClickListener() {
//            @Override
//            public void onBasketClick(View view) {
//                AnimManager animManager = new AnimManager.Builder()
//                        .with((Activity) mContext)
//                        .startView(view)
//                        .endView(shopCartRel)
//                        .imageUrl(R.drawable.basket_red)
//                        .build();
//                animManager.startAnim();
//            }
//        });
        getData();
        if ("9112-1".equals(function)) {
            plusAreaBannerAdapter.setModel("1");
        }
        if ("9112-0".equals(function)) {
            plusAreaBannerAdapter.setModel("0");
        }
    }

    @Override
    public void getData() {
        super.getData();
        if ("9112-1".equals(function)) {
            presenter.getGoodsPlusOnlyList(new SimpleHashMapBuilder<String, Object>()
                    .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
                    .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                    .puts("pageSize", 10 + "")
                    .puts("pageNum", pageNum + ""));
        }
        if ("9112-0".equals(function)) {
            presenter.getGoodsPlusList(new SimpleHashMapBuilder<String, Object>()
                    .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
                    .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                    .puts("pageSize", 10 + "")
                    .puts("pageNum", pageNum + ""));

        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("basket".equals(function)) {//加入购物车
            GoodsDetail goodsDetail = (GoodsDetail) obj;
            if (goodsDetail != null) {
                presenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
                        .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                        .puts("goodsShopId", goodsDetail.getShopId())
                        .puts("goodsSource", "1")
                        .puts("goodsType", goodsDetail.goodsType + "")
                        .puts("goodsId", goodsDetail.id + "")
                        .puts("goodsSpecId", goodsDetail.goodsChildren != null ? goodsDetail.goodsChildren.get(0).getId(goodsDetail.marketingType) : null)
                        .puts("goodsQuantity", 1 + "")
                );
            }
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        shopCartRel = (ConstraintLayout) findViewById(R.id.shop_cart_rel);
        goSub = (CornerImageView) findViewById(R.id.goSub);
        shopCartNum = (TextView) findViewById(R.id.shop_cart_num);
        mallScrollUp = (ImageView) findViewById(R.id.mall_scrollUp);
        shopCartRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "event2APPSpecialListShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "专题列表页-购物车入口点击量"));
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_BASKET)
                        .navigation();
            }
        });
    }


    @Override
    public void onSucessPlusGetList(List<GoodsDetail> result, PageInfoEarly pageInfoEarly) {
        if (result == null) {
            showEmpty();
            return;
        }
        if (result.size() == 0) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
            if (pageNum == 1) {
                showEmpty();
                specAreaActAdapter.setModel(null);
            }
        } else {
            if (pageNum == 1) {
                specAreaActAdapter.setData((ArrayList<GoodsDetail>) result);
            } else {
                specAreaActAdapter.addDatas((ArrayList<GoodsDetail>) result);
            }
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void onSucessPlusOnlyGetList(List<GoodsDetail> result, PageInfoEarly pageInfoEarly) {
        if (result == null) {
            showEmpty();
            return;
        }
        if (result.size() == 0) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
            if (pageNum == 1) {
                showEmpty();
                specAreaActAdapter.setModel(null);
            }
        } else {
            if (pageNum == 1) {
                specAreaActAdapter.setData((ArrayList<GoodsDetail>) result);
            } else {
                specAreaActAdapter.addDatas((ArrayList<GoodsDetail>) result);
            }
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void successAddShopCat(String result) {
        if (result.contains("购物车添加商品")) {
            showToast("已加入购物车");
            presenter.getShopCart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getShopCart();
    }

    @Override
    public void onGetShopCartSuccess(ShopCartModel shopCartModel) {
        if (shopCartModel == null) {
            shopCartNum.setText("0");
            return;
        }
        if (shopCartModel.total == 0) {
            shopCartNum.setVisibility(View.INVISIBLE);
        } else if (shopCartModel.total > 99) {
            shopCartNum.setVisibility(View.VISIBLE);
            shopCartNum.setText("99+");
        } else {
            shopCartNum.setVisibility(View.VISIBLE);
            shopCartNum.setText(shopCartModel.total + "");
        }
    }
}