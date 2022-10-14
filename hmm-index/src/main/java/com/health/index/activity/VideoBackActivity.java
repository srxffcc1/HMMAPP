package com.health.index.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.index.R;
import com.health.index.adapter.VideoListAdapter;
import com.health.index.contract.IndexVideoContractOl;
import com.health.index.model.IndexVideo;
import com.health.index.presenter.IndexVideoListOlPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = IndexRoutes.INDEX_VIDEOLIST)
public class VideoBackActivity extends BaseActivity implements IsNeedShare, IndexVideoContractOl.View , OnRefreshLoadMoreListener {

    @Autowired
    String merchantId;
    @Autowired
    String function;
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerNews;
    private VideoListAdapter videoListAdapter;
    private IndexVideoListOlPresenter indexVideoListPresenter;
    private int page=1;


    String surl;
    String sdes;
    String stitle;
    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_videolist;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);


        indexVideoListPresenter=new IndexVideoListOlPresenter(this,this);
        videoListAdapter=new VideoListAdapter();
        videoListAdapter.addHeaderView(getViewHaed());
        recyclerNews.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerNews.setAdapter(videoListAdapter);
        layoutRefresh.setEnableLoadMore(false);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                buildDes();
                showShare();
            }
        });
        if("9003".equals(function)){
            recyclerNews.setBackgroundResource(R.drawable.video_back_bg2);
        }
        getData();
    }



    private View getViewHaed() {
        View head=LayoutInflater.from(mContext).inflate(R.layout.index_onlinevedio_reviewhead,null);
        final ImageView imageView=head.findViewById(R.id.headVideoImg);
        if("9003".equals(function)){
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .load(R.drawable.video_back_head2)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int swidth = ScreenUtils.getScreenWidth(mContext);
                            int height = (int)(resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth() * swidth);
                            ViewGroup.LayoutParams params1=imageView.getLayoutParams();
                            params1.width=swidth;
                            params1.height=height;
                            imageView.setLayoutParams(params1);
                            com.healthy.library.businessutil.GlideCopy.with(imageView).load(resource).into(imageView);
                        }
                    });
        }else {
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .load(R.drawable.video_back_head)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int swidth = ScreenUtils.getScreenWidth(mContext);
                            int height = (int)(resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth() * swidth);
                            ViewGroup.LayoutParams params1=imageView.getLayoutParams();
                            params1.width=swidth;
                            params1.height=height;
                            imageView.setLayoutParams(params1);
                            com.healthy.library.businessutil.GlideCopy.with(imageView.getContext()).load(resource).into(imageView);
                        }
                    });
        }

        return head;
    }

    @Override
    public void getData() {
        super.getData();
        Map<String,Object> map=new HashMap<>();
        map.put("merchantId",merchantId);
        map.put(Functions.FUNCTION, function);
//        map.put("page",page+"");
//        map.put("size",10+"");
        indexVideoListPresenter.getVideoList(map);
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    public void getVideoSuccess(List<IndexVideo> videolist, PageInfoEarly pageInfoEarly) {

        if (pageInfoEarly == null) {
            if (page == 1) {
                showEmpty();
                layoutRefresh.setEnableLoadMore(false);
            } else {
                layoutRefresh.setEnableLoadMore(false);
                layoutRefresh.finishLoadMoreWithNoMoreData();
            }
            return;
        }
        page = pageInfoEarly.currentPage;
        if (page == 1) {
            videoListAdapter.setNewData(videolist);
            if (videolist.size() == 0) {
                showEmpty();
            }
        } else {
            videoListAdapter.addData(videolist);
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }


    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }



    private void buildDes() {
        if(!TextUtils.isEmpty(merchantId)){
            String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_liveVideoUrl);
            String url = String.format("%s?merchantId=%s&scheme=HMMVIDEO&function="+function, urlPrefix, merchantId);
            surl=url;
            stitle="憨妈直播精彩回放";
            sdes="育儿上的健康问题、宝宝的日常养护技巧，憨妈妈告诉你~";
        }else {
            String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_liveVideoUrl);
            String url = String.format("%s?scheme=HMMVIDEO&function="+function, urlPrefix);
            surl=url;
            stitle="憨妈直播精彩回放";
            sdes="育儿上的健康问题、宝宝的日常养护技巧，憨妈妈告诉你~";
        }

    }

    @Override
    public String getSurl() {
        return surl;
    }

    @Override
    public String getSdes() {
        return sdes;
    }

    @Override
    public String getStitle() {
        return stitle;
    }

    @Override
    public Bitmap getsBitmap() {
        return null;
    }
}
