package com.health.servicecenter.activity;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallGoodsSetAdapter;
import com.health.servicecenter.contract.ServiceGoodsSetSpecContract;
import com.health.servicecenter.presenter.ServiceGoodsSetSkuPresenter;
import com.health.servicecenter.widget.ShopOrderPickDialog;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.model.GoodsSetAll;
import com.healthy.library.model.GoodsSetCell;
import com.healthy.library.model.GoodsShop;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.business.GoodsSpecDialog;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_GOODS_SET)
public class ServiceGoodsSet extends BaseActivity implements MallGoodsSetAdapter.OnShopClickListener,MallGoodsSetAdapter.OnShopAllClickListener,MallGoodsSetAdapter.OnSpecAllClickListener,IsFitsSystemWindows,IsNeedShare,ServiceGoodsSetSpecContract.View, MallGoodsSetAdapter.OnSpecClickListener, OnRefreshLoadMoreListener {


    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private androidx.recyclerview.widget.RecyclerView recyclerList;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    ServiceGoodsSetSkuPresenter serviceGoodsDetailPresenter;

    MallGoodsSetAdapter mallGoodsSetAdapter;

    @Autowired
    String name;
    @Autowired
    String id;
    @Autowired
    String shopId;
    GoodsSpecDialog goodsSpecDialog;

    ShopOrderPickDialog shopOrderPickDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_normal_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        serviceGoodsDetailPresenter=new ServiceGoodsSetSkuPresenter(this,this);
//        layoutRefresh.setEnableRefresh(false);
        layoutRefresh.setEnableLoadMore(false);
        buildRecyclerView();
        topBar.setTitle("组合套餐");
//        topBar.setSubmitListener(new OnSubmitListener() {
//            @Override
//            public void onSubmitBtnPressed() {
//                showShare();
//            }
//        });
//        topBar.getSubmitBack().setImageResource(R.drawable.index_ic_share);
//        topBar.getSubmitBack().setVisibility(View.VISIBLE);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();

