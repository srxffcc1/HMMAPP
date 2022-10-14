package com.health.servicecenter.activity;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallGoodsItemServiceAdapter;
import com.health.servicecenter.contract.ServiceGoodsServiceContract;
import com.healthy.library.model.RecommendList;
import com.health.servicecenter.presenter.ServiceGoodsSerivicePresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_GOODS_SERVICE)
public class ServiceGoodsService extends BaseActivity implements IsFitsSystemWindows,IsNeedShare, ServiceGoodsServiceContract.View, OnRefreshLoadMoreListener {


    private TopBar topBar;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerList;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    ServiceGoodsSerivicePresenter serviceGoodsDetailPresenter;


    int page=1;
    Map<String,Object> recommandMap=new HashMap<>();

    private MallGoodsItemServiceAdapter mallGoodsItemServiceAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_normal_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        serviceGoodsDetailPresenter=new ServiceGoodsSerivicePresenter(this,this);


        recommandMap.put("type","4");
        recommandMap.put("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
        recommandMap.put("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        recommandMap.put("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        recommandMap.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        recommandMap.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        recommandMap.put("shopId", SpUtils.getValue(mContext,SpKey.CHOSE_SHOP));
        recommandMap.put("pageNum",page+"");
        recommandMap.put("pageSize","10");

        buildRecyclerView();
        topBar.setTitle("服务项目推荐");
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
        serviceGoodsDetailPresenter.getGoodsRecommend(recommandMap);
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

        mallGoodsItemServiceAdapter = new MallGoodsItemServiceAdapter();
        delegateAdapter.addAdapter(mallGoodsItemServiceAdapter);
    }


    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerList = (RecyclerView) findViewById(R.id.recycler_list);
    }

    @Override
    public String getSurl() {
        return null;
    }

    @Override
    public String getSdes() {
        return null;
    }

    @Override
    public String getStitle() {
        return null;
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
    public void successGetGoodsRecommend(List<RecommendList> result) {
        if(result==null){
            return;
        }
        if(result.size()==0){
            ////System.out.println("目前推荐的长度");
            layoutRefresh.finishLoadMoreWithNoMoreData();
            if(page==1||page==0){
//                mallGoodsTitleAdapter.setModel(null);
            }
        }else {
            if(page==1){
                mallGoodsItemServiceAdapter.setData((ArrayList<RecommendList>) result);
                ////System.out.println("目前推荐的长度设置数据");
            }else {
                mallGoodsItemServiceAdapter.addDatas((ArrayList<RecommendList>) result);
            }
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        recommandMap.put("pageNum",page+"");
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page=1;
        recommandMap.put("pageNum",page+"");
        getData();
    }
}
