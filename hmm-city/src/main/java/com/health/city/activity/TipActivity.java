package com.health.city.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.health.city.R;
import com.healthy.library.adapter.PostAdapter;
import com.health.city.contract.TipPostContract;
import com.health.city.fragment.FragmentTipActivityChild;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.model.PostDetail;
import com.healthy.library.model.PostTop;
import com.healthy.library.model.TipPost;
import com.health.city.model.UserInfoCityModel;
import com.health.city.presenter.TipPostPresenter;
import com.health.city.widget.TextContentPop;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.interfaces.OnTitleListener;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.presenter.DataStatisticsPresenter;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.utils.JsoupCopy;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 话题榜
 */
@Route(path = CityRoutes.CITY_TIP)
public class TipActivity extends BaseActivity implements IsNeedShare, TipPostContract.View,
        OnRefreshLoadMoreListener, PostAdapter.OnPostFansClickListener,
        PostAdapter.OnPostLikeClickListener, PostAdapter.OnShareClickListener, DataStatisticsContract.View{

    @Autowired
    String topicId;


    //    @Autowired
//     String cityNo;
//    @Autowired
//     String provinceNo;
    @Autowired
    String areaNo;


    private TopBar topBar;
    private SmartRefreshLayout layoutRefresh;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout progressl;
    private ProgressBar progressz;
    private TextView progressText;
    private View divider;
    private TextView joinCount;
    private TextView joinPostCount;
    private SlidingTabLayout st;
    private ViewPager vp;
    private GridLayout tipGrid;
    //    private RecyclerView recycler;
    private ConstraintLayout needS;
    private ImageView passToSendPost;
    private ImageView passToTop;
    TipPostPresenter tipPostPresenter;
    private TipPost tipPost;
    //    private PostAdapter postAdapter;
    private int currentPage;
    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    int currentIndex = 0;
    public Map<String, Object> topmap = new HashMap<>();
    private Map<String, Object> listmap = new HashMap<>();
    ;
    private TextContentPop textContentPop;
    private DataStatisticsPresenter dataStatisticsPresenter;

    private FragmentTipActivityChild leftfragmentPostList;
    private FragmentTipActivityChild rightfragmentPostList;
    private LinearLayout tipGridLL;

    @Override
    protected int getLayoutId() {
        return R.layout.city_activity_tip;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

        areaNo = LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);

        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
                String url = String.format("%s?type=4&topicId=%s&scheme=topicMange&referral_code=%s", urlPrefix, topicId, areaNo, SpUtils.getValue(mContext, SpKey.GETTOKEN));
                surl = url;
                sdes = " ";
                stitle = "#" + tipPost.topicsBaseDTO.topicName + "#";
                showShare();
                if (dataStatisticsPresenter!=null){
                    dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", topicId).puts("sourceType",1).puts("type", 2));
                }
            }
        });

        tipPostPresenter = new TipPostPresenter(mContext, this);
        dataStatisticsPresenter = new DataStatisticsPresenter(mContext, this);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
