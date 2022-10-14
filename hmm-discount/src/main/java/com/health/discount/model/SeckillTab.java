package com.health.discount.model;

import java.util.List;

public class SeckillTab {


    public int history;
    public String marketingId;
    public List<TodayActivityNew> todayActivityNew;

    public static class TodayActivityNew {
        public String id;
        public String marketingName;
        public String beginTimeHi;
        public String beginTime;
        public String endTime;
        public int status;//0未开始  1进行中  2历史活动
        public int currentNew;

        public void setCurrentNew(int currentNew) {
            this.currentNew = currentNew;
        }

        public TodayActivityNew(String id, String marketingName, String beginTimeHi, int status, int currentNew) {
            this.id = id;
            this.marketingName = marketingName;
            this.beginTimeHi = beginTimeHi;
            this.status = status;
            this.currentNew = currentNew;
        }

        public TodayActivityNew() {
        }
    }
}
