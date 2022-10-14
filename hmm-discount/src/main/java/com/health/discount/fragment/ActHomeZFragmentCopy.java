package com.health.discount.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.health.discount.R;
import com.health.discount.adapter.ActZBlockAdapter;
import com.health.discount.adapter.ActZBottomBannerAdapter;
import com.health.discount.adapter.ActZCenterBannerAdapter;
import com.health.discount.adapter.ActZEmptyAdapter;
import com.health.discount.adapter.ActZGroupAdapter;
import com.health.discount.adapter.ActZKickAdapter;
import com.health.discount.adapter.ActZKillAdapter;
import com.health.discount.adapter.ActZPointVideoAdapter;
import com.health.discount.adapter.ActZStatusAdapter;
import com.health.discount.adapter.ActZTabAdapter;
import com.health.discount.adapter.ActZTabViewPagerAdapterS1;
import com.health.discount.adapter.ActZTabViewPagerAdapterS3;
import com.health.discount.adapter.ActZTabViewPagerAdapterS4;
import com.health.discount.adapter.ActZTopBannerAdapter;
import com.health.discount.contract.ActBlockContract;
import com.health.discount.contract.ActCityBuySecondBlockContract;
import com.health.discount.contract.ActHomeBlockContract;
import com.health.discount.contract.ActHomeZContract;
import com.health.discount.contract.LiveListTContract;
import com.health.discount.model.LiveListModel;
import com.health.discount.model.PointTab;
import com.health.discount.model.SeckillTab;
import com.health.discount.presenter.ActBlockPresenter;
import com.health.discount.presenter.ActCitySecondBlockPresenter;
import com.health.discount.presenter.ActHomeBlockPresenter;
import com.health.discount.presenter.ActHomeZPresenter;
import com.health.discount.presenter.LiveListCopyPresenter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.ADDialogView;
import com.healthy.library.business.ElcCardDialogView;
import com.healthy.library.business.LocMessageDialog;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.AdContract;
import com.healthy.library.contract.BasketCountContract;
import com.healthy.library.contract.LiveGoodsContract;
import com.healthy.library.contract.PlusContract;
import com.healthy.library.message.DialogDissmiss;
import com.healthy.library.message.UpdateMallInfo;
import com.healthy.library.message.UpdateSeachInfo;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.message.UpdateUserShop;
import com.healthy.library.model.ActGoodsCityItem;
import com.healthy.library.model.ActGoodsItem;
import com.healthy.library.model.ActGroup;
import com.healthy.library.model.ActKick;
import com.healthy.library.model.ActKill;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.model.MainIconModel;
import com.healthy.library.model.MainMenuModel;
import com.healthy.library.model.MainSearchModel;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.SecondSortGoods;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.model.ShopInfo;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.BasketCountPresenter;
import com.healthy.library.presenter.ChangeVipPresenter;
import com.healthy.library.presenter.LiveGoodsPresenter;
import com.healthy.library.presenter.PlusPresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.ImageTextView;
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
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Route(path = DiscountRoutes.DIS_ACT_HOMEZ2)
public class ActHomeZFragmentCopy extends BaseFragment implements AdContract.View, LiveGoodsContract.View, BasketCountContract.View,
        ActHomeBlockContract.View, BaseAdapter.OnOutClickListener, ActCityBuySecondBlockContract.View, LiveListTContract.View,
        ActHomeZContract.View, OnRefreshLoadMoreListener, ActBlockContract.View, PlusContract.View {

    private ActHomeZPresenter actHomeZPresenter;
    private ActBlockPresenter actBlockPresenter;
    private LiveListCopyPresenter liveListCopyPresenter;
    private ActCitySecondBlockPresenter actCityBlockPresenter;
    //-----------------------------------------------------------------
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerview;
    private RelativeLayout seachTopLL;
    private LinearLayout seachTopBgLL;
    private LinearLayout seachTop;
    private TextView superShopName;
    private ImageView passMessage;
    private ImageTextView disVipCardTop;
    private ImageTextView disLoc;
    private ImageView disLocMore;
    private LinearLayout serarchKeyWordLL;
    private TextView serarchKeyWord;
    private ImageTextView disVipCard;
    private ConstraintLayout noshopenter;
    private TextView goodsTvTitle;
    private TextView goodsTvChooseCity;
    private ImageView ivBottomShader;

    //-----------------------------------------------------------------------
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    //-----------------------------------------------------------------------
    ActZBlockAdapter actZBlockAdapter;
    ActZBottomBannerAdapter actZBottomBannerAdapter;
    ActZCenterBannerAdapter actZCenterBannerAdapter;
    ActZGroupAdapter actZGroupAdapter;
    ActZKickAdapter actZKickAdapter;
    ActZKillAdapter actZKillAdapter;
    ActZPointVideoAdapter actZPointVideoAdapter;
    ActZStatusAdapter actZStatusAdapter;
    ActZTabAdapter actZTabAdapter;
    //    ActZTabViewPagerAdapter actZTabViewPagerAdapter;
    ActZTabViewPagerAdapterS1 actZTabViewPagerAdapter1;
    ActZTabViewPagerAdapterS3 actZTabViewPagerAdapter3;
    ActZTabViewPagerAdapterS4 actZTabViewPagerAdapter4;
    ActZEmptyAdapter actZEmptyAdapter;
    ActZTopBannerAdapter actZTopBannerAdapter;
//    ActZCityAdapter actZCityAdapter;
//--------------------------------------------------------------------------

    public int page = 1;
    public int firstPageSize = 0;

    public int nowTab = 1;
    public boolean needTabTop = false;
    private int tabNeedPosition = 1;


    private StatusLayout layoutStatus;
    private ConstraintLayout shopCartRel;
    private ImageView goSub;
    private TextView shopCartNum;
    private ImageView mallScrollUp;

    BasketCountPresenter basketCountPresenter;
    AdPresenter adPresenter;
    PlusPresenter plusPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.dis_fragment_actallz2;
    }

    @Override
    protected void findViews() {
        initView();
        buildRecyclerView();
        adPresenter = new AdPresenter(mContext, this);
        basketCountPresenter = new BasketCountPresenter(mContext, this);
        actBlockPresenter = new ActBlockPresenter(mContext, this);
        actHomeZPresenter = new ActHomeZPresenter(mContext, this);
        actCityBlockPresenter = new ActCitySecondBlockPresenter(mContext, this);
        liveListCopyPresenter = new LiveListCopyPresenter(mContext, this);
        plusPresenter = new PlusPresenter(mContext, this);
        isChanged=false;
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
        getData();
    }

    int alldy = 0;

    @Override
    public void getData() {
        super.getData();
        tabNeedPosition = 1;
        if (!TextUtils.isEmpty(LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))) {
            System.out.println("需要展示广告Z");
            actHomeZPresenter.getActLocVip(new SimpleHashMapBuilder<String, Object>());
        }

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "1").puts("triggerPage", "16").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);
        recyclerview.setItemAnimator(null);

        actZStatusAdapter = new ActZStatusAdapter();
        actZStatusAdapter.setModel("null");
        delegateAdapter.addAdapter(actZStatusAdapter);
        actZTopBannerAdapter = new ActZTopBannerAdapter();
        actZTopBannerAdapter.setModel("");
        delegateAdapter.addAdapter(actZTopBannerAdapter);
        actZKillAdapter = new ActZKillAdapter();
        actZKillAdapter.setModel("");
        delegateAdapter.addAdapter(actZKillAdapter);
        actZGroupAdapter = new ActZGroupAdapter();
        actZGroupAdapter.setModel("");
        delegateAdapter.addAdapter(actZGroupAdapter);
        actZKickAdapter = new ActZKickAdapter();
        actZKickAdapter.setModel("");
        delegateAdapter.addAdapter(actZKickAdapter);
        actZPointVideoAdapter = new ActZPointVideoAdapter();
        actZPointVideoAdapter.setModel("");
        delegateAdapter.addAdapter(actZPointVideoAdapter);
        actZCenterBannerAdapter = new ActZCenterBannerAdapter();
        actZCenterBannerAdapter.setModel("");
        delegateAdapter.addAdapter(actZCenterBannerAdapter);
        actZBlockAdapter = new ActZBlockAdapter();
        delegateAdapter.addAdapter(actZBlockAdapter);
