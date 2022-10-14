package com.health.sound.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.health.sound.R;
import com.health.sound.adapter.SoundListSeachAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Block;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;

import java.util.HashMap;
import java.util.Map;


@Route(path = SoundRoutes.SOUND_SEACH)
public class SoundSeachActivity extends BaseActivity implements OnRefreshLoadMoreListener, TextView.OnEditorActionListener {

    @Autowired
    String audioType;

    private android.widget.EditText serarchKeyWord;
    private ImageView imgBack;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerNews;
    public String tagname="";
    public long page=1;
    SoundListSeachAdapter soundListSeachAdapter;
    private ImageView clearEdit;

    @Override
    protected int getLayoutId() {
        return R.layout.sound_activity_seach;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        recyclerNews.setLayoutManager(new LinearLayoutManager(mContext));
        soundListSeachAdapter = new SoundListSeachAdapter(audioType);
        soundListSeachAdapter.bindToRecyclerView(recyclerNews);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        serarchKeyWord.setOnEditorActionListener(this);
        serarchKeyWord.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) ((Activity) mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                serarchKeyWord.requestFocus();
            }
        },300);
        serarchKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    clearEdit.setVisibility(View.VISIBLE);
                }else {
                    clearEdit.setVisibility(View.GONE);
                }
            }
        });
        getData();
        clearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serarchKeyWord.setText("");
                page=1;
                onSucessGetSeachList(null);
            }
        });
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
    public void getData() {
        super.getData();
        showLoading();
        if(!TextUtils.isEmpty(tagname)){
            Map<String, String> map = new HashMap<String, String>();
            map.put(DTransferConstants.SEARCH_KEY, tagname);
            map.put(DTransferConstants.PAGE, page+"");
            map.put(DTransferConstants.CALC_DIMENSION,"3");
            map.put(DTransferConstants.SORT,"desc");
            CommonRequest.getSearchedAlbums(map, new IDataCallBack<SearchAlbumList>() {
                @Override
                public void onSuccess(SearchAlbumList searchAlbumList) {
                    onSucessGetSeachList(searchAlbumList);
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }else {
            onSucessGetSeachList(null);
        }

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
    }

    private void onSucessGetSeachList(SearchAlbumList searchAlbumList) {

        showContent();
        if(searchAlbumList==null&&page==1){
            showEmpty();
            soundListSeachAdapter.setNewData(null);
            layoutRefresh.finishLoadMoreWithNoMoreData();
        }else {

            for (int i = 0; i <searchAlbumList.getAlbums().size() ; i++) {
                Album album=searchAlbumList.getAlbums().get(i);
                if(Block.checkIsBlockReplace(album.getAlbumTitle())){
                    searchAlbumList.getAlbums().remove(i);
                    i--;
                }
            }

            page=searchAlbumList.getCurrentPage();
            if (page==1||page==0) {
                soundListSeachAdapter.setNewData( searchAlbumList.getAlbums());
            } else {
                //System.out.println("加载第二页");
                soundListSeachAdapter.addData(searchAlbumList.getAlbums());
            }
            if (searchAlbumList.getTotalCount()<=page) {
                //System.out.println("没有更多了");
                layoutRefresh.finishLoadMoreWithNoMoreData();
            } else {
                layoutRefresh.setNoMoreData(false);
                layoutRefresh.setEnableLoadMore(true);
            }
            if(searchAlbumList.getAlbums().size()==0){
                showEmpty();
            }
        }
        onRequestFinish();

    }

    private void initView() {
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        imgBack = (ImageView) findViewById(R.id.img_back);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
        clearEdit = (ImageView) findViewById(R.id.clearEdit);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (TextUtils.isEmpty(serarchKeyWord.getText().toString())) {
                tagname="";
            }else {
                if(Block.checkIsBlockReplace(serarchKeyWord.getText().toString())){
                    tagname="";
                }else {

                    tagname=serarchKeyWord.getText().toString();
                }
            }
            page=1;
            getData();
        }
        return false;
    }
}
