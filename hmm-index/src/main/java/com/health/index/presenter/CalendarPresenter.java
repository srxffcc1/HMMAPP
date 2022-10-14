package com.health.index.presenter;

import androidx.appcompat.app.AppCompatActivity;

import com.health.index.adapter.CalendarAdapter;
import com.health.index.contract.CalendarContract;
import com.health.index.model.CalendarModel;
import com.health.index.model.CalendarSupModel;
import com.health.index.model.CalendarTypeModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.JsonUtils;
import com.healthy.library.utils.TimestampUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/05/08 14:25
 * @des
 */
public class CalendarPresenter implements CalendarContract.Presenter {
    private AppCompatActivity mActivity;
    private CalendarContract.View mView;

    public CalendarPresenter(AppCompatActivity activity, CalendarContract.View view) {
        mActivity = activity;
        mView = view;
    }

    @Override
    public void getCalendarByDate(final Date date) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_CALENDAR);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String time = TimestampUtils.timestamp2String(calendar.getTimeInMillis(), "yyyy-MM-dd");
        map.put("queryDate", time);
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        resolveData(date, obj);
                    }
                });
    }

    private void resolveData(Date date, String obj) {
        Calendar calendar = Calendar.getInstance();
        List<CalendarTypeModel> dataList = new ArrayList<>();
        calendar.setTime(date);
        int dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int offsetDay = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < offsetDay - 1; i++) {
            dataList.add(new CalendarTypeModel(CalendarAdapter.TYPE_CALENDAR));
        }

        for (int i = 1; i <= dayCount; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            CalendarTypeModel model = new CalendarTypeModel(CalendarAdapter.TYPE_CALENDAR);
            model.setDate(calendar.getTime());
            dataList.add(model);
        }

        try {
            String pattern = "yyyy-MM-dd";
            JSONObject jsonObject = new JSONObject(obj);
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            CalendarModel preModel = new CalendarModel();
            CalendarModel curModel = new CalendarModel();
            CalendarModel actualModel = new CalendarModel();
            CalendarSupModel supModel = new CalendarSupModel();

            JSONObject preObject = JsonUtils.getJsonObject(jsonObject, "beforeCycleList");
            JSONObject curObject = JsonUtils.getJsonObject(jsonObject, "nowCycleList");
            JSONObject supObject = JsonUtils.getJsonObject(jsonObject, "menstrualContent");
            if (!JsonUtils.checkJsonObjectEmpty(jsonObject, "beforeCycleList")) {
                preModel.setMenstruationStartDate(DateUtils.formatString2Time(pattern,
                        JsonUtils.getString(preObject, "latelyDate")));
                preModel.setMenstruationEndDate(DateUtils.formatString2Time(pattern,
                        JsonUtils.getString(preObject, "endDate")));
                preModel.setOvulationDate(DateUtils.formatString2Time(pattern,
                        JsonUtils.getString(preObject, "ovulatoryDate")));
                preModel.setOvulationStartDate(DateUtils.formatString2Time(pattern,
                        JsonUtils.getString(preObject, "ovulatoryBeginDate")));
                preModel.setOvulationEndDate(DateUtils.formatString2Time(pattern,
                        JsonUtils.getString(preObject, "ovulatoryEndDate")));
            }

            if (!JsonUtils.checkJsonObjectEmpty(jsonObject, "nowCycleList")) {
                curModel.setMenstruationStartDate(DateUtils.formatString2Time(pattern,
                        JsonUtils.getString(curObject, "latelyDate")));
                curModel.setMenstruationEndDate(DateUtils.formatString2Time(pattern,
                        JsonUtils.getString(curObject, "endDate")));
                curModel.setOvulationDate(DateUtils.formatString2Time(pattern,
                        JsonUtils.getString(curObject, "ovulatoryDate")));
                curModel.setOvulationStartDate(DateUtils.formatString2Time(pattern,
                        JsonUtils.getString(curObject, "ovulatoryBeginDate")));
                curModel.setOvulationEndDate(DateUtils.formatString2Time(pattern,
                        JsonUtils.getString(curObject, "ovulatoryEndDate")));
            }
            if (!JsonUtils.checkJsonObjectEmpty(jsonObject, "menstrualContent")) {
                supModel.setContent(JsonUtils.getString(supObject, "content"));
                supModel.setPregnancyProb(JsonUtils.getString(supObject, "pregnancyProbability"));
            }


            CalendarTypeModel typeModel = new CalendarTypeModel(CalendarAdapter.TYPE_DETAIL);
            typeModel.setSupModel(supModel);
            dataList.add(typeModel);
            mView.onGetCalendarSuccess(dataList, preModel, curModel, actualModel, date);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
