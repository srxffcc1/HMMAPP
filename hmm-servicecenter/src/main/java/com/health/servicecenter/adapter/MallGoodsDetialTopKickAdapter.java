//package com.health.servicecenter.adapter;
//
//import android.graphics.Paint;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager.widget.ViewPager;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.google.android.flexbox.FlexboxLayout;
//import com.health.servicecenter.R;
//import com.healthy.library.model.Goods2DetailKick;
//import com.healthy.library.model.GoodsDetail;
//import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
//import com.healthy.library.base.BaseActivity;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseHolder;
//import com.healthy.library.fragment.EmptyLayoutFragment;
//import com.healthy.library.fragment.PicFragment;
//import com.healthy.library.fragment.VideoFragment;
//import com.healthy.library.routes.LibraryRoutes;
//import com.healthy.library.builder.SimpleHashMapBuilder;
//import com.healthy.library.utils.DateUtils;
//import com.healthy.library.utils.FormatUtils;
//import com.healthy.library.utils.ParseUtils;
//import com.healthy.library.utils.StringUtils;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class MallGoodsDetialTopKickAdapter extends BaseAdapter<GoodsDetail> {
//
//    private List<Fragment> fragments = new ArrayList<>();
//    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
//    private VideoFragment videoFragment;
//
//    boolean hasvideo = false;
//    boolean isShowVideo=true;
//    OnTopLoadMoreListener onTopLoadMoreListener;
//
//    private int currentPageScrollIndex = -1;
//    private float currentPageScrollMove = -1;
//    private Goods2DetailKick kickGoods;
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
//    public MallGoodsDetialTopKickAdapter() {
//        this(R.layout.service_item_goodsdetail_top_kick);
//    }
//
//    private MallGoodsDetialTopKickAdapter(int viewId) {
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
//        buildKickView(baseHolder);
//    }
//
//    private void buildKickView(@NonNull BaseHolder baseHolder) {
//        final ViewPager img_banner = baseHolder.itemView.findViewById(R.id.img_banner);
//        final View fpLL = baseHolder.itemView.findViewById(R.id.fpLL);
//        final TextView nowPage;
//        TextView fpTime=baseHolder.itemView.findViewById(R.id.fpTime);
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
//
//        TextView pinMoney;
//        TextView pinMoneySingle;
//        TextView pinMoneyTeamNum;
//        TextView kickDay;
//        TextView kickHour;
//        TextView kickMin;
//        TextView kickSec;
//        TextView kickDayT;
//
//        pinMoney = (TextView) baseHolder.itemView.findViewById(R.id.pinMoney);
//        pinMoneySingle = (TextView) baseHolder.itemView.findViewById(R.id.pinMoneySingle);
//        pinMoneyTeamNum = (TextView) baseHolder.itemView.findViewById(R.id.pinMoneyTeamNum);
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
//
//        kickDay = (TextView) baseHolder.itemView.findViewById(R.id.kickDay);
//        kickHour = (TextView) baseHolder.itemView.findViewById(R.id.kickHour);
//        kickMin = (TextView) baseHolder.itemView.findViewById(R.id.kickMin);
//        kickSec = (TextView) baseHolder.itemView.findViewById(R.id.kickSec);
//        kickDayT = (TextView) baseHolder.itemView.findViewById(R.id.kickDayT);
//
//
//        goodMoneyValue2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        goodMoneyValue2.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.getStorePrice()));
//        if(goodsSpecDetail.getStorePrice()==0){
//            goodMoneyValue2.setVisibility(View.GONE);
//        }else {
//            goodMoneyValue2.setVisibility(View.GONE);
//        }
//        goodMoneyValue.setText("" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.platformPrice));
//        goodssalecount.setText("已售"+(ParseUtils.parseNumber((goodsSpecDetail.appSales+goodsSpecDetail.virtualSales)+"",10000,"万"))+"件");
//        goodsTitle.setText(goodsSpecDetail.goodsTitle);
//        fragments.clear();
//        if(currentPageScrollIndex==0){
//            fpLL.setVisibility(View.VISIBLE);
//        }
//        goodsSpecDetail.change();
//
//
//        pinMoney.setText(StringUtils.getResultPrice(kickGoods.bargainInfoDTO.floorPrice+""));
//        pinMoneySingle.setText("原价￥"+StringUtils.getResultPrice(kickGoods.goodsDetails.platformPrice+""));
//        pinMoneyTeamNum.setText("可砍至");
//        checkTimeOutKick(kickGoods,kickDay,kickDayT,kickHour,kickHour,kickSec);
//
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
//                    int secondall=Integer.parseInt(headImage.imageDescription.split("\\.")[0]);
//                    String min=(int)(secondall/60)+"′";
//                    String second=(int)(secondall%60)+"″";
//                    fpTime.setText(min+second);
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
//                final String[] array=new String[goodsSpecDetail.headImages.size()];
//                for (int j = 0; j <array.length ; j++) {
//                    array[j]=goodsSpecDetail.headImages.get(j).filePath;
//
//                }
//                ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
//                        .withCharSequenceArray("urls", array)
//                        .withInt("pos", index)
//                        .navigation();
//            }
//        });
//        img_banner.setOffscreenPageLimit(fragments.size());
//        fpLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                isShowVideo=false;
////                fpLL.setVisibility(View.GONE);
//                final String[] array=new String[goodsSpecDetail.headImages.size()];
//                for (int j = 0; j <array.length ; j++) {
//                    array[j]=goodsSpecDetail.headImages.get(j).filePath;
//
//                }
//                ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
//                        .withCharSequenceArray("urls", array)
//                        .withInt("pos", 0)
//                        .navigation();
//            }
//        });
//        if (!hasvideo) {
//            fpLL.setVisibility(View.GONE);
//        }
//        allPage.setText(fragments.size()-1 + "");
//        img_banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                currentPageScrollIndex = position;
//                currentPageScrollMove = positionOffset;
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (hasvideo) {
//                    if (position > 0) {
//                        fpLL.setVisibility(View.GONE);
//                    } else {
//                        if(isShowVideo){
//
//                            fpLL.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//                if (position == fragments.size() - 1) {
//                    img_banner.setCurrentItem(position - 1);
//                }else {
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
//                    if (currentPageScrollIndex == (fragments.size() - 1-1) && currentPageScrollMove > 0.25) {
//                        //查看商品图文详情
//                        img_banner.setCurrentItem(fragments.size() - 1-1);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                onTopLoadMoreListener.onTopLoadMore();
//                            }
//                        },100);
//
//
//                    }else if(currentPageScrollIndex == (fragments.size() - 1)){
//                        //查看商品图文详情
//
//                        img_banner.setCurrentItem(fragments.size() - 1-1);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                onTopLoadMoreListener.onTopLoadMore();
//                            }
//                        },100);
//                    }
//                }
//                return false;
//            }
//        });
//    }
//
//    public void setKickGoods(Goods2DetailKick kickGoods) {
//        this.kickGoods = kickGoods;
//    }
//
//    public Goods2DetailKick getKickGoods() {
//        return kickGoods;
//    }
//
//    public interface OnTopLoadMoreListener {
//        void onTopLoadMore();
//    }
//
//    CountDownTimer countDownTimer;
//    private void checkTimeOutKick(Goods2DetailKick url, final TextView kickDay, final TextView kickDayT, final TextView kickHour, final TextView kickMin, final TextView kickSec) {
//
//        Date startTime= null;
//        Date endTime= null;
//        try {
//            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.bargainInfoDTO.startTime);
//            endTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.bargainInfoDTO.endTime);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        long desorg = startTime.getTime();
//        long timer = startTime.getTime();
//        if(endTime!=null){
//            timer=endTime.getTime();
//            desorg=endTime.getTime();
//        }
//        timer=timer-System.currentTimeMillis();
//        if(countDownTimer!=null){
//            countDownTimer.cancel();
//            countDownTimer=null;
//        }
//        kickDay.setVisibility(View.VISIBLE);
//        kickDayT.setVisibility(View.VISIBLE);
//        if (timer >0) {
//            ////System.out.println("开始计时");
//            final long finalTimer = timer;
//            final long finalDesorg = desorg;
//            countDownTimer = new CountDownTimer(finalTimer, 1000) {
//                public void onTick(long millisUntilFinished) {
//                    String[] array= DateUtils.getDistanceTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),DateUtils.formatLongAll(finalDesorg +""));
//                    if("0".equals(array[0])){//0天
//                        kickDay.setVisibility(View.GONE);
//                        kickDayT.setVisibility(View.GONE);
//                    }
//                    kickDay.setText(array[0]);
//                    kickHour.setText(array[1]);
//                    kickMin.setText(array[2]);
//                    kickSec.setText(array[3]);
//                }
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
//}
