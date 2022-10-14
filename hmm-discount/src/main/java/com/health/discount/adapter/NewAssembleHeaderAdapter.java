package com.health.discount.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.OnEventFucListener;
import com.healthy.library.model.KKGroup;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewAssembleHeaderAdapter extends BaseAdapter<ArrayList<KKGroup>> {
    //    CountDownTimer countDownTimer;
    private SparseArray<CountDownTimer> countDownCounters = new SparseArray<>();
    OnEventFucListener onEventFucListener;



    public void setOnEventFucListener(OnEventFucListener onEventFucListener) {
        this.onEventFucListener = onEventFucListener;
    }

    public NewAssembleHeaderAdapter() {
        this(R.layout.new_assemble_header_goods_layout);
    }

    public NewAssembleHeaderAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }


    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        List<KKGroup> list = getDatas().get(0);
        LinearLayout headerGoodsLiner = holder.getView(R.id.headerGoodsLiner);
        headerGoodsLiner.removeAllViews();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                View view = null;
                List<KKGroup.TeamMemberList> teamMemberLists = list.get(i).teamMemberList;
                final KKGroup kick = list.get(i);
                CountDownTimer countDownTimer = countDownCounters.get(kick.hashCode());
//                if (list.get(i).goodsType == 1) {
//                    LayoutInflater inflater = LayoutInflater.from(context);
//                    view = inflater.inflate(R.layout.new_assemble_top_service_layout, null);
//                    GridLayout functionGrid = view.findViewById(R.id.pingtuaun_top_grid);
//                    CornerImageView goodsImg = view.findViewById(R.id.goodsImg);
//                    TextView kickHour = view.findViewById(R.id.kickHour);
//                    TextView kickMin = view.findViewById(R.id.kickMin);
//                    TextView kickSec = view.findViewById(R.id.kickSec);
//                    com.healthy.library.businessutil.GlideCopy.with(context)
//                            .load(kick.goodsImage)
//                            .error(R.drawable.img_1_1_default)
//                            .placeholder(R.drawable.img_1_1_default2)
//                            .into(goodsImg);
//                    com.healthy.library.businessutil.GlideCopy.with(context).load(kick.goodsImage)
//                            .placeholder(R.drawable.img_1_1_default)
//                            .error(R.drawable.img_1_1_default)
//                            
//                            .into(new SimpleTarget<Drawable>() {
//
//                                @Override
//                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                    kick.bitmapShre = DrawableUtils.drawableToBitmap(resource);
//                                }
//                            });
//
//
//                    checkTimeOut(countDownTimer, kick, kickHour, kickMin, kickSec);//倒计时
//                    if (teamMemberLists != null) {
//                        addFunctions(context, functionGrid, teamMemberLists, kick.regimentSize);
//                    }
//                } else {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.new_assemble_top_goods_layout, null);
                GridLayout functionGrid = view.findViewById(R.id.pingtuaun_top_grid);
                CornerImageView goodsImg = view.findViewById(R.id.goodsImg);
                TextView kickHour = view.findViewById(R.id.kickHour);
                TextView kickMin = view.findViewById(R.id.kickMin);
                TextView kickSec = view.findViewById(R.id.kickSec);
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(kick.goodsImage)
                        .error(R.drawable.img_1_1_default)
                        .placeholder(R.drawable.img_1_1_default2)
                        .into(goodsImg);
                com.healthy.library.businessutil.GlideCopy.with(context).load(kick.goodsImage)
                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default)
                        
                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                kick.bitmapShre = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
                checkTimeOut(countDownTimer, kick, kickHour, kickMin, kickSec);//倒计时
                if (teamMemberLists != null) {
                    addFunctions(context, functionGrid, teamMemberLists, kick.regimentSize);
                }
                //}
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_GROUPDETAIL)
                                .withString("orderId", kick.orderId + "")
                                .withString("teamNum", kick.teamNum)
                                .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                                .navigation();
                    }
                });
                final String lessman = (kick.regimentSize - kick.teamMemberList.size()) + "";
                view.findViewById(R.id.share_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onActShareClickListener != null) {
                            onActShareClickListener.clickShare(kick, lessman, kick.bitmapShre);
                        }
                    }
                });
                headerGoodsLiner.addView(view);
            }
        }
    }

    public void setOnActShareClickListener(OnActShareClickListener onActShareClickListener) {
        this.onActShareClickListener = onActShareClickListener;
    }

    OnActShareClickListener onActShareClickListener;

    public interface OnActShareClickListener {
        public void clickShare(KKGroup kkGroupDetail, String lessman, Bitmap bitmap);
    }

    private void addFunctions(final Context context, final GridLayout gridLayout, final List<KKGroup.TeamMemberList> list, int regimentSize) {
        gridLayout.removeAllViews();
        int row = 0;
        if (regimentSize > 6) {
            row = 6;
        } else {
            row = regimentSize;
        }
        int width = 0;//gridLayout宽度
        int imgHeight = 0;//头像宽高
        switch (row) {
            case 1:
            case 2:
                width = 120;
                imgHeight = 44;
                break;
            case 3:
            case 4:
                width = 160;
                imgHeight = 36;
                break;
            case 5:
            case 6:
                width = 200;
                imgHeight = 30;
                break;
        }

        int maxWidth = (int) TransformUtil.dp2px(context, width);
        ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
        gridlayoutparm.width = ((maxWidth) / row) * row;
        gridLayout.setLayoutParams(gridlayoutparm);
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(row);
        int w = ((maxWidth) / row);
        for (int i = 0; i < row; i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = w;
            View view = LayoutInflater.from(context).inflate(R.layout.item_pingtuan_grid_layout, gridLayout, false);
            TextView tv_category = view.findViewById(R.id.tv_category);
            CornerImageView iv_category = view.findViewById(R.id.iv_category);
            ViewGroup.LayoutParams imgParams = iv_category.getLayoutParams();
            imgParams.height = (int) TransformUtil.dp2px(context, imgHeight);
            imgParams.width = (int) TransformUtil.dp2px(context, imgHeight);
            iv_category.setLayoutParams(imgParams);
            if (i < list.size()) {
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(list.get(i).memberFaceUrl)
                        .placeholder(R.mipmap.default_pingtuan_img)
                        .error(R.mipmap.default_pingtuan_img)
                        
                        .into(iv_category);
            } else {
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(R.mipmap.default_pingtuan_img)
                        .placeholder(R.mipmap.default_pingtuan_img)
                        .error(R.mipmap.default_pingtuan_img)
                        
                        .into(iv_category);
            }
            if (i == 0) {
                tv_category.setVisibility(View.VISIBLE);
            } else {
                tv_category.setVisibility(View.GONE);
            }
            gridLayout.addView(view, params);
        }
    }

    private void checkTimeOut(CountDownTimer countDownTimer, KKGroup url, final TextView kickHour, final TextView kickMin, final TextView kickSec) {
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.joinTime);
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long nd = 1000 * url.regimentTimeLength * 60 * 60;//加入时间之后需要多少小时
        long desorg = startTime.getTime() + nd;
        long timer = startTime.getTime() + nd;
        if (endTime != null && endTime.getTime() < timer) {
            timer = endTime.getTime();
            desorg = endTime.getTime();
        }
        timer = timer - System.currentTimeMillis();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (timer > 0) {
            //System.out.println("开始计时");
            final long finalTimer = timer;
            final long finalDesorg = desorg;
            countDownTimer = new CountDownTimer(finalTimer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] array = DateUtils.getDistanceTimeNoDay(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
                    kickHour.setText(array[0]);
                    kickMin.setText(array[1]);
                    kickSec.setText(array[2]);
                }

                public void onFinish() {
                    kickHour.setText("00");
                    kickMin.setText("00");
                    kickSec.setText("00");
                }
            }.start();
            countDownCounters.put(url.hashCode(), countDownTimer);
            //将此 countDownTimer 放入list
        } else {
            kickHour.setText("00");
            kickMin.setText("00");
            kickSec.setText("00");
        }
    }
}