//        postAdapter = new PostAdapter();
//        recycler.setLayoutManager(new LinearLayoutManager(mContext));
//        postAdapter.bindToRecyclerView(recycler);
//        postAdapter.setOnShareClickListener(this);
//        postAdapter.setOnPostLikeClickListener(this);
//        postAdapter.setOnPostFansClickListener(this);
        List<String> titles = Arrays.asList("最新", "热门");

        Map<String, Object> leftmap = new HashMap<>();
        leftmap.put("type", 2 + "");
        leftmap.put("topicId", topicId);
        if(TextUtils.isEmpty(areaNo)){
            areaNo="0";
        }
        try {
            leftmap.put("type2", "1");
            leftmap.put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
            leftmap.put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
            leftmap.put("addressArea", areaNo + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        leftfragmentPostList = FragmentTipActivityChild.newInstance(leftmap);

        Map<String, Object> righttmap = new HashMap<>();
        righttmap.put("type", 2 + "");
        righttmap.put("topicId", topicId);
        if(TextUtils.isEmpty(areaNo)){
            areaNo="0";
        }
        try {
            righttmap.put("type2", "2");
            righttmap.put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
            righttmap.put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
            righttmap.put("addressArea", areaNo + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        layoutRefresh.setEnableLoadMore(false);

        rightfragmentPostList = FragmentTipActivityChild.newInstance(righttmap);


        fragments.add(leftfragmentPostList);
        fragments.add(rightfragmentPostList);
        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        fragmentPagerItemAdapter.notifyDataSetChanged();
        vp.setOffscreenPageLimit(fragments.size());
        // bind to SmartTabLayout
        st.setViewPager(vp);
        vp.setCurrentItem(currentIndex);
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentIndex = position;

//                currentPage=1;
//                listmap.put("currentPage",currentPage+"");
//                tipPostPresenter.getPostList(listmap);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        topmap.put("topicId", topicId);
        tipPostPresenter.getTipPost(topmap);


//        tipPostPresenter.getPostList(listmap);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", topicId).puts("sourceType",1).puts("type", 1));
    }

    @Override
    public void onSuccessGetPostList(List<PostDetail> posts, PageInfoEarly pageInfoEarly) {
//        if (pageInfoEarly == null) {
//            showEmpty();
//            layoutRefresh.setEnableLoadMore(false);
//            return;
//        }
//        currentPage = pageInfoEarly.currentPage;
//        if (currentPage == 1) {
//            postAdapter.setNewData(posts);
//            if (posts.size() == 0) {
//                showEmpty();
//            }
//        } else {
//            postAdapter.addData(posts);
//        }
//        if (pageInfoEarly.isMore != 1) {
//
//            layoutRefresh.finishLoadMoreWithNoMoreData();
//        } else {
//            layoutRefresh.setNoMoreData(false);
//            layoutRefresh.setEnableLoadMore(true);
//        }
    }

    private int dialogwidth = 400;

    @Override
    public void onSuccessGetTipPost(final TipPost tipPost) {
        this.tipPost = tipPost;
        if (tipPost != null) {
            if (tipPost.topicsBaseDTO != null) {
                joinCount.setText("" + tipPost.topicsBaseDTO.partInNum + "");
                joinPostCount.setText("" + tipPost.topicsBaseDTO.postingNum + "");
                textContentPop = new TextContentPop(mContext, dialogwidth, tipPost.topicsBaseDTO.topicName);
                topBar.setTitle("#" + (tipPost.topicsBaseDTO.topicName.length() > 10 ? tipPost.topicsBaseDTO.topicName.substring(0, 10) + "..." : tipPost.topicsBaseDTO.topicName));
                topBar.setmTitleListener(new OnTitleListener() {
                    @Override
                    public void onTitlePressed() {
                        textContentPop.showAsDropDown(topBar, (dialogwidth / 2), 0);
                    }
                });
                addImages(mContext, tipGrid, tipGridLL, tipPost.postingBaseList);
            } else {
                Toast.makeText(mContext, "该话题已删除，请阅读其他话题", Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }

    private int mMargin;
    private int mCornerRadius;

    private void addImages(final Context context, final GridLayout gridLayout, final View tipGridLL, final List<PostTop> urls) {
        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(mContext, 2);
            mCornerRadius = (int) TransformUtil.dp2px(mContext, 3);
        }
        tipGridLL.setVisibility(View.GONE);
        gridLayout.removeAllViews();

        if (urls != null && urls.size() > 0) {
            gridLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int row = 1;
                    int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight() - (2 + 2 * (row - 1)) * mMargin) / row;
                    gridLayout.removeAllViews();
                    for (int i = 0; i < urls.size(); i++) {
                        final PostTop url = urls.get(i);
                        final int pos = i;
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = w;
                        params.setMargins(mMargin, mMargin, mMargin, mMargin);
                        View imageparent = LayoutInflater.from(context).inflate(R.layout.city_item_activity_head_tip, gridLayout, false);
                        TextView tipName = imageparent.findViewById(R.id.content);
                        View tipNo = imageparent.findViewById(R.id.showtip);
                        if (url.topStatus == 1) {
                            tipNo.setVisibility(View.VISIBLE);
                        } else {
                            tipNo.setVisibility(View.GONE);
                        }
                        try {
                            try {
                                //String text = JsoupCopy.parse().text();
                                tipName.setText(url.postingContent.split("\\n")[0]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            imageparent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String uri;
                                    if(9 == url.postingType){
                                        uri = CityRoutes.CITY_PK_VOTING_DETAIL;
                                    } else {
                                        uri = CityRoutes.CITY_POSTDETAIL;
                                    }
                                    ARouter.getInstance()
                                            .build(uri)
                                            .withString("id", url.id + "")
                                            .navigation();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        gridLayout.addView(imageparent, params);
                        tipGridLL.setVisibility(View.VISIBLE);

                    }
                }
            }, 500);
        }


    }


    @Override
    public void onSuccessLike() {
//        onRefresh(null);
    }

    @Override
    public void onSuccessFan() {
//        onRefresh(null);
    }

    @Override
    public void onSuccessGetMine(UserInfoCityModel userInfoCityModel) {

    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {

        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        progressl = (LinearLayout) findViewById(R.id.progressl);
        progressz = (ProgressBar) findViewById(R.id.progressz);
        progressText = (TextView) findViewById(R.id.progressText);
        divider = (View) findViewById(R.id.divider);
        joinCount = (TextView) findViewById(R.id.joinCount);
        joinPostCount = (TextView) findViewById(R.id.joinPostCount);
        st = (SlidingTabLayout) findViewById(R.id.st);
        vp = (ViewPager) findViewById(R.id.vp);
        tipGrid = (GridLayout) findViewById(R.id.tip_grid);
//        recycler = (RecyclerView) findViewById(R.id.recycler);
        needS = (ConstraintLayout) findViewById(R.id.need_s);
        passToSendPost = (ImageView) findViewById(R.id.passToSendPost);
        passToSendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "话题详情页面");
                MobclickAgent.onEvent(mContext, "event2PostFrom", nokmap);
                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTSEND)
                        .withString("topicId", topicId)
                        .withString("topicName", tipPost.topicsBaseDTO != null ? tipPost.topicsBaseDTO.topicName : "")//bugly上有崩溃 加固下
                        .navigation();
            }
        });
        passToTop = (ImageView) findViewById(R.id.passToTop);
//        passToTop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recycler.smoothScrollToPosition(0);
//            }
//        });
//        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
//
//                if(firstCompletelyVisibleItemPosition<=3){
//                    passToTop.setVisibility(View.GONE);
//                }else {
//                    passToTop.setVisibility(View.VISIBLE);
//                }
//
//
//            }
//        });
        tipGridLL = (LinearLayout) findViewById(R.id.tip_gridLL);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
//        currentPage++;
//        tipPostPresenter.getPostList(listmap);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        currentPage = 1;
        listmap.put("currentPage", currentPage + "");
        tipPostPresenter.getTipPost(topmap);
//        tipPostPresenter.getPostList(listmap);
        leftfragmentPostList.onRefresh(null);
        rightfragmentPostList.onRefresh(null);

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    public void postfansclick(View view, String friendId, String type, String frtype) {
        Map<String, Object> map = new HashMap<>();
        map.put("friendId", friendId);
        map.put("friendType", frtype);
        map.put("type", type);
        tipPostPresenter.fan(map);
    }

    @Override
    public void postlikeclick(View view, String postingId, String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("postingId", postingId);
        map.put("type", type);
        tipPostPresenter.like(map);
    }

    String surl;
    String sdes;
    String stitle;

    @Override
    public void postshareclick(View view, String url, String des, String title,String postId) {
        this.surl = url;
        this.sdes = des;
        this.stitle = title;

        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "话题详情页面");
        MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);

        showShare();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
