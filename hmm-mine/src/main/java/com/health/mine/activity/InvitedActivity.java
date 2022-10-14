package com.health.mine.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.health.mine.R;
import com.health.mine.adapter.InviteBottomGoodsAdapter;
import com.health.mine.adapter.InviteBottomGoodsEmptyAdapter;
import com.health.mine.adapter.InviteCenterWithMineAdapter;
import com.health.mine.adapter.InviteCenterWithMineEmptyAdapter;
import com.health.mine.adapter.InviteTopWithMineEmptyAdapter;
import com.health.mine.contract.InviteContract;
import com.health.mine.presenter.InvitePresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.NormalGoodsContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.InviteAct;
import com.healthy.library.model.InviteJoinInfo;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.ServiceTabChangeModel;
import com.healthy.library.model.TabChangeModel;
import com.healthy.library.presenter.NormalGoodsPresenter;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StatusBarTxtColorUtil;
import com.healthy.library.widget.StringDialog;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Route(path = MineRoutes.MINE_INVITED)//被邀请页面
public class InvitedActivity extends BaseActivity implements OnRefreshLoadMoreListener,IsFitsSystemWindows, InviteContract.View, BaseAdapter.OnOutClickListener, NormalGoodsContract.View {

    private TopBar topBar;
    private RecyclerView recycler;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    //    InviteBottomItemAdapter inviteBottomItemAdapter;
//    InviteBottomItemEmptyAdapter inviteBottomItemEmptyAdapter;
//    InviteBottomTopAdapter inviteBottomTopAdapter;
//    InviteCenterAdapter inviteCenterAdapter;
//    InviteTopAdapter inviteTopAdapter;
    InviteBottomGoodsAdapter inviteBottomGoodsAdapter;
    InviteBottomGoodsEmptyAdapter inviteBottomGoodsEmptyAdapter;
    InviteCenterWithMineAdapter inviteCenterWithMineAdapter;
    InviteCenterWithMineEmptyAdapter inviteCenterWithMineEmptyAdapter;
    InviteTopWithMineEmptyAdapter inviteTopWithMineEmptyAdapter;
    InviteAct inviteAct;
    InvitePresenter invitePresenter;
    NormalGoodsPresenter normalGoodsPresenter;
    private ImageView goInvite;
    public int recommandType=1;
    public int page =1;
    public int pageSize=15;
    public int firstPageSize=0;

    @Autowired
    String inviteMemberId;//邀请人id
    @Autowired
    String merchantId;
    @Autowired
    String inviteActivityId;
    @Autowired
    String shopId;
    private SmartRefreshLayout refreshLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_invate;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        topBar.setTitle("接受邀请");
        if(inviteMemberId.contains("==")){
            inviteMemberId=new String(Base64.decode(inviteMemberId.getBytes(), Base64.DEFAULT));
        }
        invitePresenter = new InvitePresenter(this, this);
        normalGoodsPresenter = new NormalGoodsPresenter(this, this);
        invitePresenter.subMerchantInvite(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION,"invite_10000")
                .puts("activityId",inviteActivityId)
                .puts("inviteMemberId",inviteMemberId)
                .puts("inviteSource","1")
        );

    }

    @Override
    public void getData() {
        super.getData();
        getTopData();
    }
    public void getTopData(){
        firstPageSize=0;
        invitePresenter.getMerchantInvite(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION, "invite_10001")
                .puts("activityId", inviteActivityId));
    }
    public void getUnderData(){
        normalGoodsPresenter.getGoodsRecommend(new SimpleHashMapBuilder<String, Object>()
                .puts("shopId",shopId)
                .puts("type",recommandType+"")
                .puts("pageNum", page +"")
                .puts("pageSize",pageSize+"")
                .puts("firstPageSize",firstPageSize+"")
        );

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();

    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        buildRecyclerView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        goInvite = (ImageView) findViewById(R.id.goInvite);
        goInvite.setVisibility(View.GONE);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);
//        inviteTopAdapter = new InviteTopAdapter();
//        delegateAdapter.addAdapter(inviteTopAdapter);
//        inviteTopAdapter.setOutClickListener(this);
//
//        inviteCenterAdapter = new InviteCenterAdapter();
//        delegateAdapter.addAdapter(inviteCenterAdapter);
//        inviteCenterAdapter.setOutClickListener(this);

        inviteTopWithMineEmptyAdapter = new InviteTopWithMineEmptyAdapter();
        delegateAdapter.addAdapter(inviteTopWithMineEmptyAdapter);
        inviteTopWithMineEmptyAdapter.setOutClickListener(this);

        inviteCenterWithMineEmptyAdapter = new InviteCenterWithMineEmptyAdapter();
        delegateAdapter.addAdapter(inviteCenterWithMineEmptyAdapter);
        inviteCenterWithMineAdapter = new InviteCenterWithMineAdapter();
        delegateAdapter.addAdapter(inviteCenterWithMineAdapter);
        inviteCenterWithMineAdapter.setOutClickListener(this);
//        inviteCenterWithMineAdapter.setModel("");
//        inviteBottomTopAdapter = new InviteBottomTopAdapter();
//        delegateAdapter.addAdapter(inviteBottomTopAdapter);
//        inviteBottomTopAdapter.setModel("");

