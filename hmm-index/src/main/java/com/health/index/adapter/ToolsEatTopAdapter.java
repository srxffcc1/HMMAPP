package com.health.index.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.health.index.R;
import com.healthy.library.model.ToolsCETopMenu;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.IndexRoutes;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class ToolsEatTopAdapter extends BaseAdapter<ToolsCETopMenu> {

    boolean needMore = true;

    String categoryId;

    public void setEatStage(String audioType) {
        this.eatStage = audioType;
    }

    String eatStage;

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setNeedMore(boolean needMore) {
        this.needMore = needMore;
    }

    public ToolsEatTopAdapter() {
        this(R.layout.index_activity_tools_item_caneat_main_top);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {

        final ToolsCETopMenu toolsFoodTopMenu= getDatas().get(position);
         ConstraintLayout eatBg;
         ImageView eatImg;
         TextView eatTxt;
        eatBg = (ConstraintLayout) holder.itemView.findViewById(R.id.eatBg);
        eatImg = (ImageView) holder.itemView.findViewById(R.id.eatImg);
        eatTxt = (TextView) holder.itemView.findViewById(R.id.eatTxt);
        eatBg.setBackgroundResource(toolsFoodTopMenu.getBgRes());
        eatTxt.setText(toolsFoodTopMenu.name);

        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(toolsFoodTopMenu.getIconRes())
                .placeholder(R.drawable.img_avatar_default)
                .error(R.drawable.img_avatar_default)

                .into(eatImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eatStageKey="";
                String eatStageValue="";
                String nowtitle="";
                if("1".equals(eatStage)){
                    nowtitle="孕期";
                    eatStageKey="pregnantStatus";
                }
                if("2".equals(eatStage)){
                    nowtitle="坐月子";
                    eatStageKey="puerperaStatus";

                }
                if("3".equals(eatStage)){
                    nowtitle="哺乳期";
                    eatStageKey="sucklingPeriodStatus";

                }
                if("4".equals(eatStage)){
                    nowtitle="婴幼儿";
                    eatStageKey="babyStatus";
                }
                if("放心吃".equals(toolsFoodTopMenu.name)){
                    eatStageValue="1";
                }
                if("少点吃".equals(toolsFoodTopMenu.name)){
                    eatStageValue="2";

                }
                if("谨慎吃".equals(toolsFoodTopMenu.name)){
                    eatStageValue="3";

                }
                if("不能吃".equals(toolsFoodTopMenu.name)){
                    eatStageValue="4";

                }
                nowtitle=nowtitle+toolsFoodTopMenu.name;
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_CANEAT_TYPE)
                        .withString("eatStage",eatStage)
                        .withString("nowtitle",nowtitle)
                        .withString("activityType","1")
                        .withString("eatStageKey",eatStageKey)
                        .withString("eatStageValue",eatStageValue)
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

    private ToolsEatTopAdapter(int layoutResId) {
        super(layoutResId);

    }

    private void initView() {


    }
}
