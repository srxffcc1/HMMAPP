package com.health.servicecenter.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.model.CategoryModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MallStoreGridAdapter extends BaseAdapter<ArrayList<CategoryModel>> {
    RelativeLayout topGrid;

    public MallStoreGridAdapter(int viewId) {
        super(viewId);
    }


    public MallStoreGridAdapter() {
        this(R.layout.mall_top_grid_layout);
    }

    public void setBackground(String color) {
        if (topGrid != null) {
            topGrid.setBackgroundColor(Color.parseColor(color));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        GridLayout functionGrid = holder.getView(R.id.mall_top_grid);
        topGrid = holder.getView(R.id.topGrid);
        addFunctions(context, functionGrid, getDatas().get(0));
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private void addFunctions(final Context context, final GridLayout gridLayout, final List<CategoryModel> urls) {
        gridLayout.removeAllViews();
        {
            int row = 5;
            int mMargin = (int) TransformUtil.dp2px(context, 40);
            ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
            gridlayoutparm.width = ((ScreenUtils.getScreenWidth(context) - mMargin) / 5) * row;
            gridLayout.setLayoutParams(gridlayoutparm);
            gridLayout.removeAllViews();
            gridLayout.setColumnCount(row);
            int w = ((ScreenUtils.getScreenWidth(context) - mMargin) / 5);
            for (int i = 0; i < urls.size(); i++) {
                final CategoryModel indexMenu = urls.get(i);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = w;
                View view = LayoutInflater.from(context).inflate(R.layout.item_mall_grid_layout, gridLayout, false);
                TextView tv_category = view.findViewById(R.id.tv_category);

                if (i == urls.size() - 1) {
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(R.mipmap.index_service_more)
                            .placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)
                            
                            .into((ImageView) view.findViewById(R.id.iv_category));
                    tv_category.setText("更多分类");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map nokmap = new HashMap<String, String>();
                            nokmap.put("soure", "母婴商品页“更多分类”导航菜单");
                            MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_Classification", nokmap);
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_SORT)
                                    .navigation();
                        }
                    });
                } else {
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(indexMenu.getFilePath())
                            .placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)
                            
                            .into((ImageView) view.findViewById(R.id.iv_category));
                    tv_category.setText(indexMenu.getCategoryName());
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map nokmap = new HashMap<String, String>();
                            nokmap.put("soure", "母婴商品页各一级分类导航菜单");
                            MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_FirstClass", nokmap);
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_SERVICESORTGOODS)
                                    .withString("categoryId", indexMenu.getCategoryId() + "")
                                    .withString("categoryName", indexMenu.getCategoryName())
                                    .navigation();
                        }
                    });
                }

                gridLayout.addView(view, params);
            }
        }

    }
}
