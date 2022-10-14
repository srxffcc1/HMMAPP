package com.health.mine.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.gongwen.marqueen.MarqueeView;
import com.gongwen.marqueen.SimpleMarqueeFactory;
import com.health.mine.R;
import com.health.mine.adapter.InviteBottomItemAdapter;
import com.health.mine.adapter.InviteBottomItemEmptyAdapter;
import com.health.mine.adapter.InviteBottomItemUnderAdapter;
import com.health.mine.adapter.InviteBottomTopAdapter;
import com.health.mine.adapter.InviteCenterAdapter;
import com.health.mine.adapter.InviteTopAdapter;
import com.health.mine.contract.InviteContract;
import com.health.mine.presenter.InvitePresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.InviteAct;
import com.healthy.library.model.InviteJoinInfo;
import com.healthy.library.model.InviteReward;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StatusBarTxtColorUtil;
import com.healthy.library.widget.StringDialog;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

@Route(path = MineRoutes.MINE_INVITE)//邀请页面
public class InviteActivity extends BaseActivity implements IsFitsSystemWindows, InviteContract.View, BaseAdapter.OnOutClickListener {

    private TopBar topBar;
    private RecyclerView recycler;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    InviteBottomItemAdapter inviteBottomItemAdapter;
    InviteBottomItemEmptyAdapter inviteBottomItemEmptyAdapter;
    InviteBottomItemUnderAdapter inviteBottomItemUnderAdapter;
    InviteBottomTopAdapter inviteBottomTopAdapter;
    InviteCenterAdapter inviteCenterAdapter;
    InviteTopAdapter inviteTopAdapter;
    //    InviteBottomGoodsAdapter inviteBottomGoodsAdapter;
//    InviteCenterWithMineAdapter inviteCenterWithMineAdapter;
    InviteAct inviteAct;
    InvitePresenter invitePresenter;
    private ImageView goInvite;
    private SmartRefreshLayout refreshLayout;
    @Autowired
    String inviteActivityId;
    @Autowired
    String shopId;
    @Autowired
    String merchantId;
    private MarqueeView inviteJoinMan;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_invate;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if (shopId == null) {
            shopId = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP);
        }
        if (merchantId == null) {
            merchantId = SpUtils.getValue(mContext, SpKey.CHOSE_MC);
        }
        invitePresenter = new InvitePresenter(this, this);
        topBar.setTitle("邀请有礼");
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        if (!TextUtils.isEmpty(inviteActivityId)) {
            invitePresenter.getMerchantInvite(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "invite_10001")
                    .puts("activityId", inviteActivityId));
        } else {
            invitePresenter.getMerchantInvite(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "invite_10002")
                    .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
        }
        goInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outClick("邀请新人", null);
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
        buildRecyclerView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        goInvite = (ImageView) findViewById(R.id.goInvite);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        inviteJoinMan = (MarqueeView) findViewById(R.id.inviteJoinMan);
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);
        inviteTopAdapter = new InviteTopAdapter();
        delegateAdapter.addAdapter(inviteTopAdapter);
        inviteTopAdapter.setOutClickListener(this);

        inviteCenterAdapter = new InviteCenterAdapter();
        delegateAdapter.addAdapter(inviteCenterAdapter);
        inviteCenterAdapter.setOutClickListener(this);
//        inviteCenterAdapter.setModel("");
//        inviteCenterWithMineAdapter=new InviteCenterWithMineAdapter();
//        delegateAdapter.addAdapter(inviteCenterWithMineAdapter);
//        inviteCenterWithMineAdapter.setModel("");
        inviteBottomTopAdapter = new InviteBottomTopAdapter();
        delegateAdapter.addAdapter(inviteBottomTopAdapter);
//        inviteBottomTopAdapter.setModel("");


