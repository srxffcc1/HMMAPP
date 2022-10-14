package com.health.discount.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
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
import com.health.discount.R;
import com.health.discount.adapter.DiscountListAdapter;
import com.health.discount.contract.DiscountListContract;
import com.health.discount.contract.MarketGoodsSpecContract;
import com.health.discount.presenter.DiscountListPresenter;
import com.health.discount.presenter.MarketGoodsSpecPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.DisGoodsSpecDialog;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.DiscountTopModel;
import com.healthy.library.model.GoodsBasketAll;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.PopDetailInfo;
import com.healthy.library.model.PopListInfo;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.SpUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = DiscountRoutes.DIS_DISCOUNTLIST)//满减/多买多送共用商品列表
public class DiscountListActivity extends BaseActivity implements OnRefreshLoadMoreListener, IsFitsSystemWindows
        , DiscountListContract.View, MarketGoodsSpecContract.View {
    @Autowired
    String appID;
    @Autowired
    String popNo;
    @Autowired
    String departID;

    private ImageView img_back, mall_scrollUp;
    private TextView txtDefault, txtSalesVolume, txtPrice, btnGoBesket, besketTotal;
    private TextView topLable, topLablePre, kickDay, kickDayT, kickSec, kickHour, kickMin;
    private TextView couponNum, couponPrice, couponType, couponPricePre, couponPriceEnd;
    private LinearLayout goodsTimeLL, couponPriceLL, seachLL;
    private ConstraintLayout bottomT;
    private RecyclerView recycler;
    private EditText serarchKeyWord;
    private SmartRefreshLayout layout_refresh;
    private DiscountListAdapter discountListAdapter;
    private android.widget.ImageView priceUpImg;
    private android.widget.ImageView listImg, clearEdit;
    private android.widget.ImageView priceDownImg;
    private int sortType = 1;//1综合  2销量  31价格正序  32价格降序
    private Map<String, Object> map = new HashMap<>();
    private int pageNum = 1;
    private boolean isList = true;//是否是竖排列表
    private StaggeredGridLayoutManager manager;
    private DiscountListPresenter discountListPresenter;
    private SparseArray<CountDownTimer> countDownCounters = new SparseArray<>();
    private MarketGoodsSpecPresenter marketGoodsSpecPresenter;
    private DisGoodsSpecDialog disGoodsSpecDialog;
    private android.widget.TextView usableEndTime;
    private PopListInfo popListInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_discount_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        discountListPresenter = new DiscountListPresenter(this, this);
        marketGoodsSpecPresenter = new MarketGoodsSpecPresenter(mContext, this);
        initList();
        //discountListPresenter.getBasketList(new SimpleHashMapBuilder<String, Object>().puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)));
        getData();
        besketTotal.setText("");
    }

    private void initList() {
        manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        discountListAdapter = new DiscountListAdapter(mContext);
        discountListAdapter.setSpanSize(1);
        recycler.setAdapter(discountListAdapter);
        //changeType(1);
        discountListAdapter.setOutClickListener(new DiscountListAdapter.MOutClickListener() {
            @Override
            public void outClick(final PopDetailInfo.GoodsDTOListBean goodsDTOListBean) {
                if (goodsDTOListBean != null) {
                    if (disGoodsSpecDialog == null) {
                        disGoodsSpecDialog = DisGoodsSpecDialog.newInstance();
                    }
                    disGoodsSpecDialog.show(getSupportFragmentManager(), "优惠券");
                    if (goodsDTOListBean.goodsChildren != null && goodsDTOListBean.goodsChildren.size() > 0) {
                        disGoodsSpecDialog.setPopListInfo(popListInfo);
                        disGoodsSpecDialog.setSingleSelectAct(true);
                        disGoodsSpecDialog.setGoodsDetail(goodsDTOListBean);
                        disGoodsSpecDialog.initSpec(goodsDTOListBean.goodsChildren);
                        disGoodsSpecDialog.setOnSpecSubmitListener(new DisGoodsSpecDialog.OnSpecSubmitListener() {
                            @Override
                            public void onSpecSubmit(PopDetailInfo.GoodsDTOListBean.GoodsChildrenBean goodsSpecCell) {
                                discountListPresenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
                                        .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                                        .puts("goodsShopId", goodsDTOListBean.getShopId())
                                        .puts("goodsSource", "1")
                                        .puts("goodsType", goodsDTOListBean.goodsType + "")
                                        .puts("goodsId", goodsSpecCell.goodsId + "")
                                        .puts("goodsSpecId", goodsSpecCell.id)
                                        .puts("goodsQuantity", goodsSpecCell.getCount()));
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onSucessGetList(List<PopDetailInfo.GoodsDTOListBean> result) {
        showContent();
        if (pageNum == 1) {
            if (result == null || result.size() == 0) {
                showEmpty();
            } else {
                discountListAdapter.setList((ArrayList) result);
            }
        } else {
            if (result == null || result.size() == 0) {
                layout_refresh.finishLoadMoreWithNoMoreData();
            } else {
                discountListAdapter.addList((ArrayList) result);
            }
        }
    }

    @Override
    public void onSucessGetTopTitle(PopListInfo result) {
        showContent();
        if (result != null) {
            topLable.setVisibility(View.VISIBLE);
            this.popListInfo = result;
            topLable.setText(result.PopLabelName);
            topLablePre.setText(result.PopDesc);
            if (result.StartDate != null && !TextUtils.isEmpty(result.StartDate) && result.EndDate != null && !TextUtils.isEmpty(result.EndDate)) {
//            checkTimeOut(result, kickDay, kickDayT, kickHour, kickMin, kickSec);
                try {
                    usableEndTime.setText(result.getBeginTime().replaceAll("-", ".") + "-" + result.getEndTime().replaceAll("-", "."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            topLable.setVisibility(View.GONE);
            showEmpty();
        }
    }

    @Override
    public void successAddShopCat(String result) {
        if (result.contains("购物车添加商品")) {
            showToast("已加入购物车");
            //discountListPresenter.getBasketList(new SimpleHashMapBuilder<String, Object>().puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)));
        }
    }

    ArrayList<GoodsBasketCell> validList = new ArrayList<>();

    @Override
    public void onSucessGetBasketList(GoodsBasketAll goodsBasketAll) {
//        bottomT.setVisibility(View.VISIBLE);
//        if (goodsBasketAll != null) {
//            validList.clear();
//            if (goodsBasketAll.validList != null && goodsBasketAll.validList.size() > 0) {
//                for (int i = 0; i < goodsBasketAll.validList.size(); i++) {
//                    if (goodsBasketAll.validList.get(i).goodsMarketingDTO != null) {
//                        if (goodsBasketAll.validList.get(i).goodsMarketingDTO.marketingId.equals(marketingId)) {
//                            validList.add(goodsBasketAll.validList.get(i));
//                        }
//                    }
//                }
//            } else {
//                besketTotal.setText("小计：￥0");
//                btnGoBesket.setText("去购物车");
//            }
//            if (validList.size() > 0) {
//
//                for (int i = 0; i < goodsBasketAll.validList.size(); i++) {
//                    GoodsBasketCell goodsBasketCell = goodsBasketAll.validList.get(i);
//                    if (marketingId.equals(goodsBasketCell.getGoodsMarketingIdOrg())) {
//                        goodsBasketCell.checkActGift(discountTopModel);
//                        goodsBasketCell.checkActDiscount(discountTopModel);
//                    }
//
//                }
//
//                BigDecimal totalPrice = BigDecimal.valueOf(0.0);
//                for (int i = 0; i < validList.size(); i++) {
//                    totalPrice = totalPrice.add(new BigDecimal(validList.get(i).getCurGoodsAmount()).multiply(new BigDecimal(validList.get(i).getGoodsQuantity())));
//                }
//                besketTotal.setText("小计：￥" + FormatUtils.moneyKeep2Decimals(totalPrice.toString()));
//                btnGoBesket.setText("去购物车(" + validList.size() + ")");
//            }
//
//            if (discountTopModel != null) {
//                int goodsQuantityGiftNeedFixOrg = 0;//获得的赠品数量
//                BigDecimal totalDecimal = new BigDecimal(0);//满减优惠金额
//                BigDecimal virtualDiscount = new BigDecimal(0);//买送优惠金额
//                for (int i = 0; i < goodsBasketAll.validList.size(); i++) {
//                    GoodsBasketCell goodsBasketCell = goodsBasketAll.validList.get(i);
//                    if (marketingId.equals(goodsBasketCell.getGoodsMarketingIdOrg())) {
//                        goodsBasketCell.checkActGift(discountTopModel);
//                        goodsQuantityGiftNeedFixOrg += goodsBasketCell.getGoodsQuantityGiftNeedFixOrg();
//                        totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketCell.checkActDiscount(discountTopModel)));
//                        virtualDiscount = virtualDiscount.add(new BigDecimal(goodsBasketCell.virtualDiscount));
//                    }
//
//                }
//
//                if ("6".equals(marketingType)) {
//                    couponType.setText("");
//                    couponNum.setText("");
//                    couponPricePre.setText("下单立减");
//                    if (totalDecimal.doubleValue() > 0) {
//                        couponPriceEnd.setVisibility(View.GONE);
//                        couponPriceLL.setVisibility(View.VISIBLE);
//                        couponPrice.setText(FormatUtils.moneyKeep2Decimals(totalDecimal.doubleValue()));
//                    } else {
//                        couponPriceEnd.setVisibility(View.VISIBLE);
//                        couponPriceLL.setVisibility(View.GONE);
//                    }
//                } else if ("7".equals(marketingType)) {
//                    if (goodsQuantityGiftNeedFixOrg > 0 && virtualDiscount.doubleValue() > 0) {
//                        couponPriceEnd.setVisibility(View.GONE);
//                        couponPriceLL.setVisibility(View.VISIBLE);
//                        couponNum.setText(goodsQuantityGiftNeedFixOrg + "");
//                        couponPrice.setText(FormatUtils.moneyKeep2Decimals(virtualDiscount.doubleValue()));
//                    } else {
//                        couponPriceEnd.setVisibility(View.VISIBLE);
//                        couponPriceLL.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//        } else {
//            besketTotal.setText("小计：￥0");
//            btnGoBesket.setText("去购物车");
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // discountListPresenter.getBasketList(new SimpleHashMapBuilder<String, Object>().puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)));
    }

    @Override
    public void getData() {
        super.getData();
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put("appID", appID);
        map.put("popNo", popNo);
        map.put("departID", departID);
        map.put("pageNum", pageNum + "");
        map.put("pageSize", "10");
//        if (serarchKeyWord.getText().toString() != null) {
//            map.put("searchContent", serarchKeyWord.getText().toString());
//        }
        discountListPresenter.getGoodsList(map);
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

    /**
     * 改变RecycleView的显示列数
     */
    private void changeRecycleViewList() {
        if (discountListAdapter != null) {
            if (isList) { // 当前一行显示一列  则变2列
                isList = false;
                listImg.setImageDrawable(getResources().getDrawable(R.drawable.coupon_goods_list_transverse));
                manager.setSpanCount(2);
                discountListAdapter.setSpanSize(2);
            } else {// 当前一行显示两列 则变1列
                isList = true;
                listImg.setImageDrawable(getResources().getDrawable(R.drawable.coupon_goods_list_portrait));
                manager.setSpanCount(1);
                discountListAdapter.setSpanSize(1);
            }
            // 第一个参数是动画开始的位置索引
            discountListAdapter.notifyItemRangeChanged(0, discountListAdapter.getItemCount());
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
                pageNum = 1;
                sortType = 1;
                map.clear();
                if (discountListAdapter != null) {
                    discountListAdapter.clear();
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
                sortType = 2;
                pageNum = 1;
                map.clear();
                if (discountListAdapter != null) {
                    discountListAdapter.clear();
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
                if (sortType == 31) {
                    sortType = 32;
                    priceUpImg.setImageResource(R.mipmap.service_price_sort_black);
                    priceDownImg.setImageResource(R.mipmap.service_price_sort_red);
                } else {
                    sortType = 31;
                    priceUpImg.setImageResource(R.mipmap.service_price_sort_red);
                    priceDownImg.setImageResource(R.mipmap.service_price_sort_black);
                }
                pageNum = 1;
                map.clear();
                if (discountListAdapter != null) {
                    discountListAdapter.clear();
                }
                getData();
                break;
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        usableEndTime = (TextView) findViewById(R.id.usableEndTime);
        img_back = findViewById(R.id.img_back);
        recycler = findViewById(R.id.recycler);
        layout_refresh = findViewById(R.id.layout_refresh);
        txtDefault = (TextView) findViewById(R.id.txt_default);
        txtSalesVolume = (TextView) findViewById(R.id.txt_sales_volume);
        txtPrice = (TextView) findViewById(R.id.txt_price);
        topLable = (TextView) findViewById(R.id.topLable);
        topLablePre = (TextView) findViewById(R.id.topLablePre);
        goodsTimeLL = (LinearLayout) findViewById(R.id.goodsTimeLL);
        couponPriceLL = (LinearLayout) findViewById(R.id.couponPriceLL);
        seachLL = (LinearLayout) findViewById(R.id.seachLL);
        kickDay = (TextView) findViewById(R.id.kickDay);
        kickDayT = (TextView) findViewById(R.id.kickDayT);
        kickHour = (TextView) findViewById(R.id.kickHour);
        kickMin = (TextView) findViewById(R.id.kickMin);
        btnGoBesket = (TextView) findViewById(R.id.btnGoBesket);
        bottomT = (ConstraintLayout) findViewById(R.id.bottomT);
        kickSec = (TextView) findViewById(R.id.kickSec);
        besketTotal = (TextView) findViewById(R.id.besketTotal);
        couponPrice = (TextView) findViewById(R.id.couponPrice);
        couponNum = (TextView) findViewById(R.id.couponNum);
        couponType = (TextView) findViewById(R.id.couponType);
        couponPricePre = (TextView) findViewById(R.id.couponPricePre);
        couponPriceEnd = (TextView) findViewById(R.id.couponPriceEnd);
        priceUpImg = (ImageView) findViewById(R.id.price_up_img);
        listImg = (ImageView) findViewById(R.id.listImg);
        clearEdit = (ImageView) findViewById(R.id.clearEdit);
        priceDownImg = (ImageView) findViewById(R.id.price_down_img);
        mall_scrollUp = (ImageView) findViewById(R.id.mall_scrollUp);
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
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
        layout_refresh.setOnRefreshLoadMoreListener(this);
        listImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRecycleViewList();
            }
        });
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
        btnGoBesket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_BASKET)
                        .navigation();
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
                    discountListAdapter.clear();
                    getData();
                } else {
                    clearEdit.setVisibility(View.GONE);
                    getData();
                }
            }
        });
    }

    CountDownTimer countDownTimer;

    private void checkTimeOut(final DiscountTopModel url, final TextView kickDay, final TextView kickDayT, final TextView kickHour, final TextView kickMin, final TextView kickSec) {
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.beginTime);
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long timer = endTime.getTime() - new Date().getTime();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (timer > 0) {
            final long finalTimer = timer;
            countDownTimer = new CountDownTimer(finalTimer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] array = DateUtils.getDistanceTimeOnlySecondNoDouble(finalTimer);
                    if ("0".equals(array[0])) {//0天
                        kickDay.setVisibility(View.GONE);
                        kickDayT.setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(array[0]) < 10) {
                        kickDay.setText("0" + array[0]);
                    } else {
                        kickDay.setText(array[0]);
                    }
                    kickHour.setText(array[1]);
                    kickMin.setText(array[2]);
                    kickSec.setText(array[3]);
                }

                public void onFinish() {
                    kickDay.setVisibility(View.GONE);
                    kickDayT.setVisibility(View.GONE);
                    kickDay.setText("0");
                    kickHour.setText("00");
                    kickMin.setText("00");
                    kickSec.setText("00");
                }
            }.start();
        } else {
            kickDay.setVisibility(View.GONE);
            kickDayT.setVisibility(View.GONE);
            kickDay.setText("0");
            kickHour.setText("00");
            kickMin.setText("00");
            kickSec.setText("00");
        }
    }

    @Override
    public void onSucessGetList() {

    }
}