        serviceGoodsDetailPresenter.getGoodsSet(new SimpleHashMapBuilder<String, Object>().puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)).puts("goodsId",id));
        serviceGoodsDetailPresenter.getStoreList(shopId);
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerList.setLayoutManager(virtualLayoutManager);
        recyclerList.setAdapter(delegateAdapter);


        mallGoodsSetAdapter=new MallGoodsSetAdapter();
        delegateAdapter.addAdapter(mallGoodsSetAdapter);
        mallGoodsSetAdapter.setOnSpecClickListener(this);
        mallGoodsSetAdapter.setOnSpecAllClickListener(this);
        mallGoodsSetAdapter.setOnShopAllClickListener(this);
        mallGoodsSetAdapter.setOnShopClickListener(this);
    }

    @Override
    public void successGetGoodsSkuResult(GoodsSetCell goodsSetCell,List<GoodsSpecDetail> result) {
        if(result!=null&&result.size()>0){
            goodsSetCell.skuId=result.get(0).id+"";
            goodsSetCell.skuName=result.get(0).goodsSpecStr;
        }

    }

    @Override
    public void successGetGoodsSet(List<GoodsSetAll> goodsSetCellList) {
        mallGoodsSetAdapter.setData((ArrayList<GoodsSetAll>) goodsSetCellList);
    }

    List<ShopDetailModel> storeDetialModelList;
    @Override
    public void onGetStoreListSuccess(List<ShopDetailModel> storeDetialModelList) {
        this.storeDetialModelList=storeDetialModelList;
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerList = (RecyclerView) findViewById(R.id.recycler_list);
    }

    @Override
    public void onSpecClick(GoodsSetCell goodsSetCell, String goodsId, Map<String,Object> specValue) {
        serviceGoodsDetailPresenter.getGoodsSkuResult(goodsSetCell,new SimpleHashMapBuilder<String, Object>()
                .puts("goodsId", goodsId)
                .puts("specValue", specValue));
    }

    @Override
    public String getSurl() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
        String url = String.format("%s?type=8&scheme=%s&goodsId=%s", urlPrefix, "NormGoodsPreGroupDetail",id);
        return url;
    }

    @Override
    public String getSdes() {
        return "我在憨妈妈发现了一个不错的商品，赶快来看看吧。";
    }

    @Override
    public String getStitle() {
        return name;
    }

    @Override
    public Bitmap getsBitmap() {
        return null;
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            getData();
    }

    private void showSku(String goodsId, GoodsSpecDetail goodsSpecDetail, final GoodsSetCell goodsSetCell) {
        if (goodsSpecDialog == null) {
            goodsSpecDialog = GoodsSpecDialog.newInstance();
        }
        goodsSpecDialog.setGoodsId(goodsId + "",shopId);
        goodsSpecDialog.setGoodsSpecDetail(goodsSpecDetail);
        goodsSpecDialog.setSelectSku(goodsSetCell.skuName);
        goodsSpecDialog.initSpec(goodsSetCell.getSpecCell());
        goodsSpecDialog.setSet(true);
        goodsSpecDialog.setOnSpecSubmitListener(new GoodsSpecDialog.OnSpecSubmitListener() {
            @Override
            public void onSpecSubmit(GoodsSpecDetail goodsSpecDetail) {
                goodsSetCell.platformPrice=goodsSpecDetail.platformPrice;
                goodsSetCell.skuId=goodsSpecDetail.id+"";
                goodsSetCell.skuName=goodsSpecDetail.goodsSpecStr;
                mallGoodsSetAdapter.notifyDataSetChanged();
            }
        });
        goodsSpecDialog.show(getSupportFragmentManager(),"套餐");
    }

    @Override
    public void onSpecAllClick(GoodsSetCell goodsSetCell, String goodsId, Map<String, Object> specValue) {
        GoodsSpecDetail goodsSpecDetail = new GoodsSpecDetail(0, goodsSetCell.platformPrice, goodsSetCell.filePath,goodsSetCell.type,1,1,false,"0");
        showSku(goodsId,goodsSpecDetail,goodsSetCell);
    }

    @Override
    public void onShopClick(GoodsSetCell goodsSetCell, String goodsId, Map<String, Object> specValue) {

    }

    @Override
    public void onShopAllClick(GoodsSetCell goodsSetCell, String goodsId, Map<String, Object> specValue) {
        GoodsSpecDetail goodsSpecDetail = new GoodsSpecDetail(0, goodsSetCell.platformPrice, goodsSetCell.filePath,goodsSetCell.type,1,1,false,"0");
        showShop(goodsId,goodsSpecDetail,goodsSetCell);
    }

    private void showShop(String goodsId, final GoodsSpecDetail goodsSpecDetail, final GoodsSetCell goodsSetCell) {
        if (shopOrderPickDialog == null) {
            shopOrderPickDialog = ShopOrderPickDialog.newInstance();
        }
        try {
            shopOrderPickDialog.setSelectId(goodsSetCell.goodsShopId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        shopOrderPickDialog.show(getSupportFragmentManager(), "选择门店");
        shopOrderPickDialog.setGoodsShopList(buildFlashList(new SimpleArrayListBuilder<GoodsShop>().putList(storeDetialModelList, new ObjectIteraor<ShopDetailModel>() {
            @Override
            public GoodsShop getDesObj(ShopDetailModel o) {
                return new GoodsShop(o);
            }
        }), goodsSetCell.shopIdList));
        shopOrderPickDialog.setTitle("选择门店");
        shopOrderPickDialog.setOnDialogShopClickListener(new ShopOrderPickDialog.OnDialogShopClickListener() {
            @Override
            public void onDialogShopClick(GoodsShop goodsShop) {
                ShopDetailModel shopDetailModelNow = new ShopDetailModel(goodsShop);
//                goodsShopId = newStoreDetialModelSelect.id;
//                mallGoodsDetialStoreAdapter.setModel(newStoreDetialModelSelect);
//                mallGoodsDetialStoreAdapter.notifyDataSetChanged();
//                serviceGoodsDetailPresenter.getStoreDetial(goodsShopId);
                goodsSetCell.goodsShopId= shopDetailModelNow.id+"";
                goodsSetCell.goodsShopName= shopDetailModelNow.shopName;
                mallGoodsSetAdapter.notifyDataSetChanged();
            }
        });
    }
    private List<GoodsShop> buildFlashList(List<GoodsShop> goodsShopServiceList, String[] shopIdList) {//获取门店过滤列表

        List<GoodsShop> result = new ArrayList<>();
        for (int i = 0; i < goodsShopServiceList.size(); i++) {
            GoodsShop goodsShop = goodsShopServiceList.get(i);
            if (checkNowServiceShopIsRealy(goodsShop.shopId, shopIdList)) {
                result.add(goodsShopServiceList.get(i));
            }
        }
        return result;
    }
    private boolean checkNowServiceShopIsRealy(String shopId, String[] shopIdList) {
        if (shopIdList == null || shopIdList.length == 0) {
            return true;
        }
        for (int i = 0; i < shopIdList.length; i++) {
            if (shopId.equals(shopIdList[i])) {
                return true;
            }
        }
        return false;
    }
}
