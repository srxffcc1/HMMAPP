package com.health.servicecenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.google.gson.Gson;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallGoodsBasketDiscountAdapter;
import com.health.servicecenter.adapter.MallGoodsBasketDiscountGiftGoodsAdapter;
import com.health.servicecenter.adapter.MallGoodsBasketDiscountTopGoodsAdapter;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.ActVipContract;
import com.healthy.library.contract.ActVipOnlineContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.ActVipDefault;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.presenter.ActVipOnlinePresenter;
import com.healthy.library.presenter.ActVipPresenter;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Route(path = ServiceRoutes.SERVICE_BASKET_DISCOUNT)
public class ServiceGoodsBasketDiscountActivity extends BaseActivity implements ActVipOnlineContract.View, BaseAdapter.OnOutClickListener, ActVipContract.View, IsFitsSystemWindows {
    @Autowired
    ActVip actVipReq;
    @Autowired
    ActVip.VipShop vipShop;

    ActVip actVipResult;

    @Autowired
    List<GoodsBasketCell> goodsbasketlist;

    Map<String, String> imageMap = new HashMap<>();

    private TopBar topBar;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerList;
    private ImageView ivBottomShader;
    private LinearLayout basketBottom;
    private LinearLayout basketOrderBlock;
    private TextView checkDelete;
    private TextView checkOrder;
    private DelegateAdapter delegateAdapter;
    private VirtualLayoutManager virtualLayoutManager;
    MallGoodsBasketDiscountTopGoodsAdapter mallGoodsBasketDiscountTopGoodsAdapter;
    MallGoodsBasketDiscountGiftGoodsAdapter mallGoodsBasketDiscountGiftGoodsAdapter;
    MallGoodsBasketDiscountAdapter mallGoodsBasketDiscountAdapter;
    ActVipPresenter actVipPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_basket_discountall;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        buildRecyclerView();
        layoutRefresh.setEnableLoadMore(false);
        layoutRefresh.setEnableRefresh(false);
        actVipPresenter = new ActVipPresenter(this, this);
        actVipResult = actVipReq;
        actVipResult.setPopDetailFindInSales();
        for (int i = 0; i < actVipResult.PopInfo.size(); i++) {
            ActVip.PopInfo popInfo = actVipResult.PopInfo.get(i);
            Collections.sort(popInfo.PopDetail);
        }
        getData();
        checkOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra("actVipResult", getIntentResult());
                setResult(RESULT_OK, result);
                finish();
            }
        });
        checkDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();

            }
        });
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerList.setLayoutManager(virtualLayoutManager);
        recyclerList.setAdapter(delegateAdapter);

        mallGoodsBasketDiscountTopGoodsAdapter = new MallGoodsBasketDiscountTopGoodsAdapter();
        delegateAdapter.addAdapter(mallGoodsBasketDiscountTopGoodsAdapter);
        mallGoodsBasketDiscountTopGoodsAdapter.setImageMap(imageMap);
        mallGoodsBasketDiscountGiftGoodsAdapter = new MallGoodsBasketDiscountGiftGoodsAdapter();
        delegateAdapter.addAdapter(mallGoodsBasketDiscountGiftGoodsAdapter);
        mallGoodsBasketDiscountGiftGoodsAdapter.setImageMap(imageMap);
        mallGoodsBasketDiscountAdapter = new MallGoodsBasketDiscountAdapter();
        delegateAdapter.addAdapter(mallGoodsBasketDiscountAdapter);
        mallGoodsBasketDiscountAdapter.setImageMap(imageMap);
        mallGoodsBasketDiscountAdapter.setOutClickListener(this);

    }

    public String getIntentResult() {
        return new Gson().toJson(actVipResult);
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerList = (RecyclerView) findViewById(R.id.recycler_list);
        ivBottomShader = (ImageView) findViewById(R.id.iv_bottom_shader);
        basketBottom = (LinearLayout) findViewById(R.id.basketBottom);
        basketOrderBlock = (LinearLayout) findViewById(R.id.basketOrderBlock);
        checkDelete = (TextView) findViewById(R.id.checkDelete);
        checkOrder = (TextView) findViewById(R.id.checkOrder);
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("促销计算".equals(function)) {
            actVipResult.setVipShop(vipShop);
            actVipResult.Command = "pcPreCalcPop";
            actVipPresenter.getVipActs(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "YT_100002")
                    .putObject(actVipResult.sortAndExpZ(TextUtils.isEmpty(actVipResult.IsDelPop) ? "S" : "S")), null);
        }
        if ("刷新礼物".equals(function)) {
            getData();
        }
        if ("重置活动".equals(function)) {
            actVipResult.setVipShop(vipShop);
            actVipResult.Command = "pcPreCalcPop";
            actVipPresenter.getVipActs(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "YT_100002")
                    .putObject(actVipResult.sortAndExpZ("R")), null);
        }
        if ("查看更多".equals(function)) {
            ActVip.PopInfo popInfo = (ActVip.PopInfo) obj;
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_BASKET_DISCOUNT_SINGLE)
                    .withObject("popInfo", popInfo)
                    .withObject("vipShop", vipShop)
                    .navigation(this, 1007);
        }
        if ("促销计算初始".equals(function)) {
            actVipResult.Command = "pcPreCalcPop";
            actVipResult.setVipShop(vipShop);
            actVipPresenter.getVipActs(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "YT_100002")
                    .putObject(actVipResult.sortAndExpZ("S")), null);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                String dataResult = data.getStringExtra("popInfo");
                ActVip.PopInfo popInfo = ObjUtil.getObj(dataResult, ActVip.PopInfo.class);
                if (popInfo != null) {
                    Collections.sort(popInfo.PopDetail);
                    boolean isHasThisPop = actVipResult.checkHasThisPop(popInfo);
                    actVipResult.setPopInfoGoods(popInfo);
                    if (!isHasThisPop) {
                        outClick("促销计算", null);
                    } else {
                        actVipResult.setPopDetailFindInSales();
                        for (int i = 0; i < actVipResult.PopInfo.size(); i++) {
                            ActVip.PopInfo popInfoTmp = actVipResult.PopInfo.get(i);
                            Collections.sort(popInfoTmp.PopDetail);
                        }
                        outClick("刷新礼物", null);
                    }
                }
            }
        }
    }

    @Override
    public void getData() {
        super.getData();
        actVipResult.setPopDetailFindInSales();
        mallGoodsBasketDiscountTopGoodsAdapter.clear();
        mallGoodsBasketDiscountTopGoodsAdapter.setModel(actVipResult);
        mallGoodsBasketDiscountGiftGoodsAdapter.clear();
        if (actVipResult.getGoodsListWithGift().size() > 0) {
            mallGoodsBasketDiscountGiftGoodsAdapter.setModel(actVipResult);
        } else {
            mallGoodsBasketDiscountGiftGoodsAdapter.setModel(null);
        }
        mallGoodsBasketDiscountAdapter.clear();
        mallGoodsBasketDiscountAdapter.setModel(actVipResult);
        for (int i = 0; i < actVipResult.SaleInfo.size(); i++) {
            if(imageMap.get(actVipResult.SaleInfo.get(i).getGoodsID())==null){
//                imageMap.put(actVipResult.SaleInfo.get(i).getGoodsID(),"");
                new ActVipOnlinePresenter(this, this).getVipOnlineGoodsWithVipSale(new SimpleHashMapBuilder<String, Object>()
                                .putObject(new ActVipDefault(actVipResult.SaleInfo.get(i).getGoodsID(),
                                        vipShop.shopId,
                                        "",
                                        actVipResult.SaleInfo.get(i).GoodsName,
                                        SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC),
                                        "1",
                                        new ActVipDefault.GoodsChildren(actVipResult.SaleInfo.get(i).getGoodsID(),
                                                actVipResult.SaleInfo.get(i).SalePrice,
                                                actVipResult.SaleInfo.get(i).Price
                                        ), new ActVipDefault.GoodsFiles()))
                        , actVipResult.SaleInfo.get(i));
            }



        }
        for (int i = 0; i < actVipResult.PopInfo.size(); i++) {
            for (int j = 0; j < actVipResult.PopInfo.get(i).PopDetail.size(); j++) {
                if(imageMap.get(actVipResult.PopInfo.get(i).PopDetail.get(j).getGoodsID())==null){
//                    imageMap.put(actVipResult.PopInfo.get(i).PopDetail.get(j).getGoodsID(),"");
                    new ActVipOnlinePresenter(this, this).getVipOnlineGoodsWithVipCell(new SimpleHashMapBuilder<String, Object>()
                                    .putObject(new ActVipDefault(actVipResult.PopInfo.get(i).PopDetail.get(j).getGoodsID(),
                                            vipShop.shopId,
                                            "",
                                            actVipResult.PopInfo.get(i).PopDetail.get(j).GoodsName,
                                            SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC),
                                            "1",
                                            new ActVipDefault.GoodsChildren(actVipResult.PopInfo.get(i).PopDetail.get(j).getGoodsID(),
                                                    "0",
                                                    "0"
                                            ), new ActVipDefault.GoodsFiles()))
                            , actVipResult.PopInfo.get(i).PopDetail.get(j));
                }

            }
        }
        notifyDataSetChanged();

    }

    public void notifyDataSetChanged() {
        mallGoodsBasketDiscountTopGoodsAdapter.notifyDataSetChanged();
        mallGoodsBasketDiscountGiftGoodsAdapter.notifyDataSetChanged();
        mallGoodsBasketDiscountAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSucessGetVipActs(ActVip actVip, GoodsBasketStore goodsBasketStore) {
        this.actVipResult = actVip;
        actVipResult.setPopDetailFindInSales();
        for (int i = 0; i < actVipResult.PopInfo.size(); i++) {
            ActVip.PopInfo popInfo = actVipResult.PopInfo.get(i);
            Collections.sort(popInfo.PopDetail);
        }
        getData();
    }

    public void onSucessGetVipActsOrder(ActVip actVip) {
        this.actVipResult = actVip;
        this.actVipResult.setPopDetailFindInSales();
        for (int i = 0; i < actVipResult.PopInfo.size(); i++) {
            Collections.sort(actVipResult.PopInfo.get(i).PopDetail);
        }
        getData();
    }

    @Override
    public void onSucessGetVipShopDetail(ActVip.VipShop actVip) {

    }

    @Override
    public void onSucessGetVipOnlineGoods() {
        notifyDataSetChanged();
    }
}
