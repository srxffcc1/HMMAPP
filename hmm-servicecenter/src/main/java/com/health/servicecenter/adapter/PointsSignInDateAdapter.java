package com.health.servicecenter.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.health.servicecenter.R;
import com.health.servicecenter.model.PointsSignInModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.utils.DateUtils;

import java.util.Calendar;

/**
 * item 每日签到
 *
 * @author: long
 * @date: 2021/4/15
 * @des
 */
public class PointsSignInDateAdapter extends BaseAdapter<PointsSignInModel.MemberSignRecords/*DayBean*/> {
    //当天剩余总签到次数
    private int mSignNum;
    private final int mWeek;

    public PointsSignInDateAdapter() {
        super(R.layout.item_points_signin_date_layout);
        Calendar mCalendar = Calendar.getInstance();
        mWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(7);
        gridLayoutHelper.setMarginLeft(14);
        //gridLayoutHelper.setMarginBottom(25);
        gridLayoutHelper.setBgColor(Color.parseColor("#ffffff"));
        gridLayoutHelper.setAutoExpand(false);
        return gridLayoutHelper;
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseHolder holder, final int position) {
        final PointsSignInModel.MemberSignRecords memberSignRecords = getDatas().get(position);
        holder.setText(R.id.item_signIn_date_time, DateUtils.getWeek(String.valueOf(memberSignRecords.signWeek)));
        holder.setText(R.id.item_signIn_date_number, "+" + memberSignRecords.signIntegral);
        ImageView itemDateImg = holder.getView(R.id.item_signIn_date_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    //日期相对应  执行签到
                    if (mWeek == memberSignRecords.signWeek) {
                        //当天剩余签到次数大于0
                        if (mSignNum > 0) {
                            ////System.out.println("签到点击了");
                            moutClickListener.outClick("签到", memberSignRecords);
                            holder.itemView.setEnabled(true);
                        } else {
                            moutClickListener.outClick("今天已签到!", memberSignRecords);
                        }
                        return;
                    }

                    if (mWeek < memberSignRecords.signWeek) {
                        moutClickListener.outClick("还未到签到时间!", memberSignRecords);
                        return;
                    }
                    if (mWeek > memberSignRecords.signWeek) {
                        moutClickListener.outClick("签到时间已过!", memberSignRecords);
                        return;
                    }

                }
            }
        });

        //当前选中 2021/04/24 优化签到显示样式
        if (!TextUtils.isEmpty(memberSignRecords.id)) {
            itemDateImg.setImageResource(R.drawable.signin_checked);
            holder.setTextColor(R.id.item_signIn_date_number, context.getResources().getColor(R.color.colorWhite));
            /*if (memberSignRecords.signWeek < mWeek) {
                holder.setTextColor(R.id.item_signIn_date_number, context.getResources().getColor(R.color.color_d8d8d8));
            }*/
        } else {
            itemDateImg.setImageResource(R.drawable.signin_unchecked);
            holder.setTextColor(R.id.item_signIn_date_number, context.getResources().getColor(R.color.color_f7914b));
            if (memberSignRecords.signWeek < mWeek) {
                holder.setTextColor(R.id.item_signIn_date_number, context.getResources().getColor(R.color.color_d8d8d8));
            }
        }
    }

    public void setSignNum(int signNum) {
        this.mSignNum = signNum;
    }
}