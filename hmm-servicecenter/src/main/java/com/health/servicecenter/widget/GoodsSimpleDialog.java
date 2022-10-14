package com.health.servicecenter.widget;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.health.servicecenter.BuildConfig;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.GoodsSimpleDialogDetialTopAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialIntroduceAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialIntroduceEndAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialIntroduceImgAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialSelectAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialStoreAdapter;
import com.health.servicecenter.contract.ServiceGoodsDetailContract;
import com.health.servicecenter.presenter.ServiceGoodsDetailPresenter;
import com.healthy.library.LibApplication;
import com.healthy.library.adapter.GoodsDetailRankingListAdapter;
import com.healthy.library.adapter.GoodsDetailRankingShareAdapter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.OnlyActGoodsSpecDialog;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.GoodsRank;
import com.healthy.library.model.GoodsShop;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.model.MallGroupSimple;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.TabEntity;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.StatusLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.Disposable;

public class GoodsSimpleDialog extends BottomSheetDialogFragment implements ServiceGoodsDetailContract.View, BaseAdapter.OnOutClickListener {

    private View view;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    private StatusLayout layoutStatus;
    private RecyclerView recyclerview;
    private ConstraintLayout topTitle;
    private AutoClickImageView imgBack;
    private AutoClickImageView imgBack2;
    private CommonTabLayout topTab;
    private AutoClickImageView imgShare;
    private RelativeLayout shopCartRel;
    private AutoClickImageView imgBasket;
    private TextView shopCartNum;
    private ImageView closeBtn;
    private ImageView scrollUp;
    private AutoClickImageView goodsShareCoupon;
    private LinearLayout goodsUnder;
    private TextView goodsMoneyV;
    private TextView goBask;
    private TextView goOrder;
    private View topView;
    private OnlyActGoodsSpecDialog goodsSpecDialog;
    private MallGoodsDetialSelectAdapter mallGoodsDetialSelectAdapter;
    private MallGoodsDetialStoreAdapter mallGoodsDetialStoreAdapter;
    private GoodsSimpleDialogDetialTopAdapter goodsSimpleDialogDetailTopAdapter;
    private MallGoodsDetialIntroduceAdapter mallGoodsDetialIntroduceAdapter;
    private MallGoodsDetialIntroduceImgAdapter mallGoodsDetialIntroduceImgAdapter;
    private MallGoodsDetialIntroduceEndAdapter mallGoodsDetialIntroduceEndAdapter;
    private GoodsDetailRankingShareAdapter goodsDetailRankingShareAdapter;
    private GoodsDetailRankingListAdapter goodsDetailRankingListAdapter;
    private ServiceGoodsDetailPresenter serviceGoodsDetailPresenter;
    private String errMsg;
    //    private String shopId;
    private GoodsDetail goodsDetail;
//    private String id;
//    private String videoMoney;

    private int mPeekHeight;//初始高度
    private boolean mCreated;//dialog是否已经创建了
    private Window mWindow;
    private BottomSheetBehavior mBottomSheetBehavior;

    private int tab1org = 0;
    private int tab2org = 1;
    private int tab1 = tab1org;
    private int tab2 = tab2org;
    private int[] poss;
    private String[] titles = {"商品", "详情"};
    private String[] titles2 = {"商品", "详情"};
    private boolean isgoodshas = true;
    private boolean isgoodshasselect = false;
    private int dxall = 0;
    private boolean isScrolled = false;
    String selectSku;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private LiveRoomInfo mLiveRoomInfo;

    /**
     * .setSeachMap(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"9202")
     * .puts("shopId","548")
     * .puts("id","100167")
     * )
     *
     * @param seachMap
     * @return
     */
    public GoodsSimpleDialog setSeachMap(Map<String, Object> seachMap) {
        this.seachMap = seachMap;
        return this;
    }

