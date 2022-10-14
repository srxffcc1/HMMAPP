package com.health.discount.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.discount.R;
import com.health.discount.adapter.NewUserListAdapter;
import com.health.discount.contract.NewUserListContract;
import com.health.discount.presenter.NewUserListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.AdContract;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.NewUserListModel;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.AdPresenterCopy;
import com.healthy.library.presenter.CardBoomPresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = DiscountRoutes.DIS_NEWUSERACTIVITY)
public class NewUserActivity extends BaseActivity implements OnRefreshLoadMoreListener, NewUserListContract.View, AdContract.View {
    //新客专享
    private TopBar topBar;
    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    private ImageView iv_topBanner;
    //private NewUserBannerAdapter newUserBannerAdapter;
    private NewUserListAdapter newUserListAdapter;
    private NewUserListPresenter presenter;
    private Map<String, Object> map = new HashMap<>();
    private int pageNum = 1;
    private AdPresenter adPresenter;
    AdPresenterCopy adPresenterCopy;
    private long mills = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_user;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        adPresenter = new AdPresenter(mContext, this);
        presenter = new NewUserListPresenter(this, this);
        adPresenterCopy = new AdPresenterCopy(this);
//        virtualLayoutManager = new VirtualLayoutManager(this);
//        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
//        recycler.setLayoutManager(virtualLayoutManager);
//        recycler.setAdapter(delegateAdapter);
//
//        newUserBannerAdapter = new NewUserBannerAdapter();
//        delegateAdapter.addAdapter(newUserBannerAdapter);

        newUserListAdapter = new NewUserListAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(newUserListAdapter);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        getData();
        new CardBoomPresenter(mContext).boom("4");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "新客专享浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000);
        MobclickAgent.onEvent(mContext, "event_APP_NewUserList_Stop", nokmap);
    }

    @Override
    public void getData() {
        super.getData();
        if (presenter != null) {
            map.put("pageNum", pageNum + "");
            map.put("pageSize", "10");
            map.put("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
            map.put("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
            map.put("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
            map.put("marketingType", "4");
            adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "14").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG)));
            presenter.getList(map);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.getIsNewAppMember();
        }
    }

    @Override
    public void onSucessGetList(List<NewUserListModel> result) {
        if (pageNum == 1) {
            if (result == null || result.size() == 0) {
                showEmpty();
            } else {
                newUserListAdapter.setData((ArrayList) result);
            }
        } else {
            if (result == null || result.size() == 0) {
                layoutRefresh.finishLoadMoreWithNoMoreData();
            } else {
                newUserListAdapter.addDatas((ArrayList) result);
            }
        }

    }

    @Override
    public void onSucessIsNewAppMember(int result) {
        if (result == 0 || result == 1) {
            newUserListAdapter.setResult(result);
        } else {
            showEmpty();
        }
    }

    @Override
    public void onSuccessAddRemind(String result, String type) {

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
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    protected void findViews() {
        super.findViews();
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        iv_topBanner = (ImageView) findViewById(R.id.iv_topBanner);
    }

    @Override
    public void onSucessGetAds(final List<AdModel> adModels) {
//        if (adModels != null && adModels.size() > 0) {
//            List list = new ArrayList();
//            list.add(adModels);
//            newUserBannerAdapter.setData((ArrayList) list);
//        } else {
//            newUserBannerAdapter.setData(new SimpleArrayListBuilder<ArrayList<AdModel>>());
//        }
        if (adModels.size() > 0 && adModels != null) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "各轮播图、广告图曝光量");
            MobclickAgent.onEvent(mContext, "event2APPShopBannerShow", nokmap);
            iv_topBanner.setVisibility(View.VISIBLE);
            com.healthy.library.businessutil.GlideCopy.with(mActivity)
                    .load(adModels.get(0).photoUrls)
                    .error(R.drawable.new_user_top_banner)
                    .placeholder(R.drawable.new_user_top_banner)
                    .into(iv_topBanner);
            iv_topBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "各轮播图、广告图曝光量");
                    MobclickAgent.onEvent(mContext, "event2APPShopBannerShow", nokmap);
                    AdModel adModel = adModels.get(0);
                    MARouterUtils.passToTarget(mContext,adModel);

                }
            });
        } else {
            iv_topBanner.setVisibility(View.GONE);
        }
    }
}