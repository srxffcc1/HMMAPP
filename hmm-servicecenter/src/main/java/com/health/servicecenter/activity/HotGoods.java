package com.health.servicecenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.HotGoodsItemAdapter;
import com.health.servicecenter.adapter.MallGoodsItemAdapter;
import com.health.servicecenter.adapter.MallGoodsItemNoAdapter;
import com.health.servicecenter.contract.HotGoodsContract;
import com.health.servicecenter.presenter.HotGoodsPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.HotGoodsList;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.widget.AnimManager;
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

@Route(path = ServiceRoutes.SERVICE_HOTGOODS)
public class HotGoods extends BaseActivity implements IsFitsSystemWindows, HotGoodsContract.View, BaseAdapter.OnOutClickListener, OnRefreshLoadMoreListener {
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    private ImageView img_back;
    private RelativeLayout shop_cart_rel;
    private ImageView top_searchImg;
    private TextView tv_categoryName, txt_default, txt_sales_volume, txt_price, shop_cart_num;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerQuestion;
    private HotGoodsItemAdapter hotGoodsItemAdapter;

    private HotGoodsPresenter hotGoodsPresenter;
    private Map<String, Object> map = new HashMap<>();
    private int pageNum = 1;
    private MallGoodsItemNoAdapter mallGoodsItemNoAdapter;
    private ImageView topShopCatImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hot_goods;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        hotGoodsPresenter = new HotGoodsPresenter(this, this);
        // tv_categoryName.setText(categoryName);
        buildRecyclerView();
        showContent();
        getData();
        layoutRefresh.setOnRefreshLoadMoreListener(this);
    }

    @Override
    public void getData() {
        super.getData();
        map.put("position", 0);
        map.put("pageNum", pageNum+"");
        map.put("pageSize", "10");
        hotGoodsPresenter.getGoodsList(map);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hotGoodsPresenter.getShopCart();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerQuestion.setLayoutManager(virtualLayoutManager);
        recyclerQuestion.setAdapter(delegateAdapter);

        mallGoodsItemNoAdapter = new MallGoodsItemNoAdapter();
        delegateAdapter.addAdapter(mallGoodsItemNoAdapter);

        hotGoodsItemAdapter = new HotGoodsItemAdapter();
        delegateAdapter.addAdapter(hotGoodsItemAdapter);
        hotGoodsItemAdapter.setOutClickListener(this);
        hotGoodsItemAdapter.setOnBasketClickListener(new MallGoodsItemAdapter.OnBasketClickListener() {
            @Override
            public void onBasketClick(View view) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure","”爆款抢购“专题列表商品点击【加入购物车】");
                MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_AddShoppingCart",nokmap);
                AnimManager animManager = new AnimManager.Builder()
                        .with((Activity) mContext)
                        .startView(view)
                        .endView(topShopCatImg)
                        .imageUrl(R.drawable.basket_red)
                        .build();
                animManager.startAnim();
            }
        });

    }

    private void initView() {

        shop_cart_rel = (RelativeLayout) findViewById(R.id.shop_cart_rel);
        img_back = (ImageView) findViewById(R.id.img_back);
        top_searchImg = (ImageView) findViewById(R.id.top_searchImg);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
        tv_categoryName = findViewById(R.id.tv_categoryName);
        txt_default = findViewById(R.id.txt_default);
        txt_sales_volume = findViewById(R.id.txt_sales_volume);
        shop_cart_num = findViewById(R.id.shop_cart_num);
        txt_price = findViewById(R.id.txt_price);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        top_searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_SEACH)
                        .navigation();
            }
        });
        shop_cart_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_BASKET)
                        .navigation();
            }
        });
//        txt_default.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeType(0);
//            }
//        });
//        txt_sales_volume.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeType(1);
//            }
//        });
//        txt_price.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeType(2);
//            }
//        });
        topShopCatImg = (ImageView) findViewById(R.id.top_shopCatImg);
    }

    @Override
    public void onGetGoodsListSuccess(List<HotGoodsList> list) {

        if (list == null) {
            return;
        }

        if (list.size() == 0) {
            ////System.out.println("目前推荐的长度");
            layoutRefresh.finishLoadMoreWithNoMoreData();
            if (pageNum == 1) {
                mallGoodsItemNoAdapter.setModel("未查询到相关商品~");
            } else {

            }
        } else {
            mallGoodsItemNoAdapter.setModel(null);
            if (pageNum == 1) {
                hotGoodsItemAdapter.setData((ArrayList) list);
                ////System.out.println("目前推荐的长度设置数据");
            } else {
                hotGoodsItemAdapter.addDatas((ArrayList) list);
            }
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }

//        if (list == null || list.size() == 0) {
//            mallGoodsItemNoAdapter.setModel("未查询到相关商品~");
//            layoutRefresh.finishLoadMoreWithNoMoreData();
//            layoutRefresh.setNoMoreData(false);
//            layoutRefresh.setEnableLoadMore(true);
//            return;
//        }
//        hotGoodsItemAdapter.setData((ArrayList) list);
    }

    @Override
    public void successAddShopCat(String result) {
//        showToast(result);
        hotGoodsPresenter.getShopCart();
    }

    @Override
    public void onGetShopCartSuccess(ShopCartModel shopCartModel) {
        if (shopCartModel == null) {
            shop_cart_num.setText("0");
            return;
        }
        if (shopCartModel.total== 0) {
            shop_cart_num.setVisibility(View.INVISIBLE);
        } else if (shopCartModel.total > 99) {
            shop_cart_num.setVisibility(View.VISIBLE);
            shop_cart_num.setText("99+");
        } else {
            shop_cart_num.setVisibility(View.VISIBLE);
            shop_cart_num.setText(shopCartModel.total + "");
        }
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {

        if ("addShopCat".equals(function)) {
            HotGoodsList goodsListModel = (HotGoodsList) obj;
            if (goodsListModel != null) {
//                hotGoodsPresenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
//                        .puts("shopId", goodsListModel.getShopId())
//                        .puts("goodsShopId", goodsListModel.getShopId())
//                        .puts("goodsSource", "1")
//                        .puts("goodsType", goodsListModel.getGoodsType() + "")
//                        .puts("goodsId", goodsListModel.getId() + "")
//                        .puts("goodsSpecId", goodsListModel.getGoodsChildren().get(0).getId() + "")
//                        .puts("goodsQuantity", 1 + "")
//                );
            }

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
}