    public GoodsSimpleDialog setLiveInfo(LiveRoomInfo mLiveRoomInfo) {
        this.mLiveRoomInfo = mLiveRoomInfo;
        return this;
    }
    public void getGroupAll() {
        serviceGoodsDetailPresenter.getGroupAlreadyList(new SimpleHashMapBuilder<String, Object>()
                .puts("type",(goodsDetail!=null&&goodsDetail.shareGiftDTOS.size()>0)?"1":null)
        );
    }

    private Map<String, Object> seachMap = new HashMap<>();

    public GoodsSimpleDialog setOnDialogButtonOrderListener(OnGoodsDialogOrderButtonListener onDialogButtonListener) {
        this.onDialogButtonListener = onDialogButtonListener;
        return this;
    }

    OnGoodsDialogOrderButtonListener onDialogButtonListener;

    public static GoodsSimpleDialog newInstance() {
        Bundle args = new Bundle();
        GoodsSimpleDialog fragment = new GoodsSimpleDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        return new BottomSheetDialog(getContext(), R.style.TransBottomSheetDialogStyle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCreated = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            mWindow = window;
            mWindow.setWindowAnimations(R.style.BottomDialogAnimation);
            View decorView = mWindow.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(R.drawable.shape_dialog);
            decorView.setBackgroundColor(getResources().getColor(R.color.transparent));
            WindowManager.LayoutParams params = mWindow.getAttributes();
            mWindow.setAttributes(params);
            Display display = mWindow.getWindowManager().getDefaultDisplay();
            setPeekHeight((int) (display.getHeight() * 0.7));//设置初始高度为屏幕高度的70%
            setMaxHeight();//设置最大滑动延伸高度为屏幕的高度
            setBottomSheetCallback();
        }
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        fullScreenImmersive(getDialog().getWindow().getDecorView());
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {

        }
        view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_simplegoods, container, false);
            initView();
        }
        return view;
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(getActivity());
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);

        goodsSimpleDialogDetailTopAdapter = new GoodsSimpleDialogDetialTopAdapter();
        goodsSimpleDialogDetailTopAdapter.setType(1);
        goodsSimpleDialogDetailTopAdapter.setActType(0);
        goodsSimpleDialogDetailTopAdapter.setDialog(this);
        goodsSimpleDialogDetailTopAdapter.setOutClickListener(this);
        delegateAdapter.addAdapter(goodsSimpleDialogDetailTopAdapter);
        goodsSimpleDialogDetailTopAdapter.setOnTopLoadMoreListener(new GoodsSimpleDialogDetialTopAdapter.OnTopLoadMoreListener() {
            @Override
            public void onTopLoadMore() {
                virtualLayoutManager.scrollToPositionWithOffset(tab2, 0);
                topTab.setCurrentTab(1);
                buildTopTab(1);
            }
        });


        mallGoodsDetialSelectAdapter = new MallGoodsDetialSelectAdapter();
        delegateAdapter.addAdapter(mallGoodsDetialSelectAdapter);
        mallGoodsDetialSelectAdapter.setOutClickListener(this);

        mallGoodsDetialStoreAdapter = new MallGoodsDetialStoreAdapter();
        delegateAdapter.addAdapter(mallGoodsDetialStoreAdapter);
        mallGoodsDetialStoreAdapter.setOutClickListener(this);

        goodsDetailRankingShareAdapter = new GoodsDetailRankingShareAdapter();
        delegateAdapter.addAdapter(goodsDetailRankingShareAdapter);
        goodsDetailRankingShareAdapter.setOutClickListener(this);

        goodsDetailRankingListAdapter = new GoodsDetailRankingListAdapter();
        delegateAdapter.addAdapter(goodsDetailRankingListAdapter);

        mallGoodsDetialIntroduceAdapter = new MallGoodsDetialIntroduceAdapter();
        delegateAdapter.addAdapter(mallGoodsDetialIntroduceAdapter);

        mallGoodsDetialIntroduceImgAdapter = new MallGoodsDetialIntroduceImgAdapter();
        mallGoodsDetialIntroduceImgAdapter.setType(1);
        delegateAdapter.addAdapter(mallGoodsDetialIntroduceImgAdapter);

        mallGoodsDetialIntroduceEndAdapter = new MallGoodsDetialIntroduceEndAdapter();
        mallGoodsDetialIntroduceEndAdapter.setNeedShowBottom(true);
        delegateAdapter.addAdapter(mallGoodsDetialIntroduceEndAdapter);
    }

    private void initView() {
        serviceGoodsDetailPresenter = new ServiceGoodsDetailPresenter(getActivity(), this);
        layoutStatus = (StatusLayout) view.findViewById(R.id.layout_status);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        topTitle = (ConstraintLayout) view.findViewById(R.id.topTitle);
        imgBack = (AutoClickImageView) view.findViewById(R.id.img_back);
        imgBack2 = (AutoClickImageView) view.findViewById(R.id.img_back2);
        topTab = (CommonTabLayout) view.findViewById(R.id.topTab);
        imgShare = (AutoClickImageView) view.findViewById(R.id.img_share);
        shopCartRel = (RelativeLayout) view.findViewById(R.id.shop_cart_rel);
        imgBasket = (AutoClickImageView) view.findViewById(R.id.img_basket);
        shopCartNum = (TextView) view.findViewById(R.id.shop_cart_num);
        closeBtn = (ImageView) view.findViewById(R.id.closeBtn);
        scrollUp = (ImageView) view.findViewById(R.id.scrollUp);
        goodsUnder = (LinearLayout) view.findViewById(R.id.goodsUnder);
        goodsMoneyV = (TextView) view.findViewById(R.id.goodsMoneyV);
        goBask = (TextView) view.findViewById(R.id.goBask);
        goOrder = (TextView) view.findViewById(R.id.goOrder);
        topView = (View) view.findViewById(R.id.topView);
        goodsShareCoupon = (AutoClickImageView) view.findViewById(R.id.goodsShareCoupon);
        buildTabNow();
        buildRecyclerView();

        serviceGoodsDetailPresenter.getGoodsDetail(seachMap);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (!TextUtils.isEmpty(errMsg) && errMsg.contains("开启抢购")) {
            goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order_gray);
            goOrder.setTextColor(Color.parseColor("#FFFFFF"));
            goOrder.setText("等待开抢");
        } else if (!TextUtils.isEmpty(errMsg) && errMsg.contains("已抢完")) {
            goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order_gray);
            goOrder.setTextColor(Color.parseColor("#FFFFFF"));
            goOrder.setText("已抢完");
        } else {
            goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order);
            goOrder.setTextColor(Color.parseColor("#FFFFFF"));
            goOrder.setText("立即抢购");
        }
        goOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goOrderDetail();

            }
        });
        mTabEntities.clear();
        for (int i = 0; i < titles.length; i++) {
            //插入tab标签
            mTabEntities.add(new TabEntity(titles[i], 0, 0));
        }
        topTab.setTabData(mTabEntities);
        topTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    dxall = 0;
                    virtualLayoutManager.scrollToPositionWithOffset(tab1, 0);
                }
                if (position == 1) {
                    virtualLayoutManager.scrollToPositionWithOffset(tab2, 0);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                dxall += dy;
                if (dxall < 0) {
                    dxall = 0;
                }
                int firstVisibleItemPosition = virtualLayoutManager.findFirstVisibleItemPosition();
                int firstCompletelyVisibleItemPosition = virtualLayoutManager.findFirstCompletelyVisibleItemPosition();
                buildTopTab((dxall > getResources().getDimension(R.dimen.status_2020) * 2 || firstVisibleItemPosition > 0) && (firstCompletelyVisibleItemPosition != 0) ? 1 : 0);
                if (isScrolled) {
                    int pos = 0;
                    if (firstVisibleItemPosition != -1) {
                        for (int i = 0; i < poss.length; i++) {
                            if (firstVisibleItemPosition >= poss[i]) {
                                pos = i;
                            }
                        }
                        topTab.setCurrentTab(pos);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    isScrolled = false;
                } else {
                    isScrolled = true;
                }
            }
        });
        scrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topTab.setCurrentTab(0);
                virtualLayoutManager.scrollToPositionWithOffset(tab1, 0);
                buildTopTab(0);
                dxall = 0;
            }
        });
        buildTopTab(0);
    }

    private void goOrderDetail() {
        if (!TextUtils.isEmpty(errMsg)) {
            Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
        } else {
            if (onDialogButtonListener != null) {
                if (goodsSpecDetailNew == null) {
                    Toast.makeText(getActivity(), "规格未选择", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newStoreDetialModelSelect == null) {
                    Toast.makeText(getActivity(), "未获取到门店配置请联系商家", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isgoodshasselect && goodsDetail.goodsChildren.size() > 1) {
                    showSkuAct(false);
                    return;
                }
                dismiss();
                onDialogButtonListener.onOrderClick(goodsSpecDetailNew.setShopDetailModelSelect(newStoreDetialModelSelect));
            }
        }
    }

    @Override
    public void dismiss() {
         isStop = true;
        super.dismiss();
    }

    private void showSkuAct(final boolean isrightbuy) {
        if (newStoreDetialModelSelect == null) {
            Toast.makeText(LibApplication.getAppContext(), "未获取到门店配置请联系商家", Toast.LENGTH_SHORT).show();
            return;
        }
        if (goodsSpecDialog == null) {
            goodsSpecDialog = OnlyActGoodsSpecDialog.newInstance();
        }
        goodsSpecDialog.show(getChildFragmentManager(), "优惠券");
        goodsSpecDialog.setGoodsSpecDetail(goodsSpecDetail);
        goodsSpecDialog.initSpec(goodsDetail.goodsChildren);
        goodsSpecDialog.setSelectSku(selectSku);
        goodsSpecDialog.setOnSpecSubmitListener(new OnlyActGoodsSpecDialog.OnSpecSubmitListener() {
            @Override
            public void onSpecSubmit(GoodsSpecDetail goodsSpecDetail) {
                isgoodshasselect = true;
                goodsSpecDetailNew = goodsSpecDetail;
                selectSku = goodsSpecDetailNew.getGoodsSpecStr();
                mallGoodsDetialSelectAdapter.setModel(selectSku);
                buildNowMoney();
                if (!isrightbuy) {//属于按到了立即抢购弹出的
                    goOrderDetail();
                }
            }
        });
    }

    private void buildNowMoney() {
        if (goodsSpecDetailNew != null) {
            goodsMoneyV.setText(FormatUtils.moneyKeep2Decimals(goodsSpecDetailNew.marketingPrice));
        }
    }

    private void buildTabNow() {
        if (poss == null) {
            poss = new int[]{tab1, tab2};
        } else {
            poss[0] = tab1;
            poss[1] = tab2;
        }
    }

    private void buildTopTab(int firstCompletelyVisibleItemPosition) {
        if (firstCompletelyVisibleItemPosition == 0) {
            topTab.setVisibility(View.INVISIBLE);
            scrollUp.setVisibility(View.GONE);
            imgBack.setImageResource(R.drawable.index_ic_back_b);
            imgShare.setImageResource(R.drawable.index_ic_share_b);
            imgBasket.setImageResource(R.drawable.index_ic_basket_b);
            topTitle.setBackgroundColor(Color.parseColor("#00000000"));
            if (isgoodshas) {
            } else {
                imgShare.setVisibility(View.GONE);
            }
        } else {
            if (isgoodshas) {
                topTab.setVisibility(View.VISIBLE);

            } else {
                imgShare.setVisibility(View.GONE);
            }
            scrollUp.setVisibility(View.VISIBLE);
            topTitle.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            imgBack.setImageResource(R.drawable.index_ic_back_c);
            imgShare.setImageResource(R.drawable.index_ic_share_c);
            imgBasket.setImageResource(R.drawable.index_ic_basket_c);

        }
    }

    public GoodsSpecDetail goodsSpecDetail;
    public GoodsSpecDetail goodsSpecDetailNew;

    private void buildHasGoods() {
        goodsSimpleDialogDetailTopAdapter.setModel(goodsDetail);
        mallGoodsDetialIntroduceAdapter.setModel(goodsDetail);
        mallGoodsDetialIntroduceImgAdapter.setModel(goodsDetail.additionNote);
        mallGoodsDetialIntroduceEndAdapter.setModel(goodsDetail);
        if (goodsDetail.goodsChildren.size() > 1) {
            mallGoodsDetialSelectAdapter.setModel(goodsDetail.goodsChildren.get(0).getGoodsSpecStr());
            selectSku = goodsDetail.goodsChildren.get(0).getGoodsSpecStr();
        }
        goodsSpecDetail = new GoodsSpecDetail(goodsDetail.getStorePrice(),
                goodsDetail.goodsChildren.get(0).livePrice,
                goodsDetail.headImages.get(0).getSplash(),
                goodsDetail.goodsType,
                goodsDetail.salesMax,
                goodsDetail.salesMin,
                false,
                goodsDetail.isPlusOnly
        );
        goodsSpecDetail.goodsTitle = goodsDetail.goodsTitle;
        goodsSpecDetailNew = new GoodsSpecDetail(goodsDetail.goodsChildren.get(0)).setFilePath(goodsSpecDetail.filePath)
                .setCount(goodsDetail.getMarketingSalesMin() + "");
        goodsSpecDetailNew.setGoodsTitle(goodsSpecDetailNew.goodsTitle.replace(goodsSpecDetailNew.goodsSpecStr, ""));
        buildNowMoney();
    }

    String shopId;
    String goodsShopId;
    ShopDetailModel newStoreDetialModelSelect;
    List<ShopDetailModel> storeDetialModelList;
    int shopIdIndex = 0;

    @Override
    public void successGetGoodsDetail(GoodsDetail goodsDetail) {
        if (goodsDetail == null || goodsDetail.goodsChildren == null || goodsDetail.goodsChildren.size() == 0) {
            Toast.makeText(LibApplication.getAppContext(), "商品信息错误！", Toast.LENGTH_SHORT).show();
            try {
                getDialog().dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        this.goodsDetail = goodsDetail;
        serviceGoodsDetailPresenter.getGoodsDetailNormal(new SimpleHashMapBuilder<String, Object>().puts("id", goodsDetail.goodsId).puts("shopId", "0"));
        this.goodsDetail.marketingType = "-1";
        if (goodsDetail != null) {
            isgoodshas = true;
            if (goodsDetail.shopIds != null && goodsDetail.shopIds.length > 0) {

                shopId = goodsDetail.shopIds[shopIdIndex];
            } else {
                shopId = seachMap.get("shopId").toString();
            }
            layoutStatus.updateStatus(StatusLayout.Status.STATUS_CONTENT);
            buildHasGoods();
            if (newStoreDetialModelSelect == null) {
                serviceGoodsDetailPresenter.getStoreDetial(shopId);
            }
        } else {
            isgoodshas = false;
        }
        if (goodsDetail != null && !ListUtil.isEmpty(goodsDetail.shareGiftDTOS)) {
            goodsDetailRankingShareAdapter.setRankData(goodsDetail.shareGiftDTOS);
            goodsDetailRankingShareAdapter.setModel("data");
            serviceGoodsDetailPresenter.getGoodsRankList();
            getGroupAll();
        } else {
            goodsDetailRankingShareAdapter.clear();
        }
    }

    @Override
    public void successGetGoodsDetailNormal(GoodsDetail normalGoodsDetail) {
        if (normalGoodsDetail != null) {
            this.goodsDetail.additionNote = normalGoodsDetail.additionNote;
            mallGoodsDetialIntroduceImgAdapter.setModel(normalGoodsDetail.additionNote);
        } else {
            if (BuildConfig.DEBUG) {
                Toast.makeText(FrameActivityManager.instance().topActivity(), "原商品已下架", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onGetStoreDetialSuccess(ShopDetailModel storeDetialModel) {
        if (storeDetialModel == null) {
            shopId = goodsDetail.shopIds[shopIdIndex++];
            serviceGoodsDetailPresenter.getStoreDetial(shopId);
            return;
        }
        if (newStoreDetialModelSelect != null) {
            storeDetialModel.distance = newStoreDetialModelSelect.distance;
        }
        if (storeDetialModel != null) {
            newStoreDetialModelSelect = storeDetialModel;
        }
        if (newStoreDetialModelSelect != null && storeDetialModelList != null && storeDetialModelList.size() > 0) {//说明列表回来了 不用一直请求啊
            mallGoodsDetialStoreAdapter.setModel(newStoreDetialModelSelect);
            mallGoodsDetialStoreAdapter.notifyDataSetChanged();
            goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
            return;
        } else {
            goodsShopId = null;
            newStoreDetialModelSelect = null;
            serviceGoodsDetailPresenter.getStoreList(shopId);
        }
    }

    private List<GoodsShop> buildFlashList(List<GoodsShop> goodsShopServiceList, String[] shopIdList) {//获取门店过滤列表

        List<GoodsShop> result = new ArrayList<>();
        for (int i = 0; i < goodsShopServiceList.size(); i++) {
            GoodsShop goodsShop = goodsShopServiceList.get(i);
            if (checkNowServiceShopIsRealy(goodsShop.shopId, shopIdList)) {
                result.add(goodsShopServiceList.get(i));
            }
        }
        return result;
    }

    @Override
    public void onGetStoreListSuccess(List<ShopDetailModel> list) {
        this.storeDetialModelList = buildFlashListWithStore(list, goodsDetail.getGoodsShopIdListStringArray());
        if (storeDetialModelList.size() > 0) {//说明有列表
            if (newStoreDetialModelSelect == null) {
                Collections.sort(storeDetialModelList);
                if (!storeDetialModelList.get(0).id.equals(goodsShopId)) {//存在门店不一致的情况 直接对
                    goodsShopId = storeDetialModelList.get(0).id;//进行赋值然后重新getData
                    newStoreDetialModelSelect = storeDetialModelList.get(0);
                    mallGoodsDetialStoreAdapter.setModel(newStoreDetialModelSelect);
                    mallGoodsDetialStoreAdapter.setOnlyOnePiece(buildFlashList(new SimpleArrayListBuilder<GoodsShop>().putList(storeDetialModelList, new ObjectIteraor<ShopDetailModel>() {
                        @Override
                        public GoodsShop getDesObj(ShopDetailModel o) {
                            return new GoodsShop(o);
                        }
                    }), goodsDetail.getGoodsShopIdListStringArray()).size() <= 1);
                    getData();
                    return;
                }
                goodsShopId = storeDetialModelList.get(0).id;
                newStoreDetialModelSelect = storeDetialModelList.get(0);
                serviceGoodsDetailPresenter.getStoreDetial(goodsShopId);
            }
            mallGoodsDetialStoreAdapter.setOnlyOnePiece(buildFlashList(new SimpleArrayListBuilder<GoodsShop>().putList(storeDetialModelList, new ObjectIteraor<ShopDetailModel>() {
                @Override
                public GoodsShop getDesObj(ShopDetailModel o) {
                    return new GoodsShop(o);
                }
            }), goodsDetail.getGoodsShopIdListStringArray()).size() <= 1);
        }
        try {
            goodsShopId = newStoreDetialModelSelect.id;
            mallGoodsDetialStoreAdapter.setModel(newStoreDetialModelSelect);
            goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onGetGoodsRankListSuccess(List<GoodsRank> list) {
        if (!ListUtil.isEmpty(list)) {
            goodsDetailRankingListAdapter.setRankData(list);
            goodsDetailRankingListAdapter.setModel("data");
            goodsShareCoupon.setVisibility(View.VISIBLE);
            goodsShareCoupon.startLoopScaleAuto();
            goodsShareCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeckShareDialog seckShareDialog = SeckShareDialog.newInstance();
                    seckShareDialog.setActivityDialog(8, 0, mLiveRoomInfo);
                    seckShareDialog.setGroupId(mLiveRoomInfo.groupId);
                    seckShareDialog.setExtraMap(new SimpleHashMapBuilder<String, String>()
                            .puts("scheme", "LiveStream")
                            .puts("courseId", mLiveRoomInfo.courseId)
                            .puts("fromMemberId", new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))//助力分享记得填
                            .puts("liveShareType", "zbzl")//普通分享 zbzl助力分享记得改
                            .puts("merchantId", mLiveRoomInfo.merchantId)
                            .puts("shopId", mLiveRoomInfo.shopId)
                            .puts("shareLiveGoodsId", seachMap.get("liveGoodsId"))
                    );
                    seckShareDialog.show(getChildFragmentManager(), "LookLiveShare");
                }
            });
        } else {
            goodsDetailRankingListAdapter.clear();
            goodsShareCoupon.setVisibility(View.GONE);
        }
    }
    List<MallGroupSimple> kkGroupSimples = new ArrayList<>();
    @Override
    public void onSuccessGetGroupAlreadyList(List<MallGroupSimple> kkGroupSimples) {
        if (this.kkGroupSimples.size() > 0) {

        } else {
            this.kkGroupSimples.addAll(kkGroupSimples);
            runShowEveryMan();
        }
    }
    boolean isRunshow = false;
    boolean isStop = false;
    private void runShowEveryMan() {
        if (isRunshow || isStop) {
            return;
        }
        isRunshow = true;
        goodsSimpleDialogDetailTopAdapter.setMallGroupSimple(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < kkGroupSimples.size(); i++) {
                    final MallGroupSimple item = kkGroupSimples.get(i);
                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!getActivity().isFinishing()) {
                                goodsSimpleDialogDetailTopAdapter.setMallGroupSimple(item);

                            }

                        }
                    });
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!getActivity().isFinishing()) {
                                goodsSimpleDialogDetailTopAdapter.setMallGroupSimple(null);
                            }
                        }
                    });
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(getActivity()==null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!getActivity().isFinishing()) {
                            kkGroupSimples.clear();
                            serviceGoodsDetailPresenter.getGroupAlreadyList(new SimpleHashMapBuilder<String, Object>()
                                    .puts("type",goodsDetail.shareGiftDTOS.size()>0?"1":null)
                            );
                            isRunshow = false;
                        }

                    }
                });


            }
        }).start();
    }


    private List<ShopDetailModel> buildFlashListWithStore(List<ShopDetailModel> goodsShopServiceList, String[] shopIdList) {//获取门店过滤列表

        List<ShopDetailModel> result = new ArrayList<>();
        for (int i = 0; i < goodsShopServiceList.size(); i++) {
            ShopDetailModel goodsShop = goodsShopServiceList.get(i);
            if (checkNowServiceShopIsRealy(goodsShop.id, shopIdList)) {
                System.out.println("可以选择的门店有:" + goodsShop.shopName);
                result.add(goodsShopServiceList.get(i));
            }
//            if((shopIdList==null||shopIdList.length == 0)&&goodsShop.shopType==1){
//                result.add(goodsShopServiceList.get(i));
//            }
        }
        return result;
    }

    private boolean checkNowServiceShopIsRealy(String shopId, String[] shopIdList) {
        if (shopIdList == null || shopIdList.length == 0) {
            return true;
        }
        for (int i = 0; i < shopIdList.length; i++) {
            if (shopId.equals(shopIdList[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void getData() {

    }

    @Override
    public void showDataErr() {

    }

    public GoodsSimpleDialog setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setPeekHeight(int peekHeight) {
        mPeekHeight = peekHeight;
        if (mCreated) {
            setPeekHeight();
        }
    }

    private void setPeekHeight() {
        if (mPeekHeight <= 0) {
            return;
        }
        if (getBottomSheetBehavior() != null) {
            mBottomSheetBehavior.setPeekHeight(mPeekHeight);
        }
    }

    private void setMaxHeight() {
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private BottomSheetBehavior getBottomSheetBehavior() {
        if (mBottomSheetBehavior != null) {
            return mBottomSheetBehavior;
        }
        View view = mWindow.findViewById(R.id.design_bottom_sheet);
        // setContentView() 没有调用
        if (view == null) {
            return null;
        }
        mBottomSheetBehavior = BottomSheetBehavior.from(view);
        return mBottomSheetBehavior;
    }

    private void setBottomSheetCallback() {
        if (getBottomSheetBehavior() != null) {
            mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);
        }
    }

    private final BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   @BottomSheetBehavior.State int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {//完全消失
                dismiss();
            } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {//完全展开
                closeBtn.setVisibility(View.GONE);
                topTitle.setVisibility(View.VISIBLE);
                topView.setVisibility(View.VISIBLE);
            } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {//停留在mPeekHeight的位置
                closeBtn.setVisibility(View.VISIBLE);
                topTitle.setVisibility(View.GONE);
                topView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    ShopOrderPickDialog shopOrderPickDialog;

    @Override
    public void outClick(@NonNull String function, @NonNull Object obj) {
        if ("可选门店".equals(function)) {
            if (shopOrderPickDialog == null) {
                shopOrderPickDialog = ShopOrderPickDialog.newInstance();
            }
            try {
                shopOrderPickDialog.setSelectId(newStoreDetialModelSelect.id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            shopOrderPickDialog.show(getChildFragmentManager(), "选择门店");
            shopOrderPickDialog.setGoodsShopList(buildFlashList(new SimpleArrayListBuilder<GoodsShop>().putList(storeDetialModelList, new ObjectIteraor<ShopDetailModel>() {
                @Override
                public GoodsShop getDesObj(ShopDetailModel o) {
                    return new GoodsShop(o);
                }
            }), goodsDetail.getGoodsShopIdListStringArray()));
            shopOrderPickDialog.setTitle("选择门店");
            shopOrderPickDialog.setOnDialogShopClickListener(new ShopOrderPickDialog.OnDialogShopClickListener() {
                @Override
                public void onDialogShopClick(GoodsShop goodsShop) {
                    ShopDetailModel newStoreDetialModelNow = new ShopDetailModel(goodsShop);
                    newStoreDetialModelSelect = newStoreDetialModelNow;
                    goodsShopId = newStoreDetialModelSelect.id;
                    mallGoodsDetialStoreAdapter.setModel(newStoreDetialModelSelect);
                    mallGoodsDetialStoreAdapter.notifyDataSetChanged();
//                    getData();
//                    goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
//                    isErrorOversold();
                }
            });
        }
        if ("规格选择".equals(function)) {
            showSkuAct(true);
        }
        if ("shareRank".equals(function)) {
            if (mLiveRoomInfo==null){
                Toast.makeText(getActivity(),"获取分享参数失败，请重试！",Toast.LENGTH_LONG).show();
                return;
            }
            SeckShareDialog seckShareDialog = SeckShareDialog.newInstance();
            seckShareDialog.setActivityDialog(8, 0, mLiveRoomInfo);
            seckShareDialog.setGroupId(mLiveRoomInfo.groupId);
            seckShareDialog.setExtraMap(new SimpleHashMapBuilder<String, String>()
                    .puts("scheme", "LiveStream")
                    .puts("courseId", mLiveRoomInfo.courseId)
                    .puts("fromMemberId", new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))//助力分享记得填
                    .puts("liveShareType", "zbzl")//普通分享 zbzl助力分享记得改
                    .puts("merchantId", mLiveRoomInfo.merchantId)
                    .puts("shopId", mLiveRoomInfo.shopId)
                    .puts("shareLiveGoodsId", seachMap.get("liveGoodsId"))
            );
            seckShareDialog.show(getChildFragmentManager(), "LookLiveShare");
        }
    }

    public interface OnGoodsDialogOrderButtonListener {
        void onOrderClick(GoodsSpecDetail goodsSpecDetailNew);
    }

    private void fullScreenImmersive(final View view) {
        getDialog().getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN;
                    view.setSystemUiVisibility(uiOptions);
                }
            }
        });

    }
}
