package com.health.mine.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.mine.R;
import com.health.mine.model.JobType;
import com.healthy.library.routes.MineRoutes;

/**
 * @author Li
 * @date 2019/03/04 17:28
 * @des 商品列表
 */

public class JobTypeAdapter extends BaseQuickAdapter<JobType, BaseViewHolder> {



    public JobTypeAdapter() {
        this(R.layout.mine_item_job_type2);
    }


    private JobTypeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final JobType item) {
         ImageView jobTypeIcon;
         TextView jobTypeName;
        jobTypeIcon = (ImageView) helper.getView(R.id.jobTypeIcon);
        jobTypeName = (TextView) helper.getView(R.id.jobTypeName);

        com.healthy.library.businessutil.GlideCopy.with(jobTypeIcon.getContext())
                .load(item.imgUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                
                .into(jobTypeIcon);
        jobTypeName.setText(item.typeName+"");
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(MineRoutes.MINE_JOB_WANT)
                        .withString("technicianTypeId",item.id+"")
                        .withString("technicianTypeName",item.typeName+"")
                        .navigation();
            }
        });

    }

}
