package com.health.index.model;

import android.text.TextUtils;

public class IndexBabyAndMomChange {

        public String gestationDay;
        public String babyContent;
        public String babyImage;
        public String momImage;
        public String momContent;

        public String getWeight() {
                if(TextUtils.isEmpty(weight)){
                        return "0";
                }
                return weight;
        }

        public String weight;//胎重

        public String getCrownRumpLength() {
                if(TextUtils.isEmpty(crownRumpLength)){
                        return "0";
                }
                return crownRumpLength;
        }

        public String crownRumpLength;//身长
}
