package com.health.tencentlive.adapter;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.tencentlive.R;
import com.healthy.library.LibApplication;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.business.LivePassWordDialog;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveMainVideoAdapter extends BaseAdapter<LiveVideoMain> {


    @Override
    public int getItemViewType(int position) {
        return 77;
    }

    public LiveMainVideoAdapter() {
        this(R.layout.item_t_live_list_adapter_layout);
    }

    private LiveMainVideoAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int position) {
        final LiveVideoMain model = getDatas().get(position);
        ImageView bgImg;
        CornerImageView goodsImg;
        ImageView liveingImg;
        ImageView liveingAfter;
        ImageView liveingHistory;
        TextView liveTitle;
        CornerImageView liveManIcon;
        TextView liveHoster;
        TextView liveHosterEpc;
        TextView remindTxt;
        TextView goSee;
        LinearLayout liveListGoodsLin;
        GridLayout liveListGrid;
        bgImg = (ImageView) baseHolder.itemView.findViewById(R.id.bgImg);
        goodsImg = (CornerImageView) baseHolder.itemView.findViewById(R.id.goodsImg);
        liveingImg = (ImageView) baseHolder.itemView.findViewById(R.id.liveingImg);
        liveingAfter = (ImageView) baseHolder.itemView.findViewById(R.id.liveingAfter);
        liveingHistory = (ImageView) baseHolder.itemView.findViewById(R.id.liveingHistory);
        liveTitle = (TextView) baseHolder.itemView.findViewById(R.id.liveTitle);
        liveManIcon = (CornerImageView) baseHolder.itemView.findViewById(R.id.liveManIcon);
        liveHoster = (TextView) baseHolder.itemView.findViewById(R.id.liveHoster);
        liveHosterEpc = (TextView) baseHolder.itemView.findViewById(R.id.liveHosterEpc);
        remindTxt = (TextView) baseHolder.itemView.findViewById(R.id.remindTxt);
        goSee = (TextView) baseHolder.itemView.findViewById(R.id.goSee);
        liveListGoodsLin = (LinearLayout) baseHolder.itemView.findViewById(R.id.liveListGoodsLin);
        liveListGrid = (GridLayout) baseHolder.itemView.findViewById(R.id.liveListGrid);
        TextView liveIntroduce = (TextView) baseHolder.itemView.findViewById(R.id.liveIntroduce);
        liveIntroduce.setVisibility(View.GONE);
        TextView liveFlag = (TextView) baseHolder.itemView.findViewById(R.id.liveFlag);
        liveFlag.setVisibility(View.GONE);
        if (model.type == 2) {
            liveFlag.setVisibility(View.VISIBLE);
            liveFlag.setText("私密");
            liveFlag.setBackgroundResource(R.drawable.shape_live_flag_pink);
        } else if (model.type == 3) {
            liveFlag.setVisibility(View.VISIBLE);
            liveFlag.setText("付费");
            liveFlag.setBackgroundResource(R.drawable.shape_live_flag_blue);
        }

        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(model.picUrl)

                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)
                .into(goodsImg);
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(model.liveAnchorman.avatarUrl)

                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)
                .into(liveManIcon);
        liveTitle.setText(model.courseTitle);
        liveHoster.setText(model.liveAnchorman.memberName);
        if (model.liveVideoGoodsList != null) {
            liveIntroduce.setVisibility(View.GONE);
        } else {//只要没商品列表  就显示
            liveIntroduce.setVisibility(View.VISIBLE);
            liveIntroduce.setText(model.courseIntroduce);
        }
