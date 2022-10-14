package com.health.servicecenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.google.gson.Gson;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallGoodsBasketAdapter;
import com.health.servicecenter.adapter.MallGoodsBasketEmptyAdapter;
import com.health.servicecenter.adapter.MallGoodsBasketErrorAdapter;
import com.health.servicecenter.adapter.MallGoodsBasketErrorEndAdapter;
import com.health.servicecenter.adapter.MallGoodsBasketErrorTopAdapter;
import com.health.servicecenter.adapter.MallGoodsBasketStoreAdapter;
import com.health.servicecenter.adapter.MallGoodsBasketTopDiscountAdapter;
import com.health.servicecenter.adapter.MallGoodsItemAdapter;
import com.health.servicecenter.adapter.MallGoodsTitleAdapter;
import com.health.servicecenter.contract.ServiceBasketActDetailContract;
import com.health.servicecenter.contract.ServiceBasketContract;
import com.health.servicecenter.presenter.ServiceBasketPresenter;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.ActVipContract;
import com.healthy.library.contract.ActVipOnlineContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.message.UpdateCheckAllBasket;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.ActVipDefault;
import com.healthy.library.model.DiscountTopModel;
import com.healthy.library.model.GoodsBasketAll;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.model.RecommendList;
import com.healthy.library.presenter.ActVipOnlinePresenter;
import com.healthy.library.presenter.ActVipPresenter;
import com.healthy.library.presenter.PlusPresenterCopy;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.widget.AnimManager;
import com.healthy.library.widget.AutoFitCheckBox;
import com.healthy.library.widget.DisBasketDialog;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Route(path = ServiceRoutes.SERVICE_BASKET)
public class ServiceGoodsBasket extends BaseActivity implements ActVipOnlineContract.View, BaseAdapter.OnOutClickListener, ActVipContract.View, ServiceBasketActDetailContract.View, MallGoodsBasketAdapter.OnGoodsChangeListener, MallGoodsBasketAdapter.OnGoodsCountChangeListener, IsFitsSystemWindows, OnRefreshLoadMoreListener, ServiceBasketContract.View {

    //尝试改活动group
    private com.healthy.library.widget.TopBar topBar;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerList;
    private LinearLayout basketBottom;
    private ConstraintLayout basketOrderBlock;
    private com.healthy.library.widget.AutoFitCheckBox allCheck;
    private TextView allCheckRight;
    private TextView orderTranMoney;
    private TextView orderMoneyLeft;
    private TextView orderMoney;
    private TextView checkOrder;
    private TextView checkDelete;
    private androidx.constraintlayout.widget.Group delectGroup;
    private androidx.constraintlayout.widget.Group orderGroup;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    ServiceBasketPresenter serviceBasketPresenter;
    ActVipPresenter actVipPresenter;

    MallGoodsBasketTopDiscountAdapter mallGoodsBasketDiscountTopAdapter;
    //    MallGoodsBasketTopAdapter mallGoodsBasketTopAdapter;
    MallGoodsBasketStoreAdapter mallGoodsBasketAdapter;
//    MallGoodsBasketEndAdapter mallGoodsBasketEndAdapter;


    MallGoodsBasketErrorTopAdapter mallGoodsBasketErrorTopAdapter;
    MallGoodsBasketErrorAdapter mallGoodsBasketErrorAdapter;
    MallGoodsBasketErrorEndAdapter mallGoodsBasketErrorEndAdapter;


    MallGoodsBasketEmptyAdapter mallGoodsBasketEmptyAdapter;
    MallGoodsTitleAdapter mallGoodsTitleAdapter;
    MallGoodsItemAdapter mallGoodsItemAdapter;

    GoodsBasketAll mgoodsBasketAll;
    int page = 1;

    //    String IsChkPopOK = "Y";
    Map<String, Object> recommandMap = new HashMap<>();
    boolean isEmpty = true;
    boolean isNoGoods = false;
    private Group orderGroupChild;
    private Group orderGroupChildUnder;
    private android.widget.ImageView ivBottomShader;
    private ConstraintLayout basketBottomLLLeft;
    private com.healthy.library.widget.ImageTextView discountMoney;
    private DisBasketDialog dialogBaskterFragment;
    private List<GoodsBasketStore> orggoodsBasketStoreList;
    Map<String, String> imageMap = new HashMap<>();

    @Autowired
    String visitShopId;
    @Autowired
    String marketingId;//传进来的活动id

    public List<ActVip> actVipResultList = null;

    List<ActVip> actVipHistory = new ArrayList<ActVip>();

    public ActVip.VipShop vipShop = null;
    String IsChkPopOK = "Y";
    String IsChkPopOKNO = "R";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_basket;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        if (TextUtils.isEmpty(visitShopId)) {
            visitShopId = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP);
        }
        serviceBasketPresenter = new ServiceBasketPresenter(this, this);
        actVipPresenter = new ActVipPresenter(this, this);

        if (TextUtils.isEmpty(marketingId)) {
            actVipHistory = ObjUtil.getObjList(SpUtils.getValue(mContext, SpKey.USER_MARKET_MAN), ActVip.class);
        }
        if (actVipHistory == null) {
            System.out.println("上次寸的缓存空的");
            actVipHistory = new ArrayList<>();
        }
        buildRecyclerView();
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        layoutRefresh.setEnableLoadMore(false);
        recommandMap.put("type", "6");
        recommandMap.put("pageNum", page + "");
        recommandMap.put("shopId", visitShopId);
        recommandMap.put("pageSize", "10");
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                if (delectGroup.getVisibility() == View.GONE) {//说明当前是
                    delectGroup.setVisibility(View.VISIBLE);
                    topBar.setSubmit("完成");
                    orderGroup.setVisibility(View.GONE);
                    orderGroupChild.setVisibility(View.GONE);
                    orderGroupChildUnder.setVisibility(View.GONE);
//                    allCheck.setChecked(false);
//                    mallGoodsBasketAdapter.checkAll(false);
                } else {
                    topBar.setSubmit("编辑");
                    delectGroup.setVisibility(View.GONE);
                    orderGroup.setVisibility(View.VISIBLE);
                    orderGroupChild.setVisibility(View.VISIBLE);
                    orderGroupChildUnder.setVisibility(View.VISIBLE);
//                    allCheck.setChecked(false);
//                    mallGoodsBasketAdapter.checkAll(false);
//                    buildNowGoods();
                    IsChkPopOK = IsChkPopOKNO;
                    getData();

                }
            }
        });
        basketBottomLLLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (orderGroupChildUnder.getVisibility() != View.VISIBLE) {
//                    return;
//                }
//                if (dialogBaskterFragment != null) {
//                    ////System.out.println("点击关闭");
//                    dialogBaskterFragment.dismiss();
//                }
//                dialogBaskterFragment = DisBasketDialog.newInstance();
//                dialogBaskterFragment.setData(allCheck.isChecked(), orderMoney.getText().toString(), checkOrder.getText().toString(),discountMoney.getText().toString());
//                dialogBaskterFragment.show(getSupportFragmentManager(), "测试");
            }
        });
