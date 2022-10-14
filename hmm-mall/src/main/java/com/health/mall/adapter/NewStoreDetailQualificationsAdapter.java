package com.health.mall.adapter;

import android.text.Html;
import android.text.TextUtils;
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
import com.health.mall.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;

public class NewStoreDetailQualificationsAdapter extends BaseAdapter<ShopDetailModel> {


    public NewStoreDetailQualificationsAdapter() {
        this(R.layout.new_store_detial_qualifications_layout);
    }

    public NewStoreDetailQualificationsAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        GridLayout qualificationsGrid = holder.getView(R.id.qualificationsGrid);
        TextView qualificationsContent = holder.getView(R.id.qualificationsContent);
        if (getModel().shopIntroduce != null && !TextUtils.isEmpty(getModel().shopIntroduce)) {
            qualificationsContent.setText("门店介绍：" + Html.fromHtml(getModel().shopIntroduce));
        } else {
            qualificationsContent.setText("暂无门店介绍");
        }
        if (getModel().businessLicensePicUrl != null && !TextUtils.isEmpty(getModel().businessLicensePicUrl)) {
            String[] urlArray = getModel().businessLicensePicUrl.split(",");
            addFunctions(qualificationsGrid, urlArray);
        }
    }

    private void addFunctions(final GridLayout gridLayout, final String[] urls) {
        gridLayout.removeAllViews();
        int row = 2;
        int mMargin = (int) TransformUtil.dp2px(context, 40);
        ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
        gridlayoutparm.width = ((ScreenUtils.getScreenWidth(context) - mMargin) / 2) * row;
        gridLayout.setLayoutParams(gridlayoutparm);
        gridLayout.setColumnCount(row);
        int w = ((ScreenUtils.getScreenWidth(context) - mMargin) / 2);
        for (int i = 0; i < urls.length; i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = w;
            View view = LayoutInflater.from(context).inflate(R.layout.item_qualifications_grid_layout, gridLayout, false);
            ImageView qualificationsImg = view.findViewById(R.id.qualificationsImg);
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(urls[i])
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(qualificationsImg);
            final int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                            .withCharSequenceArray("urls", urls)
                            .withInt("pos", pos)
                            .navigation();
                }
            });
            gridLayout.addView(view, params);
        }
    }
}
