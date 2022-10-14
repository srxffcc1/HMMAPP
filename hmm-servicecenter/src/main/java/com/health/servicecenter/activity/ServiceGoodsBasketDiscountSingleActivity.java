package com.health.servicecenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.google.gson.Gson;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallGoodsBasketDiscountSingleStoreAdapter;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.ActVipOnlineContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.ActVipDefault;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.presenter.ActVipOnlinePresenter;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.jetbrains.annotations.NotNull;

@Route(path = ServiceRoutes.SERVICE_BASKET_DISCOUNT_SINGLE)
public class ServiceGoodsBasketDiscountSingleActivity extends BaseActivity  implements ActVipOnlineContract.View,BaseAdapter.OnOutClickListener, IsFitsSystemWindows {
    @Autowired
    ActVip.PopInfo popInfo;
    @Autowired
    ActVip.VipShop vipShop;
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private androidx.recyclerview.widget.RecyclerView recyclerList;
    private android.widget.ImageView ivBottomShader;
    private android.widget.LinearLayout basketBottom;
    private android.widget.TextView discountNowCount;
    private android.widget.LinearLayout basketOrderBlock;
    private android.widget.TextView checkDelete;
    private android.widget.TextView checkOrder;
    private DelegateAdapter delegateAdapter;
    private VirtualLayoutManager virtualLayoutManager;
    MallGoodsBasketDiscountSingleStoreAdapter mallGoodsBasketDiscountSingleStoreAdapter;
    GoodsBasketStore goodsBasketStore;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_basket_discounsingle;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        buildRecyclerView();
        layoutRefresh.setEnableRefresh(false);
        layoutRefresh.setEnableLoadMore(false);
        goodsBasketStore=popInfo.getBasketStore();
        checkOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                String resultString=getIntentResult();
                if(resultString==null){
                    finish();
                }else {
                    result.putExtra("popInfo",getIntentResult());
                    setResult(RESULT_OK,result);
                    finish();
                }

            }
        });
        checkDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
        outClick("展示底部",null);
    }
    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerList.setLayoutManager(virtualLayoutManager);
        recyclerList.setAdapter(delegateAdapter);

        mallGoodsBasketDiscountSingleStoreAdapter = new MallGoodsBasketDiscountSingleStoreAdapter();
        delegateAdapter.addAdapter(mallGoodsBasketDiscountSingleStoreAdapter);
        mallGoodsBasketDiscountSingleStoreAdapter.setOutClickListener(this);

    }

    @Override
    public void getData() {
        super.getData();
        mallGoodsBasketDiscountSingleStoreAdapter.setModel(goodsBasketStore);
        for (int i = 0; i < goodsBasketStore.getGoodsBasketCellList().size(); i++) {
            new ActVipOnlinePresenter(this,this).getVipOnlineGoodsWithCell(new SimpleHashMapBuilder<String, Object>()
                    .putObject(new ActVipDefault(goodsBasketStore.getGoodsBasketCellList().get(i).getGoodsBarCode(),
                            vipShop.shopId,
                            "",
                            goodsBasketStore.getGoodsBasketCellList().get(i).goodsTitle,
                            SpUtils.getValue(LibApplication.getAppContext(),SpKey.CHOSE_MC),
                            goodsBasketStore.getGoodsBasketCellList().get(i).goodsType,
                            new ActVipDefault.GoodsChildren(goodsBasketStore.getGoodsBasketCellList().get(i).getGoodsBarCode(),
                                    goodsBasketStore.getGoodsBasketCellList().get(i).curGoodsAmount,
                                    goodsBasketStore.getGoodsBasketCellList().get(i).curGoodsRetailAmount
                                    ),new ActVipDefault.GoodsFiles()))
                    ,goodsBasketStore.getGoodsBasketCellList().get(i));
        }

    }
    public void notifyDataSetChanged(){
        mallGoodsBasketDiscountSingleStoreAdapter.notifyDataSetChanged();
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
        discountNowCount = (TextView) findViewById(R.id.discountNowCount);
        basketOrderBlock = (LinearLayout) findViewById(R.id.basketOrderBlock);
        checkDelete = (TextView) findViewById(R.id.checkDelete);
        checkOrder = (TextView) findViewById(R.id.checkOrder);
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if("展示底部".equals(function)){
            discountNowCount.setVisibility(View.INVISIBLE);
            if("1".equals(popInfo.getSelMode())){
                discountNowCount.setVisibility(View.VISIBLE);
                discountNowCount.setText("已选赠品"+goodsBasketStore.getSelectCount()+"件，"+"可选"+popInfo.SelMaxNum+"件");
            }else if("2".equals(popInfo.getSelMode())){
                discountNowCount.setVisibility(View.VISIBLE);
                discountNowCount.setText("已选赠品"+goodsBasketStore.getSelectLine()+"种，"+"可选"+1+"种");
            }else {
                discountNowCount.setVisibility(View.INVISIBLE);
//                discountNowCount.setText("已选赠品"+goodsBasketStore.getSelectCount()+"件，"+"可选"+popInfo.PopDetail.size()+"件");
            }

        }
    }
    public String getIntentResult(){
        popInfo.PopDetail=mallGoodsBasketDiscountSingleStoreAdapter.getPopDetails();
        return new Gson().toJson(popInfo);
    }

    @Override
    public void onSucessGetVipOnlineGoods() {
        notifyDataSetChanged();
    }
}
