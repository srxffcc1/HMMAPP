package com.health.servicecenter.adapter;

import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.health.servicecenter.R;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.fragment.EmptyLayoutFragment;
import com.healthy.library.fragment.PicFragment;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.MallGroupSimple;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.widget.CornerImageView;

import java.util.ArrayList;
import java.util.List;

public class GoodsSimpleDialogDetialTopAdapter extends BaseAdapter<GoodsDetail> {


    private List<Fragment> fragments = new ArrayList<>();
    private FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    boolean hasvideo = false;
    boolean isShowVideo = true;
    private OnTopLoadMoreListener onTopLoadMoreListener;
    private int currentPageScrollIndex = -1;
    private float currentPageScrollMove = -1;
    private int actType = 0;//0直播商品
    private BottomSheetDialogFragment baseDialogFragment;

    private boolean isLoadSuccess;
    private int type = 0;

    public void setLoadSuccess(boolean loadSuccess) {
        this.isLoadSuccess = loadSuccess;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDialog(BottomSheetDialogFragment dialog) {
        baseDialogFragment = dialog;
    }

    public void setActType(int actType) {
        this.isLoadSuccess = false;
        this.actType = actType;
        notifyDataSetChanged();
    }

    public void setOnTopLoadMoreListener(OnTopLoadMoreListener onTopLoadMoreListener) {
        this.onTopLoadMoreListener = onTopLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return 4;
    }

    public GoodsSimpleDialogDetialTopAdapter() {
        this(R.layout.service_item_goodsdetail_top_all);
    }
    

    private GoodsSimpleDialogDetialTopAdapter(int viewId) {

        super(viewId);
    }
    private MallGroupSimple mallGroupSimple;
    LinearLayout personFrame;
    TextView personNameText;
    TextView personText;
    CornerImageView personIcon;
    public void setMallGroupSimple(MallGroupSimple item) {
        this.mallGroupSimple = item;
        try {
            if (item == null) {
                personFrame.setVisibility(View.GONE);
            } else {
                personFrame.setVisibility(View.VISIBLE);
                personNameText.setText(item.memberNickName);
                personText.setText(item.regimentStatusStr);
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(item.memberFaceUrl)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(personIcon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        if (isLoadSuccess) {
            return;
        }
        LinearLayout pinStoreTop = baseHolder.itemView.findViewById(R.id.pinStoreTop);
        LinearLayout goodsPinBottom = baseHolder.itemView.findViewById(R.id.goodsPinBottom);
        TextView goodsTitleFlag = baseHolder.itemView.findViewById(R.id.goodsTitleFlag);
        ConstraintLayout goodsNormal = baseHolder.itemView.findViewById(R.id.goodsNormal);
        ConstraintLayout goodsPoint = baseHolder.itemView.findViewById(R.id.goodsPoint);
        ImageView goodsShareCoupon = baseHolder.itemView.findViewById(R.id.goodsShareCoupon);

        goodsPoint.setVisibility(View.GONE);
        goodsTitleFlag.setVisibility(View.GONE);
        goodsPinBottom.setVisibility(View.GONE);
        pinStoreTop.removeAllViews();
        goodsPinBottom.removeAllViews();
        goodsNormal.setVisibility(View.GONE);
        ConstraintLayout goodsPlusLL = baseHolder.itemView.findViewById(R.id.goodsPlusLL);
        goodsPlusLL.setVisibility(View.GONE);
        //type == 1 时才会触发isLoadSuccess变为true操作进行避免多次绘制
        if (actType == 0) {
            goodsNormal.setVisibility(View.VISIBLE);
            buildNormalView(baseHolder);
        }
        //type如果为1 = ServiceGoodsDetail 优化滑动问题，也用于避免影响其他页面
        if (type == 1) {
            isLoadSuccess = true;
        }
        personNameText = (TextView) baseHolder.itemView.findViewById(R.id.personNameText);
        personFrame = (LinearLayout) baseHolder.itemView.findViewById(R.id.personFrame);
        personIcon = (CornerImageView) baseHolder.itemView.findViewById(R.id.personIcon);
        personText = (TextView) baseHolder.itemView.findViewById(R.id.personText);
    }

    /**
     * 普通商品头布局
     *
     * @param baseHolder
     */
    private void buildNormalView(BaseHolder baseHolder) {
        final ViewPager img_banner = baseHolder.itemView.findViewById(R.id.img_banner);
        final View fpLL = baseHolder.itemView.findViewById(R.id.fpLL);
        final TextView nowPage;
        TextView fpTime = baseHolder.itemView.findViewById(R.id.fpTime);
        TextView allPage;
        nowPage = (TextView) baseHolder.itemView.findViewById(R.id.nowPage);
        allPage = (TextView) baseHolder.itemView.findViewById(R.id.allPage);
        final GoodsDetail goodsSpecDetail = getModel();
        ConstraintLayout goodsTopLL;
        LinearLayout dotLL;
        ConstraintLayout titleLL;
        TextView goodMoneyFlag;
        TextView goodMoneyValue;
        TextView goodMoneyValue2;
        TextView goodssalecount;
        TextView goodsTitle;
        FlexboxLayout goodsTipsLL;
        goodsTopLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.goodsTopLL);
        dotLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.dotLL);
        titleLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.titleLL);
        goodMoneyFlag = (TextView) baseHolder.itemView.findViewById(R.id.goodMoneyFlag);
        goodMoneyValue = (TextView) baseHolder.itemView.findViewById(R.id.goodMoneyValue);
        goodMoneyValue2 = (TextView) baseHolder.itemView.findViewById(R.id.goodMoneyValue2);
        goodssalecount = (TextView) baseHolder.itemView.findViewById(R.id.goodssalecount);
        goodsTitle = (TextView) baseHolder.itemView.findViewById(R.id.goodsTitle);
        goodsTipsLL = (FlexboxLayout) baseHolder.itemView.findViewById(R.id.goodsTipsLL);

        ConstraintLayout goodsPlusLL = baseHolder.itemView.findViewById(R.id.goodsPlusLL);
        goodsPlusLL.setVisibility(View.GONE);
        TextView plusMoney = baseHolder.itemView.findViewById(R.id.plusMoney);
        goodMoneyValue2.setVisibility(View.VISIBLE);
        goodMoneyValue2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        goodMoneyValue2.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.getRetailPrice()));
        if (goodsSpecDetail.getStorePrice() > 0) {

            goodMoneyValue2.setVisibility(View.VISIBLE);
        } else {
            goodMoneyValue2.setVisibility(View.INVISIBLE);
        }
        goodMoneyValue2.setVisibility(View.VISIBLE);
        goodMoneyValue.setText("" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.getLivePrice()));