//        if(model.isBringGoods==0){  这里判断有问题  有可能出现是直播带货但是没加商品
//            liveIntroduce.setVisibility(View.VISIBLE);
//            liveIntroduce.setText(model.courseIntroduce);
//        }
        if (model != null) {
            if (position == 0) {
                bgImg.setVisibility(View.GONE);
            } else {
                bgImg.setVisibility(View.GONE);
            }
            if (model.status == 1) {//预约
                liveingImg.setVisibility(View.GONE);
                liveingAfter.setVisibility(View.VISIBLE);
                liveingHistory.setVisibility(View.GONE);
                remindTxt.setVisibility(View.GONE);
                goSee.setVisibility(View.INVISIBLE);
//                if (model.isAppointment == 0) {
//                    remindTxt.setText("预约");
//                    remindTxt.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
//                } else {
//                    remindTxt.setText("已预约");
//                    remindTxt.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                        }
//                    });
//                }
            } else if (model.status == 2) {//直播中
                liveingImg.setVisibility(View.VISIBLE);
                liveingAfter.setVisibility(View.GONE);
                liveingHistory.setVisibility(View.GONE);
                remindTxt.setVisibility(View.INVISIBLE);
                goSee.setVisibility(View.VISIBLE);
            } else {//回放
                liveingImg.setVisibility(View.GONE);
                liveingAfter.setVisibility(View.GONE);
                liveingHistory.setVisibility(View.VISIBLE);
                remindTxt.setVisibility(View.INVISIBLE);
                goSee.setVisibility(View.VISIBLE);
            }
        }
        if (model.liveVideoGoodsList != null) {
            addItems(liveListGrid, new SimpleArrayListBuilder<String>().putList(model.liveVideoGoodsList, new ObjectIteraor<LiveVideoGoods>() {
                @Override
                public String getDesObj(LiveVideoGoods o) {
                    return o.headImages.get(0).filePath;
                }
            }));
        } else {
            liveListGrid.removeAllViews();
        }

        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "直播商品图片的点击量");
                MobclickAgent.onEvent(context, "event2APPShopHomeLiveGoodsClick", nokmap);
                if (model.status == 1) {//预告

                    ARouter.getInstance()
                            .build(TencentLiveRoutes.LiveNotice)
                            .withString("courseId", model.id)
                            .navigation();
                } else {
                    if ((model.status == 2 || model.status == 3) && new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)).equals(model.liveAnchorman.memberId)) {
                        Toast.makeText(context, "当前直播异常断播，已正常续播", Toast.LENGTH_LONG).show();
                        //从列表尝试看能不能续播
                        ARouter.getInstance()
                                .build(TencentLiveRoutes.LIVE_PUSH)
                                .withString("pushAddress", model.pushAddress)
                                .withString("groupId", model.groupId)
                                .withString("courseId", model.id)
                                .withString("anchormanId", model.anchormanId)
                                .withObject("activityIdList", model.activityIdList)
                                .navigation();
                        return;
                    }
                    if (model.type == 2 && !SpUtils.getValue(LibApplication.getAppContext(), model.id + "Pass", false)) {
                        LivePassWordDialog.newInstance()
                                .setLivePassWordListener(new LivePassWordDialog.LivePassWordListener() {
                                    @Override
                                    public void onPassSucess() {
                                        ((BaseActivity) context).stopOnlineVideoFloat();
                                        ARouter.getInstance()
                                                .build(TencentLiveRoutes.LIVE_LOOK)
                                                .withString("courseId", model.id)
                                                .navigation();
                                    }
                                })
                                .setCourseId(model.id)
                                .show(((BaseActivity) context).getSupportFragmentManager(), "私密支付");
                    } else {
                        ((BaseActivity) context).stopOnlineVideoFloat();
                        ARouter.getInstance()
                                .build(TencentLiveRoutes.LIVE_LOOK)
                                .withString("courseId", model.id)
                                .navigation();
                    }

                }


            }
        });

    }

    private void addItems(final GridLayout gridLayout, final List<String> urls) {
        gridLayout.removeAllViews();
        int row = 3;
        int mMargin = (int) TransformUtil.dp2px(context, 190);
        gridLayout.setColumnCount(row);
        int w = ((ScreenUtils.getScreenWidth(context) - mMargin) / 3);
        for (int i = 0; i < (urls.size() > 3 ? 3 : urls.size()); i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = w;
            View view = LayoutInflater.from(context).inflate(R.layout.item_t_live_list_grid_layout, gridLayout, false);
            ImageView liveMoreImg = view.findViewById(R.id.liveMoreImg);
            CornerImageView itemGoodsImg = view.findViewById(R.id.itemGoodsImg);
            if (i == 2 && urls.size() > 3) {//大于三个商品才展示更多
                liveMoreImg.setVisibility(View.VISIBLE);
            } else {
                liveMoreImg.setVisibility(View.GONE);
            }
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(urls.get(i))
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(itemGoodsImg);
            gridLayout.addView(view, params);
        }
    }

}
