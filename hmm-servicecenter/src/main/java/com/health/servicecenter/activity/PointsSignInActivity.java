package com.health.servicecenter.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallGoodsItemAdapter;
import com.health.servicecenter.adapter.MallGoodsTitleAdapter;
import com.health.servicecenter.adapter.PointsSignInDateAdapter;
import com.health.servicecenter.adapter.PointsSignInGoodsAdapter;
import com.health.servicecenter.adapter.PointsSignInHeaderAdapter;
import com.health.servicecenter.contract.LotteryContract;
import com.health.servicecenter.contract.PointsSignInContract;
import com.health.servicecenter.model.PointsSignInModel;
import com.health.servicecenter.model.RecommendModel;
import com.health.servicecenter.presenter.LotteryPresenter;
import com.health.servicecenter.presenter.PointsSignInPresenter;
import com.healthy.library.adapter.BaseTitleAdapter;
import com.healthy.library.adapter.EmptyAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.dialog.PointsSignInLotteryDialog;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.LotteryInfoModel;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.TitleModel;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Long
 * @date 2021/04/21
 */
@Route(path = ServiceRoutes.SERVICE_POINTSSIGNIN)
public class PointsSignInActivity extends BaseActivity implements IsFitsSystemWindows, PointsSignInContract.View,
        OnRefreshLoadMoreListener, BaseAdapter.OnOutClickListener, LotteryContract.View {

    @Autowired
    String merchantId = "";

    @Autowired
    String shopId = "";

    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recycler;

    private PointsSignInPresenter pointsSignInPresenter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private PointsSignInHeaderAdapter mHeaderAdapter;
    private PointsSignInDateAdapter mSignInDateAdapter;
    private BaseTitleAdapter mPointsTitleAdapter;
    private PointsSignInGoodsAdapter mGoodsAdapter;
    private MallGoodsTitleAdapter mallGoodsTitleAdapter;
    private MallGoodsItemAdapter mallGoodsItemAdapter;
    private EmptyAdapter mEmptyAdapter;
    private TitleModel pointsTitle;
    private int page = 1;
    private int firstPageSize = 0;
    private Map<String, Object> map = new HashMap<>();
    private int mWeek;
    private EmptyAdapter mGoodsEmptyAdapter;
    private View mIvBack;
    private ImageView mIvShare;
    private LotteryPresenter mLotteryPresenter;
    private LotteryInfoModel mLotteryInfoModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_points_sign_in;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        pointsSignInPresenter = new PointsSignInPresenter(this, this);
        mLotteryPresenter = new LotteryPresenter(this, this);

        if (TextUtils.isEmpty(merchantId)) {
            merchantId = SpUtils.getValue(mContext, SpKey.CHOSE_MC);
        }
        if (TextUtils.isEmpty(shopId)) {
            shopId = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP);
        }

        Calendar calendar = Calendar.getInstance();
        //获取当天星期几
        mWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        getData();
    }

    @Override
    protected void findViews() {
        super.findViews();
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        setStatusLayout(layoutStatus);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutRefresh.finishLoadMoreWithNoMoreData();
        recycler = (RecyclerView) findViewById(R.id.recycler);
        mIvBack = findViewById(R.id.img_back);
        mIvShare = findViewById(R.id.img_share);
        initRecycleView();
        initListener();
    }

    @Override
    public void getData() {
        super.getData();
        //仅在刷新时调用
        if (page == 1) {
            map.clear();
            map.put("merchantId", merchantId);
            map.put("shopId", shopId);
            map.put("triggerType", 3);//签到抽奖类型
            mLotteryPresenter.getLotteryInfo(map);

            /**** 获取兑换商品列表 START *****/
            map.clear();
            map.put("merchantId", merchantId);
            map.put("shopId",shopId);
            pointsSignInPresenter.getRecommend(map);
        }

        /**** 获取为您精选商品 START *****/
        map.clear();
        map.put("pageNum", String.valueOf(page));
        map.put("firstPageSize", String.valueOf(firstPageSize));
        map.put("shopId", shopId);
        pointsSignInPresenter.getGoodsList(map);
    }

    private void initRecycleView() {
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);

        /**header*/
        mHeaderAdapter = new PointsSignInHeaderAdapter();
        delegateAdapter.addAdapter(mHeaderAdapter);

        mSignInDateAdapter = new PointsSignInDateAdapter();
        delegateAdapter.addAdapter(mSignInDateAdapter);

        /**积分兑换标题*/
        mPointsTitleAdapter = new BaseTitleAdapter();
        delegateAdapter.addAdapter(mPointsTitleAdapter);
        pointsTitle = new TitleModel()
                .setTitle("积分兑换")
                .setRightTitle("查看更多商品")
                .setDrawableRightShow(true)
                .setSolidColor(Color.WHITE);
        mPointsTitleAdapter.setModel(pointsTitle);

        /**积分兑换*/
        mGoodsAdapter = new PointsSignInGoodsAdapter();
        delegateAdapter.addAdapter(mGoodsAdapter);
        /**积分兑换空数据*/
        mGoodsEmptyAdapter = new EmptyAdapter();
        delegateAdapter.addAdapter(mGoodsEmptyAdapter);
        mGoodsEmptyAdapter.setmBackgroundColor(R.color.color_f9f9f9);

        /**商品精选title*/
        mallGoodsTitleAdapter = new MallGoodsTitleAdapter();
        delegateAdapter.addAdapter(mallGoodsTitleAdapter);
        mallGoodsTitleAdapter.setTitleBgColor(R.color.color_f9f9f9);
        mallGoodsTitleAdapter.setModel("为您精选");

        /**商品精选item*/
        mallGoodsItemAdapter = new MallGoodsItemAdapter();
        delegateAdapter.addAdapter(mallGoodsItemAdapter);
        mallGoodsItemAdapter.setKey("购物车帮你挑");

        /**空数据 item*/
        mEmptyAdapter = new EmptyAdapter();
        delegateAdapter.addAdapter(mEmptyAdapter);
        mEmptyAdapter.setmBackgroundColor(R.color.color_f9f9f9);
    }

    private void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeckShareDialog seckShareDialog = new SeckShareDialog();
                seckShareDialog.setMerchantShopId(merchantId, shopId);
                seckShareDialog.setActivityDialog(9, 0, null);
                seckShareDialog.show(getSupportFragmentManager(), "pointsSignInShareDialog");
            }
        });
        //baseTitleAdapter.setMoutClickListener(this);
        mHeaderAdapter.setOutClickListener(this);
        mSignInDateAdapter.setOutClickListener(this);
        mPointsTitleAdapter.setOutClickListener(this);
        mGoodsAdapter.setOutClickListener(this);
        mallGoodsItemAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
            @Override
            public void outClick(@NotNull String function, @NotNull Object obj) {
                if ("addShopCat".equals(function)) {
                    RecommendList goodsListModel = (RecommendList) obj;
                    if (goodsListModel != null) {
                        map.clear();
                        map.put("shopId", shopId);
                        map.put("goodsShopId", goodsListModel.getShopId());
                        map.put("goodsSource", "1");
                        map.put("goodsType", goodsListModel.goodsType + "");
                        map.put("goodsId", goodsListModel.goodsId + "");
                        map.put("goodsSpecId", goodsListModel.skuId);
                        map.put("goodsQuantity", "1");
                        pointsSignInPresenter.addShopCat(map);
                    }
                }
            }
        });
    }

    @Override
    public void onLotteryInfoSuccess(@Nullable LotteryInfoModel lotteryInfoModel) {
        mLotteryInfoModel = lotteryInfoModel;
        /**** 获取签到回显数据 START *****/
        map.clear();
        //进入页面直接获取签到数据
        map.put("signWeek", String.valueOf(mWeek));
        map.put("shopId", shopId);
        pointsSignInPresenter.getSignInData(map);
    }

    @Override
    public void onCheckLotterySuccess(int triggerPrivilege) {
        //签到成功后调用，如果返回 triggerPrivilege=1 则赠送了签到抽奖资格，
        if (triggerPrivilege == 1) {
            PointsSignInLotteryDialog.Companion.newInstance()
                    .setLotteryInfoModel(mLotteryInfoModel)
                    .show(getSupportFragmentManager(), "pointsSignInLotteryDialog");
        }
    }

    /**
     * 签到回显
     */
    @Override
    public void onGetSignInDataSuccess(PointsSignInModel model) {
        if (model == null) {
            return;
        }
        mHeaderAdapter.setLotteryInfoModel(mLotteryInfoModel);
        mHeaderAdapter.setModel(model);
        if (ListUtil.isEmpty(model.memberSignRecords)) {
            mSignInDateAdapter.setModel(null);
            return;
        }
        mSignInDateAdapter.setSignNum(model.signNum);
        mSignInDateAdapter.setData((ArrayList) model.memberSignRecords);
    }

    @Override
    public void onGetSignInSuccess() {
        showToast("签到成功");
        /**** 获取签到回显数据 START *****/
        map.clear();
        //进入页面直接获取签到数据
        map.put("signWeek", String.valueOf(mWeek));
        map.put("shopId", shopId);
        pointsSignInPresenter.getSignInData(map);

        if (mLotteryInfoModel != null) {
            /** 加入判断抽奖活动数据不为空的时候 查询是否获取抽奖资格 */
            map.clear();
            map.put("merchantId", merchantId);
            mLotteryPresenter.checkLottery(map);
        }
    }

    /**
     * 兑换商品
     *
     * @param result
     */
    @Override
    public void onGetRecommendListSuccess(List<RecommendModel.RecommendGoods> result) {

        /*if (ListUtil.isEmpty(result)) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.finishLoadMore();
            layoutRefresh.resetNoMoreData();
        }*/
        if (page == 1) {
            mGoodsAdapter.clear();
            mGoodsAdapter.setData((ArrayList<RecommendModel.RecommendGoods>) result);
        } else {
            mGoodsAdapter.addDatas((ArrayList<RecommendModel.RecommendGoods>) result);
        }
        if (ListUtil.isEmpty(mGoodsAdapter.getDatas())) {
            //mPointsTitleAdapter.setModel(null);
            //mGoodsAdapter.setModel(null);
            mGoodsEmptyAdapter.setModel("");
            return;
        }
        mGoodsEmptyAdapter.setModel(null);
        //mPointsTitleAdapter.setModel(pointsTitle);
    }

    /**
     * 为您精选列表回调
     *
     * @param list
     * @param firstPageSize
     */
    @Override
    public void onGetGoodsListSuccess(List<RecommendList> list, int firstPageSize) {
        layoutRefresh.finishRefresh();
        if (ListUtil.isEmpty(list)) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.finishLoadMore();
            layoutRefresh.resetNoMoreData();
        }

        if (page == 1) {
            this.firstPageSize = firstPageSize;
            mallGoodsItemAdapter.clear();
            mallGoodsItemAdapter.setData((ArrayList<RecommendList>) list);
        } else {
            mallGoodsItemAdapter.addDatas((ArrayList<RecommendList>) list);
        }

        if (ListUtil.isEmpty(mallGoodsItemAdapter.getDatas())) {
            mEmptyAdapter.setModel("");
            return;
        }
        mEmptyAdapter.setModel(null);
    }

    @Override
    public void addShopCatSuccess(String result) {
        showToast("已加入购物车");
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
        layoutRefresh.finishRefresh();
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {

        if ("签到".equals(function)) {
            //请求签到
            PointsSignInModel.MemberSignRecords memberSignRecords = (PointsSignInModel.MemberSignRecords) obj;
            map.clear();
            map.put("seriesSignNum", mHeaderAdapter.getModel().seriesSignNum);
            map.put("signIntegral", String.valueOf(memberSignRecords.signIntegral));
            map.put("signWeek", String.valueOf(memberSignRecords.signWeek));
            map.put("shopId", shopId);
            map.put("merchantId", merchantId);
            pointsSignInPresenter.signIn(map);
            return;
        }

        if ("积分商品".equals(function)) {
            RecommendModel.RecommendGoods goods = (RecommendModel.RecommendGoods) obj;
            //积分商品详情
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_DETAIL)
                    .withString("id", goods.id)
                    .withString("marketingType", "5")
                    .navigation();
            return;
        }

        if ("查看签到记录".equals(function)) {
            ARouter.getInstance()
                    .build(MineRoutes.MINE_VIPINTEGRALACTIVITY)
                    .navigation();
            return;
        }

        if ("查看更多商品".equals(function)) {
            ARouter.getInstance()
                    .build(DiscountRoutes.DIS_POINTHOME)
                    .navigation();
            return;
        }
//        showToast(function);
    }
}