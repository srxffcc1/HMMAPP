package com.healthy.library.model;

public class ToolsBabyModel {

    public 宝宝体重测量指标 宝宝体重测量指标;

    public String 宝宝发育状态评估;

    public 宝宝体重实体标准 宝宝体重实体标准;

    public 宝宝产检实体信息 宝宝产检实体信息;


    public class 宝宝体重测量指标 {
        public String 双顶径厘米;

        public String 腹围厘米;

        public String 股骨长厘米;

        public String 宝宝体重公斤;

        public String 宝宝体重市斤;

    }

    public class 宝宝体重标准范围实体 {
        public String 双顶径厘米;

        public String 腹围厘米;

        public String 股骨长厘米;

        public String 宝宝体重公斤;

        public String 宝宝体重市斤;


    }

    public class 宝宝体重最大范围实体 {
        public String 双顶径厘米;

        public String 腹围厘米;

        public String 股骨长厘米;

        public String 宝宝体重公斤;

        public String 宝宝体重市斤;

    }

    public class 宝宝体重最小范围实体 {
        public String 双顶径厘米;

        public String 腹围厘米;

        public String 股骨长厘米;

        public String 宝宝体重公斤;

        public String 宝宝体重市斤;


    }

    public class 宝宝体重实体标准 {
        public 宝宝体重标准范围实体 宝宝体重标准范围实体;

        public 宝宝体重最大范围实体 宝宝体重最大范围实体;

        public 宝宝体重最小范围实体 宝宝体重最小范围实体;

    }

    public class 宝宝产检实体信息 {
        public String 产检状态;

        public String 产检次数;

        public String 产检内容;

    }
}
