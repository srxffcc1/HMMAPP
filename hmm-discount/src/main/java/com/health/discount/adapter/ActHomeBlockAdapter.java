package com.health.discount.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
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
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.discount.R;
import com.health.discount.contract.ActHomeBlockContract;
import com.health.discount.presenter.ActHomeBlockPresenter;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.constant.SpKey;
import com.healthy.library.loader.ImageNetAdapter;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.ColorInfo;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.MainBlockDetailModel;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.presenter.AdPresenterCopy;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.GlideOptions;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.DashLine;
import com.healthy.library.widget.ImageTextView;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ActHomeBlockAdapter implements ActHomeBlockContract.View {
    SparseIntArray layouts = new SparseIntArray();
    Context mContext;
    private List<AdModel> adv;

    public void setAdv(List<AdModel> adv) {
        this.adv = adv;
    }

    public List<AdModel> getAdv() {
        return adv;
    }

    public interface OnHomeBasketClickListener {
        void onBasketClick(View view, Object obj);
    }

    public void setOnBasketClickListener(OnHomeBasketClickListener onBasketClickListener) {
        this.onBasketClickListener = onBasketClickListener;
    }

    OnHomeBasketClickListener onBasketClickListener;

    public ActHomeBlockAdapter(Context context) {
        this.mContext = context;
        addItemType(1, R.layout.dis_item_block_child1);//单行通栏列表
        addItemType(2, R.layout.dis_item_block_child2);//错位组合栏
        addItemType(3, R.layout.dis_item_block_child3);//通栏大图+介绍
        addItemType(4, R.layout.dis_item_block_child5);//多栏图片+商品组合展示
        addItemType(5, R.layout.dis_item_block_child4);//通栏图片+商品组合展示
        //addItemType(6, R.layout.dis_item_block_child6);
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
                    e.printStackTrace();
                }
            }
            imageLoader = new ImageNetAdapter(new SimpleArrayListBuilder<String>().putList(bannerimgs, new ObjectIteraor<AdModel>() {
                @Override
                public Object getDesObj(AdModel adModel) {
                    return adModel.photoUrls;
                }
            }), TransformUtil.dp2px(mContext, 10f), colorList);
            banner.setAdapter(imageLoader)
                    .setIndicator(new RectangleIndicator(mContext))
                    .setIndicatorGravity(IndicatorConfig.Direction.RIGHT);//设置指示器位置（左，中，右）
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(Object data, int position) {
                    AdModel adModel = bannerimgs.get(position);
                    MARouterUtils.passToTarget(mContext,adModel);
                }

                @Override
                public void onBannerChanged(int position) {

                }
            });
            //System.out.println("修改背景版333");
