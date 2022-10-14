package com.health.discount.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.lib_ShapeView.builder.ShapeDrawableBuilder;
import com.example.lib_ShapeView.layout.ShapeConstraintLayout;
import com.health.discount.R;
import com.health.discount.contract.ActHomeBlockContract;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.loader.ImageNetAdapter;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.ColorInfo;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.MainBlockDetailModel;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.GlideOptions;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.Disposable;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ActZBlockAdapter extends BaseAdapter<MainBlockModel> implements ActHomeBlockContract.View {

    SparseIntArray layouts = new SparseIntArray();
    boolean isStart = false;
    private List<AdModel> adv;
    boolean isShowBanner = false;
    int postionbanner = -1;

    public void setNeeduseNewmap(MainBlockModel  mainBlockModel) {
        viewmap.put(mainBlockModel.id,null);
    }

    boolean needuseoldmap=false;
    Map<String, SoftReference<View> > viewmap=new HashMap<>();

    public void setAdv(List<AdModel> adv) {
        this.adv = adv;
        isShowBanner = false;
    }

    public List<AdModel> getAdv() {
        return adv;
    }

    @Override
    public int getItemViewType(int position) {
        return 123;
    }

    public ActZBlockAdapter() {
        this(R.layout.dis_item_block_p);
        addItemType(1, R.layout.dis_item_block_child1);//单行通栏列表
        addItemType(2, R.layout.dis_item_block_child2);//错位组合栏
        addItemType(3, R.layout.dis_item_block_child3);//通栏大图+介绍
        addItemType(4, R.layout.dis_item_block_child5);//多栏图片+商品组合展示
        addItemType(5, R.layout.dis_item_block_child4);//通栏图片+商品组合展示
        addItemType(6, R.layout.dis_item_block_child6);//联盟单行通栏列表
        addItemType(7, R.layout.dis_item_block_child7);//通栏介绍+联盟组合列表
    }
//    addItemType(1, R.layout.dis_item_block_p1);//单行通栏列表
//    addItemType(2, R.layout.dis_item_block_p2);//错位组合栏
//    addItemType(3, R.layout.dis_item_block_p3);//通栏大图+介绍
//    addItemType(4, R.layout.dis_item_block_p4);//多栏图片+商品组合展示
//    addItemType(5, R.layout.dis_item_block_p5);//通栏图片+商品组合展示
    private void addItemType(int i, int res) {
        layouts.put(i, res);
    }

    private ActZBlockAdapter(int viewId) {
        super(viewId);
//        viewmap.clear();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        FrameLayout childP;
        childP = (FrameLayout) baseHolder.itemView.findViewById(R.id.childP);
        Banner actBottomBanner;
        actBottomBanner = (Banner) baseHolder.itemView.findViewById(R.id.act_bottom_banner);
        childP.removeAllViews();
        childP.addView(getView(i, getDatas().get(i), childP, actBottomBanner));
    }

    private int getViewRes(int position) {
        MainBlockModel item = getDatas().get(position);
        return layouts.get(item.exhibition);
    }

    public View getView(final int position, final MainBlockModel item, FrameLayout childP, final Banner banner) {
        if(viewmap.get(item.id)!=null){
            if(viewmap.get(item.id).get()!=null){
                View result=viewmap.get(item.id).get();
                try {
                    ((ViewGroup)(result.getParent())).removeAllViews();
                } catch (Exception e) {
//                    e.printStackTrace();
                }
//                if(viewmap.size()>1){//可能存在老的问题
//                    viewmap.clear();
//                    viewmap.put(item.id,new SoftReference<View>(result));
//                }
                return result;
            }
        }
        View view = LayoutInflater.from(context).inflate(getViewRes(position), childP, false);
        viewmap.put(item.id,new SoftReference<View>(view));
        try {
            view.findViewById(R.id.topLL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.exhibition < 4) {
                        MobclickAgent.onEvent(context, "event2APPShopHomeSpecialMoreClick",
                                new SimpleHashMapBuilder<String, String>().puts("soure", "专区更多按钮的点击量"));
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_SPECAREA)
                                .withString("blockId", item.id + "")
                                .withString("blockName", item.themeName + "")
                                .navigation();
                    }

                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
        }
        banner.setVisibility(View.GONE);


        int nextindex = -1;
        try {
            nextindex = getDatas().get(position + 1).exhibition;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        if (getDatas().get(position).exhibition != nextindex && !isShowBanner || (position == postionbanner)) {//第一种专区下面
            if (adv != null && adv.size() > 0) {

                com.healthy.library.businessutil.GlideCopy.with()
                        .load(adv.get(0).photoUrls)

                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                            }
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                banner.setVisibility(View.VISIBLE);
                                buildBannerView(banner, adv);

                            }
                        });
                isShowBanner = true;
                postionbanner = position;

            }
        }

        onSucessGetBlockDetailList(view, item, position);
        return view;
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
                info.setImgUrl(bannerimgs.get(j).photoUrls);
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
                    return adModel.photoUrls;
                }
            }), TransformUtil.dp2px(context, 10f), colorList);
            banner.setAdapter(imageLoader)
                    .setIndicator(new RectangleIndicator(context))
                    .setIndicatorGravity(IndicatorConfig.Direction.RIGHT);//设置指示器位置（左，中，右）
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
            //System.out.println("修改背景版333");