//        onRefresh(null);
        getData();
        allCheck.setOnCheckedChangeListener(new AutoFitCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AutoFitCheckBox buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                onAllCheckChange(isChecked);


            }
        });
        allCheckRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean resultcheck = !allCheck.isChecked();
                allCheck.setChecked(resultcheck);
                onAllCheckChange(resultcheck);
            }
        });
        checkOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "event2APPShopCartGoSettlementClick", new SimpleHashMapBuilder<String, String>().puts("soure", "去结算按钮点击量"));
                if (mallGoodsBasketAdapter.getSelectOnlyGoodsList().size() == 0) {
                    return;
                }
                buyGoods(mallGoodsBasketAdapter.getSelectNeedPassList());
            }
        });
        checkDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mallGoodsBasketAdapter.getSelectOnlyGoodsList().size() == 0) {
                    return;
                }

                StyledDialog.init(mContext);
                StyledDialog.buildIosAlert("", "确定要删除这" + mallGoodsBasketAdapter.getSelectOnlyGoodsList().size() + "种商品?", new MyDialogListener() {
                    @Override
                    public void onFirst() {
                        deleteGoods(mallGoodsBasketAdapter.getSelectOnlyGoodsList());
                    }

                    @Override
                    public void onThird() {
                        super.onThird();
                    }

                    @Override
                    public void onSecond() {

                    }
                }).setCancelable(true, true).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("确定", "取消").show();


            }
        });
    }

    private void onAllCheckChange(boolean isChecked) {
        if (mgoodsBasketAll == null) {
            return;
        }
        mallGoodsBasketAdapter.checkAll(isChecked);
        for (int i = 0; i < mgoodsBasketAll.validList.size(); i++) {
            final GoodsBasketCell goodsBasketCell = mgoodsBasketAll.validList.get(i);
            goodsBasketCell.ischeck = isChecked;
        }
        if (isChecked) {
            onGoodsAdd();
        } else {
            onGoodsRemove();
        }
    }

    boolean isDataInit = false;

    @Override
    public void getData() {
        super.getData();
        new PlusPresenterCopy(this).checkPlus(new SimpleHashMapBuilder<String, Object>()
                .puts("phoneNum", SpUtils.getValue(mContext, SpKey.PHONE))
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
        isEmpty = true;
        isNoGoods = false;
        page = 1;
        recommandMap.put("pageNum", page + "");
        if (vipShop == null) {
            actVipPresenter.getVipShopDetail(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", visitShopId));
        } else {
            getBasketList();
        }
    }

    public void getBasketData() {

    }

    public void getBasketList() {
        serviceBasketPresenter.getBasketList(new SimpleHashMapBuilder<String, Object>()
                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                .puts("shopId", visitShopId)
                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        isDataInit = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        actVipHistory= ObjUtil.getObj(SpUtils.getValue(mContext,SpKey.USER_MARKET_CHECK), ActVip.class);
        if (isDataInit) {
            IsChkPopOK = "Y";
            getData();
        }
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);


        recyclerList.setLayoutManager(virtualLayoutManager);
        recyclerList.setAdapter(delegateAdapter);

        mallGoodsBasketEmptyAdapter = new MallGoodsBasketEmptyAdapter();
        delegateAdapter.addAdapter(mallGoodsBasketEmptyAdapter);
        mallGoodsBasketEmptyAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
            @Override
            public void outClick(@NotNull String function, @NotNull Object obj) {


//                ARouter.getInstance()
//                        .build(ServiceRoutes.SERVICE_SERVICESORTGOODS)
//                        .withString("categoryId", 0+"")
//                        .withString("goodsTitle", "")
//                        .navigation();
                try {
                    FrameActivityManager.instance().finishOthersActivity(Class.forName("com.health.client.activity.MainActivity"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });
//        mallGoodsBasketTopAdapter = new MallGoodsBasketTopAdapter();
//        delegateAdapter.addAdapter(mallGoodsBasketTopAdapter);

        mallGoodsBasketDiscountTopAdapter = new MallGoodsBasketTopDiscountAdapter();
        delegateAdapter.addAdapter(mallGoodsBasketDiscountTopAdapter);
        mallGoodsBasketDiscountTopAdapter.setOutClickListener(this);
        mallGoodsBasketAdapter = new MallGoodsBasketStoreAdapter();
        delegateAdapter.addAdapter(mallGoodsBasketAdapter);
        mallGoodsBasketAdapter.setImageMap(imageMap);

        mallGoodsBasketAdapter.setOnGoodsCountChangeListener(this);
        mallGoodsBasketAdapter.setOnGoodsChangeListener(this);
        mallGoodsBasketAdapter.setOutClickListener(this);
        if (TextUtils.isEmpty(marketingId)) {
            if (SpUtils.getValue(mContext, SpKey.USER_MARKET_CHECK) != null) {
                if (new Gson().fromJson(SpUtils.getValue(mContext, SpKey.USER_MARKET_CHECK), checkMap.getClass()) != null) {
                    checkMap = new Gson().fromJson(SpUtils.getValue(mContext, SpKey.USER_MARKET_CHECK), checkMap.getClass());
                }
            }
        }

        mallGoodsBasketAdapter.setCheckMap(checkMap);
//        mallGoodsBasketEndAdapter = new MallGoodsBasketEndAdapter();
//        delegateAdapter.addAdapter(mallGoodsBasketEndAdapter);

        mallGoodsBasketErrorTopAdapter = new MallGoodsBasketErrorTopAdapter();
        delegateAdapter.addAdapter(mallGoodsBasketErrorTopAdapter);

        mallGoodsBasketErrorAdapter = new MallGoodsBasketErrorAdapter();
        delegateAdapter.addAdapter(mallGoodsBasketErrorAdapter);

        mallGoodsBasketErrorEndAdapter = new MallGoodsBasketErrorEndAdapter();
        delegateAdapter.addAdapter(mallGoodsBasketErrorEndAdapter);

        mallGoodsTitleAdapter = new MallGoodsTitleAdapter();
        delegateAdapter.addAdapter(mallGoodsTitleAdapter);

        mallGoodsItemAdapter = new MallGoodsItemAdapter();
        delegateAdapter.addAdapter(mallGoodsItemAdapter);
        mallGoodsItemAdapter.setKey("购物车帮你挑");
        mallGoodsBasketErrorTopAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
            @Override
            public void outClick(@NotNull String function, @NotNull Object obj) {
                deleteGoods(mgoodsBasketAll.invalidList);
            }
        });
        mallGoodsItemAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
            @Override
            public void outClick(@NotNull String function, @NotNull Object obj) {
                if ("addShopCat".equals(function)) {
                    RecommendList goodsListModel = (RecommendList) obj;
                    if (goodsListModel != null) {
                        serviceBasketPresenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
                                .puts("shopId", visitShopId)
                                .puts("goodsShopId", goodsListModel.getShopId())
                                .puts("goodsSource", "1")
                                .puts("goodsType", goodsListModel.goodsType + "")
                                .puts("goodsId", goodsListModel.goodsId + "")
                                .puts("goodsSpecId", goodsListModel.skuId + "")
                                .puts("goodsQuantity", 1 + "")
                        );
                    }
                }
            }
        });
        mallGoodsItemAdapter.setOnBasketClickListener(new MallGoodsItemAdapter.OnBasketClickListener() {
            @Override
            public void onBasketClick(View view) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "购物车下方”购物车帮你挑“专题商品点击【加入购物车】");
                MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_AddShoppingCart", nokmap);
                AnimManager animManager = new AnimManager.Builder()
                        .with((Activity) mContext)
                        .startView(view)
                        .endView(allCheck)
                        .imageUrl(R.drawable.basket_red)
                        .build();
                animManager.startAnim();
            }
        });

        mallGoodsBasketEmptyAdapter.setModel(null);
//        mallGoodsBasketTopAdapter.setModel(null);
        mallGoodsBasketAdapter.setModel(null);
//        mallGoodsBasketEndAdapter.setModel(null);
        mallGoodsBasketErrorTopAdapter.setModel(null);
        mallGoodsBasketErrorAdapter.setModel(null);
        mallGoodsBasketErrorEndAdapter.setModel(null);
        mallGoodsTitleAdapter.setModel(null);
        //mallGoodsItemAdapter.setData(new SimpleArrayListBuilder<MallGoods>());

    }


    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerList = (RecyclerView) findViewById(R.id.recycler_list);
        basketBottom = (LinearLayout) findViewById(R.id.basketBottom);
        basketOrderBlock = (ConstraintLayout) findViewById(R.id.basketOrderBlock);
        allCheck = (AutoFitCheckBox) findViewById(R.id.allCheck);
        allCheckRight = (TextView) findViewById(R.id.allCheckRight);
        orderTranMoney = (TextView) findViewById(R.id.orderTranMoney);
        orderMoneyLeft = (TextView) findViewById(R.id.orderMoneyLeft);
        orderMoney = (TextView) findViewById(R.id.orderMoney);
        checkOrder = (TextView) findViewById(R.id.checkOrder);
        checkDelete = (TextView) findViewById(R.id.checkDelete);
        delectGroup = (Group) findViewById(R.id.delectGroup);
        orderGroup = (Group) findViewById(R.id.orderGroup);
        orderGroupChild = (Group) findViewById(R.id.orderGroupChild);
        orderGroupChildUnder = (Group) findViewById(R.id.orderGroupChildUnder);
        ivBottomShader = (ImageView) findViewById(R.id.iv_bottom_shader);
        basketBottomLLLeft = (ConstraintLayout) findViewById(R.id.basketBottomLLLeft);
        discountMoney = (ImageTextView) findViewById(R.id.discountMoney);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

        page++;
        recommandMap.put("pageNum", page + "");
        serviceBasketPresenter.getGoodsRecommend(recommandMap);