//            banner.setIndicator(new CircleIndicator(mContext));
            banner.stop();
            banner.start();

        }
    }

    private void addItemType(int i, int res) {
        layouts.put(i, res);
    }

    public int getCount() {
        return 0;
    }

    public View getView(int position, final MainBlockModel item, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(getViewRes(position), parent, false);
//        buildData(view, item, position);
        try {
            view.findViewById(R.id.topLL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.exhibition < 4) {
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_SPECAREA)
                                .withString("blockId", item.id + "")
                                .withString("blockName", item.themeName + "")
                                .navigation();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        View pview = LayoutInflater.from(mContext).inflate(R.layout.dis_item_block_p, parent, false);
        FrameLayout frameLayout = pview.findViewById(R.id.childP);
        Banner banner = pview.findViewById(R.id.act_bottom_banner);
        banner.setVisibility(View.GONE);
        if(position==0&&adv!=null&&adv.size()>0){
            banner.setVisibility(View.VISIBLE);
            buildBannerView(banner,adv);
        }
        frameLayout.removeAllViews();
        frameLayout.addView(view);
        if (item.childList != null && item.childList.size() > 0) {
            for (int i = 0; i < item.childList.size(); i++) {
                MainBlockModel itemchild = item.childList.get(i);
                ActHomeBlockPresenter actHomeBlockPresenter = new ActHomeBlockPresenter(mContext, this);
                actHomeBlockPresenter.getBlockDetail(view, item, itemchild, position, new SimpleHashMapBuilder<String, Object>().puts("themeId", itemchild.id + "").puts("pageNum", "1").puts("pageSize", "10"));
            }

        }
        ActHomeBlockPresenter actHomeBlockPresenter = new ActHomeBlockPresenter(mContext, this);
        actHomeBlockPresenter.getBlockDetail(view, item, item, position, new SimpleHashMapBuilder<String, Object>().puts("themeId", item.id + "").puts("pageNum", "1").puts("pageSize", "10"));
        return pview;
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

        if (getItemType(position) == 0) {

        }
    }

    private void buildType6(View view, MainBlockModel item, int position) {
        ImageTextView topTitle;
        ImageTextView topSecondTitle;
        ImageTextView topMore;
        ImageView shopIcon;
        ImageView videoTip;
        TextView shopName;
        ImageTextView loc;
        TextView locDistance;
        ImageView couponMore;
        HorizontalScrollView goodsCouponPLL;
        LinearLayout goodsCouponLL;
        DashLine couponDash;
        RelativeLayout headNowLL;
        TextView manCount;
        GridLayout bottomGrid;

        topTitle = (ImageTextView) view.findViewById(R.id.topTitle);
        topSecondTitle = (ImageTextView) view.findViewById(R.id.topSecondTitle);
        topMore = (ImageTextView) view.findViewById(R.id.topMore);
        shopIcon = (ImageView) view.findViewById(R.id.shopIcon);
        videoTip = (ImageView) view.findViewById(R.id.videoTip);
        shopName = (TextView) view.findViewById(R.id.shopName);
        loc = (ImageTextView) view.findViewById(R.id.loc);
        locDistance = (TextView) view.findViewById(R.id.locDistance);
        couponMore = (ImageView) view.findViewById(R.id.couponMore);
        goodsCouponPLL = (HorizontalScrollView) view.findViewById(R.id.goodsCouponPLL);
        goodsCouponLL = (LinearLayout) view.findViewById(R.id.goodsCouponLL);
        couponDash = (DashLine) view.findViewById(R.id.couponDash);
        headNowLL = (RelativeLayout) view.findViewById(R.id.headNowLL);
        manCount = (TextView) view.findViewById(R.id.manCount);
        bottomGrid = (GridLayout) view.findViewById(R.id.bottomGrid);


        buildBottomGrid(bottomGrid, item);
    }

    private void buildBottomGrid(GridLayout gridLayout, MainBlockModel item) {
        gridLayout.removeAllViews();
        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(mContext, 2);
            mCornerRadius = (int) TransformUtil.dp2px(mContext, 5);
        }
        for (int i = 0; i < 4; i++) {
            int row = 3;
            int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight() - (2 + 2 * (row - 1)) * mMargin) / row;
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = w;
            View viewparent = LayoutInflater.from(mContext).inflate(R.layout.dis_item_block_child2_item, gridLayout, false);
            gridLayout.addView(viewparent, params);
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

    private void buildGoodsHlist(LinearLayout goodsHList, final MainBlockModel item) {
        goodsHList.removeAllViews();
        for (int i = 0; i < item.childList.size(); i++) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.dis_item_block_child5_item, goodsHList, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_SPECAREA)
                            .withString("blockId", item.id + "")
                            .withString("blockName", item.themeName + "")
                            .navigation();


                }
            });
            TextView topImgTag=view.findViewById(R.id.topImgTag);
            ImageView topImgTagIcon=view.findViewById(R.id.topImgTagIcon);
            TextView topImgTitle=view.findViewById(R.id.topImgTitle);
            TextView topImgSecondTitle=view.findViewById(R.id.topImgSecondTitle);

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
            if(finalItemchild.themeTag.contains("http")){
                topImgTag.setVisibility(View.GONE);
                topImgTagIcon.setVisibility(View.VISIBLE);
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(itemchild.themeTag)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(topImgTagIcon);
            }
            topImgTitle.setText(finalItemchild.describe);
            topImgSecondTitle.setText(finalItemchild.adContent);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_SPECAREA)
                            .withString("blockId", finalItemchild.id + "")
                            .withString("blockName", finalItemchild.themeName + "")
                            .navigation();
                }
            });

            com.healthy.library.businessutil.GlideCopy.with(mContext).load(itemchild.iconUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .apply(GlideOptions.withRoundedOptions((int)TransformUtil.dp2px(mContext,12f), RoundedCornersTransformation.CornerType.TOP, R.drawable.img_690_260_default, R.drawable.img_690_260_default))
                    .error(R.drawable.img_1_1_default)

                    .into(topImg);
            buildchild5items(goodsList, itemchild);
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

    private void buildPostList(LinearLayout postList, final MainBlockModel item) {
        postList.removeAllViews();
        for (int i = 0; i < item.childList.size(); i++) {
            final MainBlockModel itemchild = item.childList.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.dis_item_block_child4_item, postList, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_SPECAREA)
                            .withString("blockId", item.id + "")
                            .withString("blockName", item.themeName + "")
                            .navigation();
                }
            });
            TextView topImgTag=view.findViewById(R.id.topImgTag);
            TextView topImgTitle=view.findViewById(R.id.topImgTitle);
            TextView topImgSecondTitle=view.findViewById(R.id.topImgSecondTitle);
            ImageView topImg;
            Space space;
            LinearLayout goodsList;
            topImg = (ImageView) view.findViewById(R.id.topImg);
            space = (Space) view.findViewById(R.id.space);
            goodsList = (LinearLayout) view.findViewById(R.id.goodsList);

            topImgTag.setText(itemchild.themeTag);
            topImgTitle.setText(itemchild.describe);
            topImgSecondTitle.setText(itemchild.adContent);
            com.healthy.library.businessutil.GlideCopy.with(mContext).load(itemchild.iconUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    .apply(GlideOptions.withRoundedOptions((int)TransformUtil.dp2px(mContext,12f), RoundedCornersTransformation.CornerType.TOP, R.drawable.img_690_260_default, R.drawable.img_690_260_default))

                    .into(topImg);
            buildchild1items(goodsList, itemchild);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

    private void buildHPosts(LinearLayout postLL, final MainBlockModel item) {
        postLL.removeAllViews();
        for (int i = 0; i < item.childList.size(); i++) {
            final MainBlockModel itemchild = item.childList.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.dis_item_block_child3_item, postLL, false);
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
            com.healthy.library.businessutil.GlideCopy.with(mContext).load(itemchild.iconUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    .apply(GlideOptions.withRoundedOptions((int)TransformUtil.dp2px(mContext,12f), RoundedCornersTransformation.CornerType.TOP, R.drawable.img_690_260_default, R.drawable.img_690_260_default))

                    .into(postImage);
            postTitle.setText(itemchild.themeName);
            postSecondTitle.setText(itemchild.themeSubname);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            CornerImageView cornerImageView = new CornerImageView(mContext);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) TransformUtil.dp2px(mContext, 18), (int) TransformUtil.dp2px(mContext, 18));
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

    private int mMargin;
    private int mCornerRadius;

    private void buildRightGrid(GridLayout gridLayout, MainBlockModel item) {
        gridLayout.removeAllViews();
        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(mContext, 2);
            mCornerRadius = (int) TransformUtil.dp2px(mContext, 5);
        }
        for (int i = 1; i < 5; i++) {
            int row = 2;
            int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight() - (2 + 2 * (row - 1)) * mMargin) / row;
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = w;
            View view = LayoutInflater.from(mContext).inflate(R.layout.dis_item_block_child2_item, gridLayout, false);
            ImageView goodsImg;
            LinearLayout goodsCouponLL;
            TextView goodsName;
            TextView goodsMoney;
            AutoClickImageView passbasket;
            goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
            goodsCouponLL = (LinearLayout) view.findViewById(R.id.goodsCouponLL);
            goodsName = (TextView) view.findViewById(R.id.goodsName);
            goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
            passbasket = (AutoClickImageView) view.findViewById(R.id.passbasket);
            gridLayout.addView(view, params);
            MainBlockDetailModel mainBlockDetailModel = null;
            try {
                mainBlockDetailModel = item.detailList.get(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (item.detailList == null || item.detailList.size() == 0 || mainBlockDetailModel == null || item.detailList.get(i).goodsDTO == null) {
                view.setVisibility(View.INVISIBLE);
            } else {
                final MainBlockDetailModel finalMainBlockDetailModel = mainBlockDetailModel;
                passbasket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onBasketClickListener != null) {
                            onBasketClickListener.onBasketClick(v, finalMainBlockDetailModel);
                        }
                    }
                });
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(item.detailList.get(i).goodsDTO.getGoodsIcon())
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(goodsImg);
                goodsName.setText(item.detailList.get(i).goodsDTO.goodsTitle);
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(item.detailList.get(i).goodsDTO.platformPrice));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", finalMainBlockDetailModel.goodsId)
                                .navigation();
                    }
                });
            }

        }
    }

    private void buildchild5items(LinearLayout goodsList, final MainBlockModel item) {
        goodsList.removeAllViews();
        for (int i = 0; i < item.detailList.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.dis_item_block_child5_item_item, goodsList, false);
            ImageView goodsImg;
            TextView goodsMoney;
            TextView goodsOldMoney;
            goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
            goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
            goodsOldMoney=(TextView) view.findViewById(R.id.goodsOldMoney);
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
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(mainBlockDetailModel.getGoodsIcon())
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(goodsImg);
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(mainBlockDetailModel.platformPrice));
                goodsOldMoney.setText("¥"+FormatUtils.moneyKeep2Decimals(mainBlockDetailModel.retailPrice));
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

    private void buildchild1items(LinearLayout goodsList, MainBlockModel item) {
        goodsList.removeAllViews();
        for (int i = 0; i < item.detailList.size(); i++) {
            final MainBlockDetailModel mainBlockDetailModel = item.detailList.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.dis_item_block_child1_item, goodsList, false);
            ImageView goodsImg;
            LinearLayout goodsCouponLL;
            TextView goodsName;
            TextView goodsMoney;
            AutoClickImageView passbasket;
            goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
            goodsCouponLL = (LinearLayout) view.findViewById(R.id.goodsCouponLL);
            goodsName = (TextView) view.findViewById(R.id.goodsName);
            goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
            passbasket = (AutoClickImageView) view.findViewById(R.id.passbasket);
            if (mainBlockDetailModel.goodsDTO == null) {

            } else {
                passbasket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onBasketClickListener != null) {
                            onBasketClickListener.onBasketClick(v, mainBlockDetailModel);
                        }
                    }
                });
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(mainBlockDetailModel.goodsDTO.getGoodsIcon())
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(goodsImg);
                goodsName.setText(mainBlockDetailModel.goodsDTO.goodsTitle);
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(mainBlockDetailModel.goodsDTO.platformPrice));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", mainBlockDetailModel.goodsId)
                                .withString("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                                .navigation();
                    }
                });
                goodsList.addView(view);
                buildchildCoupons(goodsCouponLL, item);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", mainBlockDetailModel.goodsId)
                                .navigation();
                    }
                });
            }

        }
    }


    private void buildLeftGrid(LinearLayout leftGrid, final MainBlockModel item) {
        leftGrid.removeAllViews();
        View view = LayoutInflater.from(mContext).inflate(R.layout.dis_item_block_child2_item_big, leftGrid, false);
        ImageView goodsImg;
        LinearLayout goodsCouponLL;
        TextView goodsName;
        TextView goodsMoney;
        AutoClickImageView passbasket;
        goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
        goodsCouponLL = (LinearLayout) view.findViewById(R.id.goodsCouponLL);
        goodsName = (TextView) view.findViewById(R.id.goodsName);
        goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
        passbasket = (AutoClickImageView) view.findViewById(R.id.passbasket);
        passbasket.setVisibility(View.GONE);
        leftGrid.addView(view);
        if (item.detailList == null || item.detailList.size() == 0 || item.detailList.get(0).goodsDTO == null) {
            return;
        }
        com.healthy.library.businessutil.GlideCopy.with(mContext).load(item.detailList.get(0).goodsDTO.getGoodsIcon())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(goodsImg);
        goodsName.setText(item.detailList.get(0).goodsDTO.goodsTitle);
        goodsMoney.setText(FormatUtils.moneyKeep2Decimals(item.detailList.get(0).goodsDTO.platformPrice));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", item.detailList.get(0).goodsId)
                        .navigation();
            }
        });
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


    private void buildchildCoupons(LinearLayout goodsCouponLL, MainBlockModel item) {
        goodsCouponLL.removeAllViews();
//        for (int i = 0; i < 5; i++) {
//            View view = LayoutInflater.from(context).inflate(R.layout.item_mall_goods_coupon, goodsCouponLL, false);
//            goodsCouponLL.addView(view);
//        }
    }

    List<MainBlockModel> datas;

    public void build(LinearLayout parent, List<MainBlockModel> datas) {
        parent.removeAllViews();
        this.datas = datas;
        for (int i = 0; i < datas.size(); i++) {
            parent.addView(getView(i, datas.get(i), parent));
        }
    }

    private int getItemType(int position) {
        try {
            MainBlockModel item = datas.get(position);
            return item.exhibition;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getViewRes(int position) {
        MainBlockModel item = datas.get(position);
        return layouts.get(item.exhibition);
    }

    @Override
    public void onSucessGetBlockDetailList(View view, MainBlockModel item, int position) {
        buildData(view, item, position);
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
