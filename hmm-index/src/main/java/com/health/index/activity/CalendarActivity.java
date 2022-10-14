package com.health.index.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.health.index.R;
import com.health.index.adapter.CalendarAdapter;
import com.health.index.contract.CalendarContract;
import com.health.index.model.CalendarModel;
import com.health.index.model.CalendarTypeModel;
import com.health.index.presenter.CalendarPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.dialog.ChooseCalendarDialog;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.widget.StatusLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Li
 * @date 2019/03/22 15:31
 * @des 经期日历
 */
@Route(path = IndexRoutes.INDEX_CALENDAR)
public class CalendarActivity extends BaseActivity implements ChooseCalendarDialog.OnConfirmClickListener,
        CalendarContract.View {


    private RecyclerView mRecyclerCalendar;
    private ChooseCalendarDialog mCalendarDialog;
    private TextView mTvDate;
    private CalendarAdapter mCalendarAdapter;
    private CalendarPresenter mPresenter;
    private StatusLayout mStatusLayout;
    private Date mSelectedDate;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_calendar;
    }

    @Override
    protected void findViews() {
        mRecyclerCalendar = findViewById(R.id.recycler_calendar);
        mTvDate = findViewById(R.id.tv_date);
        mStatusLayout = findViewById(R.id.layout_status);
    }


    /**
     * 显示时间选择
     *
     * @param view 按钮
     */
    public void showDate(View view) {

        if (mCalendarDialog == null) {
            mCalendarDialog = ChooseCalendarDialog.newInstance(new Date());
            mCalendarDialog.setConfirmClickListener(this);
        }
        Fragment f = getSupportFragmentManager().findFragmentByTag("calendar");
        if(f != null){
            getSupportFragmentManager().beginTransaction().remove(f).commitNowAllowingStateLoss();
        }
        mCalendarDialog.show(getSupportFragmentManager(), "calendar");

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setStatusLayout(mStatusLayout);
        GridLayoutManager manager = new GridLayoutManager(this, 7);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {

                int type = mCalendarAdapter.getDataList().get(i).getType();
                if (type == CalendarAdapter.TYPE_CALENDAR) {
                    return 1;
                } else {
                    return 7;
                }
            }
        });
        mRecyclerCalendar.setLayoutManager(manager);

        mCalendarAdapter = new CalendarAdapter(this);
        mRecyclerCalendar.setAdapter(mCalendarAdapter);

        Calendar calendar = Calendar.getInstance();
        mSelectedDate = calendar.getTime();

        setTvDate(mSelectedDate);
        mPresenter = new CalendarPresenter(this, this);
        mPresenter.getCalendarByDate(mSelectedDate);
    }


    /**
     * 设置头部日期显示
     *
     * @param date 时间
     */
    private void setTvDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String content = String.format(Locale.CHINA, "%s年%02d月", year, month);
        SpannableString spannableString = new SpannableString(content);
        StyleSpan spanYear = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(spanYear, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        StyleSpan spanMonth = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(spanMonth, 5, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mTvDate.setText(spannableString);
    }


    public void finish(View view) {
        finish();
    }

    @Override
    public void onGetCalendarSuccess(List<CalendarTypeModel> dataList, CalendarModel preModel,
                                     CalendarModel curModel, CalendarModel actualModel,
                                     Date date) {
        mCalendarAdapter.setData(dataList, preModel, curModel, actualModel, null);
    }

    @Override
    public void onConfirmClick(Date date) {
        mSelectedDate = date;
        setTvDate(date);
        mPresenter.getCalendarByDate(date);
    }

    @Override
    public void onRetryClick() {
        super.onRetryClick();
        mPresenter.getCalendarByDate(mSelectedDate);
    }
}
