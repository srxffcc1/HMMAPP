package com.health.servicecenter.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.google.android.flexbox.FlexboxLayout;
import com.health.servicecenter.R;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.KKGroupGoodsDialog;
import com.healthy.library.business.PinWithShop2Dialog;
import com.healthy.library.constant.SpKey;
import com.healthy.library.fragment.EmptyLayoutFragment;
import com.healthy.library.fragment.PicFragment;
import com.healthy.library.fragment.VideoFragment;
import com.healthy.library.model.AssemableTeam;
import com.healthy.library.model.Goods2DetailKick;
import com.healthy.library.model.Goods2DetailPin;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.MallGroupSimple;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MallGoodsDetialTopAllAdapter extends BaseAdapter<GoodsDetail> {

    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    private VideoFragment videoFragment;

    boolean hasvideo = false;
    boolean isShowVideo = true;
    OnTopLoadMoreListener onTopLoadMoreListener;
    private MallGroupSimple mallGroupSimple;

    public void setPinOnDialogClickListener(PinWithShop2Dialog.PinOnDialogClickListener pinOnDialogClickListener) {
        this.pinOnDialogClickListener = pinOnDialogClickListener;
    }

    PinWithShop2Dialog.PinOnDialogClickListener pinOnDialogClickListener;
    private int currentPageScrollIndex = -1;
    private float currentPageScrollMove = -1;
    private List<AssemableTeam> teamList;
    private Goods2DetailPin.Assemble pinGoods;
    private Goods2DetailKick.BargainInfo kickGoods;
    private ImageTextView morePin;
    private int actType = 0;//0普通 1砍价 2拼团 3秒杀 4新人专享 8PLUS专享
    private boolean isNtReal = true;//是否有新人资格

    private boolean isLoadSuccess;
    private int type = 0;

    public void setLoadSuccess(boolean loadSuccess) {
        this.isLoadSuccess = loadSuccess;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setActType(int actType) {
        this.isLoadSuccess = false;
        this.actType = actType;
        notifyDataSetChanged();
    }

    public void setNtReal(boolean ntReal) {
        isNtReal = ntReal;
    }

    public void setOnTopLoadMoreListener(OnTopLoadMoreListener onTopLoadMoreListener) {
        this.onTopLoadMoreListener = onTopLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return 4;
    }

    public MallGoodsDetialTopAllAdapter() {
        this(R.layout.service_item_goodsdetail_top_all);
    }

    private MallGoodsDetialTopAllAdapter(int viewId) {
        super(viewId);
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
            ////System.out.println("adapter0");
            goodsNormal.setVisibility(View.VISIBLE);
            buildNormalView(baseHolder);
        } else if (actType == 1) {
            ////System.out.println("adapter1");
            pinStoreTop.addView(LayoutInflater.from(context).inflate(R.layout.item_include_kick_top, pinStoreTop, false));
            buildKickView(baseHolder);
        } else if (actType == 2) {
            goodsPinBottom.setVisibility(View.VISIBLE);
            ////System.out.println("adapter2");
            pinStoreTop.addView(LayoutInflater.from(context).inflate(R.layout.item_include_pin_top, pinStoreTop, false));
            goodsPinBottom.addView(LayoutInflater.from(context).inflate(R.layout.item_include_pin_bottom, goodsPinBottom, false));
            buildPinView(baseHolder);
        } else if (actType == 3) {
            ////System.out.println("adapter3");
            pinStoreTop.addView(LayoutInflater.from(context).inflate(R.layout.item_include_kill_top, pinStoreTop, false));
            buildKillView(baseHolder);
        } else if (actType == 4) {
            ////System.out.println("adapter4");
            goodsNormal.setVisibility(View.VISIBLE);
            goodsTitleFlag.setVisibility(View.VISIBLE);
            buildNtView(baseHolder);

        } else if (actType == 5) {
            goodsPoint.setVisibility(View.VISIBLE);
            buildPointView(baseHolder);
        } else {
            ////System.out.println("adapter0");
            goodsNormal.setVisibility(View.VISIBLE);
            buildNormalView(baseHolder);
        }
        personNameText = (TextView) baseHolder.itemView.findViewById(R.id.personNameText);
        personFrame = (LinearLayout) baseHolder.itemView.findViewById(R.id.personFrame);
        personIcon = (CornerImageView) baseHolder.itemView.findViewById(R.id.personIcon);
        personText = (TextView) baseHolder.itemView.findViewById(R.id.personText);

        //type如果为1 = ServiceGoodsDetail 优化滑动问题，也用于避免影响其他页面
        if (type == 1) {
            isLoadSuccess = true;
        }
    }

    private void buildPointView(BaseHolder baseHolder) {
        buildNormalView(baseHolder);
        final GoodsDetail goodsSpecDetail = getModel();
        TextView goodsPointValue = baseHolder.itemView.findViewById(R.id.goodsPointValue);
        TextView goodsPointValueRight = baseHolder.itemView.findViewById(R.id.goodsPointValueRight);
        TextView goodsPointValueSale = baseHolder.itemView.findViewById(R.id.goodsPointValueSale);
        if (goodsSpecDetail.marketingGoodsChildren != null && goodsSpecDetail.marketingGoodsChildren.size() > 0) {
            goodsPointValue.setText(FormatUtils.moneyKeep2Decimals(goodsSpecDetail.marketingGoodsChildren.get(0).pointsPrice));
            if (goodsSpecDetail.marketingGoodsChildren.get(0).marketingPrice > 0) {
                goodsPointValueRight.setText("+¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.marketingGoodsChildren.get(0).marketingPrice));
            }
        }
        goodsPointValueSale.setText("已兑" + (ParseUtils.parseNumber((goodsSpecDetail.marketingSales + goodsSpecDetail.virtualSales) + "", 10000, "万")) + "件");


    }

    /**
     * 新客头布局
     *
     * @param baseHolder
     */
    private void buildNtView(BaseHolder baseHolder) {
        buildNormalView(baseHolder);

        final GoodsDetail goodsSpecDetail = getModel();
        TextView goodMoneyValue;
        goodMoneyValue = (TextView) baseHolder.itemView.findViewById(R.id.goodMoneyValue);
        TextView goodsTitleFlag = baseHolder.itemView.findViewById(R.id.goodsTitleFlag);
        TextView goodMoneyValue2 = (TextView) baseHolder.itemView.findViewById(R.id.goodMoneyValue2);
        goodMoneyValue2.setVisibility(View.VISIBLE);
        if (isNtReal) {
            goodMoneyValue2.setText("￥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.retailPrice));
            goodsTitleFlag.setText("新客专享");
            goodMoneyValue.setText("" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.marketingGoodsChildren.get(0).marketingPrice));
        } else {
            goodsTitleFlag.setText("新客专享价" + "￥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.marketingGoodsChildren.get(0).marketingPrice));
            goodMoneyValue.setText("" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.platformPrice));
        }
    }

    /**
     * 秒杀头布局
     *
     * @param baseHolder
     */
    private void buildKillView(BaseHolder baseHolder) {
        buildNormalView(baseHolder);
        final GoodsDetail goodsSpecDetail = getModel();
        TextView pinMoney;
        TextView pinMoneySingle;
        TextView pinMoneyTeamNum;
        TextView kickDay;
        TextView kickHour;
        TextView kickMin;
        TextView kickSec;
        TextView kickDayT;
        pinMoney = (TextView) baseHolder.itemView.findViewById(R.id.pinMoney);
        pinMoneySingle = (TextView) baseHolder.itemView.findViewById(R.id.pinMoneySingle);
        pinMoneyTeamNum = (TextView) baseHolder.itemView.findViewById(R.id.pinMoneyTeamNum);
        kickDay = (TextView) baseHolder.itemView.findViewById(R.id.kickDay);
        kickHour = (TextView) baseHolder.itemView.findViewById(R.id.kickHour);
        kickMin = (TextView) baseHolder.itemView.findViewById(R.id.kickMin);
        kickSec = (TextView) baseHolder.itemView.findViewById(R.id.kickSec);
        kickDayT = (TextView) baseHolder.itemView.findViewById(R.id.kickDayT);
        double actPrice = goodsSpecDetail.marketingGoodsChildren.get(0).marketingPrice;
        pinMoney.setText(StringUtils.getResultPrice(actPrice + ""));
        pinMoneySingle.setText("￥" + StringUtils.getResultPrice(goodsSpecDetail.marketingGoodsChildren.get(0).retailPrice + ""));
        pinMoneySingle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        checkTimeOutEarly(goodsSpecDetail.getMarketingEndTime(), kickDay, kickDayT, kickHour, kickMin, kickSec);

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
        ////System.out.println("加载top");
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
        goodMoneyValue2.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.getStorePrice()));
//        if (goodsSpecDetail.discountTopModel != null) {
//            goodMoneyValue2.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.getStorePrice()));
//
//        }
        if (goodsSpecDetail.getStorePrice() > 0) {
            goodMoneyValue2.setVisibility(View.VISIBLE);
        } else {
            goodMoneyValue2.setVisibility(View.INVISIBLE);
        }
        goodMoneyValue2.setVisibility(View.VISIBLE);
        goodMoneyValue.setText("" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.platformPrice));
