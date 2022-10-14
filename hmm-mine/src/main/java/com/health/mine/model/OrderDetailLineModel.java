package com.health.mine.model;

import android.text.SpannableStringBuilder;

/**
 * @author Li
 * @date 2019/04/10 09:31
 * @des 订单详情
 */

public class OrderDetailLineModel {
        public int dotbackGroudRes;
        public String title;
        public SpannableStringBuilder spannableStringBuilder;
        public String time;
        public boolean isfinal;

//        public OrderDetailLineModel() {
//        }

        public OrderDetailLineModel(int dotbackGroudRes, String title, SpannableStringBuilder spannableStringBuilder, String time, boolean isfinal) {
                this.dotbackGroudRes = dotbackGroudRes;
                this.title = title;
                this.spannableStringBuilder = spannableStringBuilder;
                this.time = time;
                this.isfinal = isfinal;
        }
}
