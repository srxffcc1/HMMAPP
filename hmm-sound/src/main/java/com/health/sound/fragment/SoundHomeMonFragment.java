package com.health.sound.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.sound.R;
import com.health.sound.adapter.SoundDivideAdapter;
import com.health.sound.adapter.SoundGridAdapter;
import com.healthy.library.adapter.SoundListAdapter;
import com.health.sound.adapter.SoundTitleAdapter;
import com.health.sound.adapter.SoundTopAdapter;
import com.health.sound.adapter.SoundTopMonAdapter;
import com.health.sound.contract.SoundHomeContract;
import com.healthy.library.model.SoundHomeSplit;
import com.health.sound.presenter.SoundHomePresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Block;
import com.healthy.library.model.SoundAlbum;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoundHomeMonFragment extends BaseFragment implements OnRefreshLoadMoreListener, SoundHomeContract.View {
    public long page = 1;
    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;
    SoundTopAdapter emptyFragmentAdapter;


    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private SoundTopMonAdapter soundTopAdapter;
    private SoundTitleAdapter soundTitleAdapterToday;
    private SoundGridAdapter soundGridAdapter;
    private SoundDivideAdapter soundDivideAdapter;
    private SoundTitleAdapter soundTitleAdapterList;
    private SoundListAdapter soundListAdapter;
    SoundHomePresenter soundHomePresenter;
    Map<String, Object> mapup = new HashMap<>();
    Map<String, Object> mapdown = new HashMap<>();


    public static SoundHomeMonFragment newInstance(Map<String, Object> maporg) {
        SoundHomeMonFragment fragment = new SoundHomeMonFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sound_fragment_main;
    }

    @Override
    protected void findViews() {
        initView();
        soundHomePresenter = new SoundHomePresenter(mContext, this);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.finishLoadMoreWithNoMoreData();
        buildRecyclerView();
        getData();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerQuestion.setLayoutManager(virtualLayoutManager);
        recyclerQuestion.setAdapter(delegateAdapter);

        soundTopAdapter = new SoundTopMonAdapter(get("audioType").toString());
        soundTopAdapter.setModel("头部");
        delegateAdapter.addAdapter(soundTopAdapter);

        soundTitleAdapterToday = new SoundTitleAdapter();
//        soundTitleAdapterToday.setModel("标题1");
        soundTitleAdapterToday.setAudioType(get("audioType").toString());
        soundTitleAdapterToday.setCategoryId(get("audioFrequencyCategoryId").toString());
        delegateAdapter.addAdapter(soundTitleAdapterToday);

        soundGridAdapter = new SoundGridAdapter(get("audioType").toString());
//        List<String> emptylist1 = new ArrayList<>();
//        emptylist1.clear();
//        for (int i = 0; i < 5; i++) {
//            emptylist1.add("" + i);
//        }
//        soundGridAdapter.setData((ArrayList<String>) emptylist1);
        delegateAdapter.addAdapter(soundGridAdapter);

        soundDivideAdapter = new SoundDivideAdapter();
        soundDivideAdapter.setModel("分割线");
        delegateAdapter.addAdapter(soundDivideAdapter);

        soundTitleAdapterList = new SoundTitleAdapter();
//        soundTitleAdapterList.setModel("标题2");
        soundTitleAdapterList.setNeedMore(false);
        delegateAdapter.addAdapter(soundTitleAdapterList);

        soundListAdapter = new SoundListAdapter(get("audioType").toString());
//        List<String> emptylist2 = new ArrayList<>();
//        emptylist2.clear();
//        for (int i = 0; i < 20; i++) {
//            emptylist2.add("" + i);
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
        page = 1;
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
        if (page == 1) {
            mapup.put("number","1");
            mapup.put("audioType",get("audioType").toString());
            mapup.put("currentPage","1");
            mapup.put("pageSize","6");
            soundHomePresenter.getSoundAlbumsUp(mapup);
        }
        mapdown.put("number","2");
        mapdown.put("audioType",get("audioType").toString());
        mapdown.put("currentPage",page+"");
        mapdown.put("pageSize",10+"");
        soundHomePresenter.getSoundAlbumsDown(mapdown);
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
    }

    @Override
    public void successGetSoundAlbumUp(SoundHomeSplit soundHomeSplit) {
        soundTitleAdapterToday.setModel(soundHomeSplit.title);
        if(soundHomeSplit.values!=null&&soundHomeSplit.values.size()>0){
            soundGridAdapter.setData((ArrayList<SoundAlbum>) soundHomeSplit.values);
        }

    }

    @Override
    public void successGetSoundAlbumDown(SoundHomeSplit soundHomeSplit) {
        if(soundHomeSplit.title!=null){
            soundTitleAdapterList.setModel(soundHomeSplit.title);
        }
        page=soundHomeSplit.current_page;
        if(soundHomeSplit.values!=null){
            for (int i = 0; i <soundHomeSplit.values.size() ; i++) {
                SoundAlbum album=soundHomeSplit.values.get(i);
                if(Block.checkIsBlockReplace(album.album_title)){
                    soundHomeSplit.values.remove(i);
                    i--;
                }
            }
            if (page==1||page==0) {
                soundListAdapter.setData((ArrayList<SoundAlbum>) soundHomeSplit.values);
            } else {
                soundListAdapter.addDatas((ArrayList<SoundAlbum>) soundHomeSplit.values);
            }
        }

        if (soundHomeSplit.total_page<=page) {
            //System.out.println("没有更多了");
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }

    }
}
