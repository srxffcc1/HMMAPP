//package com.health.discount.activity;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.StaggeredGridLayoutManager;
//
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.health.discount.R;
//import com.health.discount.adapter.CouponGoodsListAdapter;
//import com.healthy.library.base.BaseActivity;
//import com.healthy.library.interfaces.IsFitsSystemWindows;
//import com.healthy.library.routes.DiscountRoutes;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Route(path = DiscountRoutes.DIS_COUPONGOODSLIST)//通用券页面
//public class CouponGoodsListActivity extends BaseActivity implements IsFitsSystemWindows, OnRefreshLoadMoreListener {
//    private SmartRefreshLayout layout_refresh;
//    private RecyclerView recycler;
//    private android.widget.TextView txtDefault;
//    private android.widget.TextView txtSalesVolume;
//    private android.widget.TextView txtPrice;
//    private android.widget.ImageView priceUpImg;
//    private android.widget.ImageView listImg;
//    private android.widget.ImageView priceDownImg;
//    private CouponGoodsListAdapter couponGoodsListAdapter;
//    private String type;
//    private int sortType = 1;//1综合 2销量 3积分
//    private int sort = 2;//1正序 2倒序
//    private Map<String, Object> map = new HashMap<>();
//    private int pageNum = 1;
//    private boolean isList = true;//是否是竖排列表
//    private StaggeredGridLayoutManager manager;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_coupon_goods_list;
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        ARouter.getInstance().inject(this);
//        initList();
//    }
//    private void initList() {
//        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        recycler.setLayoutManager(manager);
//        couponGoodsListAdapter = new CouponGoodsListAdapter(mContext);
//        couponGoodsListAdapter.setSpanSize(2);
//        recycler.setAdapter(couponGoodsListAdapter);
//        showContent();
//    }
//
//    @Override
//    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//        pageNum++;
//        getData();
//    }
//
//    @Override
//    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        pageNum = 1;
//        getData();
//    }
//
//    @Override
//    public void onRequestFinish() {
//        super.onRequestFinish();
//        layout_refresh.finishRefresh();
//        layout_refresh.finishLoadMore();
//    }
//    /**
//     * 改变RecycleView的显示列数
//     */
//    private void changeRecycleViewList() {
//        if (couponGoodsListAdapter != null) {
//            if (isList) { // 当前一行显示一列  则变2列
//                isList = false;
//                listImg.setImageDrawable(getResources().getDrawable(R.drawable.coupon_goods_list_portrait));
//                manager.setSpanCount(2);
//                couponGoodsListAdapter.setSpanSize(2);
//            } else {// 当前一行显示两列 则变1列
//                isList = true;
//                listImg.setImageDrawable(getResources().getDrawable(R.drawable.coupon_goods_list_transverse));
//                manager.setSpanCount(1);
//                couponGoodsListAdapter.setSpanSize(1);
//            }
//            // 第一个参数是动画开始的位置索引
//            couponGoodsListAdapter.notifyItemRangeChanged(0, couponGoodsListAdapter.getItemCount());
//        }
//    }
//    @Override
//    protected void findViews() {
//        super.findViews();
//        layout_refresh = findViewById(R.id.layout_refresh);
//        recycler = findViewById(R.id.recycler);
//        txtDefault = (TextView) findViewById(R.id.txt_default);
//        txtSalesVolume = (TextView) findViewById(R.id.txt_sales_volume);
//        txtPrice = (TextView) findViewById(R.id.txt_price);
//        priceUpImg = (ImageView) findViewById(R.id.price_up_img);
//        listImg = (ImageView) findViewById(R.id.listImg);
//        priceDownImg = (ImageView) findViewById(R.id.price_down_img);
//        txtDefault.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeType(1);
//            }
//        });
//        txtSalesVolume.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeType(2);
//            }
//        });
//        txtPrice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeType(3);
//            }
//        });
//        layout_refresh.setOnRefreshLoadMoreListener(this);
//        listImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeRecycleViewList();
//            }
//        });
//    }
//    public void changeType(int type) {
//        priceUpImg.setImageResource(R.mipmap.service_price_sort_black);
//        priceDownImg.setImageResource(R.mipmap.service_price_sort_black);
//        switch (type) {
//            case 1:
//                txtDefault.setTextColor(Color.parseColor("#F02846"));
//                txtDefault.getPaint().setFakeBoldText(true);
//                txtSalesVolume.setTextColor(Color.parseColor("#444444"));
//                txtSalesVolume.getPaint().setFakeBoldText(false);
//                txtPrice.setTextColor(Color.parseColor("#444444"));
//                txtPrice.getPaint().setFakeBoldText(false);
//                sort = 2;
//                pageNum = 1;
//                sortType = 1;
//                map.clear();
////                if (publicCouponFragmentListAdapter != null) {
////                    publicCouponFragmentListAdapter.clear();
////                }
//                getData();
//                break;
//            case 2:
//                txtDefault.setTextColor(Color.parseColor("#444444"));
//                txtDefault.getPaint().setFakeBoldText(false);
//                txtSalesVolume.setTextColor(Color.parseColor("#F02846"));
//                txtSalesVolume.getPaint().setFakeBoldText(true);
//                txtPrice.setTextColor(Color.parseColor("#444444"));
//                txtPrice.getPaint().setFakeBoldText(false);
//                sort = 2;
//                sortType = 2;
//                pageNum = 1;
//                map.clear();
////                if (publicCouponFragmentListAdapter != null) {
////                    publicCouponFragmentListAdapter.clear();
////                }
//                getData();
//                break;
//            case 3:
//                txtDefault.setTextColor(Color.parseColor("#444444"));
//                txtDefault.getPaint().setFakeBoldText(false);
//                txtSalesVolume.setTextColor(Color.parseColor("#444444"));
//                txtSalesVolume.getPaint().setFakeBoldText(false);
//                txtPrice.setTextColor(Color.parseColor("#F02846"));
//                txtPrice.getPaint().setFakeBoldText(true);
//                if (sort != 2) {
//                    sort = 2;
//                    priceUpImg.setImageResource(R.mipmap.service_price_sort_black);
//                    priceDownImg.setImageResource(R.mipmap.service_price_sort_red);
//                } else {
//                    sort = 1;
//                    priceUpImg.setImageResource(R.mipmap.service_price_sort_red);
//                    priceDownImg.setImageResource(R.mipmap.service_price_sort_black);
//                }
//                sortType = 3;
//                pageNum = 1;
//                map.clear();
////                if (publicCouponFragmentListAdapter != null) {
////                    publicCouponFragmentListAdapter.clear();
////                }
//                getData();
//                break;
//        }
//    }
//}