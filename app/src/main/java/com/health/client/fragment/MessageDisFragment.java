package com.health.client.fragment;

import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.client.R;
import com.health.client.adapter.MessageDisAdapter;
import com.health.client.contract.MessageHelperOtherContract;
import com.health.client.model.MonMessageOtherHelper;
import com.health.client.presenter.MessageHelperOtherPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Route(path = AppRoutes.APP_MESSAGEDIS)
public class MessageDisFragment extends BaseFragment implements MessageHelperOtherContract.View, OnRefreshLoadMoreListener {
    private TopBar topBar;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerNews;
    private MessageHelperOtherPresenter messageHelperPresenter;
    private MessageDisAdapter adapter;
    private int currentPage=1;
    private Map<String, Object> fansmap = new HashMap<>();;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_messageonlylist;
    }

    @Override
    protected void findViews() {
        initView();

    }

    @Override
    protected void onCreate() {
        super.onCreate();
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        messageHelperPresenter=new MessageHelperOtherPresenter(mContext,this);
        adapter = new MessageDisAdapter();
        recyclerNews.setLayoutManager(new LinearLayoutManager(mContext));
        adapter.bindToRecyclerView(recyclerNews);
        fansmap.put("pageNum",currentPage+"");
        fansmap.put("pageSize",10+"");
        if("收到的".equals(get("fragmentType"))){
            fansmap.put("memberId",new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            fansmap.put("replyType",new String[]{"1","2"});
        }else {
            fansmap.put("frontId",new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        }
        adapter.setType(get("fragmentType").toString());
        getData();
    }


    public static MessageDisFragment newInstance(Map<String, Object> maporg) {
        MessageDisFragment fragment = new MessageDisFragment();
        Bundle args = new Bundle();
        for (Map.Entry<String, Object> entry : maporg.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Integer) {
                args.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                args.putBoolean(key, (Boolean) value);
            } else if (value instanceof String) {
                args.putString(key, (String) value);
            } else {
                args.putSerializable(key, (Serializable) value);
            }
        }
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    protected void findViews() {
//        super.findViews();
//        initView();
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        ARouter.getInstance().inject(this);

//    }

    @Override
    public void getData() {
        super.getData();
        if("收到的".equals(get("fragmentType"))){
            messageHelperPresenter.getMessage(fansmap);
        }else {
            messageHelperPresenter.getMessageDis(fansmap);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        fansmap.put("pageNum",currentPage+"");
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage=1;
        fansmap.put("pageNum",currentPage+"");
        getData();
    }
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }
    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
    }

    @Override
    public void onSuccessGetMessageList(List<MonMessageOtherHelper> results, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if (currentPage == 1) {
            adapter.setNewData(results);
            if (results.size() == 0) {
                showEmpty();
            }
        } else {
            adapter.addData(results);
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }
}
