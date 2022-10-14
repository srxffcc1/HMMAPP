package com.health.tencentlive.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.health.tencentlive.R;
import com.health.tencentlive.adapter.MyPrizeListFragmentAdapter;
import com.health.tencentlive.contract.LiveHelpContract;
import com.health.tencentlive.impl.OnHelpClickListener;
import com.health.tencentlive.model.InteractionDetail;
import com.health.tencentlive.model.JackpotList;
import com.health.tencentlive.model.LiveHelpList;
import com.health.tencentlive.presenter.LiveHelpPresenter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


public class MyPrizeFragment extends BaseFragment implements LiveHelpContract.View, BaseAdapter.OnOutClickListener, OnRefreshListener {

    private String courseId;
    private String shopId;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerView;
    private MyPrizeListFragmentAdapter myPrizeListFragmentAdapter;
    private LiveHelpPresenter liveHelpPresenter;
    private int pageNum = 1;
    private JackpotList.HelpGoodsList model;
    private OnHelpClickListener onClickListener;

    public MyPrizeFragment() {
        // Required empty public constructor
    }

    public static MyPrizeFragment newInstance(String param1, String param2) {
        MyPrizeFragment fragment = new MyPrizeFragment();
        Bundle args = new Bundle();
        args.putString("courseId", param1);
        args.putString("shopId", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getString("courseId");
            shopId = getArguments().getString("shopId");
        }
    }

    public void setClickListener(OnHelpClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void refresh() {
        showLoading();
        getData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_prize;
    }

    @Override
    protected void findViews() {

        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        initView();
    }

    private void initView() {
        liveHelpPresenter = new LiveHelpPresenter(getContext(), this);
        layoutRefresh.setOnRefreshListener(this);
        layoutRefresh.finishRefreshWithNoMoreData();
        myPrizeListFragmentAdapter = new MyPrizeListFragmentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(myPrizeListFragmentAdapter);
        myPrizeListFragmentAdapter.setOutClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            onRefresh(null);
        }
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
        showLoading();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        liveHelpPresenter.getJackpotList(new SimpleHashMapBuilder<String, Object>()
                .puts("pageNum", pageNum)
                .puts("pageSize", 100)
                .puts("courseId", courseId)
                .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
    }

    @Override
    public void getSuccessHelpRankingList(List<LiveHelpList> result, PageInfoEarly pageInfo) {

    }

    @Override
    public void getSuccessLiveHelpList(List<LiveHelpList> result, PageInfoEarly pageInfo) {

    }

    @Override
    public void getSuccessLiveHelpInfo(LiveHelpList result) {

    }

    @Override
    public void getSuccessCoupon(String result, String couponName) {
        if (result.contains("成功")) {
            showToast(String.format("您的奖品%s已发放到我的-优惠券", couponName));
            getData();
        }
    }

    @Override
    public void getSuccessWinId(String result) {
        if (result != null && !TextUtils.isEmpty(result) && model != null) {
            buildOrder(model.detail, result);
        }
    }

    @Override
    public void getSuccessHelpStatus(int result) {

    }

    @Override
    public void getSuccessJackpotList(List<JackpotList> result, PageInfoEarly pageInfo) {
        showContent();
        layoutRefresh.finishRefresh();
        myPrizeListFragmentAdapter.clear();
        if (result == null || result.size() == 0) {
            showEmpty();
        } else {
            myPrizeListFragmentAdapter.setData((ArrayList) result);
        }
    }

    @Override
    public void outClick(@NonNull String function, @NonNull Object obj) {
        if (function.equals("help")) {
            if (onClickListener != null) {
                onClickListener.onShare();
            }
        }
        if (function.equals("goods0")) {
            model = (JackpotList.HelpGoodsList) obj;
        }
        if (function.equals("goods1")) {
            if (obj != null) {
                liveHelpPresenter.getWinId(new SimpleHashMapBuilder<String, Object>().puts("helpId", ((String) obj).split(",")[0])
                        .puts("helpItemId", ((String) obj).split(",")[1])
                        .puts("courseId", courseId)
                        .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
            } else {
                Toast.makeText(getContext(), "领取失败，请重试", Toast.LENGTH_SHORT).show();
                getData();
            }
        }
        if (function.equals("goods2")) {
            JackpotList.HelpWinGoodsList bean = (JackpotList.HelpWinGoodsList) obj;
            buildOrder(bean.detail, bean.id);
        }

        if (function.equals("coupon1")) {
            JackpotList.HelpCouponList bean = (JackpotList.HelpCouponList) obj;
            liveHelpPresenter.getCoupon(new SimpleHashMapBuilder<String, Object>()
                    .puts("helpId", bean.helpId)
                    .puts("helpItemId", bean.id)
                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                    .puts("courseId", courseId), bean.detail.basic.getCouponNormalName());
        }
        if (function.equals("coupon2")) {
            JackpotList.HelpWinCouponList bean = (JackpotList.HelpWinCouponList) obj;
            liveHelpPresenter.getCoupon(new SimpleHashMapBuilder<String, Object>()
                    .puts("helpId", bean.helpId)
                    .puts("helpItemId", bean.helpItemId)
                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                    .puts("courseId", courseId), bean.detail.basic.getCouponNormalName());
        }
    }

    private void buildOrder(InteractionDetail.Goods goods, String winId) {
        if (goods.availableInventory <= 0) {//就说明已抢光
            Toast.makeText(getContext(), "已抢光", Toast.LENGTH_SHORT).show();
            return;
        }
        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(null);
        goodsMarketing.availableInventory = goods.availableInventory;
        goodsMarketing.mapMarketingGoodsId = "";
        goodsMarketing.marketingType = "-5";
        goodsMarketing.id = "";
        goodsMarketing.pointsPrice = 0;
        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goods.platformPrice, goods.platformPrice,
                goods.platformPrice,
                goods.goodsType,
                "0",
                "0", null);
        goodsBasketCell.goodsSpecDesc = goods.goodsSpecStr;
        goodsBasketCell.goodsStock = goods.availableInventory;
        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
        goodsBasketCell.mchId = SpUtils.getValue(getContext(), SpKey.CHOSE_MC);
        goodsBasketCell.goodsId = goods.goodsId;
        try {
            goodsBasketCell.setGoodsSpecId(goods.goodsChildId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        goodsBasketCell.goodsTitle = goods.goodsTitle;
        goodsBasketCell.goodsImage = goods.goodsImage;
        goodsBasketCell.setGoodsQuantity(1);
        goodsBasketCell.shopIdList = goods.shopIds;
        goodsBasketCell.goodsShopId = "";
        List<GoodsBasketCell> list = new ArrayList<>();
        list.add(goodsBasketCell);

        ARouter.getInstance()
                .build(ServiceRoutes.SERVICE_ORDER)
                .withString("visitShopId", shopId)
                .withString("token", null)
                .withString("course_id", null)
                .withString("liveStatus", null)
                .withString("live_anchor", null)
                .withString("live_course", null)
                .withObject("goodsbasketlist", list)
                .withString("goodsMarketingType", "-5")
                .withString("winType", "1")
                .withString("winId", winId)
                .navigation();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }
}