//            banner.setIndicator(new CircleIndicator(mContext));
            if (!isStart) {
                isStart = true;
                banner.stop();
                banner.start();
            }

        }
    }

    private int getItemType(int position) {
        try {
            MainBlockModel item = getDatas().get(position);
            return item.exhibition;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onSucessGetBlockDetailList(View view, MainBlockModel item, int position) {

        buildData(view, item, position);
    }

    private void buildData(View view, MainBlockModel item, int position) {
        if (getItemType(position) == 1) {
            buildType1(view, item, position);
        }
        if (getItemType(position) == 2) {
            buildType2(view, item, position);

        }
        if (getItemType(position) == 3) {
            buildType3(view, item, position);

        }
        if (getItemType(position) == 4) {
            buildType5(view, item, position);

        }
        if (getItemType(position) == 5) {
            buildType4(view, item, position);
        }
        if (getItemType(position) == 6) {
            buildType6(view, item, position);
        }
        if (getItemType(position) == 7) {
            buildType7(view, item, position);
        }
    }
    private void buildType7(View view, final MainBlockModel item, int position) {
        ConstraintLayout cityTopTitle;
        ImageTextView topCityTitle;
        ImageTextView topSecondTitle;
        ImageTextView topMore;
        LinearLayout postList;
        cityTopTitle = (ConstraintLayout) view.findViewById(R.id.cityTopTitle);
        topCityTitle = (ImageTextView) view.findViewById(R.id.topCityTitle);
        topSecondTitle = (ImageTextView) view.findViewById(R.id.topSecondTitle);
        topMore = (ImageTextView) view.findViewById(R.id.topMore);
        postList = (LinearLayout) view.findViewById(R.id.postList);
        LinearLayout shopSecondMore = view.findViewById(R.id.shopSecondMore);
        shopSecondMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SecondRoutes.MAIN_SHOPBLOCK)
                        .withString("blockId", item.id)
                        .withString("blockName", item.themeName)
                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                        .navigation();
            }
        });
        topCityTitle.setText(item.themeName);
        topSecondTitle.setText(item.themeSubname);
        buildPostList7(postList, shopSecondMore, item);
    }

    private void buildPostListGoods7(LinearLayout mBottomGrid, final String shopId, List<MainBlockDetailModel> detailList) {
        mBottomGrid.removeAllViews();
        if (!ListUtil.isEmpty(detailList)) {
            for (int j = 0; j < detailList.size(); j++) {
                final GoodsDetail detail = detailList.get(j).goodsDTO;
                View view = LayoutInflater.from(context).inflate(R.layout.dis_item_block_child2_item_sq, mBottomGrid, false);
                ImageView mGoodsImg = view.findViewById(R.id.goodsImg);
                TextView mGoodsName = view.findViewById(R.id.goodsName);
                TextView mGoodsMoney = view.findViewById(R.id.goodsMoney);
                TextView mGoodsOldPrice = view.findViewById(R.id.pinOldPrice);
                mGoodsOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                GlideCopy.with(mGoodsImg.getContext())
                        .load(detail.getFilePath())
                        .error(R.drawable.img_1_1_default)
                        .placeholder(R.drawable.img_1_1_default)
                        .into(mGoodsImg);

                mGoodsName.setText(detail.goodsTitle);
                mGoodsMoney.setText(FormatUtils.moneyKeep2Decimals(detail.platformPrice));
                mGoodsOldPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(detail.retailPrice));

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                .withString("marketingType", detail.marketingType)
                                .withString("id", detail.id)
                                .navigation();
                    }
                });

                mBottomGrid.addView(view);
            }
        }
    }

    private void buildPostList7(LinearLayout postList, LinearLayout shopSecondMore, final MainBlockModel item) {
        postList.removeAllViews();
        List<MainBlockModel.AllianceMerchant> allianceMerchantList = item.getRealAllianceMerchantList();
        if (!ListUtil.isEmpty(allianceMerchantList)) {
            shopSecondMore.setVisibility(allianceMerchantList.size() > 3 ? View.VISIBLE : View.GONE);//TODO 暂时性先注释
            for (int i = 0; i < (allianceMerchantList.size() > 3?3:allianceMerchantList.size()); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.dis_item_block_child7_item, postList, false);
                ShapeConstraintLayout mShopCon = view.findViewById(R.id.shopCon);
                ShapeDrawableBuilder shapeDrawableBuilder = mShopCon.getShapeDrawableBuilder();
                if (i == 0) {
                    shapeDrawableBuilder.setSolidColor(Color.parseColor("#FF8453"));
                }
                if (i == 1) {
                    shapeDrawableBuilder.setSolidColor(Color.parseColor("#FFAA53"));
                }
                if (i == 2) {
                    shapeDrawableBuilder.setSolidColor(Color.parseColor("#FF5353"));
                }
                shapeDrawableBuilder.intoBackground();

                ImageView mShopIcon = view.findViewById(R.id.shopIcon);
                TextView mTvShopName = view.findViewById(R.id.shopName);
                TextView mLoc = view.findViewById(R.id.loc);
                TextView mLocDistance = view.findViewById(R.id.locDistance);
                LinearLayout mBottomGrid = view.findViewById(R.id.bottomGrid);
                final MainBlockModel.ShopDto shopDto = allianceMerchantList.get(i).shopDto;
                try {
                    GlideCopy.with(mShopCon.getContext())
                            .load(shopDto.envPicUrl.split(",")[0])
                            .error(R.drawable.img_1_1_default)
                            .placeholder(R.drawable.img_1_1_default)
                            .into(mShopIcon);
                    String mShopName = "";
                    if (!TextUtils.isEmpty(shopDto.chainShopName)) {
                        mShopName = shopDto.shopName + "(" + shopDto.chainShopName + ")";
                    } else {
                        mShopName = shopDto.shopName;
                    }
                    mTvShopName.setText(mShopName);
                    mLoc.setText(shopDto.provinceName + shopDto.cityName + shopDto.addressAreaName+shopDto.addressDetails);
                    mLocDistance.setText(ParseUtils.parseDistance(String.valueOf(shopDto.distance)));

                    mShopCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ARouter.getInstance()
                                    .build(SecondRoutes.SECOND_SHOP_DETAIL)
                                    .withString("shopId", String.valueOf(shopDto.id))
                                    .navigation();
                        }
                    });
                    buildPostListGoods7(mBottomGrid, String.valueOf(shopDto.id), allianceMerchantList.get(i).detailList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postList.addView(view);
            }
        }
    }

    private void buildType6(View view, final MainBlockModel item, int position) {
        LinearLayout cityPLL;
        ConstraintLayout cityTopLL;
        ImageTextView cityTitle;
        TextView cityTitleLfet;
        ImageTextView cityMore;
        LinearLayout cityLL;
        cityPLL = (LinearLayout) view.findViewById(R.id.cityPLL);
        cityTopLL = (ConstraintLayout) view.findViewById(R.id.cityTopLL);
        cityTitle = (ImageTextView) view.findViewById(R.id.cityTitle);
        cityTitleLfet = (TextView) view.findViewById(R.id.cityTitleLfet);
        cityMore = (ImageTextView) view.findViewById(R.id.cityMore);
        cityLL = (LinearLayout) view.findViewById(R.id.cityLL);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(context, "event2APPShopHomeSpecialMoreClick",
                        new SimpleHashMapBuilder<String, String>().puts("soure", "专区更多按钮的点击量"));
                ARouter.getInstance()
                        .build(SecondRoutes.MAIN_BLOCK)
                        .withString("blockId", item.id)
                        .withString("blockName", item.themeName)
                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                        .navigation();
            }
        });

        cityTitle.setText(item.themeName);
        cityTitleLfet.setText("|\r\r" + item.themeSubname);
        buildPostList6(cityLL, item);

    }
    private void buildPostList6(LinearLayout postList, final MainBlockModel item) {
        postList.removeAllViews();
        if (!ListUtil.isEmpty(item.detailList)) {
            final List<MainBlockDetailModel> detailList = item.detailList;
            for (int i = 0; i < detailList.size(); i++) {
                final GoodsDetail detail = detailList.get(i).goodsDTO;
                View view = LayoutInflater.from(context).inflate(R.layout.dis_item_city_h, postList, false);
                ImageView mGoodsImg = view.findViewById(R.id.cityGoodsImg);
                TextView mGoodsLabel = view.findViewById(R.id.cityGoodsLabel);
                TextView mGoodsTitle = view.findViewById(R.id.cityGoodsTitle);
                TextView mPrice = view.findViewById(R.id.cityPrice);
                TextView mGoodsOldPrice = view.findViewById(R.id.cityOldPrice);
                mGoodsOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                GlideCopy.with(mGoodsImg.getContext())
                        .load(detail.headImage)
                        .error(R.drawable.img_1_1_default)
                        .placeholder(R.drawable.img_1_1_default)
                        .into(mGoodsImg);
                mGoodsTitle.setText(detail.goodsTitle);
                mGoodsLabel.setText(detail.getMerchantName());
                mPrice.setText(FormatUtils.moneyKeep2Decimals(detail.platformPrice));
                mGoodsOldPrice.setVisibility(View.VISIBLE);
                if (detail.retailPrice > 0) {
                    mGoodsOldPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(detail.retailPrice));
                } else {
                    mGoodsOldPrice.setVisibility(View.GONE);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                .withString("marketingType", detail.marketingType)
                                .withString("id", detail.id)
                                .navigation();
                    }
                });
                postList.addView(view);
            }
        }
    }


    private void buildPostList(LinearLayout postList, final MainBlockModel item) {
        postList.removeAllViews();
        for (int i = 0; i < item.childList.size(); i++) {
            final MainBlockModel itemchild = item.childList.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.dis_item_block_child4_item, postList, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(context, "event2APPShopHomeSpecialMoreClick",
                            new SimpleHashMapBuilder<String, String>().puts("soure", "专区更多按钮的点击量"));
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_SPECAREA)
                            .withString("blockId", item.id + "")
                            .withString("blockName", item.themeName + "")
                            .navigation();
                }
            });
            TextView topImgTag = view.findViewById(R.id.topImgTag);
            TextView topImgTitle = view.findViewById(R.id.topImgTitle);
            TextView topImgSecondTitle = view.findViewById(R.id.topImgSecondTitle);
            ImageView topImg;
            Space space;
            LinearLayout goodsList;
            topImg = (ImageView) view.findViewById(R.id.topImg);
            space = (Space) view.findViewById(R.id.space);
            goodsList = (LinearLayout) view.findViewById(R.id.goodsList);

            topImgTag.setText(itemchild.themeTag);
            if (TextUtils.isEmpty(itemchild.themeTag)) {
                topImgTag.setVisibility(View.GONE);
            }
            topImgTitle.setText(itemchild.describe);
            topImgSecondTitle.setText(itemchild.adContent);
            com.healthy.library.businessutil.GlideCopy.with(context).load(itemchild.iconUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    .apply(GlideOptions.withRoundedOptions((int) TransformUtil.dp2px(context, 12f), RoundedCornersTransformation.CornerType.TOP, R.drawable.img_690_260_default, R.drawable.img_690_260_default))

                    .into(topImg);
            buildchild1items(goodsList, itemchild);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(context, "event2APPShopHomeSpecialMoreClick",
                            new SimpleHashMapBuilder<String, String>().puts("soure", "专区更多按钮的点击量"));
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_SPECAREA)
                            .withString("blockId", itemchild.id + "")
                            .withString("blockName", itemchild.themeName + "")
                            .navigation();
                }
            });
            postList.addView(view);
        }
    }

    private void buildType4(View view, MainBlockModel item, int position) {
        ImageTextView topTitle;
        ImageTextView topSecondTitle;
        ImageTextView topMore;
        LinearLayout postList;
        topTitle = (ImageTextView) view.findViewById(R.id.topTitle);
        topSecondTitle = (ImageTextView) view.findViewById(R.id.topSecondTitle);
        topMore = (ImageTextView) view.findViewById(R.id.topMore);
        postList = (LinearLayout) view.findViewById(R.id.postList);
        topTitle.setText(item.themeName);
        topSecondTitle.setText(item.themeSubname);
        if (item.childList == null) {
            return;
        }
        buildPostList(postList, item);
    }

    private void buildchild5items(LinearLayout goodsList, final MainBlockModel item) {
        goodsList.removeAllViews();
        if(item.detailList==null){
            return;
        }
        for (int i = 0; i < item.detailList.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.dis_item_block_child5_item_item, goodsList, false);
            ImageView goodsImg;
            TextView goodsMoney;
            TextView goodsOldMoney;
            goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
            goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
            goodsOldMoney = (TextView) view.findViewById(R.id.goodsOldMoney);
            goodsList.addView(view);
            GoodsDetail mainBlockDetailModel = null;
            try {
                mainBlockDetailModel = item.detailList.get(i).goodsDTO;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mainBlockDetailModel == null) {
                view.setVisibility(View.INVISIBLE);
            } else {
                TextView tagText = view.findViewById(R.id.tagText);
                tagText.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(mainBlockDetailModel.getTagFirst())) {
                    tagText.setVisibility(View.VISIBLE);
                    if (mainBlockDetailModel.getTagFirst().length() > 3) {
                        String org = mainBlockDetailModel.getTagFirst();
                        String resultOrg = org.substring(0, 2) + "\n" + org.substring(2, org.length());
                        tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
                        tagText.setText(resultOrg);
                    } else {
                        tagText.setText(mainBlockDetailModel.getTagFirst());
                        tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
                    }
                }
                com.healthy.library.businessutil.GlideCopy.with(context).load(mainBlockDetailModel.getGoodsIcon())
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(goodsImg);
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(mainBlockDetailModel.platformPrice));
                goodsOldMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(mainBlockDetailModel.retailPrice));
                goodsOldMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                final GoodsDetail finalMainBlockDetailModel = mainBlockDetailModel;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", finalMainBlockDetailModel.id)
                                .navigation();
                    }
                });
            }

        }
    }

    private void buildGoodsHlist(LinearLayout goodsHList, final MainBlockModel item) {
        goodsHList.removeAllViews();
        for (int i = 0; i < item.childList.size(); i++) {

            View view = LayoutInflater.from(context).inflate(R.layout.dis_item_block_child5_item, goodsHList, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(context, "event2APPShopHomeSpecialImgClick",
                            new SimpleHashMapBuilder<String, String>().puts("soure", "专区图片的点击量"));
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_SPECAREA)
                            .withString("blockId", item.id + "")
                            .withString("blockName", item.themeName + "")
                            .navigation();


                }
            });
            TextView topImgTag = view.findViewById(R.id.topImgTag);
            ImageView topImgTagIcon = view.findViewById(R.id.topImgTagIcon);
            TextView topImgTitle = view.findViewById(R.id.topImgTitle);
            TextView topImgSecondTitle = view.findViewById(R.id.topImgSecondTitle);

            ImageView topImg;
            Space space;
            LinearLayout goodsList;
            topImg = (ImageView) view.findViewById(R.id.topImg);
            space = (Space) view.findViewById(R.id.space);
            goodsList = (LinearLayout) view.findViewById(R.id.goodsList);
            goodsHList.addView(view);
            MainBlockModel itemchild = null;
            try {
                itemchild = item.childList.get(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (itemchild == null) {
                view.setVisibility(View.INVISIBLE);
                return;
            }
            final MainBlockModel finalItemchild = itemchild;
            topImgTag.setVisibility(View.VISIBLE);
            topImgTagIcon.setVisibility(View.GONE);
            topImgTag.setText(finalItemchild.themeTag);
            if (finalItemchild.themeTag.contains("http")) {
                topImgTag.setVisibility(View.GONE);
                topImgTagIcon.setVisibility(View.VISIBLE);
                com.healthy.library.businessutil.GlideCopy.with(context).load(itemchild.themeTag)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(topImgTagIcon);
            }
            if (TextUtils.isEmpty(finalItemchild.themeTag)) {
                topImgTag.setVisibility(View.GONE);
                topImgTagIcon.setVisibility(View.GONE);
            }
            topImgTitle.setText(finalItemchild.describe);
            topImgSecondTitle.setText(finalItemchild.adContent);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(context, "event2APPShopHomeSpecialImgClick",
                            new SimpleHashMapBuilder<String, String>().puts("soure", "专区图片的点击量"));
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_SPECAREA)
                            .withString("blockId", finalItemchild.id + "")
                            .withString("blockName", finalItemchild.themeName + "")
                            .navigation();
                }
            });

            com.healthy.library.businessutil.GlideCopy.with(context).load(itemchild.iconUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .apply(GlideOptions.withRoundedOptions((int) TransformUtil.dp2px(context, 12f), RoundedCornersTransformation.CornerType.TOP, R.drawable.img_690_260_default, R.drawable.img_690_260_default))
                    .error(R.drawable.img_1_1_default)

                    .into(topImg);
            buildchild5items(goodsList, itemchild);
        }
    }

    private void buildType5(View view, MainBlockModel item, int position) {
        ImageTextView topTitle;
        ImageTextView topSecondTitle;
        ImageTextView topMore;
        LinearLayout goodsHList;
        topTitle = (ImageTextView) view.findViewById(R.id.topTitle);
        topSecondTitle = (ImageTextView) view.findViewById(R.id.topSecondTitle);
        topMore = (ImageTextView) view.findViewById(R.id.topMore);
        goodsHList = (LinearLayout) view.findViewById(R.id.goodsHList);
        topTitle.setText(item.themeName);
        topSecondTitle.setText(item.themeSubname);

        if (item.childList == null) {
            return;
        }
        buildGoodsHlist(goodsHList, item);
    }

    private void buildHPosts(LinearLayout postLL, final MainBlockModel item) {
        postLL.removeAllViews();
        for (int i = 0; i < item.childList.size(); i++) {
            final MainBlockModel itemchild = item.childList.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.dis_item_block_child3_item, postLL, false);
            ConstraintLayout parentCategory;
            ImageView postImage;
            TextView postTitle;
            TextView postSecondTitle;
            ImageView postGo;
            LinearLayout postHeadIconLL;
            TextView postMenber;
            parentCategory = (ConstraintLayout) view.findViewById(R.id.parent_category);
            postImage = (ImageView) view.findViewById(R.id.postImage);
            postTitle = (TextView) view.findViewById(R.id.postTitle);
            postSecondTitle = (TextView) view.findViewById(R.id.postSecondTitle);
            postGo = (ImageView) view.findViewById(R.id.postGo);
            postHeadIconLL = (LinearLayout) view.findViewById(R.id.postHeadIconLL);
            postMenber = (TextView) view.findViewById(R.id.postMenber);
            com.healthy.library.businessutil.GlideCopy.with(context).load(itemchild.iconUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    .apply(GlideOptions.withRoundedOptions((int) TransformUtil.dp2px(context, 12f), RoundedCornersTransformation.CornerType.TOP, R.drawable.img_690_260_default, R.drawable.img_690_260_default))

                    .into(postImage);
            postTitle.setText(itemchild.themeName);
            postSecondTitle.setText(itemchild.themeSubname);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(context, "event2APPShopHomeSpecialMoreClick",
                            new SimpleHashMapBuilder<String, String>().puts("soure", "专区更多按钮的点击量"));
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_SPECAREA)
                            .withString("blockId", itemchild.id + "")
                            .withString("blockName", itemchild.themeName + "")
                            .navigation();
                }
            });
            postLL.addView(view);
        }
    }

    private void buildType3(View view, MainBlockModel item, int position) {
        ImageTextView topTitle;
        ImageTextView topSecondTitle;
        ImageTextView topMore;
        LinearLayout postLL;
        topTitle = (ImageTextView) view.findViewById(R.id.topTitle);
        topSecondTitle = (ImageTextView) view.findViewById(R.id.topSecondTitle);
        topMore = (ImageTextView) view.findViewById(R.id.topMore);
        postLL = (LinearLayout) view.findViewById(R.id.postLL);
        topTitle.setText(item.themeName);
        topSecondTitle.setText(item.themeSubname);

        if (item.childList == null) {
            return;
        }
        buildHPosts(postLL, item);
    }

    private void buildLeftGrid(LinearLayout leftGrid, final MainBlockModel item) {
        leftGrid.removeAllViews();
        View view = LayoutInflater.from(context).inflate(R.layout.dis_item_block_child2_item_big, leftGrid, false);
        ImageView goodsImg;
        LinearLayout goodsCouponLL;
        TextView goodsName;
        TextView goodsMoney;
        TextView pinOldPrice;
        AutoClickImageView passbasket;
        goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
        goodsCouponLL = (LinearLayout) view.findViewById(R.id.goodsCouponLL);
        goodsName = (TextView) view.findViewById(R.id.goodsName);
        goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
        pinOldPrice = (TextView) view.findViewById(R.id.pinOldPrice);
        passbasket = (AutoClickImageView) view.findViewById(R.id.passbasket);
        passbasket.setVisibility(View.GONE);
        leftGrid.addView(view);
        if (item.detailList == null || item.detailList.size() == 0 || item.detailList.get(0).goodsDTO == null) {
            return;
        }
        TextView tagText = view.findViewById(R.id.tagText);
        tagText.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(item.detailList.get(0).goodsDTO.getTagFirst())) {
            tagText.setVisibility(View.VISIBLE);
            if (item.detailList.get(0).goodsDTO.getTagFirst().length() > 3) {
                String org = item.detailList.get(0).goodsDTO.getTagFirst();
                String resultOrg = org.substring(0, 2) + "\n" + org.substring(2, org.length());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
                tagText.setText(resultOrg);
            } else {
                tagText.setText(item.detailList.get(0).goodsDTO.getTagFirst());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
            }
        }
        com.healthy.library.businessutil.GlideCopy.with(context).load(item.detailList.get(0).goodsDTO.getGoodsIcon())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(goodsImg);
        goodsName.setText(item.detailList.get(0).goodsDTO.goodsTitle);
        goodsMoney.setText(FormatUtils.moneyKeep2Decimals(item.detailList.get(0).goodsDTO.platformPrice));
        pinOldPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(item.detailList.get(0).goodsDTO.retailPrice));
        pinOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(context, "event2APPShopHomeSpecialGoodsClick",
                        new SimpleHashMapBuilder<String, String>().puts("soure", "专区商品图片标题的点击量"));
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", item.detailList.get(0).goodsId)
                        .navigation();
            }
        });
    }

    private int mMargin;
    private int mCornerRadius;

    private void buildRightGrid(GridLayout gridLayout, MainBlockModel item) {
        gridLayout.removeAllViews();
        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(context, 2);
            mCornerRadius = (int) TransformUtil.dp2px(context, 5);
        }
        for (int i = 1; i < 5; i++) {
            int row = 2;
            int w = (int) (ScreenUtils.getScreenWidth(context) / 2 - TransformUtil.dp2px(context, 6)) / row;
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = w;
            View view = LayoutInflater.from(context).inflate(R.layout.dis_item_block_child2_item, gridLayout, false);
            ImageView goodsImg;
            LinearLayout goodsCouponLL;
            TextView goodsName;
            TextView pinOldPrice;
            TextView goodsMoney;
            AutoClickImageView passbasket;
            goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
            goodsCouponLL = (LinearLayout) view.findViewById(R.id.goodsCouponLL);
            goodsName = (TextView) view.findViewById(R.id.goodsName);
            goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
            pinOldPrice = (TextView) view.findViewById(R.id.pinOldPrice);
            passbasket = (AutoClickImageView) view.findViewById(R.id.passbasket);
            gridLayout.addView(view, params);
            MainBlockDetailModel mainBlockDetailModel = null;
            try {
                mainBlockDetailModel = item.detailList.get(i);
            } catch (Exception e) {
//                e.printStackTrace();
            }
            if (item.detailList == null || item.detailList.size() == 0 || mainBlockDetailModel == null || item.detailList.get(i).goodsDTO == null) {
                view.setVisibility(View.INVISIBLE);
            } else {

                final MainBlockDetailModel finalMainBlockDetailModel = mainBlockDetailModel;
                TextView tagText = view.findViewById(R.id.tagText);
                tagText.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(mainBlockDetailModel.goodsDTO.getTagFirst())) {
                    tagText.setVisibility(View.VISIBLE);
                    if (mainBlockDetailModel.goodsDTO.getTagFirst().length() > 3) {
                        String org = mainBlockDetailModel.goodsDTO.getTagFirst();
                        String resultOrg = org.substring(0, 2) + "\n" + org.substring(2, org.length());
                        tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
                        tagText.setText(resultOrg);
                    } else {
                        tagText.setText(mainBlockDetailModel.goodsDTO.getTagFirst());
                        tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
                    }
                }
                com.healthy.library.businessutil.GlideCopy.with(context).load(item.detailList.get(i).goodsDTO.getGoodsIcon())
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(goodsImg);
                goodsName.setText(item.detailList.get(i).goodsDTO.goodsTitle);
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(item.detailList.get(i).goodsDTO.platformPrice));
                pinOldPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(item.detailList.get(i).goodsDTO.retailPrice));
                pinOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MobclickAgent.onEvent(context, "event2APPShopHomeSpecialGoodsClick",
                                new SimpleHashMapBuilder<String, String>().puts("soure", "专区商品图片标题的点击量"));
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", finalMainBlockDetailModel.goodsId)
                                .navigation();
                    }
                });
            }

        }
    }

    private void buildType2(View view, MainBlockModel item, int position) {
        ConstraintLayout topLL;
        ImageTextView topTitle;
        ImageTextView topSecondTitle;
        ImageTextView topMore;
        ConstraintLayout couponPLL;
        ImageView couponMore;
        HorizontalScrollView goodsCouponPLL;
        LinearLayout goodsCouponLL;
        LinearLayout leftGrid;
        RelativeLayout headNowLL;
        TextView manCount;
        GridLayout rightGrid;
        topLL = (ConstraintLayout) view.findViewById(R.id.topLL);
        topTitle = (ImageTextView) view.findViewById(R.id.topTitle);
        topSecondTitle = (ImageTextView) view.findViewById(R.id.topSecondTitle);
        topMore = (ImageTextView) view.findViewById(R.id.topMore);
        couponPLL = (ConstraintLayout) view.findViewById(R.id.couponPLL);
        couponMore = (ImageView) view.findViewById(R.id.couponMore);
        goodsCouponPLL = (HorizontalScrollView) view.findViewById(R.id.goodsCouponPLL);
        goodsCouponLL = (LinearLayout) view.findViewById(R.id.goodsCouponLL);
        leftGrid = (LinearLayout) view.findViewById(R.id.leftGrid);
        headNowLL = (RelativeLayout) view.findViewById(R.id.headNowLL);
        manCount = (TextView) view.findViewById(R.id.manCount);
        rightGrid = (GridLayout) view.findViewById(R.id.rightGrid);
        topTitle.setText(item.themeName);
        topSecondTitle.setText(item.themeSubname);
        buildLeftGrid(leftGrid, item);
        buildRightGrid(rightGrid, item);
        buildHaedS(headNowLL, item);

    }

    private void buildHaedS(RelativeLayout headNowLL, MainBlockModel item) {
        headNowLL.removeAllViews();
        for (int i = 0; i < 4; i++) {
            CornerImageView cornerImageView = new CornerImageView(context);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) TransformUtil.dp2px(context, 18), (int) TransformUtil.dp2px(context, 18));
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cornerImageView.setElevation(1f);
            }
            if (i == 0) {

            } else {
                layoutParams.leftMargin = -4;
                layoutParams.addRule(RelativeLayout.RIGHT_OF, i - 1);
            }
            headNowLL.addView(cornerImageView, layoutParams);
        }
    }

    private void buildType1(View view, MainBlockModel item, int position) {
        ConstraintLayout topLL;
        ImageTextView topTitle;
        ImageTextView topSecondTitle;
        ImageTextView topMore;
        LinearLayout goodsList;
        topLL = (ConstraintLayout) view.findViewById(R.id.topLL);
        topTitle = (ImageTextView) view.findViewById(R.id.topTitle);
        topSecondTitle = (ImageTextView) view.findViewById(R.id.topSecondTitle);
        topMore = (ImageTextView) view.findViewById(R.id.topMore);
        goodsList = (LinearLayout) view.findViewById(R.id.goodsList);
        topTitle.setText(item.themeName);
        topSecondTitle.setText(item.themeSubname);
        buildchild1items(goodsList, item);
    }

    private void buildchild1items(LinearLayout goodsList, MainBlockModel item) {
        goodsList.removeAllViews();
        if(item.detailList==null){
            return;
        }
        for (int i = 0; i < item.detailList.size(); i++) {
            final MainBlockDetailModel mainBlockDetailModel = item.detailList.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.dis_item_block_child1_item, goodsList, false);
            ImageView goodsImg;
            LinearLayout goodsCouponLL;
            TextView goodsName;
            TextView goodsMoney;
            TextView pinOldPrice;
            AutoClickImageView passbasket;
            goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
            goodsCouponLL = (LinearLayout) view.findViewById(R.id.goodsCouponLL);
            goodsName = (TextView) view.findViewById(R.id.goodsName);
            goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
            pinOldPrice = (TextView) view.findViewById(R.id.pinOldPrice);
            passbasket = (AutoClickImageView) view.findViewById(R.id.passbasket);
            if (mainBlockDetailModel.goodsDTO == null) {

            } else {
//                passbasket.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (onBasketClickListener != null) {
//                            onBasketClickListener.onBasketClick(v, mainBlockDetailModel);
//                        }
//                    }
//                });
                TextView tagText = view.findViewById(R.id.tagText);
                tagText.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(mainBlockDetailModel.goodsDTO.getTagFirst())) {
                    tagText.setVisibility(View.VISIBLE);
                    if (mainBlockDetailModel.goodsDTO.getTagFirst().length() > 3) {
                        String org = mainBlockDetailModel.goodsDTO.getTagFirst();
                        String resultOrg = org.substring(0, 2) + "\n" + org.substring(2, org.length());
                        tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
                        tagText.setText(resultOrg);
                    } else {
                        tagText.setText(mainBlockDetailModel.goodsDTO.getTagFirst());
                        tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
                    }
                }
                com.healthy.library.businessutil.GlideCopy.with(context).load(mainBlockDetailModel.goodsDTO.getGoodsIcon())
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(goodsImg);
                goodsName.setText(mainBlockDetailModel.goodsDTO.goodsTitle);
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(mainBlockDetailModel.goodsDTO.platformPrice));
                pinOldPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(mainBlockDetailModel.goodsDTO.retailPrice));
                pinOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MobclickAgent.onEvent(context, "event2APPShopHomeSpecialGoodsClick",
                                new SimpleHashMapBuilder<String, String>().puts("soure", "专区商品图片标题的点击量"));
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", mainBlockDetailModel.goodsId)
                                .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                                .navigation();
                    }
                });
                goodsList.addView(view);
                buildchildCoupons(goodsCouponLL, item);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MobclickAgent.onEvent(context, "event2APPShopHomeSpecialGoodsClick",
                                new SimpleHashMapBuilder<String, String>().puts("soure", "专区商品图片标题的点击量"));
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", mainBlockDetailModel.goodsId)
                                .navigation();
                    }
                });
            }

        }
    }

    private void buildchildCoupons(LinearLayout goodsCouponLL, MainBlockModel item) {
        goodsCouponLL.removeAllViews();
//        for (int i = 0; i < 5; i++) {
//            View view = LayoutInflater.from(context).inflate(R.layout.item_mall_goods_coupon, goodsCouponLL, false);
//            goodsCouponLL.addView(view);
//        }
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

    private void initView() {

    }
}
