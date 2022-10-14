package com.health.index.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.index.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.message.FoodDialogInfoOpenMsg;
import com.healthy.library.widget.ImageTextView;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class ToolsFoodTitleAdapter extends BaseAdapter<String> {

    boolean needMore = true;

    String categoryId;

    public void setEffectid(String effectid) {
        this.effectid = effectid;
    }

    public String effectid="不限";

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String foodid="不限";

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }

    String audioType;

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setNeedMore(boolean needMore) {
        this.needMore = needMore;
    }

    public ToolsFoodTitleAdapter() {
        this(R.layout.index_activity_tools_item_food_main_title);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
         ImageTextView toolRecipeEffect;
         ImageTextView toolRecipeFood;
        toolRecipeEffect = (ImageTextView) holder.itemView.findViewById(R.id.toolRecipeEffect);
        toolRecipeFood = (ImageTextView) holder.itemView.findViewById(R.id.toolRecipeFood);
        toolRecipeEffect.setVisibility(View.VISIBLE);
        toolRecipeFood.setVisibility(View.VISIBLE);
        toolRecipeEffect.setText(effectid);
        if("不限".equals(effectid)){
            toolRecipeEffect.setText("功效");
        }
        toolRecipeFood.setText(foodid);
        if("不限".equals(foodid)){
            toolRecipeFood.setText("食材");
        }
        if ("1".equals(recipeType)) {//月子食谱
            toolRecipeEffect.setVisibility(View.GONE);
            toolRecipeFood.setVisibility(View.GONE);
        }
        toolRecipeEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new FoodDialogInfoOpenMsg(effectid,1));
            }
        });
        toolRecipeFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new FoodDialogInfoOpenMsg(foodid,2));

            }
        });
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private ToolsFoodTitleAdapter(int layoutResId) {
        super(layoutResId);

    }

    private void initView() {


    }

    String recipeType;

    public void setRecipeType(String recipeType) {
        this.recipeType=recipeType;
    }
}
