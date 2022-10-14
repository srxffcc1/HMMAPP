package com.health.faq.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.faq.R;
import com.healthy.library.model.FaqHotExpertFieldChose;
import com.healthy.library.routes.AppRoutes;

/**
 * @author Li
 * @date 2019/03/04 17:28
 * @des 商品列表
 */

public class TypeFullAdapter extends BaseQuickAdapter<FaqHotExpertFieldChose, BaseViewHolder> {

    public void setLocation(  String cityCode) {
        this.cityCode = cityCode;
    }

    public String adCode;

    public String adName;

    public String categoryNo;

    public String lat;

    public String lng;

    public String cityCode;

    public int width=100;

    public TypeFullAdapter() {
        this(R.layout.item_type_fieldfull);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private TypeFullAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FaqHotExpertFieldChose item) {
         ImageView head;
         TextView name;
         TextView tip;
//        head = (ImageView) helper.itemView.findViewById(R.id.head);
        name = (TextView) helper.itemView.findViewById(R.id.name);
//        tip = (TextView) helper.itemView.findViewById(R.id.tip);
//        TextView tv_distance=helper.itemView.findViewById(R.id.tv_distance);
//        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(((ConstraintLayout.LayoutParams) head.getLayoutParams()));
//        layoutParams.width = (int) TransformUtil.dp2px(mContext,width);
//        layoutParams.height = (int) TransformUtil.dp2px(mContext,width);
//        head.setLayoutParams(layoutParams);
//        com.healthy.library.businessutil.GlideCopy.with(mContext)
//                .load(item.iconPath)
//                .placeholder(R.drawable.img_690_260_default)
//                .error(R.drawable.img_690_260_default)
//
//                .into(head);
//        name.setText(item.expertCategoryName.replace("专家","")+"\n专家");
        name.setText(item.expertCategoryName.replace("专家","")+"");
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build(AppRoutes.MODULE_EXPERT_LEFT)
                        .withString("expertCategoryName",item.expertCategoryName+"")
                        .withString("expertCategoryNo",item.expertCategoryNo+"")
                        .withString("cityNo",cityCode)
                        .navigation();
            }
        });

    }
}
