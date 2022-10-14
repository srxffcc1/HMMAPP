package com.health.servicecenter.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.arouter.utils.TextUtils;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallGoodsItemAdapter;
import com.health.servicecenter.adapter.MallGoodsItemHTransverseAdapter;
import com.health.servicecenter.adapter.MallGoodsItemNoAdapter;
import com.health.servicecenter.adapter.MallGoodsTitleAdapter;
import com.health.servicecenter.contract.ServiceSortGoodsContract;
import com.health.servicecenter.presenter.ServiceSortGoodsPresenter;
import com.health.servicecenter.utils.SortType;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.OrderListPageInfo;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.model.SortGoodsListModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.SpUtils;
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

@Route(path = ServiceRoutes.SERVICE_SERVICESORTGOODS)
public class ServiceSortGoods extends BaseActivity implements IsFitsSystemWindows, ServiceSortGoodsContract.View, BaseAdapter.OnOutClickListener, OnRefreshLoadMoreListener,
        TextView.OnEditorActionListener, View.OnClickListener {
    @Autowired
    String categoryId;
    @Autowired
    String categoryName;
    @Autowired
    String goodsTitle;

    boolean isSeachType = false;

    String textSeachTag = "";


    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    private ImageView img_back, listImg;
    private ConstraintLayout shop_cart_rel;
    private ImageView top_searchImg, price_up_img, price_down_img;
    private TextView tv_categoryName, txt_default, txt_sales_volume, txt_price, shop_cart_num;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerQuestion;
    private RecyclerView recyclerNoData;
    private MallGoodsItemHTransverseAdapter mallGoodsItemHTransverseAdapter;
    private MallGoodsItemAdapter mallGoodsItemAdapter;
    private MallGoodsTitleAdapter mallGoodsTitleAdapter;

    private ServiceSortGoodsPresenter serviceSortGoodsPresenter;
    private Map<String, Object> map = new HashMap<>();
    private Map<String, Object> bottomMap = new HashMap<>();//下方推荐的map
    private int pageNum = 1;
    private int dataType = 0;//0表示默认，1表示按销量升序，2表示按销量降序 3表示按价格升序，4表示按价格降序
    private LinearLayout seachLL;
    private EditText serarchKeyWord;
    private ImageView clearEdit, mall_scrollUp;
    private MallGoodsItemNoAdapter mallGoodsItemNoAdapter;
    private ImageView topSearchImg;
    private ImageView topShopCatImg;
    private LinearLayout sort_liner;
    private boolean isNull = false;//商品是否有数据
    private boolean isList = false;//是否是竖排列表
    private StaggeredGridLayoutManager manager;

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
        return R.layout.activity_service_sort_goods;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        serviceSortGoodsPresenter = new ServiceSortGoodsPresenter(this, this);
        textSeachTag = goodsTitle + "";
        if (categoryId != null && Integer.parseInt(categoryId) > 0) {
            isSeachType = false;
            seachLL.setVisibility(View.GONE);
            tv_categoryName.setText(categoryName);
        } else {
            isSeachType = true;
            serarchKeyWord.setText(goodsTitle);
            seachLL.setVisibility(View.VISIBLE);
            top_searchImg.setVisibility(View.GONE);
            tv_categoryName.setVisibility(View.GONE);
        }
        buildRecyclerView();
        initList();
        showContent();
        changeType(0);
        getData();
    }

    private void initList() {
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanCount(2);
        recyclerQuestion.setLayoutManager(manager);
        mallGoodsItemHTransverseAdapter = new MallGoodsItemHTransverseAdapter(mContext);
        mallGoodsItemHTransverseAdapter.setSpanSize(2);
        recyclerQuestion.setAdapter(mallGoodsItemHTransverseAdapter);
        mallGoodsItemHTransverseAdapter.setOnBasketClickListener(new MallGoodsItemHTransverseAdapter.OnBasketClickListener() {
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
        serarchKeyWord.setOnEditorActionListener(this);
        clearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serarchKeyWord.setText("");
            }
        });
        serarchKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    clearEdit.setVisibility(View.VISIBLE);
                } else {
                    clearEdit.setVisibility(View.GONE);
                }
            }
        });
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        mallGoodsItemHTransverseAdapter.setOutClickListener(new MallGoodsItemHTransverseAdapter.MOutClickListener() {
            @Override
            public void outClick(String data, SortGoodsListModel model) {
                SortGoodsListModel goodsListModel = model;
                if (goodsListModel != null) {
                    serviceSortGoodsPresenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
                            .puts("shopId", SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP))
                            .puts("goodsShopId", goodsListModel.getShopId())
                            .puts("goodsSource", "1")
                            .puts("goodsType", goodsListModel.getGoodsType() + "")
                            .puts("goodsId", goodsListModel.getId() + "")
                            .puts("goodsSpecId", goodsListModel.getGoodsChildren().get(0).getId() + "")
                            .puts("goodsQuantity", 1 + "")
                    );
                }
            }
        });
        recyclerQuestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                int columnCount = layoutManager.getSpanCount();
                int positions[] = new int[columnCount];
                layoutManager.findLastVisibleItemPositions(positions);
                for (int i = 0; i < positions.length; i++) {
                    if (positions[i] > 5) {
                        mall_scrollUp.setVisibility(View.VISIBLE);
                    } else {
                        mall_scrollUp.setVisibility(View.GONE);
                    }
                }
            }
        });
        mall_scrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerQuestion.scrollToPosition(0);
                mall_scrollUp.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void getData() {
        super.getData();
        if (dataType == SortType.SALESSORT_DESC) {
            map.put("appSalesSort", "desc");
        } else if (dataType == SortType.PRICESORT_ASC) {
            map.put("platformPriceSort", "asc");
        } else if (dataType == SortType.PRICESORT_DESC) {
            map.put("platformPriceSort", "desc");
        }
        if (!isSeachType) {
            map.put("categoryIdQuery", categoryId + "");
        } else {
            map.put("goodsTitle", textSeachTag);
        }
        map.put("pageNum", pageNum + "");
        map.put("pageSize", "10");
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put("publish", "1");
        mallGoodsItemNoAdapter.clear();
        serviceSortGoodsPresenter.getGoodsList(map);

        if (!TextUtils.isEmpty(textSeachTag)) {//说明是搜索
            serviceSortGoodsPresenter.addSearchRecord(new SimpleHashMapBuilder<String, Object>()
                    .puts("searchContent", textSeachTag)
                    .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                    .puts("contentSource", 2)
                    .puts("function", "search_record_001")
            );
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        serviceSortGoodsPresenter.getShopCart();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerNoData.setLayoutManager(virtualLayoutManager);
        recyclerNoData.setAdapter(delegateAdapter);


        mallGoodsItemNoAdapter = new MallGoodsItemNoAdapter();
        delegateAdapter.addAdapter(mallGoodsItemNoAdapter);

        mallGoodsTitleAdapter = new MallGoodsTitleAdapter();
        delegateAdapter.addAdapter(mallGoodsTitleAdapter);

        mallGoodsItemAdapter = new MallGoodsItemAdapter();
        delegateAdapter.addAdapter(mallGoodsItemAdapter);
        mallGoodsItemAdapter.setKey("搜索");
        mallGoodsItemAdapter.setOnBasketClickListener(new MallGoodsItemAdapter.OnBasketClickListener() {
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
        mallGoodsItemAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
            @Override
            public void outClick(@NotNull String function, @NotNull Object obj) {
                if ("addShopCat".equals(function)) {
                    RecommendList goodsListModel = (RecommendList) obj;
                    if (goodsListModel != null) {
                        serviceSortGoodsPresenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
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

    private void initView() {

        shop_cart_rel = (ConstraintLayout) findViewById(R.id.shop_cart_rel);
        img_back = (ImageView) findViewById(R.id.img_back);
        top_searchImg = (ImageView) findViewById(R.id.top_searchImg);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
        tv_categoryName = findViewById(R.id.tv_categoryName);
        txt_default = findViewById(R.id.txt_default);
        price_up_img = findViewById(R.id.price_up_img);
        price_down_img = findViewById(R.id.price_down_img);
        txt_default.getPaint().setFakeBoldText(true);
        txt_sales_volume = findViewById(R.id.txt_sales_volume);
        shop_cart_num = findViewById(R.id.shop_cart_num);
        txt_price = findViewById(R.id.txt_price);
        sort_liner = findViewById(R.id.sort_liner);
        listImg = findViewById(R.id.listImg);
        mall_scrollUp = findViewById(R.id.mall_scrollUp);
        recyclerNoData = findViewById(R.id.recyclerNoData);
        listImg.setOnClickListener(this);
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
//                finish();
            }
        });
        shop_cart_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "event2APPSeachListShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "搜索列表页-购物车入口点击量"));
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_BASKET)
                        .navigation();
            }
        });
        txt_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(0);
            }
        });
        txt_sales_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(1);
            }
        });
        txt_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(2);
            }
        });
        seachLL = (LinearLayout) findViewById(R.id.seachLL);
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        clearEdit = (ImageView) findViewById(R.id.clearEdit);
        topSearchImg = (ImageView) findViewById(R.id.top_searchImg);
        topShopCatImg = (ImageView) findViewById(R.id.top_shopCatImg);
    }

    public void changeType(int type) {
        pageNum = 1;
        price_up_img.setImageResource(R.mipmap.service_price_sort_black);
        price_down_img.setImageResource(R.mipmap.service_price_sort_black);
        switch (type) {
            case 0:
                txt_default.setTextColor(Color.parseColor("#FA3C5A"));
                txt_default.getPaint().setFakeBoldText(true);
                txt_sales_volume.setTextColor(Color.parseColor("#444444"));
                txt_sales_volume.getPaint().setFakeBoldText(false);
                txt_price.setTextColor(Color.parseColor("#444444"));
                txt_price.getPaint().setFakeBoldText(false);
                pageNum = 1;
                dataType = 0;
                map.clear();
                getData();
                break;
            case 1:
                txt_default.setTextColor(Color.parseColor("#444444"));
                txt_default.getPaint().setFakeBoldText(false);
                txt_sales_volume.setTextColor(Color.parseColor("#FA3C5A"));
                txt_sales_volume.getPaint().setFakeBoldText(true);
                txt_price.setTextColor(Color.parseColor("#444444"));
                txt_price.getPaint().setFakeBoldText(false);
                if (dataType != 2) {
                    dataType = 2;
                }
                pageNum = 1;
                map.clear();
                getData();
                break;
            case 2:
                txt_default.setTextColor(Color.parseColor("#444444"));
                txt_default.getPaint().setFakeBoldText(false);
                txt_sales_volume.setTextColor(Color.parseColor("#444444"));
                txt_sales_volume.getPaint().setFakeBoldText(false);
                txt_price.setTextColor(Color.parseColor("#FA3C5A"));
                txt_price.getPaint().setFakeBoldText(true);
                if (dataType != 4) {
                    dataType = 4;
                    price_up_img.setImageResource(R.mipmap.service_price_sort_black);
                    price_down_img.setImageResource(R.mipmap.service_price_sort_red);
                } else {
                    dataType = 3;
                    price_up_img.setImageResource(R.mipmap.service_price_sort_red);
                    price_down_img.setImageResource(R.mipmap.service_price_sort_black);
                }
                pageNum = 1;
                map.clear();
                getData();
                break;
            case 3:
                break;
        }
    }

    private void getRecommendList() {
        bottomMap.put("pageNum", pageNum + "");
        serviceSortGoodsPresenter.getRecommendList(bottomMap);
    }

    @Override
    public void onGetGoodsListSuccess(List<SortGoodsListModel> list, OrderListPageInfo pageInfo) {
        showContent();
        pageNum = pageInfo.getPageNum();
        if (pageNum == 1) {
            mallGoodsItemHTransverseAdapter.setList((ArrayList) list);
            if (list.size() == 0) {
                isNull = true;
                sort_liner.setVisibility(View.GONE);
                recyclerNoData.setVisibility(View.VISIBLE);
                recyclerQuestion.setVisibility(View.GONE);
                listImg.setVisibility(View.GONE);
                mallGoodsItemNoAdapter.setModel("抱歉，没有找到相关商品~");
                recyclerNoData.setBackgroundColor(Color.parseColor("#F5F5F9"));
                getRecommendList();
                showEmpty();
            } else {
                sort_liner.setVisibility(View.VISIBLE);
                recyclerNoData.setVisibility(View.GONE);
                recyclerQuestion.setVisibility(View.VISIBLE);
                listImg.setVisibility(View.VISIBLE);
                isNull = false;
            }
        } else {
            mallGoodsItemHTransverseAdapter.addList((ArrayList) list);
        }
        if (pageInfo.getNextPage() == 0) {
            ////System.out.println("没有更多了");
            if (!isNull) {
                layoutRefresh.finishLoadMoreWithNoMoreData();
            }
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void successAddShopCat(String result) {
//        showToast(result);
        serviceSortGoodsPresenter.getShopCart();
    }

    @Override
    public void onGetShopCartSuccess(ShopCartModel shopCartModel) {
        if (shopCartModel == null) {
            shop_cart_num.setText("0");
            return;
        }
        if (shopCartModel.total == 0) {
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
    public void onGetRecommendListSuccess(List<RecommendList> list) {
        if (pageNum == 1) {
            if (list != null && list.size() > 0) {
                mallGoodsTitleAdapter.setModel("猜你喜欢");
                mallGoodsItemAdapter.setData((ArrayList) list);
            }
        } else {
            if (list == null || list.size() == 0) {
                layoutRefresh.finishLoadMoreWithNoMoreData();
            } else {
                mallGoodsItemAdapter.addDatas((ArrayList) list);
            }
        }
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        if (isNull) {
            getRecommendList();
        } else {
            getData();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        if (isNull) {
            getRecommendList();
        } else {
            getData();
        }
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            if (TextUtils.isEmpty(serarchKeyWord.getText().toString())) {
//                return false;
//            }
            textSeachTag = serarchKeyWord.getText().toString();
            pageNum = 1;
            getData();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.listImg) {
            changeRecycleViewList();
        }
    }

    /**
     * 改变RecycleView的显示列数
     */
    private void changeRecycleViewList() {
        if (mallGoodsItemHTransverseAdapter != null) {
            if (isList) { // 当前一行显示一列  则变2列
                isList = false;
                listImg.setImageDrawable(getResources().getDrawable(R.drawable.server_sort_goods_list_transverse));
                manager.setSpanCount(2);
                mallGoodsItemHTransverseAdapter.setSpanSize(2);
            } else {// 当前一行显示两列 则变1列
                isList = true;
                listImg.setImageDrawable(getResources().getDrawable(R.drawable.server_sort_goods_list_portrait));
                manager.setSpanCount(1);
                mallGoodsItemHTransverseAdapter.setSpanSize(1);
            }
            // 第一个参数是动画开始的位置索引
            mallGoodsItemHTransverseAdapter.notifyItemRangeChanged(0, mallGoodsItemHTransverseAdapter.getItemCount());
        }
    }
}