package com.health.tencentlive.activity;

import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.tencentlive.R;
import com.health.tencentlive.adapter.LiveFansAdapter;
import com.health.tencentlive.contract.LikeManContract;
import com.health.tencentlive.presenter.LikeManPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.LiveFans;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Route(path = TencentLiveRoutes.LiveUserManager)
public class LiveUserMainActivity extends BaseActivity implements BaseAdapter.OnOutClickListener, IsFitsSystemWindows, OnRefreshLoadMoreListener, LikeManContract.View {

    @Autowired
    String type;//我的粉丝 还是我的关注
    @Autowired
    String anchormanId;//主播ID

    private com.healthy.library.widget.TopBar topBar;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout refreshLayout;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private androidx.recyclerview.widget.RecyclerView recyclerview;
    private LiveFansAdapter liveFansAdapter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private int page = 1;
    LikeManPresenter likeManPresenter;
    private String createTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_hostfans;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        likeManPresenter = new LikeManPresenter(this, this);
        buildRecyclerView();
        refreshLayout.setOnRefreshLoadMoreListener(this);
        if ("0".equals(type)) {
            topBar.setTitle("我的关注");
        } else {
            topBar.setTitle("我的粉丝");
        }

        getData();
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);
        liveFansAdapter = new LiveFansAdapter();
        delegateAdapter.addAdapter(liveFansAdapter);
        liveFansAdapter.setOutClickListener(this);
    }

    @Override
    public void getData() {
        super.getData();
//        showContent();
//        List<String> emptylist = new ArrayList<>();
//        emptylist.clear();
//        for (int i = 0; i < 20; i++) {
//            emptylist.add("" + i);
//        }
//        if (page == 1 || page == 0) {
//            liveFansAdapter.setData((ArrayList<String>) emptylist);
//            onRequestFinish();
//        } else {
//            liveFansAdapter.addDatas((ArrayList<String>) emptylist);
//            onRequestFinish();
//        }
        likeManPresenter.getMineFans(new SimpleHashMapBuilder<String, Object>()
                .puts("createTime", createTime)
                .puts("pageSize", "10")
                .puts("followedType", "1")
                .puts("followedId", new String(Base64.decode(SpUtils.getValue(getApplicationContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
    }

    private void initView() {

        topBar = (TopBar) findViewById(R.id.top_bar);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        createTime = null;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onSucessGetFansStatus(boolean result) {

    }

    @Override
    public void onSucessLikeMan() {
        page = 1;
        createTime = null;
        getData();

    }

    @Override
    public void onSuccessGetMemberId(AnchormanInfo result) {
    }

    @Override
    public void onSucessGetFansList(List<LiveFans> fansList, PageInfoEarly pageInfoEarly) {

        if (fansList != null && fansList.size() > 0) {
            createTime = fansList.get(fansList.size() - 1).createTime;
        }
        if (page == 1 || page == 0) {
            liveFansAdapter.setData((ArrayList<LiveFans>) fansList);
            if (fansList.size() == 0) {
                liveFansAdapter.clear();
                showEmpty();
            }
        } else {
            liveFansAdapter.addDatas((ArrayList<LiveFans>) fansList);
        }
        if (pageInfoEarly.nextPage == 0) {
            //System.out.println("没有更多了");
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("关注".equals(function)) {
            LiveFans liveFans = (LiveFans) obj;
            likeManPresenter.likeMan(new SimpleHashMapBuilder<String, Object>()
                    .puts("fansId", new String(Base64.decode(SpUtils.getValue(getApplicationContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                    .puts("followedId", liveFans.fansId)
                    .puts("status", liveFans.fansStatus));
        }
    }
}
