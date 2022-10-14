package com.health.discount.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.discount.R;
import com.health.discount.adapter.PointBlockHomeBlockAdapter;
import com.health.discount.adapter.PointBlockHomeRecommendAdapter;
import com.health.discount.adapter.PointBlockHomeRecommendTitleAdapter;
import com.health.discount.adapter.PointBlockHomeTopAdapter;
import com.health.discount.adapter.PointBlockHomeTopBannerAdapter;
import com.health.discount.contract.PointBlockContract;
import com.health.discount.contract.PointHomeContract;
import com.health.discount.model.PointOption;
import com.health.discount.model.PointTab;
import com.health.discount.model.UserPoint;
import com.health.discount.presenter.PointBlockPresenter;
import com.health.discount.presenter.PointHomePresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.IntegralModel;
import com.healthy.library.model.LotteryModel;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Route(path = DiscountRoutes.DIS_POINTHOME)
public class PointBlockHomeActivity extends BaseActivity implements BaseAdapter.OnOutClickListener, IsFitsSystemWindows, OnRefreshLoadMoreListener, PointHomeContract.View, PointBlockContract.View {

    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    PointBlockHomeTopAdapter pointBlockHomeTopAdapter;
    PointBlockHomeTopBannerAdapter pointBlockHomeTopBannerAdapter;
    PointBlockHomeRecommendTitleAdapter pointBlockHomeRecommendTitleAdapter;
    PointBlockHomeRecommendAdapter pointBlockHomeRecommendAdapter;
    PointBlockHomeBlockAdapter pointBlockHomeBlockAdapter;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private androidx.recyclerview.widget.RecyclerView recyclerQuestion;
    PointHomePresenter pointHomePresenter;
    int page = 1;
    private LotteryModel mLotteryModel;
    private String mLotteryId;
    private long mLotteryEndTime = 0;

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dis_activity_point_home;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        pointHomePresenter = new PointHomePresenter(this, this);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        buildRecyclerView();
        getData();

    }

    @Override
    public void getData() {
        super.getData();

        pointHomePresenter.getVipInfo(new SimpleHashMapBuilder<String, Object>());
        pointHomePresenter.getPointOption(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION, "9118")
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
        pointHomePresenter.getUserInfo();
        pointHomePresenter.getBannerTop(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "17"));
        pointHomePresenter.getBannerCenter(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "18"));
        pointHomePresenter.getPointTab(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION, "9114")
                .puts("themeType", 2 + "")
                .puts("isThemeShow", 1 + "")
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pointHomePresenter.getRecommend(new SimpleHashMapBuilder<String, Object>()
                        .puts(Functions.FUNCTION, "9117")
                        .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                        .puts("pageNum", page + "")
                        .puts("pageSize", 10 + "")
                        .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
            }
        }, 1000);
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerQuestion.setLayoutManager(virtualLayoutManager);
        recyclerQuestion.setAdapter(delegateAdapter);

        pointBlockHomeTopAdapter = new PointBlockHomeTopAdapter();
        delegateAdapter.addAdapter(pointBlockHomeTopAdapter);
        pointBlockHomeTopAdapter.setOutClickListener(this);
        pointBlockHomeTopBannerAdapter = new PointBlockHomeTopBannerAdapter();
        delegateAdapter.addAdapter(pointBlockHomeTopBannerAdapter);
//        pointBlockHomeTopBannerAdapter.setModel("");
        pointBlockHomeBlockAdapter = new PointBlockHomeBlockAdapter();
        delegateAdapter.addAdapter(pointBlockHomeBlockAdapter);
        pointBlockHomeBlockAdapter.setOutClickListener(this);
//        pointBlockHomeBlockAdapter.setModel("");
        pointBlockHomeRecommendTitleAdapter = new PointBlockHomeRecommendTitleAdapter();
        delegateAdapter.addAdapter(pointBlockHomeRecommendTitleAdapter);
        pointBlockHomeRecommendTitleAdapter.setModel("");
        pointBlockHomeRecommendAdapter = new PointBlockHomeRecommendAdapter();
        delegateAdapter.addAdapter(pointBlockHomeRecommendAdapter);