//        actZCityAdapter = new ActZCityAdapter();
//        actZCityAdapter.setModel(null);
//        delegateAdapter.addAdapter(actZCityAdapter);
        actZBottomBannerAdapter = new ActZBottomBannerAdapter();
        actZBottomBannerAdapter.setModel("");
        delegateAdapter.addAdapter(actZBottomBannerAdapter);
        actZTabAdapter = new ActZTabAdapter(mContext, this);
        actZTabAdapter.setModel("");
        actZTabAdapter.setOutClickListener(this);
        delegateAdapter.addAdapter(actZTabAdapter);

//        actZTabViewPagerAdapter = new ActZTabViewPagerAdapter();
//        delegateAdapter.addAdapter(actZTabViewPagerAdapter);

        actZTabViewPagerAdapter4 = new ActZTabViewPagerAdapterS4();
        delegateAdapter.addAdapter(actZTabViewPagerAdapter4);

        actZTabViewPagerAdapter3 = new ActZTabViewPagerAdapterS3();
        delegateAdapter.addAdapter(actZTabViewPagerAdapter3);

        actZTabViewPagerAdapter1 = new ActZTabViewPagerAdapterS1();
        delegateAdapter.addAdapter(actZTabViewPagerAdapter1);

        actZEmptyAdapter = new ActZEmptyAdapter();
        delegateAdapter.addAdapter(actZEmptyAdapter);

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
                if (virtualLayoutManager.findFirstVisibleItemPosition() >= 1) {
//                    System.out.println("切换目标到了"+alpha);
                    alpha = 100;
                }
