package com.health.index.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.health.index.R;
import com.healthy.library.model.ToolsCEList;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;

import java.net.URLEncoder;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class ToolsEatListAdapter extends BaseAdapter<ToolsCEList> {

    boolean needMore = true;

    String categoryId;


    public void setFragmentType(String fType) {
        this.fType = fType;
    }

    String fType;

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setNeedMore(boolean needMore) {
        this.needMore = needMore;
    }

    public ToolsEatListAdapter() {
        this(R.layout.index_activity_tools_item_caneat_main_list);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {

        final ToolsCEList toolsFoodList= getDatas().get(position);
         CornerImageView ivCategory;
         TextView tvTip;
         ImageTextView tvCategory;
        ivCategory = (CornerImageView) holder.itemView.findViewById(R.id.iv_category);
        tvTip = (TextView) holder.itemView.findViewById(R.id.tv_tip);
        tvCategory = (ImageTextView) holder.itemView.findViewById(R.id.tv_category);

        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(toolsFoodList.image)
                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)
                
                .into(ivCategory);
        if(toolsFoodList.isCurrentArea==0){
            tvTip.setVisibility(View.GONE);
        }else {
            tvTip.setVisibility(View.VISIBLE);
        }
        tvCategory.setText(toolsFoodList.foodName);
        tvCategory.setDrawable(toolsFoodList.getCanEatImgRes(fType),context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result=toolsFoodList.getCanEatStringRes();
                String urlPrefix = SpUtils.getValue(context, UrlKeys.H5_CAN_EAT);
                String url = String.format("%s?id=%s&foodName=%s&scheme=CanEatDetail&foodId=%s", urlPrefix,toolsFoodList.id+"", URLEncoder.encode(toolsFoodList.foodName),toolsFoodList.id+"");
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                        .withString("title", toolsFoodList.foodName)
                        .withString("url", url)
                        .withBoolean("needShare",true)
                        .withBoolean("isinhome",true)
                        .withBoolean("doctorshop",true)
                        .withString("zbitmap",toolsFoodList.image)
                        .withString("stitle","能不能吃-"+toolsFoodList.foodName)
                        .withString("sdes", result)
                        .navigation();
            }
        });

    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setAutoExpand(false);
        return gridLayoutHelper;
    }

    private ToolsEatListAdapter(int layoutResId) {
        super(layoutResId);

    }

    private void initView() {


    }
}