//        layoutRefresh.finishRefresh();
//        layoutRefresh.finishLoadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        IsChkPopOK = "Y";
        page = 1;
        mallGoodsItemAdapter.clear();
        mallGoodsBasketAdapter.clear();
        mallGoodsBasketAdapter.getCheckMap().clear();
        recommandMap.put("pageNum", page + "");
        SpUtils.store(mContext, SpKey.USER_MARKET_CHECK, null);
        getData();
//        if(isEmpty){
//            page=1;
//            serviceBasketPresenter.getGoodsRecommend(recommandMap);
//        }else {
//            mallGoodsBasketAdapter.checkAll(false);
//
//        }
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
    }

    @Override
    public void onSucessGetBasketList(GoodsBasketAll goodsBasketAll) {

        mallGoodsBasketEmptyAdapter.setModel(null);
//        mallGoodsBasketTopAdapter.setModel(null);
        orggoodsBasketStoreList = mallGoodsBasketAdapter.getStoreList();
        System.out.println("购物车展示长度:" + orggoodsBasketStoreList.size());
//        mallGoodsBasketAdapter.setModel(null);
//        mallGoodsBasketEndAdapter.setModel(null);
        mallGoodsBasketDiscountTopAdapter.setModel(null);
        mallGoodsBasketErrorTopAdapter.setModel(null);
        mallGoodsBasketErrorAdapter.setModel(null);
        mallGoodsBasketErrorEndAdapter.setModel(null);
//        mallGoodsTitleAdapter.setModel(null);
//        mallGoodsItemAdapter.setModel(null);
        mallGoodsBasketEmptyAdapter.setModel(null);
//        mallGoodsTitleAdapter.setModel(null);

        this.mgoodsBasketAll = goodsBasketAll;
        isEmpty = true;
        topBar.getmSubmitText().setVisibility(View.VISIBLE);
        if (goodsBasketAll != null) {//可能有购物车
//            mallGoodsTitleAdapter.setModel("购物车帮你挑");
            if (goodsBasketAll != null) {
                goodsBasketAll.checkRealInvald();//判断失效商品
                if (!ListUtil.isEmpty(goodsBasketAll.differentList)) {
                    if (ListUtil.isEmpty(goodsBasketAll.validList)) {
                        goodsBasketAll.validList = goodsBasketAll.differentList;
                    } else {
                        goodsBasketAll.validList.addAll(goodsBasketAll.differentList);
                    }
                }
                //重置下设定
                if (!ListUtil.isEmpty(goodsBasketAll.validList)) {//说明有效不为空
                    isEmpty = false;
                    buildHasValGoods();
                } else {//有效为空
                    isNoGoods = true;
                }
                if (!ListUtil.isEmpty(goodsBasketAll.invalidList)) {//说明无效不为空
                    isEmpty = false;
                    mallGoodsBasketErrorTopAdapter.setModel(goodsBasketAll.invalidList.size() + "");
                    buildHasInValGoods();
                }
            }

        }
        if (isEmpty) {
            topBar.getmSubmitText().setVisibility(View.INVISIBLE);
            basketBottom.setVisibility(View.GONE);
            buildNoGoods();
        } else {
            if (isNoGoods) {
                topBar.getmSubmitText().setVisibility(View.INVISIBLE);
                basketBottom.setVisibility(View.GONE);
                //buildNoGoods();
            }
        }

//        buildNoGoods();
    }

    public void buyGoods(ArrayList<GoodsBasketCell> list) {
        if (list.size() > 0) {
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_ORDER)
                    .withObject("goodsbasketlist", list)
                    .withObject("actVipResultList", actVipResultList)
                    .navigation();
        }


    }

    public void deleteGoods(ArrayList<GoodsBasketCell> list) {
        if (list.size() > 0) {
            serviceBasketPresenter.deleteGoods(new SimpleHashMapBuilder<String, Object>()
                    .puts("cartId", list.get(0).cartId)
                    .puts("cartDetailIdList", new SimpleArrayListBuilder<String>().putList(list, new ObjectIteraor<GoodsBasketCell>() {
                        @Override
                        public String getDesObj(GoodsBasketCell o) {
                            return o.cartDetailId + "";
                        }
                    })));
        }

    }

    @Override
    public void successGetGoodsRecommend(List<RecommendList> result) {
        if (result == null) {
            return;
        }
        if (result.size() == 0) {
            ////System.out.println("目前推荐的长度");
            layoutRefresh.finishLoadMoreWithNoMoreData();
            if (page == 1 || page == 0) {
                mallGoodsTitleAdapter.setModel(null);
            }
        } else {
            mallGoodsTitleAdapter.setModel("购物车帮你挑");
            if (page == 1 || page == 0) {
                mallGoodsItemAdapter.setData((ArrayList<RecommendList>) result);
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).actVip == null) {
                        if (vipShop != null) {
                            serviceBasketPresenter.getGoodsActVips(new SimpleHashMapBuilder<String, Object>()
                                            .puts("Command", "pcPreCalcGoodsPop")
                                            .puts("AppID", vipShop.ytbAppId)
                                            .puts("LoginSequence", "7B2978F6")
                                            .puts("GoodsID", result.get(i).barcodeSku)
                                            .puts("DepartID", vipShop.ytbDepartID)
                                    , result.get(i));
                        }
                    }
                }
                ////System.out.println("目前推荐的长度设置数据");
            } else {
                mallGoodsItemAdapter.addDatas((ArrayList<RecommendList>) result);
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).actVip == null) {
                        if (vipShop != null) {
                            serviceBasketPresenter.getGoodsActVips(new SimpleHashMapBuilder<String, Object>()
                                            .puts("Command", "pcPreCalcGoodsPop")
                                            .puts("AppID", vipShop.ytbAppId)
                                            .puts("LoginSequence", "7B2978F6")
                                            .puts("GoodsID", result.get(i).barcodeSku)
                                            .puts("DepartID", vipShop.ytbDepartID)
                                    , result.get(i));
                        }
                    }
                }
            }
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void sucessUpdate() {
        IsChkPopOK = IsChkPopOKNO;
        getData();
    }

    @Override
    public void sucessDelete() {
        checkOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_order_g);
        checkDelete.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_delete_g);
        checkDelete.setTextColor(Color.parseColor("#ff666666"));
        IsChkPopOK = IsChkPopOKNO;
        getData();
    }

    @Override
    public void successAddShopCat(String result) {
        getData();
    }


    private void buildNoGoods() {


//        mallGoodsBasketTopAdapter.setModel(null);
        mallGoodsBasketAdapter.setModel(null);
//        mallGoodsBasketEndAdapter.setModel(null);

        mallGoodsBasketErrorTopAdapter.setModel(null);
        mallGoodsBasketErrorAdapter.setModel(null);
        mallGoodsBasketErrorEndAdapter.setModel(null);

        //mallGoodsItemAdapter.setData(new SimpleArrayListBuilder<MallGoods>());
        mallGoodsBasketEmptyAdapter.setModel("");
        if (mallGoodsItemAdapter.getDatas() == null || mallGoodsItemAdapter.getDatas().size() == 0) {
            serviceBasketPresenter.getGoodsRecommend(recommandMap);
        }

    }

    private void buildHasInValGoods() {
        basketBottom.setVisibility(View.VISIBLE);
        mallGoodsBasketEmptyAdapter.setModel(null);

//        mallGoodsBasketErrorTopAdapter.setModel("null");
        mallGoodsBasketErrorAdapter.setData(mgoodsBasketAll.invalidList);
        mallGoodsBasketErrorEndAdapter.setModel("");
//        mallGoodsTitleAdapter.setModel(null);
        //mallGoodsItemAdapter.setData(new SimpleArrayListBuilder<MallGoods>());
    }

    Map<String, Boolean> checkMap = new HashMap<>();

    private void buildHasValGoods() {
        basketBottom.setVisibility(View.VISIBLE);
        mallGoodsBasketEmptyAdapter.setModel(null);
//        mallGoodsBasketTopAdapter.setModel("");


        if (mallGoodsBasketAdapter.getCheckMap().size() > 0) {
            for (int i = 0; i < mgoodsBasketAll.validList.size(); i++) {
                GoodsBasketCell goodsBasketCell = mgoodsBasketAll.validList.get(i);
                if (mallGoodsBasketAdapter.getCheckMap().get(goodsBasketCell.cartDetailId) != null) {
                    ////System.out.println("购物车：" + goodsBasketCell.cartDetailId);
                    goodsBasketCell.ischeck = mallGoodsBasketAdapter.getCheckMap().get(goodsBasketCell.cartDetailId);
                } else {
                    goodsBasketCell.ischeck = false;
                }
            }
        }
        boolean isBasketHasSelect = false; ///判断是不是购物车有选中的 则需要计算营销活动
        for (int i = 0; i < mgoodsBasketAll.validList.size(); i++) {
            GoodsBasketCell goodsBasketCell = mgoodsBasketAll.validList.get(i);
            if (goodsBasketCell.ischeck && !goodsBasketCell.isGift) {
                isBasketHasSelect = true;
            }
        }
        if (isBasketHasSelect) {//判断购物车有选择才能做
            if (actVipResultList == null) {
                actVipResultList = new ArrayList<>();
            }
            if (orggoodsBasketStoreList == null || orggoodsBasketStoreList.size() == 0) {

            } else {
                orggoodsBasketStoreList = getGoodsBasketStores(orggoodsBasketStoreList);
            }
            showRealBasketView();
        } else {//直接展示
            actVipResultList = null;
            orggoodsBasketStoreList = null;
            showRealBasketView();
        }

//        mallGoodsBasketAdapter.setData(getGoodsBasketStores());
//        onGoodsChange();
    }

    @NotNull
    private ArrayList<GoodsBasketStore> getGoodsBasketStores(List<GoodsBasketStore> OrgGoodsBasketStoreS) {
        if (OrgGoodsBasketStoreS != null && OrgGoodsBasketStoreS.size() > 0 && mgoodsBasketAll.validList.size() > 0) {
            GoodsBasketCell goodsBasketCell = mgoodsBasketAll.validList.get(0);//找到第一个商品
            if (goodsBasketCell.UUID != null && goodsBasketCell.UUID.equals(OrgGoodsBasketStoreS.get(0).getGoodsBasketCellAllList().get(0).UUID)) {
                //视为购物车变化不大 可能只是做了部分小操作 则直接分配下
                for (int k = 0; k < OrgGoodsBasketStoreS.size(); k++) {
                    GoodsBasketStore goodsBasketStore = OrgGoodsBasketStoreS.get(k);
                    OrgGoodsBasketStoreS.get(k).IsChkPopOK = IsChkPopOK;
                    for (int i = 0; i < goodsBasketStore.goodsBasketGroupList.size(); i++) {
                        GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(i);
                        for (int j = 0; j < goodsBasketGroup.goodsBasketCellList.size(); j++) {
                            GoodsBasketCell goodsBasketCellZZ = goodsBasketGroup.goodsBasketCellList.get(j);
                            if (!goodsBasketCellZZ.isGift) {
                                if (mallGoodsBasketAdapter.getCheckMap().get(goodsBasketCellZZ.cartDetailId) != null) {
                                    goodsBasketCellZZ.ischeck = mallGoodsBasketAdapter.getCheckMap().get(goodsBasketCellZZ.cartDetailId);
                                    System.out.println("购物车选中:" + goodsBasketCellZZ.goodsTitle + (goodsBasketCellZZ.ischeck));
                                } else {
                                    goodsBasketCellZZ.ischeck = false;
                                }
                            }

                        }
                    }
                    System.out.println("onSucessGetShopDetailOnly805");
                    onSucessGetShopDetailOnly(OrgGoodsBasketStoreS.get(k));
                }
                return (ArrayList<GoodsBasketStore>) OrgGoodsBasketStoreS;
            }
        }
        Map<String, Object> goodsUseMap = new HashMap<>();
        ArrayList<GoodsBasketGroup> goodsBasketGroups = new ArrayList<>();
        for (int i = 0; i < mgoodsBasketAll.validList.size(); i++) {//生成活动组
            GoodsBasketCell goodsBasketCell = mgoodsBasketAll.validList.get(i);
            goodsBasketCell.UUID = UUID.randomUUID().toString();
            if ("4".equals(goodsBasketCell.getGoodsMarketingTypeOrg())
                    || "1".equals(goodsBasketCell.getGoodsMarketingTypeOrg())
                    || "2".equals(goodsBasketCell.getGoodsMarketingTypeOrg())
                    || "3".equals(goodsBasketCell.getGoodsMarketingTypeOrg())
                    || "6".equals(goodsBasketCell.getGoodsMarketingTypeOrg())
                    || "7".equals(goodsBasketCell.getGoodsMarketingTypeOrg())) {
                goodsBasketCell.goodsMarketingDTO = null;
            }
            if (goodsUseMap.get(goodsBasketCell.getGoodsShopId()) != null) {//按门店分活动
                GoodsBasketGroup goodsBasketGroup = (GoodsBasketGroup) goodsUseMap.get(goodsBasketCell.getGoodsShopId());
                goodsBasketGroup.goodsBasketCellList.add(goodsBasketCell);
            } else {
                GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                goodsBasketGroup.groupId = goodsBasketCell.getGroupId();
                goodsBasketGroup.marketingType = goodsBasketCell.getGoodsMarketingTypeOrg();
                goodsBasketGroup.goodsCategoryFirstId = goodsBasketCell.getGoodsCategoryFirstNoId();
                goodsBasketGroup.shopId = goodsBasketCell.getGoodsShopId();
                goodsBasketGroup.shopName = goodsBasketCell.goodsShopName;
                goodsUseMap.put(goodsBasketCell.getGoodsShopId(), goodsBasketGroup);
                goodsBasketGroups.add(goodsBasketGroup);
            }
        }
        goodsUseMap.clear();
        ArrayList<GoodsBasketStore> GoodsBasketStores = new ArrayList<>();
        for (int i = 0; i < goodsBasketGroups.size(); i++) {//对活动进行分门店 直接按照活动门店来分
            GoodsBasketGroup goodsBasketCellGroup = goodsBasketGroups.get(i);
            if (goodsUseMap.get(goodsBasketCellGroup.shopId) != null) {//补充1级品类问题 用于门店分配
                ((GoodsBasketStore) goodsUseMap.get(goodsBasketCellGroup.shopId)).goodsBasketGroupList.add(goodsBasketCellGroup);
            } else {
                GoodsBasketStore goodsBasketGroup = new GoodsBasketStore(goodsBasketCellGroup);
                goodsBasketGroup.shopId = goodsBasketCellGroup.shopId;
                goodsBasketGroup.goodsCategoryFirstId = goodsBasketCellGroup.goodsCategoryFirstId;
                goodsBasketGroup.shopName = goodsBasketCellGroup.shopName;
                goodsUseMap.put(goodsBasketCellGroup.shopId, goodsBasketGroup);
                GoodsBasketStores.add(goodsBasketGroup);
            }
        }
        goodsUseMap.clear();
        for (int i = 0; i < GoodsBasketStores.size(); i++) {
            GoodsBasketStore goodsBasketStore = GoodsBasketStores.get(i);
            if (OrgGoodsBasketStoreS == null || OrgGoodsBasketStoreS.size() == 0) {
                System.out.println("onSucessGetShopDetailOnly857");
                serviceBasketPresenter.getShopDetailOnly(new SimpleHashMapBuilder<String, Object>()
                        .puts("shopId", goodsBasketStore.shopId)
                        .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                        .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)), goodsBasketStore);
            } else {
                if ("Y".equals(IsChkPopOK)) {//直接使用老的那个就行了 那个里的字段就行了
                    for (int j = 0; j < OrgGoodsBasketStoreS.size(); j++) {
                        if (goodsBasketStore.shopId.equals(OrgGoodsBasketStoreS.get(j).shopId)) {//复原老数据
                            goodsBasketStore.setUnder(OrgGoodsBasketStoreS.get(j).actVipResult,
                                    OrgGoodsBasketStoreS.get(j).actVipReq,
                                    OrgGoodsBasketStoreS.get(j).IsChkPopOK,
                                    OrgGoodsBasketStoreS.get(j).getDepartID());
                            goodsBasketStore.mchId=OrgGoodsBasketStoreS.get(j).mchId;
                            goodsBasketStore.merchantType=OrgGoodsBasketStoreS.get(j).merchantType;
                        }
                    }
                    goodsBasketStore.IsChkPopOK = "Y";
                    if (goodsBasketStore.getDepartID() != null) {
                        System.out.println("onSucessGetShopDetailOnly874");
                        onSucessGetShopDetailOnly(goodsBasketStore);
                    } else {
                        System.out.println("onSucessGetShopDetailOnly878");
                        serviceBasketPresenter.getShopDetailOnly(new SimpleHashMapBuilder<String, Object>()
                                .puts("shopId", goodsBasketStore.shopId)
                                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)), goodsBasketStore);
                    }
                } else {
                    GoodsBasketStores.get(i).IsChkPopOK = IsChkPopOK;
                    System.out.println("onSucessGetShopDetailOnly885");
                    serviceBasketPresenter.getShopDetailOnly(new SimpleHashMapBuilder<String, Object>()
                            .puts("shopId", goodsBasketStore.shopId)
                            .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                            .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)), GoodsBasketStores.get(i));
                }
            }

        }
        return GoodsBasketStores;
    }

    @Override
    public void onGoodsCountChange(GoodsBasketCell goodsBasketCell, int count) {
        serviceBasketPresenter.updateGoods(new SimpleHashMapBuilder<String, Object>()
                .puts("cartDetailId", goodsBasketCell.cartDetailId)
                .puts("goodsQuantity", count + "")
        );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkAllChange(UpdateCheckAllBasket msg) {
        if (mgoodsBasketAll == null) {
            return;
        }
        allCheck.setChecked(msg.flag);
        onAllCheckChange(msg.flag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "购物车浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000);
        MobclickAgent.onEvent(mContext, "event_APP_ShopCart_Stop", nokmap);
    }

    private long mills = System.currentTimeMillis();

    @Override
    public void onBackBtnPressed() {
        super.onBackBtnPressed();
    }

    @Override
    public void finish() {
        super.finish();
        String checkMapMan = new Gson().toJson(actVipResultList);
        if (TextUtils.isEmpty(marketingId)) {
            SpUtils.store(mContext, SpKey.USER_MARKET_MAN, checkMapMan);
        }
        String checkMapResult = new Gson().toJson(mallGoodsBasketAdapter.getCheckMap());
        if (TextUtils.isEmpty(marketingId)) {
            SpUtils.store(mContext, SpKey.USER_MARKET_CHECK, checkMapResult);
        }
    }

    @Override
    public void onGoodsAdd() {
        System.out.println("购物车选中");
        if (delectGroup.getVisibility() == View.VISIBLE) {
            buildNowGoods();
        } else {
            IsChkPopOK = IsChkPopOKNO;
            getDataUnder();
        }
    }

    public void getDataUnder() {
        buildHasValGoods();
    }

    @Override
    public void onGoodsRemove() {
        if (delectGroup.getVisibility() == View.VISIBLE) {
            buildNowGoods();
        } else {
            IsChkPopOK = "R";
            getDataUnder();
        }
    }

    Handler handlerSubmit = new Handler();
    Runnable runnableSubmit = new Runnable() {
        @Override
        public void run() {

            buildNowGoodsReal();
        }
    };

    public void buildNowGoodsReal() {
        System.out.println("计算购物车价格");
        BigDecimal totalDecimal = new BigDecimal(0);
        BigDecimal totalDecimalDis = new BigDecimal(0);
        int selectcount = 0;
        checkOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_order_g);
        checkDelete.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_delete_g);
        checkDelete.setTextColor(Color.parseColor("#ff666666"));
        boolean isAllcheck = true;
        List<GoodsBasketStore> goodsBasketStoreList = mallGoodsBasketAdapter.getDatas();
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            GoodsBasketStore goodsBasketStoreTmp = goodsBasketStoreList.get(i);
            for (int j = 0; j < goodsBasketStoreTmp.getGoodsBasketCellOnlyGoodsList().size(); j++) {
                GoodsBasketCell goodsBasketCellTmp = goodsBasketStoreTmp.getGoodsBasketCellOnlyGoodsList().get(j);
                if (goodsBasketCellTmp.ischeck) {//说明选中了
                    selectcount++;
                    checkOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_order);
                    checkDelete.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_delete);
                    checkDelete.setTextColor(Color.parseColor("#fff02846"));
                } else {
                    isAllcheck = false;
                    allCheck.setChecked(false);
                }
            }

        }
        if (isAllcheck) {
            allCheck.setChecked(true);
        }
        orderGroupChildUnder.setVisibility(View.GONE);
