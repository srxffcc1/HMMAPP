//package com.health.discount.activity;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.util.SparseArray;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.alibaba.android.arouter.facade.annotation.Autowired;
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.bumptech.glide.Glide;
//import com.health.discount.R;
//import com.health.discount.adapter.KickListAdapter;
//import com.health.discount.contract.KickListContract;
//import com.health.discount.presenter.KickListPresenter;
//import com.healthy.library.base.BaseActivity;
//import com.healthy.library.model.Kick;
//import com.healthy.library.routes.DiscountRoutes;
//import com.healthy.library.model.PageInfoEarly;
//import com.healthy.library.utils.DateUtils;
//import com.healthy.library.utils.TransformUtil;
//import com.healthy.library.widget.StatusLayout;
//import com.healthy.library.widget.TopBar;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 砍价列表
// */
//@Route(path = DiscountRoutes.DIS_KICKLIST)
//public class KickListActivity extends BaseActivity implements OnRefreshLoadMoreListener, KickListContract.View {
//    private com.healthy.library.widget.TopBar topBar;
//    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
//    private com.healthy.library.widget.StatusLayout layoutStatus;
//    private android.widget.GridLayout seeImgs;
//    private RecyclerView recycler;
//    private int currentPage=1;
//    private Map<String, Object> smap = new HashMap<>();
//    private KickListAdapter kickListAdapter;
//    private KickListPresenter kickListPresenter;
//
//    private List<Kick> usekickList=new ArrayList<>();
//    private List<Kick> nousekickList=new ArrayList<>();
//    @Autowired
//    String addressProvince;
//    @Autowired
//    String lng;
//    @Autowired
//    String lat;
//    private boolean isheadadd=false;
//    private SparseArray<CountDownTimer> countDownCounters=new SparseArray<>();
//
//    @Override
//    public void onSuccessGetTopKickList(List<Kick> kickList, PageInfoEarly pageInfoEarly) {
//        onSucessGetUseKickList(kickList,pageInfoEarly);
//
//    }
//
//    @Override
//    public void onSuccessGetKickList(List<Kick> kickList, PageInfoEarly pageInfoEarly) {
//        if(kickList!=null){
////            //System.out.println("成功返回");
////            for (int i = 0; i <kickList.size() ; i++) {
////                Kick kick=kickList.get(i);
////                if(kick.joinStatus==1){
////                    usekickList.add(kick);
////                }else {
////                    nousekickList.add(kick);
////                }
////            }
////            onSucessGetUseKickList(usekickList,pageInfoEarly);
//            onSucessGetNoUseKickList(kickList,pageInfoEarly);
//        }
//    }
//
//    private void onSucessGetNoUseKickList(List<Kick> usekickList, PageInfoEarly pageInfoEarly) {
//        if (pageInfoEarly == null) {
////            showEmpty();
//            layoutRefresh.setEnableLoadMore(false);
//            return;
//        }
//        currentPage = pageInfoEarly.currentPage;
//        if (currentPage == 1) {
//            isheadadd=false;
//            kickListAdapter.setNewData(usekickList);
//            if (usekickList.size() == 0) {
//                recycler.setVisibility(View.GONE);
//            }else {
//                recycler.setVisibility(View.VISIBLE);
//            }
//        } else {
//            kickListAdapter.addData(usekickList);
//        }
//        if(!isheadadd){
//            addHeadView();
//            isheadadd=true;
//        }
//        if (pageInfoEarly.isMore != 1) {
//            layoutRefresh.finishLoadMoreWithNoMoreData();
//        } else {
//            layoutRefresh.setNoMoreData(false);
//            layoutRefresh.setEnableLoadMore(true);
//        }
//    }
//
//    private void addHeadView() {
//        kickListAdapter.removeAllHeaderView();
//        kickListAdapter.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.dis_item_activity_disact_single_top2,null));
//        kickListAdapter.removeAllFooterView();
//        kickListAdapter.addFooterView(LayoutInflater.from(mContext).inflate(R.layout.dis_item_activity_disact_single_bottom2,null));
//    }
//
//    private int mMargin;
//    private int mCornerRadius;
//    private void addImages(final Context context, final GridLayout gridLayout, final List<Kick> urls) {
//        if (mMargin == 0) {
//            mMargin = (int) TransformUtil.dp2px(mContext, 2);
//            mCornerRadius = (int) TransformUtil.dp2px(mContext, 3);
//        }
//        //System.out.println("展示分格");
//        if(urls!=null&&urls.size()>0){
//            gridLayout.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    int row = 1;
//                    int w = (gridLayout.getWidth()-gridLayout.getPaddingLeft()-gridLayout.getPaddingRight() - (2 + 2 * (row - 1)) * mMargin) / row;
//                    for (int i = 0; i < urls.size(); i++) {
//                        final Kick url = urls.get(i);
//                        final int pos = i;
//                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                        params.width = w;
////                        params.height = (int) (w * 0.75f);
//                        params.setMargins(mMargin, mMargin, mMargin, mMargin);
//
//                        View imageparent= LayoutInflater.from(mContext).inflate(R.layout.dis_item_activity_disact_top,gridLayout,false);
//                        final TextView hour=imageparent.findViewById(R.id.kickHour);
//                        final TextView min=imageparent.findViewById(R.id.kickMin);
//                        final TextView sec=imageparent.findViewById(R.id.kickSec);
//
//
//                        CountDownTimer countDownTimer = countDownCounters.get(url.hashCode());
//                        Date startTime= null;
//                        Date endTime= null;
//                        try {
//                            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.joinTime);
//                            endTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.endTime);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        long nd = 1000 * url.bargainTimeLength * 60 * 60;//加入时间之后需要多少小时
//                        long desorg = startTime.getTime()+nd;
//                        long timer = startTime.getTime()+nd;
//                        if(endTime!=null&&endTime.getTime()<timer){
//                            timer=endTime.getTime();
//                            desorg=endTime.getTime();
//                        }
//                        timer=timer-System.currentTimeMillis();
//                        if(countDownTimer!=null){
//                            countDownTimer.cancel();
//                            countDownTimer=null;
//                        }
//
//                        if (timer >0) {
//                            final long finalTimer = timer;
//                            final long finalDesorg = desorg;
//                            countDownTimer = new CountDownTimer(finalTimer, 1000) {
//                                public void onTick(long millisUntilFinished) {
//                                    String[] array= DateUtils.getDistanceTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),DateUtils.formatLongAll(finalDesorg +""));
//
////                                    String array= DateUtils.getDistanceTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),DateUtils.formatLongAll(finalDesorg +""));
//                                    hour.setText(array[1]);
//                                    min.setText(array[2]);
//                                    sec.setText(array[3]);
////                                    //System.out.println("结束时间:"+array);
//                                }
//                                public void onFinish() {
////                                    setText(holder,R.id.timeTv,"过期");
//                                }
//                            }.start();
//                            //将此 countDownTimer 放入list.
//                            countDownCounters.put(url.hashCode(), countDownTimer);
//                        } else {
////                            setText(holder,R.id.timeTv,"过期");
//                        }
//
//                        ProgressBar goodsPro = (ProgressBar) imageparent.findViewById(R.id.goodsJoinNum);
//                        int bili= (int) (url.discountMoney*1.0/(url.goodsPlatformPrice-url.floorPrice)*1000);
//                        goodsPro.setProgress(bili);
//
//
//
//
//                        TextView goodsProFFL=imageparent.findViewById(R.id.goodsProFFL);
//                        TextView goodsProFFR=imageparent.findViewById(R.id.goodsProFFR);
//                        LinearLayout.LayoutParams layoutParamsl= (LinearLayout.LayoutParams) goodsProFFL.getLayoutParams();
//                        LinearLayout.LayoutParams layoutParamsr= (LinearLayout.LayoutParams) goodsProFFR.getLayoutParams();
//                        layoutParamsl.weight=bili;
//                        layoutParamsr.weight=1000-bili;
//                        goodsProFFL.setLayoutParams(layoutParamsl);
//                        goodsProFFR.setLayoutParams(layoutParamsr);
//
//                        ImageView goodsIcon=imageparent.findViewById(R.id.goodsIcon);
//                        com.healthy.library.businessutil.GlideCopy.with(goodsIcon.getContext())
//                                .asBitmap()
//                                .load(url.goodsImage)
//                                .placeholder(R.drawable.img_avatar_default)
//                                .error(R.drawable.img_avatar_default)
//                                
//                                .into(goodsIcon);
//
//                        TextView goodsButton=imageparent.findViewById(R.id.goodsButton);
//
//                        goodsButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                ARouter.getInstance()
//                                        .build(DiscountRoutes.DIS_KICKDETAIL)
//                                        .withString("bargainMemberId",url.bargainMemberId)
//                                        .withString("bargainId",url.id)
//                                        .navigation();
//                            }
//                        });
//
//                        TextView goodsName=imageparent.findViewById(R.id.goodsName);
//                        goodsName.setText(url.goodsTitle);
//                        TextView goodsDis=imageparent.findViewById(R.id.goodsDis);
//                        goodsDis.setText("￥"+url.discountMoney+"");
//                        gridLayout.addView(imageparent, params);
//                    }
//                }
//            },1);
//        }
//
//
//    }
//
//    private void onSucessGetUseKickList(List<Kick> usekickList, PageInfoEarly pageInfoEarly) {
//        currentPage = pageInfoEarly.currentPage;
//        if (currentPage == 1) {
//            seeImgs.removeAllViews();
//            addImages(mContext,seeImgs,usekickList);
//        } else {
//            addImages(mContext,seeImgs,usekickList);
//        }
//    }
//
//    @Override
//    protected void findViews() {
//        super.findViews();
//        initView();
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.dis_activity_disactlist;
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        ARouter.getInstance().inject(this);
//        kickListPresenter=new KickListPresenter(mContext,this);
//        kickListAdapter=new KickListAdapter();
//        recycler.setLayoutManager(new LinearLayoutManager(mContext));
//        kickListAdapter.bindToRecyclerView(recycler);
//        smap.put("currentPage",currentPage+"");
//        smap.put("pageSize",10+"");
//        smap.put("addressProvince",addressProvince+"");
//        smap.put("longitude",lng+"");
//        smap.put("latitude",lat+"");
//        layoutRefresh.setOnRefreshLoadMoreListener(this);
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        nousekickList.clear();
//        usekickList.clear();
//        currentPage=1;
//        smap.put("currentPage",currentPage+"");
//        getData();
//
//    }
//
//    @Override
//    public void getData() {
//        super.getData();
//        kickListPresenter.getKickList(smap);
//    }
//
//    @Override
//    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//        nousekickList.clear();
//        currentPage++;
//        smap.put("currentPage",currentPage+"");
//        getData();
//    }
//
//    @Override
//    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        nousekickList.clear();
//        usekickList.clear();
//        currentPage=1;
//        smap.put("currentPage",currentPage+"");
//        getData();
//    }
//
//    @Override
//    public void onRequestFinish() {
//        super.onRequestFinish();
//        layoutRefresh.finishRefresh();
//        layoutRefresh.finishLoadMore();
//    }
//
//    private void initView() {
//        topBar = (TopBar) findViewById(R.id.top_bar);
//        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
//        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
//        seeImgs = (GridLayout) findViewById(R.id.see_imgs);
//        recycler = (RecyclerView) findViewById(R.id.recycler);
//    }
//}
