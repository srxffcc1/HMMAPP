package com.health.index.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.index.R;
import com.health.index.model.CalendarModel;
import com.health.index.model.CalendarSupModel;
import com.health.index.model.CalendarTypeModel;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.widget.DotView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/22 15:44
 * @des 经期日历
 */

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private CalendarModel mPreModel;
    private CalendarModel mCurModel;
    private CalendarModel mActualModel;
    private List<CalendarTypeModel> mDataList;
    private LayoutInflater mInflater;
    private Calendar mCalendar;
    private Calendar mTodayCalendar;
    private Date mSelectedDate;
    private int mSelectedColor = Color.parseColor("#FF6266");
    private int mUnSeclectedColor = Color.parseColor("#39E0C8");
    private int mWhiteColor = Color.WHITE;
    /**
     * 头部日历
     */
    public static final int TYPE_CALENDAR = 1;

    /**
     * 底部内容
     */
    public static final int TYPE_DETAIL = 2;

    public CalendarAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mCalendar = Calendar.getInstance();
        mTodayCalendar = Calendar.getInstance();
    }

    public void setData(List<CalendarTypeModel> dataList, CalendarModel preModel, CalendarModel curModel,
                        CalendarModel actualModel, Date selectedDate) {
        mDataList = dataList;
        mPreModel = preModel;
        mCurModel = curModel;
        mActualModel = actualModel;
        mSelectedDate = selectedDate;
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_CALENDAR) {
            View view = mInflater.inflate(R.layout.item_calendar, viewGroup, false);
            return new CalendarHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.index_calendar_bottom, viewGroup, false);
            return new CalendarBottomHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = mDataList.get(position).getType();
        CalendarTypeModel typeModel = mDataList.get(position);
        if (type == TYPE_CALENDAR) {
            Date date = typeModel.getDate();
            CalendarHolder calendarHolder = (CalendarHolder) holder;
            if (date != null) {
                if (mSelectedDate != null) {
                    calendarHolder.itemView.setSelected(DateUtils.isSameDate(date, mSelectedDate));
                } else {
                    calendarHolder.itemView.setSelected(false);
                }
                calendarHolder.itemView.setVisibility(View.VISIBLE);
                mCalendar.setTime(date);
                int day = mCalendar.get(Calendar.DAY_OF_MONTH);
                if (DateUtils.isSameDate(date, mTodayCalendar.getTime())) {
                    calendarHolder.mTvCalendar.setText("今日");
                    calendarHolder.mDotView.setVisibility(View.VISIBLE);
                } else {
                    calendarHolder.mTvCalendar.setText(String.valueOf(day));
                    calendarHolder.mDotView.setVisibility(View.INVISIBLE);
                }
                renderUI(mPreModel, calendarHolder, date);
                renderUI(mCurModel, calendarHolder, date);
                renderUI(mActualModel, calendarHolder, date);


            } else {
                holder.itemView.setVisibility(View.INVISIBLE);
            }
        } else {
            CalendarSupModel supModel = typeModel.getSupModel();
            CalendarBottomHolder bottomHolder = (CalendarBottomHolder) holder;
            if (supModel.isEmpty()) {
                bottomHolder.itemView.setVisibility(View.GONE);
            } else {
                bottomHolder.itemView.setVisibility(View.VISIBLE);
                renderBottomUI(supModel, bottomHolder);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public List<CalendarTypeModel> getDataList() {
        if (mDataList == null) {
            return new ArrayList<>();
        }
        return mDataList;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getType();
    }

    static class CalendarHolder extends RecyclerView.ViewHolder {

        ImageView mIvBig;
        ImageView mIvSmall;
        TextView mTvCalendar;
        DotView mDotView;
        ImageView mIvMen;

        CalendarHolder(@NonNull View itemView) {
            super(itemView);
            mIvBig = itemView.findViewById(R.id.iv_big);
            mIvSmall = itemView.findViewById(R.id.iv_small);
            mTvCalendar = itemView.findViewById(R.id.tv_calendar);
            mDotView = itemView.findViewById(R.id.dot);
            mIvMen = itemView.findViewById(R.id.iv_men);
        }
    }

    static class CalendarBottomHolder extends RecyclerView.ViewHolder {
        TextView tvProbability;
        TextView tvDes;

        CalendarBottomHolder(@NonNull View itemView) {
            super(itemView);
            tvProbability = itemView.findViewById(R.id.tv_pregnancy_probability);
            tvDes = itemView.findViewById(R.id.tv_des);
        }
    }


    private void renderBottomUI(CalendarSupModel supModel, CalendarBottomHolder holder) {

        String content = String.format("怀孕几率：%s", supModel.getPregnancyProb()+"%");
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(styleSpan, 5, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.tvProbability.setText(spannableString);

        holder.tvDes.setText(supModel.getContent());
    }

    private void renderUI(CalendarModel model, CalendarHolder holder, Date date) {
        if (model.isEmpty()) {
            return;
        }
        if (DateUtils.isBetween(date, model.getOvulationStartDate(),
                model.getOvulationEndDate())) {
            holder.mTvCalendar.setTextColor(mSelectedColor);
            holder.mDotView.setDotColor(mSelectedColor);
            holder.mIvBig.setVisibility(View.VISIBLE);
            holder.mIvMen.setVisibility(View.GONE);
        } else if (DateUtils.isBetween(date, model.getMenstruationStartDate(),
                model.getMenstruationEndDate())) {
            holder.mTvCalendar.setTextColor(mWhiteColor);
            holder.mIvBig.setVisibility(View.GONE);
            holder.mIvMen.setVisibility(View.VISIBLE);
            holder.mIvMen.setImageResource(R.drawable.index_ic_men_expected);
        } else {
            holder.mIvBig.setVisibility(View.INVISIBLE);
            holder.mIvMen.setVisibility(View.GONE);
            holder.mTvCalendar.setTextColor(Color.parseColor("#39E0C8"));
            holder.mDotView.setDotColor(Color.parseColor("#39E0C8"));
        }
        if (DateUtils.isSameDate(date, model.getOvulationDate())) {
            holder.mIvSmall.setVisibility(View.VISIBLE);
        } else {
            holder.mIvSmall.setVisibility(View.INVISIBLE);
        }

    }
}