//        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
//            GoodsBasketStore goodsBasketStoreTmp = goodsBasketStoreList.get(i);
//            goodsBasketStoreTmp.checkAct();
//        }
        if (selectcount != 0) {
            if (delectGroup.getVisibility() == View.GONE) {
                orderGroupChildUnder.setVisibility(View.VISIBLE);
            }

            //使用线下返回的计算结果
            if (actVipResultList != null && actVipResultList.size() > 0) {
                for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                    GoodsBasketStore goodsBasketStoreTmp = goodsBasketStoreList.get(i);
                    totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketStoreTmp.getgCurPriceInBasket()));
                    totalDecimalDis = totalDecimalDis.add(new BigDecimal(goodsBasketStoreTmp.getgCurDiscountInBasket()));
                }
                totalDecimal = totalDecimal.subtract(totalDecimalDis);
            } else {
                for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                    GoodsBasketStore goodsBasketStoreTmp = goodsBasketStoreList.get(i);
                    totalDecimalDis = totalDecimalDis.add(new BigDecimal(goodsBasketStoreTmp.getgCurDiscount()));
                    totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketStoreTmp.getgCurPriceInBasket()));
                }
                totalDecimal = totalDecimal.subtract(totalDecimalDis);
            }

        }

        mallGoodsBasketAdapter.notifyDataSetChanged();
        if (totalDecimalDis.doubleValue() > 0) {
            discountMoney.setText("已优惠：" + String.format(Locale.CHINA,
                    "-￥%s", StringUtils.getResultPrice(totalDecimalDis.doubleValue() + "")));
        } else {
            discountMoney.setText("");
        }
        orderMoney.setText(String.format(Locale.CHINA,
                "￥%s", StringUtils.getResultPrice((totalDecimal.doubleValue() > 0 ? totalDecimal.doubleValue() : 0) + "")));
        checkOrder.setText("去结算(" + selectcount + ")");
        if (selectcount == 0) {
            checkOrder.setText("去结算");
        }
    }

    private void buildNowGoods() {
        if (handlerSubmit != null) {
            handlerSubmit.removeCallbacks(runnableSubmit);
        }
        handlerSubmit.postDelayed(runnableSubmit, 100);
    }

    @Override
    public void onSucessGetBasketActList(DiscountTopModel actDetail) {
        mallGoodsBasketAdapter.notifyDataSetChanged();
        buildNowGoods();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1003) {
                if (data != null) {
                    isDataInit = false;
                    String dataResult = data.getStringExtra("actVipResult");
                    ActVip actVip = ObjUtil.getObj(dataResult, ActVip.class);
                    List<GoodsBasketStore> result = mallGoodsBasketAdapter.getStoreList();
                    if (actVip != null) {
                        for (int i = 0; i < result.size(); i++) {
                            if (result.get(i).getDepartID().equals(actVip.DepartID)) {
                                result.get(i).actVipResult = actVip;
                                actVip.Command = "pcPreCalcPop";
                                actVip.setVipShop(vipShop);
                                IsChkPopOK = "S";
                                System.out.println("YT_100002_1060");
                                actVipPresenter.getVipActs(new SimpleHashMapBuilder<String, Object>()
                                        .puts(Functions.FUNCTION, "YT_100002")
                                        .putObject(actVip.sortAndExp(IsChkPopOK)), result.get(i));
                            }
                        }
                    }
                }
            }
            if (requestCode == 1007) {
                if (data != null) {
                    isDataInit = false;
                    String dataResult = data.getStringExtra("popInfo");
                    ActVip.PopInfo popInfo = ObjUtil.getObj(dataResult, ActVip.PopInfo.class);
                    List<GoodsBasketStore> result = mallGoodsBasketAdapter.getStoreList();
                    if (popInfo != null) {
                        for (int i = 0; i < actVipResultList.size(); i++) {
                            if (popInfo.DepartID.equals(actVipResultList.get(i).DepartID)) {
                                ActVip actVipResult = actVipResultList.get(i);
                                if(actVipResult!=null){
                                    for (int j = 0; j < result.size(); j++) {
                                        if (result.get(i).getDepartID().equals(popInfo.DepartID)) {
                                            actVipResult.setPopInfoGoods(popInfo);
                                            actVipResult.Command = "pcPreCalcPop";
                                            actVipResult.ReCalcPopNo = popInfo.PopNo;
                                            actVipResult.setVipShop(vipShop);
                                            IsChkPopOK = "S";
                                            System.out.println("YT_100002_1086");
                                            actVipPresenter.getVipActs(new SimpleHashMapBuilder<String, Object>()
                                                    .puts(Functions.FUNCTION, "YT_100002")
                                                    .putObject(actVipResult.sortAndExp(IsChkPopOK)), result.get(i));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public void onSucessGetBasketActListEx(final GoodsBasketCell goodsBasketCell) {

    }

    @Override
    public void onSucessGetShopDetailOnly(GoodsBasketStore goodsBasketStore) {
        if (goodsBasketStore.getSelectCountWithNoGift() > 0) {

            if ("1".equals(goodsBasketStore.merchantType)) {//异业商家需要特殊处理
                goodsBasketStore.actVipResult = null;//去掉保持他的价格计算没问题 模拟一个
                String Total = goodsBasketStore.getgCurPrice();
                goodsBasketStore.actVipReq = new ActVip();
                goodsBasketStore.actVipReq.buildWithBasket(goodsBasketStore.getGoodsBasketCellList());
                goodsBasketStore.actVipReq.Total = Total;
                goodsBasketStore.actVipReq.DepartID = goodsBasketStore.getDepartID();
                onSucessGetVipActs(goodsBasketStore.actVipReq, goodsBasketStore);
            } else {
                if (goodsBasketStore.actVipResult == null) {//说明刚进来
                    goodsBasketStore.actVipReq = new ActVip();
                    goodsBasketStore.actVipReq.buildWithBasket(goodsBasketStore.getGoodsBasketCellList());
                } else {//已经有返回结果了 说明刚才有过选择并且可能改变了商品数据了
                    goodsBasketStore.actVipReq = goodsBasketStore.actVipResult;
                    goodsBasketStore.actVipReq.buildWithBasket(goodsBasketStore.getGoodsBasketCellList());
                }
                goodsBasketStore.actVipReq.Command = "pcPreCalcPop";
                goodsBasketStore.actVipReq.setVipShop(vipShop);
                goodsBasketStore.actVipReq.DepartID = goodsBasketStore.getDepartID();
                System.out.println("YT_100002_1119");
                actVipPresenter.getVipActs(new SimpleHashMapBuilder<String, Object>()
                                .puts(Functions.FUNCTION, "YT_100002")
                                .putObject(goodsBasketStore.actVipReq.sortAndExp(IsChkPopOK))
                        , goodsBasketStore);
            }


        } else {
            if (mallGoodsItemAdapter.getDatas() == null || mallGoodsItemAdapter.getDatas().size() == 0) {
                serviceBasketPresenter.getGoodsRecommend(recommandMap);
            }
            for (int i = 0; i < goodsBasketStore.goodsBasketGroupList.size(); i++) {
                GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(i);
                for (int j = 0; j < goodsBasketGroup.goodsBasketCellList.size(); j++) {
                    GoodsBasketCell goodsBasketCell = goodsBasketGroup.goodsBasketCellList.get(j);
                    if (goodsBasketCell.isGift) {
                        goodsBasketGroup.goodsBasketCellList.remove(j);
                        j--;
                    }
                }
            }
            onSucessGetVipActs(null,goodsBasketStore);
            showRealBasketView();
        }
    }

    @Override
    public void onSucessGetGoodsActVips() {
        if (mallGoodAdapterSubmit != null) {
            mallGoodAdapterSubmit.removeCallbacks(runmallGoodAdapterSubmit);
        }
        mallGoodAdapterSubmit.postDelayed(runmallGoodAdapterSubmit, 100);
    }

    Runnable runmallGoodAdapterSubmit = new Runnable() {
        @Override
        public void run() {
            if (mallGoodsItemAdapter != null) {
                mallGoodsItemAdapter.notifyDataSetChanged();
            }
        }
    };
    Handler mallGoodAdapterSubmit = new Handler();

    public List<GoodsBasketCell> getGiftCell(ActVip.SaleInfo saleInfo, GoodsBasketStore goodsBasketStore, List<GoodsBasketCell> goodsBasketCellsCardSelect) {
        System.out.println("购物车中" + goodsBasketStore.shopName);
        List<GoodsBasketCell> result = new ArrayList<>();
        if ("Y".equals(saleInfo.IsCardGoods)) {//说明是可使用的优惠券 则要拆分张数摊开
            int count = Integer.parseInt(saleInfo.Number);
            for (int i = 0; i < count; i++) {
                GoodsBasketCell goodsBasketCell;
                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(saleInfo.PopNo);

                goodsMarketing.availableInventory = 1;
//                        goodsMarketing.mapMarketingGoodsId = goodsSpecDetail.mapMarketingGoodsId;
                goodsMarketing.marketingType = "-2";
                goodsMarketing.id = saleInfo.getGoodsID();
                goodsMarketing.marketingPrice = Double.parseDouble(saleInfo.FactPrice);
                try {
                    goodsMarketing.salesMin = 1;
                    goodsMarketing.salesMax = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                goodsBasketCell = new GoodsBasketCell(Double.parseDouble(saleInfo.SalePrice),
                        Double.parseDouble(saleInfo.Price),
                        0,
                        "2",
                        "0", "0", saleInfo.getGoodsID());
                goodsBasketCell.goodsSpecDesc = "";
                goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                goodsBasketCell.mchId = SpUtils.getValue(mContext, SpKey.CHOSE_MC);
                goodsBasketCell.goodsId = "";
                goodsBasketCell.isGift = true;
                goodsBasketCell.isUseCard = !"N".equals(saleInfo.IsCardGoods);//是否是券
                goodsBasketCell.isCardCanUse = "Y".equals(saleInfo.IsCardGoods);//是否是可用的券 用来区分是不是S问题 一般是可用的说明不是S
                goodsBasketCell.isCardSelect = "Y".equals(saleInfo.IsCardGoods);//券选择标志
                goodsBasketCell.CardNo = i;//券编号
                goodsBasketCell.ischeck = true;
                goodsBasketCell.goodsShopId = goodsBasketStore.shopId;
                System.out.println("购物车中" + goodsBasketCell.goodsShopId);
                goodsBasketCell.goodsStock = 1;
//                    goodsBasketCell.setGoodsSpecId(saleInfo.GoodsID);
                goodsBasketCell.setGoodsBarCode(saleInfo.getGoodsID());
                goodsBasketCell.goodsTitle = saleInfo.GoodsName;
//                    goodsBasketCell.goodsImage = saleInfo.goodsImage;
                goodsBasketCell.goodsQuantity = 1;
                System.out.println("设置赠品数量" + goodsBasketCell.goodsQuantity);
                ActVipOnlinePresenter actVipOnlinePresenter = new ActVipOnlinePresenter(this, this);
                actVipOnlinePresenter.getVipOnlineGoodsWithCell(new SimpleHashMapBuilder<String, Object>()
                                .putObject(new ActVipDefault(goodsBasketCell.getGoodsBarCode(),
                                        vipShop.shopId,
                                        "",
                                        goodsBasketCell.goodsTitle,
                                        SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC),
                                        goodsBasketCell.goodsType,
                                        new ActVipDefault.GoodsChildren(goodsBasketCell.getGoodsBarCode(),
                                                goodsBasketCell.curGoodsAmount,
                                                goodsBasketCell.curGoodsRetailAmount
                                        ), new ActVipDefault.GoodsFiles()))
                        , goodsBasketCell);
                int desint = ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(goodsBasketCellsCardSelect, new ObjectIteraor<GoodsBasketCell>() {
                    @Override
                    public Object getDesObj(GoodsBasketCell o) {
                        return o.getGoodsMarketingId() + o.getGoodsBarCode() + o.goodsTitle + o.CardNo;
                    }
                }), goodsBasketCell.getGoodsMarketingId() + goodsBasketCell.getGoodsBarCode() + goodsBasketCell.goodsTitle + goodsBasketCell.CardNo);
                if (desint != -1) {
                    goodsBasketCell.isCardSelect = goodsBasketCellsCardSelect.get(desint).isCardSelect;//券选择标志
                }
                result.add(goodsBasketCell);
            }
        } else {
            GoodsBasketCell goodsBasketCell;
            GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(saleInfo.PopNo);

            goodsMarketing.availableInventory = Integer.parseInt(saleInfo.Number);
//                        goodsMarketing.mapMarketingGoodsId = goodsSpecDetail.mapMarketingGoodsId;
            goodsMarketing.marketingType = "-2";
            goodsMarketing.id = saleInfo.getGoodsID();
            goodsMarketing.marketingPrice = Double.parseDouble(saleInfo.FactPrice);
            try {
                goodsMarketing.salesMin = Integer.parseInt(saleInfo.Number);
                goodsMarketing.salesMax = Integer.parseInt(saleInfo.Number);
            } catch (Exception e) {
                e.printStackTrace();
            }
            goodsBasketCell = new GoodsBasketCell(Double.parseDouble(saleInfo.SalePrice),
                    Double.parseDouble(saleInfo.Price),
                    0,
                    "2",
                    "0", "0", saleInfo.getGoodsID());
            goodsBasketCell.goodsSpecDesc = "";
            goodsBasketCell.goodsMarketingDTO = goodsMarketing;
            goodsBasketCell.mchId = SpUtils.getValue(mContext, SpKey.CHOSE_MC);
            goodsBasketCell.goodsId = "";
            goodsBasketCell.isGift = true;
            goodsBasketCell.isUseCard = !"N".equals(saleInfo.IsCardGoods);//是否是券
            goodsBasketCell.isCardCanUse = "Y".equals(saleInfo.IsCardGoods);//是否是可用的券 用来区分是不是S问题 一般是可用的说明不是S
            goodsBasketCell.ischeck = true;
            goodsBasketCell.goodsShopId = goodsBasketStore.shopId;
            goodsBasketCell.goodsStock = Integer.parseInt(saleInfo.Number);
//                    goodsBasketCell.setGoodsSpecId(saleInfo.GoodsID);
            goodsBasketCell.setGoodsBarCode(saleInfo.getGoodsID());
            goodsBasketCell.goodsTitle = saleInfo.GoodsName;
//                    goodsBasketCell.goodsImage = saleInfo.goodsImage;
            goodsBasketCell.goodsQuantity = Integer.parseInt(saleInfo.Number);
            System.out.println("设置赠品数量" + goodsBasketCell.goodsQuantity);
            ActVipOnlinePresenter actVipOnlinePresenter = new ActVipOnlinePresenter(this, this);
            actVipOnlinePresenter.getVipOnlineGoodsWithCell(new SimpleHashMapBuilder<String, Object>()
                            .putObject(new ActVipDefault(goodsBasketCell.getGoodsBarCode(),
                                    vipShop.shopId,
                                    "",
                                    goodsBasketCell.goodsTitle,
                                    SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC),
                                    goodsBasketCell.goodsType,
                                    new ActVipDefault.GoodsChildren(goodsBasketCell.getGoodsBarCode(),
                                            goodsBasketCell.curGoodsAmount,
                                            goodsBasketCell.curGoodsRetailAmount
                                    ), new ActVipDefault.GoodsFiles()))
                    , goodsBasketCell);
            result.add(goodsBasketCell);
        }


        return result;
    }

    @Override
    public void onSucessGetVipActs(ActVip actVip, GoodsBasketStore goodsBasketStore) {//返回购物车+营销活动结果 可能存在没有赠品 因为可能赠品在缓存里
        goodsBasketStore.actVipResult = actVip;//
        if (actVip == null) {//为null的话 就把移除
            if (actVipResultList != null && actVipResultList.size() > 0) {
                for (int i = 0; i < actVipResultList.size(); i++) {
                    if (actVipResultList.get(i).DepartID != null) {
                        if (actVipResultList.get(i).DepartID.equals(goodsBasketStore.getDepartID())) {//同一个东西
                            actVipResultList.remove(i);
                            i--;
                        }
                    }
                }
            }
            showRealBasketView();
            return;
        }
        actVip.setPopDetailFindInSales();
        goodsBasketStore.IsChkPopOK = "Y";
        if (goodsBasketStore.actVipResult == null) {
//            showRealBasketView();
            return;
        }
        boolean isHistoryEffect = false;
        if (actVipHistory != null) {//有缓存  缓存算法更新下
            for (int i = 0; i < actVipHistory.size(); i++) {
                if (actVip.DepartID.equals(actVipHistory.get(i).DepartID)) {
                    isHistoryEffect = true;
                    goodsBasketStore.actVipResult.checkHistory(actVipHistory.get(i));//进行对缓存用的计算
                    actVipHistory.remove(i);
                    String actVipHistoryString = new Gson().toJson(actVipHistory);
                    SpUtils.store(mContext, SpKey.USER_MARKET_CHECK, actVipHistoryString);//部分删除缓存
                    i--;
                }
            }
        }
        List<GoodsBasketCell> goodsBasketCellsCardSelect = goodsBasketStore.getUnderCardCanUse();
        for (int i = 0; i < goodsBasketStore.goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(i);
            for (int j = 0; j < goodsBasketGroup.goodsBasketCellList.size(); j++) {
                GoodsBasketCell goodsBasketCell = goodsBasketGroup.goodsBasketCellList.get(j);
                if (goodsBasketCell.isGift) {
                    goodsBasketGroup.goodsBasketCellList.remove(j);
                    j--;
                }
            }
        }

        boolean needRemoveSmallerCard=false;
        if(Double.parseDouble(goodsBasketStore.actVipResult.FactTotal)<0){//优惠券用多了啊
            needRemoveSmallerCard=true;
        }

        for (int k = 0; k < goodsBasketStore.goodsBasketGroupList.size(); k++) {
            GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(k);
            for (int i = 0; i < goodsBasketStore.actVipResult.SaleInfo.size(); i++) {//对结果进行展示
                ActVip.SaleInfo saleInfo = goodsBasketStore.actVipResult.SaleInfo.get(i);
                if ("Y".equals(saleInfo.IsZP)) {
                    if (goodsBasketGroup.goodsBasketCellList != null) {
                        goodsBasketGroup.goodsBasketCellList.addAll(getGiftCell(saleInfo, goodsBasketStore, goodsBasketCellsCardSelect));
                    }
                }
            }
        }

        if(needRemoveSmallerCard&&goodsBasketCellsCardSelect.isEmpty()){//如果超出的话 只选择最大面额的券进行使用
            List<GoodsBasketCell> cardCanUseList=goodsBasketStore.getUnderCardCanUse();
            int index=0;
            for (int i = 0; i < cardCanUseList.size(); i++) {
                if(i!=0&&cardCanUseList.get(i).goodsMarketingDTO.marketingPrice<cardCanUseList.get(index).goodsMarketingDTO.marketingPrice){
                    index=i;
                }
            }
            for (int i = 0; i < cardCanUseList.size(); i++) {
                cardCanUseList.get(i).isCardSelect=false;
            }
            cardCanUseList.get(index).isCardSelect=true;
        }

        for (int i = 0; i < goodsBasketStore.goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(i);
            for (int j = 0; j < goodsBasketGroup.goodsBasketCellList.size(); j++) {
                GoodsBasketCell goodsBasketCell = goodsBasketGroup.goodsBasketCellList.get(j);
                if (!goodsBasketCell.isGift && goodsBasketCell.ischeck) {//
                    int desindex = ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(goodsBasketStore.actVipResult.SaleInfo, new ObjectIteraor<ActVip.SaleInfo>() {
                                @Override
                                public Object getDesObj(ActVip.SaleInfo o) {
                                    return o.getGoodsID();
                                }
                            })
                            , goodsBasketCell.getGoodsBarCode());
                    if (desindex != -1) {//匹配条码相同 则对购物车数据进行修改 使用-2活动来标记
                        if (!"Y".equals(goodsBasketStore.actVipResult.SaleInfo.get(desindex).IsErr)) {
                            ActVip.SaleInfo saleInfo = goodsBasketStore.actVipResult.SaleInfo.get(desindex);
                            goodsBasketCell.curGoodsAmount = Double.parseDouble(saleInfo.SalePrice);
                            goodsBasketCell.curGoodsRetailAmount = Double.parseDouble(saleInfo.Price);
                            GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(saleInfo.PopNo);
                            goodsMarketing.availableInventory = goodsBasketCell.getRealStock();//修改-2时的库存 //修改-2时的库存
                            goodsMarketing.marketingType = "-2";
                            goodsMarketing.id = saleInfo.getGoodsID();
                            goodsMarketing.marketingPrice = Double.parseDouble(saleInfo.FactPrice);
                            try {
                                goodsMarketing.salesMin = Integer.parseInt(saleInfo.Number);
                                goodsMarketing.salesMax = Integer.parseInt(saleInfo.Number);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                        }

                    }
                }
            }
        }
        if (isHistoryEffect&&!"1".equals(goodsBasketStore.merchantType)) {
            System.out.println("被历史影响了");
//            showRealBasketView();
            goodsBasketStore.actVipResult.Command = "pcPreCalcPop";
            goodsBasketStore.actVipResult.setVipShop(vipShop);
            goodsBasketStore.actVipResult.DepartID = goodsBasketStore.getDepartID();
            goodsBasketStore.IsChkPopOK = "R";
            System.out.println("YT_100002_1373");
            actVipPresenter.getVipActs(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "YT_100002")
                    .putObject(goodsBasketStore.actVipResult.sortAndExp(goodsBasketStore.IsChkPopOK)), goodsBasketStore);

        } else {
            if (actVipResultList != null && actVipResultList.size() > 0) {
                boolean isActInThis = false;
                for (int i = 0; i < actVipResultList.size(); i++) {
                    if (actVipResultList.get(i).DepartID != null) {
                        if (actVipResultList.get(i).DepartID.equals(actVip.DepartID)) {//同一个东西
                            actVipResultList.set(i, actVip);//重设这个参数
                            isActInThis = true;
                        }
                    } else {
                        actVipResultList.remove(i);
                        i--;
                    }
                }
                if (!isActInThis) {
                    actVipResultList.add(actVip);
                }
                showRealBasketView();


            } else {
                actVipResultList = new ArrayList<>();
                actVipResultList.add(actVip);
                if (mallGoodsItemAdapter.getDatas() == null || mallGoodsItemAdapter.getDatas().size() == 0) {
                    serviceBasketPresenter.getGoodsRecommend(recommandMap);
                }
                showRealBasketView();

            }
        }
    }

    //    private List<GoodsBasketCell> getUnderCardCanUseNoSelect() {
//        List<GoodsBasketCell> result = new ArrayList<>();
//        for (int i = 0; i < getGoodsBasketStores().size(); i++) {
//            result.addAll(getGoodsBasketStores().get(i).getUnderCardCanUseNoSelect());
//        }
//        return result;
//    }
    private ActVip getOnlyActAll(List<ActVip> model) {
        ActVip actVip = new ActVip();
        boolean hasPop = false;
        for (int i = 0; i < model.size(); i++) {
            model.get(i).buildPopInfo();//把DetpId下放到PopInfo中
            if (model.get(i).PopInfo.size() > 0) {
                hasPop = true;
            }
            actVip.PopInfo.addAll(model.get(i).PopInfo);

        }
        if (!hasPop) {
            return null;
        }
        Collections.sort(actVip.PopInfo);
        return actVip;
    }

    private void showRealBasketView() {
        if (orggoodsBasketStoreList == null || orggoodsBasketStoreList.size() == 0) {
            System.out.println("购物车展示1");
            orggoodsBasketStoreList = getGoodsBasketStores(null);
            System.out.println("购物车展示1" + orggoodsBasketStoreList.size());
            return;
        }
        System.out.println("购物车展示12");
        if (actVipResultList != null && actVipResultList.size() > 0) {
//            mallGoodsBasketDiscountTopAdapter.setModel(null);
            if (getOnlyActAll(actVipResultList) != null) {
                if (mallGoodsBasketAdapter.getSelectMapSize() > 0) {
                    mallGoodsBasketDiscountTopAdapter.setModel(actVipResultList);
                }
            } else {
                mallGoodsBasketDiscountTopAdapter.setModel(null);
            }
        } else {
            for (int k = 0; k < orggoodsBasketStoreList.size(); k++) {
                GoodsBasketStore goodsBasketStore = orggoodsBasketStoreList.get(k);
                goodsBasketStore.actVipResult = null;
                for (int i = 0; i < goodsBasketStore.goodsBasketGroupList.size(); i++) {
                    GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(i);
                    for (int j = 0; j < goodsBasketGroup.goodsBasketCellList.size(); j++) {
                        GoodsBasketCell goodsBasketCell = goodsBasketGroup.goodsBasketCellList.get(j);
                        goodsBasketCell.goodsMarketingDTO = null;
                        if (goodsBasketCell.isGift) {
                            goodsBasketGroup.goodsBasketCellList.remove(j);
                            j--;
                        }
                    }
                }
            }
            mallGoodsBasketDiscountTopAdapter.setModel(null);
        }
        if (mallGoodsItemAdapter.getDatas() == null || mallGoodsItemAdapter.getDatas().size() == 0) {
            serviceBasketPresenter.getGoodsRecommend(recommandMap);
        }
        mallGoodsBasketAdapter.setData((ArrayList<GoodsBasketStore>) orggoodsBasketStoreList);
        mallGoodsBasketDiscountTopAdapter.notifyDataSetChanged();
        mallGoodsBasketAdapter.notifyDataSetChanged();
        buildNowGoods();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                recyclerList.scrollToPosition(0);
            }
        }, 100);
    }

    @Override
    public void onSucessGetVipShopDetail(ActVip.VipShop vipShop) {
        this.vipShop = vipShop;
        getBasketList();
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("打开营销活动".equals(function)) {
            GoodsBasketStore goodsBasketStore = (GoodsBasketStore) obj;
            ActVip.VipShop vipShopPass = vipShop;
            goodsBasketStore.actVipResult.buildWithBasket(goodsBasketStore.getGoodsBasketCellList());
            vipShopPass.ytbDepartID = goodsBasketStore.getDepartID();
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_BASKET_DISCOUNT)
                    .withObject("actVipReq", goodsBasketStore.actVipResult)
                    .withObject("vipShop", vipShopPass)
                    .withObject("goodsbasketlist", mallGoodsBasketAdapter.getSelectOnlyGoodsList())
                    .navigation(this, 1003);
        }
        if ("查看更多".equals(function)) {
            ActVip.PopInfo popInfo = (ActVip.PopInfo) obj;
            for (int i = 0; i < popInfo.PopDetail.size(); i++) {
                if (popInfo.PopDetail.get(i).isIscheck()) {
                    System.out.println("设置活动商品选中为:" + popInfo.PopDetail.get(i).GoodsName);
                }
            }
            ActVip.VipShop vipShopPass = vipShop;
            vipShopPass.ytbDepartID = popInfo.DepartID;
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_BASKET_DISCOUNT_SINGLE)
                    .withObject("popInfo", popInfo)
                    .withObject("vipShop", vipShopPass)
                    .navigation(this, 1007);
        }
        if ("刷新优惠券结果".equals(function)) {
            buildNowGoods();
        }
    }

    @Override
    public void onSucessGetVipOnlineGoods() {
        mallGoodsBasketAdapter.notifyDataSetChanged();
        if (actVipResultList != null && actVipResultList.size() > 0) {
//            mallGoodsBasketDiscountTopAdapter.setModel(null);
            if (getOnlyActAll(actVipResultList) != null) {
                if (mallGoodsBasketAdapter.getSelectMapSize() > 0) {
                    mallGoodsBasketDiscountTopAdapter.setModel(actVipResultList);
                }
            } else {
            }
        } else {
            mallGoodsBasketDiscountTopAdapter.setModel(null);
        }
        mallGoodsBasketDiscountTopAdapter.notifyDataSetChanged();
        buildNowGoods();
    }
}
