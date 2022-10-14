package com.health.sound.fragment;

import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.sound.R;
import com.health.sound.adapter.SoundListHistoryAdapter;
import com.health.sound.adapter.SoundTopAdapter;
import com.health.sound.contract.SoundHistoryListContract;
import com.health.sound.model.SoundHistory;
import com.health.sound.presenter.SoundHistoryListPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoundListHistoryFragment extends BaseFragment implements OnRefreshLoadMoreListener, SoundHistoryListContract.View {
    public int page = 1;
    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;
    SoundTopAdapter emptyFragmentAdapter;


    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private SoundListHistoryAdapter soundListAdapter;
    Map<String,Object> listmap=new HashMap<>();

    SoundHistoryListPresenter soundHistoryListPresenter;
    public static SoundListHistoryFragment newInstance(Map<String, Object> maporg) {
        SoundListHistoryFragment fragment = new SoundListHistoryFragment();
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
        soundHistoryListPresenter=new SoundHistoryListPresenter(mContext,this);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.finishLoadMoreWithNoMoreData();
        layoutStatus.setmEmptyContent("您还没有收听记录");
        buildRecyclerView();
        XmPlayerManager.getInstance(mContext).addPlayerStatusListener(mPlayerStatusListener);
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XmPlayerManager.getInstance(mContext).removePlayerStatusListener(mPlayerStatusListener);
    }

    private IXmPlayerStatusListener mPlayerStatusListener = new IXmPlayerStatusListener() {

        @Override
        public void onPlayStart() {
            soundListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onPlayPause() {

            soundListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onPlayStop() {

            soundListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSoundPlayComplete() {

            soundListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSoundPrepared() {
        }

        @Override
        public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {

        }

        @Override
        public void onBufferingStart() {

        }

        @Override
        public void onBufferingStop() {

            soundListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onBufferProgress(int i) {

        }

        @Override
        public void onPlayProgress(int i, int i1) {

        }

        @Override
        public boolean onError(XmPlayerException e) {
            return false;
        }
    };
    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerQuestion.setLayoutManager(virtualLayoutManager);
        recyclerQuestion.setAdapter(delegateAdapter);

        soundListAdapter = new SoundListHistoryAdapter(get("audioType").toString());
//        List<String> emptylist2=new ArrayList<>();
//        emptylist2.clear();
//        for (int i = 0; i <20 ; i++) {
//            emptylist2.add(""+i);
//        }
//        soundListAdapter.setData((ArrayList<String>) emptylist2);
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

    @Override
    public void getData() {
        super.getData();
        try {
            listmap.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            listmap.put("currentPage", page+"");
            listmap.put("pageSize", 10+"");
            soundHistoryListPresenter.getHistoryAlbum(listmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
    }

    @Override
    public void successGetHistoryAlbum(List<SoundHistory> soundAlbumList, PageInfoEarly pageInfoEarly) {
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
