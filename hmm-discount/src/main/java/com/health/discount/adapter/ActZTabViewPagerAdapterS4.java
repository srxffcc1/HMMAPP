package com.health.discount.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.health.discount.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.business.LivePassWordDialog;
import com.healthy.library.widget.StaggeredGridLayoutHelperFix;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActZTabViewPagerAdapterS4 extends BaseAdapter<MultiItemEntity> {
    private ClickListener clickListener;
    public void setClickListener(ClickListener outClickListener) {
        this.clickListener = outClickListener;
    }

    public interface ClickListener {
        void outClick(String function, String data1, String data2);
    }
    @Override
    public int getItemViewType(int position) {
        return 27;
    }
    public ActZTabViewPagerAdapterS4() {
        this(R.layout.item_live_list_adapter_layout);

    }
    private ActZTabViewPagerAdapterS4(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StaggeredGridLayoutHelperFix helper = new StaggeredGridLayoutHelperFix();
        helper.setMargin(6, 0, 6, 0);
        helper.setLane(1);
        helper.setHGap(3);
        return helper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int position) {
        final LiveVideoMain model = (LiveVideoMain) getDatas().get(position);
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
        TextView liveIntroduce=(TextView) baseHolder.itemView.findViewById(R.id.liveIntroduce);
        liveIntroduce.setVisibility(View.GONE);

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
        if(model.isBringGoods==0){
            liveIntroduce.setVisibility(View.VISIBLE);
            liveIntroduce.setText(model.courseIntroduce);
        }
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
        }else {
            liveListGrid.removeAllViews();
        }

        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "直播商品图片的点击量");
                MobclickAgent.onEvent(context, "直播商品图片的点击量", nokmap);
                if (model.status == 1) {//预告
//                    if (!TextUtils.isEmpty(SpUtils.getValue(context, SpKey.LIVEHOSTID))&&model.anchormanId.equals(SpUtils.getValue(context, SpKey.LIVEHOSTID))) {// 当前为主播
//                            ARouter.getInstance()
//                                    .build(TencentLiveRoutes.LIVE_CREATE)
//                                    .withString("courseId", model.id)
//                                    .navigation();
//                    } else {
//                        ARouter.getInstance()
//                                .build(TencentLiveRoutes.LiveNotice)
//                                .withString("courseId", model.id)
//                                .navigation();
//                    }
                    ARouter.getInstance()
                            .build(TencentLiveRoutes.LiveNotice)
                            .withString("courseId", model.id)
                            .navigation();
                } else {
                    ((BaseActivity)context).stopOnlineVideoFloat();
                    if (model.type == 2) {
                        LivePassWordDialog.newInstance()
                                .setLivePassWordListener(new LivePassWordDialog.LivePassWordListener() {
                                    @Override
                                    public void onPassSucess() {
                                        ARouter.getInstance()
                                                .build(TencentLiveRoutes.LIVE_LOOK)
                                                .withString("courseId", model.id)
                                                .navigation();
                                    }
                                })
                                .setCourseId(model.id)
                                .show(((BaseActivity) context).getSupportFragmentManager(), "私密支付");
                    } else {
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_live_list_grid_layout, gridLayout, false);
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