//        pointBlockHomeRecommendAdapter.setData(new SimpleArrayListBuilder<String>().adds("").adds(""));

    }

    private void initView() {
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        pointHomePresenter.getRecommend(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION, "9117")
                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                .puts("pageNum", page + "")
                .puts("pageSize", 10 + "")
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
        );
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        getData();

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
    }

    @Override
    public void onPointBlockSuccess() {
        pointBlockHomeBlockAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVipInfoSuccess(IntegralModel vipInfo) {
        pointBlockHomeTopAdapter.setModel(vipInfo);
    }

    @Override
    public void onPointTabSuccess(List<PointTab> pointTab) {
        pointBlockHomeBlockAdapter.setData((ArrayList<PointTab>) pointTab);
        if (pointTab != null && pointTab.size() > 0) {
            for (int i = 0; i < pointTab.size(); i++) {
                PointTab pointTabChild = pointTab.get(i);
                PointBlockPresenter pointBlockPresenter = new PointBlockPresenter(this, this);
                pointBlockPresenter.getPointTabContent(new SimpleHashMapBuilder<String, Object>()
                                .puts(Functions.FUNCTION, "9115")
                                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                                .puts("themeId", pointTabChild.id)
                                .puts("filterSoldOut", "1")
                                .puts("sortType", "1")
                                .puts("pageNum", 1 + "")
                                .puts("pageSize", 6 + "")
                        , pointTab.get(i));
            }
        }

    }

    @Override
    public void onPointOptionSuccess(List<PointOption> optionList) {
        if (optionList != null && optionList.size() >= 5) {
            List<PointOption> optionListTmp = new ArrayList<>();
            for (int i = 0; i < optionList.size(); i++) {
                optionListTmp.add(optionList.get(i));
            }
            pointBlockHomeTopAdapter.setOptionList(optionListTmp);
        } else {
            //System.out.println("没内容");
//            showEmpty();
        }

    }

    @Override
    public void onPointRecommendSuccess(List<PointTab.PointGoods> result) {
        if (result == null) {
            if (page == 1 || page == 0) {
                pointBlockHomeRecommendTitleAdapter.setModel(null);
                pointBlockHomeRecommendAdapter.setModel(null);
                layoutRefresh.finishLoadMoreWithNoMoreData();
            }
            return;
        }
        onRequestFinish();
        if (result.size() == 0) {
            //System.out.println("目前推荐的长度");
            layoutRefresh.finishLoadMoreWithNoMoreData();
            if (page == 1 || page == 0) {
                pointBlockHomeRecommendTitleAdapter.setModel(null);
                pointBlockHomeRecommendAdapter.setModel(null);
            }
        } else {
            if (page == 1) {
                pointBlockHomeRecommendAdapter.setData((ArrayList<PointTab.PointGoods>) result);
                //System.out.println("目前推荐的长度设置数据");
            } else {
                pointBlockHomeRecommendAdapter.addDatas((ArrayList<PointTab.PointGoods>) result);
            }
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void onGetUserInfoSuccess(UserPoint userInfoModel) {
        pointBlockHomeTopAdapter.setUserInfo(userInfoModel);

    }

    @Override
    public void onSucessGetTopAds(List<AdModel> adModels) {
        pointBlockHomeTopBannerAdapter.setModel(null);
        if (adModels.size() > 0) {
            pointBlockHomeTopBannerAdapter.setModel("");
            pointBlockHomeTopBannerAdapter.setAdModelList(adModels);
        }
    }

    @Override
    public void onSucessGetCenterAds(List<AdModel> adModels) {
        if (adModels.size() > 0) {
            pointBlockHomeBlockAdapter.setAdModelList(adModels);
        }
    }

    /**
     * 积分抽奖活动信息
     *
     * @param lotteryModel
     */
    @Override
    public void onLotteryInfoSuccess(LotteryModel lotteryModel) {
        this.mLotteryModel = lotteryModel;
        this.mLotteryId = lotteryModel.getId();
        this.mLotteryEndTime = DateUtils.str2Long(lotteryModel.getEndTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
        String url = "http://192.168.10.181:8000/lottery.html";
        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, UrlKeys.H5_LOTTERY_URL))) {
            url = SpUtils.getValue(mContext, UrlKeys.H5_LOTTERY_URL);
        }
        //跳转领奖中心下单页
        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEWSINGLE)
                //78434178651668480 -> 大转盘
                //78448856115200000 -> 九宫格
                .withString("url", url + "?id=" + mLotteryModel.getId() + "&token=" + SpUtils.getValue(mContext, SpKey.TOKEN))
                .withString("title", "")
                .withBoolean("isShowTopBar", false)
                .navigation();
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("兑换明细".equals(function)) {
            MobclickAgent.onEvent(mContext, "event2APPPointsShopExchangeDetailsClick", new SimpleHashMapBuilder<String, String>().puts("type", "积分商城兑换明细按钮点击量"));
            ARouter.getInstance().build(MineRoutes.MINE_VIPINTEGRALACTIVITY)
                    .navigation();
        }
        if ("我能兑换".equals(function)) {
            MobclickAgent.onEvent(mContext, "event2APPPointsShopICanExchangeClick", new SimpleHashMapBuilder<String, String>().puts("type", "积分商城我能兑换点击量"));
            ARouter.getInstance()
                    .build(DiscountRoutes.DIS_POINTICAN)
                    .navigation();
        }

        if ("积分抽奖".equals(function)) {
            //活动id为空 或 当前时间大于活动结束时间 都需要重新请求
            if (TextUtils.isEmpty(mLotteryId) || System.currentTimeMillis() > mLotteryEndTime) {
                pointHomePresenter.getLotteryInfo(new SimpleHashMapBuilder<String, Object>()
                        .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
                return;
            }
            onLotteryInfoSuccess(mLotteryModel);
        }

        if ("专区列表".equals(function)) {
            String[] array = (String[]) obj;
            ARouter.getInstance()
                    .build(DiscountRoutes.DIS_POINTBLOCK)
                    .withString("themeId", array[0])
                    .withString("themeIdName", array[1])
                    .navigation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "积分商城首页浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000);
        MobclickAgent.onEvent(mContext, "event_APP_PointsShop_Stop", nokmap);
    }

    private long mills = System.currentTimeMillis();
}
