//package com.health.discount.activity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.health.discount.R;
//import com.health.discount.adapter.LiveListAdapter;
//import com.health.discount.contract.LiveListContract;
//import com.health.discount.model.LiveLinkModel;
//import com.health.discount.model.LiveListModel;
//import com.health.discount.presenter.LiveListPresenter;
//import com.healthy.library.base.BaseActivity;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.interfaces.IsFitsSystemWindows;
//import com.healthy.library.routes.DiscountRoutes;
//import com.healthy.library.routes.IndexRoutes;
//import com.healthy.library.utils.SpUtils;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Route(path = DiscountRoutes.DIS_LIVELIST)
//public class LiveListActivity extends BaseActivity implements IsFitsSystemWindows, LiveListContract.View, OnRefreshLoadMoreListener {
//
//    private ImageView img_back;
//    private RecyclerView recycler;
//    private SmartRefreshLayout layout_refresh;
//    private LiveListAdapter adapter;
//    private LiveListPresenter presenter;
//    private int pageNum = 1;
//    private Map<String, Object> map = new HashMap<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_live_list;
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        ARouter.getInstance().inject(this);
//        presenter = new LiveListPresenter(this, this);
//        adapter = new LiveListAdapter();
//        recycler.setLayoutManager(new LinearLayoutManager(this));
//        recycler.setAdapter(adapter);
//        layout_refresh.setOnRefreshLoadMoreListener(this);
//        getData();
//        adapterClick();
//    }
//
//    @Override
//    protected void findViews() {
//        super.findViews();
//        img_back = findViewById(R.id.img_back);
//        recycler = findViewById(R.id.recycler);
//        layout_refresh = findViewById(R.id.layout_refresh);
//        img_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//    }
//
//    @Override
//    public void getData() {
//        super.getData();
//        map.put("pageNum", pageNum + "");
//        map.put("pageSize", "10");
//        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
//        presenter.getLiveList(map);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onSucessGetLiveList(List<LiveListModel> result) {
//        showContent();
//        if (pageNum == 1) {
//            if (result == null || result.size() == 0) {
//                showEmpty();
//            } else {
//                adapter.setData((ArrayList) result);
//            }
//        } else {
//            if (result == null || result.size() == 0) {
//                layout_refresh.finishLoadMoreWithNoMoreData();
//            } else {
//                layout_refresh.setNoMoreData(false);
//                layout_refresh.setEnableLoadMore(true);
//                adapter.addDatas((ArrayList) result);
//            }
//        }
//    }
//
//    @Override
//    public void subVideoSucess() {
//        getData();
//    }
//
//    @Override
//    public void getLiveLinkSucess(LiveLinkModel liveLinkModel) {
//        if (liveLinkModel != null) {
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_WEBVIEW)
//                    .withString("title", "憨妈直播")
//                    .withBoolean("isinhome", false)
//                    .withBoolean("leftnow", true)
//                    .withBoolean("videoshop", true)
//                    .withString("url", liveLinkModel.miniProgramUrl)
//                    .navigation();
//        } else {
//            showToast("该直播未生成回放");
//        }
//    }
//
//    @Override
//    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//        pageNum++;
//        getData();
//    }
//
//    @Override
//    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        pageNum = 1;
//        getData();
//    }
//
//    @Override
//    public void onRequestFinish() {
//        super.onRequestFinish();
//        layout_refresh.finishRefresh();
//        layout_refresh.finishLoadMore();
//    }
//
//    private void adapterClick() {
//        adapter.setClickListener(new LiveListAdapter.ClickListener() {
//            @Override
//            public void outClick(String function, String data1, String data2) {
//                if ("remind".equals(function)) {
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("course_id", data1);
//                    presenter.subVideo(map);
//                }
//                if ("live".equals(function)) {
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("course_id", data1);
//                    map.put("nickname", SpUtils.getValue(mContext, SpKey.USER_NICK));
//                    map.put("liveStatus", data2);
//                    presenter.getLiveLink(map);
//                }
//            }
//        });
//    }
//}