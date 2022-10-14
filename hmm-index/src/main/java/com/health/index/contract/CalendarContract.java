package com.health.index.contract;

import com.health.index.model.CalendarModel;
import com.health.index.model.CalendarTypeModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Date;
import java.util.List;

/**
 * @author Li
 * @date 2019/05/08 14:23
 * @des 经期日历
 */
public interface CalendarContract {
    interface View extends BaseView {

        /**
         * 获取经期周期成功
         *
         */
        void onGetCalendarSuccess(List<CalendarTypeModel> dataList, CalendarModel preModel, CalendarModel curModel,
                                  CalendarModel actualModel, Date date);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取日历
         *
         * @param date 时间
         */
        void getCalendarByDate(Date date);
    }
}