//        if (goodsSpecDetail.getPlusPriceShow() > 0 && ("8".equals(goodsSpecDetail.marketingType) || "0".equals(goodsSpecDetail.marketingType) || goodsSpecDetail.marketingType == null)) {
//            if (actType == 0 || actType == 8) {
//                if (actType == 8) {
//                    goodMoneyValue.setText("" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.marketingGoodsChildren.get(0).platformPrice));
//                }
//                goodsPlusLL.setVisibility(View.VISIBLE);
//            } else {
//                goodsPlusLL.setVisibility(View.GONE);
//            }
//            plusMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.getPlusPriceShow()));
//            goodsPlusLL.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ARouter.getInstance().build(MineRoutes.MINE_VIPPLUSRIGHT)
//                            .navigation();
//                }
//            });
//        }
        goodssalecount.setText("已售" + (ParseUtils.parseNumber((goodsSpecDetail.appSales + goodsSpecDetail.virtualSales) + "", 10000, "万")) + "件");
        goodssalecount.setVisibility(View.INVISIBLE);
        goodsTitle.setText(goodsSpecDetail.goodsTitle);
        fragments.clear();
        if (currentPageScrollIndex == 0) {
            fpLL.setVisibility(View.VISIBLE);
        }
        goodsSpecDetail.change();
        List<String> titles = new ArrayList<>();
        for (int j = 0; j < goodsSpecDetail.headImages.size(); j++) {
            GoodsDetail.HeadImages headImage = goodsSpecDetail.headImages.get(j);
            titles.add((j + 1) + "");
            if (headImage.fileType == 4) {//说明是头部视频
                hasvideo = true;
                try {
                    int secondall = Integer.parseInt(headImage.imageDescription.split("\\.")[0]);
                    String min = (int) (secondall / 60) + "′";
                    String second = (int) (secondall % 60) + "″";
                    fpTime.setText(min + second);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fragments.add(PicFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("url", goodsSpecDetail.headImage)));
            }
            if (headImage.fileType == 1) {//说明是头部图片
                fragments.add(PicFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("url", headImage.filePath)));
            }
        }
        titles.add((titles.size() + 1) + "");
        fragments.add(EmptyLayoutFragment.newInstance(R.layout.widget_footer_detail_horizontal2));

        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(baseDialogFragment.getChildFragmentManager(), fragments, titles);
            // adapter
            img_banner.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        fragmentPagerItemAdapter.notifyDataSetChanged();
        fragmentPagerItemAdapter.setOnPageClickListener(new FragmentStatePagerItemAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(int index) {
                final String[] array = new String[goodsSpecDetail.headImages.size()];
                for (int j = 0; j < array.length; j++) {
                    array[j] = goodsSpecDetail.headImages.get(j).filePath;

                }
                ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                        .withCharSequenceArray("urls", array)
                        .withInt("pos", index)
                        .navigation();
            }
        });
        img_banner.setOffscreenPageLimit(fragments.size());
        fpLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] array = new String[goodsSpecDetail.headImages.size()];
                for (int j = 0; j < array.length; j++) {
                    array[j] = goodsSpecDetail.headImages.get(j).filePath;

                }
                ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                        .withCharSequenceArray("urls", array)
                        .withInt("pos", 0)
                        .navigation();
            }
        });
        if (!hasvideo) {
            fpLL.setVisibility(View.GONE);
        }
        allPage.setText(fragments.size() - 1 + "");
        img_banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPageScrollIndex = position;
                currentPageScrollMove = positionOffset;
            }

            @Override
            public void onPageSelected(int position) {
                if (hasvideo) {
                    if (position > 0) {
                        fpLL.setVisibility(View.GONE);
                    } else {
                        if (isShowVideo) {

                            fpLL.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (position == fragments.size() - 1) {
                    img_banner.setCurrentItem(position - 1);
                } else {
                    nowPage.setText((position + 1) + "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        img_banner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (currentPageScrollIndex == (fragments.size() - 1 - 1) && currentPageScrollMove > 0.25) {
                        //查看商品图文详情
                        img_banner.setCurrentItem(fragments.size() - 1 - 1);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                onTopLoadMoreListener.onTopLoadMore();
                            }
                        }, 100);
                    } else if (currentPageScrollIndex == (fragments.size() - 1)) {
                        //查看商品图文详情

                        img_banner.setCurrentItem(fragments.size() - 1 - 1);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                onTopLoadMoreListener.onTopLoadMore();
                            }
                        }, 100);
                    }
                }
                return false;
            }
        });
    }

    public interface OnTopLoadMoreListener {
        void onTopLoadMore();
    }
}
