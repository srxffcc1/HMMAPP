package com.health.discount.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.discount.R;
import com.health.discount.adapter.FlashBuyActAdapter;
import com.health.discount.adapter.FlashBuyBannerAdapter;
import com.health.discount.contract.FlashBuyContract;
import com.health.discount.contract.MarketGoodsSpecContract;
import com.health.discount.presenter.FlashBuyPresenter;
import com.health.discount.presenter.MarketGoodsSpecPresenter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.DisGoodsSpecDialog;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.AdContract;
import com.healthy.library.contract.BasketCountContract;
import com.healthy.library.message.UpdateActTab;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.FlashBuyTab;
import com.healthy.library.model.PopDetailInfo;
import com.healthy.library.model.PopListInfo;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.BasketCountPresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FlashBuyFragment extends BaseFragment implements BasketCountContract.View, OnRefreshLoadMoreListener, BaseAdapter.OnOutClickListener
        , FlashBuyContract.View, MarketGoodsSpecContract.View, AdContract.View {

    public String popLabelID = null;
    public String popLabelName = null;
    private String appID = null;
    private String departID = null;
    private SmartRefreshLayout layout_refresh;
    private RecyclerView recycler;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private FlashBuyBannerAdapter flashBuyBannerAdapter;
    private FlashBuyActAdapter flashBuyActAdapter;
    private Map<String, Object> map = new HashMap<>();
    private FlashBuyPresenter flashBuyPresenter;
    private MarketGoodsSpecPresenter marketGoodsSpecPresenter;
    private AdPresenter adPresenter;
    private DisGoodsSpecDialog disGoodsSpecDialog;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private ConstraintLayout shopCartRel;
    private ImageView goSub;
    private TextView shopCartNum;
    private ImageView mallScrollUp;
    private BasketCountPresenter basketCountPresenter;
    private PopListInfo popListInfo;
    public void setPopLabelID(String popLabelID) {
        this.popLabelID = popLabelID;
    }


    public static FlashBuyFragment newInstance(String popLabelID, String appID, String departID, String popLabelName,ArrayList<PopListInfo> popListInfos) {
        FlashBuyFragment fragment = new FlashBuyFragment();
        Bundle args = new Bundle();
        args.putString("popLabelID", popLabelID);
        args.putString("appID", appID);
        args.putString("departID", departID);
        args.putString("popLabelName",popLabelName);
        args.putSerializable("popListInfos",popListInfos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            popLabelID = getArguments().getString("popLabelID");
            appID = getArguments().getString("appID");
            popLabelName=getArguments().getString("popLabelName");
            departID = getArguments().getString("departID");
            try {
                popListInfos= (ArrayList<PopListInfo>) getArguments().getSerializable("popListInfos");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("活动中心需要展示的TabZ:"+popLabelName+":"+popLabelID);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_flash_buy;
    }

    @Override
    protected void findViews() {
        initView();
        basketCountPresenter = new BasketCountPresenter(mContext, this);
        layout_refresh = findViewById(R.id.layout_refresh);
        recycler = findViewById(R.id.recycler);
        flashBuyPresenter = new FlashBuyPresenter(mContext, this);
        marketGoodsSpecPresenter = new MarketGoodsSpecPresenter(mContext, this);
        adPresenter = new AdPresenter(mContext, this);
        layout_refresh.setEnableLoadMore(false);
        buildRecyclerView();
    }
    @Override
    public void getData() {
        super.getData();
        basketCountPresenter.getShopCart();
        if (appID == null || departID == null || popLabelID == null) {
            showEmpty();
            return;
        }
        map.put("appID", appID);
        map.put("popLabelID", popLabelID);
        map.put("popNo", "");
        map.put("departID", departID);
        if(popListInfos!=null&&popListInfos.size()>0){
//            onSuccessGetGoodsList(null, popListInfos);
            flashBuyActAdapter.setData((ArrayList) popListInfos);
            System.out.println("直接展示页面");
            showContent();
        }else {

            System.out.println("请求展示页面"+popLabelName);
            showLoading();
            flashBuyPresenter.getActList(map);
        }
        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "20").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flashBuyPresenter.detach();
        basketCountPresenter.detach();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);
        recycler.setItemAnimator(null);
        layout_refresh.setOnRefreshLoadMoreListener(this);

        flashBuyBannerAdapter = new FlashBuyBannerAdapter();
        delegateAdapter.addAdapter(flashBuyBannerAdapter);
        flashBuyBannerAdapter.setOutClickListener(this);

        flashBuyActAdapter = new FlashBuyActAdapter();
        //flashBuyActAdapter.setMarketingType(marketingType);
        delegateAdapter.addAdapter(flashBuyActAdapter);
        flashBuyActAdapter.setOutClickListener(this);
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        layout_refresh.finishLoadMoreWithNoMoreData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        System.out.println("加载滞空");
        popListInfos=new ArrayList<>();
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layout_refresh.finishRefresh();
        layout_refresh.finishLoadMore();
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("marketingId".equals(function)) {
            ARouter.getInstance()
                    .build(DiscountRoutes.DIS_DISCOUNTLIST)
                    .withString("appID", appID)
                    .withString("popNo", (String) obj)
                    .withString("departID", departID)
                    .navigation();
        }

        if ("popListInfo".equals(function)) {
            popListInfo = (PopListInfo) obj;
        }
        if ("basket".equals(function)) {//加入购物车
            final PopDetailInfo.GoodsDTOListBean goodsDTOListBean = (PopDetailInfo.GoodsDTOListBean) obj;
            if (goodsDTOListBean != null) {
                if (disGoodsSpecDialog == null) {
                    disGoodsSpecDialog = DisGoodsSpecDialog.newInstance();
                }
                disGoodsSpecDialog.show(getChildFragmentManager(), "优惠券");
                if (goodsDTOListBean.goodsChildren != null && goodsDTOListBean.goodsChildren.size() > 0) {
                    disGoodsSpecDialog.setPopListInfo(popListInfo);
                    disGoodsSpecDialog.setSingleSelectAct(true);
                    disGoodsSpecDialog.setGoodsDetail(goodsDTOListBean);
                    disGoodsSpecDialog.initSpec(goodsDTOListBean.goodsChildren);
                    disGoodsSpecDialog.setOnSpecSubmitListener(new DisGoodsSpecDialog.OnSpecSubmitListener() {
                        @Override
                        public void onSpecSubmit(PopDetailInfo.GoodsDTOListBean.GoodsChildrenBean goodsSpecCell) {
                            flashBuyPresenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
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
        if ("goodsDetial".equals(function)) {
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_DETAIL)
                    .withString("id", (String) obj)
                    // .withString("marketingType", marketingType)
                    .withString("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                    .navigation();
        }
    }

    @Override
    public void onSuccessGetTabList(List<FlashBuyTab> result) {

    }

    @Override
    public void successAddShopCat(String result) {
        if (result.contains("购物车添加商品")) {
            showToast("已加入购物车");
            basketCountPresenter.getShopCart();
        }
    }

    public FlashBuyFragment setPopListInfos(ArrayList<PopListInfo> popListInfos) {
        this.popListInfos = popListInfos;
        return this;
    }

    ArrayList<PopListInfo> popListInfos;

    @Override
    public void onSuccessGetActList(List<PopListInfo> result) {
        if (result != null && result.size() > 0) {
            try {
                popListInfos = (ArrayList<PopListInfo>) result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < result.size(); i++) {
                result.get(i).adapterNumber = i;
            }
            onSuccessGetGoodsList(null, result);
            flashBuyActAdapter.setData((ArrayList) result);
        } else {
            showEmpty();
        }
    }

    @Override
    public void showEmpty() {
        EventBus.getDefault().post(new UpdateActTab(popLabelID,popLabelName,false));
        FlashBuyFragment.super.showEmpty();
        System.out.println("暂无数据:"+popLabelName);
    }

    @Override
    public void onSuccessGetYTBShopDetail(ActVip.VipShop actVip) {

    }

    @Override
    public void onSuccessGetGoodsList(PopListInfo popListInfo, List<PopListInfo> result) {
        if (flashBuyActAdapter != null) {
            if (popListInfo != null) {

            }
            if (result.size() >= 1) {
                new FlashBuyPresenter(mContext, this).getGoodsList(new SimpleHashMapBuilder<String, Object>()
                                .puts("appID", appID)
                                .puts("popNo", result.get(0).PopNo)
                                .puts("departID", departID)
                                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                                .puts("pageNum", "1")
                                .puts("pageSize", "6")
                        , result.get(0), result.subList(1, result.size()));
            } else {
                    showContent();

//                System.out.println("出现empty可能"+popListInfo.PopDesc);
                int hasGoodsCount = 0;
                for (int i = 0; i < popListInfos.size(); i++) {
                    try {
                        if (popListInfos.get(i).popDetail.goodsDTOList.size() > 0) {
                            hasGoodsCount++;
                        }
                    } catch (Exception e) {
                    }
                }
                if (hasGoodsCount <= 0) {
                    showEmpty();
                    System.out.println("出现empty了");
                }
            }
            try {
                int hasGoodsCount = 0;
                for (int i = 0; i < popListInfos.size(); i++) {
                    try {
                        if (popListInfos.get(i).popDetail.goodsDTOList.size() > 0) {
                            hasGoodsCount++;
                        }
                    } catch (Exception e) {
                    }
                }
                int popinitCoun=0;
                for (int i = 0; i < popListInfos.size(); i++) {
                    try {
                        if (popListInfos.get(i).popDetail!=null) {
                            popinitCoun++;
                        }
                    } catch (Exception e) {
                    }
                }
                if(popinitCoun>=1&&hasGoodsCount>0){//完全加载了
                    System.out.println("完全加载页面"+popLabelName);
                    EventBus.getDefault().post(new UpdateActTab(popLabelID,popLabelName,true,popListInfos));
                }
                if (hasGoodsCount >= 1) {//有商品的数量大于等于2时 刷新下页面
                        showContent();

                }
                try {
                    if (popListInfo != null && popListInfo.popDetail.goodsDTOList.size() > 0) {
                        flashBuyActAdapter.notifyItemChanged(popListInfo.adapterNumber);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSucessGetList() {
        if (flashBuyActAdapter != null) {
            flashBuyActAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {
        if (adModels == null || adModels.size() == 0) {
            flashBuyBannerAdapter.setData(new SimpleArrayListBuilder<ArrayList<AdModel>>());
            return;
        }
        List data = new ArrayList<>();
        data.clear();
        data.add(adModels);
        flashBuyBannerAdapter.setData((ArrayList) data);
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

    private void initView() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        shopCartRel = (ConstraintLayout) findViewById(R.id.shop_cart_rel);
        goSub = (ImageView) findViewById(R.id.goSub);
        shopCartNum = (TextView) findViewById(R.id.shop_cart_num);
        mallScrollUp = (ImageView) findViewById(R.id.mall_scrollUp);
        shopCartRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_BASKET)
                        .navigation();
            }
        });
    }
}