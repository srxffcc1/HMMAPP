package com.health.second.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.second.R;
import com.health.second.adapter.SecondActShopsBlockAdapter;
import com.health.second.contract.SecondShopsBlockContract;
import com.health.second.presenter.SecondShopsBlockPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = SecondRoutes.MAIN_SHOPBLOCK)
public class SecondActShopsBlockActivity extends BaseActivity implements TextView.OnEditorActionListener,OnRefreshLoadMoreListener, IsFitsSystemWindows, SecondShopsBlockContract.View {

    @Autowired
    String shopId = "";
    @Autowired
    String blockId = "";
    @Autowired
    String blockName = "";

    private StatusLayout layoutStatus;
    private SmartRefreshLayout refreshLayout;
    private LinearLayout seachTopBgLL;
    private LinearLayout seachTop;
    private LinearLayout serarchKeyWordLL;
    private LinearLayout seachTopTmp;
    private RecyclerView recyclerview;

    private int mCurrentPage = 1;
    private Map<String, Object> mParams = new HashMap();
    private List<MainBlockModel.AllianceMerchant> mData = new ArrayList<>();
    private SecondActShopsBlockAdapter mShopsAdapter;
    private SecondShopsBlockPresenter mShopsBlockPresenter;
    private List<MainBlockModel> orglist;
    private View topView;
    private androidx.constraintlayout.widget.ConstraintLayout topContent;
    private com.healthy.library.widget.TopBar topBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_block_shop;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mShopsBlockPresenter = new SecondShopsBlockPresenter(mContext, this);
        topBar.setTitle(blockName);
        getData();
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        buildRecyclerHelper();
        initListener();
    }

    private void initView() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.finishLoadMoreWithNoMoreData();
        seachTopBgLL = (LinearLayout) findViewById(R.id.seachTopBgLL);
        seachTop = (LinearLayout) findViewById(R.id.seachTop);
        serarchKeyWordLL = (LinearLayout) findViewById(R.id.serarchKeyWordLL);
        seachTopTmp = (LinearLayout) findViewById(R.id.seachTopTmp);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        topView = (View) findViewById(R.id.topView);
        topContent = (ConstraintLayout) findViewById(R.id.topContent);
        topBar = (TopBar) findViewById(R.id.top_bar);
    }

    private void buildRecyclerHelper() {
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mShopsAdapter = new SecondActShopsBlockAdapter();
        mShopsAdapter.setNewData(mData);
        recyclerview.setAdapter(mShopsAdapter);
    }

    private void initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    public void getData() {
        super.getData();
        mParams.clear();
        mParams.put("shopId", shopId);
        mParams.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        mParams.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        mParams.put("themeType", "6");
        mParams.put("exhibition", "7");
        mShopsBlockPresenter.getBlockList(mParams);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage = 1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMoreWithNoMoreData();
    }

    @Override
    public void onSuccessGetBlockList(@Nullable List<MainBlockModel> list) {
        if (mCurrentPage == 1) {
            mData.clear();
        }
        orglist=list;

        List<MainBlockModel> realList=ListUtil.where(list, new ListUtil.Where<MainBlockModel>() {
            @Override
            public boolean where(MainBlockModel obj) {
                return blockId.equals(obj.id);
            }
        });
        if (!ListUtil.isEmpty(realList)) {
            for (int i = 0; i < realList.size(); i++) {
                MainBlockModel item = realList.get(i);
                if (ListUtil.isEmpty(item.detailList)) {
                    List<MainBlockModel.AllianceMerchant> allianceMerchantList=item.getRealAllianceMerchantList();
                    if (allianceMerchantList != null && allianceMerchantList.size() > 0) {
                        for (int j = 0; j < allianceMerchantList.size(); j++) {
                            MainBlockModel.AllianceMerchant allianceMerchant=allianceMerchantList.get(j);
                            mParams.clear();
                            try {
                                mParams.put("themeId", item.id);
                                mParams.put("pageNum", mCurrentPage);
                                mParams.put("pageSize", "10");
                                mParams.put("shopId", allianceMerchant.shopDto.id);
                                mParams.put("mapThemeAllianceMerchantId", allianceMerchant.id);
                                mShopsBlockPresenter.getGoodsList(mParams, item, allianceMerchant, i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mData.addAll(allianceMerchantList);
                    }
                }
            }
        }
        if (ListUtil.isEmpty(mData)) {
            showEmpty();
        }
    }

    @Override
    public void onSuccessGetList(@NotNull MainBlockModel item, final int position) {
        mShopsAdapter.setNeeduseNewmap(item);
        mShopsAdapter.notifyDataSetChanged();
//        Observable.intervalRange(0, 1, 250, 0, TimeUnit.MILLISECONDS, Schedulers.io())
//                .to(RxLifecycleUtils.<Long>bindLifecycle(this))
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {
//                        Log.e("ShopsBlock", "onNext: " + aLong);
//                    }
//
//                    @Override
//                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        if(ListUtil.isEmpty(mShopsAdapter.getData())) {
//                            mShopsAdapter.notifyDataSetChanged();
//                        } else {
//                            try {
//                                mShopsAdapter.notifyItemChanged(position);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
