package com.health.index.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.health.index.R;
import com.health.index.model.UserInfoExModel;

import java.util.List;

public class ChosePopupWindowOnlyBaby extends PopupWindow {
    public ChosePopupWindowOnlyBaby(final Context context, int width, List<UserInfoExModel> indexChosePops, final OnStatusClickListener clickListener) {
        super(context);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(width);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(R.layout.index_check_ss_old,
                null, false);
        View index_check_sADD = LayoutInflater.from(context).inflate(R.layout.index_check_s_add_old,
                null, false);
        LinearLayout linearLayout = (LinearLayout) contentView;
        linearLayout.removeAllViews();
        //System.out.println("增加状态表1");
        for (int i = 0; i < indexChosePops.size(); i++) {
            final UserInfoExModel indexChosePop = indexChosePops.get(i);
            View index_check_s = LayoutInflater.from(context).inflate(R.layout.index_check_s_old,
                    null, false);

            TextView stut = index_check_s.findViewById(R.id.stut);
            TextView day=index_check_s.findViewById(R.id.day);
            day.setVisibility(View.VISIBLE);
            String statuseName = "";
            int status = indexChosePop.stageStatus;
            if (status == 1) {
                statuseName = "备孕中";
                if(indexChosePop.memberSex!=2){
                    statuseName="备孕中";
                }
                day.setVisibility(View.GONE);
            }
            else if (status == 2) {
                statuseName = "怀孕中";
                day.setText(indexChosePop.stageStatusStr);
            }
            else if (status == 3) {
                statuseName = indexChosePop.babyName;
                day.setText(indexChosePop.stageStatusStr);
            }else {
                statuseName = "资料越全面，服务更贴心";
                day.setVisibility(View.GONE);
            }
            stut.setText(statuseName);
            index_check_s.findViewById(R.id.icon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(indexChosePop, view);
                }
            });
            index_check_s.findViewById(R.id.stut).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(indexChosePop, view);
                }
            });
            index_check_s.findViewById(R.id.day).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(indexChosePop, view);
                }
            });
            index_check_s.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(indexChosePop, view);
                }
            });
            final ImageView icon = index_check_s.findViewById(R.id.icon);
            if(!((Activity)context).isFinishing()){
                if(status==3){
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .asBitmap()
                            .load(indexChosePop.faceUrl)
                            .placeholder(indexChosePop.babySex==1?R.drawable.app_status_boy:R.drawable.app_status_girl)
                            .error(indexChosePop.babySex==1?R.drawable.app_status_boy:R.drawable.app_status_girl)

                            .into(new BitmapImageViewTarget(icon) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    icon.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }else {
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .asBitmap()
                            .load(indexChosePop.faceUrl)
                            .placeholder(indexChosePop.memberSex==1?R.drawable.img_avatar_default_man:R.drawable.img_avatar_default)
                            .error(indexChosePop.memberSex==1?R.drawable.img_avatar_default_man:R.drawable.img_avatar_default)

                            .into(new BitmapImageViewTarget(icon) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    icon.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }
            }


            //System.out.println("增加状态表");
            linearLayout.addView(index_check_s);
        }
//        if(indexChosePops.size()>=1){
//            final UserInfoExModel indexChosePop = indexChosePops.get(0);
//            int status = indexChosePop.stageStatus;
//            String statuseName = "";
//            if (status == 1) {
//                statuseName = "备孕中";
//                if(indexChosePop.memberSex!=2){
//                    statuseName="备孕中";
//                }
//            }
//            else if (status == 2) {
//                statuseName = "怀孕中";
//            }
//            else if (status == 3) {
//                statuseName = indexChosePop.babyName;
//            }else {
//                statuseName = "资料越全面，服务更贴心";
//            }
//            if("资料越全面，服务更贴心".equals(statuseName)||indexChosePops.size()==5){//说明第一个状态都不对 不能新增
//
//            }else {
//                index_check_sADD.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ARouter.getInstance()
//                                .build(AppRoutes.APP_CHOOSE_STATUS)
//                                .withString("isadd","1")
//                                .withString("sex",indexChosePop.getMemberSex()==1?"男":"女")
//                                .navigation();
//                    }
//                });
//                linearLayout.addView(index_check_sADD);
//            }
//        }

        setContentView(contentView);
    }

    public interface OnStatusClickListener {
        void onClick(UserInfoExModel bean, View var1);
    }
}
