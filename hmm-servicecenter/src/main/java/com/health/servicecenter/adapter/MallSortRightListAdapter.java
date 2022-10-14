package com.health.servicecenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.model.CategoryChildModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;

import java.util.List;

public class MallSortRightListAdapter extends BaseAdapter<CategoryChildModel> {
    public MallSortRightListAdapter() {
        this(R.layout.mall_sort_right_list_layout);
    }

    public MallSortRightListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        GridLayout functionGrid = holder.getView(R.id.mall_top_grid);
        TextView mall_top_title = holder.getView(R.id.mall_top_title);
        mall_top_title.setText(getModel().getCategoryName());
        if (getModel().getGoodsCategorys() != null) {
            addFunctions(context, functionGrid, getModel().getGoodsCategorys());
        }
    }

    private void addFunctions(final Context context, final GridLayout gridLayout, final List<CategoryChildModel.GoodsCategorysBean> urls) {
        gridLayout.removeAllViews();
        {
            int row = 3;
            int mMargin = (int) TransformUtil.dp2px(context, 120);
            ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
            gridlayoutparm.width = ((ScreenUtils.getScreenWidth(context) - mMargin) / 3) * row;
            gridLayout.setLayoutParams(gridlayoutparm);
            gridLayout.removeAllViews();
            gridLayout.setColumnCount(row);
            int w = ((ScreenUtils.getScreenWidth(context) - mMargin) / 3);
            for (int i = 0; i < urls.size(); i++) {
                final CategoryChildModel.GoodsCategorysBean indexMenu = urls.get(i);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = w;
                View view = LayoutInflater.from(context).inflate(R.layout.item_sort_grid_layout, gridLayout, false);
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(indexMenu.getFilePath())
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)
                        
                        .into((ImageView) view.findViewById(R.id.iv_category));
                TextView tv_category = view.findViewById(R.id.tv_category);
                tv_category.setText(indexMenu.getCategoryName());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_SERVICESORTGOODS)
                                .withString("categoryId",indexMenu.getId()+"")
                                .withString("categoryName",indexMenu.getCategoryName())
                                .navigation();
                    }
                });
                gridLayout.addView(view, params);
            }
        }

    }
}
