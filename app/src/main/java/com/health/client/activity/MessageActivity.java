package com.health.client.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.client.R;
import com.health.client.adapter.MessageAdapter;
import com.health.client.adapter.MessagePAdapter;
import com.health.client.contract.MessageContract;
import com.health.client.presenter.MessagePresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.message.UpdateMessageInfo;
import com.healthy.library.model.MonMessage;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.utils.NotificationUtil;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = AppRoutes.APP_MESSAGE)
public class MessageActivity extends BaseActivity implements MessageContract.View, OnRefreshLoadMoreListener {
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerNews;
    private MessagePresenter messagePresenter;
    private MessageAdapter messageAdapter;
    private com.healthy.library.widget.TopBar topBar;
    private android.widget.TextView passOpenMessage;
    private android.widget.ImageView passCloseMessage;
    private ConstraintLayout topMessageLL;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private MessagePAdapter adpater1;
    private MessagePAdapter adpater2;
    private MessagePAdapter adpater3;

    @Override
    public void onSuccessGetMessage(List<MonMessage> results) {

//        if(results.size()==0){
//            showEmpty();
//        }
//        messageAdapter.setNewData(results);

        adpater1.setData(new SimpleArrayListBuilder<ArrayList<MonMessage>>().adds(new SimpleArrayListBuilder<MonMessage>().getChildList(results,0,2)));
        adpater2.setData(new SimpleArrayListBuilder<ArrayList<MonMessage>>().adds(new SimpleArrayListBuilder<MonMessage>().getChildList(results,3,5)));
        adpater3.setData(new SimpleArrayListBuilder<ArrayList<MonMessage>>().adds(new SimpleArrayListBuilder<MonMessage>().getChildList(results,6,7)));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_activity_message;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        layoutRefresh.setEnableLoadMore(false);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
//        recyclerNews.setLayoutManager(new LinearLayoutManager(mContext));
//        messageAdapter=new MessageAdapter();
//        messageAdapter.bindToRecyclerView(recyclerNews);
        messagePresenter=new MessagePresenter(mContext,this);
        buildRecyclerView();
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                ARouter.getInstance()
                        .build(AppRoutes.APP_MESSAGESETTING)
                        .navigation();
            }
        });
        if(!NotificationUtil.isNotificationEnabled(mContext)){
            topMessageLL.setVisibility(View.VISIBLE);
        }else {
            topMessageLL.setVisibility(View.GONE);
        }
    }
    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerNews.setLayoutManager(virtualLayoutManager);
        recyclerNews.setAdapter(delegateAdapter);

        adpater1 = new MessagePAdapter();
        delegateAdapter.addAdapter(adpater1);

        adpater2 = new MessagePAdapter();
        delegateAdapter.addAdapter(adpater2);

        adpater3 = new MessagePAdapter();
        delegateAdapter.addAdapter(adpater3);
    }

    @Override
    public void getData() {
        super.getData();
        messagePresenter.getMessage();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        messagePresenter.getMessage();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTab(UpdateMessageInfo msg) {

        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    private void initView() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
        topBar = (TopBar) findViewById(R.id.top_bar);
        passOpenMessage = (TextView) findViewById(R.id.passOpenMessage);
        passCloseMessage = (ImageView) findViewById(R.id.passCloseMessage);
        topMessageLL = (ConstraintLayout) findViewById(R.id.topMessageLL);
        passOpenMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationUtil.gotoSet(mContext);
            }
        });
        passCloseMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topMessageLL.setVisibility(View.GONE);
            }
        });
    }
}
