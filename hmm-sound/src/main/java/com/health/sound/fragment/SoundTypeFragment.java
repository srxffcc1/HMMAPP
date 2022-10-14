package com.health.sound.fragment;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.sound.R;
import com.healthy.library.adapter.SoundListAdapter;
import com.health.sound.adapter.SoundTopAdapter;
import com.health.sound.contract.SoundTypeContract;
import com.healthy.library.model.SoundTypeSplit;
import com.health.sound.presenter.SoundTypePresenter;
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

public class SoundTypeFragment extends BaseFragment implements OnRefreshLoadMoreListener, SoundTypeContract.View {
    public long page = 1;
    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;
    SoundTopAdapter emptyFragmentAdapter;


    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    SoundTypePresenter soundTypePresenter;
    Map<String, Object> mapup = new HashMap<>();
    private RadioButton radio2;
    private RadioButton radio3;
    public int radiocheck=1;
    private SoundListAdapter soundListAdapter;


    public static SoundTypeFragment newInstance(Map<String, Object> maporg) {
        SoundTypeFragment fragment = new SoundTypeFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sound_fragment_type;
    }

    @Override
    protected void findViews() {
        initView();
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.finishLoadMoreWithNoMoreData();
        buildRecyclerView();
        soundTypePresenter = new SoundTypePresenter(mContext, this);
        radio2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    radiocheck=1;
                }
                onRefresh(null);
            }
        });
        radio3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    radiocheck=2;
                }
                onRefresh(null);
            }
        });
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

    @Override
    public void getData() {
        super.getData();
        mapup.put("audioFrequencyCategoryId",get("audioFrequencyCategoryId").toString());
        mapup.put("sortBy",radiocheck==1?"play_count":"updated_at");
        mapup.put("calcDimension",radiocheck==1?"3":"2");
        mapup.put("audioType",get("audioType").toString());
        mapup.put("audioCategoryName",get("audioCategoryName").toString());
        mapup.put("currentPage",page+"");
        mapup.put("pageSize",10+"");
        soundTypePresenter.getSoundAlbums(mapup);
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
        radio2 = (RadioButton) findViewById(R.id.radio2);
        radio3 = (RadioButton) findViewById(R.id.radio3);
    }

    @Override
    public void successGetSoundAlbum(SoundTypeSplit soundHomeSplit) {
        if(soundHomeSplit!=null){
            page=soundHomeSplit.current_page;
            if(soundHomeSplit.albums!=null){

                for (int i = 0; i <soundHomeSplit.albums.size() ; i++) {
                    SoundAlbum album=soundHomeSplit.albums.get(i);
                    if(Block.checkIsBlockReplace(album.album_title)){
                        soundHomeSplit.albums.remove(i);
                        i--;
                    }
                }
                if (page==1||page==0) {
                    soundListAdapter.setData((ArrayList<SoundAlbum>) soundHomeSplit.albums);
                } else {
                    soundListAdapter.addDatas((ArrayList<SoundAlbum>) soundHomeSplit.albums);
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
}
