package com.health.city.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.city.R;
import com.health.city.adapter.BannerAdapter;
import com.health.city.contract.FansRecommendContract;
import com.healthy.library.adapter.PostAdapter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.contract.PostListSingleContract;
import com.healthy.library.contract.QiYeWeiXinContract;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.Fans;
import com.healthy.library.model.PostDetail;
import com.health.city.presenter.FansRecommabeListPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.AdContract;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.QiYeWeXin;
import com.healthy.library.model.QiYeWeXinKey;
import com.healthy.library.model.QiYeWeXinWorkShop;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.presenter.DataStatisticsPresenter;
import com.healthy.library.presenter.PostListSinglePresenter;
import com.healthy.library.presenter.QiYeWeiXinPresenter;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentPostFocusList extends BaseFragment implements AdContract.View, IsNeedShare, PostListSingleContract.View,
        PostAdapter.OnPostFansChangeListener, PostAdapter.OnPostFansAllListener, PostAdapter.OnShareClickListener,
        PostAdapter.OnPostLikeClickListener, PostAdapter.OnPostFansClickListener, OnRefreshLoadMoreListener,
        FansRecommendContract.View, BaseAdapter.OnOutClickListener, DataStatisticsContract.View,QiYeWeiXinContract.View{

    FansRecommabeListPresenter fansRecommabeListPresenter;
    PostListSinglePresenter postListSinglePresenter;
    PostAdapter postFanAdapter;
    BannerAdapter bannerAdapter;

    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;
    int page = 1;
    int pageFans = 1;
    private PageInfoEarly pageInfoEarlyfans;

    public Map<String, Object> mParams = new HashMap<>();
    List<Fans> fans;
    private ImageView passToSendPost;
    private ImageView passToTop;

    String surl;
    String sdes;
    String stitle;
    AdPresenter adPresenter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private DataStatisticsPresenter dataStatisticsPresenter;
    private boolean needloadmore = true;
    private PostDetail mFanPostDetail;
    private int addIndex;
    private QiYeWeiXinPresenter qiYeWeiXinPresenter;

    public static FragmentPostFocusList newInstance(Map<String, Object> maporg) {
        FragmentPostFocusList fragment = new FragmentPostFocusList();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    public void setNeedloadmore(boolean needloadmore) {
        this.needloadmore = needloadmore;
    }

    public FragmentPostFocusList putMap(String key, String value) {
        getBasemap().put(key, value);
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.city_fragment_focus_layout;
    }

    @Override
    protected void findViews() {
        initView();

        adPresenter = new AdPresenter(mContext, this);
        qiYeWeiXinPresenter=new QiYeWeiXinPresenter(mContext,this);
        refreshLayout.finishLoadMoreWithNoMoreData();
        postListSinglePresenter = new PostListSinglePresenter(mContext, this);
        postListSinglePresenter.setType(1);
        fansRecommabeListPresenter = new FansRecommabeListPresenter(mContext, this);
        dataStatisticsPresenter = new DataStatisticsPresenter(mContext, this);

        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerQuestion.setLayoutManager(virtualLayoutManager);
        recyclerQuestion.setAdapter(delegateAdapter);

        bannerAdapter = new BannerAdapter();
        bannerAdapter.setOutClickListener(this);
        delegateAdapter.addAdapter(bannerAdapter);

        postFanAdapter = new PostAdapter();
        delegateAdapter.addAdapter(postFanAdapter);
        postFanAdapter.setOutClickListener(this);
        postFanAdapter.setOnPostFansAllListener(this);
        postFanAdapter.setOnPostFansChangeListener(this);
        postFanAdapter.setOnPostFansClickListener(this);
        postFanAdapter.setOnPostLikeClickListener(this);
        postFanAdapter.setOnShareClickListener(this);

        refreshLayout.setOnRefreshLoadMoreListener(this);
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
        passToSendPost = (ImageView) findViewById(R.id.passToSendPost);
        passToTop = (ImageView) findViewById(R.id.passToTop);
        passToSendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTSEND)
                        .navigation();
            }
        });
        passToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerQuestion.scrollToPosition(0);
            }
        });
        recyclerQuestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition > 3) {
                    passToTop.setVisibility(View.VISIBLE);
                } else {
                    passToTop.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "3").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG)));
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        mParams.clear();
        if (fans==null) {
            mParams.put("currentPage", pageFans + "");
            mParams.put("pageSize", "5");
            fansRecommabeListPresenter.getFansList(mParams);
        } else {
            mParams.put("currentPage", page + "");
            mParams.put("pageSize", "5");
            mParams.put("type", "0");
            mParams.put("type2", "1");
            postListSinglePresenter.getPostList(mParams);
        }
        qiYeWeiXinPresenter.getRecommandWeiXinGroup(new SimpleHashMapBuilder<>());
    }

    @Override
    public void onSuccessGetFans(List<Fans> fans, PageInfoEarly pageInfoEarly) {
        pageInfoEarlyfans = pageInfoEarly;
        this.fans = fans;
        if (mFanPostDetail == null) {
            getData();
        } else {
            mFanPostDetail.fans = fans;
            postFanAdapter.notifyItemChanged(addIndex);
        }
    }

    @Override
    public void onSuccessGetPostList(List<PostDetail> posts, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly.isMore == 1) {
            refreshLayout.resetNoMoreData();
            refreshLayout.finishLoadMore();
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();
        }

        if (page == 1) {
            postFanAdapter.setData((ArrayList) posts);
        } else {
            postFanAdapter.addDatas((ArrayList) posts);
        }

        if (ListUtil.isEmpty(postFanAdapter.getDatas())) {
            if (ListUtil.isEmpty(fans)) {
                showEmpty();
                return;
            }
            addIndex = 0;
            mFanPostDetail = new PostDetail();
            mFanPostDetail.postingType = -1;
            mFanPostDetail.fans = fans;
            postFanAdapter.addData(mFanPostDetail);
        } else {
            if (mFanPostDetail == null) {
                if (!ListUtil.isEmpty(fans)) {
                    addIndex = 0;
                    if (pageInfoEarlyfans.totalNum > 5) {
                        addIndex = 4;
                    } else {
                        addIndex = pageInfoEarlyfans.totalNum;
                    }
                    mFanPostDetail = new PostDetail();
                    mFanPostDetail.postingType = -1;
                    mFanPostDetail.fans = fans;
                    addIndex = Math.min(addIndex, postFanAdapter.getDatas().size() - 1);
                    postFanAdapter.getDatas().add(addIndex, mFanPostDetail);
                    postFanAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onSuccessLike() {

    }

    @Override
    public void onSuccessGetActivityList() {

    }

    @Override
    public void onSuccessFans() {

    }

    @Override
    public void onSuccessFansAll() {
        for (int i = 0; i < fans.size(); i++) {
            fans.get(i).focusStatus = 1;
        }
        page = 1;
        mParams.put("currentPage", page + "");
        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "3").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG)));
    }


    /**
     * 单体关注回调
     *
     * @param result
     */
    @Override
    public void onSuccessFan(Object result) {
        if ("0".equals(result)) {
            Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccessFan() {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        pageFans = 1;
        fans = null;
        mFanPostDetail = null;
        getData();
        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "3").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG)));

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
       /* mParams.put("currentPage", page + "");
        postListSinglePresenter.getPostList(mappost);*/
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishRefresh();
    }

    @Override
    public void postfansall(View view) {
        if (fans != null && fans.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("friendDTOList", fans);
            fansRecommabeListPresenter.fanAll(map);
        }
    }

    @Override
    public void postfanschange(View view) {
        if (pageInfoEarlyfans == null) return;
        if (pageInfoEarlyfans.isMore != 1) {
            pageFans = 1;
        } else {
            pageFans++;
        }
        mParams.put("currentPage", pageFans + "");
        fansRecommabeListPresenter.getFansList(mParams);
    }

    @Override
    public void postfansclick(View view, String friendId, String type, String frtype) {
        Map<String, Object> map = new HashMap<>();
        map.put("friendId", friendId);
        map.put("friendType", frtype);
        map.put("type", type);
        postListSinglePresenter.fan(map);
    }

    @Override
    public void postlikeclick(View view, String postingId, String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("postingId", postingId);
        map.put("type", type);
        postListSinglePresenter.like(map);
    }

    @Override
    public void postshareclick(View view, String url, String des, String title, String postId) {
        this.surl = url;
        this.sdes = des;
        this.stitle = title;
        dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", postId).puts("sourceType", 2).puts("type", 1));
//        if("本地".equals(fragmentType)){
//            Map nokmap = new HashMap<String, String>();
//            nokmap.put("soure","本地列表");
//            MobclickAgent.onEvent(mContext, "event2ShareClick",nokmap);
//        }
//        if("发现".equals(fragmentType)){
//            Map nokmap = new HashMap<String, String>();
//            nokmap.put("soure","发现列表");
//            MobclickAgent.onEvent(mContext, "event2ShareClick",nokmap);
//        }

        showShare();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserLocationMsg msg) {
        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "3").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG)));
        if (!isFirstLoad) return;
        onRefresh(null);
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

    List<AdModel> adModels = new ArrayList<>();

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {
        bannerAdapter.setModel(null);
        if (adModels == null || adModels.size() == 0) {
            bannerAdapter.setModel(null);
        } else {
            this.adModels = adModels;
            com.healthy.library.businessutil.GlideCopy.with(this)
                    .load(adModels.get(0).photoUrls)

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            bannerAdapter.setModel("null");
                        }
                    });
        }
        bannerAdapter.setAdv(adModels);
    }

    @Override
    public void outClick(@NonNull String function, @NonNull Object obj) {
        if (function.equals("banner")){
            dataStatisticsPresenter.bannerClickNum(new SimpleHashMapBuilder<String, Object>().puts("advertisingId",(String) obj));
        }
    }

    @Override
    public void onSucessGetWeiXinKey(QiYeWeXinKey qiYeWeXinKey) {

    }

    @Override
    public void onSucessGetRecommandWeiXinGroup(List<QiYeWeXin> qiYeWeXins) {
        postFanAdapter.setQiYeWeXins(qiYeWeXins);
    }

    @Override
    public void onSucessGetRecommandWorkerGroup(List<QiYeWeXinWorkShop> qiYeWeXins) {

    }

    @Override
    public void onSucessGetMineWorker(TokerWorkerInfoModel tokerWorkerInfoModel) {

    }

    @Override
    public void onSucessGetPublicWorker(TokerWorkerInfoModel tokerWorkerInfoModel) {

    }
}
