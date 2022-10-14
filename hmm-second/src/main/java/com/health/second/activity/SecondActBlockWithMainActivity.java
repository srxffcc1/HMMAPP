package com.health.second.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.second.R;
import com.health.second.adapter.SecondBlockBarnnerAdapter;
import com.health.second.adapter.SecondBlockGoodsAdapter;
import com.health.second.contract.SecondBlockContract;
import com.health.second.contract.SecondBlockMainContract;
import com.health.second.presenter.SecondBlockPresenter;
import com.health.second.presenter.SecondBlockWithMainPresenter;
import com.healthy.library.adapter.EmptyAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.AppIndexCustom;
import com.healthy.library.model.AppIndexGoodsList;
import com.healthy.library.model.AppIndexList;
import com.healthy.library.model.AppIndexService;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.MainBlockDetailModel;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.presenter.CardBoomPresenter;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = SecondRoutes.MAIN_BLOCKWITHMAIN)
public class SecondActBlockWithMainActivity extends BaseActivity implements SecondBlockMainContract.View,OnRefreshLoadMoreListener, IsFitsSystemWindows, BaseAdapter.OnOutClickListener {

    @Autowired
    String id;
    private int page = 1;
    public int pageSize = 10;
    @Autowired
    String bannerUrl;
    @Autowired
    String mainTitle;

    private SecondBlockBarnnerAdapter secondBlockBarnnerAdapter;
    private SecondBlockGoodsAdapter secondBlockGoodsAdapter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout refreshLayout;
    private ImageView seachTopBgLL;
    private LinearLayout seachTop;
    private LinearLayout serarchKeyWordLL;
    private View seachTopUnder;
    private View seachTopTmp;
    private RecyclerView recyclerview;
    SecondBlockWithMainPresenter secondBlockPresenter;
    int alldy = 0;
    private Map<String, Object> mParams = new HashMap();
    private int mCurrentPage = 1;
    private String mSearchName;
    private EmptyAdapter mEmptyAdapter;
//    private ProgressBar mProgress;
    private TopBar topBar;
    private ProgressBar progressView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_blockwithmain;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.finishLoadMoreWithNoMoreData();
//        new CardBoomPresenter(mContext).boom("6");
        secondBlockPresenter = new SecondBlockWithMainPresenter(this, this);
        buildRecyclerView();
        getData();
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        initListener();
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
//        mProgress = findViewById(R.id.progress_view);
        seachTopBgLL = (ImageView) findViewById(R.id.seachTopBgLL);
        seachTop = (LinearLayout) findViewById(R.id.seachTop);
        serarchKeyWordLL = (LinearLayout) findViewById(R.id.serarchKeyWordLL);
        seachTopUnder = (View) findViewById(R.id.seachTopUnder);
        seachTopTmp = (View) findViewById(R.id.seachTopTmp);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        topBar = (TopBar) findViewById(R.id.top_bar);
        progressView = (ProgressBar) findViewById(R.id.progress_view);
    }

    private void initListener() {


        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //newState分 0,1,2三个状态,2是滚动状态,0是停止
                super.onScrollStateChanged(recyclerView, newState);
                //-1代表顶部,返回true表示没到顶,还可以滑
                //1代表底部,返回true表示没到底部,还可以滑
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean b = recyclerView.canScrollVertically(-1);
                if (!b) {
                    alldy = 0;
                }
                alldy += dy;
                float alpha = Math.min((alldy * 1) * 1.0f / TransformUtil.dp2px(mContext, 95), 1);
//                System.out.println("滑动起来之后:"+alpha);
                if (virtualLayoutManager.findFirstVisibleItemPosition() > 2) {
                    alpha = 1;
                }
                System.out.println("滑动:"+alpha);
                ViewGroup.LayoutParams seachTopTmpLayoutParams = seachTopTmp.getLayoutParams();
                int height=(int)(TransformUtil.dp2px(mContext, 80) * (1 - alpha));
                seachTopTmpLayoutParams.height = (int) (height<=0?1:height);
                seachTopTmp.setLayoutParams(seachTopTmpLayoutParams);

            }
        });
    }

    /**
     * 搜索处理
     */
    private void search() {
        mCurrentPage = 1;
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        secondBlockPresenter.getAPPIndexCustom(new SimpleHashMapBuilder<String, Object>().puts("id",id));

    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);

        secondBlockBarnnerAdapter = new SecondBlockBarnnerAdapter();
        secondBlockBarnnerAdapter.setOutClickListener(this);
        delegateAdapter.addAdapter(secondBlockBarnnerAdapter);

        secondBlockGoodsAdapter = new SecondBlockGoodsAdapter();
        secondBlockGoodsAdapter.setOutClickListener(this);
        delegateAdapter.addAdapter(secondBlockGoodsAdapter);

        mEmptyAdapter = new EmptyAdapter();
        mEmptyAdapter.setmBackgroundColor(R.color.transparent);
        mEmptyAdapter.setEmptyTextColor(Color.WHITE);
        mEmptyAdapter.setEmptyContentHeight(ScreenUtils.getScreenHeight(mContext));
        delegateAdapter.addAdapter(mEmptyAdapter);
    }

    @Override
    public void outClick(String function, Object obj) {
        if ("basket".equals(function)) {//加入购物车

        }
        if ("goods".equals(function)) {
            GoodsDetail goodsDetail = (GoodsDetail) obj;
            ARouter.getInstance()
                    .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                    .withString("id", String.valueOf(goodsDetail.id))
                    .withString("marketingType", goodsDetail.marketingType)
                    .navigation();
        }
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage = 1;
        getData();
    }

