package com.health.discount.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.discount.R;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.NoFucDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.constant.Ids;
import com.healthy.library.constant.SpKey;
import com.healthy.library.loader.ImageNetAdapter;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.ColorInfo;
import com.healthy.library.model.MainIconModel;
import com.healthy.library.model.MainMenuModel;
import com.healthy.library.model.MainSearchModel;
import com.healthy.library.model.MemberAction;
import com.healthy.library.model.TabChangeModel;
import com.healthy.library.presenter.ActionPresenterCopy;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.ObservableScrollView;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActZStatusAdapter extends BaseAdapter<String> {

    private List<MainSearchModel> seachList;
    private List<AdModel> adv;
    boolean isStart = false;
    private int mTopHeight;
    private ConstraintLayout.LayoutParams layoutParams;
    private boolean isInit;

    public void setTopHeight(int topHeight) {
        this.mTopHeight = topHeight;
    }

    @Override
    public int getItemViewType(int position) {
        return 19;
    }

    public ActZStatusAdapter() {
        this(R.layout.dis_item_fragment_status);
    }

    private ActZStatusAdapter(int viewId) {
        super(viewId);
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        ImageView actBigBg;
        Space disSpace;
        LinearLayout disFunLL;
        ObservableScrollView recyclerFun;
        GridLayout functionGrid;
        ImageView recyclerScroller;
        TextView seachTipTitle;
        LinearLayout seachTipLL;
        Banner actBanner;
        actBanner = (Banner) baseHolder.itemView.findViewById(R.id.act_banner_no);

        actBigBg = (ImageView) baseHolder.itemView.findViewById(R.id.act_big_bg);
        disSpace = (Space) baseHolder.itemView.findViewById(R.id.disSpace);
        disFunLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.dis_funLL);
        recyclerFun = (ObservableScrollView) baseHolder.itemView.findViewById(R.id.recycler_fun);
        functionGrid = (GridLayout) baseHolder.itemView.findViewById(R.id.functionGrid);
        recyclerScroller = (ImageView) baseHolder.itemView.findViewById(R.id.recycler_scroller);
        seachTipTitle = (TextView) baseHolder.itemView.findViewById(R.id.seachTipTitle);
        seachTipLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.seachTipLL);
        LinearLayout mSearchLayout = baseHolder.itemView.findViewById(R.id.search_layout);

        if (mTopHeight != 0 && layoutParams == null) {
            layoutParams = (ConstraintLayout.LayoutParams) mSearchLayout.getLayoutParams();
            layoutParams.topMargin = mTopHeight;
            mSearchLayout.setLayoutParams(layoutParams);
        }
        onSucessGetSeachTipList(seachTipTitle, seachTipLL, seachList);
        if(!isInit){
            onGetFucList(actBigBg, disFunLL, disSpace, actBanner, functionGrid, recyclerScroller, recyclerFun, mainMenuModelList, mainIconModel);
            isInit=true;
        }


    }

    public void setSeachList(List<MainSearchModel> seachList) {
        this.seachList = seachList;
    }

    public List<MainSearchModel> getSeachList() {
        return seachList;
    }

    private void buildFuc(LinearLayout disFunLL, GridLayout functionGrid, final ImageView recyclerScroller, ObservableScrollView recyclerFun, List<MainMenuModel> list) {
        disFunLL.setVisibility(View.GONE);
        if (list != null && list.size() > 0) {
            disFunLL.setVisibility(View.VISIBLE);
        }
        if (list != null && list.size() > 0) {
            addFunctions(context, functionGrid, list);
        }
        recyclerScroller.setVisibility(View.VISIBLE);
        if (list != null && list.size() <= 10) {
            recyclerScroller.setVisibility(View.GONE);
        }
        recyclerFun.setOnObservableScrollListener(new ObservableScrollView.OnObservableScrollListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                int scrollX = scrollView.getScrollX();
                if (scrollX < (ScreenUtils.getScreenWidth(context) / 5)) {//第一屏幕
                    recyclerScroller.setImageResource(R.drawable.main_scroller_l);
                } else {
                    recyclerScroller.setImageResource(R.drawable.main_scroller_r);
                }
            }
        });
    }

    private void addFunctions(final Context context, final GridLayout gridLayout, final List<MainMenuModel> urls) {
        gridLayout.removeAllViews();
        {
            int columnCount = 5;
            int rowCount = 2;
            double splictreal = urls.size() / 2.0;
            ////System.out.println("分割大小" + urls.size());
            ////System.out.println("分割大小" + splictreal);
            String[] splictrealstring = (splictreal + "").split("\\.");
            if (splictrealstring.length > 1 && Integer.parseInt(splictrealstring[1]) > 0) {
                columnCount = ((int) (urls.size() / 2)) + 1;
                ////System.out.println("分割大小：多一行");
            } else {
                columnCount = ((int) (urls.size() / 2));
            }
            if (urls.size() <= 10) {
                columnCount = 5;
                rowCount = 1;
            }
            if (urls.size() <= 5) {
                columnCount = 5;
                rowCount = 1;
            }
            ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
            gridlayoutparm.width = (ScreenUtils.getScreenWidth(context) / 5) * columnCount;
            gridLayout.removeAllViews();
            gridLayout.setLayoutParams(gridlayoutparm);
            int mMargin;
            mMargin = (int) TransformUtil.dp2px(context, 2);
            ////System.out.println("分割大小：" + columnCount);
            gridLayout.removeAllViews();
            gridLayout.setColumnCount(columnCount);
            gridLayout.setRowCount(2);
            int w = ((ScreenUtils.getScreenWidth(context) - (int) TransformUtil.dp2px(context, 20)) / 5);
            int needsize = urls.size();
            for (int i = 0; i < needsize; i++) {
                final MainMenuModel indexMenu = urls.get(i);
                final int pos = i;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = w;
                View view = LayoutInflater.from(context).inflate(R.layout.dis_function_menu, gridLayout, false);

                final View parent_category = view.findViewById(R.id.parent_category);
                ImageView mIvCategory = (ImageView) view.findViewById(R.id.iv_category);
                TextView tv_category = view.findViewById(R.id.tv_category);
                tv_category.setText(indexMenu.navName);
                parent_category.setLayoutParams(new RecyclerView.LayoutParams(w, RecyclerView.LayoutParams.WRAP_CONTENT));
                if (indexMenu != null) {

                    if (indexMenu.iconUrl.contains("gif")) {
                        GlideCopy.with(gridLayout.getContext())
                                    .load(indexMenu.iconUrl)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(mIvCategory);
                            Log.e("act", "addFunctions: ");
                    } else {
                        GlideCopy.with(context)
                                .load(indexMenu.iconUrl)
                                .placeholder(R.drawable.img_1_1_default2)
                                .error(R.drawable.img_1_1_default)
                                .apply(RequestOptions.circleCropTransform())

                                .into(mIvCategory);
                    }
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            outFucClick(indexMenu.navName, indexMenu);

                        }
                    });
                }
                gridLayout.addView(view, params);
            }
        }
    }

    private void outFucClick(String function, MainMenuModel indexMenu) {
        if (indexMenu.type == 1) {
            if ("憨妈赚".equals(function)) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("type", "固定导航访问量");
                MobclickAgent.onEvent(context, "event2APPShopHomeNavigation1Click", nokmap);
                if (SpUtils.getValue(context, "isHanMomVip", false) == true) {
                    PackageManager packageManager = context.getPackageManager();
                    Intent intent = packageManager.getLaunchIntentForPackage("com.tencent.mm");
                    context.startActivity(intent);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String appId = Ids.WX_APP_ID; // 本应用微信AppId
                            IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
                            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                            req.userName = "gh_93d673cec6a8"; // 小程序原始id
                            req.path = "";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                            if (ChannelUtil.isIpRealRelease()) {
                                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                            } else {
                                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
                            }
                            api.sendReq(req);
                        }
                    }, 500);

                } else {
                    ARouter.getInstance().build(MineRoutes.MINE_HANMOM)
                            .navigation();
                }
                return;
            }
            if ("积分商城".equals(function)) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("type", "固定导航访问量");
                MobclickAgent.onEvent(context, "event2APPShopHomeNavigation1Click", nokmap);
                ARouter.getInstance().build(DiscountRoutes.DIS_POINTHOME).navigation();
                return;
            }
            if ("同城母婴".equals(function)) {
//                Map nokmap = new HashMap<String, String>();
//                nokmap.put("type", "固定导航访问量");
//                MobclickAgent.onEvent(context, "event2APPShopHomeNavigation1Click", nokmap);
                ARouter.getInstance().build(SecondRoutes.MAIN_MODULE).navigation();
                return;
            }
            if ("服务预约".equals(function)) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("type", "固定导航访问量");
                MobclickAgent.onEvent(context, "event2APPShopHomeNavigation1Click", nokmap);
                /*ARouter.getInstance()
                        .build(MallRoutes.MALL_ORDERSUBNEW)
                        .withString("type", "2")
                        .navigation();*/
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_APPOINTMENTMAIN)
                        .navigation();
                return;
            }
            if ("新客专享".equals(function)) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("type", "固定导航访问量");
                MobclickAgent.onEvent(context, "event2APPShopHomeNavigation1Click", nokmap);
                ARouter.getInstance().build(DiscountRoutes.DIS_NEWUSERACTIVITY).navigation();
                return;
            }
            if ("憨妈直播".equals(function)) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("type", "固定导航访问量");
                MobclickAgent.onEvent(context, "event2APPShopHomeNavigation1Click", nokmap);
                EventBus.getDefault().post(new TabChangeModel(2));
                //EventBus.getDefault().post(new ServiceTabChangeModel(0));
                //ARouter.getInstance().build(DiscountRoutes.DIS_LIVELIST).navigation();
                return;
            }
            if ("活动中心".equals(function)) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("type", "固定导航访问量");
                MobclickAgent.onEvent(context, "event2APPShopHomeNavigation1Click", nokmap);
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_FLASHBUY)
                        .navigation();
                return;
            }
            if ("营销活动".equals(function)) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("type", "固定导航访问量");
                MobclickAgent.onEvent(context, "event2APPShopHomeNavigation1Click", nokmap);
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_FLASHBUY)
                        .navigation();
                return;
            }
            if ("领券中心".equals(function)) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("type", "固定导航访问量");
                MobclickAgent.onEvent(context, "event2APPShopHomeNavigation1Click", nokmap);
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_CARDCENTER)
                        .navigation();
                new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                        new SimpleHashMapBuilder<>()
                                .putObject(new MemberAction(
                                        BuildConfig.VERSION_NAME,
                                        1,
                                        7,
                                        "CardCenterFromCouponMallMain",
                                        "",
                                        new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                                ));
                return;
            }
            if ("签到有礼".equals(function)) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_POINTSSIGNIN)
                        .withString("function", "9112-0")
                        .withString("blockName", function)
                        .navigation();
                return;
            }
            if ("PLUS专区".equals(function)) {

                Map nokmap = new HashMap<String, String>();
                nokmap.put("type", "固定导航访问量");
                MobclickAgent.onEvent(context, "event2APPShopHomeNavigation1Click", nokmap);
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_PLUSAREA)
                        .withString("function", "9112-0")
                        .withString("blockName", function)
                        .navigation();
                return;
            }
            if ("会员专享".equals(function)) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("type", "固定导航访问量");
                MobclickAgent.onEvent(context, "event2APPShopHomeNavigation1Click", nokmap);
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_PLUSAREA)
                        .withString("function", "9112-1")
                        .withString("blockName", function)
                        .navigation();
                return;
            }
            NoFucDialog.newInstance().show(((BaseActivity) context).getSupportFragmentManager(), "新功能提醒");
        } else {
            if (indexMenu.themeType == 2) {
                if (indexMenu.themeId.startsWith("http")) {
                    Uri uri = Uri.parse(indexMenu.themeId);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    if (!ChannelUtil.isRealRelease()) {
                        Toast.makeText(context, "配的H5不对啊", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("type", "商家自定义导航访问量");
                MobclickAgent.onEvent(context, "event2APPShopHomeNavigation2Click", nokmap);
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_SPECAREA)
                        .withString("blockId", indexMenu.themeId + "")
                        .withString("blockName", indexMenu.navName + "")
                        .navigation();
            }

        }

    }

    public void onGetFucList(final ImageView actBigBg, LinearLayout disFunLL, final Space disSpace, Banner actBanner, GridLayout functionGrid, final ImageView recyclerScroller, ObservableScrollView recyclerFun, final List<MainMenuModel> list, MainIconModel mainIconModel) {
        buildFuc(disFunLL, functionGrid, recyclerScroller, recyclerFun, list);
        System.out.println("设置头图");
        actBanner.setVisibility(View.GONE);
        final ConstraintLayout.LayoutParams disSpaceLayoutP = (ConstraintLayout.LayoutParams) disSpace.getLayoutParams();
//        disSpaceLayoutP.bottomMargin = (int) TransformUtil.dp2px(context, (list != null && list.size() > 10) ? 120 : 110);
        actBigBg.setBackgroundResource(R.drawable.ic_no);
        if (mainIconModel != null && !TextUtils.isEmpty(mainIconModel.iconUrl) && "1".equals(mainIconModel.navType)) {
            GlideCopy.with(context)
                    .load(mainIconModel.iconUrl)
                    .error(R.drawable.img_1_1_default)
                    .placeholder(R.drawable.img_1_1_default)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int swidth = ScreenUtils.getScreenWidth(context);
                            int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);

                            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) actBigBg.getLayoutParams();
                            layoutParams.height = height;
                            layoutParams.width = swidth;
                            actBigBg.setLayoutParams(layoutParams);
                            GlideCopy.with(context).load(resource).into(actBigBg);
                            disSpaceLayoutP.bottomMargin = (int) (height * (0.2));//按照图片高度的20%来计算了
                        }
                    });
        } else {
            ////System.out.println("设置通用导航");
            if (adv == null || adv.size() == 0) {
                actBigBg.setBackgroundResource(R.drawable.shape_public_coupon_top_bg);
                disSpaceLayoutP.bottomMargin = (int) TransformUtil.dp2px(context, 245);
                actBanner.setVisibility(View.GONE);
                int swidth = ScreenUtils.getScreenWidth(context);
                int height = (int) ((110 * 1.0 / 375) * swidth);
                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) actBigBg.getLayoutParams();
                layoutParams.height = height;
                layoutParams.width = swidth;
                actBigBg.setLayoutParams(layoutParams);
                actBigBg.setBackgroundResource(R.drawable.shape_public_coupon_top_bg);
                actBigBg.setImageResource(R.drawable.ic_no);
            } else {
                actBigBg.setBackgroundResource(R.drawable.shape_public_coupon_top_bg);
                disSpaceLayoutP.bottomMargin = (int) TransformUtil.dp2px(context, 245);
                actBanner.setVisibility(View.VISIBLE);
                int swidth = ScreenUtils.getScreenWidth(context);
                int height = (int) ((368 * 1.0 / 375) * swidth);
                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) actBigBg.getLayoutParams();
                layoutParams.height = height;
                layoutParams.width = swidth;
                actBigBg.setLayoutParams(layoutParams);
                actBigBg.setBackgroundResource(R.drawable.shape_public_coupon_top_bg);
                actBigBg.setImageResource(R.drawable.ic_no);
                buildBannerView(actBanner, adv);
            }


        }
    }

    public void onSucessGetSeachTipList(TextView seachTipTitle, LinearLayout seachTipLL, List<MainSearchModel> list) {
        seachTipTitle.setVisibility(View.GONE);
        seachTipLL.removeAllViews();
        if (list != null && list.size() > 0) {
            seachTipTitle.setVisibility(View.VISIBLE);
            seachTipLL.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.dis_seach_tip, seachTipLL, false);
                final TextView toFollow = view.findViewById(R.id.toFollow);
                toFollow.setText(list.get(i).keyword);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "商城搜索关键词每个关键词的点击量");
                        MobclickAgent.onEvent(context, "event2APPShopSeachKeywordClick", nokmap);
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_SERVICESORTGOODS)
                                .withString("categoryId", 0 + "").withString("goodsTitle", toFollow.getText().toString()).navigation();
                    }
                });
                seachTipLL.addView(view);
            }
        }

    }

    private void initView() {

    }

    List<MainMenuModel> mainMenuModelList;
    MainIconModel mainIconModel;

    public void setFunList(List<MainMenuModel> mainMenuModelList, MainIconModel mainIconModel) {
        this.mainMenuModelList = mainMenuModelList;
        this.mainIconModel = mainIconModel;
    }

    public void setAdv(List<AdModel> adv) {
        this.adv = adv;
    }

    private void buildBannerView(Banner banner, final List<AdModel> bannerimgs) {
        List<ColorInfo> colorList = new ArrayList<>();
        ImageNetAdapter imageLoader;
        int count;
        if (bannerimgs != null && bannerimgs.size() > 0) {
            colorList.clear();
            count = bannerimgs.size();
            for (int j = 0; j < bannerimgs.size(); j++) {
                ColorInfo info = new ColorInfo();
                info.setImgUrl(bannerimgs.get(j).iconUrl);
                colorList.add(info);

            }
            for (int j = 0; j < colorList.size(); j++) {
                try {
                    colorList.get(j).setPerfectColor(Color.parseColor(bannerimgs.get(j).colorValue));
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
            imageLoader = new ImageNetAdapter(new SimpleArrayListBuilder<String>().putList(bannerimgs, new ObjectIteraor<AdModel>() {
                @Override
                public Object getDesObj(AdModel adModel) {
                    return adModel.iconUrl;
                }
            }), TransformUtil.dp2px(context, 10f), colorList);
            banner.setAdapter(imageLoader)
                    .setIndicator(new CircleIndicator(context))
                    .setIndicatorGravity(IndicatorConfig.Direction.CENTER);//设置指示器位置（左，中，右）
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(Object data, int position) {
                    AdModel adModel = bannerimgs.get(position);
                    MARouterUtils.passToTarget(context,adModel);
                }

                @Override
                public void onBannerChanged(int position) {

                }
            });
            ////System.out.println("修改背景版333");
//            banner.setIndicator(new CircleIndicator(mContext));
            if (!isStart) {
                isStart = true;
                banner.stop();
                banner.start();
            }

        }
    }

    public List<AdModel> getAdv() {
        return adv;
    }

    public void setIsInit(boolean isInit) {
        this.isInit = isInit;
    }

    public boolean getIsInit() {
        return isInit;
    }
}