//        inviteBottomItemEmptyAdapter.setModel("");

        inviteBottomItemAdapter = new InviteBottomItemAdapter();
        delegateAdapter.addAdapter(inviteBottomItemAdapter);

        inviteBottomItemUnderAdapter = new InviteBottomItemUnderAdapter();
        delegateAdapter.addAdapter(inviteBottomItemUnderAdapter);

        inviteBottomItemEmptyAdapter = new InviteBottomItemEmptyAdapter();
        delegateAdapter.addAdapter(inviteBottomItemEmptyAdapter);

        recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(final @NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e != null) {
                    //找到被点击位置的item的rootView
                    View view = rv.findChildViewUnder(e.getX(), e.getY());

                    if (view != null) {
                        if (rv.getChildViewHolder(view) instanceof InviteCenterAdapter.InviteCenterScrollViewHolder) {
                            InviteCenterAdapter.InviteCenterScrollViewHolder holder = (InviteCenterAdapter.InviteCenterScrollViewHolder) rv.getChildViewHolder(view);
                            //由ViewHolder决定要不要请求不拦截,如果不拦截的话event就回一路传到rootView中.否则被rv消费.
                            rv.requestDisallowInterceptTouchEvent(holder.isTouchNsv(e.getRawX(), e.getRawY()));
                        }
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusTxtWhite(true);//UI说这个界面状态栏文字最好是白色  不然不好看
        initView();
    }

    public void setStatusTxtWhite(boolean bDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (bDark) {
                StatusBarTxtColorUtil.transparencyBar(this);
            }
            if (bDark) {
                StatusBarTxtColorUtil.setLightStatusBar(this, true, true);
            }
        }
    }

    @Override
    public void onSuccessSubMerchantInvite(String result) {
        if (result != null) {
            SeckShareDialog dialog = SeckShareDialog.newInstance();
            dialog.setActivityDialog(6, 0, null);
            dialog.setMerchantShopId(merchantId, shopId);
            dialog.setmPostImgUrl(inviteAct.activity.pic);
            dialog.setExtraMap(new SimpleHashMapBuilder<String, String>()
                    .puts("inviteActivityId", inviteAct.activity.id)
                    .puts("inviteMemberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                    .puts("merchantId", merchantId)
                    .puts("shopId", shopId)
                    .puts("scheme", "GiftsForSharing")
                    .puts("type", "8")
            );
            dialog.show(getSupportFragmentManager(), "分享");
        }

    }

    @Override
    public void onSuccessGetMerchantInvite(InviteAct inviteAct) {
        if (inviteAct != null) {
            this.inviteAct = inviteAct;
            showContent();
            bindDateToView();
        } else {
            showEmpty();
        }
    }

    boolean isStart = false;

    private void bindDateToView() {
        inviteTopAdapter.setModel(inviteAct);
        inviteCenterAdapter.setModel(inviteAct);
        inviteBottomTopAdapter.setModel(inviteAct);
        inviteBottomItemEmptyAdapter.setModel("");
        inviteBottomItemUnderAdapter.setModel(null);
        if (inviteAct.inviteJoinInfoRank != null && inviteAct.inviteJoinInfoRank.size() > 0) {
            inviteBottomItemEmptyAdapter.setModel(null);
            inviteBottomItemUnderAdapter.setModel("null");
            inviteBottomItemAdapter.setData(inviteAct.inviteJoinInfoRank);
        }


    }

    @Override
    public void onSuccessGetMerchantInviteDetailJoinList(List<InviteJoinInfo> inviteJoinInfos) {

    }

    @Override
    public void outClick(@NonNull String function, @NonNull Object obj) {
        if ("邀请新人".equals(function)) {
            if (inviteAct != null) {
                invitePresenter.subMerchantInvite(new SimpleHashMapBuilder<String, Object>()
                        .puts(Functions.FUNCTION, "invite_10003")
                        .puts("activityId", inviteAct.activity.id)
                );
            }
        }
        if ("活动规则".equals(function)) {
            if (inviteAct != null) {
                StringDialog.newInstance(new SimpleHashMapBuilder<String, Object>().puts("layout", R.layout.layout_string_dialog_pink))
                        .setTitle("活动规则")
                        .setUrl(inviteAct.activity.comment)
                        .show(getSupportFragmentManager(), "活动规则");
            }
        }
    }
}
