package com.health.index.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.healthy.library.model.ToolsSumType;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.DateUtils;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class ToolsDayTimeListMainAdapter extends BaseQuickAdapter<ToolsSumType, BaseViewHolder> {


    public String childId;

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public ToolsDayTimeListMainAdapter() {
        this(R.layout.index_activity_tools_diary_main_item);
    }

    public ToolsDayTimeListMainAdapter(int viewId) {
        super(viewId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ToolsSumType item) {
         ImageView toolsMenuIcon;
         TextView toolsMenuTip;
         TextView toolsMenuTime;
        toolsMenuIcon = (ImageView)helper.itemView. findViewById(R.id.tools_menu_icon);
        toolsMenuTip = (TextView) helper.itemView. findViewById(R.id.tools_menu_tip);
        toolsMenuTime = (TextView) helper.itemView. findViewById(R.id.tools_menu_time);
        toolsMenuIcon.setImageResource(item.getImageLeftRes());
        toolsMenuTip.setText(item.getLeftTitle());
        toolsMenuTime.setText(DateUtils.getClassDateToolMain(DateUtils.formatLongSecAll(item.getTimeleft(),"yyyy-MM-dd HH:mm:ss")));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int despostion=0;
                if(item.recordType<=3){
                    despostion=0;
                }else if(item.recordType<5){

                    despostion=1;
                }else if(item.recordType<7){

                    despostion=2;
                }else {

                    despostion=3;
                }
                if(childId!=null){
                    ARouter.getInstance()
                            .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY_SUM)
                            .withString("childId",childId)
                            .withInt("postion",despostion)
                            .navigation();
                }
            }
        });
    }

    private void initView() {

    }
}