//        if(goodsSpecDetail.discountTopModel!=null){
//            goodMoneyValue2.setVisibility(View.GONE);
//            goodMoneyValue.setText("" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.getStandPrice(goodsSpecDetail.discountTopModel.standardPriceType)));
//
//        }
        if (goodsSpecDetail.getPlusPriceShow() > 0 && ("8".equals(goodsSpecDetail.marketingType) || "0".equals(goodsSpecDetail.marketingType) || goodsSpecDetail.marketingType == null)) {
            if (actType == 0 || actType == 8) {
                if (actType == 8) {
                    goodMoneyValue.setText("" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.marketingGoodsChildren.get(0).platformPrice));
                }
                goodsPlusLL.setVisibility(View.VISIBLE);
                if (SpUtils.getValue(context, SpKey.AUDITSTATUS, true)) {//如果在审核就不展示会员权益
                    goodsPlusLL.setVisibility(View.GONE);
                } else {
                    goodsPlusLL.setVisibility(View.VISIBLE);
                }
            } else {
                goodsPlusLL.setVisibility(View.GONE);
            }
            plusMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.getPlusPriceShow()));
            goodsPlusLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build(MineRoutes.MINE_VIPPLUSRIGHT)
                            .navigation();
                }
            });
        }
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
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(((BaseActivity) context).getSupportFragmentManager(), fragments, titles);
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

    /**
     * 拼团头布局
     *
     * @param baseHolder
     */
    private void buildPinView(@NonNull BaseHolder baseHolder) {
        buildNormalView(baseHolder);
        final GoodsDetail goodsSpecDetail = getModel();
        morePin = baseHolder.itemView.findViewById(R.id.morePin);
        groupTitleLL = baseHolder.itemView.findViewById(R.id.groupTitleLL);
        TextView pinMoney;
        TextView pinMoneySingle;
        TextView pinMoneyTeamNum;
        TextView kickDay;
        TextView kickDayT;
        TextView kickHour;
        TextView kickMin;
        TextView kickSec;
        LinearLayout groupLL;
        pinMoney = (TextView) baseHolder.itemView.findViewById(R.id.pinMoney);
        pinMoneySingle = (TextView) baseHolder.itemView.findViewById(R.id.pinMoneySingle);
        pinMoneyTeamNum = (TextView) baseHolder.itemView.findViewById(R.id.pinMoneyTeamNum);
        groupLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.groupLL);
        kickDay = (TextView) baseHolder.itemView.findViewById(R.id.kickDay);
        kickDayT = (TextView) baseHolder.itemView.findViewById(R.id.kickDayT);
        kickHour = (TextView) baseHolder.itemView.findViewById(R.id.kickHour);
        kickMin = (TextView) baseHolder.itemView.findViewById(R.id.kickMin);
        kickSec = (TextView) baseHolder.itemView.findViewById(R.id.kickSec);
        personNameText = (TextView) baseHolder.itemView.findViewById(R.id.personNameText);
        personFrame = (LinearLayout) baseHolder.itemView.findViewById(R.id.personFrame);
        personIcon = (CornerImageView) baseHolder.itemView.findViewById(R.id.personIcon);
        personText = (TextView) baseHolder.itemView.findViewById(R.id.personText);
        pinNowTeam = baseHolder.itemView.findViewById(R.id.pinNowTeam);

        LinearLayout goodsPinBottom = baseHolder.itemView.findViewById(R.id.goodsPinBottom);
        if (pinGoods != null) {
            pinMoney.setText(StringUtils.getResultPrice(pinGoods.assemblePrice + ""));
            pinMoneySingle.setText("原价￥" + StringUtils.getResultPrice(goodsSpecDetail.getRetailPrice() + ""));
            pinMoneyTeamNum.setText(pinGoods.regimentSize + "人拼");
            checkTimeOutPin(pinGoods, kickDay, kickDayT, kickHour, kickMin, kickSec);
            goodsPinBottom.setVisibility(View.GONE);
            if (teamList != null && teamList.size() > 0) {
                goodsPinBottom.setVisibility(View.VISIBLE);
                buildTeam(groupLL, teamList);
            }
        }
    }

    /**
     * 砍价头布局
     *
     * @param baseHolder
     */
    private void buildKickView(@NonNull BaseHolder baseHolder) {
        buildNormalView(baseHolder);
        final GoodsDetail goodsSpecDetail = getModel();
        TextView pinMoney;
        TextView pinMoneySingle;
        TextView pinMoneyTeamNum;
        TextView kickDay;
        TextView kickHour;
        TextView kickMin;
        TextView kickSec;
        TextView kickDayT;
        pinMoney = (TextView) baseHolder.itemView.findViewById(R.id.pinMoney);
        pinMoneySingle = (TextView) baseHolder.itemView.findViewById(R.id.pinMoneySingle);
        pinMoneyTeamNum = (TextView) baseHolder.itemView.findViewById(R.id.pinMoneyTeamNum);
        kickDay = (TextView) baseHolder.itemView.findViewById(R.id.kickDay);
        kickHour = (TextView) baseHolder.itemView.findViewById(R.id.kickHour);
        kickMin = (TextView) baseHolder.itemView.findViewById(R.id.kickMin);
        kickSec = (TextView) baseHolder.itemView.findViewById(R.id.kickSec);
        kickDayT = (TextView) baseHolder.itemView.findViewById(R.id.kickDayT);
        if (kickGoods != null) {
            pinMoney.setText(StringUtils.getResultPrice(kickGoods.floorPrice + ""));
            pinMoneySingle.setText("原价￥" + StringUtils.getResultPrice(goodsSpecDetail.getRetailPrice()/*retailPrice*/ + ""));
            pinMoneyTeamNum.setText("可砍至");
            checkTimeOutKick(kickGoods, kickDay, kickDayT, kickHour, kickMin, kickSec);
        }

    }

    private void checkTimeOutEarly(String endTimeString, final TextView kickDay, final TextView kickDayT, final TextView kickHour, final TextView kickMin, final TextView kickSec) {

        Date endTime = null;
        try {
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTimeString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long desorg = 0;
        long timer = 0;
        timer = endTime.getTime();
        desorg = endTime.getTime();
        timer = timer - System.currentTimeMillis();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        kickDay.setVisibility(View.VISIBLE);
        kickDayT.setVisibility(View.VISIBLE);
        if (timer > 0) {
            ////System.out.println("开始计时");
            final long finalTimer = timer;
            final long finalDesorg = desorg;
            countDownTimer = new CountDownTimer(finalTimer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] array = DateUtils.getDistanceTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
                    if ("0".equals(array[0])) {//0天
                        kickDay.setVisibility(View.GONE);
                        kickDayT.setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(array[0]) < 10) {
                        kickDay.setText("0" + array[0]);
                    } else {
                        kickDay.setText(array[0]);
                    }
                    kickHour.setText(array[1]);
                    kickMin.setText(array[2]);
                    kickSec.setText(array[3]);
                }

                public void onFinish() {
                    kickDay.setVisibility(View.GONE);
                    kickDayT.setVisibility(View.GONE);
                    kickDay.setText("0");
                    kickHour.setText("00");
                    kickMin.setText("00");
                    kickSec.setText("00");
                    if (moutClickListener != null) {
                        moutClickListener.outClick("倒计时结束", null);
                    }
                }
            }.start();
            //将此 countDownTimer 放入list.
        } else {
            kickDay.setVisibility(View.GONE);
            kickDayT.setVisibility(View.GONE);
            kickDay.setText("0");
            kickHour.setText("00");
            kickMin.setText("00");
            kickSec.setText("00");
        }
    }


    private void checkTimeOutKick(Goods2DetailKick.BargainInfo url, final TextView kickDay, final TextView kickDayT, final TextView kickHour, final TextView kickMin, final TextView kickSec) {

        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.startTime);
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long desorg = startTime.getTime();
        long timer = startTime.getTime();
        if (endTime != null) {
            timer = endTime.getTime();
            desorg = endTime.getTime();
        }
        timer = timer - System.currentTimeMillis();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        kickDay.setVisibility(View.VISIBLE);
        kickDayT.setVisibility(View.VISIBLE);
        if (timer > 0) {
            ////System.out.println("开始计时");
            final long finalTimer = timer;
            final long finalDesorg = desorg;
            countDownTimer = new CountDownTimer(finalTimer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] array = DateUtils.getDistanceTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
                    if ("0".equals(array[0])) {//0天
                        kickDay.setVisibility(View.GONE);
                        kickDayT.setVisibility(View.GONE);
                    }
                    kickDay.setText(array[0]);
                    kickHour.setText(array[1]);
                    kickMin.setText(array[2]);
                    kickSec.setText(array[3]);
                }

                public void onFinish() {
                    kickDay.setVisibility(View.GONE);
                    kickDayT.setVisibility(View.GONE);
                    kickDay.setText("0");
                    kickHour.setText("00");
                    kickMin.setText("00");
                    kickSec.setText("00");
                    if (moutClickListener != null) {
                        moutClickListener.outClick("倒计时结束", null);
                    }
                }
            }.start();
            //将此 countDownTimer 放入list.
        } else {
            kickDay.setVisibility(View.GONE);
            kickDayT.setVisibility(View.GONE);
            kickDay.setText("0");
            kickHour.setText("00");
            kickMin.setText("00");
            kickSec.setText("00");
        }
    }


    public void setTeamList(List<AssemableTeam> teamList) {
        this.teamList = teamList;
        notifyDataSetChanged();
    }

    public List<AssemableTeam> getTeamList() {
        return teamList;
    }

    public void setActGoods(Goods2DetailPin.Assemble pinGoods) {
        this.pinGoods = pinGoods;
        notifyDataSetChanged();
    }

    public void setActGoods(Goods2DetailKick.BargainInfo kickGoods) {
        this.kickGoods = kickGoods;
    }

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

    public MallGroupSimple getMallGroupSimple() {
        return mallGroupSimple;
    }

    public interface OnTopLoadMoreListener {
        void onTopLoadMore();
    }

    CountDownTimer countDownTimer;

    private void checkTimeOutPin(Goods2DetailPin.Assemble url, final TextView kickDay, final TextView kickDayT, final TextView kickHour, final TextView kickMin, final TextView kickSec) {

        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.startTime);
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long desorg = startTime.getTime();
        long timer = startTime.getTime();
        if (endTime != null) {
            timer = endTime.getTime();
            desorg = endTime.getTime();
        }
        timer = timer - System.currentTimeMillis();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        kickDay.setVisibility(View.VISIBLE);
        kickDayT.setVisibility(View.VISIBLE);
        if (timer > 0) {
            ////System.out.println("开始计时");
            final long finalTimer = timer;
            final long finalDesorg = desorg;
            countDownTimer = new CountDownTimer(finalTimer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] array = DateUtils.getDistanceTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
                    if ("0".equals(array[0])) {//0天
                        kickDay.setVisibility(View.GONE);
                        kickDayT.setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(array[0]) < 10) {
                        kickDay.setText("0" + array[0]);
                    } else {
                        kickDay.setText(array[0]);
                    }
                    kickHour.setText(array[1]);
                    kickMin.setText(array[2]);
                    kickSec.setText(array[3]);
                }

                public void onFinish() {
                    kickDay.setVisibility(View.GONE);
                    kickDayT.setVisibility(View.GONE);
                    kickDay.setText("0");
                    kickHour.setText("00");
                    kickMin.setText("00");
                    kickSec.setText("00");
                    if (moutClickListener != null) {
                        moutClickListener.outClick("倒计时结束", null);
                    }
                }
            }.start();
            //将此 countDownTimer 放入list.
        } else {
            kickDay.setVisibility(View.GONE);
            kickDayT.setVisibility(View.GONE);
            kickDay.setText("0");
            kickHour.setText("00");
            kickMin.setText("00");
            kickSec.setText("00");
        }
    }

    private SparseArray<CountDownTimer> countDownCounters = new SparseArray<>();
    private ConstraintLayout pinNowTeam;
    private ConstraintLayout groupTitleLL;

    private void buildTeam(LinearLayout groupLL, List<AssemableTeam> couponInfoByMerchants) {
        groupLL.removeAllViews();
        pinNowTeam.setVisibility(View.VISIBLE);
        if (couponInfoByMerchants != null && couponInfoByMerchants.size() > 0) {

            if (couponInfoByMerchants.size() > 3) {
                morePin.setVisibility(View.VISIBLE);
                groupTitleLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PinWithShop2Dialog.newInstance().setData(pinGoods, pinGoods.id + "").setPinOnDialogClickListener(pinOnDialogClickListener)
                                .show(((AppCompatActivity) context).getSupportFragmentManager(), "商品页弹出商家优惠券列表");
                    }
                });
            } else {
                morePin.setVisibility(View.GONE);
                groupTitleLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
            List<AssemableTeam> cct = new ArrayList<>();
            if (couponInfoByMerchants.size() > 4) {
                cct.add(couponInfoByMerchants.get(0));
                cct.add(couponInfoByMerchants.get(1));
                cct.add(couponInfoByMerchants.get(2));
                cct.add(couponInfoByMerchants.get(3));
            } else {
                cct.addAll(couponInfoByMerchants);
            }


            for (int i = 0; i < cct.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.mall_item_activity_group_goods, null);
                final AssemableTeam item = cct.get(i);
                CornerImageView groupIcon;
                TextView groupName;
                TextView groupTeamNumber;
                TextView groupTimeLimit;
                TextView groupButton;
                groupIcon = (CornerImageView) view.findViewById(R.id.groupIcon);
                groupName = (TextView) view.findViewById(R.id.groupName);
                groupTeamNumber = (TextView) view.findViewById(R.id.groupTeamNumber);
                groupTimeLimit = (TextView) view.findViewById(R.id.groupTimeLimit);
                groupButton = (TextView) view.findViewById(R.id.groupButton);
                View dividerN = view.findViewById(R.id.dividerN);
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(item.memberFaceUrl)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(groupIcon);
                groupTeamNumber.setText(SpanUtils.getBuilder(context, "还差").setForegroundColor(Color.parseColor("#444444"))
                        .append(item.remainderNum).setForegroundColor(Color.parseColor("#F02846"))
                        .append("人成团").setForegroundColor(Color.parseColor("#444444"))
                        .create());
                groupName.setText(item.memberNickName);
                if (item.isMyTeam(context)) {
                    groupButton.setText("邀请好友");
                } else {
                    groupButton.setText("去参团");
                }
                dividerN.setVisibility(View.VISIBLE);
                if (i == cct.size() - 1) {
                    dividerN.setVisibility(View.INVISIBLE);
                }
                item.realEndTime = pinGoods.endTime;
                item.regimentTimeLength = pinGoods.regimentTimeLength;
                item.regimentSize = pinGoods.regimentSize;
                CountDownTimer countDownTimer = countDownCounters.get(groupTimeLimit.hashCode());
                checkTimeOutPin(countDownTimer, item, groupTimeLimit, true);

                groupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.isMyTeam(context)) {
                            ARouter.getInstance()
                                    .build(DiscountRoutes.DIS_GROUPDETAIL)
                                    .withString("teamNum", item.teamNum)
                                    .navigation();
                        } else {
                            KKGroupGoodsDialog.newInstance().setAssemableTeam(item)
                                    .setGroupGoodsDialogClicker(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (pinOnDialogClickListener != null) {
                                                pinOnDialogClickListener.onGoDialogClick(v, item);
                                            }
                                        }
                                    }).show(((AppCompatActivity) context).getSupportFragmentManager(), "kkOkDialog");
                        }

                    }
                });
                groupLL.addView(view);
            }


        } else {
            pinNowTeam.setVisibility(View.GONE);
        }
    }

    private void checkTimeOutPin(CountDownTimer countDownTimer, AssemableTeam url, final TextView destview, boolean needtimer) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (needtimer) {
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.regimentTime);
                endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.realEndTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long nd = 1000 * url.regimentTimeLength * 60 * 60;//加入时间之后需要多少小时
            long desorg = startTime.getTime() + nd;
            long timer = startTime.getTime() + nd;
            if (endTime != null && endTime.getTime() <= timer) {
                timer = endTime.getTime();
                desorg = endTime.getTime();
            }
            timer = timer - System.currentTimeMillis();

            if (timer > 0) {
                ////System.out.println("开始计时");
                final long finalTimer = timer;
                final long finalDesorg = desorg;
                countDownTimer = new CountDownTimer(finalTimer, 1000) {
                    public void onTick(long millisUntilFinished) {
                        String array = DateUtils.getDistanceTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
                        destview.setText("剩余" + array + "");
                    }

                    public void onFinish() {
                        destview.setText("过期");
                    }
                }.start();
                countDownCounters.put(destview.hashCode(), countDownTimer);
            } else {
                destview.setText("过期");
            }
        }

    }
}
