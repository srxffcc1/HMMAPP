package com.health.index.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.index.R;
import com.healthy.library.model.ToolsFoodList;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class ToolsFoodListAdapter extends BaseAdapter<ToolsFoodList> {


    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    public String recipeType;


    public ToolsFoodListAdapter() {
        this(R.layout.index_activity_tools_item_food_list);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final ToolsFoodList toolsFoodList = getDatas().get(position);

        CornerImageView toolsFoodImg;
        TextView toolsFoodTitle;
        TextView toolsFoodTip;
        TextView toolsFoodContent;
        ImageTextView toolsFoodSee;
        View hasContent;
        View noMsgCon;


        hasContent = (View) holder.itemView.findViewById(R.id.hasContent);

        noMsgCon = (View) holder.itemView.findViewById(R.id.noMsgCon);


        toolsFoodImg = (CornerImageView) holder.itemView.findViewById(R.id.tools_food_img);
        toolsFoodTitle = (TextView) holder.itemView.findViewById(R.id.tools_food_title);
        toolsFoodTip = (TextView) holder.itemView.findViewById(R.id.tools_food_tip);
        toolsFoodContent = (TextView) holder.itemView.findViewById(R.id.tools_food_content);
        toolsFoodSee = (ImageTextView) holder.itemView.findViewById(R.id.tools_food_see);


        hasContent.setVisibility(View.VISIBLE);
        noMsgCon.setVisibility(View.VISIBLE);

        if (toolsFoodList != null) {
            hasContent.setVisibility(View.VISIBLE);
            noMsgCon.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    ARouter.getInstance()
                            .build(IndexRoutes.INDEX_TOOLS_FOOD_DETAIL)
                            .withString("id", toolsFoodList.id + "")
                            .withString("foodName", toolsFoodList.foodName + "")
                            .withString("foodPractice", toolsFoodList.foodPractice + "")
                            .withString("recipeImg", toolsFoodList.image + "")
                            .withString("recipeType", recipeType + "")
                            .navigation();
                }
            });
            toolsFoodTip.setVisibility(View.GONE);
            if (toolsFoodList.isCurrentArea == 1) {
                toolsFoodTip.setVisibility(View.VISIBLE);
            }
            toolsFoodContent.setText(toolsFoodList.effect);
            if (TextUtils.isEmpty(toolsFoodList.effect)) {
                toolsFoodContent.setText("");
            }
            toolsFoodTitle.setText(toolsFoodList.foodName);
            if (!TextUtils.isEmpty(toolsFoodList.virtualView)) {
                toolsFoodSee.setText((toolsFoodList.virtualView) + "浏览量");
                if (!TextUtils.isEmpty(toolsFoodList.realView)) {
                    toolsFoodSee.setText((Integer.parseInt(toolsFoodList.virtualView) + Integer.parseInt(toolsFoodList.realView)) + "浏览量");
                }
            } else {
                if (!TextUtils.isEmpty(toolsFoodList.realView)) {
                    toolsFoodSee.setText((toolsFoodList.realView) + "浏览量");
                }
            }


            if (TextUtils.isEmpty(toolsFoodList.realView) && TextUtils.isEmpty(toolsFoodList.virtualView)) {
                toolsFoodSee.setText(0 + "浏览量");
            }
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(toolsFoodList.image)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(toolsFoodImg);
        } else {
            hasContent.setVisibility(View.GONE);
            noMsgCon.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private ToolsFoodListAdapter(int layoutResId) {
        super(layoutResId);

    }

    private void initView() {


    }
}
