package com.health.index.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.index.R;
import com.healthy.library.model.ToolsCETypeList;
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

public class ToolsEatTypeListListAdapter extends BaseAdapter<ToolsCETypeList> {

    boolean needMore = true;

    String categoryId;



    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setNeedMore(boolean needMore) {
        this.needMore = needMore;
    }

    public ToolsEatTypeListListAdapter() {
        this(R.layout.index_activity_tools_item_caneat_type_list);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {

        final ToolsCETypeList toolsCETypeList = getDatas().get(position);
         CornerImageView ivCategory;
         TextView toolsEatName;
         TextView toolsEatContent;

        ivCategory = (CornerImageView) holder.itemView.findViewById(R.id.iv_category);
        toolsEatName = (TextView) holder.itemView.findViewById(R.id.toolsEatName);
        toolsEatContent = (TextView)holder.itemView. findViewById(R.id.toolsEatContent);
        ImageTextView toolEatTip1;
        ImageTextView toolEatTip2;
        ImageTextView toolEatTip3;
        ImageTextView toolEatTip4;
        toolEatTip1 = (ImageTextView) holder.itemView.findViewById(R.id.toolEatTip1);
        toolEatTip2 = (ImageTextView) holder.itemView.findViewById(R.id.toolEatTip2);
        toolEatTip3 = (ImageTextView)holder.itemView. findViewById(R.id.toolEatTip3);
        toolEatTip4 = (ImageTextView) holder.itemView.findViewById(R.id.toolEatTip4);
        toolsEatName.setText(toolsCETypeList.foodName);
        toolsEatContent.setVisibility(View.VISIBLE);
        toolsEatContent.setText(toolsCETypeList.foodAliasName);
        if(TextUtils.isEmpty(toolsCETypeList.foodAliasName)){
            toolsEatContent.setVisibility(View.GONE);

        }
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(toolsCETypeList.image)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default2)
                
                .into(ivCategory);

        toolEatTip1.setDrawable(toolsCETypeList.getCanEatImgRes("1"),context);
        toolEatTip2.setDrawable(toolsCETypeList.getCanEatImgRes("2"),context);
        toolEatTip3.setDrawable(toolsCETypeList.getCanEatImgRes("3"),context);
        toolEatTip4.setDrawable(toolsCETypeList.getCanEatImgRes("4"),context);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result=toolsCETypeList.getCanEatStringRes();
                String urlPrefix = SpUtils.getValue(context, UrlKeys.H5_CAN_EAT);
                String url = String.format("%s?id=%s&foodName=%s&scheme=CanEatDetail&foodId=%s", urlPrefix,toolsCETypeList.id+"", URLEncoder.encode(toolsCETypeList.foodName),toolsCETypeList.id+"");
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                        .withString("title", toolsCETypeList.foodName)
                        .withString("url", url)
                        .withBoolean("needShare",true)
                        .withBoolean("isinhome",true)
                        .withBoolean("doctorshop",true)
                        .withString("zbitmap",toolsCETypeList.image)
                        .withString("stitle","能不能吃-"+toolsCETypeList.foodName)
                        .withString("sdes", result)
                        .navigation();
            }
        });

    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private ToolsEatTypeListListAdapter(int layoutResId) {
        super(layoutResId);

    }

    private void initView() {



    }
}
