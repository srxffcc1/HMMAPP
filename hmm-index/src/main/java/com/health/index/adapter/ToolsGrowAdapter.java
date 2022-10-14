package com.health.index.adapter;

import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;

public class ToolsGrowAdapter extends BaseQuickAdapter<String, BaseViewHolder> {



    public ToolsGrowAdapter() {
        this(R.layout.index_tools_growline);
    }

    private ToolsGrowAdapter(int layoutResId) {
        super(layoutResId);

    }

    @Override
    public int getItemViewType(int position) {
        return 2;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
         ConstraintLayout parentCategory;
         TextView tvCategory;
        parentCategory = (ConstraintLayout) helper.itemView.findViewById(R.id.parent_category);
        tvCategory = (TextView) helper.itemView.findViewById(R.id.tv_category);
        tvCategory.setText(item);
        if (helper.getPosition() > 2) {
            parentCategory.setBackgroundResource(R.drawable.shape_tools_weight_tmp_white);
        }else {
            if(helper.getPosition()==0){

                parentCategory.setBackgroundResource(R.drawable.shape_tools_weight_tmp_yellow_left);
            }else if((helper.getPosition()+1)%3==0){
                parentCategory.setBackgroundResource(R.drawable.shape_tools_weight_tmp_yellow_right);
            }else {
                parentCategory.setBackgroundResource(R.drawable.shape_tools_weight_tmp_yellow_center);

            }

        }
    }

    private void initView() {

    }
}
