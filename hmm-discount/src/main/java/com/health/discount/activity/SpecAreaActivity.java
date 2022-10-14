package com.health.discount.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.health.discount.adapter.SpecAreaActAdapter;
import com.health.discount.adapter.SpecAreaBannerAdapter;
import com.health.discount.contract.SpecAreaContract;
import com.health.discount.model.SpecAreaPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.MainBlockDetailModel;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.presenter.CardBoomPresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AnimManager;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.StatusLayout;
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

@Route(path = DiscountRoutes.DIS_SPECAREA)
public class SpecAreaActivity extends BaseActivity implements OnRefreshLoadMoreListener, BaseAdapter.OnOutClickListener,
        IsFitsSystemWindows, SpecAreaContract.View {

    @Autowired
    String blockId;
    @Autowired
    String blockName;

    private SmartRefreshLayout layoutRefresh;
    private ConstraintLayout topView;
    private ConstraintLayout shop_cart_rel;
    private View viewHeaderBg;
    private ImageView imgBack;
    private LinearLayout seachLL;
    private TextView serarchKeyWord;
    private ImageView clearEdit;
    private TextView tvCategoryName, title;
    private ImageView shareImg;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private SpecAreaBannerAdapter specAreaBannerAdapter;
    private SpecAreaActAdapter specAreaActAdapter;
    private Map<String, Object> map = new HashMap<>();
    private int pageNum = 1;
    private SpecAreaPresenter presenter;

    private ConstraintLayout shopCartRel;
    private CornerImageView goSub;
    private TextView shopCartNum;
    private ImageView mallScrollUp;
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
        return R.layout.activity_spec_area;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if (!TextUtils.isEmpty(blockName)) {
            title.setText(blockName);
        }
        presenter = new SpecAreaPresenter(this, this);
        buildRecycle();
        layoutRefresh.setOnRefreshLoadMoreListener(this);
    }

    private void buildRecycle() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);
        layoutRefresh.setOnLoadMoreListener(this);

        specAreaBannerAdapter = new SpecAreaBannerAdapter();
        delegateAdapter.addAdapter(specAreaBannerAdapter);
        specAreaBannerAdapter.setOutClickListener(this);
        specAreaBannerAdapter.setModel(null);

        specAreaActAdapter = new SpecAreaActAdapter();
        delegateAdapter.addAdapter(specAreaActAdapter);
        specAreaActAdapter.setOutClickListener(this);
        specAreaActAdapter.setOnBasketClickListener(new SpecAreaActAdapter.OnBasketClickListener() {
            @Override
            public void onBasketClick(View view) {
                AnimManager animManager = new AnimManager.Builder()
                        .with((Activity) mContext)
                        .startView(view)
                        .endView(shop_cart_rel)
                        .imageUrl(R.drawable.basket_red)
                        .build();
                animManager.startAnim();
            }
        });
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", "12");
        map.put("themeId", blockId);
        presenter.getThemeDetial(new SimpleHashMapBuilder<String, Object>().puts("themeId", blockId));
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
        topView = (ConstraintLayout) findViewById(R.id.topView);
        shop_cart_rel = (ConstraintLayout) findViewById(R.id.shop_cart_rel);
        viewHeaderBg = (View) findViewById(R.id.view_header_bg);
        imgBack = (ImageView) findViewById(R.id.img_back);
        seachLL = (LinearLayout) findViewById(R.id.seachLL);
        serarchKeyWord = (TextView) findViewById(R.id.serarchKeyWord);
        clearEdit = (ImageView) findViewById(R.id.clearEdit);
        tvCategoryName = (TextView) findViewById(R.id.tv_categoryName);
        shareImg = (ImageView) findViewById(R.id.shareImg);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        title = (TextView) findViewById(R.id.title);
        shopCartRel = (ConstraintLayout) findViewById(R.id.shop_cart_rel);
        goSub = (CornerImageView) findViewById(R.id.goSub);
        shopCartNum = (TextView) findViewById(R.id.shop_cart_num);
        mallScrollUp = (ImageView) findViewById(R.id.mall_scrollUp);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shopCartRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "event2APPSpecialListShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "专题列表页-购物车入口点击量"));
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_BASKET)
                        .navigation();
            }
        });
        serarchKeyWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "专区搜索框点击量");
                MobclickAgent.onEvent(mContext, "event2APPSpecialSeachClick", nokmap);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_SERVICESORTGOODS)
                        .withString("categoryId", 0 + "").withString("goodsTitle", "").navigation();
            }
        });
    }

    @Override
    public void onSucessGetList(List<MainBlockDetailModel> result, PageInfoEarly pageInfoEarly) {
        if (pageNum == 1 || pageNum == 0) {
            specAreaActAdapter.setData((ArrayList) result);
            if (result.size() == 0) {
                showEmpty();
            }
        } else {
            specAreaActAdapter.addDatas((ArrayList) result);
        }
        if (pageInfoEarly.nextPage == 0) {
            //System.out.println("没有更多了");
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
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
    public void successThemeDetial(MainBlockModel mainBlockModel) {
        if (mainBlockModel != null) {
            if (mainBlockModel.themeTag!=null&&mainBlockModel.themeTag.startsWith("http")) {
                title.setText((TextUtils.isEmpty(mainBlockModel.themeName) ? "专区活动" : mainBlockModel.themeName));
            } else {
                title.setText(TextUtils.isEmpty(mainBlockModel.themeTag) ? (TextUtils.isEmpty(mainBlockModel.themeName) ? "专区活动" : mainBlockModel.themeName) : mainBlockModel.themeTag);
            }
            if (mainBlockModel.themeImage != null && !TextUtils.isEmpty(mainBlockModel.themeImage) && !"null".equals(mainBlockModel.themeImage)) {
                specAreaBannerAdapter.setModel(mainBlockModel);
            }
            presenter.getGoodsList(map);
        } else {
            showEmpty();
        }

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