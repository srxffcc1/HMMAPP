package com.health.index.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.legacy.widget.Space;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.index.R;
import com.healthy.library.model.ToolsFoodTop;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.JsoupCopy;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.ImageTextView;

import java.net.URLEncoder;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class ToolsFoodTopAdapter extends BaseAdapter<ToolsFoodTop> {

    String recipeType;

    String recipeDateString;

    String recipeDateStringShare;

    public String getSug() {
        return sug;
    }

    public String sug;

    public void setRecipeDateStringShare(String recipeDateStringShare) {
        this.recipeDateStringShare = recipeDateStringShare;
    }

    public void setRecipeDate(String recipeDateString) {
        this.recipeDateString = recipeDateString;
    }
    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    public ToolsFoodTopAdapter() {
        this(R.layout.index_activity_tools_item_food_top);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final ToolsFoodTop toolsFoodTop=getModel();
         ConstraintLayout hasContent;
         ImageView bgImg;
         Space imgSpace;
         Space imgSpace2;
         ImageView floatImg1;
         ImageView floatImg2;
         ConstraintLayout toolsBgLfet;
         LinearLayout toolsFoodTitleLL;
         TextView toolsFoodTitleDiv;
         TextView toolsFoodKey;
         TextView toolsFoodValue;
         TextView toolFoodValue2;
        ImageTextView toolSeeAll;

        toolSeeAll=(ImageTextView) holder.itemView.findViewById(R.id.toolSeeAll);
        hasContent = (ConstraintLayout) holder.itemView.findViewById(R.id.hasContent);
        bgImg = (ImageView)holder.itemView. findViewById(R.id.bgImg);
        imgSpace = (Space)holder.itemView. findViewById(R.id.imgSpace);
        imgSpace2 = (Space) holder.itemView.findViewById(R.id.imgSpace2);
        floatImg1 = (ImageView) holder.itemView.findViewById(R.id.floatImg1);
        floatImg2 = (ImageView) holder.itemView.findViewById(R.id.floatImg2);
        toolsBgLfet = (ConstraintLayout)holder.itemView. findViewById(R.id.tools_bgLfet);
        toolsFoodTitleLL = (LinearLayout) holder.itemView.findViewById(R.id.tools_food_titleLL);
        toolsFoodTitleDiv = (TextView)holder.itemView. findViewById(R.id.tools_food_title_div);
        toolsFoodKey = (TextView) holder.itemView.findViewById(R.id.tools_food_key);
        toolsFoodValue = (TextView) holder.itemView.findViewById(R.id.tools_food_value);
        toolFoodValue2 = (TextView) holder.itemView.findViewById(R.id.tool_food_value2);
        toolsFoodValue.setText(toolsFoodTop.eatWords);
        toolFoodValue2.setText(com.healthy.library.utils.JsoupCopy.parse(toolsFoodTop.detail).text());
        toolsFoodTitleDiv.setText(recipeDateString);
        if("1".equals(recipeType)){//月子食谱
            toolsFoodTitleLL.setBackgroundResource(R.drawable.shape_tools_food_b);
            bgImg.setImageResource(R.drawable.tools_food_bgb);
            floatImg2.setImageResource(R.drawable.tools_food_bgbb);
            toolsFoodTitleDiv.setText("月子"+recipeDateString);
            toolSeeAll.setTextColor(Color.parseColor("#2EB5F5"));
            toolsFoodValue.setTextColor(Color.parseColor("#2EB5F5"));
            toolSeeAll.setDrawable(R.drawable.tools_b_more,context);
            toolSeeAll.setBackgroundResource(R.drawable.shape_tools_food_bb);
        }else if("2".equals(recipeType)){//宝宝辅食
            toolsFoodTitleLL.setBackgroundResource(R.drawable.shape_tools_food_y);
            bgImg.setImageResource(R.drawable.tools_food_bgy);
            floatImg2.setImageResource(R.drawable.tools_food_bgyy);

            toolSeeAll.setTextColor(Color.parseColor("#FDA232"));
            toolsFoodValue.setTextColor(Color.parseColor("#FDA232"));
            toolSeeAll.setDrawable(R.drawable.tools_y_more,context);
            toolSeeAll.setBackgroundResource(R.drawable.shape_tools_food_yy);

        }else {//孕期食谱
            toolsFoodTitleLL.setBackgroundResource(R.drawable.shape_tools_food_r);
            bgImg.setImageResource(R.drawable.tools_food_bgr);
            floatImg2.setImageResource(R.drawable.tools_food_bgrr);

            toolSeeAll.setTextColor(Color.parseColor("#FF6266"));
            toolsFoodValue.setTextColor(Color.parseColor("#FF6266"));
            toolSeeAll.setDrawable(R.drawable.tools_r_more,context);
            toolSeeAll.setBackgroundResource(R.drawable.shape_tools_food_rr);

        }
        sug=JsoupCopy.parse(toolsFoodTop.detail).text();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlPrefix = SpUtils.getValue(context, UrlKeys.H5_dietProposeUrl);
                String url = String.format("%s?id=%s&scheme=FoodSuggest&title=%s", urlPrefix,toolsFoodTop.id+"", URLEncoder.encode(recipeDateStringShare));

                String zbitmap;
                if("1".equals(recipeType)){
                    zbitmap="R.drawable.index_share_humb_yzsp";
                }else if("2".equals(recipeType)){
                    zbitmap="R.drawable.index_share_humb_bbfs";

                }else {
                    zbitmap="R.drawable.index_share_humb_yqsp";
                }

                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                        .withString("title", recipeDateStringShare)
                        .withString("url", url)
                        .withBoolean("needShare",true)
                        .withBoolean("isinhome",true)
                        .withBoolean("doctorshop",true)
                        .withString("zbitmap",zbitmap)
                        .withString("stitle",""+recipeDateStringShare)
                        .withString("sdes", JsoupCopy.parse(toolsFoodTop.detail).text())
                        .navigation();
            }
        });


    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

     ToolsFoodTopAdapter(int layoutResId) {
        super(layoutResId);

    }

     void initView() {


    }
}