//        inviteBottomItemEmptyAdapter = new InviteBottomItemEmptyAdapter();
//        delegateAdapter.addAdapter(inviteBottomItemEmptyAdapter);
//        inviteBottomItemEmptyAdapter.setModel("");
        inviteBottomGoodsEmptyAdapter=new InviteBottomGoodsEmptyAdapter();
        delegateAdapter.addAdapter(inviteBottomGoodsEmptyAdapter);
        inviteBottomGoodsAdapter = new InviteBottomGoodsAdapter();
        delegateAdapter.addAdapter(inviteBottomGoodsAdapter);

        recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(final @NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e != null) {
                    //找到被点击位置的item的rootView
                    View view = rv.findChildViewUnder(e.getX(), e.getY());

                    if (view != null) {
                        if(rv.getChildViewHolder(view) instanceof InviteCenterWithMineAdapter.InviteScrollViewHolder){
                            InviteCenterWithMineAdapter.InviteScrollViewHolder holder = (InviteCenterWithMineAdapter.InviteScrollViewHolder) rv.getChildViewHolder(view);
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

    String inviteMsg="您已经是会员";
    @Override
    public void onSuccessSubMerchantInvite(String result) {
        if(result!=null){
            try {
                inviteMsg = new JSONObject(result).optString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getData();
    }

    @Override
    public void onSuccessGetMerchantInvite(InviteAct inviteAct) {
//        inviteTopWithMineEmptyAdapter.setModel("");
        if(inviteAct!=null){
            this.inviteAct=inviteAct;
            boolean isBefore= false;
            try {
                isBefore = new Date().before(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(inviteAct.activity.endTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(isBefore){//还没过期
                bindDateToView();
            }else {
                inviteCenterWithMineEmptyAdapter.setModel("");//活动结束
            }
        }else{
            inviteCenterWithMineEmptyAdapter.setModel("");//活动结束
        }
    }

    private void bindDateToView() {//活动正常按照新客老客判断吧
        if(inviteAct.activity!=null&&inviteAct.activity.inviteTargetRewardList.size()>0){//存在被邀请奖励 就要区分新老客了
            if(inviteMsg!=null&&(inviteMsg.contains("您已参加活动")||inviteMsg.contains("邀请成功"))){
                inviteCenterWithMineAdapter.setUnderString("您已获得邀请奖励，您也可以邀请更多好友~");
            }else {
                inviteCenterWithMineAdapter.setUnderString("仅限新用户接受邀请哦，您也可以邀请更多好友~");
            }
            inviteCenterWithMineAdapter.setModel(inviteAct);
            getUnderData();
        }else {
            if(inviteMsg!=null&&(inviteMsg.contains("您已参加活动")||inviteMsg.contains("邀请成功"))){
                inviteTopWithMineEmptyAdapter.setModel("您已接受了好友邀请，您也可以邀请更多好友~");
            }else {
                inviteTopWithMineEmptyAdapter.setModel("仅限新用户接受邀请，您也可以邀请更多好友~");
            }
        }
    }

    @Override
    public void onSuccessGetMerchantInviteDetailJoinList(List<InviteJoinInfo> inviteJoinInfos) {

    }

    @Override
    public void outClick(@NonNull String function, @NonNull Object obj) {
        if ("邀请新人".equals(function)) {
            ARouter.getInstance().build(MineRoutes.MINE_INVITE)
                    .withString("inviteActivityId",inviteActivityId)
                    .withString("shopId",shopId)
                    .withString("merchantId",merchantId)
                    .navigation();
            finish();
        }
        if ("活动规则".equals(function)) {
            if (inviteAct != null) {
                StringDialog.newInstance(new SimpleHashMapBuilder<String, Object>().puts("layout", R.layout.layout_string_dialog_pink))
                        .setTitle("活动规则")
                        .setUrl(inviteAct.activity.comment)
                        .show(getSupportFragmentManager(), "活动规则");
            }
        }
        if ("去使用".equals(function)) {
            finish();
            EventBus.getDefault().post(new TabChangeModel(3));
            EventBus.getDefault().post(new ServiceTabChangeModel(1));
        }
    }

    @Override
    public void successGetGoodsRecommend(List<RecommendList> result,int firstPageSize) {
        inviteBottomGoodsEmptyAdapter.setModel(null);
        if (result == null || result.size() == 0) {
            refreshLayout.finishLoadMoreWithNoMoreData();
            if (page == 1 || page == 0) {
                if(recommandType==1){
                    recommandType=2;
                    getUnderData();
                    return;
                }
                if(recommandType==2){
                    recommandType=3;
                    getUnderData();
                    return;
                }
                if(recommandType==3){
                    recommandType=4;
                    getUnderData();
                    return;
                }
                if(recommandType==4){
                    recommandType=5;
                    getUnderData();
                    return;
                }
                if(recommandType==5){
                    inviteBottomGoodsEmptyAdapter.setModel("");
                    return;
                }
            }
        } else {
            this.firstPageSize = firstPageSize;
            if (page == 1) {
                ArrayList<MultiItemEntity> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                inviteBottomGoodsAdapter.setData(tmp);
            } else {
                ArrayList<MultiItemEntity> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                inviteBottomGoodsAdapter.addDatas(tmp);
            }
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getUnderData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page=0;
        getData();
    }
}