//    @Override
//    public void successThemeDetial(MainBlockModel result) {
//        if (result != null) {
//            if (!TextUtils.isEmpty(result.themeImage) && !"null".equals(result.themeImage)) {
//                secondBlockBarnnerAdapter.setModel(result);
//            }
//        }
//    }
//
//    @Override
//    public void onSuccessGetList(List<MainBlockDetailModel> result, PageInfoEarly pageInfoEarly) {
//        if (0 == pageInfoEarly.nextPage) {
//            refreshLayout.finishLoadMoreWithNoMoreData();
//        } else {
//            refreshLayout.resetNoMoreData();
//            refreshLayout.finishLoadMore();
//        }
//
//        if (mCurrentPage == 1) {
//            secondBlockGoodsAdapter.setData((ArrayList<MainBlockDetailModel>) result);
//        } else {
//            secondBlockGoodsAdapter.addDatas((ArrayList<MainBlockDetailModel>) result);
//        }
//
//        if (ListUtil.isEmpty(secondBlockGoodsAdapter.getDatas())) {
//            mEmptyAdapter.setModel("");
//            refreshLayout.finishLoadMoreWithNoMoreData();
//            return;
//        }
//        mEmptyAdapter.setModel(null);
//    }
//
//    @Override
//    public void successAddShopCat(String result) {
//        if (result.contains("购物车添加商品")) {
//            showToast("已加入购物车");
//        }
//    }
//
//
//    @Override
//    public void onGetShopCartSuccess(ShopCartModel shopCartModel) {
//
//    }

//    @Override
//    public void showLoading() {
//        mProgress.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void showContent() {
//        mProgress.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void showEmpty() {
//        mProgress.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void showDataErr() {
//        mProgress.setVisibility(View.GONE);
//    }

    @Override
    public void successAddShopCat(String result) {

    }

    @Override
    public void onSuccessGetAPPIndexCustom(List<AppIndexGoodsList> goodsLists) {
        if(goodsLists !=null&&goodsLists.size()>0){
            ArrayList<MainBlockDetailModel> modelList=new SimpleArrayListBuilder<>().putList(goodsLists, new ObjectIteraor<AppIndexGoodsList>() {
                @Override
                public MainBlockDetailModel getDesObj(AppIndexGoodsList object) {
                    return new MainBlockDetailModel(object);
                }
            });
            secondBlockBarnnerAdapter.setModel(new MainBlockModel(bannerUrl));
            topBar.setTitle(mainTitle);
            secondBlockGoodsAdapter.setData(modelList);

        }
    }
}