//                System.out.println("设置头部值"+alpha);
                seachTopBgLL.setAlpha(alpha);
            }
        });
        serarchKeyWordLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "商城首页搜索框点击量");
                MobclickAgent.onEvent(mContext, "event2APPShopSeachClick", nokmap);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_SERVICESORTGOODS)
                        .withString("categoryId", 0 + "").withString("goodsTitle", "").navigation();
            }
        });
        disVipCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "商城首页顶部分类按钮点击量");
                MobclickAgent.onEvent(mContext, "event2APPShopHomeClassClick", nokmap);
                ARouter.getInstance().build(ServiceRoutes.SERVICE_SORT).navigation();
            }
        });
        disVipCardTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "商城首页会员卡的点击量");
                MobclickAgent.onEvent(mContext, "event2APPShopHomeElectronicCardClick", nokmap);
                ElcCardDialogView.newInstance().show(getChildFragmentManager(), "电子卡");
            }
        });
        actZTabViewPagerAdapter4.setClickListener(new ActZTabViewPagerAdapterS4.ClickListener() {
            @Override
            public void outClick(String function, String data1, String data2) {
                if ("remind".equals(function)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("course_id", data1);
                    liveListCopyPresenter.subVideo(map);
                }
                if ("live".equals(function)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("course_id", data1);
                    map.put("nickname", SpUtils.getValue(mContext, SpKey.USER_NICK));
                    map.put("liveStatus", data2);
                    liveListCopyPresenter.getLiveLink(map);
                }
            }
        });
        refreshLayout.finishLoadMoreWithNoMoreData();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserLocationMsg msg) {
        if(!isFirstLoad) return;
        recyclerview.scrollToPosition(0);
        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))) {
            onSucessGetActMan(true);
        } else {
            getData();
        }
        setTitle();
    }
    public boolean isChanged=false;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeLocShop(UpdateUserShop msg) {
//        if(!isChanged){
//            showLocDialog(msg);
//        }
        showLocDialog(msg);
    }

    private void showLocDialog(final UpdateUserShop updateUserShop) {
        StyledDialog.init(mContext);
        StyledDialog.buildIosAlert("当前访问的门店关店中!", "可能对您购买造成不便,我们将为您切换最近的营业中的门店,是否立即切换?", new MyDialogListener() {
            @Override
            public void onFirst() {

            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                isChanged=true;
                try {
                    LocUtil.storeLocationChose(mContext,updateUserShop.aMapLocation);
                    LocUtil.setNowShop(updateUserShop.nowShop);
                    updateUserInfo(new UpdateUserLocationMsg());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("否", "是").show();
        SpUtils.store(mContext,SpKey.KILLSETTINGTIME,System.currentTimeMillis());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateDialogDissmiss(DialogDissmiss msg) {
        disLocMore.setImageDrawable(getResources().getDrawable(R.drawable.flash_buy_tab_down));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateMallInfo(UpdateMallInfo msg) {
        if (!isFirstLoad) return;
        if (msg.flag == 199) {
            //清空切换时的重置状态
            if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))) {
                onSucessGetActMan(true);
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSeachInfo(UpdateSeachInfo msg) {
        //System.out.println("刷新数据");
        if (!isFirstLoad) return;
        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))) {
            actHomeZPresenter.getSeachTipList(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)));
            adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "1").puts("triggerPage", "16").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
        }


    }

    @Override
    public void changeFragmentShow() {
        super.changeFragmentShow();
        //System.out.println("重新进来");
//        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))) {
//            actHomeZPresenter.getSeachTipList(new SimpleHashMapBuilder<String, Object>()
//                    .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)));
//            adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "1").puts("triggerPage", "16").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
//        }
        onFirstData();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        //System.out.println("重新进来");
//        actHomeZPresenter.getSeachTipList(new SimpleHashMapBuilder<String, Object>()
//                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)));
//    }

    /**
     * 设置品牌标题
     */
    private void setTitle() {
        /**
         * 2021-06-07 格式改成：【商家品牌名】+憨妈妈
         */
        String mShopBrandName = "憨妈妈云商城";
        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.SHOP_BRAND))
                && !SpUtils.getValue(mContext, SpKey.SHOP_BRAND).equals("null")) {
            mShopBrandName = SpUtils.getValue(mContext, SpKey.SHOP_BRAND);
            mShopBrandName.replace("null", "憨妈妈云商城");
        }
        if (superShopName != null) {
            superShopName.setText(mShopBrandName + " · 憨妈妈");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSucessGetSeachTipList(List<MainSearchModel> list) {
        if (list != null && list.size() > 0) {
            Map<String, MainSearchModel> stringMainSeachModelMap = new HashMap<>();
            List<MainSearchModel> listresult = new ArrayList<>();
            Set set = new HashSet();
            for (Iterator<MainSearchModel> inter = list.iterator(); inter.hasNext();) {
                MainSearchModel element = inter.next();
                if (set.add(element.keyword))
                    listresult.add(element);
            }
//            for (int i = 0; i < (list.size() > 10 ? 10 : list.size()); i++) {
//                stringMainSeachModelMap.put(list.get(i).keyword, list.get(i));
//            }
//            Set<Map.Entry<String, MainSearchModel>> set = stringMainSeachModelMap.entrySet();
//            for (Map.Entry<String, MainSearchModel> me : set) {
//                listresult.add(me.getValue());
//            }
            actZStatusAdapter.setSeachList(listresult);
            actZStatusAdapter.notifyDataSetChanged();
        } else {
            actZStatusAdapter.setSeachList(list);
            actZStatusAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onGetFucList(List<MainMenuModel> list, MainIconModel mainIconModel, List<AdModel> adModels) {
        actZStatusAdapter.setIsInit(false);
        actZStatusAdapter.setFunList(list, mainIconModel);
        actZStatusAdapter.setAdv(adModels);
        actZStatusAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSucessGetActKill(List<ActKill> list) {
        if (list == null || list.size() == 0) {
            actZKillAdapter.setModel(null);
        } else {
            tabNeedPosition++;
            actZKillAdapter.setModel("null");
        }
        actZKillAdapter.setKillList(list);

    }

    @Override
    public void onSucessGetActGroup(List<ActGroup> list) {
        if (list == null || list.size() == 0) {
            actZGroupAdapter.setModel(null);
        } else {
            tabNeedPosition++;
            actZGroupAdapter.setModel("null");
        }
        actZGroupAdapter.setGroupList(list);

    }

    @Override
    public void onSucessGetActKick(List<ActKick> list) {
        if (list == null || list.size() == 0) {
            actZKickAdapter.setModel(null);
        } else {
            tabNeedPosition++;
            actZKickAdapter.setModel("null");
        }
        actZKickAdapter.setKickList(list);

    }

    List<PointTab.PointGoods> pointGoodsList;
    List<LiveListModel> liveListModels;

    @Override
    public void onPointRecommendSuccess(List<PointTab.PointGoods> pointGoodsList) {
        this.pointGoodsList = pointGoodsList;
        actHomeZPresenter.getVideoList(new SimpleHashMapBuilder<String, Object>()
                .puts("page", new SimpleHashMapBuilder<String, Object>()
                        .puts("pageSize", 10 + "")
                        .puts("pageNum", 1 + ""))
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
    }

    List<LiveVideoMain> liveVideoMainList;

    @Override
    public void onSucessMainGetLiveList(List<LiveVideoMain> result) {
        liveVideoMainList = result;
        if ((result == null || result.size() == 0) && (pointGoodsList == null || pointGoodsList.size() == 0)) {
            actZPointVideoAdapter.setModel(null);
        } else {
            tabNeedPosition++;
            actZPointVideoAdapter.setModel("null");
            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    LiveGoodsPresenter liveGoodsPresenter = new LiveGoodsPresenter(mContext, this);
                    if (result.get(i).activityIdList != null) {
                        liveGoodsPresenter.getLiveGoods(result.get(i), new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", result.get(i).activityIdList));
                    } else {
                        result.get(i).liveVideoGoodsList = new ArrayList<>();
                    }

                }
            }

        }
        actZPointVideoAdapter.setPonitAndVideo(pointGoodsList, liveVideoMainList);

    }


    @Override
    public void subVideoSucess() {
//        getData();
    }

    @Override
    public void getLiveLinkSucess(LiveVideoMain linkModel) {
        if (linkModel != null) {
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_WEBVIEW)
//                    .withString("title", "憨妈直播")
//                    .withBoolean("isinhome", false)
//                    .withBoolean("leftnow", true)
//                    .withBoolean("videoshop", true)
//                    .withString("url", linkModel.miniProgramUrl)
//                    .navigation();
        } else {
            showToast("该直播未生成回放");
        }
    }

    @Override
    public void onSucessGetBlockList(List<MainBlockModel> list) {
//        list= ListUtil.where(list, new ListUtil.Where<MainBlockModel>() {
//            @Override
//            public boolean where(MainBlockModel obj) {
//                return obj.exhibition==2;
//            }
//        });
        tabNeedPosition += list.size();
        if (list != null && list.size() > 0) {
//            Collections.sort(list);//注释掉按照类型归类排序 使用按照列表本身的排序
            for (int position = 0; position < list.size(); position++) {
                MainBlockModel item = list.get(position);
                if (item.childList != null && item.childList.size() > 0) {
                    for (int i = 0; i < item.childList.size(); i++) {
                        MainBlockModel itemchild = item.childList.get(i);
                        if (itemchild.detailList == null) {
//                            itemchild.detailList=new ArrayList<>();
                            ActHomeBlockPresenter actHomeBlockPresenter = new ActHomeBlockPresenter(mContext, this);
                            actHomeBlockPresenter.getBlockDetail(null, item, itemchild, position, new SimpleHashMapBuilder<String, Object>().puts("themeId", itemchild.id + "").puts("pageNum", "1").puts("pageSize", "10"));
                        } else {

                        }

                    }

                } else {
                    if (item.detailList == null) {
//                    item.detailList=new ArrayList<>();
                        if (item.exhibition == 6) {
                            ActHomeBlockPresenter actHomeBlockPresenter = new ActHomeBlockPresenter(mContext, this);
                            actHomeBlockPresenter.getBlockDetail(null, item, item, position,
                                    new SimpleHashMapBuilder<String, Object>()
                                            .puts("themeId", item.id + "")
                                            .puts("pageNum", "1")
                                            .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                                            .puts("pageSize", "4"));
                        } else if (item.exhibition == 7) {
                            if (item.getRealAllianceMerchantList() != null && item.getRealAllianceMerchantList().size() > 0) {
                                for (int i = 0; i < item.getRealAllianceMerchantList().size(); i++) {
                                    //item.allianceMerchantList.get(i).id = item.id;
                                    ActHomeBlockPresenter actHomeBlockPresenter = new ActHomeBlockPresenter(mContext, this);
                                    actHomeBlockPresenter.getBlockDetailCity(null, item, item.getRealAllianceMerchantList().get(i), position,
                                            new SimpleHashMapBuilder<String, Object>()
                                                    .puts("themeId", item.id + "")
                                                    .puts("pageNum", "1")
                                                    .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                                                    .puts("mapThemeAllianceMerchantId", item.getRealAllianceMerchantList().get(i).id)
                                                    .puts("pageSize", "4"));
                                }
                            }
                        } else {
                            ActHomeBlockPresenter actHomeBlockPresenter = new ActHomeBlockPresenter(mContext, this);
                            actHomeBlockPresenter.getBlockDetail(null, item, item, position,
                                    new SimpleHashMapBuilder<String, Object>()
                                            .puts("themeId", item.id + "")
                                            .puts("pageNum", "1")
                                            .puts("pageSize", "10"));
                        }

                    } else {

                    }
                }

            }

        }
        actZBlockAdapter.setData((ArrayList<MainBlockModel>) list);
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    @Override
    public void onSucessGetActMan(boolean hasman) {
        //pa
        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "1").puts("triggerPage", "16").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
        noshopenter.setVisibility(View.GONE);
        //superShopName.setText("憨妈妈云商城·" + LocUtil.getCityName(mContext, SpKey.LOC_CHOSE));
        setTitle();
        disLoc.setText(SpUtils.getValue(mContext, SpKey.CHOSE_SHOPNAMEDETAIL));
        disLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disLocMore.setImageDrawable(getResources().getDrawable(R.drawable.flash_buy_tab_up));
                LocMessageDialog.newInstance().show(getChildFragmentManager(), "地址选择");
            }
        });
        refreshLayout.setVisibility(View.VISIBLE);
        initViewData();
        changeTab(false, nowTab);
    }

    public void initViewData() {
        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "1").puts("triggerPage", "16").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
        actHomeZPresenter.getZxingCode();
        actHomeZPresenter.getSeachTipList(new SimpleHashMapBuilder<String, Object>()
                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)));
        actHomeZPresenter.getFucList(new SimpleHashMapBuilder<String, Object>().puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)));
        actHomeZPresenter.getAdvBlock(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "19"));
        actHomeZPresenter.getActKillHistoryList(new SimpleHashMapBuilder<String, Object>());
        actHomeZPresenter.getPinList(new SimpleHashMapBuilder<String, Object>());
        actHomeZPresenter.getKickList(new SimpleHashMapBuilder<String, Object>());
        actHomeZPresenter.getPointList(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION, "9117")
                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                .puts("pageNum", 1 + "")
                .puts("pageSize", 10 + "")
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
        actHomeZPresenter.getBlockList(new SimpleHashMapBuilder<String, Object>()
                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))

        );
        actHomeZPresenter.getBottomActTabs(new SimpleHashMapBuilder<String, Object>());
