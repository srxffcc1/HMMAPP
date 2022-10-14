//package com.health.servicecenter.adapter;
//
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.util.SparseArray;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager.widget.ViewPager;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.bumptech.glide.Glide;
//import com.google.android.flexbox.FlexboxLayout;
//import com.health.servicecenter.R;
//import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
//import com.healthy.library.base.BaseActivity;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseHolder;
//import com.healthy.library.fragment.EmptyLayoutFragment;
//import com.healthy.library.fragment.PicFragment;
//import com.healthy.library.fragment.VideoFragment;
//import com.healthy.library.model.AssemableTeam;
//import com.healthy.library.model.Goods2DetailPin;
//import com.healthy.library.model.GoodsDetail;
//import com.healthy.library.model.MallGroupSimple;
//import com.healthy.library.routes.DiscountRoutes;
//import com.healthy.library.routes.LibraryRoutes;
//import com.healthy.library.builder.SimpleHashMapBuilder;
//import com.healthy.library.utils.DateUtils;
//import com.healthy.library.utils.FormatUtils;
//import com.healthy.library.utils.ParseUtils;
//import com.healthy.library.utils.SpanUtils;
//import com.healthy.library.utils.StringUtils;
//import com.healthy.library.widget.CornerImageView;
//import com.healthy.library.widget.ImageTextView;
//import com.healthy.library.business.KKGroupGoodsDialog;
//import com.healthy.library.business.PinWithShop2Dialog;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class MallGoodsDetialTopPinAdapter extends BaseAdapter<GoodsDetail> {
//
//    private List<Fragment> fragments = new ArrayList<>();
//    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
//    private VideoFragment videoFragment;
//
//    boolean hasvideo = false;
//    boolean isShowVideo = true;
//    OnTopLoadMoreListener onTopLoadMoreListener;
//    private MallGroupSimple mallGroupSimple;
//
//    public void setPinOnDialogClickListener(PinWithShop2Dialog.PinOnDialogClickListener pinOnDialogClickListener) {
//        this.pinOnDialogClickListener = pinOnDialogClickListener;
//    }
//
//    PinWithShop2Dialog.PinOnDialogClickListener pinOnDialogClickListener;
//    private int currentPageScrollIndex = -1;
//    private float currentPageScrollMove = -1;
//    private List<AssemableTeam> teamList;
//    private Goods2DetailPin kickGoods;
//    private ImageTextView morePin;
//
//    public void setOnTopLoadMoreListener(OnTopLoadMoreListener onTopLoadMoreListener) {
//        this.onTopLoadMoreListener = onTopLoadMoreListener;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return 4;
//    }
//
//    public MallGoodsDetialTopPinAdapter() {
//        this(R.layout.service_item_goodsdetail_top_pin);
//    }
//
//    private MallGoodsDetialTopPinAdapter(int viewId) {
//        super(viewId);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        return new LinearLayoutHelper();
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
//        final ViewPager img_banner = baseHolder.itemView.findViewById(R.id.img_banner);
//        final View fpLL = baseHolder.itemView.findViewById(R.id.fpLL);
//        final TextView nowPage;
//        TextView fpTime = baseHolder.itemView.findViewById(R.id.fpTime);
//        TextView allPage;
//        nowPage = (TextView) baseHolder.itemView.findViewById(R.id.nowPage);
//        allPage = (TextView) baseHolder.itemView.findViewById(R.id.allPage);
//        final GoodsDetail goodsSpecDetail = getModel();
//
//        ConstraintLayout goodsTopLL;
//        LinearLayout dotLL;
//        ConstraintLayout titleLL;
//        TextView goodMoneyFlag;
//        TextView goodMoneyValue;
//        TextView goodMoneyValue2;
//        TextView goodssalecount;
//        TextView goodsTitle;
//        FlexboxLayout goodsTipsLL;
//
//        TextView pinMoney;
//        TextView pinMoneySingle;
//        TextView pinMoneyTeamNum;
//        TextView kickDay;
//        TextView kickDayT;
//        TextView kickHour;
//        TextView kickMin;
//        TextView kickSec;
//        LinearLayout groupLL;
//
//
//        ////System.out.println("加载top");
////        SmartRefreshHorizontal img_bannerR=baseHolder.itemView.findViewById(R.id.img_bannerR);
//        goodsTopLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.goodsTopLL);
//        dotLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.dotLL);
//        titleLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.titleLL);
//        goodMoneyFlag = (TextView) baseHolder.itemView.findViewById(R.id.goodMoneyFlag);
//        goodMoneyValue = (TextView) baseHolder.itemView.findViewById(R.id.goodMoneyValue);
//        goodMoneyValue2 = (TextView) baseHolder.itemView.findViewById(R.id.goodMoneyValue2);
//        goodssalecount = (TextView) baseHolder.itemView.findViewById(R.id.goodssalecount);
//        goodsTitle = (TextView) baseHolder.itemView.findViewById(R.id.goodsTitle);
//        goodsTipsLL = (FlexboxLayout) baseHolder.itemView.findViewById(R.id.goodsTipsLL);
//
//        pinMoney = (TextView) baseHolder.itemView.findViewById(R.id.pinMoney);
//        pinMoneySingle = (TextView) baseHolder.itemView.findViewById(R.id.pinMoneySingle);
//        pinMoneyTeamNum = (TextView) baseHolder.itemView.findViewById(R.id.pinMoneyTeamNum);
//
//        groupLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.groupLL);
//
//        kickDay = (TextView) baseHolder.itemView.findViewById(R.id.kickDay);
//        kickDayT = (TextView) baseHolder.itemView.findViewById(R.id.kickDayT);
//        kickHour = (TextView) baseHolder.itemView.findViewById(R.id.kickHour);
//        kickMin = (TextView) baseHolder.itemView.findViewById(R.id.kickMin);
//        kickSec = (TextView) baseHolder.itemView.findViewById(R.id.kickSec);
//
//        personNameText = (TextView) baseHolder.itemView.findViewById(R.id.personNameText);
//        personFrame = (LinearLayout) baseHolder.itemView.findViewById(R.id.personFrame);
//        personIcon = (CornerImageView) baseHolder.itemView.findViewById(R.id.personIcon);
//        personText = (TextView) baseHolder.itemView.findViewById(R.id.personText);
//
//        pinNowTeam = baseHolder.itemView.findViewById(R.id.pinNowTeam);
//        groupTitleLL = baseHolder.itemView.findViewById(R.id.groupTitleLL);
//        morePin=baseHolder.itemView.findViewById(R.id.morePin);
//
//        if (kickGoods != null) {
//            pinMoney.setText(StringUtils.getResultPrice(kickGoods.assembleDTO.assemblePrice + ""));
//            pinMoneySingle.setText("单买价￥" + StringUtils.getResultPrice(kickGoods.goodsDetails.platformPrice + ""));
//            pinMoneyTeamNum.setText(kickGoods.assembleDTO.regimentSize + "人拼");
//            checkTimeOut(kickGoods, kickDay, kickDayT, kickHour, kickMin, kickSec);
//            if (teamList != null) {
//                buildTeam(groupLL, teamList);
//            }
//
//        }
//
//
//        goodMoneyValue2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        goodMoneyValue2.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.getStorePrice()));
//        if (goodsSpecDetail.getStorePrice() == 0) {
//            goodMoneyValue2.setVisibility(View.GONE);
//        } else {
//            goodMoneyValue2.setVisibility(View.GONE);
//        }
//        goodMoneyValue.setText("" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.platformPrice));
//        goodssalecount.setText("已售" + (ParseUtils.parseNumber((goodsSpecDetail.appSales + goodsSpecDetail.virtualSales) + "", 10000, "万")) + "件");
//        goodsTitle.setText(goodsSpecDetail.goodsTitle);
//        fragments.clear();
//        if (currentPageScrollIndex == 0) {
//            fpLL.setVisibility(View.VISIBLE);
//        }
//        goodsSpecDetail.change();
//        List<String> titles = new ArrayList<>();
//        for (int j = 0; j < goodsSpecDetail.headImages.size(); j++) {
//            GoodsDetail.HeadImages headImage = goodsSpecDetail.headImages.get(j);
//            titles.add((j + 1) + "");
//            if (headImage.fileType == 4) {//说明是头部视频
//                hasvideo = true;
////                if(videoFragment==null){
////                    ////System.out.println("替换掉了fragment");
////                    fragments.add(PicFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("url", headImage.thumbsPath)));
//////                    videoFragment = VideoFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("url", "https://hmm-public.oss-cn-beijing.aliyuncs.com/folder/RPReplay_Final1585277294.MP4"));
//////                    videoFragment = VideoFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("url", headImage.filePath).puts("splashUrl",headImage.thumbsPath));
////                }
//                try {
//                    int secondall = Integer.parseInt(headImage.imageDescription.split("\\.")[0]);
//                    String min = (int) (secondall / 60) + "′";
//                    String second = (int) (secondall % 60) + "″";
//                    fpTime.setText(min + second);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
////                fragments.add(videoFragment);
//                fragments.add(PicFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("url", goodsSpecDetail.headImage)));
//            }
//            if (headImage.fileType == 1) {//说明是头部图片
//                fragments.add(PicFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("url", headImage.filePath)));
//            }
//        }
//        titles.add((titles.size() + 1) + "");
//        fragments.add(EmptyLayoutFragment.newInstance(R.layout.widget_footer_detail_horizontal2));
//
//        if (fragmentPagerItemAdapter == null) {
//            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(((BaseActivity) context).getSupportFragmentManager(), fragments, titles);
//            // adapter
//            img_banner.setAdapter(fragmentPagerItemAdapter);
//        } else {
//            fragmentPagerItemAdapter.setDataSource(fragments, titles);
//        }
//        fragmentPagerItemAdapter.notifyDataSetChanged();
//        fragmentPagerItemAdapter.setOnPageClickListener(new FragmentStatePagerItemAdapter.OnPageClickListener() {
//            @Override
//            public void onPageClick(int index) {
//                final String[] array = new String[goodsSpecDetail.headImages.size()];
//                for (int j = 0; j < array.length; j++) {
//                    array[j] = goodsSpecDetail.headImages.get(j).filePath;
//
//                }
////                if(hasvideo&&index==0){
////                    List<VodBean.DataBean.DetailBean> videoList=new ArrayList<>();
////                    String scanResult=goodsSpecDetail.headImages.get(0).filePath;
////                    FloatingPlayer.getInstance().destroy();
////                    Intent intent2 = new Intent(context, VodDisplayActivity.class);
////                    intent2.putExtra(Ids.PLAY_ID, -1);
////                    Ids.playingId=-1;
////                    intent2.putExtra(Ids.VIDEO_LIST, (Serializable)videoList);
////                    intent2.putExtra(Ids.PLAY_URL, scanResult);
////                    context.startActivity(intent2);
////                    return;
////                }
//                ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
//                        .withCharSequenceArray("urls", array)
//                        .withInt("pos", index)
//                        .navigation();
//            }
//        });
////        img_bannerR.setEnableRefresh(false);
////        img_bannerR.setOnLoadMoreListener(new OnLoadMoreListener() {
////            @Override
////            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//
////            }
////        });
//        img_banner.setOffscreenPageLimit(fragments.size());
//        fpLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                isShowVideo=false;
////                fpLL.setVisibility(View.GONE);
//                final String[] array = new String[goodsSpecDetail.headImages.size()];
//                for (int j = 0; j < array.length; j++) {
//                    array[j] = goodsSpecDetail.headImages.get(j).filePath;
//
//                }
//                ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
//                        .withCharSequenceArray("urls", array)
//                        .withInt("pos", 0)
//                        .navigation();
////                List<VodBean.DataBean.DetailBean> videoList=new ArrayList<>();
////                String scanResult=goodsSpecDetail.headImages.get(0).filePath;
////                FloatingPlayer.getInstance().destroy();
////                Intent intent2 = new Intent(context, VodDisplayActivity.class);
////                intent2.putExtra(Ids.PLAY_ID, -1);
////                Ids.playingId=-1;
////                intent2.putExtra(Ids.VIDEO_LIST, (Serializable)videoList);
////                intent2.putExtra(Ids.PLAY_URL, scanResult);
////                context.startActivity(intent2);
////                videoFragment.start();
////                hasvideo = false;
//
////                fpLL.setVisibility(View.GONE);
//            }
//        });
//        if (!hasvideo) {
//            fpLL.setVisibility(View.GONE);
//        }
//        allPage.setText(fragments.size() - 1 + "");
//        img_banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                currentPageScrollIndex = position;
//                currentPageScrollMove = positionOffset;
////                ////System.out.println("滑动变化"+currentPageScrollIndex+":"+currentPageScrollMove);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (hasvideo) {
//                    if (position > 0) {
//                        fpLL.setVisibility(View.GONE);
//                    } else {
//                        if (isShowVideo) {
//
//                            fpLL.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//                if (position == fragments.size() - 1) {
//                    img_banner.setCurrentItem(position - 1);
//                } else {
//                    nowPage.setText((position + 1) + "");
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        img_banner.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//
//                    if (currentPageScrollIndex == (fragments.size() - 1 - 1) && currentPageScrollMove > 0.25) {
//                        //查看商品图文详情
//                        img_banner.setCurrentItem(fragments.size() - 1 - 1);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                onTopLoadMoreListener.onTopLoadMore();
//                            }
//                        }, 100);
//
//
//                    } else if (currentPageScrollIndex == (fragments.size() - 1)) {
//                        //查看商品图文详情
//
//                        img_banner.setCurrentItem(fragments.size() - 1 - 1);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                onTopLoadMoreListener.onTopLoadMore();
//                            }
//                        }, 100);
//                    }
//                }
//                return false;
//            }
//        });
//    }
//
//    private void initView() {
//
//
//    }
//
//    public void setTeamList(List<AssemableTeam> teamList) {
//        this.teamList = teamList;
//        notifyDataSetChanged();
//    }
//
//    public List<AssemableTeam> getTeamList() {
//        return teamList;
//    }
//
//    public void setKickGoods(Goods2DetailPin kickGoods) {
//        this.kickGoods = kickGoods;
//        notifyDataSetChanged();
//    }
//
//    public Goods2DetailPin getKickGoods() {
//        return kickGoods;
//    }
//
//    LinearLayout personFrame;
//    TextView personNameText;
//    TextView personText;
//    CornerImageView personIcon;
//
//    public void setMallGroupSimple(MallGroupSimple item) {
//        this.mallGroupSimple = item;
//        try {
//            if (item == null) {
//                personFrame.setVisibility(View.GONE);
//            } else {
//                personFrame.setVisibility(View.VISIBLE);
//                personNameText.setText(item.memberNickName);
//                personText.setText(item.regimentStatusStr);
//                com.healthy.library.businessutil.GlideCopy.with(context)
//                        .load(item.memberFaceUrl)
//                        .placeholder(R.drawable.img_1_1_default2)
//                        .error(R.drawable.img_1_1_default)
//
//                        .into(personIcon);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public MallGroupSimple getMallGroupSimple() {
//        return mallGroupSimple;
//    }
//
//    public interface OnTopLoadMoreListener {
//        void onTopLoadMore();
//    }
//
//    CountDownTimer countDownTimer;
//
//    private void checkTimeOut(Goods2DetailPin url, final TextView kickDay, final TextView kickDayT, final TextView kickHour, final TextView kickMin, final TextView kickSec) {
//
//        Date startTime = null;
//        Date endTime = null;
//        try {
//            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.assembleDTO.startTime);
//            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.assembleDTO.endTime);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        long desorg = startTime.getTime();
//        long timer = startTime.getTime();
//        if (endTime != null) {
//            timer = endTime.getTime();
//            desorg = endTime.getTime();
//        }
//        timer = timer - System.currentTimeMillis();
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//            countDownTimer = null;
//        }
//        kickDay.setVisibility(View.VISIBLE);
//        kickDayT.setVisibility(View.VISIBLE);
//        if (timer > 0) {
//            ////System.out.println("开始计时");
//            final long finalTimer = timer;
//            final long finalDesorg = desorg;
//            countDownTimer = new CountDownTimer(finalTimer, 1000) {
//                public void onTick(long millisUntilFinished) {
//                    String[] array = DateUtils.getDistanceTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
//                    if ("0".equals(array[0])) {//0天
//                        kickDay.setVisibility(View.GONE);
//                        kickDayT.setVisibility(View.GONE);
//                    }
//                    if (Integer.parseInt(array[0]) < 10) {
//                        kickDay.setText("0" + array[0]);
//                    } else {
//                        kickDay.setText(array[0]);
//                    }
//                    kickHour.setText(array[1]);
//                    kickMin.setText(array[2]);
//                    kickSec.setText(array[3]);
//                }
//
//                public void onFinish() {
//                    kickDay.setVisibility(View.GONE);
//                    kickDayT.setVisibility(View.GONE);
//                    kickDay.setText("0");
//                    kickHour.setText("00");
//                    kickMin.setText("00");
//                    kickSec.setText("00");
//                }
//            }.start();
//            //将此 countDownTimer 放入list.
//        } else {
//            kickDay.setVisibility(View.GONE);
//            kickDayT.setVisibility(View.GONE);
//            kickDay.setText("0");
//            kickHour.setText("00");
//            kickMin.setText("00");
//            kickSec.setText("00");
//        }
//    }
//
//    private SparseArray<CountDownTimer> countDownCounters = new SparseArray<>();
//    private ConstraintLayout pinNowTeam;
//    private ConstraintLayout groupTitleLL;
//
//    private void buildTeam(LinearLayout groupLL, List<AssemableTeam> couponInfoByMerchants) {
//        groupLL.removeAllViews();
//        pinNowTeam.setVisibility(View.VISIBLE);
//        if (couponInfoByMerchants != null && couponInfoByMerchants.size() > 0) {
//
//            if(couponInfoByMerchants.size()>3){
//                morePin.setVisibility(View.VISIBLE);
//                groupTitleLL.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PinWithShop2Dialog.newInstance().setData(kickGoods.assembleDTO, kickGoods.assembleDTO.id + "").setPinOnDialogClickListener(pinOnDialogClickListener)
//                                .show(((AppCompatActivity) context).getSupportFragmentManager(), "商品页弹出商家优惠券列表");
//                    }
//                });
//            }else {
//                morePin.setVisibility(View.GONE);
//                groupTitleLL.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//            }
//            List<AssemableTeam> cct = new ArrayList<>();
//            if (couponInfoByMerchants.size() > 4) {
//                cct.add(couponInfoByMerchants.get(0));
//                cct.add(couponInfoByMerchants.get(1));
//                cct.add(couponInfoByMerchants.get(2));
//                cct.add(couponInfoByMerchants.get(3));
//            } else {
//                cct.addAll(couponInfoByMerchants);
//            }
//
//
//            for (int i = 0; i < cct.size(); i++) {
//                View view = LayoutInflater.from(context).inflate(R.layout.mall_item_activity_group_goods, null);
//                final AssemableTeam item = cct.get(i);
//                CornerImageView groupIcon;
//                TextView groupName;
//                TextView groupTeamNumber;
//                TextView groupTimeLimit;
//                TextView groupButton;
//                groupIcon = (CornerImageView) view.findViewById(R.id.groupIcon);
//                groupName = (TextView) view.findViewById(R.id.groupName);
//                groupTeamNumber = (TextView) view.findViewById(R.id.groupTeamNumber);
//                groupTimeLimit = (TextView) view.findViewById(R.id.groupTimeLimit);
//                groupButton = (TextView) view.findViewById(R.id.groupButton);
//                View dividerN=view.findViewById(R.id.dividerN);
//                com.healthy.library.businessutil.GlideCopy.with(context)
//                        .load(item.memberFaceUrl)
//                        .placeholder(R.drawable.img_1_1_default2)
//                        .error(R.drawable.img_1_1_default)
//
//                        .into(groupIcon);
//                groupTeamNumber.setText(SpanUtils.getBuilder(context,"还差" ).setForegroundColor(Color.parseColor("#444444"))
//                        .append(item.remainderNum ).setForegroundColor(Color.parseColor("#F02846"))
//                        .append("人成团").setForegroundColor(Color.parseColor("#444444"))
//                        .create());
//                groupName.setText(item.memberNickName);
//                if (item.isMyTeam(context)) {
//                    groupButton.setText("邀请好友");
//                }else {
//                    groupButton.setText("去参团");
//                }
//                dividerN.setVisibility(View.VISIBLE);
//                if(i==cct.size()-1){
//                    dividerN.setVisibility(View.INVISIBLE);
//                }
//                item.realEndTime = kickGoods.assembleDTO.endTime;
//                item.regimentTimeLength = kickGoods.assembleDTO.regimentTimeLength;
//                item.regimentSize = kickGoods.assembleDTO.regimentSize;
//                CountDownTimer countDownTimer = countDownCounters.get(groupTimeLimit.hashCode());
//                checkTimeOut(countDownTimer, item, groupTimeLimit, true);
//
//                groupButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (item.isMyTeam(context)) {
//                            ARouter.getInstance()
//                                    .build(DiscountRoutes.DIS_GROUPDETAIL)
//                                    .withString("teamNum", item.teamNum)
//                                    .navigation();
//                        } else {
//                            KKGroupGoodsDialog.newInstance().setAssemableTeam(item)
//                                    .setGroupGoodsDialogClicker(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            if (pinOnDialogClickListener != null) {
//                                                pinOnDialogClickListener.onGoDialogClick(v, item);
//                                            }
//                                        }
//                                    }).show(((AppCompatActivity) context).getSupportFragmentManager(), "kkOkDialog");
//                        }
//
//                    }
//                });
//                groupLL.addView(view);
//            }
//
//
//        } else {
//            pinNowTeam.setVisibility(View.GONE);
//        }
//    }
//
//    private void checkTimeOut(CountDownTimer countDownTimer, AssemableTeam url, final TextView destview, boolean needtimer) {
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//            countDownTimer = null;
//        }
//        if (needtimer) {
//            Date startTime = null;
//            Date endTime = null;
//            try {
//                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.regimentTime);
//                endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.realEndTime);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            long nd = 1000 * url.regimentTimeLength * 60 * 60;//加入时间之后需要多少小时
//            long desorg = startTime.getTime() + nd;
//            long timer = startTime.getTime() + nd;
//            if (endTime != null && endTime.getTime() <= timer) {
//                timer = endTime.getTime();
//                desorg = endTime.getTime();
//            }
//            timer = timer - System.currentTimeMillis();
//
//            if (timer > 0) {
//                ////System.out.println("开始计时");
//                final long finalTimer = timer;
//                final long finalDesorg = desorg;
//                countDownTimer = new CountDownTimer(finalTimer, 1000) {
//                    public void onTick(long millisUntilFinished) {
//                        String array = DateUtils.getDistanceTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
//                        destview.setText("剩余" + array + "");
//                    }
//
//                    public void onFinish() {
//                        destview.setText("过期");
//                    }
//                }.start();
//                countDownCounters.put(destview.hashCode(), countDownTimer);
//            } else {
//                destview.setText("过期");
//            }
//        }
//
//    }
//}
