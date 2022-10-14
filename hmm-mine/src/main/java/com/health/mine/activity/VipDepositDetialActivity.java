package com.health.mine.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.adapter.VipDespositListAdapter;
import com.health.mine.contract.VipDepositContract;
import com.health.mine.presenter.VipDepositPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.DepositList;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Route(path = MineRoutes.MINE_VIPDEPOSITDETIAL)
public class VipDepositDetialActivity extends BaseActivity implements OnRefreshLoadMoreListener, VipDepositContract.View {

    @Autowired
    String GoodsType;
    @Autowired
    String ytbAppId;
    @Autowired
    String cardNo;
    @Autowired
    com.healthy.library.model.VipDeposit VipDeposit;

    private TopBar topBar;
    private SmartRefreshLayout layoutRefresh;
    private TextView depositTitle;
    private TextView depositNum;
    private TextView depositSKU;
    private TextView serviceDate;
    private View line;
    private TextView depositShops;
    private TextView depositType;
    private StatusLayout layoutStatus;
    private RecyclerView depositList;


    private VipDepositPresenter vipDepositPresenter;
    private VipDespositListAdapter vipDespositListAdapter;
    private int page = 1;
    private TextView depositService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_deposit_detial;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        vipDepositPresenter = new VipDepositPresenter(this, this);
        if (GoodsType != null) {
            if (GoodsType.equals("1")) {
                topBar.setTitle("存取明细");
                depositType.setText("存取明细");
            } else {
                topBar.setTitle("服务明细");
                depositType.setText("服务明细");
            }

        } else {

        }
        if (VipDeposit != null) {
            initData();
        } else {

        }
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        vipDepositPresenter.getDepositList(new SimpleHashMapBuilder<String, Object>()
                .puts("cardNo", cardNo)
                .puts("ytbAppId", ytbAppId)
                .puts("goodsID", VipDeposit.GoodsID)
                .puts("pageSize", 10)
                .puts("page", page), null);
    }

    private void initData() {
        depositTitle.setText(VipDeposit.GoodsName);
        depositNum.setText("x" + VipDeposit.YeNumber);
        depositSKU.setText("SKU " + VipDeposit.StuffNo);
        if (VipDeposit.LimiteDepartID != null && !TextUtils.isEmpty(VipDeposit.LimiteDepartID)) {
            depositShops.setVisibility(View.VISIBLE);
            SpannableString str;
            if (VipDeposit.LimiteDepartID.contains("不限")) {
                str = new SpannableString("适用门店  不限门店，所有门店通用。");
                depositShops.setText("");
            } else {
                str = new SpannableString("适用门店  " + VipDeposit.LimiteDepartID);
            }
            str.setSpan(new AbsoluteSizeSpan(13, true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
            str.setSpan(new StyleSpan(Typeface.NORMAL), 5, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
            depositShops.setText(str.toString()
                    .replaceAll("【", "[").replaceAll("】", "]")
                    .replaceAll("（", "(").replaceAll("）", ")"));
        } else {
            depositShops.setVisibility(View.GONE);
        }
        serviceDate.setVisibility(View.GONE);
        if (GoodsType != null && GoodsType.equals("0")) {
            StringBuffer date = new StringBuffer();
            date.append("有效期：");
            if (VipDeposit.StartDate != null && !TextUtils.isEmpty(VipDeposit.StartDate)) {
                serviceDate.setVisibility(View.VISIBLE);
                if (VipDeposit.EndDate != null && !TextUtils.isEmpty(VipDeposit.EndDate)) {
                    date.append(VipDeposit.StartDate + " 至 " + VipDeposit.EndDate);
                } else {
                    date.append(VipDeposit.StartDate);
                }
                serviceDate.setText(date.toString());
            } else {
                serviceDate.setVisibility(View.VISIBLE);
                if (VipDeposit.EndDate != null && !TextUtils.isEmpty(VipDeposit.EndDate)) {
                    date.append(VipDeposit.EndDate);
                }
                serviceDate.setText(date.toString());
            }
        }
        if (VipDeposit.UseWay != null && !TextUtils.isEmpty(VipDeposit.UseWay)) {
            depositService.setVisibility(View.VISIBLE);
            SpannableString str;
            if (VipDeposit.UseWay.contains("不限")) {
                str = new SpannableString("服务说明  ");
                depositService.setText("");
            } else {
                str = new SpannableString("服务说明  " + VipDeposit.UseWay);
            }
            str.setSpan(new AbsoluteSizeSpan(13, true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
            str.setSpan(new StyleSpan(Typeface.NORMAL), 5, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
            depositService.setText(str.toString()
                    .replaceAll("【", "[").replaceAll("】", "]")
                    .replaceAll("（", "(").replaceAll("）", ")"));
        } else {
            depositService.setVisibility(View.GONE);
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        depositTitle = (TextView) findViewById(R.id.depositTitle);
        depositNum = (TextView) findViewById(R.id.depositNum);
        depositSKU = (TextView) findViewById(R.id.depositSKU);
        serviceDate = (TextView) findViewById(R.id.serviceDate);
        line = (View) findViewById(R.id.line);
        depositShops = (TextView) findViewById(R.id.depositShops);
        depositType = (TextView) findViewById(R.id.depositType);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        depositList = (RecyclerView) findViewById(R.id.depositList);
        layoutRefresh.setOnRefreshLoadMoreListener(this);

        vipDespositListAdapter = new VipDespositListAdapter();
        depositList.setLayoutManager(new LinearLayoutManager(mContext));
        vipDespositListAdapter.bindToRecyclerView(depositList);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        initView();
    }

    @Override
    public void onGetBalanceListSuccess(List<BalanceModel> list) {

    }

    @Override
    public void onGetListSuccess(List<com.healthy.library.model.VipDeposit> list, PageInfoEarly pageInfoEarly) {

    }

    @Override
    public void onGetDepositListSuccess(List<DepositList> list, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        showContent();
        page = pageInfoEarly.currentPage;
        if (page == 1) {
            vipDespositListAdapter.setNewData(list);
            if (list.size() == 0) {
                showEmpty();
            }
        } else {
            vipDespositListAdapter.addData(list);
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void onGetDepositGoodsSuccess() {

    }

    @Override
    public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
        page = 1;
        getData();
    }

    private void initView() {
        depositService = (TextView) findViewById(R.id.depositService);
    }
}