package com.health.sound.fragment;

import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.sound.R;
import com.health.sound.adapter.SoundListSubAdapter;
import com.health.sound.contract.SoundSubListContract;
import com.health.sound.model.SoundHistory;
import com.health.sound.presenter.SoundSubListPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoundListSubFragment extends BaseFragment implements OnRefreshLoadMoreListener, SoundSubListContract.View {
    public int page = 1;
    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;


    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private SoundListSubAdapter soundListAdapter;
    SoundSubListPresenter soundSubListPresenter;

    public static SoundListSubFragment newInstance(Map<String, Object> maporg) {
        SoundListSubFragment fragment = new SoundListSubFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.sound_fragment_list;
    }

    @Override
    protected void findViews() {
        initView();

        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.finishLoadMoreWithNoMoreData();
        buildRecyclerView();
        layoutStatus.setmEmptyContent("您还没有订阅记录");
        soundSubListPresenter=new SoundSubListPresenter(mContext,this);
        getData();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerQuestion.setLayoutManager(virtualLayoutManager);
        recyclerQuestion.setAdapter(delegateAdapter);

        try {
            soundListAdapter = new SoundListSubAdapter(get("audioType").toString());
        } catch (Exception e) {
            soundListAdapter = new SoundListSubAdapter("1");
            e.printStackTrace();
        }

        delegateAdapter.addAdapter(soundListAdapter);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page=1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    Map<String,Object> listmap=new HashMap<>();
    @Override
    public void getData() {
        super.getData();
        try {
            listmap.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            listmap.put("currentPage", page+"");
            listmap.put("pageSize", 10+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        soundSubListPresenter.getSubAlbum(listmap);
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
    }

    @Override
    public void successGetSubAlbum(List<SoundHistory> soundAlbumList, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            refreshLayout.setEnableLoadMore(false);
            return;
        }
        page = pageInfoEarly.currentPage;

        //System.out.println("刷新数据了："+get("type")+"："+get("type2"));

        if (page == 1) {
            if(soundAlbumList.size()==0){
                showEmpty();
            }
            soundListAdapter.setData((ArrayList<SoundHistory>) soundAlbumList);

        } else {
            soundListAdapter.addDatas((ArrayList<SoundHistory>) soundAlbumList);
        }
        if (pageInfoEarly.isMore != 1) {
            //System.out.println("没有更多了");
            refreshLayout.setNoMoreData(true);
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }
}
