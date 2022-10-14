package com.health.discount.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.discount.R;
import com.health.discount.model.PointTab;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.model.TabChangeModel;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.ImageTextView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ActZPointVideoAdapter extends BaseAdapter<String> {


    @Override
    public int getItemViewType(int position) {
        return 18;
    }

    public ActZPointVideoAdapter() {
        this(R.layout.dis_item_fragment_pointvideo);
    }

    private ActZPointVideoAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        LinearLayout ponit2videoPLL;
        LinearLayout pointPLL;
        ImageTextView pointTitle;
        GridLayout pointGrid;
        LinearLayout pointHeadIconLL;
        TextView pointMenber;
        LinearLayout videoPLL;
        ImageTextView videoTitle;
        GridLayout videoGrid;
        LinearLayout videoHeadIconLL;
        TextView videoMenber;
        ponit2videoPLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.ponit2videoPLL);
        pointPLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.pointPLL);
        pointTitle = (ImageTextView) baseHolder.itemView.findViewById(R.id.pointTitle);
        pointGrid = (GridLayout) baseHolder.itemView.findViewById(R.id.pointGrid);
        pointHeadIconLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.pointHeadIconLL);
        pointMenber = (TextView) baseHolder.itemView.findViewById(R.id.pointMenber);
        videoPLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.videoPLL);
        videoTitle = (ImageTextView) baseHolder.itemView.findViewById(R.id.videoTitle);
        videoGrid = (GridLayout) baseHolder.itemView.findViewById(R.id.videoGrid);
        videoHeadIconLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.videoHeadIconLL);
        videoMenber = (TextView) baseHolder.itemView.findViewById(R.id.videoMenber);
        onSucessGetLiveList(ponit2videoPLL, pointPLL, videoPLL, pointGrid, videoGrid);
    }

    int mMargin;

    public void onSucessGetLiveList(LinearLayout ponit2videoPLL, LinearLayout pointPLL, LinearLayout videoPLL, GridLayout pointGrid, GridLayout videoGrid) {
        ponit2videoPLL.setVisibility(View.GONE);
        videoPLL.setVisibility(View.GONE);
        pointPLL.setVisibility(View.GONE);


        int row = 2;
        if (liveListModels != null && liveListModels.size() > 0 && pointGoodsList != null && pointGoodsList.size() > 0 && liveListModels.get(0).liveVideoGoodsList != null && liveListModels.get(0).liveVideoGoodsList.size() > 0) {
            ponit2videoPLL.setVisibility(View.VISIBLE);
            videoPLL.setVisibility(View.VISIBLE);
            pointPLL.setVisibility(View.VISIBLE);
            row = 2;
//            try {
//                videoTitle.setText(new SimpleDateFormat("MM-DD：HH").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(liveListModels.get(0).beginTime)));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        } else {
            if (liveListModels != null && liveListModels.size() > 0 && liveListModels.get(0).liveVideoGoodsList != null && liveListModels.get(0).liveVideoGoodsList.size() > 0) {
                ponit2videoPLL.setVisibility(View.VISIBLE);
                videoPLL.setVisibility(View.VISIBLE);
                row = 4;
//                try {
//                    videoTitle.setText(new SimpleDateFormat("MM-DD：HH").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(liveListModels.get(0).beginTime)));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
            if (pointGoodsList != null && pointGoodsList.size() > 0) {
                ponit2videoPLL.setVisibility(View.VISIBLE);
                pointPLL.setVisibility(View.VISIBLE);
                row = 4;
            }
        }


        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(context, 2);
        }
        if (pointGoodsList != null && pointGoodsList.size() > 0) {
            pointGrid.removeAllViews();
            pointGrid.setColumnCount(row);
            for (int i = 0; i < row; i++) {
                View viewparent = LayoutInflater.from(context).inflate(R.layout.dis_item_other_h, pointGrid, false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(
                            GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
                    viewparent.setLayoutParams(param);
                }
                ImageView goodsImg = viewparent.findViewById(R.id.goodsImg);
                PointTab.PointGoods pointGoods = null;
                try {
                    pointGoods = pointGoodsList.get(i);
                } catch (Exception e) {
//                    e.printStackTrace();
                }

                viewparent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MobclickAgent.onEvent(context, "event2APPShopHomePointsGoodsClick", new SimpleHashMapBuilder<String, String>().puts("soure","积分兑换商品图片点击量"));
                        ARouter.getInstance().build(DiscountRoutes.DIS_POINTHOME).navigation();
                    }
                });
                viewparent.setVisibility(View.VISIBLE);
                if (pointGoods == null) {
                    viewparent.setVisibility(View.INVISIBLE);
                } else {
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(pointGoods.filePath)
                            .placeholder(R.drawable.img_1_1_default)
                            .error(R.drawable.img_1_1_default)
                            .into(goodsImg);
                }
                pointGrid.addView(viewparent);
            }

        }
        if (liveListModels != null && liveListModels.size() > 0) {
            videoGrid.removeAllViews();
            videoGrid.setColumnCount(row);
            for (int i = 0; i < row; i++) {
                View viewparent = LayoutInflater.from(context).inflate(R.layout.dis_item_other_h, videoGrid, false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(
                            GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
                    viewparent.setLayoutParams(param);
                }
                ImageView goodsImg = viewparent.findViewById(R.id.goodsImg);
                String liveListModel = null;
                try {
                    liveListModel = liveListModels.get(0).liveVideoGoodsList.get(i).headImages.get(0).filePath;
                } catch (Exception e) {
//                    e.printStackTrace();
                }
                viewparent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new TabChangeModel(2));
//                        ARouter.getInstance()
//                                .build(TencentLiveRoutes.LiveNotice)
//                                .withString("courseId", liveListModels.get(0).id)
//                                .navigation();
//                        ARouter.getInstance().build(DiscountRoutes.DIS_LIVELIST).navigation();
                    }
                });
                if (liveListModel == null) {
                    viewparent.setVisibility(View.INVISIBLE);
                } else {
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(liveListModel)
                            .placeholder(R.drawable.img_1_1_default)
                            .error(R.drawable.img_1_1_default)
                            .into(goodsImg);
                }
                videoGrid.addView(viewparent);
            }

        }

    }

    List<PointTab.PointGoods> pointGoodsList;
    List<LiveVideoMain> liveListModels;

    public void setPonitAndVideo(List<PointTab.PointGoods> pointGoodsList, List<LiveVideoMain> liveListModels) {
        this.liveListModels = liveListModels;
        this.pointGoodsList = pointGoodsList;
    }
}
