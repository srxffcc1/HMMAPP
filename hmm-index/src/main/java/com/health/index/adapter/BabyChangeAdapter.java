package com.health.index.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.health.index.model.ChangeModel;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.widget.DotView;

import java.util.Date;

/**
 * @author Li
 * @date 2019/04/28 11:35
 * @des 宝宝变化
 */
public class BabyChangeAdapter extends BaseQuickAdapter<ChangeModel, BaseViewHolder> {
    private String mDateString;

    public BabyChangeAdapter() {
        this(R.layout.index_item_baby_change);
        mDateString = DateUtils.formatTime2String("yyyy-MM-dd", new Date());

    }

    private BabyChangeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChangeModel item) {
        helper.setText(R.id.tv_baby_content, item.getBabyChange());
        String babyUrl = item.getBabyUrl();
        ImageView ivBaby = helper.getView(R.id.iv_baby);
        DotView dotView = helper.getView(R.id.dot_week);
        if (TextUtils.isEmpty(babyUrl)) {
            ivBaby.setVisibility(View.GONE);
        } else {
            ivBaby.setVisibility(View.VISIBLE);
            com.healthy.library.businessutil.GlideCopy.with(mContext).load(item.getBabyUrl()).into(ivBaby);
        }
        TextView tvWeek = helper.getView(R.id.tv_week);
        String weekDes = item.getBabyWeek();
        String babyDay = item.getBabyDay();
        if (mDateString.equals(item.getShowDate())) {
            tvWeek.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            tvWeek.setTextColor(Color.WHITE);
            dotView.setDotColor(Color.parseColor("#FF8181"),
                    Color.parseColor("#FF6177"));
        } else {
            tvWeek.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            tvWeek.setTextColor(Color.parseColor("#222222"));
            dotView.setDotColor(Color.parseColor("#EEEEF5"));
        }
        if (TextUtils.isEmpty(babyDay)) {
            if (weekDes.contains("周")) {
                int index = weekDes.indexOf("周");
                if (index == weekDes.length() - 1) {
                    tvWeek.setText(weekDes);
                } else {
                    String prefix = weekDes.substring(0, index + 1);
                    String suffix = weekDes.substring(index + 1);
                    tvWeek.setText(String.format("%s\n+%s", prefix, suffix));
                }
            } else {
                tvWeek.setText(weekDes);
            }
        } else {
            if (babyDay.contains("月")) {
                int index = babyDay.indexOf("月");
                if (index == babyDay.length() - 1) {
                    tvWeek.setText(String.format("宝宝\n%s", babyDay.substring(0, index + 1)));
                } else {
                    String prefix = babyDay.substring(0, index + 1);
                    String suffix = babyDay.substring(index + 1);
                    tvWeek.setText(String.format("宝宝\n%s\n%s", prefix, suffix));
                }
            } else {
                tvWeek.setText(String.format("宝宝\n%s", babyDay));
            }
        }
    }
}