package com.health.discount.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.discount.R;
import com.health.discount.adapter.NewKickFooterAdapter;
import com.health.discount.adapter.NewKickHeaderAdapter;
import com.health.discount.contract.NewKickListContract;
import com.health.discount.presenter.NewKickListPresenter;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.Kick;
import com.healthy.library.model.MemberAction;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.presenter.ActionPresenterCopy;
import com.healthy.library.presenter.CardBoomPresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = DiscountRoutes.DIS_NEWKICKLIST)//砍价列表
public class NewKickListActivity extends BaseActivity implements OnRefreshLoadMoreListener, IsFitsSystemWindows, NewKickListContract.View {
    @Autowired
    String addressProvince;
    @Autowired
    String lng;
    @Autowired
    String lat;
    @Autowired
    String shopId;

    private ImageView img_back;
    private AutoClickImageView mall_scrollUp;
    private NewKickHeaderAdapter newKickHeaderAdapter;
    private NewKickFooterAdapter newKickFooterAdapter;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recycler;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    private NewKickListPresenter kickListPresenter;
    private Map<String, Object> map = new HashMap<>();
    private int currentPage = 1;
    private ImageView shareImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        kickListPresenter = new NewKickListPresenter(this, this);
        init(savedInstanceState);
        addressProvince = LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE);
        lng = LocUtil.getLongitude(mContext, SpKey.LOC_ORG);
        lat = LocUtil.getLatitude(mContext, SpKey.LOC_ORG);
        if(TextUtils.isEmpty(shopId)){
            shopId = SpUtils.getValue(mContext,SpKey.CHOSE_SHOP);
        }
        new CardBoomPresenter(mContext).boom("2");
        EventBus.getDefault().register(this);
        new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                new SimpleHashMapBuilder<>()
                        .putObject(new MemberAction(
                                BuildConfig.VERSION_NAME,
                                1,
                                5,
                                getActivitySimpleName(),
                                getActivitySimpleName(),
                                new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "砍价活动列表页浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000);
        MobclickAgent.onEvent(mContext, "event_APP_KanList_Stop", nokmap);

        if(mall_scrollUp != null){
            mall_scrollUp.destroy();
        }
    }

    private long mills = System.currentTimeMillis();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_kick_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        virtualLayoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);

        newKickHeaderAdapter = new NewKickHeaderAdapter();
        delegateAdapter.addAdapter(newKickHeaderAdapter);

        newKickFooterAdapter = new NewKickFooterAdapter();
        delegateAdapter.addAdapter(newKickFooterAdapter);

        layoutRefresh.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        map.put("currentPage", currentPage + "");
        map.put("pageSize", 10 + "");
        map.put("addressProvince", addressProvince + "");
        map.put("longitude", lng + "");
        map.put("latitude", lat + "");
        kickListPresenter.getKickTopList(map);
        map.put("faceUrlNum", "3");//获取头像的数量
        map.put("shopId",shopId);
        kickListPresenter.getKickList(map);

    }


    @Override
    public void onSuccessGetTopKickList(List<Kick> kickList, PageInfoEarly pageInfoEarly) {
        if (kickList == null || kickList.size() == 0) {
            newKickHeaderAdapter.setModel(null);
            return;
        }
        List dataList = new ArrayList();
        dataList.clear();
        dataList.add(kickList);
        newKickHeaderAdapter.setData((ArrayList) dataList);
    }

    @Override
    public void onSuccessGetKickList(List<Kick> kickList, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            newKickFooterAdapter.setModel(null);
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if (currentPage == 1 || currentPage == 0) {
            newKickFooterAdapter.setData((ArrayList) kickList);
        } else {
            newKickFooterAdapter.addDatas((ArrayList) kickList);
        }
        if (pageInfoEarly.isMore != 1) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        img_back = findViewById(R.id.img_back);
        layoutRefresh = findViewById(R.id.layout_refresh);
        recycler = findViewById(R.id.recycler);
        mall_scrollUp = findViewById(R.id.mall_scrollUp);
        shareImg = findViewById(R.id.shareImg);
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "event2APPKanListShareClick",
                        new SimpleHashMapBuilder<String, String>().puts("soure", "砍价活动列表顶部分享按钮点击量"));

                SeckShareDialog dialog = SeckShareDialog.newInstance();
                //dialog.setBitmap(3, bitmap);
                dialog.setActivityDialog(3, 1, ListUtil.isEmpty(newKickFooterAdapter.getDatas()) ? null : newKickFooterAdapter.getDatas().get(0));
                dialog.show(getSupportFragmentManager(), "分享");
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mall_scrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_MYKICKACTIVITY)
                        .withString("addressProvince", addressProvince)
                        .withString("lat", lat)
                        .withString("lng", lng)
                        .navigation();
            }
        });
        mall_scrollUp.setClicktype("null");
        mall_scrollUp.startLoopScaleAuto();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage = 1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }
}