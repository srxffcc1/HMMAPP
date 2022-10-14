package com.health.tencentlive.model;

import java.util.List;

public class LineChartModel {

    public List<StatisticsListBean> statisticsList;

    public class StatisticsListBean {
        public String name;
        public int maxCount;
        public List<MonthDataListBean> monthDataList;

        public class MonthDataListBean {
            public String monthName;
            public int count;
        }
    }
}
