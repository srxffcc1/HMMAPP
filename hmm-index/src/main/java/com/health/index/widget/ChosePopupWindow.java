package com.health.index.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.lib_ShapeView.builder.TextColorBuilder;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.health.index.R;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.routes.AppRoutes;

import java.util.List;

public class ChosePopupWindow extends PopupWindow {

    public ChosePopupWindow(final Context context, int width, List<UserInfoExModel> indexChosePops, final OnStatusClickListener clickListener) {
        super(context);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(width);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View mView = LayoutInflater.from(context).inflate(R.layout.index_check_ss,
                null, false);
        LinearLayout linearLayout = mView.findViewById(R.id.contentView);
        View addClass = mView.findViewById(R.id.addClass);
        linearLayout.removeAllViews();

        for (int i = 0; i < indexChosePops.size(); i++) {
            final UserInfoExModel indexChosePop = indexChosePops.get(i);
            View index_check_s = LayoutInflater.from(context).inflate(R.layout.index_check_s,
                    null, false);

            ShapeTextView stut = index_check_s.findViewById(R.id.stut);
            TextView day = index_check_s.findViewById(R.id.day);
            day.setVisibility(View.VISIBLE);
            String statuseName = "";
            int status = indexChosePop.stageStatus;
            if (status == 1) {
                statuseName = "备孕中";
                if (indexChosePop.memberSex != 2) {
                    statuseName = "备孕中";
                }
                day.setVisibility(View.GONE);
            } else if (status == 2) {
                statuseName = "怀孕中";
                day.setText(indexChosePop.stageStatusStr);
            } else if (status == 3) {
                statuseName = indexChosePop.babyName;
                day.setText(indexChosePop.stageStatusStr);
            } else {
                statuseName = "资料越全面，服务更贴心";
                day.setVisibility(View.GONE);
            }
            stut.setText(statuseName);

            index_check_s.findViewById(R.id.edit).setVisibility(indexChosePop.useStatus == 1 ? View.VISIBLE : View.GONE);
            TextColorBuilder textColorBuilder = stut.getTextColorBuilder();
            if (indexChosePop.useStatus == 1) {
                //stut 加粗
                stut.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                textColorBuilder.setTextGradientColors(Color.parseColor("#FF6060"), Color.parseColor("#FF256C")).intoTextColor();
            } else {
                //stut 加粗
                stut.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
                textColorBuilder.setTextColor(Color.parseColor("#333333"));
            }

            index_check_s.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (indexChosePop.useStatus == 1) {
                        dismiss();
                        return;
                    }
                    clickListener.onClick(indexChosePop, view);
                }
            });
            final ImageView icon = index_check_s.findViewById(R.id.icon);
            if (!((Activity) context).isFinishing()) {
                if (status == 1) {
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(indexChosePop.faceUrl)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .placeholder(R.drawable.icon_index_status_new_preparing)
                            .error(R.drawable.icon_index_status_new_preparing)

                            .into(icon);
                }
                if (status == 2) {
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(indexChosePop.faceUrl)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .placeholder(R.drawable.icon_index_status_new_pregnant)
                            .error(R.drawable.icon_index_status_new_pregnant)

                            .into(icon);
                }
                if (status == 3) {
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(indexChosePop.faceUrl)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .placeholder(R.drawable.icon_index_status_new_baby)
                            .error(R.drawable.icon_index_status_new_baby)

                            .into(icon);
                }
            }
            linearLayout.addView(index_check_s);
        }

        if (indexChosePops.size() >= 1) {
            final UserInfoExModel indexChosePop = indexChosePops.get(0);
            int status = indexChosePop.stageStatus;
            String statuseName = "";
            if (status == 1) {
                statuseName = "备孕中";
                if (indexChosePop.memberSex != 2) {
                    statuseName = "备孕中";
                }
            } else if (status == 2) {
                statuseName = "怀孕中";
            } else if (status == 3) {
                statuseName = indexChosePop.babyName;
            } else {
                statuseName = "资料越全面，服务更贴心";
            }
            if ("资料越全面，服务更贴心".equals(statuseName) || indexChosePops.size() == 5) {//说明第一个状态都不对 不能新增
                addClass.setVisibility(View.GONE);
            } else {
                addClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(AppRoutes.APP_CHOOSE_STATUS)
                                .withString("isadd", "1")
                                .withString("sex", indexChosePop.getMemberSex() == 1 ? "男" : "女")
                                .navigation();
                    }
                });
                addClass.setVisibility(View.VISIBLE);
            }
            mView.findViewById(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(indexChosePop, view);
                }
            });
        }

        setContentView(mView);
    }

    public interface OnStatusClickListener {
        void onClick(UserInfoExModel bean, View var1);
    }
}