//        actHomeZPresenter.getBannerTopTop(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "12"));
        actHomeZPresenter.getBannerTop(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "12"));
        actHomeZPresenter.getBannerCenter(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "13"));
        actHomeZPresenter.getBannerBottom(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "15"));
//        actHomeZPresenter.getActShopGoodsList(new SimpleHashMapBuilder<String, Object>()
//                .puts(Functions.FUNCTION, "9122")
//                .puts("pageNum", "1")
//                .puts("pageSize", "6")
//                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
//                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
//                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
//        );
        basketCountPresenter.getShopCart();
        try {
            plusPresenter.checkPlus(new SimpleHashMapBuilder<String, Object>()
                    .puts("phoneNum", SpUtils.getValue(mContext, SpKey.PHONE))
                    .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void changeTab(boolean needTabTop, int index) {

        nowTab = index;
        this.needTabTop = needTabTop;
        if (page == 1) {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
            firstPageSize = 0;
        }
        if (index == 0) {
            actBlockPresenter.getActGoodsList(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "9119")
                    .puts("type", 1 + "")
                    .puts("pageNum", page + "")
                    .puts("pageSize", 10 + "")
                    .puts("firstPageSize", firstPageSize + "")
                    .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
            );
        } else if (index == 1) {
            actBlockPresenter.getActGoodsList(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "9119")
                    .puts("type", 2 + "")
                    .puts("pageNum", page + "")
                    .puts("pageSize", 10 + "")
                    .puts("firstPageSize", firstPageSize + "")
                    .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
            );
        } else if (index == 2) {
            actCityBlockPresenter.getActGoodsList(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "9201-1")
                    .puts("goodsTypes","1".split(","))
                    .puts("publish", 1)
                    .puts("pageNum", page)
                    .puts("pageSize", 10)
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("appSalesSort", "desc")
                    .puts("platformPriceSort", "asc")
                    .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
            );
        } else {
            liveListCopyPresenter.getLiveList(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "9005")
                    .puts("page", new SimpleHashMapBuilder<String, Object>()
                            .puts("pageSize", 10 + "")
                            .puts("pageNum", page + ""))
                    .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
            );
        }
    }

    @Override
    public void onSucessGetAdvBlock(final List<AdModel> adModels) {
        if (adModels != null && adModels.size() > 0) {
            com.healthy.library.businessutil.GlideCopy.with(this)
                    .load(adModels.get(0).photoUrls)
                    
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            actZBlockAdapter.setAdv(adModels);
                        }
                    });
        } else {
            actZBlockAdapter.setAdv(null);
        }

    }

    @Override
    public void onSucessGetTopAds(List<AdModel> adModels) {
        actZTopBannerAdapter.setModel(null);
        if (adModels == null || adModels.size() == 0) {
            actZTopBannerAdapter.setModel(null);
        } else {
            tabNeedPosition++;
            com.healthy.library.businessutil.GlideCopy.with(this)
                    .load(adModels.get(0).photoUrls)
                    
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            actZTopBannerAdapter.setModel("null");
                        }
                    });
        }
        actZTopBannerAdapter.setAdv(adModels);

    }

    @Override
    public void onSucessGetTopTopAds(List<AdModel> adModels) {
//        if (adModels == null || adModels.size() == 0) {
//            actZCenterBannerAdapter.setModel(null);
//        } else {
//            tabNeedPosition++;
//            actZCenterBannerAdapter.setModel("null");
//        }
//        actZStatusAdapter.setAdv(adModels);
    }

    @Override
    public void onSucessGetCenterAds(List<AdModel> adModels) {
        actZCenterBannerAdapter.setModel(null);
        if (adModels == null || adModels.size() == 0) {
            actZCenterBannerAdapter.setModel(null);
        } else {
            tabNeedPosition++;
            com.healthy.library.businessutil.GlideCopy.with(this)
                    .load(adModels.get(0).photoUrls)
                    
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            actZCenterBannerAdapter.setModel("null");
                        }
                    });
        }
        actZCenterBannerAdapter.setAdv(adModels);

    }

    @Override
    public void onSucessGetBottomAds(List<AdModel> adModels) {
        actZBottomBannerAdapter.setModel(null);
        if (adModels == null || adModels.size() == 0) {
            actZBottomBannerAdapter.setModel(null);
        } else {
            tabNeedPosition++;
            com.healthy.library.businessutil.GlideCopy.with(this)
                    .load(adModels.get(0).photoUrls)
                    
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            actZBottomBannerAdapter.setModel("null");
                        }
                    });
        }
        actZBottomBannerAdapter.setAdv(adModels);

    }

    @Override
    public void onSucessGetActKillHistoryList(SeckillTab seckillTab) {
        actZKillAdapter.setModel(null);
        if (seckillTab != null) {
            if (seckillTab.marketingId != null && !TextUtils.isEmpty(seckillTab.marketingId)) {
                if (seckillTab.todayActivityNew != null && seckillTab.todayActivityNew.size() > 0) {//说明有今日的秒杀活动
                    int kiss = 0;
                    for (int i = 0; i < seckillTab.todayActivityNew.size(); i++) {
                        if (seckillTab.todayActivityNew.get(i).status == 1) {
                            kiss++;
                        }
                    }
                    if (kiss > 0) {//说明当前已经有开始了的秒杀活动  则要展示已经开始了的秒杀活动
                        actZKillAdapter.setIsStart(true);
                        actHomeZPresenter.getKillList(new SimpleHashMapBuilder<String, Object>().puts("marketingId", seckillTab.marketingId));
                    } else {
                        if (seckillTab.history == 1) {//没有开始的活动，有历史活动，那么展示历史活动
                            actZKillAdapter.setIsStart(true);
                            actHomeZPresenter.getKillList(new SimpleHashMapBuilder<String, Object>().puts("historyQuery", "1"));
                        } else {//没有开始的活动，并且没有历史活动，那么就说明全是预售   全是预售的情况下  展示第一个预售
                            actZKillAdapter.setIsStart(false);
                            actHomeZPresenter.getKillList(new SimpleHashMapBuilder<String, Object>().puts("marketingId", seckillTab.todayActivityNew.get(0).id));
                        }
                    }
                } else {//走到这里应该是出异常了  有正在进行的秒杀活动Id 但是没有活动列表  判断下有没有历史再说吧
                    if (seckillTab.history == 1) {
                        actZKillAdapter.setIsStart(true);
                        actHomeZPresenter.getKillList(new SimpleHashMapBuilder<String, Object>().puts("historyQuery", "1"));
                    }
                }
            } else {
                if (seckillTab.history == 1) {
                    actZKillAdapter.setIsStart(true);
                    actHomeZPresenter.getKillList(new SimpleHashMapBuilder<String, Object>().puts("historyQuery", "1"));
                }
            }
        }
    }

    @Override
    public void onSucessGetList(List<ActGoodsCityItem> result, ShopInfo shopInfo) {
//        if (shopInfo == null) {
//            actZCityAdapter.setModel(null);
//        } else {
//            tabNeedPosition++;
//            shopInfo.actGoodsCityItems=result;
//            actZCityAdapter.setData(new SimpleArrayListBuilder<ShopInfo>().adds(shopInfo));//暂时设置2个
//        }
    }

    @Override
    public void getZxingCode(String result) {
        SpUtils.store(mContext, SpKey.GETTOKEN, result);

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        changeTab(false, nowTab);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
//        getData();
        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))) {
            onSucessGetActMan(true);
            new ChangeVipPresenter(mContext).getLocVip(new SimpleHashMapBuilder<String, Object>());
        } else {
            getData();
        }
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        seachTopLL = (RelativeLayout) findViewById(R.id.seachTopLL);
        seachTopBgLL = (LinearLayout) findViewById(R.id.seachTopBgLL);
        seachTop = (LinearLayout) findViewById(R.id.seachTop);
        superShopName = (TextView) findViewById(R.id.superShopName);
        passMessage = (ImageView) findViewById(R.id.passMessage);
        disVipCardTop = (ImageTextView) findViewById(R.id.dis_vip_card_top);
        disLoc = (ImageTextView) findViewById(R.id.dis_loc);
        disLocMore = (ImageView) findViewById(R.id.dis_loc_more);
        serarchKeyWordLL = (LinearLayout) findViewById(R.id.serarchKeyWordLL);
        serarchKeyWord = (TextView) findViewById(R.id.serarchKeyWord);
        disVipCard = (ImageTextView) findViewById(R.id.dis_vip_card);
        noshopenter = (ConstraintLayout) findViewById(R.id.noshopenter);
        goodsTvTitle = (TextView) findViewById(R.id.goods_tv_title);
        goodsTvChooseCity = (TextView) findViewById(R.id.goods_tv_choose_city);
        ivBottomShader = (ImageView) findViewById(R.id.iv_bottom_shader);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        shopCartRel = (ConstraintLayout) findViewById(R.id.shop_cart_rel);
        goSub = (ImageView) findViewById(R.id.goSub);
        shopCartNum = (TextView) findViewById(R.id.shop_cart_num);
        mallScrollUp = (ImageView) findViewById(R.id.mall_scrollUp);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        shopCartRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "商城首页购物车的点击量");
                MobclickAgent.onEvent(mContext, "event2APPShopHomeShoppingCartClick", nokmap);
                MobclickAgent.onEvent(mContext, "event2APPShopHomeShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商城首页-购物车入口点击量"));
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_BASKET)
                        .navigation();
            }
        });

        final ViewTreeObserver viewTreeObserver = seachTopLL.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                seachTopLL.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int height = seachTopLL.getHeight();
                if (actZStatusAdapter != null) {
                    actZStatusAdapter.setTopHeight(height);
                }

                if (actZTabAdapter != null) {
                    actZTabAdapter.setTopheight(height);
                }
            }
        });
    }

    @Override
    public void onSucessGetLiveList(List<LiveVideoMain> result) {
        actZEmptyAdapter.setModel(null);
        if (result == null) {
            if (page == 1) {
                showAdpaterEmpty();
            }
//            emptyAdapter.setModel("暂无数据");
            return;
        }
        if (result.size() == 0) {
            //System.out.println("目前推荐的长度");
            refreshLayout.finishLoadMoreWithNoMoreData();
            if (page == 1 || page == 0) {
                if (page == 1) {
                    showAdpaterEmpty();
                }
//                emptyAdapter.setModel("暂无数据");
//                actGoodsItemAdapter.setModel(null);
            }
        } else {
            if (page == 1) {
                ArrayList<MultiItemEntity> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                for (int i = 0; i < result.size(); i++) {
                    LiveGoodsPresenter liveGoodsPresenter = new LiveGoodsPresenter(mContext, this);
                    if (result.get(i).activityIdList != null) {
                        liveGoodsPresenter.getLiveGoods(result.get(i), new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", result.get(i).activityIdList));
                    } else {
                        result.get(i).liveVideoGoodsList = new ArrayList<>();
                    }

                }
                clearAllUnderAdpaterEmpty();
                actZTabViewPagerAdapter4.setData(tmp);
            } else {
                ArrayList<MultiItemEntity> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                actZTabViewPagerAdapter4.addDatas(tmp);
            }
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }

    private void clearAllUnderAdpaterEmpty() {
        actZTabViewPagerAdapter4.clear();
        actZTabViewPagerAdapter1.clear();
        actZTabViewPagerAdapter3.clear();
        if (needTabTop) {
            virtualLayoutManager.scrollToPositionWithOffset(delegateAdapter.getItemCount() - 1, 0);
            needTabTop = false;
        }
    }

    @Override
    public void onSucessGetList(List<ActGoodsItem> result, int firstPageSize) {
        actZEmptyAdapter.setModel(null);
        if (result == null) {
            if (page == 1) {
                showAdpaterEmpty();
            }
            return;
        }
        if (result.size() == 0) {
            //System.out.println("目前推荐的长度");
            refreshLayout.finishLoadMoreWithNoMoreData();
            if (page == 1 || page == 0) {
                if (page == 1) {
                    showAdpaterEmpty();
                }
//                emptyAdapter.setModel("暂无数据");
//                actGoodsItemAdapter.setModel(null);
            }
        } else {
            if (page == 1) {
                this.firstPageSize = firstPageSize;
                ArrayList<MultiItemEntity> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                clearAllUnderAdpaterEmpty();
                actZTabViewPagerAdapter1.setData(tmp);
            } else {
                ArrayList<MultiItemEntity> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                actZTabViewPagerAdapter1.addDatas(tmp);
            }
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }

    }

    @Override
    public void onSucessGetCityGoodsList(List<SecondSortGoods> result, PageInfoEarly pageInfo) {
        actZEmptyAdapter.setModel(null);
        if (result == null) {
            if (page == 1) {
                showAdpaterEmpty();
            }
            return;
        }
        if (result.size() == 0) {
            //System.out.println("目前推荐的长度");
            refreshLayout.finishLoadMoreWithNoMoreData();
            if (page == 1 || page == 0) {
                if (page == 1) {
                    showAdpaterEmpty();
                }
//                emptyAdapter.setModel("暂无数据");
//                actGoodsItemAdapter.setModel(null);
            }
        } else {
            if (page == 1) {
                ArrayList<MultiItemEntity> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                clearAllUnderAdpaterEmpty();
                actZTabViewPagerAdapter3.setData(tmp);
            } else {
                ArrayList<MultiItemEntity> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                actZTabViewPagerAdapter3.addDatas(tmp);
            }
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }

    }

    private void showAdpaterEmpty() {
        actZEmptyAdapter.setModel("null");
        clearAllUnderAdpaterEmpty();
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("切换Tab".equals(function)) {
            page = 1;
            changeTab(true, Integer.parseInt(obj.toString()));
        }
    }

    Handler handlerSubmit = new Handler();
    Runnable runnableSubmit = new Runnable() {
        @Override
        public void run() {

            actZBlockAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onSucessGetBlockDetailList(View view, MainBlockModel item, int position) {
        actZBlockAdapter.setNeeduseNewmap(item);
        if (handlerSubmit != null) {
            handlerSubmit.removeCallbacks(runnableSubmit);
        }
        handlerSubmit.postDelayed(runnableSubmit, 250);
    }

    @Override
    public void onGetShopCartSuccess(ShopCartModel shopCartModel) {
        if (shopCartModel == null) {
            shopCartNum.setText("0");
            return;
        }
        if (shopCartModel.total == 0) {
            shopCartNum.setVisibility(View.INVISIBLE);
        } else if (shopCartModel.total > 99) {
            shopCartNum.setVisibility(View.VISIBLE);
            shopCartNum.setText("99+");
        } else {
            shopCartNum.setVisibility(View.VISIBLE);
            shopCartNum.setText(shopCartModel.total + "");
        }
    }

    private long mills = System.currentTimeMillis();

    @Override
    public void onDestroy() {
        super.onDestroy();
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "商城首页浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000);
        MobclickAgent.onEvent(mContext, "event_APP_ShopHome_Stop", nokmap);
    }

    @Override
    public void onSucessGetLiveGoods(List<LiveVideoGoods> liveVideoGoods) {
        actZPointVideoAdapter.notifyDataSetChanged();
        actZTabViewPagerAdapter4.notifyDataSetChanged();
    }

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {
        if (adModels.size() > 0) {
            final AdModel adModel = adModels.get(0);
            if (adModel.type == 1 && adModel.triggerPage == 16) {
                final String showTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                final String limitTime = adModel.validityEnd;//广告有效时间
                boolean isTimeEndBefore = false;
                try {
                    isTimeEndBefore = new Date().before(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(limitTime));
                } catch (Exception e) {
                    isTimeEndBefore = true;
                    e.printStackTrace();
                }
                if (isTimeEndBefore && !SpUtils.getValue(mContext, "Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + "&" + showTime, false)) {
                    System.out.println("需要展示广告:"+"Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage);
                    com.healthy.library.businessutil.GlideCopy.with(this)
                            .load(adModel.photoUrls)
                            
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                }

                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    if (isFirstLoad) {
                                        if (!isAdded()) return;
                                        if (!SpUtils.getValue(mContext, "Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + "&" + showTime, false)) {
                                            System.out.println("需要展示广告Z:"+"Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage);
                                            ADDialogView.newInstance().setAdModel(adModel).show(getChildFragmentManager(), "广告");
                                            SpUtils.store(mContext, "Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + "&" + showTime, true);
                                        }
                                    }
                                }
                            });
                }
            }
        }
    }

    @Override
    public void onSucessCheckPlus(boolean isplus) {
        SpUtils.store(mContext, SpKey.PLUSSTATUS, isplus);
    }
}