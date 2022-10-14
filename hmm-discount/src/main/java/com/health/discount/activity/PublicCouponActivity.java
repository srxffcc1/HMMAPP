package com.health.discount.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.discount.R;
import com.health.discount.adapter.PublicCouponAdapter;
import com.health.discount.contract.PublicCouponContract;
import com.health.discount.presenter.PublicCouponPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.CouponGoodsModel;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = DiscountRoutes.DIS_PUBLICCOUPON)//通用券页面
public class PublicCouponActivity extends BaseActivity implements IsFitsSystemWindows,
        OnRefreshLoadMoreListener, PublicCouponContract.View {

    @Autowired
    String cardId;
    @Autowired
    String cardName;
    @Autowired
    String time;

    private SmartRefreshLayout layout_refresh;
    private RecyclerView recycler;
    private LinearLayout seachLL;
    private android.widget.EditText serarchKeyWord;
    private android.widget.TextView txtDefault;
    private android.widget.TextView txtSalesVolume;
    private android.widget.TextView txtPrice;
    private android.widget.TextView topLablePre;
    private android.widget.TextView usableEndTime;
    private android.widget.ImageView priceUpImg;
    private android.widget.ImageView priceDownImg;
    private android.widget.ImageView mall_scrollUp;
    private PublicCouponAdapter publicCouponAdapter;
    private android.widget.ImageView listImg, clearEdit;
    private int sortType = 1;//1综合 2销量 3价格
    private int sort = 2;//1正序 2倒序
    private Map<String, Object> map = new HashMap<>();
    private int pageNum = 1;
    private PublicCouponPresenter publicCouponPresenter;
    private boolean isList = true;//是否是竖排列表
    private StaggeredGridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_public_coupon;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        publicCouponPresenter = new PublicCouponPresenter(this, this);
        initList();
        getData();
        topLablePre.setText(cardName != null ? cardName : "");
        usableEndTime.setText(time != null ? time : "");
        layout_refresh.setOnRefreshLoadMoreListener(this);
    }

    private void initList() {
        manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        publicCouponAdapter = new PublicCouponAdapter(mContext);
        publicCouponAdapter.setSpanSize(1);
        recycler.setAdapter(publicCouponAdapter);
        changeType(1);
        layout_refresh.setOnLoadMoreListener(this);
        publicCouponAdapter.setOutClickListener(new PublicCouponAdapter.MOutClickListener() {
            @Override
            public void outClick(CouponGoodsModel couponGoodsModel) {
                publicCouponPresenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
                        .puts("shopId",SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                        .puts("goodsShopId", couponGoodsModel.getGoodsShopId())
                        .puts("goodsSource", "1")
                        .puts("goodsType", couponGoodsModel.goodsType + "")
                        .puts("goodsId", couponGoodsModel.id + "")
                        .puts("goodsSpecId", couponGoodsModel.goodsChildren.get(0).id)
                        .puts("goodsQuantity", "1"));
            }
        });
    }

    @Override
    public void getData() {
        super.getData();
        map.put("couponId", cardId);
        map.put("page", pageNum + "");
        map.put("pageSize", "10");
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        if (sortType == 2) {
            map.put("appSalesSort", "desc");
        } else if (sortType == 3) {
            if (sort == 1) {
                map.put("platformPriceSort", "asc");
            } else {
                map.put("platformPriceSort", "desc");
            }
        }
        if (serarchKeyWord.getText().toString() != null) {
            map.put("term", serarchKeyWord.getText().toString());
        }
        publicCouponPresenter.getGoodsList(map);
    }

    @Override
    public void onSucessGetList(List<CouponGoodsModel> result, PageInfoEarly pageInfoEarly) {
        if (pageNum == 1) {
            if (result == null || result.size() == 0) {
                showEmpty();
            } else {
                publicCouponAdapter.setList((ArrayList) result);
                layout_refresh.setNoMoreData(false);
                layout_refresh.setEnableLoadMore(true);
            }
        } else {
            if (result == null || result.size() == 0) {
                layout_refresh.finishLoadMoreWithNoMoreData();
            } else {
                publicCouponAdapter.addList((ArrayList) result);
                layout_refresh.setNoMoreData(false);
                layout_refresh.setEnableLoadMore(true);
            }
        }
//        if (pageInfoEarly == null) {
//            showEmpty();
//            layout_refresh.setEnableLoadMore(false);
//            return;
//        }
//        if (pageNum == 1) {
//            publicCouponAdapter.setList((ArrayList) result);
//            if (result.size() == 0) {
//                showEmpty();
//            }
//        } else {
//            publicCouponAdapter.addList((ArrayList) result);
//        }
//        if (pageInfoEarly.nextPage == 0) {
//            layout_refresh.finishLoadMoreWithNoMoreData();
//        } else {
//            layout_refresh.setNoMoreData(false);
//            layout_refresh.setEnableLoadMore(true);
//        }
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
        layout_refresh.finishRefresh();
        layout_refresh.finishLoadMore();
    }

    @Override
    public void successAddShopCat(String result) {
        if (result.contains("购物车添加商品")) {
            showToast("已加入购物车");
        }
    }

    /**
     * 改变RecycleView的显示列数
     */
    private void changeRecycleViewList() {
        if (publicCouponAdapter != null) {
            if (isList) { // 当前一行显示一列  则变2列
                isList = false;
                listImg.setImageDrawable(getResources().getDrawable(R.drawable.coupon_goods_list_transverse));
                manager.setSpanCount(2);
                publicCouponAdapter.setSpanSize(2);
            } else {// 当前一行显示两列 则变1列
                isList = true;
                listImg.setImageDrawable(getResources().getDrawable(R.drawable.coupon_goods_list_portrait));
                manager.setSpanCount(1);
                publicCouponAdapter.setSpanSize(1);
            }
            // 第一个参数是动画开始的位置索引
            publicCouponAdapter.notifyItemRangeChanged(0, publicCouponAdapter.getItemCount());
        }
    }

    public void changeType(int type) {
        priceUpImg.setImageResource(R.mipmap.service_price_sort_black);
        priceDownImg.setImageResource(R.mipmap.service_price_sort_black);
        switch (type) {
            case 1:
                txtDefault.setTextColor(Color.parseColor("#F02846"));
                txtDefault.getPaint().setFakeBoldText(true);
                txtSalesVolume.setTextColor(Color.parseColor("#444444"));
                txtSalesVolume.getPaint().setFakeBoldText(false);
                txtPrice.setTextColor(Color.parseColor("#444444"));
                txtPrice.getPaint().setFakeBoldText(false);
                sort = 2;
                pageNum = 1;
                sortType = 1;
                map.clear();
                if (publicCouponAdapter != null) {
                    publicCouponAdapter.clear();
                }
                getData();
                break;
            case 2:
                txtDefault.setTextColor(Color.parseColor("#444444"));
                txtDefault.getPaint().setFakeBoldText(false);
                txtSalesVolume.setTextColor(Color.parseColor("#F02846"));
                txtSalesVolume.getPaint().setFakeBoldText(true);
                txtPrice.setTextColor(Color.parseColor("#444444"));
                txtPrice.getPaint().setFakeBoldText(false);
                sort = 2;
                sortType = 2;
                pageNum = 1;
                map.clear();
                if (publicCouponAdapter != null) {
                    publicCouponAdapter.clear();
                }
                getData();
                break;
            case 3:
                txtDefault.setTextColor(Color.parseColor("#444444"));
                txtDefault.getPaint().setFakeBoldText(false);
                txtSalesVolume.setTextColor(Color.parseColor("#444444"));
                txtSalesVolume.getPaint().setFakeBoldText(false);
                txtPrice.setTextColor(Color.parseColor("#F02846"));
                txtPrice.getPaint().setFakeBoldText(true);
                if (sort != 2) {
                    sort = 2;
                    priceUpImg.setImageResource(R.mipmap.service_price_sort_black);
                    priceDownImg.setImageResource(R.mipmap.service_price_sort_red);
                } else {
                    sort = 1;
                    priceUpImg.setImageResource(R.mipmap.service_price_sort_red);
                    priceDownImg.setImageResource(R.mipmap.service_price_sort_black);
                }
                sortType = 3;
                pageNum = 1;
                map.clear();
                if (publicCouponAdapter != null) {
                    publicCouponAdapter.clear();
                }
                getData();
                break;
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        layout_refresh = findViewById(R.id.layout_refresh);
        recycler = findViewById(R.id.recycler);
        txtDefault = (TextView) findViewById(R.id.txt_default);
        txtSalesVolume = (TextView) findViewById(R.id.txt_sales_volume);
        txtPrice = (TextView) findViewById(R.id.txt_price);
        usableEndTime = (TextView) findViewById(R.id.usableEndTime);
        topLablePre = (TextView) findViewById(R.id.topLablePre);
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        priceUpImg = (ImageView) findViewById(R.id.price_up_img);
        priceDownImg = (ImageView) findViewById(R.id.price_down_img);
        seachLL = (LinearLayout) findViewById(R.id.seachLL);
        listImg = (ImageView) findViewById(R.id.listImg);
        clearEdit = (ImageView) findViewById(R.id.clearEdit);
        mall_scrollUp = (ImageView) findViewById(R.id.mall_scrollUp);
        txtDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(1);
            }
        });
        txtSalesVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(2);
            }
        });
        txtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(3);
            }
        });
        listImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRecycleViewList();
            }
        });
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
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

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mall_scrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler.scrollToPosition(0);
                mall_scrollUp.setVisibility(View.GONE);
            }
        });
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
                    pageNum = 1;
                    map.clear();
                    sortType = 1;
                    publicCouponAdapter.clear();
                    getData();
                } else {
                    clearEdit.setVisibility(View.GONE);
                    getData();
                }
            }
        });
    }

}