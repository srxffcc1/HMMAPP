package com.health.sound.fragment;

import android.os.Bundle;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.sound.R;
import com.healthy.library.adapter.SoundListAdapter;
import com.health.sound.adapter.SoundTopAdapter;
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

public class SoundTypeMoreFragment extends BaseFragment implements OnRefreshLoadMoreListener, SoundHomeContract.View {
    public long page = 1;
    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;
    SoundTopAdapter emptyFragmentAdapter;


    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    SoundHomePresenter soundTypePresenter;
    Map<String, Object> mapup = new HashMap<>();
    private RadioButton radio2;
    private RadioButton radio3;
    public int radiocheck=1;
    private SoundListAdapter soundListAdapter;


    public static SoundTypeMoreFragment newInstance(Map<String, Object> maporg) {
        SoundTypeMoreFragment fragment = new SoundTypeMoreFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sound_fragment_type_more;
    }

    @Override
    protected void findViews() {
        initView();
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.finishLoadMoreWithNoMoreData();
        buildRecyclerView();
        soundTypePresenter = new SoundHomePresenter(mContext, this);
        getData();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerQuestion.setLayoutManager(virtualLayoutManager);
        recyclerQuestion.setAdapter(delegateAdapter);

        soundListAdapter = new SoundListAdapter(get("audioType").toString());
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
        page = 1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    Map<String, Object> mapdown = new HashMap<>();
    @Override
    public void getData() {
        super.getData();
        mapdown.put("number","1");
        mapdown.put("audioType",get("audioType").toString());
        mapdown.put("currentPage",page+"");
        mapdown.put("pageSize",10+"");
        soundTypePresenter.getSoundAlbumsDown(mapdown);
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
    }

//    @Override
//    public void successGetSoundAlbum(SoundTypeSplit soundHomeSplit) {
//        page=soundHomeSplit.current_page;
//        if (page==1) {
//            soundListAdapter.setData((ArrayList<SoundAlbum>) soundHomeSplit.albums);
//        } else {
//            soundListAdapter.addDatas((ArrayList<SoundAlbum>) soundHomeSplit.albums);
//        }
//        if (soundHomeSplit.total_page<=page) {
//            //System.out.println("没有更多了");
//            refreshLayout.finishLoadMoreWithNoMoreData();
//        } else {
//            refreshLayout.setNoMoreData(false);
//            refreshLayout.setEnableLoadMore(true);
//        }
//    }

    @Override
    public void successGetSoundAlbumUp(SoundHomeSplit soundHomeSplit) {

    }

    @Override
    public void successGetSoundAlbumDown(SoundHomeSplit soundHomeSplit) {
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
