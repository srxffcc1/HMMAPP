package com.health.discount.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.discount.R;
import com.health.discount.model.LiveListModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;

import java.util.List;

public class LiveListAdapter extends BaseAdapter<LiveListModel> {
    private ClickListener clickListener;

    public void setClickListener(ClickListener outClickListener) {
        this.clickListener = outClickListener;
    }

    public LiveListAdapter() {
        this(R.layout.item_live_list_adapter_layout);
    }

    public LiveListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
//        CornerImageView goodsImg = holder.getView(R.id.goodsImg);
//        TextView liveTitle = holder.getView(R.id.liveTitle);
//        TextView liveShopName = holder.getView(R.id.liveShopName);
//        TextView goSee = holder.getView(R.id.goSee);
//        TextView remindTxt = holder.getView(R.id.remindTxt);
//        ImageView bgImg = holder.getView(R.id.bgImg);
//        ImageView liveingImg = holder.getView(R.id.liveingImg);
//        ImageView liveingAfter = holder.getView(R.id.liveingAfter);
//        ImageView liveingHistory = holder.getView(R.id.liveingHistory);
//        GridLayout liveListGrid = holder.getView(R.id.liveListGrid);
//        LinearLayout liveListGoodsLin = holder.getView(R.id.liveListGoodsLin);
//        final LiveListModel model = getDatas().get(position);
//        if (model != null) {
//            liveTitle.setText(model.courseName != null ? model.courseName : "");
//            com.healthy.library.businessutil.GlideCopy.with(context)
//                    .load(model.picUrl)
//                    .error(R.drawable.img_1_1_default)
//                    .placeholder(R.drawable.img_1_1_default)
//                    .into(goodsImg);
//            liveShopName.setText(model.merchantName != null ? model.merchantName : "");
//            if (position == 0) {
//                bgImg.setVisibility(View.VISIBLE);
//            } else {
//                bgImg.setVisibility(View.GONE);
//            }
//            if (model.flag == 1) {//预约
//                liveingImg.setVisibility(View.GONE);
//                liveingAfter.setVisibility(View.VISIBLE);
//                liveingHistory.setVisibility(View.GONE);
//                remindTxt.setVisibility(View.VISIBLE);
//                goSee.setVisibility(View.INVISIBLE);
//                if (model.isAppointment == 0) {
//                    remindTxt.setText("预约");
//                    remindTxt.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (clickListener != null) {
//                                clickListener.outClick("remind", model.courseId + "", null);
//                            }
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
//            } else if (model.flag == 2) {//直播中
//                liveingImg.setVisibility(View.VISIBLE);
//                liveingAfter.setVisibility(View.GONE);
//                liveingHistory.setVisibility(View.GONE);
//                remindTxt.setVisibility(View.INVISIBLE);
//                goSee.setVisibility(View.VISIBLE);
//            } else if (model.flag == 3) {//回放
//                liveingImg.setVisibility(View.GONE);
//                liveingAfter.setVisibility(View.GONE);
//                liveingHistory.setVisibility(View.VISIBLE);
//                remindTxt.setVisibility(View.INVISIBLE);
//                goSee.setVisibility(View.VISIBLE);
//            }
//            if (model.goodsPicUrlList != null && model.goodsPicUrlList.size() > 0) {
//                liveListGoodsLin.setVisibility(View.VISIBLE);
//                addItems(liveListGrid, model.goodsPicUrlList);
//            } else {
//                liveListGoodsLin.setVisibility(View.GONE);
//            }
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (clickListener != null) {
//                        clickListener.outClick("live", model.courseId + "", model.flag + "");
//                    }
//                }
//            });
//        }
    }

    public interface ClickListener {
        void outClick(String function, String data1, String data2);
    }

    private void addItems(final GridLayout gridLayout, final List<String> urls) {
        gridLayout.removeAllViews();
        int row = 3;
        int mMargin = (int) TransformUtil.dp2px(context, 210);
        ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
        gridlayoutparm.width = ((ScreenUtils.getScreenWidth(context) - mMargin) / 3) * row;
        gridLayout.setLayoutParams(gridlayoutparm);
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
