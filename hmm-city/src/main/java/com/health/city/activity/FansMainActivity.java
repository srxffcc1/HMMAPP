package com.health.city.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.example.lib_ShapeView.builder.ShapeDrawableBuilder;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.google.android.material.appbar.AppBarLayout;
import com.health.city.R;
import com.health.city.contract.FansContract;
import com.health.city.presenter.FansPresenter;
import com.healthy.library.adapter.EmptyAdapter;
import com.healthy.library.adapter.PostAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.Fans;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.PostDetail;
import com.healthy.library.presenter.DataStatisticsPresenter;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人主页 加一个字段来表示查看自己
 */
@Route(path = CityRoutes.CITY_FANDETAIL)
public class FansMainActivity extends BaseActivity implements IsFitsSystemWindows, IsNeedShare,
        PostAdapter.OnDeleteMainClickListener, FansContract.View, OnRefreshLoadMoreListener,
        PostAdapter.OnPostFansClickListener, PostAdapter.OnPostLikeClickListener,
        PostAdapter.OnShareClickListener, DataStatisticsContract.View {

    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;

    private RecyclerView recycler;
    private ConstraintLayout needS;
    private android.widget.ImageView passToSendPost;
    private android.widget.ImageView passToTop;
    private android.widget.TextView txtTitle;
    private android.widget.ImageView imgBack;
    private FansPresenter fansPresenter;
    private int currentPage = 0;
    //private PostMainAdapter postMainAdapter;
    private boolean isnowuser = false;
    private Fans mFans;

    @Autowired
    String type;//是否查看我的

    //    SpUtils.getValue(context, SpKey.USER_ID)
    @Autowired
    String memberId;
    @Autowired
    String searchMemberType;
    //Map<String, Object> postmap = new HashMap<>();
    Map<String, Object> fansmap = new HashMap<>();

    private int totalDy = 0;
    private ConstraintLayout mClCount;
    private Toolbar mToolBar;
    private AppBarLayout mAppBarLayout;
    private ImageView ivHeader;
    private int mHeaderWidth;
    private TextView name;
    private TextView status;
    private TextView toFollow;
    private TextView followTitle;
    private TextView followCount;
    private TextView fansCount;
    private TextView fansTitle;
    private Drawable followNormal;
    private Drawable followSelect;
    private ImageView mIvUserBadge;
    private ShapeTextView mUserBadgeName;
    private PostAdapter mPostAdapter;
    private EmptyAdapter mEmptyAdapter;
    private DataStatisticsPresenter dataStatisticsPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.city_activity_user_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        StyledDialog.init(this);

        fansPresenter = new FansPresenter(mContext, this);
        dataStatisticsPresenter = new DataStatisticsPresenter(mContext, this);
        if (memberId != null && !TextUtils.isEmpty(memberId)) {
            if (memberId.equals(new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))) {
                type = "0";
            }
            getData();
        } else {
            Toast.makeText(mContext, "当前用户已注销", Toast.LENGTH_LONG).show();
            finish();
        }
        buildRecyclerView();
    }

    private void buildRecyclerView() {

        layoutRefresh.setOnRefreshLoadMoreListener(this);

        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(mContext);
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);
        mPostAdapter = new PostAdapter();
        mPostAdapter.setDelete("0".equals(type) ? true : false);
        mPostAdapter.setPostViewType(1); // 区别展示样式
        mPostAdapter.setOnDeleteMainClickListener(this);
        mPostAdapter.setOnPostFansClickListener(this);
        mPostAdapter.setOnPostLikeClickListener(this);
        mPostAdapter.setOnShareClickListener(this);
        delegateAdapter.addAdapter(mPostAdapter);

        mEmptyAdapter = new EmptyAdapter();
        mEmptyAdapter.setEmptyText("暂无帖子信息~");
        delegateAdapter.addAdapter(mEmptyAdapter);

        initListener();
    }

    @Override
    protected void findViews() {
        super.findViews();
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        mAppBarLayout = findViewById(R.id.app_bar);
        mClCount = findViewById(R.id.cl_count);
        mToolBar = findViewById(R.id.tool_bar);
        ivHeader = (ImageView) findViewById(R.id.ivHeader);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        needS = (ConstraintLayout) findViewById(R.id.need_s);
        passToSendPost = (ImageView) findViewById(R.id.passToSendPost);
        passToTop = (ImageView) findViewById(R.id.passToTop);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        imgBack = (ImageView) findViewById(R.id.img_back);
        name = (TextView) findViewById(R.id.name);
        status = (TextView) findViewById(R.id.status);
        toFollow = (TextView) findViewById(R.id.toFollow);
        followTitle = (TextView) findViewById(R.id.followTitle);
        followCount = (TextView) findViewById(R.id.followCount);
        fansTitle = (TextView) findViewById(R.id.fansTitle);
        fansCount = (TextView) findViewById(R.id.fansCount);
        mIvUserBadge = findViewById(R.id.iv_user_badge);
        mUserBadgeName = findViewById(R.id.stv_user_badgeName);
        StatusLayout mLayoutStatus = findViewById(R.id.layout_status);
        setStatusLayout(mLayoutStatus);

        followNormal = mContext.getResources().getDrawable(R.drawable.ic_star_tofollow);
        followSelect = mContext.getResources().getDrawable(R.drawable.ic_star_isfollow);
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        passToSendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTSEND)
                        .navigation();
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) ivHeader.getLayoutParams();
                if (mHeaderWidth == 0) {
                    mHeaderWidth = layoutParams.width;
                }
                if (verticalOffset == 0) { //展开状态
                    mClCount.setAlpha(1);
                    int px = (int) TransformUtil.dp2px(mContext, 50f);
                    if (layoutParams.width != px) {
                        layoutParams.width = px;
                        layoutParams.height = px;
                        ivHeader.setLayoutParams(layoutParams);
//                        FrameLayout.LayoutParams imgLayoutParams = (FrameLayout.LayoutParams) imgBack.getLayoutParams();
//                        imgLayoutParams.topMargin = (int) getResources().getDimension(R.dimen.status_2020) / 2;
//                        imgBack.setLayoutParams(imgLayoutParams);
                    }


                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) { //折叠状态
                    mClCount.setAlpha(0);
                    int px = (int) TransformUtil.dp2px(mContext, 40f);
                    if (layoutParams.width != px) {
                        layoutParams.width = px;
                        layoutParams.height = px;
                        ivHeader.setLayoutParams(layoutParams);
//                        FrameLayout.LayoutParams imgLayoutParams = (FrameLayout.LayoutParams) imgBack.getLayoutParams();
//                        imgLayoutParams.topMargin = (int) getResources().getDimension(R.dimen.status_2020) * 2;
//                        imgBack.setLayoutParams(imgLayoutParams);
                    }
                } else {
                    float alpha;
                    if (verticalOffset == 0) {
                        alpha = 1;
                    } else {
                        alpha = Math.min(0, verticalOffset * 1.0f / ((appBarLayout.getTotalScrollRange()))) + 1;
                    }
                    int px = (int) (TransformUtil.dp2px(mContext, 50f) - (TransformUtil.dp2px(mContext, 10f) * (1 - alpha)));
                    if (layoutParams.width != px) {
                        layoutParams.width = px;
                        layoutParams.height = px;
                        ivHeader.setLayoutParams(layoutParams);
                    }
                    Log.e("FansMain", "onScrolled: " + verticalOffset + "\r alpha =" + alpha);

                    mClCount.setAlpha(alpha);
                }
            }
        });

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();

                if (firstCompletelyVisibleItemPosition <= 3) {
                    passToTop.setVisibility(View.GONE);
                } else {
                    passToTop.setVisibility(View.VISIBLE);
                }

                if (recyclerView.canScrollVertically(-1)) {

                } else {
                    totalDy = 0;
                }
                totalDy += dy;
            }
        });
        passToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recycler.smoothScrollToPosition(0);
            }
        });

        followCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnowuser) {
                    ARouter.getInstance().build(CityRoutes.CITY_FANLIST)
                            .withString("type", "0")
                            .navigation();
                }
            }
        });
        fansCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnowuser) {
                    ARouter.getInstance().build(CityRoutes.CITY_FANLIST)
                            .withString("type", "1")
                            .navigation();
                }
            }
        });
        followTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnowuser) {
                    ARouter.getInstance().build(CityRoutes.CITY_FANLIST)
                            .withString("type", "0")
                            .navigation();
                }
            }
        });
        fansTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnowuser) {
                    ARouter.getInstance().build(CityRoutes.CITY_FANLIST)
                            .withString("type", "1")
                            .navigation();
                }
            }
        });

        toFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFans == null){
                    return;
                }
                if (mFans.focusStatus != 0) {
                    StyledDialog.init(mContext);
                    StyledDialog.buildIosAlert("", "确认取消关注吗?", new MyDialogListener() {
                        @Override
                        public void onFirst() {


                        }

                        @Override
                        public void onThird() {
                            super.onThird();
                        }

                        @Override
                        public void onSecond() {
                            Map<String, Object> map = new HashMap<>();
                            map.put("friendId", mFans.memberId + "");
                            map.put("friendType", searchMemberType);
                            map.put("type", mFans.focusStatus == 0 ? "0" : "1");
                            fansPresenter.fan(map);

                        }
                    }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("friendId", mFans.memberId + "");
                    map.put("friendType",searchMemberType);
                    map.put("type", mFans.focusStatus == 0 ? "0" : "1");
                    fansPresenter.fan(map);
                }
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserLocationMsg msg) {
        onRefresh(null);
    }


    @Override
    public void getData() {
        super.getData();
        fansmap.clear();
        if (currentPage == 0) {
            currentPage++;
            if ("0".equals(type)) {
                fansmap.put("type", "0");
            } else {
                fansmap.put("type", "1");
                fansmap.put("searchMemberId", memberId);
                fansmap.put("searchMemberType", searchMemberType);
            }
            fansmap.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
            fansPresenter.getFansDetail(fansmap);
        } else {
            fansmap.put("currentPage", currentPage + "");
            fansmap.put("pageSize", "10");
            if ("0".equals(type)) {
                fansmap.put("type", "0");
            } else {
                fansmap.put("type", "1");
                fansmap.put("memberId", memberId);
                fansmap.put("memberType", searchMemberType);
            }
            fansPresenter.getPostList(fansmap);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        currentPage = 0;
        getData();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        currentPage++;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
    }

    @Override
    public void onSuccessGetFanDetail(Fans fans) {
        if (fans == null) {
            getData();
            return;
        }
        if (new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)).equals(fans.memberId)) {
            isnowuser = true;
        }
        this.mFans = fans;

        boolean empty = TextUtils.isEmpty(mFans.badgeId);
        int mVisibility = empty ? View.GONE : View.VISIBLE;
        mIvUserBadge.setVisibility(mVisibility);
        mUserBadgeName.setVisibility(mVisibility);
        mUserBadgeName.setText(mFans.badgeName);
        if (!empty) {
            if (mFans.badgeId != mUserBadgeName.getTag()) {
                mUserBadgeName.setTag(mFans.badgeId);
                ShapeDrawableBuilder shapeDrawableBuilder = mUserBadgeName.getShapeDrawableBuilder();
                if (0 == mFans.badgeType) {
                    mIvUserBadge.setImageResource(com.healthy.library.R.drawable.icon_user_certification);
                    shapeDrawableBuilder
                            .setSolidColor(0)
                            .setGradientColors(Color.parseColor("#FF6060"), Color.parseColor("#FF256C"))
                            .intoBackground();
                }
                if (1 == mFans.badgeType) {
                    mIvUserBadge.setImageResource(com.healthy.library.R.drawable.icon_user_official_certification);
                    shapeDrawableBuilder
                            .clearGradientColors();
                    shapeDrawableBuilder
                            .setSolidColor(Color.parseColor("#3889FD")).intoBackground();
                }
            }
        }

        toFollow.setVisibility("0".equals(type) ? View.GONE : View.VISIBLE);
        if (isnowuser) {
            toFollow.setVisibility(View.GONE);
        } else {
            toFollow.setVisibility(View.VISIBLE);
            if (mFans.focusStatus == 0) {
                toFollow.setText("关注");
                toFollow.setCompoundDrawablesWithIntrinsicBounds(followNormal, null, null, null);
            } else {
                toFollow.setText("已关注");
                toFollow.setCompoundDrawablesWithIntrinsicBounds(followSelect, null, null, null);
            }
            toFollow.setCompoundDrawablePadding(9);
        }
        name.setText(mFans.nickName);

        if (TextUtils.isEmpty(mFans.memberStatus)) {
            status.setVisibility(View.GONE);
        } else {
            status.setText(mFans.memberStatus.replace("育儿期", ""));
        }
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .asBitmap()
                .load(mFans.faceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)
                
                .into(ivHeader);
        followCount.setText(mFans.friendNum + "");
        fansCount.setText(mFans.fansNum + "");
        getData();
    }

    @Override
    public void onSuccessGetPostList(List<PostDetail> posts, PageInfoEarly pageInfoEarly) {

        mEmptyAdapter.setModel(null);

        if (pageInfoEarly.isMore != 1) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.finishLoadMore();
            layoutRefresh.resetNoMoreData();
        }
        currentPage = pageInfoEarly.currentPage;

        if (currentPage == 1 || currentPage == 0) {
            mPostAdapter.setData((ArrayList<PostDetail>) posts);
        } else {
            mPostAdapter.addDatas((ArrayList<PostDetail>) posts);
        }

        if (ListUtil.isEmpty(mPostAdapter.getDatas())) {
            mEmptyAdapter.setModel("");
        } else {
            mEmptyAdapter.setModel(null);
        }

    }

    @Override
    public void onSuccessLike() {
    }

    @Override
    public void onSuccessFans() {
        mFans.focusStatus = mFans.focusStatus == 0 ? 1 : 0;
        mFans.fansNum = mFans.fansNum + (mFans.focusStatus == 0 ? -1 : 1);
        fansCount.setText(mFans.fansNum + "");
        if (mFans.focusStatus == 0) {
            toFollow.setText("关注");
            toFollow.setCompoundDrawablesWithIntrinsicBounds(followNormal, null, null, null);
        } else {
            toFollow.setText("已关注");
            toFollow.setCompoundDrawablesWithIntrinsicBounds(followSelect, null, null, null);
        }
        toFollow.setCompoundDrawablePadding(9);
    }

    @Override
    public void onSuccessDelete() {
        onRefresh(null);
    }

    @Override
    public void postfansclick(View view, String friendId, String type, String frtype) {
        Map<String, Object> map = new HashMap<>();
        map.put("friendId", friendId);
        map.put("friendType", frtype);
        map.put("type", type);
        fansPresenter.fan(map);
    }

    String surl;
    String sdes;
    String stitle;

    @Override
    public void postlikeclick(View view, String postingId, String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("postingId", postingId);
        map.put("type", type);
        fansPresenter.like(map);
    }

    @Override
    public void postshareclick(View view, String url, String des, String title, String postId) {
        this.surl = url;
        this.sdes = des;
        this.stitle = title;


        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "个人详情页面");
        MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);

        showShare();
        dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", postId).puts("sourceType", 2).puts("type", 2));
    }

    @Override
    public void postdeleteclick(View view, final String id) {

        StyledDialog.init(this);
        StyledDialog.buildIosAlert("", "确定要删除该帖子?", new MyDialogListener() {
            @Override
            public void onFirst() {

            }

            @Override
            public void onSecond() {
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("postingStatus", 2 + "");
                fansPresenter.delete(map);
            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();


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
